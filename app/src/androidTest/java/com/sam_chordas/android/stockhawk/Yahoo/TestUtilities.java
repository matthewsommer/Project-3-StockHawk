package com.sam_chordas.android.stockhawk.Yahoo;

import android.test.AndroidTestCase;

import com.sam_chordas.android.stockhawk.model.Quote;

import java.util.ArrayList;
import java.util.List;

public class TestUtilities extends AndroidTestCase {

    static List<Quote> createStockSymbolValues() {
        final List<Quote> quoteList = new ArrayList<Quote>();
        quoteList.add(new Quote("YHOO"));
        quoteList.add(new Quote("AAPL"));
        quoteList.add(new Quote("GOOG"));
        quoteList.add(new Quote("MSFT"));
        return quoteList;
    }
}