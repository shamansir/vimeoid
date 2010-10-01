package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.base.ItemsListActivity_;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.VimeoApi.AdvancedApiCallError;
import org.vimeoid.dto.advanced.PagingData;
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
                      ItemsListActivity_<ItemType, JsonObjectsAdapter<ItemType>> {
	
    public static final String TAG = "ItemsListActivity";
    
    private String apiMethod;
    private ApiParams params;
    
    protected final ApiTasksQueue secondaryTasks;

    public ItemsListActivity(int mainView, int contextMenu) {
    	super(mainView, contextMenu);
    	
    	setMaxPagesCount(8);
    	setItemsPerPage(20);
    	
    	secondaryTasks = new ApiTasksQueue() {            
            @Override public void onPerfomed(int taskId, JSONObject result) throws JSONException {
                onSecondaryTaskPerfomed(taskId, result);
            }
        };
    }
    
    public ItemsListActivity(int contextMenu) {
    	this(R.layout.generic_list, contextMenu);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        apiMethod = getIntent().getStringExtra(Invoke.Extras.API_METHOD);
        params = ApiParams.fromBundle(getIntent().getBundleExtra(Invoke.Extras.API_PARAMS));        
        
        super.onCreate(savedInstanceState);
    }
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        subjectIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.SUBJ_ICON, R.drawable.info));
        subjectTitle.setText(getIntent().hasExtra(Invoke.Extras.SUBJ_TITLE) 
                             ? getIntent().getStringExtra(Invoke.Extras.SUBJ_TITLE) 
                             : getString(R.string.unknown));
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.RES_ICON, R.drawable.info));
    }
    
    @Override
    protected final void queryMoreItems(JsonObjectsAdapter<ItemType> adapter, int pageNum) {
        params.add("page", String.valueOf(pageNum));
        params.add("per_page", String.valueOf(getItemsPerPage()));
        // TODO: params.add("sort", "");
        new LoadUserItemsTask(adapter, apiMethod).execute(params);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.user_options_menu, menu); 
        
        return true;
    }
   
    public void onSecondaryTaskPerfomed(int id, JSONObject result)  throws JSONException { }  
   
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

        private final JsonObjectsAdapter<ItemType> adapter;
        private final String apiMethod;
        
        protected LoadUserItemsTask(JsonObjectsAdapter<ItemType> adapter, String apiMethod) {
            this.adapter = adapter;
            this.apiMethod = apiMethod;
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
                    adapter.addPage(object);
                    PagingData pd = adapter.getLastPagingData();
                    if (pd.pageNum != getCurrentPage()) throw new IllegalStateException("Received page number do not matches actual");
                    if (pd.perPage != getItemsPerPage()) throw new IllegalStateException("Received number of items per page do not matches actual");                    
                    onItemsReceived(pd.onThisPage, pd.total);
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
