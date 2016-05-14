package com.aarcosg.copdhelp.ui.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final float mVerticalSpaceHeight;

    public VerticalSpaceItemDecoration(float verticalSpaceHeight) {
        this.mVerticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = Math.round(mVerticalSpaceHeight);
        if(parent.getChildAdapterPosition(view) == 0){
            outRect.top = Math.round(mVerticalSpaceHeight);
        }
    }
}