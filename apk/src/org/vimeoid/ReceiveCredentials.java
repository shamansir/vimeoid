/**
 * 
 */
package org.vimeoid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import oauth.signpost.exception.OAuthException;

import org.vimeoid.connection.VimeoApiUtils;
import org.vimeoid.util.Dialogs;

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

    /* @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent); 
        Uri uri = intent.getData(); // came here from browser with OAuth */
    
    @Override
    protected void onResume() {
    	super.onResume(); 
        Uri uri = getIntent().getData(); // came here from browser with OAuth    
        Log.d(TAG, "Uri is " + uri);
        if (uri != null) {
            try {
                Log.d(TAG, "Got credentials from browser, checking and saving");                
                VimeoApiUtils.checkOAuthCallbackAndSaveToken(uri);
                Log.d(TAG, "Checking finished");
                Dialogs.makeToast(getApplicationContext(), "You are successfully authorized");
            } catch (OAuthException oae) {
                Log.e(TAG, oae.getLocalizedMessage());
                oae.printStackTrace();
                Dialogs.makeExceptionToast(getApplicationContext(), "OAuth Exception", oae);                
            }
        } else {
            Dialogs.makeToast(getApplicationContext(), "Failed to get OAuth token");
        }
    	
    }

}
