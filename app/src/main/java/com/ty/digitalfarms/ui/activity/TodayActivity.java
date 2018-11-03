package com.ty.digitalfarms.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.DayInfo;
import com.ty.digitalfarms.constant.ApiNameConstant;
import com.ty.digitalfarms.net.HttpMethods;
import com.ty.digitalfarms.net.ProgressSubscriber;
import com.ty.digitalfarms.util.UIUtils;
import com.zhouyou.http.exception.ApiException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.line_view)
    LineChart lineView;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_abnormal)
    TextView tvAbnormal;
    @BindView(R.id.ll_abnormal)
    LinearLayout llAbnormal;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    TextView tvRight;
    private String deviceId;
    private String funName;
    private String typeName;

    float maxValueY;
    float minValueY;

    private String unitStr;


    private List<String> xLableList = new ArrayList<>();
    private List<Entry> lineValues;
    private HighLight highLight;
    private String deviceTag;

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_today);
        StatusBarUtil.setColor(TodayActivity.this,getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initData();
        initView();
        getDayInfo();
    }

    private void initView() {
        tvTitle.setText(deviceTag);
        tvName.setText(typeName + "趋势图 " + unitStr);
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        deviceId = intent.getStringExtra("deviceId");
        funName = intent.getStringExtra("funName");
        typeName = intent.getStringExtra("typeName");
        unitStr = intent.getStringExtra("unit");
        deviceTag = intent.getStringExtra("deviceTag");
        lineValues = new ArrayList<>();
    }

    private void getDayInfo() {
        HttpMethods.getInstance().getDayInfo(new ProgressSubscriber<DayInfo>(this, true) {
            @Override
            public void onNext(DayInfo dayInfo) {
                xLableList.clear();
                if (dayInfo != null && "success".equals(dayInfo.getTag())) {
                    DayInfo.DataBean data = dayInfo.getData();
                    if (data != null) {
                        //空气温度
                        List<DayInfo.DataBean.AirTemperatureBean> airTempInfos = data.getAirTemperature();
                        //空气湿度
                        List<DayInfo.DataBean.AirHumidityBean> airHumidityInfos = data.getAirHumidity();
                        //土壤温度
                        List<DayInfo.DataBean.SoilTemperatureBean> soilTempInfos = data.getSoilTemperature();
                        //土壤湿度
                        List<DayInfo.DataBean.SoilHumidityBean> soilHumidityInfos = data.getSoilHumidity();
                        //风速
                        List<DayInfo.DataBean.WindSpeedBean> windSpeedInfos = data.getWindSpeed();
                        //光照
                        List<DayInfo.DataBean.SunshineBean> sunshineInfos = data.getSunshine();
                        //雨量
                        List<DayInfo.DataBean.RainfallBean> rainfallInfos = data.getRainfall();
                        //二氧化碳
                        List<DayInfo.DataBean.CarbonDioxideBean> carbonDioxideInfos = data.getCarbonDioxide();

                        int lastHour = -1;
                        List<Integer> hourList = new ArrayList<>();
                        if (ApiNameConstant.HISTORY_TEMPERATURE.equals(funName) && airTempInfos != null) {
                            lastHour = airTempInfos.get(airTempInfos.size() - 1).getHour();
                            for (int i = 0; i < airTempInfos.size(); i++) {
                                DayInfo.DataBean.AirTemperatureBean bean = airTempInfos.get(i);
                                float value = (float) bean.getValue();

                                int hour = bean.getHour();
                                hourList.add(hour);

                                lineValues.add(new Entry(hour, value));
                                xLableList.add(bean.getHour() + "");
                                setXY(i, value);
                            }
                        } else if (ApiNameConstant.HISTORY_HUMIDITY.equals(funName) && airHumidityInfos != null) {
                            lastHour = airHumidityInfos.get(airHumidityInfos.size() - 1).getHour();
                            for (int i = 0; i < airHumidityInfos.size(); i++) {
                                DayInfo.DataBean.AirHumidityBean bean = airHumidityInfos.get(i);
                                float value = (float) bean.getValue();

                                int hour = bean.getHour();
                                hourList.add(hour);

                                lineValues.add(new Entry(hour, value));

                                xLableList.add(bean.getHour() + "");
                                setXY(i, value);
                            }
                        } else if (ApiNameConstant.HISTORY_SOIL_TEMPERATURE.equals(funName) && soilTempInfos != null) {
                            lastHour = soilTempInfos.get(soilTempInfos.size() - 1).getHour();
                            for (int i = 0; i < soilTempInfos.size(); i++) {
                                DayInfo.DataBean.SoilTemperatureBean bean = soilTempInfos.get(i);
                                float value = (float) bean.getValue();
                                int hour = bean.getHour();
                                hourList.add(hour);
                                lineValues.add(new Entry(hour, value));
                                xLableList.add(bean.getHour() + "");
                                setXY(i, value);
                            }
                        } else if (ApiNameConstant.HISTORY_SOIL_HUMIDITY.equals(funName) && soilHumidityInfos != null) {
                            lastHour = soilHumidityInfos.get(soilHumidityInfos.size() - 1).getHour();
                            for (int i = 0; i < soilHumidityInfos.size(); i++) {
                                DayInfo.DataBean.SoilHumidityBean bean = soilHumidityInfos.get(i);
                                float value = (float) bean.getValue();
                                int hour = bean.getHour();
                                hourList.add(hour);
                                lineValues.add(new Entry(hour, value));
                                xLableList.add(bean.getHour() + "");
                                setXY(i, value);
                            }
                        } else if (ApiNameConstant.HISTORY_CARBONDIOXIDE.equals(funName) && carbonDioxideInfos != null) {
                            lastHour = carbonDioxideInfos.get(carbonDioxideInfos.size() - 1).getHour();
                            for (int i = 0; i < carbonDioxideInfos.size(); i++) {
                                DayInfo.DataBean.CarbonDioxideBean bean = carbonDioxideInfos.get(i);
                                float value = bean.getValue();
                                int hour = bean.getHour();
                                hourList.add(hour);
                                lineValues.add(new Entry(hour, value));
                                xLableList.add(bean.getHour() + "");
                                setXY(i, value);
                            }
                        } else if (ApiNameConstant.HISTORY_RAINFALL.equals(funName) && rainfallInfos != null) {
                            lastHour = rainfallInfos.get(rainfallInfos.size() - 1).getHour();
                            for (int i = 0; i < rainfallInfos.size(); i++) {
                                DayInfo.DataBean.RainfallBean bean = rainfallInfos.get(i);
                                float value = bean.getValue();
                                int hour = bean.getHour();
                                hourList.add(hour);
                                lineValues.add(new Entry(hour, value));
                                xLableList.add(bean.getHour() + "");
                                setXY(i, value);
                            }
                        } else if (ApiNameConstant.HISTORY_WINDGRADE.equals(funName) && windSpeedInfos != null) {
                            lastHour = windSpeedInfos.get(windSpeedInfos.size() - 1).getHour();
                            for (int i = 0; i < windSpeedInfos.size(); i++) {
                                DayInfo.DataBean.WindSpeedBean bean = windSpeedInfos.get(i);
                                float value = bean.getValue();
                                int hour = bean.getHour();
                                hourList.add(hour);
                                lineValues.add(new Entry(hour, value));
                                xLableList.add(bean.getHour() + "");
                                setXY(i, value);
                            }
                        } else if (ApiNameConstant.HISTORY_LUX.equals(funName) && sunshineInfos != null) {
                            lastHour = sunshineInfos.get(sunshineInfos.size() - 1).getHour();
                            for (int i = 0; i < sunshineInfos.size(); i++) {
                                DayInfo.DataBean.SunshineBean bean = sunshineInfos.get(i);
                                float value = bean.getValue() / (float) 1000;
                                int hour = bean.getHour();
                                hourList.add(hour);
                                lineValues.add(new Entry(hour, value));
                                xLableList.add(bean.getHour() + "");
                                setXY(i, value);
                            }
                        } else {
                            setAbnormal(UIUtils.getString(R.string.no_data));
                        }

                        List<Integer> allHour = new ArrayList<>();
                        for (int i = 0; i < lastHour + 1; i++) {
                            allHour.add(i);
                        }
                        allHour.removeAll(hourList);
                        for (int i : allHour) {
                            lineValues.add(i, new Entry(i, 0).setNull_Y());
                            xLableList.add(i, i + "");
                        }
                        //添加折线
                        setLine();

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
            }

        }, deviceId);

    }

    private void setLine() {
        // 高亮
        highLight = lineView.get_HighLight();
        highLight.setEnable(false);// 启用高亮显示  默认为启用状态
        //  highLight.setEnable(true);// 启用高亮显示  默认为启用状态
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "时间:" + (int) value + "点";
            }
        });
        highLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                DecimalFormat df = new DecimalFormat("0.0");
                value = Double.parseDouble(df.format(value));
                return typeName + ":" + value + unitStr;
            }
        });

        // x,y轴上的单位
        XAxis xAxis = lineView.get_XAxis();
        xAxis.setAxisWidth(1);
//        xAxis.setCalWay(Axis.CalWay.every); // 轴线上label的计算方式
        xAxis.set_unit("时间/h");
        xAxis.setUnitColor(Color.BLUE);
        xAxis.setAxisColor(Color.BLUE);
        xAxis.setLabeList(xLableList);
//        yAxis.set_enableUnit(false);
        if (lineValues.size() > 0 && lineValues.size() < 8) {
            xAxis.set_labelCountAdvice(lineValues.size() + 2);
        } else {
            xAxis.set_labelCountAdvice(8);
        }
        xAxis.set_ValueAdapter(new DefaultValueAdapter(0));// 默认精度到小数点后2位

        YAxis yAxis = lineView.get_YAxis();
        yAxis.setAxisWidth(1);
        // yAxis.set_unit(typeName+"/"+unitStr);
        yAxis.setAxisColor(Color.BLUE);
        yAxis.setUnitColor(Color.BLUE);
        yAxis.set_ValueAdapter(new DefaultValueAdapter(0));

        // 数据
        Line line = new Line();
        line.setLineColor(Color.MAGENTA);
        line.setDrawCircle(true);
        line.setEntries(lineValues);

        lineView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        highLight.setEnable(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        highLight.setEnable(false);
                        break;
                }
                return false;
            }
        });

        //设置线属性
        //  line.setCircleR(10);
        line.setLineWidth(5);
        // line.setDrawCircle(false);
        line.setLineColor(UIUtils.getColor(R.color.light_avg));

        //设置X、Y轴最小、最大点
        line.setmYMin((int) minValueY-1);
        // maxValueY = (maxValueY == 0) ? (maxValueY + 5) : maxValueY;
        line.setmYMax((int) maxValueY + 3);
        line.setmXMax(lineValues.size());

        Lines lines = new Lines();
        lines.addLine(line);
        lineView.setLines(lines);

        lineView.animateXY();//横向和纵向动画

       // Log.e("TAG--today",minValueY+"---"+maxValueY);
    }

    private void setXY(int i, float value) {
        if (i == 0) {
            maxValueY = value;
            minValueY = value;
        } else {
            maxValueY = (maxValueY < value) ? value : maxValueY;
            minValueY = (minValueY > value) ? value : minValueY;
        }
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
                Intent intent = new Intent(TodayActivity.this, HistoryActivity.class);
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

    private void setAbnormal(String text) {
        llAbnormal.setVisibility(View.VISIBLE);
        tvAbnormal.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;

            case R.id.iv_right:
                Intent intent = new Intent(TodayActivity.this, HistoryActivity.class);
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
