package com.aarcosg.copdhelp.ui.fragment.bmi;


import android.app.DatePickerDialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.aarcosg.copdhelp.di.components.BMIComponent;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIEditPresenter;
import com.aarcosg.copdhelp.mvp.view.bmi.BMIEditView;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.aarcosg.copdhelp.utils.Utils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class BMIEditFragment extends BaseFragment implements BMIEditView {

    private static final String TAG = BMIEditFragment.class.getCanonicalName();
    private static final String EXTRA_BMI = "extra_bmi_id";

    @Inject
    BMIEditPresenter mBMIEditPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.height_et)
    EditText mHeightEt;
    @Bind(R.id.weight_et)
    EditText mWeightEt;
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

    private Calendar mBMITime;
    private DatePickerDialog mDatePickerDialog;
    private BMI mBMI;
    private Double mBMIValue;
    private int mBMIStateIndex;

    public static BMIEditFragment newInstance() {
        BMIEditFragment fragment = new BMIEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static BMIEditFragment newInstance(Long bmiId) {
        BMIEditFragment fragment = new BMIEditFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_BMI, bmiId);
        fragment.setArguments(args);
        return fragment;
    }

    public BMIEditFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(BMIComponent.class).inject(this);
        mBMIEditPresenter.setView(this);
        mBMITime = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_bmi_edit, container, false);
        ButterKnife.bind(this, fragmentView);
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        if (!getArguments().isEmpty() && getArguments().containsKey(EXTRA_BMI)) {
            mBMIEditPresenter.loadRealmObject(getArguments().getLong(EXTRA_BMI));
            mAppBar.setExpanded(false, true);
            mWeightEt.requestFocus();
        } else {
            setupDatePickerDialog();
            setupDateTextView();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mBMIEditPresenter.onPause();
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
    public void bindRealmObject(BMI bmi) {
        mBMI = bmi;
        if (mBMI != null) {
            mBMIValue = mBMI.getBmi();
            mBMIStateIndex = Utils.getBMIStateArrayIndex(mBMIValue);
            mHeightEt.setText(mBMI.getHeight().toString());
            mWeightEt.setText(mBMI.getWeight().toString());
            mBMITime.setTimeInMillis(mBMI.getTimestamp().getTime());
            setupDatePickerDialog();
            setupDateTextView();
            bindBMICalcResult(mBMIValue,mBMIStateIndex);
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
                , R.string.medical_attention_save_realm_success
                , Toast.LENGTH_LONG)
                .show();
        getActivity().onBackPressed();
    }

    @Override
    public void showSaveErrorMessage() {
        Snackbar.make(mFab
                , R.string.medical_attention_save_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, v -> saveBMI())
                .show();
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
    public void bindBMICalcResult(double bmiResult, int bmiStateArrayIndex) {
        TypedArray ta = getContext().getResources().obtainTypedArray(R.array.bmi_colors);
        int colorId = ContextCompat.getColor(getContext(), ta.getResourceId(bmiStateArrayIndex, 0));
        ta.recycle();
        mBMIValue = bmiResult;
        mBMIStateIndex = bmiStateArrayIndex;
        mBMIValTv.setText(String.format("%.2f", Double.valueOf(bmiResult)));
        mBMIValTv.setTextColor(colorId);
        mBMIValTextTv.setText(getResources().getStringArray(R.array.bmi_texts)[bmiStateArrayIndex]);
        mBMIValTextTv.setTextColor(colorId);
        mBMIValIconIv.setImageDrawable(new IconicsDrawable(getContext()
                , getResources().getStringArray(R.array.bmi_icons)[bmiStateArrayIndex])
                .sizeDp(30)
                .color(colorId));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bmi_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBMI();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        saveBMI();
    }

    @OnClick(R.id.date_tv)
    public void onDateClick() {
        mDatePickerDialog.show();
    }

    @OnTextChanged(value = R.id.height_et, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onHeightTextChanged(CharSequence text) {
        mBMIEditPresenter.onHeightOrWeightChanged(text.toString(), mWeightEt.getText().toString());
    }

    @OnTextChanged(value = R.id.weight_et, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onWeightTextChanged(CharSequence text) {
        mBMIEditPresenter.onHeightOrWeightChanged(mHeightEt.getText().toString(), text.toString());
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_bmi);
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
                    mBMITime.set(Calendar.YEAR, year);
                    mBMITime.set(Calendar.MONTH, monthOfYear);
                    mBMITime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setupDateTextView();
                }
                , mBMITime.get(Calendar.YEAR)
                , mBMITime.get(Calendar.MONTH)
                , mBMITime.get(Calendar.DAY_OF_MONTH));
    }

    private void setupDateTextView() {
        mDateTv.setText(DateUtils.formatDateTime(getContext()
                ,mBMITime.getTimeInMillis()
                ,DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_SHOW_WEEKDAY
                        | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_ABBREV_MONTH
                        | DateUtils.FORMAT_ABBREV_WEEKDAY));
    }

    private void saveBMI() {
        if(TextUtils.isEmpty(mHeightEt.getText())){
            mHeightEt.setError(getString(R.string.required_field));
            mHeightEt.requestFocus();
        }else if(TextUtils.isEmpty(mWeightEt.getText())){
            mWeightEt.setError(getString(R.string.required_field));
            mWeightEt.requestFocus();
        }else{
            BMI bmi = new BMI(
                    Double.valueOf(mHeightEt.getText().toString())
                    ,Double.valueOf(mWeightEt.getText().toString())
                    ,mBMIValue
                    ,mBMITime.getTime());
            if (mBMI == null) {
                mBMIEditPresenter.addRealmObject(bmi);
            } else {
                mBMIEditPresenter.editRealmObject(mBMI.getId(), bmi);
            }
        }
    }

}
