/**
 * 
 */
package org.vimeoid.adapter.user;

import org.vimeoid.R;

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
    
    public static void injectMarkers(ViewGroup markersHolder, int[] markers) {
        if (markers.length > 4) throw new IllegalStateException("More than four markers is not currently supported");        
        ((ImageView)markersHolder.findViewById(R.id.fourthMarker)).setImageResource((markers.length > 3) ? markers[3] : 0);
        ((ImageView)markersHolder.findViewById(R.id.thirdMarker)) .setImageResource((markers.length > 2) ? markers[2] : 0);
        ((ImageView)markersHolder.findViewById(R.id.secondMarker)).setImageResource((markers.length > 1) ? markers[1] : 0);
        ((ImageView)markersHolder.findViewById(R.id.firstMarker)) .setImageResource((markers.length > 0) ? markers[0] : 0);
    }    

}
