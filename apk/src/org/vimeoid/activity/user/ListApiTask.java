/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONObject;
import org.vimeoid.activity.base.ApiPagesReceiver;
import org.vimeoid.activity.base.ListApiTask_;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.VimeoApi.AdvancedApiCallError;
import org.vimeoid.util.ApiParams;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>ListApiTask</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 3, 2010 12:22:41 PM 
 *
 */
public class ListApiTask extends ListApiTask_<ApiParams, JSONObject> {
	
    //private static final String TAG = "ListApiTask";
    
    private final String apiMethod;
        
    public ListApiTask(Reactor<JSONObject> reactor, ApiPagesReceiver<JSONObject> receiver, String apiMethod) {
        this(1, reactor, receiver, apiMethod);
    }
    
    private ListApiTask(int curPage, Reactor<JSONObject> reactor, ApiPagesReceiver<JSONObject> receiver, String apiMethod) {
        super(curPage, reactor, receiver);
        this.apiMethod = apiMethod;
    }
    
    @Override
    protected ApiParams paramsForPage(ApiParams curParams, int pageNum, int perPage) {
        return curParams.add("page", String.valueOf(pageNum))
                        .add("per_page", String.valueOf(perPage));
    }
    
    @Override
    protected ListApiTask_<ApiParams, JSONObject> makeNextPageTask(int nextPage,
                         Reactor<JSONObject> reactor, ApiPagesReceiver<JSONObject> receiver) {
        return new ListApiTask(nextPage, reactor, receiver, apiMethod);
    }

    @Override
    protected JSONObject doInBackground(ApiParams... paramsList) {
        final ApiParams params = prepareParams(paramsList);
        try {
            ensureConnected();
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
        super.onPostExecute(jsonObj);
        // Log.d(TAG, jsonObj.toString());
        if (jsonObj != null) {
            try {
                onAnswerReceived(jsonObj);
            } catch (Exception e) {
                onAnyError(e, "Failed to receive answer");
            }
        } else { onNullReturned(); }
    }
    
    protected void onApiError(AdvancedApiCallError error) {
        onAnyError(error, "API Error " + error.code + " / " + error.message);
    }
    
    protected void onException(ApiParams params, Exception e) {
        onAnyError(e, "Error while calling " + apiMethod + " " + params + " " + e.getLocalizedMessage());        
    }    

    protected void ensureConnected() {
        return; // FIXME: not used
    }    

    protected void onNullReturned() {
        onAnyError(null, "Failed to parse answer");
    }    
    
//  receiver.addSource(jsonObj);
//  PagingData pd = receiver.getLastPagingData();    
    
    //  if (pd.pageNum != curPage) throw new IllegalStateException("Received page number do not matches actual");
    //  if (pd.perPage != perPage) throw new IllegalStateException("Received number of items per page do not matches actual");
    
//    final int received = pd.onThisPage;
//    final int total = pd.total;
    
}
