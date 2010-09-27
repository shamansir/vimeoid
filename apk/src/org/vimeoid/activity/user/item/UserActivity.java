/**
 * 
 */
package org.vimeoid.activity.user.item;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.user.SingleItemActivity;
import org.vimeoid.adapter.SectionedActionsAdapter;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.dto.advanced.PortraitsData;
import org.vimeoid.dto.advanced.User;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Invoke;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user.item</dd>
 * </dl>
 *
 * <code>UserActivity</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 24, 2010 11:05:50 PM 
 *
 */
public class UserActivity extends SingleItemActivity<User> {
    
    private static final int LOAD_PORTRAITS_TASK = 1;
    private static final int LOAD_ALBUMS_TASK = 2;
    private static final int LOAD_CHANNELS_TASK = 3;
    
    private long userId;
    
    public UserActivity() {
        super(R.layout.view_single_user, Methods.people.getInfo, "person");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        userId = getIntent().getExtras().getLong(Invoke.Extras.USER_ID);
        
        final ApiParams withId = new ApiParams().add("user_id", String.valueOf(userId));
        
        addSecondaryTask(LOAD_PORTRAITS_TASK, Methods.people.getPortraitUrls, withId, "portraits");
        addSecondaryTask(LOAD_ALBUMS_TASK, Methods.albums.getAll, withId, "albums");
        addSecondaryTask(LOAD_CHANNELS_TASK, Methods.channels.getAll, withId, "channels");
        
        super.onCreate(savedInstanceState);
    }

    @Override
    protected User extractFromJson(JSONObject jsonObj) throws JSONException {
        return User.collectFromJson(jsonObj);
    }

    @Override
    protected ApiParams prepareMethodParams(String methodName, String objectKey, Bundle extras) {
        return new ApiParams().add("user_id", String.valueOf(userId));
    }

    @Override
    protected SectionedActionsAdapter fillWithActions(SectionedActionsAdapter actionsAdapter, User user) {
        return actionsAdapter;
    }
    
    @Override
    protected void onItemReceived(User user) {
        ((TextView)findViewById(R.id.userBio)).setText((user.biography.length() > 0) 
                                                       ? Html.fromHtml(user.biography)
                                                       : getString(R.string.no_biography_supplied));
        
        super.onItemReceived(user);
    }
    
    @Override
    public void onSecondaryTaskPerfomed(int taskId, JSONObject result) throws JSONException {
        switch (taskId) {
            case LOAD_PORTRAITS_TASK: {
                final ImageView uploaderPortrait = (ImageView)findViewById(R.id.userPortrait);
                final String mediumPortraitUrl = result.getJSONArray(PortraitsData.FieldsKeys.OBJECT_KEY)
                                                       .getJSONObject(2).getString(PortraitsData.FieldsKeys.URL);
                imageLoader.displayImage(mediumPortraitUrl, uploaderPortrait);
            }; break;
            case LOAD_ALBUMS_TASK: {
                // FIXME: implement
            }; break;
            case LOAD_CHANNELS_TASK: {
                // FIXME: implement
            }; break;            
        }
    }

}
