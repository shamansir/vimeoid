/**
 * 
 */
package org.vimeoid.util;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>ApplicationContext</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 29, 2010 12:00:01 AM 
 *
 */
public final class ApplicationContext {

    public static final int TOAST_KEEPS_HOT = 10000;
    
    private ApplicationContext() {
        
    }
    
    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
     * or the first access to SingletonHolder.INSTANCE, not before.
     * 
     * See: http://android-developers.blogspot.com/2010/07/how-to-have-your-cupcake-and-eat-it-too.html
     */
    private static class SingletonHolder { 
      private static final ApplicationContext INSTANCE = new ApplicationContext();
    }

    public static ApplicationContext get() {
      return SingletonHolder.INSTANCE;
    }
    
}
