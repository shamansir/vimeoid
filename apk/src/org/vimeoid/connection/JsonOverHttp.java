/**
 * 
 */
package org.vimeoid.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.connection</dd>
 * </dl>
 *
 * <code>JsonOverHttp</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 28, 2010 1:53:19 PM 
 *
 */
public class JsonOverHttp {
    
    public static final String TAG = "JsonOverHttp";
    
    private static final String NL = System.getProperty("line.separator");

    public static OAuthConsumer consumer = null;
    public static OAuthProvider provider = null;    
    
    public static JSONArray askForArray(final URI uri) throws JSONException, ClientProtocolException, IOException {
        return askForArray(uri, null);
    }
    
    public static JSONArray askForArray(final URI uri, final HttpParams params) throws JSONException, ClientProtocolException, IOException {
        return new JSONArray(execute(uri, params));
    } 
    
    public static JSONObject askForObject(final URI uri) throws JSONException, ClientProtocolException, IOException {
        return askForObject(uri, null);
    }
    
    public static JSONObject askForObject(final URI uri, final HttpParams params) throws JSONException, ClientProtocolException, IOException {
        return new JSONObject(execute(uri, params));
    }
    
    protected static String execute(final URI uri, final HttpParams params) throws ClientProtocolException, IOException {
    	return execute(uri, params, false);
    }
    
    protected static String execute(final HttpRequestBase request) throws ClientProtocolException, IOException {
    	final String uri = request.getURI().toString();
    	
        HttpClient client = new DefaultHttpClient();
        InputStream instream = null;        
        try {
            HttpResponse response = client.execute(request);
            Log.i(TAG, "Uri call executed: " + uri.toString() + '[' + response.getStatusLine().toString() + ']');
            
            if (response.getStatusLine().getStatusCode() != 200) {
            	Log.e(TAG, "Response code for " + uri + " was not 200: " + response.getStatusLine().getReasonPhrase());
            	throw new IOException("Response code for " + uri + " was not 200: " + response.getStatusLine().getReasonPhrase());
            }
            
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                instream = entity.getContent();
                String result = convertStreamToString(instream);
                //entity.consumeContent();
                instream.close();     
                return result;
            }
        } finally {
            if (instream != null) {
                try {
                    instream.close();             
                } catch (IOException ioe) {
                    Log.e(TAG, "IOException while calling " + uri.toString() + ": " + ioe.getLocalizedMessage());
                    ioe.printStackTrace();
                }
            }    
        }
        return null;    	
    }
    
    protected static String execute(final URI uri, HttpParams params, final boolean usePost) throws ClientProtocolException, IOException {
        HttpRequestBase request = usePost ? new HttpPost(uri) : new HttpGet(uri);
        
        if (params != null) request.setParams(params);
        
        return execute(request);
    }

    private static String convertStreamToString(final InputStream instream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        while ((line = reader.readLine()) != null) {
                sb.append(line + NL);
        }
        return sb.toString();
    }
    
    public static void initOuathConfiguration(OAuthConsumer consumer, OAuthProvider provider) {
        if ((JsonOverHttp.consumer != null) || (JsonOverHttp.provider != null)) {
            Log.w(TAG, "OAuth Consumer or Provider was already set");
        }
        JsonOverHttp.consumer = consumer;
        JsonOverHttp.provider = provider;
    }
    
    public static boolean isOauthInitialized() {
    	return (JsonOverHttp.consumer != null) && (JsonOverHttp.provider != null);
    }    
    
    public static String extractOauthTokenAndSave(final Uri uri) throws OAuthMessageSignerException, OAuthNotAuthorizedException, 
    																    OAuthExpectationFailedException, OAuthCommunicationException {
    	if (JsonOverHttp.consumer == null) throw new IllegalStateException("OAuth Consumer is not set, call initOuathConfiguration before");
        if (JsonOverHttp.provider == null) throw new IllegalStateException("OAuth provider is not ready, call initOuathConfiguration befor");
        
        if (uri != null) {  
        	String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);  
        	provider.retrieveAccessToken(consumer, verifier);
        	return consumer.getToken();
        }  else return null;
    }
    
    protected static String executeWithOauth(final URI uri, List<NameValuePair> params) throws ClientProtocolException, IOException, NoSuchAlgorithmException, 
    																						   OAuthMessageSignerException, OAuthExpectationFailedException, 
    																						   OAuthCommunicationException {
        if (JsonOverHttp.consumer == null) throw new IllegalStateException("OAuth Consumer is not set, call initOuathConfiguration");
        if (JsonOverHttp.provider == null) throw new IllegalStateException("OAuth provider is not ready, call initOuathConfiguration");
        
        HttpPost post = new HttpPost(uri);
        
        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
        
        consumer.sign(post);
        return execute(post);          
    }
       
}
