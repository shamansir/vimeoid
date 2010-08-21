package org.vimeoid;

import android.app.ListActivity;
import android.os.Bundle;

public class LauncherActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_view);
        
        /*
        Cursor c = getContentResolver().query(People.CONTENT_URI,
null, null, null, null);
        startManagingCursor(c);
        String[] cols = new String[]{People.NAME};
        int[] names = new int[]{R.id.row_tv};
        adapter = new SimpleCursorAdapter(this,R.layout.list_item,c,cols,names);
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
}