package com.ty.digitalfarms.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.WindDircInfo;
import com.ty.digitalfarms.constant.ApiNameConstant;
import com.ty.digitalfarms.constant.ApiParamConstant;
import com.ty.digitalfarms.ui.view.SimpleTextWatcher;
import com.ty.digitalfarms.util.DateUtil;
import com.ty.digitalfarms.util.UIUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rorbin.q.radarview.RadarData;
import rorbin.q.radarview.RadarView;

public class WindDircActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_select_start)
    TextView tvSelectStart;
    @BindView(R.id.ll_start)
    LinearLayout llStart;
    @BindView(R.id.tv_select_end)
    TextView tvSelectEnd;
    @BindView(R.id.ll_end)
    LinearLayout llEnd;
    @BindView(R.id.radar_view)
    RadarView mRadarView;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String deviceId;
    private String funName;
    private String typeName;
    private String deviceTag;
    private String unitStr;
    private RadarData radarData;

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_wind_dirc);
        StatusBarUtil.setColor(WindDircActivity.this,getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initData();
        getWindDircInfo(deviceId, DateUtil.getStateTime(-7), DateUtil.getCurrentDate(), funName);
        initView();
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
        tvName.setText(typeName + "趋势图 " + "天");
        llStart.setOnClickListener(this);
        llEnd.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        mRadarView.setWebMode(RadarView.WEB_MODE_POLYGON);//多边形
        mRadarView.setRotationEnable(false);//不旋转
        mRadarView.setRadarLineColor(0x3300bcd4);
        tvSelectStart.setText(DateUtil.getStateTime(-7));
        tvSelectEnd.setText(DateUtil.getCurrentDate());

        List<String> vertexText = new ArrayList<>();
        Collections.addAll(vertexText, "东南", "东", "东北", "北", "西北", "西", "西南", "南");
        mRadarView.setVertexText(vertexText);

        // mRadarView.setMaxValue(8f);//设置半径表示的值

        tvSelectStart.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                //判断是否选择了截止日期
                String endStr = tvSelectEnd.getText().toString().trim();
                String startStr = tvSelectStart.getText().toString().trim();
                if (!endStr.equals(UIUtils.getString(R.string.select_end_time))) {
                    // Log.e("TimeTAG",startStr+"---"+endStr);
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
                    // Log.e("TimeTAG",startStr+"---"+endStr);
                    reGetData(startStr, endStr);
                }
            }
        });

    }

    private void getWindDircInfo(String deviceId, String startTime, String endTime, String functionName) {
        final IProgressDialog dialog = UIUtils.getDialog(WindDircActivity.this);
        EasyHttp.get(functionName)
                .baseUrl(ApiNameConstant.BASE_URL)
                .params(ApiParamConstant.START_TIME, startTime)
                .params(ApiParamConstant.END_TIME, endTime)
                .params(ApiParamConstant.DEVICES_ID, deviceId)
                .execute(new ProgressDialogCallBack<String>(dialog, true, true) {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        Toast.makeText(UIUtils.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        Gson gson = new Gson();
                        try {
                            WindDircInfo historyData = gson.fromJson(response, WindDircInfo.class);
                            //  Log.e("TAG",historyData.toString());
                            if (historyData != null) {
                                List<WindDircInfo.DataBean> infos = historyData.getData();
                                updateLineView(infos);
                            } else {
                                // TODO: 2017/8/1 错误 显示无数据
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void updateLineView(List<WindDircInfo.DataBean> infos) {

        if (infos == null) {
            // TODO: 2017/8/16 没有数据
            return;
        } else if (infos.size() == 0) {
            // TODO: 2017/8/16 数据获取失败
            return;
        }
        float[] values = new float[8];
        List<Float> valueList = new ArrayList<>();
        for (WindDircInfo.DataBean bean : infos) {
            if ("东南".equals(bean.getWindDirection())) {
                values[0] += 1;
            } else if ("东".equals(bean.getWindDirection())) {
                values[1] += 1;
            } else if ("东北".equals(bean.getWindDirection())) {
                values[2] += 1;
            } else if ("北".equals(bean.getWindDirection())) {
                values[3] += 1;
            } else if ("西北".equals(bean.getWindDirection())) {
                values[4] += 1;
            } else if ("西".equals(bean.getWindDirection())) {
                values[5] += 1;
            } else if ("西南".equals(bean.getWindDirection())) {
                values[6] += 1;
            } else if ("南".equals(bean.getWindDirection())) {
                values[7] += 1;
            }
        }

        List<String> strings = new ArrayList<>();
        for (float f : values) {
            valueList.add(f);
            strings.add((int) f + "");
        }

        if (radarData==null){
            radarData = new RadarData(valueList);
            radarData.setValueText(strings);
            radarData.setValueTextEnable(true);
            radarData.setVauleTextColor(Color.RED);
        }else {
            radarData.setValue(valueList);
            radarData.setValueText(strings);
        }
        mRadarView.addData(radarData);
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

            case R.id.iv_left:
                finish();
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
            getWindDircInfo(deviceId, startTime, endTime, funName);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
