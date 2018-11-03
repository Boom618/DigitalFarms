package com.ty.digitalfarms.ui.recylervideo;

import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
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
import com.ty.digitalfarms.constant.ConstantUtil;
import com.ty.digitalfarms.constant.HCNetSDKConstant;
import com.ty.digitalfarms.util.UIUtils;

import org.MediaPlayer.PlayM4.Player;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerVideoActivity extends AppCompatActivity {

    @BindView(R.id.rv_video)
    RecyclerView rvVideo;

    private static final String TAG = "RecyclerViewVideo";

    private String companyNo;
    private List<DeviceInfo.ResultBean> deviceList;
    private List<DeviceInfo.ResultBean> devicesListCopy = new ArrayList<>();
    private int position;
    private int preIndex = -1;//上一次位置

    private int hikLoginId = -1; // return by NET_DVR_Login_v30
    private int nPort = -1;
    private int hikPort = -1;
    private int hikPlayID;

    private String strIP;
    private String strUser;
    private String strPwd;

    private SurfaceView sView;
    private ImageView play;
    private ProgressBar load;
    private String toastStr;

    private LoginTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_video_ctrl);
        ButterKnife.bind(this);
        initSdk();
        initInfo();
        initView();
    }

    private void initInfo() {
        companyNo = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE).getString(ConstantUtil.SP_COMPANY_NO, "");
        deviceList = getIntent().getParcelableArrayListExtra("deviceList");
        position = getIntent().getIntExtra("position", -1);
    }

    private void initView() {
        rvVideo.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        final CardRvAdapter adapter = new CardRvAdapter(deviceList);
        rvVideo.setAdapter(adapter);
        CustomSnapHelper mMySnapHelper = new CustomSnapHelper();
        mMySnapHelper.attachToRecyclerView(rvVideo);
        rvVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    //获取第一个完全可见的条目位置
                    int visibleItemPos = manager.findFirstCompletelyVisibleItemPosition();
                    if (visibleItemPos != -1) {
                        if (preIndex == -1) {
                            Log.e("TAG", "onScrollStateChanged----" + deviceList.get(visibleItemPos).getTag()
                                    + "----" + visibleItemPos);
                            preIndex = visibleItemPos;
                        } else {
                            if (preIndex != visibleItemPos) {
                                Log.e("TAG", "onScrollStateChanged----" + deviceList.get(visibleItemPos).getTag()
                                        + "----" + visibleItemPos);
                                doCancel();
                                preIndex = visibleItemPos;
                            }
                        }


                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });
        rvVideo.smoothScrollToPosition(position);
        adapter.setButtonClickListener(new CardRvAdapter.ButtonClickListener() {
            @Override
            public void play(DeviceInfo.ResultBean bean, int position, SurfaceView surfaceView,
                             ProgressBar ivLoad, ImageView ivPlay) {
                sView = surfaceView;
                load = ivLoad;
                play = ivPlay;
                strIP = bean.getAddressIP();
                strUser = bean.getUserName();
                strPwd = bean.getUserPwd();
                sView.getHolder().addCallback(surfaceCallback);
                task = new LoginTask();
                task.execute();// 执行

            }
        });
    }


    private boolean initSdk() {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            Log.e(TAG, HCNetSDKConstant.INIT_FAILE);
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
        return true;
    }

    private int loginDevice() {
        // get instance
        NET_DVR_DEVICEINFO_V30 hikDeviceInfo = new NET_DVR_DEVICEINFO_V30();

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
        previewInfo.dwStreamType = 1; // 码流类型：0-主码流，1-子码流，2-码流3，3-码流4
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
                RecyclerVideoActivity.this.processRealData(iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
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
                if (!Player.getInstance().play(hikPort, sView.getHolder())) {
                    // Log.e(TAG, "processRealData--444" + HCNetSDKConstant.PLAY_FAILED);
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
            // Log.i(TAG, "sur---" + "surface is created" + hikPort);
            if (-1 == hikPort) {
                return;
            }
            Surface surface = holder.getSurface();
            if (surface.isValid()) {
                Log.e(TAG, "创建成功" + hikPort);
                if (!Player.getInstance().setVideoWindow(hikPort, 0, holder)) {
                    Log.e(TAG, "sur-----" + "Player setVideoWindow failed!");
                    Log.e(TAG, "创建失败" + hikPort);

                }
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

    protected void doCancel() {
        if (hikPort != -1) {
            Player.getInstance().closeStream(hikPort);//关闭端口
            Player.getInstance().freePort(hikPort);//释放流
            Player.getInstance().stop(hikPort);//停止播放
            hikPort = -1;
        }
        if (hikPlayID > 0 && !HCNetSDK.getInstance().NET_DVR_StopRealPlay(hikPlayID)) {
            Log.e(TAG, HCNetSDKConstant.STOPREALPLAY + "--" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        hikPlayID = -1;
        if (hikLoginId > 0 && !HCNetSDK.getInstance().NET_DVR_Logout_V30(hikLoginId)) {
            Log.e(TAG, HCNetSDKConstant.LOGOUT_FAILED + "--" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        sView = null;
        hikLoginId = -1;

        //释放MyAsyncTask异步任务
        if (null != task && !task.isCancelled()) {
            task.cancel(true);
        }
        task = null;
        super.onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放SDK资源
        if (!HCNetSDK.getInstance().NET_DVR_Cleanup()) {
            Log.e(TAG, HCNetSDKConstant.CLEAN_UP + "---" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
    }
}
