/**
 * 
 */
package org.vimeoid.adapter.user;

import java.util.Set;

import org.vimeoid.dto.advanced.SubscriptionData.SubscriptionType;

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
    public void gotChannelsCount(AdapterView<?> holder, int position, long channelsCount);
    public void gotAlbumsCount(AdapterView<?> holder, int position, long albumsCount);
    public void gotSubsrcriptions(AdapterView<?> holder, int position, Set<SubscriptionType> types);

}
