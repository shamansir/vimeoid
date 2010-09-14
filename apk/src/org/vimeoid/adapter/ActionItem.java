/**
 * 
 */
package org.vimeoid.adapter;

import java.util.LinkedList;
import java.util.List;

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
        public final int title;
        public final List<ActionItem> actions = new LinkedList<ActionItem>();
        
        void addAction(ActionItem action) {
            actions.add(action);
        }
        
        void removeAction(ActionItem action) {
            actions.remove(action);
        }
        
        public int size() {
            return actions.size();
        }
        
        ActionsSection(int id, int title) {
            this.id = id;
            this.title = title;
        }

		public boolean contains(ActionItem action) {
			return actions.contains(action);
		}
        
    }
    
    public static interface ActionSelectedCallback {
        public void execute();
    }
    
    public final ActionsSection section;
    public int icon = -1;
    public int title = -1;
    public final ActionSelectedCallback callback;
    
    ActionItem(ActionsSection section, int icon, int title, ActionSelectedCallback callback) {
        this.section = section;
        this.icon = icon;
        this.title = title;
        this.callback = callback;
    }

}
