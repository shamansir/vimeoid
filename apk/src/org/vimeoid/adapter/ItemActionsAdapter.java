/**
 * 
 */
package org.vimeoid.adapter;

import java.util.LinkedList;
import java.util.List;

import org.vimeoid.R;
import org.vimeoid.adapter.ItemAction.ActionSelectedCallback;
import org.vimeoid.adapter.ItemAction.ItemActionsGroup;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter</dd>
 * </dl>
 *
 * <code>ItemActionsAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 13, 2010 9:47:43 PM 
 *
 */
public class ItemActionsAdapter extends BaseAdapter {
	
	public static final String TAG = "ItemActionsAdapter";
    
    public static final int ITEM_VIEW_TYPE = 0;
    public static final int GROUP_VIEW_TYPE = 1;
    
	private final LayoutInflater inflater;
    private final int groupLayout;
    private final int actionLayout;
    
    private final List<ItemActionsGroup> groups = new LinkedList<ItemActionsGroup>();
    private int itemsCount = 0;
    
    public ItemActionsAdapter(final LayoutInflater inflater) {
        this.inflater = inflater;
        this.groupLayout = R.layout.action_group_title;
        this.actionLayout = R.layout.action_item;
    }

    @Override
    public int getCount() {
        return itemsCount;
    }

    @Override
    public Object getItem(int position) {
        if (position < 0) throw new IllegalArgumentException("position must be greater than zero");
        
        int itemsLeft = position;
        for (ItemActionsGroup group: groups) {
            
            if (itemsLeft == 0) return group;
            itemsLeft--;
            
            if (itemsLeft < group.size()) {
                for (ItemAction action: group.actions) {
                    if (itemsLeft == 0) return action;
                    itemsLeft--;
                }
            } else itemsLeft -= group.size();
            
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public int getItemViewType(int position) {
    	int itemsLeft = position;
    	for (ItemActionsGroup group: groups) {
    		
    		if (itemsLeft == 0) return GROUP_VIEW_TYPE;
    		itemsLeft--;
    		
    		if (itemsLeft < group.size()) return ITEM_VIEW_TYPE;
    		itemsLeft -= group.size();
    		
    	}
    	return -1;
    }
    
    @Override
    public boolean isEnabled(int position) {
    	return getItemViewType(position) != GROUP_VIEW_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	final int viewType = getItemViewType(position);
    	
        if (viewType == -1) throw new IllegalStateException("Failed to get object at position " + position);
        
        Log.d(TAG, "generating view for item " + position);
        
        if (viewType == GROUP_VIEW_TYPE) {
        	
            final ItemActionsGroup group = (ItemActionsGroup) getItem(position);
            GroupHeaderHolder itemHolder = null;
            
            if (convertView == null) {
            	convertView = inflater.inflate(groupLayout, parent, false);
                itemHolder = new GroupHeaderHolder();
                itemHolder.tvTitle = (TextView) convertView.findViewById(R.id.actionsGroupTitle); 
                convertView.setTag(itemHolder);
            } else {
            	itemHolder = (GroupHeaderHolder)convertView.getTag();
            }
            
            itemHolder.tvTitle.setText(group.title);
            
        } else if (viewType == ITEM_VIEW_TYPE) {
            
        	final ItemAction item = (ItemAction) getItem(position);
            ItemActionHolder itemHolder = null;
            
            if (convertView == null) {
           	    convertView = inflater.inflate(actionLayout, parent, false);
                itemHolder = new ItemActionHolder();
                itemHolder.tvTitle = (TextView) convertView.findViewById(R.id.actionName);
                itemHolder.ivIcon = (ImageView) convertView.findViewById(R.id.actionIcon); 
                convertView.setTag(itemHolder);
           } else {
           	    itemHolder = (ItemActionHolder)convertView.getTag();
           }
           
           itemHolder.tvTitle.setText(item.title);
           itemHolder.ivIcon.setImageResource(item.icon);
            
        }
        
        return convertView;
    }

    public int addGroup(int title) {
        final ItemActionsGroup newGroup = new ItemActionsGroup(groups.size(), title); 
        groups.add(newGroup);
        itemsCount++;
        return newGroup.id;
    }
    
    public ItemAction addAction(int group, int icon, int title, ActionSelectedCallback callback) {
        if (group >= groups.size() || (group < 0)) throw new IllegalArgumentException("No group with such id (" + group + ") resgistered");
        final ItemActionsGroup subjGroup = groups.get(group);
        final ItemAction newAction = new ItemAction(subjGroup, icon, title, callback); 
        subjGroup.addAction(newAction);
        itemsCount++;
        return newAction;
    }
    
    public void clear() {
        itemsCount = 0;
        groups.clear();
    }
    
    private class GroupHeaderHolder {
        TextView tvTitle;
    }
    
    private class ItemActionHolder {
        ImageView ivIcon;
        TextView tvTitle;
    }

}
