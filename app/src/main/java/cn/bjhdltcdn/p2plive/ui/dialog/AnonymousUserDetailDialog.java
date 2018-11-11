package cn.bjhdltcdn.p2plive.ui.dialog;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.httpresponse.GetUserInfoResponse;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.Utils;
import io.rong.imlib.model.UserInfo;


/**
 * 匿名聊天室用户详情
 */
public class AnonymousUserDetailDialog extends DialogFragment implements BaseView {
    private View rootView;
    private UserInfo userInfo;
    private UserPresenter userPresenter;
    private int userRole;

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
        userPresenter = new UserPresenter(this);
        userPresenter = new UserPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_anonymous_user_detail, null);

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

        if (userInfo != null) {
            String userIdStr = userInfo.getUserId();
            if (userIdStr.startsWith("NM")) {
                userIdStr = userIdStr.substring(2, userIdStr.length());
            }
            userPresenter.getUserInfo(Long.valueOf(userIdStr), Long.valueOf(userIdStr));
        }


    }

    private void initView(User user) {


        // 头像
        try {
            ImageView userImageView = rootView.findViewById(R.id.iv_icon);
            Utils.ImageViewDisplayByUrl(String.valueOf(userInfo.getPortraitUri()), userImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 昵称，id
        TextView userNameView = rootView.findViewById(R.id.tv_nickname);

        if (userInfo != null) {
            userNameView.setText(userInfo.getName());
        }

        //学校
        TextView schoolName = rootView.findViewById(R.id.tv_school);
        if (user.getSchoolInfo() != null) {
            schoolName.setText("学校：" + user.getSchoolInfo().getSchoolName());
        }

        // 位置
        TextView ageView = rootView.findViewById(R.id.tv_city);
        ageView.setText("位置：" + user.getLocation());
        TextView tv_left = rootView.findViewById(R.id.tv_left);
        tv_left.setText("@此用户");
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onLeftClick();
                dismiss();
            }
        });

        if (userRole == 1) {
            View lineView = rootView.findViewById(R.id.line_view);
            lineView.setVisibility(View.VISIBLE);
            TextView tv_right = rootView.findViewById(R.id.tv_right);
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onRightClick();
                    }
                    dismiss();
                }
            });
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        userInfo = null;

        rootView = null;

    }

    public void show(FragmentManager manager) {
        show(manager, "dialog");
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
                    initView(response.getUser());

                }
            }
        }
    }

    public interface OnClickListener {
        void onLeftClick();

        void onRightClick();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
