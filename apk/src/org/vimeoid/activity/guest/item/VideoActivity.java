/**
 * 
 */
package org.vimeoid.activity.guest.item;

import org.vimeoid.R;
import org.vimeoid.activity.guest.SingleItemActivity;
import org.vimeoid.adapter.LActionItem;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.dto.simple.Video;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.PlayerWebView;
import org.vimeoid.util.Utils;

import android.database.Cursor;
import android.os.Bundle;
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
public class VideoActivity extends SingleItemActivity<Video> {
    
    public static final String TAG = "Video";
    
    public VideoActivity() {
        super(R.layout.view_single_video, Video.SINGLE_PROJECTION);
        setLoadManually(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        PlayerWebView.projectPlayer(Long.valueOf(callInfo.subject), this);        
        queryItem();
    }
    
    @Override
    protected void onItemReceived(final Video video) {
        
        // description
        ((TextView)findViewById(R.id.videoDescription)).setText((video.description.length() > 0) 
                                                                 ? Html.fromHtml(video.description)
                                                                 : getString(R.string.no_description_supplied));        
        
        // uploader portrait
        final ImageView uploaderPortrait = (ImageView)findViewById(R.id.uploaderPortrait);
        uploaderPortrait.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) { Invoke.Guest.selectUploader(VideoActivity.this, video); };
        });
        imageLoader.displayImage(video.mediumUploaderPortraitUrl, uploaderPortrait);        
        
        super.onItemReceived(video);
       
    }
    
    @Override
    protected SectionedActionsAdapter fillWithActions(final SectionedActionsAdapter actionsAdapter, final Video video) {
    	
    	// Statistics section
    	int statsSection = actionsAdapter.addSection(getString(R.string.statistics));
    	// tags
    	if (video.tags.length > 0) actionsAdapter.addAction(statsSection, R.drawable.tag, 
    							 getString(R.string.tags_are,
    									   Utils.adaptTags(video.tags, getString(R.string.none_of_tags))));
    	// number of plays
    	actionsAdapter.addAction(statsSection, R.drawable.play, 
    			                 getQString(R.plurals.num_of_plays, (int)video.playsCount));
    	// number of likes
    	actionsAdapter.addAction(statsSection, R.drawable.like, 
    			                 getQString(R.plurals.num_of_likes, (int)video.likesCount));
    	// number of comments
    	actionsAdapter.addAction(statsSection, R.drawable.comment_video, 
    			                 getQString(R.plurals.num_of_comments, (int)video.commentsCount));
    	
    	
    	// Information section
    	int infoSection = actionsAdapter.addSection(getString(R.string.information));
    	// duration
    	actionsAdapter.addAction(infoSection, R.drawable.duration,
			     				 getString(R.string.duration_is, Utils.adaptDuration(video.duration)));    	
    	// uploader
    	final LActionItem userAction = actionsAdapter.addAction(infoSection, R.drawable.contact, 
    			                 getString(R.string.uploaded_by, video.uploaderName));
    	userAction.onClick =  new OnClickListener() {
    		@Override public void onClick(View v) { Invoke.Guest.selectUploader(VideoActivity.this, video); };
		};
		// uploaded on
    	actionsAdapter.addAction(infoSection, R.drawable.upload,
    							 getString(R.string.uploaded_on, video.uploadedOn));
    	// dimensions
    	actionsAdapter.addAction(infoSection, R.drawable.dimensions,
    						     getString(R.string.dimensions_are, video.width, video.height));
    	return actionsAdapter;
    	
    }

    @Override
    protected Video extractFromCursor(Cursor cursor, int position) {        
        return Video.singleFromCursor(cursor, position);
    }    
    
}
