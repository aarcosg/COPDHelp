package com.aarcosg.copdhelp.ui.fragment.exercise;


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
import com.aarcosg.copdhelp.data.realm.entity.Exercise;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseChartPresenter;
import com.aarcosg.copdhelp.mvp.view.exercise.ExerciseChartView;
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

public class ExerciseChartFragment extends BaseFragment implements ExerciseChartView {

    private static final String TAG = ExerciseChartFragment.class.getCanonicalName();

    @Inject
    ExerciseChartPresenter mExerciseChartPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Calendar mCalendar;
    private FastItemAdapter<StackedBarChartItem> mFastItemAdapter;
    private RealmResults<Exercise> mWeekExercises;
    private RealmResults<Exercise> mMonthExercises;
    private RealmResults<Exercise> mYearExercises;

    public static ExerciseChartFragment newInstance() {
        ExerciseChartFragment fragment = new ExerciseChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExerciseChartFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mExerciseChartPresenter.setView(this);
        mCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_exercise_chart, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        mExerciseChartPresenter.onCreateView();
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
        mExerciseChartPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWeekExercises != null) {
            mWeekExercises.removeChangeListeners();
        }
        if (mMonthExercises != null) {
            mMonthExercises.removeChangeListeners();
        }
        if (mYearExercises != null) {
            mYearExercises.removeChangeListeners();
        }
    }

    @Override
    public void bindWeekData(RealmResults<Exercise> exercises) {
        mWeekExercises = exercises;
        mWeekExercises.addChangeListener(element -> bindWeekData());
        if (!mWeekExercises.isEmpty()) {
            bindWeekData();
        }
    }

    @Override
    public void bindMonthData(RealmResults<Exercise> exercises) {
        mMonthExercises = exercises;
        mMonthExercises.addChangeListener(element -> bindMonthData());
        if (!mMonthExercises.isEmpty()) {
            bindMonthData();
        }
    }

    @Override
    public void bindYearData(RealmResults<Exercise> exercises) {
        mYearExercises = exercises;
        mYearExercises.addChangeListener(element -> bindYearData());
        if (!mYearExercises.isEmpty()) {
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
                , R.string.exercise_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> loadChartsData())
                .show();
    }

    private void setupAdapter() {
        mFastItemAdapter = new FastItemAdapter<>();
        int[] dataSetColors = new int[]{
                R.color.exercise_type_walk
                ,R.color.exercise_type_stationary_bicycle
                ,R.color.exercise_type_breathing
                ,R.color.exercise_type_arms};
        String[] stackLabels = getResources().getStringArray(R.array.exercise_type);

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
        mExerciseChartPresenter.loadWeekData();
        mExerciseChartPresenter.loadMonthData();
        mExerciseChartPresenter.loadYearData();
    }

    private void bindWeekData() {
        Double changePercentage = Utils.getSumPercentageChange(Calendar.WEEK_OF_YEAR, RealmTable.Exercise.DURATION, mWeekExercises);
        List<BarEntry> yVals = new ArrayList<>(7);
        float[] values = new float[getResources().getStringArray(R.array.exercise_type).length];
        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
            for(int i = 0; i < values.length ; i++){
                int value =  mWeekExercises.where()
                        .equalTo(RealmTable.Exercise.WEEK_OF_YEAR, mCalendar.get(Calendar.WEEK_OF_YEAR))
                        .equalTo(RealmTable.Exercise.DAY_OF_WEEK, dayOfWeek)
                        .equalTo(RealmTable.Exercise.TYPE, i)
                        .sum(RealmTable.Exercise.DURATION).intValue();
                values[i] = value;
            }
            if (dayOfWeek == Calendar.SUNDAY) {
                yVals.add(new BarEntry(values, 6));
            } else {
                yVals.add(new BarEntry(values, dayOfWeek - 2));
            }
        }

        mFastItemAdapter.getAdapterItem(0).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(0).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

    private void bindMonthData() {
        Double changePercentage = Utils.getSumPercentageChange(Calendar.MONTH, RealmTable.Exercise.DURATION, mMonthExercises);
        List<BarEntry> yVals = new ArrayList<>(31);
        float[] values = new float[getResources().getStringArray(R.array.exercise_type).length];
        for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
            for(int i = 0; i < values.length ; i++){
                int value =  mMonthExercises.where()
                        .equalTo(RealmTable.Exercise.MONTH, mCalendar.get(Calendar.MONTH))
                        .equalTo(RealmTable.Exercise.DAY, dayOfMonth)
                        .equalTo(RealmTable.Exercise.TYPE, i)
                        .sum(RealmTable.Exercise.DURATION).intValue();
                values[i] = value;
            }
            yVals.add(new BarEntry(values, dayOfMonth - 1));
        }

        mFastItemAdapter.getAdapterItem(1).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(1).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

    private void bindYearData() {
        Double changePercentage = Utils.getSumPercentageChange(Calendar.YEAR, RealmTable.Exercise.DURATION, mYearExercises);
        List<BarEntry> yVals = new ArrayList<>(12);
        float[] values = new float[getResources().getStringArray(R.array.exercise_type).length];
        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
            for(int i = 0; i < values.length ; i++){
                int value =  mYearExercises.where()
                        .equalTo(RealmTable.Exercise.YEAR, mCalendar.get(Calendar.YEAR))
                        .equalTo(RealmTable.Exercise.MONTH, month)
                        .equalTo(RealmTable.Exercise.TYPE, i)
                        .sum(RealmTable.Exercise.DURATION).intValue();
                values[i] = value;
            }
            yVals.add(new BarEntry(values, month));
        }

        mFastItemAdapter.getAdapterItem(2).setChartYVals(yVals);
        mFastItemAdapter.getAdapterItem(2).setChangePercentage(changePercentage);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

}
