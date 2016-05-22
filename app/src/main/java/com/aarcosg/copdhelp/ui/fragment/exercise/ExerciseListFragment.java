package com.aarcosg.copdhelp.ui.fragment.exercise;


import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.Exercise;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseListPresenter;
import com.aarcosg.copdhelp.mvp.view.exercise.ExerciseListView;
import com.aarcosg.copdhelp.ui.activity.ExerciseActivity;
import com.aarcosg.copdhelp.ui.adapteritem.ExerciseItem;
import com.aarcosg.copdhelp.ui.decorator.SimpleDividerItemDecoration;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.GenericItemAdapter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class ExerciseListFragment extends BaseFragment implements ExerciseListView {

    private static final String TAG = ExerciseListFragment.class.getCanonicalName();

    @Inject
    ExerciseListPresenter mExerciseListPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.empty_view_fl)
    FrameLayout mEmptyView;
    @Bind(R.id.empty_view_icon_iv)
    ImageView mEmptyViewIcon;

    private FastAdapter mFastAdapter;
    private GenericItemAdapter<Exercise, ExerciseItem> mItemAdapter;
    private RealmResults<Exercise> mExercises;

    public static ExerciseListFragment newInstance() {
        ExerciseListFragment fragment = new ExerciseListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExerciseListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mExerciseListPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        setupEmptyView();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mExerciseListPresenter.loadAllFromRealm();
    }

    @Override
    public void onPause() {
        super.onPause();
        mExerciseListPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mExercises != null){
            mExercises.removeChangeListeners();
        }
    }

    @Override
    public void bindAll(RealmResults<Exercise> exercises) {
        mExercises = exercises;
        checkEmptyResults();
        mExercises.addChangeListener(element -> {
            mItemAdapter.setNewModel(exercises);
            checkEmptyResults();
        });
        mItemAdapter.addModel(mExercises);
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
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadAllRealmErrorMessage() {
        Snackbar.make(mFab
                , R.string.exercise_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> mExerciseListPresenter.loadAllFromRealm())
                .show();
    }

    @Override
    public void showRemoveRealmSuccessMessage() {
        Snackbar.make(mFab
                , R.string.exercise_remove_realm_success
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showRemoveRealmErrorMessage() {
        Snackbar.make(mFab
                , R.string.exercise_remove_realm_error
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        ExerciseActivity.launch(getActivity());
    }

    private void setupAdapter() {
        mItemAdapter = new GenericItemAdapter<>(ExerciseItem.class, Exercise.class);
        mFastAdapter = new FastAdapter();
        mFastAdapter.withOnClickListener((v, adapter, item, position) -> {
            ExerciseActivity.launch(
                    getActivity(), ((Exercise)v.getTag()).getId());
            return false;
        });
        mFastAdapter.withOnLongClickListener((v, adapter, item, position) -> {
            showRemoveExerciseDialog(((Exercise)v.getTag()).getId());
            return false;
        });

    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(
                new SimpleDividerItemDecoration(getContext(),R.drawable.line_divider,0,0));
        mRecyclerView.setAdapter(mItemAdapter.wrap(mFastAdapter));
    }

    private void setupEmptyView(){
        mEmptyViewIcon.setImageDrawable(
                new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_directions_walk)
                .color(Color.GRAY)
                .sizeDp(70)
        );
    }

    private void showRemoveExerciseDialog(Long id) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getResources().getString(R.string.delete));
        dialogBuilder.setMessage(getResources().getString(R.string.exercise_ask_delete));
        dialogBuilder.setPositiveButton(android.R.string.ok,(dialog, which) -> {
            mExerciseListPresenter.removeFromRealm(id);
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel,(dialog, which) -> dialog.dismiss());
        dialogBuilder.show();
    }

    private void checkEmptyResults(){
        if(mExercises != null && !mExercises.isEmpty()){
            hideEmptyView();
        }else{
            showEmptyView();
        }
    }

}
