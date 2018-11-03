package com.ty.digitalfarms.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.GXLoginInfo;
import com.ty.digitalfarms.bean.LoginInfo;
import com.ty.digitalfarms.constant.ApiNameConstant;
import com.ty.digitalfarms.constant.ConstantUtil;
import com.ty.digitalfarms.net.HttpMethods;
import com.ty.digitalfarms.net.ProgressSubscriber;
import com.ty.digitalfarms.util.UIUtils;
import com.ty.digitalfarms.util.Utils;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_userNo)
    EditText etUserPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private String mUserPhone;
    private String mPassword;
    private SharedPreferences sp;


    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_login);
        StatusBarUtil.setTransparent(LoginActivity.this);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        sp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        btnLogin.setOnClickListener(this);
    }

    /**
     * 河北登录
     *
     * @param userNo
     * @param password
     */
    private void login(String userNo, String password) {
        HttpMethods.getInstance().login(new ProgressSubscriber<LoginInfo>(this, true) {
            @Override
            public void onNext(LoginInfo loginInfo) {
                if (loginInfo != null && loginInfo.getCode() == 0) {
                    List<LoginInfo.UserInfo> data = loginInfo.getData();
                    if (data != null && data.size() > 0) {
                        String companyno = data.get(0).getCompanyno();
                        String username = data.get(0).getUserName();
                        sp.edit().putString(ConstantUtil.SP_COMPANY_NO, companyno)
                                .putString(ConstantUtil.SP_USER_NAME, username)
                                .putBoolean(ConstantUtil.SP_LOGIN_STATUS, true).apply();
                        //保存控制中心信息
                        LoginInfo.ServiceInfo serviceInfo = loginInfo.getService();
                        if (serviceInfo!=null){
                            getSharedPreferences(ConstantUtil.SERVICE_SP_NAME, MODE_PRIVATE).edit()
                                    .putString(ConstantUtil.SP_SERVICE_ADDRESS, serviceInfo.getServerAddress())
                                    .putString(ConstantUtil.SP_SERVICE_NAME, serviceInfo.getUserName())
                                    .putString(ConstantUtil.SP_SERVICE_PASSWORD, serviceInfo.getPassword())
                                    .apply();
                        }


                        startActivity(new Intent(LoginActivity.this, GridActivity.class));
                        finish();
                    } else {
                        UIUtils.showToast("登录失败，请确认账户是否存在！");
                    }
                } else {
                    UIUtils.showToast("登录失败，请确认账户是否存在！");
                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                e.printStackTrace();
                UIUtils.showToast(e.getMessage());
            }
        }, userNo, password);
    }

    /**
     * 广西登录
     *
     * @param userNo
     * @param password
     */
    private void doLogin4GX(String userNo, String password) {
        new HttpMethods(ApiNameConstant.LOGIN_URL).login4GX(new ProgressSubscriber<GXLoginInfo>(this, true) {
            @Override
            public void onNext(GXLoginInfo loginInfo) {
                if (loginInfo != null && loginInfo.getCode() == 0) {
                    // [C000032, 18278356899, 余运中, 000001]
                    String mCompanyNo = loginInfo.getUsers().get(0);
                    String mUserName = loginInfo.getUsers().get(2);
                    String mUserNo = loginInfo.getUsers().get(3);
                    saveAccountInfo(mCompanyNo, mUserNo,mUserName, true);
                    Intent intent = new Intent(LoginActivity.this, GridActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    UIUtils.showToast("登录失败，请确认账户是否存在！");
                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                e.printStackTrace();
                UIUtils.showToast(e.getMessage());
            }

        }, userNo, password);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_login) {
            if (isValid()) {
              //  doLogin4GX(mUserPhone, mPassword);
                login(mUserPhone,mPassword);
            }
        }
    }

    private boolean isValid() {
        mUserPhone = etUserPhone.getText().toString().trim();
        mPassword = etPwd.getText().toString().trim();
         mPassword = Utils.getMD5(etPwd.getText().toString().trim());//大三农和河北加密
        if (TextUtils.isEmpty(mUserPhone)) {
            UIUtils.showToast(getResources().getString(R.string.userNo_null));
            return false;
        } else if (TextUtils.isEmpty(mPassword) && TextUtils.isEmpty(Utils.getMD5(mPassword))) {
            UIUtils.showToast(getResources().getString(R.string.password_null));
            return false;
        }
        return true;
    }

    private void saveAccountInfo(String companyNo, String userNo, String userName,boolean isLogin) {
        sp.edit().putString(ConstantUtil.SP_COMPANY_NO, companyNo)
                .putString(ConstantUtil.SP_USERNO, userNo)
                .putString(ConstantUtil.SP_USER_NAME, userName)
                .putBoolean(ConstantUtil.SP_LOGIN_STATUS, isLogin)
                .apply();
    }

}
