package org.vimeoid.activity.guest.list;

import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

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
    	super(Video.SHORT_EXTRACT_PROJECTION);
    }
    
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.main_options_menu, menu); 
        
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        
        final int position = extractPosition(menuInfo);
        
        Log.d(TAG, "Opening context menu for item at " + position);
        
        if (isLoadMoreItem(position)) return; 
        
        menu.setHeaderTitle(Utils.format(getString(R.string.video_is), "title", Utils.crop(getItem(position).title, 20)));
            
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.video_context_guest_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        
        Video video = getItem(extractPosition(item));         
        
        switch (item.getItemId()) {
            /* case R.id.menu_Play: {
                    try {
                        final Uri playUri = VimeoApi.getPlayUri(video);
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(playUri));
                        return true;                            
                    } catch (VideoLinkRequestException vlre) {  
                        Dialogs.makeExceptionToast(this, "Getting Video URL exception", vlre);
                        vlre.printStackTrace();
                        return false;
                    }
            } */
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
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            /* case R.id.menu_Login: { 
	                Log.d(TAG, "Starting OAuth login");
	                if (!VimeoApi.ensureOAuth(this)) {
	                    try {
	                        Log.d(TAG, "Requesting OAuth Uri");
	                        Uri authUri = VimeoApi.requestForOAuthUri();
	                        Log.d(TAG, "Got OAuth Uri, staring Browser activity");
	                        Dialogs.makeToast(this, "Please wait while browser opens");
	                        startActivity(new Intent(Intent.ACTION_VIEW, authUri));
	                    } catch (OAuthException oae) {
	                        Log.e(TAG, oae.getLocalizedMessage());
	                        oae.printStackTrace();
	                        Dialogs.makeExceptionToast(this, "OAuth Exception", oae);  
	                    }	                    
	                } else {
	                    Log.d(TAG, "OAuth is ready, loading user name");
	                    try {
                            JSONObject user = VimeoApi.advancedApi(Methods.test.login, "user");
                            Log.d(TAG, "user object: " + user);
                            Log.d(TAG, "got user " + user.getString("id") + " / " + user.get("username"));
                        } catch (Exception e) {
                            Log.e(TAG, e.getLocalizedMessage());
                            e.printStackTrace();
                            Dialogs.makeExceptionToast(this, "Getting user exception", e); 
                        }
	                }
	            }; break; */
            case R.id.menu_Refresh: {
                    Dialogs.makeToast(this, getString(R.string.currently_not_supported)); 
                } break;
            case R.id.menu_Preferences: {
                	Dialogs.makeToast(this, getString(R.string.currently_not_supported)); 
	            } break;
            case R.id.menu_SwitchView: {
            		Dialogs.makeToast(this, getString(R.string.currently_not_supported)); 
            	} break;
            default: Dialogs.makeToast(this, getString(R.string.unknown_item));
        }         
        return super.onOptionsItemSelected(item);
        
    }
    
	@Override
	protected EasyCursorsAdapter<Video> createAdapter() {
		return new VideosListAdapter(this, getLayoutInflater());
	}
    
}