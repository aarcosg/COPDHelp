package com.aarcosg.copdhelp.ui.fragment.bmi;


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
import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIListPresenter;
import com.aarcosg.copdhelp.mvp.view.bmi.BMIListView;
import com.aarcosg.copdhelp.ui.activity.BMIActivity;
import com.aarcosg.copdhelp.ui.adapteritem.BMIItem;
import com.aarcosg.copdhelp.ui.decorator.SimpleDividerItemDecoration;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.GenericItemAdapter;
import com.mikepenz.iconics.IconicsDrawable;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class BMIListFragment extends BaseFragment implements BMIListView {

    private static final String TAG = BMIListFragment.class.getCanonicalName();

    @Inject
    BMIListPresenter mBMIListPresenter;

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
    private GenericItemAdapter<BMI, BMIItem> mItemAdapter;
    private RealmResults<BMI> mBMIs;

    public static BMIListFragment newInstance() {
        BMIListFragment fragment = new BMIListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BMIListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mBMIListPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_bmi_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        setupEmptyView();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBMIListPresenter.loadAllFromRealm();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBMIListPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBMIs != null){
            mBMIs.removeChangeListeners();
        }
    }

    @Override
    public void bindAll(RealmResults<BMI> BMIs) {
        mBMIs = BMIs;
        checkEmptyResults();
        mBMIs.addChangeListener(element -> {
            mItemAdapter.setNewModel(BMIs);
            checkEmptyResults();
        });
        mItemAdapter.addModel(mBMIs);
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
                , R.string.bmi_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> mBMIListPresenter.loadAllFromRealm())
                .show();
    }

    @Override
    public void showRemoveRealmSuccessMessage() {
        Snackbar.make(mFab
                , R.string.bmi_remove_realm_success
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showRemoveRealmErrorMessage() {
        Snackbar.make(mFab
                , R.string.bmi_remove_realm_error
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        BMIActivity.launch(getActivity());
    }

    private void setupAdapter() {
        mItemAdapter = new GenericItemAdapter<>(BMIItem.class, BMI.class);
        mFastAdapter = new FastAdapter();
        mFastAdapter.withOnClickListener((v, adapter, item, position) -> {
            BMIActivity.launch(
                    getActivity(), ((BMI)v.getTag()).getId());
            return false;
        });
        mFastAdapter.withOnLongClickListener((v, adapter, item, position) -> {
            showRemoveBMIDialog(((BMI)v.getTag()).getId());
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
                .icon(CommunityMaterial.Icon.cmd_scale_bathroom)
                .color(Color.GRAY)
                .sizeDp(70)
        );
    }

    private void showRemoveBMIDialog(Long id) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getResources().getString(R.string.delete));
        dialogBuilder.setMessage(getResources().getString(R.string.bmi_ask_delete));
        dialogBuilder.setPositiveButton(android.R.string.ok,(dialog, which) -> {
            mBMIListPresenter.removeFromRealm(id);
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel,(dialog, which) -> dialog.dismiss());
        dialogBuilder.show();
    }

    private void checkEmptyResults(){
        if(mBMIs != null && !mBMIs.isEmpty()){
            hideEmptyView();
        }else{
            showEmptyView();
        }
    }

}
