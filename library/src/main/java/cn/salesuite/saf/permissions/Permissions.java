package cn.salesuite.saf.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import org.aspectj.lang.JoinPoint;

/**
 * Created by Tony Shen on 2016/11/28.
 */

public class Permissions {

    final public static int REQUEST_CODE = 987;

    static void askSinglePermissionToActivity(JoinPoint joinPoint, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            Context context = (Context) joinPoint.getTarget();
            Activity activity = (Activity) joinPoint.getTarget();
            new PermissionProvider(context, activity).requestPermissions(new String[]{permission});
        }
    }

    public static boolean isPermissionGranted(Context context, String permission){
        return new PermissionProvider(context, null).isPermissionGranted(permission);
    }

    private static String getPermissionLabel(@NonNull Context context, @NonNull String permissionName) {
        try {
            PermissionInfo permissionInfo = context.getPackageManager().getPermissionInfo(permissionName, 0);
            PackageItemInfo groupInfo = context.getPackageManager().getPermissionGroupInfo(permissionInfo.group, 0);
            CharSequence permissionGroupLabel = groupInfo.loadLabel(context.getPackageManager());
            return permissionGroupLabel.toString();
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
