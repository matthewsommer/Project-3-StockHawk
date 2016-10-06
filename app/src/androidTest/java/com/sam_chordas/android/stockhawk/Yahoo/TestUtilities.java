package com.sam_chordas.android.stockhawk.Yahoo;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import com.sam_chordas.android.stockhawk.Yahoo.Quote;
import com.sam_chordas.android.stockhawk.data.Contract;

import java.util.ArrayList;
import java.util.List;

public class TestUtilities extends AndroidTestCase {

    static List<Quote> createStockSymbolValues() {
        final List<Quote> quoteList = new ArrayList<Quote>();
        quoteList.add(new Quote("0","YHOO"));
        quoteList.add(new Quote("1","AAPL"));
        quoteList.add(new Quote("2","GOOG"));
        quoteList.add(new Quote("3","MSFT"));
        return quoteList;
    }
}