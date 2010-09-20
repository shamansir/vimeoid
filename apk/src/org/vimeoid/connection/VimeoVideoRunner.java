package org.vimeoid.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vimeoid.dto.simple.Video;

import android.net.Uri;
import android.util.Log;

public class VimeoVideoRunner {
	
	public static final String TAG = "VimeoVideoRunner"; 
	
	private static final String VIDEO_STREAM_URL_PREFIX = "http://vimeo.com/play_redirect";
	private static final String CALL_REFERRER = "http://vimeo.com/m/";
	
	private static final String QUALITY = "mobile";
	
	public static Uri askForVideoUri(Video video) throws VideoLinkRequestException {

		InputStream is;
		try {
			URL url = new URL(VIDEO_STREAM_URL_PREFIX + "?quality=" + QUALITY + "&clip_id=" + video.id);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
			conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.1; en-us; Nexus One Build/ERD62) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17"); 			
			conn.setRequestProperty("Host", "vimeo.com");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			conn.setRequestProperty("Connection", "close");
			conn.setRequestProperty("Referer", CALL_REFERRER);
			conn.setRequestProperty("Connection","Keep-Alive");
			conn.setDoOutput(true); 
			conn.setUseCaches(true);			
			//urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
			conn.connect();
			is = conn.getInputStream();
            String res = conn.getURL().toString();
            conn.disconnect();
            
			// FIXME: re-check User-Agent            
            
            Log.d(TAG, "RespCode: " + conn.getResponseCode());
            
            Log.d(TAG, "Location: " + conn.getHeaderField("Location"));
            
			HttpURLConnection.setFollowRedirects(true);
            
        	//is.close();
			
            if (conn.getResponseCode() == 302) {
            	Log.d(TAG, "Success!!");
                return null;
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
		
		try {			
			
			final HttpClient client = new DefaultHttpClient();			
			
			Log.d(TAG, "Creating Uri for video " + video.id);			
			
			final URI uri = new URI(VIDEO_STREAM_URL_PREFIX + "?quality=" + QUALITY + "&clip_id=" + video.id);
			// final URI uri = new URI(VIDEO_STREAM_URL_PREFIX);			
			final HttpUriRequest request = new HttpHead(uri);
			
			/*final HttpParams params = new BasicHttpParams();
			params.setParameter("quality", QUALITY);
			params.setParameter("clip_id", String.valueOf(video.id));
			request.setParams(params); */
			
			request.setHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.1; en-us; Nexus One Build/ERD62) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
			request.setHeader("Host", "vimeo.com");
			request.setHeader("Connection", "Keep-Alive");	
			request.setHeader("Referer", CALL_REFERRER);	
			
			// FIXME: re-check User-Agent
			
			Log.d(TAG, "Request ready, performing response " + uri.toString());
			
			HttpResponse response = client.execute(request);
			//is = response.getEntity()
	        Log.d(TAG, "Uri call executed: " + uri.toString() + " [" + response.getStatusLine().getStatusCode() + "; " + response.getStatusLine().toString() + ']');
			
	        if (response.getStatusLine().getStatusCode() == 302) Log.d(TAG, "Success!!");
	        
	        Header[] location = response.getHeaders("Location");
	        
	        for (Header locHeader: location) {
	        	Log.d(TAG, "LOCATION/" + locHeader);
	        }
	        
	        return null;    	
			
		} catch (URISyntaxException use) {
			throw new VideoLinkRequestException("URI creation failed : " + use.getLocalizedMessage());
		} catch (ClientProtocolException cpe) {
			throw new VideoLinkRequestException("Client call failed : " + cpe.getLocalizedMessage());
		} catch (IOException ioe) {
			throw new VideoLinkRequestException("Connection failed : " + ioe.getLocalizedMessage());
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class VideoLinkRequestException extends Exception {
		
		public VideoLinkRequestException(String why) {
			super(why);
		}
		
	}

}
