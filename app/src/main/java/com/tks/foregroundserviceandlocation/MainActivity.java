package com.tks.foregroundserviceandlocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tks.foregroundserviceandlocation.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TLog.d("aaaaa");

		findViewById(R.id.btnStartService).setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, FlcService.class);
			intent.setAction(Constants.ACTION.START);
			startForegroundService(intent);
		});

		findViewById(R.id.btnStopService).setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, FlcService.class);
			intent.setAction(Constants.ACTION.STOP);
			startService(intent);
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		TLog.d("aaaaa");
	}
}