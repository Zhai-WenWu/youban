package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.OrderStatusEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOrderListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.RefuseRefundResponse;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.OrderDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.HomePostLabelRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.OrderAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.GoodsreceiptDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * Created by Hu_PC on 2017/11/8.
 * 商品列表fragment
 */

public class OrderRecycleFragment extends BaseFragment implements BaseView {
    private View rootView;
    private ShopDetailActivity mActivity;
    private RefreshLayout refreshLayout;
    private GetStoreListPresenter getStoreListPresenter;
    private DiscoverPresenter discoverPresenter;
    private RecyclerView labelRecycleView, recycleView;
    private HomePostLabelRecyclerViewAdapter homePostLabelRecyclerViewAdapter;
    private OrderAdapter orderAdapter;
    private View emptyView;
    private TextView emptyTextView;
    private int currenPosition;
    private long userId, storeId;
    private int pageSize = 10, pageNumber = 1;
    private List<LabelInfo> labelInfoList;
    private int selectLabelPosition;
    public int sellerReceiptPositon;
    private int onRefundClickPosition;
    private int onRefusingRefundClickPosition;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    public DiscoverPresenter getDiscoverPresenter() {
        if (discoverPresenter == null) {
            discoverPresenter = new DiscoverPresenter(this);
        }
        return discoverPresenter;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public void getOrderList(int pageNumber) {
        this.pageNumber = pageNumber;
        getStoreListPresenter().getOrderList(userId, storeId, labelInfoList.get(selectLabelPosition).getLabelId(), pageSize, pageNumber);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (ShopDetailActivity) context;
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
            rootView = inflater.inflate(R.layout.fragment_evaluate_recycle, null);
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
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
    }

    @Override
    protected void onVisible(boolean isInit) {
        if (isInit) {
            userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
            if (labelInfoList == null || labelInfoList.size() < 1) {
                getDiscoverPresenter().getLabelList(6);
            }
        } else {
//            getOrderList(1);
        }
    }

    private void initView() {
//        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refreshLayout);
//        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                getOrderList(pageNumber);
//            }
//        });
//        refreshLayout.setEnableLoadMore(false);
//        refreshLayout.setEnableAutoLoadMore(false);
//        refreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        emptyView = rootView.findViewById(R.id.empty_view);
        emptyTextView = rootView.findViewById(R.id.empty_textView);
        labelRecycleView = rootView.findViewById(R.id.recycler_label);
        homePostLabelRecyclerViewAdapter = new HomePostLabelRecyclerViewAdapter(this.getActivity());
        labelRecycleView.setHasFixedSize(true);

        homePostLabelRecyclerViewAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectLabelPosition = position;
                Object o = homePostLabelRecyclerViewAdapter.getItem(position);
                if (o instanceof LabelInfo) {
                    LabelInfo labelInfo = (LabelInfo) o;
                    labelInfo.setCheck(true);
                    for (int i = 0; i < homePostLabelRecyclerViewAdapter.getList().size(); i++) {
                        Object o1 = homePostLabelRecyclerViewAdapter.getList().get(i);
                        if (o1 instanceof LabelInfo) {
                            LabelInfo labelInfo1 = (LabelInfo) o1;
                            if (labelInfo1.getLabelId() != labelInfo.getLabelId()) {
                                labelInfo1.setCheck(false);
                            }
                        }

                    }
                    homePostLabelRecyclerViewAdapter.notifyDataSetChanged();
                    pageNumber = 1;
                    getStoreListPresenter().getOrderList(userId, storeId, labelInfo.getLabelId(), pageSize, pageNumber);
                }

            }
        });

        recycleView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        orderAdapter = new OrderAdapter(getActivity());
        orderAdapter.setComeInType(1);
        recycleView.setHasFixedSize(true);
        LinearLayoutManager itemLinearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        recycleView.setLayoutManager(itemLinearlayoutManager);
        recycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(11)));
        recycleView.setAdapter(orderAdapter);
        orderAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到订单详情页
                currenPosition = position;
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra(Constants.Fields.ORDER_ID, orderAdapter.getmList().get(position).getOrderId());
                intent.putExtra(Constants.Fields.POSITION, position);
                intent.putExtra(Constants.Fields.TYPE, 1);
                startActivity(intent);

            }
        });
        orderAdapter.setOnReceiptClick(new OrderAdapter.OnReceiptClick() {
            @Override
            public void OnReceiptClick(long orderId, int position) {
                //接单
                sellerReceiptPositon = position;
                getStoreListPresenter().sellerReceipt(userId, orderAdapter.getmList().get(position).orderId);
            }
        });
        orderAdapter.setOnCollectGoodsClick(new OrderAdapter.OnCollectGoodsClick() {
            @Override
            public void OnCollectGoodsClick(long orderId, final int position) {
                //确认收货
                sellerReceiptPositon = position;
                GoodsreceiptDialog dialog = new GoodsreceiptDialog();
                dialog.setOrderId(orderId);
                dialog.setIsSeller(true);
                dialog.show(getActivity().getSupportFragmentManager());
                dialog.setItemClick(new GoodsreceiptDialog.ItemClick() {
                    @Override
                    public void itemClick() {
                        orderAdapter.getmList().get(position).setOrderStatus(1);
                        orderAdapter.notifyItemChanged(position);
                    }
                });
            }
        });
        orderAdapter.setOnRefundClick(new OrderAdapter.OnRefundClick() {
            @Override
            public void OnRefundClick(long orderId, int position) {
                onRefundClickPosition = position;
                getStoreListPresenter().confirmRefund(userId, orderId);
            }
        });
        orderAdapter.setOnRefusingRefundClick(new OrderAdapter.OnRefusingRefundClick() {

            @Override
            public void OnRefusingRefundClick(long orderId, int position) {//拒绝退款
                onRefusingRefundClickPosition = position;
                getStoreListPresenter().refuseRefund(orderId, 1);
            }
        });
        emptyView.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(OrderStatusEvent event) {
        if (event == null) {
            return;
        }

        orderAdapter.getmList().get(event.getPosition()).setOrderStatus(event.getOrderStatus());
        orderAdapter.notifyItemChanged(event.getPosition());
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            if (pageNumber == 1) {
                if (mActivity != null) {
                    mActivity.finishRefresh(false);
                }
            } else {
                if (mActivity != null) {
                    mActivity.finishLoadMore(false);
//                    refreshLayout.finishLoadMore(false);
                }
            }
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_GETORDERLIST)) {
            if (object instanceof GetOrderListResponse) {
                GetOrderListResponse getOrderListResponse = (GetOrderListResponse) object;
                if (pageNumber == 1) {
                    if (mActivity != null) {
                        mActivity.finishRefresh(true);
                    }
                } else {
                    if (mActivity != null) {
                        mActivity.finishLoadMore(true);
//                        refreshLayout.finishLoadMore();
                    }
                }
                if (getOrderListResponse.getCode() == 200) {
                    List<ProductOrder> list = getOrderListResponse.getList();
                    if (list != null && list.size() > 0) {
                        recycleView.setVisibility(View.VISIBLE);
                        if (pageNumber == 1) {
                            orderAdapter.setList(list);
                        } else {
                            orderAdapter.addList(list);
                        }
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        orderAdapter.setList(list);
                        orderAdapter.notifyDataSetChanged();
                    }
                    if (getOrderListResponse.getTotal() <= pageNumber * pageSize) {
                        //没有更多数据时  下拉刷新不可用
                        mActivity.setEnableLoadMore(false, 2);
//                        refreshLayout.setEnableLoadMore(false);
                    } else {
                        //有更多数据时  下拉刷新才可用
                        mActivity.setEnableLoadMore(true, 2);
//                        refreshLayout.setEnableLoadMore(true);
                        pageNumber++;
                    }

                    if (getOrderListResponse.getTotal() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        ((TextView) emptyView.findViewById(R.id.empty_textView)).setText(getOrderListResponse.getBlankHint());
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                } else {
                    Utils.showToastShortTime(getOrderListResponse.getMsg());
                }
            }
        } else if (InterfaceUrl.URL_GETLABELLIST.equals(apiName)) {
            if (object instanceof GetLabelListResponse) {
                GetLabelListResponse getLabelListResponse = (GetLabelListResponse) object;
                if (getLabelListResponse.getCode() == 200) {
                    labelInfoList = getLabelListResponse.getLabelList();
                    if (labelInfoList != null && labelInfoList.size() > 0) {
                        labelInfoList.get(0).setCheck(true);
                        List<Object> list = new ArrayList<Object>();
                        list.addAll(labelInfoList);
                        homePostLabelRecyclerViewAdapter.setList(list);
                        homePostLabelRecyclerViewAdapter.notifyDataSetChanged();
                        if (homePostLabelRecyclerViewAdapter.getItemCount() > 0) {
                            GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), labelInfoList.size());
                            labelRecycleView.setLayoutManager(layoutManager);
                            labelRecycleView.setAdapter(homePostLabelRecyclerViewAdapter);
                            getOrderList(1);
                        } else {
                            labelRecycleView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } else if (InterfaceUrl.URL_SELLERRECEIPT.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse baseResponse = (BaseResponse) object;
                Utils.showToastShortTime(baseResponse.getMsg());
                if (baseResponse.getCode() == 200) {
                    orderAdapter.getmList().get(sellerReceiptPositon).setIsReceipt(1);
                    orderAdapter.notifyItemChanged(sellerReceiptPositon);
                }
            }
        } else if (InterfaceUrl.URL_REFUSEREFUND.equals(apiName)) {
            if (object instanceof RefuseRefundResponse) {
                RefuseRefundResponse refuseRefundResponse = (RefuseRefundResponse) object;
                Utils.showToastShortTime(refuseRefundResponse.getMsg());
                if (refuseRefundResponse.getCode() == 200) {
                    orderAdapter.getmList().get(onRefusingRefundClickPosition).setOrderStatus(refuseRefundResponse.getOrderStatus());
                    orderAdapter.notifyItemChanged(onRefusingRefundClickPosition);
                }
            }
        } else if (InterfaceUrl.URL_CONFIRMREFUND.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse baseResponse = (BaseResponse) object;
                Utils.showToastShortTime(baseResponse.getMsg());
                if (baseResponse.getCode() == 200) {
                    orderAdapter.getmList().get(onRefundClickPosition).setOrderStatus(4);
                    orderAdapter.notifyItemChanged(onRefundClickPosition);
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
        EventBus.getDefault().unregister(this);
    }
}
