/**
 * 
 */
package org.vimeoid.activity.user.item;

import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

import android.app.Activity;
import android.os.Bundle;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user.item</dd>
 * </dl>
 *
 * <code>UserActivity</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 24, 2010 11:05:50 PM 
 *
 */
public class UserActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Dialogs.makeLongToast(this, "You are " + 
                getIntent().getExtras().getLong(Invoke.Extras.USER_ID) + " : " + 
                getIntent().getExtras().getString(Invoke.Extras.USERNAME));
    }

}
