package org.vimeoid.activity;

import org.vimeoid.R;
import org.vimeoid.media.VimeoVideoPlayer;
import org.vimeoid.util.Invoke;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

public class Player extends Activity {
    
    public static final String TAG = "PlayerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.player);
		
		getWindow().setFormat(PixelFormat.TRANSPARENT);
		
		final long videoId = getIntent().getLongExtra(Invoke.VIDEO_ID_EXTRA, -1);
		if (videoId == -1) throw new IllegalStateException("Video ID must be passed to player");
		
		final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
		
		Log.d(TAG, "Running video player for video " + videoId);
		
		/*final Runnable r = new Runnable() {
		    
		    public void run() {*/
		        runOnUiThread(new Runnable() {
		            
		            public void run() {
		                VimeoVideoPlayer.use(Player.this).startPlaying(surfaceView.getHolder(), videoId);		                
		            }
		            
		        });
		    /*}
		};
		new Handler().post(r);*/
		
		Log.d(TAG, "We're back at the Player activity  " + videoId);
	}
	
}
