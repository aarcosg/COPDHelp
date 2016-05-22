package com.aarcosg.copdhelp.ui.fragment.smoke;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.Smoke;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeChartPresenter;
import com.aarcosg.copdhelp.mvp.view.smoke.SmokeChartView;
import com.aarcosg.copdhelp.ui.adapteritem.BarChartItem;
import com.aarcosg.copdhelp.ui.decorator.VerticalSpaceItemDecoration;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.aarcosg.copdhelp.utils.ChartUtils;
import com.aarcosg.copdhelp.utils.Utils;
import com.github.mikephil.charting.data.BarEntry;
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

public class SmokeChartFragment extends BaseFragment implements SmokeChartView {

    private static final String TAG = SmokeChartFragment.class.getCanonicalName();

    @Inject
    SmokeChartPresenter mSmokeChartPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Calendar mCalendar;
    private FastItemAdapter<BarChartItem> mFastItemAdapter;
    private RealmResults<Smoke> mWeekSmokeList;
    private RealmResults<Smoke> mMonthSmokeList;
    private RealmResults<Smoke> mYearSmokeList;

    public static SmokeChartFragment newInstance() {
        SmokeChartFragment fragment = new SmokeChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SmokeChartFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mSmokeChartPresenter.setView(this);
        mCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_smoke_chart, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        mSmokeChartPresenter.onCreateView();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadChartsData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSmokeChartPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWeekSmokeList != null) {
            mWeekSmokeList.removeChangeListeners();
        }
        if (mMonthSmokeList != null) {
            mMonthSmokeList.removeChangeListeners();
        }
        if (mYearSmokeList != null) {
            mYearSmokeList.removeChangeListeners();
        }
    }

    @Override
    public void bindWeekData(RealmResults<Smoke> SmokeList) {
        mWeekSmokeList = SmokeList;
        mWeekSmokeList.addChangeListener(element -> bindWeekData());
        if (!mWeekSmokeList.isEmpty()) {
            bindWeekData();
        }
    }

    @Override
    public void bindMonthData(RealmResults<Smoke> SmokeList) {
        mMonthSmokeList = SmokeList;
        mMonthSmokeList.addChangeListener(element -> bindMonthData());
        if (!mMonthSmokeList.isEmpty()) {
            bindMonthData();
        }
    }

    @Override
    public void bindYearData(RealmResults<Smoke> SmokeList) {
        mYearSmokeList = SmokeList;
        mYearSmokeList.addChangeListener(element -> bindYearData());
        if (!mYearSmokeList.isEmpty()) {
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
                , R.string.smoke_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> loadChartsData())
                .show();
    }

    private void setupAdapter() {
        mFastItemAdapter = new FastItemAdapter<>();
        int[] dataSetColors = new int[]{R.color.md_orange_500};

        ClickListenerHelper<BarChartItem> clickListenerHelper = new ClickListenerHelper<BarChartItem>(mFastItemAdapter);
        mFastItemAdapter.withOnCreateViewHolderListener(new FastAdapter.OnCreateViewHolderListener() {

            @Override
            public RecyclerView.ViewHolder onPreCreateViewHolder(ViewGroup parent, int viewType) {
                return mFastItemAdapter.getTypeInstance(viewType).getViewHolder(parent);
            }

            @Override
            public RecyclerView.ViewHolder onPostCreateViewHolder(RecyclerView.ViewHolder viewHolder) {
                clickListenerHelper.listen(viewHolder,((BarChartItem.ViewHolder) viewHolder).shareChartBtn, (v, position, item) -> {
                    Utils.shareView(getContext(),((BarChartItem.ViewHolder) viewHolder).containerCard);
                });
                return viewHolder;
            }

        });

        mFastItemAdapter.add(
                new BarChartItem(
                        ChartUtils.CHART_TYPE_WEEK
                        , getString(R.string.this_week)
                        , getString(R.string.weekly_progress)
                        , 0.0
                        , Arrays.asList(getResources().getStringArray(R.array.week_days_alt))
                        , new ArrayList<>(7)
                        , dataSetColors
                        , false)
                , new BarChartItem(
                        ChartUtils.CHART_TYPE_MONTH
                        , getString(R.string.this_month)
                        , getString(R.string.monthly_progress)
                        , 0.0
                        , ChartUtils.getMonthXVals()
                        , new ArrayList<>(31)
                        , dataSetColors
                        , false)
                , new BarChartItem(
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
        mSmokeChartPresenter.loadWeekData();
        mSmokeChartPresenter.loadMonthData();
        mSmokeChartPresenter.loadYearData();
    }

    private void bindWeekData() {
        Double changePercentage = Utils.getSumPercentageChange(Calendar.WEEK_OF_YEAR, RealmTable.Smoke.QUANTITY, mWeekSmokeList);
        List<BarEntry> yVals = new ArrayList<>(7);
        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
            int quantityVal = mWeekSmokeList.where()
                    .equalTo(RealmTable.Smoke.WEEK_OF_YEAR, mCalendar.get(Calendar.WEEK_OF_YEAR))
                    .equalTo(RealmTable.Smoke.DAY_OF_WEEK, dayOfWeek)
                    .sum(RealmTable.Smoke.QUANTITY).intValue();
            if (dayOfWeek == Calendar.SUNDAY) {
                yVals.add(new BarEntry(quantityVal, 6));
            } else {
                yVals.add(new BarEntry(quantityVal, dayOfWeek - 2));
            }
        }

        mFastItemAdapter.getAdapterItem(0).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(0).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

    private void bindMonthData() {
        Double changePercentage = Utils.getSumPercentageChange(Calendar.MONTH, RealmTable.Smoke.QUANTITY, mMonthSmokeList);
        List<BarEntry> yVals = new ArrayList<>(31);
        for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
            int quantityVal = mMonthSmokeList.where()
                    .equalTo(RealmTable.Smoke.MONTH, mCalendar.get(Calendar.MONTH))
                    .equalTo(RealmTable.Smoke.DAY, dayOfMonth)
                    .sum(RealmTable.Smoke.QUANTITY).intValue();
            yVals.add(new BarEntry(quantityVal, dayOfMonth - 1));
        }

        mFastItemAdapter.getAdapterItem(1).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(1).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

    private void bindYearData() {
        Double changePercentage = Utils.getSumPercentageChange(Calendar.YEAR, RealmTable.Smoke.QUANTITY, mYearSmokeList);
        List<BarEntry> yVals = new ArrayList<>(12);
        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
            int quantityVal = mYearSmokeList.where()
                    .equalTo(RealmTable.Smoke.YEAR, mCalendar.get(Calendar.YEAR))
                    .equalTo(RealmTable.Smoke.MONTH, month)
                    .sum(RealmTable.Smoke.QUANTITY).intValue();
            yVals.add(new BarEntry(quantityVal, month));
        }

        mFastItemAdapter.getAdapterItem(2).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(2).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

}
