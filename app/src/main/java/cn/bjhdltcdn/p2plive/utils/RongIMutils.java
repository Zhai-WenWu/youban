package cn.bjhdltcdn.p2plive.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.callback.RongYunUnreadCountCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.AnonymousChatRoomManageEvent;
import cn.bjhdltcdn.p2plive.event.AnonymousChatRoomStatusEvent;
import cn.bjhdltcdn.p2plive.event.AnonymousSendImageMessageEvent;
import cn.bjhdltcdn.p2plive.event.AttentionMessageEvent;
import cn.bjhdltcdn.p2plive.event.GiftShowEvent;
import cn.bjhdltcdn.p2plive.event.GroupOperationEvent;
import cn.bjhdltcdn.p2plive.event.RongyunReceiveUnreadCountEvent;
import cn.bjhdltcdn.p2plive.event.ShareSuccessEvent;
import cn.bjhdltcdn.p2plive.event.ShopPlaceOrderEvent;
import cn.bjhdltcdn.p2plive.event.UserPortraitClickEvent;
import cn.bjhdltcdn.p2plive.event.VideoCMDTextMessageEvent;
import cn.bjhdltcdn.p2plive.event.VideoEvent;
import cn.bjhdltcdn.p2plive.event.VideoGroupManageEvent;
import cn.bjhdltcdn.p2plive.event.VideoGroupMicApplyEvent;
import cn.bjhdltcdn.p2plive.event.VideoGroupTransferHostingEvent;
import cn.bjhdltcdn.p2plive.event.VideoPhoneStateEvent;
import cn.bjhdltcdn.p2plive.event.VideoStatusEvent;
import cn.bjhdltcdn.p2plive.httpresponse.FindTokenByUserIdResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetBaseUserInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetGroupInfoResponse;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.AddressBook;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ChatBaseUser;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.ParseShopPlaceOrderMessage;
import cn.bjhdltcdn.p2plive.model.PlaceOrderMessage;
import cn.bjhdltcdn.p2plive.model.PlayInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.model.PushdbModel;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanSendActiveInvitationMessageModel;
import cn.bjhdltcdn.p2plive.provider.GroupShareItemProvider;
import cn.bjhdltcdn.p2plive.provider.SendActiveInvitationItemProvider;
import cn.bjhdltcdn.p2plive.provider.SharedMessageProvider;
import cn.bjhdltcdn.p2plive.provider.VideoMessageItemProvider;
import cn.bjhdltcdn.p2plive.provider.YouBanApplyClerkMessageProvider;
import cn.bjhdltcdn.p2plive.provider.YouBanApplyCreateStoreResultProvider;
import cn.bjhdltcdn.p2plive.provider.YouBanFlashSalesHelpMessageProvider;
import cn.bjhdltcdn.p2plive.provider.YouBanProductOrderMessageProvider;
import cn.bjhdltcdn.p2plive.provider.YouBanShopEvaluateOrderMessageProvider;
import cn.bjhdltcdn.p2plive.provider.YouBanShopPlaceOrderMessageProvider;
import cn.bjhdltcdn.p2plive.provider.YouBanShopReceiveOrderMessageProvider;
import cn.bjhdltcdn.p2plive.provider.YouBanShopRefundOrderMessageProvider;
import cn.bjhdltcdn.p2plive.ui.activity.GroupAtUserActivity;
import cn.bjhdltcdn.p2plive.ui.activity.GroupDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.activity.VideoChatActivity;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.RongIM;
import io.rong.imkit.YouBanActivityMessage;
import io.rong.imkit.YouBanAnonymousMessage;
import io.rong.imkit.YouBanShopEvaluateOrderMessage;
import io.rong.imkit.YouBanStoreClertMessage;
import io.rong.imkit.YouBanApplyCreateStoreResultMessage;
import io.rong.imkit.YouBanAttentionMessage;
import io.rong.imkit.YouBanFlashSalesHelpMessage;
import io.rong.imkit.YouBanGroupMessage;
import io.rong.imkit.YouBanGroupSharedMessage;
import io.rong.imkit.YouBanHelpInfoMessage;
import io.rong.imkit.YouBanLiveVideoMessage;
import io.rong.imkit.YouBanLivesRoomVideoMessage;
import io.rong.imkit.YouBanOrganizationMessage;
import io.rong.imkit.YouBanProductOrder;
import io.rong.imkit.YouBanSayLoveInfoMessage;
import io.rong.imkit.YouBanSendActiveInvitationMessage;
import io.rong.imkit.YouBanSharedMessage;
import io.rong.imkit.YouBanShopPlaceOrderMessage;
import io.rong.imkit.YouBanShopReceiveOrderMessage;
import io.rong.imkit.YouBanShopRefundOrderMessage;
import io.rong.imkit.YouBanShortVideoMessage;
import io.rong.imkit.YouBanSystemMessage;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.mention.IMentionedInputListener;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by xiawenquan on 17/11/6.
 */

public class RongIMutils {
    private static ObjectMapper mMapper;
    private static Handler handler;

    public static ObjectMapper getMapper() {
        if (mMapper == null) {
            mMapper = new ObjectMapper();
        }
        //当反序列化json时，未知属性会引起的反序列化被打断，这里我们禁用未知属性打断反序列化功能，
        //因为，例如json里有10个属性，而我们的bean中只定义了2个属性，其它8个属性将被忽略
        mMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mMapper;
    }

    /**
     * 能否点击头像
     */
    private static boolean isCanOnUserPortraitClick = true;

    public static void setIsCanOnUserPortraitClick(boolean isCanOnUserPortraitClick) {
        RongIMutils.isCanOnUserPortraitClick = isCanOnUserPortraitClick;
    }

    public static boolean isCanOnUserPortraitClick() {
        return isCanOnUserPortraitClick;
    }

    /***
     * 连接融云入口
     *
     * @param token 融云sdk服务器返回的凭证
     */
    public static void connect(final String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Logger.e("----connect onTokenIncorrect--");
            }

            @Override
            public void onSuccess(String userId) {
                Logger.e("----connect onSuccess userId----" + userId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("----connect onError ErrorCode----:" + errorCode);

            }
        });


    }

    /**
     * 获取融云的appkey
     *
     * @return
     */
    public static String getRongCloudAppKey() {
        String appkey = "";
        if (BuildConfig.LOG_DEBUG) { // 测试环境
            appkey = Constants.KEY.KEY_RONG_CLOUD_TEST;
        } else {// 生产环境
            appkey = Constants.KEY.KEY_RONG_CLOUD_OFFICIAL;
        }
        Logger.e("Rong-----appkey ==== " + appkey);
        return appkey;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;

    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    public static void initDefaultListener() {

        registerMessageType();
        //设置用户信息提供者
        setUserInfoProvider();
        //设置群组信息提供者
        setGroupInfoProvider();
        //设置会话界面操作的监听器。
        setConversationBehaviorListener();
        //会话列表界面操作的监听器
        setConversationListBehaviorListener();
        /**
         * 使用消息携带用户信息
         * 前提：需要设置RongIM.getInstance().setCurrentUserInfo(userInfo);
         *
         * 消息体内是否有 userinfo 这个属性
         */
        RongIM.getInstance().setMessageAttachedUserInfo(true);


    }


    /**
     * 注册自定义消息
     */
    public static void registerMessageType() {
        // 活动消息
        RongIM.registerMessageType(YouBanActivityMessage.class);
        // 群消息
        RongIM.registerMessageType(YouBanGroupMessage.class);
        // 群分享消息
        RongIM.registerMessageType(YouBanGroupSharedMessage.class);
        // 发送活动邀请函消息
        RongIM.registerMessageType(YouBanSendActiveInvitationMessage.class);
        // 视频消息
        RongIM.registerMessageType(YouBanLiveVideoMessage.class);
        // 视频房间消息
        RongIM.registerMessageType(YouBanLivesRoomVideoMessage.class);
        // 圈子消息
        RongIM.registerMessageType(YouBanOrganizationMessage.class);
        // 表白消息
        RongIM.registerMessageType(YouBanSayLoveInfoMessage.class);
        // 帮帮忙消息
        RongIM.registerMessageType(YouBanHelpInfoMessage.class);
        // 私信礼物消息
//        RongIM.registerMessageType(GiftMessage.class);
        // 圈子，帖子，表白，PK，聊天室 分享消息
        RongIM.registerMessageType(YouBanSharedMessage.class);
        // 商品试用消息
        RongIM.registerMessageType(YouBanProductOrder.class);
        // 闪购活动推送消息
        RongIM.registerMessageType(YouBanFlashSalesHelpMessage.class);
        // 短视频消息
        RongIM.registerMessageType(YouBanShortVideoMessage.class);
        //关注
        RongIM.registerMessageType(YouBanAttentionMessage.class);
        // 系统消息
        RongIM.registerMessageType(YouBanSystemMessage.class);
        // 校园店-下单消息
        RongIM.registerMessageType(YouBanShopPlaceOrderMessage.class);
        // 校园店-接单消息
        RongIM.registerMessageType(YouBanShopReceiveOrderMessage.class);
        // 校园店-退款消息
        RongIM.registerMessageType(YouBanShopRefundOrderMessage.class);
        //匿名聊天室
        RongIM.registerMessageType(YouBanAnonymousMessage.class);
        //店员申请
        RongIM.registerMessageType(YouBanStoreClertMessage.class);
        // 订单评价
        RongIM.registerMessageType(YouBanShopEvaluateOrderMessage.class);

        // 群分享item提供者
        RongIM.registerMessageTemplate(new GroupShareItemProvider());

        // 活动邀请函item提供者
        RongIM.registerMessageTemplate(new SendActiveInvitationItemProvider());

        // 短视频提供者
        RongIM.registerMessageTemplate(new VideoMessageItemProvider());

        // 私信礼物提供者
//        RongIM.registerMessageTemplate(new GiftMessageItemProvider());

        // 分享item提供者
        RongIM.registerMessageTemplate(new SharedMessageProvider());

        // 商品试用订单提供者
        RongIM.registerMessageTemplate(new YouBanProductOrderMessageProvider());

        // 闪购活动推送
        RongIM.registerMessageTemplate(new YouBanFlashSalesHelpMessageProvider());

        // 下单推送提供者
        RongIM.registerMessageTemplate(new YouBanShopPlaceOrderMessageProvider());

        // 接单推送提供者
        RongIM.registerMessageTemplate(new YouBanShopReceiveOrderMessageProvider());

        // 退款推送提供者
        RongIM.registerMessageTemplate(new YouBanShopRefundOrderMessageProvider());

        //店员申请
        RongIM.registerMessageTemplate(new YouBanApplyClerkMessageProvider());

        //订单评价
        RongIM.registerMessageTemplate(new YouBanShopEvaluateOrderMessageProvider());

        //申请开店结果消息
        RongIM.registerMessageType(YouBanApplyCreateStoreResultMessage.class);
        RongIM.registerMessageTemplate(new YouBanApplyCreateStoreResultProvider());


    }

    /**
     * 设置用户信息提供者
     */
    public static void setUserInfoProvider() {
        //获取用户信息
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userId) {
                Logger.d("userId ==== " + userId);
                return findUserByUserId(userId);
            }
        }, true);
    }

    /**
     * 设置群组信息的提供者
     */
    public static void setGroupInfoProvider() {
        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {

            @Override
            public Group getGroupInfo(String groupId) {
                return findGroupInfo(groupId);
            }
        }, true);
    }

    public static Group findGroupInfo(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            Logger.d("groupId ===is null= " + groupId);
            return null;
        }

        if (!TextUtils.isDigitsOnly(groupId)) {
            Logger.d("groupId ===不是数字= " + groupId);
            return null;
        }
        Map map = new ArrayMap(1);
        map.put(Constants.Fields.GROUP_ID, groupId);
        ApiData.getInstance().postData(InterfaceUrl.URL_GETGROUPINFO, "", map, new JsonCallback<GetGroupInfoResponse>(GetGroupInfoResponse.class) {
            @Override
            public void onSuccess(Response object) {
                if (object.body() instanceof GetGroupInfoResponse) {
                    GetGroupInfoResponse response = (GetGroupInfoResponse) object.body();
                    if (response.getCode() == 200) {

                        cn.bjhdltcdn.p2plive.model.Group mGroup = response.getGroup();

                        // 缓存群组信息
                        if (mGroup != null) {
                            RongIM.getInstance().refreshGroupInfoCache(new Group(mGroup.getGroupId() + "", mGroup.getGroupName(), Uri.parse(!StringUtils.isEmpty(mGroup.getGroupImg()) ? mGroup.getGroupImg() : "")));
                        }

                    }
                }
            }
        });

        return null;
    }


    public static UserInfo findUserByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            Logger.d("userId ===is null= " + userId);
            return null;
        }

        if (!TextUtils.isDigitsOnly(userId)) {
            Logger.d("userId ===不是数字= " + userId);
            return null;
        }

        Map map = new ArrayMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        ApiData.getInstance().postData(InterfaceUrl.URL_GETBASEUSERINFO, "", map, new JsonCallback<GetBaseUserInfoResponse>(GetBaseUserInfoResponse.class) {
            @Override
            public void onSuccess(Response response) {
                if (response.body() instanceof GetBaseUserInfoResponse) {
                    GetBaseUserInfoResponse getBaseUserInfoResponse = (GetBaseUserInfoResponse) response.body();
                    if (getBaseUserInfoResponse.getCode() == 200) {
                        /**
                         * 刷新用户缓存数据。
                         *
                         * @param userInfo 需要更新的用户缓存数据。
                         */
                        BaseUser baseUser = getBaseUserInfoResponse.getUser();
                        if (baseUser == null) {
                            Logger.e("baseUser is null ");
                            return;
                        }
                        UserInfo userInfo = new UserInfo(baseUser.getUserId() + "", baseUser.getNickName(), Uri.parse(baseUser.getUserIcon()));
                        RongIM.getInstance().refreshUserInfoCache(userInfo);

                    }
                }
            }
        });

        return null;
    }

    /**
     * 呼叫请求
     *
     * @param toUserId   对方用户的id
     * @param roomNumber 房间号
     * @param type       呼叫方式 1视频呼叫 2语音呼叫
     */
    public static void callRequest(final long toUserId, final String roomNumber, final int type) {

        long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        GreenDaoUtils.getInstance().getBaseUser(myUserId, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(Object object) {
                if (object instanceof BaseUser) {
                    BaseUser baseUser = (BaseUser) object;

                    YouBanLiveVideoMessage textMessage = getOneToOneTextMessage(roomNumber, baseUser, "呼叫通话", 50001, type);

                    if (textMessage == null) {
                        return;
                    }

                    RongIMutils.sendMessage(Conversation.ConversationType.PRIVATE, toUserId + "", textMessage, baseUser.getNickName() + "发来视频邀请", textMessage.getExtra(), new SendMessageCallBack() {
                        @Override
                        public void sendMessageCallBack(boolean isSuccess) {
                            if (!isSuccess) {
                                EventBus.getDefault().post(new VideoStatusEvent(1));
                            }
                        }
                    });
                }
            }
        });


    }


    /**
     * 取消呼叫请求
     *
     * @param toUserId 对方用户的id
     * @param isFinish 是否关闭页面
     */
    public static void canlceCallRequest(final long toUserId, final String roomNumber, final boolean isFinish) {


        long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        GreenDaoUtils.getInstance().getBaseUser(myUserId, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(Object object) {
                if (object instanceof BaseUser) {
                    BaseUser baseUser = (BaseUser) object;

                    YouBanLiveVideoMessage textMessage = getOneToOneTextMessage(roomNumber, baseUser, "取消通话", 50004, 0);

                    if (textMessage == null) {
                        return;
                    }

                    RongIMutils.sendMessage(Conversation.ConversationType.PRIVATE, toUserId + "", textMessage, baseUser.getNickName() + "取消视频邀请", textMessage.getExtra(), new SendMessageCallBack() {
                        @Override
                        public void sendMessageCallBack(boolean isSuccess) {
                            if (isSuccess) {
                                App.getInstance().getRtcEngine().leaveChannel();
                                if (isFinish) {
                                    EventBus.getDefault().post(new VideoEvent(106));
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * 拒绝请求
     *
     * @param toUserId         对方用户的id
     * @param roomNumber       房间号
     * @param isFinishActivity 页面是否关闭
     */
    public static void refuseRequestVideo(final int messageType, final long toUserId, final String roomNumber, final boolean isFinishActivity) {


        long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        GreenDaoUtils.getInstance().getBaseUser(myUserId, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(Object object) {
                if (object instanceof BaseUser) {
                    BaseUser baseUser = (BaseUser) object;

                    //发送拒绝信令
                    YouBanLiveVideoMessage textMessage = getOneToOneTextMessage(roomNumber, baseUser, "拒绝通话", messageType, 0);

                    if (textMessage == null) {
                        return;
                    }

                    RongIMutils.sendMessage(Conversation.ConversationType.PRIVATE, toUserId + "", textMessage, "", "", new SendMessageCallBack() {
                                @Override
                                public void sendMessageCallBack(boolean isSuccess) {

                                }
                            }

                    );

                    if (isFinishActivity) {
                        App.getInstance().getRtcEngine().leaveChannel();
                        EventBus.getDefault().post(new VideoEvent(106));
                    } else {
                        EventBus.getDefault().post(new VideoEvent(100));
                    }
                }
            }
        });

    }

//    /**
//     * 挂断请求
//     *
//     * @param toUserId   对方用户的id
//     * @param roomNumber 房间号
//     */
//    public static void finishVideo(final long toUserId, final String roomNumber) {
//
//
//        long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        GreenDaoUtils.getInstance().getBaseUser(myUserId, new GreenDaoUtils.ExecuteCallBack() {
//            @Override
//            public void callBack(Object object) {
//                if (object instanceof BaseUser) {
//                    BaseUser baseUser = (BaseUser) object;
//                    //发送挂断信令
//                    YouBanLiveVideoMessage textMessage = getOneToOneTextMessage(roomNumber, baseUser, "挂断电话", 50009, 0);
//
//                    if (textMessage == null) {
//                        return;
//                    }
//
//                    RongIMutils.sendMessage(Conversation.ConversationType.PRIVATE, toUserId + "", textMessage, "", "", new SendMessageCallBack() {
//                                @Override
//                                public void sendMessageCallBack(boolean isSuccess) {
//                                    App.getInstance().getRtcEngine().leaveChannel();
//                                    EventBus.getDefault().post(new VideoEvent(106));
//                                }
//                            }
//                    );
//
//                }
//            }
//        });
//
//    }

    /**
     * 发送文本消息
     *
     * @param toUserId 对方用户的id
     * @param text     文字消息
     */
    public static void sendChatTextMessage(final long toUserId, final String text) {

        long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        GreenDaoUtils.getInstance().getBaseUser(myUserId, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(Object object) {
                if (object instanceof BaseUser) {
                    BaseUser baseUser = (BaseUser) object;
                    //发送文本信令
                    final YouBanLiveVideoMessage textMessage = getOneToOneTextMessage("", baseUser, text, 50008, 0);
                    final VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                    videoCMDTextMessageModel.setBaseUser(baseUser);
                    videoCMDTextMessageModel.setMessageTips(text);
                    if (textMessage == null) {
                        return;
                    }

                    RongIMutils.sendMessage(Conversation.ConversationType.PRIVATE, toUserId + "", textMessage, "", "", new SendMessageCallBack() {
                        @Override
                        public void sendMessageCallBack(boolean isSuccess) {

                            EventBus.getDefault().post(new VideoCMDTextMessageEvent(videoCMDTextMessageModel));
                            if (!isSuccess) {
                                Utils.showToastShortTime("消息发送失败");
                            }
                        }
                    });

                }
            }
        });

    }

    /**
     * 封装一对一发送消息实体
     *
     * @param roomNumber  房间号
     * @param baseUser    用户对象
     * @param messageTips 文字提示
     * @param messageType 类型
     * @return
     */
    private static YouBanLiveVideoMessage getOneToOneTextMessage(String roomNumber, BaseUser baseUser, String messageTips, int messageType, int type) {

        YouBanLiveVideoMessage textMessage = null;
        Map<String, Object> map = null;
        try {
            map = new ArrayMap<>();
            if (baseUser != null) {
                map.put("baseUser", baseUser);
            }
            if (!TextUtils.isEmpty(roomNumber)) {
                map.put("roomNumber", roomNumber);
            }

            if (!StringUtils.isEmpty(messageTips)) {
                map.put("messageTips", messageTips);
            }
            if (messageType != 0) {
                map.put("messageType", messageType);
            }
            if (type != 0) {
                map.put("type", type);
            }
            //addTime 时间
            if (messageType == 50001) {
                map.put("addTime", String.valueOf(System.currentTimeMillis()));
            } else {
                map.put("addTime", DateUtils.getTime(new Date()));
            }

            String messageText = getMapper().writeValueAsString(map);
            textMessage = new YouBanLiveVideoMessage();
            textMessage.setExtra(messageText);
            textMessage.setContent(messageText);
            map.clear();
            return textMessage;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 封装多人发送消息实体
     *
     * @param roomNumber  房间号
     * @param baseUser    用户对象
     * @param messageTips 文字提示
     * @param messageType 消息类型
     * @param type        分类
     * @return
     */
    private static YouBanLivesRoomVideoMessage getLivesRoomTextMessage(String roomNumber, BaseUser baseUser, String messageTips, int messageType, int type) {

        YouBanLivesRoomVideoMessage textMessage = null;
        Map<String, Object> map = null;
        try {
            map = new ArrayMap<>();
            if (!StringUtils.isEmpty(messageTips)) {
                map.put("messageTips", messageTips);
            }
            if (messageType != 0) {
                map.put("messageType", messageType);
            }
            if (baseUser != null) {
                map.put("baseUser", baseUser);
            }
            if (!TextUtils.isEmpty(roomNumber)) {
                map.put("roomNumber", roomNumber);
            }
            //addTime 时间
            map.put("addTime", DateUtils.getTime(new Date()));
            if (type != 0) {
                map.put("type", type);
            }
            String messageText = getMapper().writeValueAsString(map);
            textMessage = new YouBanLivesRoomVideoMessage();
            textMessage.setExtra(messageText);
            textMessage.setContent(messageText);
            map.clear();
            return textMessage;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送本地接听电话状态给对方
     *
     * @param toUserId    对方id
     * @param messageType 50006：接听电话，50007：挂断电话
     */
    public static void sendPhoneState2OtherUser(final long toUserId, final int messageType) {

        long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        GreenDaoUtils.getInstance().getBaseUser(myUserId, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(Object object) {
                if (object instanceof BaseUser) {
                    BaseUser baseUser = (BaseUser) object;
                    String messageTips = null;
                    if (messageType == 50006) {
                        messageTips = "接听电话";
                    } else {
                        messageTips = "挂断电话";
                    }
                    //发送手机状态
                    YouBanLiveVideoMessage textMessage = getOneToOneTextMessage("", baseUser, messageTips, messageType, 0);

                    if (textMessage == null) {
                        return;
                    }
                    RongIMutils.sendMessage(Conversation.ConversationType.PRIVATE, toUserId + "", textMessage, "", "", new SendMessageCallBack() {
                        @Override
                        public void sendMessageCallBack(boolean isSuccess) {

                        }
                    });

                }
            }
        });
    }


    /**
     * 多人视频发送消息
     *
     * @param roomId      房间号
     * @param messageType 类型
     * @param text        文本消息
     * @param type        具体分类
     */
    public static void sendGroupVideoMessage(final long roomId, final int messageType, final String text, final int type) {

        long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        GreenDaoUtils.getInstance().getBaseUser(myUserId, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(Object object) {
                if (object instanceof BaseUser) {
                    final BaseUser baseUser = (BaseUser) object;
                    YouBanLivesRoomVideoMessage textMessage = getLivesRoomTextMessage(null, baseUser, text, messageType, type);
                    if (textMessage == null) {
                        return;
                    }
                    RongIMutils.sendMessage(Conversation.ConversationType.CHATROOM, roomId + "", textMessage, "", "", new SendMessageCallBack() {
                        @Override
                        public void sendMessageCallBack(boolean isSuccess) {
                            Logger.d("===sendGroupVideoMessage===isSuccess===" + isSuccess + "======" + roomId);
                            if (isSuccess) {
                                switch (messageType) {
                                    case 60001://发送文本消息
                                        final VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                                        videoCMDTextMessageModel.setBaseUser(baseUser);
                                        videoCMDTextMessageModel.setMessageType(messageType);
                                        if (!TextUtils.isEmpty(text)) {
                                            videoCMDTextMessageModel.setMessageTips(text);
                                        }
                                        EventBus.getDefault().post(new VideoCMDTextMessageEvent(videoCMDTextMessageModel));
                                        break;
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * 融云头像长按，点击和长按回调都会执行，为解决此bug添加
     */
    private static boolean isLongClick;

    /**
     * 设置会话界面操作的监听器
     */
    public static void setConversationBehaviorListener() {
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                if (!isLongClick) {
                    if (conversationType == Conversation.ConversationType.CHATROOM) {
                        EventBus.getDefault().post(new UserPortraitClickEvent(false, userInfo));
                        return true;
                    } else if (isCanOnUserPortraitClick) {
                        try {
                            long uId = Long.valueOf(userInfo.getUserId());
                            long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                            if (userId != uId) {
                                BaseUser baseUser = new BaseUser();
                                baseUser.setUserId(uId);
                                baseUser.setUserName(userInfo.getName());
                                baseUser.setNickName(userInfo.getName());
                                baseUser.setUserIcon(userInfo.getPortraitUri().toString());
                                Intent intent = new Intent(context, UserDetailsActivity.class);
                                intent.putExtra(Constants.KEY.KEY_OBJECT, baseUser);
                                context.startActivity(intent);
                            }
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                isLongClick = false;
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                if (conversationType == Conversation.ConversationType.CHATROOM) {
                    isLongClick = true;
                    EventBus.getDefault().post(new UserPortraitClickEvent(true, userInfo));
                    return true;
                }
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                //收到私信匿名图片消息
//                if (message.getConversationType() == Conversation.ConversationType.PRIVATE && message.getSenderUserId().startsWith("NM") && message.getContent() instanceof ImageMessage) {
//                   ImageMessage imageMessage = (ImageMessage) message.getContent();
//                   imageMessage.getMediaUrl();
//
//                }
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return true;

            }
        });
//        if (RongContext.getInstance() != null) {
//            RongContext.getInstance().setConversationClickListener(new RongIM.ConversationClickListener() {
//                @Override
//                public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
//                    if (conversationType == Conversation.ConversationType.CHATROOM) {
//                        EventBus.getDefault().post(new UserPortraitClickEvent(false, userInfo));
//                        return true;
//                    } else if (isCanOnUserPortraitClick) {
//                        try {
//                            long uId = Long.valueOf(userInfo.getUserId());
//                            long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                            if (userId != uId) {
//                                BaseUser baseUser = new BaseUser();
//                                baseUser.setUserId(uId);
//                                baseUser.setUserName(userInfo.getName());
//                                baseUser.setNickName(userInfo.getName());
//                                baseUser.setUserIcon(userInfo.getPortraitUri().toString());
//                                Intent intent = new Intent(context, UserDetailsActivity.class);
//                                intent.putExtra(Constants.KEY.KEY_OBJECT, baseUser);
//                                context.startActivity(intent);
//                            }
//                            return true;
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    return false;
//                }
//
//                @Override
//                public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
//                    if (conversationType == Conversation.ConversationType.CHATROOM) {
//                        EventBus.getDefault().post(new UserPortraitClickEvent(true, userInfo));
//                        return true;
//                    }
//                    return false;
//                }
//
//                @Override
//                public boolean onMessageClick(Context context, View view, Message message) {
//                    return false;
//                }
//
//                @Override
//                public boolean onMessageLinkClick(Context context, String s, Message message) {
//                    return false;
//                }
//
//                @Override
//                public boolean onMessageLongClick(Context context, View view, Message message) {
//                    return false;
//                }
//            });
//        }
    }

    /**
     * 会话列表界面操作的监听器
     */
    public static void setConversationListBehaviorListener() {
        RongIM.setConversationListBehaviorListener(new RongIM.ConversationListBehaviorListener() {
            @Override
            public boolean onConversationPortraitClick(final Context context, Conversation.ConversationType conversationType, String s) {

                Logger.d("s === " + s);

                if (isCanOnUserPortraitClick) {

                    try {

                        if (Conversation.ConversationType.PRIVATE == conversationType) {
                            if (TextUtils.isDigitsOnly(s)) {
                                long uId = Long.valueOf(s);
                                BaseUser baseUser = new BaseUser();
                                baseUser.setUserId(uId);
                                Intent intent = new Intent(context, UserDetailsActivity.class);
                                intent.putExtra(Constants.KEY.KEY_OBJECT, baseUser);
                                context.startActivity(intent);
                            }

                        } else if (Conversation.ConversationType.GROUP == conversationType) {

                            Intent intent = new Intent(context, GroupDetailActivity.class);
                            intent.putExtra(Constants.KEY.KEY_OBJECT, s);
                            context.startActivity(intent);

                        }
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                return false;
            }

            @Override
            public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
                return false;
            }

            @Override
            public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
                return false;
            }

            @Override
            public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
                return false;
            }
        });
    }


    public static void setOtherListener() {

        setOnReceiveUnreadCountChangedListener();

        //设置连接状态监听器
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {

                switch (connectionStatus) {
                    case CONNECTED://连接成功。
                        Logger.d("=======setOtherListener=======连接成功" + connectionStatus);
                        break;
                    case DISCONNECTED://断开连接。
                        Logger.d("=======setOtherListener=======断开连接" + connectionStatus);
                        break;
                    case CONNECTING://连接中。
                        Logger.d("=======setOtherListener=======连接中" + connectionStatus);
                        break;
                    case NETWORK_UNAVAILABLE://网络不可用。
                        Logger.d("=======setOtherListener=======网络不可用" + connectionStatus);
                        break;
                    case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                        Logger.d("=======setConnectionStatusListener=======用户账户在其他设备登录，本机会被踢掉线" + connectionStatus);

                        // 注销登录(不再接收 Push 消息)
                        logoutApp();
                        // 通知主进程
                        Intent msgIntent = new Intent(Constants.KEY.ACTION_SINGLE_POINT);
                        msgIntent.putExtra(Constants.Action.ACTION_EXTRA, "当前账户在其他设备登录，本机会被踢掉线");
                        App.getInstance().sendBroadcast(msgIntent);

                        break;
                }

            }
        });


        // 设置接收消息入口
        RongIM.setOnReceiveMessageListener(new AppOnReceiveMessageListene());
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {
                //匿名私聊
                if (message.getTargetId().startsWith("NM") && message.getContent() instanceof ImageMessage) {
                    if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {
                        EventBus.getDefault().post(new AnonymousSendImageMessageEvent(message.getContent()));
                        return null;
                    } else if (message.getConversationType() == Conversation.ConversationType.CHATROOM) {
                        if (Constants.Object.CHATINFO != null) {
                            ChatBaseUser toBaseUser = Constants.Object.CHATINFO.getToBaseUser();
                            UserInfo userInfo = new UserInfo("NM" + toBaseUser.getUserId(), toBaseUser.getNickName(), Uri.parse(toBaseUser.getUserIcon()));
                            message.getContent().setUserInfo(userInfo);
                        }
                    }
                }
                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                return false;
            }
        });
    }

    /**
     * 接收未读消息的监听器
     */
    public static void setOnReceiveUnreadCountChangedListener() {
//        RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new RongIM.OnReceiveUnreadCountChangedListener() {
//            @Override
//            public void onMessageIncreased(int i) {
//                EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());
//            }
//        }, Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM);

        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                Logger.d("i ===>>>> " + i);

                EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());

            }
        }, Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM);
    }

    /**
     * 启动单聊界面。
     *
     * @param userId 要与之聊天的用户 Id。
     * @param title  聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
     */
    public static void startToConversation(Context mContext, String userId, String title) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().startPrivateChat(mContext, userId, title);
        }
    }

    /**
     * 调起群聊页面
     *
     * @param context
     * @param targetGroupId
     * @param title
     */
    public static void startGroupChat(Context context, String targetGroupId, String title) {
        setMentionedInputListener();
        RongIM.getInstance().startGroupChat(context, targetGroupId, title);
    }

    /**
     * 调起聊天室页面
     *
     * @param context
     * @param roomId
     */
    private static void startChatRoomChat(Context context, String roomId) {
        RongIM.getInstance().startChatRoomChat(context, roomId, true);
    }

    /**
     * 调用@UI
     */
    public static void setMentionedInputListener() {
        RongMentionManager.getInstance().setMentionedInputListener(new IMentionedInputListener() {
            @Override
            public boolean onMentionedInput(Conversation.ConversationType conversationType, String s) {
                Intent intent = new Intent(App.getInstance(), GroupAtUserActivity.class);
                intent.putExtra(Constants.Fields.GROUP_ID, Long.valueOf(s));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getInstance().startActivity(intent);
                return true;
            }
        });
    }

    /**
     * 发送消息
     *
     * @param message     消息实体
     * @param pushContent 推送提示语
     * @param pushData    推送数据
     */
    public static void sendMessage(Message message, String pushContent, String pushData, final SendMessageCallBack sendMessageCallBack) {
        RongIM.getInstance().sendMessage(message, pushContent, pushData, new IRongCallback.ISendMessageCallback() {

            @Override
            public void onAttached(Message message) {
                Logger.d("onAttached() 执行");
            }

            @Override
            public void onSuccess(Message message) {
                Logger.d("onSuccess() 执行");
                if (sendMessageCallBack != null) {
                    sendMessageCallBack.sendMessageCallBack(true);
                }
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Logger.d("onError() 执行 getValue()  " + errorCode.getValue() + " <======getMessage()====> " + errorCode.getMessage());
                if (sendMessageCallBack != null) {
                    sendMessageCallBack.sendMessageCallBack(false);
                }
            }
        });
    }


    /**
     * 发送消息
     *
     * @param type                聊天类型
     * @param targetId            目标id（单聊 targetId = userId；群聊 targetId = groupId
     * @param content             消息类型
     * @param pushContent         推送内容
     * @param pushData            推送数据
     * @param sendMessageCallBack 消息回调
     */
    public static void sendMessage(Conversation.ConversationType type, String targetId, MessageContent content, String pushContent, String pushData, final SendMessageCallBack sendMessageCallBack) {

        RongIMClient.getInstance().sendMessage(type, targetId, content, pushContent, pushData,
                new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Logger.d("onSuccess() 执行");
                        if (sendMessageCallBack != null) {
                            sendMessageCallBack.sendMessageCallBack(true);
                        }
                    }

                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        Logger.e("onError() 执行 getValue()  " + errorCode.getValue() + " <======getMessage()====> " + errorCode.getMessage());
                        if (sendMessageCallBack != null) {
                            sendMessageCallBack.sendMessageCallBack(false);
                        }
                    }

                });

    }


    /**
     * 免消息打扰
     *
     * @param conversationType   聊天类型
     * @param targetId           目标id（群id，讨论组id）
     * @param notificationStatus 通知状态
     */
    public static void setConversationNotificationStatus(Conversation.ConversationType conversationType, final String targetId, final Conversation.ConversationNotificationStatus notificationStatus) {
        RongIM.getInstance().setConversationNotificationStatus(conversationType, targetId, notificationStatus, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                Logger.d(conversationNotificationStatus.getValue() == 0 ? "免消息打扰关闭成功" : "免消息打扰开启成功");

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.d("操作失败 getValue() === " + errorCode.getValue());
            }
        });
    }


    /**
     * 未读消息
     *
     * @param countCallback
     */
    public static void getUnreadCount(final RongYunUnreadCountCallback countCallback) {
        try {
            RongIM.getInstance().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                @Override
                public void onSuccess(Integer object) {
                    if (countCallback != null) {
                        countCallback.onSuccess(object);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    if (countCallback != null) {
                        countCallback.onError();
                    }
                }
            }, Conversation.ConversationType.SYSTEM, Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 注销登录
     */
    public static void logoutApp() {
        if (RongIM.getInstance() == null) {
            return;
        }
        isCanOnUserPortraitClick = true;
        // 注销登录(不再接收 Push 消息)
        RongIM.getInstance().logout();

    }

    /**
     * 退出app
     */
    public static void exitApp() {
        if (RongIM.getInstance() == null) {
            return;
        }
        // 能接受push消息
        RongIM.getInstance().disconnect();

        isCanOnUserPortraitClick = true;

    }


    public interface SendMessageCallBack {
        /**
         * 是否成功
         *
         * @param isSuccess true 成功；false 失败
         */
        void sendMessageCallBack(boolean isSuccess);
    }

    /**
     * 接收消息
     */
    private static class AppOnReceiveMessageListene implements RongIMClient.OnReceiveMessageListener {

        @Override
        public boolean onReceived(final Message message, int i) {

            if (message != null) {
                Conversation.ConversationType conversationType = message.getConversationType();
                Logger.d("message=========================== " + message);
                Logger.d("conversationType.getName() =聊天类型== " + conversationType.getName() + " ===== getObjectName() ==== " + message.getObjectName());

                MessageContent content = message.getContent();
                Logger.d("conversationType.getName() =content== " + content.toString());

                //TODO 兼容OPPO手机，OPPO手机没回调融云的未读消息监听器
                try {
                    if ("OPPO".toLowerCase().equals(Build.MANUFACTURER.toLowerCase()) || "HUAWEI".toLowerCase().equals(Build.MANUFACTURER.toLowerCase())) {
                        EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (conversationType == Conversation.ConversationType.SYSTEM) { //TODO 系统消息

                    if (content != null) {

                        if (content instanceof YouBanActivityMessage) { //TODO 活动消息

                            YouBanActivityMessage youBanActivityMessage = (YouBanActivityMessage) content;
                            String extra = youBanActivityMessage.getExtra();
                            if (StringUtils.isEmpty(extra)) {
                                extra = youBanActivityMessage.getContent();
                            }
                            Logger.json(extra);

                            parseReceivedMessage(extra, message.getObjectName());

                            return true;

                        } else if (content instanceof YouBanGroupMessage) {//TODO 群消息

                            YouBanGroupMessage youBanGroupMessage = (YouBanGroupMessage) content;

                            String extra = youBanGroupMessage.getExtra();
                            if (StringUtils.isEmpty(extra)) {
                                extra = youBanGroupMessage.getContent();
                            }
                            Logger.json(extra);

                            parseReceivedMessage(extra, message.getObjectName());

                            return true;


                        } else if (content instanceof YouBanOrganizationMessage) { //TODO 圈子消息

                            YouBanOrganizationMessage youBanOrganizationMessage = (YouBanOrganizationMessage) content;

                            String extra = youBanOrganizationMessage.getExtra();
                            if (StringUtils.isEmpty(extra)) {
                                extra = youBanOrganizationMessage.getContent();
                            }
                            Logger.json(extra);

                            parseReceivedMessage(extra, message.getObjectName());

                            return true;


                        } else if (content instanceof YouBanHelpInfoMessage) { //TODO 帮帮忙消息

                            YouBanHelpInfoMessage youBanHelpInfoMessage = (YouBanHelpInfoMessage) content;

                            String extra = youBanHelpInfoMessage.getExtra();
                            if (StringUtils.isEmpty(extra)) {
                                extra = youBanHelpInfoMessage.getContent();
                            }
                            Logger.json(extra);

                            parseReceivedMessage(extra, message.getObjectName());

                            return true;


                        } else if (content instanceof YouBanSayLoveInfoMessage) {//TODO 表白消息
                            YouBanSayLoveInfoMessage youBanSayLoveInfoMessage = (YouBanSayLoveInfoMessage) content;

                            String extra = youBanSayLoveInfoMessage.getExtra();
                            if (StringUtils.isEmpty(extra)) {
                                extra = youBanSayLoveInfoMessage.getContent();
                            }
                            Logger.json(extra);

                            parseReceivedMessage(extra, message.getObjectName());

                            return true;

                        } else if (content instanceof YouBanLivesRoomVideoMessage) {//TODO 多人视频
                            YouBanLivesRoomVideoMessage youBanLivesRoomVideoMessage = (YouBanLivesRoomVideoMessage) content;
                            final VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                            analysisContentExtra(youBanLivesRoomVideoMessage.getExtra(), videoCMDTextMessageModel);
                            int messageType = videoCMDTextMessageModel.getMessageType();
                            if (messageType == 60013) {//多人视频温馨提示
                                if (handler == null) {
                                    handler = new Handler(App.getInstance().getMainLooper());
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        EventBus.getDefault().post(new VideoCMDTextMessageEvent(videoCMDTextMessageModel));
                                    }
                                }, 2000);
                            }
                            return true;

                        } else if (content instanceof YouBanProductOrder) {//TODO 商品试用消息
                            YouBanProductOrder youBanProductOrder = (YouBanProductOrder) content;
                            String extra = youBanProductOrder.getExtra();
                            if (StringUtils.isEmpty(extra)) {
                                extra = youBanProductOrder.getContent();
                            }
                            Logger.json(extra);

                        } else if (content instanceof YouBanSystemMessage) {//TODO 系统自定义消息
                            YouBanSystemMessage youBanSystemMessage = (YouBanSystemMessage) content;

                            String extra = youBanSystemMessage.getExtra();
                            if (StringUtils.isEmpty(extra)) {
                                extra = youBanSystemMessage.getContent();
                            }
                            Logger.json(extra);

                            try {
                                Map map = JsonUtil.getObjectMapper().readValue(extra, Map.class);
                                if (map != null && !map.isEmpty()) {
                                    int messageType = -1;
                                    Object object = map.get(Constants.Fields.MESSAGE_TYPE);
                                    if (object instanceof Integer) {
                                        messageType = (int) object;
                                    } else if (object instanceof Long) {
                                        messageType = (int) object;
                                    }

                                    if (messageType == 7000) {// sysToken 过期

                                        Map map1 = new LinkedHashMap(1);
                                        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                                        Logger.d("userId ==== " + userId);
                                        map.put(Constants.Fields.USER_ID, userId);

                                        try {
                                            ApiData.getInstance().postData(InterfaceUrl.URL_FINDTOKENBYUSERID, "", map, new JsonCallback<FindTokenByUserIdResponse>(FindTokenByUserIdResponse.class) {

                                                @Override
                                                public void onStart(Request request) {
                                                    super.onStart(request);
                                                }

                                                @Override
                                                public void onSuccess(Response<FindTokenByUserIdResponse> response) {

                                                    if (response.body() instanceof FindTokenByUserIdResponse) {
                                                        FindTokenByUserIdResponse systemTokenResponse = response.body();
                                                        if (systemTokenResponse.getCode() == 200) {
                                                            SafeSharePreferenceUtils.saveString(Constants.KEY.KEY_SYS_TOKEN, systemTokenResponse.getUserToken());
                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onError(Response response) {
                                                    super.onError(response);


                                                }

                                                @Override
                                                public void onFinish() {
                                                    super.onFinish();
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return true;

                        } else if (content instanceof YouBanAttentionMessage) {
                            YouBanAttentionMessage youBanAttentionMessage = (YouBanAttentionMessage) content;
                            final VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                            analysisContentExtra(youBanAttentionMessage.getExtra(), videoCMDTextMessageModel);
                            if (videoCMDTextMessageModel.getMessageType() == 80001) {
                                EventBus.getDefault().post(new AttentionMessageEvent());
                            }

                            return true;
                        } else if (content instanceof YouBanSendActiveInvitationMessage) {
                            YouBanSendActiveInvitationMessage youBanSendActiveInvitationMessage = (YouBanSendActiveInvitationMessage) content;
                            final YouBanSendActiveInvitationMessageModel youBanSendActiveInvitationMessageModel = new YouBanSendActiveInvitationMessageModel();
                        } else if (content instanceof YouBanShopPlaceOrderMessage) {
                            //下单消息
                            final YouBanShopPlaceOrderMessage youBanShopPlaceOrderMessage;
                            try {
                                youBanShopPlaceOrderMessage = (YouBanShopPlaceOrderMessage) content;
                                String contentStr = youBanShopPlaceOrderMessage.getContent();
                                ParseShopPlaceOrderMessage parseShopPlaceOrderMessage = JsonUtil.getObjectMapper().readValue(contentStr, ParseShopPlaceOrderMessage.class);
                                if (parseShopPlaceOrderMessage.getMessageType() == 1001 || parseShopPlaceOrderMessage.getMessageType() == 1003) {
                                    //卖家出现小红点
                                    EventBus.getDefault().post(new ShopPlaceOrderEvent());
                                    //方式1：xml文件存储
//                                    long userId=parseShopPlaceOrderMessage.getUserId();
//                                    long storeId=parseShopPlaceOrderMessage.getProductOrder().getStoreId();
//                                    SafeSharePreferenceUtils.saveLong(userId+"/"+storeId,1);
                                    //方式2：数据库存储（两种方式都可行）
                                    PlaceOrderMessage placeOrderMessage = new PlaceOrderMessage();
                                    placeOrderMessage.setUserIdAndStoreId(parseShopPlaceOrderMessage.getUserId() + "/" + parseShopPlaceOrderMessage.getProductOrder().getStoreId());
                                    GreenDaoUtils.getInstance().insertPlaceOrderMessage(placeOrderMessage);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (conversationType == Conversation.ConversationType.PRIVATE) {//私信
                    if (content instanceof YouBanLiveVideoMessage) {//一对一视频
                        YouBanLiveVideoMessage youBanLiveVideoMessage = (YouBanLiveVideoMessage) content;
                        final VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                        analysisContentExtra(youBanLiveVideoMessage.getExtra(), videoCMDTextMessageModel);
                        Logger.d("onReceived:videoCMDTextMessageModel===== getMessageType" + videoCMDTextMessageModel.getMessageType());
                        switch (videoCMDTextMessageModel.getMessageType()) {
                            case 50001://收到呼叫
                                //防止多方同时发起视频
                                boolean videoStatus = SafeSharePreferenceUtils.getBoolean(Constants.Fields.VIDEO_STATUS, false);
                                if (videoStatus) {
                                    refuseRequestVideo(50011, videoCMDTextMessageModel.getBaseUser().getUserId(),
                                            videoCMDTextMessageModel.getRoomNumber(), true);

//                                    DBUtils.getInstance().saveCallHistory(videoCMDTextMessage, 1);

                                    return true;
                                }
                                SafeSharePreferenceUtils.saveBoolean(Constants.Fields.VIDEO_STATUS, true);
                                break;
                            case 50003://正常拒绝
                                break;
                            case 50011://对方占线被拒绝
                                EventBus.getDefault().post(new VideoStatusEvent(1));
                                return true;
                            case 50004://取消呼叫
                                break;
                            case 50006:
                            case 50007:
                                if (videoCMDTextMessageModel.getType() == 50006) {
                                    videoCMDTextMessageModel.setMessageTips("对方正在打电话");
                                } else {
                                    videoCMDTextMessageModel.setMessageTips("正在恢复视频状态");
                                }

                                EventBus.getDefault().post(new VideoPhoneStateEvent(videoCMDTextMessageModel));
                                return true;
                            case 50008://聊天消息
                                EventBus.getDefault().post(new VideoCMDTextMessageEvent(videoCMDTextMessageModel));
                                return true;

                            case 50009://挂断
                                break;
                            case 50010://礼物
                                if (Utils.isForeground(App.getInstance(), VideoChatActivity.class.getSimpleName())) {
                                    EventBus.getDefault().post(new GiftShowEvent(videoCMDTextMessageModel));
                                }
                                return true;

//                            case 0x08:
//                                // 接收到对方检查自己当前的视频状态
//                                videoStatus = SafeSharePreferenceUtils.getBoolean(Constants.Fields.VIDEO_STATUS, false);
////                                reportMyselfVideoStatus(videoCMDTextMessage.getUserInfo().getUserId(), videoStatus ? 1 : 0);
//                                return true;
//                            // 接收到对方报告当前的视频状态
//                            case 0x09:
//                                EventBus.getDefault().post(new VideoStatusEvent(videoCMDTextMessageModel.getFlag(), 1));
//                                return true;
                        }

                        if (Utils.isForeground(App.getInstance(), VideoChatActivity.class.getSimpleName())) {
                            EventBus.getDefault().post(new VideoEvent(videoCMDTextMessageModel.getMessageType(), videoCMDTextMessageModel.getRoomNumber(), videoCMDTextMessageModel.getBaseUser()));
                            return true;
                        } else {
                            switch (videoCMDTextMessageModel.getMessageType()) {
                                case 50001:
                                    long time = Long.valueOf(videoCMDTextMessageModel.getAddTime());
                                    if (System.currentTimeMillis() - time < 30 * 1000) {//判断是否为当次有效呼叫
                                        Intent intent = new Intent(App.getInstance(), VideoChatActivity.class);
                                        intent.putExtra(Constants.Fields.BASEUSER, videoCMDTextMessageModel.getBaseUser());
                                        intent.putExtra(Constants.Fields.EXTRA, videoCMDTextMessageModel);
                                        intent.putExtra(Constants.Fields.TYPE, 1);
                                        intent.putExtra(Constants.Fields.CODE, 1);
                                        intent.putExtra(Constants.Fields.ROOMNUMBER, videoCMDTextMessageModel.getRoomNumber());
                                        intent.putExtra(Constants.Fields.VIDEO_TYPE, videoCMDTextMessageModel.getType());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        App.getInstance().startActivity(intent);
                                    }
                                    break;
                                case 50004:
                                    if (handler == null) {
                                        handler = new Handler(App.getInstance().getMainLooper());
                                    }

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Utils.isForeground(App.getInstance(), VideoChatActivity.class.getSimpleName())) {
                                                EventBus.getDefault().post(new VideoEvent(50004, videoCMDTextMessageModel.getRoomNumber(), videoCMDTextMessageModel.getBaseUser()));
                                            }
                                        }
                                    }, 500);
                                    break;
                            }
                            return true;
                        }


                    } else if (content instanceof YouBanLivesRoomVideoMessage) {//多人视频(服务器推送)
                        YouBanLivesRoomVideoMessage youBanLivesRoomVideoMessage = (YouBanLivesRoomVideoMessage) content;
                        VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                        Logger.json(youBanLivesRoomVideoMessage.getExtra());
                        analysisContentExtra(youBanLivesRoomVideoMessage.getExtra(), videoCMDTextMessageModel);
                        switch (videoCMDTextMessageModel.getMessageType()) {
                            case 60002://申请上麦
                                //主持人收到申请
                                EventBus.getDefault().post(new VideoGroupMicApplyEvent(1, videoCMDTextMessageModel.getBaseUser()));
                                break;
                            case 60003://通知上麦
                                EventBus.getDefault().post(new VideoGroupMicApplyEvent(2, videoCMDTextMessageModel.getBaseUser()));
                                break;
                            case 60004://踢出房间
                                EventBus.getDefault().post(new VideoGroupManageEvent(60004));
                                break;
                            case 60005://踢人下麦
                                EventBus.getDefault().post(new VideoGroupMicApplyEvent(3, videoCMDTextMessageModel.getBaseUser()));
                                break;
                            case 60008://收到移交主持请求(1发送申请2同意3拒绝移交主持人权限),
                                EventBus.getDefault().post(new VideoGroupTransferHostingEvent(videoCMDTextMessageModel.getType(), videoCMDTextMessageModel.getBaseUser()));
                                break;
                            case 60009://禁言--发送给被操作的用户
                                EventBus.getDefault().post(new VideoGroupManageEvent(60009, videoCMDTextMessageModel.getType()));
                                break;
                            default:
                        }

                    } else if (content instanceof YouBanAnonymousMessage) {
                        YouBanAnonymousMessage youBanAnonymousMessage = (YouBanAnonymousMessage) content;
                        VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                        analysisContentExtra(youBanAnonymousMessage.getExtra(), videoCMDTextMessageModel);
                        //匿名被踢出聊天房
                        if (videoCMDTextMessageModel.getMessageType() == 9001) {
                            EventBus.getDefault().post(new AnonymousChatRoomStatusEvent(false, videoCMDTextMessageModel.getMessageType(), videoCMDTextMessageModel.getMessageTips()));
                        } else if (videoCMDTextMessageModel.getMessageType() == 9003) {//禁言
                            EventBus.getDefault().post(new AnonymousChatRoomManageEvent(videoCMDTextMessageModel.getMessageType(), videoCMDTextMessageModel.getType()));
                        }
                    }
                } else if (conversationType == Conversation.ConversationType.GROUP) {//群

                } else if (conversationType == Conversation.ConversationType.CHATROOM) {
                    if (content instanceof YouBanLivesRoomVideoMessage) {
                        YouBanLivesRoomVideoMessage youBanLivesRoomVideoMessage = (YouBanLivesRoomVideoMessage) content;
                        VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                        analysisContentExtra(youBanLivesRoomVideoMessage.getExtra(), videoCMDTextMessageModel);
                        switch (videoCMDTextMessageModel.getMessageType()) {
                            case 60001://聊天消息
                            case 60006://刷新麦序
                            case 60007://关闭房间
                            case 60008://移交主持人刷新
                            case 60010://送礼物
                            case 60011://投票
                            case 60012://关注
                            case 60014://进入房间
                                Logger.d("CHATROOM============" + videoCMDTextMessageModel.getMessageType());
                                EventBus.getDefault().post(new VideoCMDTextMessageEvent(videoCMDTextMessageModel));
                                break;
                            case 60009://禁言----发送到公屏消息
                                EventBus.getDefault().post(new VideoCMDTextMessageEvent(videoCMDTextMessageModel));
                                break;
                        }
                    } else if (content instanceof YouBanAnonymousMessage) {
                        YouBanAnonymousMessage youBanAnonymousMessage = (YouBanAnonymousMessage) content;
                        VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                        analysisContentExtra(youBanAnonymousMessage.getExtra(), videoCMDTextMessageModel);
                        //聊天室被解散
                        if (videoCMDTextMessageModel.getMessageType() == 9002) {
                            EventBus.getDefault().post(new AnonymousChatRoomStatusEvent(false, videoCMDTextMessageModel.getMessageType(), videoCMDTextMessageModel.getMessageTips()));
                        }
                    } else if (content instanceof TextMessage) {
                        message.setSenderUserId(message.getContent().getUserInfo().getUserId());
                    } else if (content instanceof ImageMessage) {
                        message.setSenderUserId(message.getContent().getUserInfo().getUserId());
                    } else if (content instanceof VoiceMessage) {
                        message.setSenderUserId(message.getContent().getUserInfo().getUserId());
                    }
                }
            }


            return false;

        }
    }


    /**
     * 解析接收的消息并且存入数据库
     *
     * @param extra
     * @param objectName
     */
    private static void parseReceivedMessage(String extra, String objectName) {
        if (!StringUtils.isEmpty(extra)) {
            try {
                Map map = JsonUtil.getObjectMapper().readValue(extra, Map.class);
                if (map != null && !map.isEmpty()) {
                    int messageType = -1;
                    long groupId = -1;
                    Object object = map.get(Constants.Fields.MESSAGE_TYPE);
                    Object groupIdObject = map.get(Constants.Fields.GROUP_ID);

                    if (object instanceof Integer) {
                        messageType = (int) object;
                    } else if (object instanceof Long) {
                        messageType = (int) object;
                    }


                    switch (messageType) { // 需要拦截的消息

                        case 30006://TODO  群昵称更新通知

                            String groupName = "";
                            Object object2 = map.get(Constants.Fields.GROUP);
                            if (object2 instanceof Map) {
                                //group对象
                                Map map2 = (Map) map.get(Constants.Fields.GROUP);
                                Object groupNameObject = map2.get(Constants.Fields.GROUP_NAME);
                                if (groupNameObject instanceof String) {
                                    groupName = (String) groupNameObject;
                                }

                                groupIdObject = map2.get(Constants.Fields.GROUP_ID);

                                if (groupIdObject instanceof Integer) {
                                    groupId = (int) groupIdObject;
                                } else if (groupIdObject instanceof Long) {
                                    groupId = (int) groupIdObject;
                                }

                                String groupImg = "";
                                Object groupImgObject = map2.get(Constants.Fields.GROUP_IMG);
                                if (groupImgObject instanceof String) {
                                    groupImg = (String) groupImgObject;
                                }

                                Logger.d("==群昵称更新通知==groupId== " + groupId + "   ==== groupName === " + groupName);

                                // 缓存群组信息
                                RongIM.getInstance().refreshGroupInfoCache(new Group(groupId + "", groupName, Uri.parse(groupImg)));

                            }

                            if (StringUtils.isEmpty(groupName)) {
                                return;
                            }

                            EventBus.getDefault().post(new GroupOperationEvent(3, groupName));

                            return;

                        case 30007://TODO  解散群消息 再次处理业务


                            if (groupIdObject instanceof Integer) {
                                groupId = (int) groupIdObject;
                            } else if (groupIdObject instanceof Long) {
                                groupId = (int) groupIdObject;
                            }

                            if (groupId < 1) {
                                Logger.d("groupId is < 1");
                                return;
                            }

                            Logger.d("groupId  ===解散群消息 再次处理业务===== " + groupId);

                            // 删除此群在聊天列表的item
                            final long finalGroupId = groupId;
                            RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupId + "", new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    Logger.d("aBoolean === " + aBoolean);
                                    EventBus.getDefault().post(new GroupOperationEvent(1, finalGroupId));
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    Logger.d("errorCode === " + errorCode.getMessage() + " ; errorValue === " + errorCode.getValue());
                                }
                            });
                            return;
                    }

                    PushdbModel pushBbModel = new PushdbModel();
                    pushBbModel.setMessageType(messageType);
                    pushBbModel.setSource(extra);
                    pushBbModel.setObjectName(objectName);
                    //消息插入数据库
                    GreenDaoUtils.getInstance().insertPushdbModel(pushBbModel);

                    map.clear();
                    map = null;


                    /**
                     * 通知未读消息
                     */
                    EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析扩展字段数据
     *
     * @param contentExtra
     * @param videoCMDTextMessage
     */
    public static void analysisContentExtra(String contentExtra, VideoCMDTextMessageModel videoCMDTextMessage) {
        try {

            if (StringUtils.isEmpty(contentExtra)) {
                return;
            }

            Map<String, Object> map = JsonUtil.getObjectMapper().readValue(contentExtra, HashMap.class);
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Logger.d("====key=====" + entry.getKey() + "=====value=====" + entry.getValue());

                    if ("messageTips".equals(entry.getKey()) && entry.getValue() != null) {
                        videoCMDTextMessage.setMessageTips((String) entry.getValue());
                    }

                    if ("messageType".equals(entry.getKey()) && entry.getValue() != null) {
                        videoCMDTextMessage.setMessageType((Integer) entry.getValue());
                    }

                    if ("baseUser".equals(entry.getKey()) && entry.getValue() != null && entry.getValue() instanceof LinkedHashMap) {
                        LinkedHashMap linkedHashMap = (LinkedHashMap) entry.getValue();
                        String userJson = getMapper().writeValueAsString(linkedHashMap);
                        BaseUser user = getMapper().readValue(userJson, BaseUser.class);
                        videoCMDTextMessage.setBaseUser(user);
                    }

                    if ("toUserInfo".equals(entry.getKey()) && entry.getValue() != null && entry.getValue() instanceof LinkedHashMap) {
                        LinkedHashMap linkedHashMap = (LinkedHashMap) entry.getValue();
                        String userJson = getMapper().writeValueAsString(linkedHashMap);
                        BaseUser user = getMapper().readValue(userJson, BaseUser.class);
                        videoCMDTextMessage.setToUserInfo(user);
                    }

                    if ("props".equals(entry.getKey()) && entry.getValue() != null && entry.getValue() instanceof LinkedHashMap) {
                        LinkedHashMap linkedHashMap = (LinkedHashMap) entry.getValue();
                        String propsJson = getMapper().writeValueAsString(linkedHashMap);
                        Props props = getMapper().readValue(propsJson, Props.class);
                        videoCMDTextMessage.setProps(props);
                    }

                    if ("roomNumber".equals(entry.getKey())) {
                        if (entry.getValue() instanceof String) {
                            videoCMDTextMessage.setRoomNumber((String) entry.getValue());
                        }
                    }

                    if ("addTime".equals(entry.getKey()) && entry.getValue() != null) {
                        videoCMDTextMessage.setAddTime((String) entry.getValue());
                    }
                    if ("type".equals(entry.getKey()) && entry.getValue() != null) {
                        videoCMDTextMessage.setType((Integer) entry.getValue());
                    }
//                    if ("propsId".equals(entry.getKey())) {
//                        videoCMDTextMessage.setPropsId(Long.valueOf(String.valueOf(entry.getValue())).longValue());
//                    }

//                    if ("propsNum".equals(entry.getKey())) {
//                        if (entry.getValue() instanceof Integer) {
//                            videoCMDTextMessage.setPropsNum((Integer) entry.getValue());
//                        }
//                    }

//                    if ("list".equals(entry.getKey()) && entry.getValue() != null && entry.getValue() instanceof List) {
//                        videoCMDTextMessage.setList((List<String>) entry.getValue());
//                    }
//
//
//
//                    if ("propsUrl".equals(entry.getKey())) {
//                        if (entry.getValue() instanceof String) {
//                            videoCMDTextMessage.setPropsUrl((String) entry.getValue());
//                        }
//                    }
//
//                    if ("goldNum".equals(entry.getKey())) {
//                        if (entry.getValue() instanceof Integer) {
//                            videoCMDTextMessage.setGoldNum((Integer) entry.getValue());
//                        }
//                    }


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入聊天室
     *
     * @param roomId       房间号
     * @param isGroupVideo 是否为多人视频
     */
    public static void joinChatRoom(final Context context, final String roomId, final boolean isGroupVideo) {
        RongIM.getInstance().joinChatRoom(roomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Logger.d("=======joinChatRoom========" + roomId);
                //进入频道广播
                if (isGroupVideo) {
                    RongIMutils.sendGroupVideoMessage(Long.valueOf(roomId), 60014, null, 0);
                } else {
                    startChatRoomChat(context, roomId);
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    /**
     * 退出聊天室
     *
     * @param roomId 房间号
     */
    public static void quitChatRoom(String roomId) {
        RongIM.getInstance().quitChatRoom(roomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }


    /**
     * 弹出通知
     *
     * @param msgType    消息类型 用于扩展
     * @param contentStr 提示消息
     * @param context
     */
    public static void showNotification(int msgType, String contentStr, final Context context) {
        if (context == null) {
            return;
        }
        //默认系统声音震动
        int defaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;
        if (!SafeSharePreferenceUtils.getBoolean(Constants.Fields.MSG_VOICE, true)) {
            defaults = Notification.DEFAULT_VIBRATE;//关闭声音
        }
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentText(contentStr)
                .setContentTitle("友伴")
                .setTicker("您有一条新消息")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(defaults)
                .setSmallIcon(R.mipmap.ic_launcher);


        Notification notify = mBuilder.build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(msgType, notify);
    }


    /**
     * 分享发送一对一消息
     *
     * @param messageType  消息类型
     *                     30005 群分享,30006 发送活动邀请函,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白
     * @param selectObject 选择对象
     * @param sharedObject 分享对象
     */
    public static void sendSharedMessage(String pushContent, int messageType, Object selectObject, Object sharedObject) {

        switch (messageType) {
            case 30005:

                YouBanGroupSharedMessage youBanGroupSharedMessage = new YouBanGroupSharedMessage();
                Map map = new ArrayMap(1);
                if (sharedObject instanceof cn.bjhdltcdn.p2plive.model.Group) {
                    cn.bjhdltcdn.p2plive.model.Group group = (cn.bjhdltcdn.p2plive.model.Group) sharedObject;
                    map.put(Constants.Fields.GROUP_ID, group.getGroupId());
                    map.put(Constants.Fields.NUMBER, group.getNumber());
                    map.put(Constants.Fields.GROUP_NAME, group.getGroupName());
                    map.put(Constants.Fields.GROUP_IMG, group.getGroupImg());
                    map.put(Constants.Fields.MESSAGE_TYPE, messageType);
                    map.put(Constants.Fields.IS_EXIST_GROUP, group.getIsExistGroup());
                    map.put(Constants.Fields.GROUP_MODE, group.getGroupMode());

                    try {
                        String requestBody = JsonUtil.getObjectMapper().writeValueAsString(map);

                        Logger.json(requestBody);

                        youBanGroupSharedMessage.setExtra(requestBody);
                        youBanGroupSharedMessage.setContent(requestBody);

                        Message message = new Message();
                        message.setContent(youBanGroupSharedMessage);
                        message.setObjectName("RC:YouBanGroupShared");
                        if (TextUtils.isEmpty(pushContent)) {
                            sendMessage("分享群名片", message, selectObject);
                        } else {
                            sendMessage(pushContent, message, selectObject);

                        }


                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                }


                break;
            case 40004:

                YouBanSendActiveInvitationMessage youBanSendActiveInvitationMessage = new YouBanSendActiveInvitationMessage();
                Map maps = new ArrayMap(1);
                if (sharedObject instanceof ActivityInfo) {
                    ActivityInfo activityInfo = (ActivityInfo) sharedObject;
                    BaseUser baseUser = activityInfo.getBaseUser();
                    if (baseUser == null) {
                        break;
                    }
                    maps.put(Constants.Fields.BASEUSER, baseUser);
                    maps.put(Constants.Fields.ACTIVITY_ID, activityInfo.getActivityId());
                    maps.put(Constants.Fields.MESSAGE_TYPE, messageType);

                    try {
                        String requestBody = JsonUtil.getObjectMapper().writeValueAsString(maps);

                        Logger.json(requestBody);

                        youBanSendActiveInvitationMessage.setExtra(requestBody);
                        youBanSendActiveInvitationMessage.setContent(requestBody);

                        Message message = new Message();
                        message.setContent(youBanSendActiveInvitationMessage);
                        message.setObjectName("RC:YouBanSendActiveInvitationMessage");

                        sendMessage("活动邀请函", message, selectObject);


                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                }


                break;


            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 18:

                YouBanSharedMessage youBanSharedMessage = new YouBanSharedMessage();
                String requestBody = null;
                map = new ArrayMap(1);
                map.put(Constants.Fields.MESSAGE_TYPE, messageType);


                if (sharedObject instanceof PostInfo) {

                    PostInfo postInfo = (PostInfo) sharedObject;
                    try {
                        String sharedObjectJson = JsonUtil.getObjectMapper().writeValueAsString(postInfo);
                        map.put(Constants.Fields.SHARED_OBJECT, sharedObjectJson);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                } else if (sharedObject instanceof OrganizationInfo) {

                    OrganizationInfo organizationInfo = (OrganizationInfo) sharedObject;
                    try {
                        String sharedObjectJson = JsonUtil.getObjectMapper().writeValueAsString(organizationInfo);
                        map.put(Constants.Fields.SHARED_OBJECT, sharedObjectJson);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                } else if (sharedObject instanceof ActivityInfo) {

                    ActivityInfo activityInfo = (ActivityInfo) sharedObject;
                    try {
                        String sharedObjectJson = JsonUtil.getObjectMapper().writeValueAsString(activityInfo);
                        map.put(Constants.Fields.SHARED_OBJECT, sharedObjectJson);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                } else if (sharedObject instanceof RoomInfo) {

                    RoomInfo roomInfo = (RoomInfo) sharedObject;
                    try {
                        String sharedObjectJson = JsonUtil.getObjectMapper().writeValueAsString(roomInfo);
                        map.put(Constants.Fields.SHARED_OBJECT, sharedObjectJson);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                } else if (sharedObject instanceof PlayInfo) {

                    try {
                        PlayInfo playInfo = (PlayInfo) sharedObject;
                        String sharedObjectJson = JsonUtil.getObjectMapper().writeValueAsString(playInfo);
                        map.put(Constants.Fields.SHARED_OBJECT, sharedObjectJson);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }


                } else if (sharedObject instanceof SayLoveInfo) {

                    SayLoveInfo sayLoveInfo = (SayLoveInfo) sharedObject;
                    try {
                        String sharedObjectJson = JsonUtil.getObjectMapper().writeValueAsString(sayLoveInfo);
                        map.put(Constants.Fields.SHARED_OBJECT, sharedObjectJson);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }


                }

                try {
                    requestBody = JsonUtil.getObjectMapper().writeValueAsString(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Logger.json(requestBody);


                if (StringUtils.isEmpty(requestBody)) {
                    Logger.d("requestBody is null");
                    return;
                }

                youBanSharedMessage.setExtra(requestBody);
                youBanSharedMessage.setContent(requestBody);

                Message message = new Message();
                message.setContent(youBanSharedMessage);
                message.setObjectName("RC:YouBanSharedMessage");

                sendMessage(pushContent, message, selectObject);

                // 一对一分享计数业务
                EventBus.getDefault().post(new ShareSuccessEvent());

                break;

        }

    }

    /**
     * 发送给融云
     *
     * @param pushContent  提示文字
     * @param message      消息体
     * @param selectObject 选择对象
     */
    private static void sendMessage(final String pushContent, Message message, final Object selectObject) {
        if (selectObject instanceof AddressBook) {// 选择联系人
            AddressBook addressBook = (AddressBook) selectObject;
            message.setConversationType(Conversation.ConversationType.PRIVATE);
            message.setTargetId(addressBook.getUserId() + "");
        } else if (selectObject instanceof BaseUser) {
            BaseUser baseUser = (BaseUser) selectObject;
            message.setConversationType(Conversation.ConversationType.PRIVATE);
            message.setTargetId(baseUser.getUserId() + "");
        } else {
            cn.bjhdltcdn.p2plive.model.Group group = (cn.bjhdltcdn.p2plive.model.Group) selectObject;
            message.setConversationType(Conversation.ConversationType.GROUP);
            message.setTargetId(group.getGroupId() + "");
        }

        message.setMessageDirection(Message.MessageDirection.SEND);
        message.setSenderUserId(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) + "");
        message.setSentStatus(Message.SentStatus.SENT);

        RongIMutils.sendMessage(message, pushContent, pushContent, new RongIMutils.SendMessageCallBack() {
            @Override
            public void sendMessageCallBack(boolean isSuccess) {
                if (!(selectObject instanceof BaseUser)) {
                    Utils.showToastShortTime(isSuccess ? (pushContent + "成功") : (pushContent + "失败"));
                }
            }
        });

    }
}
