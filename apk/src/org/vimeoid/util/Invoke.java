/**
 * 
 */
package org.vimeoid.util;

import org.vimeoid.R;
import org.vimeoid.connection.VimeoVideoRunner;
import org.vimeoid.connection.VimeoVideoRunner.VideoLinkRequestException;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.dto.simple.Album;
import org.vimeoid.dto.simple.Channel;
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
    
        // Video
        
        protected static Uri getUploaderPageUri(Video video) {
            return Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + getUploaderId(video) + "/info");
        }
        
        protected static String getUploaderId(Video video) {
            return Utils.authorIdFromProfileUrl(video.uploaderProfileUrl);
        }        
        
        protected static Uri getVideoPageUri(Video video) {
            return Uri.withAppendedPath(VimeoProvider.BASE_URI, "/video/" + video.id);
        }
        
        public static void pickVideo(Activity parent, Video video) {
        	Log.d(TAG, "Trying to get direct url for video " + video.id);
        	try {
        		final Uri resultUri = VimeoVideoRunner.askForVideoUri(video);
				Log.d(TAG, (resultUri != null) ? resultUri.toString() : "null");
			} catch (VideoLinkRequestException e) {
				Log.e(TAG, e.getLocalizedMessage());
				e.printStackTrace();
			}        	
            Log.d(TAG, "Video with id " + video.id + " requested");
            parent.setResult(Activity.RESULT_OK, new Intent().setData(getVideoPageUri(video))
                                                             .putExtra(SUBJ_TITLE_EXTRA, video.title 
                                                              + " (" + video.uploaderName + ")"));
        }
        
        public static void selectVideo(Activity parent, Video video) {
        	Log.d(TAG, "Trying to get direct url for video " + video.id);
        	try {
        		final Uri resultUri = VimeoVideoRunner.askForVideoUri(video);
				Log.d(TAG, (resultUri != null) ? resultUri.toString() : "null");
			} catch (VideoLinkRequestException e) {
				Log.e(TAG, e.getLocalizedMessage());
				e.printStackTrace();
			}
            Log.d(TAG, "Video with id " + video.id + " selected");      
            parent.startActivity(new Intent(Intent.ACTION_VIEW, getVideoPageUri(video))
                                    .putExtra(SUBJ_TITLE_EXTRA, video.title
                                     + " (" + video.uploaderName + ")"));            
        }
        
        public static void selectVideosBy(Activity parent, User user) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                    Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + user.id + "/videos"))
                                    .putExtra(SUBJ_TITLE_EXTRA, user.displayName)
                                    .putExtra(ICON_EXTRA, R.drawable.video));
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
        
        // User

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
		
        public static void selectAlbumsOf(Activity parent, User user) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + user.id + "/albums"))
                                 .putExtra(SUBJ_TITLE_EXTRA, user.displayName)
                                 .putExtra(ICON_EXTRA, R.drawable.album));          
        }
        
        public static void selectChannelsOf(Activity parent, User user) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + user.id + "/channels"))
                                 .putExtra(SUBJ_TITLE_EXTRA, user.displayName)
                                 .putExtra(ICON_EXTRA, R.drawable.channel));          
        }        
        
        // Channel
        
        public static void pickChannel(Activity parent, Channel channel) {
            parent.setResult(Activity.RESULT_OK, new Intent()
                                        .setData(Uri.withAppendedPath(VimeoProvider.BASE_URI, "/channel/" + channel.id + "/info"))
                                        .putExtra(SUBJ_TITLE_EXTRA, channel.name));
        }
        
        public static void selectChannel(Activity parent, Channel channel) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/channel/" + channel.id + "/info"))
                                 .putExtra(SUBJ_TITLE_EXTRA, channel.name));          
        }
        
        public static void selectCreator(Activity parent, Channel channel) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                     Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + channel.creatorId + "/info"))
                                     .putExtra(SUBJ_TITLE_EXTRA, channel.creatorDisplayName));
        }        

        public static void selectChannelContent(Activity parent, Channel channel) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/channel/" + channel.id + "/videos"))
                                 .putExtra(SUBJ_TITLE_EXTRA, channel.name)
                                 .putExtra(ICON_EXTRA, R.drawable.video));            
        }
        
        public static void selectChannelContent(Activity parent, String channelName) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/channel/" + channelName + "/videos"))
                                 .putExtra(SUBJ_TITLE_EXTRA, channelName)
                                 .putExtra(ICON_EXTRA, R.drawable.video));
        }
        
        // Album
        
        public static void pickAlbum(Activity parent, Album album) {
            parent.setResult(Activity.RESULT_OK, new Intent()
                                        .setData(Uri.withAppendedPath(VimeoProvider.BASE_URI, "/album/" + album.id + "/info"))
                                        .putExtra(SUBJ_TITLE_EXTRA, album.title));
        }
        
        public static void selectAlbum(Activity parent, Album album) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/album/" + album.id + "/info"))
                                 .putExtra(SUBJ_TITLE_EXTRA, album.title));          
        }
        
        public static void selectCreator(Activity parent, Album album) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                     Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + album.creatorId + "/info"))
                                     .putExtra(SUBJ_TITLE_EXTRA, album.creatorDisplayName));
        }        

        public static void selectAlbumContent(Activity parent, Album album) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW, 
                                 Uri.withAppendedPath(VimeoProvider.BASE_URI, "/album/" + album.id + "/videos"))
                                 .putExtra(SUBJ_TITLE_EXTRA, album.title)
                                 .putExtra(ICON_EXTRA, R.drawable.video));            
        }
        
    }
    
}
