/**
 * 
 */
package org.vimeoid.activity.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.util.ApiParams;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>ApiTasksQueue</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 1, 2010 10:22:29 PM 
 *
 */
public abstract class ApiTasksQueue implements SuccessiveApiTasksSupport {

    private ApiTaskInQueue firstTask = null;
    private ApiTaskInQueue lastTask = null;
    private Map<Integer, ApiParams> tasksParams = null;
    private int currentTask = -1;
    private boolean running = false;
    
    @Override
    public void add(int taskId, String apiMethod, ApiParams params) {
        final ApiTaskInQueue newTask = new ApiTaskInQueue(this, taskId, apiMethod); 
        if (isEmpty()) {
            firstTask = newTask;
            lastTask = newTask;
            tasksParams = new HashMap<Integer, ApiParams>();
        } else {
            lastTask.setNextTask(newTask);
            lastTask = newTask;
        }
        tasksParams.put(taskId, params);
    }

    @Override
    public void run() {
        if (!isEmpty()) execute(firstTask);
        else throw new IllegalStateException("Queue is already running");
    }
    
    @Override
    public void onPerfomed(int taskId, JSONObject result) throws JSONException {
        if (taskId != currentTask) throw new IllegalStateException("Tasks queue desynchronized");
        running = false;
    }
    
    @Override
    public void execute(ApiTaskInQueue task) {
        if (running) throw new IllegalStateException("Tasks queue desynchronized");
        currentTask = task.getId();
        running = true;
        task.execute(tasksParams.get(task.getId()));
    }
    
    public void finish() {
        firstTask = null;
        lastTask = null;
        tasksParams = null;
        currentTask = -1;
        running = false;
    }
    
    public boolean isEmpty() {
        return (firstTask == null);
    }

}
