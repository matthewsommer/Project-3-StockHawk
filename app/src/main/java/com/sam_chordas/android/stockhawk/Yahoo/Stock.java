package com.sam_chordas.android.stockhawk.Yahoo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Stock implements Parcelable {
    private String id;
    private String symbol;
    private String percent_change;
    private String change;
    private String bid_price;
    private String created;
    private String is_up;
    private String is_current;

    public Stock(String id) {
        this.id = id;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
    }

    public static final Parcelable.Creator<Stock> CREATOR
            = new Parcelable.Creator<Stock>() {
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    private Stock(Parcel in) {
        id = in.readString();
    }

    public List<Stock> FetchRemoteStocks() {
        String responseStr = Client.FetchStockData();
        return Utility.ParseResponseStr(responseStr);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}