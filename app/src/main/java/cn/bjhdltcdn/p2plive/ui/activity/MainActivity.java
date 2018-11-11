package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.orhanobut.logger.Logger;
import com.taobao.sophix.SophixManager;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.callback.RongYunUnreadCountCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.AnonymousChatRoomStatusEvent;
import cn.bjhdltcdn.p2plive.event.AnonymousMsgEvent;
import cn.bjhdltcdn.p2plive.event.LocationEvent;
import cn.bjhdltcdn.p2plive.event.OnPageSelectedEvent;
import cn.bjhdltcdn.p2plive.event.RongyunReceiveUnreadCountEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetLoginRecommendListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateChatRoomResponse;
import cn.bjhdltcdn.p2plive.httpresponse.VersionResponse;
import cn.bjhdltcdn.p2plive.model.ChatInfo;
import cn.bjhdltcdn.p2plive.model.UserLocation;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.MainTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.HomeImageDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.HomeFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.MessageFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.MyFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.StoreFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends BaseActivity implements BaseView {
    private CustomViewPager mViewPager;
    private MainTabFragmentAdapter adapter;
    private TextView tab1NameView, tab2NameView, tab3NameView, tab4NameView, tab5NameView;
    private TextView tab4popView;
    private ImageView tab1ImageView, tab2ImageView, tab3ImageView, tab4ImageView, tab5ImageView;
    private RelativeLayout tab1LayoutView, tab2LayoutView, tab3LayoutView, tab4LayoutView, tab5LayoutView;

    private AMapLocationListener locationListener;
    private UserPresenter userPresenter;
    private ChatRoomPresenter chatRoomPresenter;

    private Handler handler;
    private ImageView publishView;

    private TextView chatRoomView;
    private ImageView closeChatRoomView;
    private ImageView chatRoomIcon;
    /**
     * 0进入 1退出
     */
    private int chatRoomType;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        currentUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        setContentView(R.layout.activity_main);
        initView();

        EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());

        // 更新位置
        initDBlocat();

        if (handler == null) {
            handler = new Handler();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 强制升级
                getUserPresenter().getUpgradeVersion(BuildConfig.VERSION_NAME + "", 1);
            }
        }, 3 * 1000);

        if (BuildConfig.LOG_DEBUG) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            Logger.d("density == " + displayMetrics.density + " == densityDpi === " + displayMetrics.densityDpi + " === widthPixels === " + displayMetrics.widthPixels + " ==heightPixels== " + displayMetrics.heightPixels);
        }

    }

    private void initView() {
        Button bt_test = findViewById(R.id.bt_test);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,BrandBusinessDetailActiviy.class));
            }
        });
        mViewPager = (CustomViewPager) findViewById(R.id.main_view_page);
        chatRoomView = findViewById(R.id.tv_chatroom);
        chatRoomIcon = findViewById(R.id.iv_icon_chatroom);
        closeChatRoomView = findViewById(R.id.iv_close_chatroom);
        //首页
        tab1LayoutView = (RelativeLayout) findViewById(R.id.tab_1_layout_view);
        tab1ImageView = (ImageView) findViewById(R.id.tab_1_image_view);
        tab1NameView = (TextView) findViewById(R.id.tab_1_name_view);

        //圈子
        tab2LayoutView = (RelativeLayout) findViewById(R.id.tab_2_layout_view);
        tab2ImageView = (ImageView) findViewById(R.id.tab_2_image_view);
        tab2NameView = (TextView) findViewById(R.id.tab_2_name_view);

//        //发现
//        tab3LayoutView = (RelativeLayout) findViewById(R.id.tab_3_layout_view);
//        tab3ImageView = (ImageView) findViewById(R.id.tab_3_image_view);
//        tab3NameView = (TextView) findViewById(R.id.tab_3_name_view);

        //消息
        tab4LayoutView = (RelativeLayout) findViewById(R.id.tab_4_layout_view);
        tab4ImageView = (ImageView) findViewById(R.id.tab_4_image_view);
        tab4NameView = (TextView) findViewById(R.id.tab_4_name_view);
        tab4popView = (TextView) findViewById(R.id.tab_4_pop_view);

        //我
        tab5LayoutView = (RelativeLayout) findViewById(R.id.tab_5_layout_view);
        tab5ImageView = (ImageView) findViewById(R.id.tab_5_image_view);
        tab5NameView = (TextView) findViewById(R.id.tab_5_name_view);

        if (mViewPager == null) {
            new NullPointerException();
        }

        mViewPager.setIsCanScroll(true);
        setupViewPager(mViewPager);
        publishView = findViewById(R.id.iv_public);
        publishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //一键发布界面
                Intent intent = new Intent(MainActivity.this, PublishActivity.class);
                intent.putExtra(Constants.Fields.TYPE, 1);
                startActivity(intent);
            }
        });

    }

    private void setupViewPager(final ViewPager viewPager) {

        adapter = new MainTabFragmentAdapter(getSupportFragmentManager());

        // 首页
        HomeFragment homeFragment = new HomeFragment();
        adapter.addFragment(homeFragment, "首页");

        // 店铺
//        ShopFragment shopFragment = new ShopFragment();
        StoreFragment storeFragment=new StoreFragment();
        adapter.addFragment(storeFragment, "店铺");

        //消息
        MessageFragment messageFragment = new MessageFragment();
        adapter.addFragment(messageFragment, "消息");

        //我
        MyFragment myFragment = new MyFragment();
        adapter.addFragment(myFragment, "我");


        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tab1ImageView.setImageResource(R.mipmap.main_home_icon);
        tab1NameView.setTextColor(getResources().getColor(R.color.color_333333));

        tab1LayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        tab2LayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                EventBus.getDefault().post(new OnPageSelectedEvent(1));
            }
        });

        tab4LayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                EventBus.getDefault().post(new OnPageSelectedEvent(3));
            }
        });

        tab5LayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                tab1NameView.setTextColor(getResources().getColor(R.color.color_999999));
                tab2NameView.setTextColor(getResources().getColor(R.color.color_999999));
                tab4NameView.setTextColor(getResources().getColor(R.color.color_999999));
                tab5NameView.setTextColor(getResources().getColor(R.color.color_999999));


                switch (position) {
                    case 0:
                        tab1ImageView.setImageResource(R.mipmap.main_home_icon);
                        tab2ImageView.setImageResource(R.mipmap.store_icon);
                        tab4ImageView.setImageResource(R.mipmap.main_message_nor_icon);
                        tab5ImageView.setImageResource(R.mipmap.main_my_nor_icon);
                        tab1NameView.setTextColor(getResources().getColor(R.color.color_333333));

                        break;
                    case 1:
                        tab1ImageView.setImageResource(R.mipmap.main_home_nor_icon);
                        tab2ImageView.setImageResource(R.mipmap.main_store_icon);
                        tab4ImageView.setImageResource(R.mipmap.main_message_nor_icon);
                        tab5ImageView.setImageResource(R.mipmap.main_my_nor_icon);
                        tab2NameView.setTextColor(getResources().getColor(R.color.color_333333));

                        break;
                    case 2:
                        tab1ImageView.setImageResource(R.mipmap.main_home_nor_icon);
                        tab2ImageView.setImageResource(R.mipmap.store_icon);
                        tab4ImageView.setImageResource(R.mipmap.main_message_icon);
                        tab5ImageView.setImageResource(R.mipmap.main_my_nor_icon);
                        tab4NameView.setTextColor(getResources().getColor(R.color.color_333333));

                        break;
                    case 3:
                        tab1ImageView.setImageResource(R.mipmap.main_home_nor_icon);
                        tab2ImageView.setImageResource(R.mipmap.store_icon);
                        tab4ImageView.setImageResource(R.mipmap.main_message_nor_icon);
                        tab5ImageView.setImageResource(R.mipmap.main_my_icon);
                        tab5NameView.setTextColor(getResources().getColor(R.color.color_333333));

                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    /**
     * 初始化位置
     */
    private void initDBlocat() {

        locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                if (null != location) {
                    //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                    if (location.getErrorCode() == 0) {
                        UserLocation userLocation = new UserLocation();
                        userLocation.setAddr(location.getAddress());
                        userLocation.setCity(location.getCity());
                        userLocation.setDistrict(location.getDistrict());
                        userLocation.setLatitude(location.getLatitude() + "");
                        userLocation.setLongitude(location.getLongitude() + "");
                        userLocation.setProvince(location.getProvince());
                        App.getInstance().setCity(location.getCity());
                        App.getInstance().setLoctionSuccess(true);
                        App.getInstance().setUserLocation(userLocation);
                        // 发送位置
                        updateLocation(userLocation);
                        // 停止定位
                        App.getInstance().getLocationService().stopLocation();
                    } else {
                        Utils.showToastShortTime("定位失败");
                        App.getInstance().setLoctionSuccess(false);
                        App.getInstance().setUserLocation(null);
                        updateLocation(null);
                    }
                } else {
                    Utils.showToastShortTime("定位失败");
                    App.getInstance().setLoctionSuccess(false);
                    App.getInstance().setUserLocation(null);
                    updateLocation(null);
                }

            }
        };
        App.getInstance().getLocationService().setLocationListener(locationListener);
        App.getInstance().getLocationService().startLocation();
    }


    public UserPresenter getUserPresenter() {

        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    /**
     * 更新位置
     *
     * @param userLocation
     */
    private void updateLocation(UserLocation userLocation) {

        if (userLocation != null) {
            getUserPresenter().updateUserLocation(currentUserId
                    , userLocation.getLongitude()
                    , userLocation.getLatitude()
                    , userLocation.getProvince()
                    , userLocation.getCity()
                    , userLocation.getDistrict()
                    , userLocation.getAddr());
        } else {
            getUserPresenter().updateUserLocation(currentUserId
                    , ""
                    , ""
                    , ""
                    , ""
                    , ""
                    , "");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallBack(RongyunReceiveUnreadCountEvent event) {
        if (event == null) {
            return;
        }

        try {

            RongIMutils.getUnreadCount(new RongYunUnreadCountCallback() {
                @Override
                public void onSuccess(final int count) {

                    long unreadAllCount = GreenDaoUtils.getInstance().queryPushdbModelUnreadAllCountByQueryBuilder() + count;
                    if (unreadAllCount < 1) {
                        tab4popView.setVisibility(View.GONE);
                    } else {
                        tab4popView.setVisibility(View.VISIBLE);
                    }
                    tab4popView.setText(String.valueOf(unreadAllCount));

                }

                @Override
                public void onError() {

                    Logger.d("onError() ==查询未读消息失败= ");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AnonymousChatRoomStatusEvent event) {
        if (event == null) {
            return;
        }
        if (event.isInChatRoom() && Constants.Object.CHATINFO != null) {
            chatRoomIcon.setVisibility(View.VISIBLE);
            Utils.ImageViewDisplayByUrl(Constants.Object.CHATINFO.getChatIcon(), chatRoomIcon);
            chatRoomView.setVisibility(View.VISIBLE);
            closeChatRoomView.setVisibility(View.VISIBLE);
            chatRoomView.setText(Constants.Object.CHATINFO.getChatName());
            chatRoomView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.isAllowClick()) {
                        chatRoomType = 0;
                        getChatRoomPresenter().updateChatRoomUser(currentUserId, 0, Constants.Object.CHATINFO.getChatId(), chatRoomType);
                    }
                }
            });
            closeChatRoomView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //房主
//                    if (Constants.Object.CHATINFO.getUserRole() == 1) {
//                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                        dialog.setText("关闭匿名聊天房间", "关闭代表解散此聊天房间，是否确认关闭？", "取消", "关闭");
//                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                            @Override
//                            public void onLeftClick() {
//
//                            }
//
//                            @Override
//                            public void onRightClick() {
//                                getChatRoomPresenter().updateChatRoom(currentUserId, Constants.Object.CHATINFO.getChatId(), null,
//                                        1, 0, 0, null, null);
//                            }
//                        });
//                        dialog.show(getSupportFragmentManager());
//                    } else {
                    if (Utils.isAllowClick()) {

                        chatRoomType = 1;
                        getChatRoomPresenter().updateChatRoomUser(currentUserId, 0, Constants.Object.CHATINFO.getChatId(), chatRoomType);
                    }
//                    }
                }
            });
        } else {
            if (event.getMeaaageType() != 0) {
                Utils.showToastShortTime(event.getTipMsg());
                RongIMutils.quitChatRoom(Constants.Object.CHATINFO.getChatId());
                Constants.Object.CHATINFO = null;
            }
            chatRoomIcon.setVisibility(View.GONE);
            chatRoomView.setVisibility(View.GONE);
            closeChatRoomView.setVisibility(View.GONE);
        }

    }

    @Override
    public void updateView(String apiName, Object object) {

        if (isFinishing()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            Utils.showToastShortTime(e.getMessage());
            return;
        }

        if (InterfaceUrl.URL_UPDATE_USER_LOCATION.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.getCode() == 200) {
                    Logger.d("更新用户位置成功..........");
                }
            }
        } else if (InterfaceUrl.URL_GETUPGRADEVERSION.equals(apiName)) {

            // 结束任务后回收资源
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }

            if (object instanceof VersionResponse) {
                VersionResponse response = (VersionResponse) object;
                if (response.getCode() == 200) {
                    // 是否升级 0 不升级 1 强制升级 2 普通升级,
                    App.getInstance().setLinkUrl(response.getLinkUrl());
                    App.getInstance().setShareUrl(response.getShareUrl());
                    App.getInstance().setQqShareUrl(response.getQqShareUrl());
                    App.getInstance().setWeixinShareUrl(response.getWxShareUrl());
//                    Utils.showToastShortTime("更新操作条件： " + response.getIsUpgrade());

                    switch (response.getIsUpgrade()) {
                        case 0:
                            getUserPresenter().getLoginRecommendList(currentUserId);
                            break;
                        case 1:
                            MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this).content(R.string.str_update_version_tips_1)
                                    .contentColor(getResources().getColor(R.color.color_333333))
                                    .backgroundColor(getResources().getColor(R.color.white))
                                    .negativeText("暂不升级").negativeColor(getResources().getColor(R.color.color_1b9e4e)).onNegative(new MaterialDialog.SingleButtonCallback() {

                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                            MobclickAgent.onKillProcess(App.getInstance());//方法，用来保存统计数据
                                            RongIMutils.exitApp();
                                            finish();
                                        }

                                    }).positiveText("立即升级").positiveColor(getResources().getColor(R.color.color_1b9e4e)).onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            try {
                                                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent1);

                                                dialog.dismiss();
                                                MobclickAgent.onKillProcess(App.getInstance());//方法，用来保存统计数据
                                                RongIMutils.exitApp();
                                                finish();

                                            } catch (Exception e) {
                                                Utils.showToastShortTime("很抱歉，不能启动应用市场！");
                                            }
                                        }

                                    }).show();

                            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.dismiss();
                                    MobclickAgent.onKillProcess(App.getInstance());//方法，用来保存统计数据
                                    RongIMutils.exitApp();
                                    finish();
                                }
                            });
                            break;

                        case 2:
                            new MaterialDialog.Builder(MainActivity.this).content(R.string.str_update_version_tips_2)
                                    .contentColor(getResources().getColor(R.color.color_333333))
                                    .backgroundColor(getResources().getColor(R.color.white))
                                    .negativeText("暂时忽略").negativeColor(getResources().getColor(R.color.color_1b9e4e)).onNegative(new MaterialDialog.SingleButtonCallback() {

                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    getUserPresenter().getLoginRecommendList(currentUserId);
                                }

                            }).positiveText("立即升级").positiveColor(getResources().getColor(R.color.color_1b9e4e)).onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    try {
                                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                        Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent1);

                                    } catch (Exception e) {
                                        Utils.showToastShortTime("很抱歉，不能启动应用市场！");
                                    }
                                }

                            }).show();

                            break;

                        case 3:
                            getUserPresenter().getLoginRecommendList(currentUserId);

                            // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
                            long sophixManagerTime = SafeSharePreferenceUtils.getLong(Constants.KEY.KEY_SOPHIX_MANAGER_TIME, 0);
                            long sophixManagerCount = SafeSharePreferenceUtils.getLong(Constants.KEY.KEY_SOPHIX_MANAGER_COUNT, 0);

                            if (sophixManagerTime == 0) {// 首次更新操作
//                                Utils.showToastShortTime("首次更新操作");
                                SophixManager.getInstance().queryAndLoadNewPatch();
                                SafeSharePreferenceUtils.saveLong(Constants.KEY.KEY_SOPHIX_MANAGER_TIME, System.currentTimeMillis());
                                SafeSharePreferenceUtils.saveLong(Constants.KEY.KEY_SOPHIX_MANAGER_COUNT, sophixManagerCount + 1);

                            } else if (System.currentTimeMillis() - sophixManagerTime < 24 * 60 * 60 * 1000) {// 一个账号下，每台设备上的每个app每天平均可免费查询20次
//                                Utils.showToastShortTime("更新操作在24小时内  使用次数 sophixManagerCount === " + sophixManagerCount);
                                if (sophixManagerCount < 20) {
                                    SophixManager.getInstance().queryAndLoadNewPatch();
                                    SafeSharePreferenceUtils.saveLong(Constants.KEY.KEY_SOPHIX_MANAGER_TIME, System.currentTimeMillis());
                                    SafeSharePreferenceUtils.saveLong(Constants.KEY.KEY_SOPHIX_MANAGER_COUNT, sophixManagerCount + 1);
                                }

                            } else {
//                                Utils.showToastShortTime("更新操作在24小时外  使用次数 sophixManagerCount === " + sophixManagerCount);
                                if (sophixManagerCount >= 20) {// 超过24小时并且超过20（含）次，则回归参数
                                    SafeSharePreferenceUtils.saveLong(Constants.KEY.KEY_SOPHIX_MANAGER_TIME, System.currentTimeMillis());
                                    SafeSharePreferenceUtils.saveLong(Constants.KEY.KEY_SOPHIX_MANAGER_COUNT, 1);
                                    SophixManager.getInstance().queryAndLoadNewPatch();
                                }

                            }

                            break;

                    }

                }

            }
        } else if (InterfaceUrl.URL_GETLOGINRECOMMENDLIST.equals(apiName)) {
            if (object instanceof GetLoginRecommendListResponse) {
                GetLoginRecommendListResponse response = (GetLoginRecommendListResponse) object;
                if (response.getCode() == 200 && response.getList().size() > 0) {
                    HomeImageDialog dialog = new HomeImageDialog();
                    dialog.setDate(response.getList());
                    dialog.show(getSupportFragmentManager());
                }
            }
        }
//        else if (InterfaceUrl.URL_UPDATECHATROOM.equals(apiName)) {//关闭聊天室
//            if (object instanceof UpdateChatRoomResponse) {
//                UpdateChatRoomResponse updateChatRoomResponse = (UpdateChatRoomResponse) object;
//                Utils.showToastShortTime(updateChatRoomResponse.getMsg());
//                if (updateChatRoomResponse.getCode() == 200) {
//                    RongIMutils.quitChatRoom(Constants.Object.CHATINFO.getChatId());
//                    Constants.Object.CHATINFO = null;
//                    chatRoomIcon.setVisibility(View.GONE);
//                    chatRoomView.setVisibility(View.GONE);
//                    closeChatRoomView.setVisibility(View.GONE);
//                }
//            }
//
//        }
        else if (InterfaceUrl.URL_UPDATECHATROOMUSER.equals(apiName)) {//进入、退出聊天室
            if (object instanceof UpdateChatRoomResponse) {
                UpdateChatRoomResponse updateChatRoomResponse = (UpdateChatRoomResponse) object;
                if (updateChatRoomResponse.getCode() == 200) {
                    //进入
                    if (chatRoomType == 0) {
                        final ChatInfo chatInfo = updateChatRoomResponse.getChatInfo();
                        if (chatInfo != null) {
                            Constants.Object.CHATINFO = chatInfo;
                            RongIMutils.joinChatRoom(MainActivity.this, chatInfo.getChatId(), false);
                            EventBus.getDefault().post(new AnonymousChatRoomStatusEvent(true));
                            UserInfo userInfo = new UserInfo(String.valueOf(chatInfo.getToBaseUser().getUserId()), chatInfo.getToBaseUser().getNickName(), Uri.parse(chatInfo.getToBaseUser().getUserIcon()));
                            RongIM.getInstance().refreshUserInfoCache(userInfo);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new AnonymousMsgEvent(chatInfo.getToBaseUser(), null));
                                }
                            }, 1000);
                            Utils.showToastShortTime(updateChatRoomResponse.getMsg());
                        }
                    } else if (chatRoomType == 1) {//退出
                        RongIMutils.quitChatRoom(Constants.Object.CHATINFO.getChatId());
                        Constants.Object.CHATINFO = null;
                        chatRoomIcon.setVisibility(View.GONE);
                        chatRoomView.setVisibility(View.GONE);
                        closeChatRoomView.setVisibility(View.GONE);
                        Utils.showToastShortTime(updateChatRoomResponse.getMsg());
                    }
                } else {
                    Utils.showToastShortTime(updateChatRoomResponse.getMsg());
                }
            }
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            MaterialDialog dialog = new MaterialDialog.Builder(this).title(R.string.str_dialog_title).titleColor(getResources().getColor(R.color.color_333333)).content(R.string.str_dialog_content)
                    .contentColor(getResources().getColor(R.color.color_333333))
                    .backgroundColor(getResources().getColor(R.color.white))
                    .negativeText(R.string.str_dialog_disagree).negativeColor(getResources().getColor(R.color.color_1b9e4e)).onNegative(new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();

                        }

                    }).positiveText(R.string.str_dialog_agree).positiveColor(getResources().getColor(R.color.color_1b9e4e)).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            MobclickAgent.onKillProcess(getApplicationContext());
                            RongIMutils.exitApp();
                            finish();
                        }

                    }).show();

        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        if (locationListener != null) {
            App.getInstance().getLocationService().stopLocation();
            App.getInstance().getLocationService().unRegisterLocationListener(locationListener);
        }
        App.getInstance().getLocationService().destroyLocation();

        ProgressDialogUtils.getInstance().onDestroy();

        GreenDaoUtils.getInstance().onDestroy();

        if (userPresenter != null) {
            userPresenter.onDestroy();
        }
        userPresenter = null;

        // 结束任务后回收资源
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

    }


}
