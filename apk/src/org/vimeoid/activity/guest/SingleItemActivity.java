/**
 * 
 */
package org.vimeoid.activity.guest;

import org.vimeoid.R;
import org.vimeoid.activity.base.SingleItemActivity_;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.connection.ApiCallInfo;
import org.vimeoid.connection.simple.VimeoProvider;
import org.vimeoid.util.Invoke;
import org.vimeoid.util.Item;
import org.vimeoid.util.Utils;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.guest</dd>
 * </dl>
 *
 * <code>SingleItemActivity</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 16, 2010 6:41:44 PM 
 *
 */
public abstract class SingleItemActivity<ItemType extends Item> extends SingleItemActivity_<ItemType> {
    
    // private static final String TAG = "SingleItemActivity";
    
    protected final String[] projection;
    
    protected Uri contentUri;
    protected ApiCallInfo callInfo;
    
    public SingleItemActivity(int mainView, String[] projection) {
        super(mainView);
        this.projection = projection;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contentUri = getIntent().getData();      
        
        super.onCreate(savedInstanceState);
    }
    
    protected abstract SectionedActionsAdapter fillWithActions(final SectionedActionsAdapter actionsAdapter, final ItemType item);
    
    protected abstract ItemType extractFromCursor(Cursor cursor, int position);    
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        callInfo = VimeoProvider.collectCallInfo(contentUri);        
        
        subjectIcon.setImageResource(Utils.drawableByContent(callInfo.subjectType));
        subjectTitle.setText(getIntent().hasExtra(Invoke.Extras.SUBJ_TITLE) ? getIntent().getStringExtra(Invoke.Extras.SUBJ_TITLE) : callInfo.subject);
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.ICON, R.drawable.info));
    }
    
    @Override
    protected void queryItem() {
        new LoadItemTask(projection).execute(contentUri);
    }
    
    protected class LoadItemTask extends AsyncTask<Uri, Void, Cursor> {

        private final String[] projection;
        
        protected LoadItemTask(String[] projection) {
            this.projection = projection;
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            showProgressBar();            
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
            
            hideProgressBar();
            
        }

    }

}
