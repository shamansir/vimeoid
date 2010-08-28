/**
 * 
 */
package org.vimeoid;

import android.content.ContentValues;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.simple</dd>
 * </dl>
 *
 * <code>Extractable</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 22, 2010 8:30:07 PM 
 *
 */
public interface Extractable {
    
    //public static enum ValueType { DOUBLE, FLOAT, INT, LONG, SHORT, STRING, ARRAY, OBJECT };
    
    public abstract ContentValues extract();
    
    // public abstract ContentValues extract(String[] projection);
        
}
