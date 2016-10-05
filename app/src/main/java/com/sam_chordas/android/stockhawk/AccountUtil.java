package com.sam_chordas.android.stockhawk;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AccountUtil {
    private static final String PREF_ACTIVE_ACCOUNT = "chosen_account";

    private static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getActiveAccountName(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(PREF_ACTIVE_ACCOUNT, null);
    }

    public static Account getActiveAccount(final Context context) {
//        String account = getActiveAccountName(context);
//        if (account != null) {
//            return new Account(account, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
//        } else {
//            return null;
//        }
        return null;
    }
}
