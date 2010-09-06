/**
 * 
 */
package org.vimeoid.adapter;

import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.widget.BaseAdapter;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter</dd>
 * </dl>
 *
 * <code>EasyCursorAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 5, 2010 10:07:35 PM 
 *
 */
public abstract class EasyCursorAdapter<ItemType> extends BaseAdapter {
    
    private final Cursor cursor;
    private final String idColumnName;
    
    private final Map<Integer, ItemType> cache = new HashMap<Integer, ItemType>();
    
    public EasyCursorAdapter(Cursor cursor, String idColumnName) {    	
        this.cursor = cursor;
        this.idColumnName = idColumnName;
        
        this.cursor.moveToFirst();
    }
    
    public EasyCursorAdapter(Cursor cursor) {
        this(cursor, "_id");
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }
    
    public abstract ItemType extractItem(Cursor cursor, int position);

    @Override
    public final Object getItem(int position) {
    	cursor.moveToPosition(position);
        if (cache.containsKey(position)) return cache.get(position);
        else return extractItem(cursor, position);
    }

    @Override
    public long getItemId(int position) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(idColumnName));
    }
    
    public void appendData(Cursor cursor) {
        // FIXME: implement
        
    }    
        
    public void finalize() {
    	cursor.close();
    	cursor.deactivate();
        cache.clear();
    }

}
