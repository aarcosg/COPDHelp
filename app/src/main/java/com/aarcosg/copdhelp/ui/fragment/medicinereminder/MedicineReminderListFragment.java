package com.aarcosg.copdhelp.ui.fragment.medicinereminder;


import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.medicinereminder.MedicineReminderListPresenter;
import com.aarcosg.copdhelp.mvp.view.medicinereminder.MedicineReminderListView;
import com.aarcosg.copdhelp.ui.adapteritem.MedicineReminderItem;
import com.aarcosg.copdhelp.ui.decorator.VerticalSpaceItemDecoration;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.aarcosg.copdhelp.receiver.RemindersHelper;
import com.aarcosg.copdhelp.utils.Utils;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.GenericItemAdapter;
import com.mikepenz.fastadapter.helpers.ClickListenerHelper;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class MedicineReminderListFragment extends BaseFragment implements MedicineReminderListView {

    private static final String TAG = MedicineReminderListFragment.class.getCanonicalName();

    @Inject
    MedicineReminderListPresenter mMedicineReminderListPresenter;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.empty_view_fl)
    FrameLayout mEmptyView;
    @Bind(R.id.empty_view_icon_iv)
    ImageView mEmptyViewIcon;
    @Bind((R.id.empty_view_tv))
    TextView mEmptyViewText;

    private FastAdapter mFastAdapter;
    private GenericItemAdapter<MedicineReminder, MedicineReminderItem> mItemAdapter;
    private RealmResults<MedicineReminder> mMedicineReminders;
    private AlertDialog mMedicineReminderFormDialog;
    private Calendar mMedicineReminderTime;
    private TimePickerDialog mTimePickerDialog;
    private TextView mFirstTimeTv;
    private Long mMedicineReminderId;

    public static MedicineReminderListFragment newInstance() {
        MedicineReminderListFragment fragment = new MedicineReminderListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MedicineReminderListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mMedicineReminderListPresenter.setView(this);
        mMedicineReminderTime = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_medicine_reminder_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        setupEmptyView();
        setupMedicineReminderFormDialog();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMedicineReminderListPresenter.loadAllFromRealm();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMedicineReminderListPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMedicineReminders != null) {
            mMedicineReminders.removeChangeListeners();
        }
    }

    @Override
    public void bindAll(RealmResults<MedicineReminder> MedicineReminders) {
        mMedicineReminders = MedicineReminders;
        checkEmptyResults();
        mMedicineReminders.addChangeListener(element -> {
            mItemAdapter.setNewModel(MedicineReminders);
            checkEmptyResults();
            mFastAdapter.notifyAdapterDataSetChanged();
        });
        mItemAdapter.addModel(mMedicineReminders);
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
                , R.string.medicine_reminder_load_realm_error
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.retry,
                        v -> mMedicineReminderListPresenter.loadAllFromRealm())
                .show();
    }

    @Override
    public void showRemoveRealmSuccessMessage() {
        Snackbar.make(mFab
                , R.string.medicine_reminder_remove_realm_success
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showRemoveRealmErrorMessage() {
        Snackbar.make(mFab
                , R.string.medicine_reminder_remove_realm_error
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showSaveSuccessMessage() {
        Snackbar.make(mFab
                , R.string.medicine_reminder_save_realm_success
                , Snackbar.LENGTH_LONG)
                .show();
        mMedicineReminderFormDialog.dismiss();
    }

    @Override
    public void showSaveErrorMessage() {
        Snackbar.make(mFab
                , R.string.medicine_reminder_save_realm_error
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showReminderStateChangedSuccessMessage(boolean enable) {
        Snackbar.make(mFab
                , enable ? R.string.medicine_reminder_on : R.string.medicine_reminder_off
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void enableReminderAlarm(MedicineReminder medicineReminder) {
        RemindersHelper.getInstance(getContext().getApplicationContext()).setupReminder(medicineReminder);
    }

    @Override
    public void disableReminderAlarm(MedicineReminder medicineReminder) {
        RemindersHelper.getInstance(getContext().getApplicationContext()).cancelReminder(medicineReminder);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        mMedicineReminderFormDialog.show();
    }

    private void setupAdapter() {
        mItemAdapter = new GenericItemAdapter<>(MedicineReminderItem.class, MedicineReminder.class);
        mFastAdapter = new FastAdapter();
        ClickListenerHelper<MedicineReminderItem> clickListenerHelper = new ClickListenerHelper<MedicineReminderItem>(mFastAdapter);
        mFastAdapter.withOnCreateViewHolderListener(new FastAdapter.OnCreateViewHolderListener() {

            @Override
            public RecyclerView.ViewHolder onPreCreateViewHolder(ViewGroup parent, int viewType) {
                return mFastAdapter.getTypeInstance(viewType).getViewHolder(parent);
            }

            @Override
            public RecyclerView.ViewHolder onPostCreateViewHolder(RecyclerView.ViewHolder viewHolder) {
                clickListenerHelper.listen(viewHolder,((MedicineReminderItem.ViewHolder) viewHolder).editBtn, (v, position, item) -> {
                    mMedicineReminderFormDialog.show();
                    bindMedicineReminderItemToFormDialog(item);
                });
                clickListenerHelper.listen(viewHolder,((MedicineReminderItem.ViewHolder) viewHolder).deleteBtn, (v, position, item) -> {
                    showRemoveMedicineReminderDialog(item.getModel().getId());
                });
                clickListenerHelper.listen(viewHolder,((MedicineReminderItem.ViewHolder) viewHolder).reminderSwitch, (v, position, item) -> {
                    SwitchCompat reminderSwitch = (SwitchCompat)v;
                    mMedicineReminderListPresenter.enableMedicineReminder(item.getModel().getId(),reminderSwitch.isChecked());
                });
                return viewHolder;
            }

        });
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                getResources().getDimension(R.dimen.activity_vertical_margin)));
        mRecyclerView.setAdapter(mItemAdapter.wrap(mFastAdapter));
    }

    private void setupEmptyView() {
        mEmptyViewIcon.setImageDrawable(
                new IconicsDrawable(getContext())
                        .icon(CommunityMaterial.Icon.cmd_alarm)
                        .color(Color.GRAY)
                        .sizeDp(70)
        );
        mEmptyViewText.setText(getString(R.string.reminders_empty));
    }

    private void setupMedicineReminderFormDialog(){
        final View view = LayoutInflater.from(
                getContext()).inflate(R.layout.dialog_medicine_reminder_edit, null);
        mFirstTimeTv = ButterKnife.findById(view, R.id.first_time_tv);
        mFirstTimeTv.setOnClickListener(v -> mTimePickerDialog.show());
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(view);
        dialogBuilder.setTitle(getString(R.string.add_reminder));
        dialogBuilder.setPositiveButton(getString(R.string.save), (dialog, which) -> {
            Spinner medicineSpinner = ButterKnife.findById(view, R.id.medicine_spinner);
            Spinner doseSpinner = ButterKnife.findById(view, R.id.dose_spinner);
            Spinner frequencySpinner = ButterKnife.findById(view, R.id.frequency_spinner);
            saveMedicineReminder(
                    mMedicineReminderId
                    , (String)medicineSpinner.getSelectedItem()
                    , (String)doseSpinner.getSelectedItem()
                    , Integer.valueOf(frequencySpinner.getSelectedItem().toString())
            );
        });
        dialogBuilder.setNegativeButton(getString(android.R.string.cancel), null);
        mMedicineReminderFormDialog = dialogBuilder.create();
        mMedicineReminderFormDialog.setCanceledOnTouchOutside(false);
        mMedicineReminderFormDialog.setOnShowListener(dialog -> {
            setupTimePickerDialog();
            setupTimeTextView();
        });
        mMedicineReminderFormDialog.setOnCancelListener(dialog -> mMedicineReminderId = null);
        mMedicineReminderFormDialog.setOnDismissListener(dialog -> mMedicineReminderId = null);

    }

    private void setupTimePickerDialog() {
        mTimePickerDialog = new TimePickerDialog(
                getContext()
                , (view, hourOfDay, minute) -> {
                    mMedicineReminderTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mMedicineReminderTime.set(Calendar.MINUTE, minute);
                    setupTimeTextView();
                }
                , mMedicineReminderTime.get(Calendar.HOUR_OF_DAY)
                , mMedicineReminderTime.get(Calendar.MINUTE)
                , true);
    }

    private void setupTimeTextView() {
        if(mFirstTimeTv != null){
            mFirstTimeTv.setText(DateUtils.formatDateTime(getContext()
                    , mMedicineReminderTime.getTimeInMillis()
                    , DateUtils.FORMAT_SHOW_TIME));
        }
    }

    private void bindMedicineReminderItemToFormDialog(MedicineReminderItem item){
        Spinner medicineSpinner = ButterKnife.findById(mMedicineReminderFormDialog, R.id.medicine_spinner);
        Spinner doseSpinner = ButterKnife.findById(mMedicineReminderFormDialog, R.id.dose_spinner);
        Spinner frequencySpinner = ButterKnife.findById(mMedicineReminderFormDialog, R.id.frequency_spinner);

        medicineSpinner.setSelection(Utils.getSpinnerIndex(medicineSpinner,item.getModel().getMedicine()));
        doseSpinner.setSelection(Utils.getSpinnerIndex(doseSpinner,item.getModel().getDose()));
        frequencySpinner.setSelection(Utils.getSpinnerIndex(frequencySpinner,item.getModel().getFrequency()));
        mMedicineReminderTime.setTimeInMillis(item.getModel().getTimestamp().getTime());
        mMedicineReminderId = item.getModel().getId();
    }

    private void showRemoveMedicineReminderDialog(Long id) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getResources().getString(R.string.delete));
        dialogBuilder.setMessage(getResources().getString(R.string.medicine_reminder_ask_delete));
        dialogBuilder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            mMedicineReminderListPresenter.removeFromRealm(id);
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        dialogBuilder.show();
    }

    private void checkEmptyResults() {
        if (mMedicineReminders != null && !mMedicineReminders.isEmpty()) {
            hideEmptyView();
        } else {
            showEmptyView();
        }
    }

    private void saveMedicineReminder(@Nullable Long id, String medicine, String dose, Integer frequency) {
        MedicineReminder medicineReminder = new MedicineReminder(medicine,frequency,dose,mMedicineReminderTime.getTime());
        if(id == null){
            mMedicineReminderListPresenter.addRealmObject(medicineReminder);
        }else{
            mMedicineReminderListPresenter.editRealmObject(id,medicineReminder);
        }
    }

}
