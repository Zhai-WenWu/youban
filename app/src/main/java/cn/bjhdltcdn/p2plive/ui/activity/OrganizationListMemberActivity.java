//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//
//import java.net.SocketTimeoutException;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindOrganizationMemberResponse;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.OrganMember;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.OrganizationListMemberAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 圈子成员
// */
//public class OrganizationListMemberActivity extends BaseActivity implements BaseView {
//
//    private ToolBarFragment titleFragment;
//    private RecyclerView recyclerView;
//    private OrganizationListMemberAdapter adapter;
//
//    private OrganizationInfo organizationInfo;
//
//    private AssociationPresenter presenter;
//
//
//    private int pageSiz = 20;
//    private int pageNumber = 1;
//
//    private int selecteIndex = -1;
//
//    /**
//     * 是否禁言(0不显示,1解除禁言,2禁言)
//     */
//    private int isGag;
//    private TwinklingRefreshLayout refreshLayout;
//
//    public AssociationPresenter getPresenter() {
//
//        if (presenter == null) {
//            presenter = new AssociationPresenter(this);
//        }
//        return presenter;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_organization_list_member_layout);
//
//        organizationInfo = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);
//
//        setTitle();
//
//        initView();
//
//        // 获取圈子成员
//        getPresenter().findOrganizationMember(organizationInfo.getOrganId(), pageSiz, pageNumber);
//
//
//    }
//
//
//    private void setTitle() {
//        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("圈子成员");
//
//    }
//
//    private void initView() {
//
//        recyclerView = findViewById(R.id.recycle_view);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
//        layoutManager.setAutoMeasureEnabled(true);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1)));
//        adapter = new OrganizationListMemberAdapter(null, organizationInfo.getUserRole());
//        recyclerView.setAdapter(adapter);
//        adapter.setViewOnClick(new OrganizationListMemberAdapter.ViewOnClick() {
//            @Override
//            public void onClick(int position) {
//                selecteIndex = position;
//                OrganMember organMember = adapter.getList().get(position);
//                // tyep 类型(1设置管理员,2取消管理员)
//                getPresenter().setOrganManager(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), organMember.getUserRole() == 3 ? 1 : 2, organMember.getMemberId());
//            }
//        });
//
//        adapter.setUserViewOnClick(new OrganizationListMemberAdapter.UserViewOnClick() {
//            @Override
//            public void onClick(BaseUser baseUser) {
//                startActivity(new Intent(OrganizationListMemberActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//            }
//        });
//
//        adapter.setOutAndGagViewOnClick(new OrganizationListMemberAdapter.OutAndGagViewOnClick() {
//            @Override
//            public void onClick(int position, final int itemType, String title) {
//                selecteIndex = position;
//                isGag = itemType;
//                final OrganMember organMember = adapter.getList().get(position);
//                if (itemType == 0) {
//                    ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                    dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。您可到“圈子资料-圈子黑名单”中解除该限制。", "取消", "踢出");
//                    dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                        @Override
//                        public void onLeftClick() {
//                            //取消
//                        }
//
//                        @Override
//                        public void onRightClick() {
//                            getPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organMember.getBaseUser().getUserId(), 2);
//                        }
//                    });
//                    dialog1.show(getSupportFragmentManager());
//
//
//                } else {
//
//                    ActiveLaunchSuccessTipDialog dialog2 = new ActiveLaunchSuccessTipDialog();
//                    if (itemType == 1) {
//                        dialog2.setText("", "确定解除禁言？恢复Ta的发帖和发布评论功能。", "取消", "解禁");
//                    } else {
//                        dialog2.setText("", "确定对Ta禁言，禁止Ta发帖和发布评论。您可到圈子成员列表中解除禁言。", "取消", "禁言");
//                    }
//                    dialog2.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                        @Override
//                        public void onLeftClick() {
//                            //取消
//                        }
//
//                        @Override
//                        public void onRightClick() {
//                            getPresenter().organBanningComments(organizationInfo.getOrganId(), itemType, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organMember.getBaseUser().getUserId());
//                        }
//                    });
//                    dialog2.show(getSupportFragmentManager());
//
//
//                }
//            }
//        });
//
//        refreshLayout = findViewById(R.id.refresh_layout_view);
//        // 头部加载样式
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        // 底部加载样式
//        LoadingView loadingView = new LoadingView(this);
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNumber = 1;
//                refreshLayout.finishRefreshing();
//                // 获取圈子成员
//                getPresenter().findOrganizationMember(organizationInfo.getOrganId(), pageSiz, pageNumber);
//
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                refreshLayout.finishLoadmore();
//                // 获取圈子成员
//                getPresenter().findOrganizationMember(organizationInfo.getOrganId(), pageSiz, pageNumber);
//
//            }
//        });
//
//
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (isFinishing()) {
//            return;
//        }
//
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            if (e instanceof SocketTimeoutException) {
//                Utils.showToastShortTime("网络连接超时");
//                return;
//            }
//            Utils.showToastShortTime(e.getMessage());
//            return;
//        }
//
//        if (InterfaceUrl.URL_FINDORGANIZATIONMEMBER.equals(apiName)) {
//            if (object instanceof FindOrganizationMemberResponse) {
//                FindOrganizationMemberResponse response = (FindOrganizationMemberResponse) object;
//
//                if (response.getCode() == 200) {
//
//                    if (pageNumber == 1) {
//                        adapter.setList(response.getMemberList());
//                    } else {
//                        adapter.addList(response.getMemberList());
//                    }
//
//                    if (response.getTotal() > adapter.getItemCount()) {
//                        pageNumber++;
//                        refreshLayout.setEnableLoadmore(true);
//                    }else {
//                        refreshLayout.setEnableLoadmore(false);
//                    }
//
//                }
//            }
//        } else if (InterfaceUrl.URL_SETORGANMANAGER.equals(apiName)) {
//
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                if (response.getCode() == 200) {
//
//                    if (selecteIndex > -1) {
//                        OrganMember organMember = adapter.getList().get(selecteIndex);
//                        organMember.setUserRole(organMember.getUserRole() == 2 ? 3 : 2);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                }
//            }
//
//            selecteIndex = -1;
//
//        } else if (InterfaceUrl.URL_ORGANBANNINGCOMMENTS.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//
//                if (selecteIndex > -1 && isGag > 0) {
//                    OrganMember organMember = adapter.getList().get(selecteIndex);
//                    organMember.setIsGag(isGag);
//                    adapter.notifyDataSetChanged();
//                }
//
//                selecteIndex = -1;
//                isGag = -1;
//
//            }
//        } else if (InterfaceUrl.URL_UPDATEORGANUSERSTATUS.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//
//                if (selecteIndex > -1) {
//                    adapter.getList().remove(selecteIndex);
//                    adapter.notifyDataSetChanged();
//                }
//
//                selecteIndex = -1;
//
//            }
//        }
//
//    }
//
//    @Override
//    public void showLoading() {
//
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//}
