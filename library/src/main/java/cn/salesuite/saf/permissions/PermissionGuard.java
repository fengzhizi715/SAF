package cn.salesuite.saf.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by Tony Shen on 2016/11/28.
 */

public class PermissionGuard {

    private static final int REQUEST_ID = 128;
    private Context mContext;
    private Activity mActivity;
    private Fragment mFragment;
    private PublishSubject<Integer> publishSubject;
    private boolean onPermissonsResult = false;
    private Subscription subscription;

    public PermissionGuard(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
        publishSubject = PublishSubject.create();
    }

    public PermissionGuard(Context context,Fragment fragment) {
        this.mContext = context;
        this.mFragment = fragment;
        publishSubject = PublishSubject.create();
    }

    @UiThread
    public void requestPermission(@NonNull Runnable runnable, @NonNull  String... permissions) {
        requestPermission(runnable, null, permissions);
    }

    /**
     * @param runnable When agreed, this runnable will proceed.
     * @param deniedRunnable When agreed, this deniedRunnable will proceed(special case). Usually null is used.
     * @param permissions required permissions
     */
    @UiThread
    public void requestPermission(@NonNull final Runnable runnable, @Nullable final Runnable deniedRunnable, @NonNull final String... permissions) {
        if (Build.VERSION.SDK_INT < 23  || isPermissionsGranted(permissions)) {
            runnable.run();
            return;
        }

        ArrayList<String> rationalePermissions = shouldShowRequestPermissionRationale(permissions);
        if (rationalePermissions.size() > 0) {
            new AlertDialog.Builder(mContext).setMessage("test")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermission(permissions, runnable, deniedRunnable);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (deniedRunnable != null) {
                                deniedRunnable.run();
                            }
                        }
                    }).show();
        } else {
            requestPermission(permissions, runnable, deniedRunnable);
        }
    }

    private boolean isPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     *  Denied case
     */
    private ArrayList<String> shouldShowRequestPermissionRationale(String[] permissions) {
        ArrayList<String> rationalePermissions = new ArrayList<>();
        for (String permission : permissions) {

            if (mActivity!=null) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                    rationalePermissions.add(permission);
                }
            } else if (mFragment != null) {
                if (mFragment.shouldShowRequestPermissionRationale(permission)) {
                    rationalePermissions.add(permission);
                }
            }

        }
        return rationalePermissions;
    }

    private void requestPermission(@NonNull String[] permissions, @NonNull final Runnable runnable, @Nullable final Runnable deniedRunnable) {
        if (mActivity!=null) {
            ActivityCompat.requestPermissions(mActivity, permissions, REQUEST_ID);
        } else {
            mFragment.requestPermissions(permissions, REQUEST_ID);
        }

        subscription = publishSubject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer v) {
                if (v == PackageManager.PERMISSION_GRANTED) {
                    runnable.run();
                } else {
                    if (deniedRunnable != null) {
                        deniedRunnable.run();
                    }
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        onPermissonsResult = true;
        if (requestCode == REQUEST_ID) {
            boolean allAgreed = true;
            ArrayList<String> revokedPermissions = new ArrayList<>();
            for (int i = 0, len = permissions.length;  i < len ; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
					/* http://stackoverflow.com/questions/30719047/android-m-check-runtime-permission-how-to-determine-if-the-user-checked-nev */
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions[i])) {
						/* user denied flagging NEVER ASK AGAIN */
                        revokedPermissions.add(permissions[i]);
                    }
                    allAgreed = false;
                    break;
                }
            }
            if (revokedPermissions.size() > 0) {
//                Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.permissions_do_not_ask_again,
//                        Snackbar.LENGTH_LONG)
//                        .setAction(R.string.ok, view -> {
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                    Uri.fromParts("package", mActivity.getPackageName(), null));
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            mActivity.startActivity(intent);
//                        })
//                        .show();
            }
            publishSubject.onNext(allAgreed ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED);
            subscription.unsubscribe();
        }
    }

    public void resetPermissionsResult() {
        onPermissonsResult = false;
    }

    public boolean isPermissionResult() {
        return onPermissonsResult;
    }
}
