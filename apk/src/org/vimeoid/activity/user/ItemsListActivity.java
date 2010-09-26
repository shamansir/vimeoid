package org.vimeoid.activity.user;

import org.vimeoid.R;
import org.vimeoid.activity.base.ItemsListActivity_;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.connection.ApiCallInfo;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.util.AdvancedItem;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Utils;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ItemsListActivity<ItemType extends AdvancedItem> extends 
                      ItemsListActivity_<ItemType, EasyCursorsAdapter<ItemType>> {
	
    public static final String TAG = "ItemsListActivity";	

    protected final String[] projection;
    
    protected Uri contentUri;
    protected ApiCallInfo callInfo;
    
    public ItemsListActivity(int mainView, String[] projection, int contextMenu) {
    	super(mainView, contextMenu);
    	this.projection = projection;
    }
    
    public ItemsListActivity(String[] projection, int contextMenu) {
    	this(R.layout.generic_list, projection, contextMenu);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        contentUri = getIntent().getData();
        
        super.onCreate(savedInstanceState);
    }
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        callInfo = VimeoProvider.collectCallInfo(contentUri);        
        
        subjectIcon.setImageResource(Utils.drawableByContent(callInfo.subjectType));
        subjectTitle.setText(getIntent().hasExtra(Invoke.Extras.SUBJ_TITLE) ? getIntent().getStringExtra(Invoke.Extras.SUBJ_TITLE) : callInfo.subject);
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.RES_ICON, R.drawable.info));
    }
    
    @Override
    protected final void queryMoreItems(EasyCursorsAdapter<ItemType> adapter, int pageNum) {
        final Uri nextPageUri = (pageNum == 1) 
                                ? contentUri 
                                : Uri.parse(VimeoProvider.BASE_URI + contentUri.getPath() + "?page=" + pageNum);
        Log.d(TAG, "Next page Uri: " + nextPageUri);        
        new LoadGuestItemsTask(adapter, projection).execute(nextPageUri);
    }
    
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.guest_options_menu, menu); 
        
        return true;
    }
   
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
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
    
    protected final class LoadGuestItemsTask extends LoadItemsTask<Uri, Void, Cursor> {

        // TODO: show progress as a dialog
        private final String[] projection;
        private final EasyCursorsAdapter<?> adapter;
        
        protected LoadGuestItemsTask(EasyCursorsAdapter<?> adapter, String[] projection) {
            super();
            if (adapter == null) throw new IllegalArgumentException("Adapter must not be null");
            this.adapter = adapter;
            this.projection = projection;
        }
        
        @Override
        protected Cursor doInBackground(Uri... uris) {
            if (uris.length <= 0) return null;
            if (uris.length > 1) throw new UnsupportedOperationException("This task do not supports several params");
            return getContentResolver().query(uris[0], projection, null, null, null);
        }
        
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            
        	if (cursor == null) {
        	    Log.e(TAG, "Failed to receive next page");
        		rollback();
        	} else {
                startManagingCursor(cursor);
                adapter.addSource(cursor);
                onItemsReceived(cursor.getCount());
                cursor.close();
            }
        }
        
    }
    
}
