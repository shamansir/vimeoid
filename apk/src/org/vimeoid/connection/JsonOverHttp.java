/**
 * 
 */
package org.vimeoid.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;

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
    
    public static JSONArray askForArray(URI uri) {
        return askForArray(uri, null);
    }
    
    public static JSONArray askForArray(URI uri, Map<String, String> params) {
        try {
            return new JSONArray(execute(uri, params));
        } catch (JSONException jsone) {
            Log.e(TAG, "JSONException while calling " + uri.toString() + ": " + jsone.getLocalizedMessage());
            jsone.printStackTrace();
            return null;
        }
    } 
    
    public static JSONObject askForObject(URI uri) throws JSONException {
        return askForObject(uri, null);
    }
    
    public static JSONObject askForObject(URI uri, Map<String, String> params) {
        try {
            return new JSONObject(execute(uri, params));
        } catch (JSONException jsone) {
            Log.e(TAG, "JSONException while calling " + uri.toString() + ": " + jsone.getLocalizedMessage());
            jsone.printStackTrace();
            return null;
        }
    }    
    
    public static String execute(URI uri, Map<String, String> paramsMap) {
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
        } catch (ClientProtocolException cpe) {
            Log.e(TAG, "Protocol while calling " + uri.toString() + ": " + cpe.getLocalizedMessage());
            cpe.printStackTrace();            
        } catch (IOException ioe) {
            Log.e(TAG, "IOException while calling " + uri.toString() + ": " + ioe.getLocalizedMessage());
            ioe.printStackTrace();
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

}
