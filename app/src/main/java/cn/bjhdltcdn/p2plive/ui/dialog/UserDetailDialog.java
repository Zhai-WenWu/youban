package cn.bjhdltcdn.p2plive.ui.dialog;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateFocusOnListEvent;
import cn.bjhdltcdn.p2plive.httpresponse.AttentionStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetFriendListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetUserInfoResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ReportType;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;


/**
 * 用户详情对话框
 * 用于视频UI查看
 */
public class UserDetailDialog extends DialogFragment implements BaseView {
    private View rootView;
    private BaseUser baseUser;
    private CommonPresenter commonPresenter;
    private UserPresenter userPresenter;
    /**
     * 关注状态
     * 1 双向关注，2 当前用户单向关注对方，3 双向不关注,4 双向不关注
     */
    private int status;
    private int statusType;
    private Handler handler;
    private TextView attentionView;


    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
        userPresenter = new UserPresenter(this);
        commonPresenter = new CommonPresenter(this);
        userPresenter = new UserPresenter(this);
        if (handler == null) {
            handler = new Handler();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_detail_dialog_layout, null);

        // 触摸内容区域外的需要关闭对话框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (rootView == null) {
            return;
        }

        if (getBaseUser() == null || getBaseUser().getUserId() == 0) {
            return;
        }

        userPresenter.getUserInfo(getBaseUser().getUserId(), getBaseUser().getUserId());

    }

    private void initView(User user) {


        // 头像
        try {
            ImageView userImageView = (ImageView) rootView.findViewById(R.id.img_icon);
            Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), userImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 昵称，id
        TextView userNameView = (TextView) rootView.findViewById(R.id.nick_name_view);
        TextView userIdView = (TextView) rootView.findViewById(R.id.user_id_view);

        if (baseUser != null) {
            userNameView.setText(baseUser.getNickName());//String.format("%s: %s: %s", baseUser.getNickName(), baseUser.getSex() == 1 ? "男" : "女", baseUser.getAge() > 0 ?  (baseUser.getAge()+ "岁") : "")
            userIdView.setText("ID:" + baseUser.getUserId());
        }

        // 粉丝数
        TextView numFunsView = (TextView) rootView.findViewById(R.id.num_funs_view);
        numFunsView.setText("粉丝：" + user.getFansCount());

        //学校
        TextView schoolName = rootView.findViewById(R.id.tv_school);
        if (user.getSchoolInfo() != null) {
            schoolName.setText(user.getSchoolInfo().getSchoolName());
        }

        // 年龄
        TextView ageView = rootView.findViewById(R.id.tv_age);
        ageView.setText("年龄：" + user.getAge());


        // 关注
        attentionView = (TextView) rootView.findViewById(R.id.attention_view);

        try {
            if (SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) == baseUser.getUserId()) {
                attentionView.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        rootView.findViewById(R.id.report_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
                selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
                    @Override
                    public void reportItemClick(Object object) {
                        if (userPresenter != null && baseUser != null) {
                            if (object instanceof ReportType) {
                                ReportType reportType = (ReportType) object;
                                commonPresenter.reportOperation(baseUser.getUserId(), 3, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId(), reportType.getReportTypeId());
                            }

                        }
                    }
                });
                selectorReportContentDialog.show(getFragmentManager());

            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        handler = null;

        baseUser = null;

        rootView = null;

    }

    @Override
    public void updateView(String apiName, Object object) {

        if (InterfaceUrl.URL_GETUSERINFO.equals(apiName)) {
            if (object instanceof GetUserInfoResponse) {
                GetUserInfoResponse response = (GetUserInfoResponse) object;
                if (response.getCode() == 200 && response.getUser() != null) {
                    if (isDetached()) {
                        return;
                    }

                    if (handler == null || userPresenter == null) {
                        return;
                    }
                    initView(response.getUser());

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            userPresenter.attentionStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), getBaseUser().getUserId());
                        }
                    }, 500);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            userPresenter.getFriendList(getBaseUser().getUserId(), 100, 1);
                        }
                    }, 1000);
                }
            }
        } else if (InterfaceUrl.URL_ATTENTIONSTATUS.equals(apiName)) {
            if (object instanceof AttentionStatusResponse) {
                AttentionStatusResponse response = (AttentionStatusResponse) object;
                if (response.getCode() == 200) {
                    if (isDetached()) {
                        return;
                    }

                    if (attentionView == null) {
                        return;
                    }

                    status = response.getAttentionStatus();
                    switch (status) {
                        case 1:
                        case 2:
                            attentionView.setText("已关注");
                            attentionView.setEnabled(false);
                            break;

                        case 3:
                        case 4:
                            attentionView.setEnabled(true);
                            attentionView.setText("关注");
                            break;
                    }


                    try {

                        if (SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) == baseUser.getUserId()) {
                            attentionView.setEnabled(false);
                        }

                        attentionView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                statusType = 1;
                                switch (status) {
                                    case 1:
                                    case 2:
                                        statusType = 2;
                                        break;
                                }
                                userPresenter.attentionOperation(statusType, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), getBaseUser().getUserId());
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        } else if (InterfaceUrl.URL_ATTENTIONOPERATION.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.getCode() == 200) {
                    if (attentionView == null) {
                        return;
                    }
                    attentionView.setEnabled(false);
                    if (statusType == 1) {
                        attentionView.setText("已关注");
                        //发送消息更新粉丝或者关注列表或者好友列表
                        EventBus.getDefault().post(new UpdateFocusOnListEvent(0));
                    } else {
                        attentionView.setEnabled(true);
                        attentionView.setText("关注");
                    }

                }
            }
        } else if (InterfaceUrl.URL_GETFRIENDLIST.equals(apiName)) {

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

        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
