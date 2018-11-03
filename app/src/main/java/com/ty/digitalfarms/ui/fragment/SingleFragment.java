package com.ty.digitalfarms.ui.fragment;

import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.RealPlayCallBack;
import com.ty.digitalfarms.net.exception.HikVisionError;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.DeviceInfo;
import com.ty.digitalfarms.constant.HCNetSDKConstant;
import com.ty.digitalfarms.util.UIUtils;

import org.MediaPlayer.PlayM4.Player;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/13.
 */

public class SingleFragment extends ViewPagerFragment {

    private static final String TAG = "Fragment";
    @BindView(R.id.s_view)
    SurfaceView sView;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.load)
    ProgressBar load;


    private NET_DVR_DEVICEINFO_V30 hikDeviceInfo = null;

    private int hikLoginId = -1; // return by NET_DVR_Login_v30
    private int nPort = -1;
    private int hikPort = -1;
    private int hikPlayID = -1;

    private DeviceInfo.ResultBean cameraInfo;
    private String strIP;
    private String strUser;
    private String strPwd;
    private String toastStr;
    private LoginTask task;

    public static SingleFragment newInstance(DeviceInfo.ResultBean bean, int count,
                                             int currentPosition) {
        Bundle args = new Bundle();
        args.putInt("currentPosition", currentPosition);
        args.putInt("count", count);
        args.putParcelable("cameraInfo", bean);
        SingleFragment fragment = new SingleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_video, null);
        ButterKnife.bind(this, view);
        initSdk();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new LoginTask();
                task.execute();// 执行
            }
        });
        sView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSurfaceViewClick();
                }
            }
        });
        initData();
        return view;
    }

    @Override
    protected void loadData() {
        if (hikLoginId < 0) {
            sView.getHolder().addCallback(surfaceCallback);
            // 实例化异步任务
            task = new LoginTask();
            task.execute();// 执行
        }
    }

    private void initData() {
        Bundle bundle = getArguments();
        cameraInfo = bundle.getParcelable("cameraInfo");
        int count = bundle.getInt("count", 0);
        int currentPosition = bundle.getInt("currentPosition", -1);
        strIP = cameraInfo.getAddressIP();
        nPort = Integer.parseInt(cameraInfo.getServerPort());
        strUser = cameraInfo.getUserName();
        strPwd = cameraInfo.getUserPwd();
    }


    private boolean initSdk() {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            Log.e(HCNetSDKConstant.HCNETSDK_TAG, HCNetSDKConstant.INIT_FAILE);
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
        return true;
    }

    private int loginDevice() {
        // get instance
        hikDeviceInfo = new NET_DVR_DEVICEINFO_V30();

        int loginId = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort, strUser, strPwd, hikDeviceInfo);

        if (loginId < 0) {
            Log.e(TAG, "loginDevice--" + HikVisionError.errorMsg(HCNetSDK.getInstance().NET_DVR_GetLastError()));
            toastStr = HikVisionError.errorMsg(HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        return loginId;
    }

    private void startSinglePreview() {
        RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
        if (fRealDataCallBack == null) {
            Log.e(TAG, "fRealDataCallBack object is failed!");
            return;
        }

        NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
        previewInfo.lChannel = 1; // 预览的设备通道
        // 码流类型：0-主码流，1-子码流，2-码流3
        previewInfo.dwStreamType = 1;
        previewInfo.bBlocked = 1;
        hikPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(hikLoginId, previewInfo, fRealDataCallBack);

        if (hikPlayID < 0) {
            Log.e(TAG, HikVisionError.errorMsg(HCNetSDK.getInstance().NET_DVR_GetLastError()));
            return;
        }
    }

    // 异常回调
    private ExceptionCallBack getExceptionCbf() {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                Log.e(TAG, "异常类型：" + iType);
            }
        };
        return oExceptionCbf;
    }

    private RealPlayCallBack getRealPlayerCbf() {
        RealPlayCallBack cbf = new RealPlayCallBack() {
            public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer,
                                          int iDataSize) {
                // Log.e("TAG","processRealData----"+iDataSize);
                SingleFragment.this.processRealData(iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    public void processRealData(int iDataType, byte[] pDataBuffer, int iDataSize,
                                int iStreamMode) {

        if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
            if (hikPort >= 0) {
                return;
            }
            hikPort = Player.getInstance().getPort();
            if (hikPort == -1) {
                // Log.e(TAG, "processRealData--111" + HikVisionError.errorMsg(Player.getInstance().getLastError(hikPort)));
                UIUtils.showToast(HikVisionError.errorMsg(Player.getInstance().getLastError(hikPort)));
                return;
            }
            if (iDataSize > 0) {
                if (!Player.getInstance().setStreamOpenMode(hikPort, iStreamMode)) {
                    //  Log.e(TAG, "processRealData--222" + HCNetSDKConstant.SETSTREAM_FAILED);
                    return;
                }
                if (!Player.getInstance().openStream(hikPort, pDataBuffer, iDataSize, 10 * 1024 * 1024)) {
                    //  Log.e(TAG, "processRealData--333" + HCNetSDKConstant.OPENSTREAM_FAILED);
                    return;
                }
                if (sView != null) {
                    Player.getInstance().play(hikPort, sView.getHolder());
                }
            }
        } else {
            if (!Player.getInstance().inputData(hikPort, pDataBuffer, iDataSize)) {
            }

        }
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        // [1]
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            sView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            if (-1 == hikPort) {
                return;
            }
            Surface surface = holder.getSurface();
            if (surface.isValid()) {
                if (!Player.getInstance().setVideoWindow(hikPort, 0, holder)) {
                    Log.e(TAG, "sur-----" + "Player setVideoWindow failed!");
                    Log.e(TAG, "创建失败" + hikPort);
                }
            } else {
                Log.e(TAG, "sur---" + "创建失败" + hikPort);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            //  Log.i(TAG, "sur---" + "Player setVideoWindow release!" + hikPort);
            if (-1 == hikPort) {
                return;
            }
            if (holder.getSurface().isValid()) {
                if (!Player.getInstance().setVideoWindow(hikPort, 0, null)) {
                    Log.e(TAG, "sur---" + "Player setVideoWindow failed!");
                }
            }
        }
    };

    class LoginTask extends AsyncTask<Void, Void, Void> {
        @Override
        // 必须要重写的方法
        protected Void doInBackground(Void... params) {

            publishProgress();// 手动调用onProgressUpdate方法，传入进度值
            // 登录设备操作
            try {
                if (hikLoginId < 0) {
                    // doConnServer on the device
                    hikLoginId = loginDevice();

                    setLoginId(hikLoginId);

                    //  Log.e("TAG","hikLoginId==="+hikLoginId);
                    if (hikLoginId < 0) {
                        Log.e(TAG, "doInBackground--" + HCNetSDKConstant.LOGIN_FAILED);
                        return null;
                    }
                    // get instance of exception callback and set
                    ExceptionCallBack exceptionCbf = getExceptionCbf();
                    if (exceptionCbf == null) {
                        //  Log.e(TAG, "doInBackground--" + HCNetSDKConstant.EXCEPTIONCALLBACK_FAILED + "--" + getLastError());
                        return null;
                    }

                    if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(exceptionCbf)) {
                        //   Log.e(TAG, "doInBackground--" + HCNetSDKConstant.SETEXCEPTIONCALLBACK_FAILED + "--" + getLastError());
                        return null;
                    }
                    // Thread.sleep(3000);
                    startSinglePreview();
                } else {
                    if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(hikLoginId)) {
                        //   Log.e(TAG, "doInBackground--" + HCNetSDKConstant.LOGOUT_FAILED + "--" + getLastError());
                        return null;
                    }
                    hikLoginId = -1;
                }
            } catch (Exception err) {
                Log.e(TAG, "AsyncTask错误" + err.toString());
            }
            return null;
        }

        @Override
        // 预处理
        protected void onPreExecute() {
            load.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        // 输出异步处理后结果
        protected void onPostExecute(Void aVoid) {
            load.setVisibility(View.GONE);
            if (hikLoginId < 0 || hikPlayID < 0) {
                play.setVisibility(View.VISIBLE);
                UIUtils.showToast(toastStr);
                //重连
                // HCNetSDK.getInstance().NET_DVR_SetReconnect(3000,true);
                return;
            } else {
                // startSinglePreview();
            }
            super.onPostExecute(aVoid);
        }

        @Override
        // 获取进度，更新进度条
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doCancel();
    }

    protected void doCancel() {
        if (hikPort != -1) {
            Player.getInstance().closeStream(hikPort);//关闭端口
            Player.getInstance().freePort(hikPort);//释放流
            Player.getInstance().stop(hikPort);//停止播放
            hikPort = -1;
        }
        boolean b = HCNetSDK.getInstance().NET_DVR_StopRealPlay(hikPlayID);
        if (hikPlayID > 0 && !b) {
            Log.e(TAG, HCNetSDKConstant.STOPREALPLAY + "--" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        hikPlayID = -1;
        if (hikLoginId > 0 && !HCNetSDK.getInstance().NET_DVR_Logout_V30(hikLoginId)) {
            Log.e(TAG, HCNetSDKConstant.LOGOUT_FAILED + "--" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        sView = null;
        hikLoginId = -1;
        //释放SDK资源
        if (!HCNetSDK.getInstance().NET_DVR_Cleanup()) {
            Log.e(TAG, HCNetSDKConstant.CLEAN_UP + "---" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        //释放MyAsyncTask异步任务
        if (null != task && !task.isCancelled()) {
            task.cancel(true);
        }
        task = null;
        super.onDestroy();
    }

    public int getLoginId() {
        return hikLoginId;
    }

    public void setLoginId(int loginId) {
        this.hikLoginId = loginId;
    }

    public int getPlayId() {
        return hikPlayID;
    }

    public int getHikPort() {
        return hikPort;
    }


    private SurfaceViewClickListener listener;

    public interface SurfaceViewClickListener {
        void onSurfaceViewClick();
    }

    public void setSurfaceViewClickListener(SurfaceViewClickListener listener) {
        this.listener = listener;
    }
}
