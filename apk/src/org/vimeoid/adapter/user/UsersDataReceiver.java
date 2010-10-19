/**
 * 
 */
package org.vimeoid.adapter.user;

import android.widget.AdapterView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.user</dd>
 * </dl>
 *
 * <code>UsersDataReceiver</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 19, 2010 8:16:56 PM 
 *
 */
public interface UsersDataReceiver {
    
    public void gotPersonalInfo(AdapterView<?> holder, int position, String location, 
                                long videosCount, long contactsCount);

}
