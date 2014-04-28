package ru.suvitruf.overscrolllistview;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	public static final String[] options = { 
		"Simple OverscrollListView", 
		"OverscrollListView with slow effect", 
		};
      
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options));
	}
  
	    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent;
 
		switch (position) {
			default:
			case 0:
				intent = new Intent(this, SimpleOverscrollActivity.class);
				break;
			case 1:
				intent = new Intent(this, OverscrollWithSlowEffectActivity.class);
				break;

		}

		startActivity(intent);
	}


}
