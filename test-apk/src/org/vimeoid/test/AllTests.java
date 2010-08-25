/**
 * 
 */
package org.vimeoid.test;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid-test</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.test</dd>
 * </dl>
 *
 * <code>AllTests</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 25, 2010 11:19:41 PM 
 *
 */
public class AllTests  extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
    }
}
