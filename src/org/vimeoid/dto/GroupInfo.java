/**
 * 
 */
package org.vimeoid.dto;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto</dd>
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
public class GroupInfo {
    
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
