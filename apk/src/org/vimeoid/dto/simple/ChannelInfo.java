/**
 * 
 */
package org.vimeoid.dto.simple;

import org.vimeoid.util.Item;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.simple</dd>
 * </dl>
 *
 * <code>ChannelInfo</code>
 *
 * <p>Vimeo channel representation</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 19, 2010 8:56:39 PM 
 *
 */
public class ChannelInfo implements Item {
    
    /* public static final Uri CONTENT_URI = Uri.parse("content://" + VimeoProvider.AUTHORITY +
                                                                   "/channels"); */

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.vimeo.channel";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.vimeo.channel";    

    public int id;
    public String name;
    public String description;
    
    public String logoHeader;
    public String pageUrl;
    public String rssUrl;
    
    public /*long*/ String createdOn;
    public int creatorId;
    public String creatorDisplayName;
    public String creatorProfileUrl;
    
    public int videosCount;
    public int subscribersCount;
    
}
