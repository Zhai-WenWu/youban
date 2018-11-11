package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.SaveAlipayOrderResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.PayPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.alipay.ZfbPayUtils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.wxpay.WxPayUtils;

/**
 * Created by Hu_PC on 2017/11/8.
 * 支付界面
 */

public class PayFragment extends BaseFragment implements BaseView {
    private View rootView;
    private AppCompatActivity mActivity;
    private PayPresenter mPresent;
    private TextView goldNumView, moneyNumView, verticalMoneyNumView, payView, preferentialTipView;
    private String serviceId;
    private int payType = 1;//1:微信 2:支付宝
    private RadioGroup radioGroup;
    private RadioButton zfbRadioButton, wxRadioButton;
    private int goldNum, money;
    private Long userId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_pay_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        mPresent = new PayPresenter(this);
        serviceId = mActivity.getIntent().getStringExtra("serviceId");
        goldNum = mActivity.getIntent().getIntExtra("goldnum", 0);
        money = mActivity.getIntent().getIntExtra("money", 0);
        setTitle();
        goldNumView = (TextView) rootView.findViewById(R.id.gmjbs_num_text_view);
        goldNumView.setText(goldNum + "金币");
        moneyNumView = (TextView) rootView.findViewById(R.id.zfje_num_text_view);
        moneyNumView.setText("￥" + money + "元");
//			preferentialGoldNumView= (TextView)findViewById(R.id.ssyh_num_text_view);
//      	preferentialGoldNumView.setText("赠送"+presentGoldNum+"金币");
        verticalMoneyNumView = (TextView) rootView.findViewById(R.id.yfje_num_view);
        verticalMoneyNumView.setText("￥" + money + "元");
        payView = (TextView) rootView.findViewById(R.id.pay_view);
        preferentialTipView = (TextView) rootView.findViewById(R.id.yh_tip_view);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.layout_pay);
        zfbRadioButton = (RadioButton) rootView.findViewById(R.id.zfb_pay_view);
        wxRadioButton = (RadioButton) rootView.findViewById(R.id.wx_pay_view);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == zfbRadioButton.getId()) {
                    payType = 2;
                } else if (checkedId == wxRadioButton.getId()) {
                    payType = 1;
                }
            }
        });

        payView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payType == 2) {
                    mPresent.saveAlipayOrder(serviceId, userId, 1);
                } else if (payType == 1) {
                    if (isWXAppInstalledAndSupported()) {
                        mPresent.saveWeixinOrder(serviceId, userId, 1, PlatformInfoUtils.getLocalIpAddress(mActivity));
                    } else {
                        Utils.showToastShortTime("请检查是否安装微信客户端");
                    }
                } else {
                    Utils.showToastShortTime("请选择支付方式");
                }
            }
        });
    }

    private void setTitle() {
        TitleFragment fragment = (TitleFragment) getChildFragmentManager().findFragmentById(R.id.title_fragment);
        fragment.setLeftViewTitle(R.mipmap.back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                mActivity.finish();
            }
        });
        fragment.setTitle(R.string.title_pay);
    }

    //检测手机是否安装微信客户端
    private boolean isWXAppInstalledAndSupported() {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(mActivity, null);
        msgApi.registerApp(Constants.KEY.KEY_WXPAY);

        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();

        return sIsWXAppInstalledAndSupported;
    }


    @Override
    public void updateView(String apiName, Object object) {

        if (!isAdded()) {
            return;
        }

        if (apiName.equals(InterfaceUrl.URL_SAVEALIPAYORDER)) {
            if (object instanceof SaveAlipayOrderResponse) {
                SaveAlipayOrderResponse saveAlipayOrderResponse = (SaveAlipayOrderResponse) object;
                if (saveAlipayOrderResponse.getCode() == 200) {
                    //调用第三方支付宝支付sdk
                    ZfbPayUtils.getInstance().pay(mActivity, saveAlipayOrderResponse.getSignData());
                } else {
                    Utils.showToastShortTime(saveAlipayOrderResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_SAVEWEIXINORDER)) {
            if (object instanceof SaveAlipayOrderResponse) {
                SaveAlipayOrderResponse saveAlipayOrderResponse = (SaveAlipayOrderResponse) object;
                if (saveAlipayOrderResponse.getCode() == 200) {
                    //调用第三方微信支付sdk
                    WxPayUtils.getInstance().pay(mActivity, saveAlipayOrderResponse.getSignData());
                } else {
                    Utils.showToastShortTime(saveAlipayOrderResponse.getMsg());
                }
            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresent != null) {
            mPresent.onDestroy();
        }
        mPresent = null;

        if (radioGroup != null) {
            radioGroup.removeAllViews();
        }
        radioGroup = null;

        if (mActivity != null) {
            mActivity = null;
        }

        if (zfbRadioButton != null) {
            zfbRadioButton = null;
        }

        if (wxRadioButton != null) {
            wxRadioButton = null;
        }

        if (rootView != null) {
            rootView = null;
        }

        if (userId != null) {
            userId = null;
        }

        ZfbPayUtils.getInstance().onDestroy();

    }


    @Override
    protected void onVisible(boolean isInit) {

    }
}
