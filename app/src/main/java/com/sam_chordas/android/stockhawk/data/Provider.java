package com.sam_chordas.android.stockhawk.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public class Provider extends ContentProvider {

    private UriMatcher sUriMatcher;
    private DbHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        this.sUriMatcher = new UriMatcher();
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final ProviderUriEnum providerEnum = sUriMatcher.matchUri(uri);
        return providerEnum.contentType;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        //will throw exception if not matched so default can now be default action
        ProviderUriEnum providerEnum = sUriMatcher.matchUri(uri);

        switch (providerEnum) {
            default:{
                //this default will hit for all enums that are not explicitly
                //mentioned in this switch code. ie... default :)
                Cursor cursor =  db.query(
                        providerEnum.table_name,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            case QUOTE_WITH_ID: {
                return queryEntryCursorById(uri, projection, sortOrder, providerEnum, db);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final ProviderUriEnum match = sUriMatcher.matchUri(uri);
        Uri returnUri;

        long _id = -1L;
        //this is the actual insert for all matchable tables
        if (match.table_name != null) {
            _id = db.insertOrThrow(match.table_name, null, values);
            notifyChange(uri);
        }

        //TODO: handle if there is no table name??? probably in the switch?

        switch (match) {
            case QUOTE: {
                return Contract.QuoteEntry.buildUri(_id);
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final ProviderUriEnum match = sUriMatcher.matchUri(uri);
        int rowsDeleted;

        if ( null == selection ) selection = "1";

        switch (match) {
            default:{
                rowsDeleted = db.delete(match.table_name, selection, selectionArgs);
            }
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private void normalizeDate(ContentValues values) {
//        if (values.containsKey(JIRAContract.TaskEntry.COLUMN_CREATED_DATE)) {
//            String dateStr = values.getAsString(JIRAContract.TaskEntry.COLUMN_CREATED_DATE);
//            values.put(JIRAContract.TaskEntry.COLUMN_CREATED_DATE, JIRAContract.normalizeDate(dateStr));
//        }
//        if (values.containsKey(JIRAContract.TaskEntry.COLUMN_UPDATED_DATE)) {
//            String dateStr = values.getAsString(JIRAContract.TaskEntry.COLUMN_UPDATED_DATE);
//            values.put(JIRAContract.TaskEntry.COLUMN_UPDATED_DATE, JIRAContract.normalizeDate(dateStr));
//        }
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final ProviderUriEnum match = sUriMatcher.matchUri(uri);
        int rowsUpdated;

        //by default we will normalize the date (if needed) and then update db
        switch (match) {
            default: {
                normalizeDate(values);
                rowsUpdated = db.update(match.table_name, values, selection,
                        selectionArgs);
                break;
            }
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final ProviderUriEnum match = sUriMatcher.matchUri(uri);
        switch (match) {
            case QUOTE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(match.table_name, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }


    /**
     * This is the default query into the database for a
     * "get by ID" call (which there are a lot of)
     * handy function so all the calls do not have to know of
     * the {@BaseColumns._ID} naming scheme
     *
     * WARNING: the assumption is made here that the .get(1)
     * on the uri path segments will be the ID.  This is by
     * convention
     *
     * @param uri URI that has been matched
     * @param projection Projection to be returned
     * @param sortOrder Any additional sort order
     * @param pEnum  matched provider enum
     * @param db  readable database
     * @return  Returns the cursor object
     */

    private Cursor queryEntryCursorById(Uri uri, String[] projection, String sortOrder, ProviderUriEnum pEnum, SQLiteDatabase db) {

        String taskId = uri.getPathSegments().get(1);;
        String selection = BaseColumns._ID + " =?";
        String[] selectionArgs = new String[]{taskId};
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(pEnum.table_name);


        Cursor cursor = builder.query(db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    /**
     * Notifies the system that the given {@code uri} data has changed.
     * <p/>
     * We only notify changes if the uri wasn't called by the sync adapter, to avoid issuing a large
     * amount of notifications while doing a sync.
     */
    private void notifyChange(Uri uri) {
        //TODO: as seen below (commented out) a way to guard against sync adapter
        //if (!ScheduleContractHelper.isUriCalledFromSyncAdapter(uri)) {
        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null);

        // Widgets can't register content observers so we refresh widgets separately.
        //context.sendBroadcast(ScheduleWidgetProvider.getRefreshBroadcastIntent(context, false));
        //}
    }
}