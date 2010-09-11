/**
 * 
 */
package org.vimeoid.activity.guest.item;

import org.vimeoid.R;
import org.vimeoid.connection.ApiCallInfo;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.util.Utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


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
        
        ((ImageView)findViewById(R.id.subjectIcon)).setImageResource(Utils.drawableByContent(callInfo.subjectType));
        ((TextView)findViewById(R.id.subjectTitle)).setText(callInfo.subject); // FIXME: set to Title
        ((ImageView)findViewById(R.id.resultIcon)).setImageResource(R.drawable.info);
        
        final View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        
        WebView playerView = (WebView)findViewById(R.id.videoPlayer);
        playerView.getSettings().setJavaScriptEnabled(true);
        playerView.getSettings().setLoadsImagesAutomatically(true);
        
        final long videoId = Long.valueOf(callInfo.subject);
        final int playerHeight = getResources().getDimensionPixelSize((R.dimen.video_player_height));
        
        playerView.setWebChromeClient(new WebChromeClient() {           
            @Override public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(((newProgress == 0) || (newProgress == 100)) ? View.GONE : View.VISIBLE);
                super.onProgressChanged(view, newProgress);
            }
        });        
        
        Log.d(TAG, "Loading player: " + VimeoApi.getPlayerUrl(videoId, playerHeight));
        playerView.loadUrl(VimeoApi.getPlayerUrl(videoId, playerHeight));
    }
    
}
