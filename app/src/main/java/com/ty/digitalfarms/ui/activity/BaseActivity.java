package com.ty.digitalfarms.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//当系统版本为5.0以上
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            //设置状态栏字体颜色为深色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//当系统版本为4.4以上
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            //4.4状态栏字体着色
            SystemBarTintManager manager=new SystemBarTintManager(this);
            manager.setStatusBarTintEnabled(true);
            manager.setNavigationBarTintEnabled(true);
            manager.setTintColor(UIUtils.getColor(R.color.tv_minor_color));
        }*/
        onBaseCreate();
       // AndroidBug5497Workaround.assistActivity(this);
    }
    protected abstract void  onBaseCreate();
}
