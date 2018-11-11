//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
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
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.AssociationBlackListEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetBanningCommentsListResponse;
//import cn.bjhdltcdn.p2plive.model.OrganMember;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationBlackListAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
//
///**
// * 圈子中禁言的用户
// */
//public class AssociationBannedPostUsersFragment extends BaseFragment implements BaseView {
//    private View rootView;
//    private final int pageSize = 20;
//    private int pageNumber = 1;
//    private long organId;
//    private AssociationBlackListAdapter adapter;
//    private AssociationPresenter mPresenter;
//    private RecyclerView recyclerView;
//    private int AssociationBlackListType = 1;//1为禁言 2踢出圈子
//    private View emptyView;
//    private TextView empty_tv;
//    private TwinklingRefreshLayout refreshLayout;
//    private int position = -1;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        organId = getActivity().getIntent().getLongExtra(Constants.Fields.ORGAN_ID, 0);
//        Logger.d("organId === " + organId);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_association_black_list, null);
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
//
//        init();
//    }
//
//    private void init() {
//        recyclerView = rootView.findViewById(R.id.recycle_view);
//        emptyView = rootView.findViewById(R.id.empty_view);
//        empty_tv = rootView.findViewById(R.id.empty_textView);
//        empty_tv.setText("暂无禁言用户");
//        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(0.5f)));
//
//        adapter = new AssociationBlackListAdapter(AssociationBlackListType);
//        recyclerView.setAdapter(adapter);
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
//        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setEnableLoadmore(false);
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                refreshLayout.finishRefreshing();
//                pageNumber = 1;
//                requestData();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                refreshLayout.finishLoadmore();
//                requestData();
//            }
//        });
//    }
//
//
//    @Override
//    protected void onVisible(boolean isInit) {
//        if (isInit) {
//            getmPresenter().getBanningCommentsList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organId, pageSize, pageNumber, true);
//        }
//    }
//
//    public AssociationPresenter getmPresenter() {
//        if (mPresenter == null) {
//            mPresenter = new AssociationPresenter(this);
//        }
//        return mPresenter;
//    }
//
//    /**
//     * 请求接收礼物数据
//     */
//    private void requestData() {
//        getmPresenter().getBanningCommentsList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organId, pageSize, pageNumber, false);
//    }
//
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        switch (apiName) {
//            case InterfaceUrl.URL_GETBANNINGCOMMENTSLIST:
//                if (object instanceof GetBanningCommentsListResponse) {
//                    GetBanningCommentsListResponse getBanningCommentsListResponse = (GetBanningCommentsListResponse) object;
//
//                    if (!TextUtils.isEmpty(getBanningCommentsListResponse.getBlankHint())) {
//                        empty_tv.setText(getBanningCommentsListResponse.getBlankHint());
//                    }
//
//                    if (getBanningCommentsListResponse.getCode() == 200) {
//                        if (getBanningCommentsListResponse.getTotal() == 0) {
//                            emptyView.setVisibility(View.VISIBLE);
//                        } else {
//                            emptyView.setVisibility(View.GONE);
//                            if (pageNumber == 1) {
//                                adapter.setData(getBanningCommentsListResponse.getMemberList());
//                            } else {
//                                adapter.addData(getBanningCommentsListResponse.getMemberList());
//                            }
//                            if (getBanningCommentsListResponse.getTotal() <= adapter.getItemCount() - 1) {
//                                //加载完
//                                refreshLayout.setEnableLoadmore(true);
//                            } else {
//                                refreshLayout.setEnableLoadmore(false);
//                            }
//                        }
//                    }
//                }
//                break;
//            case InterfaceUrl.URL_ORGANBANNINGCOMMENTS:
//                if (object instanceof BaseResponse) {
//                    BaseResponse baseResponse = (BaseResponse) object;
//                    if (baseResponse.getCode() == 200) {
//
//                        if (position > -1 && adapter.getItemCount() > position) {
//                            adapter.getmList().remove(position);
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        if (adapter.getItemCount() <= 1) {
//                            emptyView.setVisibility(View.VISIBLE);
//                        }
//
////                        pageNumber = 1;
////                        getmPresenter().getBanningCommentsList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organId, pageSize, pageNumber,true);
//                    }
//                }
//                break;
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventCallBack(AssociationBlackListEvent event) {
//        if (event == null) {
//            return;
//        }
//
//        position = event.getPosition();
//
//        if (event.getType() == AssociationBlackListType) {
//            final OrganMember organMember = event.getOrganMember();
//            DelectTipDialog delectTipDialog = new DelectTipDialog();
//            delectTipDialog.setTitleStr("确定解除禁言？恢复Ta的发帖和发布评论功能。");
//            delectTipDialog.setRightButtonStr("解禁");
//            delectTipDialog.setItemClick(new DelectTipDialog.ItemClick() {
//                @Override
//                public void itemClick() {
//                    getmPresenter().organBanningComments(organMember.getOrganId(), 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organMember.getBaseUser().getUserId());
//                }
//            });
//            delectTipDialog.show(getFragmentManager());
//        }
//    }
//
//    @Override
//    public void showLoading() {
//
//        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
//
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//
//        if (adapter != null) {
//            adapter = null;
//        }
//
//        if (mPresenter != null) {
//            mPresenter = null;
//        }
//
//        if (rootView != null) {
//            ((ViewGroup) rootView).removeAllViews();
//            rootView.destroyDrawingCache();
//            rootView = null;
//        }
//    }
//}
