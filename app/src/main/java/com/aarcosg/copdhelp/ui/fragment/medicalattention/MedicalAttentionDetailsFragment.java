package com.aarcosg.copdhelp.ui.fragment.medicalattention;


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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.di.components.MedicalAttentionComponent;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionDetailsPresenter;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionDetailsView;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MedicalAttentionDetailsFragment extends BaseFragment implements MedicalAttentionDetailsView {

    private static final String TAG = MedicalAttentionDetailsFragment.class.getCanonicalName();
    private static final String EXTRA_MEDICAL_ATTENTION = "extra_medical_attention_id";

    @Inject
    MedicalAttentionDetailsPresenter mMedicalAttentionDetailsPresenter;

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

    private MedicalAttention mMedicalAttention;

    public static MedicalAttentionDetailsFragment newInstance(Long medicalAttentionId) {
        MedicalAttentionDetailsFragment fragment = new MedicalAttentionDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_MEDICAL_ATTENTION, medicalAttentionId);
        fragment.setArguments(args);
        return fragment;
    }

    public MedicalAttentionDetailsFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MedicalAttentionComponent.class).inject(this);
        mMedicalAttentionDetailsPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_medical_attention_details, container, false);
        ButterKnife.bind(this, fragmentView);
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        if (!getArguments().isEmpty() && getArguments().containsKey(EXTRA_MEDICAL_ATTENTION)) {
            mMedicalAttentionDetailsPresenter.loadRealmObject(
                    getArguments().getLong(EXTRA_MEDICAL_ATTENTION));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMedicalAttentionDetailsPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void bindRealmObject(MedicalAttention medicalAttention) {
        mMedicalAttention = medicalAttention;
        if (mMedicalAttention != null) {
            int typeColor = medicalAttention.getType() == MedicalAttention.TYPE_CHECKUP ?
                    R.color.md_blue_600 : R.color.md_deep_orange_600;
            mTypeTv.setText(getResources().getStringArray(
                    R.array.medical_attention_type)[mMedicalAttention.getType()]);
            mTypeCardView.setBackgroundColor(ContextCompat.getColor(getContext(), typeColor));
            mDateTv.setText(DateUtils.getRelativeTimeSpanString(
                    mMedicalAttention.getTimestamp().getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS));
            mNoteTv.setText(TextUtils.isEmpty(mMedicalAttention.getNote()) ?
                    getString(R.string.empty_note) : mMedicalAttention.getNote());
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
                , R.string.medical_attention_not_found_realm_error
                , Toast.LENGTH_LONG)
                .show();
        getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.medical_attention_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                mMedicalAttentionDetailsPresenter.onEditButtonClick(mMedicalAttention.getId());
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        mMedicalAttentionDetailsPresenter.onEditButtonClick(mMedicalAttention.getId());
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_medical_attention);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
