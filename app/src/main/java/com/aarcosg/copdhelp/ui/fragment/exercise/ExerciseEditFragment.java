package com.aarcosg.copdhelp.ui.fragment.exercise;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.Exercise;
import com.aarcosg.copdhelp.di.components.ExerciseComponent;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseEditPresenter;
import com.aarcosg.copdhelp.mvp.view.exercise.ExerciseEditView;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExerciseEditFragment extends BaseFragment implements ExerciseEditView {

    private static final String TAG = ExerciseEditFragment.class.getCanonicalName();
    private static final String EXTRA_EXERCISE = "extra_exercise_id";

    @Inject
    ExerciseEditPresenter mExerciseEditPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.type_spinner)
    Spinner mTypeSpinner;
    @Bind(R.id.duration_et)
    EditText mDurationEt;
    @Bind(R.id.date_tv)
    TextView mDateTv;
    @Bind(R.id.note_et)
    EditText mNoteEt;
    @Bind(R.id.appbar)
    AppBarLayout mAppBar;

    private Calendar mExerciseTime;
    private DatePickerDialog mDatePickerDialog;
    private Exercise mExercise;

    public static ExerciseEditFragment newInstance() {
        ExerciseEditFragment fragment = new ExerciseEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ExerciseEditFragment newInstance(Long exerciseId) {
        ExerciseEditFragment fragment = new ExerciseEditFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_EXERCISE, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }

    public ExerciseEditFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(ExerciseComponent.class).inject(this);
        mExerciseEditPresenter.setView(this);
        mExerciseTime = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_exercise_edit, container, false);
        ButterKnife.bind(this, fragmentView);
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        if (!getArguments().isEmpty() && getArguments().containsKey(EXTRA_EXERCISE)) {
            mExerciseEditPresenter.loadRealmObject(getArguments().getLong(EXTRA_EXERCISE));
            mAppBar.setExpanded(false, true);
            mDurationEt.requestFocus();
        } else {
            setupDatePickerDialog();
            setupDateTextView();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mExerciseEditPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mDatePickerDialog != null && mDatePickerDialog.isShowing()) {
            mDatePickerDialog.dismiss();
        }
    }

    @Override
    public void bindRealmObject(Exercise Exercise) {
        mExercise = Exercise;
        if (mExercise != null) {
            mTypeSpinner.setSelection(mExercise.getType());
            mDurationEt.setText(String.valueOf(mExercise.getDuration()));
            mNoteEt.setText(mExercise.getNote());
            mExerciseTime.setTimeInMillis(mExercise.getTimestamp().getTime());
            setupDatePickerDialog();
            setupDateTextView();
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
    public void showSaveSuccessMessage() {
        Toast.makeText(getContext()
                , R.string.exercise_save_realm_success
                , Toast.LENGTH_LONG)
                .show();
        getActivity().onBackPressed();
    }

    @Override
    public void showSaveErrorMessage() {
        Snackbar.make(mFab
                , R.string.exercise_save_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, v -> saveExercise())
                .show();
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
        inflater.inflate(R.menu.exercise_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveExercise();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        saveExercise();
    }

    @OnClick(R.id.date_tv)
    public void onDateClick() {
        mDatePickerDialog.show();
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_exercise);
        mToolbar.setNavigationIcon(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_clear)
                .color(Color.WHITE)
                .sizeDp(16));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupDatePickerDialog() {
        mDatePickerDialog = new DatePickerDialog(
                getContext()
                , (view, year, monthOfYear, dayOfMonth) -> {
                    mExerciseTime.set(Calendar.YEAR, year);
                    mExerciseTime.set(Calendar.MONTH, monthOfYear);
                    mExerciseTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setupDateTextView();
                }
                , mExerciseTime.get(Calendar.YEAR)
                , mExerciseTime.get(Calendar.MONTH)
                , mExerciseTime.get(Calendar.DAY_OF_MONTH));
    }

    private void setupDateTextView() {
        mDateTv.setText(DateUtils.formatDateTime(getContext()
                , mExerciseTime.getTimeInMillis()
                , DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_SHOW_WEEKDAY
                        | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_ABBREV_MONTH
                        | DateUtils.FORMAT_ABBREV_WEEKDAY));
    }

    private void saveExercise() {
        if(TextUtils.isEmpty(mDurationEt.getText())){
            mDurationEt.setError(getString(R.string.required_field));
            mDurationEt.requestFocus();
        }else {
            Exercise exercise = new Exercise(
                    mTypeSpinner.getSelectedItemPosition(),
                    Integer.valueOf(mDurationEt.getText().toString()),
                    mNoteEt.getText().toString(),
                    mExerciseTime.getTime());
            if (mExercise == null) {
                mExerciseEditPresenter.addRealmObject(exercise);
            } else {
                mExerciseEditPresenter.editRealmObject(mExercise.getId(), exercise);
            }
        }
    }

}
