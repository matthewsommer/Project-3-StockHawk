package com.sam_chordas.android.stockhawk.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.os.Bundle;

import com.sam_chordas.android.stockhawk.data.Contract;

public class SyncHelper {

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
}
