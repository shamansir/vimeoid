/**
 * 
 */
package org.vimeoid.adapter;

import java.util.LinkedList;
import java.util.List;

import org.vimeoid.R;
import org.vimeoid.adapter.ActionItem.ActionSelectedCallback;
import org.vimeoid.adapter.ActionItem.ActionsSection;

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
 * <code>SectionedActionsAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 13, 2010 9:47:43 PM 
 *
 */
public class SectionedActionsAdapter extends BaseAdapter {
	
	public static final String TAG = "SectionedActionsAdapter";
    
    public static final int ITEM_VIEW_TYPE = 0;
    public static final int SECTION_VIEW_TYPE = 1;
    
	private final LayoutInflater inflater;
    private final int sectionLayout;
    private final int actionLayout;
    
    private final List<ActionsSection> sections = new LinkedList<ActionsSection>();
    private int itemsCount = 0;
    
    public SectionedActionsAdapter(final LayoutInflater inflater) {
        this.inflater = inflater;
        this.sectionLayout = R.layout.actions_section_title;
        this.actionLayout = R.layout.item_action;
    }

    @Override
    public int getCount() {
        return itemsCount;
    }

    @Override
    public Object getItem(int position) {
        if (position < 0) throw new IllegalArgumentException("position must be greater than zero");
        
        int itemsLeft = position;
        for (ActionsSection section: sections) {
            
            if (itemsLeft == 0) return section;
            itemsLeft--;
            
            if (itemsLeft < section.size()) {
                for (ActionItem action: section.actions) {
                    if (itemsLeft == 0) return action;
                    itemsLeft--;
                }
            } else itemsLeft -= section.size();
            
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
    	for (ActionsSection section: sections) {
    		
    		if (itemsLeft == 0) return SECTION_VIEW_TYPE;
    		itemsLeft--;
    		
    		if (itemsLeft < section.size()) return ITEM_VIEW_TYPE;
    		itemsLeft -= section.size();
    		
    	}
    	return -1;
    }
    
    @Override
    public boolean isEnabled(int position) {
    	return getItemViewType(position) != SECTION_VIEW_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	final int viewType = getItemViewType(position);
    	
        if (viewType == -1) throw new IllegalStateException("Failed to get object at position " + position);
        
        Log.d(TAG, "generating view for item " + position);
        
        if (viewType == SECTION_VIEW_TYPE) {
        	
            final ActionsSection group = (ActionsSection) getItem(position);
            SectionHeaderHolder itemHolder = null;
            
            if (convertView == null) {
            	convertView = inflater.inflate(sectionLayout, parent, false);
                itemHolder = new SectionHeaderHolder();
                itemHolder.tvTitle = (TextView) convertView.findViewById(R.id.actionsGroupTitle); 
                convertView.setTag(itemHolder);
            } else {
            	itemHolder = (SectionHeaderHolder)convertView.getTag();
            }
            
            itemHolder.tvTitle.setText(group.title);
            
        } else if (viewType == ITEM_VIEW_TYPE) {
            
        	final ActionItem item = (ActionItem) getItem(position);
            ActionItemHolder itemHolder = null;
            
            if (convertView == null) {
           	    convertView = inflater.inflate(actionLayout, parent, false);
                itemHolder = new ActionItemHolder();
                itemHolder.tvTitle = (TextView) convertView.findViewById(R.id.actionName);
                itemHolder.ivIcon = (ImageView) convertView.findViewById(R.id.actionIcon); 
                convertView.setTag(itemHolder);
           } else {
           	    itemHolder = (ActionItemHolder)convertView.getTag();
           }
           
           itemHolder.tvTitle.setText(item.title);
           itemHolder.ivIcon.setImageResource(item.icon);
            
        }
        
        return convertView;
    }

    public int addSection(int title) {
        final ActionsSection newGroup = new ActionsSection(sections.size(), title); 
        sections.add(newGroup);
        itemsCount++;
        return newGroup.id;
    }
    
    public ActionItem addAction(int section, int icon, int title, ActionSelectedCallback callback) {
        if (section >= sections.size() || (section < 0)) throw new IllegalArgumentException("No section with such id (" + section + ") resgistered");
        final ActionsSection subjGroup = sections.get(section);
        final ActionItem newAction = new ActionItem(subjGroup, icon, title, callback); 
        subjGroup.addAction(newAction);
        itemsCount++;
        return newAction;
    }
    
    public void clear() {
        itemsCount = 0;
        sections.clear();
    }
    
    private class SectionHeaderHolder {
        TextView tvTitle;
    }
    
    private class ActionItemHolder {
        ImageView ivIcon;
        TextView tvTitle;
    }

}
