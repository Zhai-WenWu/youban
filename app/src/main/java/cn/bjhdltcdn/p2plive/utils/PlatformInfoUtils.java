package cn.bjhdltcdn.p2plive.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.PlatformInfo;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by wenquan on 2016/6/14.
 */
public class PlatformInfoUtils {
    private static PlatformInfo platformInfo ;
    private static int arr[] ;

    public static boolean checkPhoneState(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查权限
     * @param context
     * @param permission 权限
     * @return
     */
    public static boolean checkPermissions(Context context, String permission) {
        if (context == null) {
            return false;
        }
        PackageManager localPackageManager = context.getPackageManager();
        return localPackageManager.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 申请权限
     * @param context 上下文
     * @param permission 权限
     * @param requestCode 返回码
     */
    public static void requestPermissionsV6(Object context, String permission, int requestCode){
        if (context == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (context instanceof Context) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission((Context)context, permission);
                if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                    if (context instanceof Activity) {
                        ActivityCompat.requestPermissions((Activity)context,new String[]{permission},requestCode);
                    } else if (context instanceof Fragment) {
                        Fragment fragment = (Fragment) context;
                        fragment.requestPermissions(new String[]{permission}, requestCode);
                    }

                    return;
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    public static String getAndroidImei(Context context) {
        if (context == null) {
            return "";
        }
        if (checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
            String deviceId = "";
            if (checkPhoneState(context)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = tm.getDeviceId();
            }
            if (deviceId != null) {
//                Logger.v("commonUtil", "deviceId:" + deviceId);
                return deviceId;
            } else {
//                Logger.e("commonUtil", "deviceId is null");
                return "";
            }
        } else {
//            Logger.e("lost permissioin", "lost----->android.permission.READ_PHONE_STATE");
            return "";
        }
    }


    public static String getMeta(Context context, final String name) {
        String str = "";
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object v = ai.metaData.get(name);
            if (v != null) {
                str = v.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 获取渠道号
     * @return
     */
    public static String getChannel(){
        return getMeta(App.getInstance(),"channel");
    }

    public static PlatformInfo getClientDataJSONObj(Context context) {
        if (platformInfo != null) {
            return platformInfo;
        }

        //(String version, String platform, String channel, String imei, String systemVersion, String model, String manufacturer)
        PlatformInfo platformInfo = new PlatformInfo(
                getAppVersion(App.getInstance()),
                "android",
                getChannel(),
                getAndroidImei(App.getInstance()),
                getOsVersion(App.getInstance()),
                Build.MODEL,
                Build.MANUFACTURER,
                NetUtils.getLocalIpAddress(App.getInstance()),
                SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID,0));

        return platformInfo;
    }

    public static String getOsVersion(Context context) {
        String osVersion = "";
        if (checkPhoneState(context)) {
            osVersion = Build.VERSION.RELEASE;
//            Logger.d("osVersion ==== " + osVersion);
            return osVersion;
        } else {
//            Logger.d("OsVerson get failed");
            return null;
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getAppVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    public static int[] getWidthOrHeight(Context context){
        if (arr != null) {
            return arr;
        }

        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        arr = new int[2];
        arr[0] = width;
        arr[1] = height;

        return arr ;
    }

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        String  ipStr="127.0.0.1";
//         try {
//             for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
//                        InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet4Address) {
//                        KLog.e(inetAddress.getHostAddress().toString());
//                        ipStr=inetAddress.getHostAddress().toString();
//                        return ipStr;
//                    }
//                 }
//             }
//        } catch (Exception ex) {
//            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
//        }
        return ipStr;
    }

}
