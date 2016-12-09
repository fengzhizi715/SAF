package cn.salesuite.saf.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import cn.salesuite.saf.config.SAFConstant;

/**
 * Created by Tony Shen on 2016/11/9.
 */

public class DeviceUtils {

    private static final String FIRST_TAG = "0";   //此值代表首次启动
    private static final String NO_FIRST_TAG = "1";   //此值代表非首次启动

    public static String getFingerPrinter(@NonNull Context context) {
        StringBuilder fa = new StringBuilder();
        fa.append(getLocal());
        fa.append(getDevice());
        fa.append(getBranding());
        if (!TextUtils.isEmpty(getMacAddress(context))) {
            fa.append(getMacAddress(context));
        } else {
            fa.append(getDeviceId(context));
        }
        fa.append(getManufacturer());
        fa.append(getScreenSize(context));
        fa.append(getCpuInfo());
        fa.append(String.valueOf((Calendar.getInstance().get(Calendar.ZONE_OFFSET) + Calendar.getInstance().get(Calendar.DST_OFFSET)) / 60000));
        String fp = String.valueOf(Math.abs(StringUtils.md5(fa.toString()).hashCode()));
        return fp;
    }

    private static String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};
        String[] arrayOfString;
        BufferedReader localBufferedReader = null;
        try {
            File file = new File(str1);
            if(!file.exists()){
                return "";
            }
            FileReader fr = new FileReader(file);
            //防止fr为空
            if (Preconditions.isBlank(fr)) {
                return "";
            }
            localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            //防止str2为空
            if (Preconditions.isBlank(str2)) {
                return "";
            }
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException ignored) {
        } finally {
            IOUtils.closeQuietly(localBufferedReader);
        }
        return Arrays.toString(cpuInfo);
    }

    public static String getNetGeneration(@NonNull Context context) {
        if (!SAFUtils.checkPermissions(context, "android.permission.ACCESS_NETWORK_STATE")) {
            return "";
        }
        String result = SAFConstant.NO_NETWORK;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() ==
                    NetworkInfo.State.CONNECTED) {
                result = SAFConstant.NETWORK_WIFI;
            }
            if (networkInfo != null && networkInfo.isAvailable()) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                        Context.TELEPHONY_SERVICE);
                switch (telephonyManager.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        result = SAFConstant.NETWORK_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        result = SAFConstant.NETWORK_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        result = SAFConstant.NETWORK_4G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        result = SAFConstant.NETWORK_4G;
                        break;
                    default:
                        result = SAFConstant.NETWORK_2G;
                        break;
                }
            }
        } catch (Exception ignored) {

        }
        return result;
    }

    /**
     * 获得Android设备唯一标识：Device_id
     * 尽量获取一次，以后把数值保存起来，减少漂移
     *
     * @param context context
     * @return device id
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(@NonNull Context context) {

        if (context == null) {
            return "";
        }
        String result = "";
        try {
            if (SAFUtils.checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                        .TELEPHONY_SERVICE);
//                获得设备IMEI标识
                String deviceId = tm.getDeviceId();
                String backId = "";
                if (deviceId != null) {
                    backId = deviceId;
                    backId = backId.replace("0", "");
                }

                if (((TextUtils.isEmpty(deviceId)) || TextUtils.isEmpty(backId)) && (Build.VERSION
                        .SDK_INT >= 9)) {
                    try {
                        Class c = Class.forName("android.os.SystemProperties");
                        Method get = c.getMethod("get", new Class[]{String.class, String.class});
                        deviceId = (String) get.invoke(c, new Object[]{"ro.serialno", "unknown"});
                    } catch (Exception t) {
                        deviceId = null;
                    }
                }

                if (Preconditions.isNotBlank(deviceId)) {
                    result = deviceId;
                } else {
                    result = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            } else {
                result = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        } catch (Exception ignored) {

        }
        return result;
    }

    /**
     * 获得系统版本
     *
     * @return os version
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备品牌
     *
     * @return branding
     */
    public static String getBranding() {
        return Build.BRAND;
    }

    /**
     * 获得制造商
     *
     * @return manufacturer
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 设备的名字
     *
     * @return device model
     */
    public static String getDevice() {
        return Build.MODEL;
    }

    //这个可获取到类似1080*1920
    public static String getScreenSize(@NonNull Context context) {

        String result = "";
        try {
            //first method
            if (Build.VERSION.SDK_INT >= 13 && Build.VERSION.SDK_INT < 17) {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context
                        .WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                int screenWidth = size.x;
                int screenHeight = size.y;
                if (context.getResources().getConfiguration().orientation == Configuration
                        .ORIENTATION_PORTRAIT) {
                    result = screenWidth + "x" + screenHeight;
                } else {
                    result = screenHeight + "x" + screenWidth;
                }
            } else if (Build.VERSION.SDK_INT >= 17) {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context
                        .WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                Point size = new Point();

                display.getRealSize(size);

                int screenWidth = size.x;
                int screenHeight = size.y;
                if (context.getResources().getConfiguration().orientation == Configuration
                        .ORIENTATION_PORTRAIT) {
                    result = screenWidth + "x" + screenHeight;
                } else {
                    result = screenHeight + "x" + screenWidth;
                }
            } else {
                DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
                // 竖屏
                if (context.getResources().getConfiguration().orientation == Configuration
                        .ORIENTATION_PORTRAIT) {
                    result = dm2.widthPixels + "x" + dm2.heightPixels;
                } else {// 横屏
                    result = dm2.heightPixels + "x" + dm2.widthPixels;
                }
            }
        } catch (Exception ignored) {

        }
        return result;
    }

    /**
     * 获得应用的包名
     *
     * @param context context
     * @return package name
     */
    public static String getPackageName(@NonNull Context context) {
        return context.getPackageName();
    }


    /**
     * 获得应用的名称
     *
     * @param context context
     * @return package name
     */
    public static String getAppName(@NonNull Context context) {
        String appName = "";
        try {
            appName = (String) context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
        } catch (Exception ignored) {
        }
        return appName;
    }


    /**
     * 获得当前应用的版本号
     *
     * @param context context
     * @return App Version
     */
    public static String getAppVersion(@NonNull Context context) {
        String result = "1.0";
        try {
            result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return result;
    }

    /**
     * 获得注册运营商的名字
     *
     * @param context context
     * @return Carrier
     */
    public static String getCarrier(@NonNull Context context) {
        String result = "";
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context
                    .TELEPHONY_SERVICE);
            if (!SAFUtils.checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
                result = carryByOperator(manager);
            } else {
                @SuppressLint("HardwareIds")
                String imsi = manager.getSubscriberId();

                if (TextUtils.isEmpty(imsi)) {
                    result = carryByOperator(manager);
                }

                if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
                    //中国移动
                    result = SAFConstant.CHINA_MOBILE;
                } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
                    //中国联通
                    result = SAFConstant.CHINA_UNICOM;
                } else if (imsi.startsWith("46003") || imsi.startsWith("46005")) {
                    //中国电信
                    result = SAFConstant.CHINA_TELECOM;
                } else if (imsi.startsWith("46020")) {
                    result = SAFConstant.CHINA_TIETONG;
                } else {
                    result = carryByOperator(manager);
                }
            }
        } catch (Exception ignored) {

        }
        return result;

    }

    private static String carryByOperator(TelephonyManager manager) {
        String operatorString = manager.getSimOperator();

        if ("46000".equals(operatorString) || "46002".equals(operatorString) || "46007".equals
                (operatorString)) {
            //中国移动
            return SAFConstant.CHINA_MOBILE;
        } else if ("46001".equals(operatorString) || "46006".equals(operatorString)) {
            //中国联通
            return SAFConstant.CHINA_UNICOM;
        } else if ("46003".equals(operatorString) || "46005".equals(operatorString)) {
            //中国电信
            return SAFConstant.CHINA_TELECOM;
        } else if ("46020".equals(operatorString)) {
            return SAFConstant.CHINA_TIETONG;
        } else {
            return SAFConstant.CHINA_CARRIER_UNKNOWN;
        }
    }

    /**
     * 获得本地语言和国家
     *
     * @return Language +county
     */
    public static String getLocal() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    /**
     * 获得网管硬件地址
     *
     * @param context context
     * @return Mac Address
     */
    @SuppressLint("HardwareIds")
    public static String getMacAddress(@NonNull Context context) {

        if (!SAFUtils.checkPermissions(context, "android.permission.ACCESS_WIFI_STATE")) {
            return "";
        }

        String result = "";

        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    result = wifiInfo.getMacAddress();
                }
            }
        } catch (Exception ignored) {

        }

        return result;
    }
}
