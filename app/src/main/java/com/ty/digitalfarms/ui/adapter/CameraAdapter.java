package com.ty.digitalfarms.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.ty.digitalfarms.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public class CameraAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mInflater;
    private List deviceList;
    private OnItemClickListener mListener;

    public CameraAdapter(Context context, List deviceList) {
        this.mInflater = LayoutInflater.from(context);
        this.deviceList = deviceList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_camera, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        final Object deviceInfo = deviceList.get(position);
        if (deviceInfo instanceof CameraInfo){
            CameraInfo cameraInfo= (CameraInfo) deviceInfo;
            itemHolder.tvRegionName.setText(cameraInfo.getName());
            if (cameraInfo.isOnline()){
                ((ItemHolder) holder).ivLeft.setImageResource(R.mipmap.ic_camera_online);
            }else {
                ((ItemHolder) holder).ivLeft.setImageResource(R.mipmap.ic_camera_no_online);
            }
        }else if (deviceInfo instanceof ControlUnitInfo){
            ControlUnitInfo controlInfo= (ControlUnitInfo) deviceInfo;
            itemHolder.tvRegionName.setText(controlInfo.getName());
            ((ItemHolder) holder).ivLeft.setImageResource(R.mipmap.ic_control);
        }
        //点击事件
        if (mListener != null) {
            itemHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
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
        @BindView(R.id.tv_region_name)
        TextView tvRegionName;
        @BindView(R.id.iv_left)
        ImageView ivLeft;
        @BindView(R.id.iv_right)
        ImageView ivRight;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object data);
    }

    public void setOnItemClickLisener(OnItemClickListener lisener) {
        this.mListener = lisener;
    }

}
