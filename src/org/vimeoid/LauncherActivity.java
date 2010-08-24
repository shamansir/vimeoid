package org.vimeoid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vimeoid.dto.Video;

import android.app.ListActivity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.SimpleAdapter;

public class LauncherActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_view);
        
        // this.registerForContextMenu();
        
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
        
        /* Intent intent = getIntent();
        String path = intent.getStringExtra("com.example.android.apis.Path");
        
        if (path == null) {
            path = "";
        } */
        
        setListAdapter(new SimpleAdapter(this, makeStubVideosList(),
                R.layout.video_item, 
                new String[] { Video.FieldsKeys.TITLE, 
                               Video.FieldsKeys.AUTHOR, 
                               Video.FieldsKeys.DURATION, 
                               Video.FieldsKeys.TAGS },
                new int[] { R.id.videoItemTitle, R.id.videoItemAuthor,
                            R.id.videoItemDuration, R.id.videoItemTags }));
        
        //getListView().setTextFilterEnabled(true);
    }
    
    /**
     * @return stub list of videos
     */
    private List<Map<String, Object>> makeStubVideosList() {
        final List<Map<String, Object>> values = new ArrayList<Map<String, Object>>(); 
        
        for (int i = 0; i <= 20; i++) {
            
            final Video videoStub = new Video();
            videoStub.id = i + 400;
            videoStub.title = "Video " + i;
            videoStub.description = "Video " + i + " description";
            videoStub.tags = new String[] { ("aa" + i), ("bb" + i), ("cc" + i) };
            videoStub.duration = 25777 + i;
            
            values.add(adaptContent(videoStub.extract()));
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