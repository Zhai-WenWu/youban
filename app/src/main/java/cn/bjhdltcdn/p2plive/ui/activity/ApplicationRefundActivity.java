package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.OrderEvalusteAndRefundEvent;
import cn.bjhdltcdn.p2plive.httpresponse.ApplyRefundResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindCashAccountResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetRefundReasonListResponse;
import cn.bjhdltcdn.p2plive.model.CashAccount;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.model.RefundReason;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ExchangePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.OrderPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.SchoolShopOrderDetailAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ApplicationRefundSuccessDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.tagview.TagContainerLayout;
import cn.bjhdltcdn.p2plive.widget.tagview.TagView;

/**
 * Created by zhaiww on 2018/5/10.
 */

public class ApplicationRefundActivity extends BaseActivity implements BaseView {

    private EditText et_remarks;
    private TextView tv_num_content;
    private TextView tv_order_num;
    private TextView tv_send_way;
    private TextView tv_submission;
    private ProductOrder productOrder;
    private OrderPresenter orderPresenter;
    private long userId;
    private TagContainerLayout tag_container_view;
    private ArrayList<Object> objectList = new ArrayList<>();
    private TextView tv_store_info;
    private List<RefundReason> refundReasonList;
    private long reasonId;
    private TitleFragment titleFragment;
    private RecyclerView rv_school_shop;
    private TextView tv_school_goods_num;
    private TextView tv_school_postage;
    private ExchangePresenter exchangePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_refund);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        getOrderPresenter().getRefundReasonList(userId);
        initView();
        initData();
        setTitle();
    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle("申请退款");
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    private void initView() {
        et_remarks = findViewById(R.id.et_remarks);
        tv_num_content = findViewById(R.id.tv_num_content);
        tv_order_num = findViewById(R.id.tv_order_num);
        tv_send_way = findViewById(R.id.tv_send_way);
        tv_store_info = findViewById(R.id.tv_store_info);
        tv_submission = findViewById(R.id.tv_submission);
        tag_container_view = findViewById(R.id.tag_container_view);
        rv_school_shop = findViewById(R.id.rv_school_shop);
        tv_school_goods_num = findViewById(R.id.tv_school_goods_num);
        tv_school_postage = findViewById(R.id.tv_school_postage);
        tag_container_view.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {
                RefundReason o = (RefundReason) objectList.get(position);
                for (int i = 0; i < objectList.size(); i++) {
                    TagView tagView = tag_container_view.getTagView(i);
                    tagView.setTagBackgroundColor(getResources().getColor(R.color.color_ffffff));
                    if (i == position) {
                        reasonId = refundReasonList.get(position).getReasonId();
//                        TagView tagView = tag_container_view.getTagView(position);
                        tagView.setTagBackgroundColor(getResources().getColor(R.color.color_ffee00));
                    }

                }

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        et_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() <= 500) {
                    tv_num_content.setText(s.length() + "/500");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        tv_submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reasonId == 0) {
                    Utils.showToastShortTime("请选择退款原因");
                } else {
                    getOrderPresenter().applyRefund(userId, productOrder.getOrderId(), reasonId, et_remarks.getText().toString());
                }
            }
        });
    }


    private void initData() {
        productOrder = getIntent().getParcelableExtra(Constants.Fields.ORDERINFO);
        StoreInfo storeInfo = productOrder.getStoreInfo();

        if (storeInfo != null) {
            tv_store_info.setText(storeInfo.getTitle());
        }

        SchoolShopOrderDetailAdapter schoolShopOrderDetailAdapter = new SchoolShopOrderDetailAdapter(this);
        rv_school_shop.setHasFixedSize(true);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(10));
        rv_school_shop.setLayoutManager(linearlayoutManager);
        rv_school_shop.addItemDecoration(linearLayoutSpaceItemDecoration);
        rv_school_shop.setAdapter(schoolShopOrderDetailAdapter);
        schoolShopOrderDetailAdapter.setmList(productOrder.getProductList());

        tv_school_goods_num.setText("共计" + productOrder.getTotalProductCount() + "件商品   合计：¥" + productOrder.getTotalPrice());

        tv_order_num.setText("订单编号：" + productOrder.getOrderNumber());

        int distributeMode = productOrder.getDistributeMode();
        if (distributeMode == 36) {
            tv_send_way.setText("配送方式：卖家配送");
        } else if (distributeMode == 37) {
            tv_send_way.setText("配送方式：自提");
        }

        if (productOrder.getPostAge() == null) {
            tv_school_postage.setText("配送费：¥0");
        } else {
            if (StringUtils.isEmpty(productOrder.getPostAge())) {
                tv_school_postage.setText("配送费：¥0");
            } else {
                tv_school_postage.setText("配送费：¥" + productOrder.getPostAge());
            }
        }


    }

    public OrderPresenter getOrderPresenter() {
        if (orderPresenter == null) {
            orderPresenter = new OrderPresenter(this);
        }
        return orderPresenter;
    }

    public ExchangePresenter getExchangePresenter() {
        if (exchangePresenter == null) {
            exchangePresenter = new ExchangePresenter(this);
        }
        return exchangePresenter;
    }


    @Override
    public void updateView(String apiName, Object object) {

        if (apiName.equals(InterfaceUrl.URL_APPLYREFUND)) {
            if (object instanceof ApplyRefundResponse) {
                ApplyRefundResponse baseResponse = (ApplyRefundResponse) object;
                if (baseResponse.getCode() == 200) {
                    EventBus.getDefault().post(new OrderEvalusteAndRefundEvent());
                    ApplicationRefundSuccessDialog dialog = new ApplicationRefundSuccessDialog();
                    dialog.show(getSupportFragmentManager());
                    if (!StringUtils.isEmpty(baseResponse.getAccount())) {
                        dialog.setTipTextGone();
                    }
                } else {
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_GETREFUNDREASONLIST)) {
            if (object instanceof GetRefundReasonListResponse) {
                GetRefundReasonListResponse getRefundReasonListResponse = (GetRefundReasonListResponse) object;
                refundReasonList = getRefundReasonListResponse.getList();
                if (refundReasonList != null) {
                    objectList.addAll(refundReasonList);
                    tag_container_view.setTags(objectList);
                }
            }
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
