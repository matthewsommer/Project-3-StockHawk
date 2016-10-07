package com.sam_chordas.android.stockhawk.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sam_chordas.android.stockhawk.data.Contract;
import com.sam_chordas.android.stockhawk.model.Quote;

import java.io.IOException;
import java.util.Vector;

public class QuotesDataHandler {
    private static final String SP_KEY_DATA_TIMESTAMP = "data_timestamp";
    Context mContext = null;
    public QuotesDataHandler(Context ctx) {
        mContext = ctx;
    }

    public static void resetDataTimestamp(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(
                SP_KEY_DATA_TIMESTAMP).commit();
    }

    public int insertQuotes(Vector<ContentValues> cVVector, Uri contentUri) {
        int inserted = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.QuoteEntry.COLUMN_ISCURRENT, 0);
        mContext.getContentResolver().update(Contract.QuoteEntry.CONTENT_URI, contentValues,
                null, null);

        if ( cVVector != null && cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(contentUri, cvArray);
        }
        return inserted;
    }
}