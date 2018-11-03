package com.ty.digitalfarms.constant;

/**
 * Created by Administrator on 2018/4/28.
 * 动态ip云台控制命令
 *
 * 1：云台转上 (使摄像头向上转动）  2：云台转下 (使摄像头向下转动）
 3：云台转左 (使摄像头向左转动）  4：云台转右 (使摄像头向右转动）
 5：图像变亮 (提高图像亮度）      6：图像变暗 (降低图像亮度）
 7：镜头拉近 (放大图像画面）      8：镜头拉远 (缩小图像画面）
 9：镜头近焦 (摄像头对焦，使能看清近距离物体）
 10：镜头远焦 (摄像头对焦，使能看清远距离物体）
 11：云台左上 (使摄像头向左上方转动）
 12：云台右上 (使摄像头向右上方转动）
 13：云台左下 (使摄像头向左下方转动）
 14：云台右下 (使摄像头向右下方转动）
 15：云镜停止 (关闭云台控制的权限）
 16：自动扫描 (使摄像头以某一特定速度连续在一水平上转动，查看个角度）
 17：预置点设置 (存储摄像头的角度位置信息）
 18：预置点调用 (使摄像头直接转动到存储好的角度位置）
 20：雨刷关 (如果有雨刷，则关闭雨刷）
 21：雨刷开 (如果有雨刷，则打开雨刷）
 22：灯光开 (打开摄像头的灯光）
 23：灯光关 (关闭摄像头的灯光）
 99：3D放大 (客户端软件上，进行手势放大缩小画面操作时使用，需提供手势操作前后的坐标值）
 */

public class HikPTZCommand {

    /**
     * 1：云台转上 (使摄像头向上转动）
     */
    public static final int TILT_UP = 1;
    /**
     * 2：云台转下 (使摄像头向下转动）
     */
    public static final int TILT_DOWN = 2;
    /**
     * 3：云台转左 (使摄像头向左转动）
     */
    public static final int PAN_LEFT = 3;
    /**
     * 4：云台转右 (使摄像头向右转动）
     */
    public static final int PAN_RIGHT = 4;
    /**
     * 7：镜头拉近 (放大图像画面）
     */
    public static final int ZOOM_IN = 7;
    /**
     *  8：镜头拉远 (缩小图像画面）
     */
    public static final int ZOOM_OUT = 8;
    /**
     * 11：云台左上 (使摄像头向左上方转动）
     */
    public static final int UP_LEFT = 11;
    /**
     * 12：云台右上 (使摄像头向右上方转动）
     */
    public static final int UP_RIGHT = 12;
    /**
     * 13：云台左下 (使摄像头向左下方转动）
     */
    public static final int DOWN_LEFT = 13;
    /**
     * 14：云台右下 (使摄像头向右下方转动）
     */
    public static final int DOWN_RIGHT =14;
    /**
     * 16：自动扫描 (使摄像头以某一特定速度连续在一水平上转动，查看个角度）
     */
    public static final int PAN_AUTO = 16;


    public static final int LIGHT_PWRON = 2;
    public static final int WIPER_PWRON = 3;
    public static final int FAN_PWRON = 4;
    public static final int HEATER_PWRON = 5;
    public static final int AUX_PWRON1 = 6;
    public static final int AUX_PWRON2 = 7;
    public static final int FOCUS_NEAR = 13;
    public static final int FOCUS_FAR = 14;
    public static final int IRIS_OPEN = 15;
    public static final int IRIS_CLOSE = 16;


    public static final int RUN_CRUISE = 36;
    public static final int RUN_SEQ = 37;
    public static final int STOP_SEQ = 38;
    public static final int GOTO_PRESET = 39;
}
