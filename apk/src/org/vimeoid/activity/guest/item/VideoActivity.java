/**
 * 
 */
package org.vimeoid.activity.guest.item;

import org.vimeoid.R;
import org.vimeoid.activity.guest.ItemActivity;
import org.vimeoid.adapter.ActionItem;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.dto.simple.Video;
import org.vimeoid.util.Utils;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
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
public class VideoActivity extends ItemActivity<Video> {
    
    public static final String TAG = "Video";
    
    public VideoActivity() {
        super(R.layout.view_single_video, Video.FULL_EXTRACT_PROJECTION);
        setLoadManually(true);
    }
    
    @Override
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        super.initTitleBar(subjectIcon, subjectTitle, resultIcon);
        if (getIntent().hasExtra(Utils.VIDEO_TITLE_EXTRA)) {
            subjectTitle.setText(getIntent().getStringExtra(Utils.VIDEO_TITLE_EXTRA));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WebView playerView = (WebView)findViewById(R.id.videoPlayer);
        playerView.getSettings().setJavaScriptEnabled(true);
        //playerView.getSettings().setLoadsImagesAutomatically(true);
        
        final long videoId = Long.valueOf(callInfo.subject);
        final int playerHeight = getResources().getDimensionPixelSize((R.dimen.video_player_height));
        
        playerView.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(((newProgress == 0) || (newProgress == 100)) ? View.GONE : View.VISIBLE);
                if (newProgress == 100) ((ListView)findViewById(R.id.actionsList)).setSelectionAfterHeaderView();
                super.onProgressChanged(view, newProgress);
            }
        });
        
        Log.d(TAG, "Loading player: " + VimeoApi.getPlayerUrl(videoId, playerHeight));
        playerView.loadUrl(VimeoApi.getPlayerUrl(videoId, playerHeight));
        
        runLoadingProcess();
        
    }
    
    @Override
    protected void onItemReceived(final Video video) {
        
        Log.d(TAG, "video " + video.id + " data received, uploader: " + video.uploaderName);
        ((TextView)titleBar.findViewById(R.id.subjectTitle)).setText(video.title);
        
        // description
        ((TextView)findViewById(R.id.videoDescription)).setText(Html.fromHtml(video.description));
        
        // uploader portrait
        final ImageView uploaderPortrait = (ImageView)findViewById(R.id.uploaderPortrait);
        uploaderPortrait.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) { invokePickAuthor(video); };
        });
        imageLoader.displayImage(video.mediumUploaderPortraitUrl, uploaderPortrait);        
        
        super.onItemReceived(video);
       
    }
    
    protected static Uri getAuthorPageUri(Video video) {
        final String authorId = Utils.authorIdFromProfileUrl(video.uploaderProfileUrl);
        Log.d(TAG, "Extracted authorId " + authorId + " from profile URL " + video.uploaderProfileUrl);
        
        return Uri.withAppendedPath(VimeoProvider.BASE_URI, "/user/" + authorId + "/info");
    }
    
    protected void invokePickAuthor(Video video) {
        startActivity(new Intent(Intent.ACTION_VIEW, getAuthorPageUri(video))
                                .putExtra(Utils.USERNAME_EXTRA, video.uploaderName));
    }
    
    @Override
    protected SectionedActionsAdapter fillWithActions(final SectionedActionsAdapter actionsAdapter, final Video video) {
    	
    	// Statistics section
    	int statsSection = actionsAdapter.addSection(getString(R.string.statistics));
    	// tags
    	if (video.tags.length > 0) actionsAdapter.addAction(statsSection, R.drawable.tag, 
    							 Utils.format(getString(R.string.tags_are), "list",
    										 Utils.adaptTags(video.tags, getString(R.string.none_of_tags))));
    	// number of plays
    	actionsAdapter.addAction(statsSection, R.drawable.play, 
    			                 Utils.format(getString(R.string.num_of_plays), "num", String.valueOf(video.playsCount)));
    	// number of likes
    	actionsAdapter.addAction(statsSection, R.drawable.like, 
    			                 Utils.format(getString(R.string.num_of_likes), "num", String.valueOf(video.likesCount)));
    	// number of comments
    	actionsAdapter.addAction(statsSection, R.drawable.comment_video, 
    			                 Utils.format(getString(R.string.num_of_comments), "num", String.valueOf(video.commentsCount)));
    	
    	
    	// Information section
    	int infoSection = actionsAdapter.addSection(getString(R.string.information));
    	// duration
    	actionsAdapter.addAction(infoSection, R.drawable.duration,
			     				 Utils.format(getString(R.string.duration_is), "time", Utils.adaptDuration(video.duration)));    	
    	// uploader
    	final ActionItem userAction = actionsAdapter.addAction(infoSection, R.drawable.contact, 
    			                 Utils.format(getString(R.string.uploaded_by), "name", video.uploaderName));
    	userAction.onClick =  new OnClickListener() {
    		@Override public void onClick(View v) { invokePickAuthor(video); };
		};
		// uploaded on
    	actionsAdapter.addAction(infoSection, R.drawable.upload,
    							 Utils.format(getString(R.string.uploaded_on), "time", video.uploadedOn));
    	// dimensions
    	actionsAdapter.addAction(infoSection, R.drawable.dimensions,
    						     Utils.format(getString(R.string.dimensions_are), "width", String.valueOf(video.width),
    						    		                                          "height", String.valueOf(video.height)));
    	
    	return actionsAdapter;
    	
    }

    @Override
    protected Video extractFromCursor(Cursor cursor, int position) {        
        return Video.fullFromCursor(cursor, position);
    }    
    
}
