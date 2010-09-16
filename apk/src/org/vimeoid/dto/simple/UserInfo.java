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
 * <code>UserInfo</code>
 *
 * <p>Vimeo UserInfo information</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 19, 2010 8:54:34 PM 
 *
 */
public class UserInfo implements Item {
    
    /* public static final Uri CONTENT_URI = Uri.parse("content://" + VimeoProvider.AUTHORITY +
                                                                   "/users"); */

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.vimeo.user";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.vimeo.user";    
    
    public long id;
    public String displayName;
    public /*long*/ String createdOn;
    public boolean fromStaff;
    public boolean isPlusMember;
    
    public String profileUrl;
    public String videosUrl;
    
    public long videosUploaded;
    public long videosAppearsIn;
    public long videosLiked;
    
    public long contactsCount;
    public long albumsCount;
    public long channelsCount;
    
    public String location;
    public String websiteUrl;
    public String biography; 
    
    public String smallPortraitUrl;
    public String mediumPortraitUrl;
    public String largePortraitUrl;
    
    public final static class FieldsKeys {
        
        public static final String _ID = "_id";
        
        public static final String NAME = "display_name";
        public static final String CREATED_ON = "created_on";
        public static final String IS_STAFF = "is_staff";
        public static final String IS_PLUS = "is_plus";        
        public static final String LOCATION = "location";
        public static final String URL = "url";
        public static final String BIO = "bio";
        
        public static final String PROFILE_URL = "profile_url";
        public static final String VIDEOS_URL = "videos_url";    
        
        public static final String PORTRAIT_SMALL = "portrait_small";
        public static final String PORTRAIT_MEDIUM = "portrait_medium";
        public static final String PORTRAIT_LARGE = "portrait_large";
        
        public static final String NUM_OF_VIDEOS = "total_videos_uploaded";
        public static final String NUM_OF_APPEARANCES = "total_videos_appears_in";
        public static final String NUM_OF_LIKES = "total_videos_liked";
        
        public static final String NUM_OF_CONTACTS = "total_contacts";
        public static final String NUM_OF_ALBUMS = "total_albums";
        public static final String NUM_OF_CHANNELS = "total_channels";        
        
    }
    
    public final static String[] SHORT_EXTRACT_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.NAME, FieldsKeys.LOCATION,
        FieldsKeys.PORTRAIT_SMALL, FieldsKeys.NUM_OF_VIDEOS,
        FieldsKeys.NUM_OF_CONTACTS, FieldsKeys.NUM_OF_ALBUMS,
        FieldsKeys.NUM_OF_CHANNELS, FieldsKeys.IS_STAFF, FieldsKeys.IS_PLUS
    };
    
    public final static String[] FULL_EXTRACT_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.NAME, FieldsKeys.LOCATION,
        FieldsKeys.PORTRAIT_MEDIUM, FieldsKeys.NUM_OF_VIDEOS,
        FieldsKeys.NUM_OF_CONTACTS, FieldsKeys.NUM_OF_ALBUMS,
        FieldsKeys.NUM_OF_CHANNELS, FieldsKeys.IS_STAFF, FieldsKeys.IS_PLUS,
        
        FieldsKeys.CREATED_ON, FieldsKeys.BIO, FieldsKeys.NUM_OF_APPEARANCES, FieldsKeys.NUM_OF_LIKES        
   };
    
    protected static UserInfo generalDataFromCursor(Cursor cursor, int position) {
        if (cursor.getPosition() != position) throw new IllegalStateException("Cursor must be properly positioned before passing it");
        
        final UserInfo user = new UserInfo();
        
        user.id = cursor.getLong(cursor.getColumnIndex(UserInfo.FieldsKeys._ID));
        user.displayName = cursor.getString(cursor.getColumnIndex(UserInfo.FieldsKeys.NAME));
        user.location = cursor.getString(cursor.getColumnIndex(UserInfo.FieldsKeys.LOCATION));
        user.fromStaff = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(UserInfo.FieldsKeys.IS_STAFF)));
        user.isPlusMember = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(UserInfo.FieldsKeys.IS_PLUS))); 
        user.videosUploaded = cursor.getLong(cursor.getColumnIndex(UserInfo.FieldsKeys.NUM_OF_VIDEOS));
        user.contactsCount = cursor.getLong(cursor.getColumnIndex(UserInfo.FieldsKeys.NUM_OF_CONTACTS));
        user.albumsCount = cursor.getLong(cursor.getColumnIndex(UserInfo.FieldsKeys.NUM_OF_ALBUMS));
        user.channelsCount = cursor.getLong(cursor.getColumnIndex(UserInfo.FieldsKeys.NUM_OF_CHANNELS));
        
        return user;
    }
    
    public static UserInfo shortFromCursor(Cursor cursor, int position) {
        final UserInfo user = generalDataFromCursor(cursor, position);
        
        user.smallPortraitUrl = cursor.getString(cursor.getColumnIndex(UserInfo.FieldsKeys.PORTRAIT_SMALL));
        
        return user;
    }
    
    public static UserInfo fullFromCursor(Cursor cursor, int position) {
        final UserInfo user = generalDataFromCursor(cursor, position);
        
        user.mediumPortraitUrl = cursor.getString(cursor.getColumnIndex(UserInfo.FieldsKeys.PORTRAIT_MEDIUM)); 
        
        user.biography = cursor.getString(cursor.getColumnIndex(UserInfo.FieldsKeys.BIO));
        user.createdOn = cursor.getString(cursor.getColumnIndex(UserInfo.FieldsKeys.CREATED_ON));
        
        user.videosAppearsIn = cursor.getLong(cursor.getColumnIndex(UserInfo.FieldsKeys.NUM_OF_APPEARANCES));
        user.videosLiked = cursor.getLong(cursor.getColumnIndex(UserInfo.FieldsKeys.NUM_OF_LIKES));
        
        return user;
    }    

}