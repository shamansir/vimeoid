/**
 * 
 */
package org.vimeoid.dto;

import java.util.Arrays;

import org.vimeoid.Extractable;
import org.vimeoid.VimeoProvider;

import android.content.ContentValues;
import android.net.Uri;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto</dd>
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
public class Video implements Extractable {
    
    public static final Uri CONTENT_URI = Uri.parse("content://" + VimeoProvider.AUTHORITY +
                                                                   "/videos");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.vimeo.video";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.vimeo.video";    
    
    public int id;
    public String url;
    public String title;
    public String description;
    
    public int width;
    public int height;
    public int duration; /* in seconds */
    
    public int likesCount;
    public int playsCount;
    public int commentsCount;
    
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
        
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String DESCRIPTION = "description";
        public static final String TAGS = "tags";
        public static final String DURATION = "duration";
        
    }
    
    @Override
    public ContentValues extract() {
        final ContentValues result = new ContentValues();
        result.put(FieldsKeys.ID, this.id);
        result.put(FieldsKeys.TITLE, this.title);
        result.put(FieldsKeys.AUTHOR, this.uploaderName);
        result.put(FieldsKeys.DESCRIPTION, this.description);
        result.put(FieldsKeys.DURATION, this.duration);
        result.put(FieldsKeys.TAGS, Arrays.toString(this.tags));
        return result;
    }
    
}
