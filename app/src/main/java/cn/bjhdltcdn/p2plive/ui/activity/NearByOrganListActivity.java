//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
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
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.model.NearOrganInfo;
//import cn.bjhdltcdn.p2plive.httpresponse.GetNearOrganListResponse;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetNearOrganListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.NearbyOrganRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 附近圈子列表
// */
//public class NearByOrganListActivity extends BaseActivity implements BaseView {
//    private GetNearOrganListPresenter pstPresenter;
//    private AssociationPresenter associationPresenter;
//    private RecyclerView recycleView;
//    private NearbyOrganRecyclerViewAdapter recyclerAdapter;
//    private View emptyView;
//    private int pageSize = 10, pageNum = 1;
//    private TextView finishView;
//    private LoadingView loadingView;
//    // 下拉刷新
//    private TwinklingRefreshLayout refreshLayout;
//    private TitleFragment titleFragment;
//    private long userId;
//    private int joinType;
//    private int currentOraginPosition;
//    private TextView empty_tv;
//    private boolean needShowLoading = true;
//    private TextView nearByPersonTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nearby_orgain_list);
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        initView();
//        setTitle();
//        associationPresenter = new AssociationPresenter(this);
//        pstPresenter = new GetNearOrganListPresenter(this);
//        pstPresenter.getNearOrganList(userId, pageSize, pageNum);
//    }
//
//    public void initView() {
//        nearByPersonTextView=findViewById(R.id.nearby_person_view);
//        nearByPersonTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到附近的人
//                startActivity(new Intent(NearByOrganListActivity.this,NearByPersonListActivity.class));
//            }
//        });
//        emptyView = findViewById(R.id.empty_view);
//        empty_tv = findViewById(R.id.empty_textView);
//        finishView = findViewById(R.id.finish_view);
//        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
//        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerAdapter = new NearbyOrganRecyclerViewAdapter(this);
//        recycleView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
//        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(11));
//        recycleView.setLayoutManager(linearLayoutManager);
//        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
//        recycleView.setAdapter(recyclerAdapter);
//        recyclerAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳转到圈子详情页
//                startActivity(new Intent(NearByOrganListActivity.this, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, recyclerAdapter.getItem(position).getOrganizationInfo()));
//
//            }
//        });
//        recyclerAdapter.setOnClick(new NearbyOrganRecyclerViewAdapter.OnClick() {
//            @Override
//            public void onJoinClick(int position, final int type) {
//                final long organId = recyclerAdapter.getItem(position).getOrganizationInfo().getOrganId();
//                joinType = type;
//                currentOraginPosition = position;
//                if (type == 2) {
//                    //申请加入圈子
//                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                    dialog.setText("", "本圈子为私密圈子，需要先发送申请，管理员同意后才能加入，发送？", "取消", "发送");
//                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                        @Override
//                        public void onLeftClick() {
//                            //取消
//                        }
//
//                        @Override
//                        public void onRightClick() {
//                            //申请
//                            associationPresenter.joinOrganization(organId, userId, type);
//                        }
//                    });
//                    dialog.show(getSupportFragmentManager());
//                } else {
//                    //直接加入圈子
//                    associationPresenter.joinOrganization(organId, userId, type);
//                }
//            }
//        });
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        loadingView = new LoadingView(getApplicationContext());
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(recycleView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNum = 1;
//                pstPresenter.getNearOrganList(userId, pageSize, pageNum);
//                refreshLayout.finishRefreshing();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                if (!loadingView.isOnLoadFinish()) {
//                    pstPresenter.getNearOrganList(userId, pageSize, pageNum);
//                }
//            }
//        });
//        emptyView.setVisibility(View.GONE);
//
//
//    }
//
//    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_nearby_organ_list);
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            String text = e.getMessage();
//            Utils.showToastShortTime(text);
//            if(refreshLayout!=null){
//                if(pageNum==1){
//                    refreshLayout.finishRefreshing();
//                }else{
//                    refreshLayout.finishLoadmore();
//                }
//            }
//            return;
//        }
//        if (apiName.equals(InterfaceUrl.URL_GETNEARORGANLIST)) {
//            if (pageNum == 1) {
//                refreshLayout.finishRefreshing();
//            } else {
//                refreshLayout.finishLoadmore();
//            }
//            if (object instanceof GetNearOrganListResponse) {
//                GetNearOrganListResponse getNearOrganListResponse = (GetNearOrganListResponse) object;
//                if (getNearOrganListResponse.getCode() == 200) {
//                    List<NearOrganInfo> nearOrganInfoList = getNearOrganListResponse.getNearList();
//                    if (nearOrganInfoList != null) {
//                        if (pageNum == 1) {
//                            recyclerAdapter.setList(nearOrganInfoList);
//                        } else {
//                            recyclerAdapter.addList(nearOrganInfoList);
//                        }
//                        recyclerAdapter.notifyDataSetChanged();
//                    }
//                    if (getNearOrganListResponse.getTotal() <= pageNum * pageSize) {
//                        //没有更多数据时  下拉刷新不可用
//                        refreshLayout.setEnableLoadmore(false);
//                        finishView.setVisibility(View.VISIBLE);
//                    } else {
//                        //有更多数据时  下拉刷新才可用
//                        refreshLayout.setEnableLoadmore(true);
//                        finishView.setVisibility(View.GONE);
//                        pageNum++;
//                    }
//
//                    if (getNearOrganListResponse.getTotal() == 0) {
//                        emptyView.setVisibility(View.VISIBLE);
//                        finishView.setVisibility(View.GONE);
//                        empty_tv.setText(getNearOrganListResponse.getBlankHint());
//                    } else {
//                        emptyView.setVisibility(View.GONE);
//                    }
//                } else if (getNearOrganListResponse.getCode() == 201) {
//                    emptyView.setVisibility(View.VISIBLE);
//                    finishView.setVisibility(View.GONE);
//                    empty_tv.setText(getNearOrganListResponse.getMsg());
//                    refreshLayout.setEnableLoadmore(false);
//                    refreshLayout.setEnableRefresh(false);
//                } else {
//                    Utils.showToastShortTime(getNearOrganListResponse.getMsg());
//                }
//            }
//        }else if (InterfaceUrl.URL_JOINORGANIZATION.equals(apiName)) {
//
//                if (object instanceof BaseResponse) {
//                    BaseResponse response = (BaseResponse) object;
//                    if (response.getCode() == 200) {
//                        Utils.showToastShortTime(response.getMsg());
//                        if (joinType == 1) {
//                            //加入成功
//                            recyclerAdapter.getList().get(currentOraginPosition).getOrganizationInfo().setUserRole(3);
//                        } else {
//                            //申请成功
//                            recyclerAdapter.getList().get(currentOraginPosition).getOrganizationInfo().setUserRole(4);
//                        }
//                        recyclerAdapter.notifyItemChanged(currentOraginPosition);
//
//                    } else {
//                        Utils.showToastShortTime(response.getMsg());
//                    }
//
//                }
//
//            }
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
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//}
