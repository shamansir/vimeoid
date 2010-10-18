/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.activity.base.ApiPagesReceiver;
import org.vimeoid.activity.base.ListApiTask_.Judge;
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
public interface SuccessiveApiTasksSupport {

    public ApiTask add(int taskId, String apiMethod, ApiParams params);
    public ListApiTask addListTask(int taskId, String apiMethod, ApiParams params, ApiPagesReceiver<JSONObject> receiver);
    public ListApiTask addListTask(int taskId, String apiMethod, ApiParams params, ApiPagesReceiver<JSONObject> receiver, Judge<JSONObject> filter);
    
    public void onPerfomed(int taskId, JSONObject result) throws JSONException;    
    public void run(); // means run first task
    public void finish(); // means all tasks done    
    public void execute(IApiTaskWithNextTask nextTask) throws Exception;
    public void onError(Exception e, String message);
    
    public boolean started();
    public boolean running();
    
}
