package com.ty.digitalfarms.constant;


import android.os.Environment;

import java.io.File;

/**
 * desc:
 * author: XingZheng
 * date:2017/1/6
 */

public class ConstantUtil {


    public static String PhotoDir = Environment.getExternalStorageDirectory()+ File.separator+"diFarm/";

    public static String ABOUT_ME_URL="http://www.zqdsnjt.com/guanyuwomen";

    /**
     * 用戶信息SharedPreferences文件名
     */
    public static  final String USER_SP_NAME="userInfo";

    /**
     * 摄像头设备控制中心SharedPreferences文件名
     */
    public static  final String SERVICE_SP_NAME="serviceInfo";

    /**
     * 控制中心服务器地址
     */
    public static final String SP_SERVICE_ADDRESS ="serverAddress";

    /**
     * 控制中心服务器用户名
     */
    public static final String SP_SERVICE_NAME ="userName";

    /**
     * 控制中心服务器密码
     */
    public static final String SP_SERVICE_PASSWORD ="password";

    /**
     *SP组织代码的Key
     */
    public static final String SP_COMPANY_NO ="companyNo";

    /**
     * 用户名
     */
    public static final String SP_USER_NAME="userName";

    /**
     *SP用户代码的Key
     */
    public static final String SP_USERNO="userNo";

    /**
     *SP登录状态的Key
     */
    public static final String SP_LOGIN_STATUS="isLogin";

}
