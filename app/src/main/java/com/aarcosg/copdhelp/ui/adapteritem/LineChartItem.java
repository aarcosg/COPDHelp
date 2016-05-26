package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.utils.ChartUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LineChartItem extends AbstractItem<LineChartItem, LineChartItem.ViewHolder> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    private int mType;
    private String mHeader;
    private String mProgressLabel;
    private Double mChangePercentage;
    private List<String> mXVals;
    private List<Entry> mYVals;
    private int[] mDataSetColors;
    private boolean mDrawValues;
    private boolean mPositiveIsBetter;

    public LineChartItem(int type, String header, String progressLabel, Double changePercentage, List<String> xVals, List<Entry> yVals,
                         int[] dataSetColors, boolean drawValues, boolean positiveIsBetter) {
        this.mType = type;
        this.mHeader = header;
        this.mProgressLabel = progressLabel;
        this.mChangePercentage = changePercentage;
        this.mXVals = xVals;
        this.mYVals = yVals;
        this.mDataSetColors = dataSetColors;
        this.mDrawValues = drawValues;
        this.mPositiveIsBetter = positiveIsBetter;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_bmi_chart_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_card_linechart;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        Context context = viewHolder.itemView.getContext();
        LineChart lineChart = viewHolder.lineChart;
        ChartUtils.setupLineChart(context,lineChart,mType);
        setupLineLimits(context, lineChart);
        LineDataSet dataSet = new LineDataSet(mYVals, "");
        dataSet.setColors(mDataSetColors);
        dataSet.setCircleColors(mDataSetColors);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleColorHole(mDataSetColors[0]);
        dataSet.setCircleRadius(3f);
        dataSet.setLineWidth(2f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData data = new LineData(mXVals, dataSets);
        if(mDrawValues){
            data.setValueTextSize(context.getResources().getDimension(R.dimen.chart_value_text_size));
        }else{
            data.setDrawValues(false);
        }

        lineChart.setData(data);
        lineChart.animateX(2000);

        viewHolder.headerTv.setText(mHeader);
        viewHolder.progressLblTv.setText(mProgressLabel);

        String format = "%s%%";
        if(mChangePercentage < 0){
            viewHolder.changePercentageTv.setTextColor(
                    ContextCompat.getColor(context,
                            mPositiveIsBetter ? R.color.change_percentage_positive : R.color.change_percentage_negative));
        }else if(mChangePercentage > 0){
            viewHolder.changePercentageTv.setTextColor(
                    ContextCompat.getColor(context,
                            mPositiveIsBetter ? R.color.change_percentage_positive : R.color.change_percentage_negative));
            format = "+%s%%";
        }
        viewHolder.changePercentageTv.setText(String.format(format,Math.round(mChangePercentage)));

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.container_card)
        public CardView containerCard;
        @Bind(R.id.header_tv)
        TextView headerTv;
        @Bind(R.id.change_percentage_lbl_tv)
        TextView progressLblTv;
        @Bind(R.id.change_percentage_tv)
        TextView changePercentageTv;
        @Bind(R.id.linechart)
        LineChart lineChart;
        @Bind(R.id.zoom_chart_btn)
        Button zoomChartBtn;
        @Bind(R.id.share_chart_btn)
        public Button shareChartBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setupLineLimits(Context context, LineChart lineChart) {
        LimitLine upperLimitLine = new LimitLine(30f, context.getString(R.string.bmi_state_obesity));
        upperLimitLine.setLineWidth(2f);
        upperLimitLine.enableDashedLine(10f, 10f, 0f);
        upperLimitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLimitLine.setTextSize(10f);

        LimitLine lowerLimitLine = new LimitLine(21f, context.getString(R.string.bmi_state_thinness));
        lowerLimitLine.setLineWidth(2f);
        lowerLimitLine.enableDashedLine(10f, 10f, 0f);
        lowerLimitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lowerLimitLine.setTextSize(10f);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upperLimitLine);
        leftAxis.addLimitLine(lowerLimitLine);
    }

    public void setChartYVals(List<Entry> yVals){
        this.mYVals = yVals;
    }

    public void setChangePercentage(Double changePercentage) {
        this.mChangePercentage = changePercentage;
    }
}