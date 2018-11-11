package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.MyPropsListResponse;
import cn.bjhdltcdn.p2plive.model.MyProps;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.WithdrawalsActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.MyPropsListAdapter;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import io.rong.eventbus.EventBus;

/**
 * Created by huwenhua on 2016/6/1.
 * 我的礼物中的收到礼物fragment
 */
public class ReceivedGiftFragment extends BaseFragment implements BaseView {
    private View rootView;
    private final int pageSize = 20;
    private int pageNumber = 1;
    private MyPropsListAdapter adapter;
    private UserPresenter mPresenter;
    private TextView totalView, tv_with;
    private RecyclerView recyclerView;
    private TextView tv_num_gold_total;
    private LinearLayout tv_withdraw;
    private static int giftType = 1;//1收到 2.送出
    private String exchangeDescImg, exchangeDescUrl;
    private View emptyView;
    private TextView empty_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_gift_received, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        recyclerView = rootView.findViewById(R.id.recycle_view);
        emptyView = rootView.findViewById(R.id.empty_view);
        empty_tv = rootView.findViewById(R.id.empty_textView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(0.5f)));

        adapter = new MyPropsListAdapter(giftType);
        recyclerView.setAdapter(adapter);

        // 刷新框架
        TwinklingRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_layout_view);
        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(getContext());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
                pageNumber = 1;
                requestData();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();
                requestData();
            }
        });
        tv_num_gold_total = rootView.findViewById(R.id.tv_num_gold_total);
        tv_withdraw = rootView.findViewById(R.id.tv_withdraw);
        tv_with = rootView.findViewById(R.id.tv_with);
        tv_withdraw.setVisibility(View.VISIBLE);
        tv_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳到收益提现界面
                if (!StringUtils.isEmpty(exchangeDescImg) && !StringUtils.isEmpty(exchangeDescUrl)) {
                    Intent intent = new Intent(getActivity(), WithdrawalsActivity.class);
                    intent.putExtra("exchangeDescImg", exchangeDescImg);
                    intent.putExtra("exchangeDescUrl", exchangeDescUrl);
                    startActivity(intent);
                } else {
                    Utils.showToastShortTime("加载失败，请刷新界面");
                }
            }
        });
    }


    @Override
    protected void onVisible(boolean isInit) {
        if (isInit) {
            requestData();
        }
    }

    public UserPresenter getmPresenter() {
        if (mPresenter == null) {
            mPresenter = new UserPresenter(this);
        }
        return mPresenter;
    }

    /**
     * 请求接收礼物数据
     */
    private void requestData() {
        getmPresenter().myPropsList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), giftType, pageSize, pageNumber);
    }


    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_MYPROPSLIST:
                if (object instanceof MyPropsListResponse) {
                    MyPropsListResponse myPropsListResponse = (MyPropsListResponse) object;
                    if (myPropsListResponse.getCode() == 200) {
                        if (!TextUtils.isEmpty(myPropsListResponse.getBlankHint())){
                            empty_tv.setText(myPropsListResponse.getBlankHint());
                        }
                        String rechargeStr = String.format(getResources().getString(R.string.str_no_withdrawals), myPropsListResponse.getUnExchangeGold());
                        tv_with.setText(rechargeStr);
                        exchangeDescImg = myPropsListResponse.getExchangeDescImg();
                        exchangeDescUrl = myPropsListResponse.getExchangeDescUrl();
                        List<MyProps> list = myPropsListResponse.getList();
                        if (pageNumber == 1) {
                            adapter.addData(list);
                        } else {
                            adapter.update(list);
                        }
                        int total = myPropsListResponse.getTotal();
                        if (total <= adapter.getItemCount()) {//没更多

                        } else { // 继续分页
                            pageNumber++;
                        }
                        if (myPropsListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                        String totalStr = String.format(getResources().getString(R.string.str_num_gold_total), myPropsListResponse.getTotalGold());
                        tv_num_gold_total.setText(totalStr);
                    }
                }
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (adapter != null) {
            adapter = null;
        }

        if (mPresenter != null) {
            mPresenter = null;
        }


        if (totalView != null) {
            totalView = null;
        }


        if (rootView != null) {
            ((ViewGroup) rootView).removeAllViews();
            rootView.destroyDrawingCache();
            rootView = null;
        }
    }
}
