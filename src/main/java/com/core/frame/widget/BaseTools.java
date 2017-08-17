package com.core.frame.widget;

import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

public class BaseTools {

    private static Point screenSize = new Point();

    public static Point getScreenSize(Context ctt) {
        if (ctt == null) {
            return screenSize;
        }
        WindowManager wm = (WindowManager) ctt.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            Display diplay = wm.getDefaultDisplay();
            if (diplay != null) {
                diplay.getMetrics(mDisplayMetrics);
                int W = mDisplayMetrics.widthPixels;
                int H = mDisplayMetrics.heightPixels;
                if (W * H > 0 && (W > screenSize.x || H > screenSize.y)) {
                    screenSize.set(W, H);
                }
            }
        }
        return screenSize;
    }

    public static String getSDPath(Context context) {
        File sdDir = null;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            Toast.makeText(context, "SD卡不存在，请插入SD卡", Toast.LENGTH_LONG).show();
            return "";
        }
        return sdDir.toString();
    }
}
