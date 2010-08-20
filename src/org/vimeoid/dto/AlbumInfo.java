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
 * <code>AlbumInfo</code>
 *
 * <p>Vimeo album information</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 20, 2010 9:55:45 AM 
 *
 */
public class AlbumInfo {
    
    public int id;
    public String title;
    public String description;
    
    public String pageUrl;
    public String thumbnail;    
    
    public /*long*/ String createdOn;
    public int creatorId;
    public String creatorDisplayName;
    public String creatorProfileUrl;    
    
    public int videosCount;    
    
    public /*long*/ String lastModifiedOn;
    
}
