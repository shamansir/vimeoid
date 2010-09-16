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
import org.vimeoid.VimeoConfig;
import org.vimeoid.connection.VimeoVideoRunner.VideoLinkRequestException;
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
    
    private static final String ADV_API_NAMESPACE = "vimeo";
    
    public static final String RESPONSE_FORMAT = "json";
    public static final int ITEMS_PER_PAGE = 20;
    public static final int MAX_NUMBER_OF_PAGES = 3;
    
    public static final Uri OAUTH_CALLBACK_URL = Uri.parse("vimeoid://oauth.done");
    public static final String OAUTH_API_PREFERENCES_ID = "org.vimeoid.vimeoauth";
    
    private static final String OAUTH_TOKEN_PARAM = "user_oauth_public";
    private static final String OAUTH_TOKEN_SECRET_PARAM = "user_oauth_secret";
    
    private static final String PLAYER_URL = "http://player.vimeo.com/video/";
    
    private VimeoApi() { };

    /* ====================== General methods =============================== */
    
    public static boolean connectedToWeb(Context context) {
        Log.d(TAG, "Testing connection to web");
        //ConnectivityManager connection =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        /* return (connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||  
                   connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED); */
        
        return true; /* connection.getActiveNetworkInfo().isConnectedOrConnecting(); */
    }
    
    public static boolean vimeoSiteReachable(Context context) {
        Log.d(TAG, "Testing connection to Vimeo site");
        
        /* TODO: Requires android.permission.CHANGE_NETWORK_STATE
        ConnectivityManager connection =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final int vimeoHost = Utils.lookupHost("vimeo.com");
        return connection.requestRouteToHost(ConnectivityManager.TYPE_WIFI, vimeoHost) ||
        	   connection.requestRouteToHost(ConnectivityManager.TYPE_MOBILE, vimeoHost) ||
        	   connection.requestRouteToHost(ConnectivityManager.TYPE_WIMAX, vimeoHost)*/
        	   
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
    
    /* ====================== Simple API ==================================== */
    
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

	public static Uri getPlayUri(Video video) throws VideoLinkRequestException {
		//URLs are: http://vimeo.com/m/play_redirect?quality=mobile&clip_id=14294054
		//Log.d(TAG, "Uri for video " + video.title + ": " + VIDEO_STREAM_URL_PREFIX + "?quality=mobile&clip_id=" + video.id);
		// http://api.vimeo.com/moogaloop_api.swf?oauth_key=key&clip_id=13214161&width=480&height=270&fullscreen=0&autoplay=1
		// return Uri.parse(VIDEO_STREAM_URL_PREFIX + "?quality=mobile&clip_id=" + video.id + "&oauth_key=" + VimeoConfig.VIMEO_API_KEY);
		
	    return VimeoVideoRunner.askForVideoUri(video);
	}
	
	public static String getPlayerUrl(long videoId, int height) {
	    Log.d(TAG, "Construction player URL for video " + videoId);
	    return PLAYER_URL + videoId + "?title=0&byline=0&portrait=0&js_api=1&fp_version=10&height=" + height;
	}
    
}
