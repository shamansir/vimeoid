/**
 * 
 */
package org.vimeoid.test;

import junit.framework.Assert;

import org.vimeoid.VimeoSimpleApiProvider;
import org.vimeoid.connection.ContentType;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.VimeoConfig;

import android.net.Uri;
import android.net.Uri.Builder;
import android.test.AndroidTestCase;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid-test</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.test</dd>
 * </dl>
 *
 * <code>VimeoApiUtilsTest</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 25, 2010 11:20:52 PM 
 *
 */
public class VimeoApiUtilsTest extends AndroidTestCase {
    
    private static final String[] testUsers = {"foo_man", "barman", "user3801", "17318"};
    private static final String[] testVideos = {"236372", "371445", "38529913", "39203562" };
    private static final String[] testGroups = {"test_group", "group", "group1616", "3123892"};
    private static final String[] testChannels = {"test_channel", "channel", "channel601", "4320193"};
    private static final String[] testAlbums = {"18562", "1203", "3373626198829", "0100101"};
    
    final Builder builder = new Uri.Builder().scheme("content").authority(VimeoSimpleApiProvider.AUTHORITY);    
    
    public void testSimpleApiUserMethods() throws Throwable {
        
        for (String userId: testUsers) {
            
            testProducesCorrectUrl(userId + "/info", // Vimeo API URL segment
                                   ContentType.USER, false,
                                   builder.path("user/" + userId + "/info").build()); // URI path
            
            testProducesCorrectUrl(userId + "/videos", // Vimeo API URL segment
                                   ContentType.VIDEO, true,
                                   builder.path("user/" + userId + "/videos").build()); // URI path
            
            testProducesCorrectUrl(userId + "/likes", // Vimeo API URL segment
                                   ContentType.VIDEO, true,
                                   builder.path("user/" + userId + "/likes").build()); // URI path
            
            testProducesCorrectUrl(userId + "/appears_in", // Vimeo API URL segment
                                   ContentType.VIDEO, true,
                                   builder.path("user/" + userId + "/appears").build()); // URI path            

            testProducesCorrectUrl(userId + "/all_videos", // Vimeo API URL segment
                                   ContentType.VIDEO, true,
                                   builder.path("user/" + userId + "/all").build()); // URI path
            
            testProducesCorrectUrl(userId + "/appears_in", // Vimeo API URL segment
                                   ContentType.VIDEO, true,
                                   builder.path("user/" + userId + "/appears").build()); // URI path
            
            testProducesCorrectUrl(userId + "/subscriptions", // Vimeo API URL segment
                                   ContentType.VIDEO, true,
                                   builder.path("user/" + userId + "/subscr").build()); // URI path
            
            testProducesCorrectUrl(userId + "/albums", // Vimeo API URL segment
                                   ContentType.ALBUM, true,
                                   builder.path("user/" + userId + "/albums").build()); // URI path
            
            testProducesCorrectUrl(userId + "/channels", // Vimeo API URL segment
                                   ContentType.CHANNEL, true,
                                   builder.path("user/" + userId + "/channels").build()); // URI path
            
            testProducesCorrectUrl(userId + "/groups", // Vimeo API URL segment
                                   ContentType.GROUP, true,
                                   builder.path("user/" + userId + "/groups").build()); // URI path
            
            testProducesCorrectUrl(userId + "/contacts_videos", // Vimeo API URL segment
                                   ContentType.VIDEO, true,
                                   builder.path("user/" + userId + "/ccreated").build()); // URI path
            
            testProducesCorrectUrl(userId + "/contacts_like", // Vimeo API URL segment
                                   ContentType.VIDEO, true,
                                   builder.path("user/" + userId + "/clikes").build()); // URI path            
        }
        
        testFailsToParse(builder.path("user/" + "with.dots.id" + "/groups").build());
        testFailsToParse(builder.path("user/" + "187*727" + "/albums").build());
        testFailsToParse(builder.path("user/" + "&^%" + "/clikes").build());
        testFailsToParse(builder.path("user/" + "155" + "/action.with.dot").build());
        testFailsToParse(builder.path("user/" + "155" + "/&#*#").build());
        
    }
    
    public void testSimpleApiVideosMethods() throws Throwable {
        
        for (String videoId: testVideos) {
            
            testProducesCorrectUrl("video/" + videoId, // Vimeo API URL segment
                                   ContentType.VIDEO, false,
                                   builder.path("video/" + videoId).build()); // URI path
        
        }
            
        testFailsToParse(builder.path("video/" + "2332withlttrs").build());
        testFailsToParse(builder.path("video/" + "373*&3737").build());
        testFailsToParse(builder.path("video/" + "___aa").build());
        
    }
    
    public void testSimpleApiActivitiesMethods() throws Throwable {
        
        for (String userId: testUsers) {
            
            testProducesCorrectUrl("activity/" + userId + "/user_did", // Vimeo API URL segment
                                   ContentType.ACTIVITY, true,
                                   builder.path("activity/" + userId + "/did").build()); // URI path
            
            testProducesCorrectUrl("activity/" + userId + "/happened_to_user", // Vimeo API URL segment
                                   ContentType.ACTIVITY, true,
                                   builder.path("activity/" + userId + "/happened").build()); // URI path
            
            testProducesCorrectUrl("activity/" + userId + "/contacts_did", // Vimeo API URL segment
                                   ContentType.ACTIVITY, true,
                                   builder.path("activity/" + userId + "/cdid").build()); // URI path
            
            testProducesCorrectUrl("activity/" + userId + "/happened_to_contacts", // Vimeo API URL segment
                                   ContentType.ACTIVITY, true,
                                   builder.path("activity/" + userId + "/chappened").build()); // URI path            

            testProducesCorrectUrl("activity/" + userId + "/everyone_did", // Vimeo API URL segment
                                   ContentType.ACTIVITY, true,
                                   builder.path("activity/" + userId + "/edid").build()); // URI path
            
        }
        
        testFailsToParse(builder.path("activity/" + "with.dots.id" + "/cdid").build());
        testFailsToParse(builder.path("activity/" + "187*727" + "/did").build());
        testFailsToParse(builder.path("activity/" + "&^%" + "/happened").build());
        testFailsToParse(builder.path("activity/" + "155" + "/action.with.dot").build());
        testFailsToParse(builder.path("activity/" + "155" + "/&#*#").build());
        
    }
    
   public void testSimpleApiGroupsMethods() throws Throwable {
        
        for (String groupId: testGroups) {
            
            testProducesCorrectUrl("group/" + groupId + "/videos", // Vimeo API URL segment
                                   ContentType.VIDEO, true,
                                   builder.path("group/" + groupId + "/videos").build()); // URI path
            
            testProducesCorrectUrl("group/" + groupId + "/users", // Vimeo API URL segment
                                   ContentType.USER, true,
                                   builder.path("group/" + groupId + "/users").build()); // URI path
            
            testProducesCorrectUrl("group/" + groupId + "/info", // Vimeo API URL segment
                                   ContentType.GROUP, false,
                                   builder.path("group/" + groupId + "/info").build()); // URI path
            
        }
        
        testFailsToParse(builder.path("group/" + "with.dots.id" + "/users").build());
        testFailsToParse(builder.path("group/" + "187*727" + "/videos").build());
        testFailsToParse(builder.path("group/" + "&^%" + "/info").build());
        testFailsToParse(builder.path("group/" + "155" + "/action.with.dot").build());
        testFailsToParse(builder.path("group/" + "155" + "/&#*#").build());
        
   }
   
   public void testSimpleApiChannelsMethods() throws Throwable {
       
       for (String channelId: testChannels) {
           
           testProducesCorrectUrl("channel/" + channelId + "/videos", // Vimeo API URL segment
                                  ContentType.VIDEO, true,
                                  builder.path("channel/" + channelId + "/videos").build()); // URI path
           
           testProducesCorrectUrl("channel/" + channelId + "/info", // Vimeo API URL segment
                                  ContentType.CHANNEL, false,
                                  builder.path("channel/" + channelId + "/info").build()); // URI path
           
       }
       
       testFailsToParse(builder.path("channel/" + "with.dots.id" + "/videos").build());
       testFailsToParse(builder.path("channel/" + "187*727" + "/info").build());
       testFailsToParse(builder.path("channel/" + "&^%" + "/videos").build());
       testFailsToParse(builder.path("channel/" + "155" + "/action.with.dot").build());
       testFailsToParse(builder.path("channel/" + "155" + "/&#*#").build());
       
   }
   
   public void testSimpleApiAlbumsMethods() throws Throwable {
       
       for (String albumId: testAlbums) {
           
           testProducesCorrectUrl("album/" + albumId + "/videos", // Vimeo API URL segment
                                  ContentType.VIDEO, true,
                                  builder.path("album/" + albumId + "/videos").build()); // URI path
           
           testProducesCorrectUrl("album/" + albumId + "/info", // Vimeo API URL segment
                                  ContentType.ALBUM, false,
                                  builder.path("album/" + albumId + "/info").build()); // URI path
           
       }
       
       testFailsToParse(builder.path("album/" + "with.dots.id" + "/videos").build());
       testFailsToParse(builder.path("album/" + "187*727" + "/info").build());
       testFailsToParse(builder.path("album/" + "&^%" + "/videos").build());
       testFailsToParse(builder.path("album/" + "155" + "/action.with.dot").build());
       testFailsToParse(builder.path("album/" + "155" + "/&#*#").build());
       
   }    
   
    
    /**
     * Tests if <code>VimeoApi</code> produces the correct Vimeo API Call URL 
     * for passed <code>actualUri</code>. Also tests <code>VimeoSimpleApiProvider</code> for 
     * correctness on returned content type (<code>expectedResultType</code>) 
     * to conform with API Call result and tests if <code>VimeoSimpleApiProvider</code> says
     * true about how much elements returns the call
     * 
     * @param expectedVimeoApiUrl the expected dynamic segment of resulting Vimeo API Call URL that located <code>http://vimeo.com/api/v2/[::here::].json</code> 
     * @param expectedResultType the expected result content type
     * @param multipleResultExpected is multiple results of this content type are expected
     * @param actualUri <code>Uri</code> object to pass the test
     */
    protected static void testProducesCorrectUrl(final String expectedVimeoApiUrl,
                                                 final ContentType expectedResultType, 
                                                 final boolean multipleResultExpected,
                                                 final Uri actualUri) {
        final String expectedUrl = VimeoConfig.VIMEO_SIMPLE_API_CALL_PREFIX + '/' +
                                   expectedVimeoApiUrl + '.' + 
                                   VimeoApi.RESPONSE_FORMAT;
        
        Assert.assertEquals(expectedUrl, VimeoApi.resolveUriForSimpleApi(actualUri).apiFullUrl.toString());
        Assert.assertEquals(expectedResultType, VimeoApi.getReturnedContentType(actualUri));
        Assert.assertEquals(multipleResultExpected, VimeoApi.getReturnsMultipleResults(actualUri));
    }
    
    protected static void testFailsToParse(final Uri uri) {
        try {
            VimeoApi.resolveUriForSimpleApi(uri);
            Assert.fail("must throw exception for URI " + uri);
        } catch (IllegalArgumentException ieae) { /* pass */ }
    }
    
}
