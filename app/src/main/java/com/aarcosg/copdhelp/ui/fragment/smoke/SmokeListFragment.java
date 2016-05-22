package com.aarcosg.copdhelp.ui.fragment.smoke;


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
import com.aarcosg.copdhelp.data.realm.entity.Smoke;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeListPresenter;
import com.aarcosg.copdhelp.mvp.view.smoke.SmokeListView;
import com.aarcosg.copdhelp.ui.activity.SmokeActivity;
import com.aarcosg.copdhelp.ui.adapteritem.SmokeItem;
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

public class SmokeListFragment extends BaseFragment implements SmokeListView {

    private static final String TAG = SmokeListFragment.class.getCanonicalName();

    @Inject
    SmokeListPresenter mSmokeListPresenter;

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
    private GenericItemAdapter<Smoke, SmokeItem> mItemAdapter;
    private RealmResults<Smoke> mSmokeList;

    public static SmokeListFragment newInstance() {
        SmokeListFragment fragment = new SmokeListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SmokeListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mSmokeListPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_smoke_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        setupEmptyView();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSmokeListPresenter.loadAllFromRealm();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSmokeListPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mSmokeList != null){
            mSmokeList.removeChangeListeners();
        }
    }

    @Override
    public void bindAll(RealmResults<Smoke> smokeList) {
        mSmokeList = smokeList;
        checkEmptyResults();
        mSmokeList.addChangeListener(element -> {
            mItemAdapter.setNewModel(smokeList);
            checkEmptyResults();
        });
        mItemAdapter.addModel(mSmokeList);
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
                , R.string.smoke_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> mSmokeListPresenter.loadAllFromRealm())
                .show();
    }

    @Override
    public void showRemoveRealmSuccessMessage() {
        Snackbar.make(mFab
                , R.string.smoke_remove_realm_success
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showRemoveRealmErrorMessage() {
        Snackbar.make(mFab
                , R.string.smoke_remove_realm_error
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        SmokeActivity.launch(getActivity());
    }

    private void setupAdapter() {
        mItemAdapter = new GenericItemAdapter<>(SmokeItem.class, Smoke.class);
        mFastAdapter = new FastAdapter();
        mFastAdapter.withOnClickListener((v, adapter, item, position) -> {
            SmokeActivity.launch(
                    getActivity(), ((Smoke)v.getTag()).getId());
            return false;
        });
        mFastAdapter.withOnLongClickListener((v, adapter, item, position) -> {
            showRemoveSmokeDialog(((Smoke)v.getTag()).getId());
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
                .icon(CommunityMaterial.Icon.cmd_smoking_off)
                .color(Color.GRAY)
                .sizeDp(70)
        );
    }

    private void showRemoveSmokeDialog(Long id) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getResources().getString(R.string.delete));
        dialogBuilder.setMessage(getResources().getString(R.string.smoke_ask_delete));
        dialogBuilder.setPositiveButton(android.R.string.ok,(dialog, which) -> {
            mSmokeListPresenter.removeFromRealm(id);
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel,(dialog, which) -> dialog.dismiss());
        dialogBuilder.show();
    }

    private void checkEmptyResults(){
        if(mSmokeList != null && !mSmokeList.isEmpty()){
            hideEmptyView();
        }else{
            showEmptyView();
        }
    }

}
