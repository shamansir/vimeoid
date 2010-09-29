package org.vimeoid.activity.user.list;

import org.vimeoid.R;
import org.vimeoid.activity.user.ItemsListActivity;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.adapter.user.VideosListAdapter;
import org.vimeoid.dto.advanced.Video;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Invoke.Extras;

import android.os.Bundle;

/**
 * 
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>Videos</code>
 *
 * <p>Activity that shows a Vimeo Items list (Video, User, Channel, Album ...) to a user
 * that has logged in</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 3, 2010 11:55:11 PM 
 *
 */

// TODO: for sync use SyncAdapter on API Level 7
public class VideosActivity extends ItemsListActivity<Video> {
    
    public VideosActivity() {
        super(R.menu.video_context_user_menu,  
              Video.FieldsKeys.OBJECT_KEY, Video.FieldsKeys.ARRAY_KEY);
    }

    @Override
    protected ApiParams prepareMethodParams(String methodName, Bundle extras) {
        return new ApiParams().add("user_id", extras.getString(Extras.USER_ID))
                              .add("full_response", "1");
    }

    @Override
    protected JsonObjectsAdapter<Video> createAdapter() {
        return new VideosListAdapter(this, getLayoutInflater());
    }

}