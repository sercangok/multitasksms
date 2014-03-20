package com.salyangoz.multitasksms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.mainactivity);
		super.onCreate(savedInstanceState);
	}

	public void toggle(View v) {
		ToggleButton toggleButton = (ToggleButton) v;
		Intent serviceIntent = new Intent(this, MultiTaskSMSService.class);
		if (toggleButton.isChecked())
			startService(serviceIntent);
		else
			stopService(serviceIntent);
	}

}
