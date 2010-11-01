/**
 * 
 */
package org.vimeoid.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter</dd>
 * </dl>
 *
 * <code>LActionItem</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 13, 2010 9:32:41 PM 
 *
 */
public class LActionItem {
    
    public static class LActionsSection {
        
        public final int id;
        public final String title;
        protected final int position;        
        protected final List<LActionItem> actions = new LinkedList<LActionItem>();
        
        void addAction(LActionItem action) {
            actions.add(action);
        }
        
        void removeAction(LActionItem action) {
            actions.remove(action);
        }
        
        public int size() {
            return actions.size();
        }
        
        LActionsSection(int position, int id, String title) {
            this.id = id;
            this.title = title;
            this.position = position;
        }

		public boolean contains(LActionItem action) {
			return actions.contains(action);
		}
        
    }
    
    public final LActionsSection section;
    public int icon = -1;
    public String title = null;
    public OnClickListener onClick = null;
    public final String iconUrl;
    public final int position;
    public RenderingAdapter adapter = null;  
    
    private LActionItem(int position, LActionsSection section, int icon, String iconUrl, String title) {
        this.section = section;
        this.position = position;
        this.icon = icon;
        this.title = title;
        this.iconUrl = iconUrl;        
    }
    
    LActionItem(int position, LActionsSection section, int icon, String title) {
        this(position, section, icon, null, title);
    }
    
    LActionItem(int position, LActionsSection section, String iconUrl, String title) {
        this(position, section, -1, iconUrl, title);
    }
    
    public void onClick(View view) {
        if (onClick != null) onClick.onClick(view);
    }
    
    public static interface RenderingAdapter {
        public TextView render(Context context, TextView source, String value);
    }

}
