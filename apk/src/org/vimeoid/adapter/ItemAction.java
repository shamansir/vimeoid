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
 * <code>ItemAction</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 13, 2010 9:32:41 PM 
 *
 */
public class ItemAction {
    
    public static class ItemActionsGroup {
        
        public final int id;
        public final int title;
        public final List<ItemAction> actions = new LinkedList<ItemAction>();
        
        void addAction(ItemAction action) {
            actions.add(action);
        }
        
        void removeAction(ItemAction action) {
            actions.remove(action);
        }
        
        public int size() {
            return actions.size();
        }
        
        ItemActionsGroup(int id, int title) {
            this.id = id;
            this.title = title;
        }
        
    }
    
    public static interface ActionSelectedCallback {
        public void execute();
    }
    
    public final ItemActionsGroup group;
    public int icon = -1;
    public int title = -1;
    public final ActionSelectedCallback callback;
    
    ItemAction(ItemActionsGroup group, int icon, int title, ActionSelectedCallback callback) {
        this.group = group;
        this.icon = icon;
        this.title = title;
        this.callback = callback;
    }
}
