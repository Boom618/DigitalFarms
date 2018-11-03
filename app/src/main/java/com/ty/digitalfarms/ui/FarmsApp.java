package com.ty.digitalfarms.ui;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.tencent.bugly.crashreport.CrashReport;
import com.ty.digitalfarms.net.download.SystemParams;
import com.ty.digitalfarms.util.gson.DoubleDefault0Adapter;
import com.ty.digitalfarms.util.gson.IntegerDefault0Adapter;
import com.ty.digitalfarms.util.gson.LongDefault0Adapter;
import com.ty.digitalfarms.util.gson.StringDefault0Adapter;
import com.zhouyou.http.EasyHttp;

import retrofit2.GsonConverterFactory;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class FarmsApp extends Application {

    private static FarmsApp context;
    private static Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        SystemParams.init(this);
        //腾讯Bugly异常统计
        CrashReport.initCrashReport(getApplicationContext(), "44388d23e7", false);
        //初始化百度地图sdk
        SDKInitializer.initialize(this);
        EasyHttp.init(this);
        EasyHttp.getInstance()
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        //初始化海康sdk
        System.loadLibrary("gnustl_shared");
        MCRSDK.init();
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
        //初始化和风天气
       // HeConfig.init("HE1712261026291165", "5e6606944a4346a28546da0b87d8ca3e");
      //  HeConfig.switchToFreeServerNode();
    }

    public static Context getContext(){
        return context;
    }

    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setLenient()
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(String.class, new StringDefault0Adapter())
                    .create();
        }
        return gson;
    }
}
