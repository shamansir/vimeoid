package org.vimeoid.activity.guest.list;

import org.vimeoid.R;
import org.vimeoid.activity.guest.ItemsListActivity;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.adapter.guest.AlbumsListAdapter;
import org.vimeoid.dto.simple.Album;
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
public class AlbumsActivity extends ItemsListActivity<Album> {

	public AlbumsActivity() {
		super(Album.SHORT_EXTRACT_PROJECTION, R.menu.album_context_guest_menu);
	}

    @Override
    protected void onItemSelected(Album album) {
        String action = getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action) ||
            Intent.ACTION_GET_CONTENT.equals(action))
        { 
            Invoke.Guest.pickAlbum(this, album);
        } else {
            Invoke.Guest.selectAlbum(this, album);
        }        
    }
    
	@Override
	protected EasyCursorsAdapter<Album> createAdapter() {
		return new AlbumsListAdapter(this, getLayoutInflater());
	}
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        
        Album album = getItem(extractPosition(item));         
        
        switch (item.getItemId()) {
            case R.id.menu_viewInfo: Invoke.Guest.selectAlbum(this, album); break;
            case R.id.menu_viewAuthorInfo: Invoke.Guest.selectCreator(this, album); break;
            case R.id.menu_viewAlbumVideos: Invoke.Guest.selectAlbumContent(this, album); break;
            default: Dialogs.makeToast(this, getString(R.string.unknown_item));
        }
        return super.onContextItemSelected(item);
        
    }    
	
}