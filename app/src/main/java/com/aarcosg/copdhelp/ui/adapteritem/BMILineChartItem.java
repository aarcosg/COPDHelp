package com.aarcosg.copdhelp.ui.adapteritem;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

public class BMILineChartItem extends LineChartItem {

    public BMILineChartItem(int type, String header, String progressLabel, Double changePercentage, List<String> xVals, List<Entry> yVals, int[] dataSetColors, boolean drawValues, boolean positiveIsBetter) {
        super(type, header, progressLabel, changePercentage, xVals, yVals, dataSetColors, drawValues, positiveIsBetter);
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        viewHolder.lineChart.getLegend().setEnabled(false);
    }
}
