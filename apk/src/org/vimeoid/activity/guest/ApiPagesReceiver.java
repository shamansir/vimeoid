/**
 * 
 */
package org.vimeoid.activity.guest;

import android.database.Cursor;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>ApiPagesReceiver</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 3, 2010 12:33:58 PM 
 *
 */
public interface ApiPagesReceiver {
    
    public void addPage(Cursor cursor);
    //public PagingData getLastPagingData() throws JSONException;
    public int getCount();

}
