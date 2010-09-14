/**
 * 
 */
package org.vimeoid.test;

import junit.framework.Assert;

import org.vimeoid.adapter.ActionItem;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.adapter.ActionItem.ActionsSection;

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
    
    final SectionedActionsAdapter adapter = new SectionedActionsAdapter(null);
    
    public void testAddingGroupsAndItems() {
        adapter.clear();
        
        // section 0
        Assert.assertEquals(0, adapter.addSection("128"));
        Assert.assertEquals(1, adapter.getCount());
        Assert.assertFalse(adapter.isEnabled(0));
        Assert.assertEquals(SectionedActionsAdapter.SECTION_VIEW_TYPE, adapter.getItemViewType(0));
        
        Assert.assertEquals("128", ((ActionsSection)adapter.getItem(0)).title);
        
        try {
            adapter.addAction(2, -1, "");
            Assert.fail("must produce IAE");
        } catch (IllegalArgumentException iae) { }
        
        try {
            adapter.addAction(-12, -1, "");
            Assert.fail("must produce IAE");
        } catch (IllegalArgumentException iae) { }
        
        try {
            adapter.addAction(5, -1, "");
            Assert.fail("must produce IAE");
        } catch (IllegalArgumentException iae) { }        

        try {
            adapter.addAction(1013, -1, "");
            Assert.fail("must produce IAE");
        } catch (IllegalArgumentException iae) { }        
        
        // section 0: action 0
        ActionItem added = adapter.addAction(0, -1, "14");
        Assert.assertEquals(0, added.section.id);
        Assert.assertEquals(2, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(1));
        Assert.assertTrue(adapter.isEnabled(1));
        Assert.assertEquals("14", ((ActionItem)adapter.getItem(1)).title);
        
        // section 0: action 1
        added = adapter.addAction(0, -1, "114");
        Assert.assertEquals(0, added.section.id);
        Assert.assertEquals(3, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(2));
        Assert.assertTrue(adapter.isEnabled(2));
        Assert.assertEquals("114", ((ActionItem)adapter.getItem(2)).title);

        // section 1
        Assert.assertEquals(1, adapter.addSection("19"));
        Assert.assertEquals(4, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.SECTION_VIEW_TYPE, adapter.getItemViewType(3));
        Assert.assertFalse(adapter.isEnabled(3));
        Assert.assertEquals("19", ((ActionsSection)adapter.getItem(3)).title);
        
        // section 2
        Assert.assertEquals(2, adapter.addSection("1378"));
        Assert.assertEquals(5, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.SECTION_VIEW_TYPE, adapter.getItemViewType(4));
        Assert.assertFalse(adapter.isEnabled(4));
        Assert.assertEquals("1378", ((ActionsSection)adapter.getItem(4)).title);

        // section 3
        Assert.assertEquals(3, adapter.addSection("40"));
        Assert.assertEquals(6, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.SECTION_VIEW_TYPE, adapter.getItemViewType(5));
        Assert.assertFalse(adapter.isEnabled(5));
        Assert.assertEquals("40", ((ActionsSection)adapter.getItem(5)).title);
        
        // section 3: action 0        
        added = adapter.addAction(3, -1, "215");
        Assert.assertEquals(3, added.section.id);
        Assert.assertEquals(7, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(6));
        Assert.assertTrue(adapter.isEnabled(6));
        Assert.assertEquals("215", ((ActionItem)adapter.getItem(6)).title);
        
        // section 3: action 1        
        added = adapter.addAction(3, -1, "19567");
        Assert.assertEquals(3, added.section.id);
        Assert.assertEquals(8, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(7));
        Assert.assertTrue(adapter.isEnabled(7));
        Assert.assertEquals("19567", ((ActionItem)adapter.getItem(7)).title);        
        
        // section 1: action 0 
        added = adapter.addAction(1, -1, "2064");
        Assert.assertEquals(1, added.section.id);
        Assert.assertEquals(9, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(4));
        Assert.assertTrue(adapter.isEnabled(4));
        Assert.assertEquals("2064", ((ActionItem)adapter.getItem(4)).title);
        
        // section 1: action 1
        added = adapter.addAction(1, -1, "3095");
        Assert.assertEquals(1, added.section.id);
        Assert.assertEquals(10, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(5));
        Assert.assertTrue(adapter.isEnabled(5));
        Assert.assertEquals("3095", ((ActionItem)adapter.getItem(5)).title);
        
        // section 1: action 2
        added = adapter.addAction(1, -1, "32700");
        Assert.assertEquals(1, added.section.id);
        Assert.assertEquals(11, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(6));
        Assert.assertTrue(adapter.isEnabled(6));
        Assert.assertEquals("32700", ((ActionItem)adapter.getItem(6)).title);
        
        // is section 0 here
        Assert.assertEquals("128", ((ActionsSection)adapter.getItem(0)).title);
        
        // is section 1 here
        Assert.assertEquals("19", ((ActionsSection)adapter.getItem(3)).title);
        
        // is section 2 here
        Assert.assertEquals("1378", ((ActionsSection)adapter.getItem(7)).title);
        
        // section 2: action 0
        added = adapter.addAction(2, -1, "-218");
        Assert.assertEquals(2, added.section.id);
        Assert.assertEquals(12, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(8));
        Assert.assertTrue(adapter.isEnabled(8));
        Assert.assertEquals("-218", ((ActionItem)adapter.getItem(8)).title);
        
        // is section 3 here
        Assert.assertEquals("40", ((ActionsSection)adapter.getItem(9)).title);
        
        // section 4
        Assert.assertEquals(4, adapter.addSection("120"));
        Assert.assertEquals(13, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.SECTION_VIEW_TYPE, adapter.getItemViewType(12));
        Assert.assertFalse(adapter.isEnabled(12));
        Assert.assertEquals("120", ((ActionsSection)adapter.getItem(12)).title);        
        
        // section 4: action 0
        added = adapter.addAction(4, -1, "315");
        Assert.assertEquals(4, added.section.id);
        Assert.assertEquals(14, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(13));
        Assert.assertTrue(adapter.isEnabled(13));
        Assert.assertEquals("315", ((ActionItem)adapter.getItem(13)).title);
        
        // section 2: action 1
        added = adapter.addAction(2, -1, "917");
        Assert.assertEquals(2, added.section.id);
        Assert.assertEquals(15, adapter.getCount());
        Assert.assertEquals(SectionedActionsAdapter.ITEM_VIEW_TYPE, adapter.getItemViewType(9));
        Assert.assertTrue(adapter.isEnabled(9));
        Assert.assertEquals("917", ((ActionItem)adapter.getItem(9)).title);
        
        // is section 4 here
        Assert.assertEquals("120", ((ActionsSection)adapter.getItem(13)).title);
        
        // is section 4: action 0 here
        Assert.assertEquals("315", ((ActionItem)adapter.getItem(14)).title);
        
    }
    
}
