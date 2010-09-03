/**
 * 
 */
package org.vimeoid;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Utils;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>ReceiveCredentials</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 31, 2010 9:04:48 PM 
 *
 */
public class ReceiveCredentials extends Activity {
    
    public static final String TAG = "ReceiveCredentials";
    
    @Override
    protected void onResume() {
    	super.onResume(); 
        Uri uri = getIntent().getData(); // came here from browser with OAuth    
        Log.d(TAG, "Uri is " + uri);
        if (uri != null) {
            try {
                Log.d(TAG, "Got credentials from browser, checking and saving");
                // TODO: use AbstractAccountAuthenticator (see SampleSyncAdapter) to store credentials 
                VimeoApi.ensureOAuthCallbackAndSaveToken(this, uri);
                Log.d(TAG, "Checking finished");
                
                VimeoApi.executeAdvApiCall(Methods.activity.happedToUser, 
                                                Utils.quickApiParams("user_id", "shamansir"));
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
                Dialogs.makeExceptionToast(getApplicationContext(), "Failure: ", e);                
            }
        } else {
            Dialogs.makeToast(getApplicationContext(), "Failed to get OAuth token");
        }
    }
    
}
