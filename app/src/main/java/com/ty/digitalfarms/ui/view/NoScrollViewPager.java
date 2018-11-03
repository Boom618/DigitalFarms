package com.ty.digitalfarms.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以设置是否滑动的ViewPager
 */
public class NoScrollViewPager extends ViewPager {

    private boolean noScroll=false;//false:可以滑动；true：不可以滑动

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //返回false禁止滑动
        return !noScroll && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //返回false禁止滑动
        return !noScroll && super.onInterceptTouchEvent(ev);
    }
}
