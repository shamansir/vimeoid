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
 * <code>GroupInfo</code>
 *
 * <p>Vimeo group representation</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 19, 2010 8:57:20 PM 
 *
 */
public class GroupInfo implements Item {
    
    /* public static final Uri CONTENT_URI = Uri.parse("content://" + VimeoProvider.AUTHORITY +
                                                                   "/groups"); */

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.vimeo.group";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.vimeo.group";    
    
    public int id;
    public String name;
    public String description;
    
    public String pageUrl;
    public String logoHeader;
    public String thumbnail;
    
    public /*long*/ String createdOn;
    public int creatorId;
    public String creatorDisplayName;
    public String creatorProfileUrl;
    
    public int membersCount;
    public int videosCount;
    
    public int filesCount;
    public int forumTopicsCount;
    public int eventsCount;
    public int upcomingEventsCount;

}
