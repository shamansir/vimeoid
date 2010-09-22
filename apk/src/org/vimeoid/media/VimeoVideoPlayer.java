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
 * <a href="http://www.androidapps.org/android-tutorial-3-custom-media-streaming-with-mediaplayer">this article about streaming media</a></p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 21, 2010 9:20:36 PM 
 *
 */
public final class VimeoVideoPlayer {
    
    public static final String TAG = "VimeoVideoPlayer";
    
    private static final int MIN_BUFFER_TO_PASS_IN_KB = 128; // assume 4096kbps*16secs/8bits per byte == 
    													            // == 4096 * 16  / 8 ==
    														        // == 4096 << 4 >> 3 ==
                                                                    // == 4096 << 1
    private static final int CHUNK_BUFFER_SIZE =  16384;
    
    private Context context;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder canvas;
    
    private final Handler handler = new Handler(); // UI Handler
    private File cacheFile;    
    
    private int kBytesRead = 0;
	
    private static int counter = 0;    

    private VimeoVideoPlayer() { }
    
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
        return this;
    }

    public static VimeoVideoPlayer use(Context context) {
      return SingletonHolder.INSTANCE.withContext(context);
    }        
    
    public void startPlaying(final SurfaceHolder canvas, final long videoId) {
    	
    	this.canvas = canvas;
    	//whereToProject.getHolder().setFixedSize(400, 300);
    	
        /* Runnable r = new Runnable() {

            @Override
            public void run() {
                try {            
                	Log.d(TAG, "Let's get stream");
                    final InputStream videoStream = VimeoVideoStreamer.getVideoStream(videoId);
                    if (videoStream == null) {
                        Log.e(TAG, "The returned video stream is null, so I will not play anything :(");
                        return;
                    }
                    Log.d(TAG, "Starting to manage the stream");
                    manageReceivedStream(videoStream);
                } catch (VideoLinkRequestException vlre) {
                    informException(vlre);
                } catch (IOException ioe) {
                    informException(ioe);
                }
            }
            
        };
        new Thread(r).start(); */
    	
        try {            
        	Log.d(TAG, "Let's get stream");
            final InputStream videoStream = VimeoVideoStreamer.getVideoStream(videoId);
            if (videoStream == null) {
                Log.e(TAG, "The returned video stream is null, so I will not play anything :(");
                return;
            }
            
            Log.d(TAG, "Creating player");
			mediaPlayer = new MediaPlayer();
			if (mediaPlayer == null) throw new IllegalStateException("Failed to create media player");
			mediaPlayer.setDisplay(canvas);            
            
    		Runnable updater = new Runnable() {
    			public void run() {
    				try {
    		            Log.d(TAG, "Starting the media thread");
	    				File temp = Utils.newTempFile(context, "mediaplayertmp", "dat");
	    				//String tempPath = temp.getAbsolutePath();
	    				FileOutputStream out = new FileOutputStream(temp);
	    				Log.d(TAG, "Created stream to temp file");
	    				byte buf[] = new byte[128];
	    				Log.d(TAG, "Now we will read video stream");
	    				do {
	    					int numread = videoStream.read(buf);
	    					if (numread <= 0) break;
	    					out.write(buf, 0, numread);
	    				} while (true);
	    				Log.d(TAG, "Stream is written to the output, setting data source");
	    				mediaPlayer.setDataSource(new FileInputStream(temp).getFD());
	    				try {
	    					videoStream.close();
	    					Log.d(TAG, "Video stream closed");
	    				} catch (IOException ex) {
	    					Log.e(TAG, "error: " + ex.getMessage(), ex);
	    				}
	    				mediaPlayer.prepare();
	    				Log.d(TAG, "Starting player!");
	    				mediaPlayer.start();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		};
    		//handler.post(updater);
    		new Thread(updater).start();
    		
        } catch (VideoLinkRequestException vlre) {
            informException(vlre);
        }
        
    }
    
    // used article: 
    // http://www.androidapps.org/android-tutorial-3-custom-media-streaming-with-mediaplayer
    private void manageReceivedStream(InputStream videoStream) throws IOException {
    	
    	Log.d(TAG, "creating cache file");
    	
        cacheFile = Utils.newTempFile(context, "v_videoCache", ".dat");
        FileOutputStream out = new FileOutputStream(cacheFile);
        
        byte byteBuf[] = new byte[CHUNK_BUFFER_SIZE];
        int bytesRead = 0;
        kBytesRead = 0;
        
        do {
            int streamGave = videoStream.read(byteBuf);
            if (streamGave <= 0) break;
            out.write(byteBuf, 0, streamGave);
            bytesRead += streamGave;
            kBytesRead = bytesRead >> 10;
        	Log.d(TAG, "new chunk is written, may be pass it to player?");            
            desideIfPassBufferToPlayer();
        } while (true);
        
        whenStreamFinished();
        videoStream.close();
    }

    private void whenStreamFinished() {
        // TODO Auto-generated method stub
        
    }

    private void desideIfPassBufferToPlayer() {
		Runnable updater = new Runnable() {
			public void run() {
				if (mediaPlayer == null) {
					Log.d(TAG, "player is not started, collecting cache");
					if (kBytesRead >= MIN_BUFFER_TO_PASS_IN_KB) {
						Log.d(TAG, "required buffer received trying to start player");
						try {
							startMediaPlayer();
						} catch (Exception e) {
							Log.e(getClass().getName(),
									"Error copying buffered conent.", e);
						}
					}
				} else if (mediaPlayer.getDuration()
						   - mediaPlayer.getCurrentPosition() <= 1000) {
					// NOTE: The media player has stopped at the end so transfer
					// any existing buffered data
					// We test for < 1second of data because the media player
					// can stop when there is still
					// a few milliseconds of data left to play
					passBufferToPlayer();
				}
			}
		};
		handler.post(updater);
        
    }

    private void informException(Exception exception) {
        //Dialogs.makeToast(context, exception.getLocalizedMessage());
        Log.e(TAG, exception.getLocalizedMessage());
        exception.printStackTrace();
    }
    
	private void startMediaPlayer() {
		try {
			Log.d(TAG, "starting player");			
			File bufferedFile = new File(context.getCacheDir(), "playingMedia"
					+ (counter++) + ".dat");
			Utils.moveFile(cacheFile, bufferedFile);
			
			Log.d(TAG, "cache file moved to buffered");

			Log.e(TAG, bufferedFile.length() + "");
			Log.e(TAG, bufferedFile.getAbsolutePath());

			mediaPlayer = new MediaPlayer();
			if (mediaPlayer == null) throw new IllegalStateException("Failed to create media player");
			mediaPlayer.setDataSource(new FileInputStream(bufferedFile).getFD());
			mediaPlayer.setDisplay(canvas);
			//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepare();
			mediaPlayer.start();
			
			Log.d(TAG, "player started ok");

		} catch (IOException e) {
			Log.e(getClass().getName(), "Error initializing the MediaPlaer.", e);
			return;
		}
	}
	
	private void passBufferToPlayer() {
		try {
			
			Log.d(TAG, "passing buffer to player");
			
			// First determine if we need to restart the player after
			// transferring data...e.g. perhaps the user pressed pause
			boolean wasPlaying = mediaPlayer.isPlaying();
			int curPosition = mediaPlayer.getCurrentPosition();
			mediaPlayer.pause();
			mediaPlayer.release();

			File bufferedFile = new File(context.getCacheDir(), "playingMedia"
					+ (counter++) + ".dat");
			// FileUtils.copyFile(downloadingMediaFile,bufferedFile);

			Log.d(TAG, "released previous player, creating new");
			
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(new FileInputStream(bufferedFile).getFD());
			// mediaPlayer.setAudioStreamType(AudioSystem.STREAM_MUSIC);
			mediaPlayer.setDisplay(canvas);			
			mediaPlayer.prepare();
			mediaPlayer.seekTo(curPosition);

			// Restart if at end of prior beuffered content or mediaPlayer was
			// previously playing.
			// NOTE: We test for < 1second of data because the media player can
			// stop when there is still
			// a few milliseconds of data left to play
			boolean atEndOfFile = mediaPlayer.getDuration()
					              - mediaPlayer.getCurrentPosition() <= 1000;
			if (wasPlaying || atEndOfFile) {
				Log.d(TAG, "we've played something, starting again");
				mediaPlayer.start();
			}
		} catch (Exception e) {
			Log.e(getClass().getName(),
					"Error updating to newly loaded content.", e);
		}
	}
	
    
    
}