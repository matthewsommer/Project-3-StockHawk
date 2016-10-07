package com.sam_chordas.android.stockhawk.data;

public enum ProviderUriEnum {

    QUOTE(100, Contract.Paths.QUOTE, Contract.QuoteEntry.CONTENT_TYPE_ID, false, DbHelper.Tables.QUOTE),
    QUOTE_WITH_ID(101, Contract.Paths.QUOTE + "/*", Contract.QuoteEntry.CONTENT_TYPE_ID, true, DbHelper.Tables.QUOTE);

    public int code;

    public String path;

    public String contentType;

    public String table_name;

    ProviderUriEnum(int code, String path, String contentTypeId, boolean singleItem, String table) {
        this.code = code;
        this.path = path;
        this.contentType = singleItem ? Contract.makeContentItemType(contentTypeId)
                : Contract.makeContentType(contentTypeId);
        this.table_name = table;
    }
}