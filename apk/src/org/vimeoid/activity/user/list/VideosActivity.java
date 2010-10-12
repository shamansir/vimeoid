package org.vimeoid.activity.user.list;

import java.util.HashSet;
import java.util.Set;

import net.londatiga.android.QActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QActionItem.QActionClickListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.base.ApiPagesReceiver;
import org.vimeoid.activity.user.QuickApiTask;
import org.vimeoid.activity.user.ItemsListActivity;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.adapter.user.VideosListAdapter;
import org.vimeoid.adapter.user.VideosListAdapter.ThumbClickListener;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.dto.advanced.PagingData;
import org.vimeoid.dto.advanced.Video;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.PagingData_;

import android.content.res.Resources;
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
    
    private long currentUserId;
    
    public VideosActivity() {
        super();
        
        likesReceiver = new VideosIdsReceiver() {
            @Override public void onComplete() {
                ((VideosListAdapter)getAdapter()).updateLikes(getListView(), videosIds);
                //onContentChanged();
            }            
        };
        
        watchLatersReceiver = new VideosIdsReceiver() {
            @Override public void onComplete() {
                ((VideosListAdapter)getAdapter()).updateWatchLaters(getListView(), videosIds);
                //onContentChanged();
            }            
        };
        
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        currentUserId = VimeoApi.getUserLoginData(this).id;
        
        final Video.SortType currentSortType = (Video.SortType) getIntent().getExtras().get(Invoke.Extras.API_SORT_TYPE);
        
        // check if fits period
        secondaryTasks.addListTask(GET_LIKES_TASK, Methods.videos.getLikes, new ApiParams().add("user_id", String.valueOf(currentUserId))
        		                                                                           .add("sort", currentSortType.toString()), 
        		                                                            likesReceiver, 3, 30);
        secondaryTasks.addListTask(GET_WATCHSLATER_TASK, Methods.albums.getWatchLater, new ApiParams().add("user_id", String.valueOf(currentUserId))
                                                                                                      .add("sort", currentSortType.toString()), 
                                                                            watchLatersReceiver, 3, 30);
        
        super.onCreate(savedInstanceState);
    }

    @Override
    protected JsonObjectsAdapter<Video> createAdapter() {
        final VideosListAdapter adapter = new VideosListAdapter(this, getLayoutInflater());
        getListView().setOnItemSelectedListener(adapter);
        adapter.setThumbClickListener(new ThumbClickListener() {
            @Override public void thumbClicked(Video video) {
                Invoke.User_.playVideo(VideosActivity.this, video);
            }
        });
        return adapter;
    }
    
    @Override
    protected void onItemSelected(Video video) { 
        Invoke.User_.selectVideo(this, video);
    }
    
    @Override
    protected QuickAction createQuickActions(final int position, final Video video, View view) {
        // TODO: add ability to watch albums / channels where video located
        final Resources resources = getResources();
        final VideosListAdapter adapter = ((VideosListAdapter)getAdapter());
        
        QuickAction qa = new QuickAction(view);
        if (video.uploaderId != currentUserId) {
            qa.addActionItem(getString(R.string.qa_later), resources.getDrawable(video.isWatchLater 
                                                                                 ? R.drawable.watchlater_white 
                                                                                 : R.drawable.watchlater_white_not), 
                new QActionClickListener() {            
                    @Override public void onClick(View v, final QActionItem item) {
                        new QuickApiTask(VideosActivity.this, video.isWatchLater 
                                                              ? Methods.albums.removeFromWatchLater 
                                                              : Methods.albums.addToWatchLater) {
                            
                            @Override
                            public void onOk() {
                                video.isWatchLater = !video.isWatchLater;
                                final Video changedVideo = adapter.switchWatchLater(getListView(), position);
                                Dialogs.makeToast(VideosActivity.this, getString(changedVideo.isWatchLater 
                                                                                 ? R.string.added_to_watchlater
                                                                                 : R.string.removed_from_watchlater));
                                item.setIcon(resources.getDrawable(changedVideo.isWatchLater 
                                                                   ? R.drawable.watchlater_white 
                                                                   : R.drawable.watchlater_white_not));
                                item.invalidate();
                            }
                            
                            @Override
                            protected int onError() {
                            	return R.string.failed_to_add_watch_later;
                            }
                            
                        }.execute(new ApiParams().add("video_id", String.valueOf(video.id)));
                    }
                });
        }
        qa.addActionItem(getString(R.string.qa_info), getResources().getDrawable(R.drawable.info), 
                new QActionClickListener() {            
                    @Override public void onClick(View v, QActionItem item) {
                        Invoke.User_.selectVideo(VideosActivity.this, video);
                    }
                });
        qa.addActionItem(getString(R.string.qa_author), getResources().getDrawable(R.drawable.contact), 
                new QActionClickListener() {            
                    @Override public void onClick(View v, QActionItem item) {
                        Invoke.User_.selectUploader(VideosActivity.this, video);
                    }
                });
        qa.addActionItem(getString(R.string.qa_comments), getResources().getDrawable(R.drawable.comment), 
                new QActionClickListener() {            
                    @Override public void onClick(View v, QActionItem item) {
                        Invoke.User_.selectCommentsOf(VideosActivity.this, video);
                    }
                });
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