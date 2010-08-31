package org.vimeoid;

import org.vimeoid.dto.simple.Video;
import org.vimeoid.util.ApplicationContext;

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
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ListForUnknownUser extends ListActivity {
    
    public static final String TAG = "ListForUnknownUser";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.unknown_user_list_view);
        
        this.registerForContextMenu(this.getListView());
        
        /* setListAdapter(new SimpleAdapter(this, callStubVideosList(),
                R.layout.video_item, 
                new String[] { Video.FieldsKeys.TITLE, 
                               Video.FieldsKeys.AUTHOR, 
                               Video.FieldsKeys.DURATION, 
                               Video.FieldsKeys.TAGS },
                new int[] { R.id.videoItemTitle, R.id.videoItemAuthor,
                            R.id.videoItemDuration, R.id.videoItemTags })); */
        
        // TODO: show loading view, support API pages
        
        Cursor cursor = getContentResolver().query(
                Uri.withAppendedPath(
                        VimeoSimpleApiProvider.BASE_URI, "/user/shamansir/videos"), 
                Video.SHORT_EXTRACT_PROJECTION, null, null, null);
        startManagingCursor(cursor);
        this.setListAdapter(new SimpleCursorAdapter(this,
                                        R.layout.video_item, 
                                        cursor,
                                        new String[] { Video.FieldsKeys.TITLE, 
                                                       Video.FieldsKeys.AUTHOR, 
                                                       Video.FieldsKeys.DURATION, 
                                                       Video.FieldsKeys.TAGS },
                                        new int[] { R.id.videoItemTitle, 
                                                    R.id.videoItemAuthor,
                                                    R.id.videoItemDuration, 
                                                    R.id.videoItemTags }));                
        
        //getListView().setTextFilterEnabled(true);
        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
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
        Toast t = Toast.makeText(getApplicationContext(), itemDescription, 
                ApplicationContext.TOAST_KEEPS_HOT);
        t.show();          
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String itemDescription;
        switch (item.getItemId()) {
            case R.id.menu_Login: itemDescription = "Login "; break;
            case R.id.menu_Preferences: itemDescription = "Preferences "; break;
            case R.id.menu_SwitchView: itemDescription = "Switch view "; break;
            default: itemDescription = "";
        }
        Toast t = Toast.makeText(getApplicationContext(), itemDescription, 
                ApplicationContext.TOAST_KEEPS_HOT);
        t.show();          
        return super.onOptionsItemSelected(item);
    }
}