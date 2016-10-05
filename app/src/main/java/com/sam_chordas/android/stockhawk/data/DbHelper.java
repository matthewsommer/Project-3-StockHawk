package com.sam_chordas.android.stockhawk.data;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sam_chordas.android.stockhawk.AccountUtil;
import com.sam_chordas.android.stockhawk.data.Contract.*;
import com.sam_chordas.android.stockhawk.sync.DataHandler;
import com.sam_chordas.android.stockhawk.sync.SyncHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VER_0_1 = 8;
    private static final int CUR_DB_VER = VER_0_1;
    static final String DB_NAME = "stockhawk.db";
    private final Context mContext;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, CUR_DB_VER);
        mContext = context;
    }

    public interface Tables {
        String QUOTE = "quotes";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTasksTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Account account = AccountUtil.getActiveAccount(mContext);
        if (account != null) {
            ContentResolver.cancelSync(account, Contract.CONTENT_AUTHORITY);
        }

        int version = oldVersion;
        boolean dataInvalidated = true;

        if (version != CUR_DB_VER) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.QUOTE);
            onCreate(db);
            version = CUR_DB_VER;
        }

        if (dataInvalidated) {
            DataHandler.resetDataTimestamp(mContext);
            if (account != null) {
                SyncHelper.requestManualSync(account);
            }
        }
    }

    private void createTasksTable(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TASK_TABLE = "CREATE TABLE " +
                Tables.QUOTE + " (" +
                QuoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                QuoteEntry.COLUMN_SYMBOL + " TEXT NOT NULL," +
                QuoteEntry.COLUMN_PERCENT_CHANGE + " TEXT NOT NULL," +
                QuoteEntry.COLUMN_CHANGE + " TEXT NOT NULL," +
                QuoteEntry.COLUMN_BIDPRICE + " TEXT NOT NULL," +
                QuoteEntry.COLUMN_CREATED + " TEXT NOT NULL," +
                QuoteEntry.COLUMN_ISUP + " INTEGER NOT NULL," +
                QuoteEntry.COLUMN_ISCURRENT + " INTEGER NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_TASK_TABLE);
    }
}