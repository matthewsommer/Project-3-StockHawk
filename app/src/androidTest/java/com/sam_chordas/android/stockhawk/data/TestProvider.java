package com.sam_chordas.android.stockhawk.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestProvider extends AndroidTestCase {

    private ContentResolver mContentResolver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mContentResolver = this.mContext.getContentResolver();
        deleteAllRecordsFromProvider();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                Provider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: Provider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + Contract.CONTENT_AUTHORITY,
                    providerInfo.authority, Contract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: Provider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void deleteAllRecordsFromProvider() {
        mContentResolver.delete(
                Contract.QuoteEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContentResolver.query(
                Contract.QuoteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from table during delete", 0, cursor.getCount());

        cursor.close();
    }

    public void testGetType() {
        testGetType(ProviderUriEnum.QUOTE.contentType, Contract.QuoteEntry.CONTENT_URI);
        testGetType(ProviderUriEnum.QUOTE.contentType, Contract.QuoteEntry.CONTENT_URI);
        testGetType(ProviderUriEnum.QUOTE_WITH_ID.contentType, Contract.QuoteEntry.buildUri(321));
    }

    public void testGetType(String contentType, Uri contentUri) {
        String type = mContentResolver.getType(contentUri);
        assertEquals("Error: CONTENT_URI should return CONTENT_TYPE", contentType, type);
    }

    public void testQuoteInsertQueryUpdateDelete() {

        //Insert
        ContentValues quoteValues = TestUtilities.createQuoteValues();
        Uri uri = mContentResolver.insert(Contract.QuoteEntry.buildUri(), quoteValues);
        long quoteRowId = Contract.QuoteEntry.getQuoteIdFromUri(uri);
        assertTrue("Unable to Insert TaskEntry into the Database", quoteRowId != -1);

        //Query
        Cursor quoteCursor = mContentResolver.query(
                Contract.QuoteEntry.buildUri(quoteRowId),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testTaskQuery", quoteCursor, quoteValues);

        //Update
        ContentValues cv = new ContentValues();
        cv.put(Contract.QuoteEntry.COLUMN_PERCENT_CHANGE, "abc123");
        mContentResolver.update(Contract.QuoteEntry.buildUri(quoteRowId),cv, null,null);
        Cursor uCursor = mContentResolver.query(Contract.QuoteEntry.buildUri(quoteRowId), new String[]{Contract.QuoteEntry.COLUMN_PERCENT_CHANGE, Contract.QuoteEntry.COLUMN_PERCENT_CHANGE}, null, null, null);
        uCursor.moveToFirst();
        assertTrue(uCursor.getString(uCursor.getColumnIndex(Contract.QuoteEntry.COLUMN_PERCENT_CHANGE)).equals("abc123"));

        //Delete
        int del = mContentResolver.delete(Contract.QuoteEntry.buildUri(), Contract.QuoteEntry.COLUMN_SYMBOL+"=?", new String[]{quoteValues.getAsString(Contract.QuoteEntry.COLUMN_SYMBOL)});
        assertTrue(del > 0);
    }

    public void testBulkInsert() {
        ContentValues[] bulkInsertContentValues = TestUtilities.createBulkInsertTaskValues();

        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        mContentResolver.registerContentObserver(Contract.QuoteEntry.CONTENT_URI, true, contentObserver);

        int insertCount = mContentResolver.bulkInsert(Contract.QuoteEntry.CONTENT_URI, bulkInsertContentValues);

        contentObserver.waitForNotificationOrFail();
        mContentResolver.unregisterContentObserver(contentObserver);

        assertEquals(TestUtilities.BULK_INSERT_RECORDS_TO_INSERT, insertCount);

        Cursor cursor = mContentResolver.query(
                Contract.QuoteEntry.buildUri(),
                null,
                null,
                null,
                null
        );

        assertEquals(cursor.getCount(), TestUtilities.BULK_INSERT_RECORDS_TO_INSERT);

        cursor.moveToFirst();
        for ( int i = 0; i < TestUtilities.BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating QuoteEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}