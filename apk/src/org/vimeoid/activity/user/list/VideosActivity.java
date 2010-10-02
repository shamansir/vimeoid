package org.vimeoid.activity.user.list;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.user.ItemsListActivity;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.adapter.user.VideosListAdapter;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.dto.advanced.SortType;
import org.vimeoid.dto.advanced.Video;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Invoke;

import android.os.Bundle;

/**
 * 
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>Videos</code>
 *
 * <p>Activity that shows a Vimeo Items list (Video, User, Channel, Album ...) to a user
 * that has logged in</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 3, 2010 11:55:11 PM 
 *
 */

// TODO: for sync use SyncAdapter on API Level 7
public class VideosActivity extends ItemsListActivity<Video> {
    
    private static final int GET_LIKES_TASK = 1;
    private static final int GET_WATCHSLATER_TASK = 2;
    
    private static final int MAX_OF_LAST_VIDEOS_DATA = 50;
    
    //private String[] watchLaters;
    //private String[] likes;
    
    public VideosActivity() {
        super(R.menu.video_context_user_menu);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        final String currentUserId = String.valueOf(VimeoApi.getUserLoginData(this).id);
        
        // check if fits period
        secondaryTasks.add(GET_LIKES_TASK, Methods.videos.getLikes, new ApiParams().add("user_id", currentUserId)
                                                                                   .add("per_page", String.valueOf(MAX_OF_LAST_VIDEOS_DATA))
                                                                                   .add("sort", SortType.NEWEST.toString()));
        secondaryTasks.add(GET_WATCHSLATER_TASK, Methods.albums.getWatchLater, new ApiParams().add("user_id", currentUserId)
                                                                                              .add("per_page", String.valueOf(MAX_OF_LAST_VIDEOS_DATA))
                                                                                              .add("sort", SortType.NEWEST.toString()));
        
        // TODO: run this tasks manually, ask more if required
        
        super.onCreate(savedInstanceState);
    }

    @Override
    protected JsonObjectsAdapter<Video> createAdapter() {
        return new VideosListAdapter(this, getLayoutInflater());
    }
    
    @Override
    public void onSecondaryTaskPerfomed(int id, JSONObject result) throws JSONException {
        // TODO: pass watchLaters and Likes to adapter
    }
    
    @Override
    protected void onItemSelected(Video video) { 
        Invoke.User_.selectVideo(this, video);
    }    

}