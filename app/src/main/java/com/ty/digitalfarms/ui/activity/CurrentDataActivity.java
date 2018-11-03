package com.ty.digitalfarms.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.CurrentData;
import com.ty.digitalfarms.bean.DeviceInfo;
import com.ty.digitalfarms.constant.ApiNameConstant;
import com.ty.digitalfarms.net.HttpMethods;
import com.ty.digitalfarms.net.ProgressSubscriber;
import com.ty.digitalfarms.ui.adapter.CurrentDataAdapter;
import com.ty.digitalfarms.ui.view.DividerGridItemDecoration;
import com.ty.digitalfarms.util.DateUtil;
import com.ty.digitalfarms.util.UIUtils;
import com.zhouyou.http.exception.ApiException;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CurrentDataActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.iv_weather)
    ImageView ivWeather;
    @BindView(R.id.tv_temp_desc)
    TextView tvTempDesc;
    @BindView(R.id.tv_temp_section)
    TextView tvTempSection;
    @BindView(R.id.rl_weather)
    RelativeLayout rlWeather;
    @BindView(R.id.tv_abnormal)
    TextView tvAbnormal;
    @BindView(R.id.ll_abnormal)
    LinearLayout llAbnormal;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String deviceId;
    public static final int REQUEST_PERMISSION = 0;
    private String realLatLng;
    private String provincesLevel;
    private String deviceTag;

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_current_data);
        StatusBarUtil.setColor(CurrentDataActivity.this,getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initData();
        tvTitle.setText("气象站—" + deviceTag);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //getWeatherInfo(realLatLng);
    }

    private void initData() {
        DeviceInfo.ResultBean deviceInfo = getIntent().getParcelableExtra("deviceInfo");
        deviceId = deviceInfo.getDeviceID() + "";
        deviceTag = deviceInfo.getTag();
        getCurrentData();
        realLatLng = deviceInfo.getLongtitude() + "," + deviceInfo.getLatitude();
        provincesLevel = deviceInfo.getProvincesLevel();
    }

    private void getCurrentData() {
        HttpMethods.getInstance().getRealTimeInfo(new ProgressSubscriber<CurrentData>(this, true) {
            @Override
            public void onNext(CurrentData currentData) {
                if (currentData != null && "success".equals(currentData.getTag())) {
                    List<CurrentData.ResultBean> datas = currentData.getResult();
                    if (datas != null) {
                        CurrentData.ResultBean datasBean = datas.get(0);
                        initAdapter(datasBean);
                    } else {
                        setAbnormal(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    setAbnormal(UIUtils.getString(R.string.no_data));
                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                setAbnormal(UIUtils.getString(R.string.err));
               // Toast.makeText(UIUtils.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }, deviceId);
    }

    private void initAdapter(CurrentData.ResultBean datasBean) {
        if (datasBean == null) {
            setAbnormal(UIUtils.getString(R.string.no_data));
            return;
        }
        String updateTime = datasBean.getMaxtime();
        if (!TextUtils.isEmpty(updateTime)) {
            updateTime = updateTime.replace("T", " ").substring(0, 19);
            tvUpdateTime.setText("更新时间：" + DateUtil.addTime(updateTime, Calendar.HOUR_OF_DAY, 8));
        }
        tvTemp.setVisibility(View.VISIBLE);
        tvTemp.setText(datasBean.getTemperature() + "º");
        GridLayoutManager manager = new GridLayoutManager(UIUtils.getContext(), 3);
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new DividerGridItemDecoration(this, 15, Color.TRANSPARENT));
        CurrentDataAdapter adapter = new CurrentDataAdapter(UIUtils.getContext(), datasBean);
        adapter.setOnItemClickLisener(new CurrentDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String funName, String typeName, String unit) {
                Intent intent;
                if (ApiNameConstant.HISTORY_WINDDIRECTION.equals(funName)) {
                    intent = new Intent(CurrentDataActivity.this, TodayWindDircActivity.class);
                } else {
                    intent = new Intent(CurrentDataActivity.this, TodayActivity.class);
                }

                //光照
                if (position == 8) {
                    unit = "10³" + unit;
                }
                intent.putExtra("deviceId", deviceId);
                intent.putExtra("funName", funName);
                intent.putExtra("typeName", typeName);
                intent.putExtra("unit", unit);
                intent.putExtra("deviceTag", deviceTag);
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // initLocation();
                    return;
                } else {
                    Toast.makeText(CurrentDataActivity.this, "你拒绝了定位权限，所以无法获取天气信息！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void setAbnormal(String text) {
        llAbnormal.setVisibility(View.VISIBLE);
        tvAbnormal.setText(text);
       // rlWeather.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.INVISIBLE);
    }


}
