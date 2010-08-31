/**
 * 
 */
package org.vimeoid.dto.simple;

import org.vimeoid.util.Extractable;

import android.content.ContentValues;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.simple</dd>
 * </dl>
 *
 * <code>TagInfo</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 25, 2010 12:07:30 AM 
 *
 */
public class TagInfo implements Extractable {
    
    public String name;
    public String url;
    public int usageCount;
    
    public final static class FieldsKeys {
        
        public static final String NAME = "name";
        public static final String URL = "url";
        public static final String USAGE_COUNT = "usage";
        
    }

    /* (non-Javadoc)
     * @see org.vimeoid.Extractable#extract()
     */
    @Override
    public ContentValues extract() {
        final ContentValues result = new ContentValues();
        result.put(FieldsKeys.NAME, this.name);
        result.put(FieldsKeys.URL, this.url);
        result.put(FieldsKeys.USAGE_COUNT, this.usageCount);
        return result;
    }    

}
