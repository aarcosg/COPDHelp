package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fastadapter.items.GenericAbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BMIItem extends GenericAbstractItem<BMI, BMIItem, BMIItem.ViewHolder> {

    private static final String DECIMAL_FORMAT = "%.2f";
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
        Context context = viewHolder.itemView.getContext();

        IconicsDrawable iconDrawable = new IconicsDrawable(context)
                .sizeDp(20);
        iconDrawable
                .icon(CommunityMaterial.Icon.cmd_scale_bathroom)
                .color(ContextCompat.getColor(context, R.color.md_blue_600));
        viewHolder.iconIv.setImageDrawable(iconDrawable);

        viewHolder.bmiTv.setText(String.format(DECIMAL_FORMAT,getModel().getBmi()));
        viewHolder.heightTv.setText(getModel().getHeight().toString());
        viewHolder.weightTv.setText(String.format(DECIMAL_FORMAT,getModel().getWeight()));
        viewHolder.dateTv.setText(DateUtils.getRelativeTimeSpanString(
                getModel().getTimestamp().getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS)
        );
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
        @Bind(R.id.bmi_tv)
        TextView bmiTv;
        @Bind(R.id.height_tv)
        TextView heightTv;
        @Bind(R.id.weight_tv)
        TextView weightTv;
        @Bind(R.id.date_tv)
        TextView dateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}