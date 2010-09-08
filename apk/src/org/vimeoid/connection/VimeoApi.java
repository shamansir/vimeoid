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
import android.content.UriMatcher;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
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
import org.vimeoid.VimeoConfig;
import org.vimeoid.dto.simple.Action;
import org.vimeoid.dto.simple.AlbumInfo;
import org.vimeoid.dto.simple.ChannelInfo;
import org.vimeoid.dto.simple.GroupInfo;
import org.vimeoid.dto.simple.UserInfo;
import org.vimeoid.dto.simple.Video;

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

    /* ====================== General methods =============================== */
    
    public static boolean connectedToWeb(Context context) {
        Log.d(TAG, "Testing connection to web");
        ConnectivityManager connection =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connection.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||  
                connection.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING);
    }
    
    public static boolean vimeoSiteReachable() {
        Log.d(TAG, "Testing connection to Vimeo site");
        return true;
    }
    
    public static void forgetCredentials() {
        // FIXME: implement
    }
    
    /* ====================== OAuth Helpers ================================= */
    
    public static boolean userLoggedIn() {
        // FIXME: implement
        return true;
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
    
    /* ====================== Advanced API: Calls =========================== */
    
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
    
    /* ====================== Simple API: Resolving URIs ==================== */
    
    public static class ApiCallInfo {
        
        public URI fullCallUrl;
        public StringBuffer apiUrlPart;
        public String subject;
        public String action;
        public ContentType subjectType;
        public ContentType resultType;
        public boolean multipleResult;
        
        public static class ExtrasKeys {
            public static final String FULL_CALL_URL = "aci_fullCallUrl";
            public static final String API_URL_PART = "aci_apiUrlPart";
            public static final String SUBJECT = "aci_subject";
            public static final String ACTION = "aci_action";
            public static final String SUBJECT_TYPE = "aci_subjectType";
            public static final String RESULT_TYPE = "aci_resultType";
            public static final String MULTIPLE_RESULT = "aci_multipleResult";
        }
        
        public void writeToExtras(Bundle bundle) {
            bundle.putString(ExtrasKeys.FULL_CALL_URL, fullCallUrl.toString());
            bundle.putString(ExtrasKeys.API_URL_PART, apiUrlPart.toString());
            bundle.putString(ExtrasKeys.SUBJECT, subject);
            bundle.putString(ExtrasKeys.ACTION, action);
            bundle.putString(ExtrasKeys.SUBJECT_TYPE, subjectType.getAlias());
            bundle.putString(ExtrasKeys.RESULT_TYPE, resultType.getAlias());
            bundle.putBoolean(ExtrasKeys.MULTIPLE_RESULT, multipleResult);
        }
        
        public static ApiCallInfo extractFromExtras(Bundle bundle) {
            final ApiCallInfo result = new ApiCallInfo();
            try {
                result.fullCallUrl = new URI(bundle.getString(ExtrasKeys.FULL_CALL_URL));
            } catch (URISyntaxException use) {
                Log.e(TAG, "Failed to extract URI from bundle " + bundle.getString(ExtrasKeys.FULL_CALL_URL));
                use.printStackTrace();
            }
            result.apiUrlPart = new StringBuffer(bundle.getString(ExtrasKeys.API_URL_PART));
            result.subject = bundle.getString(ExtrasKeys.SUBJECT);
            result.action = bundle.getString(ExtrasKeys.ACTION);
            result.subjectType = ContentType.fromAlias(bundle.getString(ExtrasKeys.SUBJECT_TYPE));
            result.resultType = ContentType.fromAlias(bundle.getString(ExtrasKeys.RESULT_TYPE));
            result.multipleResult = bundle.getBoolean(ExtrasKeys.MULTIPLE_RESULT);
            return result;
        }

    }
    
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
     * @return API URL info, containing <code>fullCallUrl</code>, pointing to the Vimeo Simple API method corresponding to this contentUri
     */    
    public static ApiCallInfo resolveUriForSimpleApi(Uri contentUri) {
        try {
            final ApiCallInfo result = getUrlInfoForSimpleApi(contentUri);
            result.fullCallUrl = new URI(VimeoConfig.VIMEO_SIMPLE_API_CALL_PREFIX + '/' + result.apiUrlPart.toString());
            return result;
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
     * 
     * @return API URL info, containing <code>apiUrlPart</code> buffer with the resulting URL part written inside
     */
    protected static ApiCallInfo getUrlInfoForSimpleApi(Uri contentUri) {
        final List<String> segments = contentUri.getPathSegments();
        final StringBuffer urlBuffer = new StringBuffer();
        Log.d(TAG, "generating API Call URL for URI " + contentUri.toString());
        
        final ApiCallInfo urlInfo = new ApiCallInfo();
        urlInfo.subjectType = ContentType.fromAlias(segments.get(0));
        switch (urlInfo.subjectType) {
            case USER: {
                    urlInfo.subject = validateShortcutOrId(segments.get(1));
                    urlInfo.action = resolveSimpleUserAction(segments.get(2));
                    urlBuffer.append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;
            case VIDEO: {
                    urlInfo.subject = validateId(segments.get(1));
                    urlInfo.action = null;
                    urlBuffer.append("video/").append(urlInfo.subject);
                } break;
            case GROUP: {
                    urlInfo.subject = validateShortcutOrId(segments.get(1));
                    urlInfo.action = resolveSimpleGroupAction(segments.get(2));
                    urlBuffer.append("group/").append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;
            case CHANNEL: {
                    urlInfo.subject = validateShortcutOrId(segments.get(1));
                    urlInfo.action = resolveSimpleChannelAction(segments.get(2));
                    urlBuffer.append("channel/").append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;
            case ALBUM: {
                    urlInfo.subject = validateId(segments.get(1));
                    urlInfo.action = resolveSimpleAlbumAction(segments.get(2));
                    urlBuffer.append("album/").append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;          
            case ACTIVITY: {
                    urlInfo.subject = validateShortcutOrId(segments.get(1));
                    urlInfo.action = resolveSimpleActivityAction(segments.get(2));
                    urlBuffer.append("activity/").append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;
        }
        
        urlBuffer.append('.').append(RESPONSE_FORMAT);
        if (contentUri.getQueryParameter("page") != null) {
            urlBuffer.append("?page=").append(contentUri.getQueryParameter("page"));
        }
        
        Log.d(TAG, "generated result: " + urlBuffer.toString());
        urlInfo.apiUrlPart = urlBuffer;
        urlInfo.multipleResult = getReturnsMultipleResults(contentUri);
        urlInfo.resultType = getReturnedContentType(contentUri);
        return urlInfo;
    }
    
    public static String getSimpleApiCallDescription(ApiCallInfo callInfo) {
        return callInfo.resultType.getAlias() + ": " + callInfo.subject + ' ' + callInfo.subjectType.getAlias();
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
    
    /* ====================== Simple API: UriMatcher ======================== */
    
    public static final String SIMPLE_API_AUTHORITY = "org.vimeoid.simple.provider";
    
    private static final UriMatcher simpleApiUriMatcher;
    
    private static final int ACTIONS_LIST_URI_TYPE   = 1;
    private static final int ACTION_SINGLE_URI_TYPE  = 2;
    private static final int ALBUMS_LIST_URI_TYPE    = 3;
    private static final int ALBUM_SINGLE_URI_TYPE   = 4;
    private static final int CHANNELS_LIST_URI_TYPE  = 5;
    private static final int CHANNEL_SINGLE_URI_TYPE = 6;
    private static final int GROUPS_LIST_URI_TYPE    = 7;
    private static final int GROUP_SINGLE_URI_TYPE   = 8;
    private static final int USERS_LIST_URI_TYPE     = 9;
    private static final int USER_SINGLE_URI_TYPE    = 10;
    private static final int VIDEOS_LIST_URI_TYPE    = 11;
    private static final int VIDEO_SINGLE_URI_TYPE   = 12;    
    
    static {
        simpleApiUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/info",     USER_SINGLE_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/videos",   VIDEOS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/likes",    VIDEOS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/appears",  VIDEOS_LIST_URI_TYPE); // videos where user appears
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/all",      VIDEOS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/subscr",   VIDEOS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/albums",   ALBUMS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/channels", CHANNELS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/groups",   GROUPS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/ccreated", VIDEOS_LIST_URI_TYPE); // videos that user's contacts created
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "user/*/clikes",   VIDEOS_LIST_URI_TYPE); // videos that user's contacts liked
        
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "video/#", VIDEO_SINGLE_URI_TYPE);
        
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "group/*/info",   GROUP_SINGLE_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "group/*/videos", VIDEOS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "group/*/users",  USERS_LIST_URI_TYPE);
        
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "channel/*/info",   CHANNEL_SINGLE_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "channel/*/videos", VIDEOS_LIST_URI_TYPE);
        
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "album/#/info",   ALBUM_SINGLE_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "album/#/videos", VIDEOS_LIST_URI_TYPE);
        
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "activity/*/did",       ACTIONS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "activity/*/happened",  ACTIONS_LIST_URI_TYPE);
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "activity/*/cdid",      ACTIONS_LIST_URI_TYPE); // contacts did
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "activity/*/chappened", ACTIONS_LIST_URI_TYPE); // happened to contacts
        simpleApiUriMatcher.addURI(SIMPLE_API_AUTHORITY, "activity/*/edid",      ACTIONS_LIST_URI_TYPE); // everyone did
    }
    
    public static String getSimpleApiReturnType(Uri uri) {
        switch (simpleApiUriMatcher.match(uri)) {
            case ACTIONS_LIST_URI_TYPE:   return Action.CONTENT_TYPE;
            case ACTION_SINGLE_URI_TYPE:  return Action.CONTENT_ITEM_TYPE;
            case ALBUMS_LIST_URI_TYPE:    return AlbumInfo.CONTENT_TYPE;
            case ALBUM_SINGLE_URI_TYPE:   return AlbumInfo.CONTENT_ITEM_TYPE;
            case CHANNELS_LIST_URI_TYPE:  return ChannelInfo.CONTENT_TYPE;
            case CHANNEL_SINGLE_URI_TYPE: return ChannelInfo.CONTENT_ITEM_TYPE;
            case GROUPS_LIST_URI_TYPE:    return GroupInfo.CONTENT_TYPE;
            case GROUP_SINGLE_URI_TYPE:   return GroupInfo.CONTENT_ITEM_TYPE;
            case USERS_LIST_URI_TYPE:     return UserInfo.CONTENT_TYPE;
            case USER_SINGLE_URI_TYPE:    return UserInfo.CONTENT_ITEM_TYPE;
            case VIDEOS_LIST_URI_TYPE:    return Video.CONTENT_TYPE;
            case VIDEO_SINGLE_URI_TYPE:   return Video.CONTENT_ITEM_TYPE;
            default: throw new IllegalArgumentException("Unknown URI type: " + uri);
        }
    }

    public static ContentType getReturnedContentType(Uri uri) {
        switch (simpleApiUriMatcher.match(uri)) {
            case ACTIONS_LIST_URI_TYPE:
            case ACTION_SINGLE_URI_TYPE:  return ContentType.ACTIVITY;
            case ALBUMS_LIST_URI_TYPE:
            case ALBUM_SINGLE_URI_TYPE:   return ContentType.ALBUM;
            case CHANNELS_LIST_URI_TYPE:
            case CHANNEL_SINGLE_URI_TYPE: return ContentType.CHANNEL;
            case GROUPS_LIST_URI_TYPE:
            case GROUP_SINGLE_URI_TYPE:   return ContentType.GROUP;
            case USERS_LIST_URI_TYPE:
            case USER_SINGLE_URI_TYPE:    return ContentType.USER;
            case VIDEOS_LIST_URI_TYPE:
            case VIDEO_SINGLE_URI_TYPE:   return ContentType.VIDEO;
            default: throw new IllegalArgumentException("Unknown URI type: " + uri);
        }        
    }
    
    public static boolean getReturnsMultipleResults(Uri uri) {
        switch (simpleApiUriMatcher.match(uri)) {
            case ACTIONS_LIST_URI_TYPE:
            case ALBUMS_LIST_URI_TYPE:
            case CHANNELS_LIST_URI_TYPE:
            case GROUPS_LIST_URI_TYPE:
            case USERS_LIST_URI_TYPE:
            case VIDEOS_LIST_URI_TYPE:    return true;            
            case ACTION_SINGLE_URI_TYPE:  
            case ALBUM_SINGLE_URI_TYPE:
            case CHANNEL_SINGLE_URI_TYPE:
            case GROUP_SINGLE_URI_TYPE:
            case USER_SINGLE_URI_TYPE:
            case VIDEO_SINGLE_URI_TYPE:   return false;
            default: throw new IllegalArgumentException("Unknown URI type: " + uri);
        }        
    }    
    
}
