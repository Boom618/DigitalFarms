package com.ty.digitalfarms.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoDetailActivity extends BaseActivity {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.vp)
    ViewPager vp;
    private List<File> fileList;

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_photo_detail);
        StatusBarUtil.setColor(PhotoDetailActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);

        Intent intent = getIntent();
        fileList = (List<File>) intent.getSerializableExtra("fileList");
        int position = intent.getIntExtra("position", -1);

        tvTitle.setText((position+1) + "/" + fileList.size());
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        List<ImageView> imageViewList = new ArrayList<>();
        for (File file : fileList) {
            ImageView view = new ImageView(this);
            ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(layout);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(file).into(view);
            imageViewList.add(view);
        }
        PhotoAdapter adapter=new PhotoAdapter(this,imageViewList);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new PhotoPageChangeListener());
        vp.setCurrentItem(position);
    }

    class PhotoAdapter extends PagerAdapter {

        Context context;
        List<ImageView> ivList;

        public PhotoAdapter(Context context, List<ImageView> ivList) {
            this.context = context;
            this.ivList = ivList;
        }

        @Override
        public int getCount() {
            return ivList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public ImageView instantiateItem(ViewGroup container, int position) {
            ImageView imageView = ivList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imageView = (ImageView) object;
            container.removeView(imageView);
        }
    }

    class PhotoPageChangeListener implements  ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            tvTitle.setText((position+1) + "/" + fileList.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    }
}
