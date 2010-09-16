/**
 * 
 */
package org.vimeoid.activity.guest.item;

import org.vimeoid.R;
import org.vimeoid.activity.guest.ItemActivity;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.dto.simple.UserInfo;
import org.vimeoid.util.Utils;

import android.database.Cursor;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.guest.item</dd>
 * </dl>
 *
 * <code>UserActivity</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 16, 2010 6:38:02 PM 
 *
 */
public class UserActivity extends ItemActivity<UserInfo> {

    public UserActivity() {
        super(R.layout.view_single_user, UserInfo.FULL_EXTRACT_PROJECTION);
    }

    @Override
    protected UserInfo extractFromCursor(Cursor cursor, int position) {
        return UserInfo.fullFromCursor(cursor, position);
    }
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        super.initTitleBar(subjectIcon, subjectTitle, resultIcon);
        if (getIntent().hasExtra(Utils.USERNAME_EXTRA)) {
            subjectTitle.setText(getIntent().getStringExtra(Utils.USERNAME_EXTRA));
        }
    }
    
    @Override
    protected void onItemReceived(final UserInfo user) {
        
        Log.d(TAG, "user " + user.id + " data received, name: " + user.displayName);
        ((TextView)titleBar.findViewById(R.id.subjectTitle)).setText(user.displayName);
        
        // biography
        ((TextView)findViewById(R.id.userBio)).setText(Html.fromHtml(user.biography));
        
        // user portrait
        final ImageView uploaderPortrait = (ImageView)findViewById(R.id.userPortrait);
        imageLoader.displayImage(user.mediumPortraitUrl, uploaderPortrait);        
        
        super.onItemReceived(user);
       
    }    

    @Override
    protected SectionedActionsAdapter fillWithActions(SectionedActionsAdapter actionsAdapter, UserInfo item) {
        return actionsAdapter;
    }

}
