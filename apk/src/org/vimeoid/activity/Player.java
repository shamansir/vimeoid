package org.vimeoid.activity;

import java.io.FileInputStream;

import org.vimeoid.R;
import org.vimeoid.media.VimeoVideoPlayingTask;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

public class Player extends Activity {
    
    public static final String TAG = "PlayerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final long videoId = getIntent().getLongExtra(Invoke.VIDEO_ID_EXTRA, -1);
		if (videoId == -1) throw new IllegalStateException("Video ID must be passed to player");
		
		final View loadingView = getLayoutInflater().inflate(R.layout.video_loading, null);
		final View playerView = getLayoutInflater().inflate(R.layout.player, null);
		//final ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		final VideoView videoView = (VideoView) playerView.findViewById(R.id.canvas);
		//switcher.showNext();
		
		Log.d(TAG, "Running video player for video " + videoId);
		
		new VimeoVideoPlayingTask(this, videoView.getHolder()) {
			
			protected void onPreExecute() {
				setContentView(loadingView);
				super.onPreExecute();
			};
			
			protected void onPostExecute(FileInputStream dataSource) {
				//getWindow().setFormat(PixelFormat.TRANSPARENT);
				setContentView(playerView);
				videoView.requestFocus();
				super.onPostExecute(dataSource);                
			};
			
			protected void onNoSpaceForVideoCache(final long required, final long actual) {
				runOnUiThread(new Runnable() {					
					@Override public void run() {
						Dialogs.makeLongToast(Player.this, Utils.format(getString(R.string.no_space_for_video_cache), 
			                       						   "required", String.valueOf(required >> 10),
			                       						   "actual", String.valueOf(actual >> 10)));
						
					}
				}); 
				finish();				
			};
			
		}.execute(videoId);
		
	}
	
	@Override
	protected void onStop() {
	    super.onStop();
	    
	    VimeoVideoPlayingTask.ensureCleanedUp();
	}
	
}
