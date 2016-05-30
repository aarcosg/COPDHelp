package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AchievementItem extends AbstractItem<AchievementItem, AchievementItem.ViewHolder> implements Parcelable {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public int mAchievementId;
    public @DrawableRes int mAchievementIconResId;
    public boolean mCompleted;
    public String mState;

    public AchievementItem() {
    }

    public AchievementItem(int achievementId, @DrawableRes int achievementIconResId, boolean completed, @Nullable String state) {
        this.mAchievementId = achievementId;
        this.mAchievementIconResId = achievementIconResId;
        this.mCompleted = completed;
        this.mState = state;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_achievement_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_achievement;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        Context context = viewHolder.itemView.getContext();
        viewHolder.image.setImageResource(mAchievementIconResId);
        if(!mCompleted){
            viewHolder.image.setColorFilter(
                    ContextCompat.getColor(context,R.color.achievement_not_completed_tint)
                    , PorterDuff.Mode.SRC_ATOP);
        }else{
            if(mState != null){
                viewHolder.state.setText(mState);
                viewHolder.state.setTextColor(ContextCompat.getColor(context,R.color.md_teal_500));
            }
        }
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
        protected View view;
        @Bind(R.id.achievement_image)
        ImageView image;
        @Bind(R.id.achievement_state_text)
        TextView state;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mAchievementId);
        dest.writeInt(this.mAchievementIconResId);
        dest.writeByte(this.mCompleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.mState);
    }

    protected AchievementItem(Parcel in) {
        this.mAchievementId = in.readInt();
        this.mAchievementIconResId = in.readInt();
        this.mCompleted = in.readByte() != 0;
        this.mState = in.readString();
    }

    public static final Creator<AchievementItem> CREATOR = new Creator<AchievementItem>() {
        @Override
        public AchievementItem createFromParcel(Parcel source) {
            return new AchievementItem(source);
        }

        @Override
        public AchievementItem[] newArray(int size) {
            return new AchievementItem[size];
        }
    };
}