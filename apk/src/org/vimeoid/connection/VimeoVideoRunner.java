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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.vimeoid.dto.simple.Video;
import org.vimeoid.util.Dialogs;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class VimeoVideoRunner {
	
	public static final String TAG = "VimeoVideoRunner"; 
	
	private static final String VIDEO_STREAM_URL_PREFIX = "http://vimeo.com/play_redirect";
	private static final String CALL_REFERRER = "http://vimeo.com/m/";
	
	private static final String QUALITY = "mobile";
	
	// FIXME: implement and do not pass context
	public static Uri askForVideoUri(Context context, Video video) throws VideoLinkRequestException {

		try {
			URL url = new URL(VIDEO_STREAM_URL_PREFIX + "?quality=mobile&clip_id=14654242");

			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestProperty("Host", "vimeo.com");
			urlc.setRequestProperty("Connection", "close");
			urlc.setRequestProperty("Referer", CALL_REFERRER);
			urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
            urlc.connect();
            
            Dialogs.makeToast(context, "RespCode: " + urlc.getResponseCode());
            
            Dialogs.makeToast(context, "Location: " + urlc.getHeaderField("Location"));
            
            if (urlc.getResponseCode() == 320) {
            	Log.d(TAG, "getResponseCode == 320");
            	Dialogs.makeToast(context, "Success!!");
                return null;
            }
        } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		try {			
			final HttpClient client = new DefaultHttpClient();			
			
			Log.d(TAG, "Creating Uri for video " + video.id);			
			
			final URI uri = new URI(VIDEO_STREAM_URL_PREFIX + "?quality=mobile&clip_id=14654242");			
			// final URI uri = new URI(VIDEO_STREAM_URL_PREFIX);			
			final HttpUriRequest request = new HttpGet(uri);
			
			/*final HttpParams params = new BasicHttpParams();
			params.setParameter("quality", QUALITY);
			params.setParameter("clip_id", String.valueOf(video.id));
			request.setParams(params); */
			
			request.setHeader("Host", "vimeo.com");
			request.setHeader("Connection", "close");	
			request.setHeader("Referer", CALL_REFERRER);		
			
			Log.d(TAG, "Request ready, performing response " + uri.toString());
			
			HttpResponse response = client.execute(request);
	        Log.d(TAG, "Uri call executed: " + uri.toString() + " [" + response.getStatusLine().getStatusCode() + "; " + response.getStatusLine().toString() + ']');
	        
	        Dialogs.makeToast(context, "Uri call executed: " + uri.toString() + " [" + response.getStatusLine().getStatusCode() + "; " + response.getStatusLine().toString() + ']');
			
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
