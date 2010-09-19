/**
 * 
 */
package org.vimeoid.adapter.guest;

import org.vimeoid.R;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.dto.simple.Album;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.guest</dd>
 * </dl>
 *
 * <code>ChannelsListAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 5, 2010 8:35:01 PM 
 *
 */
public class AlbumsListAdapter extends EasyCursorsAdapter<Album> {
    
    private final LayoutInflater layoutInflater;
    private final ImageLoader imageLoader;    

    public AlbumsListAdapter(Context context, LayoutInflater inflater) {
        super(Album.FieldsKeys._ID);

        this.layoutInflater = inflater;        
        
        this.imageLoader = new ImageLoader(context, R.drawable.thumb_loading_small, R.drawable.item_failed_small);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumItemViewHolder itemHolder = null;
        
        final Album album = (Album)getItem(position); 
        
        if (convertView == null) {
            
            convertView = layoutInflater.inflate(R.layout.item_album, parent, false);
            itemHolder = new AlbumItemViewHolder();
                
            itemHolder.ivThumb = (ImageView) convertView.findViewById(R.id.albumItemImage);
            
            itemHolder.tvTitle = (TextView) convertView.findViewById(R.id.albumItemTitle);
            itemHolder.tvAuthor = (TextView) convertView.findViewById(R.id.albumItemAuthor);
            itemHolder.tvUpdatedOn = (TextView) convertView.findViewById(R.id.albumItemUpdatedOn);
            
            itemHolder.tvVideos = (TextView) convertView.findViewById(R.id.albumItemNumOfVideos);
                
            convertView.setTag(itemHolder);
            
        } else {
            
            itemHolder = (AlbumItemViewHolder) convertView.getTag();
            
        }
        
        imageLoader.displayImage(album.thumbnail, itemHolder.ivThumb);
        
        itemHolder.tvTitle.setText(album.title);
        itemHolder.tvAuthor.setText(album.creatorDisplayName);
        itemHolder.tvUpdatedOn.setText(album.lastModifiedOn);
        
        itemHolder.tvVideos.setText(String.valueOf(album.videosCount));
        
        return convertView;
    }
    
    @Override
    public void finalize() {
        super.finalize();
        imageLoader.clearCache();
    }
    
    private class AlbumItemViewHolder {
        
        ImageView ivThumb;
        
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvUpdatedOn;                
        
        TextView tvVideos;
        
    }

	@Override
	public Album extractItem(Cursor cursor, int position) {
		return Album.shortFromCursor(cursor, position);
	}

}
