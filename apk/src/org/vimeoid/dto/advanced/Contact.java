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
        final JSONArray array = page.getJSONObject(FieldsKeys.MULTIPLE_KEY)
                                    .getJSONArray(FieldsKeys.SINGLE_KEY);
        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getLong(User.FieldsKeys.ID) == testUserId) return true;
        }
        return false;
    }

    public static boolean checkIsMutualById(JSONObject page, long testUserId) throws JSONException {
        final JSONArray array = page.getJSONObject(FieldsKeys.MULTIPLE_KEY)
                                    .getJSONArray(FieldsKeys.SINGLE_KEY);
        for (int i = 0; i < array.length(); i++) {
            final JSONObject userObj = array.getJSONObject(i); 
            if (userObj.getLong(User.FieldsKeys.ID) == testUserId) {
                if (userObj.getInt(FieldsKeys.IS_MUTUAL) == 1) return true;
            }
        }
        return false;
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
    
}
