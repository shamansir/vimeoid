package org.vimeoid.activity.user.list;

import net.londatiga.android.QuickAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.activity.base.ApiPagesReceiver;
import org.vimeoid.activity.user.ApiTasksQueue;
import org.vimeoid.activity.user.ItemsListActivity;
import org.vimeoid.activity.user.ApiTaskInQueue.TaskListener;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.adapter.user.UsersDataProvider;
import org.vimeoid.adapter.user.UsersDataReceiver;
import org.vimeoid.adapter.user.UsersListAdapter;
import org.vimeoid.connection.advanced.Methods;
import org.vimeoid.dto.advanced.Album;
import org.vimeoid.dto.advanced.Channel;
import org.vimeoid.dto.advanced.Contact;
import org.vimeoid.dto.advanced.SubscriptionData;
import org.vimeoid.dto.advanced.User;
import org.vimeoid.dto.advanced.SubscriptionData.SubscriptionsReceiver;
import org.vimeoid.dto.advanced.SubscriptionData.SubscriptionType;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class UsersActivity extends ItemsListActivity<User> implements UsersDataProvider {
    
    public static final String TAG = "UsersActivity";
    
    protected static final int SUBSCR_LIKES_TASK = 1;
    protected static final int SUBSCR_UPLOADS_TASK = 2;
    protected static final int SUBSCR_APPEARS_TASK = 3;
	
	private final ApiTasksQueue infoTasksQueue;
	
	private final ApiPagesReceiver<JSONObject> likesSubsReceiver;
	private final ApiPagesReceiver<JSONObject> uploadsSubsReceiver;
	private final ApiPagesReceiver<JSONObject> appearsSubsReceiver;
	
	private int subscrTasksPerformed = 0;
	
	public UsersActivity() {
	    
	    // FIXME: Join two queues instead of trying to synchronize them
	    infoTasksQueue = new ApiTasksQueue() {
            
            @Override public void onError(Exception e, String message) {
            	if (e != null) e.printStackTrace();
                Log.e(TAG, message + " / " + e.getLocalizedMessage());
                Dialogs.makeExceptionToast(UsersActivity.this, message, e);
            }
            
        };
        
        likesSubsReceiver = new SubscriptionsReceiver(SubscriptionData.FieldsKeys.MULTIPLE_KEY) {
            @Override public void onComplete() {                
                ((UsersDataReceiver)getAdapter()).gotSubscriptionData(getListView(),
                                                                      SubscriptionType.LIKES,
                                                                      getSubscriptions().data.get(SubscriptionType.LIKES));
                subscrTasksPerformed++;                
                runInfoTasksIfNotStarted(); // this done to make no conflicts between http-connections
            }
        };
        
        uploadsSubsReceiver = new SubscriptionsReceiver(SubscriptionData.FieldsKeys.MULTIPLE_KEY) {
            @Override public void onComplete() {                
                ((UsersDataReceiver)getAdapter()).gotSubscriptionData(getListView(),
                                                                      SubscriptionType.UPLOADS,
                                                                      getSubscriptions().data.get(SubscriptionType.UPLOADS));
                subscrTasksPerformed++;                
                runInfoTasksIfNotStarted(); // this done to make no conflicts between http-connections
            }
        };
        
        appearsSubsReceiver = new SubscriptionsReceiver(SubscriptionData.FieldsKeys.MULTIPLE_KEY) {
            @Override public void onComplete() {                
                ((UsersDataReceiver)getAdapter()).gotSubscriptionData(getListView(),
                                                                      SubscriptionType.APPEARS,
                                                                      getSubscriptions().data.get(SubscriptionType.APPEARS));
                subscrTasksPerformed++;                
                runInfoTasksIfNotStarted(); // this done to make no conflicts between http-connections
            }
        };
        
	}
    
    @Override
    protected JsonObjectsAdapter<User> createAdapter() {
        // FIXME: separate activity for contacts
        return new UsersListAdapter(Contact.FieldsKeys.MULTIPLE_KEY, this, getLayoutInflater(), this) {
            @Override protected User[] extractItems(JSONObject jsonObject) throws JSONException {
                return Contact.collectListFromJson(jsonObject);
            }
        };
    }
    
    @Override
    protected void onItemSelected(User user) { 
        Invoke.User_.selectUser(this, user);
    }
    
    @Override
    protected void prepare(Bundle extras) {
        secondaryTasks.addListTask(SUBSCR_LIKES_TASK, Methods.people.getSubscriptions, 
                                                      new ApiParams().add("types", SubscriptionType.LIKES.toString()), 
                                                      likesSubsReceiver, -1, 50);
        secondaryTasks.addListTask(SUBSCR_UPLOADS_TASK, Methods.people.getSubscriptions, 
                                                        new ApiParams().add("types", SubscriptionType.UPLOADS.toString()), 
                                                        uploadsSubsReceiver, -1, 50);
        secondaryTasks.addListTask(SUBSCR_APPEARS_TASK, Methods.people.getSubscriptions, 
                                                        new ApiParams().add("types", SubscriptionType.APPEARS.toString()), 
                                                        appearsSubsReceiver, -1, 50);
        // if not contacts activity, request for contacts
    }
    
    @Override
    protected QuickAction createQuickActions(final int position, final User user, View view) {
        // TODO: add ability to watch albums / channels where video located
        
        /* final Resources resources = getResources();
        
        QuickAction qa = new QuickAction(view);
        if (video.uploaderId != currentUserId) {
            qa.addActionItem(getString(R.string.qa_later), resources.getDrawable(video.isWatchLater 
                                                                                 ? R.drawable.watchlater_white 
                                                                                 : R.drawable.watchlater_white_not), 
                new QActionClickListener() {            
                    @Override public void onClick(View v, final QActionItem item) {
                        switchWatchLaterStatus(position, video, item);
                    }
                });
        }
        qa.addActionItem(getString(R.string.qa_info), getResources().getDrawable(R.drawable.info), 
                new QActionClickListener() {            
                    @Override public void onClick(View v, QActionItem item) {
                        Invoke.User_.selectVideo(UsersActivity.this, video);
                    }
                });
        qa.addActionItem(getString(R.string.qa_author), getResources().getDrawable(R.drawable.contact), 
                new QActionClickListener() {            
                    @Override public void onClick(View v, QActionItem item) {
                        Invoke.User_.selectUploader(UsersActivity.this, video);
                    }
                });
        qa.addActionItem(getString(R.string.qa_comments), getResources().getDrawable(R.drawable.comment), 
                new QActionClickListener() {            
                    @Override public void onClick(View v, QActionItem item) {
                        Invoke.User_.selectCommentsOf(UsersActivity.this, video);
                    }
                }); */
        return null;
    }

    @Override
    public void requestData(final int position, final long userId, final UsersDataReceiver receiver) {
        // people.getInfo : location, videos count, contacts count,
        // channels.getAll: channels count
        // albums.getAll: albums count
        // people.getSubscriptions: subscriptions status
        Log.d(TAG, "requestData: creating tasks to collect data for " + position + " / " + userId);
        infoTasksQueue.add(infoTasksQueue.size(), Methods.people.getInfo, 
                           new ApiParams().add("user_id", String.valueOf(userId)),
                           new TaskListener() {
                            
                            @Override
                            public void onPerformed(JSONObject jsonObj) throws JSONException {                            	
                                final JSONObject userObj = jsonObj.getJSONObject(User.FieldsKeys.SINGLE_KEY);
                                receiver.gotPersonalInfo(getListView(), position,
                                                         userObj.getString(User.FieldsKeys.LOCATION), 
                                                         userObj.getLong(User.FieldsKeys.NUM_OF_UPLOADS), 
                                                         userObj.getLong(User.FieldsKeys.NUM_OF_CONTACTS));
                                Log.d(TAG, "Got general info for user " + userId + ", position: " + position);
                                //onContentChanged();
                            }
                            
                        });
        infoTasksQueue.add(infoTasksQueue.size(), Methods.channels.getAll, 
                new ApiParams().add("user_id", String.valueOf(userId)).add("per_page", "1"),
                new TaskListener() {
                 
                 @Override
                 public void onPerformed(JSONObject jsonObj) throws JSONException {
                     receiver.gotChannelsCount(getListView(), position, 
                              jsonObj.getJSONObject(Channel.FieldsKeys.MULTIPLE_KEY).getLong(Channel.FieldsKeys.TOTAL));
                     Log.d(TAG, "Got channels info for user " + userId + ", position: " + position);
                     //onContentChanged();
                 }
                 
             });
        infoTasksQueue.add(infoTasksQueue.size(), Methods.albums.getAll, 
                new ApiParams().add("user_id", String.valueOf(userId)).add("per_page", "1"),
                new TaskListener() {
                 
                 @Override
                 public void onPerformed(JSONObject jsonObj) throws JSONException {
                     receiver.gotAlbumsCount(getListView(), position, 
                              jsonObj.getJSONObject(Album.FieldsKeys.MULTIPLE_KEY).getLong(Album.FieldsKeys.TOTAL));
                     Log.d(TAG, "Got albums info for user " + userId + ", position: " + position);
                     //onContentChanged();
                 }
                 
             });
        runInfoTasksIfNotStarted();
    };
    
    private void runInfoTasksIfNotStarted() {
        if ((3 == subscrTasksPerformed) && !infoTasksQueue.started() && !infoTasksQueue.isEmpty()) {
            Log.d(TAG, "requestData: tasks are ready, queue is not started, will run it");
            new Thread(infoTasksQueue, "Load secondary data").start();
        }        
    }
        
    /* private void switchWatchLaterStatus(final int position, final Video video, final QActionItem item) {
        final Resources resources = getResources();
        final VideosListAdapter adapter = ((VideosListAdapter)getAdapter());
        
        new QuickApiTask(UsersActivity.this, video.isWatchLater 
                ? Methods.albums.removeFromWatchLater 
                : Methods.albums.addToWatchLater) {

            @Override
            public void onOk() {
                video.isWatchLater = !video.isWatchLater;
                final Video changedVideo = adapter.switchWatchLater(getListView(), position);
                Dialogs.makeToast(UsersActivity.this, getString(changedVideo.isWatchLater 
                                                   ? R.string.added_to_watchlater
                                                   : R.string.removed_from_watchlater));
                item.setIcon(resources.getDrawable(changedVideo.isWatchLater 
                                     ? R.drawable.watchlater_white 
                                     : R.drawable.watchlater_white_not));
                item.invalidate();
            }

            @Override
            protected int onError() { return R.string.failed_to_modify_watch_later; }

        }.execute(new ApiParams().add("video_id", String.valueOf(video.id)));
        
    } */
    
}