/**
 * 
 */
package org.vimeoid.dto.simple;

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
public class Video {
    
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
        public static final String UPLOAD_DATE = "upload_date";
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
    
    public final static String[] SHORT_EXTRACT_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.TITLE, FieldsKeys.AUTHOR,
        FieldsKeys.DURATION, FieldsKeys.TAGS, 
        FieldsKeys.THUMB_SMALL, FieldsKeys.USER_IMG_SMALL, 
        FieldsKeys.NUM_OF_LIKES, FieldsKeys.NUM_OF_PLAYS, FieldsKeys.NUM_OF_COMMENTS
    };
    
    /* @Override
    public ContentValues extract() {
        final ContentValues result = new ContentValues();
        result.put(FieldsKeys._ID, this.id);
        result.put(FieldsKeys.TITLE, this.title);
        result.put(FieldsKeys.AUTHOR, this.uploaderName);
        result.put(FieldsKeys.DESCRIPTION, this.description);
        result.put(FieldsKeys.DURATION, this.duration);
        result.put(FieldsKeys.TAGS, Arrays.toString(this.tags));
        return result;
    } */

}
