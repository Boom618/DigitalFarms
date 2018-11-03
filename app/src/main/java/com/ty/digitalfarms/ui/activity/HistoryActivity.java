package com.ty.digitalfarms.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import com.ty.digitalfarms.bean.HistoryInfo;
import com.ty.digitalfarms.constant.ApiNameConstant;
import com.ty.digitalfarms.constant.ApiParamConstant;
import com.ty.digitalfarms.ui.view.SimpleTextWatcher;
import com.ty.digitalfarms.util.DateUtil;
import com.ty.digitalfarms.util.UIUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.PointValue;

public class HistoryActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.line_view)
    LineChart lineView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_start)
    LinearLayout llStart;
    @BindView(R.id.ll_end)
    LinearLayout llEnd;
    @BindView(R.id.ll_flag)
    LinearLayout llFlag;
    @BindView(R.id.tv_select_start)
    TextView tvSelectStart;
    @BindView(R.id.tv_select_end)
    TextView tvSelectEnd;
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


    private String funName;
    private String typeName;

    private float yMax;
    private float yMin;
    private float yAvgMax;
    private float yAvgMin;
    private String deviceId;
    private String unitStr;


    private List<String> xLableList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();
    private List<PointValue> avgValues, maxValues, minValues;
    private HighLight highLight;
    private String deviceTag;

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_history);
        StatusBarUtil.setColor(HistoryActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initData();
        initView();
        // getHistoryData(deviceId, DateUtil.getStateTime(-7), DateUtil.getCurrentDate(), funName);
        tvSelectStart.setText(DateUtil.getStateTime(-7));
        tvSelectEnd.setText(DateUtil.getCurrentDate());
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
        tvName.setText(typeName + "趋势图 " + unitStr);
        llStart.setOnClickListener(this);
        llEnd.setOnClickListener(this);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSelectStart.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                //判断是否选择了截止日期
                String endStr = tvSelectEnd.getText().toString().trim();
                String startStr = tvSelectStart.getText().toString().trim();
                if (!endStr.equals(UIUtils.getString(R.string.select_end_time))) {
                    reGetData(startStr, endStr);
                }
            }
        });

        tvSelectEnd.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                //判断是否选择了开始日期
                String startStr = tvSelectStart.getText().toString().trim();
                String endStr = tvSelectEnd.getText().toString().trim();
                if (!startStr.equals(UIUtils.getString(R.string.select_start_time))) {
                    reGetData(startStr, endStr);
                }
            }
        });
    }

    private void getHistoryData(String deviceId, String startTime, String endTime, String functionName) {
        final IProgressDialog dialog = UIUtils.getDialog(HistoryActivity.this);
        EasyHttp.get(functionName)
                .baseUrl(ApiNameConstant.BASE_URL)
                .params(ApiParamConstant.START_TIME, startTime)
                .params(ApiParamConstant.END_TIME, endTime)
                .params(ApiParamConstant.DEVICES_ID, deviceId)
                .execute(new ProgressDialogCallBack<String>(dialog, true, true) {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        setAbnormal(UIUtils.getString(R.string.err));
                        Toast.makeText(UIUtils.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        Log.e("TAG", response);
                        Gson gson = new Gson();
                        try {
                            HistoryInfo historyData = gson.fromJson(response, HistoryInfo.class);
                            if (historyData != null) {
                                List<HistoryInfo.ResultBean> infos = historyData.getData();
                                loadData(infos);
                            } else {
                                setAbnormal(UIUtils.getString(R.string.no_data));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void loadData(List<HistoryInfo.ResultBean> tempList) {
        if (tempList == null || tempList.size() == 0) {
            setAbnormal(UIUtils.getString(R.string.no_data));
            return;
        } else {
            llFlag.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            tvAbnormal.setVisibility(View.GONE);
        }
        xLableList.clear();
        dateList.clear();
        avgValues = new ArrayList<>();
        maxValues = new ArrayList<>();
        minValues = new ArrayList<>();

        try {
            for (int i = 0; i < tempList.size(); i++) {

                float avg = (float) tempList.get(i).getAvgValue();
                float max = (float) tempList.get(i).getMaxValue();
                float min = (float) tempList.get(i).getMinValue();
                if (ApiNameConstant.HISTORY_LUX.equals(funName)) {
                    avg = avg / (float) 1000;
                    max = max / (float) 1000;
                    min = min / (float) 1000;
                }
                avgValues.add(new PointValue(i, avg));
                maxValues.add(new PointValue(i, max));
                minValues.add(new PointValue(i, min));
                if (i == 0) {
                    yMin = min;
                    yMax = max;
                    yAvgMin = avg;
                    yAvgMax = avg;
                } else {
                    yMax = (yMax <= max) ? max : yMax;
                    yMin = (yMin >= min) ? min : yMin;
                    yAvgMax = (yAvgMax < avg) ? avg : yAvgMax;
                    yAvgMin = (yAvgMin > avg) ? avg : yAvgMin;
                }
                //x轴数据集
                String dateStr = tempList.get(i).getWeatherDate();
                xLableList.add(dateStr.substring(5, dateStr.length()));
                dateList.add(dateStr);
            }

            if (yMax == 0.0 && yMin == 0.0) {
                maxValues.clear();
                minValues.clear();
                yMin=yAvgMin;
                yMax=yAvgMax;
                llFlag.setVisibility(View.GONE);
            }
            setLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*    private void loadData(List<HistoryInfo.ResultBean> tempList) {
        if (tempList == null || tempList.size() == 0) {
            setAbnormal(UIUtils.getString(R.string.no_data));
            return;
        } else {
            llFlag.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            tvAbnormal.setVisibility(View.GONE);
        }
        xLableList.clear();
        dateList.clear();
        avgValues = new ArrayList<>();
        maxValues = new ArrayList<>();
        minValues = new ArrayList<>();

        try {
            for (int i = 0; i < tempList.size(); i++) {

                float avg = (float) tempList.get(i).getAvgValue();
                if (ApiNameConstant.HISTORY_LUX.equals(funName)) {
                    avg = avg / (float) 1000;
                }
                avgValues.add(new PointValue(i, avg));

                if (i == 0) {
                    yMin = avg;
                    yMax = avg;
                } else {
                    yMax = (yMax < avg) ? avg : yMax;
                    yMin = (yMin > avg) ? avg : yMin;
                }

                if (tempList.get(i).getMaxValue() != 0.0 || tempList.get(i).getMinValue() != 0.0) {
                    float max = (float) tempList.get(i).getMaxValue();
                    float min = (float) tempList.get(i).getMinValue();
                    if (ApiNameConstant.HISTORY_LUX.equals(funName)) {
                        max = max / (float) 1000;
                        min = min / (float) 1000;
                    }
                    maxValues.add(new PointValue(i, max));
                    minValues.add(new PointValue(i, min));
                    if (i == 0) {
                        yMin = min;
                        yMax = max;
                    } else {
                        yMax = (yMax <= max) ? max : yMax;
                        yMin = (yMin >= min) ? min : yMin;
                    }
                }
                if (i == 0 && tempList.get(i).getMaxValue() == 0.0
                        && tempList.get(i).getMinValue() == 0.0) {
                    llFlag.setVisibility(View.GONE);
                }
                //x轴数据集
                String dateStr = tempList.get(i).getWeatherDate();
                xLableList.add(dateStr.substring(5, dateStr.length()));
                dateList.add(dateStr);
            }
            setLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void setLine() {
        // 高亮
        highLight = lineView.get_HighLight();
        highLight.setEnable(true);// 启用高亮显示  默认为启用状态，每条折线图想要获取点击回调，highlight需要启用
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                try {
                    return "日期:" + xLableList.get((int) value);
                } catch (Exception e) {
                    return "日期:" + value;
                }
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
        xAxis.set_unit("日期");
        xAxis.setUnitColor(Color.BLUE);
        xAxis.setAxisColor(Color.BLUE);
        // xAxis.setLabeList(xLableList);
        xAxis.setEnable(true);
        if (avgValues.size() > 0 && avgValues.size() < 8) {
            xAxis.set_labelCountAdvice(avgValues.size() + 2);
        } else {
            xAxis.set_labelCountAdvice(8);
        }
        xAxis.set_ValueAdapter(new DefaultValueAdapter(0));// 默认精度到小数点后2位

        YAxis yAxis = lineView.get_YAxis();
        yAxis.setAxisWidth(1);
        //  yAxis.set_unit(typeName+"/"+unitStr);
        yAxis.setAxisColor(Color.BLUE);
        yAxis.setUnitColor(Color.BLUE);
        yAxis.set_ValueAdapter(new DefaultValueAdapter(0));

        Lines lines = new Lines();

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

        if (minValues.size() > 0) {
            Line line = createLine(minValues, UIUtils.getColor(R.color.light_min));
            //line.setFilled(true);
            lines.addLine(line);
        }
        if (maxValues.size() > 0) {
            Line line = createLine(maxValues, UIUtils.getColor(R.color.light_max));
            // line.setFilled(true);
            lines.addLine(line);
        }
        if (avgValues.size() > 0) {
            Line line = createLine(avgValues, UIUtils.getColor(R.color.light_avg));
            lines.addLine(line);
        }
      //  Log.e("TAG--history",yMin+"---"+yMax);
        lineView.setLines(lines);
        lineView.animateXY();
    }

    private Line createLine(List<PointValue> values, int color) {

        final Line line = new Line();
        List<Entry> list = new ArrayList<>();
        try {
            int dayNum = DateUtil.differentDays(DateUtil.getDateFromStr(dateList.get(0)),
                    DateUtil.getDateFromStr(dateList.get(dateList.size() - 1))) + 1;

            Map<String, Integer> allDateList = new LinkedHashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < dayNum; i++) {
                String day = sdf.format(DateUtil.getDalculateEndDate(sdf.parse(dateList.get(0)), i));
                allDateList.put(day, i);
            }
            xLableList.clear();
            for (Map.Entry<String, Integer> entry : allDateList.entrySet()) {
                int value = entry.getValue();
                String key = entry.getKey();
                if (dateList.contains(key)) {
                    list.add(new Entry(value, values.get(dateList.indexOf(key)).getY()));
                } else {
                    list.add(value, new Entry(value, 0).setNull_Y());
                }
                xLableList.add(value, key.substring(5, key.length()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        lineView.get_XAxis().setLabeList(xLableList);


        line.setEntries(list);
        //设置线属性
        //line.setCircleR(10);
        line.setLineWidth(5);
        //line.setDrawCircle(false);
        line.setLineColor(UIUtils.getColor(R.color.light_avg));

        //设置X、Y轴最小、最大点
        line.setmYMin((int) yMin - 1);
       line.setmYMax((int) yMax + 3);
        line.setmXMax(avgValues.size() + 1);
        line.setLineColor(color);
        // line.setOnEntryClick(onEntryClick);

        return line;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_start:
                showSelectTime(tvSelectStart);
                break;

            case R.id.ll_end:
                showSelectTime(tvSelectEnd);
                break;
        }
    }

    private void reGetData(String startTime, String endTime) {
        int day = 0;
        try {
            Date startDate = DateUtil.getDateFromStr(startTime);
            Date endDate = DateUtil.getDateFromStr(endTime);
            Date currentDate = new Date();
            day = DateUtil.differentDays(startDate, endDate) + 1;
            if (DateUtil.differentDays(currentDate, endDate) > 0) {
                UIUtils.showToast("截止/起始时间不能超过当前时间！");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (day > 1) {
            getHistoryData(deviceId, startTime, endTime, funName);
        } else {
            UIUtils.showToast("时间间隔有误！");
        }
    }

    public void showSelectTime(final TextView tvDate) {
        //点击"日期"按钮布局 设置日期
        final Calendar calendar = Calendar.getInstance();
        //通过自定义控件AlertDialog实现
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.date_layout, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        //初始化时间
        calendar.setTimeInMillis(System.currentTimeMillis());
        //设置日期简略显示 否则详细显示 包括:星期\周
        datePicker.setCalendarViewShown(false);
        //初始化当前日期
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
        //设置date布局
        builder.setView(view);
        builder.setTitle("设置时间");
        builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //日期格式
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                tvDate.setText(sb);
                builder.create().cancel();
            }
        });
        builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().cancel();
            }
        });
        builder.create().show();
    }


    private void setAbnormal(String text) {
        llAbnormal.setVisibility(View.VISIBLE);
        tvAbnormal.setText(text);
        lineView.setVisibility(View.INVISIBLE);
        llFlag.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
