package com.ty.digitalfarms.ui.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * description:
 * author: XingZheng
 * date: 2016/11/21.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean isGridManager;

    public SpaceItemDecoration(int space, boolean isGridManager) {
        this.space = space;
        this.isGridManager = isGridManager;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (isGridManager) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;

        } else {
            if (parent.getChildAdapterPosition(view) != 0)
                outRect.top = space;
        }
    }
}