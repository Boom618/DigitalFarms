package com.ty.digitalfarms.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */

public class DayInfo {


    /**
     * tag : success
     * message :
     * data : {"carbonDioxide":[{"hour":0,"fieldname":"二氧化碳","value":515},{"hour":1,"fieldname":"二氧化碳","value":574},{"hour":2,"fieldname":"二氧化碳","value":541},{"hour":3,"fieldname":"二氧化碳","value":592},{"hour":4,"fieldname":"二氧化碳","value":538},{"hour":5,"fieldname":"二氧化碳","value":545},{"hour":6,"fieldname":"二氧化碳","value":529},{"hour":7,"fieldname":"二氧化碳","value":510},{"hour":8,"fieldname":"二氧化碳","value":502},{"hour":9,"fieldname":"二氧化碳","value":481}],"windSpeed":[{"hour":0,"fieldname":"风速","value":12},{"hour":1,"fieldname":"风速","value":12},{"hour":2,"fieldname":"风速","value":12},{"hour":3,"fieldname":"风速","value":12},{"hour":4,"fieldname":"风速","value":12},{"hour":5,"fieldname":"风速","value":12},{"hour":6,"fieldname":"风速","value":12},{"hour":7,"fieldname":"风速","value":12},{"hour":8,"fieldname":"风速","value":12},{"hour":9,"fieldname":"风速","value":12}],"windDirection":[{"hour":0,"fieldname":"风向","value":"西北"},{"hour":1,"fieldname":"风向","value":"西北"},{"hour":2,"fieldname":"风向","value":"南"},{"hour":3,"fieldname":"风向","value":"北"},{"hour":4,"fieldname":"风向","value":"西北"},{"hour":5,"fieldname":"风向","value":"北"},{"hour":6,"fieldname":"风向","value":"西北"},{"hour":7,"fieldname":"风向","value":"北"},{"hour":8,"fieldname":"风向","value":"北"},{"hour":9,"fieldname":"风向","value":"北"}],"sunshine":[{"hour":0,"fieldname":"光照","value":0},{"hour":1,"fieldname":"光照","value":0},{"hour":2,"fieldname":"光照","value":0},{"hour":3,"fieldname":"光照","value":0},{"hour":4,"fieldname":"光照","value":0},{"hour":5,"fieldname":"光照","value":60},{"hour":6,"fieldname":"光照","value":3420},{"hour":7,"fieldname":"光照","value":11030},{"hour":8,"fieldname":"光照","value":24240},{"hour":9,"fieldname":"光照","value":97000}],"rainfall":[{"hour":0,"fieldname":"雨量","value":0},{"hour":1,"fieldname":"雨量","value":0},{"hour":2,"fieldname":"雨量","value":0},{"hour":3,"fieldname":"雨量","value":0},{"hour":4,"fieldname":"雨量","value":0},{"hour":5,"fieldname":"雨量","value":0},{"hour":6,"fieldname":"雨量","value":0},{"hour":7,"fieldname":"雨量","value":0},{"hour":8,"fieldname":"雨量","value":0},{"hour":9,"fieldname":"雨量","value":0}],"airHumidity":[{"hour":0,"fieldname":"大气湿度","value":99.9},{"hour":1,"fieldname":"大气湿度","value":99.9},{"hour":2,"fieldname":"大气湿度","value":99.9},{"hour":3,"fieldname":"大气湿度","value":99.9},{"hour":4,"fieldname":"大气湿度","value":99.9},{"hour":5,"fieldname":"大气湿度","value":99.9},{"hour":6,"fieldname":"大气湿度","value":99.9},{"hour":7,"fieldname":"大气湿度","value":99.9},{"hour":8,"fieldname":"大气湿度","value":99.9},{"hour":9,"fieldname":"大气湿度","value":96.9}],"airTemperature":[{"hour":0,"fieldname":"大气温度","value":15.7},{"hour":1,"fieldname":"大气温度","value":14.8},{"hour":2,"fieldname":"大气温度","value":15.5},{"hour":3,"fieldname":"大气温度","value":14},{"hour":4,"fieldname":"大气温度","value":14.3},{"hour":5,"fieldname":"大气温度","value":14.2},{"hour":6,"fieldname":"大气温度","value":14.9},{"hour":7,"fieldname":"大气温度","value":15.4},{"hour":8,"fieldname":"大气温度","value":16.6},{"hour":9,"fieldname":"大气温度","value":19.3}],"soilHumidity":[{"hour":0,"fieldname":"土壤湿度","value":21.4},{"hour":1,"fieldname":"土壤湿度","value":21.4},{"hour":2,"fieldname":"土壤湿度","value":21.5},{"hour":3,"fieldname":"土壤湿度","value":21.4},{"hour":4,"fieldname":"土壤湿度","value":21.4},{"hour":5,"fieldname":"土壤湿度","value":21.4},{"hour":6,"fieldname":"土壤湿度","value":21.4},{"hour":7,"fieldname":"土壤湿度","value":21.4},{"hour":8,"fieldname":"土壤湿度","value":21.4},{"hour":9,"fieldname":"土壤湿度","value":21.3}],"soilTemperature":[{"hour":0,"fieldname":"土壤温度","value":20.1},{"hour":1,"fieldname":"土壤温度","value":20.1},{"hour":2,"fieldname":"土壤温度","value":20.1},{"hour":3,"fieldname":"土壤温度","value":19.9},{"hour":4,"fieldname":"土壤温度","value":19.9},{"hour":5,"fieldname":"土壤温度","value":19.9},{"hour":6,"fieldname":"土壤温度","value":19.7},{"hour":7,"fieldname":"土壤温度","value":19.6},{"hour":8,"fieldname":"土壤温度","value":19.6},{"hour":9,"fieldname":"土壤温度","value":19.4}]}
     */

    private String tag;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<CarbonDioxideBean> carbonDioxide;
        private List<WindSpeedBean> windSpeed;
        private List<WindDirectionBean> windDirection;
        private List<SunshineBean> sunshine;
        private List<RainfallBean> rainfall;
        private List<AirHumidityBean> airHumidity;
        private List<AirTemperatureBean> airTemperature;
        private List<SoilHumidityBean> soilHumidity;
        private List<SoilTemperatureBean> soilTemperature;

        public List<CarbonDioxideBean> getCarbonDioxide() {
            return carbonDioxide;
        }

        public void setCarbonDioxide(List<CarbonDioxideBean> carbonDioxide) {
            this.carbonDioxide = carbonDioxide;
        }

        public List<WindSpeedBean> getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(List<WindSpeedBean> windSpeed) {
            this.windSpeed = windSpeed;
        }

        public List<WindDirectionBean> getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(List<WindDirectionBean> windDirection) {
            this.windDirection = windDirection;
        }

        public List<SunshineBean> getSunshine() {
            return sunshine;
        }

        public void setSunshine(List<SunshineBean> sunshine) {
            this.sunshine = sunshine;
        }

        public List<RainfallBean> getRainfall() {
            return rainfall;
        }

        public void setRainfall(List<RainfallBean> rainfall) {
            this.rainfall = rainfall;
        }

        public List<AirHumidityBean> getAirHumidity() {
            return airHumidity;
        }

        public void setAirHumidity(List<AirHumidityBean> airHumidity) {
            this.airHumidity = airHumidity;
        }

        public List<AirTemperatureBean> getAirTemperature() {
            return airTemperature;
        }

        public void setAirTemperature(List<AirTemperatureBean> airTemperature) {
            this.airTemperature = airTemperature;
        }

        public List<SoilHumidityBean> getSoilHumidity() {
            return soilHumidity;
        }

        public void setSoilHumidity(List<SoilHumidityBean> soilHumidity) {
            this.soilHumidity = soilHumidity;
        }

        public List<SoilTemperatureBean> getSoilTemperature() {
            return soilTemperature;
        }

        public void setSoilTemperature(List<SoilTemperatureBean> soilTemperature) {
            this.soilTemperature = soilTemperature;
        }

        public static class CarbonDioxideBean {
            /**
             * hour : 0
             * fieldname : 二氧化碳
             * value : 515
             */

            private int hour;
            private String fieldname;
            private int value;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class WindSpeedBean {
            /**
             * hour : 0
             * fieldname : 风速
             * value : 12
             */

            private int hour;
            private String fieldname;
            private int value;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class WindDirectionBean {
            /**
             * hour : 0
             * fieldname : 风向
             * value : 西北
             */

            private int hour;
            private String fieldname;
            private String value;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class SunshineBean {
            /**
             * hour : 0
             * fieldname : 光照
             * value : 0
             */

            private int hour;
            private String fieldname;
            private int value;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class RainfallBean {
            /**
             * hour : 0
             * fieldname : 雨量
             * value : 0
             */

            private int hour;
            private String fieldname;
            private int value;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class AirHumidityBean {
            /**
             * hour : 0
             * fieldname : 大气湿度
             * value : 99.9
             */

            private int hour;
            private String fieldname;
            private double value;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }
        }

        public static class AirTemperatureBean {
            /**
             * hour : 0
             * fieldname : 大气温度
             * value : 15.7
             */

            private int hour;
            private String fieldname;
            private double value;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }
        }

        public static class SoilHumidityBean {
            /**
             * hour : 0
             * fieldname : 土壤湿度
             * value : 21.4
             */

            private int hour;
            private String fieldname;
            private double value;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }
        }

        public static class SoilTemperatureBean {
            /**
             * hour : 0
             * fieldname : 土壤温度
             * value : 20.1
             */

            private int hour;
            private String fieldname;
            private double value;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }
        }
    }
}
