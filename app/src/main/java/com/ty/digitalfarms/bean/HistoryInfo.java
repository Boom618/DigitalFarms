package com.ty.digitalfarms.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class HistoryInfo {

    private String tag;
    private String message;

    @SerializedName(value = "bean",alternate = {"data","result"})
    private List<ResultBean> bean;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getData() {
        return bean;
    }

    public void setData(List<ResultBean> data) {
        this.bean = data;
    }

    public static class ResultBean {
        /**
         * WeatherDate : 2017-08-01
         * AirAvg_T : 25.67
         * AirMax_T : 27.4
         * AirMin_T : 25
         */


        private String WeatherDate;
        @SerializedName(value = "avgValue",alternate = {"AirAvg_T","AirAvg_H","WindGrade","WindDirection",
                "SoilAvg_T","SoilAvg_H","Rainfall","CO2","LuxAvg"})
        private double avgValue;
        @SerializedName(value = "maxValue",alternate = {"AirMax_T","AirMax_H","SoilMax_T","SoilMax_H","max"})
        private double maxValue;
        @SerializedName(value = "minValue",alternate = {"AirMin_T","AirMin_H","SoilMin_T","SoilMin_H","min"})
        private double minValue;

        public String getWeatherDate() {
            return WeatherDate;
        }

        public void setWeatherDate(String WeatherDate) {
            this.WeatherDate = WeatherDate;
        }

        public double getAvgValue() {
            return avgValue;
        }

        public void setAvgValue(double avgValue) {
            this.avgValue = avgValue;
        }

        public double getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(double maxValue) {
            this.maxValue = maxValue;
        }

        public double getMinValue() {
            return minValue;
        }

        public void setMinValue(double minValue) {
            this.minValue = minValue;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "WeatherDate='" + WeatherDate + '\'' +
                    ", avgValue=" + avgValue +
                    ", maxValue=" + maxValue +
                    ", minValue=" + minValue +
                    '}';
        }
    }
}
