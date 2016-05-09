package com.aarcosg.copdhelp.ui.fragment;


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
import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionMainPresenter;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionMainView;
import com.aarcosg.copdhelp.ui.activity.MedicalAttentionActivity;
import com.aarcosg.copdhelp.ui.adapteritem.MedicalAttentionItem;
import com.aarcosg.copdhelp.ui.decorator.SimpleDividerItemDecoration;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.GenericItemAdapter;
import com.mikepenz.iconics.IconicsDrawable;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class MedicalAttentionMainFragment extends BaseFragment implements MedicalAttentionMainView {

    private static final String TAG = MedicalAttentionMainFragment.class.getCanonicalName();

    @Inject
    MedicalAttentionMainPresenter mMedicalAttentionMainPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.medical_attention_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.empty_view_fl)
    FrameLayout mEmptyView;
    @Bind(R.id.empty_view_icon_iv)
    ImageView mEmptyViewIcon;

    private FastAdapter mFastAdapter;
    private GenericItemAdapter<MedicalAttention, MedicalAttentionItem> mItemAdapter;
    private RealmResults<MedicalAttention> mMedicalAttentions;

    public static MedicalAttentionMainFragment newInstance() {
        MedicalAttentionMainFragment fragment = new MedicalAttentionMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MedicalAttentionMainFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mMedicalAttentionMainPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_medical_attention_main, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        setupEmptyView();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMedicalAttentionMainPresenter.loadAllMedicalAttentions();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMedicalAttentionMainPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMedicalAttentions != null){
            mMedicalAttentions.removeChangeListeners();
        }
    }

    @Override
    public void bindAllMedicalAttentions(RealmResults<MedicalAttention> medicalAttentions) {
        mMedicalAttentions = medicalAttentions;
        checkEmptyResults();
        mMedicalAttentions.addChangeListener(element -> {
            mItemAdapter.setNewModel(medicalAttentions);
            checkEmptyResults();
        });
        mItemAdapter.addModel(mMedicalAttentions);
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
                , R.string.medical_attention_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> mMedicalAttentionMainPresenter.loadAllMedicalAttentions())
                .show();
    }

    @Override
    public void showRemoveRealmSuccessMessage() {
        Snackbar.make(mFab
                , R.string.medical_attention_remove_realm_success
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showRemoveRealmErrorMessage() {
        Snackbar.make(mFab
                , R.string.medical_attention_remove_realm_error
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        MedicalAttentionActivity.launch(getActivity());
    }

    private void setupAdapter() {
        mItemAdapter = new GenericItemAdapter<>(MedicalAttentionItem.class, MedicalAttention.class);
        mFastAdapter = new FastAdapter();
        mFastAdapter.withOnClickListener((v, adapter, item, position) -> {
            MedicalAttentionActivity.launch(
                    getActivity(), ((MedicalAttention)v.getTag()).getId());
            return false;
        });
        mFastAdapter.withOnLongClickListener((v, adapter, item, position) -> {
            showRemoveMedicalAttentionDialog(((MedicalAttention)v.getTag()).getId());
            return false;
        });

    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mRecyclerView.setAdapter(mItemAdapter.wrap(mFastAdapter));
    }

    private void setupEmptyView(){
        mEmptyViewIcon.setImageDrawable(
                new IconicsDrawable(getContext())
                .icon(CommunityMaterial.Icon.cmd_stethoscope)
                .color(Color.GRAY)
                .sizeDp(70)
        );
    }

    private void showRemoveMedicalAttentionDialog(Long id) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getResources().getString(R.string.delete));
        dialogBuilder.setMessage(getResources().getString(R.string.ask_delete_medical_attention));
        dialogBuilder.setPositiveButton(android.R.string.ok,(dialog, which) -> {
            mMedicalAttentionMainPresenter.removeMedicalAttention(id);
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel,(dialog, which) -> dialog.dismiss());
        dialogBuilder.show();
    }

    private void checkEmptyResults(){
        if(mMedicalAttentions != null && !mMedicalAttentions.isEmpty()){
            hideEmptyView();
        }else{
            showEmptyView();
        }
    }

}
