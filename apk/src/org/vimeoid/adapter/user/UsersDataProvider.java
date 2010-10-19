/**
 * 
 */
package org.vimeoid.adapter.user;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.user</dd>
 * </dl>
 *
 * <code>UsersDataProvider</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 17, 2010 12:03:05 AM 
 *
 */
public interface UsersDataProvider {
    
    public void requestData(int position, long userId, UsersDataReceiver receiver);

}
