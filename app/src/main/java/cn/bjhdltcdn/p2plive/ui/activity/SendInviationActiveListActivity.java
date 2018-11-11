//package cn.bjhdltcdn.p2plive.ui.activity;
//
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
//import cn.bjhdltcdn.p2plive.event.UpdateActiveListEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.GetOfflineActiveListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetNearOrganListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.OfflineActiveRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.RongIMutils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 我发布的活动中的活动列表
// */
//public class SendInviationActiveListActivity extends BaseActivity implements BaseView {
//    private GetNearOrganListPresenter mPresenter;
//    private TitleFragment titleFragment;
//    private RecyclerView recycleView;
//    private OfflineActiveRecyclerViewAdapter recyclerAdapter;
//    private View emptyView;
//    private int pageSize = 10, pageNum = 1;
//    private TextView finishView;
//    private LoadingView loadingView;
//    // 下拉刷新
//    private TwinklingRefreshLayout refreshLayout;
//    private long userId;
//    private TextView launch;
//    private TextView empty_tv;
//    private long toUserId;//被查看的用户Id,
//    private boolean needShowLoading = true;
//    private ActivityInfo activityInfo;
//    private int posi;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_active_list);
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        toUserId = getIntent().getLongExtra(Constants.Fields.TO_USER_ID, 0);
//        initView();
//        setTitle();
//        mPresenter = new GetNearOrganListPresenter(this);
//        mPresenter.getInvitationActionList(userId, toUserId, pageSize, pageNum);
//
//        EventBus.getDefault().register(this);
//
//    }
//
//    public void initView() {
//        emptyView = findViewById(R.id.empty_view);
//        empty_tv = findViewById(R.id.empty_textView);
//        finishView = findViewById(R.id.finish_view);
//        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
//        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerAdapter = new OfflineActiveRecyclerViewAdapter(this);
//        recyclerAdapter.setShowHeadView(true);
//        recycleView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
////        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(10));
//        recycleView.setLayoutManager(linearLayoutManager);
////        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
//        recycleView.setAdapter(recyclerAdapter);
//        recyclerAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if(position!=0){
//                    posi=position;
//                    //选中活动
//                    activityInfo=recyclerAdapter.getItem(position);
//                    if(activityInfo.getIsSendInvitation()==1){
//                        if(activityInfo.getIsSendMaxNumber()==1){
//                            if(!activityInfo.isCheck()){
//                                recyclerAdapter.getItem(recyclerAdapter.getSelectPosition()).setCheck(false);
//                                activityInfo.setCheck(true);
//                                recyclerAdapter.notifyDataSetChanged();
//                            }
//                        }else{
//                            //发送次数已经超过10次
//                            Utils.showToastShortTime("不能再次选择该活动发出邀请函");
//                        }
//
//                    }else{
//                        //已给该用户发送过
//                        Utils.showToastShortTime("该活动已发送邀请函");
//                    }
//                }
//            }
//        });
//
//
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
//                mPresenter.getInvitationActionList(userId, toUserId, pageSize, pageNum);
//                refreshLayout.finishRefreshing();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                if (!loadingView.isOnLoadFinish()) {
//                    pageNum++;
//                    mPresenter.getInvitationActionList(userId, toUserId, pageSize, pageNum);
//                }
//            }
//        });
//        emptyView.setVisibility(View.GONE);
//        launch = findViewById(R.id.launch_text_view);
//        launch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(recyclerAdapter.getItemCount()>1){
//                    if(posi>0){
//                        //调用发送邀请函接口
//                        mPresenter.sendNearInvitation(userId,toUserId,recyclerAdapter.getItem(posi).getActivityId());
//                    }else{
//                        Utils.showToastShortTime("请选择活动");
//                    }
//                }else{
//                    Utils.showToastShortTime("获取列表失败，请重新刷新数据");
//                }
//            }
//        });
//
//        launch.setVisibility(View.VISIBLE);
//        launch.setText("发送");
//
//
//    }
//
//
//
//    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_my_publish_active_list);
//
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
//
//    }
//
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if ((apiName.equals(InterfaceUrl.URL_GETINVITATIONACTIONLIST))) {
//            if (object instanceof GetOfflineActiveListResponse) {
//                GetOfflineActiveListResponse getOfflineActiveListResponse = (GetOfflineActiveListResponse) object;
//                if(getOfflineActiveListResponse.getCode()==200){
//                    List<ActivityInfo> ActivityInfoList = getOfflineActiveListResponse.getList();
//                    if (ActivityInfoList != null) {
//                        if (pageNum == 1) {
//                            refreshLayout.finishRefreshing();
//                            recyclerAdapter.setList(ActivityInfoList, getOfflineActiveListResponse.getDefaultImg());
//                        } else {
//                            refreshLayout.finishLoadmore();
//                            recyclerAdapter.addList(ActivityInfoList);
//                        }
//                        recyclerAdapter.notifyDataSetChanged();
//                        if (getOfflineActiveListResponse.getTotal() <= pageNum * pageSize) {
//                            //没有更多数据时  下拉刷新不可用
//                            refreshLayout.setEnableLoadmore(false);
//                            finishView.setVisibility(View.GONE);
//                        } else {
//                            //有更多数据时  下拉刷新才可用
//                            refreshLayout.setEnableLoadmore(true);
//                            finishView.setVisibility(View.GONE);
//                        }
//
//                        if (getOfflineActiveListResponse.getTotal() == 0) {
//                            emptyView.setVisibility(View.VISIBLE);
//                            finishView.setVisibility(View.GONE);
//                            empty_tv.setText(getOfflineActiveListResponse.getBlankHint());
//                        } else {
//                            emptyView.setVisibility(View.GONE);
//                        }
//                    }
//                }else{
//                    Utils.showToastShortTime(getOfflineActiveListResponse.getMsg());
//                }
//
//            }
//        } else if (InterfaceUrl.URL_SENDNEARINVITATION.equals(apiName)) {
//            if (object instanceof NoParameterResponse) {
//                NoParameterResponse response = (NoParameterResponse) object;
//                if (response.getCode() == 200) {
//                    Utils.showToastShortTime(response.getMsg());
//                    BaseUser toBaseUser=new BaseUser();
//                    toBaseUser.setUserId(toUserId);
//                    RongIMutils.sendSharedMessage("", 40004,toBaseUser, activityInfo);
//                    finish();
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        }
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
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(UpdateActiveListEvent event) {
//        if (event == null) {
//            return;
//        }
////        if (event.getType() == 1) {
////            //联网刷新
////            pageNum = 1;
////            getOfflineActiveListPresenter.getOfflineActiveList(userId, activeType, hobbyIds, sort, publisherLimit, selectSex, pageSize, pageNum, false);
////            refreshLayout.finishRefreshing();
////        } else {
////            if (event.isDelect()) {
////                //本地删除
////                recyclerAdapter.removeItem(event.getPosition());
////            } else {
////                //本地更新参加人数
////                recyclerAdapter.getItem(event.getPosition()).setJoinNumber(event.getUserNum());
////                recyclerAdapter.notifyDataSetChanged();
////            }
////        }
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}
