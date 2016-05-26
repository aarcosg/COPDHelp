package com.aarcosg.copdhelp.ui.adapteritem;

import com.github.mikephil.charting.data.BarEntry;

import java.util.List;


public class MedicalAttentionStackedBarChartItem extends StackedBarChartItem {

    public MedicalAttentionStackedBarChartItem(int type, String header, String progressLabel, Double changePercentage, List<String> xVals, List<BarEntry> yVals, int[] dataSetColors, String[] stackLabels, boolean drawValues, boolean positiveIsBetter) {
        super(type, header, progressLabel, changePercentage, xVals, yVals, dataSetColors, stackLabels, drawValues, positiveIsBetter);
    }
}
