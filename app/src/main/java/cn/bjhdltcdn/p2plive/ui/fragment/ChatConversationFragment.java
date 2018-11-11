package cn.bjhdltcdn.p2plive.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.AnonymousChatRoomManageEvent;
import cn.bjhdltcdn.p2plive.event.AnonymousSendImageMessageEvent;
import cn.bjhdltcdn.p2plive.event.RongEditLisenterEvent;
import cn.bjhdltcdn.p2plive.event.UserPortraitClickEvent;
import cn.bjhdltcdn.p2plive.model.ChatBaseUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ConversationActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.AnonymousUserDetailDialog;
import cn.bjhdltcdn.p2plive.utils.Utils;
import io.rong.imkit.InputBar;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

/**
 * @author zhudi
 */
public class ChatConversationFragment extends ConversationFragment implements BaseView {

    private RelativeLayout containerView;

    private RongExtension extension;

    private EditText inputEditText;
    /**
     * 可以发消息
     */
    private boolean isMsgEnable = true;
    /**
     * @功能群id
     */
    private long groupId;

    private UserPresenter userPresenter;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        containerView = (RelativeLayout) super.onCreateView(inflater, container, savedInstanceState);
        extension = containerView.findViewById(io.rong.imkit.R.id.rc_extension);
        if (getConversationActivity().getmTargetId().startsWith("NM") && getConversationActivity().getmConversationType() == Conversation.ConversationType.PRIVATE) {
            extension.setInputBarStyle(InputBar.Style.STYLE_CONTAINER_EXTENSION);
        }
        inputEditText = extension.getInputEditText();
        return containerView;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RongEditLisenterEvent event) {
        if (event == null || inputEditText == null) {
            return;
        }
        groupId = event.getGroupId();
        String msg = inputEditText.getText().toString();
        msg = msg + "所有人";
        inputEditText.setText(msg);
    }

    @Override
    public void onPluginToggleClick(View v, ViewGroup extensionBoard) {
        if (isMsgEnable) {
            super.onPluginToggleClick(v, extensionBoard);
        } else {
            Utils.showToastShortTime("您已被禁言");
        }
    }

    @Override
    public void onEmoticonToggleClick(View v, ViewGroup extensionBoard) {
        if (isMsgEnable) {
            super.onEmoticonToggleClick(v, extensionBoard);
        } else {
            Utils.showToastShortTime("您已被禁言");
        }
    }

    @Override
    public void onVoiceInputToggleTouch(View v, MotionEvent event) {
        if (isMsgEnable) {
            super.onVoiceInputToggleTouch(v, event);
//            VoiceMessage voiceMessage = VoiceMessage.obtain();
//            UserInfo userInfo = new UserInfo(chatBaseUser.getUserId(), chatBaseUser.getNickName(), Uri.parse(chatBaseUser.getUserIcon()));
//            textMessage.setUserInfo(userInfo);
//            Message message = Message.obtain(getTargetId(), conversationType, textMessage);
//            RongIM.getInstance().
//                    sendMessage(message, null, null, new IRongCallback.ISendMessageCallback()
//
//                    {
//                        @Override
//                        public void onAttached(Message message) {
//
//                        }
//
//                        @Override
//                        public void onSuccess(Message message) {
//
//                        }
//
//                        @Override
//                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//                        }
//                    });
        } else {
            Utils.showToastShortTime("您已被禁言");
        }
    }

    @Override
    public void onSendToggleClick(View v, String text) {
        if (!TextUtils.isEmpty(text) && text.contains("@所有人") && groupId != 0) {
            TextMessage textMessage = TextMessage.obtain(text);
            MentionedInfo mentionedInfo = new MentionedInfo(MentionedInfo.MentionedType.ALL, null, null);
            textMessage.setMentionedInfo(mentionedInfo);
            RongIM.getInstance().

                    sendMessage(Message.obtain(String.valueOf(groupId), Conversation.ConversationType.GROUP, textMessage), null, null, new IRongCallback.ISendMessageCallback()

                    {
                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
        } else if (getTargetId().startsWith("NM")) {
            TextMessage textMessage = TextMessage.obtain(text);
            Conversation.ConversationType conversationType = getConversationActivity().getmConversationType();
            ChatBaseUser chatBaseUser = getConversationActivity().getChatBaseUser();
            if (conversationType == Conversation.ConversationType.PRIVATE) {
                ChatBaseUser toChatBaseUser = getConversationActivity().getToChatBaseUser();
                textMessage.setExtra(String.valueOf(toChatBaseUser.getUserId()));
                insertLocalMessage(text, null, textMessage, chatBaseUser, toChatBaseUser);
            } else if (conversationType == Conversation.ConversationType.CHATROOM) {
                if (isMsgEnable) {
                    UserInfo userInfo = new UserInfo("NM" + chatBaseUser.getUserId(), chatBaseUser.getNickName(), Uri.parse(chatBaseUser.getUserIcon()));
                    textMessage.setUserInfo(userInfo);
                    Message message = Message.obtain(getTargetId(), conversationType, textMessage);
                    RongIM.getInstance().
                            sendMessage(message, null, null, new IRongCallback.ISendMessageCallback()

                            {
                                @Override
                                public void onAttached(Message message) {

                                }

                                @Override
                                public void onSuccess(Message message) {

                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                                }
                            });
                } else {
                    Utils.showToastShortTime("您已被禁言");
                }

            }

        } else {
            super.onSendToggleClick(v, text);
        }
    }

    /**
     * 本地插入消息
     *
     * @param text
     * @param file
     * @param messageContent
     * @param chatBaseUser
     * @param toChatBaseUser
     */
    private void insertLocalMessage(String text, String file, MessageContent messageContent, ChatBaseUser chatBaseUser, ChatBaseUser toChatBaseUser) {
        getUserPresenter().sendAnonymousMsg(chatBaseUser.getUserId(), toChatBaseUser.getUserId(), 1, text, file);
        RongIM.getInstance().insertOutgoingMessage(Conversation.ConversationType.PRIVATE, getTargetId(), Message.SentStatus.SENT, messageContent, System.currentTimeMillis(), new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AnonymousSendImageMessageEvent event) {
        if (event == null || getConversationActivity().getmConversationType() != Conversation.ConversationType.PRIVATE) {
            return;
        }
        ImageMessage imageMessage = (ImageMessage) event.getMessageContent();
        imageMessage.setExtra(String.valueOf(getConversationActivity().getToChatBaseUser().getUserId()));
        String filePath = imageMessage.getLocalPath().toString();
        if (filePath.startsWith("file:///")) {
            filePath = filePath.substring(7, filePath.length());
        }
        insertLocalMessage(null, filePath, imageMessage, getConversationActivity().getChatBaseUser(), getConversationActivity().getToChatBaseUser());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AnonymousChatRoomManageEvent event) {
        if (event == null) {
            return;
        }
        if (event.getMeaaageType() == 9003) {
            isMsgEnable = false;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final UserPortraitClickEvent event) {
        if (event == null || inputEditText == null) {
            return;
        }
        String userIdStr = event.getUserInfo().getUserId();
        if (userIdStr.startsWith("NM")) {
            userIdStr = userIdStr.substring(2, userIdStr.length());
        }
        final long userId = Long.valueOf(userIdStr);
        if (userId != getConversationActivity().getChatBaseUser().getUserId()) {
            if (!event.isLongClick()) {
                getUserPresenter().getAnonymityUser(getConversationActivity().getChatBaseUser().getUserId(), userId);
            } else if (Constants.Object.CHATINFO != null) {
                AnonymousUserDetailDialog dialog = new AnonymousUserDetailDialog();
                dialog.setUserInfo(event.getUserInfo());
                dialog.setUserRole(Constants.Object.CHATINFO.getUserRole());
                dialog.setOnClickListener(new AnonymousUserDetailDialog.OnClickListener() {
                    @Override
                    public void onLeftClick() {
                        String msg = inputEditText.getText().toString();
                        msg = msg + "@" + event.getUserInfo().getName();
                        inputEditText.setText(msg);
                    }

                    @Override
                    public void onRightClick() {
                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("踢出此用户", "踢出后，此用户将不再该匿名聊天室内，是否踢出？", "取消", "踢出");
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {
                            }

                            @Override
                            public void onRightClick() {
                                getConversationActivity().setUpdateChatRoomUserType(2);
                                getConversationActivity().getChatRoomPresenter().updateChatRoomUser(getConversationActivity().getChatBaseUser().getUserId(), userId, getTargetId(), 2);
                            }
                        });
                        dialog.show(getFragmentManager());
                    }
                });
                dialog.show(getFragmentManager());
            }

        }
    }

    private ConversationActivity getConversationActivity() {
        return (ConversationActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void updateView(String apiName, Object object) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
