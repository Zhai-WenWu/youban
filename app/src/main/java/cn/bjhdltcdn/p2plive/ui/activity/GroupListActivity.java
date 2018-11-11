package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.SocketTimeoutException;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.GroupOperationEvent;
import cn.bjhdltcdn.p2plive.event.JoinGroupEvent;
import cn.bjhdltcdn.p2plive.httpresponse.GetGroupListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JoinGroupResponse;
import cn.bjhdltcdn.p2plive.httpresponse.getJoinGroupListResponse;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.PlayInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.GroupListItemAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.GroupSharedItemDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 群组列表页面
 */
public class GroupListActivity extends BaseActivity implements BaseView {

    private RecyclerView recyclerView;
    private GroupListItemAdapter adapter;

    private GroupPresenter presenter;
    /**
     * 0 分享和正常进入
     * 1 我加入的群组
     * 2 Ta加入的群组
     */
    private int type = 0;
    private int pageSize = 10;
    private int pageNumber = 1;
    private int groupItemPosition;
    /**
     * 成员进群方式(1直接入群,2申请进群),
     */
    private int groupMode;

    /**
     * 分享的群信息
     */
    private Group sharedGroup;
    private Object object;
    private View emptyView;
    private long toUserId;
    private TextView empty_tv;
    private TwinklingRefreshLayout refreshLayout;
    public int mIsExistGroup;

    public GroupPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GroupPresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list_layout);

        EventBus.getDefault().register(this);

        object = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);
        toUserId = getIntent().getLongExtra(Constants.Fields.TO_USER_ID, 0);
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);

        setTitle();
        initView();

    }


    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        if (type == 1) {
            titleFragment.setTitleView("我加入的群组");
            getPresenter().getJoinGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber, true);
        } else if (type == 2) {
            titleFragment.setTitleView("Ta加入的群组");
            getPresenter().getJoinGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toUserId, pageSize, pageNumber, true);
        } else {
            titleFragment.setTitleView("群组列表");
            getPresenter().getGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), true);
        }
    }


    private void initView() {

        recyclerView = findViewById(R.id.recycle_view);
        empty_tv = findViewById(R.id.empty_textView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1)));

        adapter = new GroupListItemAdapter();
        if (type == 2) {
            adapter.setItemAddClick(new GroupListItemAdapter.OnItemClick() {


                @Override
                public void ItemClick(int isExistGroup, int position) {
                    mIsExistGroup = isExistGroup;
                    groupItemPosition = position;
                    List<Group> list = adapter.getList();
                    final Group group = list.get(position);
                    groupMode = group.getGroupMode();
                    if (isExistGroup == 0) {
                        if (groupMode == 2) {
                            DelectTipDialog dialog = new DelectTipDialog();
                            dialog.setTitleStr("本群为私密群，需申请同意后才能加入，现在要申请入群吗？");
                            dialog.setRightButtonStr("申请进群");
                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
                                @Override
                                public void itemClick() {
                                    getPresenter().joinGroup(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), group.getGroupId(), group.getGroupMode());
                                }
                            });
                            dialog.show(getSupportFragmentManager());
                        } else {
                            getPresenter().joinGroup(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), group.getGroupId(), group.getGroupMode());
                        }
                    } else if (isExistGroup == 1) {
                        RongIMutils.startGroupChat(GroupListActivity.this, group.getGroupId() + "", group.getGroupName());
                    }
                }
            });
        }
        adapter.setType(type);

        recyclerView.setAdapter(adapter);


        adapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {

                //30005 群分享,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白

                final Group group = adapter.getList().get(position);
                if (object == null && type != 2) {
                    RongIMutils.startGroupChat(GroupListActivity.this, group.getGroupId() + "", group.getGroupName());
                    return;
                } else if (type == 2 && group.getIsExistGroup() == 1) {
                    startActivity(new Intent(GroupListActivity.this, GroupDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, String.valueOf(group.getGroupId())));
                    return;
                }

                Logger.d("position == " + position + " ==object== " + object);

                if (object instanceof Group) {
                    sharedGroup = (Group) object;
                    if (group.getGroupId() == sharedGroup.getGroupId()) {
                        Utils.showToastShortTime("不能选择当前要分享的群");
                        return;
                    }

                    showDialog(object, 30005, object, position);
                } else if (object instanceof PostInfo) {
                    showDialog("分享给该群一个帖子", 11, object, position);
                } else if (object instanceof OrganizationInfo) {
                    showDialog("分享给该群一个圈子", 12, object, position);
                } else if (object instanceof ActivityInfo) {
                    showDialog("分享给该群一个活动", 13, object, position);
                } else if (object instanceof RoomInfo) {
                    showDialog("分享给该群一个聊天频道", 14, object, position);
                } else if (object instanceof PlayInfo) {
                    showDialog("分享给该群一个PK挑战", 15, object, position);
                } else if (object instanceof SayLoveInfo) {
                    showDialog("分享给该群一个校园表白", 18, object, position);
                }

            }
        });

        // 刷新框架
        refreshLayout = findViewById(R.id.refresh_layout_view);
        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
                pageNumber = 1;
                if (type == 1) {
                    getPresenter().getJoinGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber, false);
                } else if (type == 2) {
                    getPresenter().getJoinGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toUserId, pageSize, pageNumber, false);
                } else {
                    getPresenter().getGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), false);
                }
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();
                pageNumber += 1;
                if (type == 1) {
                    getPresenter().getJoinGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber, false);
                } else if (type == 2) {
                    getPresenter().getJoinGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toUserId, pageSize, pageNumber, false);
                } else {
                    getPresenter().getGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), false);
                }
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(GroupOperationEvent evnet) {

        if (evnet == null) {
            return;
        }

        Logger.d("群操作 === " + evnet.getOperationType());

        if (evnet.getOperationType() == 1 || evnet.getOperationType() == 2) {

            // 删除此群在聊天列表的item
            RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, evnet.getGroupId() + "", new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    Logger.d("aBoolean === " + aBoolean);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Logger.d("errorCode === " + errorCode.getMessage() + " ; errorValue === " + errorCode.getValue());
                }
            });

            pageNumber = 1;

            if (type == 1) {
                getPresenter().getJoinGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber, false);
            } else if (type == 2) {
                getPresenter().getJoinGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toUserId, pageSize, pageNumber, false);
            } else {
                getPresenter().getGroupList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), false);
            }
        }

    }

    @Override
    public void updateView(String apiName, Object object) {

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            if (e instanceof SocketTimeoutException) {
                Utils.showToastShortTime("网络连接超时");
                return;
            }
            Utils.showToastShortTime(e.getMessage());
            return;
        }

        if (InterfaceUrl.URL_GETGROUPLIST.equals(apiName)) {
            if (object instanceof GetGroupListResponse) {
                GetGroupListResponse response = (GetGroupListResponse) object;

                if (response.getCode() == 200) {
                    if (!TextUtils.isEmpty(response.getBlankHint())) {
                        empty_tv.setText(response.getBlankHint());
                    }


                    if (response.getGroupList() == null || response.getGroupList().size() == 0) {

                        //没有更多数据时  下拉刷新不可用
                        refreshLayout.setEnableLoadmore(false);

                    } else {
                        //有更多数据时  下拉刷新才可用
                        refreshLayout.setEnableLoadmore(true);

                        adapter.setList(response.getGroupList());
                    }


                } else {
                    Utils.showToastShortTime(response.getMsg());
                }

                initEmptyView(adapter.getItemCount() == 0 ? true : false, response.getBlankHint());


            }
        } else if (InterfaceUrl.URL_GETJOINGROUPLIST.equals(apiName)) {
            if (object instanceof getJoinGroupListResponse) {
                getJoinGroupListResponse response = (getJoinGroupListResponse) object;

                if (response.getCode() == 200) {
                    if (!TextUtils.isEmpty(response.getBlankHint())) {
                        empty_tv.setText(response.getBlankHint());
                    }
                    if (pageNumber == 1) {
                        adapter.setList(response.getList());
                    } else {
                        adapter.addDataList(response.getList());
                    }

                    if (response.getTotal() <= pageNumber * pageSize) {
                        //没有更多数据时  下拉刷新不可用
                        refreshLayout.setEnableLoadmore(false);
                    } else {
                        //有更多数据时  下拉刷新才可用
                        refreshLayout.setEnableLoadmore(true);
                    }
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
                if (response.getList().size() == 0) {
                    initEmptyView(adapter.getItemCount() == 0 ? true : false, response.getBlankHint());
                }

            }
        } else if (InterfaceUrl.URL_JOIN_GROUP.equals(apiName)) {
            if (object instanceof JoinGroupResponse) {
                JoinGroupResponse baseResponse = (JoinGroupResponse) object;
                if (baseResponse.getCode() == 200) {
                    if (groupItemPosition <= 2) {
                        Utils.showToastShortTime(baseResponse.getMsg());//通知用户详情更新
                    }
                    EventBus.getDefault().post(new JoinGroupEvent(groupItemPosition, mIsExistGroup));
                    if (groupMode == 2) {
                        //申请中
                        adapter.setExistGroup(groupItemPosition, 2);
                    } else {
                        //发起群聊
                        adapter.setExistGroup(groupItemPosition, 1);
                    }
                } else {
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
            }
        }

    }

    /**
     * 提示语
     *
     * @param isVisibility
     * @param blankHint
     */
    private void initEmptyView(boolean isVisibility, String blankHint) {
        if (emptyView == null) {
            emptyView = findViewById(R.id.empty_view);
        }

        if (!StringUtils.isEmpty(blankHint)) {
            TextView textView = emptyView.findViewById(R.id.empty_textView);
            textView.setText(blankHint);
        }


        emptyView.setVisibility(isVisibility ? View.VISIBLE : View.GONE);

    }

    private void showDialog(Object message, final int messageType, final Object sharedObject, int position) {

        //30005 群分享,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白
        final Group group = adapter.getList().get(position);
        if (sharedObject != null) {
            GroupSharedItemDialog dialog = new GroupSharedItemDialog();
            if (message instanceof String) {
                dialog.setShareStr(((String) message));
            } else if (message instanceof Group) {
                dialog.setGroup(((Group) message));
            }

            dialog.setSelectObject(group);

            dialog.setItemClickListener(new GroupSharedItemDialog.ItemClickListener() {
                @Override
                public void onClick(Object selectObject) {

                    if (selectObject == null) {
                        return;
                    }

                    String pushContent = null;
                    switch (messageType) {
                        case 11:
                            pushContent = "分享一个帖子";
                            break;
                        case 12:
                            pushContent = "分享一个圈子";
                            break;

                        case 13:
                            pushContent = "分享一个活动";
                            break;

                        case 14:
                            pushContent = "分享一个聊天频道";

                            break;
                        case 15:
                            pushContent = "分享一个PK挑战";
                            break;

                        case 18:
                            pushContent = "分享一个表白";
                            break;

                        default:
                            pushContent = "";
                            break;
                    }

                    RongIMutils.sendSharedMessage(pushContent, messageType, selectObject, sharedObject);
                    finish();
                }
            });

            dialog.show(getSupportFragmentManager());

            return;


        }

    }

    @Override
    public void showLoading() {

        ProgressDialogUtils.getInstance().showProgressDialog(this);

    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

    }
}
