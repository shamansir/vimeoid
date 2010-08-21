/**
 * 
 */
package org.vimeoid.dto;

import org.vimeoid.VimeoProvider;

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
public class Video {
    
    public static final Uri CONTENT_URI = Uri.parse("vimeo://" + VimeoProvider.AUTHORITY +
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
    
}
