package com.aarcosg.copdhelp.utils;

import android.content.Context;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.chart.DoubleMarkerView;
import com.aarcosg.copdhelp.ui.chart.IntegerMarkerView;
import com.aarcosg.copdhelp.ui.chart.StackedBarChartMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;
import java.util.List;

public class ChartUtils {

    public static final int CHART_TYPE_DEFAULT = 0;
    public static final int CHART_TYPE_WEEK = 1;
    public static final int CHART_TYPE_MONTH = 2;
    public static final int CHART_TYPE_YEAR = 3;
    
    public static void setupStackedBarChart(Context context, BarChart barChart, int type){
        barChart.setDescription("");
        barChart.setNoDataText(context.getString(R.string.empty_data));
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setMarkerView(new StackedBarChartMarkerView(context, R.layout.marker_view));

        YAxis axisRight = barChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMinValue(0f);
        axisLeft.setDrawGridLines(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Legend legend = barChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setTextSize(12f);
        legend.setFormSize(8f);
        legend.setFormToTextSpace(4f);
        legend.setXEntrySpace(6f);

        switch (type){
            case CHART_TYPE_WEEK:
                break;
            case CHART_TYPE_MONTH:
                xAxis.setLabelsToSkip(1);
                break;
            case CHART_TYPE_YEAR:
                xAxis.setLabelsToSkip(1);
                break;
        }
    }

    public static void setupLineChart(Context context, LineChart lineChart, int type){
        lineChart.setDescription("");
        lineChart.setNoDataText(context.getString(R.string.empty_data));
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setMarkerView(new DoubleMarkerView(context, R.layout.marker_view));

        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        YAxis axisLeft = lineChart.getAxisLeft();
        axisLeft.setAxisMinValue(0f);
        axisLeft.setDrawGridLines(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Legend legend = lineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setTextSize(12f);
        legend.setFormSize(8f);
        legend.setFormToTextSpace(4f);
        legend.setXEntrySpace(6f);

        switch (type){
            case CHART_TYPE_WEEK:
                break;
            case CHART_TYPE_MONTH:
                xAxis.setLabelsToSkip(1);
                break;
            case CHART_TYPE_YEAR:
                xAxis.setLabelsToSkip(1);
                break;
        }
    }

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
        barChart.setMarkerView(new IntegerMarkerView(context, R.layout.marker_view));

        YAxis axisRight = barChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMinValue(0f);
        axisLeft.setDrawGridLines(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Legend legend = barChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setTextSize(12f);
        legend.setFormSize(8f);
        legend.setFormToTextSpace(4f);
        legend.setXEntrySpace(6f);

        switch (type){
            case CHART_TYPE_WEEK:
                break;
            case CHART_TYPE_MONTH:
                xAxis.setLabelsToSkip(1);
                break;
            case CHART_TYPE_YEAR:
                xAxis.setLabelsToSkip(1);
                break;
        }
    }

    public static List<String> getMonthXVals(){
        List<String> xVals = new ArrayList<>(31);
        for(int i = 1; i <= 31 ; i++){
            xVals.add(String.valueOf(i));
        }
        return xVals;
    }

}
