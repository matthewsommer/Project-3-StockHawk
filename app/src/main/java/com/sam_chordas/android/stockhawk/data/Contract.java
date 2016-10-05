package com.sam_chordas.android.stockhawk.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.sam_chordas.android.stockhawk";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String CONTENT_TYPE_APP_BASE = "sam_chordas.android.";

    //Mime type for google denoting this resolves to multiple objects
    public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd."
            + CONTENT_TYPE_APP_BASE;

    //Mime type for google denoting that a single record will be retrieved
    public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd."
            + CONTENT_TYPE_APP_BASE;

    public interface Paths {
        String QUOTE = "quotes";
    }

    public static String makeContentType(String id) {
        if (id != null) {
            return CONTENT_TYPE_BASE + id;
        } else {
            return null;
        }
    }

    public static String makeContentItemType(String id) {
        if (id != null) {
            return CONTENT_ITEM_TYPE_BASE + id;
        } else {
            return null;
        }
    }

    interface QuoteColumns {
        String COLUMN_ID = "_id";
        String COLUMN_SYMBOL = "symbol";
        String COLUMN_PERCENT_CHANGE = "percent_change";
        String COLUMN_CHANGE = "change";
        String COLUMN_BIDPRICE = "bid_price";
        String COLUMN_CREATED = "created";
        String COLUMN_ISUP = "is_up";
        String COLUMN_ISCURRENT = "is_current";
    }

    public static final class QuoteEntry implements QuoteColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(Paths.QUOTE).build();
        public static final String CONTENT_TYPE_ID = Paths.QUOTE;

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public static long getTaskIdFromUri(Uri uri) {
            return Long.valueOf(uri.getPathSegments().get(1));
        }
    }
}