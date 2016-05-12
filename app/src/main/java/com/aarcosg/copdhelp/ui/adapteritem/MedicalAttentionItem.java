package com.aarcosg.copdhelp.ui.adapteritem;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fastadapter.items.GenericAbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MedicalAttentionItem extends GenericAbstractItem<MedicalAttention, MedicalAttentionItem, MedicalAttentionItem.ViewHolder> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public MedicalAttentionItem(MedicalAttention medicalAttention) {
        super(medicalAttention);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_medical_attention_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_medical_attention;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        IconicsDrawable iconDrawable = new IconicsDrawable(viewHolder.itemView.getContext())
                .icon(CommunityMaterial.Icon.cmd_stethoscope)
                .sizeDp(20);
        switch (getModel().getType()){
            case MedicalAttention.TYPE_CHECKUP:
                iconDrawable.color(
                        ContextCompat.getColor(viewHolder.itemView.getContext(),
                                R.color.md_blue_600));
                break;
            case MedicalAttention.TYPE_EMERGENCY:
                iconDrawable.color(
                        ContextCompat.getColor(viewHolder.itemView.getContext(),
                                R.color.md_deep_orange_600));
                break;
        }
        viewHolder.iconIv.setImageDrawable(iconDrawable);
        viewHolder.typeTv.setText(
                viewHolder.itemView.getContext().getResources()
                        .getStringArray(R.array.medical_attention_type)[getModel().getType()]
        );
        viewHolder.dateTv.setText(DateUtils.getRelativeTimeSpanString(
                getModel().getTimestamp().getTime(),System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS)
        );
        viewHolder.noteTv.setText(getModel().getNote());
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

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.icon_iv)
        ImageView iconIv;
        @Bind(R.id.type_tv)
        TextView typeTv;
        @Bind(R.id.note_tv)
        TextView noteTv;
        @Bind(R.id.date_tv)
        TextView dateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}