package org.vimeoid.activity.user.list;

import java.util.HashSet;
import java.util.Set;

import net.londatiga.android.QuickAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.base.ApiPagesReceiver;
import org.vimeoid.activity.user.ItemsListActivity;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.adapter.user.VideosListAdapter;
import org.vimeoid.adapter.user.VideosListAdapter.ThumbClickListener;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.dto.advanced.PagingData;
import org.vimeoid.dto.advanced.Video;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.PagingData_;

import android.os.Bundle;
import android.view.View;

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
    
    private final VideosIdsReceiver likesReceiver;
    private final VideosIdsReceiver watchLatersReceiver;
    
    //private String[] watchLaters;
    //private String[] likes;
    
    public VideosActivity() {
        super();
        
        likesReceiver = new VideosIdsReceiver() {
            @Override public void onComplete() {
                ((VideosListAdapter)getAdapter()).updateLikes(videosIds);
                onContentChanged();
            }            
        };
        
        watchLatersReceiver = new VideosIdsReceiver() {
            @Override public void onComplete() {
                ((VideosListAdapter)getAdapter()).updateWatchLaters(videosIds);
                onContentChanged();
            }            
        };
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        final String currentUserId = String.valueOf(VimeoApi.getUserLoginData(this).id);
        final Video.SortType currentSortType = (Video.SortType) getIntent().getExtras().get(Invoke.Extras.API_SORT_TYPE);
        
        // check if fits period
        secondaryTasks.addListTask(GET_LIKES_TASK, Methods.videos.getLikes, new ApiParams().add("user_id", currentUserId)
        		                                                                           .add("sort", currentSortType.toString()), 
        		                                                            likesReceiver, 3, 25);
        secondaryTasks.addListTask(GET_WATCHSLATER_TASK, Methods.albums.getWatchLater, new ApiParams().add("user_id", currentUserId)
                                                                                                      .add("sort", currentSortType.toString()), 
                                                                            watchLatersReceiver, 3, 25);
        
        // TODO: run this tasks manually, ask more if required
        
        super.onCreate(savedInstanceState);
    }

    @Override
    protected JsonObjectsAdapter<Video> createAdapter() {
        final VideosListAdapter adapter = new VideosListAdapter(this, getLayoutInflater(), getListView());
        adapter.setThumbClickListener(new ThumbClickListener() {
            @Override public void thumbClicked(Video video) {
                Invoke.User_.playVideo(VideosActivity.this, video);
            }
        });
        return adapter;
    }
    
    @Override
    public void onSecondaryTaskPerfomed(int id, JSONObject result) throws JSONException {
        // TODO: pass watchLaters and Likes to adapter
    }
    
    @Override
    protected void onItemSelected(Video video) { 
        Invoke.User_.selectVideo(this, video);
    }
    
    @Override
    protected QuickAction createQuickActions(Video item, View v) {
        QuickAction qa = new QuickAction(v);
        qa.addActionItem("Play", getResources().getDrawable(R.drawable.play));
        qa.addActionItem("Face", getResources().getDrawable(R.drawable.contact));
        qa.addActionItem("Jagagaga", getResources().getDrawable(R.drawable.video));
        qa.addActionItem("Trace", getResources().getDrawable(R.drawable.album));
        qa.addActionItem("Author", getResources().getDrawable(R.drawable.channel));
        qa.addActionItem("Later", getResources().getDrawable(R.drawable.watchlater));
        /*qa.addActionItem("Video", getResources().getDrawable(R.drawable.video));
        qa.addActionItem("Info", getResources().getDrawable(R.drawable.info));
        qa.addActionItem("Later", getResources().getDrawable(R.drawable.watchlater));
        qa.addActionItem("Gegegegegege", getResources().getDrawable(R.drawable.channel));
        qa.addActionItem("Two words", getResources().getDrawable(R.drawable.channel)); */
        /*qa.show();*/
        return qa;
    };
    
    protected abstract static class VideosIdsReceiver implements ApiPagesReceiver<JSONObject> {
        
        protected final Set<Long> videosIds = new HashSet<Long>();

        @Override
        public void addSource(JSONObject page) throws Exception {
            final String[] videosIdsArr = Video.extractIdsList(page); 
            for (String videoId: videosIdsArr) {
                //Log.d("ADDING", videoId + " as w/l or like");
                videosIds.add(Long.valueOf(videoId));
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
        
    }

    
}