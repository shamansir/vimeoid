/**
 * 
 */
package org.vimeoid.dto.simple;

import org.vimeoid.util.Item;
import org.vimeoid.util.Utils;

import android.database.Cursor;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.simple</dd>
 * </dl>
 *
 * <code>User</code>
 *
 * <p>Vimeo User information</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 19, 2010 8:54:34 PM 
 *
 */
public class User implements Item {
    
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
    
    public final static String[] LIST_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.NAME, FieldsKeys.LOCATION,
        FieldsKeys.PORTRAIT_SMALL, FieldsKeys.NUM_OF_VIDEOS,
        FieldsKeys.NUM_OF_CONTACTS, FieldsKeys.NUM_OF_ALBUMS,
        FieldsKeys.NUM_OF_CHANNELS, FieldsKeys.IS_STAFF, FieldsKeys.IS_PLUS
    };
    
    public final static String[] SINGLE_PROJECTION = {
        FieldsKeys._ID, FieldsKeys.NAME, FieldsKeys.LOCATION,
        FieldsKeys.PORTRAIT_MEDIUM, FieldsKeys.NUM_OF_VIDEOS,
        FieldsKeys.NUM_OF_CONTACTS, FieldsKeys.NUM_OF_ALBUMS,
        FieldsKeys.NUM_OF_CHANNELS, FieldsKeys.IS_STAFF, FieldsKeys.IS_PLUS,
        
        FieldsKeys.CREATED_ON, FieldsKeys.BIO, FieldsKeys.NUM_OF_APPEARANCES, FieldsKeys.NUM_OF_LIKES        
   };
    
    protected static User generalDataFromCursor(Cursor cursor, int position) {
        if (cursor.getPosition() != position) throw new IllegalStateException("Cursor must be properly positioned before passing it");
        
        final User user = new User();
        
        user.id = cursor.getLong(cursor.getColumnIndex(User.FieldsKeys._ID));
        user.displayName = cursor.getString(cursor.getColumnIndex(User.FieldsKeys.NAME));
        user.location = cursor.getString(cursor.getColumnIndex(User.FieldsKeys.LOCATION));
        user.fromStaff = Utils.adaptBoolean(cursor.getInt(cursor.getColumnIndex(User.FieldsKeys.IS_STAFF)));
        user.isPlusMember = Utils.adaptBoolean(cursor.getInt(cursor.getColumnIndex(User.FieldsKeys.IS_PLUS))); 
        user.videosUploaded = cursor.getLong(cursor.getColumnIndex(User.FieldsKeys.NUM_OF_VIDEOS));
        user.contactsCount = cursor.getLong(cursor.getColumnIndex(User.FieldsKeys.NUM_OF_CONTACTS));
        user.albumsCount = cursor.getLong(cursor.getColumnIndex(User.FieldsKeys.NUM_OF_ALBUMS));
        user.channelsCount = cursor.getLong(cursor.getColumnIndex(User.FieldsKeys.NUM_OF_CHANNELS));
        
        return user;
    }
    
    public static User itemFromCursor(Cursor cursor, int position) {
        final User user = generalDataFromCursor(cursor, position);
        
        user.smallPortraitUrl = cursor.getString(cursor.getColumnIndex(User.FieldsKeys.PORTRAIT_SMALL));
        
        return user;
    }
    
    public static User singleFromCursor(Cursor cursor, int position) {
        final User user = generalDataFromCursor(cursor, position);
        
        user.mediumPortraitUrl = cursor.getString(cursor.getColumnIndex(User.FieldsKeys.PORTRAIT_MEDIUM)); 
        
        user.biography = cursor.getString(cursor.getColumnIndex(User.FieldsKeys.BIO));
        user.createdOn = cursor.getString(cursor.getColumnIndex(User.FieldsKeys.CREATED_ON));
        
        user.videosAppearsIn = cursor.getLong(cursor.getColumnIndex(User.FieldsKeys.NUM_OF_APPEARANCES));
        user.videosLiked = cursor.getLong(cursor.getColumnIndex(User.FieldsKeys.NUM_OF_LIKES));
        
        return user;
    }    

}