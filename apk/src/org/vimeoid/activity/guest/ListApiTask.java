/**
 * 
 */
package org.vimeoid.activity.guest;

import org.vimeoid.connection.simple.VimeoProvider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
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
        public boolean afterRequest(Cursor cursor, int received, ListApiTask nextPageTask);

        public void onError(Exception e, String message);
    }
    
    private static final String TAG = "ListApiTask"; 
    
    private final Reactor reactor;
    private final ApiPagesReceiver receiver;
    
    private int maxPages = 3;
    private int curPage = 1;
    private int perPage = 20;
    
    private final ContentResolver resolver;
    private final String[] projection;
    private Uri currentUri;
    
    public ListApiTask(ContentResolver resolver, Reactor reactor, ApiPagesReceiver receiver, String[] projection) {
        super(resolver, projection);
        this.resolver = resolver;
        this.reactor = reactor;
        this.receiver = receiver;
        this.projection = projection;
    }
    
    private ListApiTask(ContentResolver resolver, int curPage, Reactor reactor, ApiPagesReceiver receiver, String[] projection) {
        this(resolver, reactor, receiver, projection);
        this.curPage = curPage;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        reactor.beforeRequest();
    }
    
    @Override
    protected Uri prepareUri(Uri... uris) {
        currentUri = (curPage == 1) 
                     ? super.prepareUri(uris)
                     : Uri.parse(VimeoProvider.BASE_URI 
                       + super.prepareUri(uris).getPath() + "?page=" + curPage);
        return currentUri;
    }
    
    @Override
    public void onAnswerReceived(Cursor cursor) {
        
        //startManagingCursor(cursor);
        //onItemsReceived(cursor.getCount(), -1);
        receiver.addSource(cursor);
        cursor.close();        
        
        final int received = cursor.getCount();
        final int total = perPage * maxPages;
        
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
        
        final ListApiTask nextPageTask = new ListApiTask(resolver, ++curPage, reactor, receiver, projection);
        nextPageTask.setPerPage(perPage);
        nextPageTask.setMaxPages(maxPages);
        
        if ((curPage < maxPages) && 
            (receiver.getCount() < total) &&
            reactor.afterRequest(cursor, received, nextPageTask)) {
            nextPageTask.execute(currentUri);        
        }
        
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
