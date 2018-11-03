package com.ty.digitalfarms.ui.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * description:
 * author: XingZheng
 * date: 2016/11/21.
 */
public class SplitItemDecoration extends RecyclerView.ItemDecoration{

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
       outRect.set(0,0,0,1);
    }
}