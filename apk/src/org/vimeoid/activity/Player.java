package org.vimeoid.activity;

import org.vimeoid.R;
import org.vimeoid.media.VimeoVideoPlayer;
import org.vimeoid.media.VimeoVideoPlayer.CachingMonitor;
import org.vimeoid.media.VimeoVideoPlayer.NoSpaceForVideoCacheException;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Utils;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

public class Player extends Activity {
    
    public static final String TAG = "PlayerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.player);
		
		getWindow().setFormat(PixelFormat.TRANSPARENT);
		
		final long videoId = getIntent().getLongExtra(Invoke.VIDEO_ID_EXTRA, -1);
		if (videoId == -1) throw new IllegalStateException("Video ID must be passed to player");
		
		final View progressView = findViewById(R.id.progressHolder);
		final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
		
		VimeoVideoPlayer.setCachingMonitor(new CachingMonitor() {
            
            @Override
            public void beforeStreamRequest() {
                progressView.setVisibility(View.VISIBLE);
                surfaceView.setVisibility(View.GONE);
            }
		    
            @Override
            public void whenStartedCaching(long cacheSize) { }
            
            @Override
            public void whenFinishedCaching() {
                surfaceView.setVisibility(View.VISIBLE);                
                progressView.setVisibility(View.GONE);
            }

        });
		
		Log.d(TAG, "Running video player for video " + videoId);
		
		try {
            VimeoVideoPlayer.use(Player.this).startPlaying(surfaceView.getHolder(), videoId);
        } catch (NoSpaceForVideoCacheException nsfvce) {
            Dialogs.makeLongToast(this, Utils.format(getString(R.string.no_space_for_video_cache), 
                                                     "required", String.valueOf(nsfvce.getRequiredSpace() >> 10),
                                                     "actual", String.valueOf(nsfvce.getActualSpace() >> 10)));
            finish();
        }		                

	}
	
	@Override
	protected void onStop() {
	    super.onStop();
	    
	    VimeoVideoPlayer.ensureCleanedUp(this);
	}
	
}
