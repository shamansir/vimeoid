/**
 * 
 */
package org.vimeoid.adapter.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.dto.advanced.Video;

import android.view.View;
import android.view.ViewGroup;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.adapter.user</dd>
 * </dl>
 *
 * <code>VideosListAdapter</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 29, 2010 8:31:27 PM 
 *
 */
public class VideosListAdapter extends JsonObjectsAdapter<Video> {

    @Override
    protected Video extractItem(JSONObject jsonObject) throws JSONException {
        return Video.collectFromJson(jsonObject);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

}
