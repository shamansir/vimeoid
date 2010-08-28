/**
 * 
 */
package org.vimeoid.dto.simple;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.simple</dd>
 * </dl>
 *
 * <code>UserInfo</code>
 *
 * <p>Vimeo UserInfo information</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 19, 2010 8:54:34 PM 
 *
 */
public class UserInfo {
    
    /* public static final Uri CONTENT_URI = Uri.parse("content://" + VimeoUnauthorizedProvider.AUTHORITY +
                                                                   "/users"); */

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.vimeo.user";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.vimeo.user";    
    
    public int id;
    public String displayName;
    public /*long*/ String createdOn;
    public boolean fromStaff;
    public boolean isPlusMember;
    
    public String profileUrl;
    public String videosUrl;
    
    public int videosUploaded;
    public int videosAppearsIn;
    public int videosLiked;
    
    public int contactsCount;
    public int albumsCount;
    public int channelsCount;
    
    public String location;
    public String websiteUrl;
    public String biography; 
    
    public String smallPortraitUrl;
    public String mediumPortraitUrl;
    public String largePortraitUrl;

}