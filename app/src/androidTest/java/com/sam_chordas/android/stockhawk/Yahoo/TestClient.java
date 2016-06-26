package com.sam_chordas.android.stockhawk.Yahoo;

import android.net.Uri;
import android.test.AndroidTestCase;
import java.util.ArrayList;
import java.util.List;

public class TestClient extends AndroidTestCase {
    private static final String LOG_TAG = TestClient.class.getSimpleName();

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testFetchStockData() {
        Uri stockSearchUri = Contract.Stocks.buildStockSearchUri(TestUtilities.createStockSymbolValues());
        String responseStr = Client.FetchStockData(stockSearchUri);
        Contract.Stocks.parseJSON(responseStr);
        assertNotNull("Error: Yahoo Client.FetchStockData() null", responseStr);
    }
}