package com.ty.digitalfarms.ui.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * author: XingZheng
 * date: 2016/9/12.
 * 轮播ViewPager
 */
public class CarouselViewPager extends ViewPager {

    private int displayTime=5000;//图片展示的时间，默认5秒
    private CarouselDirection direction= CarouselDirection.LEFT;//滚动方向，默认向左

    public CarouselViewPager(Context context) {
        super(context);
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 开始轮播
     */
    public void start(){
        stop();
        postDelayed(autoDisplay,displayTime);
    }

    /**
     * 停止轮播
     */
    public void stop() {
        removeCallbacks(autoDisplay);
    }

    /**
     * 设置图片显示时间
     * @param displayTime
     */
    public void setDisplayTime(int displayTime) {
        this.displayTime = displayTime;
    }

    /**
     * 设置图片轮播方向
     * @param direction
     */
    public void setDirection(CarouselDirection direction) {
        this.direction = direction;
    }

    private Runnable autoDisplay=new Runnable() {
        @Override
        public void run() {
            display(direction);
        }
    };

    /**
     * 图片轮播
     * @param direct 方向
     */
    public synchronized void display(CarouselDirection direct){
        PagerAdapter adapter=getAdapter();
        if (adapter!=null){
            int count = adapter.getCount();//图片的张数
            int currentItem = getCurrentItem();//当前展示的是第几张图片
            switch (direct){
                case LEFT:
                    currentItem++;
                    if (currentItem>=count){
                        currentItem=0;
                    }
                    break;

                case RIGHT:
                    currentItem--;
                    if (currentItem<0){
                        currentItem=count-1;
                    }
                    break;
            }
            setCurrentItem(currentItem);
        }
        start();
    }

    /**
     * 滚动方向枚举类
     */
    public enum CarouselDirection{
        LEFT,RIGHT
    }

    @Override
    protected void onFinishInflate() {
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                    if (state== ViewPager.SCROLL_STATE_IDLE){
                        start();
                    }else if (state== ViewPager.SCROLL_STATE_DRAGGING){
                        stop();
                    }
            }
        });
        super.onFinishInflate();
    }
}
