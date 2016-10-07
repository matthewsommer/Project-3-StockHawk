package com.sam_chordas.android.stockhawk.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sam_chordas.android.stockhawk.data.Contract;
import com.sam_chordas.android.stockhawk.model.Quote;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SyncHelper {

    private Context mContext;
    private QuotesDataHandler mQuotesDataHandler;
    private RemoteQuotesDataFetcher mRemoteQuotesDataFetcher;

    public SyncHelper(Context context) {
        mContext = context;
        mQuotesDataHandler = new QuotesDataHandler(mContext);
        mRemoteQuotesDataFetcher = new RemoteQuotesDataFetcher(mContext);
    }

    public static void requestManualSync(Account mChosenAccount) {
        requestManualSync(mChosenAccount, false);
    }

    public static void requestManualSync(Account mChosenAccount, boolean userDataSyncOnly) {
        if (mChosenAccount != null) {
            Bundle b = new Bundle();
            b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

            ContentResolver
                    .setSyncAutomatically(mChosenAccount, Contract.CONTENT_AUTHORITY, true);
            ContentResolver.setIsSyncable(mChosenAccount, Contract.CONTENT_AUTHORITY, 1);

            boolean pending = ContentResolver.isSyncPending(mChosenAccount, Contract.CONTENT_AUTHORITY);
            if (pending) {

            }
            boolean active = ContentResolver.isSyncActive(mChosenAccount, Contract.CONTENT_AUTHORITY);
            if (active) {

            }

            if (pending || active) {
                ContentResolver.cancelSync(mChosenAccount, Contract.CONTENT_AUTHORITY);
            }

            ContentResolver.requestSync(mChosenAccount, Contract.CONTENT_AUTHORITY, b);
        } else {

        }
    }

    public boolean performSync(@Nullable SyncResult syncResult, Account account, Bundle extras) {
        if (!isOnline()) {
            Log.d("Offline", "Not syncing, offline");
            return false;
        }

        Cursor c = mContext.getContentResolver().query(Contract.QuoteEntry.CONTENT_URI,
                new String[]{ Contract.QuoteEntry.COLUMN_SYMBOL},
                null,
                null,
                null);

        final List<Quote> quoteList = new ArrayList<Quote>();

        if (c.getCount() != 0) {
            try {
                while (c.moveToNext()) {
                    Log.d("cursor",c.getString(c.getColumnIndex(Contract.QuoteEntry.COLUMN_SYMBOL)));
                    quoteList.add(new Quote(c.getString(c.getColumnIndex(Contract.QuoteEntry.COLUMN_SYMBOL))));
                }
            } finally {
                c.close();
            }
        }

        Vector<ContentValues> cVVector = RemoteQuotesDataFetcher.fetchQuoteData(quoteList);
        int insertCount = mQuotesDataHandler.insertQuotes(cVVector, Contract.QuoteEntry.CONTENT_URI);
        return true;
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}