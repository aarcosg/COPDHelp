package com.aarcosg.copdhelp.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.aarcosg.copdhelp.utils.ChartUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class MedicalAttentionChartFragment extends BaseFragment implements MedicalAttentionChartView {

    private static final String TAG = MedicalAttentionChartFragment.class.getCanonicalName();
    private static final float DEFAULT_WEEK_MONTH_CHART_AXIS_LEFT_MAX_VALUE = 5f;
    private static final float DEFAULT_YEAR_CHART_AXIS_LEFT_MAX_VALUE = 15f;

    @Inject
    MedicalAttentionChartPresenter mMedicalAttentionChartPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.week_barchart)
    BarChart mWeekBarChart;
    @Bind(R.id.month_barchart)
    BarChart mMonthBarChart;
    @Bind(R.id.year_barchart)
    BarChart mYearBarChart;

    private RealmResults<MedicalAttention> mWeekMedicalAttentions;
    private RealmResults<MedicalAttention> mMonthMedicalAttentions;
    private RealmResults<MedicalAttention> mYearMedicalAttentions;

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
        setupMonthChart();
        setupYearChart();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMedicalAttentionChartPresenter.loadWeekMedicalAttentions();
        mMedicalAttentionChartPresenter.loadMonthMedicalAttentions();
        mMedicalAttentionChartPresenter.loadYearMedicalAttentions();
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
        if (mWeekMedicalAttentions != null) {
            mWeekMedicalAttentions.removeChangeListeners();
        }
        if (mMonthMedicalAttentions != null) {
            mMonthMedicalAttentions.removeChangeListeners();
        }
        if (mYearMedicalAttentions != null) {
            mYearMedicalAttentions.removeChangeListeners();
        }
    }

    @Override
    public void bindWeekMedicalAttentions(RealmResults<MedicalAttention> medicalAttentions) {
        mWeekMedicalAttentions = medicalAttentions;
        mWeekMedicalAttentions.addChangeListener(element -> bindDataWeekChart());
        if (!medicalAttentions.isEmpty()) {
            bindDataWeekChart();
        }
    }

    @Override
    public void bindMonthMedicalAttentions(RealmResults<MedicalAttention> medicalAttentions) {
        mMonthMedicalAttentions = medicalAttentions;
        mMonthMedicalAttentions.addChangeListener(element -> bindDataMonthChart());
        if (!medicalAttentions.isEmpty()) {
            bindDataMonthChart();
        }
    }

    @Override
    public void bindYearMedicalAttentions(RealmResults<MedicalAttention> medicalAttentions) {
        mYearMedicalAttentions = medicalAttentions;
        mYearMedicalAttentions.addChangeListener(element -> bindDataYearChart());
        if (!medicalAttentions.isEmpty()) {
            bindDataYearChart();
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

    private void setupWeekChart() {
        ChartUtils.setupDefaultBarChart(getContext(), mWeekBarChart);
        mWeekBarChart.getAxisLeft().setAxisMaxValue(DEFAULT_WEEK_MONTH_CHART_AXIS_LEFT_MAX_VALUE);
    }

    private void setupMonthChart() {
        ChartUtils.setupDefaultBarChart(getContext(), mMonthBarChart);
        mMonthBarChart.getAxisLeft().setAxisMaxValue(DEFAULT_WEEK_MONTH_CHART_AXIS_LEFT_MAX_VALUE);
        mMonthBarChart.getXAxis().setLabelsToSkip(1);
    }

    private void setupYearChart() {
        ChartUtils.setupDefaultBarChart(getContext(), mYearBarChart);
        mYearBarChart.getAxisLeft().setAxisMaxValue(DEFAULT_YEAR_CHART_AXIS_LEFT_MAX_VALUE);
        mYearBarChart.getXAxis().setLabelsToSkip(1);
    }

    private void bindDataWeekChart() {
        List<BarEntry> yVals = new LinkedList<>(Arrays.asList(new BarEntry[7]));

        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
            float checkupVal = mWeekMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.DAY_OF_WEEK, dayOfWeek)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_CHECKUP)
                    .count();
            float emergencyVal = mWeekMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.DAY_OF_WEEK, dayOfWeek)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_EMERGENCY)
                    .count();
            if (checkupVal + emergencyVal > DEFAULT_WEEK_MONTH_CHART_AXIS_LEFT_MAX_VALUE) {
                mWeekBarChart.getAxisLeft().resetAxisMaxValue();
            }
            if (dayOfWeek == Calendar.SUNDAY) {
                yVals.set(6, new BarEntry(new float[]{checkupVal, emergencyVal}, 6));
            } else {
                yVals.set(dayOfWeek - 2, new BarEntry(new float[]{checkupVal, emergencyVal}, dayOfWeek - 2));
            }
        }

        ChartUtils.addDataToBarChart(
                getContext()
                , mWeekBarChart
                , getResources().getStringArray(R.array.week_days_alt)
                , yVals
                , new int[]{R.color.md_blue_600, R.color.md_deep_orange_600}
                , getResources().getStringArray(R.array.medical_attention_type)
        );

    }

    private void bindDataMonthChart() {
        List<BarEntry> yVals = new ArrayList<>(31);
        List<String> xVals = new ArrayList<>(31);

        for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
            float checkupVal = mMonthMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.DAY, dayOfMonth)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_CHECKUP)
                    .count();
            float emergencyVal = mMonthMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.DAY, dayOfMonth)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_EMERGENCY)
                    .count();
            if (checkupVal + emergencyVal > DEFAULT_WEEK_MONTH_CHART_AXIS_LEFT_MAX_VALUE) {
                mMonthBarChart.getAxisLeft().resetAxisMaxValue();
            }
            yVals.add(new BarEntry(new float[]{checkupVal, emergencyVal}, dayOfMonth - 1));
            xVals.add(String.valueOf(dayOfMonth));
        }

        ChartUtils.addDataToBarChart(
                getContext()
                , mMonthBarChart
                , xVals.toArray(new String[0])
                , yVals
                , new int[]{R.color.md_blue_600, R.color.md_deep_orange_600}
                , getResources().getStringArray(R.array.medical_attention_type)
        );
    }

    private void bindDataYearChart() {
        List<BarEntry> yVals = new ArrayList<>(12);

        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
            float checkupVal = mYearMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.MONTH, month)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_CHECKUP)
                    .count();
            float emergencyVal = mYearMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.MONTH, month)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_EMERGENCY)
                    .count();
            if (checkupVal + emergencyVal > DEFAULT_YEAR_CHART_AXIS_LEFT_MAX_VALUE) {
                mYearBarChart.getAxisLeft().resetAxisMaxValue();
            }
            yVals.add(new BarEntry(new float[]{checkupVal, emergencyVal}, month));
        }

        ChartUtils.addDataToBarChart(
                getContext()
                , mYearBarChart
                , getResources().getStringArray(R.array.months_alt)
                , yVals
                , new int[]{R.color.md_blue_600, R.color.md_deep_orange_600}
                , getResources().getStringArray(R.array.medical_attention_type)
        );
    }

}
