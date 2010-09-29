/**
 * 
 */
package org.vimeoid.adapter;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.util.AdvancedItem;

import android.widget.BaseAdapter;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.user</dd>
 * </dl>
 *
 * <code>JsonObjectsAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 27, 2010 7:29:03 PM 
 *
 */
public abstract class JsonObjectsAdapter<ItemType extends AdvancedItem> extends BaseAdapter {
    
    private final List<ItemType> items = new LinkedList<ItemType>();   
    
    public JsonObjectsAdapter() { }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public final Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public void addSource(JSONArray values) throws JSONException {        
        for (int i = 0; i < values.length(); i++) {
            items.add(extractItem(values.getJSONObject(i)));
        }
    }
    
    protected void finalize() {
        items.clear();
    }    

    protected abstract ItemType extractItem(JSONObject jsonObject) throws JSONException;
    
    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }    

}
