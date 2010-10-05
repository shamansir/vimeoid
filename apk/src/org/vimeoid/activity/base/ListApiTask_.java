/**
 * 
 */
package org.vimeoid.activity.base;

import org.vimeoid.util.PagingData_;

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
public abstract class ListApiTask_<Params, Result> extends ApiTask_<Params, Result> {
    
    public static interface Reactor<Result> {
        
        public void onNoItems();
        public void onNoMoreItems();
        public void onNextPageExists();
        
        public void beforeRequest();        
        public boolean afterRequest(Result result, int received, boolean needMore, ApiTask_<?, Result> nextPageTask);

        public void onError(Exception e, String message);
    }
    
    private static final String TAG = "ListApiTask"; 
    
    private final Reactor<Result> reactor;
    private final ApiPagesReceiver<Result> receiver;
    
    private int maxPages = 3;
    private int curPage = 1;
    private int perPage = 20;
    
    private Params curParams; 
    
    protected ListApiTask_(Reactor<Result> reactor, ApiPagesReceiver<Result> receiver) {
        this.reactor = reactor;
        this.receiver = receiver;
    }
    
    protected ListApiTask_(int curPage, Reactor<Result> reactor, ApiPagesReceiver<Result> receiver) {
        this(reactor, receiver);
        this.curPage = curPage; 
    }
    
    /*
    private ListApiTask(ContentResolver resolver, int curPage, Reactor reactor, ApiPagesReceiver receiver, String[] projection) {
        this(resolver, reactor, receiver, projection);
        this.curPage = curPage;
    } */
    
    protected abstract ListApiTask_<Params, Result> makeNextPageTask(int nextPage, Reactor<Result> reactor, ApiPagesReceiver<Result> receiver);
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        reactor.beforeRequest();
    }
    
    protected final Params prepareParams(Params... params) {
        curParams = paramsForPage(super.prepareParams(params), curPage, perPage);
        return curParams;
    };
    
    protected abstract Params paramsForPage(Params curParams, int pageNum, int perPage);
    
    @SuppressWarnings("unchecked")
    @Override
    public void onAnswerReceived(Result result) throws Exception {
        
        try {
            receiver.addSource(result);
            
            final PagingData_ pd = receiver.getCurrentPagingData(result); 
            
            final int received = pd.onThisPage;
            final int total = (pd.total != -1) ? pd.total : (perPage * maxPages);
            
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
            
            final boolean needMore = (curPage < maxPages) && (receiver.getCount() < total);        
            
            ListApiTask_<Params, Result> nextPageTask = null;
            if (needMore) {
                nextPageTask = makeNextPageTask(++curPage, reactor, receiver);
                nextPageTask.setPerPage(perPage);
                nextPageTask.setMaxPages(maxPages);
            }
            
            if (reactor.afterRequest(result, received, needMore, nextPageTask)
                && needMore) {
                nextPageTask.execute(curParams);
            }
            
        } catch (Exception e) {
            onAnyError(e, "Failed to add source");
        }
        
        
        /* 
         * 
         * } else Dialogs.makeToast(this, getString(R.string.no_pages_more));
        } else Dialogs.makeToast(this, getString(R.string.please_do_not_touch)); */
        
    }
    
    @Override
    protected void onAnyError(Exception e, String message) {
        Log.e(TAG, message + " / " + e.getLocalizedMessage());
        reactor.onError(e, message);
    }
    
    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages; 
    }
    
    public void setPerPage(int perPage) {
        this.perPage = perPage; 
    }

}
