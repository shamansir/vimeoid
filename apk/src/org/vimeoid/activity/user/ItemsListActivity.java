package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.base.ItemsListActivity_;
import org.vimeoid.adapter.user.JsonObjectsAdapter;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.VimeoApi.AdvancedApiCallError;
import org.vimeoid.util.AdvancedItem;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ItemsListActivity<ItemType extends AdvancedItem> extends 
                      ItemsListActivity_<ItemType, JsonObjectsAdapter> {
	
    public static final String TAG = "ItemsListActivity";
    
    private final String apiMethod;
    private final String dataKey;
    private final String arrayKey;
    private ApiParams params;    

    public ItemsListActivity(int mainView, int contextMenu, String apiMethod, String dataKey, String arrayKey) {
    	super(mainView, contextMenu);
    	this.apiMethod = apiMethod;
    	this.dataKey = dataKey;
    	this.arrayKey = arrayKey;
    	
    	setMaxPagesCount(8);
    	setItemsPerPage(20);
    }
    
    public ItemsListActivity(int contextMenu, String apiMethod, String dataKey, String arrayKey) {
    	this(R.layout.generic_list, contextMenu, apiMethod, dataKey, arrayKey);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        params = prepareMethodParams(apiMethod, getIntent().getExtras());
        
        super.onCreate(savedInstanceState);
    }
    
    protected abstract ApiParams prepareMethodParams(String methodName, Bundle extras);    
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        subjectIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.SUBJ_ICON, R.drawable.info));
        subjectTitle.setText(getIntent().hasExtra(Invoke.Extras.SUBJ_TITLE) 
                             ? getIntent().getStringExtra(Invoke.Extras.SUBJ_TITLE) 
                             : getString(R.string.unknown));
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.RES_ICON, R.drawable.info));
    }
    
    @Override
    protected final void queryMoreItems(JsonObjectsAdapter adapter, int pageNum) {
        params.add("page", String.valueOf(pageNum));
        params.add("per_page", String.valueOf(getItemsPerPage()));
        // TODO: params.add("sort", "");
        new LoadUserItemsTask(adapter, apiMethod, dataKey, arrayKey).execute(params);
    }
    
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.user_options_menu, menu); 
        
        return true;
    }
   
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case R.id.menu_Refresh: {
                    Dialogs.makeToast(this, getString(R.string.currently_not_supported)); 
                } break;
            case R.id.menu_Preferences: {
                    Dialogs.makeToast(this, getString(R.string.currently_not_supported)); 
                } break;
            case R.id.menu_SwitchView: {
                    Dialogs.makeToast(this, getString(R.string.currently_not_supported)); 
                } break;
            default: Dialogs.makeToast(this, getString(R.string.unknown_item));
        }         
        return super.onOptionsItemSelected(item);
        
    }
    
    protected final class LoadUserItemsTask extends LoadItemsTask<ApiParams, Void, JSONObject> {

        private final JsonObjectsAdapter adapter;
        private final String apiMethod;
        private final String dataKey;
        private final String arrayKey;
        
        protected LoadUserItemsTask(JsonObjectsAdapter adapter, String apiMethod, String dataKey, String arrayKey) {
            this.adapter = adapter;
            this.apiMethod = apiMethod;
            this.dataKey = dataKey;
            this.arrayKey = arrayKey;
        }
        
        @Override
        protected JSONObject doInBackground(ApiParams... paramsLists) {
            if (paramsLists.length <= 0) return null;
            if (paramsLists.length > 1) throw new UnsupportedOperationException("This task do not supports several params lists");
            
            try {
                final ApiParams params = paramsLists[0];
                if (params == null || params.isEmpty()) {
                    return VimeoApi.advancedApi(apiMethod);
                } else {
                    return VimeoApi.advancedApi(apiMethod, params);
                }
            } catch(AdvancedApiCallError aace) {
                VimeoApi.handleApiError(ItemsListActivity.this, aace);
            } catch(Exception e) {
                Log.e(TAG, "Error while calling " + apiMethod + ": " + e.getLocalizedMessage());
                e.printStackTrace();
                Dialogs.makeExceptionToast(ItemsListActivity.this, "Error while calling " + apiMethod, e);
            }
            
            return null;
        }
        
        @Override
        protected void onPostExecute(JSONObject object) {
        	if (object == null) {
        	    Log.e(TAG, "Failed to receive next page");
        		rollback();
        	} else {
                try {
                    final JSONObject dataObj = object.getJSONObject(dataKey); 
                    final int perPage = dataObj.getInt("perpage");
                    final int onThisPage = dataObj.getInt("on_this_page");
                    final int total = dataObj.getInt("total");
                    final int pageNum = dataObj.getInt("page");
                    if (pageNum != getCurrentPage()) throw new IllegalStateException("Received page number dp not matches actual");
                    if (perPage != getCurrentPage()) throw new IllegalStateException("Received number of items per page do not matches actual");                    
                    adapter.addSource(dataObj.getJSONArray(arrayKey));
                    onItemsReceived(onThisPage, total);                    
                } catch (JSONException jsone) {
                    rollback();
                    Log.d(TAG, "JSON parsing failure: " + jsone.getLocalizedMessage());
                    jsone.printStackTrace();
                    Dialogs.makeExceptionToast(ItemsListActivity.this, "JSON parsing failure", jsone);                    
                }
            }
        	
            super.onPostExecute(object);
        }
        
    }
    
}
