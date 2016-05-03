package com.aarcosg.copdhelp.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionMainPresenter;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionMainView;
import com.aarcosg.copdhelp.ui.activity.MedicalAttentionEditActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MedicalAttentionMainFragment extends BaseFragment implements MedicalAttentionMainView {

    private static final String TAG = MedicalAttentionMainFragment.class.getCanonicalName();

    @Inject
    MedicalAttentionMainPresenter mMedicalAttentionMainPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.medical_attention_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.medical_attention_fab)
    FloatingActionButton mFab;

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
        setupFab();
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void setupFab() {
        mFab.setOnClickListener(v ->
                MedicalAttentionEditActivity.launch(this.getActivity())
        );
    }

}
