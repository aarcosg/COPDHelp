package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.mikepenz.fastadapter.items.GenericAbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MedicineReminderItem extends GenericAbstractItem<MedicineReminder, MedicineReminderItem, MedicineReminderItem.ViewHolder> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public MedicineReminderItem(MedicineReminder medicineReminder) {
        super(medicineReminder);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_medicine_reminder_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_medicine_reminder;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        Context context = viewHolder.itemView.getContext();
        viewHolder.reminderSwitch.setChecked(getModel().isEnabled());
        viewHolder.medicineTv.setText(getModel().getMedicine());
        viewHolder.doseTv.setText(getModel().getDose());
        viewHolder.nextMedicineTimeTv.setText(
                DateUtils.formatDateTime(context
                        ,getModel().getTimestamp().getTime()
                        ,DateUtils.FORMAT_SHOW_TIME)
        );
        viewHolder.frequencyTv.setText(
                String.format(context.getString(R.string.medicine_frecuency)
                        ,getModel().getFrequency() + " " + context.getString(R.string.hours)));
        viewHolder.itemView.setTag(getModel());
    }

    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }

    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.next_medicine_time_tv)
        TextView nextMedicineTimeTv;
        @Bind(R.id.medicine_tv)
        TextView medicineTv;
        @Bind(R.id.dose_tv)
        TextView doseTv;
        @Bind(R.id.frequency_tv)
        TextView frequencyTv;
        @Bind(R.id.reminder_switch)
        public SwitchCompat reminderSwitch;
        @Bind(R.id.delete_btn)
        public ImageButton deleteBtn;
        @Bind(R.id.edit_btn)
        public ImageButton editBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}