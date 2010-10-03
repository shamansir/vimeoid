/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.dto.advanced.PagingData;
import org.vimeoid.util.ApiParams;

import android.util.Log;

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
public class ListApiTask extends ApiTask {
    
    public static interface Reactor {
        
        public void onNoItems();
        public void onNoMoreItems();
        public void onNextPageExists();
        
        public void beforeRequest();        
        public boolean afterRequest(ApiObjectsReceiver receiver, int received, ListApiTask nextPageTask);

        public void onError(Exception e, String message);
    }
    
    private static final String TAG = "ListApiTask"; 
    
    private final Reactor reactor;
    private final ApiObjectsReceiver receiver;
    
    private int maxPages = 3;
    private int curPage = 1;
    private int perPage = 20;
    
    private ApiParams curParams;
    
    public ListApiTask(Reactor reactor, ApiObjectsReceiver receiver, String apiMethod) {
        super(apiMethod);
        this.reactor = reactor;
        this.receiver = receiver;
    }
    
    private ListApiTask(int curPage, Reactor reactor, ApiObjectsReceiver receiver, String apiMethod) {
        this(reactor, receiver, apiMethod);
        this.curPage = curPage;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        reactor.beforeRequest();
    }
    
    @Override
    protected ApiParams prepareParams(ApiParams... paramsList) {
        final ApiParams curParams = super.prepareParams(paramsList);
        return curParams.add("page", String.valueOf(curPage))
                        .add("per_page", String.valueOf(perPage));
                      // TODO: params.add("sort", "");
    }
    
    @Override
    public void onAnswerReceived(JSONObject jsonObj) throws JSONException {
        
        receiver.addPage(jsonObj);
        PagingData pd = receiver.getLastPagingData();
        
        if (pd.pageNum != curPage) throw new IllegalStateException("Received page number do not matches actual");
        if (pd.perPage != perPage) throw new IllegalStateException("Received number of items per page do not matches actual");
        
        final int received = pd.onThisPage;
        final int total = pd.total;
        
        if ((received == 0) && (curPage == 1)) {
            // no items in list at all
            reactor.onNoItems();      
        } else if ((received < perPage) ||     
                   (curPage == maxPages) ||
                   ((total != -1) && (((perPage * (curPage - 1)) + received) == total))) {
            // no items more
            reactor.onNoMoreItems();
        } else {
            // enable 'load more' button
            reactor.onNextPageExists();
        }
        
        Log.d(TAG, "Received " + received + " items");
        
        final ListApiTask nextPageTask = new ListApiTask(++curPage, reactor, receiver, apiMethod);
        nextPageTask.setPerPage(perPage);
        nextPageTask.setMaxPages(maxPages);
        
        if ((curPage < maxPages) && 
            (receiver.getCount() < total) &&
            reactor.afterRequest(receiver, received, nextPageTask)) {
            nextPageTask.execute(curParams);        
        }
        
        /*onContentChanged();
        
        // TODO: scroll to the first received item (smoothScrollToPosition in API 8)
        final int newPos = getListView().getCount() - howMuch - 2; // - 'load more' and one position before
        if (newPos >= 0) setSelection(newPos);
        else setSelection(0); */
        
        /* 
         * 
         * } else Dialogs.makeToast(this, getString(R.string.no_pages_more));
        } else Dialogs.makeToast(this, getString(R.string.please_do_not_touch)); */
        
    }
    
    @Override
    protected void onAnyError(Exception e, String message) {
        super.onAnyError(e, message);
        reactor.onError(e, message);
    }
    
    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages; 
    }
    
    public void setPerPage(int perPage) {
        this.perPage = perPage; 
    }
        
}
