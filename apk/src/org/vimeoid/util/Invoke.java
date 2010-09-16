/**
 * 
 */
package org.vimeoid.util;

import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.dto.simple.Video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.util</dd>
 * </dl>
 *
 * <code>Invoke</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 16, 2010 10:48:03 PM 
 *
 */
public final class Invoke {
    
    public static final String TAG = "Invoke";

    public static class Guest {
    
        protected static Uri getUploaderPageUri(Video video) {
            final String authorId = Utils.authorIdFromProfileUrl(video.uploaderProfileUrl);
            Log.d(TAG, "Extracted authorId " + authorId + " from profile URL " + video.uploaderProfileUrl);
            
            return Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + authorId + "/info");
        }
        
        protected static Uri getVideoPageUri(Video video) {
            return Uri.withAppendedPath(VimeoProvider.BASE_URI, "/video/" + video.id);
        }
        
        public static void pickUploader(Activity parent, Video video) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, getUploaderPageUri(video))
                                    .putExtra(Utils.USERNAME_EXTRA, video.uploaderName));
        }
        
        public static void pickVideo(Activity parent, Video video) {
            Log.d(TAG, "Video with id " + video.id + " requested");
            parent.setResult(Activity.RESULT_OK, new Intent().setData(getVideoPageUri(video))
                                                             .putExtra(Utils.VIDEO_TITLE_EXTRA, video.title));
        }
        
        public static void selectVideo(Activity parent, Video video) {
            Log.d(TAG, "Video with id " + video.id + " selected");      
            parent.startActivity(new Intent(Intent.ACTION_VIEW, getVideoPageUri(video))
                                    .putExtra(Utils.VIDEO_TITLE_EXTRA, video.title));
        }    
    
    }
    
}
