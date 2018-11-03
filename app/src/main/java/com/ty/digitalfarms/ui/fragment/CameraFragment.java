package com.ty.digitalfarms.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.constant.ConstantUtil;
import com.ty.digitalfarms.constant.HikConstants;
import com.ty.digitalfarms.hikvision.callback.MsgCallback;
import com.ty.digitalfarms.hikvision.callback.MsgIds;
import com.ty.digitalfarms.hikvision.data.TempData;
import com.ty.digitalfarms.hikvision.resource.ResourceControl;
import com.ty.digitalfarms.ui.activity.CameraListActivity;
import com.ty.digitalfarms.ui.adapter.RegionAdapter;
import com.ty.digitalfarms.ui.view.SpaceItemDecoration;
import com.ty.digitalfarms.ui.view.banner.Banner;
import com.ty.digitalfarms.ui.view.banner.GlideImageLoader;
import com.ty.digitalfarms.util.UIUtils;
import com.ty.digitalfarms.util.Utils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class CameraFragment extends Fragment implements MsgCallback {

    @BindView(R.id.rv_region)
    RecyclerView rvRegion;
    @BindView(R.id.banner_view)
    Banner bannerView;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;

    private Integer[] imgRes = {
            R.mipmap.pic1,
            R.mipmap.pic2,
            R.mipmap.pic3,
    };

    private LoginMsgHandler loginHandler = new LoginMsgHandler();
    private ReqResourceMsgHandler reqHandler = new ReqResourceMsgHandler();
    private ServInfo servInfo = new ServInfo();//登录成功返回的数据
    //父节点资源类型，TYPE_UNKNOWN表示首次获取资源列表
    private int pResType = HikConstants.Resource.TYPE_UNKNOWN;
    //父控制中心的id，只有当parentResType为TYPE_CTRL_UNIT才有用
    private int pCtrlUnitId;
    //父区域的id，只有当parentResType为TYPE_REGION才有用
    private int pRegionId;
    //获取资源逻辑控制类
    private ResourceControl resControl = new ResourceControl();
    private String serverAddress;
    private String userName;
    private String password;
    private ImageView[] mIndicatorPts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regon, null);
        ButterKnife.bind(this, view);
        initView();

        initData();
        doConnServer();//连接设备控制中心
        return view;
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(UIUtils.getContext());
        rvRegion.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(5),false));
        rvRegion.setLayoutManager(manager);
        resControl.setCallback(this);

        //初始化轮播图小点
        mIndicatorPts = new ImageView[imgRes.length];
        for (int i = 0; i < mIndicatorPts.length; i++) {
            ImageView point = new ImageView(getActivity());
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(15, 15);
            layout.setMargins(10, 0, 10, 0);
            point.setScaleType(ImageView.ScaleType.CENTER_CROP);
            point.setLayoutParams(layout);
            mIndicatorPts[i] = point;
            if (i == 0) {
                mIndicatorPts[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            } else {
                mIndicatorPts[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
            llPoint.addView(point);
        }

        bannerView.setImages(Arrays.asList(imgRes))
                .setImageLoader(new GlideImageLoader())
                .addOnPageChangeListener(new OnPagerChange())
                .init();
    }

    class OnPagerChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            int selectItem = position % imgRes.length;
            for (int i = 0; i < mIndicatorPts.length; i++) {
                if (i == selectItem) {
                    mIndicatorPts[i].setBackgroundResource(R.mipmap.page_indicator_focused);
                } else {
                    mIndicatorPts[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    }

    private void initData() {
        SharedPreferences sp = getActivity().getSharedPreferences(ConstantUtil.SERVICE_SP_NAME, MODE_PRIVATE);
        serverAddress = sp.getString(ConstantUtil.SP_SERVICE_ADDRESS, null);
        userName = sp.getString(ConstantUtil.SP_SERVICE_NAME, null);
        password = sp.getString(ConstantUtil.SP_SERVICE_PASSWORD, null);
        if (TextUtils.isEmpty(serverAddress) || TextUtils.isEmpty(userName)
                || TextUtils.isEmpty(password)) {
            UIUtils.showToast("获取服务器信息失败，请联系管理员");
        }
    }

    protected void doConnServer() {
        final String macAddress = Utils.getMacAddress();

        // 新线程进行登录操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                loginHandler.sendEmptyMessage(HikConstants.Login.SHOW_LOGIN_PROGRESS);
                // 登录请求
                boolean ret = VMSNetSDK.getInstance().login(serverAddress, userName, password, 1,
                        macAddress, 4, servInfo);
                if (ret) {
                    //保存登录成功返回的数据
                    TempData.getInstance().setLoginData(servInfo);
                    reqResList();
                } else {
                    loginHandler.sendEmptyMessage(HikConstants.Login.LOGIN_FAILED);
                }

            }
        }).start();
    }

    /**
     * 获取当前控制中心信息
     */
    private void reqResList() {
        int pId = 0;
        if (HikConstants.Resource.TYPE_CTRL_UNIT == pResType) {
            pId = pCtrlUnitId;
        } else if (HikConstants.Resource.TYPE_REGION == pResType) {
            pId = pRegionId;
        }
        resControl.reqResList(pResType, pId);
    }

    @SuppressLint("HandlerLeak")
    private final class LoginMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HikConstants.Login.SHOW_LOGIN_PROGRESS:
                    showLoginProgress("连接服务器...");
                    break;

                case HikConstants.Login.CANCEL_LOGIN_PROGRESS:
                    cancelProgress();
                    break;

                case HikConstants.Login.LOGIN_FAILED:
                    // 登录失败
                    onLoginFailed();
                    break;

                default:
                    break;
            }

        }
    }

    @SuppressLint("HandlerLeak")
    private final class ReqResourceMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 获取控制中心列表成功
                case MsgIds.GET_C_F_NONE_SUC:
                    List ctrData = (List) msg.obj;
                    if (ctrData != null && ctrData.size() > 0) {
                        getRegionList(ctrData.get(0));
                    }
                    break;

                // 从控制中心获取下级资源列表成功
                case MsgIds.GET_SUB_F_C_SUC:
                    cancelProgress();
                    refreshResList((List) msg.obj);
                    break;

                // 获取控制中心列表失败
                case MsgIds.GET_C_F_NONE_FAIL:
                    // 调用getControlUnitList失败
                case MsgIds.GET_CU_F_CU_FAIL:
                    // 调用getRegionListFromCtrlUnit失败
                case MsgIds.GET_R_F_C_FAIL:
                    // 调用getCameraListFromCtrlUnit失败
                case MsgIds.GET_C_F_C_FAIL:
                    // 从控制中心获取下级资源列表成失败
                case MsgIds.GET_SUB_F_C_FAIL:
                    // 调用getRegionListFromRegion失败
                case MsgIds.GET_SUB_F_R_FAILED:
                    cancelProgress();
                    onGetResListFailed();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onMsg(int msgId, Object data) {
        Message msg = Message.obtain();
        msg.what = msgId;
        msg.obj = data;
        reqHandler.sendMessage(msg);
    }

    /**
     * 获取控制中心下的区域
     *
     * @param itemData
     */
    private void getRegionList(final Object itemData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pId = 0;
                if (itemData instanceof ControlUnitInfo) {
                    ControlUnitInfo info = (ControlUnitInfo) itemData;
                    pResType = HikConstants.Resource.TYPE_CTRL_UNIT;
                    pId = Integer.parseInt(info.getControlUnitID());
                }
                resControl.reqResList(pResType, pId);
            }
        }).start();
    }

    private void refreshResList(List<RegionInfo> data) {
        if (data == null || data.isEmpty()) {
            UIUtils.showToast(UIUtils.getString(R.string.no_data));
            return;
        }
        RegionAdapter adapter = new RegionAdapter(UIUtils.getContext(), data);
        adapter.setOnItemClickListener(new RegionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final Object itemData) {
                if (itemData instanceof ControlUnitInfo) {
                    ControlUnitInfo info = (ControlUnitInfo) itemData;
                    Intent intent = new Intent(getActivity(), CameraListActivity.class);
                    intent.putExtra("regionName", info.getName());
                    intent.putExtra("controlId", info.getControlUnitID());
                    startActivity(intent);
                }

            }
        });

        rvRegion.setAdapter(adapter);
    }

    private void onGetResListFailed() {
        UIUtils.showToast(UIUtils.getString(R.string.fetch_reslist_failed));
    }

    private static ProgressDialog dialog;

    private void showLoginProgress(String string) {
        dialog = ProgressDialog.show(getActivity(), "", string);
        dialog.setCancelable(true);
    }

    private void cancelProgress() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    public void onLoginFailed() {
        cancelProgress();
        UIUtils.showToast(UIUtils.getString(R.string.login_failed) + "--errDesc:" + UIUtils.getErrorDesc());
    }

}
