package cn.bjhdltcdn.p2plive.ui.fragment;

import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.GroupVideoListAdapter;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

/**
 * Description:自定义消息列表
 * Data: 2018/6/6
 * author: zhudi
 */
public class ChatConversationListFragment extends ConversationListFragment implements BaseView {
    private UserPresenter userPresenter;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                Conversation conversation = conversations.get(position);
                if (conversation.getTargetId().startsWith("NM")) {
                    RongIMClient.getInstance().getMessage(conversation.getLatestMessageId(), new RongIMClient.ResultCallback<Message>() {
                        @Override
                        public void onSuccess(Message message) {
                            long toUserId = 0;
                            String extraStr = null;
                            if (message.getSenderUserId().startsWith("NM")) {
                                VideoCMDTextMessageModel videoCMDTextMessageModel = new VideoCMDTextMessageModel();
                                if (message.getContent() instanceof TextMessage) {
                                    TextMessage textMessage = (TextMessage) message.getContent();
                                    extraStr = textMessage.getExtra();
                                } else if (message.getContent() instanceof ImageMessage) {
                                    ImageMessage imageMessage = (ImageMessage) message.getContent();
                                    extraStr = imageMessage.getExtra();
                                }
                                RongIMutils.analysisContentExtra(extraStr, videoCMDTextMessageModel);
                                toUserId = videoCMDTextMessageModel.getBaseUser().getUserId();
                            } else {
                                if (message.getContent() instanceof TextMessage) {
                                    TextMessage textMessage = (TextMessage) message.getContent();
                                    toUserId = Long.valueOf(textMessage.getExtra());
                                } else if (message.getContent() instanceof ImageMessage) {
                                    ImageMessage imageMessage = (ImageMessage) message.getContent();
                                    toUserId = Long.valueOf(imageMessage.getExtra());
                                }
                            }
                            getUserPresenter().getAnonymityUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toUserId);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                } else {
                    ChatConversationListFragment.super.onItemClick(parent, view, position, id);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
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
