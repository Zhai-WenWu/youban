//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.FindFragmentRefreshEvent;
//import cn.bjhdltcdn.p2plive.event.FindListEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHomeBannerListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHomeNewCountResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetItemFollowListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetRoomListResponse;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.RecommendInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.FollowPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetHomeNearbyPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PublishActivity;
//import cn.bjhdltcdn.p2plive.ui.adapter.FindListAdapter;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * Created by Hu_PC on 2017/11/8.
// * 发现
// */
//
//public class FindFragment extends BaseFragment implements BaseView {
//
//    private View rootView;
//    private RecyclerView recycler_find;
//    private DiscoverPresenter discoverPresenter;
//    private int pageSize = 18, pageNumber = 1;
//    private FindListAdapter findListAdapter;
//    private TwinklingRefreshLayout refreshLayout;
//    private int sort = 2;
//    private int firstVisibleItemPosition;
//    private GetRecommendListPresenter getRecommendListPresenter;
//    private ImageView iv_scroll_top;
//    private FollowPresenter followPresenter;
//    private GetHomeNearbyPresenter getHomeNearbyPresenter;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_find, null);
//        }
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        EventBus.getDefault().register(this);
//        getHomeNearbyPresenter().getHomeNewCount(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//        getFollowPresenter().getAllFollowList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sort, pageSize, pageNumber, true);
//        getGetRecommendListPresenter().getHomeBannerList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 3);
////        getPresenter().getRoomList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 5, pageNumber);
//        setTitle();
//        init();
//    }
//
//    @Override
//    protected void onVisible(boolean isInit) {
//
//    }
//
//    private void init() {
//        recycler_find = rootView.findViewById(R.id.recycler_find);
//        iv_scroll_top = rootView.findViewById(R.id.iv_scroll_top);
//        final GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//        recycler_find.setLayoutManager(gridLayoutManager);
//        findListAdapter = new FindListAdapter(getActivity());
//        recycler_find.setAdapter(findListAdapter);
//
//        findListAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (position > 0) {
//                    HomeInfo HomeInfo = findListAdapter.getList_find().get(position);
//                    Intent intent = new Intent(getActivity(), PKVideoPlayActivity.class);
//                    intent.putExtra(Constants.Fields.TYPE, HomeInfo.getType());
//                    if (HomeInfo.getType() == 1) {
//                        intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, HomeInfo.getPostInfo().getPostId());
//                    } else if (HomeInfo.getType() == 8) {
//                        intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, HomeInfo.getSayLoveInfo().getSayLoveId());
//                    } else if (HomeInfo.getType() == 9) {
//                        intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, HomeInfo.getHelpInfo().getHelpId());
//                    }
//                    getActivity().startActivity(intent);
//                }
//            }
//        });
//
//        recycler_find.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
//                if (firstVisibleItemPosition != 0) {
//                    iv_scroll_top.setVisibility(View.VISIBLE);
//                } else {
//                    iv_scroll_top.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        ImageView publishView = rootView.findViewById(R.id.publish_view);
//        publishView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //一键发布界面
//                Intent intent = new Intent(getActivity(), PublishActivity.class);
//                intent.putExtra(Constants.Fields.TYPE, 1);
//                startActivity(intent);
//            }
//        });
//
//        // 刷新框架
//        refreshLayout = rootView.findViewById(R.id.refresh_layout_view);
//        // 头部加载样式
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        // 底部加载样式
//        LoadingView loadingView = new LoadingView(getContext());
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(recycler_find);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setEnableLoadmore(true);
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNumber = 1;
//                requestData();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                pageNumber += 1;
//                requestData();
//            }
//        });
//        findListAdapter.setFindTabClick(new FindListAdapter.FindTabClick() {
//            @Override
//            public void Newest() {
//                sort = 2;
//                pageNumber = 1;
//                if (sort != 2) {
//                    getHomeNearbyPresenter().getHomeNewCount(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//                }
//                getFollowPresenter().getAllFollowList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sort, pageSize, pageNumber, true);
//            }
//
//            @Override
//            public void Hot() {
//                sort = 1;
//                pageNumber = 1;
//                if (sort != 2) {
//                    getHomeNearbyPresenter().getHomeNewCount(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//                }
//                getFollowPresenter().getAllFollowList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sort, pageSize, pageNumber, true);
//            }
//        });
//
//
//        iv_scroll_top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recycler_find.scrollToPosition(0);
//                iv_scroll_top.setVisibility(View.GONE);
//            }
//        });
//
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (!isAdded()) {
//            return;
//        }
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            Utils.showToastShortTime(e.getMessage());
//            if (pageNumber == 1) {
//                refreshLayout.finishRefreshing();
//            } else {
//                refreshLayout.finishLoadmore();
//            }
//            return;
//        }
//
//        switch (apiName) {
//            case InterfaceUrl.URL_GETALLFOLLOWLIST:
//                if (object instanceof GetItemFollowListResponse) {
//                    GetItemFollowListResponse getItemFollowListResponse = (GetItemFollowListResponse) object;
//                    if (pageNumber == 1) {
//                        refreshLayout.finishRefreshing();
//                    } else {
//                        refreshLayout.finishLoadmore();
//                    }
//                    if (getItemFollowListResponse.getCode() == 200) {
//
//                        if (pageNumber == 1) {
//                            findListAdapter.setData(getItemFollowListResponse.getList());
//                        } else {
//                            findListAdapter.addData(getItemFollowListResponse.getList());
//                        }
//
//                        if (getItemFollowListResponse.getTotal() <= pageNumber * pageSize) {
//                            // 没有更多数据时  下拉刷新不可用
//                            refreshLayout.setEnableLoadmore(false);
//                        } else {
//                            //有更多数据时  下拉刷新才可用
//                            refreshLayout.setEnableLoadmore(true);
//                        }
//                    }
//                }
//                break;
//
//            case InterfaceUrl.URL_GETHOMEBANNERLIST:
//                if (object instanceof GetHomeBannerListResponse) {
//                    GetHomeBannerListResponse getHomeBannerListResponse = (GetHomeBannerListResponse) object;
//                    if (getHomeBannerListResponse.getCode() == 200) {
//                        List<RecommendInfo> recommendInfoList = getHomeBannerListResponse.getList();
//                        findListAdapter.setRecommendInfoList(recommendInfoList);
//                    } else {
//                        Utils.showToastShortTime(getHomeBannerListResponse.getMsg());
//                    }
//                }
//                break;
//
//            case InterfaceUrl.URL_GETROOMLIST:
//                if (object instanceof GetRoomListResponse) {
//                    GetRoomListResponse getRoomListResponse = (GetRoomListResponse) object;
//                    if (getRoomListResponse.getCode() == 200) {
//                        findListAdapter.setRoomList(getRoomListResponse.getRoomList());
//                    }
//                }
//            case InterfaceUrl.URL_GETHOMENEWCOUNT:
//                if (object instanceof GetHomeNewCountResponse) {
//                    GetHomeNewCountResponse response = (GetHomeNewCountResponse) object;
//                    if (response.getCode() == 200) {
//                        if (response.getDiscoverCount() > 0) {
//                            //展示关注小红点
//                            findListAdapter.setHasNewViewVisible(true);
//                        } else {
//                            findListAdapter.setHasNewViewVisible(false);
//                        }
//
//                    } else {
//                        Utils.showToastShortTime(response.getMsg());
//                    }
//                }
//                break;
//        }
//    }
//
//    private void requestData() {
//        if (sort != 2) {
//            getHomeNearbyPresenter().getHomeNewCount(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//        }
//        getFollowPresenter().getAllFollowList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sort, pageSize, pageNumber, false);
//        getGetRecommendListPresenter().getHomeBannerList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 3);
////        getPresenter().getRoomList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 5, pageNumber);
//    }
//
//    private GetHomeNearbyPresenter getHomeNearbyPresenter() {
//        if (getHomeNearbyPresenter == null) {
//            getHomeNearbyPresenter = new GetHomeNearbyPresenter(this);
//        }
//        return getHomeNearbyPresenter;
//    }
//
//    private DiscoverPresenter getPresenter() {
//        if (discoverPresenter == null) {
//            discoverPresenter = new DiscoverPresenter(this);
//        }
//        return discoverPresenter;
//    }
//
//    public FollowPresenter getFollowPresenter() {
//        if (followPresenter == null) {
//            followPresenter = new FollowPresenter(this);
//        }
//        return followPresenter;
//    }
//
//    public GetRecommendListPresenter getGetRecommendListPresenter() {
//        if (getRecommendListPresenter == null) {
//            getRecommendListPresenter = new GetRecommendListPresenter(this);
//        }
//        return getRecommendListPresenter;
//    }
//
//    private void setTitle() {
//        TitleFragment titleFragment = (TitleFragment) getChildFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_video);
//        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                getActivity().finish();
//            }
//        });
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventCallback(FindListEvent event) {
//        if (event == null) {
//            return;
//        }
//        if (event.getType() == 1) {
//            pageNumber = 1;
//            requestData();
//        }
//
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventFindFragmentRefreshEvent(FindFragmentRefreshEvent event) {
//        if (event != null) {
//            requestData();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (rootView != null) {
//            rootView = null;
//        }
//        EventBus.getDefault().unregister(this);
//
//        if (rootView != null) {
//            ((ViewGroup) rootView).removeAllViews();
//        }
//        rootView = null;
//
//        if (recycler_find != null) {
//            recycler_find.removeAllViews();
//        }
//        recycler_find = null;
//
//        if (discoverPresenter != null) {
//            discoverPresenter.onDestroy();
//        }
//        discoverPresenter = null;
//
//        if (findListAdapter != null) {
//            if (findListAdapter.getItemCount() > 0) {
//                findListAdapter.getList_find().clear();
//            }
//            findListAdapter = null;
//        }
//
//        if (refreshLayout != null) {
//            refreshLayout.removeAllViews();
//            refreshLayout = null;
//        }
//
//        if (getRecommendListPresenter != null) {
//            getRecommendListPresenter.onDestroy();
//        }
//        getRecommendListPresenter = null;
//
//
//        if (iv_scroll_top != null) {
//            iv_scroll_top = null;
//        }
//
//        if (followPresenter != null) {
//            followPresenter.onDestroy();
//        }
//        followPresenter = null;
//
//        if (getHomeNearbyPresenter != null) {
//            getHomeNearbyPresenter.onDestroy();
//        }
//        getHomeNearbyPresenter = null;
//
//
//    }
//
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//
//    }
//}
