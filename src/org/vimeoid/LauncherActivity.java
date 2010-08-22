package org.vimeoid;

import android.app.ListActivity;
import android.content.Intent;
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
        
        /*
        Cursor c = getContentResolver().query(People.CONTENT_URI,
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
        
        /*
        Intent intent = getIntent();
        String path = intent.getStringExtra("com.example.android.apis.Path");
        
        if (path == null) {
            path = "";
        }

        setListAdapter(new SimpleAdapter(this, getData(path),
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 }));
        getListView().setTextFilterEnabled(true);
         */
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