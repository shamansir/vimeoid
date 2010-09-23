package org.vimeoid.connection;

import java.io.InputStream;

import org.vimeoid.dto.simple.Video;

public class VimeoVideoStreamer {
	
	public static final String TAG = "VimeoVideoStreamer"; 
	
	public static InputStream getVideoStream(long videoId) throws VideoLinkRequestException { 
		// this method is removed from repository because it is not allowed by vimeo staff
		// to share this method with developers :(. 
		return null;
	}
	
	public static long getReceivedStreamLength() { }

}