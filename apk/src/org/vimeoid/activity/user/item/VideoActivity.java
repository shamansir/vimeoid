/**
 * 
 */
package org.vimeoid.activity.user.item;

import org.json.JSONException;
import org.json.JSONObject;

import org.vimeoid.R;
import org.vimeoid.activity.user.SingleItemActivity;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.dto.advanced.Video;
import org.vimeoid.util.Invoke;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
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
    
    //private static final int LOAD_PORTRAITS_TASK = 1;
    //private static final int LOAD_ALBUMS_TASK = 2;
    //private static final int LOAD_CHANNELS_TASK = 3;
    
    //private LActionItem albumAction;
    //private LActionItem channelAction;
    
    public VideoActivity() {
        super(R.layout.view_single_video);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        //userId = getIntent().getExtras().getLong(Invoke.Extras.USER_ID);
        
        //final String userIdStr = String.valueOf(userId);
        
        //secondaryTasks.add(LOAD_PORTRAITS_TASK, Methods.people.getPortraitUrls, new ApiParams().add("user_id", userIdStr));
        //secondaryTasks.add(LOAD_ALBUMS_TASK, Methods.albums.getAll, new ApiParams().add("user_id", userIdStr));
        //secondaryTasks.add(LOAD_CHANNELS_TASK, Methods.channels.getAll, new ApiParams().add("user_id", userIdStr));                
        
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Video extractFromJson(JSONObject jsonObj) throws JSONException {
        return Video.collectFromJson(jsonObj);
    }

    @Override
    protected SectionedActionsAdapter fillWithActions(SectionedActionsAdapter actionsAdapter, final Video video) {
        
        // TODO: add "watch later" and "like" if it is not owner video
        
        
        return actionsAdapter;
    }
    
    @Override
    protected void onItemReceived(final Video video) {
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
    }
    
    /* @Override
    public void onSecondaryTaskPerfomed(int taskId, JSONObject result) throws JSONException {
        switch (taskId) {
            case LOAD_PORTRAITS_TASK: {
                final ImageView uploaderPortrait = (ImageView)findViewById(R.id.userPortrait);
                final String mediumPortraitUrl = result.getJSONObject(PortraitsData.FieldsKeys.MULTIPLE_KEY)
                                                       .getJSONArray(PortraitsData.FieldsKeys.SINGLE_KEY)
                                                       .getJSONObject(2).getString(PortraitsData.FieldsKeys.URL);
                imageLoader.displayImage(mediumPortraitUrl, uploaderPortrait);
            }; break;
            case LOAD_ALBUMS_TASK: {
                final int albumsCount = result.getJSONObject("albums").getInt("total");                
                Log.d(TAG, "got albums count, its " + albumsCount);
                albumAction.title = Utils.format(getString(R.string.num_of_albums), "num", String.valueOf(albumsCount));
                if (albumsCount == 0) albumAction.onClick = null;
                getActionsList().invalidateViews();
            }; break;
            case LOAD_CHANNELS_TASK: {
                final int channelsCount = result.getJSONObject("channels").getInt("total");                
                Log.d(TAG, "got channels count, its " + channelsCount);
                channelAction.title = Utils.format(getString(R.string.num_of_channels), "num", String.valueOf(channelsCount));
                if (channelsCount == 0) channelAction.onClick = null;
                getActionsList().invalidateViews();
            }; break;            
        }
    } */

}
