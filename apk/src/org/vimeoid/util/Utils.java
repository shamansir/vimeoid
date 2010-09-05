/**
 * 
 */
package org.vimeoid.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
    
    // TODO: Optimize these methods
    
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
    
}
