/**
 * 
 */
package org.vimeoid.connection;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>JsonObjectsCursor</code>
 *
 * <p>A cursor over JSON objects, uses their <code>"id"<code> field as <code>"_id"<code> key
 * and suppresses exceptions to the stack trace</p>
 * 
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 28, 2010 5:47:34 PM 
 *
 */
public class JsonObjectsCursor extends StatsCollectingCursor {
    
    private static final String TAG = "JsonObjectsCursor";
    
    private final JSONArray source;
    private final String[] projection;
    
    public JsonObjectsCursor(JSONArray source, String[] projection, ApiCallInfo callStats) {
        super(callStats);
        this.source = source;
        this.projection = projection;
    }
    
    public JsonObjectsCursor(JSONObject source, String[] projection, ApiCallInfo callStats) {
        this(new JSONArray(Arrays.asList(source)), projection, callStats);
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
        	if (mPos < 0) throw new IllegalStateException("Cursor is not positioned");        	
            return source.getJSONObject(mPos).getDouble(adaptColumnName(projection[column]));
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
        	if (mPos < 0) throw new IllegalStateException("Cursor is not positioned");        	
            return source.getJSONObject(mPos).getInt(adaptColumnName(projection[column]));
        } catch (JSONException jsone) {
            notifyException(jsone);
            return 0;
        }
    }

    @Override
    public long getLong(int column) {
        try {
        	if (mPos < 0) throw new IllegalStateException("Cursor is not positioned");
            return source.getJSONObject(mPos).getLong(adaptColumnName(projection[column]));
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
        	if (mPos < 0) throw new IllegalStateException("Cursor is not positioned");        	
            return source.getJSONObject(mPos).getString(adaptColumnName(projection[column]));
        } catch (JSONException jsone) {
            notifyException(jsone);
            return null;
        }
    }

    @Override
    public boolean isNull(int column) {
        try {
        	if (mPos < 0) throw new IllegalStateException("Cursor is not positioned");        	
            return source.getJSONObject(mPos).isNull(adaptColumnName(projection[column]));
        } catch (JSONException jsone) {
            notifyException(jsone);
            return false;
        }
    }
    
    protected static final String adaptColumnName(String columnName) {
    	if ("_id".equals(columnName)) return "id";
    	return columnName;
    }
    
    private static void notifyException(JSONException jsone) {
        Log.e(TAG, "JSON Parsing exception when getting property : " + jsone.getLocalizedMessage());
        jsone.printStackTrace();
    }
    
}
