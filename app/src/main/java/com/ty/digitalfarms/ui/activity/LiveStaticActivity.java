package com.ty.digitalfarms.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.PTZCommand;
import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.ui.adapter.VideoPagerAdapter;
import com.ty.digitalfarms.bean.DeviceInfo;
import com.ty.digitalfarms.ui.fragment.SingleFragment;
import com.ty.digitalfarms.util.UIUtils;
import com.ty.digitalfarms.ui.view.NoScrollViewPager;

import org.MediaPlayer.PlayM4.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveStaticActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private static final String TAG = "TAG";

    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
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
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.iv_record)
    ImageView ivRecord;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.iv_record_point)
    ImageView ivRecordPoint;
    @BindView(R.id.timer)
    Chronometer timer;
    @BindView(R.id.ll_record_tag)
    LinearLayout llRecordTag;
    @BindView(R.id.iv_zoom_in)
    ImageView ivZoomIn;
    @BindView(R.id.iv_zoom_out)
    ImageView ivZoomOut;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_all_screen)
    ImageView ivAllScreen;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.ll_ctr_root)
    LinearLayout llCtrRoot;
    @BindView(R.id.rl_sv)
    RelativeLayout rlSv;
    @BindView(R.id.iv_landscape_camera)
    ImageView ivLandscapeCamera;
    @BindView(R.id.iv_landscape_record)
    ImageView ivLandscapeRecord;
    @BindView(R.id.ll_landscape_camera)
    LinearLayout llLandscapeCamera;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_landscape_title)
    TextView tvLandscapeTitle;
    @BindView(R.id.rl_landscape_top)
    RelativeLayout rlLandscapeTop;


    private SparseArray<SingleFragment> mFragments = new SparseArray<>();

    private VideoPagerAdapter mAdapter;
    private ArrayList<DeviceInfo.ResultBean> deviceList;

    private int position;//fragment初始位置
    private int currentPosition = -1;

    private static final int PREVIEW_START = 0;//开始预览
    private static final int PREVIEW_END = 1;//结束预览
    private static final int PREVIEW_SPEED = 4;
    private int hikLoginId = -1;
    private int hikPlayID = -1;
    private int hikPort = -1;

    private boolean isStartRecord = false;//标志录像是否开始
    private SingleFragment singleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_static);
        StatusBarUtil.setColor(LiveStaticActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initData();
        initView();
        initFragment();
        initInfo();
    }


    private void initData() {
        deviceList = getIntent().getParcelableArrayListExtra("deviceList");
        position = getIntent().getIntExtra("position", -1);
        String deviceTag = getIntent().getStringExtra("deviceTag");
        tvTitle.setText(deviceTag);
        tvLandscapeTitle.setText(deviceTag);
    }

    private void initView() {
        ivCamera.setOnClickListener(this);
        ivRecord.setOnClickListener(this);
        ivLandscapeCamera.setOnClickListener(this);
        ivLandscapeRecord.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        ivAllScreen.setOnClickListener(this);
        ivBack.setOnClickListener(this);

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

        ivRight.setVisibility(View.GONE);
      //  ivRight.setOnClickListener(this);

        isShowAllScreen = false;//隐藏缩放
        ivAllScreen.setVisibility(View.GONE);
        ivAllScreen.setImageResource(R.mipmap.icon_screen_out);
        viewpager.setNoScroll(false);//可以滑动
    }

    private void initInfo() {
        int index = 0;
        for (DeviceInfo.ResultBean bean : deviceList) {
            mFragments.put(index, SingleFragment.newInstance(bean, deviceList.size(), index));
            index++;
        }
        //创建适配器
        mAdapter = new VideoPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpager.setAdapter(mAdapter);
        if (position != -1) {
            viewpager.setCurrentItem(position);
            currentPosition = position;
            if (position == 0) {
                singleFragment = mFragments.get(currentPosition);
                singleFragment.setSurfaceViewClickListener(new SingleFragment.SurfaceViewClickListener() {
                    @Override
                    public void onSurfaceViewClick() {
                        doCtrViewStatus();
                    }
                });
            }
        }
    }

    private void initFragment() {
        viewpager.setOffscreenPageLimit(mFragments.size() * 2);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                tvTitle.setText(deviceList.get(position).getTag());
                tvLandscapeTitle.setText(deviceList.get(position).getTag());
                singleFragment = mFragments.get(currentPosition);
                singleFragment.setSurfaceViewClickListener(new SingleFragment.SurfaceViewClickListener() {
                    @Override
                    public void onSurfaceViewClick() {
                        doCtrViewStatus();
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //滑动状态改变，若正在录像，则停止
                if (isStartRecord) {
                    doRecord();
                }
            }

        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (singleFragment == null) {
            singleFragment = mFragments.get(currentPosition);
        }
        hikPlayID = singleFragment.getPlayId();
        hikLoginId = singleFragment.getLoginId();
        // Log.e("TAG",hikPlayID+"----hikLoginId=="+hikLoginId);
        boolean b = false;
        if (hikLoginId < 0) {
            UIUtils.showToast("请先预览设备！");
            return b;
        } else {
            switch (view.getId()) {
                case R.id.iv_northwest:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivNorthwest.setImageResource(R.mipmap.icon_northwest);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivNorthwest.setImageResource(R.mipmap.icon_northwestcopy);
                    }
                    //左上
                    b = ptzCommand(motionEvent, PTZCommand.UP_LEFT);
                    break;
                case R.id.iv_north:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivNorth.setImageResource(R.mipmap.icon_north);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivNorth.setImageResource(R.mipmap.icon_northcopy);
                    }
                    //上转
                    b = ptzCommand(motionEvent, PTZCommand.TILT_UP);
                    break;
                case R.id.iv_northeast:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivNortheast.setImageResource(R.mipmap.icon_northeast);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivNortheast.setImageResource(R.mipmap.icon_northeastcopy);
                    }
                    //右上
                    b = ptzCommand(motionEvent, PTZCommand.UP_RIGHT);
                    break;

                case R.id.iv_west:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivWest.setImageResource(R.mipmap.icon_west);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivWest.setImageResource(R.mipmap.icon_westcopy);
                    }
                    //左转
                    b = ptzCommand(motionEvent, PTZCommand.PAN_LEFT);
                    break;

                case R.id.iv_auto:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivAuto.setImageResource(R.mipmap.icon_auto);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivAuto.setImageResource(R.mipmap.icon_autocopy);
                    }
                    //自动
                    b = ptzCommand(motionEvent, PTZCommand.PAN_AUTO);

                    break;

                case R.id.iv_east:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivEast.setImageResource(R.mipmap.icon_east);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivEast.setImageResource(R.mipmap.icon_eastcopy);
                    }
                    //右转
                    b = ptzCommand(motionEvent, PTZCommand.PAN_RIGHT);
                    break;

                case R.id.iv_southwest:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivSouthwest.setImageResource(R.mipmap.icon_southwest);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivSouthwest.setImageResource(R.mipmap.icon_southwestcopy);
                    }
                    //左下
                    b = ptzCommand(motionEvent, PTZCommand.DOWN_LEFT);
                    break;

                case R.id.iv_south:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivSouth.setImageResource(R.mipmap.icon_south);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivSouth.setImageResource(R.mipmap.icon_southcopy);
                    }
                    //下转
                    b = ptzCommand(motionEvent, PTZCommand.TILT_DOWN);
                    break;

                case R.id.iv_southeast:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivSoutheast.setImageResource(R.mipmap.icon_southeast);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivSoutheast.setImageResource(R.mipmap.icon_southeastcopy);
                    }
                    //右下
                    b = ptzCommand(motionEvent, PTZCommand.DOWN_RIGHT);
                    break;

                case R.id.iv_zoom_in:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivZoomIn.setImageResource(R.mipmap.ic_ptz_bbd_default);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivZoomIn.setImageResource(R.mipmap.ic_ptz_bbd_selected);
                    }
                    //倍数+
                    b = ptzCommand(motionEvent, PTZCommand.ZOOM_IN);
                    break;

                case R.id.iv_zoom_out:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ivZoomOut.setImageResource(R.mipmap.ic_ptz_bbx_default);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ivZoomOut.setImageResource(R.mipmap.ic_ptz_bbx_selected);
                    }
                    //倍数-
                    b = ptzCommand(motionEvent, PTZCommand.ZOOM_OUT);
                    break;
            }
            return b;
        }
    }

    private boolean ptzCommand(final MotionEvent eventAction, final int ptzCommand) {

        if (hikLoginId < 0 || hikPlayID < 0) {
            Log.e(TAG, "please doConnServer on a device first");
            return false;
        }
        //HCNetSDK.getInstance().NET_DVR_PTZControl_Other(hikLoginId, 1, ptzCommand, 0);
        //HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed(hikPlayID, ptzCommand, PREVIEW_START, PREVIEW_SPEED)
        try {
            if (eventAction.getAction() == MotionEvent.ACTION_DOWN) {
                new Thread() {
                    @Override
                    public void run() {
                        HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed(hikPlayID, ptzCommand, PREVIEW_START,
                                PREVIEW_SPEED);
                    }
                }.start();


            } else if (eventAction.getAction() == MotionEvent.ACTION_UP) {
                new Thread() {
                    @Override
                    public void run() {
                        HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed(hikPlayID, ptzCommand, PREVIEW_END,
                                PREVIEW_SPEED);
                    }
                }.start();

            }
            return true;
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
                doScreenshot();
                break;

            case R.id.iv_record:
                doRecord();
                break;

            case R.id.iv_landscape_camera:
                doScreenshot();
                break;

            case R.id.iv_landscape_record:
                doRecord();
                break;

            case R.id.iv_left:
                if (UIUtils.getResource().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    doScreenDirection();
                } else {
                    finish();
                }
                break;

            case R.id.iv_back://横屏状态下的返回按钮
                doScreenDirection();
                break;

            case R.id.iv_all_screen:
                doScreenDirection();
                break;

        }
    }

    private void doScreenshot() {
        if (singleFragment == null) {
            singleFragment = mFragments.get(currentPosition);
        }
        hikPort = singleFragment.getHikPort();
        FileOutputStream file = null;
        try {
            if (hikPort < 0) {
                Log.e(TAG, "请先预览设备！");
                return;
            }
            Player.MPInteger stWidth = new Player.MPInteger();
            Player.MPInteger stHeight = new Player.MPInteger();
            if (!Player.getInstance().getPictureSize(hikPort, stWidth,
                    stHeight)) {
                Log.e(TAG, "getPictureSize failed with error code:"
                        + Player.getInstance().getLastError(hikPort));
                return;
            }
            int nSize = 5 * stWidth.value * stHeight.value;
            byte[] picBuf = new byte[nSize];
            Player.MPInteger stSize = new Player.MPInteger();
            if (!Player.getInstance().getBMP(hikPort, picBuf, nSize, stSize)) {
                Log.e(TAG, "getBMP failed with error code:" + Player.getInstance().getLastError(hikPort));
                return;
            }

            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String date = sDateFormat.format(new Date());
            File path = new File("/mnt/sdcard/DCIM/Camera/");
            if (!path.exists()) {
                path.mkdirs();
            }
            File f = new File(path, date + ".jpg");
            file = new FileOutputStream(f);
            file.write(picBuf, 0, stSize.value);
            UIUtils.showToast("保存成功:" + "/mnt/sdcard/DCIM/Camera/" + date + ".jpg");

        } catch (Exception err) {
            UIUtils.showToast("抓图失败，请重试！");
            Log.e(TAG, "error: " + err.toString());
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRecord() {
        if (singleFragment == null) {
            singleFragment = mFragments.get(currentPosition);
        }
        hikPlayID = singleFragment.getPlayId();
        if (hikPlayID < 0) {
            UIUtils.showToast("请先预览");
            return;
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String path = "/sdcard/" + sDateFormat.format(new Date()) + ".mp4";
        if (!isStartRecord) {

            if (!HCNetSDK.getInstance().NET_DVR_SaveRealData(hikPlayID, path)) {
                UIUtils.showToast("录像开始失败" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            } else {
                if (UIUtils.getResource().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    ivLandscapeRecord.setImageResource(R.mipmap.ic_record_stop_default);
                } else {
                    tvRecord.setText("停止");
                    ivRecord.setImageResource(R.mipmap.ic_record_stop_default);
                }
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

                isStartRecord = true;
            }

        } else {
            if (!HCNetSDK.getInstance().NET_DVR_StopSaveRealData(hikPlayID)) {
                UIUtils.showToast("录像停止失败" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            } else {
                if (UIUtils.getResource().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    ivLandscapeRecord.setImageResource(R.mipmap.ic_record_default);
                } else {
                    tvRecord.setText("开始");
                    ivRecord.setImageResource(R.mipmap.ic_record_default);
                }

                UIUtils.showToast("保存成功:" + path);
            }

            isStartRecord = false;

            timer.stop();//计时结束
            ivRecordPoint.clearAnimation();//结束动画
            llRecordTag.setVisibility(View.GONE);
        }
    }

    /**
     * --------横竖屏切换---------
     */
    private boolean isShowAllScreen = false;//是否显示全屏按钮

    private void doScreenDirection() {
        if (isStartRecord) {
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
                rlLandscapeTop.setVisibility(View.VISIBLE);
                ivAllScreen.setVisibility(View.VISIBLE);
            } else {
                rlLandscapeTop.setVisibility(View.GONE);
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
                rlLandscapeTop.setVisibility(View.GONE);
                isShowAllScreen = false;
                ivAllScreen.setImageResource(R.mipmap.icon_screen_out);
                llLandscapeCamera.setVisibility(View.GONE);
                viewpager.setNoScroll(false);//可以滑动
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                rlSv.setLayoutParams(params1);
                llCtrRoot.setVisibility(View.GONE);
                rlTop.setVisibility(View.GONE);
                rlLandscapeTop.setVisibility(View.GONE);
                ivAllScreen.setImageResource(R.mipmap.icon_screen_in);
                ivAllScreen.setVisibility(View.GONE);
                llLandscapeCamera.setVisibility(View.VISIBLE);
                isShowAllScreen = false;
                viewpager.setNoScroll(true);//禁止滑动
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

}
