package com.aarcosg.copdhelp.ui.fragment.medicalattention;


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
import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionChartPresenter;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionChartView;
import com.aarcosg.copdhelp.ui.adapteritem.StackedBarChartItem;
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

public class MedicalAttentionChartFragment extends BaseFragment implements MedicalAttentionChartView {

    private static final String TAG = MedicalAttentionChartFragment.class.getCanonicalName();

    @Inject
    MedicalAttentionChartPresenter mMedicalAttentionChartPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Calendar mCalendar;
    private FastItemAdapter<StackedBarChartItem> mFastItemAdapter;
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
        mCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_medical_attention_chart, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        mMedicalAttentionChartPresenter.onCreateView();
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
    public void bindWeekData(RealmResults<MedicalAttention> medicalAttentions) {
        mWeekMedicalAttentions = medicalAttentions;
        mWeekMedicalAttentions.addChangeListener(element -> bindWeekData());
        if (!mWeekMedicalAttentions.isEmpty()) {
            bindWeekData();
        }
    }

    @Override
    public void bindMonthData(RealmResults<MedicalAttention> medicalAttentions) {
        mMonthMedicalAttentions = medicalAttentions;
        mMonthMedicalAttentions.addChangeListener(element -> bindMonthData());
        if (!mMonthMedicalAttentions.isEmpty()) {
            bindMonthData();
        }
    }

    @Override
    public void bindYearData(RealmResults<MedicalAttention> medicalAttentions) {
        mYearMedicalAttentions = medicalAttentions;
        mYearMedicalAttentions.addChangeListener(element -> bindYearData());
        if (!mYearMedicalAttentions.isEmpty()) {
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
                , R.string.medical_attention_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> loadChartsData())
                .show();
    }

    private void setupAdapter() {
        mFastItemAdapter = new FastItemAdapter<>();
        int[] dataSetColors = new int[]{R.color.md_blue_600, R.color.md_deep_orange_600};
        String[] stackLabels = getResources().getStringArray(R.array.medical_attention_type);

        ClickListenerHelper<StackedBarChartItem> clickListenerHelper = new ClickListenerHelper<StackedBarChartItem>(mFastItemAdapter);
        mFastItemAdapter.withOnCreateViewHolderListener(new FastAdapter.OnCreateViewHolderListener() {

            @Override
            public RecyclerView.ViewHolder onPreCreateViewHolder(ViewGroup parent, int viewType) {
                return mFastItemAdapter.getTypeInstance(viewType).getViewHolder(parent);
            }

            @Override
            public RecyclerView.ViewHolder onPostCreateViewHolder(RecyclerView.ViewHolder viewHolder) {
                clickListenerHelper.listen(viewHolder,((StackedBarChartItem.ViewHolder) viewHolder).shareChartBtn, (v, position, item) -> {
                    Utils.shareView(getContext(),((StackedBarChartItem.ViewHolder) viewHolder).containerCard);
                });
                return viewHolder;
            }

        });

        mFastItemAdapter.add(
                new StackedBarChartItem(
                        ChartUtils.CHART_TYPE_WEEK
                        , getString(R.string.this_week)
                        , getString(R.string.weekly_progress)
                        , 0.0
                        , Arrays.asList(getResources().getStringArray(R.array.week_days_alt))
                        , new ArrayList<>(7)
                        , dataSetColors
                        , stackLabels
                        , false)
                , new StackedBarChartItem(
                        ChartUtils.CHART_TYPE_MONTH
                        , getString(R.string.this_month)
                        , getString(R.string.monthly_progress)
                        , 0.0
                        , ChartUtils.getMonthXVals()
                        , new ArrayList<>(31)
                        , dataSetColors
                        , stackLabels
                        , false)
                , new StackedBarChartItem(
                        ChartUtils.CHART_TYPE_YEAR
                        , getString(R.string.this_year)
                        , getString(R.string.yearly_progress)
                        , 0.0
                        , Arrays.asList(getResources().getStringArray(R.array.months_alt))
                        , new ArrayList<>(12)
                        , dataSetColors
                        , stackLabels
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
        mMedicalAttentionChartPresenter.loadWeekData();
        mMedicalAttentionChartPresenter.loadMonthData();
        mMedicalAttentionChartPresenter.loadYearData();
    }

    private void bindWeekData() {
        Double changePercentage = Utils.getCountPercentageChange(Calendar.WEEK_OF_YEAR,mWeekMedicalAttentions);
        List<BarEntry> yVals = new ArrayList<>(7);
        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
            float checkupVal = mWeekMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.WEEK_OF_YEAR, mCalendar.get(Calendar.WEEK_OF_YEAR))
                    .equalTo(RealmTable.MedicalAttention.DAY_OF_WEEK, dayOfWeek)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_CHECKUP)
                    .count();
            float emergencyVal = mWeekMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.WEEK_OF_YEAR, mCalendar.get(Calendar.WEEK_OF_YEAR))
                    .equalTo(RealmTable.MedicalAttention.DAY_OF_WEEK, dayOfWeek)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_EMERGENCY)
                    .count();
            if (dayOfWeek == Calendar.SUNDAY) {
                yVals.add(new BarEntry(new float[]{checkupVal, emergencyVal}, 6));
            } else {
                yVals.add(new BarEntry(new float[]{checkupVal, emergencyVal}, dayOfWeek - 2));
            }
        }

        mFastItemAdapter.getAdapterItem(0).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(0).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

    private void bindMonthData() {
        Double changePercentage = Utils.getCountPercentageChange(Calendar.MONTH,mMonthMedicalAttentions);
        List<BarEntry> yVals = new ArrayList<>(31);
        for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
            float checkupVal = mMonthMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.MONTH, mCalendar.get(Calendar.MONTH))
                    .equalTo(RealmTable.MedicalAttention.DAY, dayOfMonth)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_CHECKUP)
                    .count();
            float emergencyVal = mMonthMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.MONTH, mCalendar.get(Calendar.MONTH))
                    .equalTo(RealmTable.MedicalAttention.DAY, dayOfMonth)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_EMERGENCY)
                    .count();
            yVals.add(new BarEntry(new float[]{checkupVal, emergencyVal}, dayOfMonth - 1));
        }

        mFastItemAdapter.getAdapterItem(1).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(1).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

    private void bindYearData() {
        Double changePercentage = Utils.getCountPercentageChange(Calendar.YEAR,mYearMedicalAttentions);
        List<BarEntry> yVals = new ArrayList<>(12);
        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
            float checkupVal = mYearMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.YEAR, mCalendar.get(Calendar.YEAR))
                    .equalTo(RealmTable.MedicalAttention.MONTH, month)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_CHECKUP)
                    .count();
            float emergencyVal = mYearMedicalAttentions.where()
                    .equalTo(RealmTable.MedicalAttention.YEAR, mCalendar.get(Calendar.YEAR))
                    .equalTo(RealmTable.MedicalAttention.MONTH, month)
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_EMERGENCY)
                    .count();
            yVals.add(new BarEntry(new float[]{checkupVal, emergencyVal}, month));
        }

        mFastItemAdapter.getAdapterItem(2).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(2).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

}
