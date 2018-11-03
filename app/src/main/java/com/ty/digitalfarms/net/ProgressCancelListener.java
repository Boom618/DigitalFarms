package com.ty.digitalfarms.net;

/**
 * description:取消Progress提示框时取消Http请求
 * author: XingZheng
 * date: 2016/11/22.
 */
public interface ProgressCancelListener {
    /**
     * 取消Progress提示框
     */
    void onCancelProgress();
}
