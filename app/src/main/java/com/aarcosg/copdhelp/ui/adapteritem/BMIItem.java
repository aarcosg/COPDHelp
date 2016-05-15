package com.aarcosg.copdhelp.ui.adapteritem;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.mikepenz.fastadapter.items.GenericAbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import butterknife.ButterKnife;

public class BMIItem extends GenericAbstractItem<BMI, BMIItem, BMIItem.ViewHolder> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public BMIItem(BMI bmi) {
        super(bmi);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_medical_attention_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_bmi;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        /*Context context = viewHolder.itemView.getContext();
        IconicsDrawable iconDrawable = new IconicsDrawable(context)
                .sizeDp(20);
        switch (getModel().getType()){
            case MedicalAttention.TYPE_CHECKUP:
                iconDrawable
                        .icon(CommunityMaterial.Icon.cmd_stethoscope)
                        .color(ContextCompat.getColor(context, R.color.md_blue_600));
                break;
            case MedicalAttention.TYPE_EMERGENCY:
                iconDrawable
                        .icon(CommunityMaterial.Icon.cmd_ambulance)
                        .color(ContextCompat.getColor(context, R.color.md_deep_orange_600));
                break;
        }
        viewHolder.iconIv.setImageDrawable(iconDrawable);
        viewHolder.typeTv.setText(
                context.getResources()
                        .getStringArray(R.array.medical_attention_type)[getModel().getType()]
        );
        viewHolder.dateTv.setText(DateUtils.getRelativeTimeSpanString(
                getModel().getTimestamp().getTime(),System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS)
        );
        viewHolder.noteTv.setText(TextUtils.isEmpty(getModel().getNote()) ?
                context.getString(R.string.empty_note) : getModel().getNote());
        viewHolder.itemView.setTag(getModel());*/
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

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        /*@Bind(R.id.icon_iv)
        ImageView iconIv;
        @Bind(R.id.type_tv)
        TextView typeTv;
        @Bind(R.id.note_tv)
        TextView noteTv;
        @Bind(R.id.date_tv)
        TextView dateTv;*/

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}