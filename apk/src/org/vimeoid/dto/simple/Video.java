/**
 * 
 */
package org.vimeoid.dto.simple;

import org.vimeoid.util.SimpleItem;
import org.vimeoid.util.Utils;

import android.database.Cursor;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.simple</dd>
 * </dl>
 *
 * <code>Video video information</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 19, 2010 8:55:56 PM 
 *
 */
public class Video implements SimpleItem {
    
    /* public static final Uri CONTENT_URI = Uri.parse("content://" + VimeoProvider.AUTHORITY +
                                                                   "/videos"); */

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.vimeo.video";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.vimeo.video";    
    
    public long id;
    public String title;    
    public String url;
    public String description;
    
    public int width;
    public int height;
    public long duration; /* in seconds */
    
    public long likesCount;
    public long playsCount;
    public long commentsCount;
    
    public String[] tags;    

    public /*long*/ String uploadedOn;
    public String uploaderName;
    public String uploaderProfileUrl;
    
    public String smallThumbnailUrl;
    public String mediumThumbnailUrl;
    public String largeThumbnailUrl;
    
    public String smallUploaderPortraitUrl;
    public String mediumUploaderPortraitUrl;
    public String largeUploaderPortraitUrl;
    
    public final static class FieldsKeys {
        
        public static final String _ID = "_id";
        
        public static final String TITLE = "title";
        public static final String URL = "url";
        public static final String MOBILE_URL = "mobile_url";
        public static final String DESCRIPTION = "description";        
        public static final String UPLOADED_ON = "upload_date";
        public static final String TAGS = "tags";
        public static final String DURATION = "duration";
        
        public static final String AUTHOR = "user_name";
        public static final String AUTHOR_URL = "user_url";    
        
        public static final String THUMB_SMALL = "thumbnail_small";
        public static final String THUMB_MEDIUM = "thumbnail_medium";
        public static final String THUMB_LARGE = "thumbnail_large";
        
        public static final String USER_IMG_SMALL = "user_portrait_small";
        public static final String USER_IMG_MEDIUM = "user_portrait_medium";
        public static final String USER_IMG_LARGE = "user_portrait_large";
        
        public static final String NUM_OF_LIKES = "stats_number_of_likes";
        public static final String NUM_OF_PLAYS = "stats_number_of_plays";
        public static final String NUM_OF_COMMENTS = "stats_number_of_comments";
        
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";        
               
    }
    
    public final static String[] LIST_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.TITLE, FieldsKeys.AUTHOR, FieldsKeys.AUTHOR_URL,
        FieldsKeys.DURATION, FieldsKeys.TAGS,
        FieldsKeys.THUMB_SMALL, FieldsKeys.USER_IMG_SMALL, 
        FieldsKeys.NUM_OF_LIKES, FieldsKeys.NUM_OF_PLAYS, FieldsKeys.NUM_OF_COMMENTS
    };
    
    public final static String[] SINGLE_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.TITLE, FieldsKeys.AUTHOR, FieldsKeys.AUTHOR_URL,
        FieldsKeys.DURATION, FieldsKeys.TAGS, 
        FieldsKeys.THUMB_SMALL, FieldsKeys.USER_IMG_MEDIUM, 
        FieldsKeys.NUM_OF_LIKES, FieldsKeys.NUM_OF_PLAYS, FieldsKeys.NUM_OF_COMMENTS,
        FieldsKeys.DESCRIPTION, FieldsKeys.UPLOADED_ON, 
        FieldsKeys.WIDTH, FieldsKeys.HEIGHT
    };
    
    protected static Video generalDataFromCursor(Cursor cursor, int position) {
    	if (cursor.getPosition() != position) throw new IllegalStateException("Cursor must be properly positioned before passing it");
    	
        final Video video = new Video();
        
        video.id = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys._ID));
        video.title = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.TITLE));
        video.uploaderName = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.AUTHOR));
        video.uploaderProfileUrl = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.AUTHOR_URL));        
        video.duration = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys.DURATION));
        video.tags = Utils.extractTags(cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.TAGS))); 
        video.likesCount = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys.NUM_OF_LIKES));
        video.playsCount = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys.NUM_OF_PLAYS));
        video.commentsCount = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys.NUM_OF_COMMENTS));
        
        video.smallThumbnailUrl = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.THUMB_SMALL));
        
        return video;
    }
    
    public static Video itemFromCursor(Cursor cursor, int position) {
    	final Video video = generalDataFromCursor(cursor, position);
    	
        video.smallUploaderPortraitUrl = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.USER_IMG_SMALL));
        
        return video;
    }
    
    public static Video singleFromCursor(Cursor cursor, int position) {
    	final Video video = generalDataFromCursor(cursor, position);
        
    	video.mediumUploaderPortraitUrl = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.USER_IMG_MEDIUM)); 
    	
        video.description = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.DESCRIPTION));
        video.uploadedOn = Utils.adaptDate(cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.UPLOADED_ON)));
        
        video.width = cursor.getInt(cursor.getColumnIndex(Video.FieldsKeys.WIDTH));
        video.height = cursor.getInt(cursor.getColumnIndex(Video.FieldsKeys.HEIGHT));
        
        return video;
    }    
    
}
