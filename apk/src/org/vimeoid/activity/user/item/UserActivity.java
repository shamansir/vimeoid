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
import org.vimeoid.dto.advanced.User;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

import android.os.Bundle;

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
    
    public UserActivity() {
        super(R.layout.view_single_user, Methods.people.getInfo, "person");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Dialogs.makeLongToast(this, "You are " + 
                getIntent().getExtras().getLong(Invoke.Extras.USER_ID) + " : " + 
                getIntent().getExtras().getString(Invoke.Extras.USERNAME));
    }

    @Override
    protected User extractFromJson(JSONObject jsonObj) throws JSONException {
        return User.collectFromJson(jsonObj);
    }

    @Override
    protected ApiParams prepareMethodParams(String methodName, String objectKey, Bundle extras) {
        return new ApiParams().add("user_id", String.valueOf(extras.getLong(Invoke.Extras.USER_ID)));
    }

    @Override
    protected SectionedActionsAdapter fillWithActions(SectionedActionsAdapter actionsAdapter, User user) {
        return actionsAdapter;
    }
    
    @Override
    protected void onItemReceived(User user) {
        super.onItemReceived(user);
    }

}
