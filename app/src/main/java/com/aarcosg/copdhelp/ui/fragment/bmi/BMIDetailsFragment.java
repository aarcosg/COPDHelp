package com.aarcosg.copdhelp.ui.fragment.bmi;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
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
import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.aarcosg.copdhelp.di.components.BMIComponent;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIDetailsPresenter;
import com.aarcosg.copdhelp.mvp.view.bmi.BMIDetailsView;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.aarcosg.copdhelp.utils.Utils;
import com.mikepenz.iconics.IconicsDrawable;

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
    @Bind(R.id.date_tv)
    TextView mDateTv;
    @Bind(R.id.appbar)
    AppBarLayout mAppBar;
    @Bind(R.id.bmi_val_tv)
    TextView mBMIValTv;
    @Bind(R.id.bmi_val_icon_iv)
    ImageView mBMIValIconIv;
    @Bind(R.id.bmi_val_text_tv)
    TextView mBMIValTextTv;
    @Bind(R.id.height_tv)
    TextView mHeightTv;
    @Bind(R.id.weight_tv)
    TextView mWeightTv;

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
        final View fragmentView = inflater.inflate(R.layout.fragment_bmi_details, container, false);
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
    public void bindRealmObject(BMI bmi) {
        mBMI = bmi;
        if (mBMI != null) {
            mHeightTv.setText(String.valueOf(Math.round(mBMI.getHeight())));
            mWeightTv.setText(mBMI.getWeight().toString());
            mDateTv.setText(DateUtils.getRelativeTimeSpanString(
                    mBMI.getTimestamp().getTime(),System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS));
            int bmiStateArrayIndex = Utils.getBMIStateArrayIndex(mBMI.getBmi());
            TypedArray ta = getContext().getResources().obtainTypedArray(R.array.bmi_colors);
            int colorId = ContextCompat.getColor(getContext(), ta.getResourceId(bmiStateArrayIndex, 0));
            ta.recycle();
            mBMIValTv.setText(String.format("%.2f", Double.valueOf(mBMI.getBmi())));
            mBMIValTv.setTextColor(colorId);
            mBMIValTextTv.setText(getResources().getStringArray(R.array.bmi_texts)[bmiStateArrayIndex]);
            mBMIValTextTv.setTextColor(colorId);
            mBMIValIconIv.setImageDrawable(new IconicsDrawable(getContext()
                    , getResources().getStringArray(R.array.bmi_icons)[bmiStateArrayIndex])
                    .sizeDp(30)
                    .color(colorId));
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
                , R.string.bmi_not_found_realm_error
                , Toast.LENGTH_LONG)
                .show();
        getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bmi_details, menu);
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
    public void onFabClick() {
        mBMIDetailsPresenter.onEditButtonClick(mBMI.getId());
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_bmi);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
