package cn.bjhdltcdn.p2plive.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.UpdateFocusOnListEvent;
import cn.bjhdltcdn.p2plive.event.UpdatePayResultEvent;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.ui.dialog.UserDetailDialog;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.SlideMenuDrawerLayout;


/**
 * 占线状态
 */
public class VideoBusyFragment extends VideoBaseFragment {

    private View rootView;
    private UserPresenter userPresenter;
    private TextView attentionView;
    /**
     * 关注状态
     * 1 双向关注，2 当前用户单向关注对方，3 双向不关注,4 对方关注当前用户，当前用户不关注对方
     */
    private int status;

    //用户余额
    private int userBalance;

    private Handler handler;
    // 当前位置
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPresenter = new UserPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_video_busy_layout, null);
        }

        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public View getUIView(int id) {
        if (rootView != null) {
            return rootView.findViewById(id);
        } else {
            return null;
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (rootView != null) {

            Fragment fragment = getParentFragment();
            if (fragment != null) {
                if (fragment instanceof VideoChatFragment) {
                    VideoChatFragment videoChatFragment = (VideoChatFragment) fragment;
                    SlideMenuDrawerLayout slideMenuDrawerLayout = videoChatFragment.getSlideMenuDrawerLayout();
                    if (slideMenuDrawerLayout != null) {
                        //关闭滑动
                        slideMenuDrawerLayout.setEnabledScroll(false);
                    }
                }
            }


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


            View attentionViewLayout = rootView.findViewById(R.id.attention_view_layout);
            if (attentionViewLayout != null) {
                attentionView = (TextView) attentionViewLayout.findViewById(R.id.attention_view);
                attentionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userPresenter != null) {
//                            userPresenter.attentionOperation(status, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), .getUserId());
                        }
                    }
                });

                // 视频状态
                TextView videoStatusView = (TextView) attentionViewLayout.findViewById(R.id.video_status_view);
                videoStatusView.setText("占线中");

                if (getVideoChatActivity() != null) {
                    BaseUser baseUser = getVideoChatActivity().getBaseUser();

                    //用户昵称
                    TextView nicknameView = (TextView) attentionViewLayout.findViewById(R.id.tv_nickname);
                    nicknameView.setText(baseUser.getNickName());

                    //头像
                    final ImageView userImageView = (ImageView) attentionViewLayout.findViewById(R.id.user_image_view);
                    Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), userImageView);
                    userImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                BaseUser baseUser = getVideoChatActivity().getBaseUser();
                                if (baseUser != null) {
                                    UserDetailDialog userDetailDialog = new UserDetailDialog();
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



            GreenDaoUtils.getInstance().getBaseUser(myUserId, new GreenDaoUtils.ExecuteCallBack() {
                @Override
                public void callBack(Object object) {
                    if (object instanceof BaseUser) {
                        BaseUser baseUser = (BaseUser) object;
                        //自己头像
                        ImageView userImageView2 = (ImageView) getUIView(R.id.user_image_view_2);
                        Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), userImageView2);

                    }
                }
            });

            View closeView = getUIView(R.id.video_close_view);
            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        getActivity().finish();
                    }

                }
            });

        }
    }

    @Override
    public void updateUIView(String type, Object object) {

        if (!isAdded() || isHidden()) {
            return;
        }

//if (InterfaceUrl.URL_QUERYUSERBALANCE.equals(type)) {
//            if (object instanceof QueryUserBalanceResponse) {
//                QueryUserBalanceResponse response = (QueryUserBalanceResponse) object;
//                userBalance = response.getUserBalance();
//                KLog.d("userBalance === " + userBalance);
//            }
//
//        }
    }

//    @Override
//    public void updateUIAttentionStatus(int status) {
//
//        if (!isAdded() || isHidden()) {
//            return;
//        }
//
//        if (getActivity() == null || getActivity().isFinishing()) {
//            return;
//        }
//
//        if (attentionView == null) {
//            return;
//        }
//
//        this.status = status;
//        if (attentionView != null) {
//            //1 双向关注，2 当前用户单向关注对方，3 双向不关注,
//            String text = null;
//            switch (status) {
//                case 1:
//                case 2:
//                    text = "已关注";
//                    attentionView.setEnabled(false);
//                    attentionView.setBackgroundResource(R.drawable.shape_round_60_solid_666666);
//                    attentionView.setTextColor(getResources().getColor(R.color.white));
//                    break;
//
//                case 3:
//                case 4:
//                    text = "关注";
//                    break;
//
//            }
//            attentionView.setText(text);
//        }
//    }


    //接收充值成功消息

    public void onEventMainThread(UpdatePayResultEvent event) {
        if (event == null) {
            return;
        }
//        final BaseUser baseUser = DBUtils.getInstance().getBaseUser();
//        if (baseUser != null && mPresenter != null) {
//            userPresenter.queryUserBalance(baseUser.getUserId());
//        } else {
//            getActivity().finish();
//        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateFocusOnListEvent event) {
        if (event != null) {
            attentionView.setEnabled(false);
            attentionView.setText("已关注");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        if (rootView != null) {
            rootView = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
