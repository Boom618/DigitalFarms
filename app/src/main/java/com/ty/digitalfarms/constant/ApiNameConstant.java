package com.ty.digitalfarms.constant;

/**
 * Created by Administrator on 2017/7/26.
 */

public class ApiNameConstant {

    /*-------------------------------河北和大三农------------------------------------------*/
    public final static String BASE_URL = "http://difarm.tcc.so/5991/";//河北和大三农api地址


    /*-------------------------------广西------------------------------------------*/
    // public final static String BASE_URL = "http://124.227.108.92:5999";//广西api地址
    public final static String LOGIN_URL = "http://124.227.108.92:3888/";//广西登录地址
    public final static String LOGIN4GX = "doConnServer";//广西登录接口

    /*-------------------------------森淼------------------------------------------*/
    //  public final static String BASE_URL = "http://senmiao.tcc.so/4060/";//森淼api地址


    /**
     * 登录
     */
      public final static String APP_LOGIN="app/base/appLogin";

    /**
     * app自动更新
     */
    public final static String UPGRADE_INFO = "getAppInfo";

    /**
     * 修改密码
     */
    public final static String MODIFY_PWD = "updatePassword";


    /**
     * 设备列表
     */
    public final static String DEVICES_LIST = "app/base/getDeviceList";

    /**
     * 九合一气象站实时数据
     */
    public final static String REAL_TIME = "app/realTime/getRealTimeData";

    /**
     * 获取某一天数据
     */
    public final static String DAY_INFO = "app/realTime/getTodayData";

    /**
     * 获取当天空气温度
     */
    public final static String DAY_TEMPERATURE = "global/history/getAirTemperatureHistory";

    /**
     * 获取当天空气湿度
     */
    public final static String DAY_HUMIDITY = "global/history/getAirHumidityHistory";

    /**
     * 获取当天土壤温度
     */
    public final static String DAY_SOIL_TEMPERATURE = "global/history/getSoilTemperatureHistory";

    /**
     * 获取当天土壤湿度
     */
    public final static String DAY_SOIL_HUMIDITY = "global/history/getSoilHumidityHistory";

    /**
     * 获取历史大气温度
     */
    public final static String HISTORY_TEMPERATURE = "global/history/getAirTemperatureHistory";

    /**
     * 获取历史大气湿度
     */
    public final static String HISTORY_HUMIDITY = "global/history/getAirHumidityHistory";

    /**
     * 获取历史土壤温度
     */
    public final static String HISTORY_SOIL_TEMPERATURE = "global/history/getSoilTemperatureHistory";

    /**
     * 获取历史土壤湿度
     */
    public final static String HISTORY_SOIL_HUMIDITY = "global/history/getSoilHumidityHistory";

    /**
     * 获取历史雨量
     */
    public final static String HISTORY_RAINFALL = "global/history/getRainfallHistory";

    /**
     * 获取历史二氧化碳
     */
    public final static String HISTORY_CARBONDIOXIDE = "global/history/getCarbonDioxideHistory";

    /**
     * 获取历史光照
     */
    public final static String HISTORY_LUX = "global/history/getLuxAvgHistory";

    /**
     * 获取历史风速
     */
    public final static String HISTORY_WINDGRADE = "global/history/getWindGradeHistory";

    /**
     * 获取历史风向
     */
    public final static String HISTORY_WINDDIRECTION = "global/history/getWindDirectionHistory";

}
