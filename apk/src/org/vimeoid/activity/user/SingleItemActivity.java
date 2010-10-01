/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.base.SingleItemActivity_;
import org.vimeoid.util.AdvancedItem;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

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
public abstract class SingleItemActivity<ItemType extends AdvancedItem> extends SingleItemActivity_<ItemType> {
    
    private static final String TAG = "SingleItemActivity";
    
    private String apiMethod;
    private ApiParams params;
    
    protected final ApiTasksQueue secondaryTasks;
    
    public SingleItemActivity(int mainView) {
        super(mainView);
        
        secondaryTasks = new ApiTasksQueue() {
            @Override public void onPerfomed(int taskId, JSONObject result)
                    throws JSONException {
                onSecondaryTaskPerfomed(taskId, result);
            }
        };
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
            new LongApiTask(apiMethod).execute(params).get();
        } catch (Exception e) {
            Log.e(TAG, "failed to get item");
            e.printStackTrace();
            Dialogs.makeExceptionToast(this, "Failed to get item", e);
        }    
        if (!secondaryTasks.isEmpty()) secondaryTasks.run();
    }
    
    public void onSecondaryTaskPerfomed(int id, JSONObject result)  throws JSONException { }
    
    protected class LongApiTask extends ApiTask {

        protected LongApiTask(String apiMethod) {
            super(apiMethod);
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();            
        }
        
        @Override
        protected void onPostExecute(JSONObject jsonObj) {
            super.onPostExecute(jsonObj);
            hideProgressBar();
        }
        
        @Override
        public void onAnswerReceived(JSONObject jsonObj) throws JSONException {
            onItemReceived(extractFromJson(jsonObj));
        }

    }

}
