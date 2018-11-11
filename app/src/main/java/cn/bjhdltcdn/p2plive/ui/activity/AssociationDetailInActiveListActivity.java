//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.UpdateActiveListEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindUserOrganStatusResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetPostAndActivityListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSchoolmateOrganListResponse;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.ReportType;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationDetailInActiveListAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationDetailListAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.SelectorReportContentDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 圈子详情的活动列表页面
// */
//public class AssociationDetailInActiveListActivity extends BaseActivity implements BaseView {
//
//
//    private ListView listView;
//
//    private AssociationDetailInActiveListAdapter adapter;
//
//    private TwinklingRefreshLayout refreshLayout;
//    private PostCommentListPresenter postCommentListPresenter;
//    private long userId;
//
//    private long organId;
//    private int pageSize = 20;
//    private int pageNumber = 1;
//    private View emptyView;
//
//    private OrganizationInfo organizationInfo;
//    private int itemPosition;
//    private int itemMessageType;
//    private AssociationPresenter associationPresenter;
//    private long reportParentId;
//    private int reportType;
//    private PostCommentListPresenter postPresenter;
//    private CommonPresenter commonPresenter;
//    private UserPresenter userPresenter;
//
//    /**
//     * 是否可以踢出圈子(0不显示(否),1可以)
//     */
//    private int isKickedOut;
//
//    /**
//     * 是否禁言(0不显示,1解除禁言,2禁言)
//     */
//    private int isGag;
//
//    public PostCommentListPresenter getPostCommentListPresenter() {
//        if (postCommentListPresenter == null) {
//            postCommentListPresenter = new PostCommentListPresenter(this);
//        }
//        return postCommentListPresenter;
//    }
//
//
//    public AssociationPresenter getAssociationPresenter() {
//        if (associationPresenter == null) {
//            associationPresenter = new AssociationPresenter(this);
//        }
//        return associationPresenter;
//    }
//
//
//    public PostCommentListPresenter getPostPresenter() {
//
//        if (postPresenter == null) {
//            postPresenter = new PostCommentListPresenter(this);
//        }
//        return postPresenter;
//    }
//
//    public CommonPresenter getCommonPresenter() {
//        if (commonPresenter == null) {
//            commonPresenter = new CommonPresenter(this);
//        }
//        return commonPresenter;
//    }
//
//    public UserPresenter getUserPresenter() {
//        if (userPresenter == null) {
//            userPresenter = new UserPresenter(this);
//        }
//        return userPresenter;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_association_detail_in_active_list_layout);
//
//        EventBus.getDefault().register(this);
//
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        organizationInfo = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);
//
//        if (organizationInfo != null) {
//            organId = organizationInfo.getOrganId();
//        }
//
//        isGag = getIntent().getIntExtra(Constants.Fields.IS_GAG, 0);
//
//        setTitle();
//
//        initView();
//
//
//    }
//
//    private void setTitle() {
//        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("活动");
//    }
//
//
//    private void initView() {
//
//        if (emptyView == null) {
//            emptyView = findViewById(R.id.empty_view_layout);
//        }
//
//        listView = findViewById(R.id.list_view);
//
//        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        LoadingView loadingView = new LoadingView(App.getInstance());
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(listView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//
//                refreshLayout.finishRefreshing();
//                pageNumber = 1;
//                if (organizationInfo.getType() != 2) {
//                    getPostCommentListPresenter().getPostAndActivityList(userId, organId, 3, pageSize, pageNumber, false);
//
//                } else {
//
//                    getAssociationPresenter().getSchoolmateOrganList(userId, organId, 3, pageSize, pageNumber, false);
//
//                }
//
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//
//                refreshLayout.finishLoadmore();
//                if (organizationInfo.getType() != 2) {
//                    getPostCommentListPresenter().getPostAndActivityList(userId, organId, 3, pageSize, pageNumber, false);
//
//                } else {
//
//                    getAssociationPresenter().getSchoolmateOrganList(userId, organId, 3, pageSize, pageNumber, false);
//
//                }
//            }
//
//        });
//
//        adapter = new AssociationDetailInActiveListAdapter(this);
//        listView.setAdapter(adapter);
//
//        adapter.setItemWidgetOnClick(new AssociationDetailListAdapter.ItemWidgetOnClick() {
//            @Override
//            public void onPraise(long postId, int type, int position) {
//
//
//            }
//
//            @Override
//            public void onOrgain(long orgainId, int position) {
//
//            }
//
//            @Override
//            public void moreImg(int itemType, int position) {
//
//                itemPosition = position;
//                itemMessageType = itemType;
//
//                final HomeInfo homeInfo = adapter.getList().get(position);
//                switch (homeInfo.getType()) {//1帖子,2圈子,3活动,4房间,5PK挑战
//                    case 3:
//                        ActivityInfo activityInfo = homeInfo.getActivityInfo();
//                        // 圈子或管理员,检查是否有禁言和踢出圈子
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && activityInfo.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            getAssociationPresenter().findUserOrganStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), activityInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                            return;
//
//                        }
//                        showPostOperationFragmentDialog(itemType, position, "", "");
//
//                        break;
//
//                }
//
//
//            }
//
//            @Override
//            public void onApplyClerk(long storeId) {
//
//            }
//        });
//
//        if (organizationInfo.getType() != 2) {
//            getPostCommentListPresenter().getPostAndActivityList(userId, organId, 3, pageSize, pageNumber, true);
//
//        } else {
//
//            getAssociationPresenter().getSchoolmateOrganList(userId, organId, 3, pageSize, pageNumber, true);
//
//        }
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(UpdateActiveListEvent event) {
//        if (event == null) {
//            return;
//        }
//
//        if (event.getType() == 2) {
//            if (event.isDelect()) {
//                //本地删除
//                if (adapter.getCount() > event.getPosition()) {
//                    adapter.getList().remove(event.getPosition());
//                    adapter.notifyDataSetChanged();
//                }
//
//                // 空数据显示状态
//                if ((adapter.getCount()) < 1) {
//                    emptyView.setVisibility(View.VISIBLE);
//                } else {
//                    emptyView.setVisibility(View.GONE);
//                }
//
//            }
//        }
//
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
//            Utils.showToastShortTime(e.getMessage());
//            return;
//        }
//
//        if (InterfaceUrl.URL_FINDUSERORGANSTATUS.equals(apiName)) {
//            if (object instanceof FindUserOrganStatusResponse) {
//                FindUserOrganStatusResponse response = (FindUserOrganStatusResponse) object;
//                if (response.getIsKickedOut() == 1) {
//
//                    isKickedOut = response.getIsKickedOut();
//                    isGag = response.getIsGag();
//
//                    if (itemMessageType > 0 && itemPosition > -1 && isKickedOut == 1) {
//                        showPostOperationFragmentDialog(itemMessageType, itemPosition, "将Ta踢出圈子", isGag == 1 ? "对Ta禁言" : "对Ta解除禁言");
//                    }
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_GETPOSTANDACTIVITYLIST.equals(apiName)) {
//
//            if (object instanceof GetPostAndActivityListResponse) {
//
//                GetPostAndActivityListResponse response = (GetPostAndActivityListResponse) object;
//
//                if (response.getCode() == 200) {
//
//                    if (pageNumber == 1) {
//                        adapter.setList(response.getList());
//                    } else {
//                        adapter.setListAll(response.getList());
//                    }
//
//                    if (adapter.getCount() < response.getTotal()) {
//                        pageNumber++;
//                        refreshLayout.setEnableLoadmore(true);
//                    } else {
//                        refreshLayout.setEnableLoadmore(false);
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//                // 空数据显示状态
//                if ((adapter.getCount()) < 1) {
//                    emptyView.setVisibility(View.VISIBLE);
//                    if (!StringUtils.isEmpty(response.getBlankHint())) {
//                        TextView emptyTextView = emptyView.findViewById(R.id.empty_textView);
//                        emptyTextView.setText(!StringUtils.isEmail(response.getBlankHint()) ? response.getBlankHint() : "我的心里空空嘚");
//                    }
//
//                } else {
//                    emptyView.setVisibility(View.GONE);
//                }
//
//
//            }
//
//
//        } else if (InterfaceUrl.URL_FINDUSERORGANSTATUS.equals(apiName)) {
//            if (object instanceof FindUserOrganStatusResponse) {
//                FindUserOrganStatusResponse response = (FindUserOrganStatusResponse) object;
//                if (response.getIsKickedOut() == 1) {
//
//                    isKickedOut = response.getIsKickedOut();
//                    isGag = response.getIsGag();
//
//                    if (itemMessageType > 0 && itemPosition > -1 && isKickedOut == 1) {
//                        showPostOperationFragmentDialog(itemMessageType, itemPosition, "将Ta踢出圈子", isGag == 1 ? "对Ta禁言" : "对Ta解除禁言");
//                    }
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_ORGANBANNINGCOMMENTS.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//
//                isKickedOut = -1;
//                isGag = -1;
//
//            }
//        } else if (InterfaceUrl.URL_DELETEORGANCONTENT.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//
//                if (response.getCode() == 200) {
//
//                    // 删除圈子内容
//                    adapter.deleteItem(itemPosition);
//                    itemPosition = -1;
//
//                    // 空数据显示状态
//                    if ((adapter.getCount()) < 1) {
//
//                        emptyView.setVisibility(View.VISIBLE);
//                        if (!StringUtils.isEmpty(response.getBlankHint())) {
//                            TextView emptyTextView = emptyView.findViewById(R.id.empty_textView);
//                            emptyTextView.setText(response.getBlankHint());
//                        }
//
//                    } else {
//                        emptyView.setVisibility(View.GONE);
//                    }
//
//
//                }
//
//            }
//        } else if (InterfaceUrl.URL_UPDATEORGANUSERSTATUS.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//
//            }
//        } else if (InterfaceUrl.URL_POSTTOP.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//                if (response.getCode() == 200) {
//
//                    pageNumber = 1;
//                    getPostCommentListPresenter().getPostAndActivityList(userId, organId, 3, pageSize, pageNumber, true);
//
//                    itemPosition = -1;
//
//                }
//            }
//        } else if (InterfaceUrl.URL_REPORTOPERATION.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                if (response.getCode() == 200) {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_GETSCHOOLMATEORGANLIST.equals(apiName)) {
//            if (object instanceof GetSchoolmateOrganListResponse) {
//                GetSchoolmateOrganListResponse response = (GetSchoolmateOrganListResponse) object;
//                if (response.getCode() == 200) {
//
//                    if (pageNumber == 1) {
//
//                        adapter.setList(response.getList());
//
//                    } else {
//                        adapter.setListAll(response.getList());
//                    }
//
//                    if (adapter.getCount() < response.getTotal()) {
//                        pageNumber++;
//                        refreshLayout.setEnableLoadmore(true);
//                    } else {
//                        refreshLayout.setEnableLoadmore(false);
//                    }
//
//
//                    // 空数据显示状态
//                    if ((adapter.getCount()) < 1) {
//
//                        emptyView.setVisibility(View.VISIBLE);
//                        if (!StringUtils.isEmpty(response.getBlankHint())) {
//                            TextView emptyTextView = emptyView.findViewById(R.id.empty_textView);
//                            emptyTextView.setText(response.getBlankHint());
//                        }
//
//                    } else {
//                        emptyView.setVisibility(View.GONE);
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        }
//
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
//
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//
//    }
//
//    /**
//     * 显示操作框
//     *
//     * @param itemType
//     * @param position
//     */
//    private void showPostOperationFragmentDialog(int itemType, int position, final String title1, final String title2) {
//
//        final HomeInfo homeInfo = adapter.getList().get(position);
//        final PostOperationFragmentDialog dialog = new PostOperationFragmentDialog();
//
//        switch (itemType) {//类型((1帖子,2圈子,3活动,4房间,5PK挑战))
//
//
//            case 3:// 活动
//                final ActivityInfo activityInfo = homeInfo.getActivityInfo();
//                if (activityInfo == null) {
//                    return;
//                }
//
//
//                //用户角色：圈主或者管理员是自己
//                if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && activityInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    dialog.setTextList("从本圈子删除", "置顶/取消置顶", "分享", "取消");
//                } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//                    if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                        dialog.setTextList(title1, title2, "举报", "删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                    } else {
//                        dialog.setTextList("举报", "从本圈子删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                    }
//
//
//                } else if (activityInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //自己的活动
//                    dialog.setTextList("从本圈子删除", "分享", "取消");
//                } else {//别人的活动
//                    dialog.setTextList("举报", "分享", "拉黑", "取消");
//                }
//
//                reportParentId = activityInfo.getActivityId();
//                reportType = 9;
//
//                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                    @Override
//                    public void onClick(int type) {
//
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && activityInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//
//                            switch (type) {
//
//                                case 1://删除 活动
//
//                                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                    dialog.setText("", "确定删除活动？", "取消", "确定");
//                                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                        @Override
//                                        public void onLeftClick() {
//                                            //取消
//                                        }
//
//                                        @Override
//                                        public void onRightClick() {
//
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), activityInfo.getActivityId(), 3);
//
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//                                case 2://置顶/取消置顶 活动
//                                    //是否置顶(0取消,1置顶)
//                                    getPostPresenter().postTop(organId, activityInfo.getActivityId(), 3, activityInfo.getIsTop() == 0 ? 1 : 2);
//
//                                    break;
//
//                                case 3://分享 活动
//
//                                    String imgUrl = "";
//
//                                    if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                        imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                    }
//                                    ShareUtil.getInstance().showShare(AssociationDetailInActiveListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                            }
//
//                        } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//                            if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                                switch (type) {
//
//                                    case 1:// 提出圈子
//
//                                        ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                                        dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。您可到“圈子资料-圈子黑名单”中解除该限制。", "取消", "踢出");
//                                        dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//                                                getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), activityInfo.getBaseUser().getUserId(), 2);
//                                            }
//                                        });
//                                        dialog1.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 2:// 禁言/解禁
//
//                                        ActiveLaunchSuccessTipDialog dialog2 = new ActiveLaunchSuccessTipDialog();
//                                        if (isGag == 1) {
//                                            dialog2.setText("", "确定对Ta禁言，禁止Ta发帖和发布评论。您可到圈子成员列表中解除禁言。", "取消", "禁言");
//                                        } else {
//                                            dialog2.setText("", "确定解除禁言？恢复Ta的发帖和发布评论功能。", "取消", "解禁");
//                                        }
//                                        dialog2.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//                                                getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), activityInfo.getBaseUser().getUserId());
//                                            }
//                                        });
//                                        dialog2.show(getSupportFragmentManager());
//
//
//                                        break;
//
//                                    case 3://举报活动
//
//                                        SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                        selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                            @Override
//                                            public void reportItemClick(Object object) {
//                                                if (object instanceof ReportType) {
//
//                                                    ReportType reportTypeObj = (ReportType) object;
//                                                    getCommonPresenter().reportOperation(reportParentId, reportType
//                                                            , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
//                                                            , 0
//                                                            , reportTypeObj.getReportTypeId());
//                                                }
//                                            }
//                                        });
//                                        selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 4://删除活动
//
//                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                        dialog.setText("", "确定删除活动？", "取消", "确定");
//                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), activityInfo.getActivityId(), 3);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//
//                                        break;
//
//                                    case 5://置顶/取消置顶 活动
//                                        //是否置顶(0取消,1置顶)
//                                        getPostPresenter().postTop(organId, activityInfo.getActivityId(), 3, activityInfo.getIsTop() == 0 ? 1 : 2);
//
//                                        break;
//
//                                    case 6://分享 活动
//
//                                        String imgUrl = "";
//
//                                        if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                            imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailInActiveListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//                                        break;
//
//                                    case 7://拉黑 发活动的人
//
//                                        BaseUser baseUser = activityInfo.getBaseUser();
//                                        if (baseUser != null) {
//                                            long toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//
//                                        break;
//
//                                }
//                            } else {
//
//                                switch (type) {
//
//                                    case 1://举报活动
//
//                                        SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                        selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                            @Override
//                                            public void reportItemClick(Object object) {
//                                                if (object instanceof ReportType) {
//
//                                                    ReportType reportTypeObj = (ReportType) object;
//                                                    getCommonPresenter().reportOperation(reportParentId, reportType
//                                                            , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
//                                                            , 0
//                                                            , reportTypeObj.getReportTypeId());
//                                                }
//                                            }
//                                        });
//                                        selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 2://删除活动
//
//                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                        dialog.setText("", "确定删除活动？", "取消", "确定");
//                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), activityInfo.getActivityId(), 3);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//
//                                        break;
//
//                                    case 3://置顶/取消置顶 活动
//                                        //是否置顶(0取消,1置顶)
//                                        getPostPresenter().postTop(organId, activityInfo.getActivityId(), 3, activityInfo.getIsTop() == 0 ? 1 : 2);
//
//                                        break;
//
//                                    case 4://分享 活动
//
//                                        String imgUrl = "";
//
//                                        if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                            imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailInActiveListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//                                        break;
//
//                                    case 5://拉黑 发活动的人
//
//                                        BaseUser baseUser = activityInfo.getBaseUser();
//                                        if (baseUser != null) {
//                                            long toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//
//                                        break;
//
//                                }
//
//                            }
//
//
//                        } else if (activityInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {//自己发布的活动
//
//                            switch (type) {
//
//                                case 1://删除 活动
//
//                                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                    dialog.setText("", "确定删除活动？", "取消", "确定");
//                                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                        @Override
//                                        public void onLeftClick() {
//                                            //取消
//                                        }
//
//                                        @Override
//                                        public void onRightClick() {
//
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), activityInfo.getActivityId(), 3);
//
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//                                case 2://分享 活动
//
//                                    String imgUrl = "";
//
//                                    if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                        imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                    }
//                                    ShareUtil.getInstance().showShare(AssociationDetailInActiveListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                            }
//
//                        } else {//别人的活动
//                            switch (type) {
//
//                                case 1://举报 活动
//                                    SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                    selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                        @Override
//                                        public void reportItemClick(Object object) {
//                                            if (object instanceof ReportType) {
//
//                                                ReportType reportTypeObj = (ReportType) object;
//                                                getCommonPresenter().reportOperation(reportParentId, reportType
//                                                        , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
//                                                        , 0
//                                                        , reportTypeObj.getReportTypeId());
//                                            }
//                                        }
//                                    });
//                                    selectorReportContentDialog.show(getSupportFragmentManager());
//                                    break;
//
//                                case 2://分享 活动
//
//                                    String imgUrl = "";
//
//                                    if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                        imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                    }
//                                    ShareUtil.getInstance().showShare(AssociationDetailInActiveListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                                case 3://拉黑 发活动的人
//
//                                    BaseUser baseUser = activityInfo.getBaseUser();
//                                    if (baseUser != null) {
//                                        long toBlacklistUserId = baseUser.getUserId();
//                                        getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                    }
//                                    break;
//
//                            }
//                        }
//
//                    }
//                });
//
//
//                break;
//
//
//        }
//
//        dialog.show(getSupportFragmentManager());
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        EventBus.getDefault().unregister(this);
//
//        if (listView != null) {
//            listView = null;
//        }
//
//        if (adapter != null) {
//            if (adapter.getList() != null) {
//                adapter.getList().clear();
//            }
//            adapter = null;
//        }
//
//        if (refreshLayout != null) {
//            refreshLayout.removeAllViews();
//        }
//        refreshLayout = null;
//
//        if (postCommentListPresenter != null) {
//            postCommentListPresenter.onDestroy();
//        }
//        postCommentListPresenter = null;
//
//
//        if (emptyView != null) {
//            emptyView = null;
//        }
//
//        if (organizationInfo != null) {
//            organizationInfo = null;
//        }
//
//        if (associationPresenter != null) {
//            associationPresenter.onDestroy();
//        }
//        associationPresenter = null;
//
//        if (postPresenter != null) {
//            postPresenter.onDestroy();
//        }
//        postPresenter = null;
//
//        if (commonPresenter != null) {
//            commonPresenter.onDestroy();
//        }
//        commonPresenter = null;
//
//        if (userPresenter != null) {
//            userPresenter.onDestroy();
//        }
//        userPresenter = null;
//
//
//    }
//
//
//}
