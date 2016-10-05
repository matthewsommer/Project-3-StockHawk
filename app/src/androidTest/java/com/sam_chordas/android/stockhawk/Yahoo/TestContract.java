package com.sam_chordas.android.stockhawk.Yahoo;

import android.net.Uri;
import android.test.AndroidTestCase;

public class TestContract extends AndroidTestCase {
    private static final String TEST_SEARCH_URL = "https://query.yahooapis.com/v1/public" +
            "/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22" +
            "YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22)&format=json&diagnostics=true&" +
            "env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    public void testBuildStockSearchUri() {
        Uri stockSearchUri = Contract.Stocks.buildStockSearchUri(TestUtilities.createStockSymbolValues());
        assertEquals("Error: Uri unmatched", stockSearchUri.toString(), TEST_SEARCH_URL);
    }
}