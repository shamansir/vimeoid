package org.vimeoid.activity.user;

import org.json.JSONArray;
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
    private final String arrayKey;
    private ApiParams params;    

    public ItemsListActivity(int mainView, int contextMenu, String apiMethod, String arrayKey) {
    	super(mainView, contextMenu);
    	this.apiMethod = apiMethod;
    	this.arrayKey = arrayKey;
    }
    
    public ItemsListActivity(int contextMenu, String apiMethod, String arrayKey) {
    	this(R.layout.generic_list, contextMenu, apiMethod, arrayKey);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        params = prepareMethodParams(apiMethod, arrayKey, getIntent().getExtras());
        
        super.onCreate(savedInstanceState);
    }
    
    protected abstract ApiParams prepareMethodParams(String methodName, String arrayKey, Bundle extras);    
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        subjectIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.SUBJ_ICON, R.drawable.info));
        subjectTitle.setText(getIntent().hasExtra(Invoke.Extras.SUBJ_TITLE) 
                             ? getIntent().getStringExtra(Invoke.Extras.SUBJ_TITLE) 
                             : getString(R.string.unknown));
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.RES_ICON, R.drawable.info));
    }
    
    @Override
    protected final void queryMoreItems(JsonObjectsAdapter adapter, int pageNum) {
        // FIXME: use pageNum
        new LoadUserItemsTask(adapter, apiMethod, arrayKey).execute(params);
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
        private final String arrayKey;
        
        protected LoadUserItemsTask(JsonObjectsAdapter adapter, String apiMethod, String keyParam) {
            this.adapter = adapter;
            this.apiMethod = apiMethod;
            this.arrayKey = keyParam;
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
                    return VimeoApi.advancedApi(apiMethod, params.getValue());
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
        	    JSONArray values;
                try {
                    values = object.getJSONArray(arrayKey);
                    adapter.addSource(values);
                    onItemsReceived(values.length());                    
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
