package cn.bjhdltcdn.p2plive.ui.dialog;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.AttentionStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetFriendListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetUserInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateUserStatusResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ReportType;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;


/**
 * 多人视频聊天页面的查看用户详情
 */
public class GroupVideoUserDetailDialog extends DialogFragment implements BaseView {

    private View rootView;

    // 对方用户基本信息
    private RoomBaseUser roomBaseUser;
    // 房间id
    private long roomId;
    // 关注控件
    private TextView attentionView;
    // 关注状态
    private int attentionStatus;
    private TextView tv_disablemsg;

    // 头像
    private ImageView userIconImageView;
    //麦上用户id列表
//    private List<Long> list;

//    public void setList(List<Long> list) {
//        this.list = list;
//    }

    //用户身份1--主持人 2管理 3观众
    private int userRole;

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    private UserPresenter userPresenter;
    private CommonPresenter commonPresenter;
    private ChatRoomPresenter chatRoomPresenter;

    public void setDate(BaseUser baseUser, long roomId) {
        roomBaseUser = new RoomBaseUser();
        roomBaseUser.setUserId(baseUser.getUserId());
        roomBaseUser.setUserIcon(baseUser.getUserIcon());
        roomBaseUser.setNickName(baseUser.getNickName());
        this.roomId = roomId;
    }

    private UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public CommonPresenter getCommonPresenter() {
        if (commonPresenter == null) {
            commonPresenter = new CommonPresenter(this);
        }
        return commonPresenter;
    }

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_video_group_user_detail, null);
        rootView.setAnimation(AnimationUtils.loadAnimation(App.getInstance(), R.anim.popup_enter));
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        // 触摸内容区域外的需要关闭对话框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof ViewGroup) {
                    View layoutView = ((ViewGroup) v).getChildAt(0);
                    if (layoutView != null) {
                        float y = event.getY();
                        float x = event.getX();
                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
                        if (!rect.contains((int) x, (int) y)) {
                            dismiss();
                        }
                        rect.setEmpty();
                        rect = null;
                    }
                }
                return false;
            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (userRole == 1 || userRole == 2 || userRole == 4) {
            // 禁言
            tv_disablemsg = (TextView) rootView.findViewById(R.id.disables_msg_view);
            tv_disablemsg.setVisibility(View.VISIBLE);
            // 移除房间
            rootView.findViewById(R.id.remove_view).setVisibility(View.VISIBLE);
        }
        // 举报
        rootView.findViewById(R.id.report_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
                selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
                    @Override
                    public void reportItemClick(Object object) {
                        if (roomBaseUser != null) {
                            if (object instanceof ReportType) {
                                ReportType reportType = (ReportType) object;
                                getCommonPresenter().reportOperation(roomBaseUser.getUserId(), 3, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomBaseUser.getUserId(), reportType.getReportTypeId());
                            }
                        }
                    }
                });
                selectorReportContentDialog.show(getFragmentManager());
            }
        });

        // 禁言
        rootView.findViewById(R.id.disables_msg_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type;
                if (roomBaseUser.getIsGag() == 2) {
                    type = 1;//取消
                } else {
                    type = 2;//开启
                }
                getChatRoomPresenter().banningComments(roomId, type, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomBaseUser.getUserId());
            }
        });

        // 移除房间
        rootView.findViewById(R.id.remove_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomTwoBtnDialog dialog = new BottomTwoBtnDialog();
                dialog.setDialogText("确定将TA移除房间？", "取消", "确定");

                dialog.setRightBottomViewClickListener(new BottomTwoBtnDialog.RightBottomViewClickListener() {
                    @Override
                    public void onRightClick() {
                        getChatRoomPresenter().updateUserStatus(roomBaseUser.getUserId(), roomId, 2, 0, "");
                    }
                });

                dialog.show(getChildFragmentManager());
            }
        });

        // 发送消息
        rootView.findViewById(R.id.send_msg_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomBaseUser != null) {
                    RongIMutils.startToConversation(getContext(), roomBaseUser.getUserId() + "", roomBaseUser.getNickName());
                }
            }
        });

        // 关注
        attentionView = (TextView) rootView.findViewById(R.id.attention_view);


        if (roomBaseUser == null) {
            return;
        }

        // 头像
        userIconImageView = (ImageView) rootView.findViewById(R.id.img_icon);
        Utils.ImageViewDisplayByUrl(roomBaseUser.getUserIcon(), userIconImageView);

        // 昵称
        TextView nickNameView = (TextView) rootView.findViewById(R.id.nick_name_view);
        nickNameView.setText(roomBaseUser.getNickName());

        //用户ID
        TextView userIdView = (TextView) rootView.findViewById(R.id.user_id_view);
        userIdView.setText("ID: " + roomBaseUser.getUserId());

        // 查询关注状态
        getUserPresenter().attentionStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomBaseUser.getUserId());
        // 获取用户详情
        getChatRoomPresenter().getUserInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomBaseUser.getUserId(), roomId);

        // 获取好友数量
        getUserPresenter().getFriendList(roomBaseUser.getUserId(), 1, 1);


    }

    @Override
    public void dismiss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            super.dismiss();
        }
    }

    public void show(FragmentManager manager) {
        show(manager, "dialog");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
            // 解决java.lang.IllegalStateException: Can not perform this action问题
            final FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETUSERINFO:
                if (object instanceof GetUserInfoResponse) {
                    GetUserInfoResponse response = (GetUserInfoResponse) object;
                    if (response.getCode() == 200) {
                        User user = response.getUser();
                        if (user != null) {

                            if (rootView == null) {
                                return;
                            }
                            if (user.getRoomBaseUser() != null) {
                                if (user.getRoomBaseUser().getIsGag() == 2) {//已经被禁言
                                    if (tv_disablemsg != null) {
                                        tv_disablemsg.setText("取消禁言");
                                    }
                                    roomBaseUser.setIsGag(2);
                                }
                            }

                            // 粉丝数
                            TextView numFunsView = rootView.findViewById(R.id.num_funs_view);
                            numFunsView.setText("粉丝：" + user.getFansCount());

                            //学校
                            TextView schoolName = rootView.findViewById(R.id.tv_school);
                            if (user.getSchoolInfo() != null) {
                                schoolName.setText(user.getSchoolInfo().getSchoolName());
                            }

                            // 年龄
                            TextView ageView = rootView.findViewById(R.id.tv_age);
                            ageView.setText("年龄：" + user.getAge());
                        }

                    }

                }
                break;
            case InterfaceUrl.URL_ATTENTIONSTATUS:
                if (object instanceof AttentionStatusResponse) {
                    AttentionStatusResponse response = (AttentionStatusResponse) object;
                    if (response.getCode() == 200) {
                        this.attentionStatus = response.getAttentionStatus();

                        attentionView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (roomBaseUser != null) {
                                    getChatRoomPresenter().attentionOperation((attentionStatus == 1 || attentionStatus == 2) ? 2 : 1, roomId, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomBaseUser.getUserId());
                                }
                            }
                        });

                        switch (attentionStatus) {
                            case 1://双向关注
                            case 2://当前用户单向关注对方  不能发起视频
                                attentionView.setText("已关注");
                                attentionView.setEnabled(false);
                                break;
                            case 3: // 双向不关注 视频和聊天都不可用
                            case 4: // 对方关注我，我不关注对方
                                attentionView.setText("关注");
                                attentionView.setEnabled(true);
                                break;
                            default:
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_ATTENTIONOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {//关注类型(1-->关注;2-->取消关注)
                        int type = (attentionStatus == 1 || attentionStatus == 2) ? 2 : 1;
                        switch (type) {
                            case 1: // 1-->关注
                                attentionView.setText("已关注");
                                attentionView.setEnabled(false);
                                break;

                            case 2:// 2-->取消关注
                                attentionView.setText("关注");
                                attentionView.setEnabled(true);
                                break;
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_REPORTOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    Utils.showToastShortTime(response.getMsg());
                }
                break;
            case InterfaceUrl.URL_UPDATEUSERSTATUS:
                if (object instanceof UpdateUserStatusResponse) {
                    UpdateUserStatusResponse response = (UpdateUserStatusResponse) object;
                    Utils.showToastShortTime(response.getMsg());
                    if (response.getCode() == 200) {
                        dismiss();
                    }
                }
                break;

            case InterfaceUrl.URL_GETFRIENDLIST:
                if (object instanceof GetFriendListResponse) {
                    GetFriendListResponse response = (GetFriendListResponse) object;
                    if (response.getCode() == 200) {
                        // 好友数
                        if (rootView != null) {
                            TextView numFriendsView = (TextView) rootView.findViewById(R.id.num_friends_view);
                            numFriendsView.setText("好友：" + response.getList().size());
                        }

                    }
                }
                break;
            case InterfaceUrl.URL_BANNINGCOMMENTS:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {
                        if (roomBaseUser.getIsGag() == 2) {//已经被禁言
                            Utils.showToastShortTime("取消禁言成功");
                            tv_disablemsg.setText("禁言");
                            roomBaseUser.setIsGag(1);
                        } else {
                            Utils.showToastShortTime("禁言成功");
                            tv_disablemsg.setText("取消禁言");
                            roomBaseUser.setIsGag(2);
                        }
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
