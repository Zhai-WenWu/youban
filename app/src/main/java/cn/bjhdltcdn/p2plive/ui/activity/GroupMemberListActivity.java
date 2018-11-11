package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetGroupUserListResponse;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.GroupMemberListAdapter;
import cn.bjhdltcdn.p2plive.model.GroupUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * 群成员列表
 */
public class GroupMemberListActivity extends BaseActivity implements BaseView {

    private RecyclerView recyclerView;
    private GroupMemberListAdapter adapter;

    private GroupPresenter presenter;

    private Group group;
    private int pageSize = 50;
    private int pageNumber = 1;
    private ToolBarFragment titleFragment;
    private int userRole = 3;
    private TwinklingRefreshLayout refreshLayout;
    private boolean needShowLoading = true;

    public GroupPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GroupPresenter(this);
        }
        return presenter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_list_layout);

        userRole = getIntent().getIntExtra(Constants.KEY.KEY_EXTRA_PARAM, 3);
        group = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);

        setTitle();

        initView();


        getPresenter().getGroupUserList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), group.getGroupId(), pageSize, pageNumber);


    }


    private void setTitle() {
        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("群成员");
    }


    private void initView() {

        recyclerView = findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), 5);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GroupMemberListAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                GroupUser groupUser = adapter.getList().get(position);
                if (groupUser != null) {// 跳转到用户详情页面
                    if (groupUser.getUserRole() > 0 && groupUser.getUserId() > 0) {
                        if (groupUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            startActivity(new Intent(GroupMemberListActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, groupUser.getBaseUser()));
                        }
                    } else if (groupUser.getUserRole() == adapter.getADD_ICON_ROLE()) {
                        Intent intent =  new Intent(GroupMemberListActivity.this, SelectAddressBookActivity.class);
                        intent.putExtra(Constants.KEY.KEY_OBJECT, group);
                        intent.putExtra(Constants.Fields.TYPE, 1);
                        startActivity(intent);
                    } else if (groupUser.getUserRole() == adapter.getDEL_ICON_ROLE()) {
                        Intent intent = new Intent(GroupMemberListActivity.this, AddGroupAdministratorsActivity.class);
                        intent.putExtra(Constants.Fields.GROUP_ID, group.getGroupId());
                        intent.putExtra(Constants.Fields.TYPE, 2);
                        startActivity(intent);
                    }
                }
            }
        });

//        // 删除事件
//        adapter.setViewClickListener(new GroupMemberListAdapter.ViewClickListener() {
//            @Override
//            public void onClick(int position) {
//                GroupUser groupUser = adapter.getList().get(position);
//                if (groupUser != null && groupUser.getBaseUser() != null) {
//                    List<Long> outUserIds = new ArrayList<Long>(1);
//                    long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                    outUserIds.add(groupUser.getBaseUser().getUserId());
//                    getPresenter().signOutGroup(group.getGroupId(), userId, outUserIds);
//
//                    adapter.getList().remove(position);
//                    adapter.notifyDataSetChanged();
//
//                }
//
//            }
//        });


        // 刷新框架
        refreshLayout = findViewById(R.id.refresh_layout_view);
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
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
                refreshLayout.setEnableLoadmore(true);
                pageNumber = 1;
                getPresenter().getGroupUserList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), group.getGroupId(), pageSize, pageNumber);

            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();
                getPresenter().getGroupUserList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), group.getGroupId(), pageSize, pageNumber);

            }
        });

    }


    @Override
    public void updateView(String apiName, Object object) {

        if (isFinishing()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            if (e instanceof SocketTimeoutException) {
                Utils.showToastShortTime("网络连接超时");
                return;
            }

            Utils.showToastShortTime(e.getMessage());
            return;
        }

        if (InterfaceUrl.URL_GETGROUPUSERLIST.equals(apiName)) {

            if (object instanceof GetGroupUserListResponse) {

                GetGroupUserListResponse response = (GetGroupUserListResponse) object;

                if (response.getCode() == 200) {

                    // 设置群成员
                    if (pageNumber == 1) {
                        adapter.setList(response.getGroupUserList());
                        titleFragment.setTitleView("群成员(" + response.getTotal() + ")");
                        if (userRole == 1 || userRole == 2) {
                            adapter.setDeleteItem();
                        }
                    } else {
                        adapter.addList(response.getGroupUserList());
                    }

                    if (response.getTotal() > adapter.getItemCount() - 1) {// 减去最后item
                        pageNumber++;
                    } else {
                        refreshLayout.setEnableLoadmore(false);
                        Utils.showToastShortTime("已经全部加载完毕");
                    }


                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }

        } else if (InterfaceUrl.URL_SIGNOUTGROUP.equals(apiName)) {
            if (object instanceof BaseResponse) {

                BaseResponse response = (BaseResponse) object;
                Utils.showToastShortTime(response.getMsg());

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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.onDestroy();
        }
        presenter = null;

    }
}
