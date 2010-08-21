package org.vimeoid;

import android.app.ListActivity;
import android.os.Bundle;

public class LauncherActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list);
        
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