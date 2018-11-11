package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateWithdrawCashEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetClertListResponse;
import cn.bjhdltcdn.p2plive.model.ClertInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.ClerkListAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

/**
 * Created by zhaiww on 2018/5/19.
 */

public class ClerkListActiviity extends BaseActivity implements BaseView {

    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView rv_clerk_item;
    private int pageNum = 1;
    private int pageSize = 10;
    private View empty_view;
    private GetStoreListPresenter getStoreListPresenter;
    private long userId;
    private ClerkListAdapter clerkListAdapter;
    public List<ClertInfo> list = new ArrayList<>();
    private ToolBarFragment titleFragment;
    public int mPosition;
    private TextView empty_tv;
    private int type;//1:卖家 2：买家
    private int comeInType;//1:发工资店员列表0：店员列表
    private double totalMoney;//总金额
    private String payMoney;//支付金额
    private long toUserId;
    private long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_staff_list);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        storeId = getIntent().getLongExtra(Constants.Fields.STORE_ID, 0);
        getStoreListPresenter = new GetStoreListPresenter(this);
        getStoreListPresenter.getClertList(userId, storeId, pageSize, pageNum);
        setTitle();
        initView();
    }

    private void initView() {
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        comeInType = getIntent().getIntExtra(Constants.Fields.COME_IN_TYPE, 0);
        totalMoney = getIntent().getDoubleExtra("totalMoney", 0);
        rv_clerk_item = findViewById(R.id.rv_clerk_item);
        rv_clerk_item.setHasFixedSize(true);
        LinearLayoutManager itemLinearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        rv_clerk_item.setLayoutManager(itemLinearlayoutManager);
        clerkListAdapter = new ClerkListAdapter(this);
        clerkListAdapter.setComeInType(comeInType);
        clerkListAdapter.setTotalMoney(totalMoney);
        rv_clerk_item.setAdapter(clerkListAdapter);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(2));
        rv_clerk_item.addItemDecoration(linearLayoutSpaceItemDecoration);

//        for (int i = 0; i < 10; i++) {
//            list.add(new ClertInfo());
//        }
        if (type == 1) {
            clerkListAdapter.setDeleteVisible(true);
        } else {
            clerkListAdapter.setDeleteVisible(false);
        }
        if(comeInType==1){
            clerkListAdapter.setDeleteVisible(true);
        }
//        clerkListAdapter.setmList(list);
        clerkListAdapter.setOnFireClick(new ClerkListAdapter.OnFireClick() {

            @Override
            public void onFireClick(int position) {
                mPosition = position;
                getStoreListPresenter.fireClerk(userId, clerkListAdapter.getmList().get(position).getUserId(), storeId);
            }

            @Override
            public void payOffCliclk(long toUserId, String moneyStr) {
                //发工资接口
                payMoney = moneyStr;
                toUserId = toUserId;
                getStoreListPresenter.saveShopUserPayOff(userId, toUserId, moneyStr);

            }
        });

        clerkListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ClerkListActiviity.this, ClerkDetailActivity.class);
                intent.putExtra(Constants.Fields.COME_IN_TYPE, type);
                if(storeId>0){
                    intent.putExtra(Constants.Fields.STORE_ID, storeId);
                }else{
                    intent.putExtra(Constants.Fields.STORE_ID, clerkListAdapter.getmList().get(position).getStoreId());
                }
                intent.putExtra(Constants.Fields.TO_USER_ID, clerkListAdapter.getmList().get(position).getUserId());
                startActivity(intent);
            }
        });

        empty_view = findViewById(R.id.empty_view);
        empty_tv = empty_view.findViewById(R.id.empty_textView);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        LoadingView loadingView = new LoadingView(getApplicationContext());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(rv_clerk_item);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                getStoreListPresenter.getClertList(userId, storeId, pageSize, pageNum);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageNum++;
                getStoreListPresenter.getClertList(userId, storeId, pageSize, pageNum);
            }
        });
        empty_view.setVisibility(View.GONE);
    }

    private void setTitle() {
        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        if(storeId>0){
            titleFragment.setTitleView("查看店员");
        }else{
            titleFragment.setTitleView("发工资");
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETCLERTLIST:
                if (object instanceof GetClertListResponse) {
                    GetClertListResponse getClertListResponse = (GetClertListResponse) object;
                    if (getClertListResponse.getCode() == 200) {
                        clerkListAdapter.setmList(getClertListResponse.getList());

                        if (!TextUtils.isEmpty(getClertListResponse.getBlankHint())) {
                            empty_tv.setText(getClertListResponse.getBlankHint());
                        }

                        refreshLayout.finishRefreshing();
                        refreshLayout.finishLoadmore();

                        if (getClertListResponse.getTotal() == 0) {
                            empty_view.setVisibility(View.VISIBLE);
                        } else {
                            empty_view.setVisibility(View.GONE);
                        }

                        if (getClertListResponse.getTotal() <= pageNum * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadmore(false);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadmore(true);
                        }
                    } else {
                        Utils.showToastShortTime(getClertListResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_FIRECLERK:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    if (baseResponse.getCode() == 200) {
                        clerkListAdapter.getmList().remove(mPosition);
                        clerkListAdapter.notifyItemRemoved(mPosition);
                    }
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
                break;
            case InterfaceUrl.URL_SAVESHOPUSERPAYOFF:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    if (baseResponse.getCode() == 200) {
                        //更新提现界面
                        EventBus.getDefault().post(new UpdateWithdrawCashEvent());
                        totalMoney = totalMoney - Double.parseDouble(payMoney);
                        clerkListAdapter.setTotalMoney(totalMoney);
                    }
                    Utils.showToastShortTime(baseResponse.getMsg());
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
}
