package cn.salesuite.saf.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.safframework.tony.common.reflect.Reflect;

/**
 * Created by Tony Shen on 2017/2/16.
 */

public class SAFUtils {

    public static boolean isHoneycombOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isICSOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    @TargetApi(19)
    public static boolean isKitkatOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 获取全局的context，也就是Application Context
     * @return
     */
    @TargetApi(14)
    public static Context getContext() {
        return Reflect.on("android.app.ActivityThread").call("currentApplication").get();
    }

    /**
     * 检查权限是否开启
     *
     * @param permission
     * @return true or false
     */
    public static boolean checkPermissions(Context context, String permission) {

        if (context==null) {
            if (SAFUtils.isICSOrHigher()) {
                context = getContext();
            } else {
                return false;
            }
        }

        PackageManager localPackageManager = context.getApplicationContext().getPackageManager();
        return localPackageManager.checkPermission(permission, context.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断是否存在sd卡
     * @return
     */
    public static boolean hasSdcard() {

        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }

    /**
     * 获取SD 卡内存
     * @return
     */
    public static long getAvailableSD() {
        if (!hasSdcard())
            return 0;

        String storageDirectory = Environment.getExternalStorageDirectory().toString();

        try {
            StatFs stat = new StatFs(storageDirectory);
            long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat
                    .getBlockSize());
            return avaliableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }
}
