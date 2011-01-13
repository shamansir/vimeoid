/**
 * 
 */
package org.vimeoid.activity.guest.item;

import org.vimeoid.R;
import org.vimeoid.activity.guest.SingleItemActivity;
import org.vimeoid.adapter.LActionItem;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.dto.simple.Channel;
import org.vimeoid.util.Invoke;

import android.database.Cursor;
import android.text.Html;
import android.util.Log;
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
public class ChannelActivity extends SingleItemActivity<Channel> {
    
    public static final String TAG = "ChannelActivity";

    public ChannelActivity() {
        super(R.layout.view_single_channel, Channel.SINGLE_PROJECTION);
    }

    @Override
    protected Channel extractFromCursor(Cursor cursor, int position) {
        return Channel.singleFromCursor(cursor, position);
    }
    
    @Override
    protected void onItemReceived(final Channel channel) {
        
        // biography        
        ((TextView)findViewById(R.id.channelDescription)).setText((channel.description.length() > 0) 
                                                       ? Html.fromHtml(channel.description)
                                                       : getString(R.string.no_description_supplied));
        if (channel.logoHeader.length() > 0) {
            Log.d(TAG, "Logo header URL: " + channel.logoHeader);
            imageLoader.displayImage(channel.logoHeader, (ImageView)findViewById(R.id.channelHeader));
        }
            
        super.onItemReceived(channel);
    }    

    @Override
    protected SectionedActionsAdapter fillWithActions(SectionedActionsAdapter actionsAdapter, final Channel channel) {
        
        // Statistics section
        int statsSection = actionsAdapter.addSection(getString(R.string.statistics));
        // number of videos
        final LActionItem videoAction = actionsAdapter.addAction(statsSection, R.drawable.video, 
                                        getQString(R.plurals.num_of_videos, (int)channel.videosCount));
        if (channel.videosCount > 0) {
            videoAction.onClick = new OnClickListener() {
                @Override public void onClick(View v) { Invoke.Guest.selectChannelContent(ChannelActivity.this, channel); };
            };
        }
        // number of subsribers
        actionsAdapter.addAction(statsSection, R.drawable.subscribe, 
                                 getQString(R.plurals.num_of_subscribers, (int)channel.subscribersCount));
        
        // Information section
        int infoSection = actionsAdapter.addSection(getString(R.string.information));
        //actionsAdapter.addAction(infoSection, R.drawable.info, channel.logoHeader);
        // creator
        final LActionItem creatorAction = actionsAdapter.addAction(infoSection, R.drawable.contact,
                                 getString(R.string.created_by, channel.creatorDisplayName));
        creatorAction.onClick = new OnClickListener() {
            @Override public void onClick(View v) { Invoke.Guest.selectCreator(ChannelActivity.this, channel); };
        };        
        // created on
        actionsAdapter.addAction(infoSection, R.drawable.duration,
                                 getString(R.string.created_on, channel.createdOn));
        return actionsAdapter;
    }

}
