package cn.bjhdltcdn.p2plive.app;

import android.app.Activity;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.bugtags.library.Bugtags;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.mob.MobApplication;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.umeng.analytics.MobclickAgent;

import org.lasque.tusdk.core.TuSdk;

import java.util.List;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.callback.AgoraMessageHandlerCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.model.UserLocation;
import cn.bjhdltcdn.p2plive.service.JobSchedulerService;
import cn.bjhdltcdn.p2plive.service.LocationService;
import cn.bjhdltcdn.p2plive.ui.activity.BaseEngineEventHandlerActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.AppLogAdapter;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.widget.imkit.CallExtensionModule;
import cn.bjhdltcdn.p2plive.widget.imkit.PicturesExtensionModule;
import cn.bjhdltcdn.p2plive.widget.imkit.videoExtensionModule;
import io.agora.rtc.RtcEngine;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.push.RongPushClient;
import okhttp3.Interceptor;

/**
 * Ò
 * Created by xiawenquan on 17/11/6.
 */

public class App extends MobApplication implements Application.ActivityLifecycleCallbacks {


    private static App instance;
    private String linkUrl;//链接地址,
    private String shareUrl;//普通分享地址,
    private String weixinShareUrl;//微信分享地址,
    private String qqShareUrl;//QQ分享地址,
    private Activity currentActivity;

    public static App getInstance() {
        return instance;
    }

    private LocationService locationService;
    private String city;
    private boolean loctionSuccess;
    private UserLocation userLocation;

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getWeixinShareUrl() {
        return weixinShareUrl;
    }

    public void setWeixinShareUrl(String weixinShareUrl) {
        this.weixinShareUrl = weixinShareUrl;
    }

    public String getQqShareUrl() {
        return qqShareUrl;
    }

    public void setQqShareUrl(String qqShareUrl) {
        this.qqShareUrl = qqShareUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public boolean isLoctionSuccess() {
        return loctionSuccess;
    }

    public void setLoctionSuccess(boolean loctionSuccess) {
        this.loctionSuccess = loctionSuccess;
    }

    private boolean isBackground;

    public boolean isBackground() {
        return isBackground;
    }

    public void setBackground(boolean background) {
        isBackground = background;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    /**
     * app版本号
     */
    private String appVersion;

    /*******
     * 声网相关
     **********/
    private RtcEngine rtcEngine;
    private AgoraMessageHandlerCallback messageHandler;

    /**
     * 分割 Dex 支持
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        // 阿里热修复组件
        initSophixManager();

    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Density.setDensity(this, 375f);
        registerActivityLifecycleCallbacks(this);

        instance = this;
        GreenDaoUtils.getInstance().getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 网络层初始化
                // 可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                OkGo.getInstance().init(instance).setRetryCount(0);
                List<Interceptor> list = OkGo.getInstance().getOkHttpClient().interceptors();
                if (list != null && list.size() > 0) {
                    for (Interceptor interceptor : list) {
                        if (interceptor instanceof HttpLoggingInterceptor) {
                            HttpLoggingInterceptor httpLoggingInterceptor = (HttpLoggingInterceptor) interceptor;
                            httpLoggingInterceptor.setPrintLevel(BuildConfig.LOG_DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
                            break;
                        }
                    }
                }

                // 初始化日志框架
                Logger.addLogAdapter(BuildConfig.LOG_DEBUG ? new AndroidLogAdapter() : new AppLogAdapter());

                onStartJobSchedulerService();
                //声网
                messageHandler = new AgoraMessageHandlerCallback();

                //普通统计场景类型
                MobclickAgent.setScenarioType(instance, MobclickAgent.EScenarioType.E_UM_NORMAL);
                //6.0.0版本及以后
                MobclickAgent.enableEncrypt(true);


            }
        });

        rongInit();

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());

        TuSdk.init(getInstance(), Constants.KEY.TUSDK_SECRET_KEY);
        TuSdk.enableDebugLog(true);
        Bugtags.start(Constants.KEY.BUG_KEY, this, Bugtags.BTGInvocationEventNone);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

    }

    private void rongInit() {
        /**
         * 注意：
         * IMKit SDK调用第一步 初始化
         * context上下文
         * 只有两个进程需要初始化，主进程和 push 进程
         */
        if (getApplicationInfo().packageName.equals(RongIMutils.getCurProcessName(getApplicationContext()))) {

            /**
             * 注册小米推送
             */
            RongPushClient.registerMiPush(instance, Constants.KEY.KEY_MIPUSH_APP_ID, Constants.KEY.KEY_MIPUSH_APP_KEY);

            /**
             * 注册华为推送
             */
            try {
                RongPushClient.registerHWPush(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
             * 初始化融云sdk
             */
            RongIM.init(this, RongIMutils.getRongCloudAppKey());

            /**
             * 融云SDK事件监听处理
             * 注册相关代码，只需要在主进程里做。
             */
            RongIMutils.initDefaultListener();
            RongIMutils.setOtherListener();


        }
    }

    /**
     * 初始化阿里热修复组件
     */
    private void initSophixManager() {

        try {
            appVersion = BuildConfig.VERSION_NAME;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }

        Log.d("补丁", "appVersion === " + appVersion);


        // initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setSecretMetaData("24759303-1", "0804270bc469644873a918fecc9afe6f", "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCoXD1sO4IKcynaYFO6RouPMeF0dvfoaG9T8H3ShY1G89b85X5hDG95LtHDxPPhg8lL9lAVrKhmsK00o92iZ48OXDj8k+2RlKG9ceHhnmOO2VYPCv3WK3v1EEHcsRoGe/aD1IjVG/MfMiPkdhAALRFclzEF622PTuXEr566f4PCpTKpI1AJBza/1vAvPGBRfsWaSAU6GrcUFtam7pU7viB7mfkO+bFmZ2NSo1mShLbcX0C4EOqBOMnH/gDOFShDLr1COjXUmAEAqRHzq4AGVlJozZpSX8kF2PWN5CNUFmkVqLDFeilT7/sTUd3iL2fcArFtmcj8EHvIG69leeo1584pAgMBAAECggEBAKSYyVQvOYuN27Urx6+tmHN+iLScOvJ0Z6Lg06Il/2EGs4C7vTqUNcd8Bogm3Wf0t7AUN2d5TYpX4H4+VkiYhju73drRpMlXTmkwFW3Gs/7MbF54mIHyMVX9duaUHqWIPBHWj5AJqz8sffq1kh8Z8IddKQhoisw4jlokeEJMuDxqOs100rcC/jYP6UedJCJJbcDflvWH84e3WcEvI3ZkYEMH/JAvWhPoBktI/n+qllUz27CkxXPrziDKG6MAdKaOYgcrs/Y/a4yujz3TTkdxaTXMG4draMaFUos8afrCdtAGgL4AY2f1YMzopu5/1ZQmpV20V+l6oIfyxr+DOOXfH6ECgYEA8gIZEM1m9YTOZCf788TqjsMtrJt4lKJ8obvMO6S7kQgyZvbM60mfQTmu8sll2TVJekPXffvl+vs3FF/Utd0wgtJIWmU6goeAaCVbs6LwIoFHT64a7kLNsSPKWi4Ybdban9aslOIlVxYvYyHngipptPx4qbS7oD9lON8d62jKu1cCgYEAshgZLGw4clrotqNm/Vk49OvTg7f8NHkJ2eor89uzjPXAwIFqtg2vtDEfUnQwEgrmZqPI8zbpIGLAWm40FAM2SnYG6LE6xh7JvxORTusepiR9n79xUNRP6/e80D9q8Mh8G3aEkYCep6//6Tji17Wj4TkfHjNYA5Meq8SNsqBlUn8CgYATygYgsJsdsnlqCTb6DP0dJWqjtabbiJnmY8PkkKjhyCjO8Jl7F+8U11Gt+rgAVfInNEr3u1Rn/IjZeqibInYoDGfsNGDfcZMXcQ6ZltZKDAY3xsXe+8l0FbgjPaezu6Du9w/vsLpa7656TbhvlWZFbWu0fNnm1ahCSURaDpydSwKBgFZ45vgvz7SOuYG8k0weH06klr3U/qtHveXGEvND0Ml3pdCSxgqBWLfIRioV6iVIj5h0nCSjm8wyxqzh5310Fc9PwYkITL5XwqN5T8ue0/Hds/V10gJqJCgx0MbYlAc3gTMgd7viVKadCUfnbBVKx7iasKZMTcbmjzTdqeN6h9fnAoGBAIlox3neUAGou/tq9w3X9PKndufkdAXXUUL9dAlmGJsD9F5RvTzTHBxlViMEgd1Uc4zVlEXQAWwzi7kuA7vBQBHqxknLlyeg/NQAV736uLEvta8C5dorT0CJnTWVx1UwM1K7n+h35cEc55IM8ThEwnt2nPaaw1P2FpZpyRWMcgOv")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {

                        Log.d("补丁", "补丁 ===mode === " + mode + " ==code === " + code + " ===handlePatchVersion === " + handlePatchVersion);
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Log.d("补丁", "表明补丁加载成功" + " ==info === " + info);
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3

                            Log.d("补丁", "表明新补丁生效需要重启" + " ==info === " + info + " handlePatchVersion === " + handlePatchVersion);

                            if (isBackground()) {
                                SophixManager.getInstance().killProcessSafely();
                            }

                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.d("补丁", "其它错误信息" + " ==info === " + info);
                        }
                    }
                }).initialize();
    }

    /**
     * 设置加号扩展
     *
     * @param conversationType 聊天类型
     * @param isAnonymous      是否匿名
     */
    public static void setMyExtensionModule(Conversation.ConversationType conversationType, boolean isAnonymous) {

        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
            }
        }
        if (isAnonymous) {
            RongExtensionManager.getInstance().registerExtensionModule(new PicturesExtensionModule());
        } else {
            if (conversationType == Conversation.ConversationType.GROUP) {
                RongExtensionManager.getInstance().registerExtensionModule(new videoExtensionModule());
            } else if (conversationType == Conversation.ConversationType.PRIVATE) {
                RongExtensionManager.getInstance().registerExtensionModule(new CallExtensionModule());
            }
        }

    }


    public void setRtcEngine() {
        if (rtcEngine == null) {
            try {
                rtcEngine = RtcEngine.create(getApplicationContext(), Constants.KEY.AGORA_APP_KEY, messageHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public RtcEngine getRtcEngine() {
        setRtcEngine();
        return rtcEngine;
    }

    public void setEngineEventHandlerActivity(BaseEngineEventHandlerActivity engineEventHandlerActivity) {
        messageHandler.setActivity(engineEventHandlerActivity);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

//        Logger.d("onTrimMemory() " + level);

        //在 App 被置换到后台的时候，调用 Glide.cleanMemroy() 清理掉所有的内存缓存
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
            return;
        }

        //在其它情况的 onTrimMemroy() 回调中，直接调用 Glide.trimMemory() 方法来交给 Glide 处理内存情况
        Glide.get(this).trimMemory(level);


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

//        Logger.d("onLowMemory() ");

        // 内存低的时候清理Glide的缓存
        Glide.get(this).onLowMemory();


    }

    /**
     * 启动保活进程
     */
    public void onStartJobSchedulerService() {

        // 5.0以后启动进程
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                JobInfo.Builder builder = new JobInfo.Builder(10001, new ComponentName(getPackageName(), JobSchedulerService.class.getName()));
                builder.setMinimumLatency(5000).setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                builder.setPersisted(true);//设备重启之后你的任务是否还要继续执行
                jobScheduler.schedule(builder.build());
            } else {
                JobInfo jobInfo = new JobInfo.Builder(10000, new ComponentName(getPackageName(), JobSchedulerService.class.getName()))
                        .setPeriodic(5000)//间隔50000毫秒
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)////设置需要的网络条件，默认NETWORK_TYPE_NONE
                        .setPersisted(true)//设备重启之后你的任务是否还要继续执行
//                            .setRequiresDeviceIdle(false)// 设置手机是否空闲的条件,默认false
//                            .setRequiresCharging(false)// 设置是否充电的条件,默认false
//                            .setOverrideDeadline(1000 * 60 * 5)// 设置deadline，若到期还没有达到规定的条件则会开始执行
                        .build();
                //.setMinimumLatency(3000);// 设置任务运行最少延迟时间


                // 启动一个任务
                jobScheduler.schedule(jobInfo);
            }

        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
//        Logger.d("onActivityCreated() " + activity == null ? "" : activity.getClass().getSimpleName() + " === bundle ===" + bundle);

    }

    @Override
    public void onActivityStarted(Activity activity) {
//        Logger.d("onActivityStarted() " + activity == null ? "" : activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
//        Logger.d("onActivityResumed() " + activity == null ? "" : activity.getClass().getSimpleName());

    }

    @Override
    public void onActivityPaused(Activity activity) {
//        Logger.d("onActivityPaused() " + activity == null ? "" : activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
//        Logger.d("onActivityStopped() " + activity == null ? "" : activity.getClass().getSimpleName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
//        Logger.d("onActivitySaveInstanceState() " + activity == null ? "" : activity.getClass().getSimpleName() + " === bundle ===" + bundle);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
//        Logger.d("onActivityDestroyed() " + activity == null ? "" : activity.getClass().getSimpleName());

    }


    public Activity getCurrentActivity() {
        return currentActivity;
    }


    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_cccccc, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


}
