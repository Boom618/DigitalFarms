package com.ty.digitalfarms.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/11/15.
 */

public class BaseFragment extends Fragment {
    // 标识view 是否初始化完成
    protected boolean isViewInit = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
