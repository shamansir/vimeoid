/**
 * 
 */
package org.vimeoid.activity.guest.item;

import org.vimeoid.R;
import org.vimeoid.activity.guest.SingleItemActivity;
import org.vimeoid.adapter.LActionItem;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.dto.simple.User;
import org.vimeoid.util.Invoke;

import android.database.Cursor;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.guest.item</dd>
 * </dl>
 *
 * <code>UserActivity</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 16, 2010 6:38:02 PM 
 *
 */
public class UserActivity extends SingleItemActivity<User> {
    
    public static final String TAG = "UserActivity";

    public UserActivity() {
        super(R.layout.view_single_user, User.SINGLE_PROJECTION);
    }

    @Override
    protected User extractFromCursor(Cursor cursor, int position) {
        return User.singleFromCursor(cursor, position);
    }
    
    @Override
    protected void onItemReceived(final User user) {
        
        // biography        
        ((TextView)findViewById(R.id.userBio)).setText((user.biography.length() > 0) 
                                                       ? Html.fromHtml(user.biography)
                                                       : getString(R.string.no_biography_supplied));
        
        // user portrait
        final ImageView uploaderPortrait = (ImageView)findViewById(R.id.userPortrait);
        imageLoader.displayImage(user.mediumPortraitUrl, uploaderPortrait);        
        
        super.onItemReceived(user);
       
    }    

    @Override
    protected SectionedActionsAdapter fillWithActions(SectionedActionsAdapter actionsAdapter, final User user) {
        
        // Statistics section
        int statsSection = actionsAdapter.addSection(getString(R.string.statistics));
        // number of videos
        final LActionItem videoAction = actionsAdapter.addAction(statsSection, R.drawable.video, 
                                        getQString(R.plurals.num_of_videos, (int)user.videosUploaded));
        if (user.videosUploaded > 0) {
            videoAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.Guest.selectVideosBy(UserActivity.this, user); };
            };
        }
        // number of albums
        final LActionItem albumAction = actionsAdapter.addAction(statsSection, R.drawable.album, 
                                        getQString(R.plurals.num_of_albums, (int)user.albumsCount));
        if (user.albumsCount > 0) {
            albumAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.Guest.selectAlbumsOf(UserActivity.this, user); };
            };
        }
        // number of channels
        final LActionItem channelAction = actionsAdapter.addAction(statsSection, R.drawable.channel, 
                                          getQString(R.plurals.num_of_channels, (int)user.channelsCount));
        if (user.channelsCount > 0) {
            channelAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.Guest.selectChannelsOf(UserActivity.this, user); };
            };
        }
        // number of contacts
        actionsAdapter.addAction(statsSection, R.drawable.contact, 
                       getQString(R.plurals.num_of_contacts, (int)user.contactsCount));
        // number of appearances
        final LActionItem appearanceAction = actionsAdapter.addAction(statsSection, R.drawable.appearance,
                       getQString(R.plurals.num_of_appearances, (int)user.videosAppearsIn));
        if (user.videosAppearsIn > 0) {
            appearanceAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.Guest.selectApperancesOf(UserActivity.this, user); };
            };
        }
        // number of likes
        final LActionItem likeAction = actionsAdapter.addAction(statsSection, R.drawable.like, 
                       getQString(R.plurals.num_of_likes, (int)user.videosLiked));
        if (user.videosLiked > 0) {	
        	likeAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.Guest.selectLikesOf(UserActivity.this, user); };
            };
        }
        // subsriptions
        final LActionItem subscriptionAction = actionsAdapter.addAction(statsSection, R.drawable.subscribe, 
        																			 getString(R.string.subscriptions));
        subscriptionAction.onClick =  new OnClickListener() {
            @Override public void onClick(View v) { Invoke.Guest.selectSubsriptionsOf(UserActivity.this, user); };
        };        
        
        // Information section
        int infoSection = actionsAdapter.addSection(getString(R.string.information));
        // location
        if (user.location.length() > 0) {
        	actionsAdapter.addAction(infoSection, R.drawable.location,
                           getString(R.string.location_is, user.location));        			
        }
        // created on
        actionsAdapter.addAction(infoSection, R.drawable.duration,
                       getString(R.string.created_on, user.createdOn));
        // from staff
        if (user.fromStaff) {
            actionsAdapter.addAction(infoSection, R.drawable.staff, getString(R.string.user_from_staff));    
        }
        // plus member
        if (user.isPlusMember) {
            actionsAdapter.addAction(infoSection, R.drawable.plus, getString(R.string.user_is_plus_member));    
        }        
        
        return actionsAdapter;
    }

}
