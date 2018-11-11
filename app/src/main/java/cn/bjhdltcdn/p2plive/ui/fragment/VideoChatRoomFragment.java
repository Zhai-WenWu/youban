package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.FindRoomDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserAuthStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetRoomListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateUserStatusResponse;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.GroupVideoActivity;
import cn.bjhdltcdn.p2plive.ui.activity.CreateVideoChatRoomActivity;
import cn.bjhdltcdn.p2plive.ui.activity.OpenRoomSettingPassWordActivity;
import cn.bjhdltcdn.p2plive.ui.activity.RealNameCertificationActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.VideoChatRoomListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.RealNameDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * Description:多人视频聊天室
 * Data:2018/6/6
 * author: zhudi
 */
public class VideoChatRoomFragment extends BaseFragment implements BaseView {
    private View rootView;
    private RecyclerView roomRecyclerView;
    private ChatRoomPresenter chatRoomPresenter;
    private int pageSize = 20, pageNumber = 1;
    private VideoChatRoomListAdapter videoChatRoomListAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private View emptyView;
    private TextView empty_textView;
    private UserPresenter userPresenter;
    private long currentUserId;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_list_room, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        currentUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNumber = 1;
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        getChatRoomPresenter().getRoomList(currentUserId, pageSize, pageNumber);
    }

    private void init() {
        emptyView = rootView.findViewById(R.id.empty_view);
        roomRecyclerView = rootView.findViewById(R.id.recycler_room);
        empty_textView = rootView.findViewById(R.id.empty_textView);
        videoChatRoomListAdapter = new VideoChatRoomListAdapter();
        roomRecyclerView.setAdapter(videoChatRoomListAdapter);
        videoChatRoomListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Utils.isAllowClick()) {
                    RoomInfo roomInfo = videoChatRoomListAdapter.getList_room().get(position);
                    if (roomInfo.getUserRole() == 1 || roomInfo.getPasswordType() == 0) {//不加密
                        getChatRoomPresenter().updateUserStatus(currentUserId, roomInfo.getRoomId(), 0, roomInfo.getPasswordType(),
                                roomInfo.getPassword());
                    } else if (roomInfo.getPasswordType() == 1) {//加密
                        getChatRoomPresenter().findRoomDetail(currentUserId, roomInfo.getRoomId());
                    }
                }

            }
        });
        // 刷新框架
        refreshLayout = rootView.findViewById(R.id.refresh_layout_view);
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
        refreshLayout.setTargetView(roomRecyclerView);//设置滚动事件的作用对象。
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
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETROOMLIST:
                if (object instanceof GetRoomListResponse) {
                    GetRoomListResponse getRoomListResponse = (GetRoomListResponse) object;
                    if (getRoomListResponse.getCode() == 200) {
                        if (!StringUtils.isEmpty(getRoomListResponse.getBlankHint())) {
                            empty_textView.setText(getRoomListResponse.getBlankHint());
                        }
                        int total = getRoomListResponse.getTotal();
                        if (pageNumber == 1) {
                            videoChatRoomListAdapter.setDate(getRoomListResponse.getRoomList());
                        } else {
                            videoChatRoomListAdapter.updateList(getRoomListResponse.getRoomList());
                        }
                        if (total <= videoChatRoomListAdapter.getItemCount()) {
                            refreshLayout.setEnableLoadmore(false);
                        } else {
                            refreshLayout.setEnableLoadmore(true);
                            pageNumber++;
                        }
                        if (total == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATEUSERSTATUS://进入房间
                if (object instanceof UpdateUserStatusResponse) {
                    UpdateUserStatusResponse response = (UpdateUserStatusResponse) object;
                    if (response.getCode() == 200) {
                        Intent intent = new Intent(getContext(), GroupVideoActivity.class);
                        intent.putExtra(Constants.Fields.ROOMINFO, response.getRoomInfo());
                        startActivity(intent);
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_FINDUSERAUTHSTATUS:
                if (object instanceof FindUserAuthStatusResponse) {
                    FindUserAuthStatusResponse findUserAuthStatusResponse = (FindUserAuthStatusResponse) object;
                    if (findUserAuthStatusResponse.getCode() == 200) {
                        int authStatus = findUserAuthStatusResponse.getAuthStatus();
                        if (authStatus == 2) {//未实名认证
                            startActivity(new Intent(getContext(), CreateVideoChatRoomActivity.class));
                        } else if (authStatus == 0 || authStatus == 3) {
                            RealNameDialog realNameDialog = new RealNameDialog();
                            realNameDialog.setItemClick(new RealNameDialog.ItemClick() {
                                @Override
                                public void itemClick() {
                                    startActivity(new Intent(getContext(), RealNameCertificationActivity.class));
                                }
                            });
                            realNameDialog.show(getFragmentManager());
                        } else if (authStatus == 1) {
                            Utils.showToastShortTime("实名认证审核中，请等待审核结果");
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_FINDROOMDETAIL:
                if (object instanceof FindRoomDetailResponse) {
                    FindRoomDetailResponse findRoomDetailResponse = (FindRoomDetailResponse) object;
                    if (findRoomDetailResponse.getCode() == 200) {
                        RoomInfo roomInfo = findRoomDetailResponse.getRoomInfo();
                        if (roomInfo.getStatus() == 0) {
                            Intent intent = new Intent(getContext(), OpenRoomSettingPassWordActivity.class);
                            intent.putExtra(Constants.Fields.TYPE, 2);
                            intent.putExtra(Constants.Fields.ROOMINFO, roomInfo);
                            startActivity(intent);
                        } else {
                            Utils.showToastShortTime("该聊天频道已关闭");
                        }
                    }
                }
                break;
            default:
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onVisible(boolean isInit) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
