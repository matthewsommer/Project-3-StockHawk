package com.sam_chordas.android.stockhawk.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestDbHelper extends AndroidTestCase {

    void deleteTheDatabase() {
        mContext.deleteDatabase(DbHelper.DB_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(DbHelper.Tables.QUOTE);

        mContext.deleteDatabase(DbHelper.DB_NAME);
        SQLiteDatabase db = new DbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: Database not created correctly", c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());
        assertTrue("Error: Database created without proper entries", tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + DbHelper.Tables.QUOTE + ")", null);
        assertTrue("Error: Unable to query database for table information.", c.moveToFirst());

        final HashSet<String> columnHashSet = new HashSet<String>();
        columnHashSet.add(Contract.QuoteEntry._ID);
        columnHashSet.add(Contract.QuoteEntry.COLUMN_SYMBOL);
        columnHashSet.add(Contract.QuoteEntry.COLUMN_PERCENT_CHANGE);
        columnHashSet.add(Contract.QuoteEntry.COLUMN_CHANGE);
        columnHashSet.add(Contract.QuoteEntry.COLUMN_BIDPRICE);
        columnHashSet.add(Contract.QuoteEntry.COLUMN_CREATED);
        columnHashSet.add(Contract.QuoteEntry.COLUMN_ISUP);
        columnHashSet.add(Contract.QuoteEntry.COLUMN_ISCURRENT);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while(c.moveToNext());
        assertTrue("Error: Database doesn't contain all columns", columnHashSet.isEmpty());

        db.close();
    }

    public void testAllDbTables() {
        TestDbTable(DbHelper.Tables.QUOTE, createQuoteValues());
    }

    private void TestDbTable(String tableName, ContentValues contentValues) {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowId = db.insert(tableName, null, contentValues);
        assertTrue(rowId != -1);
        Cursor queryCursor = db.query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertTrue( "Error: No Records returned from query", queryCursor.moveToFirst() );

        validateCurrentRecord("testInsertReadDb failed", queryCursor, contentValues);
        assertFalse( "Error: More than one record returned from query", queryCursor.moveToNext());

        queryCursor.close();
        dbHelper.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createQuoteValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.QuoteEntry.COLUMN_ID, 321);
        contentValues.put(Contract.QuoteEntry.COLUMN_SYMBOL, "AAPL");
        contentValues.put(Contract.QuoteEntry.COLUMN_PERCENT_CHANGE, "12%");
        contentValues.put(Contract.QuoteEntry.COLUMN_CHANGE, "$2");
        contentValues.put(Contract.QuoteEntry.COLUMN_BIDPRICE, "$2");
        contentValues.put(Contract.QuoteEntry.COLUMN_CREATED, "06/12/16");
        contentValues.put(Contract.QuoteEntry.COLUMN_ISUP, 1);
        contentValues.put(Contract.QuoteEntry.COLUMN_ISCURRENT, 0);
        return contentValues;
    }
}