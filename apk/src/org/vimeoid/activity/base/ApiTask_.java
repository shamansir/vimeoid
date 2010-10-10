/**
 * 
 */
package org.vimeoid.activity.base;

import android.os.AsyncTask;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.base</dd>
 * </dl>
 *
 * <code>ApiTask_</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 4, 2010 7:12:37 PM 
 *
 */
public abstract class ApiTask_<Params, Result> extends AsyncTask<Params, Void, Result> {
    
    protected void onAnyError(Exception e, String message) {};
    protected abstract void onAnswerReceived(Result result) throws Exception;
    // protected void ensureConnected(); // TODO: use this method
    
    protected Params prepareParams(Params... params) {
        if (params.length <= 0) return null;
        if (params.length > 1) throw new UnsupportedOperationException("This task do not supports several params");
        return params[0];
    }    
    
}
