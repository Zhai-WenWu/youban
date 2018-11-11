package cn.bjhdltcdn.p2plive.broadcast;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.utils.Utils;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 融云2.6.0 Dev之后使用此机制，弃用setOnReceivePushMessageListener()机制
 */
public class RongPushMessageReceiver extends PushMessageReceiver {

    /**
     * onNotificationMessageArrived 用来接收服务器发来的通知栏消息(消息到达客户端时触发)，默认return false，
     * 通知消息会以融云 SDK 的默认形式展现。
     * 如果需要自定义通知栏的展示，在这里实现⾃己的通知栏展现代码，同时 return true 即可。
     *
     * @param context
     * @param pushNotificationMessage
     * @return
     */
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        Utils.wakeUpAndUnlock(App.getInstance());
        return false;
    }

    /**
     * onNotificationMessageClicked 是在⽤户点击通知栏消息时触发 (注意:如果⾃定义了通知栏的展现，则不会触发 标红)，默认 return false 。
     * 如果需要自定义点击通知时的跳转，return true 即可。
     *
     * @param context
     * @param pushNotificationMessage
     * @return
     */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {


        Logger.d("pushContent ===== " + pushNotificationMessage.getPushContent());
        Logger.d("extra ===== " + pushNotificationMessage.getExtra());
        Logger.d("pushData ===== " + pushNotificationMessage.getPushData());
        Logger.d("getObjectName ===== " + pushNotificationMessage.getObjectName());

        doStartApplicationWithPackageName(context.getPackageName());

        return true;
    }

    public static void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = App.getInstance().getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = App.getInstance().getPackageManager().queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cn);
            App.getInstance().startActivity(intent);
        }
    }

}
