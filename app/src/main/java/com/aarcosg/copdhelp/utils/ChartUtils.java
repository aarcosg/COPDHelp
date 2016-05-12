package com.aarcosg.copdhelp.utils;

import android.content.Context;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.CustomMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartUtils {
    
    public static void setupDefaultBarChart(Context context, BarChart barChart){
        barChart.setDescription("");
        barChart.setNoDataText(context.getString(R.string.empty_data));
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setMarkerView(new CustomMarkerView(context, R.layout.custom_marker_view));

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
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setTextSize(12f);
        legend.setFormSize(8f);
        legend.setFormToTextSpace(4f);
        legend.setXEntrySpace(6f);
    }

    public static void addDataToBarChart(
            Context context
            , BarChart barChart
            , String[] xVals
            , List<BarEntry> yVals
            , int[] dataSetColors
            , String[] stackLabels){
        BarDataSet dataSet;
        if(barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            dataSet = (BarDataSet)barChart.getData().getDataSetByIndex(0);
            dataSet.getYVals().clear();
            dataSet.getYVals().addAll(yVals);
            barChart.getData().notifyDataChanged();
        }else{
            dataSet = new BarDataSet(yVals, "");
            dataSet.setStackLabels(stackLabels);
            dataSet.setColors(dataSetColors,context);

            List<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            BarData data = new BarData(xVals, dataSets);
            data.setDrawValues(false);
            /*data.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) ->
                    String.valueOf(Math.round(value))
            );
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(context.getResources().getDimension(R.dimen.chart_value_text_size));*/

            barChart.setData(data);
            barChart.animateY(2000);
        }
    }
}
