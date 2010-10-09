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
import org.vimeoid.dto.advanced.Video;
import org.vimeoid.util.Utils;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.util.Log;
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
	
	public static interface PlayButtonClickedListener {
		public void playButtonClicked(Video forVideo);
	}
    
    private final LayoutInflater layoutInflater;
    private final ImageLoader imageLoader;
    
    private View lastSelected; 
    private PlayButtonClickedListener playButtonListener;
    
    public VideosListAdapter(Context context, LayoutInflater inflater, ListView listView) {
        super(Video.FieldsKeys.MULTIPLE_KEY);
        
        this.layoutInflater = inflater;        
        this.imageLoader = new ImageLoader(context, R.drawable.thumb_loading_small, R.drawable.video_unknown_item);
        
        listView.setOnItemSelectedListener(this);
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
        /* itemHolder.ivPlay.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				Log.d("VLA", "Thumb at " + position + " is focused: " + hasFocus);
				//ivPlay.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
				ImageView iv = (ImageView)v.findViewById(R.id.playVideo);
				iv.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
			}
			
		}); */
        itemHolder.ivPlay.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				Log.d("VLA", "Thumb at " + position + " is clicked");
				if (playButtonListener != null) playButtonListener.playButtonClicked(video);
			}
		});
        
        itemHolder.tvTitle.setText(video.title);
        itemHolder.tvAuthor.setText(video.uploaderName);
        itemHolder.tvDuration.setText(Utils.adaptDuration(video.duration));
        injectTags(itemHolder.llTags, position, video.tags);
        
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
		Log.d("VLA", "Thumb at " + position + " is selected");
		if (lastSelected != null) lastSelected.findViewById(R.id.playVideo).setVisibility(View.INVISIBLE); 
		view.findViewById(R.id.playVideo).setVisibility(View.VISIBLE);
		lastSelected = view;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Log.d("VLA", "Nothing selected");
		if (lastSelected != null) lastSelected.findViewById(R.id.playVideo).setVisibility(View.INVISIBLE);		
	}    
    
    protected int[] getRequiredMarkers(Video video) {
        if (video.isLike && !video.isWatchLater) { return new int[] { R.drawable.like_marker }; };
        if (!video.isLike && video.isWatchLater) { return new int[] { R.drawable.watchlater_marker }; };
        if (video.isLike && video.isWatchLater) { return new int[] { R.drawable.like_marker, R.drawable.watchlater_marker }; };
        return new int[0];
    }
    
    protected void injectTags(final ViewGroup holder, final int curPosition, final String[] tags) {
        holder.removeAllViews();
        if (tags.length == 0) {
            holder.addView(layoutInflater.inflate(R.layout.no_tags_for_item, null));
            return;
        }
        
        for (final String tag: tags) {
            final View tagStruct = layoutInflater.inflate(R.layout.tag_for_the_item, null);
            ((TextView)tagStruct.findViewById(R.id.tagItem)).setText(tag);
            holder.addView(tagStruct);
        }
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

    public void updateLikes(Set<Long> videosIds) {
        final Set<Long> videosList = new HashSet<Long>();
        videosList.add(Long.valueOf(15166258));
        for (Video video: getItems()) {
            if (videosList.contains(video.getId())) video.isLike = true;
        }
    }

    public void updateWatchLaters(Set<Long> videosIds) {
        final Set<Long> videosList = new HashSet<Long>();
        videosList.add(Long.valueOf(14011251));
        for (Video video: getItems()) {
            if (videosList.contains(video.getId())) video.isWatchLater = true;
        }
    }
    
    public void setPlayButtonListener(PlayButtonClickedListener listener) {
    	this.playButtonListener = listener;
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
