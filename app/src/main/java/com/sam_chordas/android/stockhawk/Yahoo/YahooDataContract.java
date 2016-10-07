package com.sam_chordas.android.stockhawk.Yahoo;

import android.net.Uri;

import java.util.List;

import com.sam_chordas.android.stockhawk.model.Quote;

public class YahooDataContract {
    public static final class Stocks {
        private static final String LOG_TAG = Stocks.class.getSimpleName();
        private static final String BASE_URL = "https://query.yahooapis.com/v1/public/yql";
        private static final String SEARCH_KEY = "q";
        private static final String SEARCH_VALUE = "select * from yahoo.finance.quotes where symbol "
                + "in (";
        private static final String DATA_FORMAT_KEY = "format";
        private static final String DATA_FORMAT_VALUE = "json";
        private static final String DIAGNOSTICS_KEY = "diagnostics";
        private static final String DIAGNOSTICS_VALUE = "true";
        private static final String ENV_KEY = "env";
        private static final String ENV_VALUE = "store://datatables.org/alltableswithkeys";
        private static final String CALLBACK_KEY = "callback";

        static final String QUERY = "query";
        static final String RESULTS = "results";
        static final String QUOTE = "quote";
        static final String SYMBOL = "symbol";
        static final String BID = "Bid";
        static final String CHANGE = "Change";
        static final String PERCENTCHANGE = "PercentChange";

        public static Uri buildStockSearchUri(List<Quote> quoteList) {
            StringBuilder stocksString = new StringBuilder();

            for (Quote quote : quoteList) {
                stocksString.append("\"" + quote.getSymbol() + "\",");
            }
            stocksString.replace(stocksString.length() - 1, stocksString.length(), ")");

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SEARCH_KEY, SEARCH_VALUE + stocksString)
                    .appendQueryParameter(DATA_FORMAT_KEY, DATA_FORMAT_VALUE)
                    .appendQueryParameter(DIAGNOSTICS_KEY, DIAGNOSTICS_VALUE)
                    .appendQueryParameter(ENV_KEY, ENV_VALUE)
                    .build();
            return builtUri;
        }
    }
}