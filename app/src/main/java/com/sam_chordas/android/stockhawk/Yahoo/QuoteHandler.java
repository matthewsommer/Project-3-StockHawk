package com.sam_chordas.android.stockhawk.Yahoo;

import android.content.ContentValues;
import android.util.Log;

import com.sam_chordas.android.stockhawk.data.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class QuoteHandler {
    private static final String LOG_TAG = YahooDataContract.Stocks.class.getSimpleName();
    public static Vector<ContentValues> parseJSON(String jsonStr) {
        try {
            JSONObject responseJSON = new JSONObject(jsonStr);
            JSONObject queryJSON = responseJSON.getJSONObject(YahooDataContract.Stocks.QUERY);
            JSONObject resultsJSON = queryJSON.getJSONObject(YahooDataContract.Stocks.RESULTS);
            JSONArray quoteJSONArray = new JSONArray();
            Object quotesJSON = resultsJSON.get(YahooDataContract.Stocks.QUOTE);

            if (quotesJSON instanceof JSONArray) {
                quoteJSONArray = (JSONArray)quotesJSON;
            }
            else if (quotesJSON instanceof JSONObject) {
                quoteJSONArray.put((JSONObject)quotesJSON);
            }

            Vector<ContentValues> cVVector = new Vector<ContentValues>(quoteJSONArray.length());

            for (int i = 0; i < quoteJSONArray.length(); i++) {
                JSONObject stockData = quoteJSONArray.getJSONObject(i);

                String symbol = stockData.getString(YahooDataContract.Stocks.SYMBOL);
                String bid = stockData.getString(YahooDataContract.Stocks.BID);
                String change = stockData.getString(YahooDataContract.Stocks.CHANGE);
                String percentchange = stockData.getString(YahooDataContract.Stocks.PERCENTCHANGE);

                ContentValues stockValues = new ContentValues();
                stockValues.put(Contract.QuoteEntry.COLUMN_SYMBOL, symbol);
                stockValues.put(Contract.QuoteEntry.COLUMN_PERCENT_CHANGE, Utility.truncateChange(percentchange,true));
                stockValues.put(Contract.QuoteEntry.COLUMN_CHANGE, Utility.truncateChange(change,false));
                stockValues.put(Contract.QuoteEntry.COLUMN_BIDPRICE, Utility.truncateBidPrice(bid));
                stockValues.put(Contract.QuoteEntry.COLUMN_CREATED, "");
                stockValues.put(Contract.QuoteEntry.COLUMN_ISUP, 0);
                stockValues.put(Contract.QuoteEntry.COLUMN_ISCURRENT, 1);

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