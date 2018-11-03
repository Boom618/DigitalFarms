package com.ty.digitalfarms.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.ty.digitalfarms.ui.fragment.SingleFragment;

/**
 * ViewPager的适配器
 */

public class VideoPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
    private SparseArray<SingleFragment> mFragments;

    public VideoPagerAdapter(FragmentManager fm, SparseArray<SingleFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }


    @Override
    public Fragment getItem(int position) {
        SingleFragment testFragment=mFragments.valueAt(position);
        return testFragment;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

   /* *//**
     * 返回每一个item所有对应的key，在instantiateItem中 会根据这个item去查找是否已经存在这个item
     * 如果不存在则调用getItem创建
     * 存在则在FragmentManager中去find
     * 详情查看instantiateItem源码
     * @param position
     * @return
     *//*
    @Override
    public long getItemId(int position) {
        return mFragments.keyAt(position);
    }*/

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }
}
