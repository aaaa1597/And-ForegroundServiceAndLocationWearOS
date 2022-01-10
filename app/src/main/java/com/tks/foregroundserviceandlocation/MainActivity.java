package com.tks.foregroundserviceandlocation;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

public class MainActivity extends Activity {
	private final static int	REQUEST_LOCATION_SETTINGS	= 1111;

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

		findViewById(R.id.btnStartLocation).setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, FlcService.class);
			intent.setAction(Constants.ACTION.STARTLOC);
			startService(intent);
		});

		findViewById(R.id.btnStopLocation).setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, FlcService.class);
			intent.setAction(Constants.ACTION.STOPLOC);
			startService(intent);
		});

		/* 設定の位置情報ON/OFFチェック */
		LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder().build();
		SettingsClient settingsClient = LocationServices.getSettingsClient(this);
		settingsClient.checkLocationSettings(locationSettingsRequest)
				.addOnFailureListener(this, exception -> {
					int statusCode = ((ApiException)exception).getStatusCode();
					switch (statusCode) {
						case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
							try {
								ResolvableApiException rae = (ResolvableApiException)exception;
								rae.startResolutionForResult(MainActivity.this, REQUEST_LOCATION_SETTINGS);
							}
							catch (IntentSender.SendIntentException sie) {
								ErrDialog.create(MainActivity.this, "システムエラー!\n再起動で直ることがあります。\n終了します。").show();
							}
							break;
						case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
							ErrDialog.create(MainActivity.this, "このアプリには位置情報をOnにする必要があります。\n再起動後にOnにしてください。\n終了します。").show();
							break;
						case LocationSettingsStatusCodes.DEVELOPER_ERROR:
							if(((ApiException)exception).getMessage().contains("Not implemented")) {
								/* 問題ないことにする。未実装ということは、常にONと解釈する。 */
								break;
							}
							ErrDialog.create(MainActivity.this, "位置情報の機能が存在しない端末です。\n動作しないので、終了します。").show();
							break;
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		TLog.d("aaaaa");
	}
}