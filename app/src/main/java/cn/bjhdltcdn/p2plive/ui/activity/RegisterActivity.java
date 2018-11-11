package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetVerificationCodeResponse;
import cn.bjhdltcdn.p2plive.httpresponse.RegisterResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.LoginPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.RegisterPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

public class RegisterActivity extends BaseActivity implements BaseView, View.OnClickListener {
    private RegisterPresenter registerPresenter;
    private LoginPresenter loginPresenter;
    private EditText edit_phone, edit_password, edit_code;
    private TextView tv_getcode, tv_register, tv_agree;
    //定时器
    private Handler mHandler;
    private int countTime = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle();
        initView();
        registerPresenter = new RegisterPresenter(this);
        loginPresenter = new LoginPresenter(this);


    }

    public void initView() {
        edit_phone = findViewById(R.id.edit_phone);
        edit_password = findViewById(R.id.edit_password);
        edit_code = findViewById(R.id.edit_code);
        tv_getcode = findViewById(R.id.tv_getcode);
        tv_register = findViewById(R.id.tv_register);
        tv_agree = findViewById(R.id.tv_agree);
        tv_getcode.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tv_getcode.setTextColor(getResources().getColor(R.color.color_333333));
                    tv_getcode.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                } else {
                    tv_getcode.setTextColor(getResources().getColor(R.color.color_b7b7b7));
                    tv_getcode.setBackground(getResources().getDrawable(R.drawable.shape_round_10_stroke_999999));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tv_register.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, WXPayEntryActivity.class);
                intent.putExtra(Constants.Action.ACTION_BROWSE, 1);
                startActivity(intent);
            }
        });
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
            case InterfaceUrl.URL_REGISTER:
                if (object instanceof RegisterResponse) {
                    RegisterResponse registerResponse = (RegisterResponse) object;
                    if (registerResponse.getCode() == 200) {
                        Intent intent = new Intent(RegisterActivity.this, CompleteInfoActivity.class);
                        intent.putExtra(Constants.Fields.USER_ID, registerResponse.getUser().getUserId());
                        startActivity(intent);

                        MobclickAgent.onProfileSignIn("Phone", registerResponse.getUser().getUserId() + "");

                        finish();
                    } else {
                        Utils.showToastShortTime(registerResponse.getMsg());
                    }
                }
                break;

        }

    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode:
                if (TextUtils.isEmpty(getPhoneStr())) {
                    Utils.showToastShortTime("手机号不能为空");
                } else if (!StringUtils.isFormatedPhoneNumber(getPhoneStr())) {
                    Utils.showToastShortTime("请输入正确的手机号");
                } else if (TextUtils.isEmpty(getPasswordStr())) {
                    Utils.showToastShortTime("密码不能为空");
                } else if (!StringUtils.isFormatedPassword(getPasswordStr())) {
                    Utils.showToastShortTime("请按正确的密码格式输入");
                } else {
                    edit_phone.setEnabled(false);
                    edit_password.setEnabled(false);
                    startTimer();
                    if (registerPresenter != null) {
                        registerPresenter.getVerificationCode(getPhoneStr());
                    }
                }
                break;
            case R.id.tv_register:
                if (TextUtils.isEmpty(getCodeStr())) {
                    Utils.showToastShortTime("验证码不能为空");
                } else {
                    if (registerPresenter != null) {
                        registerPresenter.register(getPhoneStr(), getPasswordStr(), getCodeStr());
                    }
                }
                break;
        }
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
        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_register_phone);
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }
}
