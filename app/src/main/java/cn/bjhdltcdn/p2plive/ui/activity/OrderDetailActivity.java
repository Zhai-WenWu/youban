package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.OrderEvalusteAndRefundEvent;
import cn.bjhdltcdn.p2plive.event.OrderStatusEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindOrderDetailResponse;
import cn.bjhdltcdn.p2plive.model.AddressInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.model.RefundReason;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.OrderPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.SchoolShopOrderDetailAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.GoodsreceiptDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.VerificationCodeView;

/**
 * Created by zhaiww on 2018/4/17.
 */

public class OrderDetailActivity extends BaseActivity implements BaseView, View.OnClickListener {

    private TextView tv_orderid;
    private TextView tv_orderstatus;
    private TextView tv_goods_receipt_time;
    private LinearLayout ll_order_status;
    private TextView tv_goods_receipt_code;
    private TextView tv_order_user_name;
    private TextView tv_order_user_phone;
    private TextView tv_order_user_address;
    private TextView tv_productdesc;
    private ImageView iv_productimg;
    private TextView tv_productname;
    private TextView tv_limited;
    private TextView tv_productprice;
    private TextView tv_goods_receipt;
    private OrderPresenter orderPresenter;
    private long userId;
    private long orderId;
    private int position;
    private RelativeLayout rv_try;
    private RelativeLayout rv_shop;
    private ImageView iv_shop_productimg;
    private TextView tv_shop_productname;
    private TextView tv_shop_productprice;
    private TextView tv_shop_now_productprice;
    private TextView tv_shop_num;
    private TextView tv_shop_praice;
    private TextView tv_shop_goods_receipt;
    private TextView tv_shop_bottom;
    private TextView tv_shop_text;
    private TextView tv_phone;
    private TextView tv_now_productprice;
    private EditText et_remarks;
    private TextView tv_num_content;
    private RelativeLayout rela_msg;
    private RelativeLayout rela_video;
    private RelativeLayout rl_school_goods_receipt;
    private TextView tv_school_goods_receipt;
    private TextView tv_school_goods_applicatrion_refund;
    private ProductOrder orderInfo;
    private GoodsreceiptDialog dialog;
    private int orderStatus;
    private RecyclerView rv__school_shop;
    private SchoolShopOrderDetailAdapter schoolShopOrderDetailAdapter;
    private LinearLayout ll_contact_seller;
    private Intent intent;
    private VerificationCodeView verificationcodeview;
    private View view_edit;
    private String codeSrting = "";
    private TextView tv_resouce;
    private TextView tv_remarks;
    private LinearLayout ll_school_refund_reason;
    private RelativeLayout rv_school;
    private TextView tv_send_way;
    private TextView tv_receipt_way;
    private int comeInType;//1:卖家
    private TextView tv_receipt;
    private GetStoreListPresenter getStoreListPresenter;
    private TextView tv_store_info;
    private TextView tv_shop_goods_refund;
    private TextView tv_school_goods_num;
    private TextView tv_shop_remake;
    private TextView tv_refuse_refund;
    private boolean needRefreshList;
    private TextView view_5;
    private boolean isBuyerSign;
    private TextView tv_contact_buller;
    private TextView tv_school_postage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        EventBus.getDefault().register(this);
        setTitle();
        initView();
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        orderId = getIntent().getLongExtra(Constants.Fields.ORDER_ID, 0);
        position = getIntent().getIntExtra(Constants.Fields.POSITION, 0);
        //  comeInType = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        getOrderPresenter().findOrderDetail(userId, orderId);
    }

    private void initView() {
        //试用
        tv_orderid = findViewById(R.id.tv_orderid);
        rv_try = findViewById(R.id.rv_try);
        tv_orderstatus = findViewById(R.id.tv_orderstatus);
        tv_goods_receipt_time = findViewById(R.id.tv_goods_receipt_time);
        tv_goods_receipt_code = findViewById(R.id.tv_goods_receipt_code);
        ll_order_status = findViewById(R.id.ll_order_status);
        tv_order_user_name = findViewById(R.id.tv_order_user_name);
        tv_order_user_phone = findViewById(R.id.tv_order_user_phone);
        tv_order_user_address = findViewById(R.id.tv_order_user_address);
        tv_productdesc = findViewById(R.id.tv_productdesc);
        iv_productimg = findViewById(R.id.iv_productimg);
        tv_productname = findViewById(R.id.tv_productname);
        tv_limited = findViewById(R.id.tv_limited);
        tv_productprice = findViewById(R.id.tv_productprice);
        tv_goods_receipt = findViewById(R.id.tv_goods_receipt);
        tv_phone = findViewById(R.id.tv_phone);
        tv_now_productprice = findViewById(R.id.tv_now_productprice);
        tv_goods_receipt.setOnClickListener(this);

        //闪购
        rv_shop = findViewById(R.id.rv_shop);
        iv_shop_productimg = findViewById(R.id.iv_shop_productimg);
        tv_shop_productname = findViewById(R.id.tv_shop_productname);
        tv_shop_productprice = findViewById(R.id.tv_shop_productprice);
        tv_shop_now_productprice = findViewById(R.id.tv_shop_now_productprice);
        tv_shop_num = findViewById(R.id.tv_shop_num);
        tv_shop_praice = findViewById(R.id.tv_shop_praice);
        tv_shop_goods_receipt = findViewById(R.id.tv_shop_goods_receipt);
        tv_shop_bottom = findViewById(R.id.tv_shop_bottom);
        tv_shop_text = findViewById(R.id.tv_shop_text);
        tv_shop_goods_receipt.setOnClickListener(this);

        //校园店
        rela_msg = findViewById(R.id.rela_msg);
        rela_video = findViewById(R.id.rela_video);
        rl_school_goods_receipt = findViewById(R.id.rl_school_goods_receipt);
        tv_school_goods_applicatrion_refund = findViewById(R.id.tv_school_goods_applicatrion_refund);
        tv_school_goods_receipt = findViewById(R.id.tv_school_goods_receipt);
        rv__school_shop = findViewById(R.id.rv__school_shop);
        ll_contact_seller = findViewById(R.id.ll_contact_seller);
        verificationcodeview = findViewById(R.id.verificationcodeview);
        ll_school_refund_reason = findViewById(R.id.ll_school_refund_reason);
        view_edit = findViewById(R.id.view_edit);
        tv_resouce = findViewById(R.id.tv_resouce);
        tv_remarks = findViewById(R.id.tv_remarks);
        rv_school = findViewById(R.id.rv_school);
        tv_send_way = findViewById(R.id.tv_send_way);
        tv_receipt_way = findViewById(R.id.tv_receipt_way);
        tv_receipt = findViewById(R.id.tv_receipt);
        tv_store_info = findViewById(R.id.tv_store_info);
        tv_school_goods_num = findViewById(R.id.tv_school_goods_num);
        tv_shop_remake = findViewById(R.id.tv_shop_remake);
        tv_refuse_refund = findViewById(R.id.tv_refuse_refund);
        view_5 = findViewById(R.id.view_5);
        tv_school_postage = findViewById(R.id.tv_school_postage);
        tv_refuse_refund.setOnClickListener(this);

        tv_store_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, ShopDetailActivity.class);
                intent.putExtra(Constants.Fields.STORE_ID, orderInfo.getStoreInfo().getStoreId());
                startActivity(intent);
            }
        });

        verificationcodeview.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onComplete(String content) {
                codeSrting = content;
            }
        });

        view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < verificationcodeview.getChildCount(); i++) {
                    EditText childView = (EditText) verificationcodeview.getChildAt(i);
                    childView.setText("");
                    codeSrting = "";
                }
                KeyBoardUtils.openKeybord((EditText) verificationcodeview.getChildAt(0), App.getInstance());
            }
        });

        schoolShopOrderDetailAdapter = new SchoolShopOrderDetailAdapter(this);

        schoolShopOrderDetailAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                StoreDetail storeDetail = new StoreDetail();
                storeDetail.setStoreInfo(orderInfo.getStoreInfo());
                Intent intent = new Intent(OrderDetailActivity.this, GoodsDetailActivity.class);
                intent.putExtra(Constants.Fields.PRODUCT_ID, orderInfo.getProductList().get(position).getProductId());
                intent.putExtra("userType", 1);
                intent.putExtra("sellerBaseUser", orderInfo.getStoreInfo().getBaseUser());
                intent.putParcelableArrayListExtra("shopChartList", new ArrayList<ProductDetail>());
                intent.putExtra("productNum", 0);
                intent.putExtra("totalPrice", new BigDecimal(0));
                intent.putExtra("storeDetail", storeDetail);
                intent.putExtra("isSchoolmate", orderInfo.getStoreInfo().getBaseUser().getIsSchoolmate());
                intent.putExtra("isReportMax", orderInfo.getStoreInfo().getIsReportMax());//是否举报20次
                intent.putExtra("isAuth", orderInfo.getStoreInfo().getIsAuth());//店铺是否审核中
                startActivity(intent);
            }
        });

        rv__school_shop.setHasFixedSize(true);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(10));
        rv__school_shop.setLayoutManager(linearlayoutManager);
        rv__school_shop.addItemDecoration(linearLayoutSpaceItemDecoration);
        rv__school_shop.setAdapter(schoolShopOrderDetailAdapter);
        tv_school_goods_applicatrion_refund.setOnClickListener(this);
        tv_school_goods_receipt.setOnClickListener(this);
        rela_msg.setOnClickListener(this);
        rela_video.setOnClickListener(this);
        tv_receipt.setOnClickListener(this);
    }

    private void initData() {

        if (orderInfo.getBaseUser().getUserId() != userId) {
            comeInType = 1;
        }
        tv_orderid.setText(orderInfo.getOrderNumber());
        AddressInfo addressInfo = orderInfo.getAddressInfo();
        if (addressInfo != null) {
            tv_order_user_name.setText(addressInfo.getContactName());
            tv_order_user_phone.setText(addressInfo.getPhoneNumber());
            tv_order_user_address.setText(addressInfo.getAddress());
        }
        int orderStatus = orderInfo.getOrderStatus();
        if (orderStatus == 1) {//已收货
            tv_goods_receipt_time.setText(orderInfo.getUpdateTime());
            ll_order_status.setVisibility(View.GONE);
        } else {//未收货
            tv_goods_receipt_code.setText(orderInfo.getClaimCode());

        }

        switch (orderInfo.getOrderStatus()) {
            case 0:
                if (comeInType == 1) {//卖家
                    tv_orderstatus.setText("待发货");
                } else {
                    tv_orderstatus.setText("待收货");
                }
            case 2:
                if (comeInType == 1) {//卖家
                    if (orderInfo.getIsReceipt() == 1) {
                        tv_orderstatus.setText("待发货");
                    } else {
                        tv_orderstatus.setText("未接单");
                    }

                } else {
                    tv_orderstatus.setText("待收货");
                }
                break;
            case 1:
                if (comeInType == 1) {//卖家
                    tv_orderstatus.setText("已完成");
                } else {
                    if (orderInfo.getBuyerSign() != 3) {
                        tv_orderstatus.setText("待收货");
                    } else {
                        tv_orderstatus.setText("已收货");
                    }
                }
                break;
            case 3:
                if (comeInType == 1) {//卖家
                    tv_orderstatus.setText("退款中");
                } else {
                    tv_orderstatus.setText("退款申请中");
                }
                break;
            case 4:
                tv_orderstatus.setText("退款成功");
                break;
        }

        if (orderInfo.getProductInfo().getProductType() == 1) {//——————————————————试用——————————————————
            rv_try.setVisibility(View.VISIBLE);
            tv_phone.setVisibility(View.VISIBLE);

            ProductInfo productInfo = orderInfo.getProductInfo();
            tv_productdesc.setText(productInfo.getProductDesc());
            Utils.ImageViewDisplayByUrl(productInfo.getProductImg(), iv_productimg);
            tv_productname.setText(productInfo.getProductName());
            tv_now_productprice.setText(orderInfo.getPrice());
            String productTotal = String.format(App.getInstance().getResources().getString(R.string.str_product_total), productInfo.getProductTotal());
            tv_limited.setText(productTotal);
            tv_productprice.setText(productInfo.getProductPrice() + "");
            tv_productprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv_phone.setText("售后服务电话：" + orderInfo.getProductInfo().getSaleTel());
            if (orderInfo.getOrderStatus() == 1) {//已收货
                tv_goods_receipt.setText("发布试用报告");
                if (orderInfo.getIsSendReport() == 2) {
                    tv_goods_receipt.setVisibility(View.INVISIBLE);
                }
            } else {//未收货
                tv_goods_receipt.setText("确认收货");
            }
        }
        else {//——————————————————闪购/校园店——————————————————
            rv_school.setVisibility(View.VISIBLE);

            if (!StringUtils.isEmpty(orderInfo.getRemark())) {
                tv_shop_remake.setVisibility(View.VISIBLE);
                findViewById(R.id.view_6).setVisibility(View.VISIBLE);
                tv_shop_remake.setText("备注: " + orderInfo.getRemark());
            }

            this.orderStatus = orderInfo.getOrderStatus();

            if (orderStatus == 4) {
                tv_send_way.setVisibility(View.GONE);
            }

            if (orderInfo.getDistributeMode() == 36) {
                tv_send_way.setText("配送方式：卖家配送");
            } else if (orderInfo.getDistributeMode() == 37) {
                tv_send_way.setText("配送方式：自取");
            }

            if (orderInfo.getIsEval() == 1) {
                tv_school_goods_applicatrion_refund.setVisibility(View.GONE);
            }

            StoreInfo storeInfo = orderInfo.getStoreInfo();

            if (storeInfo != null && comeInType != 1) {
                tv_store_info.setVisibility(View.VISIBLE);
                tv_store_info.setText(orderInfo.getStoreInfo().getTitle());
            }

            if (orderInfo.getIsClert() == 1) {
                tv_receipt_way.setText("店长接单");
            } else if (orderInfo.getReceiptUser() != null) {
                tv_receipt_way.setText("店员" + orderInfo.getReceiptUser().getNickName() + "接单");
            }

            if (orderInfo.getIsReceipt() == 1 && orderStatus != 4) {
                tv_receipt_way.setVisibility(View.VISIBLE);
            }

            if ((orderStatus == 1 && orderInfo.getBuyerSign() == 3) || orderStatus == 4) {
                ll_order_status.setVisibility(View.GONE);
            } else {
                ll_order_status.setVisibility(View.VISIBLE);
                tv_goods_receipt_code.setText(orderInfo.getClaimCode());
            }

            schoolShopOrderDetailAdapter.setmList(orderInfo.getProductList());
            schoolShopOrderDetailAdapter.notifyDataSetChanged();

            switch (this.orderStatus) {
                case 0://待收货
                    rl_school_goods_receipt.setVisibility(View.VISIBLE);
                    tv_school_goods_applicatrion_refund.setText("申请退款");
                    tv_refuse_refund.setVisibility(View.GONE);
                    ll_school_refund_reason.setVisibility(View.GONE);
                    if (orderInfo.getIsShow() != null) {
                        if (orderInfo.getIsShow().equals("NO")) {
                            tv_school_goods_applicatrion_refund.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 1://已收货
                    if (comeInType != 1 && orderInfo.getBuyerSign() != 3) {//买家未确认收货
                        rl_school_goods_receipt.setVisibility(View.VISIBLE);
                    } else if (comeInType == 1 && orderInfo.getSellerSign() != 1) {//买家未确认收货
                        rl_school_goods_receipt.setVisibility(View.VISIBLE);
                    } else {
                        rl_school_goods_receipt.setVisibility(View.GONE);
                    }

                    if (orderInfo.getIsEval() == 0 && orderInfo.getBuyerSign() == 3) {
                        tv_school_goods_applicatrion_refund.setText("立即评价");
                        tv_school_goods_applicatrion_refund.setVisibility(View.VISIBLE);
                    } else {
                        tv_school_goods_applicatrion_refund.setVisibility(View.GONE);
                    }

                    ll_school_refund_reason.setVisibility(View.GONE);
                    break;
                case 2://买家已付款
                    if (orderInfo.getDistributeMode() == 1) {
                        rl_school_goods_receipt.setVisibility(View.VISIBLE);
                    }
                    tv_school_goods_applicatrion_refund.setText("申请退款");
                    tv_refuse_refund.setVisibility(View.GONE);
                    ll_school_refund_reason.setVisibility(View.GONE);
                    break;
                case 3://退款申请中
                    ll_school_refund_reason.setVisibility(View.VISIBLE);
                    tv_school_goods_applicatrion_refund.setText("申请退款中");
                    tv_school_goods_applicatrion_refund.setTextColor(getResources().getColor(R.color.color_666666));
                    tv_school_goods_applicatrion_refund.setBackgroundResource(R.drawable.shape_round_10_solid_cccccc);
                    rl_school_goods_receipt.setVisibility(View.VISIBLE);
                    if (orderInfo.getBaseUser().getUserId() == userId) {
                        tv_refuse_refund.setVisibility(View.VISIBLE);
                    }

                    break;
                case 4://退款成功
                    rl_school_goods_receipt.setVisibility(View.GONE);
                    ll_school_refund_reason.setVisibility(View.VISIBLE);
                    tv_refuse_refund.setVisibility(View.GONE);
                    tv_school_goods_applicatrion_refund.setVisibility(View.GONE);
                    break;
            }

            if ((orderInfo.getDistributeMode() == 2 && orderInfo.getIsReceipt() != 1)) {//未接单不可确认收货
                rl_school_goods_receipt.setVisibility(View.GONE);
            }

            if (comeInType == 1) {

                tv_contact_buller = findViewById(R.id.tv_contact_buller);

                tv_contact_buller.setText("联系买家");

                view_5.setText("确保买家已收到货物且验收完毕，输入买家下单时的取货码，确认买家已收货。");

                tv_school_goods_receipt.setText("确认送达");

                tv_refuse_refund.setText("拒绝退款");

                if (orderInfo.getOrderStatus() == 2 && orderInfo.getDistributeMode() == 36 && orderInfo.getIsReceipt() != 1) {
                    //自提的不展示接单按钮
                    tv_receipt.setVisibility(View.VISIBLE);
                    tv_receipt.setText("接单");
                    tv_receipt.setClickable(true);
                } else {
                    tv_receipt.setVisibility(View.GONE);
                }

                ll_order_status.setVisibility(View.GONE);

                if (orderStatus == 1) {
                    rl_school_goods_receipt.setVisibility(View.GONE);
                }

                switch (this.orderStatus) {
                    case 0://待收货
                    case 1://已收货
                    case 2://买家已付款
                    case 4://退款成功
                        tv_school_goods_applicatrion_refund.setVisibility(View.GONE);
                        break;
                    case 3://退款申请中
                        BaseUser receiptUser = orderInfo.getReceiptUser();
                        if (receiptUser != null) {
                            if (receiptUser.getUserId() == userId) {
                                tv_refuse_refund.setVisibility(View.VISIBLE);
                                tv_school_goods_applicatrion_refund.setVisibility(View.VISIBLE);
                                tv_school_goods_applicatrion_refund.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                                tv_school_goods_applicatrion_refund.setText("确认退款");
                            }
                        } else if (orderInfo.getStoreInfo().getBaseUser().getUserId() == userId) {
                            tv_refuse_refund.setVisibility(View.VISIBLE);
                            tv_school_goods_applicatrion_refund.setVisibility(View.VISIBLE);
                            tv_school_goods_applicatrion_refund.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                            tv_school_goods_applicatrion_refund.setText("确认退款");
                        }
                        break;
                }
            }

            if (orderInfo.getPostAge() == null) {
                tv_school_postage.setText("配送费：¥0");
            } else {
                if (StringUtils.isEmpty(orderInfo.getPostAge())) {
                    tv_school_postage.setText("配送费：¥0");
                } else {
                    tv_school_postage.setText("配送费：¥" + orderInfo.getPostAge());
                }
            }

            tv_school_goods_num.setText("共计" + orderInfo.getTotalProductCount() + "件商品   合计：¥" + orderInfo.getTotalPrice());
            //退款原因
            RefundReason refundReason = orderInfo.getRefundReason();
            if (refundReason != null) {
                tv_resouce.setText(refundReason.getReasonName());
                if (StringUtils.isEmpty(refundReason.getRemark())) {
                    tv_remarks.setVisibility(View.GONE);
                    findViewById(R.id.view_7).setVisibility(View.GONE);
                }
                tv_remarks.setText(refundReason.getRemark());
            }else {
                ll_school_refund_reason.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //闪购确认收货
            case R.id.tv_shop_goods_receipt:
                dialog = new GoodsreceiptDialog();
                dialog.setOrderId(orderId);
                if (comeInType == 1) {
                    dialog.setIsSeller(true);
                } else {
                    dialog.setIsSeller(false);
                }
                dialog.show(getSupportFragmentManager());
                dialog.setItemClick(new GoodsreceiptDialog.ItemClick() {
                    @Override
                    public void itemClick() {
                        getOrderPresenter().findOrderDetail(userId, orderId);
                    }
                });
                break;

            //试用确认收货
            case R.id.tv_goods_receipt:
                orderStatus = orderInfo.getOrderStatus();

                if (orderStatus == 0) {//待收货

                    dialog = new GoodsreceiptDialog();
                    dialog.setOrderId(orderId);
                    dialog.show(getSupportFragmentManager());
                    dialog.setItemClick(new GoodsreceiptDialog.ItemClick() {
                        @Override
                        public void itemClick() {
                            getOrderPresenter().findOrderDetail(userId, orderId);
                        }
                    });
                } else if (orderStatus == 1) {//已收货
                    Intent intent = new Intent(OrderDetailActivity.this, PublishActivity.class);
                    startActivity(intent);
                }
                break;

            //校园店确认收货
            case R.id.tv_school_goods_receipt:
                orderStatus = orderInfo.getOrderStatus();
                if (codeSrting.length() != 6) {
                    Utils.showToastShortTime("请输入正确的取货码");
                } else {
                    int upDateOrderStatusIsClerk;
                    if (comeInType == 1) {
                        upDateOrderStatusIsClerk = 1;
                    } else {
                        upDateOrderStatusIsClerk = 3;
                    }
                    orderPresenter.updateOrderStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), orderId, codeSrting, 0, upDateOrderStatusIsClerk);
                }
                break;

            //校园店退款
            case R.id.tv_school_goods_applicatrion_refund:

                if (comeInType == 1) {//确认退款
                    getStoreListPresenter().confirmRefund(userId, orderId);
                } else {
                    switch (orderStatus) {
                        case 0://待收货
                        case 2://买家已付款
                            intent = new Intent(OrderDetailActivity.this, ApplicationRefundActivity.class);
                            intent.putExtra(Constants.Fields.ORDERINFO, orderInfo);
                            startActivity(intent);
                            break;
                        case 1://已收货
                            Intent intent = new Intent(OrderDetailActivity.this, PublishEvaluateActivity.class);
                            intent.putExtra(Constants.Fields.ORDERINFO, orderInfo);
                            startActivity(intent);
                            break;
                    }
                }

                break;
            //闪购退货
            case R.id.tv_shop_goods_refund:

                switch (orderStatus) {
                    case 0://待收货
                    case 2://买家已付款
                        intent = new Intent(OrderDetailActivity.this, ApplicationRefundActivity.class);
                        intent.putExtra(Constants.Fields.ORDERINFO, orderInfo);
                        startActivity(intent);
                        break;
                    case 1://已收货
                        Intent intent = new Intent(OrderDetailActivity.this, PublishEvaluateActivity.class);
                        intent.putExtra(Constants.Fields.ORDERINFO, orderInfo);
                        startActivity(intent);
                        break;
                }

                break;

            //接单
            case R.id.tv_receipt:
                getStoreListPresenter().sellerReceipt(userId, orderId);
                tv_receipt.setClickable(false);
                break;

            //联系卖家
            case R.id.rela_msg:
                if (comeInType == 1) {
                    RongIMutils.startToConversation(this, orderInfo.getBaseUser().getUserId() + "", orderInfo.getBaseUser().getNickName());
                } else {
                    RongIMutils.startToConversation(this, orderInfo.getStoreInfo().getBaseUser().getUserId() + "", orderInfo.getStoreInfo().getBaseUser().getNickName());
                }
                break;

            //语音通话
            case R.id.rela_video:
                intent = new Intent(this, VideoChatActivity.class);
                intent.putExtra(Constants.Fields.BASEUSER, orderInfo.getStoreInfo().getBaseUser());
                intent.putExtra(Constants.Fields.TYPE, 5);
                intent.putExtra(Constants.Fields.ROOMNUMBER, Utils.getRoomNumber());
                startActivity(intent);
                break;

            //拒绝退款
            case R.id.tv_refuse_refund:
                if (comeInType == 1) {
                    getStoreListPresenter().refuseRefund(orderId, 1);
                } else {
                    getStoreListPresenter().refuseRefund(orderId, 3);
                }
                break;
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_FINDORDERDETAIL:
                if (object instanceof FindOrderDetailResponse) {
                    FindOrderDetailResponse findOrderDetailResponse = (FindOrderDetailResponse) object;
                    if (findOrderDetailResponse.getCode() == 200) {
                        orderInfo = findOrderDetailResponse.getOrderInfo();
                        if (needRefreshList) {
                            OrderStatusEvent orderStatusEvent = new OrderStatusEvent(position, orderInfo.getOrderStatus());
                            if (isBuyerSign) {
                                orderStatusEvent.setBuyerSign(3);
                            }
                            EventBus.getDefault().post(orderStatusEvent);
                        }
                        needRefreshList = true;
                        initData();
                    } else {
                        Utils.showToastShortTime(findOrderDetailResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATEORDERSTATUS:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {
                        isBuyerSign = true;
                        getOrderPresenter().findOrderDetail(userId, orderId);
                    }
                    Utils.showToastShortTime(response.getMsg());
                }
                break;
            case InterfaceUrl.URL_SELLERRECEIPT:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                    if (baseResponse.getCode() == 200) {
                        EventBus.getDefault().post(new OrderStatusEvent(position, 0));
                        tv_receipt.setVisibility(View.GONE);
//                        tv_receipt.setBackgroundResource(R.drawable.shape_round_10_solid_cccccc);
//                        tv_receipt.setClickable(false);
                    }
                }
                break;
            case InterfaceUrl.URL_CONFIRMREFUND:
            case InterfaceUrl.URL_REFUSEREFUND:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                    if (baseResponse.getCode() == 200) {
                        getOrderPresenter().findOrderDetail(userId, orderId);
                        tv_school_goods_applicatrion_refund.setTextColor(getResources().getColor(R.color.color_333333));
                        tv_school_goods_applicatrion_refund.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
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

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("订单详情");
    }

    public OrderPresenter getOrderPresenter() {
        if (orderPresenter == null) {
            orderPresenter = new OrderPresenter(this);
        }
        return orderPresenter;
    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(OrderEvalusteAndRefundEvent event) {
        if (event == null) {
            return;
        }
        getOrderPresenter().findOrderDetail(userId, orderId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        if (orderPresenter != null) {
            orderPresenter.onDestroy();
        }
        orderPresenter = null;

        if (ll_order_status != null) {
            ll_order_status.removeAllViews();
        }
        ll_order_status = null;
        if (rv_try != null) {
            rv_try.removeAllViews();
        }
        rv_try = null;
        if (rv_shop != null) {
            rv_shop.removeAllViews();
        }
        rv_shop = null;

        if (tv_orderid != null) {
            tv_orderid = null;
        }
        if (tv_orderstatus != null) {
            tv_orderstatus = null;
        }
        if (tv_goods_receipt_time != null) {
            tv_goods_receipt_time = null;
        }
        if (tv_goods_receipt_code != null) {
            tv_goods_receipt_code = null;
        }
        if (tv_order_user_name != null) {
            tv_order_user_name = null;
        }
        if (tv_order_user_phone != null) {
            tv_order_user_phone = null;
        }
        if (tv_order_user_address != null) {
            tv_order_user_address = null;
        }
        if (tv_productdesc != null) {
            tv_productdesc = null;
        }
        if (iv_productimg != null) {
            iv_productimg = null;
        }
        if (tv_productname != null) {
            tv_productname = null;
        }
        if (tv_limited != null) {
            tv_limited = null;
        }
        if (tv_productprice != null) {
            tv_productprice = null;
        }
        if (tv_goods_receipt != null) {
            tv_goods_receipt = null;
        }
        if (iv_shop_productimg != null) {
            iv_shop_productimg = null;
        }
        if (tv_shop_productname != null) {
            tv_shop_productname = null;
        }
        if (tv_shop_productprice != null) {
            tv_shop_productprice = null;
        }
        if (tv_shop_now_productprice != null) {
            tv_shop_now_productprice = null;
        }
        if (tv_shop_num != null) {
            tv_shop_num = null;
        }
        if (tv_shop_praice != null) {
            tv_shop_praice = null;
        }
        if (tv_shop_goods_receipt != null) {
            tv_shop_goods_receipt = null;
        }
        if (tv_shop_bottom != null) {
            tv_shop_bottom = null;
        }

    }


}
