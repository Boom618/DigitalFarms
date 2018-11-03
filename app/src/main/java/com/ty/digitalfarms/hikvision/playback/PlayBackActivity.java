package com.ty.digitalfarms.hikvision.playback;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hik.mcrsdk.rtsp.ABS_TIME;
import com.hik.mcrsdk.rtsp.PlaybackInfo;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hikvision.vmsnetsdk.CameraInfoEx;
import com.hikvision.vmsnetsdk.RecordInfo;
import com.hikvision.vmsnetsdk.RecordSegment;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.hikvision.vmsnetsdk.netLayer.mag.MAGServer;
import com.hikvision.vmsnetsdk.netLayer.msp.deviceInfo.DeviceInfo;
import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.constant.ConstantUtil;
import com.ty.digitalfarms.hikvision.data.TempData;
import com.ty.digitalfarms.ui.activity.BaseActivity;
import com.ty.digitalfarms.ui.view.CustomDatePicker;
import com.ty.digitalfarms.ui.view.timeline.RecordDataExistTimeSegment;
import com.ty.digitalfarms.ui.view.timeline.TimebarView;
import com.ty.digitalfarms.util.DateUtil;
import com.ty.digitalfarms.util.TimerUtil;
import com.ty.digitalfarms.util.UIUtils;
import com.ty.digitalfarms.util.UtilAudioPlay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 回放UI类
 */
public class PlayBackActivity extends BaseActivity implements OnClickListener, PlayBackCallBack {

    private static final String TAG = "PlayBackActivity";

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.playbackSurfaceView)
    SurfaceView mSurfaceView;
    @BindView(R.id.current_time_tv)
    TextView currernTimeTv;
    @BindView(R.id.my_timebar_view)
    TimebarView mTimebarView;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_cut)
    ImageView ivCut;
    @BindView(R.id.iv_pause)
    ImageView ivPause;
    @BindView(R.id.playBackProgressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.ll_control)
    LinearLayout llControl;


    private boolean mIsPause;
    private VMSNetSDK mVmsNetSDK;
    private String mSessionId;
    private RecordInfo mRecordInfo;
    private SimpleDateFormat zeroTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DeviceInfo mDeviceInfo;//单个设备信息
    private boolean isDrag = false;//是否拖动进度条
    private boolean isSwitchDate = false;//是否切换日期

    private Calendar mEndCalendar;//录像结束时间
    private Calendar mStartCalendar;//录像开始时间
    private Calendar mPlaybackTime;//默认时间：当前日期的00:00:00
    private String mCurrentDate;//当前录像的日期
    private String mPlaybackUrl;//回放地址
    private PlayBackControl mControl;//回放控制层对象
    private PlayBackParams mParamsObj;//回放需要的参数对象
    private ServInfo mServInfo;//服务器信息
    private String mServerAddress;//服务器ip地址
    private String mCameraId;//设备Id
    private CameraInfoEx mCameraInfoEx;//监控点详细信息对象

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.playback_activity);
        StatusBarUtil.setColor(PlayBackActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initView();
        initPlaybackInfo();
        queryPlaybackInfo();
    }

    private void initView() {
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvTitle.setText("回放");
        ivRight.setImageResource(R.mipmap.ic_date);
        ivRight.setVisibility(View.VISIBLE);
        ivPlay.setOnClickListener(this);
        ivCut.setOnClickListener(this);
        ivPause.setOnClickListener(this);
        mTimebarView.setOnBarMoveListener(new TimebarView.OnBarMoveListener() {
            @Override
            public void onBarMove(long screenLeftTime, long screenRightTime, long currentTime) {
                if (currentTime == -1) {
                    Toast.makeText(PlayBackActivity.this, "当前时刻没有录像", Toast.LENGTH_SHORT).show();
                    currernTimeTv.setText(DateUtil.getCurrentDate());
                }else {
                    currernTimeTv.setText(zeroTimeFormat.format(currentTime));
                }
            }

            @Override
            public void OnBarMoveFinish(long screenLeftTime, long screenRightTime, long currentTime) {
                mTimebarView.closeMove();
                currernTimeTv.setText(zeroTimeFormat.format(currentTime));
                Calendar startTime = getCalender(zeroTimeFormat.format(currentTime));
                isDrag = true;
                doStart(startTime, mEndCalendar);

            }
        });
    }

    private void initPlaybackInfo() {
        mVmsNetSDK = VMSNetSDK.getInstance();
        mControl = new PlayBackControl();
        mControl.setPlayBackCallBack(this);// 设置远程回放控制层回调
        mParamsObj = new PlayBackParams();
        mParamsObj.surfaceView = mSurfaceView;

        mServInfo = TempData.getIns().getLoginData();
        mCameraId = getIntent().getStringExtra("cameraId");
        mCameraInfoEx = new CameraInfoEx();
        mCameraInfoEx.setId(mCameraId);
        SharedPreferences sp = getSharedPreferences(ConstantUtil.SERVICE_SP_NAME, MODE_PRIVATE);
        mServerAddress = sp.getString(ConstantUtil.SP_SERVICE_ADDRESS, null);
        mSessionId = mServInfo.getSessionID();

        //默认播放当天的视频
        mPlaybackTime = getDefaultCurrentTime();
        mStartCalendar = Calendar.getInstance();
        mEndCalendar = Calendar.getInstance();
        mStartCalendar.setTimeInMillis(mPlaybackTime.getTimeInMillis());
        mCurrentDate = TimerUtil.getTime_nnnnyydd(mStartCalendar).split(" ")[0];
    }

    public void queryPlaybackInfo() {
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取监控点详情结果
                mVmsNetSDK.getCameraInfoEx(mServerAddress, mSessionId, mCameraId, mCameraInfoEx);
                String mDeviceID = mCameraInfoEx.getDeviceId();
                mDeviceInfo = new DeviceInfo();
                //获取设备信息
                boolean getDeviceInfoResult = mVmsNetSDK.getDeviceInfo(mServerAddress, mSessionId, mDeviceID, mDeviceInfo);
                if (!getDeviceInfoResult) {
                    mDeviceInfo.setLoginName("admin");
                    mDeviceInfo.setLoginPsw("12345");
                }

                mParamsObj.name = mDeviceInfo.getLoginName();
                mParamsObj.passwrod = mDeviceInfo.getLoginPsw();

                // 查询录像库中的时间对象，注意Calendar时间，使用前请先了解下Calendar
                com.hikvision.vmsnetsdk.ABS_TIME startTime = new com.hikvision.vmsnetsdk.ABS_TIME(mStartCalendar);
                com.hikvision.vmsnetsdk.ABS_TIME endTime = new com.hikvision.vmsnetsdk.ABS_TIME(mEndCalendar);

                setParamsObjTime(startTime, endTime);

                mRecordInfo = new RecordInfo();
                queryRecord(startTime, endTime, mRecordInfo);
            }
        }).start();
    }

    private boolean queryRecord(com.hikvision.vmsnetsdk.ABS_TIME startTime, com.hikvision.vmsnetsdk.ABS_TIME endTime,
                                RecordInfo recordInfo) {
        // cms平台地址
        String servHeadAddr = mServInfo.getMagServer().getMagHttpRequestAddrHead(true);
        // 查询的录像类型，1-计划录像，2-移动录像，16-手动录像，4-报警录像
        String recordType = "1,2,4,16";
        List<Integer> recordPoses = mCameraInfoEx.getRecordPos();
        if ((null == recordPoses)) {
            sendToastMessage(getString(R.string.playback_not_allot_record));
            return false;
        }
        int i = 0;
        while (true) {
            /* 查询录像失败 */
            if (i == recordPoses.size()) {
                /* 查询录像失败 */
                int errorCode = mVmsNetSDK.getLastErrorCode();
                if (errorCode == VMSNetSDK.VMSNETSDK_MSP_NO_DATA) {
                    Log.e(TAG, "查询录像失败，没有录像文件");
                } else if (errorCode == VMSNetSDK.VMSNETSDK_MSP_SESSION_ERROR) {
                    Log.e(TAG, "查询录像失败，sessionid异常");
                } else {
                    Log.e(TAG, "查询录像失败，错误码：" + mVmsNetSDK.getLastErrorCode());
                }
                Log.e(TAG, "查询录像失败，错误码：" + mVmsNetSDK.getLastErrorCode());
                sendMessageCase(ConstantPlayBack.GET_RECORD_FILE_FAIL);
                return false;
            }

            int mPlaybackMedium = recordPoses.get(i);// 录像存储介质
            recordInfo.recSegmentList.clear();
            boolean ret = mVmsNetSDK.queryCameraRecord(servHeadAddr, mSessionId, mCameraId, recordType,
                    String.valueOf(mPlaybackMedium), startTime, endTime, recordInfo);

            List<RecordSegment> recSegmentList = recordInfo.recSegmentList;
            if (recordInfo.recSegmentList==null||recordInfo.recSegmentList.size()==0) {
                Log.e(TAG, "查询录像失败，没有录像文件");
                sendMessageCase(ConstantPlayBack.GET_RECORD_FILE_FAIL);
                return false;
            }
            for (RecordSegment info:recSegmentList){
                Log.e("TAG444",info.getStartTime()+"-----"+info.getEndTime());
            }
            if (ret) {
                Log.d(TAG, "查询录像成功, mPlaybackMediums:" + recordPoses);
                break;
            }
            i++;
        }
        sendMessageCase(ConstantPlayBack.GET_RECORD_FILE_SUCCESS);
        return true;
    }

    /**
     * 设置远程回放取流的开始时间和结束时间
     *
     * @param startTime
     * @param endTime
     * @since V1.0
     */
    protected void setParamsObjTime(com.hikvision.vmsnetsdk.ABS_TIME startTime, com.hikvision.vmsnetsdk.ABS_TIME endTime) {
        // 取流库中的时间对象
        ABS_TIME rtspEndTime = new ABS_TIME();
        ABS_TIME rtspStartTime = new ABS_TIME();

        // 设置播放结束时间
        rtspEndTime.setYear(endTime.dwYear);
        // 之所以要加1，是由于我们查询接口中的时间和取流中的时间采用的是两个自定义的时间类，这个地方开发者按照demo中实现就可以了。
        rtspEndTime.setMonth(endTime.dwMonth + 1);
        rtspEndTime.setDay(endTime.dwDay);
        rtspEndTime.setHour(endTime.dwHour);
        rtspEndTime.setMinute(endTime.dwMinute);
        rtspEndTime.setSecond(endTime.dwSecond);

        // 设置开始播放时间
        rtspStartTime.setYear(startTime.dwYear);
        // 之所以要加1，是由于我们查询接口中的时间和取流中的时间采用的是两个自定义的时间类，这个地方开发者按照demo中实现就可以了。
        rtspStartTime.setMonth(startTime.dwMonth + 1);
        rtspStartTime.setDay(startTime.dwDay);
        rtspStartTime.setHour(startTime.dwHour);
        rtspStartTime.setMinute(startTime.dwMinute);
        rtspStartTime.setSecond(startTime.dwSecond);

        if (mParamsObj != null) {
            // 设置开始远程回放的开始时间和结束时间。
            mParamsObj.endTime = rtspEndTime;
            mParamsObj.startTime = rtspStartTime;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_cut:
                doCapture();
                break;

            case R.id.iv_pause:
                doPause();
                break;

            case R.id.iv_left:
                finish();
                break;

            case R.id.iv_right:
                doSelectPlaybackTime();
                break;
        }
    }

    private void doStart(final Calendar startCalendar, final Calendar endCalendar) {
        if (mProgressBar.getVisibility()!=View.VISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (null != mControl) {
                    setPlaybackUrl(mRecordInfo.segmentListPlayUrl, startCalendar, endCalendar);
                    mParamsObj.url = mPlaybackUrl;
                    //当前是播放状态
                    if (mControl.PLAYBACK_PLAY == mControl.getPlayBackState()) {
                        mControl.stopPlayBack();
                    }
                    //当前是初始化状态
                    if (mControl.PLAYBACK_INIT == mControl.getPlayBackState()
                            || mControl.PLAYBACK_RELEASE == mControl.getPlayBackState()) {
                        mControl.startPlayBack(mParamsObj);
                    }
                }
            }
        }.start();
    }

    private void setPlaybackUrl(String url, Calendar startTime, Calendar endTime) {
        if (TextUtils.isEmpty(url)) { return; }
        String mToken = null;
        if (mServInfo != null && mServInfo.isTokenVerify()) {
            mToken = mVmsNetSDK.getPlayToken(mServInfo.getSessionID()); //获取播放Token
        }
        String begin = getPlaybackTime(startTime);
        String end = getPlaybackTime(endTime);
        if (mServInfo != null) {
            PlaybackInfo playbackInfo = new PlaybackInfo();
            MAGServer magserver = mServInfo.getMagServer();
            if (magserver != null && magserver.getMagStreamSerAddr() != null) {
                playbackInfo.setMagIp(magserver.getMagStreamSerAddr());
                playbackInfo.setMagPort(magserver.getMagStreamSerPort());
            }
            int magVersion = mVmsNetSDK.isNewMagVersion();
            playbackInfo.setMagVersion(magVersion);
            playbackInfo.setPlaybackUrl(url);
            if (mToken != null) {
                playbackInfo.setToken(mToken);
            }
            playbackInfo.setBegin(begin);
            playbackInfo.setEnd(end);
            playbackInfo.setmcuNetID(mServInfo.getAppNetId());
            mPlaybackUrl = RtspClient.getInstance().generatePlaybackUrl(playbackInfo);
        }
        Log.e(TAG, "setPlaybackUrl() PlaybackUrl:" + mPlaybackUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doStop();
    }

    private void doStop() {
        if (null != mControl) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mControl.stopPlayBack();
                }
            }.start();
        }
    }

    private void doPause() {
        if (mIsPause) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        if (null != mControl) {
            new Thread() {
                @Override
                public void run() {
                    if (!mIsPause) {
                        mControl.pausePlayBack();
                    } else {
                        mControl.resumePlayBack();
                    }
                    super.run();
                }
            }.start();
        }
    }

    private void doCapture() {
        if (null != mControl) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String date = sDateFormat.format(new Date());
            boolean ret = mControl.capture(ConstantUtil.PhotoDir, date + ".jpg");
            if (ret) {
                UIUtils.showToast("保存成功:" +ConstantUtil.PhotoDir + date + ".jpg");
                UtilAudioPlay.playAudioFile(this, R.raw.paizhao);
            } else {
                UIUtils.showToast( "抓拍失败");
                Log.e(TAG, "doCapture():: 抓拍失败");
            }
        }
    }

    private void doSelectPlaybackTime() {
        String endTime = TimerUtil.getTime_nnnnyydd(mPlaybackTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String now = sdf.format(new Date());
        CustomDatePicker datePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                String date = time.split(" ")[0];
                String today = TimerUtil.getTime_nnnnyydd(mPlaybackTime).split(" ")[0];
                try {
                    if (mCurrentDate.equals(date)) { return; }
                    Date endDate = DateUtil.getDateFromStr(date);
                    Date startDate = DateUtil.getDateFromStr(today);
                    int diffDay = DateUtil.differentDays(startDate, endDate);
                    if (diffDay <= 0) {
                        mTimebarView.closeMove();
                        isSwitchDate = true;
                        doStop();
                        String selectTime = time + ":00";
                        Calendar calender = getCalender(selectTime);
                        mStartCalendar.setTimeInMillis(calender.getTimeInMillis());
                        mEndCalendar.setTimeInMillis(mStartCalendar.getTimeInMillis() + 24 * 3600 * 1000 - 1000);
                        Log.e("TAG111",TimerUtil.getTime_nnnnyydd(mStartCalendar)+"-----"+
                                TimerUtil.getTime_nnnnyydd(mEndCalendar));

                        queryPlaybackInfo();
                    } else {
                        UIUtils.showToast("日期选择有误，请重新选择");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, "2018-01-01 00:00", endTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        datePicker.showSpecificTime(false); // 显示时和分
        datePicker.setIsLoop(false); // 允许循环滚动
        datePicker.show(now);
    }

    private void updateView() {
        if (mRecordInfo != null) {
            mCurrentDate = TimerUtil.getTime_nnnnyydd(mStartCalendar).split(" ")[0];//当前录像的日期
            List<RecordSegment> recSegmentList = mRecordInfo.recSegmentList;
            if (recSegmentList != null && recSegmentList.size() > 0) {
                long timebarLeftEndPointTime = formatTime(recSegmentList.get(0).getStartTime());
                long timebarRightEndPointTime = formatTime(recSegmentList.get(recSegmentList.size() - 1).getEndTime());
                mTimebarView.initTimebarLengthAndPosition(timebarLeftEndPointTime,
                        timebarRightEndPointTime - 1000, timebarLeftEndPointTime);
            }

            final List<RecordDataExistTimeSegment> recordDataList = new ArrayList();
            for (RecordSegment rs : recSegmentList) {
                recordDataList.add(new RecordDataExistTimeSegment(formatTime(rs.getStartTime()), formatTime(rs.getEndTime())));
            }

            mTimebarView.setRecordDataExistTimeClipsList(recordDataList);
            mTimebarView.checkVideo(true);
        }
        llControl.setVisibility(View.VISIBLE);
        doStart(mStartCalendar, mEndCalendar);
    }

    /*----------------------回放消息回调------------------------*/
    @SuppressLint("HandlerLeak")
    Handler mMessageHandler= new Handler (){

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                //获取录像文件消息
                case ConstantPlayBack.GET_RECORD_FILE_SUCCESS:
                //    Log.e("TAG", "获取录像文件成功");
                    updateView();
                    break;
                case ConstantPlayBack.GET_RECORD_FILE_FAIL:
                //    Log.e("TAG", "获取录像文件失败");
                    UIUtils.showToast("获取录像文件失败/没有录像");
                    llControl.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    break;

                //启动取流下消息
                case ConstantPlayBack.START_RTSP_SUCCESS:
                   // UIUtils.showToast("启动取流库成功");
                  //  Log.e("TAG", "启动取流库成功");
                    break;
                case ConstantPlayBack.START_RTSP_FAIL:
                  //  Log.e("TAG", "启动取流库失败");
                    UIUtils.showToast("启动取流库失败");
                    mProgressBar.setVisibility(View.GONE);
                    break;

                //回放成功与否消息
                case ConstantPlayBack.PLAY_DISPLAY_SUCCESS:
                    // Log.e("TAG", "回放成功");
                    if (!mTimebarView.isMoveing()) {
                      //  Log.e("TAG", "回放成功");
                        mTimebarView.openMove();
                    }
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case ConstantPlayBack.START_OPEN_FAILED:
                 //   Log.e("TAG", "启动播放库失败");
                    UIUtils.showToast("启动播放库失败");
                    mProgressBar.setVisibility(View.GONE);
                    break;

                //暂停消息
                case ConstantPlayBack.PAUSE_SUCCESS:
                  //  Log.e("TAG", "暂停成功");
                    if (mTimebarView.isMoveing()) {
                        mTimebarView.closeMove();
                    }
                    ivPause.setImageResource(R.drawable.iv_play_selector);
                    ivPlay.setVisibility(View.VISIBLE);
                    mIsPause = true;
                    break;
                case ConstantPlayBack.PAUSE_FAIL:
                  //  Log.e("TAG", "暂停失败");
                    UIUtils.showToast("暂停失败");
                    mIsPause = false;
                    break;

                //恢复播放消息
                case ConstantPlayBack.RESUEM_SUCCESS:
                    if (!mTimebarView.isMoveing()) {
                  //      Log.e("TAG", "重新播放成功");
                        mTimebarView.openMove();
                    }
                    ivPause.setImageResource(R.drawable.iv_pause_selector);
                    mProgressBar.setVisibility(View.GONE);
                    ivPlay.setVisibility(View.GONE);
                    mIsPause = false;
                    break;
                case ConstantPlayBack.RESUEM_FAIL:
                 //   Log.e("TAG", "恢复播放失败");
                    UIUtils.showToast("恢复播放失败");
                    ivPlay.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mIsPause = true;
                    break;


                case ConstantPlayBack.STOP_SUCCESS:
                 //   Log.e("TAG", "停止成功");
                    if (isDrag || isSwitchDate) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        isDrag = false;
                        isSwitchDate = false;
                    }
                    break;

                case ConstantPlayBack.PAUSE_FAIL_NPLAY_STATE:
                  //  Log.e("TAG", "非播放状态不能暂停");
                    UIUtils.showToast("非播放状态不能暂停");
                    break;

                case ConstantPlayBack.RESUEM_FAIL_NPAUSE_STATE:
                  //  Log.e("TAG", "非播放状态");
                    UIUtils.showToast("非播放状态");
                    break;

                case RtspClient.RTSPCLIENT_MSG_CONNECTION_EXCEPTION:
                  //  Log.e("TAG", "RTSP链接异常");
                    mProgressBar.setVisibility(View.GONE);
                    UIUtils.showToast("RTSP链接异常");
                    break;

                case ConstantPlayBack.SHOW_TOARST:
                  //  Log.e("TAG----", (String) msg.obj);
                    showToast((String) msg.obj);
                    mProgressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onMessageCallback(int message) {
        sendMessageCase(message);
    }

    private void sendMessageCase(int message) {
        if (null != mMessageHandler) {
            Message msg = Message.obtain();
            msg.arg1 = message;
            mMessageHandler.sendMessage(msg);
        }
    }

    private void sendToastMessage(String messageStr) {
        Message msg = Message.obtain();
        msg.arg1 = ConstantPlayBack.SHOW_TOARST;
        msg.obj = messageStr;
        if (null != mMessageHandler) {
            mMessageHandler.sendMessage(msg);
        }
    }


    private long formatTime(com.hikvision.vmsnetsdk.ABS_TIME time) {
        long timeInMillis = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = df.parse(time.toString());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            timeInMillis = cal.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeInMillis;

    }

    private void showToast(String notice) {
        if (!isFinishing()) {
            Toast aToast = Toast.makeText(this, notice, Toast.LENGTH_SHORT);
            aToast.setGravity(Gravity.CENTER, 0, 0);
            aToast.show();
        }
    }

    private String getPlaybackTime(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String format_month = formatDate(month);//格式为00;
        String format_day = formatDate(day);//格式为00;
        String format_hour = formatDate(hour);//格式为00;
        String format_minute = formatDate(minute);//格式为00;
        String format_second = formatDate(second);//格式为00;
        StringBuffer buffer = new StringBuffer();
        buffer.append(year)
                .append(format_month)
                .append(format_day)
                .append("T")
                .append(format_hour)
                .append(format_minute)
                .append(format_second)
                .append("Z");
        return buffer.toString();
    }

    private String formatDate(int date) {
        String format_date = "" + date;
        if (date < 10) {
            format_date = "0" + date;
        }
        return format_date;
    }

    /**
     * 当天00:00:00
     *
     * @return
     */
    private Calendar getDefaultCurrentTime() {
        Calendar currentTimeCalendar = Calendar.getInstance();
        currentTimeCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentTimeCalendar.set(Calendar.MINUTE, 0);
        currentTimeCalendar.set(Calendar.SECOND, 0);
        currentTimeCalendar.set(Calendar.MILLISECOND, 0);
        return currentTimeCalendar;
    }

    private Calendar getCalender(String sting) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = df.parse(sting);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }



}
