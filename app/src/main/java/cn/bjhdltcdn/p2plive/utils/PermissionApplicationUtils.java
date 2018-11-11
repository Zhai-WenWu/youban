package cn.bjhdltcdn.p2plive.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限检测申请工具类
 *
 * @author zhudi
 */
public class PermissionApplicationUtils {
    /**
     * 需要申请的权限
     */
    private List<String> needPermissionList;
    /**
     * 检测的权限
     */
    private List<String> checkPermissionList = new ArrayList<>(1);
    /**
     * 拒绝的权限
     */
    private List<String> refusePermissionList;

    public void checkPermission(Activity context, IperssionCallBack callBack) {
        if (needPermissionList == null) {
            needPermissionList = new ArrayList<>(1);
            needPermissionList.add(Manifest.permission.READ_PHONE_STATE);
            needPermissionList.add(Manifest.permission.READ_SMS);
            needPermissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            needPermissionList.add(Manifest.permission.CAMERA);
            needPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.RECORD_AUDIO);
            needPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (context == null) {
            return;
        }

        for (int i = 0; i < needPermissionList.size(); i++) {
            String permission = needPermissionList.get(i);
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                checkPermissionList.add(permission);
            }
        }
        if (checkPermissionList.size() == 0) {
            callBack.isSuccess();
            return;
        }
        ActivityCompat.requestPermissions(context, checkPermissionList.toArray(new String[checkPermissionList.size()]), 1);
    }

    /**
     * 权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param callBack
     */
    public void permissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, final IperssionCallBack callBack) {
        if (requestCode == 1) {
            if (refusePermissionList == null) {
                refusePermissionList = new ArrayList<>(1);
            }
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    //位置信息只需要提示一次 不需要强制开启
                    if (!permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        refusePermissionList.add(permissions[i]);
                    }
                }
            }
            if (!refusePermissionList.isEmpty()) {
                callBack.isFalure();
            } else {
                callBack.isSuccess();
            }
        }
    }

    public interface IperssionCallBack {
        void isSuccess();

        void isFalure();

    }

}