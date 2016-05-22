package com.aarcosg.copdhelp.ui.fragment.smoke;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
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
import com.aarcosg.copdhelp.data.realm.entity.Smoke;
import com.aarcosg.copdhelp.di.components.SmokeComponent;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeDetailsPresenter;
import com.aarcosg.copdhelp.mvp.view.smoke.SmokeDetailsView;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmokeDetailsFragment extends BaseFragment implements SmokeDetailsView {

    private static final String TAG = SmokeDetailsFragment.class.getCanonicalName();
    private static final String EXTRA_SMOKE = "extra_smoke_id";

    @Inject
    SmokeDetailsPresenter mSmokeDetailsPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.date_tv)
    TextView mDateTv;
    @Bind(R.id.appbar)
    AppBarLayout mAppBar;
    @Bind(R.id.quantity_tv)
    TextView mQuantityTv;

    private Smoke mSmoke;

    public static SmokeDetailsFragment newInstance(Long smokeId) {
        SmokeDetailsFragment fragment = new SmokeDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_SMOKE, smokeId);
        fragment.setArguments(args);
        return fragment;
    }

    public SmokeDetailsFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(SmokeComponent.class).inject(this);
        mSmokeDetailsPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_smoke_details, container, false);
        ButterKnife.bind(this, fragmentView);
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        if (!getArguments().isEmpty() && getArguments().containsKey(EXTRA_SMOKE)) {
            mSmokeDetailsPresenter.loadRealmObject(
                    getArguments().getLong(EXTRA_SMOKE));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSmokeDetailsPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void bindRealmObject(Smoke smoke) {
        mSmoke = smoke;
        if (mSmoke != null) {
            mQuantityTv.setText(String.valueOf(mSmoke.getQuantity()));
            mDateTv.setText(DateUtils.getRelativeTimeSpanString(
                    mSmoke.getTimestamp().getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS));
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
                , R.string.smoke_not_found_realm_error
                , Toast.LENGTH_LONG)
                .show();
        getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.smoke_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                mSmokeDetailsPresenter.onEditButtonClick(mSmoke.getId());
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        mSmokeDetailsPresenter.onEditButtonClick(mSmoke.getId());
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_smoke);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
