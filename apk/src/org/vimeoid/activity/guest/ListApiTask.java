/**
 * 
 */
package org.vimeoid.activity.guest;

import org.vimeoid.activity.base.ApiPagesReceiver;
import org.vimeoid.activity.base.ListApiTask_;
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
public class ListApiTask extends ListApiTask_<Uri, Cursor> {
    
    private static final String TAG = "ListApiTask"; 
    
    private final ContentResolver resolver;
    private final String[] projection;
    
    protected ListApiTask(ContentResolver resolver, Reactor<Cursor> reactor, ApiPagesReceiver<Cursor> receiver, String[] projection) {
        this(resolver, 1, reactor, receiver, projection);
    }
    
    private ListApiTask(ContentResolver resolver, int curPage, Reactor<Cursor> reactor, ApiPagesReceiver<Cursor> receiver, String[] projection) {
        super(curPage, reactor, receiver);
        this.resolver = resolver;
        this.projection = projection;        
    }
    
    @Override
    protected Uri paramsForPage(Uri curParams, int pageNum, int perPage) {
        return (pageNum == 1) 
               ? curParams 
               : Uri.parse(VimeoProvider.BASE_URI 
                    + curParams.getPath() + "?page=" + pageNum);
    }

    @Override
    protected ListApiTask_<Uri, Cursor> makeNextPageTask(int nextPage,
            Reactor<Cursor> reactor, ApiPagesReceiver<Cursor> receiver) {
        return new ListApiTask(resolver, nextPage, reactor, receiver, projection);
    }

    @Override
    protected Cursor doInBackground(Uri... uris) {
        return resolver.query(prepareParams(uris), 
                              projection, null, null, null);
    }
    
    @Override
    protected void onPostExecute(Cursor cursor) {
            
        if (cursor != null) {
            try {                
                cursor.moveToFirst();
                onAnswerReceived(cursor);
                //onItemReceived(extractor.fromCursor(cursor, 0));
                cursor.close();
            } catch (Exception e) {
                onAnyError(e, "Failed while receiving answer: " + e.getLocalizedMessage());
            }
        } else {
            onNullReturned();   
        }
            
    }

    protected void onNullReturned() {
        Log.e(TAG, "Failed to receive next page");
        onAnyError(null, "Failed to receive next page");
    }    
    
}
