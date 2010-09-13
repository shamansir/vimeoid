/**
 * 
 */
package org.vimeoid.test;

import junit.framework.Assert;

import org.vimeoid.adapter.ItemAction;
import org.vimeoid.adapter.ItemActionsAdapter;
import org.vimeoid.adapter.ItemAction.ItemActionsGroup;

import android.test.AndroidTestCase;

/**
 * 
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid-test</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.test</dd>
 * </dl>
 *
 * <code>ItemActionsAdapterTest</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 13, 2010 10:52:06 PM 
 *
 */
public class ItemActionsAdapterTest extends AndroidTestCase {
    
    final ItemActionsAdapter adapter = new ItemActionsAdapter(null);    
    
    public void testAddingGroupsAndItems() {
        adapter.clear();
        
        // group 0
        Assert.assertEquals(0, adapter.addGroup(128));
        Assert.assertEquals(1, adapter.getCount());
        Assert.assertEquals(128, ((ItemActionsGroup)adapter.getItem(0)).title);
        
        try {
            adapter.addAction(2, -1, -1, null);
            Assert.fail("must produce IAE");
        } catch (IllegalArgumentException iae) { }
        
        try {
            adapter.addAction(-12, -1, -1, null);
            Assert.fail("must produce IAE");
        } catch (IllegalArgumentException iae) { }
        
        try {
            adapter.addAction(5, -1, -1, null);
            Assert.fail("must produce IAE");
        } catch (IllegalArgumentException iae) { }        

        try {
            adapter.addAction(1013, -1, -1, null);
            Assert.fail("must produce IAE");
        } catch (IllegalArgumentException iae) { }        
        
        // group 0: action 0
        ItemAction added = adapter.addAction(0, -1, 14, null);
        Assert.assertEquals(0, added.group.id);
        Assert.assertEquals(2, adapter.getCount());
        Assert.assertEquals(14, ((ItemAction)adapter.getItem(1)).title);
        
        // group 0: action 1
        added = adapter.addAction(0, -1, 114, null);
        Assert.assertEquals(0, added.group.id);
        Assert.assertEquals(3, adapter.getCount());
        Assert.assertEquals(114, ((ItemAction)adapter.getItem(2)).title);

        // group 1
        Assert.assertEquals(1, adapter.addGroup(19));
        Assert.assertEquals(4, adapter.getCount());
        Assert.assertEquals(19, ((ItemActionsGroup)adapter.getItem(3)).title);
        
        // group 2
        Assert.assertEquals(2, adapter.addGroup(1378));
        Assert.assertEquals(5, adapter.getCount());
        Assert.assertEquals(1378, ((ItemActionsGroup)adapter.getItem(4)).title);

        // group 3
        Assert.assertEquals(3, adapter.addGroup(40));
        Assert.assertEquals(6, adapter.getCount());
        Assert.assertEquals(40, ((ItemActionsGroup)adapter.getItem(5)).title);
        
        // group 3: action 0        
        added = adapter.addAction(3, -1, 215, null);
        Assert.assertEquals(3, added.group.id);
        Assert.assertEquals(7, adapter.getCount());
        Assert.assertEquals(215, ((ItemAction)adapter.getItem(6)).title);
        
        // group 3: action 1        
        added = adapter.addAction(3, -1, 19567, null);
        Assert.assertEquals(3, added.group.id);
        Assert.assertEquals(8, adapter.getCount());
        Assert.assertEquals(19567, ((ItemAction)adapter.getItem(7)).title);        
        
        // group 1: action 0 
        added = adapter.addAction(1, -1, 2064, null);
        Assert.assertEquals(1, added.group.id);
        Assert.assertEquals(9, adapter.getCount());
        Assert.assertEquals(2064, ((ItemAction)adapter.getItem(4)).title);
        
        // group 1: action 1
        added = adapter.addAction(1, -1, 3095, null);
        Assert.assertEquals(1, added.group.id);
        Assert.assertEquals(10, adapter.getCount());
        Assert.assertEquals(3095, ((ItemAction)adapter.getItem(5)).title);
        
        // group 1: action 2
        added = adapter.addAction(1, -1, 32700, null);
        Assert.assertEquals(1, added.group.id);
        Assert.assertEquals(11, adapter.getCount());
        Assert.assertEquals(32700, ((ItemAction)adapter.getItem(6)).title);
        
        // is group 0 here
        Assert.assertEquals(128, ((ItemActionsGroup)adapter.getItem(0)).title);
        
        // is group 1 here
        Assert.assertEquals(19, ((ItemActionsGroup)adapter.getItem(3)).title);
        
        // is group 2 here
        Assert.assertEquals(1378, ((ItemActionsGroup)adapter.getItem(7)).title);
        
        // group 2: action 0
        added = adapter.addAction(2, -1, -218, null);
        Assert.assertEquals(2, added.group.id);
        Assert.assertEquals(12, adapter.getCount());
        Assert.assertEquals(-218, ((ItemAction)adapter.getItem(8)).title);
        
        // is group 3 here
        Assert.assertEquals(40, ((ItemActionsGroup)adapter.getItem(9)).title);
        
        // group 4
        Assert.assertEquals(4, adapter.addGroup(120));
        Assert.assertEquals(13, adapter.getCount());
        Assert.assertEquals(120, ((ItemActionsGroup)adapter.getItem(12)).title);        
        
        // group 4: action 0
        added = adapter.addAction(4, -1, 315, null);
        Assert.assertEquals(4, added.group.id);
        Assert.assertEquals(14, adapter.getCount());
        Assert.assertEquals(315, ((ItemAction)adapter.getItem(13)).title);
        
        // group 2: action 1
        added = adapter.addAction(2, -1, 917, null);
        Assert.assertEquals(2, added.group.id);
        Assert.assertEquals(15, adapter.getCount());
        Assert.assertEquals(917, ((ItemAction)adapter.getItem(9)).title);
        
        // is group 4 here
        Assert.assertEquals(120, ((ItemActionsGroup)adapter.getItem(13)).title);
        
        // is group 4: action 0 here
        Assert.assertEquals(315, ((ItemAction)adapter.getItem(14)).title);
        
    }
    
}
