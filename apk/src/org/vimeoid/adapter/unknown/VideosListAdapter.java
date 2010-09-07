/**
 * 
 */
package org.vimeoid.adapter.unknown;

import org.vimeoid.R;
import org.vimeoid.adapter.EasyCursorAdapter;
import org.vimeoid.dto.simple.Video;

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
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.unknown</dd>
 * </dl>
 *
 * <code>VideosListAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 5, 2010 8:35:01 PM 
 *
 */
public class VideosListAdapter extends EasyCursorAdapter<Video> {
    
    private final LayoutInflater layoutInflater;
    private final ImageLoader imagesLoader;    

    public VideosListAdapter(Context context, LayoutInflater inflater, Cursor cursor) {
        super(cursor);

        this.layoutInflater = inflater;        
        
        this.imagesLoader = new ImageLoader(context, R.drawable.thumb_loading_small, R.drawable.video_unknown_item);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VideoItemViewHolder itemHolder = null;
        
        final Video video = (Video)getItem(position); 
        
        if (convertView == null) {
            
            convertView = layoutInflater.inflate(R.layout.item_video, parent, false);
            itemHolder = new VideoItemViewHolder();
                
            itemHolder.ivThumb = (ImageView) convertView.findViewById(R.id.videoItemImage);
            itemHolder.tvTitle = (TextView) convertView.findViewById(R.id.videoItemTitle);
            itemHolder.tvAuthor = (TextView) convertView.findViewById(R.id.videoItemAuthor);
            itemHolder.tvDuration = (TextView) convertView.findViewById(R.id.videoItemDuration);
            itemHolder.tvTags = (TextView) convertView.findViewById(R.id.videoItemTags);
            
            itemHolder.tvLikes = (TextView) convertView.findViewById(R.id.videoItemNumOfLikes);
            itemHolder.tvPlays = (TextView) convertView.findViewById(R.id.videoItemNumOfPlays);
            itemHolder.tvComments = (TextView) convertView.findViewById(R.id.videoItemNumOfComments);
                
            convertView.setTag(itemHolder);
            
        } else {
            
            itemHolder = (VideoItemViewHolder) convertView.getTag();
            
        }
        
        itemHolder.ivThumb.setTag(video.smallThumbnailUrl);
        imagesLoader.displayImage(video.smallThumbnailUrl, itemHolder.ivThumb);
        
        itemHolder.tvTitle.setText(video.title);
        itemHolder.tvAuthor.setText(video.uploaderName);
        itemHolder.tvDuration.setText(String.valueOf(video.duration));
        //itemHolder.tvTags.setText(video.tags);
        
        itemHolder.tvLikes.setText(String.valueOf(video.likesCount));
        itemHolder.tvPlays.setText(String.valueOf(video.playsCount));
        itemHolder.tvComments.setText(String.valueOf(video.commentsCount));
        
        return convertView;
    }
    
    @Override
    public void finalize() {
        super.finalize();
        imagesLoader.clearCache();
    }
    
    private class VideoItemViewHolder {
        
        ImageView ivThumb;
        
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvDuration;
        TextView tvTags;
        
        TextView tvLikes;
        TextView tvPlays;
        TextView tvComments;
    }

    @Override
    public Video extractItem(Cursor cursor, int position) {
        final Video result = new Video();
        
        result.id = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys._ID));
        result.title = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.TITLE));
        result.uploaderName = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.AUTHOR));
        result.duration = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys.DURATION));
        
        final String tags = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.TAGS));
        result.tags = (tags != null) ? tags.split(",") : new String[0];
        
        result.likesCount = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys.NUM_OF_LIKES));
        result.playsCount = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys.NUM_OF_PLAYS));
        result.commentsCount = cursor.getLong(cursor.getColumnIndex(Video.FieldsKeys.NUM_OF_COMMENTS));
        
        result.smallThumbnailUrl = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.THUMB_SMALL));
        result.smallUploaderPortraitUrl = cursor.getString(cursor.getColumnIndex(Video.FieldsKeys.USER_IMG_SMALL));
        
        return result;
    }

}
