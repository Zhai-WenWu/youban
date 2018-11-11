package cn.bjhdltcdn.p2plive.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetWheatPropsDataResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.MyPropsListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.RoomReceiveGiftListDialog;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomReceiveGiftListFragment extends BaseFragment implements BaseView {

    private View rootView;
    private MyPropsListAdapter adapter;
    private RecyclerView recyclerView;
    private int pageSize = 20;
    private int pageNumber = 1;
    private int total;
    private View emptyView;
    /**
     * 1 本场为我送礼物； 2 累计为我送礼物
     */
    private int type = 1;

    private ChatRoomPresenter chatRoomPresenter;

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    /**
     * 房间id
     */
    private long roomId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getParentFragment();
        if (fragment instanceof RoomReceiveGiftListDialog) {
            RoomReceiveGiftListDialog dialog = (RoomReceiveGiftListDialog) fragment;
            roomId = dialog.getRoomId();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_room_receive_gift_list_layout, null);
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = rootView.findViewById(R.id.recycle_view);
        emptyView = rootView.findViewById(R.id.empty_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(0.5f)));

        adapter = new MyPropsListAdapter(3);
        recyclerView.setAdapter(adapter);

        // 刷新框架
        TwinklingRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_layout_view);
        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(getContext());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
                pageNumber = 1;
                requestData();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();
                requestData();
            }
        });
    }


    @Override
    protected void onVisible(boolean isInit) {

        if (roomId <= 0) {
            return;
        }

        if (type <= 0) {
            return;
        }

        pageNumber = 1;
        requestData();

    }


    private void requestData() {
        getChatRoomPresenter().getWheatPropsData(roomId, type, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber);
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (InterfaceUrl.URL_GETWHEATPROPSDATA.equals(apiName)) {
            if (object instanceof GetWheatPropsDataResponse) {
                GetWheatPropsDataResponse response = (GetWheatPropsDataResponse) object;
                if (response.getCode() == 200) {
                    total = response.getTotal();
                    if (pageNumber == 1) {
                        adapter.addData(response.getReceiveList());
                    } else {
                        adapter.update(response.getReceiveList());
                    }
                    adapter.notifyDataSetChanged();

                    if (total <= adapter.getItemCount()) {
                    } else {
                        pageNumber++;
                    }
                    if (total == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }

        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter = null;
        }


        if (chatRoomPresenter != null) {
            chatRoomPresenter = null;
        }

        if (rootView != null) {
            ((ViewGroup) rootView).removeAllViews();
            rootView.destroyDrawingCache();
            rootView = null;
        }


    }
}
