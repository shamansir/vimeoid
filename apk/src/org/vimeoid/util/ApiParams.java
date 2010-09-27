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
 * <code>ApiParams</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 27, 2010 7:49:03 PM 
 *
 */
public class ApiParams {
    
    private final List<NameValuePair> params = new ArrayList<NameValuePair>();
    
    public ApiParams() { }
    
    public ApiParams param(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
        return this;
    }
    
    public List<NameValuePair> getValue() {
        return params;
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }

}
