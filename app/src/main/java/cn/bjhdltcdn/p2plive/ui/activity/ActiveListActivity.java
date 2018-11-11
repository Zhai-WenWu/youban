//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.IdRes;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
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
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.UpdateActiveListEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHobbyListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetOfflineActiveListResponse;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetOfflineActiveListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.CategoryLabelTabsRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.OfflineActiveRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.PublishActiveSelectDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Solve7PopupWindow;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 线下活动列表
// */
//public class ActiveListActivity extends BaseActivity implements BaseView {
//    private GetOfflineActiveListPresenter getOfflineActiveListPresenter;
//    private AssociationPresenter associationPresenter;
//    private TitleFragment titleFragment;
//    private RecyclerView recycleView;
//    private OfflineActiveRecyclerViewAdapter recyclerAdapter;
//    private View emptyView;
//    private int pageSize = 10, pageNum = 1;
//    private TextView finishView;
//    private LoadingView loadingView;
//    // 下拉刷新
//    private TwinklingRefreshLayout refreshLayout;
//    private Solve7PopupWindow popupWindow;
//    private RelativeLayout popView;
//    private RadioGroup locationRadioGroup, sortRadioGroup;
//    private RadioButton allLocationRadioButton, sameLocationRadioButton, sortByTimeRadioButton, sortByDurationUserButton, sortByDurationActiveButton;
//    private RadioGroup sexGroup;
//    private RadioButton allSexRadioButton, boyGrilRadioButton,boyRadioButton, girlRadioButton;
//    private TextView cancelButton, okButton;
//    private int location;
//    private int selectSex=3;//性别限制(0不限,1仅限男生,2仅限女生),
//    private int publisherLimit;
//    private List<Long> hobbyIds;//选完的类别标签
//    private RecyclerView categoryRecycleView;
//    private CategoryLabelTabsRecyclerViewAdapter categoryLabelTabsRecyclerViewAdapter;
//    private List<HobbyInfo> hobbyInfoList, addAllHobbyInfoList;
//    private long userId;
//    private int sort = 3;//排序(1最新发布,2活动离我距离,3发布者离我距离),
//    private int activeType = 0;//活动类型(0全部,1其他类型),
//    private TextView launch;
//    private TextView empty_tv;
//    private boolean showPopWindow;
//    private int comeInType;//1:所有活动列表 2：他人的活动列表 3：我的活动列表
//    private long toUserId;//被查看的用户Id,
//    private boolean needShowLoading = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_active_list);
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        comeInType = getIntent().getIntExtra(Constants.Fields.COME_IN_TYPE, 0);
//        toUserId = getIntent().getLongExtra(Constants.Fields.TO_USER_ID, 0);
//        initView();
//        setTitle();
//        getOfflineActiveListPresenter = new GetOfflineActiveListPresenter(this);
//        associationPresenter = new AssociationPresenter(this);
//        if (comeInType == 2 || comeInType == 3) {
//            getOfflineActiveListPresenter.getJoinActionList(userId, toUserId, pageSize, pageNum, true);
//        } else {
//            getOfflineActiveListPresenter.getOfflineActiveList(userId, activeType, hobbyIds, sort, publisherLimit, selectSex, pageSize, pageNum, true);
//        }
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
//        recycleView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
////        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(10));
//        recycleView.setLayoutManager(linearLayoutManager);
////        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
//        recycleView.setAdapter(recyclerAdapter);
//        recyclerAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳转到活动详情页
//                Intent intent = new Intent(ActiveListActivity.this, ActiveDetailActivity.class);
//                intent.putExtra("activityId", recyclerAdapter.getItem(position).getActivityId());
//                intent.putExtra("position", position);
//                startActivity(intent);
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
//                if (comeInType == 2 || comeInType == 3) {
//                    getOfflineActiveListPresenter.getJoinActionList(userId, toUserId, pageSize, pageNum, false);
//                } else {
//                    getOfflineActiveListPresenter.getOfflineActiveList(userId, activeType, hobbyIds, sort, publisherLimit, selectSex, pageSize, pageNum, true);
//                }
//                refreshLayout.finishRefreshing();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                if (!loadingView.isOnLoadFinish()) {
//                    if (comeInType == 2 || comeInType == 3) {
//                        getOfflineActiveListPresenter.getJoinActionList(userId, toUserId, pageSize, pageNum, false);
//                    } else {
//                        getOfflineActiveListPresenter.getOfflineActiveList(userId, activeType, hobbyIds, sort, publisherLimit, selectSex, pageSize, pageNum, true);
//                    }
//                }
//            }
//        });
//        emptyView.setVisibility(View.GONE);
//        initTopPopWindow();
//        launch = findViewById(R.id.launch_text_view);
//        launch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PublishActiveSelectDialog publishActiveSelectDialog = new PublishActiveSelectDialog();
//                publishActiveSelectDialog.show(getSupportFragmentManager());
//
//            }
//        });
//        if (comeInType == 1) {
//            launch.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void initTopPopWindow() {
//        popView = (RelativeLayout) getLayoutInflater().inflate(R.layout.active_screening_popwindow_layout, null, false);
//        locationRadioGroup = (RadioGroup) popView.findViewById(R.id.radio_location);
//        allLocationRadioButton = (RadioButton) popView.findViewById(R.id.radio_location_all);
//        sameLocationRadioButton = (RadioButton) popView.findViewById(R.id.radio_location_same);
//        locationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if (checkedId == allLocationRadioButton.getId()) {
//                    publisherLimit = 0;
//                } else {
//                    publisherLimit = 1;
//                }
//            }
//        });
//        sortRadioGroup = (RadioGroup) popView.findViewById(R.id.radio_sort);
//        sortByTimeRadioButton = (RadioButton) popView.findViewById(R.id.radio_sort_time);
//        sortByDurationUserButton = (RadioButton) popView.findViewById(R.id.radio_sort_duration_user);
//        sortByDurationActiveButton = (RadioButton) popView.findViewById(R.id.radio_sort_duration_active);
//        sortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if (checkedId == sortByTimeRadioButton.getId()) {
//                    sort = 1;
//                } else if (checkedId == sortByDurationUserButton.getId()) {
//                    sort = 3;
//                } else {
//                    sort = 2;
//                }
//            }
//        });
//        sexGroup = (RadioGroup) popView.findViewById(R.id.radio_sex);
//        allSexRadioButton = (RadioButton) popView.findViewById(R.id.radio_all_sex);
//        boyGrilRadioButton= (RadioButton) popView.findViewById(R.id.radio_sex_boy_girl);
//        boyRadioButton = (RadioButton) popView.findViewById(R.id.radio_boy);
//        girlRadioButton = (RadioButton) popView.findViewById(R.id.radio_girl);
//        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if (checkedId == boyGrilRadioButton.getId()) {
//                    selectSex = 0;
//                } else if (checkedId == boyRadioButton.getId()) {
//                    selectSex = 1;
//                }else if (checkedId == girlRadioButton.getId()) {
//                    selectSex = 2;
//                } else {
//                    selectSex = 3;
//                }
//            }
//        });
//        okButton = (TextView) popView.findViewById(R.id.tv_ok);
//        cancelButton = (TextView) popView.findViewById(R.id.tv_cancel);
//        categoryRecycleView = (RecyclerView) popView.findViewById(R.id.category_recycler_view);
//        categoryLabelTabsRecyclerViewAdapter = new CategoryLabelTabsRecyclerViewAdapter(this);
//        categoryRecycleView.setHasFixedSize(true);
//        GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), 3);
//        categoryRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(15), 3, false));
//        categoryRecycleView.setLayoutManager(layoutManager);
//        categoryRecycleView.setAdapter(categoryLabelTabsRecyclerViewAdapter);
//
//        popupWindow = new Solve7PopupWindow(popView, LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT);//ViewGroup.LayoutParams.MATCH_PARENT, true
//        popupWindow.setFocusable(false);
//        popupWindow.setOutsideTouchable(false);
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.pay_radiobutton_unselect_icon));
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hobbyIds = categoryLabelTabsRecyclerViewAdapter.getSelectLabelInfoList();
//                if (hobbyIds.size() == 0) {
//                    activeType = 0;
//                } else {
//                    activeType = 1;
//                }
//                popupWindow.dismiss();
//                titleFragment.setRightViewTitle("筛选", R.mipmap.home_recommend_sort_close_icon);
//                showPopWindow = true;
//                pageNum = 1;
//                getOfflineActiveListPresenter.getOfflineActiveList(userId, activeType, hobbyIds, sort, publisherLimit, selectSex, pageSize, pageNum, true);
//                refreshLayout.finishRefreshing();
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                titleFragment.setRightViewTitle("筛选", R.mipmap.home_recommend_sort_close_icon);
//                showPopWindow = true;
//            }
//        });
//
//    }
//
//    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        if (comeInType == 1) {
//            titleFragment.setTitle(R.string.title_active_list);
//        } else if (comeInType == 3) {
//            titleFragment.setTitle(R.string.title_my_active_list);
//        } else {
//            titleFragment.setTitle(R.string.title_ta_active_list);
//        }
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
//        if (comeInType == 1) {
//            titleFragment.setRightViewTitle(R.mipmap.home_recommend_sort_close_icon, "筛选", new TitleFragment.RightViewClick() {
//
//                @Override
//                public void onClick() {
//                    //弹出筛选顺序框
//                    if (addAllHobbyInfoList == null) {
//                        //从网络下载兴趣爱好
//                        associationPresenter.getHobbyList(userId, 0, 2,false);
//                    } else {
//                        if (showPopWindow) {
//                            categoryLabelTabsRecyclerViewAdapter.setList(addAllHobbyInfoList);
//                            categoryLabelTabsRecyclerViewAdapter.notifyDataSetChanged();
//                            popupWindow.showAsDropDown(titleFragment.getRightView());
//                            titleFragment.setRightViewTitle("筛选", R.mipmap.home_recommend_sort_open_icon);
//                            showPopWindow = false;
//                        } else {
//                            popupWindow.dismiss();
//                            titleFragment.setRightViewTitle("筛选", R.mipmap.home_recommend_sort_close_icon);
//                            showPopWindow = true;
//                        }
//
//                    }
//
//                }
//            });
//        }
//    }
//
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
//        if ((apiName.equals(InterfaceUrl.URL_GETOFFLINEACTIVELIST) || apiName.equals(InterfaceUrl.URL_GETJOINACTIONLIST))) {
//            if(pageNum==1){
//                refreshLayout.finishRefreshing();
//            }else{
//                refreshLayout.finishLoadmore();
//            }
//            if (object instanceof GetOfflineActiveListResponse) {
//                GetOfflineActiveListResponse getOfflineActiveListResponse = (GetOfflineActiveListResponse) object;
//                if(getOfflineActiveListResponse.getCode()==200){
//                    refreshLayout.setEnableRefresh(true);
//                    List<ActivityInfo> ActivityInfoList = getOfflineActiveListResponse.getList();
//                    if (ActivityInfoList != null) {
//                        if (pageNum == 1) {
//                            recyclerAdapter.setList(ActivityInfoList, getOfflineActiveListResponse.getDefaultImg());
//                        } else {
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
//                            pageNum++;
//                        }
//
//                        if (getOfflineActiveListResponse.getTotal() == 0) {
//                            emptyView.setVisibility(View.VISIBLE);
//                            finishView.setVisibility(View.GONE);
//                            empty_tv.setText(getOfflineActiveListResponse.getBlankHint());
//                        } else {
//                            emptyView.setVisibility(View.GONE);
//                        }
//                        }
//                }else if (getOfflineActiveListResponse.getCode() == 201) {
//                    emptyView.setVisibility(View.VISIBLE);
//                    finishView.setVisibility(View.GONE);
//                    empty_tv.setText(getOfflineActiveListResponse.getMsg());
//                    refreshLayout.setEnableLoadmore(false);
//                    refreshLayout.setEnableRefresh(false);
//                } else {
//                    Utils.showToastShortTime(getOfflineActiveListResponse.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_GETHOBBYLIST.equals(apiName)) {
//            if (object instanceof GetHobbyListResponse) {
//                GetHobbyListResponse response = (GetHobbyListResponse) object;
//                if (response.getCode() == 200) {
//                    hobbyInfoList = response.getHobbyList();
//                    //筛选弹窗
//                    if (addAllHobbyInfoList == null) {
//                        addAllHobbyInfoList = new ArrayList<HobbyInfo>();
//                    }
//                    addAllHobbyInfoList.addAll(hobbyInfoList);
//                    HobbyInfo hobbyInfo = new HobbyInfo();
//                    hobbyInfo.setHobbyId(0);
//                    hobbyInfo.setHobbyName("全部        ");
//                    hobbyInfo.setIsCheck(1);
//                    addAllHobbyInfoList.add(0, hobbyInfo);
//                    categoryLabelTabsRecyclerViewAdapter.setList(addAllHobbyInfoList);
//                    categoryLabelTabsRecyclerViewAdapter.notifyDataSetChanged();
//                    popupWindow.showAsDropDown(titleFragment.getRightView());
//                    titleFragment.setRightViewTitle("筛选", R.mipmap.home_recommend_sort_open_icon);
//                    showPopWindow = false;
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
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
//        if (event.getType() == 1) {
//            //联网刷新
//            pageNum = 1;
//            getOfflineActiveListPresenter.getOfflineActiveList(userId, activeType, hobbyIds, sort, publisherLimit, selectSex, pageSize, pageNum, false);
//            refreshLayout.finishRefreshing();
//        } else {
//            if (event.isDelect()) {
//                //本地删除
//                recyclerAdapter.removeItem(event.getPosition());
//            } else {
//                //本地更新参加人数
//                recyclerAdapter.getItem(event.getPosition()).setJoinNumber(event.getUserNum());
//                recyclerAdapter.notifyDataSetChanged();
//            }
//        }
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
