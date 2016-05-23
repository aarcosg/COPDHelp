package com.aarcosg.copdhelp.ui.fragment.bmi;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIChartPresenter;
import com.aarcosg.copdhelp.mvp.view.bmi.BMIChartView;
import com.aarcosg.copdhelp.ui.adapteritem.BMILineChartItem;
import com.aarcosg.copdhelp.ui.decorator.VerticalSpaceItemDecoration;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.aarcosg.copdhelp.utils.ChartUtils;
import com.aarcosg.copdhelp.utils.Utils;
import com.github.mikephil.charting.data.Entry;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.helpers.ClickListenerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class BMIChartFragment extends BaseFragment implements BMIChartView {

    private static final String TAG = BMIChartFragment.class.getCanonicalName();

    @Inject
    BMIChartPresenter mBMIChartPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Calendar mCalendar;
    private FastItemAdapter<BMILineChartItem> mFastItemAdapter;
    private RealmResults<BMI> mWeekBMIs;
    private RealmResults<BMI> mMonthBMIs;
    private RealmResults<BMI> mYearBMIs;

    public static BMIChartFragment newInstance() {
        BMIChartFragment fragment = new BMIChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BMIChartFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mBMIChartPresenter.setView(this);
        mCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_bmi_chart, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        mBMIChartPresenter.onCreateView();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadChartsData();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBMIChartPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWeekBMIs != null) {
            mWeekBMIs.removeChangeListeners();
        }
        if (mMonthBMIs != null) {
            mMonthBMIs.removeChangeListeners();
        }
        if (mYearBMIs != null) {
            mYearBMIs.removeChangeListeners();
        }
    }

    @Override
    public void bindWeekData(RealmResults<BMI> BMIs) {
        mWeekBMIs = BMIs;
        mWeekBMIs.addChangeListener(element -> bindWeekData());
        if (!mWeekBMIs.isEmpty()) {
            bindWeekData();
        }
    }

    @Override
    public void bindMonthData(RealmResults<BMI> BMIs) {
        mMonthBMIs = BMIs;
        mMonthBMIs.addChangeListener(element -> bindMonthData());
        if (!mMonthBMIs.isEmpty()) {
            bindMonthData();
        }
    }

    @Override
    public void bindYearData(RealmResults<BMI> BMIs) {
        mYearBMIs = BMIs;
        mYearBMIs.addChangeListener(element -> bindYearData());
        if (!mYearBMIs.isEmpty()) {
            bindYearData();
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
    public void showLoadRealmErrorMessage() {
        Snackbar.make(mProgressBar.getRootView()
                , R.string.bmi_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> loadChartsData())
                .show();
    }

    private void setupAdapter() {
        mFastItemAdapter = new FastItemAdapter<>();
        int[] dataSetColors = new int[]{ContextCompat.getColor(getContext(),R.color.md_orange_500)};

        ClickListenerHelper<BMILineChartItem> clickListenerHelper = new ClickListenerHelper<BMILineChartItem>(mFastItemAdapter);
        mFastItemAdapter.withOnCreateViewHolderListener(new FastAdapter.OnCreateViewHolderListener() {

            @Override
            public RecyclerView.ViewHolder onPreCreateViewHolder(ViewGroup parent, int viewType) {
                return mFastItemAdapter.getTypeInstance(viewType).getViewHolder(parent);
            }

            @Override
            public RecyclerView.ViewHolder onPostCreateViewHolder(RecyclerView.ViewHolder viewHolder) {
                clickListenerHelper.listen(viewHolder,((BMILineChartItem.ViewHolder) viewHolder).shareChartBtn, (v, position, item) -> {
                    Utils.shareView(getContext(),((BMILineChartItem.ViewHolder) viewHolder).containerCard);
                });
                return viewHolder;
            }

        });

        mFastItemAdapter.add(
                new BMILineChartItem(
                        ChartUtils.CHART_TYPE_WEEK
                        , getString(R.string.this_week)
                        , getString(R.string.weekly_progress)
                        , 0.0
                        , Arrays.asList(getResources().getStringArray(R.array.week_days_alt))
                        , new ArrayList<>(7)
                        , dataSetColors
                        , false)
                , new BMILineChartItem(
                        ChartUtils.CHART_TYPE_MONTH
                        , getString(R.string.this_month)
                        , getString(R.string.monthly_progress)
                        , 0.0
                        , ChartUtils.getMonthXVals()
                        , new ArrayList<>(31)
                        , dataSetColors
                        , false)
                , new BMILineChartItem(
                        ChartUtils.CHART_TYPE_YEAR
                        , getString(R.string.this_year)
                        , getString(R.string.yearly_progress)
                        , 0.0
                        , Arrays.asList(getResources().getStringArray(R.array.months_alt))
                        , new ArrayList<>(12)
                        , dataSetColors
                        , false)
        );
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                getResources().getDimension(R.dimen.activity_vertical_margin)));
        mRecyclerView.setAdapter(mFastItemAdapter);
    }

    private void loadChartsData(){
        mBMIChartPresenter.loadWeekData();
        mBMIChartPresenter.loadMonthData();
        mBMIChartPresenter.loadYearData();
    }

    private void bindWeekData() {
        Double changePercentage = Utils.getAveragePercentageChange(
                Calendar.WEEK_OF_YEAR,RealmTable.BMI.BMI,mWeekBMIs);
        List<Entry> yVals = new ArrayList<>(7);
        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
            float bmiAvg = (float) mWeekBMIs.where()
                    .equalTo(RealmTable.BMI.WEEK_OF_YEAR, mCalendar.get(Calendar.WEEK_OF_YEAR))
                    .equalTo(RealmTable.BMI.DAY_OF_WEEK, dayOfWeek)
                    .average(RealmTable.BMI.BMI);
            if(bmiAvg > 0){
                if (dayOfWeek == Calendar.SUNDAY) {
                    yVals.add(new Entry(bmiAvg, 6));
                } else {
                    yVals.add(new Entry(bmiAvg, dayOfWeek - 2));
                }
            }
        }

        mFastItemAdapter.getAdapterItem(0).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(0).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

    private void bindMonthData() {
        Double changePercentage = Utils.getAveragePercentageChange(
                Calendar.MONTH,RealmTable.BMI.BMI,mMonthBMIs);
        List<Entry> yVals = new ArrayList<>(31);
        for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
            float bmiAvg = (float) mMonthBMIs.where()
                    .equalTo(RealmTable.BMI.MONTH, mCalendar.get(Calendar.MONTH))
                    .equalTo(RealmTable.BMI.DAY, dayOfMonth)
                    .average(RealmTable.BMI.BMI);
            if(bmiAvg > 0){
                yVals.add(new Entry(bmiAvg, dayOfMonth - 1));
            }
        }

        mFastItemAdapter.getAdapterItem(1).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(1).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

    private void bindYearData() {
        Double changePercentage = Utils.getAveragePercentageChange(
                Calendar.YEAR,RealmTable.BMI.BMI,mYearBMIs);
        List<Entry> yVals = new ArrayList<>(12);
        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
            float bmiAvg = (float) mYearBMIs.where()
                    .equalTo(RealmTable.BMI.YEAR, mCalendar.get(Calendar.YEAR))
                    .equalTo(RealmTable.BMI.MONTH, month)
                    .average(RealmTable.BMI.BMI);
            if(bmiAvg > 0){
                yVals.add(new Entry(bmiAvg, month));
            }
        }

        mFastItemAdapter.getAdapterItem(2).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(2).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

}
