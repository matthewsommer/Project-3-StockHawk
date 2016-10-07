package com.sam_chordas.android.stockhawk.sync;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.sam_chordas.android.stockhawk.Yahoo.YahooDataContract;
import com.sam_chordas.android.stockhawk.Yahoo.QuoteHandler;
import com.sam_chordas.android.stockhawk.model.Quote;
import com.sam_chordas.android.stockhawk.Yahoo.YahooClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RemoteQuotesDataFetcher {

    private Context mContext = null;

    public RemoteQuotesDataFetcher(Context context) {
        mContext = context;
    }

    public static Vector<ContentValues> fetchQuoteData(List<Quote> quoteList) {
        Vector<ContentValues> cVVector = null;
        if (quoteList.size() > 0) {
            Uri stockSearchUri = YahooDataContract.Stocks.buildStockSearchUri(quoteList);
            String responseStr = YahooClient.FetchStockData(stockSearchUri);
            cVVector = QuoteHandler.parseJSON(responseStr);
        }
        return cVVector;
    }
}