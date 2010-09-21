/**
 * 
 */
package org.vimeoid.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.vimeoid.connection.VideoLinkRequestException;
import org.vimeoid.connection.VimeoVideoStreamer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

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
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 21, 2010 9:20:36 PM 
 *
 */
public final class VimeoVideoPlayer {
    
    public static final String TAG = "VimeoVideoPlayer";
    
    private Context context;
    private MediaPlayer mediaPlayer;

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
    
    public void startPlaying(final org.vimeoid.dto.simple.Video video) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {            
                    final InputStream videoStream = VimeoVideoStreamer.getVideoStream(video);
                    if (videoStream == null) {
                        Log.e(TAG, "The returned video stream is null, so I will not play anything :(");
                        return;
                    }
                    manageReceivedStream(videoStream);
                } catch (VideoLinkRequestException vlre) {
                    informException(vlre);
                } catch (IOException ioe) {
                    informException(ioe);
                }
            }
            
        };
        new Thread(r).start();
        
    }
    
    // used article: 
    // http://www.androidapps.org/android-tutorial-3-custom-media-streaming-with-mediaplayer
    private void manageReceivedStream(InputStream videoStream) throws IOException {
        File cacheFile = File.createTempFile("v_videoCache", ".dat");
        FileOutputStream out = new FileOutputStream(cacheFile);
        
        byte byteBuf[] = new byte[16384 * 1024];
        int bytesRead = 0;
        int kBytesRead = 0;
        
        do {
            int streamGave = videoStream.read(byteBuf);
            if (streamGave <= 0) break;
            out.write(byteBuf, 0, streamGave);
            bytesRead += streamGave;
            kBytesRead = kBytesRead >> 10;
            mayBePassBuffer();
        } while (true);
        
        whenStreamFinished();
        videoStream.close();
    }

    /**
     * 
     */
    private void whenStreamFinished() {
        // TODO Auto-generated method stub
        
    }

    private void mayBePassBuffer() {
        // TODO Auto-generated method stub
        
    }

    private void informException(Exception exception) {
        //Dialogs.makeToast(context, exception.getLocalizedMessage());
        Log.e(TAG, exception.getLocalizedMessage());
        exception.printStackTrace();
    }
    
}

/**
public class StreamingMediaPlayer {

private static final int INTIAL_KB_BUFFER =  96*10/8;//assume 96kbps*10secs/8bits per byte

private TextView textStreamed;

private ImageButton playButton;

private ProgressBar progressBar;

//  Track for display by progressBar
private long mediaLengthInKb, mediaLengthInSeconds;
private int totalKbRead = 0;

// Create Handler to call View updates on the main UI thread.
private final Handler handler = new Handler();

private MediaPlayer     mediaPlayer;

private File downloadingMediaFile; 

private boolean isInterrupted;

private Context context;

private int counter = 0;

public StreamingMediaPlayer(Context  context,TextView textStreamed, ImageButton playButton, Button  streamButton,ProgressBar    progressBar) 
{
    this.context = context;
    this.textStreamed = textStreamed;
    this.playButton = playButton;
    this.progressBar = progressBar;
}
  
// Progressivly download the media to a temporary location and update the MediaPlayer as new content becomes available.
public void startStreaming(final String mediaUrl, long  mediaLengthInKb, long   mediaLengthInSeconds) throws IOException {
    
    this.mediaLengthInKb = mediaLengthInKb;
    this.mediaLengthInSeconds = mediaLengthInSeconds;
    
    Runnable r = new Runnable() {   
        public void run() {   
            try {   
                downloadAudioIncrement(mediaUrl);
            } catch (IOException e) {
                Log.e(getClass().getName(), "Unable to initialize the MediaPlayer for fileUrl=" + mediaUrl, e);
                return;
            }   
        }   
    };   
    new Thread(r).start();
}

// Download the url stream to a temporary location and then call the setDataSource  
// for that local file  
public void downloadAudioIncrement(String mediaUrl) throws IOException {
    
    URLConnection cn = new URL(mediaUrl).openConnection();   
    cn.connect();   
    InputStream stream = cn.getInputStream();
    if (stream == null) {
        Log.e(getClass().getName(), "Unable to create InputStream for mediaUrl:" + mediaUrl);
    }
    
    downloadingMediaFile = new File(context.getCacheDir(),"downloadingMedia_" + (counter++) + ".dat");
    FileOutputStream out = new FileOutputStream(downloadingMediaFile);   
    byte buf[] = new byte[16384];
    int totalBytesRead = 0, incrementalBytesRead = 0;
    do {
        int numread = stream.read(buf);   
        if (numread <= 0)   
            break;   
        out.write(buf, 0, numread);
        totalBytesRead += numread;
        incrementalBytesRead += numread;
        totalKbRead = totalBytesRead/1000;
        
        testMediaBuffer();
        fireDataLoadUpdate();
    } while (validateNotInterrupted());   

    stream.close();
    if (validateNotInterrupted()) {
        fireDataFullyLoaded();
    }
}  

private boolean validateNotInterrupted() {
    if (isInterrupted) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            //mediaPlayer.release();
        }
        return false;
    } else {
        return true;
    }
}


// Test whether we need to transfer buffered data to the MediaPlayer.
// Interacting with MediaPlayer on non-main UI thread can causes crashes to so perform this using a Handler.
private void  testMediaBuffer() {
    Runnable updater = new Runnable() {
        public void run() {
            if (mediaPlayer == null) {
                //  Only create the MediaPlayer once we have the minimum buffered data
                if ( totalKbRead >= INTIAL_KB_BUFFER) {
                    try {
                        startMediaPlayer();
                    } catch (Exception e) {
                        Log.e(getClass().getName(), "Error copying buffered conent.", e);               
                    }
                }
            } else if ( mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 1000 ){ 
                //  NOTE:  The media player has stopped at the end so transfer any existing buffered data
                //  We test for < 1second of data because the media player can stop when there is still
                //  a few milliseconds of data left to play
                transferBufferToMediaPlayer();
            }
        }
    };
    handler.post(updater);
}

private void startMediaPlayer() {
    try {   
        File bufferedFile = new File(context.getCacheDir(),"playingMedia" + (counter++) + ".dat");
        moveFile(downloadingMediaFile,bufferedFile);
        
        Log.e("Player",bufferedFile.length()+"");
        Log.e("Player",bufferedFile.getAbsolutePath());
        
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(bufferedFile.getAbsolutePath());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepare();
        fireDataPreloadComplete();
        
    } catch (IOException e) {
        Log.e(getClass().getName(), "Error initializing the MediaPlaer.", e);
        return;
    }   
}

// Transfer buffered data to the MediaPlayer.
// Interacting with MediaPlayer on non-main UI thread can causes crashes to so perform this using a Handler.
private void transferBufferToMediaPlayer() {
    try {
        // First determine if we need to restart the player after transferring data...e.g. perhaps the user pressed pause
        boolean wasPlaying = mediaPlayer.isPlaying();
        int curPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();

        File bufferedFile = new File(context.getCacheDir(),"playingMedia" + (counter++) + ".dat");
        //FileUtils.copyFile(downloadingMediaFile,bufferedFile);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(bufferedFile.getAbsolutePath());
        //mediaPlayer.setAudioStreamType(AudioSystem.STREAM_MUSIC);
        mediaPlayer.prepare();
        mediaPlayer.seekTo(curPosition);
        
        //  Restart if at end of prior beuffered content or mediaPlayer was previously playing.  
        //  NOTE:  We test for < 1second of data because the media player can stop when there is still
        //  a few milliseconds of data left to play
        boolean atEndOfFile = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 1000;
        if (wasPlaying || atEndOfFile){
            mediaPlayer.start();
        }
    }catch (Exception e) {
        Log.e(getClass().getName(), "Error updating to newly loaded content.", e);                  
    }
}

private void fireDataLoadUpdate() {
    Runnable updater = new Runnable() {
        public void run() {
            textStreamed.setText((CharSequence) (totalKbRead + " Kb read"));
            float loadProgress = ((float)totalKbRead/(float)mediaLengthInKb);
            progressBar.setSecondaryProgress((int)(loadProgress*100));
        }
    };
    handler.post(updater);
}

// We have preloaded enough content and started the MediaPlayer so update the buttons & progress meters.
private void fireDataPreloadComplete() {
    Runnable updater = new Runnable() {
        public void run() {
            mediaPlayer.start();
            startPlayProgressUpdater();
            playButton.setEnabled(true);
            //streamButton.setEnabled(false);
        }
    };
    handler.post(updater);
}

private void fireDataFullyLoaded() {
    Runnable updater = new Runnable() { 
        public void run() {
            transferBufferToMediaPlayer();
            textStreamed.setText((CharSequence) ("Audio full loaded: " + totalKbRead + " Kb read"));
        }
    };
    handler.post(updater);
}

public MediaPlayer getMediaPlayer() {
    return mediaPlayer;
}

public void startPlayProgressUpdater() {
    float progress = (((float)mediaPlayer.getCurrentPosition()/1000)/(float)mediaLengthInSeconds);
    progressBar.setProgress((int)(progress*100));
    
    if (mediaPlayer.isPlaying()) {
        Runnable notification = new Runnable() {
            public void run() {
                startPlayProgressUpdater();
            }
        };
        handler.postDelayed(notification,1000);
    }
}    

public void interrupt() {
    playButton.setEnabled(false);
    isInterrupted = true;
    validateNotInterrupted();
}

public void moveFile(File   oldLocation, File   newLocation)
throws IOException {

    if ( oldLocation.exists( )) {
        BufferedInputStream  reader = new BufferedInputStream( new FileInputStream(oldLocation) );
        BufferedOutputStream  writer = new BufferedOutputStream( new FileOutputStream(newLocation, false));
        try {
            byte[]  buff = new byte[8192];
            int numChars;
            while ( (numChars = reader.read(  buff, 0, buff.length ) ) != -1) {
                writer.write( buff, 0, numChars );
            }
        } catch( IOException ex ) {
            throw new IOException("IOException when transferring " + oldLocation.getPath() + " to " + newLocation.getPath());
        } finally {
            try {
                if ( reader != null ){
                    writer.close();
                    reader.close();
                }
            } catch( IOException ex ){
                Log.e(getClass().getName(),"Error closing files when transferring " + oldLocation.getPath() + " to " + newLocation.getPath() ); 
            }
        }
    } else {
        throw new IOException("Old location does not exist when transferring " + oldLocation.getPath() + " to " + newLocation.getPath() );
    }
}
}
*/