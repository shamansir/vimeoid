/**
 * 
 */
package org.vimeoid.adapter;

import org.vimeoid.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.user</dd>
 * </dl>
 *
 * <code>TagsSupport</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 8, 2010 8:41:09 PM 
 *
 */
public final class TagsSupport {
    
    public static void injectTags(LayoutInflater inflater, final String[] tags, 
                              final ViewGroup group) {
        group.removeAllViews();
        if (tags.length == 0) {
            group.addView(inflater.inflate(R.layout.no_tags_for_item, null));
            return;
        }
        
        for (final String tag: tags) group.addView(makeTag(inflater, tag));
    }
    
    public static View makeTag(LayoutInflater inflater, String text) {
        final View tagStruct = inflater.inflate(R.layout.tag_for_the_item, null);
        ((TextView)tagStruct.findViewById(R.id.tagItem)).setText(text);
        return tagStruct;
    }
    
    public static View makeTag(LayoutInflater inflater, int text, int background) {
        final View tagStruct = inflater.inflate(R.layout.tag_for_the_item, null);
        ((TextView)tagStruct.findViewById(R.id.tagItem)).setText(text);
        ((TextView)tagStruct.findViewById(R.id.tagItem)).setBackgroundResource(background);
        return tagStruct;
    }
    
    public static View makeTag(LayoutInflater inflater, int text) {
        return makeTag(inflater, text, R.drawable.small_tag_shape);
    }
    
}
