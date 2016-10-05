package com.sam_chordas.android.stockhawk.sync;

import android.content.Context;
import android.preference.PreferenceManager;

public class DataHandler {
    private static final String SP_KEY_DATA_TIMESTAMP = "data_timestamp";

    public static void resetDataTimestamp(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(
                SP_KEY_DATA_TIMESTAMP).commit();
    }
}