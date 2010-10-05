/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.activity.base.ApiPagesReceiver;

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
public class ListApiTaskInQueue extends ListApiTask implements IApiTaskWithNextTask {

    private final int taskId;
    private IApiTaskWithNextTask nextTask = null;
    private final SuccessiveApiTasksSupport performer;
    
    public ListApiTaskInQueue(SuccessiveApiTasksSupport performer, int taskId, String apiMethod, ApiPagesReceiver<JSONObject> receiver) {
        super(apiMethod, receiver);
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
    public void onAnswerReceived(JSONObject jsonObj) {
        super.onAnswerReceived(jsonObj);
        try {
            performer.onPerfomed(taskId, jsonObj);
        } catch (JSONException jsone) {
            onException(getParams(), jsone);
        }
    }
    
    public int getId() { return taskId; }
    
    public void setNextTask(IApiTaskWithNextTask nextTask) {
        if (this.nextTask != null) throw new IllegalStateException("Next task is already set");
        this.nextTask = nextTask;
    }

    @Override protected void onAnyError(Exception e, String message) {
        super.onAnyError(e, message);
        performer.onError(e, message);
    }    
    
}
