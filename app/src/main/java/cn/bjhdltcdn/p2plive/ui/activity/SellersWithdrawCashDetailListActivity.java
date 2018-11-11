package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetExchangeListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetMyTransactionResponse;
import cn.bjhdltcdn.p2plive.model.ExchangeRecord;
import cn.bjhdltcdn.p2plive.model.TransactionRecord;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.WithdrawCashRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * 卖家提现明细列表
 */
public class SellersWithdrawCashDetailListActivity extends BaseActivity implements BaseView {
    private GetStoreListPresenter presenter;
    private UserPresenter userPresenter;
    private TitleFragment titleFragment;
    private RefreshLayout refreshLayout;
    private RecyclerView recycleView;
    private WithdrawCashRecyclerViewAdapter recyclerAdapter;
    private int pageSize = 20, pageNumber = 1;
    private View emptyView;
    private long userId;
    private TextView empty_textView;

    public GetStoreListPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GetStoreListPresenter(this);
        }
        return presenter;
    }

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_withdraw_cash_detail_list);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        Intent intent = getIntent();
        initView();
        setTitle();
//        getPresenter().getExchangeList(userId,pageSize,pageNumber);
        getUserPresenter().myTransactionRecordList(userId, pageSize, pageNumber);
    }

    public void initView() {
        emptyView = findViewById(R.id.empty_view);
        empty_textView = emptyView.findViewById(R.id.empty_textView);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        recycleView = (RecyclerView) findViewById(R.id.detail_recycler_view);
        recyclerAdapter = new WithdrawCashRecyclerViewAdapter(this);
        recycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.setAdapter(recyclerAdapter);
        emptyView.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNumber = 1;
//                getPresenter().getExchangeList(userId,pageSize,pageNumber);
                getUserPresenter().myTransactionRecordList(userId, pageSize, pageNumber);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                getPresenter().getExchangeList(userId,pageSize,pageNumber);
                getUserPresenter().myTransactionRecordList(userId, pageSize, pageNumber);
            }
        });
        refreshLayout.setEnableLoadMore(false);

    }


    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_withdraw_cash_detail);
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
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
        if (apiName.equals(InterfaceUrl.URL_GETEXCHANGELIST)) {
            if (object instanceof GetExchangeListResponse) {
                GetExchangeListResponse getExchangeListResponse = (GetExchangeListResponse) object;
                if (pageNumber == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
                if (getExchangeListResponse.getCode() == 200) {
                    List<ExchangeRecord> exchangeRecordList = getExchangeListResponse.getList();
                    if (exchangeRecordList != null && exchangeRecordList.size() > 0) {
//                        if(pageNumber==1){
//                            recyclerAdapter.setList(exchangeRecordList);
//                        }else{
//                            recyclerAdapter.addList(exchangeRecordList);
//                        }
//                        recyclerAdapter.notifyDataSetChanged();
                        if (getExchangeListResponse.getTotal() <= pageNumber * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadMore(true);
                            pageNumber++;
                        }
                        if (getExchangeListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            ((TextView) emptyView.findViewById(R.id.empty_textView)).setText(getExchangeListResponse.getBlankHint());
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }

                } else {
                    Utils.showToastShortTime(getExchangeListResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_GETTRANSACTIONRECORDLIST)) {
            if (object instanceof GetMyTransactionResponse) {
                GetMyTransactionResponse myTransactionResponse = (GetMyTransactionResponse) object;
                if (pageNumber == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
                if (myTransactionResponse.getCode() == 200) {
                    empty_textView.setText(myTransactionResponse.getBlankHint());
                    List<TransactionRecord> exchangeRecordList = myTransactionResponse.getList();
                    if (exchangeRecordList != null && exchangeRecordList.size() > 0) {
                        if (pageNumber == 1) {
                            recyclerAdapter.setList(exchangeRecordList);
                        } else {
                            recyclerAdapter.addList(exchangeRecordList);
                        }
                        recyclerAdapter.notifyDataSetChanged();
                        if (myTransactionResponse.getTotal() <= pageNumber * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadMore(true);
                            pageNumber++;
                        }
                        if (myTransactionResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            ((TextView) emptyView.findViewById(R.id.empty_textView)).setText(myTransactionResponse.getBlankHint());
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Utils.showToastShortTime(myTransactionResponse.getMsg());
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
