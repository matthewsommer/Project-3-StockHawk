package com.sam_chordas.android.stockhawk.Yahoo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Quote implements Parcelable {
    private String id;
    private String symbol;
    private String percent_change;
    private String change;
    private String bid_price;
    private String created;
    private String is_up;
    private String is_current;

    public Quote(String id, String symbol) {
        setId(id);
        setSymbol(symbol);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getId());
        out.writeString(getSymbol());
    }

    public static final Parcelable.Creator<Quote> CREATOR
            = new Parcelable.Creator<Quote>() {
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    private Quote(Parcel in) {
        setId(in.readString());
        setSymbol(in.readString());
    }

    public List<Quote> FetchRemoteStocks() {
        return null;
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

    public String getPercent_change() {
        return percent_change;
    }

    public void setPercent_change(String percent_change) {
        this.percent_change = percent_change;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getBid_price() {
        return bid_price;
    }

    public void setBid_price(String bid_price) {
        this.bid_price = bid_price;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getIs_up() {
        return is_up;
    }

    public void setIs_up(String is_up) {
        this.is_up = is_up;
    }

    public String getIs_current() {
        return is_current;
    }

    public void setIs_current(String is_current) {
        this.is_current = is_current;
    }
}