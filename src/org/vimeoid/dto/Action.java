/**
 * 
 */
package org.vimeoid.dto;

import org.vimeoid.VimeoProvider;

import android.net.Uri;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto</dd>
 * </dl>
 *
 * <code>Action</code>
 *
 * <p>Vimeo activity representation (currently not implemented)</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 19, 2010 9:21:38 PM 
 *
 */
public class Action {
    
    public static final Uri CONTENT_URI = Uri.parse("content://" + VimeoProvider.AUTHORITY +
                                                                   "/actions");
    
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.vimeo.action";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.vimeo.action";

}
