package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainGuideItem extends AbstractItem<MainGuideItem, MainGuideItem.ViewHolder> {
    
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    private Drawable mImage;
    private String mTitle;
    private String mSubTitle;

    public MainGuideItem withImage(Drawable image) {
        this.mImage = image;
        return this;
    }

    public MainGuideItem withTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public MainGuideItem withSubTitle(String subTitle) {
        this.mSubTitle = subTitle;
        return this;
    }


    @Override
    public int getType() {
        return R.id.fastadapter_main_guide_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_main_guide;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        Context context = viewHolder.itemView.getContext();
        viewHolder.title.setText(mTitle);
        viewHolder.image.setImageDrawable(mImage);
        if(TextUtils.isEmpty(mSubTitle)){
            viewHolder.subtitle.setVisibility(View.GONE);
        }else{
            viewHolder.subtitle.setText(mSubTitle);
        }

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        @Bind(R.id.image_iv)
        protected ImageView image;
        @Bind(R.id.title_tv)
        protected TextView title;
        @Bind(R.id.subtitle_tv)
        protected TextView subtitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

}