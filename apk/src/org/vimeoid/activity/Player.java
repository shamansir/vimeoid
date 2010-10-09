package org.vimeoid.activity;

import java.io.FileInputStream;

import org.vimeoid.R;
import org.vimeoid.media.VimeoVideoPlayingTask;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

public class Player extends Activity {
    
    public static final String TAG = "PlayerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.player);
        getWindow().setFormat(PixelFormat.TRANSPARENT);        

		final long videoId = getIntent().getLongExtra(Invoke.Extras.VIDEO_ID, -1);
		if (videoId == -1) throw new IllegalStateException("Video ID must be passed to player");
		
		//final ViewFlipper flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
		//final View loadingView = (View) findViewById(R.id.loadingProgress);
		//final View playerView = getLayoutInflater().inflate(R.layout.player, null);
		//final ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		final VideoView videoView = (VideoView) findViewById(R.id.canvas);
		//videoView.setVisibility(View.INVISIBLE);
		//switcher.showNext();
		
		Log.d(TAG, "Running video player for video " + videoId);
		
		new VimeoVideoPlayingTask(this, videoView.getHolder()) {
		    
		    private ProgressDialog progressDialog;
			
			protected void onPreExecute() {
				//setContentView(loadingView);
			    progressDialog = ProgressDialog.show(Player.this, "", getString(R.string.caching_video), true);
				super.onPreExecute();
			};
			
			protected void onPostExecute(FileInputStream dataSource) {
				//setContentView(playerView);
				//flipper.showNext();
                //loadingView.setVisibility(View.GONE);			    
				//videoView.setVisibility(View.VISIBLE);
                //videoView.bringToFront();
				//videoView.requestFocus();
			    progressDialog.dismiss();
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
			
			protected void onFailedToGetVideoStream() {
                runOnUiThread(new Runnable() {                  
                    @Override public void run() {
                        Dialogs.makeLongToast(Player.this, getString(R.string.failed_to_get_video_stream));
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
