package com.sam_chordas.android.stockhawk.Yahoo;

import java.util.List;
import java.util.Locale;

public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();
    private static Locale defaultLocale = Locale.getDefault();
    private static final String SEARCH_START = "select * from yahoo.finance.quotes where symbol "
            + "in (";

    public static List<Quote> ParseResponseStr(String responseStr) {
        return null;
    }

    public static String truncateBidPrice(String bidPrice) {
        try {
            bidPrice = String.format(defaultLocale, "%.2f", Float.parseFloat(bidPrice));
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Argument: " + e);
        }
        return bidPrice;
    }

    public static String buildSearchUri() {
        return "";
    }
}