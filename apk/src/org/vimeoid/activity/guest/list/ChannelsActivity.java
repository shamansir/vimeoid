package org.vimeoid.activity.guest.list;

import org.vimeoid.R;
import org.vimeoid.activity.guest.ItemsListActivity;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.adapter.guest.ChannelsListAdapter;
import org.vimeoid.dto.simple.Channel;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

import android.content.Intent;
import android.view.MenuItem;

/**
 * 
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>Channels</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 3, 2010 11:58:57 PM 
 *
 */
public class ChannelsActivity extends ItemsListActivity<Channel> {

	public ChannelsActivity() {
		super(Channel.ITEM_PROJECTION, R.menu.channel_context_guest_menu);
	}

    @Override
    protected void onItemSelected(Channel channel) {
        String action = getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action) ||
            Intent.ACTION_GET_CONTENT.equals(action))
        { 
            Invoke.Guest.pickChannel(this, channel);
        } else {
            Invoke.Guest.selectChannel(this, channel);
        }        
    }
    
	@Override
	protected EasyCursorsAdapter<Channel> createAdapter() {
		return new ChannelsListAdapter(this, getLayoutInflater());
	}
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        
        Channel channel = getItem(extractPosition(item));         
        
        switch (item.getItemId()) {
            case R.id.menu_viewInfo: Invoke.Guest.selectChannel(this, channel); break;
            case R.id.menu_viewAuthorInfo: Invoke.Guest.selectCreator(this, channel); break;
            case R.id.menu_viewChannelVideos: Invoke.Guest.selectChannelContent(this, channel); break;
            default: Dialogs.makeToast(this, getString(R.string.unknown_item));
        }
        return super.onContextItemSelected(item);
        
    }    
	
}