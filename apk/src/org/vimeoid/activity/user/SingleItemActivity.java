/**
 * 
 */
package org.vimeoid.activity.user;

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
public abstract class SingleItemActivity<ItemType extends AdvancedItem> extends SingleItemActivity_<ItemType> {
    
    private static final String TAG = "SingleItemActivity";
    
    private final String apiMethod;
    private final String objectKey;
    private ApiParams params;
    
    public SingleItemActivity(int mainView, String apiMethod, String objectKey) {
        super(mainView);
        this.apiMethod = apiMethod;
        this.objectKey = objectKey;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        params = prepareMethodParams(apiMethod, objectKey, getIntent().getExtras());
    }
    
    protected abstract ApiParams prepareMethodParams(String methodName, String objectKey, Bundle extras);
    
    protected abstract ItemType extractFromJson(JSONObject jsonObj);
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        subjectIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.SUBJ_ICON, R.drawable.info));
        subjectTitle.setText(getIntent().hasExtra(Invoke.Extras.SUBJ_TITLE) 
                             ? getIntent().getStringExtra(Invoke.Extras.SUBJ_TITLE) 
                             : getString(R.string.unknown));
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.RES_ICON, R.drawable.info));
    }
    
    @Override 
    protected final void queryItem() {
        new LoadUserItemTask(apiMethod, objectKey).execute(params);
    }
    
    protected class LoadUserItemTask extends AsyncTask<ApiParams, Void, JSONObject> {

        private final String apiMethod;
        private final String objectKey;
        
        protected LoadUserItemTask(String apiMethod, String keyParam) {
            this.apiMethod = apiMethod;
            this.objectKey = keyParam;
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
                    return VimeoApi.advancedApi(apiMethod, params.getValue());
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
            if (jsonObj != null) {
                try {
                    onItemReceived(extractFromJson(jsonObj.getJSONObject(objectKey)));
                } catch (JSONException jsone) {
                    Log.d(TAG, "JSON parsing failure: " + jsone.getLocalizedMessage());
                    jsone.printStackTrace();
                    Dialogs.makeExceptionToast(SingleItemActivity.this, "JSON parsing failure", jsone);
                }
            }
            hideProgressBar();
        }

    }

}
