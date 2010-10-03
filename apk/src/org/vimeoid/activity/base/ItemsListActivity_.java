package org.vimeoid.activity.base;

import org.vimeoid.R;
import org.vimeoid.connection.VimeoApi;
import org.vimeoid.util.Dialogs;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ItemsListActivity_<ItemType, AdapterType extends BaseAdapter> extends ListActivity {
	
    public static final String TAG = "ItemsListActivity_";
    
    private View emptyView;    
    private View titleBar;
    private View progressBar;
    private View footerView;
    
    private final int mainView;
    private final int contextMenu;
    
    private AdapterType adapter;    
    
    public ItemsListActivity_(int mainView, int contextMenu) {
    	this.mainView = mainView;
    	this.contextMenu = contextMenu;
    }
    
    public ItemsListActivity_(int contextMenu) {
    	this(R.layout.generic_list, contextMenu);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        Log.d(TAG, "Starting items view");
        
        setContentView(mainView);

        final ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setItemsCanFocus(true);                
        registerForContextMenu(listView);
        
        emptyView = findViewById(R.id.emptyView);
        listView.setEmptyView(emptyView);

        footerView = getLayoutInflater().inflate(R.layout.item_footer_load_more, null);
        listView.addFooterView(footerView);        
        
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);           
                
        this.adapter = createAdapter();
        setListAdapter(adapter);
        
        titleBar = findViewById(R.id.titleBar);
        initTitleBar((ImageView)titleBar.findViewById(R.id.subjectIcon),
                     (TextView)titleBar.findViewById(R.id.subjectTitle),
                     (ImageView)titleBar.findViewById(R.id.resultIcon));
        
        checkConnectionAndQueryMoreItems(adapter, pageNum);
        
    }
    
    protected abstract AdapterType createAdapter();
    
    protected abstract void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon);
    
    protected abstract void queryMoreItems(AdapterType adapter, int pageNum);
    
    protected void onItemSelected(ItemType item) { }
    
    protected String getContextMenuTitle(int position) { 
        return getString(R.string.context_menu); 
    };
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        
        final int position = extractPosition(menuInfo);
        
        Log.d(TAG, "Opening context menu for item at " + position);
        
        if (isLoadMoreItem(position)) return; 
        
        menu.setHeaderTitle(getContextMenuTitle(position));
            
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(contextMenu, menu);
    }
    
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
    
    private void checkConnectionAndQueryMoreItems(AdapterType adapter, int pageNum) {
        
        if (VimeoApi.connectedToWeb(this) && VimeoApi.vimeoSiteReachable(this)) {
            Log.d(TAG, "Connection test is passed OK");
            queryMoreItems(adapter, pageNum);
        } else {
            Log.d(TAG, "Connection test failed");            
            Dialogs.makeToast(this, getString(R.string.no_iternet_connection));
        }
        
    }
    
    private void loadNextPage() {
        if (!queryRunning) {
            if (pageNum < maxPages) {
                Log.d(TAG, "Loading next page...");
                checkConnectionAndQueryMoreItems(adapter, ++pageNum);
            } else Dialogs.makeToast(this, getString(R.string.no_pages_more));
        } else Dialogs.makeToast(this, getString(R.string.please_do_not_touch));        
    }
    
    protected final int getCurrentPage() {
        return pageNum;
    }
    
    protected final void setMaxPagesCount(int count) {
        this.maxPages = count;
    }
    
    protected final void setItemsPerPage(int perPage) {
        this.perPage = perPage;
    }
    
    protected final int getItemsPerPage() {
        return perPage;
    }
    
    protected final boolean isLoadMoreItem(int position) {
        return (position == (getListView().getCount() - 1));
    }
    
    protected final int extractPosition(MenuItem item) {
        return extractPosition(item.getMenuInfo());
    }
    
    protected final int extractPosition(ContextMenuInfo info) {
        try {
            return ((AdapterView.AdapterContextMenuInfo) info).position;
        } catch (ClassCastException cce) {
            Log.e(TAG, "incorrect menu info", cce);
            return -1;
        }
        
    }
    
    @SuppressWarnings("unchecked")
    protected final ItemType getItem(int position) {
        return (ItemType)getListView().getItemAtPosition(position);
    }    
    
    protected abstract class LoadItemsTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

        private final View progressBar;
        private final TextView footerText;
        private final TextView emptyText;
        private final ImageView emptyImage;
        
        protected LoadItemsTask() {
            this.progressBar = findViewById(R.id.progressBar);   
            this.footerText = (TextView) footerView.findViewById(R.id.itemsListFooterText);
            this.emptyText = (TextView) emptyView.findViewById(R.id.itemsEmptyListView);
            this.emptyImage = (ImageView) emptyView.findViewById(R.id.itemsEmptyListImage);
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            queryRunning = true;
            
            progressBar.setVisibility(View.VISIBLE);
            
            footerView.setEnabled(false);
            footerText.setTextColor(getResources().getColor(R.color.load_more_disabled_text));
            footerText.setBackgroundResource(R.color.load_more_disabled_bg);
            footerText.setText(R.string.loading);            
            
            emptyImage.setImageResource(R.drawable.item_loading_small);            
            emptyText.setText(R.string.loading);
            
        }
        
        protected void onItemsReceived(int howMuch, int total) {
            if ((howMuch == 0) && (pageNum == 1)) {
                // no items in list at all
                getListView().removeFooterView(footerView);
                emptyImage.setImageResource(R.drawable.no_more_small);
                emptyText.setText(R.string.no_items_in_list);            
            } else if ((howMuch < perPage) ||     
                       (pageNum == maxPages) ||
                       ((total != -1) && (((perPage * (pageNum - 1)) + howMuch) == total))) {
                // no items more
                footerView.setVisibility(View.GONE);
            } else {
                // enable 'load more' button
                footerView.setEnabled(true);
                footerText.setTextColor(getResources().getColor(R.color.load_more_default_text));
                footerText.setBackgroundResource(R.color.load_more_default_bg); 
                footerText.setText(R.string.load_more);
            }
            
            Log.d(TAG, "Received " + howMuch + " items");
            
            onContentChanged();
            
            // TODO: scroll to the first received item (smoothScrollToPosition in API 8)
            final int newPos = getListView().getCount() - howMuch - 2; // - 'load more' and one position before
            if (newPos >= 0) setSelection(newPos);
            else setSelection(0);    
            
        }
        
        protected final void rollback() {
            pageNum--;
            
            progressBar.setVisibility(View.GONE);
        }
        
        @Override
        protected void onPostExecute(Result result) {
            progressBar.setVisibility(View.GONE);
            queryRunning = false;
        }
        
    }
    
}
