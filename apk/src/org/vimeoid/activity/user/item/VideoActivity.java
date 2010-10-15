/**
 * 
 */
package org.vimeoid.activity.user.item;

import org.json.JSONException;
import org.json.JSONObject;

import org.vimeoid.R;
import org.vimeoid.activity.user.QuickApiTask;
import org.vimeoid.activity.user.SingleItemActivity;
import org.vimeoid.adapter.LActionItem;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.dto.advanced.Video;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.PlayerWebView;
import org.vimeoid.util.Utils;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user.item</dd>
 * </dl>
 *
 * <code>UserActivity</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 24, 2010 11:05:50 PM 
 *
 */
public class VideoActivity extends SingleItemActivity<Video> {
    
    public static final String TAG = "VideoActivity";
    
    private final OnClickListener clickDisabler;
    
    private Boolean isLike;
    private Boolean isWatchLater;
    
    private long currentUserId;
    private long subjectVideoId;
    private long ownerId;
    
    public VideoActivity() {
        super(R.layout.view_single_video);
        setLoadManually(true);
        
        this.clickDisabler = new OnClickListener() {
            @Override public void onClick(View v) { Log.d(TAG, "clicking is disabled"); }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);        
        
        subjectVideoId = Long.valueOf(getIntent().getExtras().getLong(Invoke.Extras.VIDEO_ID));
        
        final WebView playerView = 
            PlayerWebView.projectPlayer(subjectVideoId, this);
        playerView.setOnClickListener(clickDisabler);
         
        findViewById(R.id.playOverlay).setOnClickListener(clickDisabler);
        
        queryItem();
    }

    @Override
    protected void prepare(Bundle extras) {
        
        currentUserId = VimeoApi.getUserLoginData(this).id;
        
        ownerId = extras.getLong(Invoke.Extras.USER_ID);
        
        if (currentUserId != ownerId) {
            
            if (extras.containsKey(Invoke.Extras.IS_LIKE) && 
                (extras.get(Invoke.Extras.IS_LIKE) != null)) {
                isLike = (Boolean)extras.get(Invoke.Extras.IS_LIKE);
            } else {
                throw new IllegalStateException("IS_LIKE extra is not found or not set, it is unexpected situation");
            }
            
            if (extras.containsKey(Invoke.Extras.IS_WATCHLATER) && 
                (extras.get(Invoke.Extras.IS_WATCHLATER) != null)) {
                    isWatchLater = (Boolean)extras.get(Invoke.Extras.IS_WATCHLATER);
            } else {
                throw new IllegalStateException("IS_WATCHLATER extra is not found or not set, it is unexpected situation");
            }
            
        }
    }

    @Override
    protected Video extractFromJson(JSONObject jsonObj) throws JSONException {
        return Video.collectFromJson(jsonObj);
    }

    @Override
    protected SectionedActionsAdapter fillWithActions(SectionedActionsAdapter actionsAdapter, final Video video) {
        
        // TODO: video tags as activity
        // TODO: video likers
        // TODO: video comments as activity
        
    	if (currentUserId != ownerId) {
	        // Operations section
	        int operationsSection = actionsAdapter.addSection(getString(R.string.operations));
	        // like
	        initLikeAction(actionsAdapter.addAction(operationsSection, isLike ? R.drawable.like : R.drawable.like_not, R.string.like));
	        // watch later
	        initWatchLaterAction(actionsAdapter.addAction(operationsSection, isWatchLater ? R.drawable.watchlater : R.drawable.watchlater_not, R.string.watch_later));
    	}
        
        // TODO: add "watch later" and "like" if it is not owner video
        // Statistics section
        int statsSection = actionsAdapter.addSection(getString(R.string.statistics));
        // tags
        if (video.tags.length > 0) actionsAdapter.addAction(statsSection, R.drawable.tag, 
                                 Utils.format(getString(R.string.tags_are), "list",
                                             Utils.adaptTags(video.tags, getString(R.string.none_of_tags))));
        // number of plays
        actionsAdapter.addAction(statsSection, R.drawable.play, 
                                 Utils.format(getString(R.string.num_of_plays), "num", String.valueOf(video.playsCount)));
        // number of likes
        actionsAdapter.addAction(statsSection, R.drawable.like, 
                                 Utils.format(getString(R.string.num_of_likes), "num", String.valueOf(video.likesCount)));        
        // number of comments
        actionsAdapter.addAction(statsSection, R.drawable.comment_video, 
                                 Utils.format(getString(R.string.num_of_comments), "num", String.valueOf(video.commentsCount)));
        
        
        // Information section
        int infoSection = actionsAdapter.addSection(getString(R.string.information));
        // duration
        actionsAdapter.addAction(infoSection, R.drawable.duration,
                                 Utils.format(getString(R.string.duration_is), "time", Utils.adaptDuration(video.duration)));       
        // uploader
        final LActionItem userAction = actionsAdapter.addAction(infoSection, R.drawable.contact, 
                                 Utils.format(getString(R.string.uploaded_by), "name", video.uploaderName));
        userAction.onClick =  new OnClickListener() {
            @Override public void onClick(View v) { Invoke.User_.selectUploader(VideoActivity.this, video); };
        };
        // uploaded on
        actionsAdapter.addAction(infoSection, R.drawable.upload,
                                 Utils.format(getString(R.string.uploaded_on), "time", video.uploadedOn));
        // dimensions
        actionsAdapter.addAction(infoSection, R.drawable.dimensions,
                                 Utils.format(getString(R.string.dimensions_are), "width", String.valueOf(video.width),
                                                                                  "height", String.valueOf(video.height)));
        
        return actionsAdapter;
    }
    
    @Override
    protected void onItemReceived(final Video video) {
        
        // enabling overlay
        findViewById(R.id.playOverlay).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                Invoke.User_.playVideo(VideoActivity.this, video);
            }
        });
        
        
        // description
        ((TextView)findViewById(R.id.videoDescription)).setText((video.description.length() > 0) 
                                                                 ? Html.fromHtml(video.description)
                                                                 : getString(R.string.no_description_supplied));        
        
        // uploader portrait
        final ImageView uploaderPortrait = (ImageView)findViewById(R.id.uploaderPortrait);
        uploaderPortrait.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) { Invoke.User_.selectUploader(VideoActivity.this, video); };
        });
        imageLoader.displayImage(video.uploaderPortraits.medium.url, uploaderPortrait);
        
        super.onItemReceived(video);
    }

    private LActionItem initLikeAction(final LActionItem actionItem) {
        
        actionItem.onClick = new OnClickListener() {
            @Override public void onClick(View v) {
                new QuickApiTask(VideoActivity.this, Methods.videos.setLike) {

                    @Override
                    protected void onOk() {
                        isLike = !isLike;
                        
                        // TODO: add icon to toast
                        
                        Dialogs.makeToast(VideoActivity.this, getString(
                                isLike ? R.string.liked
                                       : R.string.disliked));
                        
                        actionItem.icon =  
                                isLike ? R.drawable.like
                                       : R.drawable.like_not;
                        
                    }

                    @Override
                    protected int onError() { return R.string.failed_to_change_like; }                            
                    
                }.execute(new ApiParams().add("video_id", String.valueOf(subjectVideoId))
                                         .add("like", isLike ? "0" : "1"));
            }
        };
        
        return actionItem;
        
    }
    
    private LActionItem initWatchLaterAction(final LActionItem actionItem) {
        
        actionItem.onClick = new OnClickListener() {
            @Override public void onClick(View v) {
                new QuickApiTask(VideoActivity.this, isWatchLater 
                                                     ? Methods.albums.removeFromWatchLater
                                                     : Methods.albums.addToWatchLater) {

                    @Override
                    protected void onOk() {
                        isWatchLater = !isWatchLater;
                        
                        // TODO: add icon to toast
                        
                        Dialogs.makeToast(VideoActivity.this, getString(
                                isWatchLater ? R.string.added_to_watchlater
                                             : R.string.removed_from_watchlater));
                        
                        actionItem.icon =  
                                isWatchLater ? R.drawable.watchlater
                                             : R.drawable.watchlater_not;
                        
                    }

                    @Override
                    protected int onError() { return R.string.failed_to_modify_watch_later; }                            
                    
                }.execute(new ApiParams().add("video_id", String.valueOf(subjectVideoId)));
            }
        };
        
        return actionItem;
        
    }       
    
}
