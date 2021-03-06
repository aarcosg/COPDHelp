package com.aarcosg.copdhelp.ui.chart;

import android.content.Context;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class StackedBarChartMarkerView extends MarkerView {

    private TextView mTextView;

    public StackedBarChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mTextView = (TextView) findViewById(R.id.marker_tv);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mTextView.setText(String.valueOf(Math.round(highlight.getRange().to - highlight.getRange().from)));
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
}