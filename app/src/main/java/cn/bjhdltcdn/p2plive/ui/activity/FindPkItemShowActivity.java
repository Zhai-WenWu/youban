//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.httpresponse.GetItemFollowListResponse;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.FollowPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.FindItemShowListAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * Created by ZHAI on 2018/2/24.
// */
//
//public class FindPkItemShowActivity extends BaseActivity implements BaseView {
//
//    private RecyclerView recycler_pk_item;
//    private TwinklingRefreshLayout refreshLayout;
//    private DiscoverPresenter discoverPresenter;
//    private int pageSize = 20, pageNumber = 1;
//    private FindItemShowListAdapter findListAdapter;
//    private FollowPresenter followPresenter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_find_pk_item_show);
//        recycler_pk_item = findViewById(R.id.recycler_pk_item);
//        refreshLayout = findViewById(R.id.refresh_layout_view);
//        setTitle();
//        init();
//    }
//
//    private void init() {
//        findListAdapter = new FindItemShowListAdapter(this);
//        recycler_pk_item.setAdapter(findListAdapter);
//        findListAdapter.setOnVideoClick(new FindItemShowListAdapter.OnVideoClick() {
//            @Override
//            public void onClick(int position, int item) {
//                List<HomeInfo> listfind = findListAdapter.getListfind();
//                Intent intent = new Intent(FindPkItemShowActivity.this, PKVideoPlayActivity.class);
//
//                if (listfind.get(position).getType() == 1) {
//                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, listfind.get(position).getPostInfo().getFollowList().get(item).getPostId());
//                } else if (listfind.get(position).getType() == 8) {
//                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, listfind.get(position).getSayLoveInfo().getFollowList().get(item).getSayLoveId());
//                } else if (listfind.get(position).getType() == 9) {
//                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, listfind.get(position).getHelpInfo().getFollowList().get(item).getHelpId());
//                }
//
//                intent.putExtra(Constants.Fields.TYPE, listfind.get(position).getType());
//                startActivity(intent);
//            }
//        });
//        findListAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                List<HomeInfo> listfind = findListAdapter.getListfind();
//                if (listfind.get(position).getType() == 1) {
//                    Intent intent = new Intent(FindPkItemShowActivity.this, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, listfind.get(position).getPostInfo().getParentId());
//                    intent.putExtra(Constants.Fields.TYPE, listfind.get(position).getType());
//                    startActivity(intent);
//                } else if (listfind.get(position).getType() == 8) {
//                    Intent intent = new Intent(FindPkItemShowActivity.this, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, listfind.get(position).getSayLoveInfo().getParentId());
//                    intent.putExtra(Constants.Fields.TYPE, listfind.get(position).getType());
//                    startActivity(intent);
//                } else if (listfind.get(position).getType() == 9) {
//                    Intent intent = new Intent(FindPkItemShowActivity.this, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, listfind.get(position).getHelpInfo().getParentId());
//                    intent.putExtra(Constants.Fields.TYPE, listfind.get(position).getType());
//                    startActivity(intent);
//                }
//            }
//        });
//        getFollowPresenter().getItemFollowList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 0, 1, 0, 0, pageSize, pageNumber, true);
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        // 底部加载样式
//        LoadingView loadingView = new LoadingView(this);
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(recycler_pk_item);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setEnableLoadmore(true);
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNumber = 1;
//                getFollowPresenter().getItemFollowList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 0, 1, 0, 0, pageSize, pageNumber, false);
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                pageNumber += 1;
//                getFollowPresenter().getItemFollowList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 0, 1, 0, 0, pageSize, pageNumber, false);
//            }
//        });
//    }
//
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        switch (apiName) {
//            case InterfaceUrl.URL_GETITEMFOLLOWLIST:
//                if (object instanceof GetItemFollowListResponse) {
//                    final GetItemFollowListResponse getItemFollowListResponse = (GetItemFollowListResponse) object;
//                    if (getItemFollowListResponse.getCode() == 200) {
//                        List<HomeInfo> list = getItemFollowListResponse.getList();
//                        if (list.size() > 0) {
//                            if (pageNumber == 1) {
//                                findListAdapter.setData(list);
//                            } else {
//                                findListAdapter.addData(list);
//                            }
//                            refreshLayout.finishLoadmore();
//                            refreshLayout.finishRefreshing();
//                            if (getItemFollowListResponse.getTotal() <= pageNumber * pageSize) {
//                                //没有更多数据时  下拉刷新不可用
//                                refreshLayout.setEnableLoadmore(false);
//                            } else {
//                                //有更多数据时  下拉刷新才可用
//                                refreshLayout.setEnableLoadmore(true);
//                            }
//
//                        }
//                    }
//                }
//                break;
//        }
//    }
//
//    private void setTitle() {
//        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
//        titleFragment.setTitle("跟拍条目");
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
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//}
