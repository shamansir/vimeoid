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
    
    public Thumbnail nano;
    public Thumbnail small;
    public Thumbnail medium;
    public Thumbnail large;
    
    public static class FieldsKeys {
        public static final String SINGLE_KEY = "portrait";
        public static final String MULTIPLE_KEY = "portraits";
        
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
        if (thumbnailsArr.length() != 4) throw new IllegalStateException("Unknown thumbnails object");
        final ThumbnailsData thumbnailsData = new ThumbnailsData();
        thumbnailsData.nano = extractFromJson(thumbnailsArr.getJSONObject(0));
        thumbnailsData.small = extractFromJson(thumbnailsArr.getJSONObject(1));
        thumbnailsData.medium = extractFromJson(thumbnailsArr.getJSONObject(2));
        thumbnailsData.large = extractFromJson(thumbnailsArr.getJSONObject(3));
        return thumbnailsData;
    }
    
}
