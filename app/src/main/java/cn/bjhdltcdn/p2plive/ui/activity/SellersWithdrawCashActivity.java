package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateWithdrawCashEvent;
import cn.bjhdltcdn.p2plive.event.updateZFBAccountEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindExchangeInfoResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

import static org.lasque.tusdk.impl.view.widget.TuProgressHub.dismiss;

/**
 * 卖家提取现金界面
 */
public class SellersWithdrawCashActivity extends BaseActivity implements BaseView {
    private GetStoreListPresenter presenter;
    private long userId;
    private TextView tipTextView, saleAmountTextView, remainExchangeAmountTextView, cashTextView, payOffTextView;
    private EditText amountEditText;
    private int authStatus, isDayExchange;
    private double remainExchangeAmount, minAmount, maxAmount;
    private String account;

    public GetStoreListPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GetStoreListPresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_withdraw_cash);
        EventBus.getDefault().register(this);
        getView();
        setTitle();
        getPresenter().findExchangeInfo(userId, 1);
    }


    public void getView() {
        Intent intent = getIntent();
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        tipTextView = findViewById(R.id.tip_three);
        saleAmountTextView = findViewById(R.id.tip_four);
        remainExchangeAmountTextView = findViewById(R.id.tip_sex);
        amountEditText = findViewById(R.id.charge_edit);
        cashTextView = findViewById(R.id.withdrw_cash_text_view);
        payOffTextView = findViewById(R.id.pay_off_text_view);
        payOffTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(SellersWithdrawCashActivity.this, ClerkListActiviity.class);
                intent1.putExtra(Constants.Fields.COME_IN_TYPE, 1);
                intent1.putExtra("totalMoney", remainExchangeAmount);
                startActivity(intent1);
            }
        });
        cashTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实名认证状态(0未认证,1等待审核,2已认证,3审核失败),
                if (authStatus == 0 || authStatus == 3) {
                    Utils.showToastShortTime("未实名认证，不能提现");
                    return;
                } else if (authStatus == 1) {
                    Utils.showToastShortTime("实名认证等待审核中，审核通过后才能提现");
                    return;
                }

                if (StringUtils.isEmpty(account)) {
                    //未绑定支付宝 跳转到绑定支付宝界面
                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                    dialog.setText("", "请到“我-更多功能”绑定您的支付宝账号", "取消", "确定");
                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                        @Override
                        public void onLeftClick() {

                        }

                        @Override
                        public void onRightClick() {
                            startActivity(new Intent(SellersWithdrawCashActivity.this, AlipayInfoActivity.class));
                            dismiss();
                        }
                    });
                    dialog.show(getSupportFragmentManager());
                    return;
                }

                if (StringUtils.isEmpty(amountEditText.getText().toString())) {
                    Utils.showToastShortTime("请输入正确的可提现金额");
                    return;
                }
                double num = Double.parseDouble(amountEditText.getText().toString());
                if (num > remainExchangeAmount || num < minAmount || num > maxAmount) {
                    Utils.showToastShortTime("请输入正确的可提现金额");
                    return;
                }
                if (isDayExchange == 1) {
                    Utils.showToastShortTime("今天已提现，明天再来吧！");
                    return;
                }
                //提现
                getPresenter().saveExchangeInfo(userId, 1, num + "");
            }
        });
    }

    private void setTitle() {
        TitleFragment fragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        fragment.setTitle(R.string.title_withdraw_cash);
        fragment.setLeftViewTitle(R.mipmap.back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        fragment.setRightViewTitle("明细", new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {
                //跳转到提现明细界面
                Intent intent = new Intent(SellersWithdrawCashActivity.this, SellersWithdrawCashDetailListActivity.class);
                startActivity(intent);
            }
        });
        fragment.setRightViewColor(R.color.color_ffb700);
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_FINDEXCHANGEINFO)) {
            if (object instanceof FindExchangeInfoResponse) {
                FindExchangeInfoResponse findExchangeInfoResponse = (FindExchangeInfoResponse) object;
                if (findExchangeInfoResponse.getCode() == 200) {
                    tipTextView.setText(findExchangeInfoResponse.getContent());
                    saleAmountTextView.setText("账户余额：" + findExchangeInfoResponse.getSaleAmount() + "元");
                    remainExchangeAmount = findExchangeInfoResponse.getRemainExchangeAmount();
                    minAmount = findExchangeInfoResponse.getMinAmount();
                    maxAmount = findExchangeInfoResponse.getMaxAmount();
                    isDayExchange = findExchangeInfoResponse.getIsDayExchange();
                    String remainExchangeAmountStr = String.valueOf(remainExchangeAmount);
                    if (remainExchangeAmountStr.endsWith(".0")) {
                        remainExchangeAmountStr = remainExchangeAmountStr.substring(0, remainExchangeAmountStr.length() - 2);
                    }
                    remainExchangeAmountTextView.setText("可提现金额" + remainExchangeAmountStr + "元");
                    authStatus = findExchangeInfoResponse.getAuthStatus();
                    account = findExchangeInfoResponse.getAccount();
                } else {
                    Utils.showToastShortTime(findExchangeInfoResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_SAVEEXCHANGEINFO)) {
            if (object instanceof BaseResponse) {
                BaseResponse baseResponse = (BaseResponse) object;
                Utils.showToastShortTime(baseResponse.getMsg());
                if (baseResponse.getCode() == 200) {
                    finish();
                }
            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(SellersWithdrawCashActivity.this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final updateZFBAccountEvent event) {
        if (event == null) {
            return;
        }
        getPresenter().findExchangeInfo(userId, 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateWithdrawCashEvent event) {
        if (event == null) {
            return;
        }
        getPresenter().findExchangeInfo(userId, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
