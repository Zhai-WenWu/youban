package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.SocketTimeoutException;
import java.util.Locale;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.AnonymousChatRoomStatusEvent;
import cn.bjhdltcdn.p2plive.event.AnonymousMsgEvent;
import cn.bjhdltcdn.p2plive.event.AssociationInfoEditorEvent;
import cn.bjhdltcdn.p2plive.event.GroupOperationEvent;
import cn.bjhdltcdn.p2plive.event.GroupShareItemClickEvent;
import cn.bjhdltcdn.p2plive.event.RongyunReceiveUnreadCountEvent;
import cn.bjhdltcdn.p2plive.event.YouBanProductOrderMessageEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JoinGroupResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateChatRoomResponse;
import cn.bjhdltcdn.p2plive.model.ChatBaseUser;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ChatConversationFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends BaseActivity implements BaseView {

    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 标题
     */
    private String title;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    /**
     * 匿名发送和接收用户对象
     */
    private ChatBaseUser chatBaseUser, toChatBaseUser;
    /**
     * 当前用户id
     */
    private long currentBaseUserId;
    /**
     * 页面
     */
    private Fragment mFragment;
    private ToolBarFragment titleFragment;

    /**
     * 成员进群方式(1直接入群,2申请进群)
     */
    private Group selectSharedGroup;
    /**
     * 标题栏右侧左边按钮点击
     */
    private ToolBarFragment.ViewOnclick onTitileRightLeftBtnClick;

    /**
     * 标题栏右侧左边按钮点击
     */
    private ToolBarFragment.ViewOnclick onTitileRightRightBtnClick;
    /**
     * 锁修改完状态
     */
    private int toLock;
    /**
     * 操作类型 0 进入聊天室 1 主动退出聊天室 2 用户被移除聊天室,
     */
    private int updateChatRoomUserType;
    /**
     * 是否最小化
     */
    private boolean isMinimize;

    public void setUpdateChatRoomUserType(int updateChatRoomUserType) {
        this.updateChatRoomUserType = updateChatRoomUserType;
    }

    public String getmTargetId() {
        return mTargetId;
    }

    private GroupPresenter groupPresenter;
    private UserPresenter userPresenter;
    private ChatRoomPresenter chatRoomPresenter;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public ChatBaseUser getChatBaseUser() {
        return chatBaseUser;
    }

    public ChatBaseUser getToChatBaseUser() {
        return toChatBaseUser;
    }

    public Conversation.ConversationType getmConversationType() {
        return mConversationType;
    }

    public GroupPresenter getGroupPresenter() {
        if (groupPresenter == null) {
            groupPresenter = new GroupPresenter(this);
        }
        return groupPresenter;
    }

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversatio_layout);

        EventBus.getDefault().register(this);

        getIntentDate(getIntent());

        setTitle();

        intView();

        Logger.d("mTargetId == " + mTargetId + " ==onCreate()== title === " + title);

        // 兼容OPPO手机，OPPO手机没回调融云的未读消息监听器
        try {
            if ("OPPO".toLowerCase().equals(Build.MANUFACTURER.toLowerCase()) || "HUAWEI".toLowerCase().equals(Build.MANUFACTURER.toLowerCase())) {
                EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

        getIntentDate(getIntent());

        setTitle();
        intView();


        Logger.d("mTargetId == " + mTargetId + " ==onNewIntent()== title === " + title);

        // 兼容OPPO手机，OPPO手机没回调融云的未读消息监听器
        try {
            if ("OPPO".toLowerCase().equals(Build.MANUFACTURER.toLowerCase()) || "HUAWEI".toLowerCase().equals(Build.MANUFACTURER.toLowerCase())) {
                EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void intView() {

        mFragment = new ChatConversationFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();
        if (mConversationType == Conversation.ConversationType.GROUP) {
            RongIMutils.setMentionedInputListener();
        }

        if (uri != null) {
            ((ConversationFragment) mFragment).setUri(uri);
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.conversation);

    }

    private void setTitle() {
        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        // 群详情入口
        if (mConversationType == Conversation.ConversationType.GROUP) {
            titleFragment.setRightView(R.mipmap.detail_group_icon, new ToolBarFragment.ViewOnclick() {
                @Override
                public void onClick() {
                    startActivity(new Intent(ConversationActivity.this, GroupDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, mTargetId));
                }
            });
        } else if (mConversationType == Conversation.ConversationType.CHATROOM && mTargetId.startsWith("NM")
                && Constants.Object.CHATINFO != null) {
            title = Constants.Object.CHATINFO.getChatName();
            titleFragment.setLeftView(new ToolBarFragment.ViewOnclick() {
                @Override
                public void onClick() {
                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                    dialog.setText("退出匿名聊天房间", "退出房间将不再参与匿名聊天，是否确认退出？", "退出", "最小化");
                    dialog.setCloseViewShow(true);
                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                        @Override
                        public void onLeftClick() {
                            updateChatRoomUserType = 1;
                            getChatRoomPresenter().updateChatRoomUser(currentBaseUserId, 0, Constants.Object.CHATINFO.getChatId(), updateChatRoomUserType);
                        }

                        @Override
                        public void onRightClick() {
                            startActivity(new Intent(ConversationActivity.this, MainActivity.class));
                            isMinimize = true;
                            finish();
                        }
                    });
                    dialog.show(getSupportFragmentManager());

                }
            });
            if (Constants.Object.CHATINFO.getUserRole() == 1) {
                onTitileRightLeftBtnClick = new ToolBarFragment.ViewOnclick() {
                    @Override
                    public void onClick() {
                        String rightText = null;
                        String titleStr = null;
                        if (Constants.Object.CHATINFO.getIsLock() == 0) {
                            rightText = "上锁";
                            titleStr = "您可以上锁，其他人则无权进入此聊天房间";
                            toLock = 1;
                        } else if (Constants.Object.CHATINFO.getIsLock() == 1) {
                            rightText = "解锁";
                            titleStr = "您可以解锁，其他人则可以进入此聊天房间";
                            toLock = 0;
                        }
                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("匿名聊天室", titleStr, "取消", rightText);
                        dialog.setCloseViewShow(true);
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {

                            }

                            @Override
                            public void onRightClick() {
                                getChatRoomPresenter().updateChatRoomLock(currentBaseUserId, Constants.Object.CHATINFO.getChatId(), toLock);
                            }
                        });
                        dialog.show(getSupportFragmentManager());
                    }
                };
                onTitileRightRightBtnClick = new ToolBarFragment.ViewOnclick() {
                    @Override
                    public void onClick() {
                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("关闭匿名聊天房间", "关闭代表解散此聊天房间，是否确认关闭？", "取消", "关闭");
                        dialog.setCloseViewShow(true);
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {

                            }

                            @Override
                            public void onRightClick() {
                                getChatRoomPresenter().updateChatRoom(currentBaseUserId, Constants.Object.CHATINFO.getChatId(), null,
                                        1, 0, 0, null, null);
                            }
                        });
                        dialog.show(getSupportFragmentManager());
                    }
                };
                setLockImage();
            }
        }
        if (!TextUtils.isEmpty(title)) {
            titleFragment.setTitleView(title);
        }
    }

    /**
     * 设置锁显示图片
     */
    private void setLockImage() {
        if (Constants.Object.CHATINFO.getIsLock() == 0) {
            titleFragment.setDoubleRightView(R.drawable.unlock_chatroom, R.drawable.quit_chatroom, onTitileRightLeftBtnClick, onTitileRightRightBtnClick);
        } else if (Constants.Object.CHATINFO.getIsLock() == 1) {
            titleFragment.setDoubleRightView(R.drawable.lock_chatroom, R.drawable.quit_chatroom, onTitileRightLeftBtnClick, onTitileRightRightBtnClick);
        }
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
        mTargetId = intent.getData().getQueryParameter("targetId");
        title = intent.getData().getQueryParameter("title");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        String mConversationTypeString = intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault());
        Logger.d("mConversationTypeString === " + mConversationTypeString);
        mConversationType = Conversation.ConversationType.valueOf(mConversationTypeString);
        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        if (mTargetId.startsWith("NM")) {
            App.setMyExtensionModule(mConversationType, true);
        } else {
            App.setMyExtensionModule(mConversationType, false);
            RongIMutils.findUserByUserId(String.valueOf(currentBaseUserId));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(GroupOperationEvent evnet) {

        if (evnet == null) {
            return;
        }

        Logger.d("群操作 === " + evnet.getOperationType());

        if (mConversationType != Conversation.ConversationType.GROUP) {
            Logger.d("群操作 ==mConversationType.getName()= " + mConversationType.getName());
            return;
        }


        if (evnet.getOperationType() == 1 || evnet.getOperationType() == 2) {

            // 删除此群在聊天列表的item
            RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, evnet.getGroupId() + "", new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    Logger.d("aBoolean === " + aBoolean);

                    finish();

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Logger.d("errorCode === " + errorCode.getMessage() + " ; errorValue === " + errorCode.getValue());
                }
            });

        } else if (evnet.getOperationType() == 3) {

            titleFragment.setTitleView(evnet.getNickName());
            RongIMutils.findGroupInfo(mTargetId);

        } else if (evnet.getOperationType() == 6) {// 刷新用户信息
            RongIMutils.findUserByUserId(mTargetId);

        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AnonymousMsgEvent event) {
        if (event == null) {
            return;
        }
        chatBaseUser = event.getChatBaseUser();
        toChatBaseUser = event.getToChatBaseUser();
        if (mConversationType == Conversation.ConversationType.PRIVATE && mTargetId.startsWith("NM")) {
            if (toChatBaseUser.getIsFriend() == 1) {
                titleFragment.setRightViewIsVisibility(View.GONE);
            } else {
                titleFragment.setRightViewIsVisibility(View.VISIBLE);
                titleFragment.setRightView("加为匿名好友", new ToolBarFragment.ViewOnclick() {
                    @Override
                    public void onClick() {
                        getUserPresenter().saveAnonymityUser(currentBaseUserId, toChatBaseUser.getUserId());
                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AnonymousChatRoomStatusEvent event) {
        if (event == null) {
            return;
        }
        if (!event.isInChatRoom() && event.getMeaaageType() != 0) {
            Utils.showToastShortTime(event.getTipMsg());
            if (Constants.Object.CHATINFO != null) {
                RongIMutils.quitChatRoom(Constants.Object.CHATINFO.getChatId());
                Constants.Object.CHATINFO = null;
            }
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(final GroupShareItemClickEvent evnet) {
        if (evnet == null) {
            return;
        }

        if (evnet.getGroup() == null) {
            Logger.d("evnet.getGroup() is null");
            return;
        }

        selectSharedGroup = evnet.getGroup();

        if (selectSharedGroup == null) {
            Logger.d("分享的数据为空 selectSharedGroup is null ");
            return;
        }


        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
        dialog.setText("", "是否要加入本群？", "取消", "加入群");
        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
            @Override
            public void onLeftClick() {
                //取消
            }

            @Override
            public void onRightClick() {
                //申请
                getGroupPresenter().joinGroup(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), evnet.getGroup().getGroupId(), evnet.getGroup().getGroupMode());
            }
        });
        dialog.show(getSupportFragmentManager());

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(final YouBanProductOrderMessageEvent event) {// 商品确认收货事件

        if (event == null) {
            return;
        }

//        switch (event.getMessageType()) {
//            case 7000:

//                if (!isFinishing()) {
//                    GoodsreceiptDialog dialog = new GoodsreceiptDialog();
//                    dialog.setOrderId(event.getOrderId());
//                    dialog.setItemClick(new GoodsreceiptDialog.ItemClick() {
//                        @Override
//                        public void itemClick() {
//
//                            RongIMClient.getInstance().setMessageExtra(event.getMessageId(), "1", new RongIMClient.ResultCallback<Boolean>() {
//                                @Override
//                                public void onSuccess(Boolean aBoolean) {
//                                    Logger.d("aBoolean ==== " + aBoolean);
//                                }
//
//                                @Override
//                                public void onError(RongIMClient.ErrorCode errorCode) {
//                                    Logger.d("errorCode ==== " + errorCode);
//                                }
//                            });
//
//                        }
//                    });
//                    dialog.show(getSupportFragmentManager());
//                }

//                break;
//
//        }


    }

    @Override
    public void updateView(String apiName, Object object) {
        if (isFinishing()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            if (e instanceof SocketTimeoutException) {
                Utils.showToastShortTime("网络连接超时");
                return;
            }

            Utils.showToastShortTime(e.getMessage());

            return;
        }

        if (InterfaceUrl.URL_JOIN_GROUP.equals(apiName)) {

            if (object instanceof JoinGroupResponse) {

                JoinGroupResponse response = (JoinGroupResponse) object;

                // 200 加入群成功，203 已经在群里
                if (response.getCode() == 200) {
                    // 跳进群聊页面
                    if (selectSharedGroup != null && selectSharedGroup.getGroupMode() == 1) {
                        RongIMutils.startGroupChat(this, selectSharedGroup.getGroupId() + "", selectSharedGroup.getGroupName());
                    } else {
                        Utils.showToastShortTime(!StringUtils.isEmpty(response.getContent()) ? response.getContent() : response.getMsg());
                    }

                } else if (response.getCode() == 203) {
                    // 跳进群聊页面
                    if (selectSharedGroup != null) {
                        RongIMutils.startGroupChat(this, selectSharedGroup.getGroupId() + "", selectSharedGroup.getGroupName());
                    }
                } else {
                    Utils.showToastShortTime(!StringUtils.isEmpty(response.getContent()) ? response.getContent() : response.getMsg());
                }

            }

        } else if (InterfaceUrl.URL_SAVEANONYMITYUSER.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse baseResponse = (BaseResponse) object;
                if (baseResponse.getCode() == 200) {
                    titleFragment.setRightViewIsVisibility(View.GONE);
                }
                Utils.showToastShortTime(baseResponse.getMsg());
            }
        } else if (InterfaceUrl.URL_UPDATECHATROOMLOCK.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.getCode() == 200) {
                    Constants.Object.CHATINFO.setIsLock(toLock);
                    if (toLock == 0) {
                        EventBus.getDefault().post(new AssociationInfoEditorEvent(3));
                    } else if (toLock == 1) {
                        EventBus.getDefault().post(new AssociationInfoEditorEvent(4));
                    }
                    setLockImage();
                }
                Utils.showToastShortTime(response.getMsg());
            }
        } else if (InterfaceUrl.URL_UPDATECHATROOM.equals(apiName)) {
            if (object instanceof UpdateChatRoomResponse) {
                UpdateChatRoomResponse updateChatRoomResponse = (UpdateChatRoomResponse) object;
                Utils.showToastShortTime(updateChatRoomResponse.getMsg());
                if (updateChatRoomResponse.getCode() == 200) {
                    RongIMutils.quitChatRoom(Constants.Object.CHATINFO.getChatId());
                    Constants.Object.CHATINFO = null;
                    EventBus.getDefault().post(new AnonymousChatRoomStatusEvent(false));
                    finish();
                }
            }

        } else if (InterfaceUrl.URL_UPDATECHATROOMUSER.equals(apiName)) {
            if (object instanceof UpdateChatRoomResponse && updateChatRoomUserType == 1) {
                UpdateChatRoomResponse updateChatRoomResponse = (UpdateChatRoomResponse) object;
                Utils.showToastShortTime(updateChatRoomResponse.getMsg());
                if (updateChatRoomResponse.getCode() == 200) {
                    RongIMutils.quitChatRoom(Constants.Object.CHATINFO.getChatId());
                    Constants.Object.CHATINFO = null;
                    EventBus.getDefault().post(new AnonymousChatRoomStatusEvent(false));
                    finish();
                }
            }
        }


    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        if (titleFragment != null) {
            titleFragment = null;
        }

        if (mFragment != null) {
            mFragment = null;
        }
        //页面关闭后，收不到聊天室消息，需要执行重新加入
        if (mConversationType == Conversation.ConversationType.CHATROOM && mTargetId.startsWith("NM") && isMinimize) {
            RongIM.getInstance().joinChatRoom(mTargetId, -1, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            });
        }
        if (mTargetId != null) {
            mTargetId = null;
        }

        if (title != null) {
            title = null;
        }

        if (mConversationType != null) {
            mConversationType = null;
        }

        if (selectSharedGroup != null) {
            selectSharedGroup = null;
        }

    }


}