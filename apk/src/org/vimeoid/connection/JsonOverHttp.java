/**
 * 
 */
package org.vimeoid.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.CharBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static String oauthToken = null;
    private static String oauthTokenSecret = null;
    
    public static String consumerKey = null;
    public static String consumerSecret = null;    
    
    public static JSONArray askForArray(final URI uri) throws JSONException, ClientProtocolException, IOException {
        return askForArray(uri, null);
    }
    
    public static JSONArray askForArray(final URI uri, final Map<String, String> params) throws JSONException, ClientProtocolException, IOException {
        return new JSONArray(execute(uri, params));
    } 
    
    public static JSONObject askForObject(final URI uri) throws JSONException, ClientProtocolException, IOException {
        return askForObject(uri, null);
    }
    
    public static JSONObject askForObject(final URI uri, final Map<String, String> params) throws JSONException, ClientProtocolException, IOException {
        return new JSONObject(execute(uri, params));
    }
    
    protected static String execute(URI uri, Map<String, String> paramsMap) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(uri);
        if (paramsMap != null) {
            HttpParams params = new BasicHttpParams();
            for (Map.Entry<String, String> param: paramsMap.entrySet()) {
                params.setParameter(param.getKey(), param.getValue());
            }
            request.setParams(params);            
        }
        
        InputStream instream = null;        
        try {
            HttpResponse response = client.execute(request);
            Log.i(TAG,"Uri call executed: " + uri.toString() + '[' + response.getStatusLine().toString() + ']');
            
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                instream = entity.getContent();
                String result = convertStreamToString(instream);                
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

    private static String convertStreamToString(InputStream instream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        while ((line = reader.readLine()) != null) {
                sb.append(line + NL);
        }
        return sb.toString();
    }
    
    public static void initOuathConfiguration(final String consumerKey, final String consumerSecret) {
        if ((JsonOverHttp.consumerKey != null) || (JsonOverHttp.consumerSecret != null)) {
            Log.w(TAG, "Consumer Key and Secret were already set");
        }
        JsonOverHttp.consumerKey = consumerKey;
        JsonOverHttp.consumerSecret = consumerSecret;
    }
    
    public static void setOuathTokenAndSecret(final String token, final String tokenSecret) {
        if (JsonOverHttp.oauthToken != null) {
            Log.w(TAG, "OAuth Token was already set");
        }
        if (JsonOverHttp.oauthTokenSecret != null) {
            Log.w(TAG, "OAuth Token Secret was already set");
        }        
        JsonOverHttp.oauthToken = token;
        JsonOverHttp.oauthTokenSecret = tokenSecret;
    }
    
    protected static String percentEncode(String source) {
        final CharBuffer result = CharBuffer.allocate(source.length() << 2);
        //final CharBuffer result = CharBuffer.wrap(source.toCharArray());
        for (char sym: source.toCharArray()) {
            if (percentsMap.containsKey(sym)) result.append(percentsMap.get(sym));
            else result.append(sym);
        }
        return result.toString();
    }
    
    protected static String executeWithOauth(final URI uri, Map<String, String> paramsMap) throws ClientProtocolException, IOException, NoSuchAlgorithmException {
        paramsMap.put("oauth_consumer_key", consumerKey);
        paramsMap.put("oauth_nonce", ""); // random string, unique to call
        paramsMap.put("oauth_signature_method", "HMAC-SHA1");
        paramsMap.put("oauth_timestamp", String.valueOf((long) (System.currentTimeMillis() / 1000L)));
        paramsMap.put("oauth_version", "1.0");
        
        final Map<String, String> sortedParams = new TreeMap<String, String>(paramsMap);
        
        final StringBuffer baseString = new StringBuffer();
        baseString.append("GET&");
        baseString.append(percentEncode(uri.toString())).append('&');
        for (Map.Entry<String, String> param: sortedParams.entrySet()) {
            baseString.append(percentEncode(
                              percentEncode(param.getKey()) + '=' + 
                              percentEncode(param.getValue()) + '&'));
        }
        baseString.delete(baseString.length() - 3, baseString.length()); // remove last unneeded ampersand encoded
        
        final String baseKey = consumerSecret + '&' + ((oauthToken != null) ? oauthToken : "");
        
        SecretKeySpec keySpec = new SecretKeySpec(baseKey.getBytes(), "HmacSHA1"); 
        Mac mac = Mac.getInstance(keySpec.getAlgorithm());
        try {
            mac.init(keySpec);
        } catch (InvalidKeyException ike) {
            Log.e(TAG, "Invalid key provided in OAuth call: " + ike.getLocalizedMessage());
        }
        mac.update(baseString.toString().getBytes());
        
        final String signature = new String(mac.doFinal());
        paramsMap.put("oauth_signature", signature);
        
        // TODO: Base64.
        
        return execute(uri, paramsMap);
    }
       
    private final static Map<String, String> percentsMap = new HashMap<String, String>();
    static {
        percentsMap.put("!", "%21");
        percentsMap.put("*", "%2A");
        percentsMap.put("'", "%27");
        percentsMap.put("(", "%28");
        percentsMap.put(")", "%29");
        percentsMap.put(";", "%3B");
        percentsMap.put(":", "%3A");
        percentsMap.put("@", "%40");
        percentsMap.put("&", "%26");
        percentsMap.put("=", "%3D");
        percentsMap.put("+", "%2B");
        percentsMap.put("$", "%24");
        percentsMap.put(",", "%2C");
        percentsMap.put("/", "%2F");
        percentsMap.put("?", "%3F");
        percentsMap.put("%", "%25");
        percentsMap.put("#", "%23");
        percentsMap.put("[", "%5B");
        percentsMap.put("]", "%5D");
    }    

}
