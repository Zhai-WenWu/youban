package cn.bjhdltcdn.p2plive.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.AnonymousChatRoomStatusEvent;
import cn.bjhdltcdn.p2plive.event.AnonymousMsgEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetChatRoomListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateChatRoomResponse;
import cn.bjhdltcdn.p2plive.model.ChatInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.AnonymousChatRoomListAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.HomePostLabelRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Description:匿名聊天室
 * Data: 2018/6/6
 * author: zhudi
 */
public class AnonymousChatRoomFragment extends BaseFragment implements BaseView {
    private View rootView;
    private RecyclerView labelRecyclerView, roomRecyclerView;
    private ChatRoomPresenter chatRoomPresenter;
    private int pageSize = 20, pageNumber = 1;
    private AnonymousChatRoomListAdapter anonymousChatRoomListAdapter;
    private HomePostLabelRecyclerViewAdapter homePostLabelRecyclerViewAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private View emptyView;
    private TextView empty_textView;
    private UserPresenter userPresenter;
    private DiscoverPresenter discoverPresenter;
    private long currentUserId;
    private long labelId;
    /**
     * 加解锁操作位置
     */
    private int lockIndex;
    /**
     * 锁修改完状态
     */
    private int toLock;
    /**
     * 当前用户创建的聊天室
     */
    private ChatInfo chatInfo;

    public ChatInfo getChatInfo() {
        return chatInfo;
    }

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

    public DiscoverPresenter getDiscoverPresenter() {
        if (discoverPresenter == null) {
            discoverPresenter = new DiscoverPresenter(this);
        }
        return discoverPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_list_room_chat, null);
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
        getDiscoverPresenter().getLabelList(12);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (labelId != 0) {
            pageNumber = 1;
            requestData();
        }
    }

    /**
     * 请求数据
     */
    private void requestData() {
        getChatRoomPresenter().getChatRoomList(currentUserId, pageSize, pageNumber, (int) labelId);
    }

    private void init() {
        emptyView = rootView.findViewById(R.id.empty_view);
        labelRecyclerView = rootView.findViewById(R.id.rv_label);
        labelRecyclerView.setHasFixedSize(true);
        homePostLabelRecyclerViewAdapter = new HomePostLabelRecyclerViewAdapter(this.getActivity());
        homePostLabelRecyclerViewAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                LabelInfo labelInfo = (LabelInfo) homePostLabelRecyclerViewAdapter.getItem(position);
                labelInfo.setCheck(true);
                for (int i = 0; i < homePostLabelRecyclerViewAdapter.getList().size(); i++) {
                    LabelInfo labelInfo1 = (LabelInfo) homePostLabelRecyclerViewAdapter.getList().get(i);
                    if (labelInfo1.getLabelId() != labelInfo.getLabelId()) {
                        labelInfo1.setCheck(false);
                    }
                }
                homePostLabelRecyclerViewAdapter.notifyDataSetChanged();
                labelId = labelInfo.getLabelId();
                pageNumber = 1;
                requestData();
            }
        });
        roomRecyclerView = rootView.findViewById(R.id.recycler_room);
        roomRecyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(10)));
        empty_textView = rootView.findViewById(R.id.empty_textView);
        anonymousChatRoomListAdapter = new AnonymousChatRoomListAdapter();
        roomRecyclerView.setAdapter(anonymousChatRoomListAdapter);
        anonymousChatRoomListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Utils.isAllowClick()) {
                    final ChatInfo chatInfo = anonymousChatRoomListAdapter.getList_room().get(position);
                    if (chatInfo.getUserRole() == 1 || chatInfo.getIsLock() == 0) {
                        if (Constants.Object.CHATINFO != null && !Constants.Object.CHATINFO.getChatId().equals(chatInfo.getChatId())) {
                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                            dialog.setText("是否进入此聊天室", "进入该聊天室则代表退出之前所在聊天室", "否", "是");
                            dialog.setCloseViewShow(true);
                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                                @Override
                                public void onLeftClick() {
                                }

                                @Override
                                public void onRightClick() {
                                    getChatRoomPresenter().updateChatRoomUser(currentUserId, 0, chatInfo.getChatId(), 0);
                                }
                            });
                            dialog.show(getFragmentManager());
                        } else {
                            getChatRoomPresenter().updateChatRoomUser(currentUserId, 0, chatInfo.getChatId(), 0);
                        }
                    } else if (chatInfo.getIsLock() == 1) {
                        if (Constants.Object.CHATINFO != null && Constants.Object.CHATINFO.getChatId().equals(chatInfo.getChatId())) {
                            getChatRoomPresenter().updateChatRoomUser(currentUserId, 0, chatInfo.getChatId(), 0);
                        } else {
                            Utils.showToastShortTime("房主已锁门");
                        }
                    }
                }

            }
        });
        anonymousChatRoomListAdapter.setOnClick(new AnonymousChatRoomListAdapter.onClick() {
            @Override
            public void onClick(int position, int isLock) {
                if (Utils.isAllowClick()) {
                    lockIndex = position;
                    ChatInfo chatInfo = anonymousChatRoomListAdapter.getList_room().get(position);
                    if (isLock == 0) {
                        toLock = 1;
                    } else if (isLock == 1) {
                        toLock = 0;
                    }
                    getChatRoomPresenter().updateChatRoomLock(currentUserId, chatInfo.getChatId(), toLock);
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
            case InterfaceUrl.URL_GETCHATROOMLIST:
                if (object instanceof GetChatRoomListResponse) {
                    GetChatRoomListResponse getChatRoomListResponse = (GetChatRoomListResponse) object;
                    if (getChatRoomListResponse.getCode() == 200) {
                        if (!StringUtils.isEmpty(getChatRoomListResponse.getBlankHint())) {
                            empty_textView.setText(getChatRoomListResponse.getBlankHint());
                        }
                        chatInfo = getChatRoomListResponse.getChatInfo();
                        int total = getChatRoomListResponse.getTotal();
                        if (pageNumber == 1) {
                            anonymousChatRoomListAdapter.setDate(getChatRoomListResponse.getChatList());
                        } else {
                            anonymousChatRoomListAdapter.updateList(getChatRoomListResponse.getChatList());
                        }
                        if (total <= anonymousChatRoomListAdapter.getItemCount()) {
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
            case InterfaceUrl.URL_UPDATECHATROOMUSER://进入聊天室
                if (object instanceof UpdateChatRoomResponse) {
                    UpdateChatRoomResponse response = (UpdateChatRoomResponse) object;
                    if (response.getCode() == 200) {
                        final ChatInfo chatInfo = response.getChatInfo();
                        if (chatInfo != null) {
                            Constants.Object.CHATINFO = chatInfo;
                            RongIMutils.joinChatRoom(getActivity(), chatInfo.getChatId(), false);
                            EventBus.getDefault().post(new AnonymousChatRoomStatusEvent(true));
                            UserInfo userInfo = new UserInfo(String.valueOf(chatInfo.getToBaseUser().getUserId()), chatInfo.getToBaseUser().getNickName(), Uri.parse(chatInfo.getToBaseUser().getUserIcon()));
                            RongIM.getInstance().refreshUserInfoCache(userInfo);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new AnonymousMsgEvent(chatInfo.getToBaseUser(), null));
                                    if (!SafeSharePreferenceUtils.getBoolean(Constants.Fields.IS_ANONYMOUS_CHATROOM_TOAST_SHOW, false)) {
                                        SafeSharePreferenceUtils.saveBoolean(Constants.Fields.IS_ANONYMOUS_CHATROOM_TOAST_SHOW, true);
                                        Utils.showToastShortTime("您进入的是匿名聊天房间，头像和昵称随机生成，在此聊天室请您注意言语措辞！");
                                    }
                                }
                            }, 1000);
                            Utils.showToastShortTime(response.getMsg());

                        }
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATECHATROOMLOCK:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {
                        anonymousChatRoomListAdapter.getList_room().get(lockIndex).setIsLock(toLock);
                        anonymousChatRoomListAdapter.notifyItemChanged(lockIndex);
                    }
                    Utils.showToastShortTime(response.getMsg());
                }
                break;
            case InterfaceUrl.URL_GETLABELLIST:
                if (object instanceof GetLabelListResponse) {
                    GetLabelListResponse getLabelListResponse = (GetLabelListResponse) object;
                    if (getLabelListResponse.getCode() == 200) {
                        List<LabelInfo> labelInfoList = getLabelListResponse.getLabelList();
                        if (labelInfoList != null && labelInfoList.size() > 0) {
                            labelInfoList.get(0).setCheck(true);
                            labelId = labelInfoList.get(0).getLabelId();
                            List<Object> list = new ArrayList<>(1);
                            list.addAll(labelInfoList);
                            homePostLabelRecyclerViewAdapter.setList(list);
                            homePostLabelRecyclerViewAdapter.notifyDataSetChanged();
                            if (homePostLabelRecyclerViewAdapter.getItemCount() > 0) {
                                GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), labelInfoList.size());
                                labelRecyclerView.setLayoutManager(layoutManager);
                                labelRecyclerView.setAdapter(homePostLabelRecyclerViewAdapter);
                                labelRecyclerView.setVisibility(View.VISIBLE);
                                requestData();
                            } else {
                                labelRecyclerView.setVisibility(View.GONE);
                            }
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
