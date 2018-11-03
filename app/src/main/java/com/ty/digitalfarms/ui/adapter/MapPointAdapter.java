package com.ty.digitalfarms.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.MapPointInfo;
import com.ty.digitalfarms.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 地图搜索地点
 */
public class MapPointAdapter extends RecyclerView.Adapter<MapPointAdapter.ViewHolder> implements Filterable {


    private List<MapPointInfo> list;
    private List<MapPointInfo> mCopyDatas;
    private ItemClickListener onItemClickListener;

    public MapPointAdapter(List<MapPointInfo> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_point)
        TextView tvPoint;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //绑定视图管理者
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MapPointInfo info = list.get(position);
        holder.tvPoint.setText(info.getDevicesName());
        Log.e("TAG",info.getDevicesName());
        if (onItemClickListener!=null){
            holder.tvPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(info.getLatitude(),info.getLongtitude());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.device_name_item, null);
        ViewHolder vh = new ViewHolder(root);
        return vh;
    }

    public interface ItemClickListener {
        //点击事件
        void onItemClickListener(String lat,String lon);
    }

    //设置点击事件
    public void setItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                //子线程中调用
                FilterResults results = new FilterResults();//初始化过滤结果对象
                List<MapPointInfo> newValues = new ArrayList<>();
                if (mCopyDatas==null){
                    mCopyDatas=new ArrayList<>(list);
                }
                if (charSequence==null||charSequence.length()==0){
                    results.count=mCopyDatas.size();
                    results.values=mCopyDatas;
                }else {
                    charSequence=charSequence.toString().toLowerCase();
                    for (int i = 0; i < mCopyDatas.size(); i++) {
                        MapPointInfo info = mCopyDatas.get(i);
                        String devicesName = info.getDevicesName();//耳标号
                        if (devicesName.contains(charSequence)) {
                            newValues.add(info);//满足筛选条件添加该条数据
                        }
                    }
                    results.count = newValues.size();
                    results.values = newValues;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //UI线程中调用
                list= (List<MapPointInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}