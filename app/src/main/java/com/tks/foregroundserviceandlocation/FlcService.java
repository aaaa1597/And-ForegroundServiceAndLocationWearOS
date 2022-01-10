package com.tks.foregroundserviceandlocation;

import static com.tks.foregroundserviceandlocation.Constants.NOTIFICATION_CHANNEL_STARTSTOP;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class FlcService extends Service {
	private Handler mHandler = new Handler();
	private Runnable mLogger = new Runnable() {
		int cnt = 0;
		@Override
		public void run() {
			TLog.d("aaaaaaa cnt={0}", cnt++);
			mHandler.postDelayed(this,2500);
		}
	};

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// if user starts the service
		switch (intent.getAction()) {
			case Constants.ACTION.START:
				TLog.d("aaaaaaaaaaaa start");
				startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());
				mHandler.post(mLogger);
				break;
			case Constants.ACTION.STOP:
				stopForeground(true);
				mHandler.removeCallbacks(mLogger);
				TLog.d("aaaaaaaaaaaa end.");
				stopSelf();
				break;
			default:
				stopForeground(true);
				stopSelf();
		}

		return START_NOT_STICKY;
	}

	private Notification prepareNotification() {
		TLog.d("aaaaaaaaaaaa 000");
		/* 通知のチャンネル生成 */
		NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_STARTSTOP, "startstop", NotificationManager.IMPORTANCE_DEFAULT);
		channel.enableVibration(false);
		NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationManager.createNotificationChannel(channel);

		/* 停止ボタン押下の処理実装 */
		Intent stopIntent = new Intent(this, FlcService.class)
				.setAction(Constants.ACTION.STOP);
		PendingIntent pendingStopIntent = PendingIntent.getService(this, 2222, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
		remoteViews.setOnClickPendingIntent(R.id.btnStop, pendingStopIntent);

		TLog.d("aaaaaaaaaaaa 000");

		return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_STARTSTOP)
				.setContent(remoteViews)
				.setSmallIcon(R.mipmap.ic_launcher)
//				.setCategory(NotificationCompat.CATEGORY_SERVICE)
//				.setOnlyAlertOnce(true)
//				.setOngoing(true)
//				.setAutoCancel(true)
//				.setContentIntent(pendingIntent);
//				.setVisibility(Notification.VISIBILITY_PUBLIC)
				.build();
	}
}
