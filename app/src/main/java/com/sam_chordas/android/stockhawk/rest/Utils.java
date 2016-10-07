package com.sam_chordas.android.stockhawk.rest;

import java.util.Locale;

//
//import android.util.Log;
//
//import com.sam_chordas.android.stockhawk.data.YahooDataContract;
//import com.sam_chordas.android.stockhawk.data.Provider;
//
//import java.util.ArrayList;
//import java.util.Locale;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by sam_chordas on 10/8/15.
// */
public class Utils {
    private static String LOG_TAG = Utils.class.getSimpleName();
    private static Locale defaultLocale = Locale.getDefault();
    public static boolean showPercent = true;
    private static final String CHANGE = "Change";
    private static final String SYMBOL = "symbol";
    private static final String BID = "Bid";
    private static final String CHANGE_IN_PERCENT = "ChangeinPercent";
//
//    public static ArrayList quoteJsonToContentVals(String JSON) {
//        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
//        JSONObject jsonObject = null;
//        JSONArray resultsArray = null;
//        try {
//            jsonObject = new JSONObject(JSON);
//            if (jsonObject != null && jsonObject.length() != 0) {
//                jsonObject = jsonObject.getJSONObject("query");
//                int count = Integer.parseInt(jsonObject.getString("count"));
//                if (count == 1) {
//                    jsonObject = jsonObject.getJSONObject("results")
//                            .getJSONObject("quote");
//                    batchOperations.add(buildBatchOperation(jsonObject));
//                } else {
//                    resultsArray = jsonObject.getJSONObject("results").getJSONArray("quote");
//
//                    if (resultsArray != null && resultsArray.length() != 0) {
//                        for (int i = 0; i < resultsArray.length(); i++) {
//                            jsonObject = resultsArray.getJSONObject(i);
//                            batchOperations.add(buildBatchOperation(jsonObject));
//                        }
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, "String to JSON failed: " + e);
//        }
//        return batchOperations;
//    }
//
    public static String truncateChange(String change, boolean isPercentChange) {
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
        try {
            bidPrice = String.format(defaultLocale, "%.2f", Float.parseFloat(bidPrice));
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Argument: " + e);
        }
        return bidPrice;
    }

//    public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject) {
//     //   ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
//          //      YahooDataContract.QuoteEntry.CONTENT_URI);
////        try {
////
////
////            String bid = jsonObject.getString(BID);
////            String percent_change =jsonObject.getString(CHANGE_IN_PERCENT);
////
////            String change = jsonObject.getString(CHANGE);
////            builder.withValue(YahooDataContract.QuoteEntry.COLUMN_SYMBOL, jsonObject.getString(SYMBOL));
////            if(bid != "null") {
////                builder.withValue(YahooDataContract.QuoteEntry.COLUMN_BIDPRICE, truncateBidPrice(bid));
////            }
////            if(percent_change != "null"){
////                builder.withValue(YahooDataContract.QuoteEntry.COLUMN_PERCENT_CHANGE, truncateChange(
////                        percent_change, true));
////            }
////            if(change != "null"){
////                builder.withValue(YahooDataContract.QuoteEntry.COLUMN_CHANGE, truncateChange(change, false));
////            }
////            builder.withValue(YahooDataContract.QuoteEntry.COLUMN_ISCURRENT, 1);
////            if (change.charAt(0) == '-') {
////                builder.withValue(YahooDataContract.QuoteEntry.COLUMN_ISUP, 0);
////            } else {
////                builder.withValue(YahooDataContract.QuoteEntry.COLUMN_ISUP, 1);
////            }
////
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//    //    return builder.build();
//        return null;
//    }
}