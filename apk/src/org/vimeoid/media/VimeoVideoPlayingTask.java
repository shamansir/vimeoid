/**
 * 
 */
package org.vimeoid.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.vimeoid.R;
import org.vimeoid.connection.FailedToGetVideoStreamException;
import org.vimeoid.connection.VideoStreamRequestException;
import org.vimeoid.connection.VimeoVideoStreamer;
import org.vimeoid.util.Utils;
import org.vimeoid.util.Utils.VideoQuality;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.media</dd>
 * </dl>
 *
 * <code>VimeoVideoPlayingTask</code>
 *
 * <p>Description</p>
 * 
 * <p>used  
 * <a href="http://ballardhack.wordpress.com/2010/04/25/android-audio-streaming/">this article about streaming media</a></p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 21, 2010 9:20:36 PM 
 *
 */
public class VimeoVideoPlayingTask extends AsyncTask<Long, Long, FileInputStream> implements SurfaceHolder.Callback {
	
	public static final String TAG = "VimeoVideoPlayingTask";
    
    public static final String CACHE_DIR_NAME = "___v_video_player";
    public static final String STREAM_FILE_NAME = "___v_video_streamed";
    
    private static final int COPY_CHUNK_SIZE =  4 << 10; // 4 kBytes
    
    private static MediaPlayer mediaPlayer;
    
    private static File cacheDir;
    private final SurfaceHolder canvas;
    
    public VimeoVideoPlayingTask(Context context, SurfaceHolder canvas) { 
        if (cacheDir == null) cacheDir = Utils.createCacheDir(context, CACHE_DIR_NAME);
        this.canvas = canvas;
        this.canvas.addCallback(this);
        this.canvas.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.canvas.setFixedSize(R.dimen.video_view_width, R.dimen.video_view_height);        
        ensureCleanedUp();
    }
    
    @Override
    protected void onPreExecute() {
    	super.onPreExecute();
    	
    	Log.d(TAG, "Creating player");
    	
		mediaPlayer = new MediaPlayer();
		if (mediaPlayer == null) throw new IllegalStateException("Failed to create media player");
		 	
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setScreenOnWhilePlaying(true);
		 
		mediaPlayer.setOnErrorListener(new OnErrorListener() {
		    @Override
		 	public boolean onError(MediaPlayer mp, int what, int extra) {
			 	Log.e(TAG, "Media player error: " + what + "/" + extra);
			 	return false;
		 	}
		});    	
    }

	@Override
	protected FileInputStream doInBackground(Long... params) {
		if ((params == null) || (params.length == 0)) throw new IllegalStateException("No params were specified, please specify one!");
		if (params.length > 1) throw new IllegalStateException("I can play only one video at a time.");
		
		final long videoId = params[0];
		
		try {
		    InputStream videoStream = null;
			try {
			    videoStream = VimeoVideoStreamer.getVideoStream(videoId, VideoQuality.MOBILE);
			} catch (FailedToGetVideoStreamException ftgvse) {
			    videoStream = VimeoVideoStreamer.getVideoStream(videoId, VideoQuality.SD);
			}
			
			if (videoStream == null) {
				Log.e(TAG, "The returned video stream is null, so I will not play anything :(");
				return null;
			}
			
			ensureWeHaveEnoughSpace(VimeoVideoStreamer.getLastContentLength());
			
			Log.d(TAG, "Starting the media thread");
			
			final File streamFile = File.createTempFile(STREAM_FILE_NAME, ".dat", cacheDir);
			streamFile.deleteOnExit();

			final FileOutputStream out = new FileOutputStream(streamFile);
			byte buf[] = new byte[COPY_CHUNK_SIZE];
				 
			Log.i(TAG, "Now we will write stream to file");
				 
			do {
				int numread = videoStream.read(buf);
				if (numread <= 0) break;
				out.write(buf, 0, numread);
			} while (true);
			
			Log.i(TAG, "File is ready");
			
			videoStream.close();
			Log.d(TAG, "Video stream closed");
			 
			return new FileInputStream(streamFile);
		} catch (NoSpaceForVideoCacheException nsfvce) {
			//onException(nsfvce);
			onNoSpaceForVideoCache(nsfvce.getRequiredSpace(), nsfvce.getActualSpace());
		} catch (FailedToGetVideoStreamException ftgvse) {
		    onFailedToGetVideoStream();
		} catch (VideoStreamRequestException vle) { onException(vle); }
	      catch (IOException ioe) { onException(ioe); }
	      /* finally {
	    	  videoStream.close();
	      } */
	      
	    return null;
	}
	
	@Override
	protected void onPostExecute(FileInputStream dataSource) {
		try {
			if (dataSource == null) {
				Log.e(TAG, "No dataSource was passed to player");
				return;
			}
			
		    mediaPlayer.setDisplay(canvas);
			mediaPlayer.setDataSource(dataSource.getFD());		
			dataSource.close();
			mediaPlayer.setScreenOnWhilePlaying(true);
			mediaPlayer.prepare();
			Log.i(TAG, "Starting player! Duration: " + Utils.adaptDuration(mediaPlayer.getDuration() / 1000));
			mediaPlayer.start();
		} catch (IOException ioe) { onException(ioe); }		
	}
	
    public static void ensureCleanedUp() {
        if (mediaPlayer != null) {        	
            //if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.reset();
            Log.i(TAG, "Called reset for previous instance");
            mediaPlayer = null;
        }        
        for (File file: cacheDir.listFiles()) if (file.isFile() && file.exists()) file.delete();
        Log.i(TAG, "Cleared player cache");
    }
    
    private void informException(Exception exception) {
		 //Dialogs.makeToast(context, exception.getLocalizedMessage());
	     Log.e(TAG, exception.getLocalizedMessage());
	     exception.printStackTrace();
	}
	
    private void ensureWeHaveEnoughSpace(long expectedSpace) throws NoSpaceForVideoCacheException {
        final long spaceLeft = Utils.computeFreeSpace();
		Log.i(TAG, "Checking free space | required " + expectedSpace + " : left " + spaceLeft);
		if (expectedSpace > (spaceLeft * 0.8)) throw new NoSpaceForVideoCacheException(expectedSpace, spaceLeft);
	}
    
	protected void onException(Exception e) { informException(e); }
	
	protected void onNoSpaceForVideoCache(final long required, final long actual) { }
	
	protected void onFailedToGetVideoStream() { }
	
    @SuppressWarnings("serial")
    public static final class NoSpaceForVideoCacheException extends IOException {
        
        private final long requiredSpace;
        private final long actualSpace;
        
        public NoSpaceForVideoCacheException(long requiredSpace, long actualSpace) {
            this.requiredSpace = requiredSpace;
            this.actualSpace = actualSpace;
        }
        
        public long getRequiredSpace() {
            return requiredSpace;
        }
        
        public long getActualSpace() {
            return actualSpace;
        }        
        
    }

    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        // TODO Auto-generated method stub
        
    }

    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mediaPlayer == null) {
            Log.w(TAG, "Media Player was null when surface already created");
            return;
        }
        mediaPlayer.setDisplay(canvas);
    }

    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ensureCleanedUp();
    }

}
