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
    private static final int TRANSFER_CHUNK_SIZE =  16 << 10; // 16 kBytes
     
    //private Context context;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder canvas;
    
    private final Handler handler; // UI Handler
    private File downloadingMedia;    
    private static File cacheDir;
    
    private static int chunk = 0;    

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
        ensureCleanedUp(context);
        return this;
    }
    
    public static VimeoVideoPlayer use(Context context) {
      return SingletonHolder.INSTANCE.withContext(context);
    }
    
    public static void ensureCleanedUp(Context context) {
        if (cacheDir == null) cacheDir = Utils.createCacheDir(context, CACHE_DIR_NAME);
        for (File file: cacheDir.listFiles()) if (file.isFile() && file.exists()) file.delete();        
    }
    
    public void startPlaying(final SurfaceHolder canvas, final long videoId) {
    	
    	this.canvas = canvas;
    	//whereToProject.getHolder().setFixedSize(400, 300);
    	
        Runnable r = new Runnable() {

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
                    weGotStream(videoStream);
                } catch (VideoLinkRequestException vlre) {
                    informException(vlre);
                } catch (IOException ioe) {
                    informException(ioe);
                }
            }
            
        };
        new Thread(r).start();
    }
    
    private static MediaPlayer createMediaPlayer(File mediaFile) throws IOException {
        MediaPlayer mPlayer = new MediaPlayer();
        if (mPlayer == null) throw new IllegalStateException("Failed to create media player");
        mPlayer.setOnErrorListener(
                new MediaPlayer.OnErrorListener() {
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.e(TAG, "Error in MediaPlayer: (" + what +") with extra (" +extra +")" );
                        return false;
                    }
                });

        //  It appears that for security/permission reasons, it is better to pass a FileDescriptor rather than a direct path to the File.
        //  Also I have seen errors such as "PVMFErrNotSupported" and "Prepare failed.: status=0x1" if a file path String is passed to
        //  setDataSource().  So unless otherwise noted, we use a FileDescriptor here.
        FileInputStream fis = new FileInputStream(mediaFile);      
        mPlayer.setDataSource(fis.getFD());
        return mPlayer;     
    }
    
    private void weGotStream(InputStream videoStream) throws IOException {
    	
    	Log.d(TAG, "Creating cache file");
    	
        downloadingMedia = File.createTempFile(STREAM_FILE_NAME, ".dat", cacheDir);
        
        if (downloadingMedia.exists()) {
            downloadingMedia.delete();
        }
        
        FileOutputStream out = new FileOutputStream(downloadingMedia);
        
        Log.d(TAG, "Starting to read stream into the cache");
        
        byte byteBuf[] = new byte[TRANSFER_CHUNK_SIZE];
        int bytesRead = 0;
        
        do {
            int streamGave = videoStream.read(byteBuf);
            if (streamGave <= 0) break;
            out.write(byteBuf, 0, streamGave);
            bytesRead += streamGave;
            
            if (mediaPlayer == null) { // if not created and first chunk is ready, start
                if (bytesRead >= MIN_FIRST_CHUNK_SIZE) {
                    startMediaPlayer();
                }
            } else if (mediaPlayer.getDuration() -
                       mediaPlayer.getCurrentPosition() <= 1000) {
                 // player reached the last seconds of current chunk
                passNewChunkToPlayer();
            }
        } while (true);
        
        videoStream.close();
    }

	private void startMediaPlayer() {
		try {
			Log.d(TAG, "Starting new player");
			
			final File firstBuffer = File.createTempFile(CACHE_FILES_PREFIX + (chunk++), ".dat", cacheDir);
			
            // We double buffer the data to avoid potential read/write errors that could happen if the 
            // download thread attempted to write at the same time the MediaPlayer was trying to read.
            // For example, we can't guarantee that the MediaPlayer won't open a file for playing and leave it locked while 
            // the media is playing.  This would permanently deadlock the file download.  To avoid such a deadloack, 
            // we move the currently loaded data to a temporary buffer file that we start playing while the remaining 
            // data downloads.  			
			Utils.copyFile(downloadingMedia, firstBuffer);
			
			Log.d(TAG, "Downloading file is copied to first buffer ok ");

			Log.d(TAG, "buffer size:" + firstBuffer.length());
			Log.d(TAG, "buffering to: " + firstBuffer.getAbsolutePath());

			// Run on UI thread
			final Runnable startPlayer = new Runnable() {

                @Override
                public void run() {
                    try {
                        mediaPlayer = createMediaPlayer(firstBuffer);
                    
                        mediaPlayer.setDisplay(canvas);                    
                        // We have pre-loaded enough content
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        Log.d(TAG, "Starting player! Duration: " + Utils.adaptDuration(mediaPlayer.getDuration() / 1000));
                        
                        Log.d(TAG, "Player started ok");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
			    
			};
			
			handler.post(startPlayer);
		} catch (IOException ioe) {
			Log.e(TAG, "Error initializing the MediaPlayer.", ioe);
			informException(ioe);
			return;
		}
	}
	
	private void passNewChunkToPlayer() {
		try {
            // First determine if we need to restart the player after transferring data...e.g. perhaps the user pressed pause
            final boolean wasPlaying = mediaPlayer.isPlaying();
            final int curPosition = mediaPlayer.getCurrentPosition();
            
            // Copy the currently downloaded content to a new buffered File.  Store the old File for deleting later. 
            final File previousBuffer = File.createTempFile(CACHE_FILES_PREFIX + chunk, ".dat", cacheDir);
            final File currentBuffer = File.createTempFile(CACHE_FILES_PREFIX + (chunk++), ".dat", cacheDir);
            
            //  This may be the last buffered File so ask that it be delete on exit.  If it's already deleted, then this won't mean anything.  If you want to 
            // keep and track fully downloaded files for later use, write caching code and please send me a copy.
            currentBuffer.deleteOnExit();   
            Utils.copyFile(downloadingMedia, currentBuffer);

            final Runnable reloadPlayer = new Runnable() {

                @Override
                public void run() {
                    try {                    
                        // Pause the current player now as we are about to create and start a new one.  So far (Android v1.5),
                        // this always happens so quickly that the user never realized we've stopped the player and started a new one
                        mediaPlayer.pause();
    
                        // Create a new MediaPlayer rather than try to re-prepare the prior one.
                        mediaPlayer = createMediaPlayer(currentBuffer);
                        mediaPlayer.setDisplay(canvas);
                        mediaPlayer.prepare();
    
                        mediaPlayer.seekTo(curPosition);
                        
                        //  Restart if at end of prior buffered content or mediaPlayer was previously playing.  
                        //  NOTE:  We test for < 1second of data because the media player can stop when there is still
                        //  a few milliseconds of data left to play
                        boolean atEndOfFile = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 1000;
                        if (wasPlaying || atEndOfFile){
                            mediaPlayer.start();
                        }
    
                        // Lastly delete the previously playing buffered File as it's no longer needed.
                        previousBuffer.delete();
                    } catch (IOException ioe) {
                        informException(ioe);
                    }                    
                }
                
            };
            
            handler.post(reloadPlayer);
            
		} catch (IOException ioe) {
			Log.e(TAG, "Error updating to newly loaded content.", ioe);
			informException(ioe);
		}
	}
	
    private void informException(Exception exception) {
        //Dialogs.makeToast(context, exception.getLocalizedMessage());
        Log.e(TAG, exception.getLocalizedMessage());
        exception.printStackTrace();
    }
    
    /* try {            
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
        mediaPlayer.setAudioStreamType(2);
        mediaPlayer.setOnErrorListener(new OnErrorListener() {
            
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "Media player error: " + what + "/" + extra);
                return false;
            }
        });
        
        Runnable updater = new Runnable() {
            public void run() {
                try {
                    Log.d(TAG, "Starting the media thread");
                    File temp = Utils.newTempFile(context, "mediaplayertmp", "dat");
                    temp.deleteOnExit();
                    //String tempPath = temp.getAbsolutePath();
                    FileOutputStream out = new FileOutputStream(temp);
                    Log.d(TAG, "Created stream to temp file");
                    byte buf[] = new byte[CHUNK_BUFFER_SIZE];
                    Log.d(TAG, "Now we will read video stream");
                    do {
                        int numread = videoStream.read(buf);
                        if (numread <= 0) break;
                        out.write(buf, 0, numread);
                    } while (true);
                    //out.close();
                    Log.d(TAG, "Stream is written to the output, setting data source");
                    mediaPlayer.setDataSource(new FileInputStream(temp).getFD());
                    try {
                        videoStream.close();
                        Log.d(TAG, "Video stream closed");
                    } catch (IOException ex) {
                        Log.e(TAG, "error: " + ex.getMessage(), ex);
                    }
                    mediaPlayer.prepare();
                    Log.d(TAG, "Starting player! Duration: " + Utils.adaptDuration(mediaPlayer.getDuration() / 1000));
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
        //updater.run();
        //handler.post(updater);
        new Thread(updater).start();
        
    } catch (VideoLinkRequestException vlre) {
        informException(vlre);
    } */    
    
}
