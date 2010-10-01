/**
 * 
 */
package org.vimeoid.activity.user;

import java.util.HashMap;
import java.util.Map;

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
    }
    
    @Override
    public void execute(ApiTaskInQueue task) {
        task.execute(tasksParams.get(task.getId()));
    }
    
    public boolean isEmpty() {
        return (firstTask == null);
    }

}
