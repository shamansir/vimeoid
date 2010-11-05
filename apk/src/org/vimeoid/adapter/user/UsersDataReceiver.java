/**
 * 
 */
package org.vimeoid.adapter.user;

import org.vimeoid.dto.advanced.SubscriptionData;

import android.widget.ListView;

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
    
    public void gotPersonalInfo(ListView holder, int position, String location, 
                                long videosCount, long contactsCount);
    public void gotChannelsCount(ListView holder, int position, long channelsCount);
    public void gotAlbumsCount(ListView holder, int position, long albumsCount);
    public void gotSubscriptionData(ListView holder, SubscriptionData subscriptions);

}
