package com.ty.digitalfarms.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ty.digitalfarms.constant.ApiNameConstant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/8/1.
 */

public class Utils {
    /**
     * 获取气象站元素Code
     *
     * @param position
     * @return
     */
    public static String getFunName(int position) {
        String funName;
        switch (position) {
            case 0:
                //温度
                funName = ApiNameConstant.HISTORY_TEMPERATURE;
                break;

            case 1:
                //湿度
                funName = ApiNameConstant.HISTORY_HUMIDITY;
                break;
            case 2:
                //风速
                funName = ApiNameConstant.HISTORY_WINDGRADE;
                break;
            case 3:
                //风向
                funName = ApiNameConstant.HISTORY_WINDDIRECTION;
                break;
            case 4:
                //土溫
                funName = ApiNameConstant.HISTORY_SOIL_TEMPERATURE;
                break;

            case 5:
                //土湿
                funName = ApiNameConstant.HISTORY_SOIL_HUMIDITY;
                break;
            case 6:
                //雨量
                funName = ApiNameConstant.HISTORY_RAINFALL;
                break;
            case 7:
                //二氧化碳
                funName = ApiNameConstant.HISTORY_CARBONDIOXIDE;
                break;

            case 8:
                //光照
                funName = ApiNameConstant.HISTORY_LUX;
                break;

            default:
                //（默认）
                funName = "";
                break;
        }
        return funName;
    }

    /**
     * 将实体类转换成json字符串对象
     *
     * @param obj 对象
     * @return map
     */
    public static String toJson(Object obj, int method) {
        // TODO Auto-generated method stub
        if (method == 1) {
            //字段是首字母小写，其余单词首字母大写
            Gson gson = new Gson();
            String obj2 = gson.toJson(obj);
            return obj2;
        } else if (method == 2) {
            // FieldNamingPolicy.LOWER_CASE_WITH_DASHES:全部转换为小写，并用空格或者下划线分隔
            //FieldNamingPolicy.UPPER_CAMEL_CASE    所以单词首字母大写
            Gson gson2 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            String obj2 = gson2.toJson(obj);
            return obj2;
        }
        return "";
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName() {
        PackageManager manager = UIUtils.getContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            String version = info.versionName;
            return "版本：" + version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本";
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {
        PackageManager manager = UIUtils.getContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getFilePathFromContentUri(Uri selectedVideoUri) {
        ContentResolver contentResolver = UIUtils.getContext().getContentResolver();
        String filePath = "";
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    public static String getMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取现在是夜里还是白天
     *
     * @return 0：白天，1：夜晚
     */
    public static boolean isDay() {
        int hour = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 18) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 判断是否到期
     *
     * @param currentDate 当前时间
     * @param outDate     到期时间
     * @return
     * @throws ParseException
     */
    public static boolean isOutDate(String currentDate, String outDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 当前的时刻
        Calendar pre = Calendar.getInstance();
        Date predate;
        if (TextUtils.isEmpty(currentDate)) {
            predate = new Date(System.currentTimeMillis());
        } else {
            predate = sdf.parse(currentDate);
        }
        pre.setTime(predate);


        // 设定的时刻
        Calendar cal = Calendar.getInstance();
        Date date = sdf.parse(outDate);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);
            int diffHour = cal.get(Calendar.HOUR_OF_DAY) - pre.get(Calendar.HOUR_OF_DAY);
            int diffMin = cal.get(Calendar.MINUTE) - pre.get(Calendar.MINUTE);
            if (diffDay == 0) {
                if (diffHour == 0) {
                    if (diffMin >= 0) {
                        return true;
                    }
                } else if (diffHour > 0) {
                    return true;
                }
            } else if (diffDay > 0) {
                return true;
            }
        } else if (cal.get(Calendar.YEAR) > (pre.get(Calendar.YEAR))) {
            return true;
        }
        return false;
    }

    /**
     * 获取登录设备mac地址
     *
     * @return
     */
    public static String getMacAddress() {
        WifiManager wm = (WifiManager) UIUtils.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String mac = wm.getConnectionInfo().getMacAddress();
        return mac == null ? "" : mac;
    }

    /**
     * 获取应用程序的缓存路径
     *
     * @return
     */
    public static String getSystemFilePath() {
        Context context = UIUtils.getContext();
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //返回/storage/emulated/0/Android/data/packagename/files/Pictures
            cachePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            //返回/storage/emulated/0/Android/data/packagename/cache
            //  cachePath = context.getExternalCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        } else {
            cachePath = context.getFilesDir().getAbsolutePath();
            // cachePath = context.getCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        }
        return cachePath;
    }

    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate;
        java.util.Date endDate;

        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }


}
