package com.ty.digitalfarms.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.constant.ConstantUtil;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {
    private boolean isLogin;//判断是否登录过

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_splash);
        StatusBarUtil.setTransparent(SplashActivity.this);
        ButterKnife.bind(this);
        initData();
        initEvent();
    }

    private void initData() {
        //获取本地用户信息SharedPreferences数据
        SharedPreferences sp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        isLogin = sp.getBoolean(ConstantUtil.SP_LOGIN_STATUS, false);
    }

    private void initEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin) {
                    startActivity(new Intent(SplashActivity.this, GridActivity.class));
                } else {
                    //跳转到登录界面
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}
