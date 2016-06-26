package com.sam_chordas.android.stockhawk.Yahoo;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

public class TestUtilities extends AndroidTestCase {

    static List<Stock> createStockSymbolValues() {
        final List<Stock> stockList = new ArrayList<Stock>();
        stockList.add(new Stock("0","YHOO"));
        stockList.add(new Stock("1","AAPL"));
        stockList.add(new Stock("2","GOOG"));
        stockList.add(new Stock("3","MSFT"));
        return stockList;
    }
}