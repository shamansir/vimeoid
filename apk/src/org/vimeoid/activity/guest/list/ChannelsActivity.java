package org.vimeoid.activity.guest.list;

import org.vimeoid.activity.guest.ItemsListActivity;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.dto.simple.Channel;

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

	public ChannelsActivity(String[] projection) {
		super(Channel.SHORT_EXTRACT_PROJECTION);
	}

	@Override
	protected EasyCursorsAdapter<Channel> createAdapter() {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}