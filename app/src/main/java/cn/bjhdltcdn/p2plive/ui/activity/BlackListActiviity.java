package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetBlackListResponse;
import cn.bjhdltcdn.p2plive.model.BlackUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.BlackListAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 黑名单列表
 */
public class BlackListActiviity extends BaseActivity implements BaseView {

    private RecyclerView recyclerView;
    private View emptyView;
    private TextView empty_tv;
    private BlackListAdapter blackListAdapter;
    private int pageNumber = 1;
    private int pageSize = 20;
    private int total;
    // 操作当前item
    private int position = -1;
    private UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        userPresenter = new UserPresenter(this);
        setTitle();
        init();
    }

    public void init() {
        recyclerView = findViewById(R.id.recycler_view);
        emptyView = findViewById(R.id.empty_view);
        empty_tv = findViewById(R.id.empty_textView);
        empty_tv.setText("你还没有拉黑任何人");
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(0.5f)));
        requestData();
        blackListAdapter = new BlackListAdapter();
        recyclerView.setAdapter(blackListAdapter);
        blackListAdapter.setDeleteListener(new BlackListAdapter.DeleteBlackUserListener() {
            @Override
            public void deleteItem(int position) {
                BlackListActiviity.this.position = position;
                Object object = blackListAdapter.getItem(position);
                if (object instanceof BlackUser) {
                    final BlackUser blackUser = (BlackUser) object;
                    final long blackId = blackUser.getBlackId();
                    if (blackUser != null) {
                        RongIM.getInstance().removeFromBlacklist(String.valueOf(blackUser.getToUserId()), new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                if (userPresenter != null && !isFinishing()) {
                                    userPresenter.removeBlackList(blackId);
                                }
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                                Logger.d("errorCode.getValue() == " + errorCode.getValue() + " ====errorCode.getMessage()==== " + errorCode.getMessage());
                            }
                        });
                    }
                }

            }
        });
        // 刷新框架
        TwinklingRefreshLayout refreshLayout = findViewById(R.id.refresh_layout_view);
        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(this);
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
    }

    /**
     * 请求数据
     */
    private void requestData() {
        userPresenter.getBlackList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber);
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETBLACKLIST:
                if (object instanceof GetBlackListResponse) {
                    GetBlackListResponse response = (GetBlackListResponse) object;
                    if (response.getCode() == 200) {
                        total = response.getTotal();
                        if (response.getTotal() > 0) {
                            emptyView.setVisibility(View.GONE);
                        } else {

                        }
                        if (pageNumber == 1) {
                            blackListAdapter.setList(response.getList());
                        } else {
                            blackListAdapter.updateList(response.getList());
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_REMOVEBLACKLIST:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {
                        if (blackListAdapter != null) {
                            blackListAdapter.deleteItem(position);
                            int size = blackListAdapter.getList().size();
                            if (size == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    Utils.showToastShortTime(response.getMsg());
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

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView(getResources().getString(R.string.title_black_list));
    }
}
