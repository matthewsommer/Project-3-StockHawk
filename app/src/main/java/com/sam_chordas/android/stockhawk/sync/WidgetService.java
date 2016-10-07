package com.sam_chordas.android.stockhawk.sync;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.Contract;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;
            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                data = getContentResolver().query(Contract.QuoteEntry.CONTENT_URI,
                        new String[]{Contract.QuoteEntry.COLUMN_ID, Contract.QuoteEntry.COLUMN_SYMBOL, Contract.QuoteEntry.COLUMN_BIDPRICE,
                                Contract.QuoteEntry.COLUMN_PERCENT_CHANGE, Contract.QuoteEntry.COLUMN_CHANGE, Contract.QuoteEntry.COLUMN_ISUP},
                        Contract.QuoteEntry.COLUMN_ISCURRENT + " = ?",
                        new String[]{"1"},
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.list_item_quote);
                String symbol = data.getString(data.getColumnIndex(Contract.QuoteEntry.COLUMN_SYMBOL));

                views.setTextViewText(R.id.stock_symbol, symbol);
                views.setTextViewText(R.id.bid_price, data.getString(data.getColumnIndex(Contract.QuoteEntry.COLUMN_BIDPRICE)));
                views.setTextViewText(R.id.change, data.getString(data.getColumnIndex(Contract.QuoteEntry.COLUMN_PERCENT_CHANGE)));


                if (data.getInt(data.getColumnIndex(Contract.QuoteEntry.COLUMN_ISUP)) == 1) {
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                } else {
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                }

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(data.getColumnIndexOrThrow(Contract.QuoteEntry.COLUMN_ID));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
