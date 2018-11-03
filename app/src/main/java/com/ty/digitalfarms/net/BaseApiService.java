package com.ty.digitalfarms.net;

import com.ty.digitalfarms.bean.AppInfo;
import com.ty.digitalfarms.bean.CurrentData;
import com.ty.digitalfarms.bean.DayInfo;
import com.ty.digitalfarms.bean.DeviceInfo;
import com.ty.digitalfarms.bean.GXLoginInfo;
import com.ty.digitalfarms.bean.LoginInfo;
import com.ty.digitalfarms.bean.SubmitInfo;
import com.ty.digitalfarms.bean.WeatherInfo;
import com.ty.digitalfarms.constant.ApiNameConstant;
import com.ty.digitalfarms.constant.ApiParamConstant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface BaseApiService {

    /**
     * 登录（河北）
     * @param userPhone
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.APP_LOGIN)
    Observable<LoginInfo> login(@Field("userPhone") String userPhone,
                                @Field("credential") String password);


    /**
     * 登录（广西）
     * @param userPhone
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.LOGIN4GX)
    Observable<GXLoginInfo> login4GX(@Field("userPhone") String userPhone,
                                     @Field("password") String password);

    /**
     * 修改密码
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.MODIFY_PWD)
    Observable<SubmitInfo> modifyPwd(@Field("userinfo") String json);

    /**
     * 获取设备列表
     * @param status
     * @return
     */
    @GET(ApiNameConstant.DEVICES_LIST)
    Observable<DeviceInfo> getDevicesInfo(@Query(ApiParamConstant.STATUS) String status,
                                          @Query(ApiParamConstant.COMPANY_NO) String companyNo);

    /**
     * 获取实时数据
     * @param deviceId
     * @return
     */
    @GET(ApiNameConstant.REAL_TIME)
    Observable<CurrentData> getRealTimeInfo(@Query(ApiParamConstant.DEVICES_ID) String deviceId
    );

    @GET("v5/now")
    Observable<WeatherInfo> getWeather(@Query("city") String lng,@Query("key") String key);

    /**
     * 获取历史数据
     * @param deviceId
     * @param startTime
     * @param endTime
     * @return
     */
    @GET()
    Observable<CurrentData> getHistoyInfo(@Query(ApiParamConstant.DEVICES_ID) String deviceId,
                                          @Query(ApiParamConstant.START_TIME) String startTime,
                                          @Query(ApiParamConstant.END_TIME) String endTime);

    @GET(ApiNameConstant.DAY_INFO)
    Observable<DayInfo> getDayInfo(@Query(ApiParamConstant.DEVICES_ID) String deviceId);

    /**
     * 获取App更新消息
     * @return
     */
    @GET(ApiNameConstant.UPGRADE_INFO)
    Observable<AppInfo> getAppInfo(@Query("appNo") String appNo);

}
