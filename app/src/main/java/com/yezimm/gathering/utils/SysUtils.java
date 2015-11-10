package com.yezimm.gathering.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.yezimm.gathering.MainActivity;
import com.yezimm.gathering.mapsdk.ctrl.MapWrapperController;

import java.io.IOException;
import java.io.InputStream;

/**
 * 系统相关常用操作类
 */
public class SysUtils {

    /**
     * 因为一些与UI相关的类比如dialogue只有Activity作为Context参数才能运行，因此提供该方法
     * 不要滥用该方法，其仅仅用来获取MainActivity作为context, 目前由于要兼容以前的一些旧代码,
     * 也会在一些非context的地方调用该代码. 新的代码尽量不要调用此方法.
     *
     * 正确使用方法的地方： 1.必须使用Activity作为context参数 2.要操作定位按钮，指南针等全局的一些UI。
     */
    public static MainActivity getMainActivity() {
        return MainActivity.getInstance();
    }

    public static MapWrapperController getMapCtrl() {
        if (MainActivity.getInstance() != null ){
            return MainActivity.getInstance().getMapController();
        }else{
            return null;
        }
    }

    public static String getString(Context context, int resId) {
        return context.getString(resId);
    }

    public static String getString(Context context, int resId, Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }

    public static Drawable getDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    public static int getColor(Context context, int resId){
        return context.getResources().getColor(resId);
    }

    public static float getDimension(Context context, int resId){
        return context.getResources().getDimension(resId);
    }

    public static int getDimensionPixelSize(Context context, int resId){
        return context.getResources().getDimensionPixelSize(resId);
    }

    /**
     * 取设备显示信息
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WM.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 隐藏输入法键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        if (activity == null)
            return;
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager mm = ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
            mm.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 取当前接入点的名称，不会返回null
     *
     * @param context
     * @return
     */
    public static synchronized String getCurrentNetName(Context context) {
        if (context == null)
            return "";

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        String name = "";
        if (net != null) {
            name = net.getExtraInfo();
        }
        if (name == null) {
            name = "";
        }
        return name;
    }

    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

}
