/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.base.SingleItemActivity_;
import org.vimeoid.connection.VimeoApi;
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
    
    private long currentUserId;    
    private long subjectUserId;
    
    public SingleItemActivity(int mainView) {
        super(mainView);
        
        secondaryTasks = new ApiTasksQueue() {
            @Override public void onPerfomed(int taskId, JSONObject result)
                    throws JSONException {
            	super.onPerfomed(taskId, result);
                onSecondaryTaskPerfomed(taskId, result);
            }

            @Override public void onError(final Exception e, final String message) {
            	runOnUiThread(new Runnable() {
					@Override public void run() {
		                Log.e(TAG, message + " / " + ((e != null) ? e.getLocalizedMessage() : "?"));
		                Dialogs.makeExceptionToast(SingleItemActivity.this, message, e);
					}
				});
            }
        };
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Bundle extras = getIntent().getExtras();
        
        currentUserId = VimeoApi.getUserLoginData(this).id;
        if (!extras.containsKey(Invoke.Extras.USER_ID)) 
            throw new IllegalArgumentException("Subject User ID must be passed in extras"); 
        subjectUserId = extras.getLong(Invoke.Extras.USER_ID);
        
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void prepare(Bundle extras) {
        apiMethod = getIntent().getStringExtra(Invoke.Extras.API_METHOD);
        params = ApiParams.fromBundle(getIntent().getBundleExtra(Invoke.Extras.API_PARAMS));

        super.prepare(extras);
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
            new ItemApiTask(apiMethod).execute(params).get();
        } catch (Exception e) {
            Log.e(TAG, "failed to get item");
            e.printStackTrace();
            Dialogs.makeExceptionToast(this, "Failed to get item", e);
        }    
        if (!secondaryTasks.isEmpty()) secondaryTasks.run();
    }
    
    public void onSecondaryTaskPerfomed(int id, JSONObject result)  throws JSONException { }
    
    public long getCurrentUserId() { return currentUserId; };
    public long getSubjectUserId() { return subjectUserId; };
    
    protected class ItemApiTask extends ApiTask {

        protected ItemApiTask(String apiMethod) {
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

        @Override
        protected void onAnyError(final Exception e, final String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, message + " / " + e.getLocalizedMessage());
                    Dialogs.makeExceptionToast(SingleItemActivity.this, message, e);
                }
            });
        }

    }

}
