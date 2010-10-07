/**
 * 
 */
package org.vimeoid.adapter;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.activity.base.ApiPagesReceiver;
import org.vimeoid.dto.advanced.PagingData;
import org.vimeoid.util.AdvancedItem;
import org.vimeoid.util.PagingData_;

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
public abstract class JsonObjectsAdapter<ItemType extends AdvancedItem> extends BaseAdapter 
                                                                        implements ApiPagesReceiver<JSONObject> {
    
    private final List<ItemType> items = new LinkedList<ItemType>();
    private final String dataKey;
    private PagingData lastPagingData;    
    
    public JsonObjectsAdapter(String dataKey) { 
        this.dataKey = dataKey;
    }

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

    @Override
    public void addSource(JSONObject value) throws JSONException {
        lastPagingData = PagingData.collectFromJson(value, dataKey);
        
        final ItemType[] extracted = extractItems(value);
        for (int i = 0; i < extracted.length; i++) {
            items.add(extracted[i]);
        }
    }
    
    @Override
    public PagingData_ getCurrentPagingData(JSONObject lastPage) {
        return lastPagingData;
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        items.clear();
    }    

    protected abstract ItemType[] extractItems(JSONObject jsonObject) throws JSONException;
    
    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override public void onComplete() { }
    
    protected List<ItemType> getItems() { 
        return items;
    }

}
