/**
 * 
 */
package org.vimeoid.adapter.user;

import java.util.Set;

import org.vimeoid.dto.advanced.SubscriptionData.SubscriptionType;

import android.view.View;
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
 * @date Oct 17, 2010 12:02:41 AM 
 *
 */
public interface UsersDataReceiver {
    
    public void gotVideosCount(AdapterView<?> holder, View view, long userId, int videosCount);  
    public void gotAlbumsCount(AdapterView<?> holder, View view, long userId, int videosCount);
    public void gotChannelsCount(AdapterView<?> holder, View view, long userId, int videosCount);
    public void gotContactsCount(AdapterView<?> holder, View view, long userId, int videosCount);
    public void gotLocation(AdapterView<?> holder, View view, long userId, String location);
    public void gotSubscriptions(AdapterView<?> holder, View view, long userId, Set<SubscriptionType> subscriptions);
    public void gotIsContact(AdapterView<?> holder, View view, long userId, Boolean isContact); 
    public void gotMarkers(AdapterView<?> holder, View view, long userId, Boolean isStaffMember, Boolean isPlusMember);

}
