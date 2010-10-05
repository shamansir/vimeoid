/**
 * 
 */
package org.vimeoid.activity.user;

import org.vimeoid.util.ApiParams;

import android.os.AsyncTask;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>IApiTaskWithNextTask</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 5, 2010 10:37:48 PM 
 *
 */
public interface IApiTaskWithNextTask {

    public int getId();
    void setNextTask(IApiTaskWithNextTask task);
    public AsyncTask<?, ?, ?> execute(ApiParams... apiParams);

}
