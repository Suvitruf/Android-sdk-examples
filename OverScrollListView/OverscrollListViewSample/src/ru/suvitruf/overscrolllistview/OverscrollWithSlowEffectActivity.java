package ru.suvitruf.overscrolllistview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class OverscrollWithSlowEffectActivity extends Activity {

	private OverscrollListView mOverscrollListView;
	private ArrayAdapter<String> mAdapter;
	  
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oslv_with_slow_effect);

		mOverscrollListView = (OverscrollListView)findViewById(R.id.activity_oslv_list);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings);
		mOverscrollListView.setAdapter(mAdapter); 
		
	}
	           
	
	
	private String[] mStrings = {  "Ukhta", "Tomsk", "Tula","Abakan", "Raduzhny", "Moscow", "Ufa", "Volgograd",
			"Yemva", "Makhachkala", "Kirov" , "Dobryanka" , "Piter" , "Inta" , "Ekb" , "Vladivostok" };
}
