package org.vimeoid.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.vimeoid.R;

import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.util.Dialogs;

/**
 * 
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>Videos</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 3, 2010 11:58:57 PM 
 *
 */
public class Welcome extends Activity {
    
    public static final String TAG = "Welcome";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        Log.d(TAG, "Running Vimeo App");
        
        setContentView(R.layout.welcome);
        
        // TODO: if credentials already saved, just start User's profile (tryToEnterAsUser)
        
        final Button enterButton = (Button) findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {                
                tryToEnterAsUser();
            }
        });
        
        final Button guestButton = (Button) findViewById(R.id.guestButton);
        guestButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                enterAsGuest();
            }
            
        });
        
    }
    
    protected void tryToEnterAsUser() {
        
        if (!VimeoApi.connectedToWeb(this)) {
            Dialogs.makeToast(this, getString(R.string.no_iternet_connection));
            return;
        }
        
        if (!VimeoApi.vimeoSiteReachable(this)) {
            Dialogs.makeToast(this, getString(R.string.vimeo_not_reachable));
            return;
        }
        
        Dialogs.makeToast(this, "Say, you are logged in");
        
        // TODO: See guest.Videos login menu handler to Login
        
        /* Intent directIntent = new Intent(activity, BasicViewActivity.class);
           activity.start(directIntent); */        
        
    }
    
    protected void enterAsGuest() {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.withAppendedPath(VimeoProvider.BASE_URI, "/channel/staffpicks/videos"));
        startActivity(intent);        
    }
       
}