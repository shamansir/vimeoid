/**
 * 
 */
package org.vimeoid.activity.guest;

import org.json.JSONException;
import org.vimeoid.activity.base.ApiTask_;

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
 * <code>ApiTask</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 1, 2010 8:55:58 PM 
 *
 */
public abstract class ApiTask extends ApiTask_<Uri, Cursor> {
    
    private static final String TAG = "ApiTask";
    
    private final String[] projection;
    private final ContentResolver contentResolver;
        
    protected ApiTask(ContentResolver contentResolver, 
                      String[] projection) {
		this.contentResolver = contentResolver;
		this.projection = projection;		
	}
        
    @Override
    protected Cursor doInBackground(Uri... uris) {
        return contentResolver.query(prepareParams(uris), 
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
            Log.e(TAG, "Failed to receive next page");
            onAnyError(null, "Failed to receive next page");            
        }
            
    }
    
    @Override protected abstract void onAnswerReceived(Cursor cursor) throws JSONException;
    
}
