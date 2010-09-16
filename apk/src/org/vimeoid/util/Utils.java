/**
 * 
 */
package org.vimeoid.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.vimeoid.R;
import org.vimeoid.connection.ContentType;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.util</dd>
 * </dl>
 *
 * <code>Utils</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 3, 2010 10:06:22 PM 
 *
 */
public class Utils {
    
    public static final String VIDEO_TITLE_EXTRA = "v_videotitle";
    public static final String USERNAME_EXTRA = "v_username";
    
    public static List<NameValuePair> quickApiParams(String name1, String value1) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(name1, value1));        
        return params;
    }
    
    public static List<NameValuePair> quickApiParams(String name1, String value1,
                                                     String name2, String value2) {
        final List<NameValuePair> params = quickApiParams(name1, value1);
        params.add(new BasicNameValuePair(name2, value2));        
        return params;
    }
    
    public static List<NameValuePair> quickApiParams(String name1, String value1,
                                                     String name2, String value2,
                                                     String name3, String value3) {
        final List<NameValuePair> params = quickApiParams(name1, value1, 
                                                          name2, value2);
        params.add(new BasicNameValuePair(name3, value3));        
        return params;
    }
    
    public static List<NameValuePair> quickApiParams(String name1, String value1,
                                                     String name2, String value2,
                                                     String name3, String value3,
                                                     String name4, String value4) {
        final List<NameValuePair> params = quickApiParams(name1, value1, 
                                                          name2, value2,
                                                          name3, value3);
        params.add(new BasicNameValuePair(name4, value4));        
        return params;
    }
    
    public static String format(String source, String... params) {
        String result = source;
        int pos = 0;
        while (pos < params.length) {
            result = result.replaceAll("\\{" + params[pos++] + "\\}", params[pos++]);
        }
        return result;
    }
    
    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }    
    
    public static String adaptDuration(long duration) {
    	final long remainder = duration % 60; 
        return ((duration - remainder) / 60) + ":" + ((remainder < 10) ? ("0" + remainder) : remainder); 
    }
    
    public static String[] extractTags(String source) {
        final List<String> result = new LinkedList<String>();
        for (String tag: source.split(",")) {
            if (tag.trim().length() > 0) result.add(tag.trim());
        }
        return result.toArray(new String[result.size()]);
    }
    
    public static String adaptTags(String[] tags, String noneText, String delimiter) {
    	if (tags.length == 0) return noneText;
    	if (tags.length == 1) return tags[0];
    	final StringBuffer result = new StringBuffer();
    	for (int i = 0; i < (tags.length - 1); i++) {
    		result.append(tags[i]).append(delimiter);
    	}
    	result.append(tags[tags.length - 1]);
    	return result.toString();
    }    
    
    public static String adaptTags(String[] tags, String noneText) {
    	return adaptTags(tags, noneText, " / ");
    }
    
    public static int lookupHost(String hostname) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            return -1;
        }
        byte[] addrBytes;
        int addr;
        addrBytes = inetAddress.getAddress();
        addr = ((addrBytes[3] & 0xff) << 24)
                | ((addrBytes[2] & 0xff) << 16)
                | ((addrBytes[1] & 0xff) << 8)
                |  (addrBytes[0] & 0xff);
        return addr;
    }    
    
    public static String validateShortcutOrId(final String shortcut) { 
        if (!shortcut.matches("^[\\d\\w_]+$")) throw new IllegalArgumentException("Not correct schortcut or _ID: " + shortcut);
        return shortcut;
    }
    
    public static String validateId(final String id) { 
        if (!id.matches("^\\d+$")) throw new IllegalArgumentException("Not correct _ID: " + id);
        return id;
    }
    
    public static String validateActionName(String action) {
        if (!action.matches("^[\\w_]+$")) throw new IllegalArgumentException("Not correct action name: " + action);
        return action;
    }    
    
    public static String crop(String value, int howMuch) {
        return (value.length() <= howMuch) ? value : (value.substring(0, howMuch - 3) + "...");  
    }
    
    public static int drawableByContent(ContentType contentType) {
        if (contentType == null) return R.drawable.info;
        switch (contentType) {
            case ACTIVITY: return R.drawable.activity;
            case ALBUM:    return R.drawable.album;
            case CHANNEL:  return R.drawable.channel;
            case COMMENT:  return R.drawable.comment;
            case GROUP:    return R.drawable.group;
            case LIKE:     return R.drawable.like;
            case MESSAGE:  return R.drawable.message;
            case TAG:      return R.drawable.tag;
            case USER:     return R.drawable.contact;
            case VIDEO:    return R.drawable.video;
            default: return R.drawable.icon;
        }
    }

    public static String authorIdFromProfileUrl(String uploaderProfileUrl) {
        return uploaderProfileUrl.substring(17);
    }

}
