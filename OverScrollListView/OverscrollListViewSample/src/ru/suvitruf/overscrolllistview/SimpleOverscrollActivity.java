package ru.suvitruf.overscrolllistview;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;


public class SimpleOverscrollActivity extends Activity {

	private OverscrollListView mOverscrollListView;
	private ArrayAdapter<String> mAdapter;
	 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_oslv);
		Log.d("overscroll", "111"); 
		mOverscrollListView = (OverscrollListView)findViewById(R.id.activity_oslv_list);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings);
		mOverscrollListView.setAdapter(mAdapter);	
	}

	
	
	private String[] mStrings = {  "Ukhta", "Tomsk", "Tula","Abakan", "Raduzhny", "Moscow", "Ufa", "Volgograd",
			"Yemva", "Makhachkala", "Kirov" , "Dobryanka" };
}
