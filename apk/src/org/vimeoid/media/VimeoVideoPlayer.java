/**
 * 
 */
package org.vimeoid.media;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
    public static final String CACHE_FILES_PREFIX = "___v_video_chunk";
    public static final String STREAM_FILE_NAME = "___v_video_streamed";
    
    private static final int MIN_FIRST_CHUNK_SIZE = 256 << 10; // 256 kBytes
    private static final int COPY_CHUNK_SIZE =  4 << 10; // 4 kBytes
    private static final int TRANSFER_CHUNK_SIZE =  16 << 10; // 16 kBytes
     
    private Context context;
    private static MediaPlayer mediaPlayer;
    private SurfaceHolder canvas;
    
    private final Handler handler; // UI Handler
    private File downloadingMediaFile;    
    private static File cacheDir;
    
    private static int totalKbRead = 0;
    private static int counter = 0; 

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
    	this.canvas = canvas;
    	
    	try {
    		
            final InputStream videoStream = VimeoVideoStreamer.getVideoStream(videoId);
			if (videoStream == null)
				throw new IllegalStateException("The stream I've got is null, so I will not play anything :(");
			
			final File streamFile = File.createTempFile(STREAM_FILE_NAME, ".dat", cacheDir);
			streamFile.deleteOnExit();
			
			Log.d(TAG, "Starting to copy stream in physical file");

			FileOutputStream out = new FileOutputStream(streamFile);
			byte buf[] = new byte[COPY_CHUNK_SIZE];
			do {
				int numread = videoStream.read(buf);
				if (numread <= 0)
					break;
				out.write(buf, 0, numread);
			} while (true);
			try {
				videoStream.close();
			} catch (IOException ex) {
				Log.e(TAG, "error: " + ex.getMessage(), ex);
			}
			
			Log.d(TAG, "Copying finished, starting player");
			
		    final Runnable fireStart = new Runnable() {

				@Override
				public void run() {					
					try {
						mediaPlayer = new MediaPlayer();
						mediaPlayer.setOnErrorListener(
					                new MediaPlayer.OnErrorListener() {
					                    public boolean onError(MediaPlayer mp, int what, int extra) {
					                        Log.e(getClass().getName(), "Error in MediaPlayer: (" + what +") with extra (" +extra +")" );
					                        return false;
					                    }
					                });
						
						final FileInputStream fis = new FileInputStream(streamFile);
					    mediaPlayer.setDataSource(fis.getFD());						
						mediaPlayer.prepare();
						
						Log.d(TAG, "Player prepared, musta run shortly");
												
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				     mediaPlayer.setDisplay(canvas);
				     mediaPlayer.start();
				     Log.d(TAG, "We've started!");
				}
				
			};
			handler.post(fireStart);			
			            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (VideoLinkRequestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
