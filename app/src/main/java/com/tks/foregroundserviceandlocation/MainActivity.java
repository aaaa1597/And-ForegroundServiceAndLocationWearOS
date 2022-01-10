package com.tks.foregroundserviceandlocation;

import androidx.annotation.NonNull;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import java.util.Arrays;

public class MainActivity extends Activity {
	private final static int	REQUEST_LOCATION_SETTINGS	= 1111;
	private final static int	REQUEST_PERMISSIONS			= 2222;

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

		/* 権限が許可されていない場合はリクエスト. */
		if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
				requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_PERMISSIONS);
			else
				requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS);
		}

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

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		/* 対象外なので、無視 */
		if (requestCode != REQUEST_PERMISSIONS) return;

		/* 権限リクエストの結果を取得する. */
		long ngcnt = Arrays.stream(grantResults).filter(value -> value != PackageManager.PERMISSION_GRANTED).count();
		if (ngcnt > 0) {
			ErrDialog.create(MainActivity.this, "このアプリには必要な権限です。\n再起動後に許可してください。\n終了します。").show();
			return;
		}
	}
}