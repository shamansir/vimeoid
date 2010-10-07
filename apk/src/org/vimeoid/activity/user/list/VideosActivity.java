package org.vimeoid.activity.user.list;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.base.ApiPagesReceiver;
import org.vimeoid.activity.user.ItemsListActivity;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.adapter.user.VideosListAdapter;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.dto.advanced.PagingData;
import org.vimeoid.dto.advanced.Video;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.PagingData_;

import android.os.Bundle;
import android.util.Log;

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
    
    //private String[] watchLaters;
    //private String[] likes;
    
    public VideosActivity() {
        super(R.menu.video_context_user_menu);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        final String currentUserId = String.valueOf(VimeoApi.getUserLoginData(this).id);
        
        // check if fits period
        secondaryTasks.addListTask(GET_LIKES_TASK, Methods.videos.getLikes, new ApiParams().add("user_id", currentUserId)
        		                                                                           .add("sort", Video.SortType.NEWEST.toString()), 
        		                                                            new VideosIdsReceiver(), 3, 25);
        secondaryTasks.addListTask(GET_WATCHSLATER_TASK, Methods.albums.getWatchLater, new ApiParams().add("user_id", currentUserId), new VideosIdsReceiver(), 3, 25);
        
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
    
    protected static final class VideosIdsReceiver implements ApiPagesReceiver<JSONObject> {
        
        final Set<String> videosIds = new HashSet<String>();

        @Override
        public void addSource(JSONObject page) throws Exception {
            final String[] videosIdsArr = Video.extractIdsList(page); 
            for (String videoId: videosIdsArr) {
                //Log.d("ADDING", videoId + " as w/l or like");
                videosIds.add(videoId);
            }
        }

        @Override
        public int getCount() {
            return videosIds.size();
        }

        @Override
        public PagingData_ getCurrentPagingData(JSONObject lastPage) throws JSONException {
            return PagingData.collectFromJson(lastPage, Video.FieldsKeys.MULTIPLE_KEY);
        }

        @Override
        public void onComplete() {
            Log.d("ADDING", "complete");
        }
        
    };
    

}