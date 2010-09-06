package org.vimeoid;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;

import oauth.signpost.exception.OAuthException;

import org.json.JSONObject;
import org.vimeoid.adapter.unknown.VideosListAdapter;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.dto.simple.Video;
import org.vimeoid.util.Dialogs;

/**
 * 
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>ListForUnknownUser</code>
 *
 * <p>Activity that shows a list of Vimeo Items (Videos, Users, Channels, Albums ...) to a user
 * that has not logged in (came using {@link VimeoSimpleApiProvider} or started 
 * application without attaching it to account</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 3, 2010 11:58:57 PM 
 *
 */
public class ListForUnknownUser extends ListActivity {
    
    public static final String TAG = "ListForUnknownUser";
    
    private boolean connected = false;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        Log.d(TAG, "Testing is Vimeo site accessible");
        
        // setEmptyView
        
        if (VimeoApi.connectedToWeb(this) && VimeoApi.vimeoSiteReachable()) {

            Log.d(TAG, "Connection test is passed OK");            
            
            connected = true;
            
            setContentView(R.layout.view_list_unknown_user);
            
            final ListView listView = getListView();            
            registerForContextMenu(listView);
            listView.setItemsCanFocus(true);            
            listView.addFooterView(getLayoutInflater().inflate(R.layout.item_footer_load_more, null));
            //getListView().setTextFilterEnabled(true);            
                    
            // TODO: show loading view, show title, support API pages
            
            // TODO: check if already attached to vimeo, so just start KnownListView
            
            Cursor cursor = getContentResolver().query(
                    Uri.withAppendedPath(
                            VimeoSimpleApiProvider.BASE_URI, "/channel/staffpicks/videos"), 
                    Video.SHORT_EXTRACT_PROJECTION, null, null, null);
            startManagingCursor(cursor);
            setListAdapter(new VideosListAdapter(this, getLayoutInflater(), cursor));
            
            // setWindowTitle
            
        } else {
            
            Log.d(TAG, "Connection test failed");            
            
            connected = false;
            Dialogs.makeToast(this, "No connection. Please enable Internet connection and hit Refresh"); // TODO: change to alert
            
        }
        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // getListView()>getItemAtPosition
        //if (v.getId())
        
        Log.d(TAG, "item at position " + position + " with id " + id + ", view id " + v.getId() + " is clicked");
        
        Uri itemUri = Uri.withAppendedPath(
                VimeoSimpleApiProvider.BASE_URI, "/video/" + id);
        Log.d(TAG, "Video with id " + id + " selected");
        
        String action = getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action) ||
                  Intent.ACTION_GET_CONTENT.equals(action))
        {
            setResult(RESULT_OK, new Intent().setData(itemUri));
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, itemUri));
        }        
        super.onListItemClick(l, v, position, id);
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
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Video " + getSelectedItemId());
        
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.video_context_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String itemDescription;
        switch (item.getItemId()) {
            case R.id.menu_Play: itemDescription = "Play "; break;
            case R.id.menu_watchLater: itemDescription = "WatchLater "; break;
            case R.id.menu_viewInfo: itemDescription = "View info "; break;
            case R.id.menu_viewAuthorInfo: itemDescription = "View author info "; break;
            default: itemDescription = "";
        }
        Dialogs.makeToast(this, itemDescription);
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_Login: { 
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
	            }; break;
            case R.id.menu_Preferences: {
                	Dialogs.makeToast(this, "Preferences"); 
	            } break;
            case R.id.menu_SwitchView: {
            		Dialogs.makeToast(this, "Switch view"); 
            	} break;
            default: Dialogs.makeToast(this, "Unknown menu element");
        }         
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        ((VideosListAdapter)getListAdapter()).destroy();
    }
    
}