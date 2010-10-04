package org.vimeoid.activity.guest;

import org.vimeoid.R;
import org.vimeoid.activity.base.ItemsListActivity_;
import org.vimeoid.activity.guest.ListApiTask.Reactor;
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
import android.util.Log;
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
    private final Reactor mainReactor;
    private boolean needMorePages = true;
    
    public ItemsListActivity(int mainView, String[] projection, int contextMenu) {
    	super(mainView, contextMenu);
    	this.projection = projection;
    	
        mainReactor = new ListReactor() {
            @Override public boolean afterRequest(Cursor cursor, int received, boolean needMore, 
                    							  ListApiTask nextPageTask) {
                startManagingCursor(cursor);
                
                needMorePages = needMore;
                mainTask = nextPageTask;                
                
                return super.afterRequest(cursor, received, needMore, nextPageTask);
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
    /* @Override
    protected final void queryMoreItems(EasyCursorsAdapter<ItemType> adapter, int pageNum) {
        final Uri nextPageUri = (pageNum == 1) 
                                ? contentUri 
                                : Uri.parse(VimeoProvider.BASE_URI + contentUri.getPath() + "?page=" + pageNum);
        Log.d(TAG, "Next page Uri: " + nextPageUri);        
        new LoadGuestItemsTask(adapter, projection).execute(nextPageUri);
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
    
    protected class ListReactor implements Reactor {
        
        @Override public void beforeRequest() {
            setToLoadingState();
        }

		@Override public boolean afterRequest(Cursor cursor, int received, boolean needMore, 
        		                              ListApiTask nextPageTask) {
            startManagingCursor(cursor);
            
            hideProgressBar();
            
            onContentChanged();
            
            // TODO: scroll to the first received item (smoothScrollToPosition in API 8)
            final int newPos = getListView().getCount() - received - 2; // - 'load more' and one position before
            if (newPos >= 0) setSelection(newPos);
            else setSelection(0);
            
            needMorePages = needMore;
            mainTask = nextPageTask;
            
            return false; 
        }

        @Override public void onNextPageExists() {
            setToTheresMoreItems();                    
        }

        @Override public void onNoItems() {
            setToNoItemsInList();
        }

        @Override public void onNoMoreItems() {
            setToNoItemsMore();
        }

        @Override public void onError(Exception e, String message) {
            hideProgressBar();
            Log.e(TAG, message);
            
            Dialogs.makeExceptionToast(ItemsListActivity.this, message, e);
        }
        
    }
    
}
