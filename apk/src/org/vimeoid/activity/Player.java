package org.vimeoid.activity;

import java.io.FileInputStream;

import org.vimeoid.R;
import org.vimeoid.media.VimeoVideoPlayingTask;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

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
		
		final VideoView videoView = (VideoView) findViewById(R.id.canvas);
		
		Log.d(TAG, "Running video player for video " + videoId);
		
		new VimeoVideoPlayingTask(this, videoView.getHolder()) {
		    
		    private ProgressDialog progressDialog;
			
			@Override protected void onPreExecute() {
			    progressDialog = ProgressDialog.show(Player.this, "", getString(R.string.caching_video), true);
				super.onPreExecute();
			};
			
			@Override protected void onPostExecute(FileInputStream dataSource) {
			    progressDialog.dismiss();
				super.onPostExecute(dataSource);                
			};
			
			@Override protected void onNoSpaceForVideoCache(final long required, final long actual) {
				runOnUiThread(new Runnable() {					
					@Override public void run() {
						Dialogs.makeLongToast(Player.this, getString(R.string.no_space_for_video_cache, 
			                       						             required >> 10, actual >> 10));
						
					}
				}); 
				finish();				
			};
			
			@Override protected void onFailedToGetVideoStream() {
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
