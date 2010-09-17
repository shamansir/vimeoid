/**
 * 
 */
package org.vimeoid.util;

import org.vimeoid.R;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.dto.simple.User;
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
    
    public static final String SUBJ_TITLE_EXTRA = "v_subjtitle";
    public static final String ICON_EXTRA = "v_icon";

    public static class Guest {
    
        protected static Uri getUploaderPageUri(Video video) {
            return Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + getUploaderId(video) + "/info");
        }
        
        protected static String getUploaderId(Video video) {
            return Utils.authorIdFromProfileUrl(video.uploaderProfileUrl);
        }        
        
        protected static Uri getVideoPageUri(Video video) {
            return Uri.withAppendedPath(VimeoProvider.BASE_URI, "/video/" + video.id);
        }
        
        public static void selectVideosBy(Activity parent, User user) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
            		                Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + user.id + "/videos"))
                                    .putExtra(SUBJ_TITLE_EXTRA, user.displayName));
        }
        
        public static void selectVideosByUploader(Activity parent, Video video) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
            		                Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + getUploaderId(video) + "/videos"))
                                    .putExtra(SUBJ_TITLE_EXTRA, video.uploaderName)
                                    .putExtra(ICON_EXTRA, R.drawable.video));
        }        
        
        public static void selectUploader(Activity parent, Video video) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, getUploaderPageUri(video))
                                    .putExtra(SUBJ_TITLE_EXTRA, video.uploaderName));
        }
        
        public static void pickVideo(Activity parent, Video video) {
            Log.d(TAG, "Video with id " + video.id + " requested");
            parent.setResult(Activity.RESULT_OK, new Intent().setData(getVideoPageUri(video))
                                                             .putExtra(SUBJ_TITLE_EXTRA, video.title));
        }
        
        public static void selectVideo(Activity parent, Video video) {
            Log.d(TAG, "Video with id " + video.id + " selected");      
            parent.startActivity(new Intent(Intent.ACTION_VIEW, getVideoPageUri(video))
                                    .putExtra(SUBJ_TITLE_EXTRA, video.title));
        }

		public static void selectApperancesOf(Activity parent, User user) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
					   				 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + user.id + "/appears"))
					   				 .putExtra(SUBJ_TITLE_EXTRA, user.displayName)
					   				 .putExtra(ICON_EXTRA, R.drawable.appearance));
			
		}

		public static void selectLikesOf(Activity parent, User user) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
	                				 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + user.id + "/likes"))
                    				 .putExtra(SUBJ_TITLE_EXTRA, user.displayName)
                    				 .putExtra(ICON_EXTRA, R.drawable.like));
		}

		public static void selectSubsriptionsOf(Activity parent, User user) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
					   				 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + user.id + "/subscr"))
					   				 .putExtra(SUBJ_TITLE_EXTRA, user.displayName)
					   				 .putExtra(ICON_EXTRA, R.drawable.subscribe));			
		}    
    
    }
    
}
