/**
 * 
 */
package org.vimeoid.activity.guest;

import org.vimeoid.util.SimpleItem;

import android.database.Cursor;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.guest</dd>
 * </dl>
 *
 * <code>ItemsExtractor</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 3, 2010 11:06:49 PM 
 *
 */
public interface ItemsExtractor<ItemType extends SimpleItem> {

    public ItemType fromCursor(Cursor cursor, int position);
    
}
