package com.ty.digitalfarms.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ty.digitalfarms.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public class GridPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private List<File> list;
    private OnItemClickListener mListener;
    private Context context;
    private boolean isShowCheckBox;
    /**
     * 防止Checkbox错乱 做setTag  getTag操作
     */
    private HashMap<Integer, Boolean> checkMap;

    public GridPhotoAdapter(Context context, List<File> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        initCheckMap(list);
    }

    private void initCheckMap(List<File> list) {
        checkMap = new HashMap<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                checkMap.put(i, false);
            }
        }
    }

    public HashMap<Integer, Boolean> getCheckMap() {
        return checkMap;
    }

    public void setCheckMap(HashMap<Integer, Boolean> checkMap) {
        this.checkMap = checkMap;
    }

    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_grid_pic, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.cbSelect.setTag(position);//防止复用导致CheckBox显示混乱
        final File file = list.get(position);
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();//文件扩展名
        if ("mp4".equals(extension)) {
            itemHolder.ivVideoType.setVisibility(View.VISIBLE);
            itemHolder.ivPic.setScaleType(ImageView.ScaleType.FIT_XY);
            itemHolder.ivPic.setImageResource(R.mipmap.ic_load_err);
          /*  Glide.with(context).load(R.mipmap.ic_load_err)
                    .placeholder(R.mipmap.ic_placeholder)
                    .into(itemHolder.ivPic);*/
        } else {
            Glide.with(context).load(file.getPath())
                    .placeholder(R.mipmap.ic_placeholder)
                    .error(R.mipmap.ic_load_err)
                    .into(itemHolder.ivPic);
        }

        if (checkMap != null) {
            itemHolder.cbSelect.setChecked(checkMap.get(position));
        } else {
            itemHolder.cbSelect.setChecked(false);
        }


        if (isShowCheckBox) {
            itemHolder.cbSelect.setVisibility(View.VISIBLE);
            //  itemHolder.cbSelect.setChecked(mCheckStates.get(position, false));
        } else {
            itemHolder.cbSelect.setVisibility(View.INVISIBLE);
            //取消掉Checkbox后不再保存当前选择的状态
            itemHolder.cbSelect.setChecked(false);
            // mCheckStates.clear();
        }

        if (mListener != null) {
            itemHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShowCheckBox) {
                        itemHolder.cbSelect.setChecked(!itemHolder.cbSelect.isChecked());
                    }
                    mListener.onItemClick(position, file, isShowCheckBox);
                }
            });
        }
        itemHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int pos = (int) compoundButton.getTag();
                checkMap.put(pos, b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.iv_video_type)
        ImageView ivVideoType;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;
        @BindView(R.id.cb_select)
        CheckBox cbSelect;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, File file, boolean isShowCheckBox);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


}
