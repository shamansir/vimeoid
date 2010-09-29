/**
 * 
 */
package org.vimeoid.dto.advanced;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.util.AdvancedItem;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.advanced</dd>
 * </dl>
 *
 * <code>Tag</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 29, 2010 10:13:14 PM 
 *
 */
public class Tag implements AdvancedItem {
    
    public long id;
    
    public String authorId;
    public String normalized;
    public String url;
    public String content;
    
    public static class FieldsKeys {
        
        public static final String OBJECT_KEY = "tags";
        public static final String ARRAY_KEY = "tag";
        
        public static final String ID = "id";
        
        public static final String AUTHOR = "author";
        public static final String NORMALIZED = "normalized";
        public static final String URL = "url";
        public static final String CONTENT = "_content";        
        
    }
    
    public static Tag collectFromJson(JSONObject jsonObj) throws JSONException {
        final Tag tag = new Tag();
        
        tag.id = jsonObj.getLong(FieldsKeys.ID);
        
        tag.authorId = jsonObj.getString(FieldsKeys.AUTHOR);
        tag.normalized = jsonObj.getString(FieldsKeys.NORMALIZED);
        tag.url = jsonObj.getString(FieldsKeys.URL);
        tag.content = jsonObj.getString(FieldsKeys.CONTENT);
        
        return tag;
    }
    
    public static Tag[] collectListFromJson(JSONObject jsonObj) throws JSONException {
        final JSONArray array = jsonObj.getJSONObject(FieldsKeys.OBJECT_KEY)
                                       .getJSONArray(FieldsKeys.ARRAY_KEY);
        final Tag[] result = new Tag[array.length()];
        for (int i = 0; i < array.length(); i++) {
            result[i] = collectFromJson(array.getJSONObject(i));
        }
        return result;
    }
    
    public static String[] collectQuickListFromJson(JSONObject jsonObj) throws JSONException {
        final JSONArray array = jsonObj.getJSONObject(FieldsKeys.OBJECT_KEY)
                                       .getJSONArray(FieldsKeys.ARRAY_KEY);
        final String[] result = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            result[i] = array.getJSONObject(i).getString(FieldsKeys.CONTENT);
        }
        return result;
    }    

    @Override
    public long getId() { return id; }

}
