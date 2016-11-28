package cn.salesuite.saf.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import cn.salesuite.saf.utils.Preconditions;

/**
 * Created by Tony Shen on 2016/11/28.
 */

public class PermissionProvider {

    private Context context;
    private Activity activity;

    PermissionProvider(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    void requestPermissions(String[] permissions) {
        List<String> permissionsToCheck = new ArrayList<>();

        // Get the list of requested permissions that are not permission granted
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToCheck.add(permission);
            }
        }

        // Request the permissions
        if (Preconditions.isNotBlank(permissionsToCheck) && Build.VERSION.SDK_INT >= 23) {
            String[] permissionsToRequest = permissionsToCheck.toArray(new String[permissionsToCheck.size()]);
            activity.requestPermissions(permissionsToRequest, Permissions.REQUEST_CODE);
        }
    }

    boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    Context getContext() {
        return context;
    }

    void setContext(Context context) {
        this.context = context;
    }

    Activity getActivity() {
        return activity;
    }

    void setActivity(Activity activity) {
        this.activity = activity;
    }
}
