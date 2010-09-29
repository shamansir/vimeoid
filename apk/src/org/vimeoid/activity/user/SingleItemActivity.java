/**
 * 
 */
package org.vimeoid.activity.user;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.base.SingleItemActivity_;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.VimeoApi.AdvancedApiCallError;
import org.vimeoid.util.AdvancedItem;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>SingleItemActivity</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 26, 2010 2:35:40 PM 
 *
 */
public abstract class SingleItemActivity<ItemType extends AdvancedItem> extends SingleItemActivity_<ItemType> 
    implements SecondaryTasksSupport {
    
    private static final String TAG = "SingleItemActivity";
    
    private String apiMethod;
    private ApiParams params;
    
    private LinkedList<SecondaryTask> secondaryTasks = null;
    private Map<Integer, ApiParams> secondaryTasksParams = null;
    
    public SingleItemActivity(int mainView) {
        super(mainView);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        apiMethod = getIntent().getStringExtra(Invoke.Extras.API_METHOD);
        params = ApiParams.fromBundle(getIntent().getBundleExtra(Invoke.Extras.API_PARAMS));
        super.onCreate(savedInstanceState);
    }
    
    protected abstract ItemType extractFromJson(JSONObject jsonObj) throws JSONException;
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        subjectIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.SUBJ_ICON, R.drawable.info));
        subjectTitle.setText(getIntent().hasExtra(Invoke.Extras.SUBJ_TITLE) 
                             ? getIntent().getStringExtra(Invoke.Extras.SUBJ_TITLE) 
                             : getString(R.string.unknown));
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.RES_ICON, R.drawable.info));
    }
    
    @Override 
    protected final void queryItem() {
        try {
            new LoadUserItemTask(apiMethod).execute(params).get();
        } catch (Exception e) {
            Log.e(TAG, "failed to get item");
            e.printStackTrace();
            Dialogs.makeExceptionToast(this, "Failed to get item", e);
        }    
        runSecondaryTasks();
    }
    
    @Override
    public final void addSecondaryTask(int taskId, String apiMethod, ApiParams params) {
        if (secondaryTasks == null) secondaryTasks = new LinkedList<SecondaryTask>();
        if (secondaryTasksParams == null) secondaryTasksParams = new HashMap<Integer, ApiParams>();
        final SecondaryTask newTask = new SecondaryTask(taskId, apiMethod);
        try {
            SecondaryTask last = secondaryTasks.getLast();
            last.setNextTask(newTask);
        } catch (NoSuchElementException nsee) { };
        secondaryTasks.add(newTask);
        secondaryTasksParams.put(taskId, params);
    }
    
    @Override
    public void onSecondaryTaskPerfomed(int id, JSONObject result)  throws JSONException { }
    
    @Override
    protected void onItemReceived(ItemType item) {
        super.onItemReceived(item);
    }
    
    @Override
    public final void runSecondaryTasks() {
        if (secondaryTasks != null) {
            SecondaryTask first = secondaryTasks.getFirst();
            first.execute(secondaryTasksParams.get(first.getId())); // will automatically call runNextSecondaryTask
        }
    }
    
    private void runNextSecondaryTask(SecondaryTask task) {
        task.execute(secondaryTasksParams.get(task.getId()));
    }
    
    protected class LoadUserItemTask extends AsyncTask<ApiParams, Void, JSONObject> {

    	protected final String apiMethod;
        
        protected LoadUserItemTask(String apiMethod) {
            this.apiMethod = apiMethod;
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            showProgressBar();            
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
                VimeoApi.handleApiError(SingleItemActivity.this, aace);
            } catch(Exception e) {
                Log.e(TAG, "Error while calling " + apiMethod + ": " + e.getLocalizedMessage());
                e.printStackTrace();
                Dialogs.makeExceptionToast(SingleItemActivity.this, "Error while calling " + apiMethod, e);
            }
            
            return null;
        }
        
        @Override
        protected void onPostExecute(JSONObject jsonObj) {
            // Log.d(TAG, jsonObj.toString());
            if (jsonObj != null) {
                try {
                    onItemReceived(extractFromJson(jsonObj));
                } catch (JSONException jsone) {
                    Log.d(TAG, "JSON parsing failure: " + jsone.getLocalizedMessage());
                    jsone.printStackTrace();
                    Dialogs.makeExceptionToast(SingleItemActivity.this, "JSON parsing failure", jsone);
                }
            }
            hideProgressBar();
        }

    }

    protected class SecondaryTask extends LoadUserItemTask {
        
        private final int taskId;
        private SecondaryTask nextTask = null;
        
        public SecondaryTask(int taskId, String apiMethod) {
            super(apiMethod);
            this.taskId = taskId;
        }
        
        @Override
        protected void onPostExecute(JSONObject jsonObj) {
            // Log.d(TAG, jsonObj.toString());
            if (jsonObj != null) {
                try {
                    onSecondaryTaskPerfomed(taskId, jsonObj);
                } catch (JSONException jsone) {
                    Log.d(TAG, "JSON parsing failure: " + jsone.getLocalizedMessage());
                    jsone.printStackTrace();
                    Dialogs.makeExceptionToast(SingleItemActivity.this, "JSON parsing failure", jsone);
                }
            }
            hideProgressBar();
            
            if (nextTask != null) runNextSecondaryTask(nextTask);
        }        
        
        public int getId() { return taskId; }
        
        public void setNextTask(SecondaryTask nextTask) {
            if (this.nextTask != null) throw new IllegalStateException("Next task is already set");
            this.nextTask = nextTask;
        }
        
    }

}
