package com.ty.digitalfarms.ui.recylervideo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.DeviceInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/2/6.
 */

public class CardRvAdapter extends RecyclerView.Adapter<CardRvAdapter.ItemViewHolder> {


    private List<DeviceInfo.ResultBean> list;
    private HouseSelectedListener listener;

    public CardRvAdapter(List<DeviceInfo.ResultBean> list) {
        this.list = list;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final DeviceInfo.ResultBean bean = list.get(position);

       // listener.getSelectInfo(bean,position);
        if (clickListener!=null){
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.play(bean,position,holder.sView,holder.load,holder.play);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.s_view)
        SurfaceView sView;
        @BindView(R.id.play)
        ImageView play;
        @BindView(R.id.load)
        ProgressBar load;
        @BindView(R.id.iv_all_screen)
        ImageView ivAllScreen;
        @BindView(R.id.rl_view)
        RelativeLayout rlView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    /**
     * 设置选中回调
     *
     * @param listener
     */
    public void setSelectCallBack(HouseSelectedListener listener) {
        this.listener = listener;
    }

    public interface ButtonClickListener {
        void play(DeviceInfo.ResultBean bean,int position,SurfaceView sView,ProgressBar load,ImageView play);
    }

    public ButtonClickListener clickListener;

    public void setButtonClickListener(ButtonClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
