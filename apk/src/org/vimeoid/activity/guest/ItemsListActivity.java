package org.vimeoid.activity.guest;

import org.vimeoid.R;
import org.vimeoid.adapter.EasyCursorsAdapter;
import org.vimeoid.connection.ApiCallInfo;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Item;
import org.vimeoid.util.Utils;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ItemsListActivity<ItemType extends Item> extends ListActivity {
	
    public static final String TAG = "ItemsListActivity";	

    private View footerView;
    private int pageNum = 1;
    
    private boolean queryRunning = false;
    
    protected View titleBar;
    protected View progressBar;
    
    protected final int mainView;
    protected final String[] projection;
    protected Uri contentUri;
    protected ApiCallInfo callInfo;
    private EasyCursorsAdapter<ItemType> adapter;    
    
    public ItemsListActivity(int mainView, String[] projection) {
    	this.mainView = mainView;
    	this.projection = projection;
    }
    
    public ItemsListActivity(String[] projection) {
    	this(R.layout.generic_list, projection);
    }    
    
    protected abstract EasyCursorsAdapter<ItemType> createAdapter();
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        subjectIcon.setImageResource(Utils.drawableByContent(callInfo.subjectType));
        subjectTitle.setText(getIntent().hasExtra(Invoke.SUBJ_TITLE_EXTRA) ? getIntent().getStringExtra(Invoke.SUBJ_TITLE_EXTRA) : callInfo.subject);
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.ICON_EXTRA, R.drawable.info));
    }    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        Log.d(TAG, "Starting items view");
        
        setContentView(mainView);

        final ListView listView = getListView();
        
        registerForContextMenu(listView);
        listView.setItemsCanFocus(true);                    
        listView.setEmptyView(getLayoutInflater().inflate(R.layout.item_list_empty, null));        

        footerView = getLayoutInflater().inflate(R.layout.item_footer_load_more, null);
        listView.addFooterView(footerView);
        
        listView.setTextFilterEnabled(true);  
        
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);           
                
        this.adapter = createAdapter();
        setListAdapter(adapter);
        
        contentUri = getIntent().getData();
        callInfo = VimeoProvider.collectCallInfo(contentUri);
        
        titleBar = findViewById(R.id.titleBar);
        initTitleBar((ImageView)titleBar.findViewById(R.id.subjectIcon),
                     (TextView)titleBar.findViewById(R.id.subjectTitle),
                     (ImageView)titleBar.findViewById(R.id.resultIcon));
        
        queryMoreItems(contentUri, adapter, projection);
        
    }
    
    protected void onItemSelected(ItemType item) { }
    
	@Override
    protected final void onListItemClick(ListView l, View v, int position, long id) {

    	if (!isLoadMoreItem(position)) {
            Log.d(TAG, "item at position " + position + " (" + getListView().getCount() + ") with id " + id + ", view id " + v.getId() + " is clicked");
            onItemSelected(getItem(position));        
        } else { 
        	loadNextPage();
        }
    	
        super.onListItemClick(l, v, position, id);
        
    }
	
	protected boolean isLoadMoreItem(int position) {
		return (position == (getListView().getCount() - 1));
	}
    
    protected void loadNextPage() {
        if (!queryRunning) {
            if (pageNum <= VimeoApi.MAX_NUMBER_OF_PAGES) {
            
                Log.d(TAG, "Loading next page...");
                
                final Uri nextPageUri = Uri.parse(VimeoProvider.BASE_URI + contentUri.getPath() + "?page=" + (++pageNum));
                
                Log.d(TAG, "Next page Uri: " + nextPageUri);
                
                queryMoreItems(nextPageUri, adapter, projection);
                
            } else Dialogs.makeToast(this, getString(R.string.no_pages_more));
        } else Dialogs.makeToast(this, getString(R.string.please_do_not_touch));    	
    }
    
    protected int extractPosition(MenuItem item) {
        return extractPosition(item.getMenuInfo());
    }
    
    protected int extractPosition(ContextMenuInfo info) {
        
        try {
            return ((AdapterView.AdapterContextMenuInfo) info).position;
        } catch (ClassCastException cce) {
            Log.e(TAG, "incorrect menu info", cce);
            return -1;
        }
        
    }    
    
    @SuppressWarnings("unchecked")
	protected ItemType getItem(int position) {
    	return (ItemType)getListView().getItemAtPosition(position);
    }
	
    protected void queryMoreItems(Uri uri, EasyCursorsAdapter<?> adapter, String[] projection) {
    	
        if (VimeoApi.connectedToWeb(this) && VimeoApi.vimeoSiteReachable(this)) {

            Log.d(TAG, "Connection test is passed OK");
            
            new LoadItemsTask(adapter, projection).execute(uri);
            
        } else {
            
            Log.d(TAG, "Connection test failed");            
           
            Dialogs.makeToast(this, getString(R.string.no_iternet_connection));
            
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
        	
        	if (cursor == null) {
        		Log.e(TAG, "Failed to receive next page");
        		pageNum--;
        	}
            
            if (cursor != null) {
                startManagingCursor(cursor);
    
                adapter.addSource(cursor);
                if (cursor.getCount() < VimeoApi.ITEMS_PER_PAGE) {
                	footerView.setVisibility(View.GONE);
                }
                
                
                onContentChanged();                
                
                cursor.close();
            }
            
            progressBar.setVisibility(View.GONE);
                        
            footerView.setEnabled(true);
            footerText.setTextColor(getResources().getColor(R.color.load_more_default_text));
            footerText.setBackgroundResource(R.color.load_more_default_bg);            
            
            // TODO: scroll to the first received item (smoothScrollToPosition in API 8)

            Log.d(TAG, "List count: " + getListView().getCount() + "; cursor: " + cursor);
            if (cursor != null) {
            	final int newPos = getListView().getCount() - cursor.getCount() - 2; // - 'load more' and one position before
                if (newPos >= 0) setSelection(newPos);
                else setSelection(0);            	
            }
            
            if (pageNum == VimeoApi.MAX_NUMBER_OF_PAGES) {
                footerView.setVisibility(View.GONE);
            }
            
            // TODO: change window title
            /* final ApiCallInfo callInfo = ((StatsCollectingCursor)cursor).getCallStats();
            setTitle(VimeoApi.getSimpleApiCallDescription(callInfo)); */
            
            queryRunning = false;
            
        }
        
    }
    
}
