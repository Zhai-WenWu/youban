package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.tubb.smrv.SwipeMenuRecyclerView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAnonymityFriendListResponse;
import cn.bjhdltcdn.p2plive.model.AnonymousUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.AnonymousListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;


/**
 * @author zhudi
 */
public class AnonymousFriendsListActivity extends BaseActivity implements BaseView {
    private SwipeMenuRecyclerView recyclerView;
    private View emptyView;
    private TextView empty_tv;
    private AnonymousListAdapter anonymousListAdapter;
    private int pageNumber = 1;
    private int pageSize = 100;
    // 操作当前item
    private int position = -1;
    private UserPresenter userPresenter;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_list);
        setTitle();
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_view);
        emptyView = findViewById(R.id.empty_view);
        empty_tv = findViewById(R.id.empty_textView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(0.5f)));
        requestData();
        anonymousListAdapter = new AnonymousListAdapter();
        recyclerView.setAdapter(anonymousListAdapter);
        anonymousListAdapter.setDeleteListener(new AnonymousListAdapter.DeleteAnonymousUserListener() {
            @Override
            public void deleteItem(final int position) {
                final ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                dialog.setText("", "是否删除该好友", "取消", "确定");
                dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {
                        AnonymousFriendsListActivity.this.position = position;
                        AnonymousUser anonymousUser = anonymousListAdapter.getItem(position);
                        long userId = anonymousUser.getUserId();
                        getUserPresenter().deleteAnonymityUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), userId);
                    }
                });
                dialog.show(getSupportFragmentManager());

            }
        });
        anonymousListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                AnonymousUser anonymousUser = anonymousListAdapter.getItem(position);
                long userId = anonymousUser.getUserId();
                getUserPresenter().getAnonymityUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), userId);

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
        refreshLayout.setEnableRefresh(false);//灵活的设置是否禁用上下拉。
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
        getUserPresenter().getAnonymityFriendList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber);
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETANONYMITYFRIENDLIST:
                if (object instanceof GetAnonymityFriendListResponse) {
                    GetAnonymityFriendListResponse response = (GetAnonymityFriendListResponse) object;
                    if (response.getCode() == 200) {
                        empty_tv.setText(response.getBlankHint());
                        if (response.getTotal() <= 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        }
                        if (pageNumber == 1) {
                            anonymousListAdapter.setList(response.getList());
                        } else {
                            anonymousListAdapter.updateList(response.getList());
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_DELETEANONYMITYUSER:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {
                        if (anonymousListAdapter != null) {
                            anonymousListAdapter.deleteItem(position);
                            int size = anonymousListAdapter.getList().size();
                            if (size == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    Utils.showToastShortTime(response.getMsg());
                }
                break;
            default:
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
        titleFragment.setTitleView("匿名好友");
    }
}
