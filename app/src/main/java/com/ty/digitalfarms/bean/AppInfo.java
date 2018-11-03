package com.ty.digitalfarms.bean;

/**
 * desc:
 * author: XingZheng
 * date:2016/12/13
 */

public class AppInfo {

    private AppinfosBean appinfos;
    private int code;
    private String msg;

    public AppinfosBean getAppinfos() {
        return appinfos;
    }

    public void setAppinfos(AppinfosBean appinfos) {
        this.appinfos = appinfos;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class AppinfosBean {
        private String appName;
        private String appNo;
        private String archiveFlag;
        private String archiveTS;
        private String createTS;
        private String creator;
        private String modifer;
        private String modifyTS;
        private String versionAddress;
        private int versionCode;
        private String versionDesc;
        private String versionName;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppNo() {
            return appNo;
        }

        public void setAppNo(String appNo) {
            this.appNo = appNo;
        }

        public String getArchiveFlag() {
            return archiveFlag;
        }

        public void setArchiveFlag(String archiveFlag) {
            this.archiveFlag = archiveFlag;
        }

        public String getArchiveTS() {
            return archiveTS;
        }

        public void setArchiveTS(String archiveTS) {
            this.archiveTS = archiveTS;
        }

        public String getCreateTS() {
            return createTS;
        }

        public void setCreateTS(String createTS) {
            this.createTS = createTS;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getModifer() {
            return modifer;
        }

        public void setModifer(String modifer) {
            this.modifer = modifer;
        }

        public String getModifyTS() {
            return modifyTS;
        }

        public void setModifyTS(String modifyTS) {
            this.modifyTS = modifyTS;
        }

        public String getVersionAddress() {
            return versionAddress;
        }

        public void setVersionAddress(String versionAddress) {
            this.versionAddress = versionAddress;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionDesc() {
            return versionDesc;
        }

        public void setVersionDesc(String versionDesc) {
            this.versionDesc = versionDesc;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }
    }
}
