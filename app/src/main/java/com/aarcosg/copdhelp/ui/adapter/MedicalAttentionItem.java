package com.aarcosg.copdhelp.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MedicalAttentionItem extends AbstractItem<MedicalAttentionItem, MedicalAttentionItem.ViewHolder> {

    private Integer mId;
    private Integer mType;
    private Date mDate;
    private String mNote;

    public MedicalAttentionItem(Integer mId, Integer mType, Date mDate, String mNote) {
        this.mId = mId;
        this.mType = mType;
        this.mDate = mDate;
        this.mNote = mNote;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_medical_attention_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.medical_attention_item;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        IconicsDrawable iconDrawable = new IconicsDrawable(viewHolder.itemView.getContext())
                .icon(CommunityMaterial.Icon.cmd_stethoscope)
                .sizeDp(20);
        switch (mType){
            //Check up
            case 0:
                iconDrawable.color(Color.BLUE);
                break;
            //Emergency
            case 1:
                iconDrawable.color(Color.RED);
                break;
        }
        viewHolder.iconIv.setImageDrawable(iconDrawable);
        viewHolder.typeTv.setText(
                viewHolder.itemView.getContext().getResources()
                        .getStringArray(R.array.medical_attention_type)[mType]
        );
        viewHolder.dateTv.setText(DateUtils.getRelativeTimeSpanString(
                mDate.getTime(),System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS)
        );
        viewHolder.noteTv.setText(mNote);
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