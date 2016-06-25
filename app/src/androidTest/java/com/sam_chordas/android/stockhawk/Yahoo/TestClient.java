package com.sam_chordas.android.stockhawk.Yahoo;

import android.test.AndroidTestCase;
import java.util.ArrayList;
import java.util.List;

public class TestClient extends AndroidTestCase {
    private static final String LOG_TAG = TestClient.class.getSimpleName();
    private static final List<Stock> stockList = new ArrayList<Stock>();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Stock yahoo = new Stock("0","YHOO");
        Stock apple = new Stock("1","AAPL");
        Stock google = new Stock("2","GOOG");
        Stock microsoft = new Stock("3","MSFT");
        stockList.add(yahoo);
        stockList.add(apple);
        stockList.add(google);
        stockList.add(microsoft);
    }

    public void testFetchStockData() {
        String responseStr = Client.FetchStockData(stockList);
        assertNotNull("Error: Yahoo Client.FetchStockData() null", responseStr);
    }
}