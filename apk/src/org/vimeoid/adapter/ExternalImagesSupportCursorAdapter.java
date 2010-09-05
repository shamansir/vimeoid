/**
 * 
 */
package org.vimeoid.adapter;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter</dd>
 * </dl>
 *
 * <code>ExternalImagesSupportCursorAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 5, 2010 7:46:08 PM 
 *
 */
public abstract class ExternalImagesSupportCursorAdapter extends SimpleCursorAdapter {
    
    private final ImageLoader imagesLoader;
    
    public ExternalImagesSupportCursorAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to,
                                              int defaultDrawable) {
        super(context, layout, cursor, from, to);        
        imagesLoader = new ImageLoader(context, defaultDrawable);
    }
    
    protected void lazyLoadImage(Uri url, ImageView imageView) {
        imagesLoader.displayImage(url.toString(), imageView);
    }
    
    protected void clearCache() {
        imagesLoader.clearCache();
    }

}
