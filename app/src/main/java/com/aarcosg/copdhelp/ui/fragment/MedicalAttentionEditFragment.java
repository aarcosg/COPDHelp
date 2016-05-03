package com.aarcosg.copdhelp.ui.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.api.entity.MedicalAttentionEntity;
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
    private static final String EXTRA_MEDICAL_ATTENTION = "extra_medical_attention";

    @Inject
    MedicalAttentionEditPresenter mMedicalAttentionEditPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.type_spinner)
    Spinner mTypeSpinner;
    @Bind(R.id.note_et)
    EditText mNoteEt;
    static TextView mDateTv;
    static TextView mTimeTv;

    private static Calendar mMedicalAttentionTime;
    private MedicalAttentionEntity mMedicalAttentionEntity;

    public static MedicalAttentionEditFragment newInstance() {
        MedicalAttentionEditFragment fragment = new MedicalAttentionEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(MedicalAttentionEntity medicalAttentionEntity) {
        MedicalAttentionEditFragment fragment = new MedicalAttentionEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_MEDICAL_ATTENTION, medicalAttentionEntity);
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
        if (!getArguments().isEmpty() && getArguments().containsKey(EXTRA_MEDICAL_ATTENTION)) {
            mMedicalAttentionEntity = getArguments().getParcelable(EXTRA_MEDICAL_ATTENTION);
        }
        mMedicalAttentionTime = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_medical_attention_edit, container, false);
        ButterKnife.bind(this, fragmentView);
        mDateTv = ButterKnife.findById(fragmentView,R.id.date_tv);
        mTimeTv = ButterKnife.findById(fragmentView,R.id.time_tv);
        setupToolbar();
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.medical_attention_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                Toast.makeText(getContext(), R.string.save, Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.date_tv)
    public void onDateClick(){
        new DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
    }

    @OnClick(R.id.time_tv)
    public void onTimeClick(){
        new TimePickerFragment().show(getSupportFragmentManager(), "timePicker");
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.title_fragment_medical_attention);
        if (mMedicalAttentionEntity == null) {
            mToolbar.setSubtitle(R.string.add);
        } else {
            mToolbar.setSubtitle(R.string.edit);
        }
        mToolbar.setNavigationIcon(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_clear)
                .color(Color.WHITE)
                .sizeDp(16));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mTimeTv.setText(getString(R.string.time_string, hourOfDay, String.format("%02d", minute)));
            mMedicalAttentionTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mMedicalAttentionTime.set(Calendar.MINUTE, minute);
        }
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
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
