package com.ty.digitalfarms.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/28.
 */

public class RegionAdapter extends RecyclerView.Adapter {

    private List regionList;
    private OnItemClickListener mListener;
    private LayoutInflater mInflater;
    private final Bitmap controlBitmap;

    public RegionAdapter(Context context, List regionList) {
        this.regionList = regionList;
        this.mInflater = LayoutInflater.from(context);
        controlBitmap = BitmapFactory.decodeResource(UIUtils.getResource(), R.mipmap.ic_control);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(mInflater.inflate(R.layout.item_region, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Object regionData = regionList.get(position);
        ControlUnitInfo info = (ControlUnitInfo) regionData;
        String infoName = info.getName();
        String provinceName,regionName;
        if (infoName.contains("省")){
            String[] split = infoName.split("省");
            provinceName=split[0]+"省";
            regionName=split[1];
        }else if (infoName.contains("市")){
            String[] split = infoName.split("市");
            provinceName=split[0]+"市";
            regionName=split[1];
        }else {
            provinceName="";
            regionName=infoName;
        }
        if (provinceName.equals("山西省")){
            ((ItemHolder) holder).llLocate.setBackgroundResource(R.mipmap.ic_bg_shanxi);
        }else {
            ((ItemHolder) holder).llLocate.setBackgroundResource(R.mipmap.ic_bg_henan);
        }
        ((ItemHolder) holder).tvProvince.setText(provinceName);
        ((ItemHolder) holder).tvRegionName.setText(regionName);
        if (mListener != null) {
            ((ItemHolder) holder).llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, regionData);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return regionList.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_province)
        TextView tvProvince;
        @BindView(R.id.tv_region_name)
        TextView tvRegionName;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;
        @BindView(R.id.ll_locate)
        LinearLayout llLocate;
        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
