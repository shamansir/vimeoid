/**
 * 
 */
package org.vimeoid.util;

import android.content.Context;
import android.widget.Toast;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.util</dd>
 * </dl>
 *
 * <code>Dialogs</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 31, 2010 10:12:27 PM 
 *
 */
public class Dialogs {
    
    public static final int TOAST_KEEPS_HOT = 40000;    
    
    public static void makeToast(Context context, String description) {
        Toast t = Toast.makeText(context, description, TOAST_KEEPS_HOT);
        t.show();
    }
    
    public static void makeExceptionToast(Context context, String description, Exception e) {
        makeToast(context, description + " " + e.getLocalizedMessage());
    }

}
