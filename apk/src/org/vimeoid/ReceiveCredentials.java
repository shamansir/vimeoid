/**
 * 
 */
package org.vimeoid;

import android.app.Activity;
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

    @Override
    protected void onResume() {
        Uri uri = this.getIntent().getData(); // came here from browser with OAuth
        if (uri != null) {
            try {
                VimeoApiUtils.checkOAuthCallbackAndSaveToken(uri);
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
