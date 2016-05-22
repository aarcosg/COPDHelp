package com.aarcosg.copdhelp.ui.adapteritem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.Exercise;
import com.mikepenz.fastadapter.items.GenericAbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExerciseItem extends GenericAbstractItem<Exercise, ExerciseItem, ExerciseItem.ViewHolder> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public ExerciseItem(Exercise exercise) {
        super(exercise);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_exercise_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_exercise;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        Context context = viewHolder.itemView.getContext();
        viewHolder.iconIv.setImageDrawable(
                new IconicsDrawable(context
                        , context.getResources()
                            .getStringArray(R.array.exercise_type_icons)[getModel().getType()])
                        .colorRes(R.color.icon_dark)
                        .sizeDp(20)
        );
        viewHolder.typeTv.setText(
                context.getResources()
                        .getStringArray(R.array.exercise_type)[getModel().getType()]
        );
        viewHolder.durationTv.setText(String.valueOf(getModel().getDuration()));
        viewHolder.dateTv.setText(DateUtils.getRelativeTimeSpanString(
                getModel().getTimestamp().getTime(),System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS)
        );
        viewHolder.noteTv.setText(TextUtils.isEmpty(getModel().getNote()) ?
                context.getString(R.string.empty_note_alt) : getModel().getNote()
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
        @Bind(R.id.type_tv)
        TextView typeTv;
        @Bind(R.id.note_tv)
        TextView noteTv;
        @Bind(R.id.date_tv)
        TextView dateTv;
        @Bind(R.id.duration_tv)
        TextView durationTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}