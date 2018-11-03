package com.ty.digitalfarms.ui.activity;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.SubmitInfo;
import com.ty.digitalfarms.net.HttpMethods;
import com.ty.digitalfarms.net.ProgressSubscriber;
import com.ty.digitalfarms.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdatePwdActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.btn_modify)
    Button btnModify;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String mOldPwd;
    private String mNewPwd;
    private String mConfirmPwd;

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_update_pwd);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        btnModify.setOnClickListener(this);
        tvTitle.setText("实时监控—地图模式");
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void modifyPwd(String json) {
        HttpMethods.getInstance().modifyPwd(new ProgressSubscriber<SubmitInfo>(this, true) {
            @Override
            public void onNext(SubmitInfo info) {

            }
        }, json);
    }

    private String getParam() {
        String json = "";
        mOldPwd = etOldPwd.getText().toString().trim();
        mNewPwd = etNewPwd.getText().toString().trim();
        mConfirmPwd = etConfirmPwd.getText().toString().trim();
        return json;
    }

    private boolean checkValid() {
        if (TextUtils.isEmpty(mOldPwd) || TextUtils.isEmpty(mNewPwd) ||
                TextUtils.isEmpty(mConfirmPwd)) {
            UIUtils.showToast(UIUtils.getString(R.string.info_null));
            return false;
        } else {
            if (mNewPwd.equals(mOldPwd)) {
                UIUtils.showToast(UIUtils.getString(R.string.pwd_no_yiy));
                return false;
            } else {
                if (!mNewPwd.equals(mConfirmPwd)) {
                    UIUtils.showToast(UIUtils.getString(R.string.pwd_no));
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_modify) {
            String json = getParam();
            if (checkValid()) {
                modifyPwd(json);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
