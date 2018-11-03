package com.ty.digitalfarms.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.DeviceInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mInflater;
    private List<DeviceInfo.ResultBean> deviceList;
    private OnItemClickListener mListener;

    public GridAdapter(Context context, List<DeviceInfo.ResultBean> deviceList) {
        this.mInflater = LayoutInflater.from(context);
        this.deviceList = deviceList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_device, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        final DeviceInfo.ResultBean deviceInfo = deviceList.get(position);
        itemHolder.tvDevicesName.setText(deviceInfo.getTag());
        itemHolder.tvDevicesLocate.setText(deviceInfo.getFarmName());
        if (deviceInfo.getTypeCategory() == 2) {
            itemHolder.ivDevice.setImageResource(R.mipmap.weather_device);
            ((ItemHolder) holder).ivPlay.setVisibility(View.GONE);
        }
        //点击事件
        if (mListener != null) {
            itemHolder.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, deviceInfo);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_device)
        ImageView ivDevice;
        @BindView(R.id.tv_devices_name)
        TextView tvDevicesName;
        @BindView(R.id.iv_locate)
        ImageView ivLocate;
        @BindView(R.id.tv_devices_locate)
        TextView tvDevicesLocate;
        @BindView(R.id.iv_play)
        ImageView ivPlay;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, DeviceInfo.ResultBean data);
    }

    public void setOnItemClickLisener(OnItemClickListener lisener) {
        this.mListener = lisener;
    }

}
