package cn.bjhdltcdn.p2plive.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.CallVideoEvent;
import cn.bjhdltcdn.p2plive.httpresponse.AttentionStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ReportType;
import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.VideoChatActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.SelectorReportContentDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.UserDetailDialog;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.SlideMenuDrawerLayout;


/**
 * 视频等待状态(接收方)
 */
public class VideoWaitFragment extends VideoBaseFragment {


    private View rootView;

    private VideoChatActivity activity;
    private BaseUser baseUser;

    private UserDetailDialog userDetailDialog;
    private CommonPresenter commonPresenter;
    private VideoChatFragment videoChatFragment;

    public CommonPresenter getCommonPresenter() {
        if (commonPresenter == null) {
            commonPresenter = new CommonPresenter(this);
        }
        return commonPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (VideoChatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_video_wait_layout, container, false);
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

        // 举报按钮
        rootView.findViewById(R.id.reply_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (baseUser == null) {
                    return;
                }

                //举报
                SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
                selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
                    @Override
                    public void reportItemClick(Object object) {
                        if (baseUser == null) {
                            return;
                        }

                        if (object instanceof ReportType) {
                            ReportType reportType = (ReportType) object;
                            getCommonPresenter().reportOperation(0, 11, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId(), reportType.getReportTypeId());
                        } else {
                            getActivity().finish();
                        }
                    }
                });
                selectorReportContentDialog.show(getFragmentManager());

            }
        });

        //设置背景
        final ImageView bgView = (ImageView) rootView.findViewById(R.id.bg_view);
        long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        GreenDaoUtils.getInstance().getBaseUser(myUserId, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(final Object object) {
                if (object instanceof BaseUser) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BaseUser baseUser = (BaseUser) object;
                            Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), bgView);
                        }
                    });

                }
            }
        });


        //蒙层
        if (getUIView(R.id.over_layer_view).getBackground() != null) {
            getUIView(R.id.over_layer_view).getBackground().setAlpha((int) (255 * 0.7));
        }

        baseUser = activity.getBaseUser();

        if (baseUser != null) {

            // 头像
            ImageView userImageView = (ImageView) rootView.findViewById(R.id.user_image_view);
            Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), userImageView);
            userImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (baseUser != null) {
                            if (userDetailDialog != null && userDetailDialog.isDetached()) {
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

            //昵称
            TextView nickNameView = (TextView) getUIView(R.id.tip_user_view);
            nickNameView.setText(baseUser.getNickName());

            // 拒绝按钮事件
            TextView refuseView = rootView.findViewById(R.id.refuse_view);
            refuseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (baseUser == null) {
                        return;
                    }

                    if (getVideoChatActivity() != null && baseUser != null) {
                        // 插入一条已接来电
//                        DBUtils.getInstance().saveCallHistory(baseUser, 0, getVideoChatActivity().getIsFriend(), 0, getVideoChatActivity().getRoomNumber());
                        oneToOneVideo(activity.getVideoType(), 3);
                        RongIMutils.refuseRequestVideo(50003, getVideoChatActivity().getBaseUser().getUserId(),
                                getVideoChatActivity().getRoomNumber(),
                                true);
                    }

                }
            });


            // 接通按钮事件
            TextView agreeView = rootView.findViewById(R.id.agree_view);
            TextView tv_agree_voice = rootView.findViewById(R.id.tv_agree_voice);
            TextView agree_view_voice = rootView.findViewById(R.id.agree_view_voice);
            TextView tv_video_type = rootView.findViewById(R.id.tv_video_type);

            if (activity.getVideoType() == 1) {
                tv_video_type.setText("邀你视频聊天");
                agreeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoAgree(1);
                    }
                });
                tv_agree_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoAgree(2);
                    }
                });
            } else {
                tv_video_type.setText("邀你语音聊天");
                agreeView.setVisibility(View.GONE);
                tv_agree_voice.setVisibility(View.GONE);
                agree_view_voice.setVisibility(View.VISIBLE);
                agree_view_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        videoAgree(2);
                    }
                });
            }

            //开始播放来电铃声
            Utils.playVoice("ringtones.mp3");
        }


    }

    /**
     * 接通视频
     *
     * @param videoType 1视频通话 2 语音通话
     */
    private void videoAgree(int videoType) {
        // 停止呼叫音乐
        Utils.releaseSource();

        if (videoChatFragment != null) {
            int type = 2;
            EventBus.getDefault().post(new CallVideoEvent(videoType));
            videoChatFragment.setupLocalViewIsCreated();
            if (videoType == 2) {
                videoChatFragment.setLocalVideoOpen(false);
                videoChatFragment.setPrarentLayout(0, 0);
            } else {
                videoChatFragment.setPrarentLayout(150, 170);
            }
            if (activity.getVideoType() == 2) {//对方语音呼叫，并且为接收方
                videoChatFragment.setRemoteVideoOpen(false);
            }
            oneToOneVideo(videoType, 2);
            videoChatFragment.setType(type);
            videoChatFragment.addFragmentToActivity(type);

        }
    }


    @Override
    public View getUIView(int id) {
        return rootView.findViewById(id);
    }


    @Override
    public void updateUIView(String type, Object object) {

        if (!isAdded() || isHidden()) {
            return;
        }
        switch (type) {
            case InterfaceUrl.URL_ATTENTIONSTATUS:
                if (object instanceof AttentionStatusResponse) {
                    AttentionStatusResponse getAttentionStatusResponse = (AttentionStatusResponse) object;
                    if (getAttentionStatusResponse.getCode() == 200) {
                        int attentionStatus = getAttentionStatusResponse.getAttentionStatus();
                        TextView tipView = (TextView) rootView.findViewById(R.id.tip_view);
                        //1 双向关注，2 当前用户单向关注对方，3 双向不关注，4 对方单向关注我,
                        if (attentionStatus == 1) {
                            tipView.setVisibility(View.GONE);
                        } else {
                            tipView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                break;
            case InterfaceUrl.URL_PULLBLACKUSER:
                if (object instanceof BaseResponse) {
                    BaseResponse rsponse = (BaseResponse) object;
                    if (rsponse.getCode() == 200 || rsponse.getCode() == 1) {
                        Utils.showToastShortTime("拉黑成功");
                        if (getVideoChatActivity() != null && baseUser != null) {
                            oneToOneVideo(activity.getVideoType(), 3);
                            RongIMutils.refuseRequestVideo(50003, getVideoChatActivity().getBaseUser().getUserId(),
                                    getVideoChatActivity().getRoomNumber(),
                                    true);
                        }
                    } else {
                        Utils.showToastShortTime("拉黑失败");
                    }
                }
                break;
            case InterfaceUrl.URL_REPORTOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse reportOperationResponse = (BaseResponse) object;
                    //拉黑
                    getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId());
                    Utils.showToastShortTime(reportOperationResponse.getMsg());
                }
                break;
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();

        try {
            if (userDetailDialog != null && !userDetailDialog.isHidden()) {
                userDetailDialog.dismiss();
            }

            if (rootView != null) {
                rootView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.releaseSource();

        if (videoChatFragment != null) {
            videoChatFragment = null;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
