package com.tks.foregroundserviceandlocation;

public class Constants {
	public static final int		NOTIFICATION_ID_FOREGROUND_SERVICE = 1231234;
	public static final String	NOTIFICATION_CHANNEL_STARTSTOP = "NOTIFICATION_CHANNEL_STARTSTOP";

	public static class ACTION {
		public static final String MAIN_ACTION = "uws.action.main";
		public static final String START = "uws.action.start";
		public static final String STOP = "uws.action.stop";
	}

	public static class STATE_SERVICE {
		public static final int CONNECTED = 10;
		public static final int NOT_CONNECTED = 0;
	}
}
