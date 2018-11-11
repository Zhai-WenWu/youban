package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateGoodsPayAddressEvent;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserDefaultAddressResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveAlipayProductOrderResponse;
import cn.bjhdltcdn.p2plive.model.AddressInfo;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductImage;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PayPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.AddrsListActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.GoodsPayDeliverModeRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.OrderAdapter;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.alipay.ZfbPayUtils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.wxpay.WxPayUtils;

/**
 * Created by Hu_PC on 2017/11/8.
 * 商品支付界面
 */

public class GoodsPayFragment extends BaseFragment implements BaseView {
    private View rootView;
    private AppCompatActivity mActivity;
    private PayPresenter mPresent;
    private GetStoreListPresenter presenter;
    private TextView changeAddrsTextView, verticalMoneyNumView, payView;
    private TextView addrsTextView, nameTextView, phoneNumTextView, remarkTextView, contentNumView;
    private int payType = 2;//1:微信 2:支付宝
    private RadioGroup radioGroup;
    private RadioButton zfbRadioButton, wxRadioButton;
    private long userId;
    private AddressInfo addressInfo;
    private StoreDetail storeDetail;
    private List<ProductDetail> productDetailList;
    private List<ProductInfo> productInfoList;
    private List<ProductOrder> productOrderList;
    private ProductOrder productOrder;
    private RecyclerView deliveryModeRecycleView, orderRecycleView;
    private GoodsPayDeliverModeRecyclerViewAdapter deliverModeAdapter;
    private OrderAdapter orderAdapter;
    private int productNum;
    private BigDecimal totalPrice;
    private long storeId;
    private String postage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_goods_pay_layout, null);
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
        Intent intent = mActivity.getIntent();
        storeDetail = intent.getParcelableExtra("storeDetail");
        productDetailList = intent.getParcelableArrayListExtra("productDetailList");
        productNum = intent.getIntExtra("productNum", 0);
        totalPrice = (BigDecimal) intent.getSerializableExtra("totalPrice");
        postage=intent.getStringExtra("postage");
        storeId = intent.getLongExtra(Constants.Fields.STORE_ID, 0);
        if(storeId==0){
            if(storeDetail!=null&&storeDetail.getStoreInfo()!=null){
                storeId= storeDetail.getStoreInfo().getStoreId();
            }
        }
        initView();
        setTitle();
        mPresent = new PayPresenter(this);
        presenter = new GetStoreListPresenter(this);
        presenter.findUserDefaultAddress(userId);
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    private void initView() {
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        verticalMoneyNumView = (TextView) rootView.findViewById(R.id.yfje_num_view);
        addrsTextView = (TextView) rootView.findViewById(R.id.addrs_text_view);
        nameTextView = (TextView) rootView.findViewById(R.id.name_text_view);
        phoneNumTextView = (TextView) rootView.findViewById(R.id.phonenum_text_view);
        changeAddrsTextView = (TextView) rootView.findViewById(R.id.change_addrs_text_view);
        changeAddrsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到选择收货地址列表
                Intent intent = new Intent(mActivity, AddrsListActivity.class);
                startActivity(intent);
            }
        });
        remarkTextView = (TextView) rootView.findViewById(R.id.edit_view_order_describe);
        deliveryModeRecycleView = rootView.findViewById(R.id.recycler_delivery_mode);
        deliverModeAdapter = new GoodsPayDeliverModeRecyclerViewAdapter(mActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.HORIZONTAL, false);
        deliveryModeRecycleView.setLayoutManager(linearLayoutManager);
        deliverModeAdapter.setList(storeDetail.getStoreInfo().getDistributeList());
        deliveryModeRecycleView.setAdapter(deliverModeAdapter);


        orderRecycleView = rootView.findViewById(R.id.recycler_order);
        orderAdapter = new OrderAdapter(mActivity);
        orderAdapter.setCanOpenOrderDetailActivity(false);
        orderAdapter.setPayOrderrder(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        orderRecycleView.setLayoutManager(layoutManager);
        //组装数据
        productInfoList = new ArrayList<ProductInfo>();
        productOrderList = new ArrayList<ProductOrder>();
        ProductOrder productOrder = new ProductOrder();
        for (int i = 0; i < productDetailList.size(); i++) {
            ProductDetail productDetail = productDetailList.get(i);
            ProductInfo productInfo = productDetail.getProductInfo();
            productInfo.setProductType(3);
            List<ProductImage> productImageList = productDetail.getImageList();
            if (productImageList != null && productImageList.size() > 0) {
                productInfo.setProductImg(productImageList.get(0).getThumbnailUrl());
            }
            productInfoList.add(productInfo);
        }
        productOrder.setProductList(productInfoList);
//        productOrder.setStoreInfo(storeDetail.getStoreInfo());
//        productOrder.setProductInfo(productInfoList.get(0));
        productOrder.setTotalProductCount(productNum);
        productOrder.setPostAge(postage);
        if(!StringUtils.isEmpty(postage)){
            BigDecimal postageBigDecimal=new BigDecimal(postage);
            productOrder.setTotalPrice(totalPrice.add(postageBigDecimal)+"" );
            verticalMoneyNumView.setText("¥" + totalPrice.add(postageBigDecimal) );
        }else
        {
            productOrder.setTotalPrice(totalPrice+"" );
            verticalMoneyNumView.setText("¥" + totalPrice );
        }
        productOrder.setProductInfo(new ProductInfo());
        productOrderList.add(productOrder);
        //组装数据完毕
        orderAdapter.setList(productOrderList);
        orderRecycleView.setAdapter(orderAdapter);

        contentNumView = (TextView) rootView.findViewById(R.id.content_num_view);
        remarkTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 500) {
                    contentNumView.setText(charSequence.length() + "/500");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        payView = (TextView) rootView.findViewById(R.id.pay_view);
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
                if (addressInfo == null) {
                    //跳转到新增地址界面
                    Utils.showToastShortTime("请选择默认收货地址");
                    return;
                }
                if (deliverModeAdapter.getSelectServiceTypeId() <= 0) {
                    Utils.showToastShortTime("请选择配送方式");
                    return;
                }
                if (payType == 2) {
                    presenter.saveAlipayProductOrder(productInfoList.get(0).getProductId(), productInfoList.get(0).getProductNum(), userId, addressInfo.getAddressId(), 1, deliverModeAdapter.getSelectServiceTypeId(), storeId, productInfoList,postage, remarkTextView.getText().toString());
//                    mPresent.saveAlipayOrder(serviceId, userId, 1);
                } else if (payType == 1) {
//                    if (isWXAppInstalledAndSupported()) {
//                        mPresent.saveWeixinOrder(serviceId, userId, 1, PlatformInfoUtils.getLocalIpAddress(mActivity));
//                    } else {
//                        Utils.showToastShortTime("请检查是否安装微信客户端");
//                    }
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
        fragment.setTitle(R.string.title_pay_goods);
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

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            return;
        }

        if (apiName.equals(InterfaceUrl.URL_SAVEALIPAYPRODUCTORDER)) {
            if (object instanceof SaveAlipayProductOrderResponse) {
                SaveAlipayProductOrderResponse saveAlipayProductOrderResponse = (SaveAlipayProductOrderResponse) object;
                if (saveAlipayProductOrderResponse.getCode() == 200) {
                    //调用第三方支付宝支付sdk
                    ZfbPayUtils.getInstance().pay(mActivity, saveAlipayProductOrderResponse.getSignData());
                } else {
                    Utils.showToastShortTime(saveAlipayProductOrderResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_SAVEWEIXINPRODUCTORDER)) {
            if (object instanceof SaveAlipayProductOrderResponse) {
                SaveAlipayProductOrderResponse saveAlipayProductOrderResponse = (SaveAlipayProductOrderResponse) object;
                if (saveAlipayProductOrderResponse.getCode() == 200) {
                    //调用第三方微信支付sdk
                    WxPayUtils.getInstance().pay(mActivity, saveAlipayProductOrderResponse.getSignData());
                } else {
                    Utils.showToastShortTime(saveAlipayProductOrderResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_FINDUSERDEFAULTADDRESS)) {
            if (object instanceof FindUserDefaultAddressResponse) {
                FindUserDefaultAddressResponse response = (FindUserDefaultAddressResponse) object;
                if (response.getCode() == 200) {
                    addressInfo = response.getAddressInfo();
                    if (addressInfo != null) {
                        changeAddrsTextView.setText("更改地址");
                        addrsTextView.setText(addressInfo.getAddress());
                        nameTextView.setText(addressInfo.getContactName());
                        phoneNumTextView.setText(addressInfo.getPhoneNumber());
                    } else {
                        changeAddrsTextView.setText("选择地址");
                    }
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateGoodsPayAddressEvent event) {
        if (event == null) {
            return;
        }
        addressInfo = event.getAddressInfo();
        if (addressInfo != null) {
            changeAddrsTextView.setText("更改地址");
            addrsTextView.setText(addressInfo.getAddress());
            nameTextView.setText(addressInfo.getContactName());
            phoneNumTextView.setText(addressInfo.getPhoneNumber());
        } else {
            changeAddrsTextView.setText("选择地址");
            addrsTextView.setText("请选择默认收货地址");
            nameTextView.setText("");
            phoneNumTextView.setText("");
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

        ZfbPayUtils.getInstance().onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onVisible(boolean isInit) {

    }


}
