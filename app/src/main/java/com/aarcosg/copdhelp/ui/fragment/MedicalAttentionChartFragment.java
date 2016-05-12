package com.aarcosg.copdhelp.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionChartPresenter;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionChartView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class MedicalAttentionChartFragment extends BaseFragment implements MedicalAttentionChartView {

    private static final String TAG = MedicalAttentionChartFragment.class.getCanonicalName();
    private static final float DEFAULT_WEEK_CHART_AXIS_LEFT_MAX_VALUE = 5f;

    @Inject
    MedicalAttentionChartPresenter mMedicalAttentionChartPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.week_barchart)
    BarChart mWeekBarChart;

    private RealmResults<MedicalAttention> mMedicalAttentions;

    public static MedicalAttentionChartFragment newInstance() {
        MedicalAttentionChartFragment fragment = new MedicalAttentionChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MedicalAttentionChartFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mMedicalAttentionChartPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_medical_attention_chart, container, false);
        ButterKnife.bind(this, fragmentView);
        setupWeekChart();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMedicalAttentionChartPresenter.loadWeekMedicalAttentions();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMedicalAttentionChartPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMedicalAttentions != null){
            mMedicalAttentions.removeChangeListeners();
        }
    }

    @Override
    public void bindWeekMedicalAttentions(RealmResults<MedicalAttention> medicalAttentions) {
        mMedicalAttentions = medicalAttentions;
        mMedicalAttentions.addChangeListener(element -> bindDataWeekChart());
        if(!medicalAttentions.isEmpty()){
            bindDataWeekChart();
        }
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadWeekRealmErrorMessage() {
        Snackbar.make(mProgressBar.getRootView()
                , R.string.medical_attention_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> mMedicalAttentionChartPresenter.loadWeekMedicalAttentions())
                .show();
    }

    private void setupWeekChart(){
        mWeekBarChart.setDescription("");
        mWeekBarChart.setPinchZoom(false);
        mWeekBarChart.setDrawGridBackground(false);
        mWeekBarChart.setDrawBarShadow(false);
        mWeekBarChart.setDrawValueAboveBar(false);
        mWeekBarChart.setDrawGridBackground(false);
        mWeekBarChart.getAxisRight().setEnabled(false);
        mWeekBarChart.getAxisLeft().setAxisMinValue(0f);
        mWeekBarChart.getXAxis().setLabelsToSkip(0);
        mWeekBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mWeekBarChart.getXAxis().setDrawGridLines(false);
        mWeekBarChart.getAxisLeft().setAxisMaxValue(DEFAULT_WEEK_CHART_AXIS_LEFT_MAX_VALUE);
        mWeekBarChart.getAxisLeft().setDrawGridLines(false);

        Legend legend = mWeekBarChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setTextSize(12f);
        legend.setFormSize(8f);
        legend.setFormToTextSpace(4f);
        legend.setXEntrySpace(6f);
    }

    private void bindDataWeekChart(){
        ArrayList<BarEntry> yVals = new ArrayList<>(7);

        // Add Monday to Saturday counters
        for (int dayOfWeek = Calendar.MONTH ; dayOfWeek <= Calendar.SATURDAY ; dayOfWeek++){
            float checkupVal = mMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.DAY_OF_WEEK,dayOfWeek)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_CHECKUP)
                    .count();
            float emergencyVal = mMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.DAY_OF_WEEK,dayOfWeek)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_EMERGENCY)
                    .count();
            yVals.add(new BarEntry(new float[] {checkupVal, emergencyVal}, dayOfWeek - 2));
            if(checkupVal  + emergencyVal > DEFAULT_WEEK_CHART_AXIS_LEFT_MAX_VALUE){
                mWeekBarChart.getAxisLeft().resetAxisMaxValue();
            }
        }

        // Add Sunday counter
        float checkupVal = mMedicalAttentions.where()
                .equalTo(RealmTable.MedicalAttention.DAY_OF_WEEK,Calendar.SUNDAY)
                .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_CHECKUP)
                .count();
        float emergencyVal = mMedicalAttentions.where()
                .equalTo(RealmTable.MedicalAttention.DAY_OF_WEEK,Calendar.SUNDAY)
                .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_EMERGENCY)
                .count();
        yVals.add(new BarEntry(new float[] {checkupVal, emergencyVal}, 6));
        if(checkupVal  + emergencyVal > DEFAULT_WEEK_CHART_AXIS_LEFT_MAX_VALUE){
            mWeekBarChart.getAxisLeft().resetAxisMaxValue();
        }

        BarDataSet dataSet;

        if(mWeekBarChart.getData() != null &&
                mWeekBarChart.getData().getDataSetCount() > 0) {
            dataSet = (BarDataSet)mWeekBarChart.getData().getDataSetByIndex(0);
            dataSet.getYVals().clear();
            dataSet.getYVals().addAll(yVals);
            mWeekBarChart.getData().notifyDataChanged();
            mWeekBarChart.notifyDataSetChanged();
        }else{
            dataSet = new BarDataSet(yVals, "");
            dataSet.setStackLabels(getResources().getStringArray(R.array.medical_attention_type));
            dataSet.setColors(new int[]{
                    ContextCompat.getColor(getContext(),R.color.md_blue_600)
                    ,ContextCompat.getColor(getContext(),R.color.md_deep_orange_600)
            });

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            ArrayList<String> xVals = new ArrayList<>();
            xVals.addAll(Arrays.asList(getResources().getStringArray(R.array.week_days_alt)));

            BarData data = new BarData(xVals, dataSets);
            data.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) ->
                String.valueOf(Math.round(value))
            );
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(12f);

            mWeekBarChart.setData(data);
            mWeekBarChart.invalidate();
        }
    }

}
