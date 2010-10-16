/**
 * 
 */
package org.vimeoid.adapter.user;

import java.util.Set;

import org.vimeoid.dto.advanced.SubscriptionData.SubscriptionType;

import android.view.View;

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
 * @date Oct 17, 2010 12:02:41 AM 
 *
 */
public interface UsersDataReceiver {
    
    public void gotVideosCount(View view, long userId, int videosCount);  
    public void gotAlbumsCount(View view, long userId, int videosCount);
    public void gotChannelsCount(View view, long userId, int videosCount);
    public void gotContactsCount(View view, long userId, int videosCount);
    public void gotLocation(View view, long userId, String location);
    public void gotSubscriptions(View view, long userId, Set<SubscriptionType> subscriptions);
    public void gotIsContact(View view, long userId, Boolean isContact); 
    public void gotMarkers(View view, long userId, Boolean isStaffMember, Boolean isPlusMember);

}
