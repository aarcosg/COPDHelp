package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.utils.ChartUtils;
import com.aarcosg.copdhelp.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StackedBarChartItem extends AbstractItem<StackedBarChartItem, StackedBarChartItem.ViewHolder> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    private int mType;
    private String mHeader;
    private List<String> mXVals;
    private List<BarEntry> mYVals;
    private int[] mDataSetColors;
    private String[] mStackLabels;
    private boolean mDrawValues;

    public StackedBarChartItem(int type, String header, List<String> xVals, List<BarEntry> yVals,
                               int[] dataSetColors, String[] stackLabels, boolean drawValues) {
        this.mType = type;
        this.mHeader = header;
        this.mXVals = xVals;
        this.mYVals = yVals;
        this.mDataSetColors = dataSetColors;
        this.mStackLabels = stackLabels;
        this.mDrawValues = drawValues;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_medical_attention_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_card_barchart;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        Context context = viewHolder.itemView.getContext();
        ChartUtils.setupBarChart(context,viewHolder.barChart,mType);

        BarDataSet dataSet = new BarDataSet(mYVals, "");
        dataSet.setStackLabels(mStackLabels);
        dataSet.setColors(mDataSetColors,context);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        BarData data = new BarData(mXVals, dataSets);
        if(mDrawValues){
            data.setValueFormatter(new StackedValueFormatter(true,"",0));
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(context.getResources().getDimension(R.dimen.chart_value_text_size));
        }else{
            data.setDrawValues(mDrawValues);
        }

        viewHolder.barChart.setData(data);
        viewHolder.barChart.animateY(2000);

        viewHolder.headerTv.setText(mHeader);

        this.mTag = viewHolder;
    }

    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }

    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.header_tv)
        TextView headerTv;
        @Bind(R.id.change_percentage_tv)
        TextView changePercentageTv;
        @Bind(R.id.barchart)
        BarChart barChart;
        @Bind(R.id.zoom_chart_btn)
        Button zoomChartBtn;
        @Bind(R.id.share_chart_btn)
        Button shareChartBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setChartYVals(List<BarEntry> yVals){
        ViewHolder viewHolder = (ViewHolder) getTag();
        BarChart barChart = viewHolder.barChart;
        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            this.mYVals = yVals;
            BarDataSet dataSet = (BarDataSet)barChart.getData().getDataSetByIndex(0);
            dataSet.getYVals().clear();
            dataSet.getYVals().addAll(mYVals);
            barChart.getBarData().notifyDataChanged();
            barChart.notifyDataSetChanged();
            barChart.animateY(2000);
        }
    }

    public void setChangePercentage(Double changePercentage) {
        ViewHolder viewHolder = (ViewHolder) getTag();
        String format = "%s%%";
        Context context = viewHolder.itemView.getContext();
        if(changePercentage < 0){
            viewHolder.changePercentageTv.setTextColor(
                    ContextCompat.getColor(context,R.color.change_percentage_negative));
        }else if(changePercentage > 0){
            viewHolder.changePercentageTv.setTextColor(
                    ContextCompat.getColor(context,R.color.change_percentage_positive));
            format = "+%s%%";
        }
        Utils.animateNumberTextView(0,(int)Math.round(changePercentage),viewHolder.changePercentageTv,format);
    }
}