package cn.bjhdltcdn.p2plive.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.greenrobot.eventbus.EventBus;

import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.GroupVideoGiftSuccessUpdateUiEvent;
import cn.bjhdltcdn.p2plive.event.One2OneVideoPropsEvent;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Props;

/**
 * 显示大礼物的队列服务
 * Android 8.0 还对特定函数做出了以下变更：
 *
 * 如果针对 Android 8.0 的应用尝试在不允许其创建后台服务的情况下使用 startService() 函数，则该函数将引发一个 IllegalStateException。
 *
 * 新的 Context.startForegroundService() 函数将启动一个前台服务。现在，即使应用在后台运行，系统也允许其调用 Context.startForegroundService()。不过，应用必须在创建服务后的五秒内调用该服务的 startForeground() 函数。
 *
 * 在被启动的Service创建服务后的五秒内调用startForground(0, new Notification())，如果不调用或调用时间超过5秒会抛出一个ANR。
 */
public class GiftBigAnimationIntentService extends IntentService {

    public GiftBigAnimationIntentService() {
        super("GiftBigAnimationIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_LOW);

            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, "id").build();

            startForeground(1, notification);
        }

    }

    /**
     * 显示礼物特效
     *
     * @param context
     * @param props   礼物对象
     */
    public static void startShowOnePropsAnimationQueue(Context context, Props props) {
        Intent intent = new Intent(context, GiftBigAnimationIntentService.class);
        intent.setAction(Constants.Action.ACTION_SHOW_ONE_PROPS_ANIMATION);
        intent.putExtra(Constants.Fields.PROPS, props);

        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        } else {
            // Pre-O behavior.
            context.startService(intent);
        }

    }

    /**
     * 显示礼物特效
     *
     * @param context
     * @param props           礼物对象
     * @param receiveBaseUser 接收人对象
     * @param sendBaseUser    发送人对象
     */
    public static void startShowPropsAnimationQueue(Context context, Props props, BaseUser receiveBaseUser, BaseUser sendBaseUser) {
        Intent intent = new Intent(context, GiftBigAnimationIntentService.class);
        intent.setAction(Constants.Action.ACTION_SHOW_MORE_PROPS_ANIMATION);
        intent.putExtra(Constants.Fields.PROPS, props);
        intent.putExtra(Constants.Fields.RECEVICE_BASEUSER, receiveBaseUser);
        intent.putExtra(Constants.Fields.SEND_BASEUSER, sendBaseUser);
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        } else {
            // Pre-O behavior.
            context.startService(intent);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();

            if (Constants.Action.ACTION_SHOW_MORE_PROPS_ANIMATION.equals(action)) {// 多人房间

                try {
                    // 从队列取出礼物消息
                    Props props = intent.getParcelableExtra(Constants.Fields.PROPS);
                    BaseUser receiveBaseUser = intent.getParcelableExtra(Constants.Fields.RECEVICE_BASEUSER);
                    BaseUser sendBaseUser = intent.getParcelableExtra(Constants.Fields.SEND_BASEUSER);

                    EventBus.getDefault().post(new GroupVideoGiftSuccessUpdateUiEvent(props, receiveBaseUser, sendBaseUser));

                    Thread.sleep(8 * 1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (Constants.Action.ACTION_SHOW_ONE_PROPS_ANIMATION.equals(action)) {// 一对一房间

                try {
                    // 从队列取出礼物消息
                    Props props = intent.getParcelableExtra(Constants.Fields.PROPS);
                    EventBus.getDefault().post(new One2OneVideoPropsEvent(props));

                    Thread.sleep(8 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
