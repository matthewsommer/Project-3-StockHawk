package com.sam_chordas.android.stockhawk.Yahoo;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.Vector;
import com.sam_chordas.android.stockhawk.data.Contract.*;

public class Contract {
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

        private static final String QUERY = "query";
        private static final String RESULTS = "results";
        private static final String QUOTE = "quote";
        private static final String SYMBOL = "symbol";
        private static final String BID = "Bid";
        private static final String CHANGE = "Change";
        private static final String PERCENTCHANGE = "PercentChange";

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

        public static Vector<ContentValues> parseJSON(String jsonStr) {
            try {
                JSONObject responseJSON = new JSONObject(jsonStr);
                JSONObject queryJSON = responseJSON.getJSONObject(QUERY);
                JSONObject resultsJSON = queryJSON.getJSONObject(RESULTS);
                JSONArray quoteJSONArray = resultsJSON.getJSONArray(QUOTE);

                Vector<ContentValues> cVVector = new Vector<ContentValues>(quoteJSONArray.length());

                for (int i = 0; i < quoteJSONArray.length(); i++) {
                    JSONObject stockData = quoteJSONArray.getJSONObject(i);

                    String symbol = stockData.getString(SYMBOL);
                    String bid = stockData.getString(BID);
                    String change = stockData.getString(CHANGE);
                    String percentchange = stockData.getString(PERCENTCHANGE);

                    ContentValues stockValues = new ContentValues();
                    stockValues.put(QuoteEntry.COLUMN_SYMBOL, symbol);
                    stockValues.put(QuoteEntry.COLUMN_BIDPRICE, bid);
                    stockValues.put(QuoteEntry.COLUMN_CHANGE, change);
                    stockValues.put(QuoteEntry.COLUMN_PERCENT_CHANGE, percentchange);

                    cVVector.add(stockValues);
                }
                return cVVector;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
    }
}