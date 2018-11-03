package com.ty.digitalfarms.bean;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 我的相册存放视频文件和相册文件
 */
public class FileInfo implements Serializable{
    private String fileLastModified;
    private int fileCount;
    private double fileSize;
    private List<File> fileList;

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileLastModified() {
        return fileLastModified;
    }

    public void setFileLastModified(String fileLastModified) {
        this.fileLastModified = fileLastModified;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }
}
