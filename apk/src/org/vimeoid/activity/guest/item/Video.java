/**
 * 
 */
package org.vimeoid.activity.guest.item;

import org.vimeoid.R;
import org.vimeoid.connection.ApiCallInfo;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.simple.VimeoProvider;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.guest.item</dd>
 * </dl>
 *
 * <code>Video</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 10, 2010 12:15:38 AM 
 *
 */
public class Video extends Activity {
    
    public static final String TAG = "Video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.view_single_video);
        
        final Uri contentUri = getIntent().getData();
        ApiCallInfo callInfo = VimeoProvider.collectCallInfo(contentUri);
        setTitle(VimeoProvider.getCallDescription(callInfo));
        
        final long videoId = Long.valueOf(callInfo.subject);
        final int playerHeight = getResources().getDimensionPixelSize((R.dimen.video_player_height));
        WebView playerView = (WebView)findViewById(R.id.videoPlayer);
        playerView.getSettings().setJavaScriptEnabled(true);
        playerView.getSettings().setLoadsImagesAutomatically(true);
        
        //playerView.setHttpAuthUsernamePassword(host, realm, username, password)
        //playerView.addJavascriptInterface(obj, interfaceName)
        
        playerView.setWebChromeClient(new WebChromeClient() {           
            @Override public void onProgressChanged(WebView view, int newProgress) {
                // TODO: show progress bar
                super.onProgressChanged(view, newProgress);
            }            
        });
        
        /* playerView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        }); */ 
        
        Log.d(TAG, "Loading player: " + VimeoApi.getPlayerUrl(videoId, playerHeight));
        playerView.loadUrl(VimeoApi.getPlayerUrl(videoId, playerHeight));
    }
    
}
