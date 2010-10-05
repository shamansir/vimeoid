package org.vimeoid.activity.guest;

import org.vimeoid.R;
import org.vimeoid.activity.base.ApiTask_;
import org.vimeoid.activity.base.ItemsListActivity_;
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
                      ItemsListActivity_<ItemType, EasyCursorsAdapter<ItemType>> {
	
    public static final String TAG = "ItemsListActivity";	

    protected final String[] projection;
    
    protected Uri contentUri;
    protected ApiCallInfo callInfo;
    
    private ListApiTask mainTask;
    private final Reactor<Cursor> mainReactor;
    private boolean needMorePages = true;
    
    public ItemsListActivity(int mainView, String[] projection, int contextMenu) {
    	super(mainView, contextMenu);
    	this.projection = projection;
    	
        mainReactor = new ListReactor<Cursor>() {
            
            @Override public boolean afterRequest(Cursor result, int received,
                                        boolean needMore, ApiTask_<?, Cursor> nextPageTask) {
                
                startManagingCursor(result);
                
                needMorePages = needMore;
                mainTask = (ListApiTask)nextPageTask;                
                
                return super.afterRequest(result, received, needMore, nextPageTask);
            }
            
        };    	
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
    protected void loadNextPage(EasyCursorsAdapter<ItemType> adapter) {
    	if (!needMorePages) return;
        if (mainTask == null) {
            mainTask = new ListApiTask(getContentResolver(), mainReactor, adapter, projection);
            mainTask.setMaxPages(VimeoApi.MAX_NUMBER_OF_PAGES);
            mainTask.setPerPage(VimeoApi.ITEMS_PER_PAGE);
        } 
        
        mainTask.execute(contentUri);
        
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
        
}
