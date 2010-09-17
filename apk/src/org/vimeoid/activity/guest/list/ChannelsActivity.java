package org.vimeoid.activity.guest.list;

import org.vimeoid.R;
import org.vimeoid.activity.guest.ItemsListActivity;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.adapter.guest.ChannelsListAdapter;
import org.vimeoid.dto.simple.Channel;

import android.view.Menu;
import android.view.MenuInflater;

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
		super(Channel.SHORT_EXTRACT_PROJECTION, R.menu.video_context_guest_menu);
	}

    @Override
    protected void onItemSelected(Channel channel) {
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.main_options_menu, menu); 
        
        return true;
    }
	
	@Override
	protected EasyCursorsAdapter<Channel> createAdapter() {
		return new ChannelsListAdapter(this, getLayoutInflater());
	}
    
    
}