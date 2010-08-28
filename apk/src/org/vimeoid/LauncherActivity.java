package org.vimeoid;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.connection.JsonOverHttp;
import org.vimeoid.connection.VimeoApiUtils;
import org.vimeoid.dto.simple.TagInfo;
import org.vimeoid.dto.simple.Video;

import android.app.ListActivity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;

public class LauncherActivity extends ListActivity {
    
    private static final String TAG = "LauncherActivity";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.popular_view);
        
        // this.registerForContextMenu();
        
        setListAdapter(new SimpleAdapter(this, callStubVideosList(),
                R.layout.video_item, 
                new String[] { Video.FieldsKeys.TITLE, 
                               Video.FieldsKeys.AUTHOR, 
                               Video.FieldsKeys.DURATION, 
                               Video.FieldsKeys.TAGS },
                new int[] { R.id.videoItemTitle, R.id.videoItemAuthor,
                            R.id.videoItemDuration, R.id.videoItemTags }));
        
        //getListView().setTextFilterEnabled(true);
        
        Button tagsButton = (Button) findViewById(R.id.popularTagsButton);
        
        tagsButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                setListAdapter(new SimpleAdapter(LauncherActivity.this, callStubTagsList(),
                        R.layout.tag_item, 
                        new String[] { TagInfo.FieldsKeys.NAME, 
                                       TagInfo.FieldsKeys.USAGE_COUNT },
                        new int[] { R.id.tagItemName, R.id.tagItemUsageCount }));
                onContentChanged();  
            }
        });
        
        /* Cursor c = getContentResolver().query(People.CONTENT_URI,
        null, null, null, null);
        startManagingCursor(c);
        String[] cols = new String[]{People.NAME};
        int[] names = new int[]{R.id.row_tv};
        adapter = new SimpleCursorAdapter(this,R.layout.video_item,c,cols,names);
        this.setListAdapter(adapter);
        */
                
        /* Button sampleButton = (Button) findViewById(R.id.stubButton);
        sampleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast t = Toast.makeText(getApplicationContext(), "Vimeo Client Application", 10000);
                t.show();
            }
        }); */
        
    }
    
    /**
     * @return stub list of videos
     */
    private List<Map<String, Object>> callStubVideosList() {
        final List<Map<String, Object>> values = new ArrayList<Map<String, Object>>(); 
        
        for (Video video: getVideosFor("shamansir")) {
            values.add(adaptContent(video.extract()));
        }
        
        return values;
    }
    
    private List<Video> getVideosFor(String user) {
        final URI vimeoApiUri = VimeoApiUtils.resolveUriForSimpleApi(
                Uri.withAppendedPath(VimeoUnauthorizedProvider.BASE_URI, "/user/" + user + "/videos"));
        JSONArray videosArr = JsonOverHttp.askForArray(vimeoApiUri);
        
        try {
            final List<Video> result = new LinkedList<Video>();
            for (int i = 0; i < videosArr.length(); i++) {
                 JSONObject videoObj = (JSONObject)videosArr.get(i);
                 Video video = new Video();
                 video.id = videoObj.getInt("id");
                 video.title = videoObj.getString("title");
                 video.description = videoObj.getString("description");
                 video.duration = videoObj.getInt("duration");
                 video.tags = videoObj.getString("tags").split(",");
                 video.uploaderName = videoObj.getString("user_name");
                 result.add(video);
            }
            return result;
        } catch (JSONException jsone) {
            Log.e(TAG, "JSON error while getting videos list: " + jsone.getLocalizedMessage());
            jsone.printStackTrace();
            return null;
        }
    }
    
    /**
     * @return stub list of videos
     */
    private List<Map<String, Object>> callStubTagsList() {
        final List<Map<String, Object>> values = new ArrayList<Map<String, Object>>(); 
        
        for (int i = 0; i <= 20; i++) {
            
            final TagInfo tagStub = new TagInfo();
            tagStub.name = "Tag" + 300 + i;
            tagStub.url = "tag/" + i;
            tagStub.usageCount = 12 * i;
            
            values.add(adaptContent(tagStub.extract()));
        }
        
        return values;
    }    
    
    protected static Map<String, Object> adaptContent(ContentValues values) {
        if (values == null) return new HashMap<String, Object>();
        
        final Map<String, Object> result = new HashMap<String, Object>();        

        for (final Map.Entry<String, Object> entry: values.valueSet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        
        return result;
        
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        
        /* MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.main_options_menu, menu); 
        
        return true; */
        
        return super.onPrepareOptionsMenu(menu);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        
        /* MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.video_context_menu, menu); 
        
        return true; */
        
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onContextItemSelected(item);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }
}