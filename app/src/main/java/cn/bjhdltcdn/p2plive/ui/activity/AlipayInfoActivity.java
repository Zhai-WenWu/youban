package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.updateZFBAccountEvent;
import cn.bjhdltcdn.p2plive.httpresponse.FindCashAccountResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveCashAccountResponse;
import cn.bjhdltcdn.p2plive.model.CashAccount;
import cn.bjhdltcdn.p2plive.mvp.presenter.ExchangePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.RegisterPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * 支付宝绑定页面
 */
public class AlipayInfoActivity extends BaseActivity implements BaseView {

    private ExchangePresenter exchangePresenter;
    private long userId;
    private TextView tvId;
    private TextView tvNickName;
    private LinearLayout ll_code;
    private TextView tvZhiFuBao;
    private EditText edPhone;
    private EditText edZhiFuBaoNum;
    private TextView tvGetCode;
    private EditText edCode;
    private TextView tvSave;
    private Handler mHandler;
    private int countTime = 60;
    private int SAVE = 1, MODIFY = 2;
    private int type;
    private RegisterPresenter registerPresenter;
    private boolean phoneStr;
    private boolean zhiFuBaoStr;
    private boolean codeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_info_layout);
        registerPresenter = new RegisterPresenter(this);
        setTitle();
        initView();
        initData();
    }

    private void initView() {
        tvId = findViewById(R.id.id_text_view);
        tvNickName = findViewById(R.id.nicke_text_view);
        tvZhiFuBao = findViewById(R.id.zhi_fu_bao_view);
        edPhone = findViewById(R.id.edit_phone);
        tvGetCode = findViewById(R.id.tv_getcode);
        ll_code = findViewById(R.id.ll_code);
        edCode = findViewById(R.id.edit_code);
        tvSave = findViewById(R.id.save_view);
        edZhiFuBaoNum = findViewById(R.id.ed_zhi_fu_bao_num);

        initTextChangeListener();

        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneStr = edPhone.getText().toString();
                if (TextUtils.isEmpty(phoneStr)) {
                    Utils.showToastShortTime("手机号不能为空");
                } else if (!StringUtils.isFormatedPhoneNumber(phoneStr)) {
                    Utils.showToastShortTime("请输入正确的手机号");
                } else {
                    startTimer();
                    if (registerPresenter != null) {
                        registerPresenter.getVerificationCode(phoneStr);
                    }
                }
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == SAVE) {
                    String phoneStr = edPhone.getText().toString();
                    String codeStr = edCode.getText().toString();
                    String zhiFuBaoNumStr = edZhiFuBaoNum.getText().toString();
                    if (TextUtils.isEmpty(zhiFuBaoNumStr)) {
                        Utils.showToastShortTime("支付宝账号不能为空");
                    } else if (TextUtils.isEmpty(phoneStr)) {
                        Utils.showToastShortTime("手机号不能为空");
                    } else if (!StringUtils.isFormatedPhoneNumber(phoneStr)) {
                        Utils.showToastShortTime("请输入正确的手机号");
                    } else if (TextUtils.isEmpty(codeStr)) {
                        Utils.showToastShortTime("验证码不能为空");
                    } else {
                        exchangePresenter.saveCashAccount(userId, zhiFuBaoNumStr, phoneStr, codeStr);
                        tvSave.setClickable(false);
                    }
                } else if (type == MODIFY) {
                    type = SAVE;
                    tvGetCode.setVisibility(View.VISIBLE);
                    ll_code.setVisibility(View.VISIBLE);
                    tvSave.setText("保存");
                    edZhiFuBaoNum.setFocusable(true);
                    edZhiFuBaoNum.setFocusableInTouchMode(true);
                    edZhiFuBaoNum.requestFocus();
                    edPhone.setFocusable(true);
                    edPhone.setFocusableInTouchMode(true);
                    edPhone.requestFocus();
                    tvSave.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
                }
            }
        });
    }

    private void initTextChangeListener() {
        edPhone.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    phoneStr = true;
                } else {
                    phoneStr = false;
                }
                changeBottemTextview();
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edZhiFuBaoNum.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    zhiFuBaoStr = true;
                } else {
                    zhiFuBaoStr = false;
                }
                changeBottemTextview();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edCode.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    codeStr = true;
                } else {
                    codeStr = false;
                }
                changeBottemTextview();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void changeBottemTextview() {
        if (type == SAVE && phoneStr && zhiFuBaoStr && codeStr) {
            tvSave.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
        } else {
            tvSave.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
        }
        if (type == MODIFY) {
            tvSave.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
        }
    }

    /**
     * 倒计时
     */
    private void startTimer() {
        tvGetCode.setEnabled(false);
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
                    tvGetCode.setEnabled(true);
                    tvGetCode.setText("获取验证码");
                    tvGetCode.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                    mHandler.removeCallbacks(this);
                    countTime = 60;
                    return;
                }


                mHandler.postDelayed(this, 1000);
                countTime -= 1;

                tvGetCode.setEnabled(false);
                if (tvGetCode != null) {
                    tvGetCode.setText(countTime + "s");
                    tvGetCode.setBackground(getResources().getDrawable(R.drawable.shape_round_10_stroke_999999));
                }

            }
        }, 0);
    }

    private void initData() {
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        getExchangePresenter().findCashAccount(userId);
    }

    private void setTitle() {
        final TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle("绑定支付宝");
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    public ExchangePresenter getExchangePresenter() {
        if (exchangePresenter == null) {
            exchangePresenter = new ExchangePresenter(this);
        }
        return exchangePresenter;
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_FINDCASHACCOUNT:
                if (object instanceof FindCashAccountResponse) {
                    FindCashAccountResponse findCashAccountResponse = (FindCashAccountResponse) object;
                    if (findCashAccountResponse.getCode() == 200) {
                        CashAccount cashAccount = findCashAccountResponse.getAccountInfo();
                        tvId.setText("用户ID: " + cashAccount.getUserId());
                        tvNickName.setText("用户姓名: " + cashAccount.getName());
                        if (cashAccount.getAccount() != null) {
                            type = MODIFY;
                            edZhiFuBaoNum.setText(cashAccount.getAccount());
                            tvGetCode.setVisibility(View.GONE);
                            ll_code.setVisibility(View.GONE);
                            tvSave.setText("更改账号");
                            edPhone.setText(String.valueOf(cashAccount.getPhoneNumber()));
                            edZhiFuBaoNum.setFocusable(false);
                            edPhone.setFocusable(false);
                            tvSave.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                        } else {
                            type = SAVE;
                            tvGetCode.setVisibility(View.VISIBLE);
                            ll_code.setVisibility(View.VISIBLE);
                            tvSave.setText("保存");
                            tvSave.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
                        }
                    } else {
                        Utils.showToastShortTime(findCashAccountResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_SAVECASHACCOUNTR:
                if (object instanceof SaveCashAccountResponse) {
                    SaveCashAccountResponse saveCashAccountResponse = (SaveCashAccountResponse) object;
                    tvSave.setClickable(true);
                    if (saveCashAccountResponse.getCode() == 200) {
                        CashAccount cashAccount = saveCashAccountResponse.getCashAccount();
                        type = MODIFY;
                        tvGetCode.setVisibility(View.GONE);
                        ll_code.setVisibility(View.GONE);
                        tvSave.setText("更改账号");
                        edZhiFuBaoNum.setFocusable(false);
                        edPhone.setFocusable(false);
                        tvSave.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                        EventBus.getDefault().post(new updateZFBAccountEvent());
                    }
                    Utils.showToastShortTime(saveCashAccountResponse.getMsg());
                    finish();
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
}
