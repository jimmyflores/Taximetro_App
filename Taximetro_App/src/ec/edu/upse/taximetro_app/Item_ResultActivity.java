package ec.edu.upse.taximetro_app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Item_ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item__result);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item__result, menu);
		return true;
	}

}
