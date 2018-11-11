//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindUserOrganStatusResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetPostAndActivityListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSchoolmateOrganListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.HelpPraiseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SayLovePraiseResponse;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.PlayInfo;
//import cn.bjhdltcdn.p2plive.model.PostInfo;
//import cn.bjhdltcdn.p2plive.model.ReportType;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.model.StoreDetail;
//import cn.bjhdltcdn.p2plive.model.StoreInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ClassMateHelpPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationDetailListAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationDetailPKVideoListAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.SelectorReportContentDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 圈子详情的PK视频列表页面
// */
//public class AssociationDetailPKVideoListActivity extends BaseActivity implements BaseView {
//
//    private ListView listView;
//
//    private AssociationDetailPKVideoListAdapter adapter;
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
//    private ClassMateHelpPresenter classMateHelpPresenter;
//    private long toBlacklistUserId;
//    private GetCommentListPresenter mCommentListPresenter;
//    private GetStoreListPresenter getStoreListPresenter;
//
//    public GetStoreListPresenter getStoreListPresenter() {
//        if (getStoreListPresenter == null) {
//            getStoreListPresenter = new GetStoreListPresenter(this);
//        }
//        return getStoreListPresenter;
//    }
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
//    public ClassMateHelpPresenter getClassMateHelpPresenter() {
//        if (classMateHelpPresenter == null) {
//            classMateHelpPresenter = new ClassMateHelpPresenter(this);
//        }
//        return classMateHelpPresenter;
//    }
//
//    public GetCommentListPresenter getCommentListPresenter() {
//        if (mCommentListPresenter == null) {
//            mCommentListPresenter = new GetCommentListPresenter(this);
//        }
//        return mCommentListPresenter;
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_association_detail_pkvideo_list_layout);
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
//
//    private void setTitle() {
//        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("原创视频");
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
//                    getPostCommentListPresenter().getPostAndActivityList(userId, organId, 2, pageSize, pageNumber, false);
//
//                } else {
//
//                    getAssociationPresenter().getSchoolmateOrganList(userId, organId, 2, pageSize, pageNumber, false);
//
//                }
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//
//                refreshLayout.finishLoadmore();
//
//                if (organizationInfo.getType() != 2) {
//                    getPostCommentListPresenter().getPostAndActivityList(userId, organId, 2, pageSize, pageNumber, false);
//
//                } else {
//
//                    getAssociationPresenter().getSchoolmateOrganList(userId, organId, 2, pageSize, pageNumber, false);
//
//                }
//
//            }
//
//        });
//
//        adapter = new AssociationDetailPKVideoListAdapter(null, this);
//        listView.setAdapter(adapter);
//
//        adapter.setItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Object object = adapter.getItem(position);
//                if (object instanceof HomeInfo) {
//                    HomeInfo homeInfo = (HomeInfo) object;
//                    switch (homeInfo.getType()) {//1帖子,2圈子,3活动,4房间,5PK挑战
//                        case 1:
//                            PostInfo postInfo = homeInfo.getPostInfo();
//                            if (postInfo != null) {
//                                startActivity(new Intent(AssociationDetailPKVideoListActivity.this, PostDetailActivity.class)
//                                        .putExtra(Constants.KEY.KEY_OBJECT, postInfo)
//                                        .putExtra(Constants.KEY.KEY_POSITION, position)
//                                        .putExtra(Constants.Fields.COME_IN_TYPE, 1));
//                            }
//                            break;
//
//                        case 8:
//                            SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                            if (sayLoveInfo != null) {
//                                //跳转到表白详情页
//                                Intent intent = new Intent(AssociationDetailPKVideoListActivity.this, SayLoveDetailActivity.class);
//                                intent.putExtra(Constants.KEY.KEY_SAYLOVE_ID, sayLoveInfo.getSayLoveId());
//                                intent.putExtra(Constants.Fields.POSITION, position);
//                                startActivity(intent);
//                            }
//
//                            break;
//
//                        case 9:
//
//                            HelpInfo helpInfo = homeInfo.getHelpInfo();
//                            Intent intent = new Intent(AssociationDetailPKVideoListActivity.this, ClassMateHelpDetailActivity.class);
//                            intent.putExtra(Constants.Fields.POSITION, position);
//                            intent.putExtra(Constants.KEY.KEY_OBJECT, helpInfo);
//                            startActivity(intent);
//
//                            break;
//                        case 5:
//
//                            PlayInfo playInfo = homeInfo.getPlayInfo();
//                            if (playInfo != null) {
//                                intent = new Intent(AssociationDetailPKVideoListActivity.this, VideoPlayFullScreenActivity.class);
//                                intent.putExtra(Constants.Fields.VIDEO_PATH, playInfo.getVideoUrl());
//                                intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, playInfo.getVideoImageUrl());
//                                startActivity(intent);
//
//                            }
//
//                            break;
//
//                    }
//
//                }
//            }
//        });
//
//        adapter.setItemWidgetOnClick(new AssociationDetailListAdapter.ItemWidgetOnClick() {
//            @Override
//            public void onPraise(long postId, int type, int position) {
//                itemPosition = position;
//
//                HomeInfo homeInfo = adapter.getList().get(position);
//                switch (homeInfo.getType()) {//1帖子,2圈子,3活动,4房间,5PK挑战
//
//                    case 1:
//                        getPostCommentListPresenter().postPraise(userId, postId, type);
//                        break;
//
//                    case 8:
//                        SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                        getCommentListPresenter().sayLovePraise(sayLoveInfo.getSayLoveId(), sayLoveInfo.getIsPraise() == 1 ? 2 : 1, userId);
//
//                        break;
//
//
//                    case 9:
//
//                        HelpInfo helpInfo = homeInfo.getHelpInfo();
//                        if (helpInfo != null) {
//                            getClassMateHelpPresenter().helpPraise(userId, helpInfo.getHelpId(), helpInfo.getIsPraise() == 1 ? 2 : 1);
//                        }
//
//                        break;
//
//                }
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
//
//                itemPosition = position;
//                itemMessageType = itemType;
//
//                final HomeInfo homeInfo = adapter.getList().get(position);
//                switch (homeInfo.getType()) {//1帖子,2圈子,3活动,4房间,5PK挑战
//                    case 1:
//                        PostInfo postInfo = homeInfo.getPostInfo();
//
//                        // 圈子或管理员,检查是否有禁言和踢出圈子
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && postInfo.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            getAssociationPresenter().findUserOrganStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), postInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                            return;
//
//                        }
//
//                        showPostOperationFragmentDialog(itemType, position, "", "");
//                        break;
//
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
//
//                    case 5:
//
//                        PlayInfo playInfo = homeInfo.getPlayInfo();
//
//                        // 圈子或管理员,检查是否有禁言和踢出圈子
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && playInfo.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            getAssociationPresenter().findUserOrganStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), playInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                            return;
//
//                        }
//
//                        showPostOperationFragmentDialog(itemType, position, "", "");
//                        break;
//
//                    case 8:
//
//                        SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                        // 圈子或管理员,检查是否有禁言和踢出圈子
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && sayLoveInfo.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            getAssociationPresenter().findUserOrganStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sayLoveInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                            return;
//
//                        }
//
//                        showPostOperationFragmentDialog(itemType, position, "", "");
//
//                        break;
//
//                    case 9:
//
//
//                        HelpInfo helpInfo = homeInfo.getHelpInfo();
//
//                        // 圈子或管理员,检查是否有禁言和踢出圈子
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && helpInfo.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            getAssociationPresenter().findUserOrganStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), helpInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                            return;
//
//                        }
//
//                        showPostOperationFragmentDialog(itemType, position, "", "");
//
//                        break;
//
//                }
//
//            }
//
//            @Override
//            public void onApplyClerk(long storeId) {
//                getStoreListPresenter().findStoreDetail(userId,storeId);
//            }
//        });
//
//
//        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object object = parent.getAdapter().getItem(position);
//                if (object instanceof HomeInfo) {
//                    HomeInfo homeInfo = (HomeInfo) object;
//                    PlayInfo playInfo = homeInfo.getPlayInfo();
//                    if (playInfo != null) {
//                        playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                        Intent intent = new Intent(AssociationDetailPKVideoListActivity.this, PKVideoPlayActivity.class);
//                        intent.putExtra(Constants.Fields.PK_ID, playInfo.getPlayId());
//                        startActivity(intent);
//                    }
//                }
//            }
//        });*/
//
//        pageNumber = 1;
//        if (organizationInfo.getType() != 2) {
//            getPostCommentListPresenter().getPostAndActivityList(userId, organId, 2, pageSize, pageNumber, true);
//
//        } else {
//
//            getAssociationPresenter().getSchoolmateOrganList(userId, organId, 2, pageSize, pageNumber, true);
//
//        }
//
//    }
//
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
//        if (InterfaceUrl.URL_GETPOSTANDACTIVITYLIST.equals(apiName)) {
//
//            if (object instanceof GetPostAndActivityListResponse) {
//
//                GetPostAndActivityListResponse response = (GetPostAndActivityListResponse) object;
//
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
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
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
//            }
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
//                    getPostCommentListPresenter().getPostAndActivityList(userId, organId, 2, pageSize, pageNumber, true);
//
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
//        } else if (InterfaceUrl.URL_POSTPRAISE.equals(apiName)) {
//            if (object instanceof PostPraiseResponse) {
//                PostPraiseResponse response = (PostPraiseResponse) object;
//
//                if (response.getCode() == 200) {
//                    HomeInfo homeInfo = adapter.getList().get(itemPosition);
//                    if (homeInfo != null) {
//                        homeInfo.getPostInfo().setIsPraise(response.getIsPraise());
//                        adapter.notifyDataSetChanged();
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//            }
//        } else if (InterfaceUrl.URL_HELPPRAISE.equals(apiName)) {
//            if (object instanceof HelpPraiseResponse) {
//                HelpPraiseResponse response = (HelpPraiseResponse) object;
//
//                if (response.getCode() == 200) {
//                    HomeInfo homeInfo = adapter.getList().get(itemPosition);
//                    if (homeInfo != null) {
//                        homeInfo.getHelpInfo().setIsPraise(response.getIsPraise());
//                        adapter.notifyDataSetChanged();
//                    }
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//            }
//        } else if (InterfaceUrl.URL_GETSCHOOLMATEORGANLIST.equals(apiName)) {
//            if (object instanceof GetSchoolmateOrganListResponse) {
//                GetSchoolmateOrganListResponse response = (GetSchoolmateOrganListResponse) object;
//                if (response.getCode() == 200) {
//
//                    if (response.getList() != null) {
//                        if (pageNumber == 1) {
//
//                            adapter.setList(response.getList());
//
//                        } else {
//                            adapter.setListAll(response.getList());
//                        }
//
//                        if (response.getList() != null && response.getList().size() > 0) {
//                            pageNumber++;
//                            refreshLayout.setEnableLoadmore(true);
//                        } else {
//                            refreshLayout.setEnableLoadmore(false);
//                        }
//
//                    }
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
//        } else if (InterfaceUrl.URL_DELETEPOST.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//                if (response.getCode() == 200) {
//
//                    adapter.deleteItem(itemPosition);
//                    itemPosition = -1;
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_SAYLOVEPRAISE)) {
//            if (object instanceof SayLovePraiseResponse) {
//                SayLovePraiseResponse response = (SayLovePraiseResponse) object;
//
//                if (response.getCode() == 200) {
//                    HomeInfo homeInfo = adapter.getList().get(itemPosition);
//                    if (homeInfo != null) {
//                        homeInfo.getSayLoveInfo().setIsPraise(response.getIsPraise());
//                        adapter.notifyDataSetChanged();
//                    }
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//            }
//        }else if (apiName.equals(InterfaceUrl.URL_FINDSTOREDETAIL)) {
//            if (object instanceof FindStoreDetailResponse) {
//                FindStoreDetailResponse findStoreDetailResponse = (FindStoreDetailResponse) object;
//                if (findStoreDetailResponse.getCode() == 200) {
//                    StoreDetail storeDetail = findStoreDetailResponse.getStoreDetail();
//                    if (storeDetail != null) {
//                        int isClert = storeDetail.getIsClert();//当前是否是店员(1店主,2店员,3普通用户,4店员申请中),
//                        StoreInfo storeInfo=storeDetail.getStoreInfo();
//                        int isPublish=storeInfo.getIsPublish();
//                        if(isPublish==1){
//                            if (isClert == 1) {
//                                //店长自己不跳转
//                            }else if (isClert == 2) {
//                                Utils.showToastShortTime("您已成为该店店员");
//                            } else if (isClert == 3) {
//                                //跳到店员申请界面
//                                Intent intent = new Intent(AssociationDetailPKVideoListActivity.this, ApplyClerkActivity.class);
//                                intent.putExtra(Constants.Fields.STORE_ID, storeInfo.getStoreId());
//                                startActivity(intent);
//                            } else if (isClert == 4) {
//                                //店员申请中
//                                Utils.showToastShortTime("店员申请中");
//                            }
//                        }else{
//                            Utils.showToastShortTime("该店已关闭招聘信息");
//                        }
//                    }
//                } else {
//                    Utils.showToastShortTime(findStoreDetailResponse.getMsg());
//                }
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
//
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//
//    }
//
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
//            case 1:
//                final PostInfo postInfo = homeInfo.getPostInfo();
//                if (postInfo == null) {
//                    return;
//                }
//
//                final BaseUser baseUser = postInfo.getBaseUser() != null ? postInfo.getBaseUser() : homeInfo.getBaseUser();
//
//                //用户角色：圈主或者管理员是自己
//                if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    dialog.setTextList("删除", "置顶/取消置顶", "分享", "取消");
//                } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                    if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                        dialog.setTextList(title1, title2, "举报", "删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                    } else {
//                        dialog.setTextList("举报", "删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                    }
//
//                } else if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //自己的帖子
//                    dialog.setTextList("删除", "分享", "取消");
//                } else {//别人的帖子
//                    dialog.setTextList("举报", "分享", "拉黑", "取消");
//                }
//
//                reportParentId = postInfo.getPostId();
//                reportType = 1;
//
//                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                    @Override
//                    public void onClick(int type) {
//
//                        //用户角色：圈主或者管理员是自己
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//
//                            switch (type) {
//
//                                case 1://删除 帖子
//
//                                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                    dialog.setText("", "确定删除帖子？", "取消", "确定");
//                                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                        @Override
//                                        public void onLeftClick() {
//                                            //取消
//                                        }
//
//                                        @Override
//                                        public void onRightClick() {
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), postInfo.getPostId(), 1);
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//                                case 2:
//
//                                    //是否置顶(0取消,1置顶)
//                                    getPostPresenter().postTop(organId, postInfo.getPostId(), 1, postInfo.getIsTop() == 0 ? 1 : 2);
//
//                                    break;
//
//                                case 3://分享 帖子
//
//                                    String imgUrl = "";
//
//                                    if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                        imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                    }
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//                                    break;
//
//                            }
//                        } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//
//                            if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//
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
//                                                getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId(), 2);
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
//                                                getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId());
//                                            }
//                                        });
//                                        dialog2.show(getSupportFragmentManager());
//
//
//                                        break;
//
//                                    case 3://举报帖子
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
//                                    case 4://删除帖子
//
//                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                        dialog.setText("", "确定删除帖子？", "取消", "确定");
//                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), postInfo.getPostId(), 1);
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 5://置顶/取消置顶 帖子
//                                        //是否置顶(0取消,1置顶)
//                                        getPostPresenter().postTop(organId, postInfo.getPostId(), 1, postInfo.getIsTop() == 0 ? 1 : 2);
//
//                                        break;
//
//                                    case 6://分享 帖子
//
//                                        String imgUrl = "";
//
//                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//
//                                        ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//                                        break;
//
//                                    case 7://拉黑 发帖子的人
//
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//
//
//                                        break;
//
//                                }
//
//                            } else {
//
//                                switch (type) {
//
//                                    case 1://举报帖子
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
//                                    case 2://删除帖子
//
//                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                        dialog.setText("", "确定删除帖子？", "取消", "确定");
//                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), postInfo.getPostId(), 1);
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 3://置顶/取消置顶 帖子
//                                        //是否置顶(0取消,1置顶)
//                                        getPostPresenter().postTop(organId, postInfo.getPostId(), 1, postInfo.getIsTop() == 0 ? 1 : 2);
//
//                                        break;
//
//                                    case 4://分享 帖子
//
//                                        String imgUrl = "";
//
//                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//
//                                        ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//                                        break;
//
//                                    case 5://拉黑 发帖子的人
//
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//
//
//                                        break;
//
//                                }
//
//                            }
//
//
//                        } else if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {//自己的帖子
//
//                            switch (type) {
//
//                                case 1://删除 帖子
//
//                                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                    dialog.setText("", "确定删除帖子？", "取消", "确定");
//                                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                        @Override
//                                        public void onLeftClick() {
//                                            //取消
//                                        }
//
//                                        @Override
//                                        public void onRightClick() {
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), postInfo.getPostId(), 1);
//
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//                                case 2://分享 帖子
//
//                                    String imgUrl = "";
//
//                                    if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                        imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                    }
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                            }
//
//                        } else {//别人的帖子
//                            switch (type) {
//
//                                case 1://举报 帖子
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
//                                case 2://分享 帖子
//
//                                    String imgUrl = "";
//
//                                    if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                        imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                    }
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                                case 3://拉黑 发帖子的人
//
//                                    if (baseUser != null) {
//                                        toBlacklistUserId = baseUser.getUserId();
//                                        getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                    }
//
//                                    break;
//
//                            }
//                        }
//
//                    }
//                });
//                break;
//
//            case 3:// 活动
//                final ActivityInfo activityInfo = homeInfo.getActivityInfo();
//                if (activityInfo == null) {
//                    return;
//                }
//
//                final BaseUser baseUser1 = activityInfo.getBaseUser() != null ? activityInfo.getBaseUser() : homeInfo.getBaseUser();
//
//                //用户角色：圈主或者管理员是自己
//                if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && baseUser1.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
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
//                } else if (baseUser1.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
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
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && baseUser1.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
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
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
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
//                                                getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser1.getUserId(), 2);
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
//                                                getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser1.getUserId());
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
//                                        ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//                                        break;
//
//                                    case 7://拉黑 发活动的人
//
//                                        if (baseUser1 != null) {
//                                            toBlacklistUserId = baseUser1.getUserId();
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
//                                        ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//                                        break;
//
//                                    case 5://拉黑 发活动的人
//
//                                        if (baseUser1 != null) {
//                                            toBlacklistUserId = baseUser1.getUserId();
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
//                        } else if (baseUser1.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {//自己发布的活动
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
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
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
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                                case 3://拉黑 发活动的人
//
//                                    if (baseUser1 != null) {
//                                        toBlacklistUserId = baseUser1.getUserId();
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
//            case 5:// PK
//                final PlayInfo playInfo = homeInfo.getPlayInfo();
//                if (playInfo == null) {
//                    return;
//                }
//
//                final BaseUser baseUser2 = playInfo.getBaseUser() != null ? playInfo.getBaseUser() : homeInfo.getBaseUser();
//
//                //用户角色：圈主或者管理员是自己
//                if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && baseUser2.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    dialog.setTextList("从本圈子删除", "置顶/取消置顶", "分享", "取消");
//                } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                    if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                        dialog.setTextList(title1, title2, "举报", "删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                    } else {
//                        dialog.setTextList("举报", "从本圈子删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//
//                    }
//                } else if (baseUser2.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //自己的PK
//                    dialog.setTextList("从本圈子删除", "分享", "取消");
//                } else {//别人的PK
//                    dialog.setTextList("举报", "分享", "拉黑", "取消");
//                }
//
//                reportParentId = playInfo.getPlayId();
//                reportType = 10;
//
//                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                    @Override
//                    public void onClick(int type) {
//
//                        //用户角色：圈主或者管理员是自己
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && baseUser2.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//
//                            switch (type) {
//
//                                case 1://删除 PK
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
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), playInfo.getPlayId(), 5);
//
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//                                case 2://置顶/取消置顶 活动
//                                    //是否置顶(0取消,1置顶)
//                                    getPostPresenter().postTop(organId, playInfo.getPlayId(), 5, playInfo.getIsTop() == 0 ? 1 : 2);
//
//                                    break;
//
//                                case 3://分享 PK
//
//                                    String imgUrl = "";
//
//                                    if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                        imgUrl = playInfo.getVideoImageUrl();
//                                    }
//
//                                    playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                            }
//
//
//                        } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
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
//                                                getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser2.getUserId(), 2);
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
//                                                getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser2.getUserId());
//                                            }
//                                        });
//                                        dialog2.show(getSupportFragmentManager());
//
//
//                                        break;
//
//                                    case 3://举报PK
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
//                                    case 4://删除PK
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
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), playInfo.getPlayId(), 5);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 5://置顶/取消置顶 活动
//                                        //是否置顶(0取消,1置顶)
//                                        getPostPresenter().postTop(organId, playInfo.getPlayId(), 5, playInfo.getIsTop() == 0 ? 1 : 2);
//
//                                        break;
//
//                                    case 6://分享 活动
//
//                                        String imgUrl = "";
//
//                                        if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                            imgUrl = playInfo.getVideoImageUrl();
//                                        }
//                                        playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                        ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                    case 7://拉黑 发PK的人
//
//                                        BaseUser baseUser = baseUser2;
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//                                        break;
//
//                                }
//                            } else {
//                                switch (type) {
//
//                                    case 1://举报PK
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
//                                    case 2://删除PK
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
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), playInfo.getPlayId(), 5);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 3://置顶/取消置顶 活动
//                                        //是否置顶(0取消,1置顶)
//                                        getPostPresenter().postTop(organId, playInfo.getPlayId(), 5, playInfo.getIsTop() == 0 ? 1 : 2);
//
//                                        break;
//
//                                    case 4://分享 活动
//
//                                        String imgUrl = "";
//
//                                        if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                            imgUrl = playInfo.getVideoImageUrl();
//                                        }
//                                        playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                        ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                    case 5://拉黑 发PK的人
//
//                                        if (baseUser2 != null) {
//                                            toBlacklistUserId = baseUser2.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//                                        break;
//
//                                }
//                            }
//
//
//                        } else if (baseUser2.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {// 自己的PK
//
//                            switch (type) {
//
//                                case 1://删除 PK
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
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), playInfo.getPlayId(), 5);
//
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//                                case 2://分享 PK
//
//                                    String imgUrl = "";
//
//                                    if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                        imgUrl = playInfo.getVideoImageUrl();
//                                    }
//
//                                    playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                            }
//
//                        } else {//别人的PK
//                            switch (type) {
//
//                                case 1://举报 PK
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
//                                case 2://分享 PK
//
//                                    String imgUrl = "";
//
//                                    if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                        imgUrl = playInfo.getVideoImageUrl();
//                                    }
//
//                                    playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                                case 3://拉黑 发PK的人
//
//                                    if (baseUser2 != null) {
//                                        toBlacklistUserId = baseUser2.getUserId();
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
//                break;
//
//
//            case 8:// 表白墙
//
//                final SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                if (sayLoveInfo == null) {
//                    return;
//                }
//
//                //用户角色：圈主或者管理员是自己
//                if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && sayLoveInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    dialog.setTextList("从本圈子删除", "分享", "取消");
//                } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                    if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                        dialog.setTextList(title1, title2, "举报", "从本圈子删除", "分享", "拉黑", "取消");
//                    } else {
//                        dialog.setTextList("举报", "从本圈子删除", "分享", "拉黑", "取消");
//                    }
//
//                } else if (sayLoveInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //自己的表白墙
//                    dialog.setTextList("从本圈子删除", "分享", "取消");
//                } else {//别人的表白墙
//                    dialog.setTextList("举报", "分享", "拉黑", "取消");
//                }
//
//                reportParentId = sayLoveInfo.getSayLoveId();
//                reportType = 1;
//
//                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                    @Override
//                    public void onClick(int type) {
//
//                        //用户角色：圈主或者管理员是自己
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && sayLoveInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//
//                            switch (type) {
//
//                                case 1://删除 表白墙
//
//                                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                    dialog.setText("", "确定删除表白？", "取消", "确定");
//                                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                        @Override
//                                        public void onLeftClick() {
//                                            //取消
//                                        }
//
//                                        @Override
//                                        public void onRightClick() {
//
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), sayLoveInfo.getSayLoveId(), 8);
//
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//                                case 2://分享表白墙弹窗
//
//                                    String imgUrl = "";
//                                    if (sayLoveInfo.getConfessionType() == 2) {
//                                        imgUrl = sayLoveInfo.getVideoImageUrl();
//                                    } else {
//                                        if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                            imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                    }
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//                                    break;
//
//                            }
//                        } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//
//                            if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//
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
//                                                getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sayLoveInfo.getBaseUser().getUserId(), 2);
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
//                                                getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sayLoveInfo.getBaseUser().getUserId());
//                                            }
//                                        });
//                                        dialog2.show(getSupportFragmentManager());
//
//
//                                        break;
//
//                                    case 3://举报帖子
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
//                                    case 4://删除表白
//
//                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                        dialog.setText("", "确定删除表白？", "取消", "确定");
//                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), sayLoveInfo.getSayLoveId(), 8);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//
//                                        break;
//
//                                    case 5://分享 帖子
//
//                                        String imgUrl = "";
//                                        if (sayLoveInfo.getConfessionType() == 2) {
//                                            imgUrl = sayLoveInfo.getVideoImageUrl();
//                                        } else {
//                                            if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                                imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//                                        break;
//
//                                    case 6://拉黑 发帖子的人
//
//                                        BaseUser baseUser = sayLoveInfo.getBaseUser();
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//
//
//                                        break;
//
//                                }
//
//                            } else {
//
//                                switch (type) {
//
//                                    case 1://举报帖子
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
//                                    case 2://删除表白
//
//                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                        dialog.setText("", "确定删除表白？", "取消", "确定");
//                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), sayLoveInfo.getSayLoveId(), 8);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//
//                                        break;
//
//
//                                    case 3://分享 帖子
//
//                                        String imgUrl = "";
//                                        if (sayLoveInfo.getConfessionType() == 2) {
//                                            imgUrl = sayLoveInfo.getVideoImageUrl();
//                                        } else {
//                                            if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                                imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                    case 4://拉黑 发帖子的人
//
//                                        BaseUser baseUser = sayLoveInfo.getBaseUser();
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//
//
//                                        break;
//
//                                }
//
//                            }
//
//
//                        } else if (sayLoveInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {//自己的表白墙
//
//                            switch (type) {
//
//                                case 1://删除 表白
//
//                                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                    dialog.setText("", "确定删除表白？", "取消", "确定");
//                                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                        @Override
//                                        public void onLeftClick() {
//                                            //取消
//                                        }
//
//                                        @Override
//                                        public void onRightClick() {
//
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), sayLoveInfo.getSayLoveId(), 8);
//
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//                                case 2://分享 表白墙
//
//                                    String imgUrl = "";
//                                    if (sayLoveInfo.getConfessionType() == 2) {
//                                        imgUrl = sayLoveInfo.getVideoImageUrl();
//                                    } else {
//                                        if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                            imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                    }
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                            }
//
//                        } else {//别人的表白墙
//                            switch (type) {
//
//                                case 1://举报 表白墙
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
//                                case 2://分享 帖子
//
//                                    String imgUrl = "";
//                                    if (sayLoveInfo.getConfessionType() == 2) {
//                                        imgUrl = sayLoveInfo.getVideoImageUrl();
//                                    } else {
//                                        if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                            imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                    }
//                                    ShareUtil.getInstance().showShare(AssociationDetailPKVideoListActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//
//                                    break;
//
//                                case 3://拉黑 发帖子的人
//
//                                    BaseUser baseUser = sayLoveInfo.getBaseUser();
//                                    if (baseUser != null) {
//                                        toBlacklistUserId = baseUser.getUserId();
//                                        getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                    }
//
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
//            case 9:// 同学帮帮忙
//
//                final HelpInfo helpInfo = homeInfo.getHelpInfo();
//                if (helpInfo == null) {
//                    return;
//                }
//
//                final BaseUser baseUser3 = helpInfo.getBaseUser() != null ? helpInfo.getBaseUser() : homeInfo.getBaseUser();
//
//                //用户角色：圈主或者管理员是自己
//                if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && baseUser3.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    dialog.setTextList("从本圈子删除", "取消");
//                } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                    if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                        dialog.setTextList(title1, title2, "举报", "删除", "拉黑", "取消");
//                    } else {
//                        dialog.setTextList("举报", "从本圈子删除", "拉黑", "取消");
//
//                    }
//                } else if (baseUser3.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //自己的帮帮忙
//                    dialog.setTextList("从本圈子删除", "取消");
//                } else {//别人的帮帮忙
//                    dialog.setTextList("举报", "拉黑", "取消");
//                }
//
//                reportParentId = helpInfo.getHelpId();
//                reportType = 10;
//
//                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                    @Override
//                    public void onClick(int type) {
//
//                        //用户角色：圈主或者管理员是自己
//                        if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && baseUser3.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//
//                            switch (type) {
//
//                                case 1://删除 帮帮忙
//
//                                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                    dialog.setText("", "确定删除同学帮帮忙？", "取消", "确定");
//                                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                        @Override
//                                        public void onLeftClick() {
//                                            //取消
//                                        }
//
//                                        @Override
//                                        public void onRightClick() {
//
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), helpInfo.getHelpId(), 9);
//
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//
//                            }
//
//
//                        } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
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
//                                                getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser3.getUserId(), 2);
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
//                                                getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser3.getUserId());
//                                            }
//                                        });
//                                        dialog2.show(getSupportFragmentManager());
//
//
//                                        break;
//
//                                    case 3://举报帮帮忙
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
//                                    case 4://删除帮帮忙
//
//                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                        dialog.setText("", "确定删除同学帮帮忙？", "取消", "确定");
//                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), helpInfo.getHelpId(), 9);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//
//                                    case 5://拉黑 发同学帮帮忙的人
//
//                                        BaseUser baseUser = baseUser3;
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//                                        break;
//
//                                }
//                            } else {
//                                switch (type) {
//
//                                    case 1://举报同学帮帮忙
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
//                                    case 2://删除帮帮忙
//
//                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                        dialog.setText("", "确定删除同学帮帮忙？", "取消", "确定");
//                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//
//                                                getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), helpInfo.getHelpId(), 9);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 3://拉黑 发同学帮帮忙的人
//
//                                        BaseUser userInfo = baseUser3;
//                                        if (userInfo != null) {
//                                            toBlacklistUserId = userInfo.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//                                        break;
//
//                                }
//                            }
//
//
//                        } else if (baseUser3.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {// 自己的帮帮忙
//
//                            switch (type) {
//
//                                case 1://删除 同学帮帮忙
//
//                                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                    dialog.setText("", "确定删除同学帮帮忙？", "取消", "确定");
//                                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                        @Override
//                                        public void onLeftClick() {
//                                            //取消
//                                        }
//
//                                        @Override
//                                        public void onRightClick() {
//
//                                            getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), helpInfo.getHelpId(), 9);
//
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager());
//
//                                    break;
//
//                            }
//
//                        } else {//别人的同学帮帮忙
//                            switch (type) {
//
//                                case 1://举报 同学帮帮忙
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
//                                case 2://拉黑 发同学帮帮忙的人
//
//                                    BaseUser userInfo = baseUser3;
//                                    if (userInfo != null) {
//                                        toBlacklistUserId = userInfo.getUserId();
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
//}
