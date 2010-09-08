package org.vimeoid.connection;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.vimeoid.connection.VimeoApi.ApiCallInfo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.providers</dd>
 * </dl>
 *
 * <code>VimeoSimpleApiProvider</code>
 *
 * <p>This content provider can provide a cursors for another applications
 * and supports only {@link VimeoSimpleApiProvider#query(Uri, String[], String, String[], String)}
 * from {@link ContentProvider} abstract methods. It is not connected to the database, 
 * it makes requests over <code>HTTP</code> to Vimeo and just simulates the cursor. 
 * The <code>Uri</code> schemes are almost similar</p> to Vimeo Simple API calls,
 * but differ in some cases, the actual list will be provided in future documentation.  
 * 
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 20, 2010 5:29:21 PM 
 *
 */

public class VimeoSimpleApiProvider extends ContentProvider {
    
    public static final String AUTHORITY = VimeoApi.SIMPLE_API_AUTHORITY;
    
    public static final String TAG = "VimeoSimpleApiProvider";
    
    public static final Uri BASE_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).build();
    

    /**
     * This method is not supported in <code>VimeoSimpleApiProvider</code>
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Deletion of something in not supported in VimeoSimpleApiProvider");
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        return VimeoApi.getSimpleApiReturnType(uri);
    }

    
    /**
     * This method is not supported in <code>VimeoSimpleApiProvider</code>
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Insertion of something in not supported in VimeoSimpleApiProvider");
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
     */
    @Override
    public Cursor query(Uri contentUri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        if ((selection != null) || (selectionArgs != null))
            throw new UnsupportedOperationException("SQL Where-selections are not supported in VimeoSimpleApiProvider, please use URI to filter the selection query");
        if (sortOrder != null) throw new UnsupportedOperationException("SQL-styled sorting is not supported in VimeoSimpleApiProvider, please use URI parameters to specify sorting order (if supported by the method)");
        if (projection == null) throw new IllegalArgumentException("Please specify projection, at least empty one"); 
        final ApiCallInfo apiCallInfo = VimeoApi.resolveUriForSimpleApi(contentUri); 
        try {
            if (apiCallInfo.multipleResult) {
                final JSONArray jsonArr = JsonOverHttp.use().askForArray(apiCallInfo.fullCallUrl);
                return new JsonObjectsCursor(jsonArr, projection, apiCallInfo);
            } else {
                final JSONObject jsonObj = JsonOverHttp.use().askForObject(apiCallInfo.fullCallUrl);
                return new JsonObjectsCursor(jsonObj, projection, apiCallInfo);
            }            
        } catch (ClientProtocolException cpe) {
            Log.e(TAG, "Client protocol exception" + cpe.getLocalizedMessage());
            cpe.printStackTrace();
        } catch (JSONException jsone) {
            Log.e(TAG, "JSON parsing exception " + jsone.getLocalizedMessage());
            jsone.printStackTrace();
        } catch (IOException ioe) {
            Log.e(TAG, "Connection/IO exception " + ioe.getLocalizedMessage());
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * This method is not supported in <code>VimeoSimpleApiProvider</code>
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        throw new UnsupportedOperationException("Updation of something in not supported in VimeoSimpleApiProvider");
    }

}
