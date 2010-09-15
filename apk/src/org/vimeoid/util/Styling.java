package org.vimeoid.util;

import org.vimeoid.R;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;

public class Styling {
    
    public static interface RenderingAdapter {        
        public View render(View inView);
    }
    
    // public static RenderingAdapter TAGS_RENDERER = new RenderingAdapter() {
        
    // TODO: use
    public static class TagsRenderer implements RenderingAdapter {
        
        private final Context context;
        private final String[] tags;
        
        public TagsRenderer(Context context, String[] tags) {
            this.context = context;
            this.tags = tags;
        }
    
        @Override
        public View render(View source) {
            // FIXME: implement
            return null;
        }
        
    };
    
    public static Spannable stylizeTags(Context context, Spannable target, String[] tags) {
        return stylizeTags(context, target, tags, 1);
    }

    public static Spannable stylizeTags(Context context, Spannable target, String[] tags, int delimLen) {
    	if (tags.length == 0) {
    		target.setSpan(new StyleSpan(Typeface.ITALIC), 0, target.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    		target.setSpan(new RelativeSizeSpan((float) 0.8), 0, target.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    		target.setSpan(new ForegroundColorSpan(
    					context.getResources().getColor(R.color.action_no_tags_text_color)), 
    					0, target.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    		return target;
    	}
    	final int tagBgColor = context.getResources().getColor(R.color.action_tag_bg);
    	if (tags.length == 1) {
    		target.setSpan(new BackgroundColorSpan(tagBgColor), 
    				           0, target.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    		return target;
    	}
    	int position = 0;
    	for (String tag: tags) {
    		target.setSpan(new BackgroundColorSpan(tagBgColor), 
			               position, position + tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    		position += tag.length() + delimLen;
    	}    	
    	return target;
    }	
	
}
