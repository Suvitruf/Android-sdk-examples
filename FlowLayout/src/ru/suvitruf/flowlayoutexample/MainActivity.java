package ru.suvitruf.flowlayoutexample;

import ru.suvitruf.flowlayoutexample.view.FlowLayout;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends Activity {
	private static final int TEST_ELEMENTS_COUNT = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FlowLayout l = (FlowLayout) findViewById(R.id.test_flow_layout);

		for (int i = 0; i < TEST_ELEMENTS_COUNT; ++i) {
			Button btn = new Button(this);
			btn.setText(getResources().getString(R.string.test_string)
					+ Integer.toString(i));
			l.addView(btn);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
