//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.support.annotation.IdRes;
//import android.support.v7.widget.LinearLayoutCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
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
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.UpdateActiveListEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetNearPersonListResponse;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetNearOrganListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.NearByPersonListAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.PublishActiveSelectDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Solve7PopupWindow;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 附近的人列表
// */
//public class NearByPersonListActivity extends BaseActivity implements BaseView {
//    private GetNearOrganListPresenter pstPresenter;
//    private UserPresenter userPresenter;
//    private RecyclerView recycleView;
//    private NearByPersonListAdapter recyclerAdapter;
//    private View emptyView;
//    private int pageSize = 10, pageNum = 1;
//    private TextView finishView;
//    private LoadingView loadingView;
//    // 下拉刷新
//    private TwinklingRefreshLayout refreshLayout;
//    private TitleFragment titleFragment;
//    private long userId;
//    private TextView empty_tv;
//    private boolean needShowLoading = true;
//    private boolean showPopWindow;
//    private Solve7PopupWindow popupWindow;
//    private RadioGroup sortRadioGroup,sexRadioGroup;
//    private RadioButton allRadioButton, cityRadioButton, alumnusRadioButton,allSexRadioButton,boyRadioButton, girlRadioButton;
//    private TextView cancelButton, okButton;
//    private RelativeLayout popView;
//    private int type = 0;//类型(0全部,1同城,2校友),
//    private int sexLimit = 0;//性别限制(0全部,1男生,2女生),
//    private ImageView publishView;
//    private int itemPosition;
//    private int isPublishActivity;//是否发布过活动(1未发布过，2已发布),
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nearby_person_list);
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        initView();
//        setTitle();
//        userPresenter = new UserPresenter(this);
//        pstPresenter = new GetNearOrganListPresenter(this);
//        pstPresenter.getNearPersonList(userId,type,sexLimit, pageSize, pageNum);
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
//        recyclerAdapter = new NearByPersonListAdapter(this);
//        recycleView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
//        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(1));
//        recycleView.setLayoutManager(linearLayoutManager);
//        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
//        recycleView.setAdapter(recyclerAdapter);
//        recyclerAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳转到用户详情页
//
//            }
//        });
//        recyclerAdapter.setOnClick(new NearByPersonListAdapter.OnClick() {
//
//            @Override
//            public void onAttention(int position,int type,long formUserId,long toUserId) {
//                itemPosition=position;
//                userPresenter.attentionOperation(type, userId, toUserId);
//            }
//
//            @Override
//            public void onSendInvitaion(long userId, long toUserId) {
//                if(isPublishActivity==2){
//                    //跳转到我发布的活动界面
//                    Intent intent=new Intent(NearByPersonListActivity.this,SendInviationActiveListActivity.class);
//                    intent.putExtra(Constants.Fields.TO_USER_ID,toUserId);
//                    startActivity(intent);
//                }else{
//                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                    dialog.setText("", "您当前还未发布活动，无法邀请好友参加，您可以现在发布一个活动", "稍后发布", "现在发布");
//                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                        @Override
//                        public void onLeftClick() {
//                            //取消
//                        }
//
//                        @Override
//                        public void onRightClick() {
//                            //跳转到发布活动界面
//                            PublishActiveSelectDialog publishActiveSelectDialog = new PublishActiveSelectDialog();
//                            publishActiveSelectDialog.show(getSupportFragmentManager());
//
//                        }
//                    });
//                    dialog.show(getSupportFragmentManager());
//                }
//
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
//                pstPresenter.getNearPersonList(userId,type,sexLimit ,pageSize, pageNum);
//                refreshLayout.finishRefreshing();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                if (!loadingView.isOnLoadFinish()) {
//                    pstPresenter.getNearPersonList(userId,type,sexLimit, pageSize, pageNum);
//                }
//            }
//        });
//
//        publishView = findViewById(R.id.publish_view);
//        publishView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到发布活动界面
//                PublishActiveSelectDialog publishActiveSelectDialog = new PublishActiveSelectDialog();
//                publishActiveSelectDialog.show(getSupportFragmentManager());
//            }
//        });
//        initPopWindow();
//
//
//    }
//
//    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_nearby_person_list);
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
//        titleFragment.setRightViewTitle(R.mipmap.home_recommend_sort_close_icon,"筛选",  new TitleFragment.RightViewClick() {
//            @Override
//            public void onClick() {
//                //弹出popWindow
//                //弹出筛选顺序框
//                if (!showPopWindow) {
//                    popupWindow.showAsDropDown(titleFragment.getRightView());
//                    titleFragment.setRightViewTitle(R.mipmap.home_recommend_sort_open_icon);
//                    showPopWindow = true;
//                } else {
//                    popupWindow.dismiss();
//                    titleFragment.setRightViewTitle(R.mipmap.home_recommend_sort_close_icon);
//                    showPopWindow = false;
//                }
//            }
//        });
//    }
//
//    private void initPopWindow() {
//        popView = (RelativeLayout) getLayoutInflater().inflate(R.layout.nearby_person_order_popwindow_layout, null, false);
//        popView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                if (v instanceof ViewGroup) {
//                    View layoutView = ((ViewGroup) v).getChildAt(0);
//                    if (v != null) {
//                        float y = event.getY();
//                        float x = event.getX();
//                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
//                        if (!rect.contains((int) x, (int) y)) {
//                            popupWindow.dismiss();
//                            titleFragment.setRightViewTitle(R.mipmap.home_recommend_sort_close_icon);
//                            showPopWindow = false;
//                        }
//                        rect.setEmpty();
//                        rect = null;
//                    }
//                }
//                return false;
//            }
//        });
//        sortRadioGroup = (RadioGroup) popView.findViewById(R.id.radio_sort);
//        allRadioButton = (RadioButton) popView.findViewById(R.id.radio_sort_all);
//        cityRadioButton = (RadioButton) popView.findViewById(R.id.radio_sort_city);
//        alumnusRadioButton = (RadioButton) popView.findViewById(R.id.radio_sort_alumnus);
//        sortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if (checkedId == allRadioButton.getId()) {
//                    type =0;
//                } else if (checkedId == cityRadioButton.getId()) {
//                    type = 1;
//                } else {
//                    type = 2;
//                }
//            }
//        });
//        sexRadioGroup = (RadioGroup) popView.findViewById(R.id.radio_sex);
//        allSexRadioButton = (RadioButton) popView.findViewById(R.id.radio_all_sex);
//        boyRadioButton = (RadioButton) popView.findViewById(R.id.radio_boy);
//        girlRadioButton = (RadioButton) popView.findViewById(R.id.radio_girl);
//        sexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if (checkedId == allSexRadioButton.getId()) {
//                    sexLimit = 0;
//                } else if (checkedId == boyRadioButton.getId()) {
//                    sexLimit = 1;
//                } else {
//                    sexLimit = 2;
//                }
//            }
//        });
//        okButton = (TextView) popView.findViewById(R.id.tv_ok);
//        cancelButton = (TextView) popView.findViewById(R.id.tv_cancel);
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                titleFragment.setRightViewTitle("筛选", R.mipmap.home_recommend_sort_close_icon);
//                showPopWindow = false;
//                pageNum = 1;
//                pstPresenter.getNearPersonList(userId,type,sexLimit, pageSize, pageNum);
//                refreshLayout.finishRefreshing();
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                titleFragment.setRightViewTitle("筛选", R.mipmap.home_recommend_sort_close_icon);
//                showPopWindow = false;
//            }
//        });
//        popupWindow = new Solve7PopupWindow(popView, LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT);//ViewGroup.LayoutParams.MATCH_PARENT, true
//        popupWindow.setFocusable(false);
//        popupWindow.setOutsideTouchable(false);
//
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
//        if (apiName.equals(InterfaceUrl.URL_GETNEARPERSONLIST)) {
//            if(pageNum==1){
//                refreshLayout.finishRefreshing();
//            }else{
//                refreshLayout.finishLoadmore();
//            }
//            if (object instanceof GetNearPersonListResponse) {
//                GetNearPersonListResponse getNearPersonListResponse = (GetNearPersonListResponse) object;
//                if (getNearPersonListResponse.getCode() == 200) {
//                    List<BaseUser> list = getNearPersonListResponse.getList();
//                    isPublishActivity=getNearPersonListResponse.getIsPublishActivity();
//                    if (list != null) {
//                        if (pageNum == 1) {
//                            recyclerAdapter.setList(list);
//                        } else {
//                            recyclerAdapter.setListAll(list);
//                        }
//                        recyclerAdapter.notifyDataSetChanged();
//                    }
//                    if (getNearPersonListResponse.getTotal() <= pageNum * pageSize) {
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
//                    if (getNearPersonListResponse.getTotal() == 0) {
//                        emptyView.setVisibility(View.VISIBLE);
//                        finishView.setVisibility(View.GONE);
//                        empty_tv.setText(getNearPersonListResponse.getBlankHint());
//                    } else {
//                        emptyView.setVisibility(View.GONE);
//                    }
//                }else if (getNearPersonListResponse.getCode() == 201) {
//                    emptyView.setVisibility(View.VISIBLE);
//                    finishView.setVisibility(View.GONE);
//                    empty_tv.setText(getNearPersonListResponse.getMsg());
//                    refreshLayout.setEnableLoadmore(false);
//                    refreshLayout.setEnableRefresh(false);
//                } else {
//                    Utils.showToastShortTime(getNearPersonListResponse.getMsg());
//                }
//            }
//        }else if (apiName.equals(InterfaceUrl.URL_ATTENTIONOPERATION)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse baseResponse = (BaseResponse) object;
//                int code = baseResponse.getCode();
//                if (code == 200) {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                    //关注成功
//                    recyclerAdapter.getItem(itemPosition).setIsAttention(1);
//                    recyclerAdapter.notifyDataSetChanged();
//                } else {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                }
//
//            }
//        }
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
//            pstPresenter.getNearPersonList(userId,type,sexLimit ,pageSize, pageNum);
//            refreshLayout.finishRefreshing();
//        }
//
//
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
//        EventBus.getDefault().unregister(this);
//    }
//}
