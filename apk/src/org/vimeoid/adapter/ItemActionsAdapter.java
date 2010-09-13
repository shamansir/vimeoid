/**
 * 
 */
package org.vimeoid.adapter;

import java.util.LinkedList;
import java.util.List;

import org.vimeoid.R;
import org.vimeoid.adapter.ItemAction.ActionSelectedCallback;
import org.vimeoid.adapter.ItemAction.ItemActionsGroup;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Object testObj = getItem(position);
        if (testObj == null) throw new IllegalStateException("Failed to get object at position " + position);
        if (testObj instanceof ItemActionsGroup) {
            final ItemActionsGroup group = (ItemActionsGroup)testObj;
            final GroupHeaderHolder itemHolder = null;
        } else if (testObj instanceof ItemAction) {
            final ItemAction item = (ItemAction)testObj;
            final ItemActionHolder itemHolder = null;
        }
        return null;
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
