package com.ty.digitalfarms.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.FileInfo;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public class FileInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Map.Entry<String, FileInfo>> list;
    private OnItemClickListener mListener;

    public FileInfoAdapter(Context context, List<Map.Entry<String, FileInfo>> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_file_info, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        Map.Entry<String, FileInfo> entry = list.get(position);
        final FileInfo fileInfo = entry.getValue();
        itemHolder.tvDate.setText(fileInfo.getFileLastModified());
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSize= df.format(fileInfo.getFileSize());
        itemHolder.tvFileInfo.setText("( "+fileInfo.getFileCount()+"个文件，"+fileSize+"M )");
        if (mListener != null) {
            itemHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, fileInfo);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_file_info)
        TextView tvFileInfo;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position,FileInfo data);
    }

    public void setOnItemClickLisener(OnItemClickListener lisener) {
        this.mListener = lisener;
    }

}
