package org.vimeoid.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

		try {
			URL url = new URL(VIDEO_STREAM_URL_PREFIX + "?quality=" + QUALITY + "&clip_id=" + video.id);

			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestProperty("Host", "vimeo.com");
			urlc.setRequestProperty("Connection", "close");
			urlc.setRequestProperty("Referer", CALL_REFERRER);
			urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
            urlc.connect();
            
			// FIXME: re-check User-Agent            
            
            Log.d(TAG, "RespCode: " + urlc.getResponseCode());
            
            Log.d(TAG, "Location: " + urlc.getHeaderField("Location"));
            
            if (urlc.getResponseCode() == 320) {
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
			final HttpUriRequest request = new HttpGet(uri);
			
			/*final HttpParams params = new BasicHttpParams();
			params.setParameter("quality", QUALITY);
			params.setParameter("clip_id", String.valueOf(video.id));
			request.setParams(params); */
			
			request.setHeader("Host", "vimeo.com");
			request.setHeader("Connection", "close");	
			request.setHeader("Referer", CALL_REFERRER);	
			
			// FIXME: re-check User-Agent
			
			Log.d(TAG, "Request ready, performing response " + uri.toString());
			
			HttpResponse response = client.execute(request);
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
