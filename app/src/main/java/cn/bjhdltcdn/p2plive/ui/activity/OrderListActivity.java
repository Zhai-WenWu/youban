package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.OrderStatusEvent;
import cn.bjhdltcdn.p2plive.httpresponse.GetLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetMyOrderListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.RefuseRefundResponse;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.OrderPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeTabRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.OrderAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.GoodsreceiptDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.DiviceSizeUtil;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * Created by zhaiww on 2018/4/16.
 */

public class OrderListActivity extends BaseActivity implements BaseView {

    private RecyclerView itemRecycleView;
    private OrderPresenter orderPresenter;
    private int pageSize = 10, pageNum = 1;
    private long userId;
    private TwinklingRefreshLayout refreshLayout;
    private LoadingView loadingView;
    private View emptyView;
    private TextView empty_tv;
    private OrderAdapter orderAdapter;
    private LinearLayout ll_tab;
    private DiscoverPresenter discoverPresenter;
    private long lableId;
    private int onRefusingRefundClickPosition;
    private GetStoreListPresenter getStoreListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item);
        EventBus.getDefault().register(this);
        setTitle();
        initView();
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        getDiscoverPresenter().getLabelList(5);
        lableId = 22;
        getOrderPresenter().getMyOrderList(userId, lableId, pageSize, pageNum);
    }

    private void initData(List<LabelInfo> labelList) {
        for (int i = 0; i < labelList.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.order_list_tag, null);
            Button bt = view.findViewById(R.id.bt);
            //tag数据填充
            View bt_bottom = view.findViewById(R.id.bt_bottom);
            if (i == 0) {
                bt_bottom.setVisibility(View.VISIBLE);
            }
            bt.setText(labelList.get(i).getLabelName());

            bt.setOnClickListener(new OnTagClickListener(labelList, i));
            ll_tab.setVisibility(View.VISIBLE);
            ll_tab.addView(view);
            //控件平分宽度
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = DiviceSizeUtil.getScreenWidth(App.getInstance()) / labelList.size();
            view.setLayoutParams(layoutParams);
        }

    }

    //tag点击事件
    class OnTagClickListener implements View.OnClickListener {
        int position;
        List<LabelInfo> labelList;

        public OnTagClickListener(List<LabelInfo> labelList, int position) {
            this.position = position;
            this.labelList = labelList;
        }

        @Override
        public void onClick(View v) {
            lableId = labelList.get(position).getLabelId();
            getOrderPresenter().getMyOrderList(userId, lableId, pageSize, pageNum);

            for (int i = 0; i < labelList.size(); i++) {
                View childView = ll_tab.getChildAt(i);
                if (i == position) {
                    childView.findViewById(R.id.bt_bottom).setVisibility(View.VISIBLE);
                } else {
                    childView.findViewById(R.id.bt_bottom).setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void initView() {
        emptyView = findViewById(R.id.empty_view);
        empty_tv = emptyView.findViewById(R.id.empty_textView);
        ll_tab = findViewById(R.id.ll_tab);
        HomeTabRecyclerViewAdapter homeTabRecyclerViewAdapter = new HomeTabRecyclerViewAdapter(this);
        itemRecycleView = (RecyclerView) findViewById(R.id.rv_order_item);
        itemRecycleView.setHasFixedSize(true);
        LinearLayoutManager itemLinearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        itemRecycleView.setLayoutManager(itemLinearlayoutManager);
        orderAdapter = new OrderAdapter(this);
        itemRecycleView.setAdapter(orderAdapter);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(11));
        itemRecycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
        orderAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                intent.putExtra(Constants.Fields.ORDER_ID, orderAdapter.getmList().get(position).getOrderId());
                intent.putExtra(Constants.Fields.POSITION, position);
                startActivity(intent);
            }
        });
        orderAdapter.setOnCollectGoodsClick(new OrderAdapter.OnCollectGoodsClick() {
            @Override
            public void OnCollectGoodsClick(long orderId, final int position) {
                ProductOrder productOrder = orderAdapter.getmList().get(position);
                int orderStatus = productOrder.getOrderStatus();
                if (orderStatus == 0 || orderStatus == 2 || orderStatus == 3 || (orderStatus == 1 && productOrder.getBuyerSign() != 3)) {//待收货
                    GoodsreceiptDialog dialog = new GoodsreceiptDialog();
                    dialog.setOrderId(orderId);
                    dialog.setIsSeller(false);
                    dialog.show(getSupportFragmentManager());
                    dialog.setItemClick(new GoodsreceiptDialog.ItemClick() {
                        @Override
                        public void itemClick() {
                            orderAdapter.getmList().get(position).setOrderStatus(1);
                            orderAdapter.notifyItemChanged(position);
                        }
                    });
                } else if (orderStatus == 1 && productOrder.getIsEval() != 1) {
                    if (orderAdapter.getmList().get(position).getProductInfo().getProductType() == 1) {
                        Intent intent = new Intent(OrderListActivity.this, PublishActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(OrderListActivity.this, PublishEvaluateActivity.class);
                        intent.putExtra(Constants.Fields.ORDERINFO, orderAdapter.getmList().get(position));
                        startActivity(intent);
                    }
                }
            }
        });

        orderAdapter.setOnRefusingRefundClick(new OrderAdapter.OnRefusingRefundClick() {

            @Override
            public void OnRefusingRefundClick(long orderId, int position) {//拒绝退款
                onRefusingRefundClickPosition = position;
                getStoreListPresenter().refuseRefund(orderId, 1);
            }
        });

        //刷新
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        loadingView = new LoadingView(getApplicationContext());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(itemRecycleView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                getOrderPresenter().getMyOrderList(userId, lableId, pageSize, pageNum);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageNum++;
                if (!loadingView.isOnLoadFinish()) {
                    getOrderPresenter().getMyOrderList(userId, lableId, pageSize, pageNum);
                }
            }
        });
        emptyView.setVisibility(View.GONE);
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("我的订单");
    }

    public OrderPresenter getOrderPresenter() {
        if (orderPresenter == null) {
            orderPresenter = new OrderPresenter(this);
        }
        return orderPresenter;
    }

    public DiscoverPresenter getDiscoverPresenter() {
        if (discoverPresenter == null) {
            discoverPresenter = new DiscoverPresenter(this);
        }
        return discoverPresenter;
    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }


    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETMYORDERLIST:
                if (object instanceof GetMyOrderListResponse) {
                    GetMyOrderListResponse getMyOrderListResponse = (GetMyOrderListResponse) object;
                    if (getMyOrderListResponse.getCode() == 200) {
                        refreshLayout.finishRefreshing();
                        refreshLayout.finishLoadmore();
                        List<ProductOrder> list = getMyOrderListResponse.getList();

                        if (!TextUtils.isEmpty(getMyOrderListResponse.getBlankHint())) {
                            empty_tv.setText(getMyOrderListResponse.getBlankHint());
                        }

                        if (getMyOrderListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }

                        if (list != null) {
                            if (pageNum == 1) {
                                refreshLayout.finishRefreshing();
                                orderAdapter.setList(list);
                            } else {
                                refreshLayout.finishLoadmore();
                                orderAdapter.addList(list);
                            }

                            orderAdapter.notifyDataSetChanged();
                            if (getMyOrderListResponse.getTotal() <= pageNum * pageSize) {
                                //没有更多数据时  下拉刷新不可用
                                refreshLayout.setEnableLoadmore(false);
                            } else {
                                //有更多数据时  下拉刷新才可用
                                refreshLayout.setEnableLoadmore(true);
                            }
                        }
                    } else {
                        Utils.showToastShortTime(getMyOrderListResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_GETLABELLIST:
                if (object instanceof GetLabelListResponse) {
                    GetLabelListResponse response = (GetLabelListResponse) object;
                    if (response.getCode() == 200 && response.getLabelList().size() > 0) {
                        initData(response.getLabelList());
                    }
                }
            case InterfaceUrl.URL_REFUSEREFUND:
                if (object instanceof RefuseRefundResponse) {
                    RefuseRefundResponse refuseRefundResponse = (RefuseRefundResponse) object;
                    Utils.showToastShortTime(refuseRefundResponse.getMsg());
                    if (refuseRefundResponse.getCode() == 200) {
                        orderAdapter.getmList().get(onRefusingRefundClickPosition).setOrderStatus(refuseRefundResponse.getOrderStatus());
                        orderAdapter.notifyItemChanged(onRefusingRefundClickPosition);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(OrderStatusEvent event) {
        if (event == null) {
            return;
        }

        orderAdapter.getmList().get(event.getPosition()).setOrderStatus(event.getOrderStatus());

        if (event.getBuyerSign() == 3) {
            orderAdapter.getmList().get(event.getPosition()).setBuyerSign(3);
        }
        orderAdapter.notifyItemChanged(event.getPosition());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (orderPresenter != null) {
            orderPresenter.onDestroy();
        }
        orderPresenter = null;
        if (discoverPresenter != null) {
            discoverPresenter.onDestroy();
        }
        discoverPresenter = null;

        if (itemRecycleView != null) {
            itemRecycleView.removeAllViews();
        }
        itemRecycleView = null;

        if (ll_tab != null) {
            ll_tab.removeAllViews();
        }
        ll_tab = null;

    }
}
