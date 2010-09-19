/**
 * 
 */
package org.vimeoid.dto.simple;

import org.vimeoid.util.Item;

import android.database.Cursor;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.simple</dd>
 * </dl>
 *
 * <code>Album</code>
 *
 * <p>Vimeo album information</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 20, 2010 9:55:45 AM 
 *
 */
public class Album implements Item {
    
    /* public static final Uri CONTENT_URI = Uri.parse("content://" + VimeoProvider.AUTHORITY +
                                                                   "/albums"); */

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.vimeo.album";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.vimeo.album";    
    
    public long id;
    public String title;
    public String description;
    
    public String pageUrl;
    public String smallThumbnailUrl;
    public String mediumThumbnailUrl;
    public String largeThumbnailUrl;
    
    public /*long*/ String createdOn;
    public long creatorId;
    public String creatorDisplayName;
    public String creatorProfileUrl;    
    
    public long videosCount;    
    
    public /*long*/ String lastModifiedOn;
    
    public final static class FieldsKeys {
        
        public static final String _ID = "_id";
        
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";        
        public static final String THUMBNAIL_SMALL = "thumbnail_small";
        public static final String THUMBNAIL_MEDIUM = "thumbnail_medium";
        public static final String THUMBNAIL_LARGE = "thumbnail_large";
        public static final String PAGE_URL = "url";
        
        public static final String CREATED_ON = "created_on";
        public static final String MODIFIED_ON = "last_modified";        
        
        public static final String CREATOR_ID = "user_id";
        public static final String CREATOR_DISPLAY_NAME = "user_display_name";
        public static final String CREATOR_URL = "user_url";
        
        public static final String NUM_OF_VIDEOS = "total_videos";
        
    }    
    
    public final static String[] SHORT_EXTRACT_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.TITLE, FieldsKeys.THUMBNAIL_SMALL, 
        FieldsKeys.CREATED_ON, FieldsKeys.MODIFIED_ON, FieldsKeys.NUM_OF_VIDEOS
    };
    
    public final static String[] FULL_EXTRACT_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.TITLE, FieldsKeys.THUMBNAIL_MEDIUM, 
        FieldsKeys.CREATOR_DISPLAY_NAME, FieldsKeys.CREATOR_ID, 
        FieldsKeys.CREATED_ON, FieldsKeys.MODIFIED_ON, FieldsKeys.NUM_OF_VIDEOS,        
        FieldsKeys.DESCRIPTION
    };    
    
    protected static Album generalDataFromCursor(Cursor cursor, int position) {
        if (cursor.getPosition() != position) throw new IllegalStateException("Cursor must be properly positioned before passing it");
        
        final Album album = new Album();
        
        album.id = cursor.getLong(cursor.getColumnIndex(Album.FieldsKeys._ID));
        album.title = cursor.getString(cursor.getColumnIndex(Album.FieldsKeys.TITLE));
        album.createdOn = cursor.getString(cursor.getColumnIndex(Album.FieldsKeys.CREATED_ON));        
        album.lastModifiedOn = cursor.getString(cursor.getColumnIndex(Album.FieldsKeys.MODIFIED_ON));        
        album.videosCount = cursor.getLong(cursor.getColumnIndex(Album.FieldsKeys.NUM_OF_VIDEOS));        
        
        return album;
    }
    
    public static Album shortFromCursor(Cursor cursor, int position) {
        final Album album = generalDataFromCursor(cursor, position);
        
        album.smallThumbnailUrl = cursor.getString(cursor.getColumnIndex(Album.FieldsKeys.THUMBNAIL_SMALL));
        
        return album;
    }
    
    public static Album fullFromCursor(Cursor cursor, int position) {
        final Album album = generalDataFromCursor(cursor, position);
        
        album.mediumThumbnailUrl = cursor.getString(cursor.getColumnIndex(Album.FieldsKeys.THUMBNAIL_MEDIUM));        
        album.description = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.DESCRIPTION));
        album.creatorDisplayName = cursor.getString(cursor.getColumnIndex(Album.FieldsKeys.CREATOR_DISPLAY_NAME));        
        album.creatorId = cursor.getLong(cursor.getColumnIndex(Album.FieldsKeys.CREATOR_ID));        
        
        return album;
    }    
    
    
}
