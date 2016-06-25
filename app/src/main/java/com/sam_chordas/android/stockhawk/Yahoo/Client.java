package com.sam_chordas.android.stockhawk.Yahoo;

import android.net.Uri;
import android.util.Log;
import android.util.StringBuilderPrinter;

import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class Client {
    public static final String LOG_TAG = Client.class.getSimpleName();

    public static String FetchStockData(List<Stock> stockList) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResponseStr = null;
        StringBuilder stocksString = new StringBuilder();

        for (Stock stock : stockList) {
            stocksString.append("\"" + stock.getSymbol() + "\",");
        }
        stocksString.replace(stocksString.length() - 1, stocksString.length(), ")");

        try {
            Contract.Stocks.buildStockSearchUri(stocksString.toString());
            URL url = new URL(Contract.Stocks.buildStockSearchUri(stocksString.toString()).toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return "";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return "";
            }
            jsonResponseStr = buffer.toString();
            return jsonResponseStr;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }
}