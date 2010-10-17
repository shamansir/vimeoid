/**
 * 
 */
package org.vimeoid.adapter.user;

import org.vimeoid.dto.advanced.User;

import android.view.View;

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

    public void requestData(View view, int position, User user);

}
