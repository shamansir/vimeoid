/**
 * 
 */
package org.vimeoid.adapter.unknown;

import org.vimeoid.R;
import org.vimeoid.adapter.ExternalImagesSupportCursorAdapter;
import org.vimeoid.dto.simple.Video;

import android.content.Context;
import android.database.Cursor;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.unknown</dd>
 * </dl>
 *
 * <code>VideosListAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 5, 2010 8:35:01 PM 
 *
 */
public class VideosListAdapter extends ExternalImagesSupportCursorAdapter {

    public VideosListAdapter(Context context, Cursor cursor) {
        super(context, R.layout.video_item, cursor, 
                                new String[] { Video.FieldsKeys.THUMB_SMALL,
                                               Video.FieldsKeys.TITLE, 
                                               Video.FieldsKeys.AUTHOR, 
                                               Video.FieldsKeys.DURATION, 
                                               Video.FieldsKeys.TAGS,
                                               Video.FieldsKeys.NUM_OF_LIKES,
                                               Video.FieldsKeys.NUM_OF_PLAYS,
                                               Video.FieldsKeys.NUM_OF_COMMENTS }, 
                                new int[] { R.id.videoItemImage,
                                            R.id.videoItemTitle, 
                                            R.id.videoItemAuthor,
                                            R.id.videoItemDuration, 
                                            R.id.videoItemTags,
                                            R.id.videoNumOfLikes,
                                            R.id.videoNumOfPlays,
                                            R.id.videoNumOfComments }, 
                                -1);
    }    

}
