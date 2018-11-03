package com.ty.digitalfarms.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * author: XingZheng
 * date: 2016/9/12.
 * 轮播图Adapter
 */
public class CarouselAdapter extends PagerAdapter {

    private List<ImageView> views;

    public CarouselAdapter(List<ImageView> views) {
        this.views = views;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //对Viewpager页号求模去除View列表中要显示的项  
        position %= views.size();
        if (position<0) {
            position = views.size() + position;
        }
        ImageView view = views.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。  

        ViewParent viewParent = view.getParent();
        if (viewParent!=null){
            ViewGroup parent = (ViewGroup)viewParent;
            parent.removeView(view);
        }
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return views == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
