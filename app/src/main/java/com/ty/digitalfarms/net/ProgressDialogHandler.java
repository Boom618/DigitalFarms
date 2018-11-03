package com.ty.digitalfarms.net;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.ty.digitalfarms.R;
import com.ty.digitalfarms.ui.view.CustomDialog;


/**
 * description:进度提示框
 * author: XingZheng
 * date: 2016/11/22.
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG=0;
    public static final int DISMISS_PROGRESS_DIALOG=1;

    private CustomDialog pd;
    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context,ProgressCancelListener listener,boolean cancelable){
        super();
        this.context=context;
        this.mProgressCancelListener=listener;
        this.cancelable=cancelable;
    }

    private void initProgressDialog(){
        if (pd == null) {
            pd = new CustomDialog(context, R.style.CustomDialog);
            pd.setCancelable(true);

            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }

            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissProgressDialog(){
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
