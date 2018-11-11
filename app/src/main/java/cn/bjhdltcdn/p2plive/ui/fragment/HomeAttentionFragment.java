package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindRoomDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAttentionListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SayLovePraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateUserStatusResponse;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetAttentionListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PkPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ApplyClerkActivity;
import cn.bjhdltcdn.p2plive.ui.activity.GroupVideoActivity;
import cn.bjhdltcdn.p2plive.ui.activity.OpenRoomSettingPassWordActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PostDetailActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeAttentionRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * Created by Hu_PC on 2017/11/8.
 * 首页关注
 */

public class HomeAttentionFragment extends BaseFragment implements BaseView {
    private View rootView;
    private AppCompatActivity mActivity;
    private GetAttentionListPresenter getAttentionListPresenter;
    private PostCommentListPresenter postCommentListPresenter;
    private GetCommentListPresenter getCommentListPresenter;
    private GetStoreListPresenter getStoreListPresenter;
    private ChatRoomPresenter chatRoomPresenter;
    private PkPresenter pkPresenter;
    private RecyclerView recycleView;
    private HomeAttentionRecyclerViewAdapter recyclerAdapter;
    private View emptyView;
    // 下拉刷新
    private TwinklingRefreshLayout refreshLayout;
    private int pageSize = 10, pageNum = 1;
    private TextView finishView;
    private LoadingView loadingView;
    private long userId;
    private int joinType;
    private int currentPosition;
    private TextView empty_tv;
    private String version = "1.0";
    private UserPresenter userPresenter;

    private GetAttentionListPresenter getAttentionListPresenter() {
        if (getAttentionListPresenter == null) {
            getAttentionListPresenter = new GetAttentionListPresenter(this);
        }
        return getAttentionListPresenter;
    }


    public PkPresenter getPkPresenter() {
        if (pkPresenter == null) {
            pkPresenter = new PkPresenter(this);
        }
        return pkPresenter;
    }

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_attention, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        initView();
        postCommentListPresenter = new PostCommentListPresenter(this);
        getCommentListPresenter = new GetCommentListPresenter(this);
        getAttentionListPresenter = new GetAttentionListPresenter(this);
        chatRoomPresenter = new ChatRoomPresenter(this);
        getAttentionListPresenter.getAttentionList(userId, pageSize, pageNum, version);
    }

    @Override
    protected void onVisible(boolean isInit) {

        Logger.d("isInit ==== " + isInit);

//        if (isInit) {
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        pageNum = 1;
//        getAttentionListPresenter().getAttentionList(userId, pageSize, pageNum, version);
//        //清除首页关注小红点
//        EventBus.getDefault().post(new UpdateHomeNewEvent(1));
//        }

    }


    private void initView() {
        emptyView = rootView.findViewById(R.id.empty_view);
        empty_tv = rootView.findViewById(R.id.empty_textView);
        refreshLayout = (TwinklingRefreshLayout) rootView.findViewById(R.id.refresh_layout_view);
        recycleView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        finishView = (TextView) rootView.findViewById(R.id.finish_view);

        recyclerAdapter = new HomeAttentionRecyclerViewAdapter(mActivity);
        recycleView.setHasFixedSize(true);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(11));
        recycleView.setLayoutManager(linearlayoutManager);
        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
        recycleView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOneToOneCharOnClick(new HomeAttentionRecyclerViewAdapter.OneToOneCharOnClick() {
            @Override
            public void onClick(long toUserId) {
                getUserPresenter().getAnonymityUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toUserId);
            }
        });
        recyclerAdapter.setOnClick(new HomeAttentionRecyclerViewAdapter.OnClick() {
            @Override
            public void onPraise(long postId, int type, int position, int operationType) {
                //点赞/取消点赞
                currentPosition = position;
                if (operationType == 1) {
                    //帖子
                    postCommentListPresenter.postPraise(userId, postId, type);
                } else if (operationType == 2) {
                    //表白
                    getCommentListPresenter.sayLovePraise(postId, type, userId);
                }

            }

            @Override
            public void onOrgain(long orgainId) {
                //点击帖子的圈子 跳到圈子详情页

            }

            @Override
            public void onDelete(long postId, int position) {
                //删除帖子
            }

            @Override
            public void onDeleteShare(long shareId, int position) {
                //删除分享
            }

            @Override
            public void onJoinClick(int position, final int type) {
                final long organId = recyclerAdapter.getItem(position).getOrganizationInfo().getOrganId();
                joinType = type;
                currentPosition = position;
                if (type == 2) {
                    //申请加入圈子
                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                    dialog.setText("", "本圈子为私密圈子，需要先发送申请，管理员同意后才能加入，发送？", "取消", "发送");
                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                        @Override
                        public void onLeftClick() {
                            //取消
                        }

                        @Override
                        public void onRightClick() {
                            //申请
                        }
                    });
                    dialog.show(mActivity.getSupportFragmentManager());
                } else {
                    //直接加入圈子
                }
            }

            @Override
            public void onApplyClerk(long storeId) {
                getStoreListPresenter().findStoreDetail(userId,storeId);
            }
        });
        recyclerAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                HomeInfo homeInfo = recyclerAdapter.getItem(position);
                switch (homeInfo.getType()) {
                    case 1:
                    case 11:
                        //1帖子详情页
                        if (homeInfo.getPostInfo() != null) {
                            if (homeInfo.getPostInfo().getContentLimit() == 2) {
                                return;
                            }
                            Intent intent = new Intent(mActivity, PostDetailActivity.class);
                            intent.putExtra(Constants.KEY.KEY_OBJECT, homeInfo.getPostInfo());
                            intent.putExtra(Constants.KEY.KEY_POSITION, position);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                    case 12:
                        //2圈子
                        if (homeInfo.getOrganizationInfo() != null) {
                        }
                        break;
                    case 3:
                    case 13:
                        //3活动详情页
                        break;
                    case 4:
                    case 14:
                        //4进入房间
                        RoomInfo roomInfo = homeInfo.getRoomInfo();
                        if (roomInfo != null) {
                            if (roomInfo.getUserRole() == 1 || roomInfo.getPasswordType() == 0) {
                                //不加密
                                chatRoomPresenter.updateUserStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomInfo.getRoomId(), 0, roomInfo.getPasswordType(),
                                        roomInfo.getPassword());
                            } else if (roomInfo.getPasswordType() == 1) {
                                //加密
                                chatRoomPresenter.findRoomDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomInfo.getRoomId());
                            }
                        }
                        break;
                    case 5:
                    case 15:
                        //5进入pk播放视频界面
                        if (homeInfo.getPlayInfo() != null) {
                            getPkPresenter().findPlayDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), homeInfo.getPlayInfo().getPlayId());
                        }
                        break;
                    case 8:
                    case 18:
                        //8进入表白详情
                        break;
                    default:
                        break;
                }
            }
        });

        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        loadingView = new LoadingView(mActivity);
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recycleView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                getAttentionListPresenter().getAttentionList(userId, pageSize, pageNum, version);
                //清除首页关注小红点
//                EventBus.getDefault().post(new UpdateHomeNewEvent(1));
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                if (!loadingView.isOnLoadFinish()) {
                    getAttentionListPresenter().getAttentionList(userId, pageSize, pageNum, version);
                }
            }
        });
        emptyView.setVisibility(View.GONE);

        final ImageView tabTopImageView = rootView.findViewById(R.id.tab_top);
        tabTopImageView.setVisibility(View.GONE);
        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerAdapter.setRefreshItem(false);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    tabTopImageView.setVisibility(View.VISIBLE);
                } else {
                    tabTopImageView.setVisibility(View.GONE);
                }
            }
        });
        tabTopImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleView.smoothScrollToPosition(0);
//                listView.setSelectionAfterHeaderView();
//                listView.smoothScrollToPosition(0);
            }
        });
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            if (refreshLayout != null) {
                if (pageNum == 1) {
                    refreshLayout.finishRefreshing();
                } else {
                    refreshLayout.finishLoadmore();
                }
            }
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_GETATTENTIONLIST)) {
            if (object instanceof GetAttentionListResponse) {
                if (pageNum == 1) {
                    refreshLayout.finishRefreshing();
                } else {
                    refreshLayout.finishLoadmore();
                }
                GetAttentionListResponse getAttentionListResponse = (GetAttentionListResponse) object;
                if (getAttentionListResponse.getCode() == 200) {
                    List<HomeInfo> homeInfoList = getAttentionListResponse.getList();
                    if (homeInfoList != null) {
                        if (pageNum == 1) {
                            recyclerAdapter.setList(homeInfoList, getAttentionListResponse.getDefaultImg());
                        } else {
                            recyclerAdapter.addList(homeInfoList);
                        }
                        recyclerAdapter.notifyDataSetChanged();
                        if (getAttentionListResponse.getTotal() <= pageNum * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadmore(false);
                            finishView.setVisibility(View.VISIBLE);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadmore(true);
                            finishView.setVisibility(View.GONE);
                            pageNum++;
                        }

                        if (getAttentionListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            finishView.setVisibility(View.GONE);
                            empty_tv.setText(getAttentionListResponse.getBlankHint());
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Utils.showToastShortTime(getAttentionListResponse.getMsg());
                }
            }
        } else if (InterfaceUrl.URL_JOINORGANIZATION.equals(apiName)) {

            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.getCode() == 200) {
                    Utils.showToastShortTime(response.getMsg());
                    if (joinType == 1) {
                        //加入成功
                        recyclerAdapter.getList().get(currentPosition).getOrganizationInfo().setMyUserRole(3);
                    } else {
                        //申请成功
                        recyclerAdapter.getList().get(currentPosition).getOrganizationInfo().setMyUserRole(4);
                    }
                    recyclerAdapter.notifyItemChanged(currentPosition);

                } else {
                    Utils.showToastShortTime(response.getMsg());
                }

            }

        } else if (apiName.equals(InterfaceUrl.URL_POSTPRAISE)) {
            if (object instanceof PostPraiseResponse) {
                PostPraiseResponse postPraiseResponse = (PostPraiseResponse) object;
                if (postPraiseResponse.getCode() == 200) {
                    Utils.showToastShortTime(postPraiseResponse.getMsg());
                    int praiseCount = recyclerAdapter.getItem(currentPosition).getPostInfo().getPraiseCount();
                    if (postPraiseResponse.getIsPraise() == 1) {
                        //点赞
                        recyclerAdapter.getItem(currentPosition).getPostInfo().setIsPraise(1);
                        recyclerAdapter.getItem(currentPosition).getPostInfo().setPraiseCount(praiseCount + 1);
                        recyclerAdapter.setRefreshItem(true);
                        recyclerAdapter.notifyDataSetChanged();
                    } else if (postPraiseResponse.getIsPraise() == 2) {
                        //取消点赞
                        recyclerAdapter.getItem(currentPosition).getPostInfo().setIsPraise(0);
                        recyclerAdapter.getItem(currentPosition).getPostInfo().setPraiseCount(praiseCount - 1);
                        recyclerAdapter.setRefreshItem(true);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                } else {
                    Utils.showToastShortTime(postPraiseResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_SAYLOVEPRAISE)) {
            if (object instanceof SayLovePraiseResponse) {
                SayLovePraiseResponse sayLovePraiseResponse = (SayLovePraiseResponse) object;
                if (sayLovePraiseResponse.getCode() == 200) {
                    Utils.showToastShortTime(sayLovePraiseResponse.getMsg());
                    int praiseCount = recyclerAdapter.getItem(currentPosition).getSayLoveInfo().getPraiseCount();
                    if (sayLovePraiseResponse.getIsPraise() == 1) {
                        //点赞
                        recyclerAdapter.getItem(currentPosition).getSayLoveInfo().setIsPraise(1);
                        recyclerAdapter.getItem(currentPosition).getSayLoveInfo().setPraiseCount(praiseCount + 1);
                        recyclerAdapter.setRefreshItem(true);
                        recyclerAdapter.notifyDataSetChanged();
                    } else if (sayLovePraiseResponse.getIsPraise() == 2) {
                        //取消点赞
                        recyclerAdapter.getItem(currentPosition).getSayLoveInfo().setIsPraise(0);
                        recyclerAdapter.getItem(currentPosition).getSayLoveInfo().setPraiseCount(praiseCount - 1);
                        recyclerAdapter.setRefreshItem(true);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                } else {
                    Utils.showToastShortTime(sayLovePraiseResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_UPDATEUSERSTATUS)) {
            if (object instanceof UpdateUserStatusResponse) {
                UpdateUserStatusResponse response = (UpdateUserStatusResponse) object;
                if (response.getCode() == 200) {
                    Intent intent = new Intent(mActivity, GroupVideoActivity.class);
                    intent.putExtra(Constants.Fields.ROOMINFO, response.getRoomInfo());
                    startActivity(intent);
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_FINDROOMDETAIL)) {
            if (object instanceof FindRoomDetailResponse) {
                FindRoomDetailResponse findRoomDetailResponse = (FindRoomDetailResponse) object;
                if (findRoomDetailResponse.getCode() == 200) {
                    RoomInfo roomInfo = findRoomDetailResponse.getRoomInfo();
                    if (roomInfo.getStatus() == 0) {
                        Intent intent = new Intent(mActivity, OpenRoomSettingPassWordActivity.class);
                        intent.putExtra(Constants.Fields.TYPE, 2);
                        intent.putExtra(Constants.Fields.ROOMINFO, roomInfo);
                        startActivity(intent);
                    } else {
                        Utils.showToastShortTime("该聊天频道已关闭");
                    }
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_FINDSTOREDETAIL)) {
            if (object instanceof FindStoreDetailResponse) {
                FindStoreDetailResponse findStoreDetailResponse = (FindStoreDetailResponse) object;
                if (findStoreDetailResponse.getCode() == 200) {
                    StoreDetail storeDetail = findStoreDetailResponse.getStoreDetail();
                    if (storeDetail != null) {
                        int isClert = storeDetail.getIsClert();//当前是否是店员(1店主,2店员,3普通用户,4店员申请中),
                        StoreInfo storeInfo=storeDetail.getStoreInfo();
                        int isPublish=storeInfo.getIsPublish();
                        if(isPublish==1){
                            if (isClert == 1) {
                                //店长自己不跳转
                            }else if (isClert == 2) {
                                Utils.showToastShortTime("您已成为该店店员");
                            } else if (isClert == 3) {
                                //跳到店员申请界面
                                Intent intent = new Intent(mActivity, ApplyClerkActivity.class);
                                intent.putExtra(Constants.Fields.STORE_ID, storeInfo.getStoreId());
                                mActivity.startActivity(intent);
                            } else if (isClert == 4) {
                                //店员申请中
                                Utils.showToastShortTime("店员申请中");
                            }
                        }else{
                            Utils.showToastShortTime("该店已关闭招聘信息");
                        }
                    }
                } else {
                    Utils.showToastShortTime(findStoreDetailResponse.getMsg());
                }
            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getAttentionListPresenter != null) {
            getAttentionListPresenter.onDestroy();
        }
        getAttentionListPresenter = null;
    }


}
