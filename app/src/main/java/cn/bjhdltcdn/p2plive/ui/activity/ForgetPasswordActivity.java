package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.CheckPhoneNumberResponse;
import cn.bjhdltcdn.p2plive.httpresponse.ForgetPasswordResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetVerificationCodeResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.LoginPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.RegisterPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by Hu_PC on 2017/11/9.
 */

public class ForgetPasswordActivity extends BaseActivity implements BaseView, View.OnClickListener {
    private ImageView img_title;
    private EditText edit_phone, edit_code, edit_password;
    private View view_line_phone, view_line_code, view_line_passsword;
    private TextView tv_getcode, tv_next;
    private RegisterPresenter registerPresenter;
    private LoginPresenter loginPresenter;
    //定时器
    private Handler mHandler;
    private int countTime = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        registerPresenter = new RegisterPresenter(this);
        loginPresenter = new LoginPresenter(this);
        setTitle();
        init();
    }

    private void init() {
        img_title = findViewById(R.id.img_title);
        edit_phone = findViewById(R.id.edit_phone);
        edit_code = findViewById(R.id.edit_code);
        edit_password = findViewById(R.id.edit_password);
        view_line_phone = findViewById(R.id.view_line_phone);
        view_line_code = findViewById(R.id.view_line_code);
        view_line_passsword = findViewById(R.id.view_line_password);
        tv_getcode = findViewById(R.id.tv_getcode);
        tv_next = findViewById(R.id.tv_next);
        tv_getcode.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tv_next.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                } else {
                    tv_next.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_e6e6e6));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tv_next.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                } else {
                    tv_next.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_e6e6e6));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode:
                startTimer();
                if (registerPresenter != null) {
                    registerPresenter.getVerificationCode(getPhoneStr());
                }
                break;
            case R.id.tv_next:
                if (tv_getcode.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(getCodeStr())) {
                        Utils.showToastShortTime("验证码不能为空");
                    } else if (!StringUtils.isFormatedPhoneNumber(getPhoneStr())) {
                        Utils.showToastShortTime("请输入正确的手机号");
                    } else {
                        if (loginPresenter != null) {
                            loginPresenter.forgetPassword(getPhoneStr(), getPasswordStr(), getCodeStr());
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(getPhoneStr())) {
                        Utils.showToastShortTime("手机号不能为空");
                    } else if (!StringUtils.isFormatedPhoneNumber(getPhoneStr())) {
                        Utils.showToastShortTime("请输入正确的手机号");
                    } else {
                        if (loginPresenter != null) {
                            loginPresenter.checkPhoneNumber(getPhoneStr());
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETVERIFICATIONCODE:
                if (object instanceof GetVerificationCodeResponse) {
                    GetVerificationCodeResponse getVerificationCodeResponse = (GetVerificationCodeResponse) object;
                    Utils.showToastShortTime(getVerificationCodeResponse.getMsg());
                }
                break;
            case InterfaceUrl.URL_CHECKPHONENUMBER:
                if (object instanceof CheckPhoneNumberResponse) {
                    CheckPhoneNumberResponse checkPhoneNumberResponse = (CheckPhoneNumberResponse) object;
                    if (checkPhoneNumberResponse.getCode() == 200) {
                        if (checkPhoneNumberResponse.getFlag() == 1) {
                            img_title.setBackgroundResource(R.drawable.forget_password_second);
                            edit_phone.setVisibility(View.INVISIBLE);
                            view_line_phone.setVisibility(View.GONE);
                            edit_code.setVisibility(View.VISIBLE);
                            tv_getcode.setVisibility(View.VISIBLE);
                            view_line_code.setVisibility(View.VISIBLE);
                            edit_password.setVisibility(View.VISIBLE);
                            view_line_passsword.setVisibility(View.VISIBLE);
                            tv_next.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_e6e6e6));
                        } else {
                            Utils.showToastShortTime("手机号未注册");
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_FORGETPASSWORD:
                if (object instanceof ForgetPasswordResponse) {
                    ForgetPasswordResponse forgetPasswordResponse = (ForgetPasswordResponse) object;
                    if (forgetPasswordResponse.getCode() == 200) {
                        SafeSharePreferenceUtils.saveString(Constants.Fields.PASS_WORD, getPasswordStr());
                        startActivity(new Intent(this, LoginActivity.class));
                    } else {
                        Utils.showToastShortTime(forgetPasswordResponse.getMsg());
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
     * 倒计时
     */
    private void startTimer() {
        tv_getcode.setEnabled(false);
        if (mHandler == null) {
            mHandler = new Handler();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mHandler == null) {
                    return;
                }

                if (countTime <= 1) {
                    tv_getcode.setEnabled(true);
                    tv_getcode.setText("获取验证码");
                    tv_getcode.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                    mHandler.removeCallbacks(this);
                    countTime = 60;
                    return;
                }


                mHandler.postDelayed(this, 1000);
                countTime -= 1;

                tv_getcode.setEnabled(false);
                if (tv_getcode != null) {
                    tv_getcode.setText(countTime + "s");
                    tv_getcode.setBackground(getResources().getDrawable(R.drawable.shape_round_10_stroke_999999));
                }

            }
        }, 0);
    }

    /**
     * 获取手机号
     */
    private String getPhoneStr() {
        return edit_phone.getText().toString();
    }

    /**
     * 获取密码
     */
    private String getPasswordStr() {
        return edit_password.getText().toString();
    }

    /**
     * 获取验证码
     */
    private String getCodeStr() {
        return edit_code.getText().toString();
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("重置密码");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
