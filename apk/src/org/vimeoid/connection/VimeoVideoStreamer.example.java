package org.vimeoid.connection;

import java.io.InputStream;

import android.util.Log;
import org.vimeoid.dto.simple.Video;

public class VimeoVideoStreamer {
	
	public static final String TAG = "VimeoVideoStreamer"; 
	
	public static InputStream getVideoStream(long videoId) throws VideoLinkRequestException { 
		// this method is removed from repository because it is not allowed by vimeo staff
		// to share this method with developers :(.
		Log.e(TAG, "This function is not supported in open-source version, please consult author");
		return null;
	}
	
	public static long getLastContentLength() { return -1; }

}