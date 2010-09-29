/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.util.ApiParams;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>SecondaryTasksSupport</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 27, 2010 10:52:27 PM 
 *
 */
public interface SecondaryTasksSupport {

    public void addSecondaryTask(int taskId, String apiMethod, ApiParams params);
    public void onSecondaryTaskPerfomed(int taskId, JSONObject result) throws JSONException;    
    public void runSecondaryTasks();    
    
}
