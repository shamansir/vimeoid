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
 * <code>Contact</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 12, 2010 11:11:04 PM 
 *
 */
public class Contact extends User {

    public static final class FieldsKeys {
        public static final String MULTIPLE_KEY = "contacts";
        public static final String SINGLE_KEY = "contact";
        
        public static final String IS_MUTUAL = "mutual";
    }
    
    public static boolean contactWithIdExist(JSONObject page, long testUserId) throws JSONException {
        if (!page.has(FieldsKeys.MULTIPLE_KEY)) return false;
        final JSONObject jsonColl = page.getJSONObject(FieldsKeys.MULTIPLE_KEY);
        if (!jsonColl.has(FieldsKeys.SINGLE_KEY)) return false;
        final JSONArray array = jsonColl.getJSONArray(FieldsKeys.SINGLE_KEY);
        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getLong(User.FieldsKeys.ID) == testUserId) return true;
        }
        return false;
    }

    public static boolean checkIsMutualById(JSONObject page, long testUserId) throws JSONException {
        if (!page.has(FieldsKeys.MULTIPLE_KEY)) return false;
        final JSONObject jsonColl = page.getJSONObject(FieldsKeys.MULTIPLE_KEY);
        if (!jsonColl.has(FieldsKeys.SINGLE_KEY)) return false;
        final JSONArray array = jsonColl.getJSONArray(FieldsKeys.SINGLE_KEY);
        for (int i = 0; i < array.length(); i++) {
            final JSONObject userObj = array.getJSONObject(i); 
            if (userObj.getLong(User.FieldsKeys.ID) == testUserId) {
                if (userObj.getInt(FieldsKeys.IS_MUTUAL) == 1) return true;
            }
        }
        return false;
    }
    
    public static User[] collectListFromJson(JSONObject jsonObj) throws JSONException {
        if (!jsonObj.has(FieldsKeys.MULTIPLE_KEY)) return new User[0];
        final JSONObject jsonColl = jsonObj.getJSONObject(FieldsKeys.MULTIPLE_KEY);
        if (!jsonColl.has(FieldsKeys.SINGLE_KEY)) return new User[0];
        final JSONArray array = jsonColl.getJSONArray(FieldsKeys.SINGLE_KEY);
        final User[] users = new User[array.length()];
        for (int i = 0; i < array.length(); i++) {
            users[i] = extractFromJson(array.getJSONObject(i));
        }
        return users;
    }    
    
}
