package com.aarcosg.copdhelp.ui.fragment.bmi;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.aarcosg.copdhelp.di.components.BMIComponent;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIDetailsPresenter;
import com.aarcosg.copdhelp.mvp.view.bmi.BMIDetailsView;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BMIDetailsFragment extends BaseFragment implements BMIDetailsView {

    private static final String TAG = BMIDetailsFragment.class.getCanonicalName();
    private static final String EXTRA_BMI = "extra_bmi_id";

    @Inject
    BMIDetailsPresenter mBMIDetailsPresenter;

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

    private BMI mBMI;

    public static BMIDetailsFragment newInstance(Long BMIId) {
        BMIDetailsFragment fragment = new BMIDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_BMI, BMIId);
        fragment.setArguments(args);
        return fragment;
    }

    public BMIDetailsFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(BMIComponent.class).inject(this);
        mBMIDetailsPresenter.setView(this);
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
        if (!getArguments().isEmpty() && getArguments().containsKey(EXTRA_BMI)) {
            mBMIDetailsPresenter.loadRealmObject(
                    getArguments().getLong(EXTRA_BMI));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBMIDetailsPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void bindRealmObject(BMI BMI) {
        mBMI = BMI;
       /* if (mBMI != null) {
            mTypeTv.setText(getResources().getStringArray(
                    R.array.medical_attention_type)[BMI.getType()]);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(BMI.getTimestamp().getTime());
            mDateTv.setText(getString(R.string.date_string,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    String.format("%02d",
                            calendar.get(Calendar.MONTH) + 1),
                    calendar.get(Calendar.YEAR)));
            mNoteTv.setText(TextUtils.isEmpty(mBMI.getNote()) ?
                    getString(R.string.empty_note) : mBMI.getNote());
        }*/
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
                mBMIDetailsPresenter.onEditButtonClick(mBMI.getId());
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        mBMIDetailsPresenter.onEditButtonClick(mBMI.getId());
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_medical_attention);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
