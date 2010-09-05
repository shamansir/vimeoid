/**
 * 
 */
package org.vimeoid.adapter;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter</dd>
 * </dl>
 *
 * <code>ExternalImagesSupportAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 5, 2010 7:46:08 PM 
 *
 */
public abstract class ExternalImagesSupportAdapter extends BaseAdapter {
    
    private final ImageLoader imagesLoader;
    
    public ExternalImagesSupportAdapter(Context context, int defaultDrawable) {
        imagesLoader = new ImageLoader(context, defaultDrawable);
    }
    
    protected void lazyLoadImage(Uri url, ImageView imageView) {
        imagesLoader.displayImage(url.toString(), imageView);
    }
    
    protected void clearCache() {
        imagesLoader.clearCache();
    }

}
