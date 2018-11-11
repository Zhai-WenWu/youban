//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//import com.orhanobut.logger.Logger;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.lang.ref.WeakReference;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.AnonymousChatRoomStatusEvent;
//import cn.bjhdltcdn.p2plive.event.AnonymousMsgEvent;
//import cn.bjhdltcdn.p2plive.event.AssociationInfoEditorEvent;
//import cn.bjhdltcdn.p2plive.event.ClassMateHelpEvent;
//import cn.bjhdltcdn.p2plive.event.CreateAssociationEvent;
//import cn.bjhdltcdn.p2plive.event.PublishObjectEvent;
//import cn.bjhdltcdn.p2plive.event.UpdatePostListEvent;
//import cn.bjhdltcdn.p2plive.event.UpdateSayloveListEvent;
//import cn.bjhdltcdn.p2plive.handler.AdvertisementHandlerInActivity;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindOrganizationDetailResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindUserOrganStatusResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHomeBannerListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetPostAndActivityListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSchoolmateOrganListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.HelpPraiseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SayLovePraiseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SchoolOrganSwitchResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.UpdateChatRoomResponse;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.AssociationDetailRecyListItemModel;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.ChatInfo;
//import cn.bjhdltcdn.p2plive.model.Group;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.PlayInfo;
//import cn.bjhdltcdn.p2plive.model.PostInfo;
//import cn.bjhdltcdn.p2plive.model.RecommendInfo;
//import cn.bjhdltcdn.p2plive.model.ReportType;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.model.StoreDetail;
//import cn.bjhdltcdn.p2plive.model.StoreInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ClassMateHelpPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationDetailListAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationDetailRecyListAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.SelectorReportContentDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.RongIMutils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//import cn.bjhdltcdn.p2plive.widget.WrapContentLinearLayoutManager;
//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;
//import io.rong.imlib.model.UserInfo;
//
///**
// * 圈子详情
// */
//public class AssociationDetailActivity extends BaseActivity implements BaseView {
//
//    private long organId;//圈子Id,
//    private OrganizationInfo organizationInfo;
//
//    private RecyclerView mRecyclerView;
//
//    private AssociationDetailRecyListAdapter adapter;
//
//    private int toLock;
//
//    private AssociationPresenter associationPresenter;
//
//    private GetRecommendListPresenter getRecommendListPresenter;
//
//    private ToolBarFragment titleFragment;
//
//    private PostCommentListPresenter postCommentListPresenter;
//
//    private GetCommentListPresenter mCommentListPresenter;
//
//    private int pageSize = 20;
//    private int pageNumber = 1;
//
//    private GroupPresenter groupPresenter;
//    private TwinklingRefreshLayout refreshLayout;
//
//    private long reportParentId;
//    private int reportType;
//
//    private CommonPresenter commonPresenter;
//    private PostCommentListPresenter postPresenter;
//
//    private int itemPosition = -1;
//    private int itemMessageType;
//
//    private UserPresenter userPresenter;
//    private GetStoreListPresenter getStoreListPresenter;
//
//    /**
//     * 添加到黑名单的用户id
//     */
//    private long toBlacklistUserId;
//
//    /**
//     * 是否可以踢出圈子(0不显示(否),1可以)
//     */
//    private int isKickedOut;
//    /**
//     * 是否禁言(0不显示,1解除禁言,2禁言)
//     */
//    private int isGag;
//    //banner图view
//    public CustomViewPager viewPager;
//    public AdvertisementHandlerInActivity handler;
//    private long userId;
//    private View publishView;
//    private ClassMateHelpPresenter classMateHelpPresenter;
//
//    /**
//     * 是否刷新
//     * true 刷新
//     * false 不刷新
//     */
//    private boolean isRefreshed;
//
//    /**
//     * 列表总数
//     */
//    private int total;
//
//    /**
//     * 是否更改圈子资料
//     */
//    private boolean isAssociationInfoEditorEvent;
//    private View tabTopImageView;
//    private ChatRoomPresenter chatRoomPresenter;
//    private boolean isPublish;
//    public boolean canClickCharRoom = true;
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
//    public AssociationPresenter getAssociationPresenter() {
//        if (associationPresenter == null) {
//            associationPresenter = new AssociationPresenter(this);
//        }
//        return associationPresenter;
//    }
//
//    public GroupPresenter getGroupPresenter() {
//        if (groupPresenter == null) {
//            groupPresenter = new GroupPresenter(this);
//        }
//        return groupPresenter;
//    }
//
//    public CommonPresenter getCommonPresenter() {
//        if (commonPresenter == null) {
//            commonPresenter = new CommonPresenter(this);
//        }
//        return commonPresenter;
//    }
//
//    public PostCommentListPresenter getPostPresenter() {
//
//        if (postPresenter == null) {
//            postPresenter = new PostCommentListPresenter(this);
//        }
//        return postPresenter;
//    }
//
//    public UserPresenter getUserPresenter() {
//        if (userPresenter == null) {
//            userPresenter = new UserPresenter(this);
//        }
//        return userPresenter;
//    }
//
//    public GetRecommendListPresenter getGetRecommendListPresenter() {
//        if (getRecommendListPresenter == null) {
//            getRecommendListPresenter = new GetRecommendListPresenter(this);
//        }
//        return getRecommendListPresenter;
//    }
//
//    public ChatRoomPresenter getChatRoomPresenter() {
//        if (chatRoomPresenter == null) {
//            chatRoomPresenter = new ChatRoomPresenter(this);
//        }
//        return chatRoomPresenter;
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
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_association_detail_layout);
//
//        EventBus.getDefault().register(this);
//
//        getExtraDate(getIntent());
//
//        setTitle();
//
//        initView();
//
//        EventBus.getDefault().post(new CreateAssociationEvent(3));
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//
//        getAssociationPresenter().findOrganizationDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organId);
//
//
//    }
//
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        setIntent(intent);
//
//        getExtraDate(getIntent());
//
//
//        setTitle();
//        initView();
//
//        EventBus.getDefault().post(new CreateAssociationEvent(3));
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//
//        getAssociationPresenter().findOrganizationDetail(userId, organId);
//
//    }
//
//
//    private void getExtraDate(Intent intent) {
//        organId = intent.getLongExtra(Constants.Fields.ORGAN_ID, 0);
//        organizationInfo = intent.getParcelableExtra(Constants.KEY.KEY_OBJECT);
//
//        if (organizationInfo != null && organId < 1) {
//            organId = organizationInfo.getOrganId();
//        }
//
//    }
//
//    private void setTitle() {
//
//        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        if (organizationInfo != null && organizationInfo.getType() == 2) {
//            titleFragment.getRootView().setVisibility(View.GONE);
//            findViewById(R.id.line_view_1).setVisibility(View.GONE);
//        } else {
//            titleFragment.getRootView().setVisibility(View.VISIBLE);
//            findViewById(R.id.line_view_1).setVisibility(View.VISIBLE);
//        }
//
//        titleFragment.setTitleView(organizationInfo != null ? organizationInfo.getOrganName() : "");
//        titleFragment.setRightView(R.mipmap.asso_right_gray_icon, new ToolBarFragment.ViewOnclick() {
//            @Override
//            public void onClick() {
//                if (organizationInfo != null) {
//                    startActivity(new Intent(AssociationDetailActivity.this, AssociationInfoActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo).putExtra(Constants.Fields.POSITION, 1));
//                }
//            }
//        });
//
//    }
//
//
//    private void initView() {
//
//        handler = new AdvertisementHandlerInActivity(new WeakReference<BaseActivity>(this));
//
//        mRecyclerView = findViewById(R.id.recycler_view);
//
//        final WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(App.getInstance());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        adapter = new AssociationDetailRecyListAdapter(this);
//        mRecyclerView.setAdapter(adapter);
//
//        adapter.setCharRoomOnClick(new AssociationDetailRecyListAdapter.CharRoomOnClick() {
//            @Override
//            public void onClick(ChatInfo chatInfo) {
//                if (canClickCharRoom) {
//                    canClickCharRoom = false;
//                    if (chatInfo.getUserRole() == 1 || chatInfo.getIsLock() == 0) {
//                        getChatRoomPresenter().updateChatRoomUser(userId, 0, chatInfo.getChatId(), 0);
//                    } else if (chatInfo.getIsLock() == 1) {
//                        Utils.showToastShortTime("房主已锁门");
//                        canClickCharRoom = true;
//                    }
//                }
//            }
//
//            @Override
//            public void onClockClick(ChatInfo chatInfo) {
//                if (Utils.isAllowClick()) {
//                    int isLock = chatInfo.getIsLock();
//                    if (isLock == 0) {
//                        toLock = 1;
//                    } else if (isLock == 1) {
//                        toLock = 0;
//                    }
//                    getChatRoomPresenter().updateChatRoomLock(userId, chatInfo.getChatId(), toLock);
//                }
//            }
//        });
//
//        adapter.setOneToOneCharOnClick(new AssociationDetailRecyListAdapter.OneToOneCharOnClick() {
//            @Override
//            public void onClick(long toUserId) {
//                getUserPresenter().getAnonymityUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toUserId);
//            }
//        });
//
//        tabTopImageView = findViewById(R.id.tab_top);
//        tabTopImageView.setVisibility(View.GONE);
//        tabTopImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mRecyclerView.smoothScrollToPosition(0);
//            }
//        });
//
//
//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
//                if (firstVisibleItem > 0) {
//                    tabTopImageView.setVisibility(View.VISIBLE);
//                } else {
//                    tabTopImageView.setVisibility(View.GONE);
//                }
//
//            }
//        });
//
//        // 对圈子群处理
//        adapter.setJoinGroupCallBack(new AssociationDetailRecyListAdapter.JoinGroupCallBack() {
//            @Override
//            public void joinGroupCallBack(Group group) {
//
//                if (group != null) {
//                    getGroupPresenter().joinGroup(userId, group.getGroupId(), group.getGroupMode());
//                }
//
//            }
//        });
//
//        // 对圈子处理
//        adapter.setJoinOrganizationCallBack(new AssociationDetailRecyListAdapter.JoinOrganizationCallBack() {
//            @Override
//            public void joinOrganizationCallBack(OrganizationInfo organizationInfo, int type) {
//
//                if (organizationInfo != null) {
//                    getAssociationPresenter().joinOrganization(organizationInfo.getOrganId(), userId, type);
//                }
//
//            }
//        });
//
//        adapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                Logger.d("position ==== " + position);
//
//                Object object = adapter.getList().get(position);
//                if (object instanceof HomeInfo) {
//                    HomeInfo homeInfo = (HomeInfo) object;
//                    switch (homeInfo.getType()) {//1帖子,2圈子,3活动,4房间,5PK挑战
//                        case 1:
//                            PostInfo postInfo = homeInfo.getPostInfo();
//                            if (postInfo != null) {
//                                if (postInfo.getContentLimit() != 2 && organizationInfo.getType() == 2) {
//                                    startActivity(new Intent(AssociationDetailActivity.this, PostDetailActivity.class)
//                                            .putExtra(Constants.KEY.KEY_OBJECT, postInfo)
//                                            .putExtra(Constants.KEY.KEY_POSITION, position)
//                                            .putExtra(Constants.Fields.COME_IN_TYPE, 1));
//                                }
//
//                                if (organizationInfo.getType() != 2) {
//                                    startActivity(new Intent(AssociationDetailActivity.this, PostDetailActivity.class)
//                                            .putExtra(Constants.KEY.KEY_OBJECT, postInfo)
//                                            .putExtra(Constants.KEY.KEY_POSITION, position)
//                                            .putExtra(Constants.Fields.COME_IN_TYPE, 1));
//                                }
//
//                            }
//                            break;
//
//                        case 3:
//                            ActivityInfo activityInfo = homeInfo.getActivityInfo();
//                            if (activityInfo != null) {
//                                startActivity(new Intent(AssociationDetailActivity.this, ActiveDetailActivity.class)
//                                        .putExtra(Constants.KEY.KEY_ACTIVITY_ID, activityInfo.getActivityId())
//                                        .putExtra(Constants.Fields.POSITION, position));
//                            }
//
//
//                            break;
//
//
//                        case 5:
//                            PlayInfo playInfo = homeInfo.getPlayInfo();
//                            if (playInfo != null) {
//                                Intent intent = new Intent(AssociationDetailActivity.this, VideoPlayFullScreenActivity.class);
//                                intent.putExtra(Constants.Fields.VIDEO_PATH, playInfo.getVideoUrl());
//                                intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, playInfo.getVideoImageUrl());
//                                startActivity(intent);
//
//                            }
//
//                            break;
//
//                        case 8:
//                            SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                            if (sayLoveInfo != null) {
//                                //跳转到表白详情页
//                                Intent intent = new Intent(AssociationDetailActivity.this, SayLoveDetailActivity.class);
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
//                            Intent intent = new Intent(AssociationDetailActivity.this, ClassMateHelpDetailActivity.class);
//                            intent.putExtra(Constants.Fields.POSITION, position);
//                            intent.putExtra(Constants.KEY.KEY_OBJECT, helpInfo);
//                            startActivity(intent);
//
//                            break;
//
//                    }
//
//                }
//
//            }
//        });
//
//        adapter.setItemWidgetOnClick(new AssociationDetailListAdapter.ItemWidgetOnClick() {
//            @Override
//            public void onPraise(long postId, int type, int position) {// 点赞回调
//
//                itemPosition = position;
//
//                Object object = adapter.getList().get(position);
//                if (object instanceof HomeInfo) {
//                    HomeInfo homeInfo = (HomeInfo) object;
//
//                    switch (homeInfo.getType()) {//1帖子,2圈子,3活动,4房间,5PK挑战
//
//                        case 1:
//                            getPostCommentListPresenter().postPraise(userId, postId, type);
//
//                            break;
//
//                        case 8:
//                            SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                            getCommentListPresenter().sayLovePraise(sayLoveInfo.getSayLoveId(), sayLoveInfo.getIsPraise() == 1 ? 2 : 1, userId);
//
//                            break;
//
//                        case 9:
//
//                            HelpInfo helpInfo = homeInfo.getHelpInfo();
//                            if (helpInfo != null) {
//                                getClassMateHelpPresenter().helpPraise(userId, helpInfo.getHelpId(), helpInfo.getIsPraise() == 1 ? 2 : 1);
//                            }
//
//                            break;
//
//                    }
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
//            public void moreImg(int itemType, int position) {// 更多按钮回调
//
//
//                itemPosition = position;
//                itemMessageType = itemType;
//
//                Object object = adapter.getList().get(position);
//                if (object instanceof HomeInfo) {
//                    HomeInfo homeInfo = (HomeInfo) object;
//
//                    switch (homeInfo.getType()) {//1帖子,2圈子,3活动,4房间,5PK挑战
//                        case 1:
//                            PostInfo postInfo = homeInfo.getPostInfo();
//
//                            // 圈子或管理员,检查是否有禁言和踢出圈子
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && postInfo.getBaseUser().getUserId() != userId) {
//                                getAssociationPresenter().findUserOrganStatus(userId, postInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                                return;
//
//                            }
//
//                            showPostOperationFragmentDialog(itemType, position, "", "");
//                            break;
//
//                        case 3:
//                            ActivityInfo activityInfo = homeInfo.getActivityInfo();
//                            // 圈子或管理员,检查是否有禁言和踢出圈子
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && activityInfo.getBaseUser().getUserId() != userId) {
//                                getAssociationPresenter().findUserOrganStatus(userId, activityInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                                return;
//
//                            }
//                            showPostOperationFragmentDialog(itemType, position, "", "");
//
//                            break;
//
//
//                        case 5:
//
//                            PlayInfo playInfo = homeInfo.getPlayInfo();
//
//                            // 圈子或管理员,检查是否有禁言和踢出圈子
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && playInfo.getBaseUser().getUserId() != userId) {
//                                getAssociationPresenter().findUserOrganStatus(userId, playInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                                return;
//
//                            }
//
//                            showPostOperationFragmentDialog(itemType, position, "", "");
//                            break;
//
//                        case 8:
//
//                            SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                            // 圈子或管理员,检查是否有禁言和踢出圈子
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && sayLoveInfo.getBaseUser().getUserId() != userId) {
//                                getAssociationPresenter().findUserOrganStatus(userId, sayLoveInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                                return;
//
//                            }
//
//                            showPostOperationFragmentDialog(itemType, position, "", "");
//
//                            break;
//
//                        case 9:
//
//
//                            HelpInfo helpInfo = homeInfo.getHelpInfo();
//
//                            // 圈子或管理员,检查是否有禁言和踢出圈子
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && helpInfo.getBaseUser().getUserId() != userId) {
//                                getAssociationPresenter().findUserOrganStatus(userId, helpInfo.getBaseUser().getUserId(), organizationInfo.getOrganId());
//
//                                return;
//
//                            }
//
//                            showPostOperationFragmentDialog(itemType, position, "", "");
//
//                            break;
//
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onApplyClerk(long storeId) {
//                getStoreListPresenter().findStoreDetail(userId, storeId);
//            }
//        });
//
//
//        refreshLayout = findViewById(R.id.refresh_layout_view);
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        LoadingView loadingView = new LoadingView(App.getInstance());
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(mRecyclerView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(true);//是否在底部越界的时候自动切换到加载更多模式
//        refreshLayout.setEnableLoadmore(true);//设置底部是否可以上拉加载
//
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//
//                refreshLayout.finishRefreshing();
//                pageNumber = 1;
//
//                refreshDataPost(true);
//
//
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//
//                refreshLayout.finishLoadmore();
//
//                if (total <= 0) {
//                    return;
//                }
//
//                refreshDataPost(false);
//
//            }
//
//        });
//
//        // 发布帖子按钮
//        publishView = findViewById(R.id.publish_image_view);
//
//        publishView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //发布内容
//                isPublish = true;
//                getAssociationPresenter().findUserOrganStatus(userId, userId, organizationInfo.getOrganId());
//
//            }
//        });
//
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
//
//        if (InterfaceUrl.URL_FINDORGANIZATIONDETAIL.equals(apiName)) {
//            if (object instanceof FindOrganizationDetailResponse) {
//                FindOrganizationDetailResponse response = (FindOrganizationDetailResponse) object;
//                if (response.getCode() == 200) {
//
//                    if (response.getOrganizationInfo() == null) {
//                        Utils.showToastShortTime(response.getMsg());
//                        finish();
//                        return;
//                    }
//
//                    organizationInfo = response.getOrganizationInfo() != null ? response.getOrganizationInfo() : organizationInfo;
//
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tabTopImageView.getLayoutParams();
//                    // 学校圈子
//                    if (organizationInfo.getType() == 2) {
//
//                        if (layoutParams != null) {
//                            layoutParams.topMargin = Utils.dp2px(10);
//                        }
//
//                    } else {
//                        if (layoutParams != null) {
//                            layoutParams.topMargin = Utils.dp2px(60);
//                        }
//
//                    }
//
//                    // 获取学校圈子发布开关
//                    if (organizationInfo.getType() == 2) {
//                        getAssociationPresenter().schoolOrganSwitch(userId, organizationInfo.getOrganId());
//                    }
//
//                    // 发布帖子按钮
//                    //所属角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                    if (organizationInfo.getUserRole() == 0 || organizationInfo.getUserRole() == 4) {
//                        publishView.setVisibility(View.GONE);
//                    } else if (organizationInfo.getType() == 2 && organizationInfo.getUserRole() == 3) {// 学校圈子中的普通成员
//                        publishView.setVisibility(View.GONE);
//                    } else {
//                        publishView.setVisibility(View.VISIBLE);
//                    }
//
//                    setTitle();
//
//
//                    // 设置头部数据
//                    AssociationDetailRecyListItemModel model = new AssociationDetailRecyListItemModel();
//                    model.setOrganizationInfo(organizationInfo);
//                    model.setType(100);
//
//                    if (adapter == null) {
//                        adapter = new AssociationDetailRecyListAdapter(this);
//                    }
//
//                    if (adapter.getItemCount() == 0) {
//                        adapter.setHeaderViewData(model, null);
//                        // 请求广告数据
//                        getGetRecommendListPresenter().getHomeBannerList(userId, 2);
//
//                    } else {
//                        adapter.setHeaderViewData(model, organizationInfo);
//
//                    }
//
//                    if (!isAssociationInfoEditorEvent) {
//                        isAssociationInfoEditorEvent = false;
//                        refreshDataPost(true);
//                    }
//
//                }
//            }
//        } else if (InterfaceUrl.URL_SCHOOLORGANSWITCH.equals(apiName)) {
//
//            if (object instanceof SchoolOrganSwitchResponse) {
//                final SchoolOrganSwitchResponse response = (SchoolOrganSwitchResponse) object;
//                if (response.getCode() == 200) {
//
//                    //系统开关(1开启,2关闭)
//                    if (response.getIsOpen() == 2) {
//                        publishView.setVisibility(View.GONE);
//
//                    }
//
//                    // 是否有发布权限(1有,2没有)
//                    if (response.getIsAuth() == 2) {
//                        publishView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Utils.showToastShortTime(response.getMsg());
//                            }
//                        });
//                    }
//                }
//
//            }
//        } else if (InterfaceUrl.URL_UPDATECHATROOMLOCK.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                if (response.getCode() == 200) {
//                    organizationInfo.getChatInfo().setIsLock(toLock);
//                    adapter.notifyItemChanged(0);
//                }
//                Utils.showToastShortTime(response.getMsg());
//            }
//        } else if (InterfaceUrl.URL_GETPOSTANDACTIVITYLIST.equals(apiName)) {
//
//            if (object instanceof GetPostAndActivityListResponse) {
//
//                GetPostAndActivityListResponse response = (GetPostAndActivityListResponse) object;
//
//                if (response.getCode() == 200) {
//
//                    total = response.getTotal();
//
//                    Logger.d("total =====>>>>>>> " + total);
//
//                    if (pageNumber == 1) {
//                        if (isRefreshed) {
//                            List list = response.getList();
//                            if (list != null) {
//                                adapter.setOnRefreshData(list);
//                            }
//
//                            isRefreshed = false;
//                        } else {
//                            adapter.setList(response.getList());
//                        }
//
//                    } else {
//                        adapter.setListAll(response.getList());
//                    }
//
//                    pageNumber++;
//
//
//                    if (total > 0) {
//
//                        Object object3 = adapter.getList().get(adapter.getItemCount() - 1);
//                        if (object3 instanceof AssociationDetailRecyListItemModel) {
//                            AssociationDetailRecyListItemModel recyListItemModel = (AssociationDetailRecyListItemModel) object3;
//                            if (recyListItemModel.getType() == 1002) {// 如果列表中有emptyView则删除
//                                adapter.getList().remove(object3);
//                                adapter.notifyItemRemoved(adapter.getItemCount() - 1);
//                                adapter.notifyItemRangeChanged(adapter.getItemCount() - 1, 1);
//                            }
//
//                        }
//
//                    } else {
//
//                        if (adapter.getItemCount() > 0) {
//                            Object object4 = adapter.getList().get(adapter.getItemCount() - 1);
//                            if (object4 instanceof AssociationDetailRecyListItemModel) {
//                                AssociationDetailRecyListItemModel recyListItemModel = (AssociationDetailRecyListItemModel) object4;
//                                if (recyListItemModel.getType() == 102) {// 如果已经添加空布局则不能再次添加
//                                    return;
//                                }
//
//                            }
//                        }
//
//                        if (!isHasHomeInfo()) {
//                            // 设置空数据
//                            AssociationDetailRecyListItemModel model = new AssociationDetailRecyListItemModel();
//                            model.setType(102);
//                            model.setEmptyText(response.getBlankHint());
//                            adapter.setEmptyViewData(model);
//                        }
//
//
//                    }
//
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//            }
//
//
//        } else if (InterfaceUrl.URL_GETSCHOOLMATEORGANLIST.equals(apiName)) {
//            if (object instanceof GetSchoolmateOrganListResponse) {
//                GetSchoolmateOrganListResponse response = (GetSchoolmateOrganListResponse) object;
//                if (response.getCode() == 200) {
//
//                    total = response.getTotal();
//
//                    if (pageNumber == 1) {
//                        if (isRefreshed) {
//                            List list = response.getList();
//                            if (list != null) {
//                                adapter.setOnRefreshData(list);
//                            }
//
//                            isRefreshed = false;
//                        } else {
//                            adapter.setList(response.getList());
//                        }
//
//                    } else {
//                        adapter.setListAll(response.getList());
//                    }
//
//                    pageNumber++;
//
//
//                    if (total > 0) {
//
//                        Object object3 = adapter.getList().get(adapter.getItemCount() - 1);
//                        if (object3 instanceof AssociationDetailRecyListItemModel) {
//                            AssociationDetailRecyListItemModel recyListItemModel = (AssociationDetailRecyListItemModel) object3;
//                            if (recyListItemModel.getType() == 1002) {// 如果列表中有emptyView则删除
//                                adapter.getList().remove(object3);
//                                adapter.notifyItemRemoved(adapter.getItemCount() - 1);
//                                adapter.notifyItemRangeChanged(adapter.getItemCount() - 1, 1);
//                            }
//
//                        }
//
//                    } else {
//
//                        if (adapter.getItemCount() > 0) {
//                            Object object4 = adapter.getList().get(adapter.getItemCount() - 1);
//                            if (object4 instanceof AssociationDetailRecyListItemModel) {
//                                AssociationDetailRecyListItemModel recyListItemModel = (AssociationDetailRecyListItemModel) object4;
//                                if (recyListItemModel.getType() == 102) {// 如果已经添加空布局则不能再次添加
//                                    return;
//                                }
//
//                            }
//                        }
//
//                        if (!isHasHomeInfo()) {
//                            // 设置空数据
//                            AssociationDetailRecyListItemModel model = new AssociationDetailRecyListItemModel();
//                            model.setType(102);
//                            model.setEmptyText(response.getBlankHint());
//                            adapter.setEmptyViewData(model);
//                        }
//
//
//                    }
//
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_JOIN_GROUP.equals(apiName) || InterfaceUrl.URL_JOINORGANIZATION.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//
//                Utils.showToastShortTime(response.getMsg());
//
//                if (response.getCode() == 200) {
//                    getAssociationPresenter().findOrganizationDetail(userId, organId > 0 ? organId : (organizationInfo != null ? organizationInfo.getOrganId() : 0));
//                }
//
//            }
//        } else if (InterfaceUrl.URL_POSTPRAISE.equals(apiName)) {
//            if (object instanceof PostPraiseResponse) {
//                PostPraiseResponse response = (PostPraiseResponse) object;
//                if (response.getCode() != 200) {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//        } else if (InterfaceUrl.URL_FINDUSERORGANSTATUS.equals(apiName)) {
//            if (object instanceof FindUserOrganStatusResponse) {
//                FindUserOrganStatusResponse response = (FindUserOrganStatusResponse) object;
//                if (isPublish) {
//                    if (response.getIsGag() == 2) {
//                        Utils.showToastShortTime("您已被该圈子禁言，不能发贴");
//                    } else {
//                        Intent intent = new Intent(AssociationDetailActivity.this, PublishActivity.class);
//                        intent.putExtra(Constants.Fields.TYPE, 2);
//                        intent.putExtra(Constants.KEY.KEY_OBJECT, organizationInfo);
//                        if (organizationInfo.getType() == 3) {
//                            intent.putExtra(Constants.Fields.IS_ANONYMOUS, 1);
//                        }
//                        startActivity(intent);
//                    }
//                    isPublish = false;
//                    return;
//                }
//                if (response.getIsKickedOut() == 1) {
//
//                    isKickedOut = response.getIsKickedOut();
//                    isGag = response.getIsGag();
//
//                    if (adapter != null) {
//                        adapter.setIsGag(isGag);
//                    }
//
//                    if (itemMessageType > 0 && itemPosition > -1 && isKickedOut == 1) {
//                        switch (isGag) {
//                            case 1:
//                                showPostOperationFragmentDialog(itemMessageType, itemPosition, "将Ta踢出圈子", "对Ta禁言");
//                                break;
//                            case 2:
//                                showPostOperationFragmentDialog(itemMessageType, itemPosition, "将Ta踢出圈子", "解除禁言");
//                                break;
//                            case 0:
//                                showPostOperationFragmentDialog(itemMessageType, itemPosition, "将Ta踢出圈子", "");
//                                break;
//                        }
//                    }
//
//                } else {
//                    switch (isGag) {
//                        case 1:
//                            showPostOperationFragmentDialog(itemMessageType, itemPosition, "对Ta禁言", "");
//                            break;
//                        case 2:
//                            showPostOperationFragmentDialog(itemMessageType, itemPosition, "解除禁言", "");
//                            break;
//                        case 0:
//                            showPostOperationFragmentDialog(itemMessageType, itemPosition, "", "");
//                            break;
//                    }
////                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_DELETEPOST.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//                if (response.getCode() == 200) {
//                    // 删除帖子
//                    if (itemPosition > -1 && adapter.getItemCount() > itemPosition) {
//                        adapter.getList().remove(itemPosition);
//                        adapter.notifyItemRemoved(itemPosition);
//                        adapter.notifyItemRangeChanged(itemPosition, adapter.getItemCount() - itemPosition);
//                    }
//                    itemPosition = -1;
//
//                }
//            }
//        } else if (InterfaceUrl.URL_REPORTOPERATION.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//            }
//        } else if (InterfaceUrl.URL_POSTTOP.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//                if (response.getCode() == 200) {
//
//                    refreshDataPost(true);
//
//                    itemPosition = -1;
//
//                }
//            }
//        } else if (InterfaceUrl.URL_DELETEORGANCONTENT.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//
//                if (response.getCode() == 200) {// 删除圈子内容
//
//                    if (itemPosition > -1 && adapter.getItemCount() > itemPosition) {
//                        adapter.getList().remove(itemPosition);
//                        adapter.notifyItemRemoved(itemPosition);
//                        adapter.notifyItemRangeChanged(itemPosition, adapter.getItemCount() - itemPosition);
//                    }
//                    itemPosition = -1;
//
//                }
//
//
//            }
//        } else if (InterfaceUrl.URL_HELPPRAISE.equals(apiName)) {
//            if (object instanceof HelpPraiseResponse) {
//                HelpPraiseResponse response = (HelpPraiseResponse) object;
//
//                if (response.getCode() == 200) {
//
//                    if (adapter.getItemCount() > 0 && adapter.getItemCount() > itemPosition && itemPosition > -1) {
//                        Object itemObject = adapter.getList().get(itemPosition);
//                        if (itemObject instanceof HomeInfo) {
//                            HomeInfo homeInfo = (HomeInfo) itemObject;
//                            if (homeInfo.getType() == 9) {
//
//                                HelpInfo helpInfo = homeInfo.getHelpInfo();
//                                helpInfo.setIsPraise(response.getIsPraise());
//                                helpInfo.setPraiseCount(helpInfo.getPraiseCount() + (response.getIsPraise() == 1 ? 1 : -1));
//
//                                adapter.notifyItemChanged(itemPosition, true);
//                                itemPosition = -1;
//                            }
//
//                        }
//                    }
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//        } else if (apiName.equals(InterfaceUrl.URL_SAYLOVEPRAISE)) {
//            if (object instanceof SayLovePraiseResponse) {
//                SayLovePraiseResponse response = (SayLovePraiseResponse) object;
//                if (response.getCode() == 200) {
//                    if (adapter.getItemCount() > 0 && adapter.getItemCount() > itemPosition && itemPosition > -1) {
//                        Object itemObject = adapter.getList().get(itemPosition);
//                        if (itemObject instanceof HomeInfo) {
//                            HomeInfo homeInfo = (HomeInfo) itemObject;
//                            if (homeInfo.getType() == 8) {
//
//                                SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                                sayLoveInfo.setIsPraise(response.getIsPraise());
//                                sayLoveInfo.setPraiseCount(sayLoveInfo.getPraiseCount() + (response.getIsPraise() == 1 ? 1 : -1));
//
//                                adapter.notifyItemChanged(itemPosition, true);
//                                itemPosition = -1;
//                            }
//
//                        }
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_PULLBLACKUSER.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//                if (response.getCode() == 200) {
//                    RongIM.getInstance().addToBlacklist(toBlacklistUserId + "", new RongIMClient.OperationCallback() {
//                        @Override
//                        public void onSuccess() {
//
//                            if (isFinishing()) {
//                                return;
//                            }
//
//                            toBlacklistUserId = 0;
//                            Logger.d("拉黑成功");
//
//                        }
//
//                        @Override
//                        public void onError(RongIMClient.ErrorCode errorCode) {
//
//                            if (isFinishing()) {
//                                return;
//                            }
//
//                            toBlacklistUserId = 0;
//                            Logger.d("失败原因：" + errorCode.getMessage());
//
//                        }
//                    });
//                }
//            }
//        } else if (InterfaceUrl.URL_UPDATEORGANUSERSTATUS.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//
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
//        } else if (apiName.equals(InterfaceUrl.URL_GETHOMEBANNERLIST)) {
//            if (object instanceof GetHomeBannerListResponse) {
//                GetHomeBannerListResponse getHomeBannerListResponse = (GetHomeBannerListResponse) object;
//                if (getHomeBannerListResponse.getCode() == 200) {
//                    List<RecommendInfo> recommendInfoList = getHomeBannerListResponse.getList();
//                    if (recommendInfoList != null && recommendInfoList.size() > 0) {
//
//                        AssociationDetailRecyListItemModel model = new AssociationDetailRecyListItemModel();
//                        model.setType(101);
//                        model.setRecommendInfoList(getHomeBannerListResponse.getList());
//                        adapter.setADHeaderViewData(model);
//
//
//                        if (handler != null) {
//                            adapter.setHandler(handler);
//                        }
//
//
//                    }
//
//                } else {
//                    Utils.showToastShortTime(getHomeBannerListResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_UPDATECHATROOMUSER)) {
//
//            if (object instanceof UpdateChatRoomResponse) {
//                UpdateChatRoomResponse response = (UpdateChatRoomResponse) object;
//                canClickCharRoom = true;
//                if (response.getCode() == 200) {
//                    final ChatInfo chatInfo = response.getChatInfo();
//                    if (chatInfo != null) {
//                        Constants.Object.CHATINFO = chatInfo;
//                        RongIMutils.joinChatRoom(this, chatInfo.getChatId(), false);
//                        EventBus.getDefault().post(new AnonymousChatRoomStatusEvent(true));
//                        UserInfo userInfo = new UserInfo(String.valueOf(chatInfo.getToBaseUser().getUserId()), chatInfo.getToBaseUser().getNickName(), Uri.parse(chatInfo.getToBaseUser().getUserIcon()));
//                        RongIM.getInstance().refreshUserInfoCache(userInfo);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                EventBus.getDefault().post(new AnonymousMsgEvent(chatInfo.getToBaseUser(), null));
//                                if (!SafeSharePreferenceUtils.getBoolean(Constants.Fields.IS_ANONYMOUS_CHATROOM_TOAST_SHOW, false)) {
//                                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.IS_ANONYMOUS_CHATROOM_TOAST_SHOW, true);
//                                    Utils.showToastShortTime("您进入的是匿名聊天房间，头像和昵称随机生成，在此聊天室请您注意言语措辞！");
//                                }
//                            }
//                        }, 1000);
//                        Utils.showToastShortTime(response.getMsg());
//                    }
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//        } else if (apiName.equals(InterfaceUrl.URL_FINDSTOREDETAIL)) {
//            if (object instanceof FindStoreDetailResponse) {
//                FindStoreDetailResponse findStoreDetailResponse = (FindStoreDetailResponse) object;
//                if (findStoreDetailResponse.getCode() == 200) {
//                    StoreDetail storeDetail = findStoreDetailResponse.getStoreDetail();
//                    if (storeDetail != null) {
//                        int isClert = storeDetail.getIsClert();//当前是否是店员(1店主,2店员,3普通用户,4店员申请中),
//                        StoreInfo storeInfo = storeDetail.getStoreInfo();
//                        int isPublish = storeInfo.getIsPublish();
//                        if (isPublish == 1) {
//                            if (isClert == 1) {
//                                //店长自己不跳转
//                            } else if (isClert == 2) {
//                                Utils.showToastShortTime("您已成为该店店员");
//                            } else if (isClert == 3) {
//                                //跳到店员申请界面
//                                Intent intent = new Intent(AssociationDetailActivity.this, ApplyClerkActivity.class);
//                                intent.putExtra(Constants.Fields.STORE_ID, storeInfo.getStoreId());
//                                startActivity(intent);
//                            } else if (isClert == 4) {
//                                //店员申请中
//                                Utils.showToastShortTime("店员申请中");
//                            }
//                        } else {
//                            Utils.showToastShortTime("该店已关闭招聘信息");
//                        }
//                    }
//                } else {
//                    Utils.showToastShortTime(findStoreDetailResponse.getMsg());
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 检查是否有数据
//     *
//     * @return
//     */
//    private boolean isHasHomeInfo() {
//        for (int i = 0; i < adapter.getItemCount(); i++) {
//            Object object = adapter.getList().get(i);
//            if (object instanceof HomeInfo) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 请求刷新数据
//     */
//    private void refreshDataPost(boolean isRefreshed) {
//
//        if (isRefreshed) {
//            pageNumber = 1;
//            this.isRefreshed = true;
//        } else {
//            this.isRefreshed = false;
//        }
//
//        // 获取列表数据
//        if (organizationInfo != null && organizationInfo.getType() != 2) {
//            getPostCommentListPresenter().getPostAndActivityList(userId, organizationInfo.getOrganId(), 1, pageSize, pageNumber, true);
//
//        } else {
//
//            if (organizationInfo != null) {
//                getAssociationPresenter().getSchoolmateOrganList(userId, organizationInfo.getOrganId(), 1, pageSize, pageNumber, true);
//            }
//
//
//        }
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
//        Object object = adapter.getList().get(position);
//
//        if (object instanceof HomeInfo) {
//
//            final HomeInfo homeInfo = (HomeInfo) object;
//
//            final PostOperationFragmentDialog dialog = new PostOperationFragmentDialog();
//
//            switch (itemType) {//类型((1帖子,2圈子,3活动,4房间,5PK挑战))
//
//                case 1:
//                    final PostInfo postInfo = homeInfo.getPostInfo();
//                    if (postInfo == null) {
//                        return;
//                    }
//
//                    //用户角色：圈主或者管理员是自己
//                    if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && postInfo.getBaseUser().getUserId() == userId) {
//                        dialog.setTextList("删除", "从本圈子删除", "置顶/取消置顶", "分享", "取消");//都有
//                    } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                        if (postInfo.getIsAnonymous() == 1) {//匿名           //从本圈子删除
//                            if (!StringUtils.isEmpty(title1)) {
//                                dialog.setTextList(title1, "举报", "从本圈子删除", "置顶/取消置顶", "分享", "取消");
//                            }
//                            if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                                dialog.setTextList(title1, title2, "举报", "从本圈子删除", "置顶/取消置顶", "分享", "取消");
//                            }
//
//                            if (StringUtils.isEmpty(title1)) {
//                                dialog.setTextList("举报", "从本圈子删除", "置顶/取消置顶", "分享", "取消");
//                            }
//                        } else {
//                            if (!StringUtils.isEmpty(title1)) {
//                                dialog.setTextList(title1, "举报", "从本圈子删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                            }
//                            if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                                dialog.setTextList(title1, title2, "举报", "从本圈子删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                            }
//
//                            if (StringUtils.isEmpty(title1)) {
//                                dialog.setTextList("举报", "从本圈子删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                            }
//                        }
//
//                    } else if (postInfo.getBaseUser().getUserId() == userId) {
//                        //自己的帖子
//                        dialog.setTextList("删除", "分享", "取消");
//                    } else {//别人的帖子
//                        if (postInfo.getIsAnonymous() == 1) {
//                            dialog.setTextList("举报", "分享", "取消");
//                        } else {
//                            dialog.setTextList("举报", "分享", "拉黑", "取消");
//                        }
//
//                    }
//
//                    reportParentId = postInfo.getPostId();
//                    reportType = 1;
//
//                    dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                        @Override
//                        public void onClick(int type) {
//
//                            //用户角色：圈主或者管理员是自己
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && postInfo.getBaseUser().getUserId() == userId) {
//
//                                switch (type) {
//
//                                    case 1://删除 帖子
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
//                                                getPostPresenter().deletePost(userId, postInfo.getPostId());
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 2://本圈子删除帖子
//
//                                        ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                                        dialog1.setText("", "确定删除帖子？", "取消", "确定");
//                                        dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//                                                getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), postInfo.getPostId(), 1);
//                                            }
//                                        });
//                                        dialog1.show(getSupportFragmentManager());
//
//                                        break;
//                                    case 3:
//                                        //是否置顶(0取消,1置顶)
//                                        getPostPresenter().postTop(organId, postInfo.getPostId(), 1, postInfo.getIsTop() == 0 ? 1 : 2);
//
//                                        break;
//
//                                    case 4://分享 帖子
//                                        String imgUrl = "";
//
//                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//                                        break;
//
//                                }
//                            } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//
//                                if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//
//                                    switch (type) {
//
//                                        case 1:// 提出圈子
//
//                                            ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                                            if (organizationInfo.getType() == 3) {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。", "取消", "踢出");
//                                            } else {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。您可到“圈子资料-圈子黑名单”中解除该限制。", "取消", "踢出");
//                                            }
//                                            dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), userId, postInfo.getBaseUser().getUserId(), 2);
//                                                }
//                                            });
//                                            dialog1.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2:// 禁言/解禁
//
//                                            ActiveLaunchSuccessTipDialog dialog2 = new ActiveLaunchSuccessTipDialog();
//                                            if (isGag == 1) {
//                                                dialog2.setText("", "确定对Ta禁言，禁止Ta发帖和发布评论。您可到圈子成员列表中解除禁言。", "取消", "禁言");
//                                            } else {
//                                                dialog2.setText("", "确定解除禁言？恢复Ta的发帖和发布评论功能。", "取消", "解禁");
//                                            }
//                                            dialog2.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, userId, postInfo.getBaseUser().getUserId());
//                                                }
//                                            });
//                                            dialog2.show(getSupportFragmentManager());
//
//
//                                            break;
//
//                                        case 3://举报帖子
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , userId
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 4://删除帖子
//
//                                            ActiveLaunchSuccessTipDialog dialog4 = new ActiveLaunchSuccessTipDialog();
//                                            dialog4.setText("", "确定删除帖子？", "取消", "确定");
//                                            dialog4.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), postInfo.getPostId(), 1);
//                                                }
//                                            });
//                                            dialog4.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 5://置顶/取消置顶 帖子
//                                            //是否置顶(0取消,1置顶)
//                                            getPostPresenter().postTop(organId, postInfo.getPostId(), 1, postInfo.getIsTop() == 0 ? 1 : 2);
//
//                                            break;
//
//                                        case 6://分享 帖子
//
//                                            String imgUrl = "";
//
//                                            if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                                imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//
//                                            ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//                                            break;
//
//                                        case 7://拉黑 发帖子的人
//
//                                            BaseUser baseUser = postInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                            }
//
//
//                                            break;
//
//                                    }
//
//                                } else if (!StringUtils.isEmpty(title1)) {
//
//                                    switch (type) {
//
//                                        case 1:// 提出圈子
//
//                                            ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                                            if (organizationInfo.getType() == 3) {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。", "取消", "踢出");
//                                            } else {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。您可到“圈子资料-圈子黑名单”中解除该限制。", "取消", "踢出");
//                                            }
//                                            dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), userId, postInfo.getBaseUser().getUserId(), 2);
//                                                }
//                                            });
//                                            dialog1.show(getSupportFragmentManager());
//
//
//                                            break;
//
//                                        case 2://举报帖子
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , userId
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 3://删除帖子
//
//                                            ActiveLaunchSuccessTipDialog dialog3 = new ActiveLaunchSuccessTipDialog();
//                                            dialog3.setText("", "确定删除帖子？", "取消", "确定");
//                                            dialog3.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), postInfo.getPostId(), 1);
//                                                }
//                                            });
//                                            dialog3.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 4://置顶/取消置顶 帖子
//                                            //是否置顶(0取消,1置顶)
//                                            getPostPresenter().postTop(organId, postInfo.getPostId(), 1, postInfo.getIsTop() == 0 ? 1 : 2);
//
//                                            break;
//
//                                        case 5://分享 帖子
//
//                                            String imgUrl = "";
//
//                                            if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                                imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//
//                                            ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//                                            break;
//
//                                        case 6://拉黑 发帖子的人
//
//                                            BaseUser baseUser = postInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                            }
//
//
//                                            break;
//
//                                    }
//
//                                } else {
//
//                                    switch (type) {
//
//                                        case 1://举报帖子
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , userId
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2://删除帖子
//
//                                            ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                                            dialog1.setText("", "确定删除帖子？", "取消", "确定");
//                                            dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), postInfo.getPostId(), 1);
//                                                }
//                                            });
//                                            dialog1.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 3://置顶/取消置顶 帖子
//                                            //是否置顶(0取消,1置顶)
//                                            getPostPresenter().postTop(organId, postInfo.getPostId(), 1, postInfo.getIsTop() == 0 ? 1 : 2);
//
//                                            break;
//
//                                        case 4://分享 帖子
//
//                                            String imgUrl = "";
//
//                                            if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                                imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//
//                                            ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//                                            break;
//
//                                        case 5://拉黑 发帖子的人
//
//                                            BaseUser baseUser = postInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                            }
//
//
//                                            break;
//
//                                    }
//
//                                }
//
//
//                            } else if (postInfo.getBaseUser().getUserId() == userId) {//自己的帖子
//
//                                switch (type) {
//
//                                    case 1://删除 帖子
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
//                                                getPostPresenter().deletePost(userId, postInfo.getPostId());
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 2://分享 帖子
//
//                                        String imgUrl = "";
//
//                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                }
//
//                            } else {//别人的帖子
//                                switch (type) {
//
//                                    case 1://举报 帖子
//                                        SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                        selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                            @Override
//                                            public void reportItemClick(Object object) {
//                                                if (object instanceof ReportType) {
//
//                                                    ReportType reportTypeObj = (ReportType) object;
//                                                    getCommonPresenter().reportOperation(reportParentId, reportType
//                                                            , userId
//                                                            , 0
//                                                            , reportTypeObj.getReportTypeId());
//                                                }
//                                            }
//                                        });
//                                        selectorReportContentDialog.show(getSupportFragmentManager());
//                                        break;
//
//                                    case 2://分享 帖子
//
//                                        String imgUrl = "";
//
//                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                    case 3://拉黑 发帖子的人
//
//                                        BaseUser baseUser = postInfo.getBaseUser();
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                        }
//
//                                        break;
//
//                                }
//                            }
//
//                        }
//                    });
//                    break;
//
//                case 3:// 活动
//                    final ActivityInfo activityInfo = homeInfo.getActivityInfo();
//                    if (activityInfo == null) {
//                        return;
//                    }
//
//
//                    //用户角色：圈主或者管理员是自己
//                    if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && activityInfo.getBaseUser().getUserId() == userId) {
//                        dialog.setTextList("从本圈子删除", "置顶/取消置顶", "分享", "取消");
//                    } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//                        if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                            dialog.setTextList(title1, title2, "举报", "删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                        } else {
//                            dialog.setTextList("举报", "从本圈子删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                        }
//
//
//                    } else if (activityInfo.getBaseUser().getUserId() == userId) {
//                        //自己的活动
//                        dialog.setTextList("从本圈子删除", "分享", "取消");
//                    } else {//别人的活动
//                        dialog.setTextList("举报", "分享", "拉黑", "取消");
//                    }
//
//                    reportParentId = activityInfo.getActivityId();
//                    reportType = 9;
//
//                    dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                        @Override
//                        public void onClick(int type) {
//
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && activityInfo.getBaseUser().getUserId() == userId) {
//
//                                switch (type) {
//
//                                    case 1://删除 活动
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
//                                                getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), activityInfo.getActivityId(), 3);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 2://置顶/取消置顶 活动
//                                        //是否置顶(0取消,1置顶)
//                                        getPostPresenter().postTop(organId, activityInfo.getActivityId(), 3, activityInfo.getIsTop() == 0 ? 1 : 2);
//
//                                        break;
//
//                                    case 3://分享 活动
//
//                                        String imgUrl = "";
//
//                                        if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                            imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                }
//
//                            } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//                                if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                                    switch (type) {
//
//                                        case 1:// 提出圈子
//
//                                            ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                                            if (organizationInfo.getType() == 3) {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。", "取消", "踢出");
//                                            } else {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。您可到“圈子资料-圈子黑名单”中解除该限制。", "取消", "踢出");
//                                            }
//                                            dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), userId, activityInfo.getBaseUser().getUserId(), 2);
//                                                }
//                                            });
//                                            dialog1.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2:// 禁言/解禁
//
//                                            ActiveLaunchSuccessTipDialog dialog2 = new ActiveLaunchSuccessTipDialog();
//                                            if (isGag == 1) {
//                                                dialog2.setText("", "确定对Ta禁言，禁止Ta发帖和发布评论。您可到圈子成员列表中解除禁言。", "取消", "禁言");
//                                            } else {
//                                                dialog2.setText("", "确定解除禁言？恢复Ta的发帖和发布评论功能。", "取消", "解禁");
//                                            }
//                                            dialog2.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, userId, activityInfo.getBaseUser().getUserId());
//                                                }
//                                            });
//                                            dialog2.show(getSupportFragmentManager());
//
//
//                                            break;
//
//                                        case 3://举报活动
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , userId
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 4://删除活动
//
//                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                            dialog.setText("", "确定删除活动？", "取消", "确定");
//                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//
//                                                    getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), activityInfo.getActivityId(), 3);
//
//                                                }
//                                            });
//                                            dialog.show(getSupportFragmentManager());
//
//
//                                            break;
//
//                                        case 5://置顶/取消置顶 活动
//                                            //是否置顶(0取消,1置顶)
//                                            getPostPresenter().postTop(organId, activityInfo.getActivityId(), 3, activityInfo.getIsTop() == 0 ? 1 : 2);
//
//                                            break;
//
//                                        case 6://分享 活动
//
//                                            String imgUrl = "";
//
//                                            if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                                imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//                                            ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//                                            break;
//
//                                        case 7://拉黑 发活动的人
//
//                                            BaseUser baseUser = activityInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                            }
//
//                                            break;
//
//                                    }
//                                } else {
//
//                                    switch (type) {
//
//                                        case 1://举报活动
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , userId
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2://删除活动
//
//                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                            dialog.setText("", "确定删除活动？", "取消", "确定");
//                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//
//                                                    getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), activityInfo.getActivityId(), 3);
//
//                                                }
//                                            });
//                                            dialog.show(getSupportFragmentManager());
//
//
//                                            break;
//
//                                        case 3://置顶/取消置顶 活动
//                                            //是否置顶(0取消,1置顶)
//                                            getPostPresenter().postTop(organId, activityInfo.getActivityId(), 3, activityInfo.getIsTop() == 0 ? 1 : 2);
//
//                                            break;
//
//                                        case 4://分享 活动
//
//                                            String imgUrl = "";
//
//                                            if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                                imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//                                            ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//                                            break;
//
//                                        case 5://拉黑 发活动的人
//
//                                            BaseUser baseUser = activityInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                            }
//
//                                            break;
//
//                                    }
//
//                                }
//
//
//                            } else if (activityInfo.getBaseUser().getUserId() == userId) {//自己发布的活动
//
//                                switch (type) {
//
//                                    case 1://删除 活动
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
//                                                getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), activityInfo.getActivityId(), 3);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 2://分享 活动
//
//                                        String imgUrl = "";
//
//                                        if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                            imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                }
//
//                            } else {//别人的活动
//                                switch (type) {
//
//                                    case 1://举报 活动
//                                        SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                        selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                            @Override
//                                            public void reportItemClick(Object object) {
//                                                if (object instanceof ReportType) {
//
//                                                    ReportType reportTypeObj = (ReportType) object;
//                                                    getCommonPresenter().reportOperation(reportParentId, reportType
//                                                            , userId
//                                                            , 0
//                                                            , reportTypeObj.getReportTypeId());
//                                                }
//                                            }
//                                        });
//                                        selectorReportContentDialog.show(getSupportFragmentManager());
//                                        break;
//
//                                    case 2://分享 活动
//
//                                        String imgUrl = "";
//
//                                        if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                            imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                    case 3://拉黑 发活动的人
//
//                                        BaseUser baseUser = activityInfo.getBaseUser();
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                        }
//                                        break;
//
//                                }
//                            }
//
//                        }
//                    });
//
//
//                    break;
//
//                case 5:// PK
//                    final PlayInfo playInfo = homeInfo.getPlayInfo();
//                    if (playInfo == null) {
//                        return;
//                    }
//
//                    //用户角色：圈主或者管理员是自己
//                    if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && playInfo.getBaseUser().getUserId() == userId) {
//                        dialog.setTextList("从本圈子删除", "置顶/取消置顶", "分享", "取消");
//                    } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                        if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                            dialog.setTextList(title1, title2, "举报", "删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//                        } else {
//                            dialog.setTextList("举报", "从本圈子删除", "置顶/取消置顶", "分享", "拉黑", "取消");
//
//                        }
//                    } else if (playInfo.getBaseUser().getUserId() == userId) {
//                        //自己的PK
//                        dialog.setTextList("从本圈子删除", "分享", "取消");
//                    } else {//别人的PK
//                        dialog.setTextList("举报", "分享", "拉黑", "取消");
//                    }
//
//                    reportParentId = playInfo.getPlayId();
//                    reportType = 10;
//
//                    dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                        @Override
//                        public void onClick(int type) {
//
//                            //用户角色：圈主或者管理员是自己
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && playInfo.getBaseUser().getUserId() == userId) {
//
//                                switch (type) {
//
//                                    case 1://删除 PK
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
//                                                getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), playInfo.getPlayId(), 5);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 2://置顶/取消置顶 活动
//                                        //是否置顶(0取消,1置顶)
//                                        getPostPresenter().postTop(organId, playInfo.getPlayId(), 5, playInfo.getIsTop() == 0 ? 1 : 2);
//
//                                        break;
//
//                                    case 3://分享 PK
//
//                                        String imgUrl = "";
//
//                                        if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                            imgUrl = playInfo.getVideoImageUrl();
//                                        }
//
//                                        playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                }
//
//
//                            } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//
//                                if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                                    switch (type) {
//
//                                        case 1:// 提出圈子
//
//                                            ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                                            if (organizationInfo.getType() == 3) {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。", "取消", "踢出");
//                                            } else {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。您可到“圈子资料-圈子黑名单”中解除该限制。", "取消", "踢出");
//                                            }
//                                            dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), userId, playInfo.getBaseUser().getUserId(), 2);
//                                                }
//                                            });
//                                            dialog1.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2:// 禁言/解禁
//
//                                            ActiveLaunchSuccessTipDialog dialog2 = new ActiveLaunchSuccessTipDialog();
//                                            if (isGag == 1) {
//                                                dialog2.setText("", "确定对Ta禁言，禁止Ta发帖和发布评论。您可到圈子成员列表中解除禁言。", "取消", "禁言");
//                                            } else {
//                                                dialog2.setText("", "确定解除禁言？恢复Ta的发帖和发布评论功能。", "取消", "解禁");
//                                            }
//                                            dialog2.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, userId, playInfo.getBaseUser().getUserId());
//                                                }
//                                            });
//                                            dialog2.show(getSupportFragmentManager());
//
//
//                                            break;
//
//                                        case 3://举报PK
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , userId
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 4://删除PK
//
//                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                            dialog.setText("", "确定删除活动？", "取消", "确定");
//                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//
//                                                    getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), playInfo.getPlayId(), 5);
//
//                                                }
//                                            });
//                                            dialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 5://置顶/取消置顶 活动
//                                            //是否置顶(0取消,1置顶)
//                                            getPostPresenter().postTop(organId, playInfo.getPlayId(), 5, playInfo.getIsTop() == 0 ? 1 : 2);
//
//                                            break;
//
//                                        case 6://分享 活动
//
//                                            String imgUrl = "";
//
//                                            if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                                imgUrl = playInfo.getVideoImageUrl();
//                                            }
//                                            playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                            ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                            break;
//
//                                        case 7://拉黑 发PK的人
//
//                                            BaseUser baseUser = playInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                            }
//                                            break;
//
//                                    }
//                                } else {
//                                    switch (type) {
//
//                                        case 1://举报PK
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , userId
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2://删除PK
//
//                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                            dialog.setText("", "确定删除活动？", "取消", "确定");
//                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//
//                                                    getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), playInfo.getPlayId(), 5);
//
//                                                }
//                                            });
//                                            dialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 3://置顶/取消置顶 活动
//                                            //是否置顶(0取消,1置顶)
//                                            getPostPresenter().postTop(organId, playInfo.getPlayId(), 5, playInfo.getIsTop() == 0 ? 1 : 2);
//
//                                            break;
//
//                                        case 4://分享 活动
//
//                                            String imgUrl = "";
//
//                                            if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                                imgUrl = playInfo.getVideoImageUrl();
//                                            }
//                                            playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                            ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                            break;
//
//                                        case 5://拉黑 发PK的人
//
//                                            BaseUser baseUser = playInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                            }
//                                            break;
//
//                                    }
//                                }
//
//
//                            } else if (playInfo.getBaseUser().getUserId() == userId) {// 自己的PK
//
//                                switch (type) {
//
//                                    case 1://删除 PK
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
//                                                getAssociationPresenter().deleteOrganContent(userId, organizationInfo.getOrganId(), playInfo.getPlayId(), 5);
//
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    case 2://分享 PK
//
//                                        String imgUrl = "";
//
//                                        if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                            imgUrl = playInfo.getVideoImageUrl();
//                                        }
//
//                                        playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                }
//
//                            } else {//别人的PK
//                                switch (type) {
//
//                                    case 1://举报 PK
//                                        SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                        selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                            @Override
//                                            public void reportItemClick(Object object) {
//                                                if (object instanceof ReportType) {
//
//                                                    ReportType reportTypeObj = (ReportType) object;
//                                                    getCommonPresenter().reportOperation(reportParentId, reportType
//                                                            , userId
//                                                            , 0
//                                                            , reportTypeObj.getReportTypeId());
//                                                }
//                                            }
//                                        });
//                                        selectorReportContentDialog.show(getSupportFragmentManager());
//                                        break;
//
//                                    case 2://分享 PK
//
//                                        String imgUrl = "";
//
//                                        if (!StringUtils.isEmpty(playInfo.getVideoImageUrl())) {
//                                            imgUrl = playInfo.getVideoImageUrl();
//                                        }
//
//                                        playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.PK, playInfo.getPlayId(), playInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                    case 3://拉黑 发PK的人
//
//                                        BaseUser baseUser = playInfo.getBaseUser();
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(userId, toBlacklistUserId);
//                                        }
//                                        break;
//
//                                }
//                            }
//
//                        }
//                    });
//
//                    break;
//
//                case 8:// 表白墙
//
//                    final SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                    if (sayLoveInfo == null) {
//                        return;
//                    }
//
//                    //用户角色：圈主或者管理员是自己
//                    if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && sayLoveInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        dialog.setTextList("从本圈子删除", "分享", "取消");
//                    } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                        if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                            dialog.setTextList(title1, title2, "举报", "从本圈子删除", "分享", "拉黑", "取消");
//                        } else {
//                            dialog.setTextList("举报", "从本圈子删除", "分享", "拉黑", "取消");
//                        }
//
//                    } else if (sayLoveInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        //自己的表白墙
//                        dialog.setTextList("从本圈子删除", "分享", "取消");
//                    } else {//别人的表白墙
//                        dialog.setTextList("举报", "分享", "拉黑", "取消");
//                    }
//
//                    reportParentId = sayLoveInfo.getSayLoveId();
//                    reportType = 1;
//
//                    dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                        @Override
//                        public void onClick(int type) {
//
//                            //用户角色：圈主或者管理员是自己
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && sayLoveInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//
//                                switch (type) {
//
//                                    case 1://删除 表白墙
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
//                                        break;
//
//                                    case 2://分享表白墙弹窗
//
//                                        String imgUrl = "";
//                                        if (sayLoveInfo.getConfessionType() == 2) {
//                                            imgUrl = sayLoveInfo.getVideoImageUrl();
//                                        } else {
//                                            if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                                imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//                                        break;
//
//                                }
//                            } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//
//                                if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//
//                                    switch (type) {
//
//                                        case 1:// 提出圈子
//
//                                            ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                                            if (organizationInfo.getType() == 3) {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。", "取消", "踢出");
//                                            } else {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。您可到“圈子资料-圈子黑名单”中解除该限制。", "取消", "踢出");
//                                            }
//                                            dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sayLoveInfo.getBaseUser().getUserId(), 2);
//                                                }
//                                            });
//                                            dialog1.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2:// 禁言/解禁
//
//                                            ActiveLaunchSuccessTipDialog dialog2 = new ActiveLaunchSuccessTipDialog();
//                                            if (isGag == 1) {
//                                                dialog2.setText("", "确定对Ta禁言，禁止Ta发帖和发布评论。您可到圈子成员列表中解除禁言。", "取消", "禁言");
//                                            } else {
//                                                dialog2.setText("", "确定解除禁言？恢复Ta的发帖和发布评论功能。", "取消", "解禁");
//                                            }
//                                            dialog2.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sayLoveInfo.getBaseUser().getUserId());
//                                                }
//                                            });
//                                            dialog2.show(getSupportFragmentManager());
//
//
//                                            break;
//
//                                        case 3://举报帖子
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 4://删除表白
//
//                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                            dialog.setText("", "确定删除表白？", "取消", "确定");
//                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//
//                                                    getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), sayLoveInfo.getSayLoveId(), 8);
//
//                                                }
//                                            });
//                                            dialog.show(getSupportFragmentManager());
//
//
//                                            break;
//
//                                        case 5://分享 帖子
//
//                                            String imgUrl = "";
//                                            if (sayLoveInfo.getConfessionType() == 2) {
//                                                imgUrl = sayLoveInfo.getVideoImageUrl();
//                                            } else {
//                                                if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                                    imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                                }
//                                            }
//                                            ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//                                            break;
//
//                                        case 6://拉黑 发帖子的人
//
//                                            BaseUser baseUser = sayLoveInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                            }
//
//
//                                            break;
//
//                                    }
//
//                                } else {
//
//                                    switch (type) {
//
//                                        case 1://举报帖子
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2://删除表白
//
//                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                            dialog.setText("", "确定删除表白？", "取消", "确定");
//                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//
//                                                    getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), sayLoveInfo.getSayLoveId(), 8);
//
//                                                }
//                                            });
//                                            dialog.show(getSupportFragmentManager());
//
//
//                                            break;
//
//
//                                        case 3://分享 帖子
//
//                                            String imgUrl = "";
//                                            if (sayLoveInfo.getConfessionType() == 2) {
//                                                imgUrl = sayLoveInfo.getVideoImageUrl();
//                                            } else {
//                                                if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                                    imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                                }
//                                            }
//                                            ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//
//                                            break;
//
//                                        case 4://拉黑 发帖子的人
//
//                                            BaseUser baseUser = sayLoveInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                            }
//
//
//                                            break;
//
//                                    }
//
//                                }
//
//
//                            } else if (sayLoveInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {//自己的表白墙
//
//                                switch (type) {
//
//                                    case 1://删除 表白
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
//                                        break;
//
//                                    case 2://分享 表白墙
//
//                                        String imgUrl = "";
//                                        if (sayLoveInfo.getConfessionType() == 2) {
//                                            imgUrl = sayLoveInfo.getVideoImageUrl();
//                                        } else {
//                                            if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                                imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                }
//
//                            } else {//别人的表白墙
//                                switch (type) {
//
//                                    case 1://举报 表白墙
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
//                                        break;
//
//                                    case 2://分享 帖子
//
//                                        String imgUrl = "";
//                                        if (sayLoveInfo.getConfessionType() == 2) {
//                                            imgUrl = sayLoveInfo.getVideoImageUrl();
//                                        } else {
//                                            if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                                                imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                                            }
//                                        }
//                                        ShareUtil.getInstance().showShare(AssociationDetailActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//
//                                        break;
//
//                                    case 3://拉黑 发帖子的人
//
//                                        BaseUser baseUser = sayLoveInfo.getBaseUser();
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//
//                                        break;
//
//                                }
//                            }
//
//                        }
//                    });
//
//
//                    break;
//
//                case 9:// 同学帮帮忙
//
//                    final HelpInfo helpInfo = homeInfo.getHelpInfo();
//                    if (helpInfo == null) {
//                        return;
//                    }
//
//                    //用户角色：圈主或者管理员是自己
//                    if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && helpInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        dialog.setTextList("从本圈子删除", "取消");
//                    } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                        if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                            dialog.setTextList(title1, title2, "举报", "删除", "拉黑", "取消");
//                        } else {
//                            dialog.setTextList("举报", "从本圈子删除", "拉黑", "取消");
//
//                        }
//                    } else if (helpInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        //自己的帮帮忙
//                        dialog.setTextList("从本圈子删除", "取消");
//                    } else {//别人的帮帮忙
//                        dialog.setTextList("举报", "拉黑", "取消");
//                    }
//
//                    reportParentId = helpInfo.getHelpId();
//                    reportType = 10;
//
//                    dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                        @Override
//                        public void onClick(int type) {
//
//                            //用户角色：圈主或者管理员是自己
//                            if ((organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) && helpInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//
//                                switch (type) {
//
//                                    case 1://删除 帮帮忙
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
//                                }
//
//
//                            } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2) {//用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//
//
//                                if (!StringUtils.isEmpty(title1) && !StringUtils.isEmpty(title2)) {
//                                    switch (type) {
//
//                                        case 1:// 提出圈子
//
//                                            ActiveLaunchSuccessTipDialog dialog1 = new ActiveLaunchSuccessTipDialog();
//                                            if (organizationInfo.getType() == 3) {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。", "取消", "踢出");
//                                            } else {
//                                                dialog1.setText("", "踢出该圈友，Ta将不能再加入本圈子。您可到“圈子资料-圈子黑名单”中解除该限制。", "取消", "踢出");
//                                            }
//                                            dialog1.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().updateOrganUserStatus(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), helpInfo.getBaseUser().getUserId(), 2);
//                                                }
//                                            });
//                                            dialog1.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2:// 禁言/解禁
//
//                                            ActiveLaunchSuccessTipDialog dialog2 = new ActiveLaunchSuccessTipDialog();
//                                            if (isGag == 1) {
//                                                dialog2.setText("", "确定对Ta禁言，禁止Ta发帖和发布评论。您可到圈子成员列表中解除禁言。", "取消", "禁言");
//                                            } else {
//                                                dialog2.setText("", "确定解除禁言？恢复Ta的发帖和发布评论功能。", "取消", "解禁");
//                                            }
//                                            dialog2.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//                                                    getAssociationPresenter().organBanningComments(organizationInfo.getOrganId(), isGag == 1 ? 2 : 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), helpInfo.getBaseUser().getUserId());
//                                                }
//                                            });
//                                            dialog2.show(getSupportFragmentManager());
//
//
//                                            break;
//
//                                        case 3://举报帮帮忙
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 4://删除帮帮忙
//
//                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                            dialog.setText("", "确定删除同学帮帮忙？", "取消", "确定");
//                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//
//                                                    getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), helpInfo.getHelpId(), 9);
//
//                                                }
//                                            });
//                                            dialog.show(getSupportFragmentManager());
//
//                                            break;
//
//
//                                        case 5://拉黑 发同学帮帮忙的人
//
//                                            BaseUser baseUser = helpInfo.getBaseUser();
//                                            if (baseUser != null) {
//                                                toBlacklistUserId = baseUser.getUserId();
//                                                getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                            }
//                                            break;
//
//                                    }
//                                } else {
//                                    switch (type) {
//
//                                        case 1://举报同学帮帮忙
//
//                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                @Override
//                                                public void reportItemClick(Object object) {
//                                                    if (object instanceof ReportType) {
//
//                                                        ReportType reportTypeObj = (ReportType) object;
//                                                        getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
//                                                                , 0
//                                                                , reportTypeObj.getReportTypeId());
//                                                    }
//                                                }
//                                            });
//                                            selectorReportContentDialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 2://删除帮帮忙
//
//                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                            dialog.setText("", "确定删除同学帮帮忙？", "取消", "确定");
//                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                @Override
//                                                public void onLeftClick() {
//                                                    //取消
//                                                }
//
//                                                @Override
//                                                public void onRightClick() {
//
//                                                    getAssociationPresenter().deleteOrganContent(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), helpInfo.getHelpId(), 9);
//
//                                                }
//                                            });
//                                            dialog.show(getSupportFragmentManager());
//
//                                            break;
//
//                                        case 3://拉黑 发同学帮帮忙的人
//
//                                            BaseUser userInfo = helpInfo.getBaseUser();
//                                            if (userInfo != null) {
//                                                toBlacklistUserId = userInfo.getUserId();
//                                                getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                            }
//                                            break;
//
//                                    }
//                                }
//
//
//                            } else if (helpInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {// 自己的帮帮忙
//
//                                switch (type) {
//
//                                    case 1://删除 同学帮帮忙
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
//                                }
//
//                            } else {//别人的同学帮帮忙
//                                switch (type) {
//
//                                    case 1://举报 同学帮帮忙
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
//                                        break;
//
//                                    case 2://拉黑 发同学帮帮忙的人
//
//                                        BaseUser userInfo = helpInfo.getBaseUser();
//                                        if (userInfo != null) {
//                                            toBlacklistUserId = userInfo.getUserId();
//                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
//                                        }
//                                        break;
//
//                                }
//                            }
//
//                        }
//                    });
//
//                    break;
//
//
//            }
//
//            dialog.show(getSupportFragmentManager());
//        }
//
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void callBackEvent(UpdatePostListEvent event) {
//
//        if (event.getPosition() > -1) {
//
//            if (adapter.getItemCount() > event.getPosition()) {
//
//                Object object = adapter.getList().get(event.getPosition());
//                if (object instanceof HomeInfo) {
//                    HomeInfo homeInfo = (HomeInfo) object;
//
//                    switch (homeInfo.getType()) {
//                        case 1:
//                            if (event.getType() == 1) {// 更新评论数
//
//                                PostInfo postInfo = homeInfo.getPostInfo();
//                                if (postInfo != null) {
//                                    postInfo.setCommentCount(event.getCommentNum());
//                                    adapter.notifyItemChanged(event.getPosition(), true);
//                                }
//
//                            } else if (event.getType() == 2) {// 更新点赞数
//
//                                PostInfo postInfo = homeInfo.getPostInfo();
//                                if (postInfo != null) {
//                                    postInfo.setIsPraise(event.getIsPraise());
//                                    postInfo.setPraiseCount(homeInfo.getPostInfo().getPraiseCount() + (event.getIsPraise() == 1 ? 1 : -1));
//                                    adapter.notifyItemChanged(event.getPosition(), true);
//                                }
//
//                            } else if (event.getType() == 5) { // 删除帖子
//
//                                if (event.getPosition() > -1 && adapter.getItemCount() > event.getPosition()) {
//                                    adapter.getList().remove(event.getPosition());
//                                    adapter.notifyItemRemoved(event.getPosition());
//                                    adapter.notifyItemRangeChanged(event.getPosition(), adapter.getItemCount() - event.getPosition());
//                                }
//
//                                if (!isHasHomeInfo()) {
//                                    // 设置空数据
//                                    AssociationDetailRecyListItemModel model = new AssociationDetailRecyListItemModel();
//                                    model.setType(102);
//                                    adapter.setEmptyViewData(model);
//                                }
//
//                            }
//
//                            break;
//
//                    }
//
//                }
//
//            }
//
//
//        }
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void callBackEvent(UpdateSayloveListEvent event) {
//
//        if (event.getPosition() > -1) {
//
//            if (adapter.getItemCount() > event.getPosition()) {
//
//                Object object = adapter.getList().get(event.getPosition());
//                if (object instanceof HomeInfo) {
//                    HomeInfo homeInfo = (HomeInfo) object;
//
//                    switch (homeInfo.getType()) {
//
//                        case 8:
//
//                            // 1:更新评论 2：更新点赞 3：删除表白 4:发布表白
//                            if (event.getType() == 1) {// 更新评论数
//
//                                SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                                if (sayLoveInfo != null) {
//                                    sayLoveInfo.setCommentCount(event.getCommentNum());
//                                    adapter.notifyItemChanged(event.getPosition(), true);
//                                }
//
//                            } else if (event.getType() == 2) {// 更新点赞数
//
//                                SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                                if (sayLoveInfo != null) {
//                                    sayLoveInfo.setIsPraise(event.getIsPraise());
//                                    sayLoveInfo.setPraiseCount(sayLoveInfo.getPraiseCount() + (event.getIsPraise() == 1 ? 1 : -1));
//                                    adapter.notifyItemChanged(event.getPosition(), true);
//                                }
//
//                            } else if (event.getType() == 3) { // 删除表白墙
//
//                                if (event.getPosition() > -1 && adapter.getItemCount() > event.getPosition()) {
//                                    adapter.getList().remove(event.getPosition());
//                                    adapter.notifyItemRemoved(event.getPosition());
//                                    adapter.notifyItemRangeChanged(event.getPosition(), adapter.getItemCount() - event.getPosition());
//                                }
//
//                                if (!isHasHomeInfo()) {
//                                    // 设置空数据
//                                    AssociationDetailRecyListItemModel model = new AssociationDetailRecyListItemModel();
//                                    model.setType(102);
//                                    adapter.setEmptyViewData(model);
//                                }
//
//                            }
//
//                            break;
//
//                    }
//
//                }
//
//            }
//
//
//        }
//
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void callBackEvent(ClassMateHelpEvent event) {
//
//        if (event.getPosition() > -1) {
//
//            if (adapter.getItemCount() > event.getPosition()) {
//
//                Object object = adapter.getList().get(event.getPosition());
//                if (object instanceof HomeInfo) {
//                    HomeInfo homeInfo = (HomeInfo) object;
//
//                    switch (homeInfo.getType()) {
//
//                        case 9:
//
//                            //1:刷新 2：更新点赞 5:删除
//                            if (event.getType() == 1) {// 更新评论数
//
//                                HelpInfo helpInfo = homeInfo.getHelpInfo();
//                                if (helpInfo != null) {
//                                    helpInfo.setCommentCount(helpInfo.getCommentCount() + 1);
//                                    adapter.notifyItemChanged(event.getPosition(), true);
//                                }
//
//                            } else if (event.getType() == 2) {// 更新点赞数
//
//                                HelpInfo helpInfo = homeInfo.getHelpInfo();
//                                if (helpInfo != null) {
//                                    helpInfo.setIsPraise(event.getIsPraise());
//                                    helpInfo.setPraiseCount(helpInfo.getPraiseCount() + (event.getIsPraise() == 1 ? 1 : -1));
//                                    adapter.notifyItemChanged(event.getPosition(), true);
//                                }
//
//                            } else if (event.getType() == 5) { // 删除同学帮帮忙
//
//                                if (event.getPosition() > -1 && adapter.getItemCount() > event.getPosition()) {
//                                    adapter.getList().remove(event.getPosition());
//                                    adapter.notifyItemRemoved(event.getPosition());
//                                    adapter.notifyItemRangeChanged(event.getPosition(), adapter.getItemCount() - event.getPosition());
//                                }
//
//                                if (!isHasHomeInfo()) {
//                                    // 设置空数据
//                                    AssociationDetailRecyListItemModel model = new AssociationDetailRecyListItemModel();
//                                    model.setType(102);
//                                    adapter.setEmptyViewData(model);
//                                }
//                            }
//
//                            break;
//
//                    }
//
//                }
//
//            }
//
//
//        }
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventCallback(AssociationInfoEditorEvent event) {
//
//        if (event == null) {
//            return;
//        }
//
//        // 解散当前圈子、退出当前圈子
//        if (event.getOrganId() == organizationInfo.getOrganId()) {
//            finish();
//        }
//
//        isAssociationInfoEditorEvent = true;
//
//        if (event.getType() == 5) {
//            isAssociationInfoEditorEvent = false;
//        } else if (event.getType() == 3) {
//            adapter.getOrganizationInfo().getChatInfo().setIsLock(0);
//            adapter.notifyItemChanged(0);
//        } else if (event.getType() == 4) {
//            adapter.getOrganizationInfo().getChatInfo().setIsLock(1);
//            adapter.notifyItemChanged(0);
//        }
//
//
//        long organId = organizationInfo.getOrganId();
//        getAssociationPresenter().findOrganizationDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organId);
//
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventCallback(final PublishObjectEvent event) {
//        if (event == null) {
//            return;
//        }
//
//
//        if (event.getObject() instanceof HomeInfo) {
//            HomeInfo homeInfo = (HomeInfo) event.getObject();
//            if (adapter != null) {
//                adapter.addItem(homeInfo);
//            }
//
//        }
//
//    }
//
//
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//
//        if (organizationInfo != null) {
//            organizationInfo = null;
//        }
//
//        if (mRecyclerView != null) {
//            mRecyclerView.removeAllViews();
//            mRecyclerView = null;
//        }
//
//        if (adapter != null) {
//            adapter = null;
//        }
//
//        if (associationPresenter != null) {
//            associationPresenter.onDestroy();
//        }
//        associationPresenter = null;
//
//
//        if (getRecommendListPresenter != null) {
//            getRecommendListPresenter.onDestroy();
//        }
//        getRecommendListPresenter = null;
//
//
//        if (titleFragment != null) {
//            titleFragment = null;
//        }
//
//        if (postCommentListPresenter != null) {
//            postCommentListPresenter.onDestroy();
//        }
//        postCommentListPresenter = null;
//
//        if (mCommentListPresenter != null) {
//            mCommentListPresenter.onDestroy();
//        }
//        mCommentListPresenter = null;
//
//
//        if (groupPresenter != null) {
//            groupPresenter.onDestroy();
//        }
//        groupPresenter = null;
//
//
//        if (refreshLayout != null) {
//            refreshLayout.removeAllViews();
//        }
//        refreshLayout = null;
//
//        if (commonPresenter != null) {
//            commonPresenter.onDestroy();
//        }
//        commonPresenter = null;
//
//        if (postPresenter != null) {
//            postPresenter.onDestroy();
//        }
//        postPresenter = null;
//
//        if (userPresenter != null) {
//            userPresenter.onDestroy();
//        }
//        userPresenter = null;
//
//        if (classMateHelpPresenter != null) {
//            classMateHelpPresenter.onDestroy();
//        }
//        classMateHelpPresenter = null;
//
//        if (publishView != null) {
//            publishView = null;
//        }
//
//        if (tabTopImageView != null) {
//            tabTopImageView = null;
//        }
//
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
//        handler = null;
//
//        if (viewPager != null) {
//            viewPager.removeAllViews();
//        }
//        viewPager = null;
//
//
//    }
//}
