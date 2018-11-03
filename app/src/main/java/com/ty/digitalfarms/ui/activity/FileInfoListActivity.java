package com.ty.digitalfarms.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.FileInfo;
import com.ty.digitalfarms.ui.adapter.FileInfoAdapter;
import com.ty.digitalfarms.ui.view.SpaceItemDecoration;
import com.ty.digitalfarms.util.UIUtils;
import com.ty.digitalfarms.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileInfoListActivity extends BaseActivity {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_file)
    RecyclerView rvFile;
    @BindView(R.id.ll_abnormal)
    LinearLayout llAbnormal;
    @BindView(R.id.tv_abnormal)
    TextView tvAbnormal;
    private String mTitle;

    private Map<String, FileInfo> fileInfoMap;

    private final static int REQUEST_CODE = 111;
    private final static int RESULT_CODE = 222;
    private List<Map.Entry<String, FileInfo>> fileInfoList;
    private FileInfoAdapter adapter;

    @Override
    protected void onBaseCreate() {
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        fileInfoMap = (Map<String, FileInfo>) intent.getSerializableExtra("fileInfoList");
    }

    private void initView() {
        setContentView(R.layout.activity_file_info_list);
        StatusBarUtil.setColor(FileInfoListActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(UIUtils.getContext());
        rvFile.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(10), false));
        rvFile.setLayoutManager(manager);
        tvTitle.setText(mTitle);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });

        if (fileInfoMap != null) {
            fileInfoList = new ArrayList<>(fileInfoMap.entrySet());
            //按照图片的修改日期倒叙排序
            Collections.sort(fileInfoList, new Comparator<Map.Entry<String, FileInfo>>() {
                @Override
                public int compare(Map.Entry<String, FileInfo> entry1, Map.Entry<String, FileInfo> entry2) {
                    String lastModified1 = entry1.getValue().getFileLastModified();
                    String lastModified2 = entry2.getValue().getFileLastModified();
                    int daySub = (int) Utils.getDaySub(lastModified1, lastModified2);
                    return daySub;
                }
            });
            adapter = new FileInfoAdapter(UIUtils.getContext(), fileInfoList);
            adapter.setOnItemClickLisener(new FileInfoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, FileInfo fileInfo) {
                    Intent intent = new Intent(FileInfoListActivity.this, PhotoVideoListActivity.class);
                    intent.putExtra("fileInfo", fileInfo);
                    intent.putExtra("position", position);
                    intent.putExtra("type",mTitle);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
            rvFile.setAdapter(adapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            FileInfo fileInfo = (FileInfo) data.getSerializableExtra("fileInfo");
            int position = data.getIntExtra("position", -1);
            if (fileInfoList != null && fileInfoList.size() > position) {
                Map.Entry<String, FileInfo> entry = fileInfoList.get(position);
                entry.setValue(fileInfo);
                fileInfoList.remove(position);
                if (fileInfo.getFileCount() > 0) {
                    fileInfoList.add(position, entry);
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                if (fileInfoList.size()==0){
                    if ("照片".equals(mTitle)){
                        tvAbnormal.setText("无照片文件");
                    }else {
                        tvAbnormal.setText("无视频文件");
                    }
                    llAbnormal.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doFinish();
            return false; // 事件继续向下传播
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doFinish() {
        if (fileInfoList != null && fileInfoMap != null) {
            for (Map.Entry<String, FileInfo> entry : fileInfoList) {
                fileInfoMap.put(entry.getKey(), entry.getValue());
            }
            Intent intent = new Intent();
            intent.putExtra("title", mTitle);
            intent.putExtra("fileInfoList", (Serializable) fileInfoMap);
            setResult(RESULT_CODE, intent);
        }
        finish();
    }
}
