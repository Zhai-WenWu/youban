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
//import com.orhanobut.logger.Logger;
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
//import cn.bjhdltcdn.p2plive.event.AttenttionResultEvent;
//import cn.bjhdltcdn.p2plive.event.PkListChangeEvent;
//import cn.bjhdltcdn.p2plive.event.PublishSuccessLabelFragmentDialogEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetItemFollowListResponse;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.LabelInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.FollowPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.PkDetailAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.PublishSuccessLabelFragmentDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Created by ZHAI on 2017/12/20.
// * PK广场
// */
//
//public class PKParkActivity extends BaseActivity implements BaseView {
//
//    private CircleImageView ivAddPk;
//    private RecyclerView recyclerPhoto;
//    private int pageSize = 18;
//    private int pageNumber = 1;
//    private PkDetailAdapter pkDetailAdapter;
//    private ToolBarFragment titleFragment;
//    private UserPresenter myPresenter;
//    private TwinklingRefreshLayout refreshLayout;
//    private FollowPresenter followPresenter;
//    private List<HomeInfo> mList;
//    private long userId, toUserId;
//    private long parentId;
//    private int type;
//
//    private Runnable successLabelFragmentDialogRunnable;
//    private PublishSuccessLabelFragmentDialog successLabelFragmentDialog;
//
//    private View rootLayout;
//    private int total;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pk_detail);
//        EventBus.getDefault().register(this);
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        toUserId = getIntent().getLongExtra(Constants.Fields.TO_USER_ID, 0);
//        parentId = getIntent().getLongExtra(Constants.Fields.PARENT_ID, -1);
//        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
//        getFollowPresenter().getItemFollowList(userId, toUserId, 1, parentId, type, pageSize, pageNumber, true);
//        initView();
//    }
//
//    public FollowPresenter getFollowPresenter() {
//        if (followPresenter == null) {
//            followPresenter = new FollowPresenter(this);
//        }
//        return followPresenter;
//    }
//
//
//    private UserPresenter getMyPresenter() {
//        if (myPresenter == null) {
//            myPresenter = new UserPresenter(this);
//        }
//        return myPresenter;
//    }
//
//    private void initView() {
//
//        rootLayout = findViewById(R.id.root_layout);
//
//        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("");
//        recyclerPhoto = findViewById(R.id.recycler_photo);
//        ivAddPk = findViewById(R.id.iv_add_pk);
//        refreshLayout = findViewById(R.id.refresh_layout_view);
//        pkDetailAdapter = new PkDetailAdapter(this);
//        recyclerPhoto.setAdapter(pkDetailAdapter);
//        pkDetailAdapter.setItemClickListener(new PkDetailAdapter.OnClickListener() {
//            @Override
//            public void userIconClick() {
//                // startActivity(new Intent(PKParkActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, launchPlay.getBaseUser()));
//            }
//
//            @Override
//            public void attClick(long userId) {
//                getMyPresenter().attentionOperation(1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), userId);
//            }
//        });
//
//        // 头部加载样式
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        // 底部加载样式
//        LoadingView loadingView = new LoadingView(this);
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(recyclerPhoto);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setEnableLoadmore(true);
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNumber = 1;
//                getFollowPresenter().getItemFollowList(userId, toUserId, 1, parentId, type, pageSize, pageNumber, false);
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                pageNumber += 1;
//                getFollowPresenter().getItemFollowList(userId, toUserId, 1, parentId, type, pageSize, pageNumber, false);
//            }
//        });
//
//        ivAddPk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PKParkActivity.this, PublishActivity.class);
//                intent.putExtra(Constants.Fields.TYPE, 1);
//                intent.putExtra(Constants.Fields.PARENT_ID, parentId);
//                intent.putExtra(Constants.Fields.TO_USER_ID, toUserId);
//                intent.putExtra(Constants.Fields.FOLLOWING_SHOT_ENTER_TYPE, type);
//                startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        switch (apiName) {
//            case InterfaceUrl.URL_GETITEMFOLLOWLIST:
//                if (object instanceof GetItemFollowListResponse) {
//                    final GetItemFollowListResponse getItemFollowListResponse = (GetItemFollowListResponse) object;
//                    if (getItemFollowListResponse.getCode() == 200) {
//                        if (getItemFollowListResponse.getStatus() != 0) {
//                            Utils.showToastShortTime(getItemFollowListResponse.getMsg());
//                            finish();
//                        } else {
//                            mList = getItemFollowListResponse.getList();
//                            if (mList.size() > 0) {
//                                if (pageNumber == 1) {
//                                    pkDetailAdapter.setData(mList.get(0));
//                                    refreshLayout.finishRefreshing();
//                                } else {
//                                    pkDetailAdapter.addData(mList.get(0));
//                                    refreshLayout.finishLoadmore();
//                                }
//
//                                int type = mList.get(0).getType();
//                                switch (type) {
//                                    case Constants.Constant.HOME_TYPE_POST:
//                                        total = mList.get(0).getPostInfo().getFollowCount();
//                                        break;
//                                    case Constants.Constant.HOME_TYPE_SAYLOVE:
//                                        total = mList.get(0).getSayLoveInfo().getFollowCount();
//                                        break;
//                                    case Constants.Constant.HOME_TYPE_HELPINFO:
//                                        total = mList.get(0).getHelpInfo().getFollowCount();
//                                        break;
//                                }
//
//                                if (total <= pageNumber * pageSize) {
//                                    //没有更多数据时  下拉刷新不可用
//                                    refreshLayout.setEnableLoadmore(false);
//                                } else {
//                                    //有更多数据时  下拉刷新才可用
//                                    refreshLayout.setEnableLoadmore(true);
//                                }
//
//                            }
//                        }
//
//                    }
//                }
//                break;
//            case InterfaceUrl.URL_ATTENTIONOPERATION:
//                if (object instanceof BaseResponse) {
//                    BaseResponse baseResponse = (BaseResponse) object;
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                    if (baseResponse.getCode() == 200) {
//                        pkDetailAdapter.getData().getBaseUser().setIsAttention(1);
//                        pkDetailAdapter.notifyItemChanged(0);
//                    }
//                }
//                break;
//        }
//    }
//
//    /**
//     * 关注状态改变
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onAttentionEvent(AttenttionResultEvent event) {
//        if (event != null) {
//            pkDetailAdapter.getData().getBaseUser().setIsAttention(1);
//            pkDetailAdapter.notifyItemChanged(0);
//        }
//    }
//
//    /**
//     * 我要参加拍摄视频/pk视频播放页删除视频后回调
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onPkListChangeEvent(PkListChangeEvent event) {
//        if (event != null) {
//            int position = event.getPosition();
//            if (position == -1) {//“我要参加”传-1，重新请求列表数据
//                pageNumber = 1;
//            }
//        }
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(PublishSuccessLabelFragmentDialogEvent event) {
//
//        try {
//            List<LabelInfo> labelInfoList = new ArrayList<>(1);
//
//
//            if (event.getLabelId() > 0) {
//                LabelInfo labelInfo3 = new LabelInfo();
//                labelInfo3.setIsSelect(1);
//                labelInfo3.setLabelName("赛事专区");
//                labelInfoList.add(0, labelInfo3);
//            }
//
//
//            LabelInfo labelInfo = new LabelInfo();
//            labelInfo.setIsSelect(1);
//            labelInfo.setLabelName("学校校友录");
//            labelInfoList.add(labelInfo);
//
//            // 发布成功结果提示框
//            successLabelFragmentDialog = new PublishSuccessLabelFragmentDialog();
//
//            Logger.d("event.getList() === " + event.getList());
//
//            if (event.getList() != null) {
//                for (int i = 0; i < event.getList().size(); i++) {
//                    LabelInfo labelInfo2 = new LabelInfo();
//                    labelInfo2.setIsSelect(1);
//                    labelInfo2.setLabelName(event.getList().get(i).getOrganName());
//                    labelInfoList.add(labelInfo2);
//                }
//            }
//
//
//            successLabelFragmentDialog.setLabelInfoList(labelInfoList);
//            successLabelFragmentDialog.show(getSupportFragmentManager());
//
//
//            successLabelFragmentDialogRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    if (successLabelFragmentDialog != null && successLabelFragmentDialog.isAdded()) {
//                        successLabelFragmentDialog.dismiss();
//                    }
//                }
//            };
//
//            rootLayout.postDelayed(successLabelFragmentDialogRunnable, 5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//
//        if (rootLayout != null && successLabelFragmentDialogRunnable != null) {
//            rootLayout.removeCallbacks(successLabelFragmentDialogRunnable);
//        }
//
//    }
//}
