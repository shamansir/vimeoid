/**
 * 
 */
package org.vimeoid.adapter.guest;

import org.vimeoid.R;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.dto.simple.Channel;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ChannelsListAdapter extends EasyCursorsAdapter<Channel> {
    
    private final LayoutInflater layoutInflater;
    private final ImageLoader imageLoader;    

    public ChannelsListAdapter(Context context, LayoutInflater inflater) {
        super(Channel.FieldsKeys._ID);

        this.layoutInflater = inflater;        
        
        this.imageLoader = new ImageLoader(context, R.drawable.thumb_loading_small, R.drawable.video_unknown_item);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChannelItemViewHolder itemHolder = null;
        
        final Channel channel = (Channel)getItem(position); 
        
        if (convertView == null) {
            
            convertView = layoutInflater.inflate(R.layout.item_channel, parent, false);
            itemHolder = new ChannelItemViewHolder();
                
            itemHolder.tvName = (TextView) convertView.findViewById(R.id.channelItemName);
            itemHolder.tvAuthor = (TextView) convertView.findViewById(R.id.channelItemAuthor);
            itemHolder.tvCreatedOn = (TextView) convertView.findViewById(R.id.channelItemCreatedOn);
            
            itemHolder.tvVideos = (TextView) convertView.findViewById(R.id.channelItemNumOfVideos);
            itemHolder.tvSubsribers = (TextView) convertView.findViewById(R.id.channelItemNumOfSubscribers);
                
            convertView.setTag(itemHolder);
            
        } else {
            
            itemHolder = (ChannelItemViewHolder) convertView.getTag();
            
        }
        
        itemHolder.tvName.setText(channel.name);
        itemHolder.tvAuthor.setText(channel.creatorDisplayName);
        itemHolder.tvCreatedOn.setText(channel.createdOn);
        
        itemHolder.tvVideos.setText(String.valueOf(channel.videosCount));
        itemHolder.tvSubsribers.setText(String.valueOf(channel.subscribersCount));
        
        return convertView;
    }
    
    @Override
    public void finalize() {
        super.finalize();
        imageLoader.clearCache();
    }
    
    private class ChannelItemViewHolder {
        
        TextView tvName;
        TextView tvAuthor;
        TextView tvCreatedOn;
        
        TextView tvVideos;
        TextView tvSubsribers;
        
    }

	@Override
	public Channel extractItem(Cursor cursor, int position) {
		return Channel.shortFromCursor(cursor, position);
	}

}
