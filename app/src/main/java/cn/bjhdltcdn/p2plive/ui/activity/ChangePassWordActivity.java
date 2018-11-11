package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.CompleteInfoPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * 更改密码
 */

public class ChangePassWordActivity extends BaseActivity implements BaseView {
    private TextView tv_save;
    private EditText edit_password_old, edit_password_new, edit_password_verify;
    private CompleteInfoPresenter completeInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        completeInfoPresenter = new CompleteInfoPresenter(this);
        setTitle();
        init();
    }

    private void init() {
        edit_password_old = findViewById(R.id.edit_password_old);
        edit_password_new = findViewById(R.id.edit_password_new);
        edit_password_verify = findViewById(R.id.edit_password_verify);
        tv_save = findViewById(R.id.tv_save);
        edit_password_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    tv_save.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                    tv_save.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getOldPassWordStr().equals(SafeSharePreferenceUtils.getString(Constants.Fields.PASS_WORD, ""))) {
                    Utils.showToastShortTime("旧密码输入不正确");
                } else if (TextUtils.isEmpty(getNewPassWordStr()) || !StringUtils.isFormatedPassword(getNewPassWordStr())) {
                    Utils.showToastShortTime("请输入正确格式的新密码");
                } else if (!getNewPassWordStr().equals(getPassWordVerifyStr())) {
                    Utils.showToastShortTime("两次输入密码不一致");
                } else {


                    long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

                    completeInfoPresenter.ResetPassword(myUserId, getOldPassWordStr(), getNewPassWordStr());


                }
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_RESETPASSWORD:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    if (baseResponse.getCode() == 200) {
                        SafeSharePreferenceUtils.saveString(Constants.Fields.PASS_WORD, getNewPassWordStr());
                        Utils.showToastShortTime("修改密码成功");
                        finish();
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

    /**
     * 旧密码
     *
     * @return
     */
    private String getOldPassWordStr() {
        return edit_password_old.getText().toString();
    }

    /**
     * 新密码
     *
     * @return
     */
    private String getNewPassWordStr() {
        return edit_password_new.getText().toString();
    }

    /**
     * 确认密码
     *
     * @return
     */
    private String getPassWordVerifyStr() {
        return edit_password_verify.getText().toString();
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("更改密码");
    }
}
