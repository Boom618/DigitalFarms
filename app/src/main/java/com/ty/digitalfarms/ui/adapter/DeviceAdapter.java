package com.ty.digitalfarms.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.DeviceInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/28.
 */

public class DeviceAdapter extends RecyclerView.Adapter {

    private List<DeviceInfo.ResultBean> deviceList;
    private OnItemClickListener mListener;
    private LayoutInflater mInflater;
    private final static int TYPE_DEVICES=1;
    private final static int TYPE_TITLE=2;

    public DeviceAdapter(Context context, List<DeviceInfo.ResultBean> deviceList) {
        this.deviceList = deviceList;
        this.mInflater = LayoutInflater.from(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DEVICES) {
            return new ItemHolder(mInflater.inflate(R.layout.device_layout, parent, false));
        } else {
            return new TypeItemViewHolder(mInflater.inflate(R.layout.device_type_item, null));
        }
    }

    @Override
    public int getItemViewType(int position) {
        String deviceTypeName = deviceList.get(position).getDeviceTypeName();
        if (TextUtils.isEmpty(deviceTypeName)){
            return TYPE_DEVICES;
        }else {
            return TYPE_TITLE;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DeviceInfo.ResultBean data = deviceList.get(position);
        if (holder instanceof TypeItemViewHolder){
            ((TypeItemViewHolder) holder).tvType.setText(data.getDeviceTypeName());
        }else {
            ((ItemHolder) holder).tvDevicesName.setText(data.getDeviceName());
            ((ItemHolder) holder).tvFarmName.setText(data.getFarmName());
            ((ItemHolder) holder).tvAddress.setText(data.getProvincesLevel() + data.getMunicipalLevel());
            if (data.getTypeCategory() == 1) {
                ((ItemHolder) holder).ivDevices.setImageResource(R.mipmap.ic_camera);
            } else if (data.getTypeCategory() == 2) {
                ((ItemHolder) holder).ivDevices.setImageResource(R.mipmap.weatherstation);
            }
            if (mListener != null) {
                ((ItemHolder) holder).rlRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onItemClick(position,data);
                    }
                });
            }
        }

    }

    class TypeItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_type)
        TextView tvType;

        public TypeItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_devices_name)
        TextView tvDevicesName;
        @BindView(R.id.tv_farm_name)
        TextView tvFarmName;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.iv_devices)
        ImageView ivDevices;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

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
