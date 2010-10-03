/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.dto.advanced.PagingData;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>ApiObjectsReceiver</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 3, 2010 12:33:58 PM 
 *
 */
public interface ApiObjectsReceiver {
    
    public void addPage(JSONObject pageObj);
    public PagingData getLastPagingData() throws JSONException;

}
