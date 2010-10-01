/**
 * 
 */
package org.vimeoid.dto.advanced;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.advanced</dd>
 * </dl>
 *
 * <code>SortType</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 2, 2010 1:03:19 AM 
 *
 */
public enum SortType {
    
    NEWEST, OLDEST, MOST_PLAYED, MOST_COMMENTED, MOST_LIKED;
    
    @Override public String toString() { return this.name().toLowerCase(); };

}
