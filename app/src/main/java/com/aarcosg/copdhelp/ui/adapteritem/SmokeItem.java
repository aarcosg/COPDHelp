package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.Smoke;
import com.mikepenz.fastadapter.items.GenericAbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SmokeItem extends GenericAbstractItem<Smoke, SmokeItem, SmokeItem.ViewHolder> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public SmokeItem(Smoke smoke) {
        super(smoke);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_smoke_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_smoke;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        Context context = viewHolder.itemView.getContext();
        viewHolder.dateTv.setText(DateUtils.getRelativeTimeSpanString(
                getModel().getTimestamp().getTime(),System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS)
        );
        viewHolder.quantityTv.setText(String.valueOf(getModel().getQuantity()));
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
        @Bind(R.id.quantity_tv)
        TextView quantityTv;
        @Bind(R.id.date_tv)
        TextView dateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}