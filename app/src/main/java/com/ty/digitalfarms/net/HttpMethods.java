package com.ty.digitalfarms.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ty.digitalfarms.bean.AppInfo;
import com.ty.digitalfarms.bean.CurrentData;
import com.ty.digitalfarms.bean.DayInfo;
import com.ty.digitalfarms.bean.DeviceInfo;
import com.ty.digitalfarms.bean.GXLoginInfo;
import com.ty.digitalfarms.bean.LoginInfo;
import com.ty.digitalfarms.bean.SubmitInfo;
import com.ty.digitalfarms.bean.WeatherInfo;
import com.ty.digitalfarms.constant.ApiNameConstant;
import com.ty.digitalfarms.util.gson.DoubleDefault0Adapter;
import com.ty.digitalfarms.util.gson.IntegerDefault0Adapter;
import com.ty.digitalfarms.util.gson.LongDefault0Adapter;
import com.ty.digitalfarms.util.gson.StringDefault0Adapter;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * description:
 * author: XingZheng
 * date: 2016/11/22.
 */
public class HttpMethods {

    public static final int DEFAULT_TIMEOUT = 20;//默认超时时间
    private  Retrofit mRetrofit;
    private final BaseApiService mService;
    private static Gson gson;
    private String baseUrl= ApiNameConstant.BASE_URL;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private HttpMethods() {
        //创建OKHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

        mService = mRetrofit.create(BaseApiService.class);

    }

    public HttpMethods(String url) {
        //创建OKHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();

        mService = mRetrofit.create(BaseApiService.class);

    }

    //构建单例
    public static class SingletonHolder {
        public static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 登录(河北)
     * @param subscriber
     * @param userNo
     * @param password
     */
    public void login(ProgressSubscriber<LoginInfo> subscriber,String userNo,String password){

        mService.login(userNo,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 登录（广西）
     * @param subscriber
     * @param userNo
     * @param password
     */
    public void login4GX(ProgressSubscriber<GXLoginInfo> subscriber, String userNo, String password){

        mService.login4GX(userNo,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取设备信息
     * @param subscriber
     * @param status
     */
    public void getDevices(ProgressSubscriber<DeviceInfo> subscriber, String status,String companyNo) {
        mService.getDevicesInfo(status,companyNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取实时数据
     * @param subscriber
     * @param devicesId
     */
    public void getRealTimeInfo(ProgressSubscriber<CurrentData> subscriber, String devicesId) {
        mService.getRealTimeInfo(devicesId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDayInfo(ProgressSubscriber<DayInfo> subscriber, String devicesId) {
        mService.getDayInfo(devicesId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取天气信息
     * @param subscriber
     * @param lng
     * @param key
     */
    public void getWeatherInfo(ProgressSubscriber<WeatherInfo> subscriber, String lng,String key) {
        mService.getWeather(lng,key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取App的更新消息
     * @param subscriber
     */
    public void getAppInfo(Subscriber<AppInfo> subscriber,String appNo){
        mService.getAppInfo(appNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改密码
     * @param subscriber
     * @param json
     */
    public void modifyPwd(ProgressSubscriber<SubmitInfo> subscriber, String json) {
        mService.modifyPwd(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
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
