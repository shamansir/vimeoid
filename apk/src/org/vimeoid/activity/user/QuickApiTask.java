package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.util.Dialogs;

import android.app.Activity;
import android.util.Log;

public abstract class QuickApiTask extends ApiTask {
	
	public static final String TAG = "QuickApiTask";
	
	private final Activity parent;

    protected QuickApiTask(Activity parent, String apiMethod) {
		super(apiMethod);
		
		this.parent = parent;
	}

	@Override
    public final void onAnswerReceived(JSONObject jsonObj) throws JSONException {
        onOk();
    }
    
    @Override
    protected void onAnyError(Exception e, final String message) {
    	parent.runOnUiThread(new Runnable() {
            @Override public void run() {
                Log.e(TAG, apiMethod + " : " + message);
                final int errorString = onError();
                Dialogs.makeLongToast(parent, ((errorString != -1) ? R.string.failed_to_add_watch_later + " : " : "") + message);                                    
            }
        });
    }
    
    protected abstract void onOk();
	
	protected int onError() { return -1; };
}
