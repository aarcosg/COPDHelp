package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.bumptech.glide.Glide;
import com.google.api.services.youtube.model.Video;
import com.mikepenz.fastadapter.items.GenericAbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoItem extends GenericAbstractItem<Video, VideoItem, VideoItem.ViewHolder> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public VideoItem(Video video) {
        super(video);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_video_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_video;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        Context context = viewHolder.itemView.getContext();
        viewHolder.videoTitle.setText(getModel().getSnippet().getTitle());
        viewHolder.videoDescription.setText(getModel().getSnippet().getDescription());
        Glide.with(context).load(getModel().getSnippet().getThumbnails().getMedium().getUrl()).into(viewHolder.videoThumbnail);
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
        @Bind(R.id.video_thumbnail)
        ImageView videoThumbnail;
        @Bind(R.id.video_title)
        TextView videoTitle;
        @Bind(R.id.video_description)
        TextView videoDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}