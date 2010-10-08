/**
 * 
 */
package org.vimeoid.adapter.user;

import org.vimeoid.R;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.user</dd>
 * </dl>
 *
 * <code>MarkersSupport</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 8, 2010 8:41:09 PM 
 *
 */
public final class MarkersSupport {
    
    public static void injectMarkers(LayoutInflater inflater, ViewGroup markersHolder, int[] markers) {
        markersHolder.removeAllViews();
        final Resources resources = markersHolder.getContext().getResources();
        for (int marker: markers) {
            final ImageView markerView = (ImageView)inflater.inflate(R.layout.marker_for_the_item, null); 
            markerView.setImageDrawable(resources.getDrawable(marker));
            markerView.setMinimumHeight(resources.getDimensionPixelSize(R.dimen.marker_side));
            markerView.setMinimumWidth(resources.getDimensionPixelSize(R.dimen.marker_side));
            markersHolder.addView(markerView);
        }
        /* final int sidePx = resources.getDimensionPixelSize(R.dimen.marker_side);
        final MarginLayoutParams params = new MarginLayoutParams(markersHolder.getLayoutParams());
        params.setMargins(1, 1, 1, 1);
        for (int marker: markers) {            
            final ImageView markerView = new ImageView(context);
            markerView.setImageDrawable(resources.getDrawable(marker));
            markerView.setMaxHeight(resources.getDimensionPixelSize(R.dimen.marker_side));
            markerView.setAdjustViewBounds(true);
            markerView.setMaxWidth(sidePx);
            markerView.setMaxHeight(sidePx);
            markersHolder.addView(markerView, params);
        } */
    }    

}
