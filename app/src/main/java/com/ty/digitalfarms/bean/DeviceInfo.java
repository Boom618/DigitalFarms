package com.ty.digitalfarms.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class DeviceInfo implements Parcelable {


    private String message;
    private String tag;
    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Parcelable {


        private String addressIP;
        private String addressPort;
        private String companyName;
        private String companyNo;
        private String countyLevel;
        private int deviceID;
        private int deviceModelID;
        private String deviceModelName;
        private String deviceName;
        private String farmName;
        private String farmNo;
        private String latitude;
        private String longtitude;
        private String municipalLevel;
        private String plotName;
        private String plotNo;
        private String provincesLevel;
        private String serverPort;
        private int status;
        private String tag;
        private int typeCategory;
        private String userName;
        private String userPwd;
        private String deviceTypeName;

        @Override
        public String toString() {
            return "tag='" + tag + '\'' + ',';
        }

        public String getDeviceTypeName() {
            return deviceTypeName;
        }

        public void setDeviceTypeName(String deviceTypeName) {
            this.deviceTypeName = deviceTypeName;
        }

        public String getAddressIP() {
            return addressIP;
        }

        public void setAddressIP(String addressIP) {
            this.addressIP = addressIP;
        }

        public String getAddressPort() {
            return addressPort;
        }

        public void setAddressPort(String addressPort) {
            this.addressPort = addressPort;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyNo() {
            return companyNo;
        }

        public void setCompanyNo(String companyNo) {
            this.companyNo = companyNo;
        }

        public String getCountyLevel() {
            return countyLevel;
        }

        public void setCountyLevel(String countyLevel) {
            this.countyLevel = countyLevel;
        }

        public int getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(int deviceID) {
            this.deviceID = deviceID;
        }

        public int getDeviceModelID() {
            return deviceModelID;
        }

        public void setDeviceModelID(int deviceModelID) {
            this.deviceModelID = deviceModelID;
        }

        public String getDeviceModelName() {
            return deviceModelName;
        }

        public void setDeviceModelName(String deviceModelName) {
            this.deviceModelName = deviceModelName;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getFarmName() {
            return farmName;
        }

        public void setFarmName(String farmName) {
            this.farmName = farmName;
        }

        public String getFarmNo() {
            return farmNo;
        }

        public void setFarmNo(String farmNo) {
            this.farmNo = farmNo;
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

        public String getMunicipalLevel() {
            return municipalLevel;
        }

        public void setMunicipalLevel(String municipalLevel) {
            this.municipalLevel = municipalLevel;
        }

        public String getPlotName() {
            return plotName;
        }

        public void setPlotName(String plotName) {
            this.plotName = plotName;
        }

        public String getPlotNo() {
            return plotNo;
        }

        public void setPlotNo(String plotNo) {
            this.plotNo = plotNo;
        }

        public String getProvincesLevel() {
            return provincesLevel;
        }

        public void setProvincesLevel(String provincesLevel) {
            this.provincesLevel = provincesLevel;
        }

        public String getServerPort() {
            return serverPort;
        }

        public void setServerPort(String serverPort) {
            this.serverPort = serverPort;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getTypeCategory() {
            return typeCategory;
        }

        public void setTypeCategory(int typeCategory) {
            this.typeCategory = typeCategory;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPwd() {
            return userPwd;
        }

        public void setUserPwd(String userPwd) {
            this.userPwd = userPwd;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.addressIP);
            dest.writeString(this.addressPort);
            dest.writeString(this.companyName);
            dest.writeString(this.companyNo);
            dest.writeString(this.countyLevel);
            dest.writeInt(this.deviceID);
            dest.writeInt(this.deviceModelID);
            dest.writeString(this.deviceModelName);
            dest.writeString(this.deviceName);
            dest.writeString(this.farmName);
            dest.writeString(this.farmNo);
            dest.writeString(this.latitude);
            dest.writeString(this.longtitude);
            dest.writeString(this.municipalLevel);
            dest.writeString(this.plotName);
            dest.writeString(this.plotNo);
            dest.writeString(this.provincesLevel);
            dest.writeString(this.serverPort);
            dest.writeInt(this.status);
            dest.writeString(this.tag);
            dest.writeInt(this.typeCategory);
            dest.writeString(this.userName);
            dest.writeString(this.userPwd);
            dest.writeString(this.deviceTypeName);
        }

        public ResultBean() {
        }

        protected ResultBean(Parcel in) {
            this.addressIP = in.readString();
            this.addressPort = in.readString();
            this.companyName = in.readString();
            this.companyNo = in.readString();
            this.countyLevel = in.readString();
            this.deviceID = in.readInt();
            this.deviceModelID = in.readInt();
            this.deviceModelName = in.readString();
            this.deviceName = in.readString();
            this.farmName = in.readString();
            this.farmNo = in.readString();
            this.latitude = in.readString();
            this.longtitude = in.readString();
            this.municipalLevel = in.readString();
            this.plotName = in.readString();
            this.plotNo = in.readString();
            this.provincesLevel = in.readString();
            this.serverPort = in.readString();
            this.status = in.readInt();
            this.tag = in.readString();
            this.typeCategory = in.readInt();
            this.userName = in.readString();
            this.userPwd = in.readString();
            this.deviceTypeName = in.readString();
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel source) {
                return new ResultBean(source);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.tag);
        dest.writeList(this.result);
    }

    public DeviceInfo() {
    }

    protected DeviceInfo(Parcel in) {
        this.message = in.readString();
        this.tag = in.readString();
        this.result = new ArrayList<ResultBean>();
        in.readList(this.result, ResultBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<DeviceInfo> CREATOR = new Parcelable.Creator<DeviceInfo>() {
        @Override
        public DeviceInfo createFromParcel(Parcel source) {
            return new DeviceInfo(source);
        }

        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };
}
