package cn.bjhdltcdn.p2plive.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.CallVideoEvent;
import cn.bjhdltcdn.p2plive.event.VideoStatusEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.ui.dialog.UserDetailDialog;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.SlideMenuDrawerLayout;


/**
 * 空闲状态
 */
public class VideoIdleFragment extends VideoBaseFragment {

    private View rootView;


    /**
     * 是否发起呼叫
     */
    private boolean isVideoCall;

    /**
     * 呼叫等待时间
     */
    private int videoCallTime = 0;

    /**
     * 计时任务
     */
    private TimerTask task;
    private Timer timer;

    private UserDetailDialog userDetailDialog;

    private Animation animation;
    private BaseResponse baseResponse;//检测视频状态
    private VideoChatFragment videoChatFragment;
    private int callType = 1;//呼叫类型 1视频 2语音

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_video_idle_layout, null);
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

        if (rootView != null) {

            Fragment fragment = getParentFragment();
            if (fragment != null) {
                if (fragment instanceof VideoChatFragment) {
                    videoChatFragment = (VideoChatFragment) fragment;
                    SlideMenuDrawerLayout slideMenuDrawerLayout = videoChatFragment.getSlideMenuDrawerLayout();
                    if (slideMenuDrawerLayout != null) {
                        //关闭滑动
                        slideMenuDrawerLayout.setEnabledScroll(false);
                    }
                }
            }


            //蒙层
            if (getUIView(R.id.over_layer_view).getBackground() != null) {
                getUIView(R.id.over_layer_view).getBackground().setAlpha((int) (255 * 0.7));
            }

            /**
             * 取消按钮
             */
            getUIView(R.id.cancel_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if (getVideoChatActivity().getCallHistory() != null) {
//                        getVideoChatActivity().getCallHistory().setCallStatus(2);
//                        EventBus.getDefault().post(new ChangeCallHistoryEnvet(getVideoChatActivity().getCallHistory()));
//                    }

                    cancelCall();
                }
            });

            /**
             * 取消按钮
             */
            getUIView(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if (getVideoChatActivity().getCallHistory() != null) {
//                        getVideoChatActivity().getCallHistory().setCallStatus(2);
//                        EventBus.getDefault().post(new ChangeCallHistoryEnvet(getVideoChatActivity().getCallHistory()));
//                    }

                    cancelCall();
                }
            });

            /**
             * 视频呼叫按钮
             */
            getUIView(R.id.call_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callType = 1;
                    videoCall();
                }
            });
            /**
             * 语音呼叫按钮
             */
            getUIView(R.id.tv_call_voice).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callType = 2;
                    videoCall();
                }
            });

            if (getVideoChatActivity() != null) {
                final BaseUser baseUser = getVideoChatActivity().getBaseUser();
                if (baseUser != null) {
                    getChatRoomPresenter().checkOneToOneVideo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId());
                    //昵称
                    TextView nicknameView = (TextView) getUIView(R.id.nick_name_view);
                    nicknameView.setText("呼叫" + baseUser.getNickName());

                    // 头像
                    ImageView userImageView = (ImageView) getUIView(R.id.user_image_view);
                    Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), userImageView);
                    //设置背景
                    final ImageView bgView = (ImageView) rootView.findViewById(R.id.bg_view);
                    Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), bgView);
                    userImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (baseUser != null) {
                                    if (userDetailDialog != null) {
                                        userDetailDialog.dismiss();
                                    }

                                    userDetailDialog = new UserDetailDialog();
                                    userDetailDialog.setBaseUser(baseUser);
                                    userDetailDialog.show(getChildFragmentManager(), "dialog");

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }


            }
        }


    }

    /**
     * 取消呼叫
     */
    private void cancelCall() {
        if (isVideoCall) { // 已经发出呼叫请求
            isVideoCall = false;
            //取消呼叫
            if (getVideoChatActivity() == null) {
                return;
            }

            if (getVideoChatActivity().getBaseUser() == null) {
                return;
            }

            if (getVideoChatActivity().getBaseUser().getUserId() == 0) {
                return;
            }
            oneToOneVideo(callType, 5);
            RongIMutils.canlceCallRequest(getVideoChatActivity().getBaseUser().getUserId(),
                    getVideoChatActivity().getRoomNumber(),
                    true);

        } else {// 还没有发出呼叫请求
            App.getInstance().getRtcEngine().leaveChannel();
            getVideoChatActivity().finish();
            isVideoCall = false;
        }
    }


    /**
     * 呼叫
     */
    private void videoCall() {
        if (baseResponse != null) {
            if (baseResponse.getCode() == 200) {
                //显示呼叫动画
                getUIView(R.id.load_layou_view).setVisibility(View.VISIBLE);
                // 呼叫动画的文字
                getUIView(R.id.text_view).setVisibility(View.VISIBLE);

                animation = AnimationUtils.loadAnimation(getActivity(), R.anim.call_laod_anim);
                getUIView(R.id.load_view).startAnimation(animation);

                getUIView(R.id.tv_cancel).setVisibility(View.VISIBLE);
                // 隐藏呼叫按钮
                getUIView(R.id.call_view).setVisibility(View.GONE);
                //隐藏关闭按钮
                getUIView(R.id.cancel_view).setVisibility(View.GONE);

                // 隐藏语音呼叫按钮
                getUIView(R.id.tv_call_voice).setVisibility(View.GONE);

                // 隐藏头像布局
                getUIView(R.id.call_lay_view).setVisibility(View.GONE);

                isVideoCall = true;
                EventBus.getDefault().post(new CallVideoEvent(callType));
                videoChatFragment.setupLocalViewIsCreated();
                if (callType == 2) {
                    videoChatFragment.setLocalVideoOpen(false);
                    videoChatFragment.setRemoteVideoOpen(false);
                    videoChatFragment.setPrarentLayout(0, 0);
                } else {
                    videoChatFragment.setPrarentLayout(150, 170);
                }
                getVideoChatActivity().setVideoType(callType);
                //设置呼叫时间
                setVideoCallUpTime();
                oneToOneVideo(callType, 1);
            } else {
                Utils.showToastShortTime(baseResponse.getMsg());
            }
        }
    }

    /**
     * 设置呼叫等待时间
     */
    private void setVideoCallUpTime() {

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    videoCallTime++;
                    if (videoCallTime >= 30) {
                        stopTimer();
                        if (getVideoChatActivity() == null) {
                            return;
                        }

                        if (getActivity() != null && !getActivity().isFinishing()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showToastShortTime("对方手机可能不在身边，建议稍后再试！");
                                }
                            });
                        }

                        if (getVideoChatActivity().getBaseUser() != null) {
                            oneToOneVideo(callType, 5);
                            RongIMutils.canlceCallRequest(getVideoChatActivity().getBaseUser().getUserId(),
                                    getVideoChatActivity().getRoomNumber(),
                                    true);
                        }

                        getVideoChatActivity().finish();
                    }

                }
            };

            timer = new Timer();
            timer.schedule(task, 1000, 1000);
        }

    }


    /**
     * 检查视频状态事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final VideoStatusEvent event) {

        if (event == null) {
            return;
        }

        if (animation != null) {
            animation.cancel();
        }

        switch (event.getVideoStatus()) {

            case 1://占线
                Utils.showToastShortTime("对方占线，请稍后再试...");
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
        }


    }

    @Override
    public View getUIView(int id) {
        return rootView.findViewById(id);
    }


    @Override
    public void updateUIView(String type, Object object) {
        if (type.equals(InterfaceUrl.URL_CHECKONETOONEVIDEO)) {
            if (object instanceof BaseResponse) {
                baseResponse = (BaseResponse) object;
            }
        }
    }

    public boolean isVideoCall() {
        return isVideoCall;
    }

    private void stopTimer() {
        if (task != null) {
            task.cancel();
        }
        task = null;

        if (timer != null) {
            timer.cancel();
        }

        timer = null;

    }


    @Override
    public void onDetach() {
        super.onDetach();

        Utils.releaseSource();
        stopTimer();
        EventBus.getDefault().unregister(this);

        if (animation != null) {
            animation.cancel();
        }

        if (rootView != null) {
            rootView = null;
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
