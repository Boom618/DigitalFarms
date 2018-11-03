package com.ty.digitalfarms.util;

/**
 * Created by Administrator on 2017/8/29.
 */

public class WindUtil {

    public static String getWindLevel(String value) {
        String name;
        try {
            float v = Float.parseFloat(value);
            if (0 <= v && v < 0.3) {
                name = "无风";
            } else if (0.3 <= v && v < 1.6) {
                name = "软风";
            } else if (1.6 <= v && v < 3.4) {
                name = "轻风";
            } else if (3.4 <= v && v < 5.5) {
                name = "微风";
            } else if (5.5 <= v && v < 8) {
                name = "和风";
            } else if (8 <= v && v < 10.8) {
                name = "清风";
            } else if (10.8 <= v && v < 13.9) {
                name = "强风";
            } else if (13.9 <= v && v < 17.2) {
                name = "劲风";
            } else if (17.2 <= v && v < 20.8) {
                name = "大风";
            } else if (20.8 <= v && v < 24.5) {
                name = "烈风";
            } else if (24.5 <= v && v < 28.5) {
                name = "狂风";
            } else if (28.5 <= v && v < 32.6) {
                name = "暴风";
            } else if (32.6 <= v && v < 37) {
                name = "台风";
            } else {
                name = "未知";
            }
        } catch (Exception e) {
            e.printStackTrace();
            name = "未知";
        }
        return name;
    }

    public static String getWindDirection(String value) {
        String windDirecStr;
        try {
            float v = Float.parseFloat(value);
            if (22.5 < v && v <= 67.5) {
                windDirecStr = "东北";
            } else if (67.5 < v && v <= 112.5) {
                windDirecStr = "东";
            } else if (112.5 < v && v <= 157.5) {
                windDirecStr = "东南";
            } else if (157.5 < v && v <= 202.5) {
                windDirecStr = "南";
            } else if (202.5 < v && v <= 247.5) {
                windDirecStr = "西南";
            } else if (247.5 < v && v <= 292.5) {
                windDirecStr = "西";
            } else if (292.5 < v && v <= 337.5) {
                windDirecStr = "西北";
            } else if (337.5 < v || v <= 22.5) {
                windDirecStr = "北";
            }else {
                windDirecStr="无风";
            }
        }catch (Exception e){
            e.printStackTrace();
            windDirecStr="无风";
        }
        return windDirecStr;
    }

}
