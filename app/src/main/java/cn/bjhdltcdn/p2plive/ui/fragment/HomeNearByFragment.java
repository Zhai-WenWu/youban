//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.UpdateHomeNewEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetNewCountListResponse;
//import cn.bjhdltcdn.p2plive.model.CountInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetHomeNearbyPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.activity.ActiveListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ClassMateHelpListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.NearByOrganListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.SayLoveListActivity;
//import cn.bjhdltcdn.p2plive.ui.adapter.HomeNearByRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.GlideCacheUtil;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
///**
// * Created by Hu_PC on 2017/11/8.
// * 首页附近
// */
//
//public class HomeNearByFragment extends BaseFragment implements BaseView {
//    private View rootView;
//    private AppCompatActivity mActivity;
//    private GetHomeNearbyPresenter presenter;
//    private RecyclerView recycleView;
//    private HomeNearByRecyclerViewAdapter recyclerAdapter;
//    private View emptyView;
//    // 下拉刷新
//    private TwinklingRefreshLayout refreshLayout;
//    private int currenPosition;
//    private long userId;
//    private int newAllCount, newUpdateCount;
//
//    private GetHomeNearbyPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new GetHomeNearbyPresenter(this);
//        }
//        return presenter;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mActivity = (AppCompatActivity) context;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_home_nearby, null);
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
//        GlideCacheUtil.clearImageAllCache(mActivity);
//        initView();
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//    }
//
//    @Override
//    protected void onVisible(boolean isInit) {
////        if(isInit){
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        getPresenter().getNewCountList(userId);
////        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getPresenter().getNewCountList(userId);
//    }
//
//    private void initView() {
//        emptyView = rootView.findViewById(R.id.empty_view);
//        refreshLayout = (TwinklingRefreshLayout) rootView.findViewById(R.id.refresh_layout_view);
//        recycleView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
//
//        recyclerAdapter = new HomeNearByRecyclerViewAdapter(mActivity);
//        recycleView.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 2);
//        recycleView.setLayoutManager(gridLayoutManager);
//        recycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(13), 2, false));
//        recycleView.setAdapter(recyclerAdapter);
//        recyclerAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳转到详情页
//                currenPosition = position;
//                CountInfo countInfo = recyclerAdapter.getItem(position);
//                int type = countInfo.getType();
//                Intent intent;
//                switch (type) {
//                    case 1:
//                        //线下活动
//                        newUpdateCount = countInfo.getUpdateCount();
//                        intent = new Intent(mActivity, ActiveListActivity.class);
//                        intent.putExtra(Constants.Fields.COME_IN_TYPE, 1);
//                        intent.putExtra(Constants.Fields.TO_USER_ID, 0);
//                        startActivity(intent);
//                        break;
//                    case 2:
//                        //附近圈子
//                        newUpdateCount = countInfo.getUpdateCount();
//                        startActivity(new Intent(mActivity, NearByOrganListActivity.class));
//                        break;
//                    case 3:
//                        //附近表白
//                        newUpdateCount = countInfo.getUpdateCount();
//                        intent = new Intent(mActivity, SayLoveListActivity.class);
//                        intent.putExtra(Constants.Fields.USER_ID, 0);
//                        intent.putExtra("comeInType", 1);
//                        startActivity(intent);
//                        break;
//                    case 4:
//                        //同学帮帮忙
//                        newUpdateCount = countInfo.getUpdateCount();
//                        intent = new Intent(getContext(), ClassMateHelpListActivity.class);
//                        intent.putExtra(Constants.Fields.TYPE, 1);
//                        startActivity(intent);
//                        break;
//                    default:
//                        break;
//                }
//                //更新刷新时间
//                getPresenter().updateRefreshTime(userId, type);
//
//            }
//        });
//
//
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(recycleView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//        refreshLayout.setEnableLoadmore(false);
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                getPresenter().getNewCountList(userId);
//                refreshLayout.finishRefreshing();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//            }
//        });
//        emptyView.setVisibility(View.GONE);
//    }
//
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (apiName.equals(InterfaceUrl.URL_GETNEWCOUNTLIST)) {
//            if (object instanceof GetNewCountListResponse) {
//                GetNewCountListResponse getNewCountListResponse = (GetNewCountListResponse) object;
//                if (getNewCountListResponse.getCode() == 200) {
//                    List<CountInfo> countInfoList = getNewCountListResponse.getList();
//                    if (countInfoList != null && countInfoList.size() > 0) {
//                        recyclerAdapter.setList(countInfoList);
//                        recyclerAdapter.notifyDataSetChanged();
//                        emptyView.setVisibility(View.GONE);
//                        newAllCount = 0;
//                        for (int i = 0; i < countInfoList.size(); i++) {
//                            CountInfo countInfo = countInfoList.get(i);
//                            newAllCount += countInfo.getUpdateCount();
//                        }
//                    } else {
//                        emptyView.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    Utils.showToastShortTime(getNewCountListResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_UPDATEREFRESHTIME)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse baseResponse = (BaseResponse) object;
//                if (baseResponse.getCode() == 200) {
//                    recyclerAdapter.getItem(currenPosition).setUpdateCount(0);
//                    recyclerAdapter.notifyDataSetChanged();
//                    newAllCount = newAllCount - newUpdateCount;
//                    if (newAllCount <= 0) {
//                        //清除附近小红点
//                        EventBus.getDefault().post(new UpdateHomeNewEvent(2));
//                    }
//                }
//            }
//        }
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
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//}
