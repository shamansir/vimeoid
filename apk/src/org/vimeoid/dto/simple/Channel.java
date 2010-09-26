/**
 * 
 */
package org.vimeoid.dto.simple;

import org.vimeoid.util.SimpleItem;

import android.database.Cursor;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.simple</dd>
 * </dl>
 *
 * <code>Channel</code>
 *
 * <p>Vimeo channel representation</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 19, 2010 8:56:39 PM 
 *
 */
public class Channel implements SimpleItem {
    
    /* public static final Uri CONTENT_URI = Uri.parse("content://" + VimeoProvider.AUTHORITY +
                                                                   "/channels"); */

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.vimeo.channel";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.vimeo.channel";

    public long id;
    public String name;
    public String description;
    
    public String logoHeader;
    public String pageUrl;
    public String rssUrl;
    
    public /*long*/ String createdOn;
    public long creatorId;
    public String creatorDisplayName;
    public String creatorProfileUrl;
    
    public long videosCount;
    public long subscribersCount;
    
    public final static class FieldsKeys {
        
        public static final String _ID = "_id";
        
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";        
        public static final String LOGO = "logo";
        public static final String PAGE_URL = "url";        
        public static final String RSS_URL = "ress";
        
        public static final String CREATED_ON = "created_on";
        public static final String CREATOR_ID = "creator_id";
        public static final String CREATOR_DISPLAY_NAME = "creator_display_name";
        public static final String CREATOR_URL = "creator_url";
        
        public static final String NUM_OF_VIDEOS = "total_videos";
        public static final String NUM_OF_SUBSCRIBERS = "total_subscribers";        
        
    }    
    
    public final static String[] ITEM_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.NAME, FieldsKeys.LOGO, 
        FieldsKeys.CREATOR_DISPLAY_NAME, FieldsKeys.CREATOR_ID, 
        FieldsKeys.CREATED_ON, FieldsKeys.NUM_OF_VIDEOS, FieldsKeys.NUM_OF_SUBSCRIBERS        
    };
    
    public final static String[] SINGLE_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.NAME, FieldsKeys.LOGO, 
        FieldsKeys.CREATOR_DISPLAY_NAME, FieldsKeys.CREATOR_ID, 
        FieldsKeys.CREATED_ON, FieldsKeys.NUM_OF_VIDEOS, FieldsKeys.NUM_OF_SUBSCRIBERS,
        
        FieldsKeys.DESCRIPTION
    };    
    
    protected static Channel generalDataFromCursor(Cursor cursor, int position) {
    	if (cursor.getPosition() != position) throw new IllegalStateException("Cursor must be properly positioned before passing it");
    	
        final Channel channel = new Channel();
        
        channel.id = cursor.getLong(cursor.getColumnIndex(Channel.FieldsKeys._ID));
        channel.name = cursor.getString(cursor.getColumnIndex(Channel.FieldsKeys.NAME));
        channel.logoHeader = cursor.getString(cursor.getColumnIndex(Channel.FieldsKeys.LOGO));
        channel.creatorDisplayName = cursor.getString(cursor.getColumnIndex(Channel.FieldsKeys.CREATOR_DISPLAY_NAME));        
        channel.creatorId = cursor.getLong(cursor.getColumnIndex(Channel.FieldsKeys.CREATOR_ID));
        channel.createdOn = cursor.getString(cursor.getColumnIndex(Channel.FieldsKeys.CREATED_ON)); 
        channel.videosCount = cursor.getLong(cursor.getColumnIndex(Channel.FieldsKeys.NUM_OF_VIDEOS));
        channel.subscribersCount = cursor.getLong(cursor.getColumnIndex(Channel.FieldsKeys.NUM_OF_SUBSCRIBERS));
        
        return channel;
    }
    
    public static Channel itemFromCursor(Cursor cursor, int position) {
    	return generalDataFromCursor(cursor, position);
    }
    
    public static Channel singleFromCursor(Cursor cursor, int position) {
    	final Channel channel = generalDataFromCursor(cursor, position);
        
    	channel.description = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.DESCRIPTION)); 
    	
        return channel;
    }    
    
}
