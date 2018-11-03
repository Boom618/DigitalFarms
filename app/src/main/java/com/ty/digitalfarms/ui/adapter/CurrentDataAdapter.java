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
import com.ty.digitalfarms.bean.CurrentData;
import com.ty.digitalfarms.util.Utils;
import com.ty.digitalfarms.util.WindUtil;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentDataAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    private CurrentData.ResultBean data;
    private OnItemClickListener mListener;

    public CurrentDataAdapter(Context context, CurrentData.ResultBean data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.current_data_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        final String unit,name;
        String value;
        int resId;
        switch (position) {
            case 0:
                name = "温度";
                value = data.getTemperature()+"";
                unit = " ℃";
                resId = R.mipmap.icon_temp;
                break;

            case 1:
                name = "湿度";
                value = data.getHumidity()+"";
                unit = " RH";
                resId = R.mipmap.icon_humidity;
                break;
            case 2:
                name = "风速";
               // value = WindUtil.getWindLevel(data.getWindSpeed());
                value=data.getWindSpeed()+"";
                unit = " m/s";
                resId = R.mipmap.icon_wind_speed;
                break;
            case 3:
                name = "风向";
                value = WindUtil.getWindDirection(data.getWindDirection()+"");
                unit = " °";
                resId = R.mipmap.icon_wind_dirc;
                break;
            case 4:
                name = "土温";
                value = data.getSoilTemperature()+"";
                unit = " ℃";
                resId = R.mipmap.icon_soil_temp;
                break;

            case 5:
                name = "土湿";
                value = data.getSoilHumidity()+"";
                unit = " RH";
                resId = R.mipmap.icon_soil_humidity;
                break;
            case 6:
                name = "雨量";
                value = data.getRainfall()+"";
                unit = " mm";
                resId = R.mipmap.icon_rain;
                break;
            case 7:
                name = "CO₂";
                value = data.getCarbondioxide()+"";
                unit = " ppm";
                resId = R.mipmap.icon_co2;
                break;

            case 8:
                name = "光照";
                value = data.getIllumination()+"";
                unit = " LUX";
                resId = R.mipmap.icon_light;
                break;

            default:
                name = "无数据";
                value = "无数据";
                unit="";
                resId = R.mipmap.icon_temp;
                break;
        }

        final String funName = Utils.getFunName(position);
        if (mListener != null) {
            ((ItemHolder) holder).llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, funName, name,unit);
                }
            });

        }
        try {
            double d = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("0.0");
            value = df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (position==2 || position==3){
            itemHolder.tvName.setText(name);
        }else {
            itemHolder.tvName.setText(name+"/"+unit);
        }
        itemHolder.tvValue.setText(value);
        itemHolder.ivIcon.setImageResource(resId);
       // itemHolder.tvValue.setTextColor(UIUtils.getResource().getColor(R.color.time_label));
       // itemHolder.tvName.setTextColor(UIUtils.getResource().getColor(R.color.light_blue));
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_value)
        TextView tvValue;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String funName, String typeName,String unit);
    }

    public void setOnItemClickLisener(OnItemClickListener lisener) {
        this.mListener = lisener;
    }
}
