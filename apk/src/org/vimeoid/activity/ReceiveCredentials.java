/**
 * 
 */
package org.vimeoid.activity;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.VimeoApi.AdvancedApiCallError;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

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
 * <p>The principle is the same that used here:
 * http://dev.bostone.us/2009/07/16/android-oauth-twitter-updates/
 * or in any another example of using <code>oauth-signpost</code> library for Android</p>
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
        if (uri != null) {
            try {
                Log.i(TAG, "Got credentials from browser, checking and saving");
                // TODO: use AbstractAccountAuthenticator (see SampleSyncAdapter) in 2.0> to store credentials
                //       and sync with Vimeo
                VimeoApi.ensureOAuthCallbackAndSaveToken(this, uri);
                Log.i(TAG, "Credential saved");
                
                JSONObject user = VimeoApi.advancedApi(Methods.test.login).getJSONObject("user");
                Log.d(TAG, "got user " + user.getString("id") + " / " + user.get("username"));
                Invoke.User_.showPersonalPage(this, user.getLong("id"), user.getString("username"));                
                
                /* VimeoApi.advancedApi(Methods.activity.happenedToUser, 
                                     Utils.quickApiParams("user_id", "shamansir"), "activity"); */
            } catch (AdvancedApiCallError aace) {
                VimeoApi.handleApiError(this, aace);
            } catch (JSONException jsone) {
                Log.e(TAG, "Error while getting user: " + jsone.getLocalizedMessage());
                jsone.printStackTrace();
                Dialogs.makeExceptionToast(this, "JSON Parsing Failure: ", jsone);                
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
                Dialogs.makeExceptionToast(this, "Failure: ", e);
            }
        } else {
            Dialogs.makeToast(this, "Failed to get OAuth token");
        }
    }
    
}
