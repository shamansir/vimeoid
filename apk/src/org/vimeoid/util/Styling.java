package org.vimeoid.util;

import org.vimeoid.R;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

public class Styling {

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
