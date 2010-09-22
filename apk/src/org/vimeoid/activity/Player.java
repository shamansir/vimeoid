package org.vimeoid.activity;

import org.vimeoid.R;
import org.vimeoid.media.VimeoVideoPlayer;
import org.vimeoid.util.Invoke;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceView;

public class Player extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.player);
		
		getWindow().setFormat(PixelFormat.TRANSPARENT);
		
		long videoId = getIntent().getLongExtra(Invoke.VIDEO_ID_EXTRA, -1);
		if (videoId == -1) throw new IllegalStateException("Video ID must be passed to player");
		
		SurfaceView sufrace = (SurfaceView) findViewById(R.id.surface);
		
		VimeoVideoPlayer.use(this).startPlaying(sufrace.getHolder(), videoId);
	}
	
}
