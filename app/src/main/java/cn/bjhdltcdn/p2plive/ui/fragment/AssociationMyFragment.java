//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
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
//import java.net.SocketTimeoutException;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.AssociationInfoEditorEvent;
//import cn.bjhdltcdn.p2plive.event.OnPageSelectedEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetJoinOrganListResponse;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.activity.CreateAssociationActivity;
//import cn.bjhdltcdn.p2plive.ui.adapter.MyAssociationAdapter;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 我的圈子
// */
//
//public class AssociationMyFragment extends BaseFragment implements BaseView {
//    private View rootView;
//
//    private RecyclerView recycleView;
//    private MyAssociationAdapter adapter;
//
//    private int pageSize = 20;
//    private int pageNumber = 1;
//
//    private AssociationPresenter presenter;
//    private TwinklingRefreshLayout refreshLayout;
//    private long userId;
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_association_my, null);
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
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        initView();
//
//    }
//
//
//    @Override
//    protected void onVisible(boolean isInit) {
//
//        Logger.d("isInit == " + isInit);
//
//        if (!isInit) {
//            pageNumber = 1;
//            userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//            getPresenter().getJoinOrganList(userId, userId, 0, 1, pageSize, pageNumber, false);
//
//        }
//
//    }
//
//    private void initView() {
//
//        recycleView = rootView.findViewById(R.id.recycle_view);
//        recycleView.setHasFixedSize(true);
//        GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), 2);
//        layoutManager.setAutoMeasureEnabled(true);
//        recycleView.setLayoutManager(layoutManager);
//
//        recycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(15), 2, true));
//
//        adapter = new MyAssociationAdapter(getActivity());
//        recycleView.setAdapter(adapter);
//
//        adapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                Logger.d("position = " + position);
//
//                if (position == 0) {
//                    getPresenter().getJudgeOrganizationCount(userId);
//                }
//            }
//        });
//
//        // 刷新框架
//        refreshLayout = rootView.findViewById(R.id.refresh_layout_view);
//
//        // 头部加载样式
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        // 底部加载样式
//        LoadingView loadingView = new LoadingView(App.getInstance());
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
//                onVisible(false);
//                refreshLayout.finishRefreshing();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//
//                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                getPresenter().getJoinOrganList(userId, userId, 0, 1, pageSize, pageNumber, false);
//
//                refreshLayout.finishLoadmore();
//            }
//        });
//
//
//    }
//
//
//    private AssociationPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new AssociationPresenter(this);
//        }
//        return presenter;
//    }
//
//    private void setData(List<OrganizationInfo> list) {
//
//        if (adapter.getList() != null) {
//            adapter.getList().clear();
//        }
//
//        // 添加第一条item
//        OrganizationInfo organizationInfo = new OrganizationInfo();
//        organizationInfo.setType(-1);
//        adapter.addItem(organizationInfo);
//
//        if (list == null) {
//            return;
//        }
//        adapter.setList(list);
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void callBackEvent(AssociationInfoEditorEvent evnet) {
//
//        if (evnet == null) {
//            return;
//        }
//
//        onVisible(false);
//
//
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void callBackEvent(OnPageSelectedEvent evnet) {
//
//        if (evnet == null) {
//            return;
//        }
//
//        if (evnet.getTabSelectIndex() == 1 && adapter.getItemCount() < 1) {
//            onVisible(false);
//        }
//
//    }
//
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (!isAdded()) {
//            return;
//        }
//
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            if (e instanceof SocketTimeoutException) {
//                Utils.showToastShortTime("网络连接超时");
//                return;
//            }
//            Utils.showToastShortTime(e.getMessage());
//            return;
//        }
//
//        if (InterfaceUrl.URL_GETJOINORGANLIST.equals(apiName)) {
//            if (object instanceof GetJoinOrganListResponse) {
//                GetJoinOrganListResponse response = (GetJoinOrganListResponse) object;
//
//                if (response.getCode() == 200) {
//                    if (pageNumber == 1) {
//                        setData(response.getList());
//                    } else {
//                        adapter.addItemAll(response.getList());
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//                refreshLayout.setEnableLoadmore(true);
//                if (adapter.getItemCount() - 1 < response.getTotal()) {
//                    pageNumber++;
//                } else {
//                    refreshLayout.setEnableLoadmore(false);
//                }
//            }
//        } else if (InterfaceUrl.URL_JUDGEORGANIZATIONCOUNT.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//
//                if (response.getCode() == 200) {
//                    startActivity(new Intent(getActivity(), CreateAssociationActivity.class));
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        }
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
//    }
//
//
//}
