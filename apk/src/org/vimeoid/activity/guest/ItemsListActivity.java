package org.vimeoid.activity.guest;

import org.vimeoid.R;
import org.vimeoid.activity.base.ItemsListActivity_;
import org.vimeoid.activity.base.ListApiTask_;
import org.vimeoid.activity.base.ListApiTask_.Reactor;
import org.vimeoid.activity.guest.ListApiTask;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.connection.ApiCallInfo;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.SimpleItem;
import org.vimeoid.util.Utils;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ItemsListActivity<ItemType extends SimpleItem> extends 
                      ItemsListActivity_<ItemType, EasyCursorsAdapter<ItemType>,
                                         Uri, Cursor> {
	
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
    protected Uri collectTaskParams(Bundle extras) {
        contentUri = getIntent().getData();
        return contentUri;
    }    

    @Override
    protected ListApiTask_<Uri, Cursor> prepareListTask(
            Reactor<Uri, Cursor> reactor, EasyCursorsAdapter<ItemType> adapter) {
        final ListApiTask listTask = new ListApiTask(getContentResolver(), reactor, adapter, projection);
        listTask.setMaxPages(VimeoApi.MAX_NUMBER_OF_PAGES);
        listTask.setPerPage(VimeoApi.ITEMS_PER_PAGE);        
        return listTask;
    }
    
    @Override
    protected void executeTask(ListApiTask_<Uri, Cursor> task, Uri params) {
        task.execute(params);
    }

    @Override
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        callInfo = VimeoProvider.collectCallInfo(contentUri);        
        
        subjectIcon.setImageResource(Utils.drawableByContent(callInfo.subjectType));
        subjectTitle.setText(getIntent().hasExtra(Invoke.Extras.SUBJ_TITLE) ? getIntent().getStringExtra(Invoke.Extras.SUBJ_TITLE) : callInfo.subject);
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.RES_ICON, R.drawable.info));
    }
    
    /* 
    @Override
    protected void whenPageReceived(Cursor page) {
        startManagingCursor(page);
    } */
    
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
        
}
