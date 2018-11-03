package com.ty.digitalfarms.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.AppInfo;
import com.ty.digitalfarms.bean.DeviceInfo;
import com.ty.digitalfarms.constant.ConstantUtil;
import com.ty.digitalfarms.net.HttpMethods;
import com.ty.digitalfarms.net.ProgressSubscriber;
import com.ty.digitalfarms.ui.fragment.CameraFragment;
import com.ty.digitalfarms.ui.fragment.GridFragment;
import com.ty.digitalfarms.util.UIUtils;
import com.ty.digitalfarms.util.Utils;
import com.wevey.selector.dialog.NormalSelectionDialog;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;


public class GridActivity extends BaseActivity implements View.OnClickListener {


    private static final int REQUEST_PERMISSION = 1;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rb_camera)
    RadioButton rbCamera;
    @BindView(R.id.rb_weather)
    RadioButton rbWeather;
    @BindView(R.id.rg)
    RadioGroup rGroup;
    @BindView(R.id.rl__cancel)
    RelativeLayout rlCancel;
    @BindView(R.id.nav_sliding)
    NavigationView navSliding;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tv_person_name)
    TextView tvPersonName;
    @BindView(R.id.ll_abnormal)
    LinearLayout llAbnormal;
    @BindView(R.id.iv_map)
    ImageView ivMap;
    @BindView(R.id.rl_about_me)
    RelativeLayout rlAboutMe;
    @BindView(R.id.rl_album)
    RelativeLayout rlAlbum;

    private String companyNo;
    private final static int CAMERA = 0;
    private final static int WEATHER_STATION = 1;
    private String userName;
    private List<DeviceInfo.ResultBean> weatherStationList;


    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_grid);
        StatusBarUtil.setColor(GridActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initRequestPermissions();
        initView();
        getAppInfo();
        getDevicesList();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        SharedPreferences sp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        companyNo = sp.getString(ConstantUtil.SP_COMPANY_NO, "");
        userName = sp.getString(ConstantUtil.SP_USER_NAME, "");
        ivMenu.setOnClickListener(this);
        rlCancel.setOnClickListener(this);
        ivMap.setVisibility(View.VISIBLE);
        ivMap.setOnClickListener(this);
        rlAboutMe.setOnClickListener(this);
        rlAlbum.setOnClickListener(this);
        tvPersonName.setText(userName);
    }

    private void getDevicesList() {
        HttpMethods.getInstance().getDevices(new ProgressSubscriber<DeviceInfo>(this, false) {
            @Override
            public void onNext(DeviceInfo deviceInfo) {
                List<DeviceInfo.ResultBean> data = deviceInfo.getResult();
                if (data == null) {
                    UIUtils.showToast("获取设备列表失败！");
                    return;
                } else if (data.size() == 0) {
                    llAbnormal.setVisibility(View.VISIBLE);
                    return;
                }
                //刪除录像机设备和摄像头设备
                Iterator<DeviceInfo.ResultBean> it = data.iterator();
                while (it.hasNext()) {
                    DeviceInfo.ResultBean next = it.next();
                    if (next.getTypeCategory() == 3 || next.getTypeCategory() == 1) {
                        it.remove();
                    }
                }
                initData(data);
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                UIUtils.showToast(e.getMessage());
            }

        }, "1", companyNo);
    }

    private void initData(List<DeviceInfo.ResultBean> deviceInfos) {
        //初始化Fragment的信息
        weatherStationList = new ArrayList<>();
        weatherStationList.addAll(deviceInfos);
        CameraFragment cameraFg = new CameraFragment();
        GridFragment weatherStationFg = initFragment(deviceInfos);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(cameraFg);
        fragmentList.add(weatherStationFg);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PagerChangeListener());
        rGroup.setOnCheckedChangeListener(new RadioGroupListener());
        rGroup.check(R.id.rb_camera);
        viewPager.setCurrentItem(CAMERA);

    }

    private GridFragment initFragment(List<DeviceInfo.ResultBean> deviceList) {
        GridFragment fragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("deviceInfos", (ArrayList<? extends Parcelable>) deviceList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;

            case R.id.iv_map:
                Intent intent2 = new Intent(GridActivity.this, MapActivity.class);
                intent2.putParcelableArrayListExtra("weatherStationList", (ArrayList<? extends Parcelable>) weatherStationList);
                startActivity(intent2);
                break;

            case R.id.rl__cancel:
                cancel();
                break;

            case R.id.rl_about_me:
                startActivity(new Intent(GridActivity.this,AboutMeActivity.class));
                break;

            case R.id.rl_album:
                startActivity(new Intent(GridActivity.this,AlbumActivity.class));
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);//禁止销毁fragment
        }
    }

    class PagerChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case CAMERA:
                    rGroup.check(R.id.rb_camera);
                    break;
                case WEATHER_STATION:
                    rGroup.check(R.id.rb_weather);
                    break;
            }
        }

    }

    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_camera:
                    viewPager.setCurrentItem(CAMERA);
                    break;
                case R.id.rb_weather:
                    viewPager.setCurrentItem(WEATHER_STATION);
                    break;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false; // 事件继续向下传播
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime;

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);// 退出程序
        }
    }

    private void cancel() {
        List<String> list = new ArrayList<>();
        list.add("确认");
        new NormalSelectionDialog.Builder(this).setlTitleVisible(true)   //设置是否显示标题
                .setTitleHeight(50)   //设置标题高度
                .setTitleText("确定要退出登录？")  //设置标题提示文本
                .setTitleTextSize(14) //设置标题字体大小 sp
                .setTitleTextColor(R.color.colorPrimary) //设置标题文本颜色
                .setItemHeight(40)  //设置item的高度
                .setItemWidth(0.9f)  //屏幕宽度*0.9
                .setItemTextColor(R.color.colorPrimaryDark)  //设置item字体颜色
                .setItemTextSize(14)  //设置item字体大小
                .setCancleButtonText("取消")  //设置最底部“取消”按钮文本
                .setOnItemListener(new com.wevey.selector.dialog.DialogInterface.
                        OnItemClickListener<NormalSelectionDialog>() {

                    @Override
                    public void onItemClick(NormalSelectionDialog dialog, View button, int
                            position) {
                        if (position == 0) {
                            //清楚缓存信息
                            getSharedPreferences(ConstantUtil.USER_SP_NAME, Context.MODE_PRIVATE)
                                    .edit().clear().apply();

                            Intent intent = new Intent(GridActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        dialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)  //设置是否可点击其他地方取消dialog
                .build()
                .setDatas(list)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // initLocation();
                    return;
                } else {
                    Toast.makeText(GridActivity.this, "你拒绝了定位权限，所以无法获取天气信息！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void initRequestPermissions() {
        // 应用已启动加载就开始申请权限
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(GridActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GridActivity.this, permissions, REQUEST_PERMISSION);
        }else if (ContextCompat.checkSelfPermission(GridActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(GridActivity.this, permissions, REQUEST_PERMISSION);
        }
    }

    private void getAppInfo() {
        HttpMethods.getInstance().getAppInfo(new Subscriber<AppInfo>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(AppInfo appInfo) {
                int versionCode = Utils.getVersionCode();
                if (appInfo.getCode() == 0) {
                    //  Log.e("VersionCode:", appInfo.getAppinfos().getVersionCode() + "");
                    if (versionCode < appInfo.getAppinfos().getVersionCode()) {
                        showUpgradeDialog(appInfo.getAppinfos().getVersionAddress());
                    }
                }
            }
        }, "000003");//“000006”代表河北数字农场
    }

    private void showUpgradeDialog(final String downloadUrl) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //没有网络
        if (!wifi.isConnected() && !mobile.isConnected()) {
            UIUtils.showToast("请检查网络是否连接");
            return;
        }
        //Wifi可用
        if (wifi.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("发现新版本，是否立即更新");
            builder.setNegativeButton("忽略", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UIUtils.download(GridActivity.this, downloadUrl, "数字农场");
                }
            });
            builder.show();
        }
        //数据流量可用
        if ((!wifi.isConnected()) && mobile.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("发现新版本。\n检测到正在使用数据流量，是否更新？");
            builder.setNegativeButton("忽略", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UIUtils.download(GridActivity.this, downloadUrl, "数字农场");
                }
            });
            builder.show();
        }
    }

}
