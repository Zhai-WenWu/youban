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

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetVerificationCodeResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.RegisterPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * 绑定手机
 */
public class BindPhoneNumberActivity extends BaseActivity implements BaseView, View.OnClickListener {
    private RegisterPresenter registerPresenter;
    private EditText edit_phone, edit_code;
    private TextView tv_getcode, tv_bind;
    //定时器
    private Handler mHandler;
    private int countTime = 60;
    private boolean needShowLoading = true;
    //登录返回user
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_number);
        user = getIntent().getParcelableExtra(Constants.Fields.EXTRA);
        setTitle();
        initView();
        registerPresenter = new RegisterPresenter(this);
    }

    public void initView() {
        edit_phone = findViewById(R.id.edit_phone);
        edit_code = findViewById(R.id.edit_code);
        tv_getcode = findViewById(R.id.tv_getcode);
        tv_bind = findViewById(R.id.tv_bind);
        tv_getcode.setOnClickListener(this);
        tv_bind.setOnClickListener(this);
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
                    tv_bind.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
            case InterfaceUrl.URL_BINDPHONENUMBER:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    if (baseResponse.getCode() == 200) {
                        // 更新用户基本信息
                        user.setPhoneNumber(getPhoneStr());
                        BaseUser baseUser = Utils.userToBaseUser(user);
                        GreenDaoUtils.getInstance().insertBaseUser(baseUser);

                        if (user.getHobbyList() == null || user.getHobbyList().size() == 0 ||
                                TextUtils.isEmpty(user.getBirthday()) || user.getOccupationInfo() == null ||
                                user.getSchoolInfo() == null) {
                            Intent intent = new Intent(BindPhoneNumberActivity.this, CompleteInfoActivity.class);
                            intent.putExtra(Constants.Fields.USER_ID, user.getUserId());
                            intent.putExtra(Constants.Fields.USER_ICON, user.getUserIcon());
                            intent.putExtra(Constants.Fields.NICK_NAME, user.getNickName());
                            intent.putExtra(Constants.Fields.SEX, user.getSex());
                            intent.putExtra(Constants.Fields.BIRTHDAY, user.getBirthday());
                            intent.putExtra(Constants.Fields.OCCUPATION_INFO, user.getOccupationInfo());
                            intent.putExtra(Constants.Fields.SCHOOL_INFO, user.getSchoolInfo());
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(BindPhoneNumberActivity.this, MainActivity.class));
                        }
                        finish();
                    } else {
                        Utils.showToastShortTime(baseResponse.getMsg());
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
                } else {
                    edit_phone.setEnabled(false);
                    startTimer();
                    if (registerPresenter != null) {
                        registerPresenter.getVerificationCode(getPhoneStr());
                    }
                }
                break;
            case R.id.tv_bind:
                if (TextUtils.isEmpty(getCodeStr())) {
                    Utils.showToastShortTime("验证码不能为空");
                } else {
                    if (registerPresenter != null) {
                        registerPresenter.bindPhoneNumber(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), getPhoneStr(), getCodeStr());
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
     * 获取验证码
     */
    private String getCodeStr() {
        return edit_code.getText().toString();
    }

    private void setTitle() {
        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_bind_phone);
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
