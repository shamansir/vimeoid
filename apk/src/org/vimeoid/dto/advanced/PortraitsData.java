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
public class PortraitsData {
    
    public static class Portrait {
        public int width;
        public int height;
        public String url;
    }
    
    public Portrait nano;
    public Portrait small;
    public Portrait medium;
    public Portrait large;
    
    public static class FieldsKeys {
        public static final String SINGLE_KEY = "portrait";
        public static final String MULTIPLE_KEY = "portraits";
        
        public static final String HEIGHT = "height";
        public static final String WIDTH = "width";
        public static final String URL = "_content";
    }
    
    private static Portrait extractFromJson(JSONObject jsonObj) throws JSONException {
        final Portrait portrait = new Portrait();
        portrait.width = jsonObj.getInt(FieldsKeys.WIDTH);
        portrait.height = jsonObj.getInt(FieldsKeys.HEIGHT);
        portrait.url = jsonObj.getString(FieldsKeys.URL);
        return portrait;
    }
    
    public static PortraitsData collectFromJson(JSONObject jsonObj) throws JSONException {        
        final JSONArray portraitsArr = jsonObj.getJSONObject(FieldsKeys.MULTIPLE_KEY).getJSONArray(FieldsKeys.SINGLE_KEY);
        if (portraitsArr.length() != 4) throw new IllegalStateException("Unknown portraits object");
        final PortraitsData portraitsData = new PortraitsData();
        portraitsData.nano = extractFromJson(portraitsArr.getJSONObject(0));
        portraitsData.small = extractFromJson(portraitsArr.getJSONObject(1));
        portraitsData.medium = extractFromJson(portraitsArr.getJSONObject(2));
        portraitsData.large = extractFromJson(portraitsArr.getJSONObject(3));
        return portraitsData;
    }
    
}
