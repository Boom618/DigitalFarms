package com.ty.digitalfarms.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.BuildConfig;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.FileInfo;
import com.ty.digitalfarms.ui.adapter.GridPhotoAdapter;
import com.ty.digitalfarms.ui.view.SpaceItemDecoration;
import com.ty.digitalfarms.util.FileUtil;
import com.ty.digitalfarms.util.UIUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoVideoListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_picture)
    RecyclerView rvPicture;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.tv_abnormal)
    TextView tvAbnormal;
    @BindView(R.id.ll_abnormal)
    LinearLayout llAbnormal;

    private String mTitle;
    private List<File> mFileList;
    private List<File> mCheckList;//记录选中的CheckBox
    private List<String> mCheckFileNameList;//记录选中的CheckBox文件名
    private boolean mIsShowCheckBox = false;//是否显示CheckBox

    private GridPhotoAdapter adapter;
    private int position;//上一界面中item位置
    private FileInfo fileInfo;
    private final static int RESULT_CODE = 222;
    private List<Map.Entry<String, FileInfo>> list;
    private String type;

    @Override
    protected void onBaseCreate() {
        initData();
        initView();
        initAdapter();
    }

    private void initData() {
        fileInfo = (FileInfo) getIntent().getSerializableExtra("fileInfo");
        position = getIntent().getIntExtra("position", -1);
        type = getIntent().getStringExtra("type");
        if (fileInfo != null) {
            mTitle = fileInfo.getFileLastModified();
            mFileList = fileInfo.getFileList();
        }
        mCheckList = new ArrayList<>();
        mCheckFileNameList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_photo_video_list);
        StatusBarUtil.setColor(PhotoVideoListActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = "图片/录像列表";
        }
        tvTitle.setText(mTitle);
        ivLeft.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    private void initAdapter() {
        if (mFileList != null) {
            GridLayoutManager manager = new GridLayoutManager(UIUtils.getContext(), 3);
            manager.setAutoMeasureEnabled(true);//自适应宽高
            rvPicture.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(3), true));
            rvPicture.setLayoutManager(manager);
            adapter = new GridPhotoAdapter(UIUtils.getContext(), mFileList);
            adapter.setOnItemClickListener(new GridPhotoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, File file, boolean isShowCheck) {
                    if (mIsShowCheckBox) {
                        doCheckPic(file);
                    } else {
                        toDetailActivity(position, file);
                    }
                }
            });
            rvPicture.setAdapter(adapter);
        }
    }

    private void doCheckPic(File file) {
        if (mCheckFileNameList.contains(file.getName())) {
            mCheckList.remove(file);
            mCheckFileNameList.remove(file.getName());
            tvRight.setText("全选");
            mIsAllChecked = false;
        } else {
            mCheckList.add(file);
            mCheckFileNameList.add(file.getName());
            HashMap<Integer, Boolean> checkMap = adapter.getCheckMap();
            if (!checkMap.containsValue(false)) {
                tvRight.setText("全不选");
                mIsAllChecked = true;
            } else {
                mIsAllChecked = false;
            }
        }
    }

    private void toDetailActivity(int position, File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();//文件扩展名
        if ("mp4".equals(extension)) {
            //调用系统播放器播放视频
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(UIUtils.getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", file);
                intent.setDataAndType(contentUri, "video/*");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "video/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            startActivity(intent);
        } else {
            Intent intent = new Intent(PhotoVideoListActivity.this, PhotoDetailActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("fileList", (Serializable) mFileList);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                doFinish();
                break;

            case R.id.tv_left:
                hideCheckBox();
                break;

            case R.id.iv_right:
                showCheckBox();
                break;

            case R.id.tv_right:
                if (mIsAllChecked) {
                    //取消全选
                    tvRight.setText("全选");
                } else {
                    //全选
                    tvRight.setText("全不选");
                }
                mIsAllChecked = !mIsAllChecked;
                doCheck(mIsAllChecked);
                break;

            case R.id.btn_delete:
                for (File file : mCheckList) {
                    FileUtil.deleteFile(file);
                    mFileList.remove(file);
                }
                mCheckList.clear();
                mCheckFileNameList.clear();
                HashMap<Integer, Boolean> checkMap = new HashMap<>();
                for (int i = 0; i < mFileList.size(); i++) {
                    checkMap.put(i, false);
                }
                adapter.setCheckMap(checkMap);
                mIsAllChecked = false;
                tvRight.setText("全选");
                adapter.notifyDataSetChanged();

                if (mFileList.size() == 0) {
                    if ("照片".equals(type)) {
                        tvAbnormal.setText("无照片文件");
                    } else {
                        tvAbnormal.setText("无视频文件");
                    }
                    llAbnormal.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    private void hideCheckBox() {
        ivRight.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        if (adapter != null) {
            adapter.setShowCheckBox(false);
            adapter.notifyDataSetChanged();
            mCheckList.clear();
            mCheckFileNameList.clear();
            mIsShowCheckBox = false;
        }
    }

    private void showCheckBox() {
        ivRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.GONE);
        tvLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        if (adapter != null) {
            adapter.setShowCheckBox(true);
            adapter.notifyDataSetChanged();
            mIsShowCheckBox = true;
        }
    }

    private boolean mIsAllChecked = false;

    private void doCheck(boolean isAllChecked) {
        HashMap<Integer, Boolean> checkMap = adapter.getCheckMap();
        if (checkMap != null) {
            for (Map.Entry<Integer, Boolean> entry : checkMap.entrySet()) {
                entry.setValue(isAllChecked);
            }
            adapter.notifyDataSetChanged();
        }
        mCheckList.clear();
        mCheckFileNameList.clear();
        if (isAllChecked) {
            for (File file : mFileList) {
                mCheckList.add(file);
                mCheckFileNameList.add(file.getName());
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsShowCheckBox) {
                hideCheckBox();
            } else {
                doFinish();
            }
            return false; // 事件继续向下传播
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doFinish() {
        if (fileInfo != null) {
            fileInfo.setFileCount(mFileList.size());
            fileInfo.setFileList(mFileList);
            double fileSize = 0;
            for (File file : mFileList) {
                fileSize += FileUtil.getFileSize(file);
            }
            fileInfo.setFileSize(fileSize);
            Intent intent = new Intent();
            intent.putExtra("fileInfo", fileInfo);
            intent.putExtra("position", position);
            setResult(RESULT_CODE, intent);
        }
        finish();
    }

}
