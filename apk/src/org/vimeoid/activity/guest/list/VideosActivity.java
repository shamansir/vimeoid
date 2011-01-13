package org.vimeoid.activity.guest.list;

import android.content.Intent;
import android.view.MenuItem;

import org.vimeoid.R;
import org.vimeoid.activity.guest.ItemsListActivity;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.adapter.guest.VideosListAdapter;
import org.vimeoid.dto.simple.Video;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Utils;

/**
 * 
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>Videos</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 3, 2010 11:58:57 PM 
 *
 */
public class VideosActivity extends ItemsListActivity<Video> {
    
    public static final String TAG = "Videos";
    
    public VideosActivity() {
    	super(Video.LIST_PROJECTION, R.menu.video_context_guest_menu);
    }
    
    @Override
    protected void onItemSelected(Video video) { 
    	String action = getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action) ||
            Intent.ACTION_GET_CONTENT.equals(action))
        { 
        	Invoke.Guest.pickVideo(this, video);
        } else {
            Invoke.Guest.selectVideo(this, video);
        }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        
        Video video = getItem(extractPosition(item));         
        
        switch (item.getItemId()) {
            case R.id.menu_Play: { Invoke.Guest.playVideo(this, video); } break;
            //case R.id.menu_watchLater: itemDescription = "WatchLater "; break;       
            // view comments, tags, ...
            case R.id.menu_viewInfo: Invoke.Guest.selectVideo(this, video); break;
            case R.id.menu_viewAuthorInfo: Invoke.Guest.selectUploader(this, video); break;
            case R.id.menu_viewAuthorVideos: Invoke.Guest.selectVideosByUploader(this, video); break;
            default: Dialogs.makeToast(this, getString(R.string.unknown_item));
        }
        return super.onContextItemSelected(item);
        
    }
    
	@Override
	protected EasyCursorsAdapter<Video> createAdapter() {
		return new VideosListAdapter(this, getLayoutInflater());
	}
	
	@Override
	public String getContextMenuTitle(int position) { 
	    return getString(R.string.video_is, Utils.crop(getItem(position).title, 20)); 
	};
    
}