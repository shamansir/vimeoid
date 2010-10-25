/**
 * 
 */
package org.vimeoid.adapter.user;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.adapter.TagsSupport;
import org.vimeoid.dto.advanced.Video;
import org.vimeoid.util.Utils;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.user</dd>
 * </dl>
 *
 * <code>VideosListAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 29, 2010 8:31:27 PM 
 *
 */
public class VideosListAdapter extends JsonObjectsAdapter<Video> implements OnItemSelectedListener {
	
	public static interface ThumbClickListener {
		public void thumbClicked(Video video);
	}
    
    private final LayoutInflater layoutInflater;
    private final ImageLoader imageLoader;
    
    private View lastSelected; 
    private ThumbClickListener thumbClickListener;
    
    public VideosListAdapter(Context context, LayoutInflater inflater) {
        super(Video.FieldsKeys.MULTIPLE_KEY);
        
        this.layoutInflater = inflater;        
        this.imageLoader = new ImageLoader(context, R.drawable.thumb_loading_small, R.drawable.video_unknown_item);
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        VideoItemViewHolder itemHolder = null;
        
        final Video video = (Video)getItem(position); 
        
        if (convertView == null) {
            
            convertView = layoutInflater.inflate(R.layout.item_video, parent, false);
            itemHolder = new VideoItemViewHolder();
                
            itemHolder.ivThumb = (ImageView) convertView.findViewById(R.id.videoItemImage);
            itemHolder.ivPlay = (ImageView) convertView.findViewById(R.id.playVideo);
            itemHolder.tvTitle = (TextView) convertView.findViewById(R.id.videoItemTitle);
            itemHolder.tvAuthor = (TextView) convertView.findViewById(R.id.videoItemAuthor);
            itemHolder.tvDuration = (TextView) convertView.findViewById(R.id.videoItemDuration);
            itemHolder.llTags = (LinearLayout) convertView.findViewById(R.id.videoItemTags);
            
            itemHolder.tvLikes = (TextView) convertView.findViewById(R.id.videoItemNumOfLikes);
            itemHolder.tvPlays = (TextView) convertView.findViewById(R.id.videoItemNumOfPlays);
            itemHolder.tvComments = (TextView) convertView.findViewById(R.id.videoItemNumOfComments);
            
            itemHolder.vgMarkers = (ViewGroup) convertView.findViewById(R.id.markersArea);
                
            convertView.setTag(itemHolder);
            
        } else {
            
            itemHolder = (VideoItemViewHolder) convertView.getTag();
            
        }
        
        imageLoader.displayImage(video.thumbnails.small.url, itemHolder.ivThumb);
              
        itemHolder.ivPlay.setVisibility(View.INVISIBLE);
        itemHolder.ivThumb.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				//Log.d("VLA", "Thumb at " + position + " is clicked");
				if (thumbClickListener != null) thumbClickListener.thumbClicked(video);
			}
		});
        
        itemHolder.tvTitle.setText(video.title);
        itemHolder.tvAuthor.setText(video.uploaderName);
        itemHolder.tvDuration.setText(Utils.adaptDuration(video.duration));
        TagsSupport.injectTags(layoutInflater, video.tags, itemHolder.llTags);
        
        itemHolder.tvLikes.setText(String.valueOf(video.likesCount));
        itemHolder.tvPlays.setText(String.valueOf(video.playsCount));
        itemHolder.tvComments.setText(String.valueOf(video.commentsCount));
        
        MarkersSupport.injectMarkers(layoutInflater, itemHolder.vgMarkers, getRequiredMarkers(video));
        
        /* if (video.isLike) convertView.setBackgroundResource(R.drawable.like_marker);
        else if (video.isWatchLater) convertView.setBackgroundResource(R.drawable.watchlater_marker);
        else convertView.setBackgroundResource(0); */
        
        return convertView;
    }
    
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		//Log.d("VLA", "Thumb at " + position + " is selected");
		if (lastSelected != null) lastSelected.findViewById(R.id.playVideo).setVisibility(View.INVISIBLE); 
		view.findViewById(R.id.playVideo).setVisibility(View.VISIBLE);
		lastSelected = view;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//Log.d("VLA", "Nothing selected");
		if (lastSelected != null) lastSelected.findViewById(R.id.playVideo).setVisibility(View.INVISIBLE);		
	}    
    
    protected int[] getRequiredMarkers(Video video) {        
        if (((video.isLike != null) && video.isLike) &&
            ((video.isWatchLater == null) || !video.isWatchLater)) 
                               { return new int[] { R.drawable.like_marker }; };
        if (((video.isLike == null) || !video.isLike) && 
            ((video.isWatchLater != null) && video.isWatchLater)) 
                               { return new int[] { R.drawable.watchlater_marker }; };
        if (((video.isLike != null) && video.isLike) && 
            ((video.isWatchLater != null) || video.isWatchLater)) 
                               { return new int[] { R.drawable.like_marker, R.drawable.watchlater_marker }; };
        return new int[0];
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        imageLoader.clearCache();
    }
    
    @Override
    protected Video[] extractItems(JSONObject jsonObject) throws JSONException {
        return Video.collectListFromJson(jsonObject);
    }

    public void updateLikes(ListView holder, Set<Long> videosIds) {
        final Set<Long> videosList = new HashSet<Long>();
        videosList.add(Long.valueOf(15166258));
        for (Video video: getItems()) {
            video.isLike = videosList.contains(video.getId());
        }
        holder.invalidateViews();
    }

    public void updateWatchLaters(ListView holder, Set<Long> videosIds) {
        final Set<Long> videosList = new HashSet<Long>();
        videosList.add(Long.valueOf(14011251));
        for (Video video: getItems()) {
            video.isWatchLater = videosList.contains(video.getId());
        }
        holder.invalidateViews();
    }
    
    public Video switchWatchLater(AdapterView<?> holder, int position) {
        final Video subject = (Video)getItem(position);
        subject.isWatchLater = !subject.isWatchLater;
        final View itemView = Utils.getItemViewIfVisible(holder, position);
        if (itemView != null) {
            itemView.invalidate();
            MarkersSupport.injectMarkers(layoutInflater, (ViewGroup)itemView.findViewById(R.id.markersArea), getRequiredMarkers(subject));
        }
        return subject;
    }
    
    public void setThumbClickListener(ThumbClickListener listener) {
    	this.thumbClickListener = listener;
    }
    
    private class VideoItemViewHolder {
        
        ImageView ivThumb;
        ImageView ivPlay;
        
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvDuration;
        LinearLayout llTags;
        
        TextView tvLikes;
        TextView tvPlays;
        TextView tvComments;
        
        ViewGroup vgMarkers;
    }


}
