/**
 * 
 */
package org.vimeoid.connection;

import java.util.List;

import org.vimeoid.connection.simple.ContentType;

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
    
    public static final String RESPONSE_FORMAT = "json";
    
    private final static String TAG = "VimeoApiUtils";
    
    private VimeoApiUtils() { };
    
    public static StringBuffer resolveUriForSimpleApi(Uri contentUri) {
        return resolveUriForSimpleApi(contentUri, null);
    }
    
    /**
     * Returns Simple (unauthorized) Vimeo API URL part using the passed Content URI
     * which can be passed from VimeoUnauthorizedProvider
     * 
     * For example, for URI <code>content://[unauthorized-provider-authority]/user/shamansir/videos</code>,
     * URL part <code>shamansir/videos.json</code> will be returned and for 
     * URI <code>content://[unauthorized-provider-authority]/album/2239381/videos</code>,
     * URL part <code>album/2239381/info.json</code> will be returned and for 
     * URI <code>content://[unauthorized-provider-authority]/video/125623</code>,
     * URL part <code>video/125623.json</code> will be returned...
     * 
     * @param contentUri Content URI to resolve
     * @param appendTo if not null, appends the part to this buffer and returns it
     * 
     * @return buffer with the resulting URL part written inside
     */
    public static StringBuffer resolveUriForSimpleApi(Uri contentUri, StringBuffer appendTo) {
        final List<String> segments = contentUri.getPathSegments();
        final StringBuffer urlBuffer = (appendTo != null) ? appendTo : new StringBuffer();
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
        urlBuffer.append('.').append(RESPONSE_FORMAT);
        Log.d(TAG, "generated result: " + urlBuffer.toString());
        return urlBuffer;
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
