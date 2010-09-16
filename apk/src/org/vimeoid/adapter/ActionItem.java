/**
 * 
 */
package org.vimeoid.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter</dd>
 * </dl>
 *
 * <code>ActionItem</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 13, 2010 9:32:41 PM 
 *
 */
public class ActionItem {
    
    public static class ActionsSection {
        
        public final int id;
        public final String title;
        protected final List<ActionItem> actions = new LinkedList<ActionItem>();
        
        void addAction(ActionItem action) {
            actions.add(action);
        }
        
        void removeAction(ActionItem action) {
            actions.remove(action);
        }
        
        public int size() {
            return actions.size();
        }
        
        ActionsSection(int id, String title) {
            this.id = id;
            this.title = title;
        }

		public boolean contains(ActionItem action) {
			return actions.contains(action);
		}
        
    }
    
    public final ActionsSection section;
    public int icon = -1;
    public String title = null;
    public OnClickListener onClick = null;
    public final String iconUrl;
    public RenderingAdapter adapter = null;  
    
    private ActionItem(ActionsSection section, int icon, String iconUrl, String title) {
        this.section = section;
        this.icon = icon;
        this.title = title;
        this.iconUrl = iconUrl;        
    }
    
    ActionItem(ActionsSection section, int icon, String title) {
        this(section, icon, null, title);
    }
    
    ActionItem(ActionsSection section, String iconUrl, String title) {
        this(section, -1, iconUrl, title);
    }
    
    public void onClick() {
        
    }
    
    public static interface RenderingAdapter {
        public TextView render(Context context, TextView source, String value);
    }

}
