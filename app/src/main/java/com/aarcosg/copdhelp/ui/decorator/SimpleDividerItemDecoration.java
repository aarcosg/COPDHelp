package com.aarcosg.copdhelp.ui.decorator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDivider;
    private final float mLeftMargin;
    private final float mRightMargin;
 
    public SimpleDividerItemDecoration(Context context, @DrawableRes int drawableId,
                                       @DimenRes float leftMargin, @DimenRes float rightMargin) {
        this.mDivider = ContextCompat.getDrawable(context, drawableId);
        this.mLeftMargin = leftMargin;
        this.mRightMargin = rightMargin;
    }
 
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + Math.round(mLeftMargin);
        int right = parent.getWidth() - parent.getPaddingRight() + Math.round(mRightMargin);
 
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
 
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
 
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
 
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}