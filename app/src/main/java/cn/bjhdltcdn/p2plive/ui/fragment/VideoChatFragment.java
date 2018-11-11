package cn.bjhdltcdn.p2plive.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.CallVideoEvent;
import cn.bjhdltcdn.p2plive.event.VideoEvent;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.VideoChatActivity;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.SlideMenuDrawerLayout;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;


/**
 * 视频UI
 */
public class VideoChatFragment extends BaseFragment {

    private Fragment mFragment;
    private ChatRoomPresenter mPresenter;
    private View rootView;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /***
     * type的值
     * 接收者：
     * 1---->等待状态
     * 2---->视频状态
     * 呼叫者：
     * 4---->占线状态
     * 5---->空闲
     * 6---->视频状态
     */
    private int type;

    //本地视频UI
    private SurfaceView mLocalView;

    private Handler mHandler;

    private SlideMenuDrawerLayout slideMenuDrawerLayout;
    //本地视频容器
    private FrameLayout localViewContainer;
    //远程视频view
    private SurfaceView remoteView = null;
    // 是否切换视频
    private boolean isChange;
    //本地摄像头是否开启
    private boolean isLocalVideoOpen = true;
    //对方摄像头是否开启
    private boolean isRemoteVideoOpen = true;
    //聊天房间里面的uid
    private int uId;

    // 蒙层图
    private ImageView bgImageView;

    //本地视频空间
    private RelativeLayout prarentLayout;

    private VideoChatActivity activity;

    private FrameLayout remoteUserView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (VideoChatActivity) context;
    }

    public static VideoChatFragment getVideoFragment(int type) {
        VideoChatFragment fragment = new VideoChatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.Fields.TYPE, type);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            type = bundle.getInt(Constants.Fields.TYPE);
        }

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_video_chat_layout, container, false);
        }

        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slideMenuDrawerLayout = (SlideMenuDrawerLayout) rootView.findViewById(R.id.slidemenu_drawer_layout);
        bgImageView = (ImageView) rootView.findViewById(R.id.background_view);
        //显示背景图片
        String imgUrl = "";

        if (activity != null) {
            if (TextUtils.isEmpty(imgUrl)) {
                if (activity.getBaseUser() != null) {
                    imgUrl = activity.getBaseUser().getUserIcon();
                }
            }

        }
        Utils.ImageViewDisplayByUrl(imgUrl, bgImageView);
//        Glide.with(getActivity()).load(imgUrl).asBitmap().transform(new BlurTransformation(App.getInstance(), 10)).error(R.drawable.video_prepare_default_bg).placeholder(R.drawable.video_prepare_default_bg).into(bgImageView);
        bgImageView.setVisibility(View.VISIBLE);


        //本地视频缩放出理
        prarentLayout = (RelativeLayout) rootView.findViewById(R.id.local_surface_view_prarent_layout);
        setPrarentLayout(0, 0);

        //视频画面切换
        localViewContainer = (FrameLayout) rootView.findViewById(R.id.local_surface_view_layout);
        localViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                App.getInstance().getRtcEngine().enableVideo();
                /**
                 * 该方法设置本地视频显示信息
                 * 应用程序通过调用此接口绑定本地视频流的显示视窗(view),并设置视频显示模式
                 */

                if (!isChange) {
                    setLocalVideoBig();
                } else {
                    setRemoteVideoBig();
                }
                isChange = !isChange;
            }
        });
        setupRtcEngine();
        addFragmentToActivity(type);


    }

    @Override
    protected void onVisible(boolean isInit) {
    }

    /**
     * 缩放的视频控件
     *
     * @param width
     * @param height
     */
    public void setPrarentLayout(int width, int height) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) prarentLayout.getLayoutParams();

        if (params == null) {
            params = (RelativeLayout.LayoutParams) new ViewGroup.LayoutParams(Utils.dp2px(width), Utils.dp2px(height));
        }

        params.width = Utils.dp2px(width);
        params.height = Utils.dp2px(height);
        prarentLayout.setLayoutParams(params);


    }

    /**
     * 加载布局
     *
     * @param type
     */
    public void addFragmentToActivity(int type) {

        switch (type) {
            case 1: // 收到对方呼叫请求页面
                if (!(mFragment instanceof VideoWaitFragment)) {
                    mFragment = new VideoWaitFragment();
                }

                break;
            case 2:
            case 6://视频页面
                if (!(mFragment instanceof VideoFragment)) {
                    mFragment = new VideoFragment();
                }

                break;

            case 4://对方通话中页面
                if (!(mFragment instanceof VideoBusyFragment)) {
                    mFragment = new VideoBusyFragment();
                }

                break;
            case 5://呼叫等待页面
                if (!(mFragment instanceof VideoIdleFragment)) {
                    mFragment = new VideoIdleFragment();
                }

                break;
        }

        if (mFragment == null) {
            return;
        }

        SafeSharePreferenceUtils.saveBoolean(Constants.Fields.VIDEO_STATUS, true);

        ActivityUtils.addFragmentToActivity(getChildFragmentManager(), mFragment, R.id.sliding_view);

//        switch (type) {
//            case 2://接收方
//            case 6:
//                setBackgroundView();
//                EventBus.getDefault().post(new CallVideoEvent(activity.getVideoType()));
//
//                break;
//        }

    }

    /**
     * 设置背景图片
     */
    public void setBackgroundView() {

        BaseUser baseUser = ((VideoChatActivity) getActivity()).getBaseUser();
        if (baseUser == null) {
        }

        if (bgImageView != null) {
            bgImageView.setVisibility(View.GONE);
        }


    }


    /**
     * 视频呼叫事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CallVideoEvent event) {
        if (event == null) {
            return;
        }

        final int type = event.getType();
        switch (type) {
            case 1: // 视频呼叫事件
            case 2: // 语音呼叫事件
                if (mHandler == null) {
                    mHandler = new Handler();
                }

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //开启视频模式
                        App.getInstance().getRtcEngine().enableVideo();
                        //暂停发送本地视频流:True: 不发送本地视频流;False: 发送本地视频流
                        if (type == 1) {
                            App.getInstance().getRtcEngine().muteLocalVideoStream(false);
                        } else {
                            App.getInstance().getRtcEngine().muteLocalVideoStream(true);
                        }
                        //将自己静音:True:麦克风静音;False:取消静音
                        App.getInstance().getRtcEngine().muteLocalAudioStream(false);
                        //暂停所有远端视频流:True: 停止播放接收到的所有视频流;False: 允许播放接收到的所有视频流
                        App.getInstance().getRtcEngine().muteAllRemoteVideoStreams(false);
                        //切换音频输出方式:扬声器或听筒。True:音频输出至扬声器;False:音频输出至听筒
                        App.getInstance().getRtcEngine().setEnableSpeakerphone(true);
                        //默认播放对方的声音
                        App.getInstance().getRtcEngine().muteRemoteAudioStream((int) ((VideoChatActivity) getActivity()).getBaseUser().getUserId(), false);
                        setupChannel();

                    }
                }, 500);
                break;
        }
    }

    /**
     * 设置背景是否显示
     *
     * @param isVisiable true为显示
     */
    public void setBgDisplay(boolean isVisiable) {
        if (isVisiable) {
            bgImageView.setVisibility(View.VISIBLE);
            bgImageView.setImageResource(R.drawable.video_close_bg);
        } else {
            bgImageView.setVisibility(View.GONE);
        }
    }

    /**
     * 对方的图像是大图
     */
    public void setRemoteVideoBig() {
        App.getInstance().getRtcEngine().setupLocalVideo(new VideoCanvas(mLocalView));
        App.getInstance().getRtcEngine().setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uId));
    }

    /**
     * 自己的图像是大图
     */
    public void setLocalVideoBig() {
        App.getInstance().getRtcEngine().setupLocalVideo(new VideoCanvas(remoteView));
        App.getInstance().getRtcEngine().setupRemoteVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, uId));
    }

    /**
     * 获取对方视频开关状态改变
     */
    public void getRemoteVideoStatus(final int uid, final boolean enabled) {
        if (this.uId == uid) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (enabled) {
                        isRemoteVideoOpen = false;
                        Utils.showToastShortTime("对方关闭了摄像头");
                        if ((mFragment instanceof VideoFragment)) {
                            ((VideoFragment) mFragment).setRemoteVideoOpen(false);
                        }
                        if (isLocalVideoOpen) {
                            setBgDisplay(false);
                            setLocalVideoBig();
                            setPrarentLayout(0, 0);
                        } else {
                            setBgDisplay(true);
                        }
                    } else {
                        isRemoteVideoOpen = true;
                        if ((mFragment instanceof VideoFragment)) {
                            ((VideoFragment) mFragment).setRemoteVideoOpen(true);
                        }
                        setBgDisplay(false);
                        setRemoteVideoBig();
                        if (isLocalVideoOpen) {
                            setPrarentLayout(150, 170);
                        } else {
                            setPrarentLayout(0, 0);
                        }
                    }
                }
            });
        }
    }

//    /**
//     * 视频中，接听电话状态事件
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(VideoPhoneStateEvent event) {
//
//        if (event == null) {
//            return;
//        }
//
//        if (activity == null) {
//            return;
//        }
//
//        if (activity.getBaseUser() == null) {
//            return;
//        }
//
//        if (activity.getBaseUser().getUserId() == 0) {
//            return;
//        }
//
//
//        /**
//         * 视频类型状态
//         * 0 接收对方的电话状态通知
//         * 1 本地电话状态通知 响铃（接听中）,摘机（有呼入和呼出）
//         * 2 本地电话状态通知 空闲（无呼入或已挂机）
//         */
//        switch (event.getVideoTypeStatus()) {
//            case 0:
//                if (event.getMessageModel() == null) {
//                    return;
//                }
//
//                final TextView videoHintTextView = (TextView) rootView.findViewById(R.id.video_hint_text_view);
//                videoHintTextView.setText(event.getMessageModel().getMessageTips());
//                videoHintTextView.setVisibility(View.VISIBLE);
//                if (event.getMessageModel().getMessageType() == 50007) {// 视频恢复
//                    if (mHandler == null) {
//                        mHandler = new Handler();
//                    }
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            videoHintTextView.setVisibility(View.GONE);
//                        }
//                    }, 3000);
//                }
//
//                break;
//
//            case 1:
//
//                SafeSharePreferenceUtils.saveBoolean(Constants.Fields.VIDEO_STATUS, true);
//
//                if (mFragment instanceof VideoIdleFragment) {
//                    if (((VideoIdleFragment) mFragment).isVideoCall()) {
//                        RongIMutils.canlceCallRequest(activity.getBaseUser().getUserId(), activity.getRoomNumber(), true);
//                    }
//
//                } else if (mFragment instanceof VideoWaitFragment) {
//                    // 插如一条未接来电记录
//                    // TODO: 2017/12/11
////                    DBUtils.getInstance().saveCallHistory(activity.getBaseUser(), 1, activity.getIsFriend(), 0, activity.getRoomNumber());
//
//                    // 拒接
//                    RongIMutils.refuseRequestVideo(50011, activity.getBaseUser().getUserId(),
//                            activity.getRoomNumber(), true);
//                } else if (mFragment instanceof VideoFragment) {
//                    //暂停发送本地视频流:True: 不发送本地视频流;False: 发送本地视频流
//                    App.getInstance().getRtcEngine().muteLocalVideoStream(true);
//                    //将自己静音:True:麦克风静音;False:取消静音
//                    App.getInstance().getRtcEngine().muteLocalAudioStream(true);
//                    //暂停所有远端视频流:True: 停止播放接收到的所有视频流; False: 允许播放接收到的所有视频流
//                    App.getInstance().getRtcEngine().muteAllRemoteVideoStreams(true);
//                    //切换音频输出方式:扬声器或听筒。True:音频输出至扬声器;False:音频输出至听筒
//                    App.getInstance().getRtcEngine().setEnableSpeakerphone(false);
//                    //静音 True:麦克风静音;False:取消静音
//                    App.getInstance().getRtcEngine().muteAllRemoteAudioStreams(true);
//
//                    //通知对方接听电话状态
//                    RongIMutils.sendPhoneState2OtherUser(activity.getBaseUser().getUserId(), 50006);
//
//                }
//                Utils.releaseSource();
//
//                break;
//
//            case 2:
//
//                SafeSharePreferenceUtils.saveBoolean(Constants.Fields.VIDEO_STATUS, true);
//
//                if (mFragment instanceof VideoFragment) {
//                    //暂停发送本地视频流:True: 不发送本地视频流;False: 发送本地视频流
//                    if (isLocalVideoOpen) {
//                        App.getInstance().getRtcEngine().muteLocalVideoStream(false);
//                    }
//                    //将自己静音:True:麦克风静音;False:取消静音
//                    App.getInstance().getRtcEngine().muteLocalAudioStream(false);
//                    //暂停所有远端视频流:True: 停止播放接收到的所有视频流;False: 允许播放接收到的所有视频流
//                    App.getInstance().getRtcEngine().muteAllRemoteVideoStreams(false);
//                    //切换音频输出方式:扬声器或听筒。True:音频输出至扬声器;False:音频输出至听筒
//                    App.getInstance().getRtcEngine().setEnableSpeakerphone(true);
//                    //静音 True:麦克风静音;False:取消静音
//                    App.getInstance().getRtcEngine().muteAllRemoteAudioStreams(false);
//                    RongIMutils.sendPhoneState2OtherUser(activity.getBaseUser().getUserId(), 50007);
//                }
//
//                break;
//
//        }
//
//
//    }

    /**
     * 事件页面监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoEvent event) {
        if (event == null) {
            return;
        }

        if (activity == null) {
            return;
        }

        switch (event.getType()) {
            case 50001:// 请求通话

                if (type != 5) {
                    type = 5;
                    ((VideoChatFragment) mFragment).setType(type);
                    ((VideoChatFragment) mFragment).addFragmentToActivity(type);
                }

                break;
            case 50003://请求拒绝
                if (!TextUtils.isEmpty(event.getRoomNumber()) && event.getRoomNumber().equals(activity.getRoomNumber())) {
                    Utils.showToastShortTime("TA现在可能不方便跟您视频，稍后再试吧！");
                    activity.finish();
                }

                break;
            case 50009:// 会话结束
                if (!TextUtils.isEmpty(event.getRoomNumber()) && event.getRoomNumber().equals(activity.getRoomNumber())) {
                    Utils.showToastShortTime("视频已结束");
                    activity.finish();
                }

                break;
            case 50004:// 取消呼叫业务

                if (!TextUtils.isEmpty(event.getRoomNumber()) && event.getRoomNumber().equals(activity.getRoomNumber())) {
                    Utils.showToastShortTime("对方已取消呼叫");
                    activity.finish();
                }

                break;

            case 106:// 关闭页面
                activity.finish();
                break;
        }
    }


    /**
     * 初始化声网视频引擎
     */
    private void setupRtcEngine() {
        App.getInstance().setRtcEngine();
        // setup engine event activity
        App.getInstance().setEngineEventHandlerActivity((VideoChatActivity) getActivity());
        App.getInstance().getRtcEngine().enableVideo();
    }

    /**
     * 初始化本地视频控件
     */
    public void setupLocalViewIsCreated() {
        if (this.mLocalView == null) {
            // local view has not been added before
            if (localViewContainer == null) {
                localViewContainer = (FrameLayout) rootView.findViewById(R.id.local_surface_view_layout);
            }
            localViewContainer.removeAllViews();

            SurfaceView localView = App.getInstance().getRtcEngine().CreateRendererView(App.getInstance());
            this.mLocalView = localView;
            localView.setZOrderOnTop(true);
            localView.setZOrderMediaOverlay(true);
            localViewContainer.addView(localView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            if (localViewContainer == null) {
                localViewContainer = (FrameLayout) rootView.findViewById(R.id.local_surface_view_layout);
            }

            this.mLocalView.setZOrderOnTop(true);
            this.mLocalView.setZOrderMediaOverlay(true);
            localViewContainer.removeAllViews();
            localViewContainer.addView(this.mLocalView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        }

        App.getInstance().getRtcEngine().enableVideo();
        /**
         * 该方法设置本地视频显示信息
         * 应用程序通过调用此接口绑定本地视频流的显示视窗(view),并设置视频显示模式
         */
        App.getInstance().getRtcEngine().setupLocalVideo(new VideoCanvas(this.mLocalView));
    }

    /**
     * 初始化远程画面
     */
    public void setupRemoteView(final int uid, int width, int height, int elapsed) {
        this.uId = uid;
        if (remoteUserView == null) {
            remoteUserView = (FrameLayout) rootView.findViewById(R.id.remote_video_view_layout);
        }

        if (remoteUserView.getChildCount() < 1) {
            // ensure container is added
            remoteView = RtcEngine.CreateRendererView(App.getInstance());
            remoteUserView.addView(remoteView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            remoteView = (SurfaceView) remoteUserView.getChildAt(0);
        }

        if (remoteView == null) {
            return;
        }

        remoteView.setZOrderOnTop(false);
        remoteView.setZOrderMediaOverlay(false);

        App.getInstance().getRtcEngine().enableVideo();
        int successCode = App.getInstance().getRtcEngine().setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        if (successCode < 0) {
            final SurfaceView finalRemoteView = remoteView;
            if (mHandler == null) {
                mHandler = new Handler();
            }

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**
                     * 设置远端视频显示属性
                     * 该方法绑定远程用户和显示视图,即设定 uId 指定的用户用哪个视图显示。
                     */
                    App.getInstance().getRtcEngine().setupRemoteVideo(new VideoCanvas(finalRemoteView, VideoCanvas.RENDER_MODE_FIT, uid));
                    finalRemoteView.invalidate();
                }
            }, 500);

        }
    }

    /**
     * 加入视频聊天室
     * String channelKey,
     * String channelName,
     * String optionalInfo,
     * int optionalUid
     */
    private void setupChannel() {
        final String channelId = ((VideoChatActivity) getActivity()).getRoomNumber();

        App.getInstance().getRtcEngine().joinChannel(Constants.KEY.AGORA_APP_KEY, channelId, "", (int) SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
    }


    public Fragment getmFragment() {
        return mFragment;
    }

    public void setmFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }


    /**
     * 在onResume中执行一下本地
     */
    public void setonResume() {
        if (isRemoteVideoOpen) {
            setBgDisplay(false);
            setRemoteVideoBig();
            if (isLocalVideoOpen) {
                setupLocalViewIsCreated();
                setPrarentLayout(150, 170);
            } else {
                setPrarentLayout(0, 0);
            }
        } else {
            if (isLocalVideoOpen) {
                setBgDisplay(false);
                setupLocalViewIsCreated();
                setLocalVideoBig();
                setPrarentLayout(0, 0);
            } else {
                setPrarentLayout(0, 0);
                setBgDisplay(true);
            }
        }
    }

    public SlideMenuDrawerLayout getSlideMenuDrawerLayout() {
        return slideMenuDrawerLayout;
    }

    public void setSlideMenuDrawerLayout(SlideMenuDrawerLayout slideMenuDrawerLayout) {
        this.slideMenuDrawerLayout = slideMenuDrawerLayout;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        App.getInstance().getRtcEngine().leaveChannel();
        App.getInstance().setEngineEventHandlerActivity(null);

        SafeSharePreferenceUtils.saveBoolean(Constants.Fields.VIDEO_STATUS, false);

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;

        if (mLocalView != null) {
            mLocalView = null;
        }

        if (remoteView != null) {
            remoteView = null;
        }

        if (mPresenter != null) {
            mPresenter = null;
        }

        if (localViewContainer != null) {
            localViewContainer.removeAllViews();
            localViewContainer = null;
        }

        if (remoteUserView != null) {
            remoteUserView.removeAllViews();
            remoteUserView = null;
        }

        if (slideMenuDrawerLayout != null) {
            slideMenuDrawerLayout.removeAllViews();
            slideMenuDrawerLayout = null;
        }

        if (prarentLayout != null) {
            prarentLayout.removeAllViews();
            prarentLayout = null;
        }

        if (bgImageView != null) {
            bgImageView.destroyDrawingCache();
            bgImageView = null;
        }

        if (mFragment != null) {
            mFragment = null;
        }

        if (activity != null) {
            activity = null;
        }

    }

    public void setLocalVideoOpen(boolean localVideoOpen) {
        isLocalVideoOpen = localVideoOpen;
    }

    public boolean isLocalVideoOpen() {
        return isLocalVideoOpen;
    }

    public boolean isRemoteVideoOpen() {
        return isRemoteVideoOpen;
    }

    public void setRemoteVideoOpen(boolean remoteVideoOpen) {
        Logger.d("=======remoteVideoOpen=======" + remoteVideoOpen);
        isRemoteVideoOpen = remoteVideoOpen;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
