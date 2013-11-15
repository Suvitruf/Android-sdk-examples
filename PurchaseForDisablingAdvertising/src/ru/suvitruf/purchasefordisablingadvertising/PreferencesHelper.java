package ru.suvitruf.purchasefordisablingadvertising;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
	private static final String SETTINGS = "settings";
	private static final String TAG_DISABLED_ADS = "disabledADS";

	private static boolean disabledADS;

	public static boolean isAdsDisabled() {
//		LOG.d("disabledADS = "+disabledADS);
		return disabledADS;
	}

	public static enum Purchase {
		DISABLE_ADS
	};

	public static void savePurchase(Context c, Purchase p, boolean v) {
		SharedPreferences settings = c.getSharedPreferences(SETTINGS, 0);
		SharedPreferences.Editor editor = settings.edit();
		switch (p) {
		case DISABLE_ADS:
			editor.putBoolean("TAG_DISABLED_ADS", v);
			disabledADS = v;
			break;
		}
		editor.commit();
	}

	public static void loadSettings(Context c) {

		SharedPreferences settings = c.getSharedPreferences(SETTINGS, 0);
		if (settings.getAll().size() == 0) {
			SharedPreferences.Editor editor = settings.edit();
			editor.clear();
			editor.commit();

			disabledADS = false;
		} else
			disabledADS = settings.getBoolean(TAG_DISABLED_ADS, false);

	}

	public static void saveSettings(Context c) {
		SharedPreferences settings = c.getSharedPreferences(SETTINGS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(TAG_DISABLED_ADS, disabledADS);

		editor.commit();
	}
}
