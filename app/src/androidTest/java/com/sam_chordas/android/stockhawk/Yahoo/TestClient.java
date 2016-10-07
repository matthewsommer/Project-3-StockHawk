package com.sam_chordas.android.stockhawk.Yahoo;

import android.content.ContentValues;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.Vector;

public class TestClient extends AndroidTestCase {
    private static final String LOG_TAG = TestClient.class.getSimpleName();

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testFetchStockData() {
        Uri stockSearchUri = YahooDataContract.Stocks.buildStockSearchUri(TestUtilities.createStockSymbolValues());
        String responseStr = YahooClient.FetchStockData(stockSearchUri);
        assertNotNull("Error: Yahoo YahooClient.FetchStockData() null", responseStr);
        Vector<ContentValues> cVVector = QuoteHandler.parseJSON(responseStr);
        assertNotNull("Error: Yahoo YahooDataContract.Stocks.parseJSON(responseStr) null", cVVector);
        assertEquals(4,cVVector.size());
    }
}