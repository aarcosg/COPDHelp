package com.aarcosg.copdhelp.utils;

import android.content.Context;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.chart.StackedBarChartMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;
import java.util.List;

public class ChartUtils {

    public static final int BARCHART_TYPE_DEFAULT = 0;
    public static final int BARCHART_TYPE_WEEK = 1;
    public static final int BARCHART_TYPE_MONTH = 2;
    public static final int BARCHART_TYPE_YEAR = 3;

    public static final float MEDICAL_ATTENTION_WEEK_AXIS_MAX_VALUE = 5;
    public static final float MEDICAL_ATTENTION_MONTH_AXIS_MAX_VALUE = 5;
    public static final float MEDICAL_ATTENTION_YEAR_AXIS_MAX_VALUE = 20;
    
    public static void setupBarChart(Context context, BarChart barChart, int type){
        barChart.setDescription("");
        barChart.setNoDataText(context.getString(R.string.empty_data));
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setMarkerView(new StackedBarChartMarkerView(context, R.layout.stacked_barchart_marker_view));

        YAxis axisRight = barChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMinValue(0f);
        axisLeft.setDrawGridLines(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Legend legend = barChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setTextSize(12f);
        legend.setFormSize(8f);
        legend.setFormToTextSpace(4f);
        legend.setXEntrySpace(6f);

        switch (type){
            case BARCHART_TYPE_WEEK:
                //axisLeft.setAxisMaxValue(MEDICAL_ATTENTION_WEEK_AXIS_MAX_VALUE);
                break;
            case BARCHART_TYPE_MONTH:
                //axisLeft.setAxisMaxValue(MEDICAL_ATTENTION_MONTH_AXIS_MAX_VALUE);
                xAxis.setLabelsToSkip(1);
                break;
            case BARCHART_TYPE_YEAR:
                //axisLeft.setAxisMaxValue(MEDICAL_ATTENTION_YEAR_AXIS_MAX_VALUE);
                xAxis.setLabelsToSkip(1);
                break;
        }
    }

    public static List<String> getMonthXVals(){
        List<String> xVals = new ArrayList<>(31);
        for(int i = 0; i <= 31 ; i++){
            xVals.add(String.valueOf(i));
        }
        return xVals;
    }

}
