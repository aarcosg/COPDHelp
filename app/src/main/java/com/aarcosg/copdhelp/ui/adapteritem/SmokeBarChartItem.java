package com.aarcosg.copdhelp.ui.adapteritem;

import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class SmokeBarChartItem extends BarChartItem {

    public SmokeBarChartItem(int type, String header, String progressLabel, Double changePercentage, List<String> xVals, List<BarEntry> yVals, int[] dataSetColors, boolean drawValues, boolean positiveIsBetter) {
        super(type, header, progressLabel, changePercentage, xVals, yVals, dataSetColors, drawValues, positiveIsBetter);
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        viewHolder.barChart.getLegend().setEnabled(false);
    }
}
