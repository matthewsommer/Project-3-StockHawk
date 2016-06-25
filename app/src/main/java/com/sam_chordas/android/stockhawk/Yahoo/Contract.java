package com.sam_chordas.android.stockhawk.Yahoo;

import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by Matt on 6/25/16.
 */
public class Contract {
    public static final class Stocks {
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

        public static Uri buildStockSearchUri(String stocksString) {
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SEARCH_KEY,SEARCH_VALUE + stocksString)
                    .appendQueryParameter(DATA_FORMAT_KEY,DATA_FORMAT_VALUE)
                    .appendQueryParameter(DIAGNOSTICS_KEY,DIAGNOSTICS_VALUE)
                    .appendQueryParameter(ENV_KEY,ENV_VALUE)
                    .appendQueryParameter(CALLBACK_KEY,"") //TODO:Do we need this?
                    .build();
            return builtUri;
        }
    }
}