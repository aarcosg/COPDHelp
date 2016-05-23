package com.aarcosg.copdhelp.ui.adapteritem;

import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class ExerciseStackedBarChartItem extends StackedBarChartItem {

    public ExerciseStackedBarChartItem(int type, String header, String progressLabel, Double changePercentage, List<String> xVals, List<BarEntry> yVals, int[] dataSetColors, String[] stackLabels, boolean drawValues) {
        super(type, header, progressLabel, changePercentage, xVals, yVals, dataSetColors, stackLabels, drawValues);
    }

}