package com.ty.digitalfarms.net;

import android.content.Context;

import com.zhouyou.http.exception.ApiException;

/**
 * description:
 * author: XingZheng
 * date: 2016/11/22.
 */
public abstract class ProgressSubscriber<T> extends BaseSubscriber<T> implements ProgressCancelListener {

    private ProgressDialogHandler mProgressDialogHandler;
    private boolean isShowDialog;

    public ProgressSubscriber(Context context, boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
        if (isShowDialog) {
            mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
        }
    }

    @Override
    public void onStart() {
        if (isShowDialog)
            showProgressDialog();
    }

    @Override
    public void onNext(T t) {}

    @Override
    public void onError(ApiException e) {
        if (isShowDialog) {
            dismissProgressDialog();
        }
    }

    @Override
    public void onCompleted() {
        if (isShowDialog)
            dismissProgressDialog();
    }

    @Override
    public void onCancelProgress() {
        //当已经订阅时，取消提示框就取消订阅
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }
}
