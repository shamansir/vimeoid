package org.vimeoid;

import org.vimeoid.dto.simple.Video;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.SimpleCursorAdapter;

public class ListForUnknownUser extends ListActivity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.unknown_user_list_view);
        
        // this.registerForContextMenu();
        
        /* setListAdapter(new SimpleAdapter(this, callStubVideosList(),
                R.layout.video_item, 
                new String[] { Video.FieldsKeys.TITLE, 
                               Video.FieldsKeys.AUTHOR, 
                               Video.FieldsKeys.DURATION, 
                               Video.FieldsKeys.TAGS },
                new int[] { R.id.videoItemTitle, R.id.videoItemAuthor,
                            R.id.videoItemDuration, R.id.videoItemTags })); */
        
        // TODO: show loading bar, support API pages
        
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
    
    
    /* (non-Javadoc)
     * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        
        /* MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.main_options_menu, menu); 
        
        return true; */
        
        return super.onPrepareOptionsMenu(menu);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        
        /* MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.video_context_menu, menu); 
        
        return true; */
        
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onContextItemSelected(item);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }
}