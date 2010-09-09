/**
 * 
 */
package org.vimeoid.connection;

import android.database.AbstractCursor;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.connection</dd>
 * </dl>
 *
 * <code>StatsCollectingCursor</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 8, 2010 10:36:39 PM 
 *
 */
public abstract class StatsCollectingCursor extends AbstractCursor {

    private final ApiCallInfo callStats;
    
    protected StatsCollectingCursor(ApiCallInfo callStats) {
        this.callStats = callStats;
    }
    
    public ApiCallInfo getCallStats() {
        return callStats;
    }
    
}
