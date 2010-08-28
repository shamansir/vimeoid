/**
 * 
 */
package org.vimeoid.connection;

import java.util.List;

import org.vimeoid.VimeoUnauthorizedProvider.ContentType;

import android.net.Uri;
import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>VimeoApiUtils</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 25, 2010 9:30:14 PM 
 *
 */
public class VimeoApiUtils {
    
    private final static String TAG = "VimeoApiUtils";
    
    public static final String VIMEO_SITE_URL = "http://vimeo.com";
    public static final String VIMEO_API_URL = VIMEO_SITE_URL + "/api";
    public static final String VIMEO_REST_API_URL = VIMEO_API_URL + "/rest";
    public static final int VIMEO_API_VERSION = 2;
    
    public static final String VIMEO_SIMPLE_API_CALL_PREFIX = VIMEO_API_URL +
                                                              "/v" + VIMEO_API_VERSION;
    public static final String VIMEO_REST_API_CALL_PREFIX   = VIMEO_REST_API_URL +
                                                              "/v" + VIMEO_API_VERSION;
    public static final String VIMEO_AUTH_API_CALL_PREFIX   = VIMEO_SITE_URL +
                                                              "services/auth";  
    
    public static final String DEFAULT_RESPONSE_FORMAT = "json";
    
    private VimeoApiUtils() { };
    
    /* public static class ApiCallInfo {
        
        public static String callUrl;
        public static ContentType subjectType;
        public static ContentType resultType;
        public static boolean singleResultExpected;
        
    } */
        
    public static String getSimpleApiCallUrlForUri(Uri contentUri) {
        final List<String> segments = contentUri.getPathSegments();
        final StringBuffer urlBuffer = new StringBuffer().append(VIMEO_SIMPLE_API_CALL_PREFIX).append('/');
        Log.d(TAG, "generating API Call URL for URI " + contentUri.toString());
        switch (ContentType.fromAlias(segments.get(0))) {
            case USER: urlBuffer.append(validateShortcutOrId(segments.get(1))).append('/')                                
                                .append(resolveSimpleUserAction(segments.get(2))); break;
            case VIDEO: urlBuffer.append("video/")
                                 .append(validateId(segments.get(1))); break;
            case GROUP: urlBuffer.append("group/")
                                 .append(validateShortcutOrId(segments.get(1))).append('/')
                                 .append(resolveSimpleGroupAction(segments.get(2))); break;
            case CHANNEL: urlBuffer.append("channel/")
                                   .append(validateShortcutOrId(segments.get(1))).append('/')
                                   .append(resolveSimpleChannelAction(segments.get(2))); break;
            case ALBUM: urlBuffer.append("album/")
                                 .append(validateId(segments.get(1))).append('/')
                                 .append(resolveSimpleAlbumAction(segments.get(2))); break;            
            case ACTIVITY: urlBuffer.append("activity/")
                                    .append(validateShortcutOrId(segments.get(1))).append('/')
                                    .append(resolveSimpleActivityAction(segments.get(2))); break;
                                    
        }
        urlBuffer.append('.').append(DEFAULT_RESPONSE_FORMAT);
        Log.d(TAG, "generated result: " + urlBuffer.toString());
        return urlBuffer.toString();
    }
    
    protected static String validateShortcutOrId(final String shortcut) { 
        if (!shortcut.matches("^[\\d\\w_]+$")) throw new IllegalArgumentException("Not correct schortcut or ID: " + shortcut);
        return shortcut;
    }
    
    protected static String validateId(final String id) { 
        if (!id.matches("^\\d+$")) throw new IllegalArgumentException("Not correct ID: " + id);
        return id;
    }
    
    protected static String validateActionName(String action) {
        if (!action.matches("^[\\w_]+$")) throw new IllegalArgumentException("Not correct action name: " + action);
        return action;
    }

    private static String resolveSimpleUserAction(String action) {
        if ("all".equals(action)) return "all_videos";        
        if ("appears".equals(action)) return "appears_in";
        if ("subscr".equals(action)) return "subscriptions";
        if ("ccreated".equals(action)) return "contacts_videos";
        if ("clikes".equals(action)) return "contacts_like";
        return validateActionName(action);
    }
    
    private static String resolveSimpleActivityAction(String action) {
        if ("did".equals(action)) return "user_did";        
        if ("happened".equals(action)) return "happened_to_user";
        if ("cdid".equals(action)) return "contacts_did";
        if ("chappened".equals(action)) return "happened_to_contacts";
        if ("edid".equals(action)) return "everyone_did";
        return validateActionName(action);
    }
    
    private static String resolveSimpleGroupAction(String action) {
        return validateActionName(action);
    }
    
    private static String resolveSimpleAlbumAction(String action) {
        return validateActionName(action);
    }    
    
    private static String resolveSimpleChannelAction(String action) {
        return validateActionName(action);
    }    

}
