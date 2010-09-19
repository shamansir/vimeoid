/**
 * 
 */
package org.vimeoid.activity.guest.item;

import org.vimeoid.R;
import org.vimeoid.activity.guest.ItemActivity;
import org.vimeoid.adapter.ActionItem;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.dto.simple.Album;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Utils;

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
 * <code>Video</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 10, 2010 12:15:38 AM 
 *
 */
public class AlbumActivity extends ItemActivity<Album> {
    
    public static final String TAG = "Album";
    
    public AlbumActivity() {
        super(R.layout.view_single_album, Album.FULL_EXTRACT_PROJECTION);
    }

    @Override
    protected void onItemReceived(final Album album) {
        
        // thumbnail
        imageLoader.displayImage(album.mediumThumbnailUrl, (ImageView)findViewById(R.id.albumThumb));
        
        // description
        ((TextView)findViewById(R.id.albumDescription)).setText((album.description.length() > 0) 
                                                                 ? Html.fromHtml(album.description)
                                                                 : getString(R.string.no_description_supplied));        
        
        super.onItemReceived(album);
       
    }
    
    @Override
    protected SectionedActionsAdapter fillWithActions(final SectionedActionsAdapter actionsAdapter, final Album album) {
    	
    	// Statistics section
    	int statsSection = actionsAdapter.addSection(getString(R.string.statistics));
    	// number of videos
    	final ActionItem videoAction = actionsAdapter.addAction(statsSection, R.drawable.video, 
    			          Utils.format(getString(R.string.num_of_videos), "num", String.valueOf(album.videosCount)));
    	if (album.videosCount > 0) {
    	    videoAction.onClick =  new OnClickListener() {
                @Override public void onClick(View v) { Invoke.Guest.selectAlbumContent(AlbumActivity.this, album); };
            };
    	}
    	
    	// Information section
    	int infoSection = actionsAdapter.addSection(getString(R.string.information));
        // creator
        final ActionItem userAction = actionsAdapter.addAction(infoSection, R.drawable.contact, 
                                 Utils.format(getString(R.string.uploaded_by), "name", album.creatorDisplayName));
        userAction.onClick =  new OnClickListener() {
            @Override public void onClick(View v) { Invoke.Guest.selectCreator(AlbumActivity.this, album); };
        };    	
    	// updated on
    	actionsAdapter.addAction(infoSection, R.drawable.duration,
			     				 Utils.format(getString(R.string.updated_on), "time", album.lastModifiedOn));
    	return actionsAdapter;
    	
    }

    @Override
    protected Album extractFromCursor(Cursor cursor, int position) {        
        return Album.fullFromCursor(cursor, position);
    }    
    
}
