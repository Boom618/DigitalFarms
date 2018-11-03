package com.ty.digitalfarms.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.ui.FarmsApp;
import com.ty.digitalfarms.ui.view.DownloadProgressDialog;
import com.zhouyou.http.subsciber.IProgressDialog;

/**
 * Created by Administrator on 2017/7/26.
 */

public final class UIUtils {


    public static String getPackName() {
        return getContext().getPackageName();
    }


    public static int getVersionCode(){
        int versionCode=-1;
        try {
            PackageManager packageManager = getContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("com.ty.mbets", 0);
            versionCode = packageInfo.versionCode;
            if (versionCode==-1) {
                return versionCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     *
     * @param deviceType
     * @return
     */
    public static BitmapDescriptor getMarker(int deviceType){
        BitmapDescriptor bitmap;
        switch (deviceType){
            case 1:
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_carmer);
                break;

            case 2:
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_weather_stat);
                break;
            default:
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_carmer);
                break;
        }

        return bitmap;
    }

    public static Context getContext() {
        return FarmsApp.getContext();
    }

    /**
     * 获取Resources对象
     *
     * @return
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {

        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResource().getDisplayMetrics()));

    }

    /**
     * px转换dip
     */
    public static int px2dip(int px) {
        final float scale = getResource().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取dimens
     *
     * @param id
     * @return
     */
    public static int getDimens(int id) {
        return (int) getResource().getDimension(id);
    }

    /**
     * 获取String 资源
     *
     * @param id
     * @return
     */
    public static String getString(int id) {
        return getResource().getString(id);
    }

    /**
     * 获取加载进度框
     * @param context
     * @return
     */
    public static IProgressDialog getDialog(final Context context){
        IProgressDialog mProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(context);
                dialog.setMessage(UIUtils.getString(R.string.loding));
                return dialog;
            }
        };
        return mProgressDialog;
    }

    /**
     * 显示Toast
     *
     * @param text
     */
    public static void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static int getColor(int resId) {
        return getResource().getColor(resId);
    }

    public static float dp2px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static void setTopBarTint(Activity activity,int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //设置系统栏设置颜色
            //tintManager.setTintColor(R.color.red);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(colorId);
            //Apply the specified drawable or color resource to the system navigation bar.
            //给导航栏设置资源
            tintManager.setNavigationBarTintResource(colorId);
        }
    }

    /**
     * 下载更新App
     * @param apkUrl
     * @param apkName
     */
    public static void download(final Activity activity, String apkUrl, String apkName) {

        final DownloadProgressDialog progressDialog = new DownloadProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        progressDialog.setTitle("下载提示");
        // 设置ProgressDialog 提示信息
        progressDialog.setMessage("当前下载进度:");
        // 设置ProgressDialog 标题图标
        //progressDialog.setIcon(R.drawable.a);
        // 设置ProgressDialog 进度条进度
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);
        progressDialog.show();


        new AppUpdateInstallUtils(activity, apkUrl, apkName, new AppUpdateInstallUtils.DownloadCallBack() {
            @Override
            public void onStart() {
                progressDialog.setProgress(0);
            }

            @Override
            public void onComplete(String path) {
                AppUpdateInstallUtils.installAPK(activity, path, getPackName() + ".fileProvider", new AppUpdateInstallUtils.InstallCallBack() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                        showToast("正在安装程序");
                    }

                    @Override
                    public void onFail(Exception e) {
                        progressDialog.dismiss();
                        showToast("安装失败,"+e.getMessage());
                    }
                });
            }

            @Override
            public void onLoading(long total, long current) {
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onFail(Exception e) {
                progressDialog.dismiss();
                showToast("下载失败:" + e.toString());
            }

        }).downloadAPK();

    }

    private static ProgressDialog dialog;

    public static void showProgressDialog(Context c, String msg) {
        dialog = ProgressDialog.show(c, "", msg);
    }

    public static void showProgressDialog(Context c, int resId) {
        dialog = ProgressDialog.show(c, "", c.getString(resId));
        Log.e("TAG",dialog.toString());
    }

    public static void cancelProgressDialog() {
        if (dialog != null) {
            Log.e("TAG",dialog.toString());
            dialog.cancel();
            dialog = null;
        }
    }

    public static String getErrorDesc() {
        int errorCode = VMSNetSDK.getInstance().getLastErrorCode();
        String errorDesc = VMSNetSDK.getInstance().getLastErrorDesc();
        return "errorCode:" + errorCode + ",errorDesc:" + errorDesc;
    }
}
