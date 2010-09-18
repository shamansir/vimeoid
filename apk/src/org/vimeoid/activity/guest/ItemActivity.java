/**
 * 
 */
package org.vimeoid.activity.guest;

import org.vimeoid.R;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.connection.ApiCallInfo;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Item;
import org.vimeoid.util.Utils;

import com.fedorvlasov.lazylist.ImageLoader;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.guest</dd>
 * </dl>
 *
 * <code>ItemActivity</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 16, 2010 6:41:44 PM 
 *
 */
public abstract class ItemActivity<ItemType extends Item> extends Activity {
    
    // private static final String TAG = "ItemActivity";
    
    protected final int mainView;
    protected final String[] projection;
    
    private boolean loadManually = false;
    
    protected View titleBar;
    protected View progressBar;    
    
    protected ImageLoader imageLoader;
    
    protected Uri contentUri;
    protected ApiCallInfo callInfo;
    
    public ItemActivity(int mainView, String[] projection) {
        this.mainView = mainView;
        this.projection = projection;
    }
    
    protected abstract SectionedActionsAdapter fillWithActions(final SectionedActionsAdapter actionsAdapter, final ItemType item);
    
    protected abstract ItemType extractFromCursor(Cursor cursor, int position);    
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        subjectIcon.setImageResource(Utils.drawableByContent(callInfo.subjectType));
        subjectTitle.setText(getIntent().hasExtra(Invoke.SUBJ_TITLE_EXTRA) ? getIntent().getStringExtra(Invoke.SUBJ_TITLE_EXTRA) : callInfo.subject);
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.ICON_EXTRA, R.drawable.info));
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(mainView);
        
        contentUri = getIntent().getData();
        callInfo = VimeoProvider.collectCallInfo(contentUri);        
        
        titleBar = findViewById(R.id.titleBar);
        initTitleBar((ImageView)titleBar.findViewById(R.id.subjectIcon),
                     (TextView)titleBar.findViewById(R.id.subjectTitle),
                     (ImageView)titleBar.findViewById(R.id.resultIcon));
        
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        
        imageLoader = new ImageLoader(this, R.drawable.item_loading_small, R.drawable.item_failed_small);
    
        if (!loadManually) runLoadingProcess();
    }
    
    protected void setLoadManually(boolean value) {
        loadManually = value;
    }
    
    protected void runLoadingProcess() {
        new LoadItemTask(projection).execute(contentUri);
    }
    
    protected void onItemReceived(final ItemType item) {
        final ListView actionsList = (ListView)findViewById(R.id.actionsList);
        actionsList.setAdapter(fillWithActions(new SectionedActionsAdapter(this, getLayoutInflater(), imageLoader), item));
        actionsList.invalidate();        
    }
    
    protected class LoadItemTask extends AsyncTask<Uri, Void, Cursor> {

        private final String[] projection;
        private final View progressBar;
        
        protected LoadItemTask(String[] projection) {
            this.projection = projection;
            this.progressBar = findViewById(R.id.progressBar);   
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
            
            if (cursor != null) {
                
                if (cursor.getCount() > 1) throw new IllegalStateException("There must be the only one item returned");
                
                cursor.moveToFirst();
                onItemReceived(extractFromCursor(cursor, 0));
                //onVideoDataReceived(Video.fullFromCursor(cursor, 0));
                cursor.close();
            }
            
            progressBar.setVisibility(View.GONE);            
        }

    }

}
