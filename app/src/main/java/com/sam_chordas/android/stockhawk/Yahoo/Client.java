package com.sam_chordas.android.stockhawk.Yahoo;

import android.net.Uri;
import android.util.Log;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Matt on 6/25/16.
 */
public class Client {
    public static final String LOG_TAG = Client.class.getSimpleName();
    private static final String BASE_URL = "https://query.yahooapis.com/v1/public/yql?q=";
    private static final String SEARCH_QUERY = "select * from yahoo.finance.quotes where symbol in (";
    private static final String ENCODING = "UTF-8";
    private static final String DEFAULT_STOCKS = "\"YHOO\",\"AAPL\",\"GOOG\",\"MSFT\")";
    private static final String DATA_FORMAT_KEY = "format";
    private static final String DATA_FORMAT_VALUE = "json";
    private static final String DIAGNOSTICS_KEY = "diagnostics";
    private static final String DIAGNOSTICS_VALUE = "true";
    private static final String ENV_KEY = "env";
    private static final String ENV_VALUE = "store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    private static final String CALLBACK_KEY = "callback";

    private static final String QUERY_PARAMS = "&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables."
            + "org%2Falltableswithkeys&callback=";


    public static String FetchStockData() {
        Log.d(LOG_TAG, "Starting sync");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResponseStr = null;

        try {
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(DATA_FORMAT_KEY,DATA_FORMAT_VALUE)
                    .appendQueryParameter(DIAGNOSTICS_KEY,DIAGNOSTICS_VALUE)
                    .appendQueryParameter(ENV_KEY,ENV_VALUE)
                    .appendQueryParameter(CALLBACK_KEY,"") //TODO:Do we need this?
                    .build();
            URL url = new URL(builtUri.toString());
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
            //getWeatherDataFromJson(jsonResponseStr, locationQuery);
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
        return "";
    }
}
