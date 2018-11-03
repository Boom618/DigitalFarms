package com.ty.digitalfarms.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.constant.HikConstants;
import com.ty.digitalfarms.hikvision.callback.MsgCallback;
import com.ty.digitalfarms.hikvision.callback.MsgIds;
import com.ty.digitalfarms.hikvision.data.TempData;
import com.ty.digitalfarms.hikvision.resource.ResourceControl;
import com.ty.digitalfarms.ui.adapter.CameraAdapter;
import com.ty.digitalfarms.ui.view.LineItemDecoration;
import com.ty.digitalfarms.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraListActivity extends BaseActivity implements View.OnClickListener, MsgCallback {

    @BindView(R.id.iv_left)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_camera)
    RecyclerView rvCamera;

    private String controlId;
    private int pResType;
    private int pId;
    private ResourceControl resControl = new ResourceControl(); //获取资源逻辑控制类
    private ReqResourceMsgHandler reqHandler = new ReqResourceMsgHandler();

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_camera_list);
        StatusBarUtil.setColor(CameraListActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        ivBack.setOnClickListener(this);
        String regionName = getIntent().getStringExtra("regionName");
        controlId = getIntent().getStringExtra("controlId");
        tvTitle.setText(regionName);
        LinearLayoutManager manager = new LinearLayoutManager(UIUtils.getContext());
        rvCamera.addItemDecoration(new LineItemDecoration(UIUtils.getContext(),
                LineItemDecoration.VERTICAL_LIST, 2, R.color.line_color));
        rvCamera.setLayoutManager(manager);

        resControl.setCallback(this);
        getCameraList();
    }

    private void getCameraList() {
        showLoginProgress("获取设备...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                pResType = HikConstants.Resource.TYPE_CTRL_UNIT;
                pId = Integer.parseInt(controlId);
                resControl.reqResList(pResType, pId);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private final class ReqResourceMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                // 从区域获取下级列表成功
                case MsgIds.GET_SUB_F_C_SUC:
                    cancelProgress();
                    refreshResList((List) msg.obj);
                    break;

                case MsgIds.GET_C_F_C_FAIL:
                    // 从控制中心获取下级资源列表成失败
                case MsgIds.GET_SUB_F_C_FAIL:
                    // 调用getRegionListFromRegion失败
                case MsgIds.GET_R_F_R_FAIL:
                    // 调用getCameraListFromRegion失败
                case MsgIds.GET_C_F_R_FAIL:
                    // 从区域获取下级列表失败
                case MsgIds.GET_SUB_F_R_FAILED:
                    cancelProgress();
                    onGetResListFailed();
                    break;

                default:
                    break;
            }
        }
    }

    private void refreshResList(List<CameraInfo> cameraInfos) {
        if (cameraInfos != null) {
            CameraAdapter adapter=new CameraAdapter(UIUtils.getContext(),cameraInfos);
            adapter.setOnItemClickLisener(new CameraAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, Object data) {
                    if (data instanceof ControlUnitInfo){
                        ControlUnitInfo info= (ControlUnitInfo) data;
                        Intent intent = new Intent(CameraListActivity.this, CameraListActivity.class);
                        intent.putExtra("regionName", info.getName());
                        intent.putExtra("controlId", info.getControlUnitID());
                        startActivity(intent);
                    }else if (data instanceof CameraInfo){
                        CameraInfo info= (CameraInfo) data;
                        Intent intent = new Intent(CameraListActivity.this, LiveByDynamicActivity.class);
                        intent.putExtra(HikConstants.IntentKey.CAMERA_ID, info.getId());
                        //传递设备信息
                        TempData.getIns().setCameraInfo(info);
                        startActivity(intent);
                    }
                }
            });
            rvCamera.setAdapter(adapter);
        }
    }

    @Override
    public void onMsg(int msgId, Object data) {
        Message msg = Message.obtain();
        msg.what = msgId;
        msg.obj = data;
        reqHandler.sendMessage(msg);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_left) {
            finish();
        }
    }

    private static ProgressDialog dialog;

    private void showLoginProgress(String string) {
        dialog = ProgressDialog.show(this, "", string);
        dialog.setCancelable(true);
    }

    private void cancelProgress() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    private void onGetResListFailed() {
        UIUtils.showToast(UIUtils.getString(R.string.fetch_cameralist_failed)+ UIUtils.getErrorDesc());
    }

}
