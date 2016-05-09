package com.aarcosg.copdhelp.ui.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.di.components.MedicalAttentionComponent;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionEditPresenter;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionEditView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MedicalAttentionEditFragment extends BaseFragment implements MedicalAttentionEditView {

    private static final String TAG = MedicalAttentionEditFragment.class.getCanonicalName();
    private static final String EXTRA_MEDICAL_ATTENTION = "extra_medical_attention_id";

    @Inject
    MedicalAttentionEditPresenter mMedicalAttentionEditPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.type_spinner)
    Spinner mTypeSpinner;
    @Bind(R.id.note_et)
    EditText mNoteEt;
    @Bind(R.id.appbar)
    AppBarLayout mAppBar;

    private static TextView mDateTv;
    private static Calendar mMedicalAttentionTime;
    private MedicalAttention mMedicalAttention;

    public static MedicalAttentionEditFragment newInstance() {
        MedicalAttentionEditFragment fragment = new MedicalAttentionEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static MedicalAttentionEditFragment newInstance(Long medicalAttentionId) {
        MedicalAttentionEditFragment fragment = new MedicalAttentionEditFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_MEDICAL_ATTENTION, medicalAttentionId);
        fragment.setArguments(args);
        return fragment;
    }

    public MedicalAttentionEditFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MedicalAttentionComponent.class).inject(this);
        mMedicalAttentionEditPresenter.setView(this);
        mMedicalAttentionTime = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_medical_attention_edit, container, false);
        ButterKnife.bind(this, fragmentView);
        mDateTv = ButterKnife.findById(fragmentView, R.id.date_tv);
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        setDateTV(mMedicalAttentionTime);
        if (!getArguments().isEmpty() && getArguments().containsKey(EXTRA_MEDICAL_ATTENTION)) {
            mMedicalAttentionEditPresenter.loadMedicalAttention(
                    getArguments().getLong(EXTRA_MEDICAL_ATTENTION));
            mAppBar.setExpanded(false,true);
            mNoteEt.requestFocus();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMedicalAttentionEditPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(mDateTv);
        ButterKnife.unbind(this);
    }

    @Override
    public void bindMedicalAttention(MedicalAttention medicalAttention) {
        mMedicalAttention = medicalAttention;
        if (mMedicalAttention != null) {
            mTypeSpinner.setSelection(mMedicalAttention.getType());
            mMedicalAttentionTime.setTimeInMillis(mMedicalAttention.getDate().getTime());
            setDateTV(mMedicalAttentionTime);
            mNoteEt.setText(mMedicalAttention.getNote());
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
    public void showSaveRealmSuccessMessage() {
        Toast.makeText(getContext()
                , R.string.medical_attention_save_realm_success
                , Toast.LENGTH_LONG)
                .show();
        getActivity().onBackPressed();
    }

    @Override
    public void showSaveRealmErrorMessage() {
        Snackbar.make(mToolbar.getRootView()
                , R.string.medical_attention_save_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, v -> saveMedicalAttention())
                .show();
    }

    @Override
    public void showMedicalAttentionNotFoundError() {
        Toast.makeText(getContext()
                , R.string.medical_attention_not_found_realm_error
                , Toast.LENGTH_LONG)
                .show();
        getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.medical_attention_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveMedicalAttention();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        saveMedicalAttention();
    }

    @OnClick(R.id.date_tv)
    public void onDateClick() {
        new DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_medical_attention);
        mToolbar.setNavigationIcon(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_clear)
                .color(Color.WHITE)
                .sizeDp(16));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDateTV(Calendar calendar) {
        mDateTv.setText(getString(R.string.date_string,
                calendar.get(Calendar.DAY_OF_MONTH),
                String.format("%02d",
                        calendar.get(Calendar.MONTH) + 1),
                calendar.get(Calendar.YEAR))
        );
    }

    private void saveMedicalAttention() {
        MedicalAttention medicalAttention = new MedicalAttention(
                mTypeSpinner.getSelectedItemPosition(),
                mMedicalAttentionTime.getTime(),
                mNoteEt.getText().toString());
        if (mMedicalAttention == null) {
            mMedicalAttentionEditPresenter.addMedicalAttention(medicalAttention);
        } else {
            mMedicalAttentionEditPresenter.editMedicalAttention(mMedicalAttention.getId(), medicalAttention);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
            int year = mMedicalAttentionTime.get(Calendar.YEAR);
            int month = mMedicalAttentionTime.get(Calendar.MONTH);
            int day = mMedicalAttentionTime.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            mDateTv.setText(getString(R.string.date_string, day, String.format("%02d", month + 1), year));
            mMedicalAttentionTime.set(Calendar.YEAR, year);
            mMedicalAttentionTime.set(Calendar.MONTH, month);
            mMedicalAttentionTime.set(Calendar.DAY_OF_MONTH, day);
        }
    }

}
