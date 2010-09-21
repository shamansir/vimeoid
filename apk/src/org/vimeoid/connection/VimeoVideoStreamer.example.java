package org.vimeoid.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vimeoid.dto.simple.Video;

import android.util.Log;

public class VimeoVideoStreamer {
	
	public static final String TAG = "VimeoVideoStreamer"; 
	
	public static InputStream getVideoStream(Video video) throws VideoLinkRequestException { 
		return null;
	}

}
