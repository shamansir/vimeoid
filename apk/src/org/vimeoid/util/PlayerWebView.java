/**
 * 
 */
package org.vimeoid.util;

import org.vimeoid.R;
import org.vimeoid.activity.base.SingleItemActivity_;
import org.vimeoid.connection.VimeoApi;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ListView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.util</dd>
 * </dl>
 *
 * <code>PlayerWebView</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 9, 2010 9:02:07 PM 
 *
 */
public class PlayerWebView {
    
    public static final String TAG = "PlayerWebView";
    
    public static WebView projectPlayer(final long videoId, final SingleItemActivity_<?> parent) {
        WebView playerView = (WebView)parent.findViewById(R.id.videoPlayer);
        
        playerView.getSettings().setJavaScriptEnabled(true);
        //playerView.getSettings().setLoadsImagesAutomatically(true);
        playerView.getSettings().setUserAgentString(VimeoApi.WEBVIEW_USER_AGENT);
        
        final int playerHeight = parent.getResources().getDimensionPixelSize((R.dimen.vimeo_player_height));        
        
        playerView.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view, int newProgress) {
                parent.setProgressBarVisibile((newProgress != 0) && (newProgress != 100));
                if (newProgress == 100) ((ListView)parent.findViewById(R.id.actionsList)).setSelectionAfterHeaderView();
                super.onProgressChanged(view, newProgress);
            }
        });
        
        Log.d(TAG, "Loading player: " + VimeoApi.getPlayerUrl(videoId, playerHeight));
        playerView.loadUrl(VimeoApi.getPlayerUrl(videoId, playerHeight));
        
        return playerView;
    }

}
