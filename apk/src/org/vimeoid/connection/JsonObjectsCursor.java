/**
 * 
 */
package org.vimeoid.connection;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.AbstractCursor;
import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>JsonObjectsCursor</code>
 *
 * <p>Description</p>
 * 
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 28, 2010 5:47:34 PM 
 *
 */
public class JsonObjectsCursor extends AbstractCursor {
    
    private static final String TAG = "JsonObjectsCursor";
    
    private final JSONArray source;
    private final String[] projection;
    
    public JsonObjectsCursor(JSONArray source, String[] projection) {
        this.source = source;
        this.projection = projection;
    }
    
    public JsonObjectsCursor(JSONObject source, String[] projection) {
        this(new JSONArray(Arrays.asList(source)), projection);
    }
        
    @Override
    public String[] getColumnNames() {
        return projection;
    }

    @Override
    public int getCount() {
        return source.length();
    }

    @Override
    public double getDouble(int column) {
        try {
            return source.getJSONObject(mPos).getDouble(projection[column]);
        } catch (JSONException jsone) {
            notifyException(jsone);
            return 0;
        }
    }

    @Override
    public float getFloat(int column) {
        throw new UnsupportedOperationException("Not allowed to get double values in this cursor");
    }

    @Override
    public int getInt(int column) {
        try {
            return source.getJSONObject(mPos).getInt(projection[column]);
        } catch (JSONException jsone) {
            notifyException(jsone);
            return 0;
        }
    }

    @Override
    public long getLong(int column) {
        try {
            return source.getJSONObject(mPos).getLong(projection[column]);
        } catch (JSONException jsone) {
            notifyException(jsone);
            return 0;
        }
    }

    @Override
    public short getShort(int column) {
        throw new UnsupportedOperationException("Not allowed to get short values in this cursor");
    }

    @Override
    public String getString(int column) {
        try {
            return source.getJSONObject(mPos).getString(projection[column]);
        } catch (JSONException jsone) {
            notifyException(jsone);
            return null;
        }
    }

    @Override
    public boolean isNull(int column) {
        try {
            return source.getJSONObject(mPos).isNull(projection[column]);
        } catch (JSONException jsone) {
            notifyException(jsone);
            return false;
        }
    }
    
    private static void notifyException(JSONException jsone) {
        Log.e(TAG, "JSON Parsing exception when getting property : " + jsone.getLocalizedMessage());
        jsone.printStackTrace();
    }

}
