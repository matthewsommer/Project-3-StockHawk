package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.Contract;

public class StockDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG_STOCK_SYMBOL = "STOCK_SYMBOL";
    private static final int STOCKS_LOADER = 1;
    private String symbol = "";
    private LineChartView lineChartView;

    public static Intent getStartActivityIntent(Context context, String symbol) {
        Intent intent = new Intent(context, StockDetailsActivity.class);
        intent.putExtra(StockDetailsActivity.TAG_STOCK_SYMBOL, symbol);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        symbol = getIntent().getStringExtra(TAG_STOCK_SYMBOL);
        setTitle(symbol);
        lineChartView = (LineChartView) findViewById(R.id.linechart);
        getSupportLoaderManager().initLoader(STOCKS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case STOCKS_LOADER:
                return new CursorLoader(this, Contract.QuoteEntry.CONTENT_URI,
                        new String[]{Contract.QuoteEntry.COLUMN_ID, Contract.QuoteEntry.COLUMN_SYMBOL, Contract.QuoteEntry.COLUMN_BIDPRICE,
                                Contract.QuoteEntry.COLUMN_PERCENT_CHANGE, Contract.QuoteEntry.COLUMN_CHANGE, Contract.QuoteEntry.COLUMN_ISUP},
                        Contract.QuoteEntry.COLUMN_SYMBOL + " = ?",
                        new String[]{symbol},
                        null);
        }
        return null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0)
            renderChart(data);

    }

    public void renderChart(Cursor data) {
        LineSet lineSet = new LineSet();
        float minimumPrice = Float.MAX_VALUE;
        float maximumPrice = Float.MIN_VALUE;

        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            String label = data.getString(data.getColumnIndexOrThrow(Contract.QuoteEntry.COLUMN_BIDPRICE));
            if ( label != null && !label.equalsIgnoreCase("null")) {
                float price = Float.parseFloat(label);
                lineSet.addPoint(label, price);
                minimumPrice = Math.min(minimumPrice, price);
                maximumPrice = Math.max(maximumPrice, price);
            }
        }

        lineSet.setColor(Color.parseColor("#758cbb"))
                .setFill(Color.parseColor("#2d374c"))
                .setDotsColor(Color.parseColor("#758cbb"))
                .setThickness(4)
                .setDashed(new float[]{10f, 10f});


        lineChartView.setBorderSpacing(Tools.fromDpToPx(15))
                .setYLabels(AxisController.LabelPosition.OUTSIDE)
                .setXLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#6a84c3"))
                .setXAxis(false)
                .setYAxis(false)
                .setAxisBorderValues(Math.round(Math.max(0f, minimumPrice - 5f)), Math.round(maximumPrice + 5f))
                .addData(lineSet);

        Animation anim = new Animation();

        if (lineSet.size() > 1)
            lineChartView.show(anim);
        else
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
    }
}