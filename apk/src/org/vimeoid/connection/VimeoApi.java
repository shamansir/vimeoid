/**
 * 
 */
package org.vimeoid.connection;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.connection.simple.ContentType;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>VimeoApi</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 25, 2010 9:30:14 PM 
 *
 */
public class VimeoApi {
    
    private final static String TAG = "VimeoApi";
    
    public static final String RESPONSE_FORMAT = "json";
    private static final String ADV_API_NAMESPACE = "vimeo";
    
    public static final Uri OAUTH_CALLBACK_URL = Uri.parse("vimeoid://oauth.done");
    public static final String OAUTH_API_PREFERENCES_ID = "org.vimeoid.vimeoauth";
    
    private static final String OAUTH_TOKEN_PARAM = "user_oauth_public";
    private static final String OAUTH_TOKEN_SECRET_PARAM = "user_oauth_secret";
    
    private VimeoApi() { };
    
    /**
     * <p>Returns Simple (unauthorized) Vimeo API URL using the passed Content URI
     * (see <code>VimeoSimpleApiProvider</code> for the supported URIs types)</p>
     * 
     * <p>For example, for URI <code>content://[unauthorized-provider-authority]/user/shamansir/videos</code>,
     * URI <code>http://vimeo.com/api/v2/shamansir/videos.json</code> will be returned and for 
     * URI <code>content://[unauthorized-provider-authority]/album/2239381/videos</code>,
     * URI <code>http://vimeo.com/api/v2/album/2239381/info.json</code> will be returned and for 
     * URI <code>content://[unauthorized-provider-authority]/video/125623</code>,
     * URI <code>http://vimeo.com/api/v2/video/125623.json</code> will be returned...</p>
     * 
     * @param contentUri Content URI to resolve
     * 
     * @return URI, pointing to the Vimeo Simple API method corresponding to this contentUri
     */    
    public static URI resolveUriForSimpleApi(Uri contentUri) {
        try {
            return new URI(getUrlPartForSimpleApi(contentUri, 
                           new StringBuffer(VimeoConfig.VIMEO_SIMPLE_API_CALL_PREFIX).append('/')).toString());
        } catch (URISyntaxException use) {
            Log.e(TAG, "URI settings exception when getting URI for " + contentUri);
            use.printStackTrace();
            return null;
        }
    }
    
    /**
     * <p>Returns Simple (unauthorized) Vimeo API URL part using the passed Content URI</p>
     * 
     * <p>For example, for URI <code>content://[unauthorized-provider-authority]/user/shamansir/videos</code>,
     * URL part <code>shamansir/videos.json</code> will be returned and for 
     * URI <code>content://[unauthorized-provider-authority]/album/2239381/videos</code>,
     * URL part <code>album/2239381/info.json</code> will be returned and for 
     * URI <code>content://[unauthorized-provider-authority]/video/125623</code>,
     * URL part <code>video/125623.json</code> will be returned...</p>
     * 
     * @param contentUri Content URI to resolve
     * @param appendTo if not null, appends the part to this buffer and returns it
     * 
     * @return buffer with the resulting URL part written inside
     */
    public static StringBuffer getUrlPartForSimpleApi(Uri contentUri, StringBuffer appendTo) {
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
    
    public static boolean ensureOAuth(Context context) {
        if (!JsonOverHttp.use().isOAuthInitialized()) {
            JsonOverHttp.use().subscribeOAuth(
                    new CommonsHttpOAuthConsumer(VimeoConfig.VIMEO_API_KEY, 
                                                 VimeoConfig.VIMEO_SHARED_SECRET), 
                    new CommonsHttpOAuthProvider(VimeoConfig.VIMEO_OAUTH_API_ROOT + "/request_token", 
                                                 VimeoConfig.VIMEO_OAUTH_API_ROOT + "/access_token", 
                                                 VimeoConfig.VIMEO_OAUTH_API_ROOT + "/authorize"));
            // check if we already have token
            final SharedPreferences storage = context.getSharedPreferences(OAUTH_API_PREFERENCES_ID, Context.MODE_PRIVATE);
            final String oToken = storage.getString(OAUTH_TOKEN_PARAM, null);
            final String oTokenSecret = storage.getString(OAUTH_TOKEN_SECRET_PARAM, null);
            if ((oToken != null) && (oTokenSecret != null)) {
                JsonOverHttp.use().iKnowTokens(oToken, oTokenSecret);
                return true;
            } else return false;
        } return true;
    }
        
    public static Uri requestForOAuthUri() throws OAuthMessageSignerException, OAuthNotAuthorizedException, 
                                           OAuthExpectationFailedException, OAuthCommunicationException {
        return JsonOverHttp.use().retreiveOAuthRequestToken(OAUTH_CALLBACK_URL);
    }
    
    public static void ensureOAuthCallbackAndSaveToken(Context context, Uri callbackUri) throws 
                                                       OAuthMessageSignerException, OAuthNotAuthorizedException, 
                                                       OAuthExpectationFailedException, OAuthCommunicationException, 
                                                       IllegalCallbackUriException {
        if (callbackUri.toString().startsWith(OAUTH_CALLBACK_URL.toString())) {
            final JsonOverHttp joh = JsonOverHttp.use(); 
            joh.retreiveOAuthAccessToken(callbackUri);
            final SharedPreferences storage = context.getSharedPreferences(OAUTH_API_PREFERENCES_ID, Context.MODE_PRIVATE);
            Editor editor = storage.edit();  
            editor.putString(OAUTH_TOKEN_PARAM, joh.getOAuthToken());
            editor.putString(OAUTH_TOKEN_SECRET_PARAM, joh.getOAuthTokenSecret());
            editor.commit();
        } else throw new IllegalCallbackUriException("Illegal callback Uri passed");
    }
    
    public static JSONObject advancedApi(final String method, final String object) 
                                                        throws ClientProtocolException, NoSuchAlgorithmException, 
                                                               OAuthMessageSignerException, OAuthExpectationFailedException, 
                                                               OAuthCommunicationException, JSONException, IOException, 
                                                               URISyntaxException, AdvancedApiCallError {
        
        return advancedApi(method, new ArrayList<NameValuePair>(), object);
    }
    
    public static JSONObject advancedApi(final String method, List<NameValuePair> params, final String object) 
                                                                    throws ClientProtocolException, NoSuchAlgorithmException, 
                                                                           OAuthMessageSignerException, OAuthExpectationFailedException, 
                                                                           OAuthCommunicationException, JSONException, IOException, 
                                                                           URISyntaxException, AdvancedApiCallError {
        params.add(new BasicNameValuePair("method", ADV_API_NAMESPACE + "." + method));
        params.add(new BasicNameValuePair("format", RESPONSE_FORMAT));
        JSONObject result = JsonOverHttp.use().signedAskForObject(new URI(VimeoConfig.VIMEO_ADVANCED_API_ROOT), params);
        if (!"ok".equals(result.getString("stat"))) {
            final JSONObject errObj = result.getJSONObject("err");
            throw new AdvancedApiCallError(errObj.getInt("code"), errObj.getString("msg"));
        }
        return result.getJSONObject(object);
    }    
    
    protected static String validateShortcutOrId(final String shortcut) { 
        if (!shortcut.matches("^[\\d\\w_]+$")) throw new IllegalArgumentException("Not correct schortcut or _ID: " + shortcut);
        return shortcut;
    }
    
    protected static String validateId(final String id) { 
        if (!id.matches("^\\d+$")) throw new IllegalArgumentException("Not correct _ID: " + id);
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
    
    @SuppressWarnings("serial")
    public static class IllegalCallbackUriException extends Exception {

        public IllegalCallbackUriException(String description) {
            super(description);
        }
        
    }
    
    @SuppressWarnings("serial")
    public static class AdvancedApiCallError extends Exception {

        public final int code;
        public final String message;
        
        public AdvancedApiCallError(int code, String message) {
            super(code + ": " + message);
            
            this.code = code;
            this.message = message;
        }
        
    }    
    
}
