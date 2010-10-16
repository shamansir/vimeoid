/**
 * 
 */
package org.vimeoid.dto.advanced;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.advanced</dd>
 * </dl>
 *
 * <code>PortraitsData</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 27, 2010 11:34:44 PM 
 *
 */
public class ThumbnailsData {
    
    public static class Thumbnail {
        public int width;
        public int height;
        public String url;
    }
    
    public Thumbnail small; // 100x75
    public Thumbnail medium; // 200x150
    public Thumbnail large; // 640x480, 640x360, 640x400, ...
    public Thumbnail huge; // 1280x720, 1280x800, ...
    
    public static class FieldsKeys {
        public static final String SINGLE_KEY = "thumbnail";
        public static final String MULTIPLE_KEY = "thumbnails";
        
        public static final String HEIGHT = "height";
        public static final String WIDTH = "width";
        public static final String URL = "_content";
    }
    
    private static Thumbnail extractFromJson(JSONObject jsonObj) throws JSONException {
        final Thumbnail thumbnail = new Thumbnail();
        thumbnail.width = jsonObj.getInt(FieldsKeys.WIDTH);
        thumbnail.height = jsonObj.getInt(FieldsKeys.HEIGHT);
        thumbnail.url = jsonObj.getString(FieldsKeys.URL);
        return thumbnail;
    }
    
    public static ThumbnailsData collectFromJson(JSONObject jsonObj) throws JSONException {        
        final JSONArray thumbnailsArr = jsonObj.getJSONObject(FieldsKeys.MULTIPLE_KEY).getJSONArray(FieldsKeys.SINGLE_KEY);
        if ((thumbnailsArr.length() != 3) && (thumbnailsArr.length() != 4)) throw new IllegalStateException("Unknown thumbnails object");
        final ThumbnailsData thumbnailsData = new ThumbnailsData();
        thumbnailsData.small = extractFromJson(thumbnailsArr.getJSONObject(0));
        thumbnailsData.medium = extractFromJson(thumbnailsArr.getJSONObject(1));
        thumbnailsData.large = extractFromJson(thumbnailsArr.getJSONObject(2));
        if ((thumbnailsArr.length() == 4)) thumbnailsData.huge = extractFromJson(thumbnailsArr.getJSONObject(3)); 
        return thumbnailsData;
    }
    
}
