/**
 * 
 */
package org.vimeoid.test;

import junit.framework.Assert;

import org.vimeoid.VimeoProvider;
import org.vimeoid.VimeoUriParser;
import org.vimeoid.VimeoProvider.ContentType;

import android.net.Uri;
import android.net.Uri.Builder;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid-test</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.test</dd>
 * </dl>
 *
 * <code>VimeoUriParserTest</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 25, 2010 11:20:52 PM 
 *
 */
public class VimeoUriParserTest extends AndroidTestCase {
    
    private static final String[] testUsers = {"aaa", "user1616", "17373"};
    
    public void testUriParsingAndResults() throws Throwable {
        Builder builder = new Uri.Builder().scheme("content").authority(VimeoProvider.AUTHORITY);
        
        for (String userId: testUsers) {
            final Uri infoUri = builder.path("user/" + userId + "/info").build();
            Assert.assertEquals(makeApiCallUrl(null, userId, "info"), 
                                VimeoUriParser.getApiCallUrlForUri(infoUri));
            Assert.assertEquals(ContentType.USER, VimeoProvider.getReturnedContentType(infoUri));
            Assert.assertEquals(false, VimeoProvider.getReturnsMultipleResults(infoUri));
            
            final Uri videosUri = builder.path("user/" + userId + "/videos").build();
            Assert.assertEquals(makeApiCallUrl(null, userId, "videos"), 
                                VimeoUriParser.getApiCallUrlForUri(videosUri));
            Assert.assertEquals(ContentType.VIDEO, VimeoProvider.getReturnedContentType(videosUri));
            Assert.assertEquals(true, VimeoProvider.getReturnsMultipleResults(videosUri));
            
            // TODO: finish tests
        }
    }
    
    
    private static String makeApiCallUrl(String type, String subject, String method) {
        return VimeoUriParser.VIMEO_API_CALL_PREFIX + '/' +
                ((type != null) ? type + '/' : "") +
                ((subject != null) ? subject + '/' : "") +
                method + '.' + VimeoUriParser.DEFAULT_RESPONSE_FORMAT;
    }
}
