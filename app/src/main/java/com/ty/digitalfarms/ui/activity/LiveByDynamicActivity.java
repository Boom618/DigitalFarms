package com.ty.digitalfarms.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hik.mcrsdk.rtsp.LiveInfo;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.CameraInfoEx;
import com.hikvision.vmsnetsdk.RealPlayURL;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.hikvision.vmsnetsdk.netLayer.msp.deviceInfo.DeviceInfo;
import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.constant.ConstantUtil;
import com.ty.digitalfarms.constant.HikConstants;
import com.ty.digitalfarms.constant.HikPTZCommand;
import com.ty.digitalfarms.hikvision.data.TempData;
import com.ty.digitalfarms.hikvision.live.ConstantLive;
import com.ty.digitalfarms.hikvision.live.LiveCallBack;
import com.ty.digitalfarms.hikvision.live.LiveControl;
import com.ty.digitalfarms.hikvision.playback.PlayBackActivity;
import com.ty.digitalfarms.util.UIUtils;
import com.ty.digitalfarms.util.UtilAudioPlay;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveByDynamicActivity extends BaseActivity implements View.OnClickListener, LiveCallBack, View.OnTouchListener {

    private final static String TAG = "LiveByDynamicActivity";

    @BindView(R.id.iv_left)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.s_view)
    SurfaceView sufaceView;
    @BindView(R.id.play)
    ImageView ivStartPlay;
    @BindView(R.id.load)
    ProgressBar progressBar;
    @BindView(R.id.iv_northwest)
    ImageView ivNorthwest;
    @BindView(R.id.iv_north)
    ImageView ivNorth;
    @BindView(R.id.iv_northeast)
    ImageView ivNortheast;
    @BindView(R.id.iv_west)
    ImageView ivWest;
    @BindView(R.id.iv_auto)
    ImageView ivAuto;
    @BindView(R.id.iv_east)
    ImageView ivEast;
    @BindView(R.id.iv_southwest)
    ImageView ivSouthwest;
    @BindView(R.id.iv_south)
    ImageView ivSouth;
    @BindView(R.id.iv_southeast)
    ImageView ivSoutheast;
    @BindView(R.id.iv_zoom_out)
    ImageView ivZoomOut;
    @BindView(R.id.iv_zoom_in)
    ImageView ivZoomIn;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.ll_capture)
    LinearLayout llCapture;
    @BindView(R.id.iv_record)
    ImageView ivRecord;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.ll_record_tag)
    LinearLayout llRecordTag;
    @BindView(R.id.iv_record_point)
    ImageView ivRecordPoint;
    @BindView(R.id.timer)
    Chronometer timer;
    @BindView(R.id.ll_fluent)
    LinearLayout llFluent;
    @BindView(R.id.ll_stand)
    LinearLayout llStand;
    @BindView(R.id.ll_high)
    LinearLayout llHigh;
    @BindView(R.id.ll_stream_type)
    LinearLayout llStreamType;
    @BindView(R.id.ll_stream)
    LinearLayout llStream;
    @BindView(R.id.iv_all_screen)
    ImageView ivAllScreen;
    @BindView(R.id.iv_landscape_stream)
    ImageView ivLandscapeStream;
    @BindView(R.id.iv_landscape_camera)
    ImageView ivLandscapeCamera;
    @BindView(R.id.iv_landscape_record)
    ImageView ivLandscapeRecord;
    @BindView(R.id.ll_landscape_camera)
    LinearLayout llLandscapeCamera;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.ll_ctr_root)
    LinearLayout llCtrRoot;
    @BindView(R.id.rl_sv)
    RelativeLayout rlSv;
    @BindView(R.id.tv_right)
    TextView tvRight;


    private CameraInfo mCameraInfo;
    private ServInfo mServInfo;
    private VMSNetSDK mVmsNetSDK;
    private String mCameraID;
    private CameraInfoEx mCameraInfoEx;
    private boolean mCameraInfoResult = false;//获取监控点详情结果；true:成功，false:失败
    private String mDeviceId;//设备Id
    private DeviceInfo mDeviceInfo;//设备信息
    private boolean mDeviceInfoResult = false;//获取设备详情结果；true:成功，false:失败
    private String mDeviceLoginName;//设备登录名
    private String mDeviceLoginPsw;//设备登录密码
    private RealPlayURL mRealPlayURL;
    private LiveControl mLiveControl;
    private String mToken; // 获取播放Token
    private RtspClient mRtspClient;//RTSP sdk句柄
    private int mStreamType = ConstantLive.SUB_STREAM;//码流类型
    private boolean mIsRecord = false;//是否正在录像

    /**
     * 获取监控点信息成功
     */
    private final static int GET_CAMERA_DETAIL_INFO_SUCCESS = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GET_CAMERA_DETAIL_INFO_SUCCESS) {
                doStartPlay();
            }
        }
    };

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_live);
        StatusBarUtil.setColor(LiveByDynamicActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mCameraInfo = TempData.getIns().getCameraInfo();//监控点信息
        mServInfo = TempData.getIns().getLoginData();//登录成功返回的信息
        if (mCameraInfo == null || mServInfo == null) {
            UIUtils.showToast("设备信息获取失败,请重试");
            return;
        }
        tvTitle.setText(mCameraInfo.getName());

        mCameraID = mCameraInfo.getId();//监控点Id
        mCameraInfoEx = new CameraInfoEx();
        mCameraInfoEx.setId(mCameraID);

        mVmsNetSDK = VMSNetSDK.getInstance();
        if (mVmsNetSDK == null) {
            UIUtils.showToast("SDK初始化失败,请重试");
            return;
        }

        mRealPlayURL = new RealPlayURL();
        mLiveControl = new LiveControl();
        mLiveControl.setLiveCallBack(this);


        mRtspClient = RtspClient.getInstance();
        if (null == mRtspClient) {
            Log.e(HikConstants.LOG_TAG, "initialize:" + "RealPlay mRtspHandle is null!");
            return;
        }
        getCameraDetailInfo(HikConstants.SERVER_ADDRESS, mServInfo.getSessionID());
    }

    private void initView() {
        ivBack.setOnClickListener(this);
        ivStartPlay.setOnClickListener(this);
        ivStartPlay.setVisibility(View.INVISIBLE);
        llCapture.setOnClickListener(this);
        llRecord.setOnClickListener(this);
        llFluent.setOnClickListener(this);
        llStand.setOnClickListener(this);
        llHigh.setOnClickListener(this);
        ivAllScreen.setOnClickListener(this);
        llStreamType.setOnClickListener(this);
        ivLandscapeStream.setOnClickListener(this);
        ivLandscapeCamera.setOnClickListener(this);
        ivLandscapeRecord.setOnClickListener(this);
        sufaceView.getHolder().addCallback(mSurfaceCallback);
        ivNorthwest.setOnTouchListener(this);
        ivNorth.setOnTouchListener(this);
        ivNortheast.setOnTouchListener(this);
        ivWest.setOnTouchListener(this);
        ivAuto.setOnTouchListener(this);
        ivEast.setOnTouchListener(this);
        ivSouthwest.setOnTouchListener(this);
        ivSouth.setOnTouchListener(this);
        ivSoutheast.setOnTouchListener(this);
        ivZoomIn.setOnTouchListener(this);
        ivZoomOut.setOnTouchListener(this);

        isShowAllScreen = false;//隐藏缩放
        ivAllScreen.setVisibility(View.GONE);
        ivAllScreen.setImageResource(R.mipmap.icon_screen_out);

        sufaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCtrViewStatus();
            }
        });
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsRecord) {
                    UIUtils.showToast("请先停止录像");
                    return;
                }
                Intent intent = new Intent(LiveByDynamicActivity.this, PlayBackActivity.class);
                intent.putExtra("cameraId", mCameraID);
                TempData.getInstance().setLoginData(mServInfo);
                startActivity(intent);
                doStopPlay();
                ivStartPlay.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 获取监控点详情方法
     *
     * @param serAddress 服务器地址
     * @param sessionId  会话ID
     */
    private void getCameraDetailInfo(final String serAddress, final String sessionId) {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCameraInfoResult = mVmsNetSDK.getCameraInfoEx(serAddress, sessionId,
                        mCameraID, mCameraInfoEx);
                Log.i(HikConstants.LOG_TAG, "result is :" + mCameraInfoResult);
                mDeviceId = mCameraInfoEx.getDeviceId();
                Log.i(HikConstants.LOG_TAG, "mDeviceID is :" + mDeviceId);
                mDeviceInfo = new DeviceInfo();
                // 获取设备信息
                mDeviceInfoResult = mVmsNetSDK.getDeviceInfo(serAddress, sessionId, mDeviceId, mDeviceInfo);
                if (!mDeviceInfoResult || TextUtils.isEmpty(mDeviceInfo.getLoginName())
                        || TextUtils.isEmpty(mDeviceInfo.getLoginPsw())) {
                    //设置设备的默认登录名和密码
                    mDeviceInfo.setLoginName("admin");
                    mDeviceInfo.setLoginPsw("12345");
                }
                mDeviceLoginName = mDeviceInfo.getLoginName();
                mDeviceLoginPsw = mDeviceInfo.getLoginPsw();

                //发送消息获取监控点信息成功
                Message message = Message.obtain();
                message.what = GET_CAMERA_DETAIL_INFO_SUCCESS;
                mHandler.sendMessage(message);


            }
        }).start();

    }

    private void doStartPlay() {
        if (progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (ivStartPlay.getVisibility() != View.VISIBLE) {
            ivStartPlay.setVisibility(View.GONE);
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                mLiveControl.setLiveParams(getPlayUrl(mStreamType), mDeviceLoginName, mDeviceLoginPsw);
                if (mLiveControl.LIVE_PLAY == mLiveControl.getLiveState()) {
                    mLiveControl.stop();
                }

                if (mLiveControl.LIVE_INIT == mLiveControl.getLiveState()) {
                    mLiveControl.startLive(sufaceView);
                }
            }
        }.start();
    }

    /**
     * 该方法是获取播放地址的，当mStreamType=2时，获取的是MAG，当mStreamType =1时获取的子码流，
     * 当mStreamType = 0时获取的是主码流.
     * <p>
     * 由于该方法中部分参数是监控点的属性，所以需要先获取监控点信息
     *
     * @param streamType 2、表示MAG取流方式；1、表示子码流取流方式；0、表示主码流取流方式；
     *                   <p>
     * @return String 播放地址 ：2、表示返回的是MAG的播放地址;1、表示返回的是子码流的播放地址；
     * 0、表示返回的是主码流的播放地址。
     * @since V1.0
     */
    private String getPlayUrl(int streamType) {
        String url = "";
        if (mServInfo != null && mServInfo.isTokenVerify()) {
            // 获取播放Token
            mToken = mVmsNetSDK.getPlayToken(mServInfo.getSessionID());
            Log.i(HikConstants.LOG_TAG, "mToken is :" + mToken);
        }
/*
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl MagStreamSerAddr:" + mServInfo.getMagServer().getMagStreamSerAddr());
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl MagStreamSerPort:" + mServInfo.getMagServer().getMagStreamSerPort());
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl cameraId:" + mCameraInfoEx.getId());
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl token:" + mToken);
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl streamType:" + streamType);
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl appNetId:" + mServInfo.getAppNetId());
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl deviceNetID:" + mCameraInfoEx.getDeviceNetId());
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl userAuthority:" + mServInfo.getUserAuthority());
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl cascadeFlag:" + mCameraInfoEx.getCascadeFlag());
        Log.d(HikConstants.LOG_TAG, "generateLiveUrl internet:" + mServInfo.isInternet());
*/

        LiveInfo liveInfo = new LiveInfo();
        liveInfo.setMagIp(mServInfo.getMagServer().getMagStreamSerAddr());
        liveInfo.setMagPort(mServInfo.getMagServer().getMagStreamSerPort());
        liveInfo.setCameraIndexCode(mCameraInfoEx.getId());

        if (mToken != null) {
            liveInfo.setToken(mToken);
        }

        // 转码不区分主子码流
        liveInfo.setStreamType(streamType);
        liveInfo.setMcuNetID(mServInfo.getAppNetId());
        liveInfo.setDeviceNetID(mCameraInfoEx.getDeviceNetId());
        liveInfo.setiPriority(mServInfo.getUserAuthority());
        liveInfo.setCascadeFlag(mCameraInfoEx.getCascadeFlag());

        if (mDeviceInfo != null) {
            if (mCameraInfoEx.getCascadeFlag() == LiveInfo.CASCADE_TYPE_YES) {
                mDeviceInfo.setLoginName("admin");
                mDeviceInfo.setLoginPsw("12345");
            }
        }

        if (mServInfo.isInternet()) {
            liveInfo.setIsInternet(LiveInfo.NETWORK_TYPE_INTERNET);
            // 获取不转码地址
            liveInfo.setbTranscode(false);
            mRealPlayURL.setUrl1(mRtspClient.generateLiveUrl(liveInfo));

            // 获取转码地址
            // 使用默认转码参数cif 128 15 h264 ps
            liveInfo.setbTranscode(true);
            mRealPlayURL.setUrl2(mRtspClient.generateLiveUrl(liveInfo));
        } else {
            liveInfo.setIsInternet(LiveInfo.NETWORK_TYPE_LOCAL);
            liveInfo.setbTranscode(false);
            // 内网不转码
            mRealPlayURL.setUrl1(mRtspClient.generateLiveUrl(liveInfo));
            mRealPlayURL.setUrl2("");
        }

        Log.d(HikConstants.LOG_TAG, "url1:" + mRealPlayURL.getUrl1());
        Log.d(HikConstants.LOG_TAG, "url2:" + mRealPlayURL.getUrl2());

        url = mRealPlayURL.getUrl1();
        if (streamType == 2 && mRealPlayURL.getUrl2() != null && mRealPlayURL.getUrl2().length() > 0) {
            url = mRealPlayURL.getUrl2();
        }
        Log.i(HikConstants.LOG_TAG, "mRTSPUrl" + url);
        return url;
    }

    private void sendCtrlCmd(final int ptzCommand) {
        new Thread() {
            @Override
            public void run() {
                String sessionID = mServInfo.getSessionID();
                // 云台控制速度 取值范围(1-10)
                int speed = 5;
                // Log.i(HikConstants.LOG_TAG, "ip:" + mCameraInfoEx.getAcsIP() + ",port:" + mCameraInfoEx.getAcsPort() + ",isPTZControl:" + mUserCap.contains(PTZ_CONTROL));
                // 发送控制命令
                boolean ret = mVmsNetSDK.sendStartPTZCmd(mCameraInfoEx.getAcsIP(), mCameraInfoEx.getAcsPort(),
                        sessionID, mCameraID, ptzCommand, speed, 600,
                        Integer.toString(mCameraInfoEx.getCascadeFlag()));
                Log.i(HikConstants.LOG_TAG, "sendStartPTZCmd ret:" + ret);

            }
        }.start();

    }

    private void stopCloudCtrl() {
        new Thread() {
            @Override
            public void run() {
                String sessionID = mServInfo.getSessionID();
                boolean ret = mVmsNetSDK.sendStopPTZCmd(mCameraInfoEx.getAcsIP(), mCameraInfoEx.getAcsPort(),
                        sessionID, mCameraID, Integer.toString(mCameraInfoEx.getCascadeFlag()));
                Log.i(HikConstants.LOG_TAG, "stopPtzCmd sent,ret:" + ret);
            }
        }.start();
    }

    @Override
    public void onMessageCallback(int messageId) {
        if (null != mLiveControlHandler) {
            Message msg = Message.obtain();
            msg.arg1 = messageId;
            mLiveControlHandler.sendMessage(msg);
        }
    }

    SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    private void doStopPlay() {
        if (null != mLiveControl) {
            mLiveControl.stop();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;

            case R.id.play:
                doStartPlay();
                break;

            case R.id.ll_capture:
                doCapture();
                break;

            case R.id.ll_record:
                doRecord();
                break;

            case R.id.ll_fluent:
                //流畅
                if (mStreamType != ConstantLive.MAG) {
                    mStreamType = ConstantLive.MAG;
                    llFluent.setBackgroundColor(UIUtils.getColor(R.color.white));
                    llStand.setBackgroundColor(UIUtils.getColor(R.color.transparent_color));
                    llHigh.setBackgroundColor(UIUtils.getColor(R.color.transparent_color));
                    doStartPlay();
                }
                llStream.setVisibility(View.GONE);
                break;

            case R.id.ll_stand:
                //标清
                if (mStreamType != ConstantLive.SUB_STREAM) {
                    mStreamType = ConstantLive.SUB_STREAM;
                    llStand.setBackgroundColor(UIUtils.getColor(R.color.white));
                    llFluent.setBackgroundColor(UIUtils.getColor(R.color.transparent_color));
                    llHigh.setBackgroundColor(UIUtils.getColor(R.color.transparent_color));
                    doStartPlay();
                }
                llStream.setVisibility(View.GONE);
                break;

            case R.id.ll_high:
                //高清
                if (mStreamType != ConstantLive.MAIN_STREAM) {
                    mStreamType = ConstantLive.MAIN_STREAM;
                    llHigh.setBackgroundColor(UIUtils.getColor(R.color.white));
                    llFluent.setBackgroundColor(UIUtils.getColor(R.color.transparent_color));
                    llStand.setBackgroundColor(UIUtils.getColor(R.color.transparent_color));
                    doStartPlay();
                }
                llStream.setVisibility(View.GONE);
                break;

            case R.id.ll_stream_type:
                if (llStream.getVisibility() != View.VISIBLE) {
                    llStream.setVisibility(View.VISIBLE);
                } else {
                    llStream.setVisibility(View.GONE);
                }
                break;

            case R.id.iv_all_screen:
                //全屏
                doScreenDirection();
                break;

            case R.id.iv_landscape_camera:
                doCapture();
                break;

            case R.id.iv_landscape_record:
                doRecord();
                break;

            case R.id.iv_landscape_stream:
                if (llStream.getVisibility() != View.VISIBLE) {
                    llStream.setVisibility(View.VISIBLE);
                } else {
                    llStream.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.iv_northwest:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivNorthwest.setImageResource(R.mipmap.icon_northwest);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivNorthwest.setImageResource(R.mipmap.icon_northwestcopy);
                }
                //左上
                ptzCommand(motionEvent, HikPTZCommand.UP_LEFT);
                break;
            case R.id.iv_north:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivNorth.setImageResource(R.mipmap.icon_north);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivNorth.setImageResource(R.mipmap.icon_northcopy);
                }
                //上转
                ptzCommand(motionEvent, HikPTZCommand.TILT_UP);
                break;
            case R.id.iv_northeast:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivNortheast.setImageResource(R.mipmap.icon_northeast);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivNortheast.setImageResource(R.mipmap.icon_northeastcopy);
                }
                //右上
                ptzCommand(motionEvent, HikPTZCommand.UP_RIGHT);
                break;

            case R.id.iv_west:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivWest.setImageResource(R.mipmap.icon_west);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivWest.setImageResource(R.mipmap.icon_westcopy);
                }
                //左转
                ptzCommand(motionEvent, HikPTZCommand.PAN_LEFT);
                break;

            case R.id.iv_auto:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivAuto.setImageResource(R.mipmap.icon_auto);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivAuto.setImageResource(R.mipmap.icon_autocopy);
                }
                //自动
                ptzCommand(motionEvent, HikPTZCommand.PAN_AUTO);

                break;

            case R.id.iv_east:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivEast.setImageResource(R.mipmap.icon_east);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivEast.setImageResource(R.mipmap.icon_eastcopy);
                }
                //右转
                ptzCommand(motionEvent, HikPTZCommand.PAN_RIGHT);
                break;

            case R.id.iv_southwest:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivSouthwest.setImageResource(R.mipmap.icon_southwest);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivSouthwest.setImageResource(R.mipmap.icon_southwestcopy);
                }
                //左下
                ptzCommand(motionEvent, HikPTZCommand.DOWN_LEFT);
                break;

            case R.id.iv_south:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivSouth.setImageResource(R.mipmap.icon_south);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivSouth.setImageResource(R.mipmap.icon_southcopy);
                }
                //下转
                ptzCommand(motionEvent, HikPTZCommand.TILT_DOWN);
                break;

            case R.id.iv_southeast:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivSoutheast.setImageResource(R.mipmap.icon_southeast);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivSoutheast.setImageResource(R.mipmap.icon_southeastcopy);
                }
                //右下
                ptzCommand(motionEvent, HikPTZCommand.DOWN_RIGHT);
                break;

            case R.id.iv_zoom_in:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivZoomIn.setImageResource(R.mipmap.ic_ptz_bbd_default);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivZoomIn.setImageResource(R.mipmap.ic_ptz_bbd_selected);
                }
                //倍数+
                ptzCommand(motionEvent, HikPTZCommand.ZOOM_IN);
                break;

            case R.id.iv_zoom_out:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivZoomOut.setImageResource(R.mipmap.ic_ptz_bbx_default);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivZoomOut.setImageResource(R.mipmap.ic_ptz_bbx_selected);
                }
                //倍数-
                ptzCommand(motionEvent, HikPTZCommand.ZOOM_OUT);
                break;
        }
        return true;
    }

    private void ptzCommand(final MotionEvent eventAction, final int ptzCommand) {
        try {
            if (eventAction.getAction() == MotionEvent.ACTION_DOWN) {
                sendCtrlCmd(ptzCommand);
            } else if (eventAction.getAction() == MotionEvent.ACTION_UP) {
                stopCloudCtrl();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mLiveControlHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case ConstantLive.RTSP_SUCCESS:
                    Log.e(TAG, "启动取流成功");
                    break;

                case ConstantLive.STOP_SUCCESS:
                    Log.e(TAG, "停止成功");
                    break;

                case ConstantLive.START_OPEN_FAILED:
                    UIUtils.showToast("开启播放失败");
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                        ivStartPlay.setVisibility(View.VISIBLE);
                    }
                    break;

                case ConstantLive.PLAY_DISPLAY_SUCCESS:
                    Log.e(TAG, "播放成功");
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                        ivStartPlay.setVisibility(View.GONE);
                    }
                    break;

                case ConstantLive.RTSP_FAIL:
                    UIUtils.showToast("RTSP链接失败");
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                        ivStartPlay.setVisibility(View.VISIBLE);
                    }
                    if (null != mLiveControl) {
                        mLiveControl.stop();
                    }
                    break;

                case ConstantLive.GET_OSD_TIME_FAIL:
                    UIUtils.showToast("获取OSD时间失败");
                    break;

                case ConstantLive.SD_CARD_UN_USEABLE:
                    UIUtils.showToast("SD卡不可用");
                    break;

                case ConstantLive.SD_CARD_SIZE_NOT_ENOUGH:
                    UIUtils.showToast("SD卡空间不足");
                    break;
                case ConstantLive.CAPTURE_FAILED_NPLAY_STATE:
                    UIUtils.showToast("非播放状态不能抓拍");
                    break;
                case ConstantLive.RECORD_FAILED_NPLAY_STATE:
                    UIUtils.showToast("非播放状态不能录像");
                    break;
                case ConstantLive.AUDIO_START_FAILED_NPLAY_STATE:
                    UIUtils.showToast("非播放状态不能开启音频");
                    break;
            }
        }
    };


    private void doCapture() {
        if (null != mLiveControl) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String date = sDateFormat.format(new Date());

            boolean ret = mLiveControl.capture(ConstantUtil.PhotoDir, date + ".jpg");
            if (ret) {
                UIUtils.showToast("保存成功:" + ConstantUtil.PhotoDir + date + ".jpg");
                UtilAudioPlay.playAudioFile(LiveByDynamicActivity.this, R.raw.paizhao);
            } else {
                UIUtils.showToast("抓拍失败");
            }
        }
    }

    private void doRecord() {
        if (null != mLiveControl) {
            if (!mIsRecord) {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                String date = sDateFormat.format(new Date());
                boolean ret = mLiveControl.startRecord(ConstantUtil.PhotoDir, date + ".mp4");
                if (ret) {
                    tvRecord.setText("停止");
                    ivRecord.setImageResource(R.mipmap.ic_record_stop_default);
                    UIUtils.showToast("录像开始");
                    llRecordTag.setVisibility(View.VISIBLE);
                    timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                    int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
                    timer.setFormat("0" + String.valueOf(hour) + ":%s");
                    timer.start();

                    //闪烁
                    AlphaAnimation alphaAnimation1 = new AlphaAnimation(0f, 1.0f);
                    alphaAnimation1.setDuration(300);
                    alphaAnimation1.setRepeatCount(Animation.INFINITE);
                    alphaAnimation1.setRepeatMode(Animation.REVERSE);
                    ivRecordPoint.setAnimation(alphaAnimation1);
                    alphaAnimation1.start();
                    mIsRecord = true;
                } else {
                    UIUtils.showToast("启动录像失败");
                }
            } else {
                mLiveControl.stopRecord();
                mIsRecord = false;
                tvRecord.setText("开始");
                ivRecord.setImageResource(R.mipmap.ic_record_default);
                timer.stop();//计时结束
                ivRecordPoint.clearAnimation();//结束动画
                llRecordTag.setVisibility(View.GONE);
                UIUtils.showToast("停止录像成功");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doStopPlay();
    }

    /**
     * --------横竖屏切换---------
     */
    private boolean isShowAllScreen = false;//是否显示全屏按钮

    private void doScreenDirection() {
        if (mIsRecord) {
            UIUtils.showToast("请先停止录制视频");
            return;
        }
        int screenDirect = UIUtils.getResource().getConfiguration().orientation;//当前屏幕方向
        if (screenDirect == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (screenDirect == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏——>竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void doCtrViewStatus() {
        if (UIUtils.getResource().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            rlTop.setVisibility(View.GONE);
            if (!isShowAllScreen) {
                ivAllScreen.setVisibility(View.VISIBLE);
            } else {
                ivAllScreen.setVisibility(View.GONE);
            }
        } else {
            //竖屏
            if (!isShowAllScreen) {
                ivAllScreen.setVisibility(View.VISIBLE);
            } else {
                ivAllScreen.setVisibility(View.GONE);
            }
        }
        isShowAllScreen = !isShowAllScreen;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        UIUtils.dip2px(280));
                rlSv.setLayoutParams(params);
                llCtrRoot.setVisibility(View.VISIBLE);
                rlTop.setVisibility(View.VISIBLE);
                ivAllScreen.setVisibility(View.GONE);
                isShowAllScreen = false;
                ivAllScreen.setImageResource(R.mipmap.icon_screen_out);
                llLandscapeCamera.setVisibility(View.GONE);
                //设置码流选择控件位置
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        llStream.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM | RelativeLayout.ALIGN_PARENT_RIGHT, R.id.ll_stream);
                layoutParams.setMargins(0, 0, 0, 0);
                llStream.setLayoutParams(layoutParams);
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                rlSv.setLayoutParams(params1);
                llCtrRoot.setVisibility(View.GONE);
                rlTop.setVisibility(View.GONE);
                ivAllScreen.setImageResource(R.mipmap.icon_screen_in);
                ivAllScreen.setVisibility(View.GONE);
                llLandscapeCamera.setVisibility(View.VISIBLE);
                isShowAllScreen = false;

                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams)
                        llStream.getLayoutParams();
                layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM | RelativeLayout.ALIGN_PARENT_RIGHT, R.id.ll_stream);
                layoutParams1.setMargins(0, 0, UIUtils.dip2px(60), 0);
                llStream.setLayoutParams(layoutParams1);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (UIUtils.getResource().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                doScreenDirection();
            } else {
                finish();
            }
            return false; // 事件继续向下传播
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
