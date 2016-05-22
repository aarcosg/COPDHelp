package com.aarcosg.copdhelp.ui.fragment.smoke;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.Smoke;
import com.aarcosg.copdhelp.di.components.SmokeComponent;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeEditPresenter;
import com.aarcosg.copdhelp.mvp.view.smoke.SmokeEditView;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmokeEditFragment extends BaseFragment implements SmokeEditView {

    private static final String TAG = SmokeEditFragment.class.getCanonicalName();
    private static final String EXTRA_SMOKE = "extra_smoke_id";

    @Inject
    SmokeEditPresenter mSmokeEditPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.quantity_et)
    EditText mQuantityEt;
    @Bind(R.id.date_tv)
    TextView mDateTv;
    @Bind(R.id.appbar)
    AppBarLayout mAppBar;

    private Calendar mSmokeTime;
    private DatePickerDialog mDatePickerDialog;
    private Smoke mSmoke;

    public static SmokeEditFragment newInstance() {
        SmokeEditFragment fragment = new SmokeEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static SmokeEditFragment newInstance(Long smokeId) {
        SmokeEditFragment fragment = new SmokeEditFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_SMOKE, smokeId);
        fragment.setArguments(args);
        return fragment;
    }

    public SmokeEditFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(SmokeComponent.class).inject(this);
        mSmokeEditPresenter.setView(this);
        mSmokeTime = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_smoke_edit, container, false);
        ButterKnife.bind(this, fragmentView);
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        if (!getArguments().isEmpty() && getArguments().containsKey(EXTRA_SMOKE)) {
            mSmokeEditPresenter.loadRealmObject(getArguments().getLong(EXTRA_SMOKE));
            mAppBar.setExpanded(false, true);
            mQuantityEt.requestFocus();
        } else {
            setupDatePickerDialog();
            setupDateTextView();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mSmokeEditPresenter.onPause();
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
    public void bindRealmObject(Smoke smoke) {
        mSmoke = smoke;
        if (mSmoke != null) {
            mQuantityEt.setText(mSmoke.getQuantity().toString());
            mSmokeTime.setTimeInMillis(mSmoke.getTimestamp().getTime());
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
                , R.string.smoke_save_realm_success
                , Toast.LENGTH_LONG)
                .show();
        getActivity().onBackPressed();
    }

    @Override
    public void showSaveErrorMessage() {
        Snackbar.make(mFab
                , R.string.smoke_save_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, v -> saveSmoke())
                .show();
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
        inflater.inflate(R.menu.smoke_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveSmoke();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        saveSmoke();
    }

    @OnClick(R.id.date_tv)
    public void onDateClick() {
        mDatePickerDialog.show();
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_smoke);
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
            mSmokeTime.set(Calendar.YEAR, year);
            mSmokeTime.set(Calendar.MONTH, monthOfYear);
            mSmokeTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setupDateTextView();
        }
                , mSmokeTime.get(Calendar.YEAR)
                , mSmokeTime.get(Calendar.MONTH)
                , mSmokeTime.get(Calendar.DAY_OF_MONTH));
    }

    private void setupDateTextView() {
        mDateTv.setText(DateUtils.formatDateTime(getContext()
                , mSmokeTime.getTimeInMillis()
                , DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_SHOW_WEEKDAY
                        | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_ABBREV_MONTH
                        | DateUtils.FORMAT_ABBREV_WEEKDAY));
    }

    private void saveSmoke() {
        Smoke smoke = new Smoke(
                Integer.valueOf(mQuantityEt.getText().toString()),
                mSmokeTime.getTime());
        if (mSmoke == null) {
            mSmokeEditPresenter.addRealmObject(smoke);
        } else {
            mSmokeEditPresenter.editRealmObject(mSmoke.getId(), smoke);
        }
    }

}
