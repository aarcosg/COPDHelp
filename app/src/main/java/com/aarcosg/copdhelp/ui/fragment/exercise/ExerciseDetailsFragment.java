package com.aarcosg.copdhelp.ui.fragment.exercise;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.Exercise;
import com.aarcosg.copdhelp.di.components.ExerciseComponent;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseDetailsPresenter;
import com.aarcosg.copdhelp.mvp.view.exercise.ExerciseDetailsView;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.mikepenz.iconics.IconicsDrawable;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExerciseDetailsFragment extends BaseFragment implements ExerciseDetailsView {

    private static final String TAG = ExerciseDetailsFragment.class.getCanonicalName();
    private static final String EXTRA_EXERCISE = "extra_exercise_id";

    @Inject
    ExerciseDetailsPresenter mExerciseDetailsPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.type_tv)
    TextView mTypeTv;
    @Bind(R.id.note_tv)
    TextView mNoteTv;
    @Bind(R.id.date_tv)
    TextView mDateTv;
    @Bind(R.id.appbar)
    AppBarLayout mAppBar;
    @Bind(R.id.type_cardview)
    CardView mTypeCardView;
    @Bind(R.id.type_icon_iv)
    ImageView mTypeIconIv;
    @Bind(R.id.duration_tv)
    TextView mDurationTv;

    private Exercise mExercise;

    public static ExerciseDetailsFragment newInstance(Long exerciseId) {
        ExerciseDetailsFragment fragment = new ExerciseDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_EXERCISE, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }

    public ExerciseDetailsFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(ExerciseComponent.class).inject(this);
        mExerciseDetailsPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_exercise_details, container, false);
        ButterKnife.bind(this, fragmentView);
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        if (!getArguments().isEmpty() && getArguments().containsKey(EXTRA_EXERCISE)) {
            mExerciseDetailsPresenter.loadRealmObject(
                    getArguments().getLong(EXTRA_EXERCISE));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mExerciseDetailsPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void bindRealmObject(Exercise exercise) {
        mExercise = exercise;
        if (mExercise != null) {
            mTypeTv.setText(getResources().getStringArray(
                    R.array.exercise_type)[mExercise.getType()]);
            mDurationTv.setText(String.valueOf(exercise.getDuration()));
            mDateTv.setText(DateUtils.getRelativeTimeSpanString(
                    mExercise.getTimestamp().getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS));
            mNoteTv.setText(TextUtils.isEmpty(mExercise.getNote()) ?
                    getString(R.string.empty_note_alt) : mExercise.getNote());
            mTypeIconIv.setImageDrawable(new IconicsDrawable(getContext()
                    , getResources().getStringArray(R.array.exercise_type_icons)[mExercise.getType()])
                    .colorRes(R.color.md_white_1000)
                    .sizeDp(20));
            TypedArray ta = getContext().getResources().obtainTypedArray(R.array.exercise_type_colors);
            int colorId = ContextCompat.getColor(getContext(), ta.getResourceId(exercise.getType(), 0));
            ta.recycle();
            mTypeCardView.setBackgroundColor(colorId);
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
    public void showRealmObjectNotFoundError() {
        Toast.makeText(getContext()
                , R.string.exercise_not_found_realm_error
                , Toast.LENGTH_LONG)
                .show();
        getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.exercise_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                mExerciseDetailsPresenter.onEditButtonClick(mExercise.getId());
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        mExerciseDetailsPresenter.onEditButtonClick(mExercise.getId());
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_exercise);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
