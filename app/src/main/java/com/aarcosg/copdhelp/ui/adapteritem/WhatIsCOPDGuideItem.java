package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WhatIsCOPDGuideItem extends AbstractItem<WhatIsCOPDGuideItem, WhatIsCOPDGuideItem.ViewHolder> implements IExpandable<WhatIsCOPDGuideItem, IItem> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public String mTitle;
    public int mLayoutResId;

    private boolean mExpanded = false;

    public WhatIsCOPDGuideItem withTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public WhatIsCOPDGuideItem withLayoutRes(int layoutResId) {
        this.mLayoutResId = layoutResId;
        return this;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public WhatIsCOPDGuideItem withIsExpanded(boolean expanded) {
        mExpanded = expanded;
        return this;
    }

    @Override
    public WhatIsCOPDGuideItem withSubItems(List<IItem> subItems) {
        return null;
    }

    @Override
    public List<IItem> getSubItems() {
        return null;
    }

    final private FastAdapter.OnClickListener<WhatIsCOPDGuideItem> onClickListener = new FastAdapter.OnClickListener<WhatIsCOPDGuideItem>() {
        @Override
        public boolean onClick(View v, IAdapter adapter, WhatIsCOPDGuideItem item, int position) {
            if (item.getSubItems() != null) {
                if (!item.isExpanded()) {
                    ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(180).start();
                } else {
                    ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(0).start();
                }
                return true;
            }
            return false;
        }
    };

    @Override
    public FastAdapter.OnClickListener<WhatIsCOPDGuideItem> getOnItemClickListener() {
        return onClickListener;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_what_is_copd_guide_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_what_is_copd;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        Context context = viewHolder.itemView.getContext();

        viewHolder.title.setText(mTitle);

        viewHolder.expandIcon.clearAnimation();
        if (isExpanded()) {
            ViewCompat.setRotation(viewHolder.expandIcon, 0);
        } else {
            ViewCompat.setRotation(viewHolder.expandIcon, 180);
        }

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(mLayoutResId, viewHolder.descriptionLayout, false);

    }

    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected final View view;
        @Bind(R.id.title_tv)
        TextView title;
        @Bind(R.id.expand_icon)
        ImageView expandIcon;
        @Bind(R.id.description_layout)
        FrameLayout descriptionLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}