/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>ApiTaskInQueue</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 1, 2010 9:25:33 PM 
 *
 */
public class ApiTaskInQueue extends ApiTask {

    private final int taskId;
    private ApiTaskInQueue nextTask = null;
    private final SuccessiveApiTasksSupport performer;
    
    public ApiTaskInQueue(SuccessiveApiTasksSupport performer, int taskId, String apiMethod) {
        super(apiMethod);
        this.taskId = taskId;
        this.performer = performer;
    }
    
    @Override
    protected void onPostExecute(JSONObject jsonObj) {
        super.onPostExecute(jsonObj);            
        if (nextTask != null) performer.execute(nextTask);
        else performer.finish();
    }
    
    @Override
    public void onAnswerReceived(JSONObject jsonObj) throws JSONException {
        performer.onPerfomed(taskId, jsonObj);
    }
    
    public int getId() { return taskId; }
    
    public void setNextTask(ApiTaskInQueue nextTask) {
        if (this.nextTask != null) throw new IllegalStateException("Next task is already set");
        this.nextTask = nextTask;
    }    
    
}
