package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetSalesRecordListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.RefuseRefundResponse;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.OrderDetailActivity;
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

public class SalesRecordRecycleFragment extends BaseFragment implements BaseView {
    private View rootView;
    private AppCompatActivity mActivity;
    private RefreshLayout refreshLayout;
    private GetStoreListPresenter getStoreListPresenter;
    private RecyclerView recycleView;
    private OrderAdapter orderAdapter;
    private View emptyView;
    private int currenPosition;
    private long userId, storeId;
    private int pageSize = 10, pageNumber = 1;
    private int type;//0:全部1：本月
    private int sellerReceiptPositon;
    private TextView tipView;
    private int onRefundClickPosition;
    private int onRefusingRefundClickPosition;

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

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
            rootView = inflater.inflate(R.layout.fragment_sales_record_list_layout, null);
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

    @Override
    protected void onVisible(boolean isInit) {
        if (isInit) {
            pageNumber = 1;
            userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
            getStoreListPresenter().getSalesRecordtList(userId, storeId, type, pageSize, pageNumber);
        }
    }

    private void initView() {
        tipView = rootView.findViewById(R.id.tip_view);
        emptyView = rootView.findViewById(R.id.empty_view);
        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNumber = 1;
                getStoreListPresenter().getSalesRecordtList(userId, storeId, type, pageSize, pageNumber);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getStoreListPresenter().getSalesRecordtList(userId, storeId, type, pageSize, pageNumber);
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableNestedScroll(true);
        recycleView = (RecyclerView) rootView.findViewById(R.id.sales_record_recycler_view);
        recycleView.setHasFixedSize(true);
        orderAdapter = new OrderAdapter(getActivity());
        orderAdapter.setSaleRecord(true);
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


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            if (pageNumber == 1) {
                refreshLayout.finishRefresh(false);//传入false表示刷新失败
            } else {
                refreshLayout.finishLoadMore(false);//传入false表示加载失败
            }
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_GETSALESRECORDTLIST)) {
            if (object instanceof GetSalesRecordListResponse) {
                GetSalesRecordListResponse getSalesRecordListResponse = (GetSalesRecordListResponse) object;
                if (pageNumber == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
                if (getSalesRecordListResponse.getCode() == 200) {
                    List<ProductOrder> list = getSalesRecordListResponse.getList();
                    if (list != null) {
                        if (pageNumber == 1) {
                            orderAdapter.setList(list);
                        } else {
                            orderAdapter.addList(list);
                        }
                        orderAdapter.notifyDataSetChanged();
                        if (getSalesRecordListResponse.getTotal() <= pageNumber * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadMore(true);
                            pageNumber++;
                        }
                        if (getSalesRecordListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            ((TextView) emptyView.findViewById(R.id.empty_textView)).setText(getSalesRecordListResponse.getBlankHint());
                            tipView.setVisibility(View.GONE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                            tipView.setVisibility(View.VISIBLE);
                            tipView.setText(getSalesRecordListResponse.getContent());
                        }
                    }
                } else {
                    Utils.showToastShortTime(getSalesRecordListResponse.getMsg());
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
            if (object instanceof BaseResponse) {
                BaseResponse baseResponse = (BaseResponse) object;
                Utils.showToastShortTime(baseResponse.getMsg());
                if (baseResponse.getCode() == 200) {
                    orderAdapter.getmList().get(sellerReceiptPositon).setOrderStatus(0);
                    orderAdapter.notifyItemChanged(sellerReceiptPositon);
                }
            }
        } else if (InterfaceUrl.URL_REFUSEREFUND.equals(apiName)) {
            if (object instanceof RefuseRefundResponse) {
                RefuseRefundResponse refuseRefundResponse = (RefuseRefundResponse) object;
                Utils.showToastShortTime(refuseRefundResponse.getMsg());
                if (refuseRefundResponse.getCode() == 200) {
//                    orderAdapter.getmList().get(onRefusingRefundClickPosition).setOrderStatus(refuseRefundResponse.getOrderStatus());
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
    }
}
