package com.ty.digitalfarms.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class WindDircInfo {

    private String tag;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * WeatherDate : 2017-08-31
         * WindDirection : 西
         */

        private String WeatherDate;
        private String WindDirection;

        public String getWeatherDate() {
            return WeatherDate;
        }

        public void setWeatherDate(String WeatherDate) {
            this.WeatherDate = WeatherDate;
        }

        public String getWindDirection() {
            return WindDirection;
        }

        public void setWindDirection(String WindDirection) {
            this.WindDirection = WindDirection;
        }
    }
}
