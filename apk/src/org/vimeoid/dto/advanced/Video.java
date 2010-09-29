/**
 * 
 */
package org.vimeoid.dto.advanced;

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
    
    public static enum Privacy { ANYBODY, CONTACTS }; 
    
    public long id;
    
    public Privacy privacy;
    
    public String title;
    public String description;
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
    
    public String[] tags;
    
    public ThumbnailsData thumbnails;
     
    public final static class FieldsKeys {
        
        public static final String OBJECT_KEY = "video";
        public static final String ARRAY_KEY = "videos";
        
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
        public static final String WIDTH = "width";
        public static final String HEIGHT= "height";
        public static final String DURATION = "duration";
        
    }
    
    public static Video collectFromJson(JSONObject jsonObj) throws JSONException {
        final Video video = new Video();
        
        video.id = jsonObj.getLong(FieldsKeys.ID);
        
        video.privacy = Privacy.valueOf(jsonObj.getString(FieldsKeys.PRIVACY));        
        video.title = jsonObj.getString(FieldsKeys.TITLE);
        video.description = jsonObj.getString(FieldsKeys.DESCRIPTION);
        
        video.uploadedOn = jsonObj.getString(FieldsKeys.UPLOADED_ON);
        video.modifiedOn = jsonObj.getString(FieldsKeys.MODIFIED_ON);
        
        video.likesCount = jsonObj.getLong(FieldsKeys.NUM_OF_LIKES);
        video.playsCount = jsonObj.getLong(FieldsKeys.NUM_OF_PLAYS);
        video.commentsCount = jsonObj.getLong(FieldsKeys.NUM_OF_COMMENTS);
        
        video.isHd = Utils.adaptBoolean(jsonObj.getInt(FieldsKeys.IS_HD));
        video.width = jsonObj.getInt(FieldsKeys.WIDTH);
        video.height = jsonObj.getInt(FieldsKeys.HEIGHT);
        video.duration = jsonObj.getLong(FieldsKeys.DURATION);
        
        video.tags = Tag.collectQuickListFromJson(jsonObj);
        video.thumbnails = ThumbnailsData.collectFromJson(jsonObj.getJSONObject(ThumbnailsData.FieldsKeys.ARRAY_KEY));
        
        video.uploaderName = jsonObj.getJSONObject("owner"/*User.FieldsKeys.OBJECT_KEY*/).getString(User.FieldsKeys.NAME);
        
        return video;
    }
    
    public long getId() { return id; }    
    
}
