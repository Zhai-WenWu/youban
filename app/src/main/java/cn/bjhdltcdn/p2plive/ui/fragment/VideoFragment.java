package cn.bjhdltcdn.p2plive.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.callback.RongYunUnreadCountCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.RongyunReceiveUnreadCountEvent;
import cn.bjhdltcdn.p2plive.event.UpdateFocusOnListEvent;
import cn.bjhdltcdn.p2plive.event.VideoCMDTextMessageEvent;
import cn.bjhdltcdn.p2plive.event.VideoEvent;
import cn.bjhdltcdn.p2plive.event.VideoMoreEvent;
import cn.bjhdltcdn.p2plive.httpresponse.AttentionStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.VideoChatActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.VideoChatAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.UserDetailDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.VideoMoreDialog;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.SlideMenuDrawerLayout;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by xiawenquan on 16/7/4.
 */
public class VideoFragment extends VideoBaseFragment implements View.OnClickListener {

    private View rootView;
    private UserPresenter userPresenter;
    private ChatRoomPresenter chatRoomPresenter;
    private Button messageView;

    private RecyclerView mRecyclerView;
    private VideoChatAdapter mAdapter;

    // 输入框
    private View sendLayoutView;
    private EditText editText;

    private TextView attentionView;
    /**
     * 关注状态
     * 1 双向关注，2 当前用户单向关注对方，3 双向不关注,
     */
    private int status;


    /**
     * 本地话筒是否关闭
     */
    private boolean isLocalMicClose = false;

    private UserDetailDialog userDetailDialog;

    /**
     * 本地摄像头是否开启
     */
    private boolean isLocalVideoOpen = true;

    /**
     * 对方摄像头是否开启
     */
    private boolean isRemoteVideoOpen = true;
    /**
     * 声网美颜
     */
//    private AgoraYuvEnhancer yuvEnhancer = null;
    private BaseUser otherUser;//对方用户
    private BaseUser currentUser;//当前登录的用户
    private long currentUserId;//当前登录的用户

    private PopupWindow popupWindow;

    @Override
    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    @Override
    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_video_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

//        yuvEnhancer = new AgoraYuvEnhancer(App.getInstance());
//        yuvEnhancer.StartPreProcess();

        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (rootView == null) {
            return;
        }

        otherUser = getVideoChatActivity().getBaseUser();
        currentUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        GreenDaoUtils.getInstance().getBaseUser(currentUserId, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(Object object) {
                if (object instanceof BaseUser) {
                    currentUser = (BaseUser) object;
                }
            }
        });

        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment mFragment = getParentFragment();
        if (mFragment instanceof VideoChatFragment) {
            VideoChatFragment videoChatFragment = (VideoChatFragment) mFragment;
            isLocalVideoOpen = videoChatFragment.isLocalVideoOpen();
            isRemoteVideoOpen = videoChatFragment.isRemoteVideoOpen();
            videoChatFragment.setonResume();
        }
    }

    private void initView() {
        Fragment fragment = getParentFragment();
        if (fragment != null) {
            if (fragment instanceof VideoChatFragment) {
                VideoChatFragment videoChatFragment = (VideoChatFragment) fragment;
                SlideMenuDrawerLayout slideMenuDrawerLayout = videoChatFragment.getSlideMenuDrawerLayout();
                if (slideMenuDrawerLayout != null) {
                    //关闭滑动
                    slideMenuDrawerLayout.setEnabledScroll(true);
                }
            }
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new VideoChatAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //关注
        View attentionViewLayout = rootView.findViewById(R.id.attention_view_layout);
        attentionView = (TextView) attentionViewLayout.findViewById(R.id.attention_view);
        attentionView.setOnClickListener(this);
        // 头像
        ImageView userImageView = (ImageView) attentionViewLayout.findViewById(R.id.user_image_view);
        Utils.ImageViewDisplayByUrl(otherUser.getUserIcon(), userImageView);
        userImageView.setOnClickListener(this);
        // 通话状态
        TextView videoStatusView = (TextView) attentionViewLayout.findViewById(R.id.video_status_view);
        videoStatusView.setText("通话中");
        //昵称
        TextView tv_nickname = (TextView) getUIView(R.id.tv_nickname);
        tv_nickname.setText(otherUser.getNickName());
        //公屏消息
        messageView = (Button) rootView.findViewById(R.id.message_view);
        messageView.setOnClickListener(this);
        //消息发送框
        sendLayoutView = rootView.findViewById(R.id.send_layout_view);
        editText = (EditText) sendLayoutView.findViewById(R.id.reply_edit_input);
        final Button sendView = (Button) sendLayoutView.findViewById(R.id.send_view);
        sendView.setEnabled(true);
        sendView.setOnClickListener(this);
        //输入框无输入时点击隐藏
        if (getActivity().getWindow() != null) {
            getActivity().getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    //获取View可见区域的bottom
                    Rect rect = new Rect();
                    getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    if (bottom != 0 && oldBottom != 0 && bottom - rect.bottom <= 200) {
                        //键盘消失
                        if (sendLayoutView != null && sendLayoutView.getVisibility() == View.VISIBLE && editText.isFocused() && editText.getText().length() == 0) {
                            sendLayoutView.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
        //私信
        getUIView(R.id.btn_msg_private).setOnClickListener(this);
        //结束按钮
        getUIView(R.id.finish_view).setOnClickListener(this);
        //更多
        getUIView(R.id.btn_more).setOnClickListener(this);

//        getGreenConvention();
//        getUserPresenter().attentionStatus(currentUserId, otherUser.getUserId());
        // 获取消息未读数
        EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attention_view:
                getUserPresenter().attentionOperation(status, currentUserId, otherUser.getUserId());
                break;
            case R.id.message_view:
                if (sendLayoutView != null) {
                    sendLayoutView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.send_view:
                if (editText != null) {
                    String text = editText.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        Toast.makeText(App.getInstance(), "内容不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (getVideoChatActivity() == null) {
                        return;
                    }
                    if (sendLayoutView != null) {
                        sendLayoutView.setVisibility(View.GONE);
                    }
                    Utils.hideSystemSoftInputKeyboard(editText);
                    editText.setText("");
                    RongIMutils.sendChatTextMessage(otherUser.getUserId(), text);
                }
                break;
            case R.id.user_image_view:
                if (otherUser != null) {

                    if (userDetailDialog != null && userDetailDialog.isDetached()) {
                        userDetailDialog.dismiss();
                    }
                    userDetailDialog = new UserDetailDialog();
                    userDetailDialog.setBaseUser(otherUser);
                    userDetailDialog.show(getChildFragmentManager(), "dialog");
                }
                break;
            case R.id.btn_msg_private:
                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        if (conversations == null || conversations.isEmpty()) {
                            Utils.showToastShortTime("暂无私信");
                            return;
                        }

                        GroupVideoMessageListDialogFragment messageListDialogFragment = new GroupVideoMessageListDialogFragment();
                        messageListDialogFragment.show(getChildFragmentManager());
                        RongIMutils.setIsCanOnUserPortraitClick(false);

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        // 发生错误强制吊起私信对话框
                        GroupVideoMessageListDialogFragment messageListDialogFragment = new GroupVideoMessageListDialogFragment();
                        messageListDialogFragment.show(getChildFragmentManager());
                        RongIMutils.setIsCanOnUserPortraitClick(false);

                    }
                });
                break;
            case R.id.finish_view:
                if (getVideoChatActivity() != null) {
                    App.getInstance().getRtcEngine().leaveChannel();
                    EventBus.getDefault().post(new VideoEvent(106));
                    oneToOneVideo(getVideoChatActivity().getVideoType(), 4);
//                    RongIMutils.finishVideo(otherUser.getUserId(),
//                            getVideoChatActivity().getRoomNumber());
//                    if (getVideoChatActivity().getCallHistory() != null) {
//                        getVideoChatActivity().getCallHistory().setCallStatus(0);
//                        EventBus.getDefault().post(new ChangeCallHistoryEnvet(getVideoChatActivity().getCallHistory()));
//                    }

                }
                break;
            case R.id.btn_more:
                VideoMoreDialog videoMoreDialog = new VideoMoreDialog();
                videoMoreDialog.setLocalVideoOpen(isLocalVideoOpen);
                videoMoreDialog.setLocalMicClose(isLocalMicClose);
                videoMoreDialog.show(getChildFragmentManager());
                break;
            default:
        }
    }

    @Override
    public View getUIView(int id) {
        return rootView.findViewById(id);
    }

    /**
     * 更多按钮
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoMoreEvent event) {
        if (event == null) {
            return;
        }
        switch (event.getType()) {
            case 1://翻转摄像头
                App.getInstance().getRtcEngine().switchCamera();
                break;
            case 2://关闭开启麦克风
                isLocalMicClose = !isLocalMicClose;
                App.getInstance().getRtcEngine().muteLocalAudioStream(isLocalMicClose);
                break;
            case 3://关闭摄像头
                if (getParentFragment() instanceof VideoChatFragment) {
                    if (isLocalVideoOpen) {
                        //暂停发送本地视频流:True: 不发送本地视频流;False: 发送本地视频流
                        App.getInstance().getRtcEngine().muteLocalVideoStream(true);
                        ((VideoChatFragment) getParentFragment()).setPrarentLayout(0, 0);
                        if (isRemoteVideoOpen) {
                            ((VideoChatFragment) getParentFragment()).setBgDisplay(false);
                            ((VideoChatFragment) getParentFragment()).setRemoteVideoBig();
                        } else {
                            ((VideoChatFragment) getParentFragment()).setBgDisplay(true);
                        }
                    } else {
                        ((VideoChatFragment) getParentFragment()).setBgDisplay(false);
                        //暂停发送本地视频流:True: 不发送本地视频流;False: 发送本地视频流
                        App.getInstance().getRtcEngine().muteLocalVideoStream(false);
                        if (isRemoteVideoOpen) {
                            ((VideoChatFragment) getParentFragment()).setPrarentLayout(144, 188);
                        } else {
                            ((VideoChatFragment) getParentFragment()).setLocalVideoBig();
                            ((VideoChatFragment) getParentFragment()).setPrarentLayout(0, 0);
                        }
                    }
                    isLocalVideoOpen = !isLocalVideoOpen;
                    ((VideoChatFragment) getParentFragment()).setLocalVideoOpen(isLocalVideoOpen);
                }
                break;

        }
    }


    /**
     * 接收文本消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoCMDTextMessageEvent event) {

        if (event == null) {
            return;
        }

        VideoCMDTextMessageModel message = event.getTextMessageModel();
        if (message == null) {
            return;
        }

        if (mAdapter == null) {
            return;
        }

        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.addItemData(message);
        mRecyclerView.getLayoutManager().scrollToPosition(mAdapter.getItemCount() - 1);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateFocusOnListEvent event) {
        if (event != null && attentionView != null) {
            attentionView.setEnabled(false);
            attentionView.setText("已关注");
        }
    }

    /**
     * 接收融云未读消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RongyunReceiveUnreadCountEvent event) {

        if (event == null) {
            return;
        }

        try {
            RongIMutils.getUnreadCount(new RongYunUnreadCountCallback() {
                @Override
                public void onSuccess(int count) {

                    if (rootView != null) {
                        TextView popView = (TextView) rootView.findViewById(R.id.img_msg_private_pop_view);
                        if (count > 0) {
                            popView.setText(count > 99 ? "99+" : count + "");
                            popView.setVisibility(View.VISIBLE);
                        } else {
                            popView.setVisibility(View.GONE);
                        }
                    }

                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUIView(String type, Object object) {
        switch (type) {
            case InterfaceUrl.URL_ATTENTIONSTATUS:
                if (object instanceof AttentionStatusResponse) {
                    AttentionStatusResponse getAttentionStatusResponse = (AttentionStatusResponse) object;
                    if (getAttentionStatusResponse.getCode() == 200) {
                        int attentionStatus = getAttentionStatusResponse.getAttentionStatus();
                        //1 双向关注，2 当前用户单向关注对方，3 双向不关注，4 对方单向关注我,
                        if (attentionStatus == 1 || attentionStatus == 2) {
                            status = 2;
                            attentionView.setText("已关注");
                            attentionView.setEnabled(false);
                            attentionView.setBackgroundResource(R.drawable.shape_round_60_solid_666666);
                            attentionView.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            status = 1;
                            attentionView.setText("关注");
                        }
                    }
                }

                break;
            case InterfaceUrl.URL_ATTENTIONOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse getAttentionStatusResponse = (BaseResponse) object;
                    if (getAttentionStatusResponse.getCode() == 200) {
                        if (status == 1) {
                            status = 2;
                            attentionView.setText("已关注");
                            attentionView.setEnabled(false);
                            attentionView.setBackgroundResource(R.drawable.shape_round_60_solid_666666);
                            attentionView.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                }

                break;
        }

    }


    @Override
    public VideoChatActivity getVideoChatActivity() {
        return super.getVideoChatActivity();
    }


//
//    public void getGreenConvention() {
//        GetGreenConventionRequest getGreenConventionRequest = new GetGreenConventionRequest(2);
//        try {
//            ApiData.getInstance().getGreenConvention(
//                    getGreenConventionRequest, new JsonParserCallback(GetGreenConventionResponse.class) {
//                        @Override
//                        public void onBefore(Request request, int id) {
//                            super.onBefore(request, id);
//                            ProgressDialogUtil.getInstance().getDialog("正在加载", mPresenter, InterfaceUrlConstants.URL_GETGREENCONVENTION, getCurrentActivity());
//                        }
//
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            KLog.e(e);
//                            if (rootView != null) {
//                                ProgressDialogUtil.getInstance().dismiss();
//                            }
//
//                        }
//
//                        @Override
//                        public void onResponse(Object response, int id) {
//                            if (response instanceof GetGreenConventionResponse) {
//                                GetGreenConventionResponse getGreenConventionResponse = (GetGreenConventionResponse) response;
//                                int code = getGreenConventionResponse.getCode();
//                                if (code == 200) {
//                                    showGreenConventionDialog(getGreenConventionResponse.getTitle(), getGreenConventionResponse.getContent());
//                                }
//                            }
//                            ProgressDialogUtil.getInstance().dismiss();
//                        }
//                    }
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//            KLog.e(e);
//            ProgressDialogUtil.getInstance().dismiss();
//        }
//    }
//
//
//    public void showGreenConventionDialog(String title, String content) {
//        if (StringUtils.isEmpty(content)) {
//            return;
//        }
//        //展示绿色公约dialog
//        VideoConventionDialog videoConventionDialog = new VideoConventionDialog();
//        videoConventionDialog.setTitle(title);
//        videoConventionDialog.setContent(content);
//        videoConventionDialog.setCancelable(false);
//        videoConventionDialog.show(getChildFragmentManager());
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (userDetailDialog != null && userDetailDialog.isVisible()) {
                userDetailDialog.dismiss();
                userDetailDialog = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        popupWindow = null;

        if (mAdapter != null) {
            mAdapter = null;
        }

    }

    public void setRemoteVideoOpen(boolean remoteVideoOpen) {
        isRemoteVideoOpen = remoteVideoOpen;
    }

    @Override
    public void onDetach() {
        super.onDetach();


//        if (getVideoChatActivity().getVideoType() == 0) {//主叫方
//            if (getVideoChatActivity().getCallHistory() != null) {//更新未接来电状态
//                getVideoChatActivity().getCallHistory().setCallStatus(0);
//                getVideoChatActivity().getCallHistory().setCallDuration(System.currentTimeMillis() / 1000 - startVideoTime);
//                EventBus.getDefault().post(new ChangeCallHistoryEnvet(getVideoChatActivity().getCallHistory()));
//            }
//
//        } else { // 被叫方结束通话后插入一条已接来电
//            DBUtils.getInstance().saveCallHistory(otherUser, 0, getVideoChatActivity().getIsFriend(), System.currentTimeMillis() / 1000 - startVideoTime, getVideoChatActivity().getRoomNumber());
//        }

        EventBus.getDefault().unregister(this);

//        if (yuvEnhancer != null) {
//            yuvEnhancer.StopPreProcess();
//        }
//
//        yuvEnhancer = null;

        // 清除声网监听回调
        App.getInstance().setEngineEventHandlerActivity(null);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
