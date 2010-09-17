/**
 * 
 */
package org.vimeoid.adapter;

import java.util.HashMap;
import java.util.Map;

import org.vimeoid.util.Item;

import android.database.Cursor;
import android.widget.BaseAdapter;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter</dd>
 * </dl>
 *
 * <code>EasyCursorsAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 5, 2010 10:07:35 PM 
 *
 */
public abstract class EasyCursorsAdapter<ItemType extends Item> extends BaseAdapter {
    
    private static final int MAX_CURSORS_COUNT = 6;
    
    private final Cursor[] cursors = new Cursor[MAX_CURSORS_COUNT];
    private final String idColumnName;
    
    private final Map<Integer, ItemType> cache = new HashMap<Integer, ItemType>();
    
    private int cursorsCount = 0;
    private int itemsCount = 0;
    
    public EasyCursorsAdapter(String idColumnName) {        
        this.idColumnName = idColumnName;
        cursorsCount = 0;
    }
    
    public EasyCursorsAdapter() {
        this("_id");
    }

    @Override
    public int getCount() {
        return itemsCount;
    }
    
    public abstract ItemType extractItem(Cursor cursor, int position);

    @Override
    public final Object getItem(int position) {
        if (position >= itemsCount) return null;
        
        if (cache.containsKey(position)) return cache.get(position);        
        
        int passedCount = 0;
        
        for (int i = 0; i < cursorsCount; i++) {
            final Cursor cursor = cursors[i];
            if (position >= (passedCount + cursor.getCount())) {
                passedCount += cursor.getCount();
            } else {
                cursor.moveToPosition(position - passedCount);
                final ItemType result = extractItem(cursor, position - passedCount);
                cache.put(position, result);
                return result;
            }
        }        
        
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (position >= itemsCount) return -1;
        
        int passedCount = 0;
        
        for (int i = 0; i < cursorsCount; i++) {
            final Cursor cursor = cursors[i];
            if (position >= (passedCount + cursor.getCount())) {
                passedCount += cursor.getCount();
            } else {
                cursor.moveToPosition(position - passedCount);
                return cursor.getLong(cursor.getColumnIndexOrThrow(idColumnName));
            }
        }         
        
        return -1;
    }
    
    public void addSource(Cursor cursor) {
        if (cursorsCount == MAX_CURSORS_COUNT) 
            throw new IllegalArgumentException("Max number of cursors for this adapter reached");
        
        cursors[cursorsCount] = cursor;
        cursorsCount++;
        
        itemsCount += cursor.getCount();
        
        cursor.moveToFirst();
    }    
        
    public void finalize() {
        for (int i = 0; i < cursorsCount; i++) {
            cursors[i].close();
        }
        cache.clear();
    }

}
