package org.vimeoid.connection;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.vimeoid.dto.simple.Video;

import android.net.Uri;
import android.util.Log;

public class VimeoVideoRunner {
	
	public static final String TAG = "VimeoVideoRunner"; 
	
	private static final String VIDEO_STREAM_URL_PREFIX = "http://vimeo.com/play_redirect";
	private static final String CALL_REFERRER = "http://vimeo.com/m/";
	
	private static final String QUALITY = "mobile";
	
	public static Uri askForVideoUri(Video video) throws VideoLinkRequestException {
		final HttpClient client = new DefaultHttpClient();
		
		Log.d(TAG, "Creating Uri for video " + video.id);
		
		try {
			final URI uri = new URI(VIDEO_STREAM_URL_PREFIX);			
			final HttpUriRequest request = new HttpGet(uri);
			
			final HttpParams params = new BasicHttpParams();
			params.setParameter("quality", QUALITY);
			params.setParameter("clip_id", String.valueOf(video.id));
			request.setParams(params);
			
			request.setHeader("Referer", CALL_REFERRER);
			
			Log.d(TAG, "Request ready, performing response " + uri.toString() + " " + params.toString());
			
			HttpResponse response = client.execute(request);
	        Log.d(TAG, "Uri call executed: " + uri.toString() + " " + params.toString() + " [" + response.getStatusLine().getStatusCode() + "; " + response.getStatusLine().toString() + ']');
			
	        // check if 302
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
