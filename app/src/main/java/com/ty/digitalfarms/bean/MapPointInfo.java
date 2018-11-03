package com.ty.digitalfarms.bean;

/**
 * Created by Administrator on 2017/10/25.
 */

public class MapPointInfo {

    public MapPointInfo(String devicesName, String latitude, String longtitude) {
        this.devicesName = devicesName;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    private String devicesName;
    private String latitude;
    private String longtitude;

    public String getDevicesName() {
        return devicesName;
    }

    public void setDevicesName(String devicesName) {
        this.devicesName = devicesName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}
