package com.ty.digitalfarms.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.FileInfo;
import com.ty.digitalfarms.constant.ConstantUtil;
import com.ty.digitalfarms.util.FileUtil;
import com.ty.digitalfarms.util.UIUtils;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_photo_num)
    TextView tvPhotoNum;
    @BindView(R.id.tv_video_num)
    TextView tvVideoNum;
    @BindView(R.id.rl_capture)
    RelativeLayout rlCapture;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;

    private SimpleDateFormat mFormat;
    private Map<String,FileInfo> mImgMap;
    private Map<String,FileInfo> mVideoMap;
    private static final int INIT_FILE_COMPLETE=1;//文件初始化完成
    private int imgCount=0,videoCount=0;
    private boolean isInitComplete=false;//文件查找是否完成
    private final static int REQUEST_CODE = 111;
    private final static int RESULT_CODE = 222;

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==INIT_FILE_COMPLETE){
                tvPhotoNum.setText(imgCount+"张");
                tvVideoNum.setText(videoCount+"个");
                isInitComplete=true;
            }
        }
    };

    @Override
    protected void onBaseCreate() {
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_album);
        StatusBarUtil.setColor(AlbumActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        tvTitle.setText(UIUtils.getString(R.string.label_photo_video));
        ivLeft.setOnClickListener(this);
        rlCapture.setOnClickListener(this);
        rlVideo.setOnClickListener(this);
    }

    private void initData() {
        mFormat = new SimpleDateFormat("yyyy-MM-dd");
        mImgMap = new HashMap<>();
        mVideoMap = new HashMap<>();

        new Thread(){
            @Override
            public void run() {
                initFiles();
                Message message=Message.obtain();
                message.what=INIT_FILE_COMPLETE;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private void initFiles() {
        List<String> imgLastModifiedList=new ArrayList<>();
        List<String> videoLastModifiedList=new ArrayList<>();
        String filePath = ConstantUtil.PhotoDir;
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        //遍历文件列表，查询图片文件和MP4文件
        if (files!=null){
            for (File file : files) {
                String fileName = file.getName();
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1,
                        fileName.length()).toLowerCase();//文件扩展名
                if ("jpg".equals(extension) || "png".equals(extension) || "gif".equals(extension)
                        || "jpeg".equals(extension) || "bmp".equals(extension)) {//图片
                    imgCount+=1;
                    addFileByLastModified(file,mImgMap,imgLastModifiedList);
                } else if ("mp4".equals(extension)){//录像
                    videoCount+=1;
                    addFileByLastModified(file,mVideoMap,videoLastModifiedList);
                }
            }
        }

    }

    /**
     * 根据文件的最后修改日期将文件分类
     */
    private void addFileByLastModified(File file,Map<String,FileInfo> map,
                                       List<String> lastModifiedList) {
        String lastModified = mFormat.format(new Date(file.lastModified()));
        double fileSize = FileUtil.getFileSize(file);//文件大小
        //按照日期将文件分类
        if (lastModifiedList.contains(lastModified)){
            FileInfo fileInfo = map.get(lastModified);
            fileInfo.setFileCount(fileInfo.getFileCount()+1);
            List<File> fileList = fileInfo.getFileList();
            fileList.add(file);
            fileInfo.setFileList(fileList);
            BigDecimal bd=new BigDecimal(fileInfo.getFileSize()+fileSize);
            fileSize=bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            fileInfo.setFileSize(fileSize);
            map.put(lastModified,fileInfo);
        }else {
            FileInfo fileInfo=new FileInfo();
            fileInfo.setFileLastModified(lastModified);
            fileInfo.setFileCount(1);
            fileInfo.setFileSize(fileSize);
            List<File> fileList=new ArrayList<>();
            fileList.add(file);
            fileInfo.setFileList(fileList);
            map.put(lastModified,fileInfo);//添加到相册集合
            lastModifiedList.add(lastModified);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;

            case R.id.rl_capture:
                if (imgCount>0&&isInitComplete){
                    Intent intent=new Intent(AlbumActivity.this,FileInfoListActivity.class);
                    intent.putExtra("title","照片");
                    intent.putExtra("fileInfoList", (Serializable) mImgMap);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;

            case R.id.rl_video:
                if (videoCount>0&&isInitComplete){
                    Intent intent=new Intent(AlbumActivity.this,FileInfoListActivity.class);
                    intent.putExtra("title","录像");
                    intent.putExtra("fileInfoList", (Serializable) mVideoMap);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            String title = data.getStringExtra("title");
            Map<String, FileInfo> fileInfoList = (Map<String, FileInfo>) data.getSerializableExtra("fileInfoList");
            if (fileInfoList!=null){
                int count=0;
                for (Map.Entry<String, FileInfo> entry: fileInfoList.entrySet()){
                    count+=entry.getValue().getFileCount();
                }
                if ("照片".equals(title)){
                    mImgMap.clear();
                    mImgMap.putAll(fileInfoList);
                    imgCount=count;
                    tvPhotoNum.setText(imgCount+"张");
                }else {
                    mVideoMap.clear();
                    mVideoMap.putAll(fileInfoList);
                    videoCount=count;
                    tvVideoNum.setText(videoCount+"个");
                }
            }

        }
    }
}