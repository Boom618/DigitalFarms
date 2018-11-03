package com.ty.digitalfarms.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.DayInfo;
import com.ty.digitalfarms.net.HttpMethods;
import com.ty.digitalfarms.net.ProgressSubscriber;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rorbin.q.radarview.RadarData;
import rorbin.q.radarview.RadarView;

public class TodayWindDircActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.radar_view)
    RadarView mRadarView;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    TextView tvRight;

    private String deviceId;
    private String funName;
    private String typeName;
    private String deviceTag;
    private String unitStr;

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_today_wind_dirc);
        StatusBarUtil.setColor(TodayWindDircActivity.this,getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initData();
        initView();
        getDayInfo();
    }

    private void initData() {
        Intent intent = getIntent();
        deviceId = intent.getStringExtra("deviceId");
        funName = intent.getStringExtra("funName");
        typeName = intent.getStringExtra("typeName");
        deviceTag = intent.getStringExtra("deviceTag");
        unitStr = intent.getStringExtra("unit");
    }

    private void initView() {
        tvTitle.setText(deviceTag);
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);

        tvName.setText(typeName + "趋势图 " + "次");

        mRadarView.setWebMode(RadarView.WEB_MODE_POLYGON);//多边形
        mRadarView.setRotationEnable(false);//不旋转

        mRadarView.setRadarLineColor(0x3300bcd4);

        List<String> vertexText = new ArrayList<>();
        Collections.addAll(vertexText, "东南", "东", "东北", "北", "西北", "西", "西南", "南");
        mRadarView.setVertexText(vertexText);
    }

    private void getDayInfo() {
        HttpMethods.getInstance().getDayInfo(new ProgressSubscriber<DayInfo>(this, true) {
            @Override
            public void onNext(DayInfo dayInfo) {
                if (dayInfo != null && "success".equals(dayInfo.getTag())) {
                    DayInfo.DataBean data = dayInfo.getData();
                    if (data != null) {
                        List<DayInfo.DataBean.WindDirectionBean> windDirection = data.getWindDirection();
                        updateLineView(windDirection);
                    }
                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
            }

        }, deviceId);

    }

    private void updateLineView(List<DayInfo.DataBean.WindDirectionBean> infos) {

        if (infos == null) {
            // TODO: 2017/8/16 没有数据
            return;
        } else if (infos.size() == 0) {
            // TODO: 2017/8/16 数据获取失败
            return;
        }
        float[] values = new float[8];
        List<Float> valueList = new ArrayList<>();
        for (DayInfo.DataBean.WindDirectionBean bean : infos) {
            if ("东南".equals(bean.getValue())) {
                values[0] += 1;
            } else if ("东".equals(bean.getValue())) {
                values[1] += 1;
            } else if ("东北".equals(bean.getValue())) {
                values[2] += 1;
            } else if ("北".equals(bean.getValue())) {
                values[3] += 1;
            } else if ("西北".equals(bean.getValue())) {
                values[4] += 1;
            } else if ("西".equals(bean.getValue())) {
                values[5] += 1;
            } else if ("西南".equals(bean.getValue())) {
                values[6] += 1;
            } else if ("南".equals(bean.getValue())) {
                values[7] += 1;
            }
        }

        List<String> strings = new ArrayList<>();
        for (float f : values) {
            valueList.add(f);
            strings.add((int) f + "");
        }


        RadarData radarData = new RadarData(valueList);
        radarData.setValueText(strings);
        radarData.setValueTextEnable(true);
        radarData.setVauleTextColor(Color.RED);
        mRadarView.addData(radarData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_history:
                Intent intent = new Intent(TodayWindDircActivity.this, WindDircActivity.class);
                intent.putExtra("deviceId", deviceId);
                intent.putExtra("funName", funName);
                intent.putExtra("typeName", typeName);
                intent.putExtra("unit", unitStr);
                intent.putExtra("deviceTag", deviceTag);
                startActivity(intent);
                break;

            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                finish();
                break;

            case R.id.iv_right:
                Intent intent = new Intent(TodayWindDircActivity.this, WindDircActivity.class);
                intent.putExtra("deviceId", deviceId);
                intent.putExtra("funName", funName);
                intent.putExtra("typeName", typeName);
                intent.putExtra("unit", unitStr);
                intent.putExtra("deviceTag", deviceTag);
                startActivity(intent);
                break;
        }
    }
}
