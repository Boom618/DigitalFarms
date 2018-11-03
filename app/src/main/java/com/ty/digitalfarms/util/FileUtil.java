package com.ty.digitalfarms.util;

import java.io.File;
import java.math.BigDecimal;

public class FileUtil {

    /**
     * 删除文件
     * @param file
     * @return
     */
    public static boolean deleteFile(File file){
        if (file.isFile()&&file.exists()){
            return file.delete();
        }
        return false;
    }

    /**
     * 获取文件大小，单位：M
     * @param f
     * @return
     */
    public static double getFileSize(File f) {
        double  fileSize=(double)((double)(f.length()/1024))/1024;
        BigDecimal bd=new BigDecimal(fileSize);
        fileSize=bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return fileSize;
    }
}
