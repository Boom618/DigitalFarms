package com.ty.digitalfarms.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public class CurrentData {

    /**
     * tag : success
     * message :
     * result : [{"Hour":0,"Maxtime":"2017-09-06T16:01:00.000Z","Temperature":25,"Humidity":56.9,"SoilTemperature":24.1,"SoilHumidity":55.5,"WindSpeed":37.7,"Rainfall":641.4,"Carbondioxide":1236,"WindDirection":286,"Illumination":161550}]
     */

    private String tag;
    private String message;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * Hour : 0
         * Maxtime : 2017-09-06T16:01:00.000Z
         * Temperature : 25
         * Humidity : 56.9
         * SoilTemperature : 24.1
         * SoilHumidity : 55.5
         * WindSpeed : 37.7
         * Rainfall : 641.4
         * Carbondioxide : 1236
         * WindDirection : 286
         * Illumination : 161550
         */

        private int Hour;
        private String Maxtime;
        private int Temperature;
        private double Humidity;
        private double SoilTemperature;
        private double SoilHumidity;
        private double WindSpeed;
        private double Rainfall;
        private int Carbondioxide;
        private int WindDirection;
        private int Illumination;

        public int getHour() {
            return Hour;
        }

        public void setHour(int Hour) {
            this.Hour = Hour;
        }

        public String getMaxtime() {
            return Maxtime;
        }

        public void setMaxtime(String Maxtime) {
            this.Maxtime = Maxtime;
        }

        public int getTemperature() {
            return Temperature;
        }

        public void setTemperature(int Temperature) {
            this.Temperature = Temperature;
        }

        public double getHumidity() {
            return Humidity;
        }

        public void setHumidity(double Humidity) {
            this.Humidity = Humidity;
        }

        public double getSoilTemperature() {
            return SoilTemperature;
        }

        public void setSoilTemperature(double SoilTemperature) {
            this.SoilTemperature = SoilTemperature;
        }

        public double getSoilHumidity() {
            return SoilHumidity;
        }

        public void setSoilHumidity(double SoilHumidity) {
            this.SoilHumidity = SoilHumidity;
        }

        public double getWindSpeed() {
            return WindSpeed;
        }

        public void setWindSpeed(double WindSpeed) {
            this.WindSpeed = WindSpeed;
        }

        public double getRainfall() {
            return Rainfall;
        }

        public void setRainfall(double Rainfall) {
            this.Rainfall = Rainfall;
        }

        public int getCarbondioxide() {
            return Carbondioxide;
        }

        public void setCarbondioxide(int Carbondioxide) {
            this.Carbondioxide = Carbondioxide;
        }

        public int getWindDirection() {
            return WindDirection;
        }

        public void setWindDirection(int WindDirection) {
            this.WindDirection = WindDirection;
        }

        public int getIllumination() {
            return Illumination;
        }

        public void setIllumination(int Illumination) {
            this.Illumination = Illumination;
        }
    }
}
