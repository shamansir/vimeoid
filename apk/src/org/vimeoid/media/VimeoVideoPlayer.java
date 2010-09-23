/**
 * 
 */
package org.vimeoid.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.vimeoid.connection.VideoLinkRequestException;
import org.vimeoid.connection.VimeoVideoStreamer;
import org.vimeoid.util.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.media</dd>
 * </dl>
 *
 * <code>VimeoVideoPlayer</code>
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
public final class VimeoVideoPlayer {
    
    public static final String TAG = "VimeoVideoPlayer";
    
    public static final String CACHE_DIR_NAME = "___v_video_player";
    //public static final String CACHE_FILES_PREFIX = "___v_video_chunk";
    public static final String STREAM_FILE_NAME = "___v_video_streamed";
    
    //private static final int MIN_FIRST_CHUNK_SIZE = 256 << 10; // 256 kBytes
    private static final int COPY_CHUNK_SIZE =  4 << 10; // 4 kBytes
    //private static final int TRANSFER_CHUNK_SIZE =  16 << 10; // 16 kBytes
     
    @SuppressWarnings("unused")
	private Context context;
    private static MediaPlayer mediaPlayer;
    //private SurfaceHolder canvas;
    
	private final Handler handler; // UI Handler
    private static File cacheDir;
    
    private VimeoVideoPlayer() { 
        this.handler = new Handler();
    }
    
    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
     * or the first access to SingletonHolder.INSTANCE, not before.
     * 
     * See: http://android-developers.blogspot.com/2010/07/how-to-have-your-cupcake-and-eat-it-too.html
     */
    private static class SingletonHolder { 
      private static final VimeoVideoPlayer INSTANCE = new VimeoVideoPlayer();
    }
    
    private VimeoVideoPlayer withContext(Context context) {
        this.context = context;
        if (cacheDir == null) cacheDir = Utils.createCacheDir(context, CACHE_DIR_NAME);        
        ensureCleanedUp(context);
        return this;
    }
    
    public static VimeoVideoPlayer use(Context context) {
      return SingletonHolder.INSTANCE.withContext(context);
    }
    
    public static void ensureCleanedUp(Context context) {
        for (File file: cacheDir.listFiles()) if (file.isFile() && file.exists()) file.delete();
        if (mediaPlayer != null) {
        	mediaPlayer.release();
        	mediaPlayer = null;
        }
    }
    
    public void startPlaying(final SurfaceHolder canvas, final long videoId) {
    	try {
    		 Log.d(TAG, "Let's get the stream");
    		 
    		 final InputStream videoStream = VimeoVideoStreamer.getVideoStream(videoId);
    		 if (videoStream == null) {
    			 Log.e(TAG, "The returned video stream is null, so I will not play anything :(");
    			 return;
    		 }
    		 
    		 ensureWeHaveEnoughSpace(VimeoVideoStreamer.getReceivedStreamLength());
    		 
    		 Log.d(TAG, "Creating player");
    		 mediaPlayer = new MediaPlayer();
    		 if (mediaPlayer == null) throw new IllegalStateException("Failed to create media player");
    		 
    		 mediaPlayer.setDisplay(canvas);
    		 mediaPlayer.setAudioStreamType(2);
    		 
    		 mediaPlayer.setOnErrorListener(new OnErrorListener() {
    			 @Override
    			 	public boolean onError(MediaPlayer mp, int what, int extra) {
    				 	Log.e(TAG, "Media player error: " + what + "/" + extra);
    				 	return false;
    			 	}
    		 });
    		 
    		 Runnable saveAndRun = new Runnable() {
    			 public void run() {
    				 try {
    					 
    					 Log.d(TAG, "Starting the media thread");
    					 File streamFile = File.createTempFile(STREAM_FILE_NAME, ".dat", cacheDir);
    					 streamFile.deleteOnExit();
    		 
    					 FileOutputStream out = new FileOutputStream(streamFile);
    					 byte buf[] = new byte[COPY_CHUNK_SIZE];
    					 
    					 Log.d(TAG, "Now we will write stream to file");
    					 
    					 do {
    						 int numread = videoStream.read(buf);
    						 if (numread <= 0) break;
    						 out.write(buf, 0, numread);
    					 } while (true);
    		 
    					 Log.d(TAG, "Stream is written to the file, setting data source");
    					 mediaPlayer.setDataSource(new FileInputStream(streamFile).getFD());
    		 
    					 try {
    						 videoStream.close();
    						 Log.d(TAG, "Video stream closed");
    					 } catch (IOException ex) {
    						 Log.e(TAG, "error: " + ex.getMessage(), ex);
    					 }
    					 
    					 letPlayerStart();
    					 
    				 } catch (IllegalArgumentException iae) {
    					 informException(iae);
    				 } catch (SecurityException se) {
    					 informException(se);
    				 } catch (IllegalStateException ise) {
    					 informException(ise);
    				 } catch (IOException ioe) {
    					 informException(ioe);
    				 }
    			 }
    		 
    		 };
    		 
    	     new Thread(saveAndRun).start();    		 
    	} catch (VideoLinkRequestException vlre) {
    		 informException(vlre); 
        }
    	
    }
    
	private void letPlayerStart() {
		 final Runnable playThread = new Runnable() {
			@Override
			public void run() {
				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException ise) {
					informException(ise);
				} catch (IOException ioe) {
					informException(ioe);
				}
				Log.d(TAG, "Starting player! Duration: " + Utils.adaptDuration(mediaPlayer.getDuration() / 1000));
				mediaPlayer.start();
				
			}
		 };
		 handler.post(playThread);
    }
    	
 	private void informException(Exception exception) {
 		 //Dialogs.makeToast(context, exception.getLocalizedMessage());
 	     Log.e(TAG, exception.getLocalizedMessage());
 	     exception.printStackTrace();
 	}
 	
    private void ensureWeHaveEnoughSpace(long receivedStreamLength) {
		// TODO: implement (~50kbps)
	}

}
