/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.VimeoApi.AdvancedApiCallError;
import org.vimeoid.util.ApiParams;

import android.os.AsyncTask;
import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>ApiTask</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 1, 2010 8:55:58 PM 
 *
 */
public abstract class ApiTask extends AsyncTask<ApiParams, Void, JSONObject> {
    
    private static final String TAG = "ApiTask";
    
    protected final String apiMethod;
    
    protected ApiTask(String apiMethod) {
        this.apiMethod = apiMethod;
    }
    
    @Override
    protected JSONObject doInBackground(ApiParams... paramsLists) {
        if (paramsLists.length <= 0) return null;
        if (paramsLists.length > 1) throw new UnsupportedOperationException("This task do not supports several params lists");
        
        final ApiParams params = paramsLists[0];
        try {            
            if (params == null || params.isEmpty()) {
                return VimeoApi.advancedApi(apiMethod);
            } else {
                return VimeoApi.advancedApi(apiMethod, params);
            }
        } catch (AdvancedApiCallError aace) { onApiError(aace); } 
          catch (Exception e) { onException(params, e); }
        
        return null;
    }
    
    @Override
    protected void onPostExecute(JSONObject jsonObj) {
        // Log.d(TAG, jsonObj.toString());
        if (jsonObj != null) {
            try {
                onAnswerReceived(jsonObj);
            } catch (JSONException jsone) {
                onJsonParsingError(jsone);
            }
        } else { onNullReturned(); }
    }
    
    protected void onAnyError(String message, Exception e) {
        Log.e(TAG, message);
    }
    
    protected void onApiError(AdvancedApiCallError error) {
        onAnyError("API Error " + error.code + " / " + error.message, error);
    }
    
    protected void onNullReturned() {
        onAnyError("Failed to receive object " + apiMethod, null);        
    }
    
    protected void onJsonParsingError(JSONException jsone) {
        onAnyError("JSON parsing failure: " + jsone.getLocalizedMessage(), jsone);
    }
    
    protected void onException(ApiParams params, Exception e) {
        onAnyError("Error while calling " + apiMethod + " " + params + " " + e.getLocalizedMessage(), e);        
    }
    
    public abstract void onAnswerReceived(JSONObject jsonObj) throws JSONException;

}
