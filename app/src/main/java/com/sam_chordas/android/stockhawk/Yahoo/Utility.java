package com.sam_chordas.android.stockhawk.Yahoo;

import com.sam_chordas.android.stockhawk.model.Quote;

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

    public static String buildSearchUri() {
        return "";
    }

    public static String truncateChange(String change, boolean isPercentChange) {
        if (change.equalsIgnoreCase("null")) {
            return "null";
        }
        String weight = change.substring(0, 1);
        String ampersand = "";
        if (isPercentChange) {
            ampersand = change.substring(change.length() - 1, change.length());
            change = change.substring(0, change.length() - 1);
        }
        change = change.substring(1, change.length());
        double round = (double) Math.round(Double.parseDouble(change) * 100) / 100;
        change = String.format(defaultLocale, "%.2f", round);
        StringBuffer changeBuffer = new StringBuffer(change);
        changeBuffer.insert(0, weight);
        changeBuffer.append(ampersand);
        change = changeBuffer.toString();
        return change;
    }

    public static String truncateBidPrice(String bidPrice) {
        if (bidPrice.equalsIgnoreCase("null")) {
            return "null";
        }
        try {
            bidPrice = String.format(defaultLocale, "%.2f", Float.parseFloat(bidPrice));
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Argument: " + e);
        }
        return bidPrice;
    }
}