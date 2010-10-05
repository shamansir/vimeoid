/**
 * 
 */
package org.vimeoid.dto.advanced;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
public class Video implements AdvancedItem {
    
    public static enum SortType { NEWEST, OLDEST, MOST_PLAYED, MOST_COMMENTED, MOST_LIKED }; 
    
    public static enum Privacy { 
    	
    	ANYBODY, CONTACTS; 
    
    	public static Privacy parse(String value) {
    		final String toTest = value.trim().toLowerCase();
    		if (toTest.equals("anybody")) return ANYBODY;
    		if (toTest.equals("contacts")) return CONTACTS;
    		return null;
    	}
    
    }; 
    
    public long id;
    
    public Privacy privacy;
    
    public String title;
    public String description;
    public long uploaderId;
    public String uploaderName;
    
    public /*long */ String uploadedOn;
    public /*long */ String modifiedOn;
    
    public long likesCount;
    public long playsCount;
    public long commentsCount;
    
    public int width;
    public int height;
    public long duration;
    public boolean isHd;
    public boolean isLike;
    
    public String[] tags;
    
    public ThumbnailsData thumbnails;
    public PortraitsData uploaderPortraits; 
     
    public final static class FieldsKeys {
        
        public static final String SINGLE_KEY = "video";
        public static final String MULTIPLE_KEY = "videos";
        
        public static final String ID = "id";
        
        public static final String PRIVACY = "privacy";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        
        public static final String UPLOADED_ON = "upload_date";
        public static final String MODIFIED_ON = "modified_date";
        
        public static final String NUM_OF_LIKES = "number_of_likes";
        public static final String NUM_OF_PLAYS = "number_of_plays";
        public static final String NUM_OF_COMMENTS = "number_of_comments";
        
        public static final String IS_HD = "is_hd";
        public static final String IS_LIKE = "is_like";
        // is_transcoding
        // liked_on
        
        public static final String WIDTH = "width";
        public static final String HEIGHT= "height";
        public static final String DURATION = "duration";
        
    }
    
    public static Video extractFromJson(JSONObject jsonObj) throws JSONException {
        final Video video = new Video();
        
        video.id = jsonObj.getLong(FieldsKeys.ID);
        
        video.privacy = Privacy.parse(jsonObj.getString(FieldsKeys.PRIVACY));        
        video.title = jsonObj.getString(FieldsKeys.TITLE);
        video.description = jsonObj.getString(FieldsKeys.DESCRIPTION);
        
        video.uploadedOn = jsonObj.getString(FieldsKeys.UPLOADED_ON);
        video.modifiedOn = jsonObj.getString(FieldsKeys.MODIFIED_ON);
        
        video.likesCount = jsonObj.getLong(FieldsKeys.NUM_OF_LIKES);
        video.playsCount = jsonObj.getLong(FieldsKeys.NUM_OF_PLAYS);
        video.commentsCount = jsonObj.getLong(FieldsKeys.NUM_OF_COMMENTS);
        
        video.isHd = Utils.adaptBoolean(jsonObj.getInt(FieldsKeys.IS_HD));
        if (jsonObj.has(FieldsKeys.IS_LIKE)) {
        	video.isLike = Utils.adaptBoolean(jsonObj.getInt(FieldsKeys.IS_LIKE));
        }
        
        video.width = jsonObj.getInt(FieldsKeys.WIDTH);
        video.height = jsonObj.getInt(FieldsKeys.HEIGHT);
        video.duration = jsonObj.getLong(FieldsKeys.DURATION);
        
        video.tags = Tag.collectQuickListFromJson(jsonObj);
        video.thumbnails = ThumbnailsData.collectFromJson(jsonObj);
        video.thumbnails = ThumbnailsData.collectFromJson(jsonObj);
        
        video.uploaderName = jsonObj.getJSONObject(User.FieldsKeys.OWNER_KEY).getString(User.FieldsKeys.NAME);
        video.uploaderId = jsonObj.getJSONObject(User.FieldsKeys.OWNER_KEY).getLong(User.FieldsKeys.ID);
        video.uploaderPortraits = PortraitsData.collectFromJson(jsonObj.getJSONObject(User.FieldsKeys.OWNER_KEY));
        
        return video;
    }
    
    public static Video[] collectListFromJson(JSONObject jsonObj) throws JSONException {
        final JSONArray dataArray = jsonObj.getJSONObject(FieldsKeys.MULTIPLE_KEY)
                                           .getJSONArray(FieldsKeys.SINGLE_KEY);
        final Video[] videos = new Video[dataArray.length()];
        for (int i = 0; i < dataArray.length(); i++) {
            videos[i] = extractFromJson(dataArray.getJSONObject(i));
        }
        return videos;
    }    
    
    public static Video collectFromJson(JSONObject jsonObj) throws JSONException {
        return extractFromJson(jsonObj.getJSONArray(FieldsKeys.SINGLE_KEY).getJSONObject(0));
    }
    
    public static String[] extractIdsList(JSONObject page) throws JSONException {
        final JSONArray dataArray = page.getJSONObject(FieldsKeys.MULTIPLE_KEY)
                                        .getJSONArray(FieldsKeys.SINGLE_KEY);
        final String[] ids = new String[dataArray.length()];
        for (int i = 0; i < dataArray.length(); i++) {
            ids[i] = dataArray.getJSONObject(i).getString(FieldsKeys.ID);
        }
        return ids;    
    }
    
    public long getId() { return id; }

}
