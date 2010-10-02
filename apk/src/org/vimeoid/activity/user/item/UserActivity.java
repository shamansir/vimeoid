/**
 * 
 */
package org.vimeoid.activity.user.item;

import org.json.JSONException;
import org.json.JSONObject;

import org.vimeoid.R;
import org.vimeoid.activity.user.SingleItemActivity;
import org.vimeoid.adapter.LActionItem;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.dto.advanced.PortraitsData;
import org.vimeoid.dto.advanced.User;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Utils;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
public class UserActivity extends SingleItemActivity<User> {
    
    public static final String TAG = "UserActivity";
    
    private static final int LOAD_PORTRAITS_TASK = 1;
    private static final int LOAD_ALBUMS_TASK = 2;
    private static final int LOAD_CHANNELS_TASK = 3;
    
    private LActionItem albumAction;
    private LActionItem channelAction;
    
    public UserActivity() {
        super(R.layout.view_single_user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        final String subjectUserId = String.valueOf(getIntent().getExtras().getLong(Invoke.Extras.USER_ID));
        
        secondaryTasks.add(LOAD_PORTRAITS_TASK, Methods.people.getPortraitUrls, new ApiParams().add("user_id", subjectUserId));
        secondaryTasks.add(LOAD_ALBUMS_TASK, Methods.albums.getAll, new ApiParams().add("user_id", subjectUserId));
        secondaryTasks.add(LOAD_CHANNELS_TASK, Methods.channels.getAll, new ApiParams().add("user_id", subjectUserId));
        // load subscriptions and check if subscribed // people.getSubscriptions (likes/)
        // check contacts if friends // contacts.getAll
        
        super.onCreate(savedInstanceState);
    }

    @Override
    protected User extractFromJson(JSONObject jsonObj) throws JSONException {
        return User.collectFromJson(jsonObj);
    }

    @Override
    protected SectionedActionsAdapter fillWithActions(SectionedActionsAdapter actionsAdapter, final User user) {
        
        // TODO: add "subscribe" and "add contact" if it is not current user
        // mark if already subscribed or friends 
        final long currentUserId = VimeoApi.getUserLoginData(this).id;
        if (currentUserId != user.id) {
            int operationsSection = actionsAdapter.addSection(getString(R.string.operations));
            // subscribe
            final LActionItem subscribeAction = actionsAdapter.addAction(operationsSection, R.drawable.subscribe, 
                                                                         R.string.subscribe);
            
            final LActionItem addContactAction = actionsAdapter.addAction(operationsSection, R.drawable.contact, 
                                                                          R.string.addContact);
        }
        
        // Statistics section
        int statsSection = actionsAdapter.addSection(getString(R.string.statistics));
        // number of videos
        final LActionItem videoAction = actionsAdapter.addAction(statsSection, R.drawable.video, 
                                 Utils.format(getString(R.string.num_of_videos), "num", String.valueOf(user.videosCount)));
        if (user.videosCount > 0) {
            videoAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.User_.selectVideosBy(UserActivity.this, user); };
            };
        }
        // number of albums
        albumAction = actionsAdapter.addAction(statsSection, R.drawable.album, 
                      Utils.format(getString(R.string.num_of_albums), "num", "?"));
        albumAction.onClick =  new OnClickListener() {
            @Override public void onClick(View v) { 
                Invoke.User_.selectAlbumsOf(UserActivity.this, user);
            };
        };
        // number of channels
        channelAction = actionsAdapter.addAction(statsSection, R.drawable.channel, 
                        Utils.format(getString(R.string.num_of_channels), "num", "?"));
        channelAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.User_.selectChannelsOf(UserActivity.this, user); };
        };
        // number of contacts
        final LActionItem contactAction = actionsAdapter.addAction(statsSection, R.drawable.contact, 
                        Utils.format(getString(R.string.num_of_contacts), "num", String.valueOf(user.contactsCount)));
        if (user.contactsCount > 0) {
            contactAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.User_.selectContactsOf(UserActivity.this, user); };
            };
        }
        // number of appearances
        final LActionItem appearanceAction = actionsAdapter.addAction(statsSection, R.drawable.appearance, 
                        Utils.format(getString(R.string.num_of_appearances), "num", String.valueOf(user.videosAppearsIn)));
        if (user.videosAppearsIn > 0) {
            appearanceAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.User_.selectApperancesOf(UserActivity.this, user); };
            };
        }
        // number of likes
        final LActionItem likeAction = actionsAdapter.addAction(statsSection, R.drawable.like, 
                        Utils.format(getString(R.string.num_of_likes), "num", String.valueOf(user.videosLiked)));
        if (user.videosLiked > 0) { 
            likeAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.User_.selectLikesOf(UserActivity.this, user); };
            };
        }
        // subsriptions
        final LActionItem subscriptionAction = actionsAdapter.addAction(statsSection, R.drawable.subscribe, 
                                                                                     getString(R.string.subscriptions));
        subscriptionAction.onClick =  new OnClickListener() {
            @Override public void onClick(View v) { Invoke.User_.selectSubsriptionsOf(UserActivity.this, user); };
        };        
        
        // Information section
        int infoSection = actionsAdapter.addSection(getString(R.string.information));
        // location
        if (user.location.length() > 0) {
            actionsAdapter.addAction(infoSection, R.drawable.location,
                    Utils.format(getString(R.string.location_is), "place", user.location));                 
        }
        // created on
        actionsAdapter.addAction(infoSection, R.drawable.duration,
                                 Utils.format(getString(R.string.created_on), "time", user.createdOn));
        // from staff
        if (user.fromStaff) {
            actionsAdapter.addAction(infoSection, R.drawable.staff, getString(R.string.user_from_staff));    
        }
        // plus member
        if (user.isPlusMember) {
            actionsAdapter.addAction(infoSection, R.drawable.plus, getString(R.string.user_is_plus_member));    
        }
        
        // TODO: add websites URLs
        
        return actionsAdapter;
    }
    
    @Override
    protected void onItemReceived(User user) {
        ((TextView)findViewById(R.id.userBio)).setText((user.biography.length() > 0) 
                                                       ? Html.fromHtml(user.biography)
                                                       : getString(R.string.no_biography_supplied));
        
        super.onItemReceived(user);
    }
    
    @Override
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
    }

}
