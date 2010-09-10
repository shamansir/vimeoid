package org.vimeoid.activity.guest.list;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.vimeoid.R;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.adapter.guest.VideosListAdapter;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.dto.simple.Video;
import org.vimeoid.util.Dialogs;
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
public class Videos extends ListActivity {
    
    public static final String TAG = "Videos";
    
    public static final int MAX_NUMBER_OF_PAGES = 3; 
    
    private EasyCursorsAdapter<?> adapter = null;
    private View footerView;
    private int pageNum = 1;
    
    private boolean queryRunning = false;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        Log.d(TAG, "Starting videos view");
        
        setContentView(R.layout.view_list_unknown_user);

        final ListView listView = getListView();
        
        registerForContextMenu(listView);
        listView.setItemsCanFocus(true);                    
        listView.setEmptyView(getLayoutInflater().inflate(R.layout.item_list_empty, null));        

        footerView = getLayoutInflater().inflate(R.layout.item_footer_load_more, null);
        listView.addFooterView(footerView);
        
        listView.setTextFilterEnabled(true);  
        
        final View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);           
                
        this.adapter = new VideosListAdapter(this, getLayoutInflater());
        setListAdapter(adapter);
        
        final Uri contentUri = getIntent().getData();
        setTitle(VimeoProvider.getCallDescription(VimeoProvider.collectCallInfo(contentUri)));
        
        // TODO: Use TextView to set title, disable titlebar in application
        
        queryMoreItems(contentUri, adapter, Video.SHORT_EXTRACT_PROJECTION);
        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

    	if (position != (getListView().getCount() - 1)) {
        
            Log.d(TAG, "item at position " + position + " (" + getListView().getCount() + ") with id " + id + ", view id " + v.getId() + " is clicked");
            
            // Opening item information
            
            // getListView().getItemAtPosition()            
            
            Uri itemUri = Uri.withAppendedPath(
                    VimeoProvider.BASE_URI, "/video/" + id);
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
            
            if (!queryRunning) {
                if (pageNum <= MAX_NUMBER_OF_PAGES) { 
                
                    Log.d(TAG, "Loading next page...");
                    
                    final Uri nextPageUri = Uri.parse(
                            VimeoProvider.BASE_URI + "/channel/staffpicks/videos" + "?page=" + (++pageNum));
                    
                    queryMoreItems(nextPageUri, adapter, Video.SHORT_EXTRACT_PROJECTION);
                    
                } else Dialogs.makeToast(this, "Maximum number of pages is reached");
            } else Dialogs.makeToast(this, "Don't mess with me!");
            
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        
        // if (v.getId() != R.id.list) return;
        
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException cce) {
            Log.e(TAG, "incorrect menu info", cce);
            return;
        }
        
        final int position = info.position;
        
        Log.d(TAG, "Opening context menu for item at " + position);
        
        if (position == (getListView().getCount() - 1)) return; 
        
        Video video = (Video)getListView().getItemAtPosition(position);
        menu.setHeaderTitle("Video " + Utils.crop(video.title, 20));
            
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.video_context_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException cce) {
            Log.e(TAG, "incorrect menu info", cce);
            return false;
        }
        
        final int position = info.position;
        
        Log.d(TAG, "Selected context menu item for item at " + position);
        
        Video video = (Video)getListView().getItemAtPosition(position);         
        
        String itemDescription;
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
            case R.id.menu_watchLater: itemDescription = "WatchLater "; break;        
            case R.id.menu_viewInfo: itemDescription = "View info "; break;
            case R.id.menu_viewAuthorInfo: itemDescription = "View author info"; break;
            default: itemDescription = "";
        }
        Dialogs.makeToast(this, itemDescription);
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
                    Dialogs.makeToast(this, "Refresh"); 
                } break;
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
    
    protected void queryMoreItems(Uri uri, EasyCursorsAdapter<?> adapter, String[] projection) {
    	
        if (VimeoApi.connectedToWeb(this) && VimeoApi.vimeoSiteReachable(this)) {

            Log.d(TAG, "Connection test is passed OK");
            
            new LoadItemsTask(adapter, projection).execute(uri);
            
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
        private final View progressBar;
        private final TextView footerText;
        private final EasyCursorsAdapter<?> adapter;
        
        protected LoadItemsTask(EasyCursorsAdapter<?> adapter, String[] projection) {
            if (adapter == null) throw new IllegalArgumentException("Adapter must not be null");
            this.adapter = adapter;
            this.projection = projection;
            this.progressBar = findViewById(R.id.progressBar);   
            this.footerText = (TextView) footerView.findViewById(R.id.itemsListFooterText);
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            queryRunning = true;
            
            progressBar.setVisibility(View.VISIBLE);
            
            footerView.setEnabled(false);            
            footerText.setTextColor(getResources().getColor(R.color.load_more_disabled_text));
            footerText.setBackgroundResource(R.color.load_more_disabled_bg);
            
        }

        @Override
        protected Cursor doInBackground(Uri... uris) {
            if (uris.length <= 0) return null;
            if (uris.length > 1) throw new UnsupportedOperationException("This task do not supports several params");
            return getContentResolver().query(uris[0], projection, null, null, null);
        }
        
        @Override
        protected void onPostExecute(Cursor cursor) {
            
            if (cursor != null) {
                startManagingCursor(cursor);
    
                adapter.addSource(cursor);
                onContentChanged();
                
                cursor.close();
            }
            
            progressBar.setVisibility(View.GONE);
                        
            footerView.setEnabled(true);
            footerText.setTextColor(getResources().getColor(R.color.load_more_default_text));
            footerText.setBackgroundResource(R.color.load_more_default_bg);            
            
            // TODO: scroll to the first received item (smoothScrollToPosition in API 8)

            final int newPos = getListView().getCount() - cursor.getCount() - 2; // - 'load more' and one position before            
            if (newPos >= 0) setSelection(newPos);
            else setSelection(0);
            
            if (pageNum == MAX_NUMBER_OF_PAGES) {
                footerView.setVisibility(View.GONE);
            }
            
            // TODO: change window title
            /* final ApiCallInfo callInfo = ((StatsCollectingCursor)cursor).getCallStats();
            setTitle(VimeoApi.getSimpleApiCallDescription(callInfo)); */
            
            queryRunning = false;
            
        }
        
    }
    
}