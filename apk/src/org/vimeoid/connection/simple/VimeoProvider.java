package org.vimeoid.connection.simple;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.vimeoid.VimeoConfig;
import org.vimeoid.connection.ApiCallInfo;
import org.vimeoid.connection.ContentType;
import org.vimeoid.connection.JsonObjectsCursor;
import org.vimeoid.connection.JsonOverHttp;
import org.vimeoid.dto.simple.Operation;
import org.vimeoid.dto.simple.Album;
import org.vimeoid.dto.simple.Channel;
import org.vimeoid.dto.simple.Group;
import org.vimeoid.dto.simple.User;
import org.vimeoid.dto.simple.Video;
import org.vimeoid.util.Utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.providers</dd>
 * </dl>
 *
 * <code>VimeoProvider</code>
 *
 * <p>This content provider can provide a cursors for another applications
 * and supports only {@link VimeoProvider#query(Uri, String[], String, String[], String)}
 * from {@link ContentProvider} abstract methods. It is not connected to the database, 
 * it makes requests over <code>HTTP</code> to Vimeo and just simulates the cursor. 
 * The <code>Uri</code> schemes are almost similar</p> to Vimeo Simple API calls,
 * but differ in some cases, the actual list will be provided in future documentation.  
 * 
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 20, 2010 5:29:21 PM 
 *
 */

public class VimeoProvider extends ContentProvider {
    
    public static final String TAG = "VimeoProvider";

    public static final String AUTHORITY = "org.vimeoid.simple.provider";
    public static final String RESPONSE_FORMAT = "json";
    
    public static final Uri BASE_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).build();
    

    /**
     * This method is not supported in <code>VimeoProvider</code>
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Deletion of something in not supported in VimeoProvider");
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case OPER8NS_LIST_URI_TYPE:   return Operation.CONTENT_TYPE;
            case OPER8N_SINGLE_URI_TYPE:  return Operation.CONTENT_ITEM_TYPE;
            case ALBUMS_LIST_URI_TYPE:    return Album.CONTENT_TYPE;
            case ALBUM_SINGLE_URI_TYPE:   return Album.CONTENT_ITEM_TYPE;
            case CHANNELS_LIST_URI_TYPE:  return Channel.CONTENT_TYPE;
            case CHANNEL_SINGLE_URI_TYPE: return Channel.CONTENT_ITEM_TYPE;
            case GROUPS_LIST_URI_TYPE:    return Group.CONTENT_TYPE;
            case GROUP_SINGLE_URI_TYPE:   return Group.CONTENT_ITEM_TYPE;
            case USERS_LIST_URI_TYPE:     return User.CONTENT_TYPE;
            case USER_SINGLE_URI_TYPE:    return User.CONTENT_ITEM_TYPE;
            case VIDEOS_LIST_URI_TYPE:    return Video.CONTENT_TYPE;
            case VIDEO_SINGLE_URI_TYPE:   return Video.CONTENT_ITEM_TYPE;
        default: throw new IllegalArgumentException("Unknown URI type: " + uri);
    }

    }

    
    /**
     * This method is not supported in <code>VimeoProvider</code>
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Insertion of something in not supported in VimeoProvider");
    }

    @Override
    public boolean onCreate() {
        return true;
    }
    
    @Override
    public Cursor query(Uri contentUri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if ((selection != null) || (selectionArgs != null))
            throw new UnsupportedOperationException("SQL Where-selections are not supported in VimeoProvider, please use URI to filter the selection query");
        if (sortOrder != null) throw new UnsupportedOperationException("SQL-styled sorting is not supported in VimeoProvider, please use URI parameters to specify sorting order (if supported by the method)");
        if (projection == null) throw new IllegalArgumentException("Please specify projection, at least empty one"); 
        final ApiCallInfo apiCallInfo = collectCallInfo(contentUri); 
        final URI fullCallUrl = getFullApiUrl(apiCallInfo.apiUrlPart);
        try {
            if (getReturnsJsonArray(contentUri)) {
                final JSONArray jsonArr = JsonOverHttp.use().askForArray(fullCallUrl);
                //Log.d(TAG, "JSON Array received: " + jsonArr.toString());
                return new JsonObjectsCursor(jsonArr, projection, apiCallInfo);
            } else {
                final JSONObject jsonObj = JsonOverHttp.use().askForObject(fullCallUrl);
                //Log.d(TAG, "JSON Object received: " + jsonObj.toString());                
                return new JsonObjectsCursor(jsonObj, projection, apiCallInfo);
            }          
        } catch (ClientProtocolException cpe) {
            Log.e(TAG, "Client protocol exception" + cpe.getLocalizedMessage());
            cpe.printStackTrace();
        } catch (JSONException jsone) {
            Log.e(TAG, "JSON parsing exception " + jsone.getLocalizedMessage());
            jsone.printStackTrace();
        } catch (IOException ioe) {
            Log.e(TAG, "Connection/IO exception " + ioe.getLocalizedMessage());
            // TDODO: catch UnknownHostException and fire it as VimeoUnreachable
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * This method is not supported in <code>VimeoProvider</code>
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        throw new UnsupportedOperationException("Updation of something in not supported in VimeoProvider");
    }
    
    public static URI getFullApiUrl(StringBuffer uriPart) { // TODO: make protected
        try {
            return new URI(VimeoConfig.VIMEO_SIMPLE_API_CALL_PREFIX + '/' + uriPart.toString());
        } catch (URISyntaxException use) {
            Log.e(TAG, "URI settings exception when getting URI for " + uriPart);
            use.printStackTrace();
            return null;
        }
    }

    /* ====================== Simple API: UriMatcher ======================== */    
    
    private static final UriMatcher uriMatcher;
    
    private static final int OPER8NS_LIST_URI_TYPE   = 1;
    private static final int OPER8N_SINGLE_URI_TYPE  = 2;
    private static final int ALBUMS_LIST_URI_TYPE    = 3;
    private static final int ALBUM_SINGLE_URI_TYPE   = 4;
    private static final int CHANNELS_LIST_URI_TYPE  = 5;
    private static final int CHANNEL_SINGLE_URI_TYPE = 6;
    private static final int GROUPS_LIST_URI_TYPE    = 7;
    private static final int GROUP_SINGLE_URI_TYPE   = 8;
    private static final int USERS_LIST_URI_TYPE     = 9;
    private static final int USER_SINGLE_URI_TYPE    = 10;
    private static final int VIDEOS_LIST_URI_TYPE    = 11;
    private static final int VIDEO_SINGLE_URI_TYPE   = 12;    
    
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        
        uriMatcher.addURI(AUTHORITY, "user/*/info",     USER_SINGLE_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "user/*/videos",   VIDEOS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "user/*/likes",    VIDEOS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "user/*/appears",  VIDEOS_LIST_URI_TYPE); // videos where user appears
        uriMatcher.addURI(AUTHORITY, "user/*/all",      VIDEOS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "user/*/subscr",   VIDEOS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "user/*/albums",   ALBUMS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "user/*/channels", CHANNELS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "user/*/groups",   GROUPS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "user/*/ccreated", VIDEOS_LIST_URI_TYPE); // videos that user's contacts created
        uriMatcher.addURI(AUTHORITY, "user/*/clikes",   VIDEOS_LIST_URI_TYPE); // videos that user's contacts liked
        
        uriMatcher.addURI(AUTHORITY, "video/#", VIDEO_SINGLE_URI_TYPE);
        
        uriMatcher.addURI(AUTHORITY, "group/*/info",   GROUP_SINGLE_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "group/*/videos", VIDEOS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "group/*/users",  USERS_LIST_URI_TYPE);
        
        uriMatcher.addURI(AUTHORITY, "channel/*/info",   CHANNEL_SINGLE_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "channel/*/videos", VIDEOS_LIST_URI_TYPE);
        
        uriMatcher.addURI(AUTHORITY, "album/#/info",   ALBUM_SINGLE_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "album/#/videos", VIDEOS_LIST_URI_TYPE);
        
        uriMatcher.addURI(AUTHORITY, "activity/*/did",       OPER8NS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "activity/*/happened",  OPER8NS_LIST_URI_TYPE);
        uriMatcher.addURI(AUTHORITY, "activity/*/cdid",      OPER8NS_LIST_URI_TYPE); // contacts did
        uriMatcher.addURI(AUTHORITY, "activity/*/chappened", OPER8NS_LIST_URI_TYPE); // happened to contacts
        uriMatcher.addURI(AUTHORITY, "activity/*/edid",      OPER8NS_LIST_URI_TYPE); // everyone did
    }
    
    public static ContentType getReturnedContentType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case OPER8NS_LIST_URI_TYPE:
            case OPER8N_SINGLE_URI_TYPE:  return ContentType.ACTIVITY;
            case ALBUMS_LIST_URI_TYPE:
            case ALBUM_SINGLE_URI_TYPE:   return ContentType.ALBUM;
            case CHANNELS_LIST_URI_TYPE:
            case CHANNEL_SINGLE_URI_TYPE: return ContentType.CHANNEL;
            case GROUPS_LIST_URI_TYPE:
            case GROUP_SINGLE_URI_TYPE:   return ContentType.GROUP;
            case USERS_LIST_URI_TYPE:
            case USER_SINGLE_URI_TYPE:    return ContentType.USER;
            case VIDEOS_LIST_URI_TYPE:
            case VIDEO_SINGLE_URI_TYPE:   return ContentType.VIDEO;
            default: throw new IllegalArgumentException("Unknown URI type: " + uri);
        }        
    }
    
    public static boolean getReturnsMultipleResults(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case OPER8NS_LIST_URI_TYPE:
            case ALBUMS_LIST_URI_TYPE:
            case CHANNELS_LIST_URI_TYPE:
            case GROUPS_LIST_URI_TYPE:
            case USERS_LIST_URI_TYPE:
            case VIDEOS_LIST_URI_TYPE:    return true;
            case OPER8N_SINGLE_URI_TYPE:  
            case ALBUM_SINGLE_URI_TYPE:
            case CHANNEL_SINGLE_URI_TYPE:
            case GROUP_SINGLE_URI_TYPE:
            case USER_SINGLE_URI_TYPE:
            case VIDEO_SINGLE_URI_TYPE:   return false;
            default: throw new IllegalArgumentException("Unknown URI type: " + uri);
        }        
    }
    
    protected static boolean getReturnsJsonArray(Uri uri) {
        if ("video".equals(uri.getPathSegments().get(0))) return true; // videos are always returned as array
        return getReturnsMultipleResults(uri);
    }
    
    /* ====================== Simple API: Parsing Uri ======================= */    
        
    public static ApiCallInfo collectCallInfo(Uri contentUri) {
        final List<String> segments = contentUri.getPathSegments();
        final StringBuffer urlBuffer = new StringBuffer();
        Log.d(TAG, "generating API Call URL for URI " + contentUri.toString());
        
        final ApiCallInfo urlInfo = new ApiCallInfo();
        urlInfo.subjectType = ContentType.fromAlias(segments.get(0));
        switch (urlInfo.subjectType) {
            case USER: {
                    urlInfo.subject = Utils.validateShortcutOrId(segments.get(1));
                    urlInfo.action = resolveUserAction(segments.get(2));
                    urlBuffer.append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;
            case VIDEO: {
                    urlInfo.subject = Utils.validateId(segments.get(1));
                    urlInfo.action = null;
                    urlBuffer.append("video/").append(urlInfo.subject);
                } break;
            case GROUP: {
                    urlInfo.subject = Utils.validateShortcutOrId(segments.get(1));
                    urlInfo.action = resolveGroupAction(segments.get(2));
                    urlBuffer.append("group/").append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;
            case CHANNEL: {
                    urlInfo.subject = Utils.validateShortcutOrId(segments.get(1));
                    urlInfo.action = resolveChannelAction(segments.get(2));
                    urlBuffer.append("channel/").append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;
            case ALBUM: {
                    urlInfo.subject = Utils.validateId(segments.get(1));
                    urlInfo.action = resolveAlbumAction(segments.get(2));
                    urlBuffer.append("album/").append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;          
            case ACTIVITY: {
                    urlInfo.subject = Utils.validateShortcutOrId(segments.get(1));
                    urlInfo.action = resolveActivityAction(segments.get(2));
                    urlBuffer.append("activity/").append(urlInfo.subject).append('/').append(urlInfo.action);
                } break;
        }
        
        urlBuffer.append('.').append(RESPONSE_FORMAT);
        if (contentUri.getQueryParameter("page") != null) {
            urlBuffer.append("?page=").append(contentUri.getQueryParameter("page"));
        }
        
        Log.d(TAG, "generated result: " + urlBuffer.toString());
        urlInfo.apiUrlPart = urlBuffer;
        urlInfo.multipleResult = getReturnsMultipleResults(contentUri);
        urlInfo.resultType = getReturnedContentType(contentUri);
        return urlInfo;        
    }
    
    public static String getCallDescription(ApiCallInfo callInfo) {
        return callInfo.resultType.getAlias() + ": " + callInfo.subject + ' ' + callInfo.subjectType.getAlias();
    }
    
    protected static String resolveUserAction(String action) {
        if ("all".equals(action)) return "all_videos";        
        if ("appears".equals(action)) return "appears_in";
        if ("subscr".equals(action)) return "subscriptions";
        if ("ccreated".equals(action)) return "contacts_videos";
        if ("clikes".equals(action)) return "contacts_like";
        return Utils.validateActionName(action);
    }
    
    protected static String resolveActivityAction(String action) {
        if ("did".equals(action)) return "user_did";        
        if ("happened".equals(action)) return "happened_to_user";
        if ("cdid".equals(action)) return "contacts_did";
        if ("chappened".equals(action)) return "happened_to_contacts";
        if ("edid".equals(action)) return "everyone_did";
        return Utils.validateActionName(action);
    }
    
    protected static String resolveGroupAction(String action) {
        return Utils.validateActionName(action);
    }
    
    protected static String resolveAlbumAction(String action) {
        return Utils.validateActionName(action);
    }    
    
    protected static String resolveChannelAction(String action) {
        return Utils.validateActionName(action);
    }    
    
}
