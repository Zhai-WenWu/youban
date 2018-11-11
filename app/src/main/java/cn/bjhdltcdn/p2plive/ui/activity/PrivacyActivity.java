package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserVideoInfoResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;


/**
 * 隐私
 */
public class PrivacyActivity extends BaseActivity implements BaseView {
    private UserPresenter userPresenter;
    private long userId;
    private int status;//是否接收陌生人来电(1接收,2拒绝),
    private ToggleButton togglebtn_call;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        setTitle();
        init();
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        //getUserPresenter().findUserVideoInfo(userId);
    }

    private void init() {
        TextView tv_blacklist = findViewById(R.id.tv_blacklist);
        tv_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrivacyActivity.this, BlackListActiviity.class));
            }
        });
        togglebtn_call = findViewById(R.id.togglebtn_call);
        togglebtn_call.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getUserPresenter().updateUserVideoInfo(userId, 1);
                } else {
                    getUserPresenter().updateUserVideoInfo(userId, 2);
                }

            }
        });
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("隐私");
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_FINDUSERVIDEOINFO:
                if (object instanceof FindUserVideoInfoResponse) {
                    FindUserVideoInfoResponse infoResponse = (FindUserVideoInfoResponse) object;
                    if (infoResponse.getCode() == 200) {
                        status = infoResponse.getStatus();
                        if (status == 1) {
                            togglebtn_call.setChecked(true);
                        } else {
                            togglebtn_call.setChecked(false);
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATEUSERVIDEOINFO:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    if (baseResponse.getCode() == 200) {
                        if (status == 1) {
                            status = 2;
                        } else {
                            status = 1;
                        }
                    } else {
                        Utils.showToastShortTime(baseResponse.getMsg());
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
