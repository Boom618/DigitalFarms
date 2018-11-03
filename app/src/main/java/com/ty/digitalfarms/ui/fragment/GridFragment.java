package com.ty.digitalfarms.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.DeviceInfo;
import com.ty.digitalfarms.ui.activity.CurrentDataActivity;
import com.ty.digitalfarms.ui.activity.LiveStaticActivity;
import com.ty.digitalfarms.ui.adapter.GridAdapter;
import com.ty.digitalfarms.ui.view.DividerGridItemDecoration;
import com.ty.digitalfarms.util.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridFragment extends Fragment {

    @BindView(R.id.rv_device)
    RecyclerView rvDevice;
    @BindView(R.id.tv_abnormal)
    TextView tvAbnormal;
    @BindView(R.id.ll_abnormal)
    LinearLayout llAbnormal;

    private ArrayList<DeviceInfo.ResultBean> deviceInfos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_fg_layout, null);
        ButterKnife.bind(this, view);
        GridLayoutManager mLayoutManager = new GridLayoutManager(UIUtils.getContext(), 2);
        rvDevice.addItemDecoration(new DividerGridItemDecoration(getActivity(), 15, Color.TRANSPARENT));
        rvDevice.setLayoutManager(mLayoutManager);

        Bundle bundle = getArguments();
        deviceInfos = bundle.getParcelableArrayList("deviceInfos");

        if (deviceInfos == null) {
            llAbnormal.setVisibility(View.VISIBLE);
            tvAbnormal.setText("数据获取失败");
            //Toast.makeText(UIUtils.getContext(), "数据获取失败，请检查网络！", Toast.LENGTH_LONG).show();
        } else {
            initAdapter();
        }
        return view;
    }

    private void initAdapter() {
        if (deviceInfos == null) {
            llAbnormal.setVisibility(View.VISIBLE);
            tvAbnormal.setText("数据获取失败");
            return;
        } else if (deviceInfos.size() == 0) {
            llAbnormal.setVisibility(View.VISIBLE);
            tvAbnormal.setText("没有设备信息");
            return;
        }

        GridAdapter adapter = new GridAdapter(UIUtils.getContext(), deviceInfos);
        adapter.setOnItemClickLisener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, DeviceInfo.ResultBean data) {
                if (data.getTypeCategory() == 1) {
                    Intent intent = new Intent(getActivity(), LiveStaticActivity.class);
                    intent.putParcelableArrayListExtra("deviceList", deviceInfos);
                    intent.putExtra("deviceTag", data.getTag());
                    intent.putExtra("position", position);
                    intent.putExtra("deviceInfo", data);
                    startActivity(intent);
                } else if (data.getTypeCategory() == 2) {
                    Intent intent = new Intent(getActivity(), CurrentDataActivity.class);
                    intent.putExtra("deviceTag", data.getTag());
                    intent.putExtra("deviceInfo", data);
                    startActivity(intent);
                }
            }
        });
        rvDevice.setAdapter(adapter);
    }
}
