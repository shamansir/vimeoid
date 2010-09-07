package org.vimeoid;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.ProgressBar;

import oauth.signpost.exception.OAuthException;

import org.json.JSONObject;
import org.vimeoid.adapter.EasyCursorAdapter;
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
    
    private EasyCursorAdapter<?> adapter = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        Log.d(TAG, "Testing is Vimeo site accessible");
        
        setContentView(R.layout.view_list_unknown_user);

        final ListView listView = getListView();
        
        registerForContextMenu(listView);
        listView.setItemsCanFocus(true);                    
        listView.setEmptyView(getLayoutInflater().inflate(R.layout.item_list_empty, null));        

        listView.addFooterView(getLayoutInflater().inflate(R.layout.item_footer_load_more, null));
        //listView.setTextFilterEnabled(true);  
        
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);           
        
        // TODO: Load Uri from passed intent, not hardcode
        
        // TODO: check if already attached to vimeo, so just start KnownListView
        
        this.adapter = new VideosListAdapter(this, getLayoutInflater());
        
        queryItems(Uri.withAppendedPath(VimeoSimpleApiProvider.BASE_URI, "/channel/staffpicks/videos"), 
        		   adapter, Video.SHORT_EXTRACT_PROJECTION);
        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

    	if (position != (getListView().getCount() - 1)) {
        
            Log.d(TAG, "item at position " + position + " (" + getListView().getCount() + ") with id " + id + ", view id " + v.getId() + " is clicked");
            
            // Opening item information
            
            // getListView()>getItemAtPosition            
            
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
            
        } else {
            // Load more videos
            
            Log.d(TAG, "Loading next page...");
            
            final Uri nextPageUri = Uri.parse(
                    VimeoSimpleApiProvider.BASE_URI + "/channel/staffpicks/videos" + "?page=2");
            
            queryItems(nextPageUri, adapter, Video.SHORT_EXTRACT_PROJECTION);
            
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
            case R.id.menu_watchLater: itemDescription = "INVISIBLEWatchLater "; break;
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
    
    protected void queryItems(Uri uri, EasyCursorAdapter<?> adapter, String[] projection) {
    	
        if (VimeoApi.connectedToWeb(this) && VimeoApi.vimeoSiteReachable()) {

            Log.d(TAG, "Connection test is passed OK");
            
            new LoadItemsTask(adapter, projection).execute(uri);
            
            // TODO: show title, support API pages
            
            // setWindowTitle
            
        } else {
            
            Log.d(TAG, "Connection test failed");            
           
            Dialogs.makeToast(this, "No connection. Please enable Internet connection and hit Refresh"); // TODO: change to alert
            
        }    	
    	
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        adapter.finalize();
    }
    
    protected class LoadItemsTask extends AsyncTask<Uri, Void, Cursor> {

        // TODO: show progress as a dialog
        
        private final String[] projection;
        private final ProgressBar progressBar;
        private final EasyCursorAdapter<?> adapter;
        
        protected LoadItemsTask(EasyCursorAdapter<?> adapter, String[] projection) {
            if (adapter == null) throw new IllegalArgumentException("Adapter must not be null");
            this.adapter = adapter;
            this.projection = projection;
            this.progressBar = (ProgressBar) findViewById(R.id.progressBar);            
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cursor doInBackground(Uri... uris) {
            if (uris.length <= 0) return null;
            if (uris.length > 1) throw new UnsupportedOperationException("This task do not supports several params");
            return getContentResolver().query(uris[0], projection, null, null, null);
        }
        
        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor == null) return;
            
            progressBar.setVisibility(View.GONE);
            startManagingCursor(cursor);

            adapter.addSource(cursor);
            setListAdapter(adapter); // FIXME: Call once?
            onContentChanged();
            
            cursor.close();
        }
        
    }
    
}