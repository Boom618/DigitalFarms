package com.ty.digitalfarms.bean;


import java.util.List;

public class LoginInfo {

    private int code;
    private List<UserInfo> data;
    private ServiceInfo service;

    public class UserInfo{
        private String userName;
        private String companyname;
        private String companyno;
        private String companyzip;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCompanyname() {
            return companyname;
        }

        public void setCompanyname(String companyname) {
            this.companyname = companyname;
        }

        public String getCompanyno() {
            return companyno;
        }

        public void setCompanyno(String companyno) {
            this.companyno = companyno;
        }

        public String getCompanyzip() {
            return companyzip;
        }

        public void setCompanyzip(String companyzip) {
            this.companyzip = companyzip;
        }
    }

    public class ServiceInfo{
        private String serverAddress;
        private String userName;
        private String password;

        public String getServerAddress() {
            return serverAddress;
        }

        public void setServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<UserInfo> getData() {
        return data;
    }

    public void setData(List<UserInfo> data) {
        this.data = data;
    }

    public ServiceInfo getService() {
        return service;
    }

    public void setService(ServiceInfo service) {
        this.service = service;
    }
}
