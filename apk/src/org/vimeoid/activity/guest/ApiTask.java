/**
 * 
 */
package org.vimeoid.activity.guest;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
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
public abstract class ApiTask extends AsyncTask<Uri, Void, Cursor> {
    
    private static final String TAG = "ApiTask";
    
    private final String[] projection;
    private final ContentResolver contentResolver;
        
    protected ApiTask(ContentResolver contentResolver, 
                      String[] projection) {
        this.projection = projection;
        this.contentResolver = contentResolver;
    }
        
    @Override
    protected Cursor doInBackground(Uri... uris) {
        return contentResolver.query(prepareUri(uris), 
                                     projection, null, null, null);
    }
    
    protected Uri prepareUri(Uri... uris) {
        if (uris.length <= 0) return null;
        if (uris.length > 1) throw new UnsupportedOperationException("This task do not supports several params");
        return uris[0];
    }
        
    @Override
    protected void onPostExecute(Cursor cursor) {
            
        if (cursor != null) {
                
            if (cursor.getCount() > 1) throw new IllegalStateException("There must be the only one item returned");
                
            cursor.moveToFirst();
            onAnswerReceived(cursor);
            //onItemReceived(extractor.fromCursor(cursor, 0));
            cursor.close();
        } else {
            Log.e(TAG, "Failed to receive next page");
            onAnyError(null, "Failed to receive next page");            
        }
            
    }
    
    protected void onAnyError(Exception e, String message) {
        Log.e(TAG, message);
    }
    
    protected abstract void onAnswerReceived(Cursor cursor);

}
