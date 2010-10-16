/**
 * 
 */
package org.vimeoid.dto.advanced;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.dto.advanced.SubscriptionData.SubscriptionType;
import org.vimeoid.util.AdvancedItem;
import org.vimeoid.util.Utils;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.advanced</dd>
 * </dl>
 *
 * <code>User</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 26, 2010 10:23:00 PM 
 *
 */
public class User implements AdvancedItem {
    
    public enum SortType implements ISortType { 
        
        NEWEST, OLDEST, ALPHABETICAL, MOST_CREDITED;
        
        @Override public String toString() { return name().toLowerCase(); };
        
    };
    
    public long id;
    public String displayName;
    public String username;
    public /*long*/ String createdOn;
    public Boolean fromStaff = null;
    public Boolean isPlusMember = null;
    
    public String profileUrl;
    public String videosUrl;
    
    public long videosCount = -1;    
    public long uploadsCount = -1;
    public long videosAppearsIn = -1;
    public long videosLiked = -1;
    
    public long contactsCount = -1;
    public long albumsCount = -1;
    public long channelsCount = -1;
    
    public String location;
    public String[] websiteUrls;
    public String biography;
    
    public PortraitsData portraits;
    
    public Boolean isContact = null;
    public Boolean isMutual = null;
    public Set<SubscriptionType> subscriptonsStatus = null;

    public final static class FieldsKeys {
        
        public static final String SINGLE_KEY = "person";
        public static final String MULTIPLE_KEY = "persons";
        public static final String OWNER_SINGLE_KEY = "owner";
        
        public static final String ID = "id";
        
        public static final String CREATED_ON = "created_on";
        public static final String IS_STAFF = "is_staff";
        public static final String IS_PLUS = "is_plus";
        
        public static final String NAME = "display_name";
        public static final String USERNAME = "username";  
        public static final String LOCATION = "location";
        // realname
        
        public static final String URL = "url";
        public static final String BIO = "bio";
        
        public static final String PROFILE_URL = "profileurl";
        public static final String VIDEOS_URL = "videosurl";    
        
        public static final String NUM_OF_VIDEOS = "number_of_videos";
        public static final String NUM_OF_UPLOADS = "number_of_uploads";
        public static final String NUM_OF_LIKES = "number_of_likes";
        public static final String NUM_OF_APPEARANCES = "number_of_videos_appears_in";
        public static final String NUM_OF_CONTACTS = "number_of_contacts";
        
    }
    
    public static User extractFromJson(JSONObject jsonObj) throws JSONException {
        final User user = new User();
        
        user.id = jsonObj.getLong(FieldsKeys.ID);
        user.createdOn = jsonObj.getString(FieldsKeys.CREATED_ON);
        user.fromStaff = Utils.adaptBoolean(jsonObj.getInt(FieldsKeys.IS_STAFF));
        user.isPlusMember = Utils.adaptBoolean(jsonObj.getInt(FieldsKeys.IS_PLUS));
        
        user.displayName = jsonObj.getString(FieldsKeys.NAME);
        user.username = jsonObj.getString(FieldsKeys.USERNAME);
        user.location = jsonObj.getString(FieldsKeys.LOCATION);
        
        user.websiteUrls = Utils.stringArrayFromJson(jsonObj.getJSONArray(FieldsKeys.URL));
        user.biography = jsonObj.getString(FieldsKeys.BIO);
        
        user.profileUrl = jsonObj.getString(FieldsKeys.PROFILE_URL);
        user.videosUrl = jsonObj.getString(FieldsKeys.VIDEOS_URL);
        
        user.uploadsCount = jsonObj.getLong(FieldsKeys.NUM_OF_UPLOADS);
        user.videosCount = jsonObj.getLong(FieldsKeys.NUM_OF_VIDEOS);
        user.videosLiked = jsonObj.getLong(FieldsKeys.NUM_OF_LIKES);
        user.videosAppearsIn = jsonObj.getLong(FieldsKeys.NUM_OF_APPEARANCES);
        user.contactsCount = jsonObj.getLong(FieldsKeys.NUM_OF_CONTACTS);
        
        if (jsonObj.has(PortraitsData.FieldsKeys.MULTIPLE_KEY)) 
            user.portraits = PortraitsData.collectFromJson(jsonObj);
        
        return user;
    }
    
	public static User[] collectListFromJson(JSONObject jsonObj) throws JSONException {
        final JSONArray dataArray = jsonObj.getJSONObject(FieldsKeys.MULTIPLE_KEY)
		        						   .getJSONArray(FieldsKeys.SINGLE_KEY);
		final User[] users = new User[dataArray.length()];
		for (int i = 0; i < dataArray.length(); i++) {
			users[i] = extractFromJson(dataArray.getJSONObject(i));
		}
		return users;
	}
    
    public static User collectFromJson(JSONObject jsonObj) throws JSONException {
        return extractFromJson(jsonObj.getJSONObject(FieldsKeys.SINGLE_KEY));
    }
    
    public long getId() { return id; }
}
