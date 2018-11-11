package cn.bjhdltcdn.p2plive.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;
import com.tubb.smrv.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.RongyunReceiveUnreadCountEvent;
import cn.bjhdltcdn.p2plive.greendao.PushdbModelDao;
import cn.bjhdltcdn.p2plive.httpresponse.CheckIsDeleteCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.HelpCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SayLoveCommentResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.GroupApply;
import cn.bjhdltcdn.p2plive.model.HelpInfo;
import cn.bjhdltcdn.p2plive.model.OrganApply;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.YouBanActivityMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanGroupMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanHelpInfoMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanOrganizationMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanSayLoveInfoMessageModel;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.PostDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.NotifyListItemAdapter;
import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * 通知消息页面
 */
public class LocalNotifyMessageListFragment extends BaseFragment implements BaseView, ViewTreeObserver.OnGlobalLayoutListener {


    private View rootView;

    private SwipeMenuRecyclerView recycleView;
    private NotifyListItemAdapter adapter;

    private int pageNumber = 1;
    private int pageSize = 20;

    /**
     * 1 通知
     * 2 回复
     * 3 待处理
     */
    private int type;
    private View emptyView;


    private EditText editText;
    private TextView sendTextView;
    private View sendView;

    // 回复item数据
    private Object replyItemObject;
    private PostCommentListPresenter postPresenter;
    private Switch anonymousSwitch;
    //当前登录用户的id
    private long currentBaseUserId;

    public PostCommentListPresenter getPostPresenter() {

        if (postPresenter == null) {
            postPresenter = new PostCommentListPresenter(this);
        }
        return postPresenter;
    }

    private UserPresenter userPresenter;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    private GetCommentListPresenter getCommentListPresenter;

    public GetCommentListPresenter getGetCommentListPresenter() {
        if (getCommentListPresenter == null) {
            getCommentListPresenter = new GetCommentListPresenter(this);
        }
        return getCommentListPresenter;
    }

    public static LocalNotifyMessageListFragment newInstance(int type) {
        LocalNotifyMessageListFragment fragment = new LocalNotifyMessageListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY.KEY_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt(Constants.KEY.KEY_TYPE, 0);
        }

        Logger.d("type ====>>>>> " + type);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_notify_list_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;

    }

    @Override
    protected void onVisible(boolean isInit) {

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        initView();
        initSendView();

        loadData();

    }

    private void initView() {

        recycleView = rootView.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        recycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1), true));
        recycleView.setLayoutManager(layoutManager);

        adapter = new NotifyListItemAdapter((AppCompatActivity) getActivity());
        recycleView.setAdapter(adapter);
        adapter.setUserIconClickListener(new NotifyListItemAdapter.UserIconClickListener() {
            @Override
            public void onClick(BaseUser baseUser, boolean isAnonymous) {
                if (baseUser != null) {
                    if (isAnonymous) {
                        getUserPresenter().getAnonymityUser(currentBaseUserId, baseUser.getUserId());
                    } else {

                        startActivity(new Intent(getActivity(), UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                    }
                }
            }
        });

        adapter.setItemClickListener(new NotifyListItemAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Object object) {
                if (object instanceof YouBanActivityMessageModel) {// 活动

                } else if (object instanceof YouBanOrganizationMessageModel) {// 圈子

                    YouBanOrganizationMessageModel youBanOrganizationMessageModel = (YouBanOrganizationMessageModel) object;
                    /**
                     * 10000：申请
                     * 10002：拒绝
                     * 10003：同意
                     * 10005：评论
                     * 10006回复
                     */
                    switch (youBanOrganizationMessageModel.getMessageType()) {
                        case 10003:
                            OrganApply organApply = youBanOrganizationMessageModel.getOrganApply();
                            if (organApply != null) {
                                OrganizationInfo organizationInfo = new OrganizationInfo();
                                organizationInfo.setOrganName(organApply.getOrganName());
                                organizationInfo.setOrganId(organApply.getOrganId());
                                organizationInfo.setOrganImg(organApply.getOrganImg());
                            }

                            break;

                        case 10005:
                        case 10006:
                            PostInfo postInfo = new PostInfo();
                            postInfo.setPostId(youBanOrganizationMessageModel.getPostInfoId());
                            startActivity(new Intent(getActivity(), PostDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, postInfo).putExtra(Constants.Fields.COME_IN_TYPE, 4));

                            break;

                    }


                } else if (object instanceof YouBanGroupMessageModel) {// 群

                    YouBanGroupMessageModel youBanGroupMessageModel = (YouBanGroupMessageModel) object;
                    // 群同意消息才能跳转
                    if (youBanGroupMessageModel.getMessageType() == 30002) {
                        GroupApply groupApply = youBanGroupMessageModel.getGroupApply();
                        if (groupApply != null) {
                            RongIMutils.startGroupChat(getActivity(), groupApply.getGroupId() + "", groupApply.getGroupName());
                        }
                    }

                } else if (object instanceof YouBanSayLoveInfoMessageModel) {// 表白

                    YouBanSayLoveInfoMessageModel youBanSayLoveInfoMessageModel = (YouBanSayLoveInfoMessageModel) object;

                } else if (object instanceof YouBanHelpInfoMessageModel) {
                    YouBanHelpInfoMessageModel youBanHelpInfoMessageModel = (YouBanHelpInfoMessageModel) object;
                    HelpInfo helpInfo = new HelpInfo();
                    helpInfo.setHelpId(youBanHelpInfoMessageModel.getHelpId());
                }
            }
        });

        // 回复事件
        adapter.setReplyViewClickListener(new NotifyListItemAdapter.ReplyViewClickListener() {
            @Override
            public void onClickListener(Object object) {

                replyItemObject = object;

                // 查询圈子是否被删除

                long commentId = 0;
                int type = 0;
                if (replyItemObject instanceof YouBanSayLoveInfoMessageModel) {// 表白
                    YouBanSayLoveInfoMessageModel youBanSayLoveInfoMessageModel = (YouBanSayLoveInfoMessageModel) replyItemObject;
                    commentId = youBanSayLoveInfoMessageModel.getCommentId();
                    type = 1;

                } else if (replyItemObject instanceof YouBanOrganizationMessageModel) {// 圈子
                    YouBanOrganizationMessageModel youBanOrganizationMessageModel = (YouBanOrganizationMessageModel) replyItemObject;
                    commentId = youBanOrganizationMessageModel.getCommentId();
                    type = 2;

                } else if (replyItemObject instanceof YouBanHelpInfoMessageModel) {//帮帮忙
                    YouBanHelpInfoMessageModel youBanHelpInfoMessageModel = (YouBanHelpInfoMessageModel) replyItemObject;
                    commentId = youBanHelpInfoMessageModel.getCommentId();
                    type = 3;
                }

                getUserPresenter().checkIsDeleteComment(currentBaseUserId, commentId, type);

            }
        });

        adapter.setNotifyItemRemovedViewClickListener(new NotifyListItemAdapter.NotifyItemRemovedViewClickListener() {
            @Override
            public void notifyItemRemoved(int position, Object object) {

                if (object != null) {
                    // 删除本地数据
                    if (object instanceof YouBanActivityMessageModel) {// 活动
                        final YouBanActivityMessageModel youBanActivityMessageModel = (YouBanActivityMessageModel) object;
                        GreenDaoUtils.getInstance().deletePushdbModelById(youBanActivityMessageModel.getMessageId());
                    } else if (object instanceof YouBanGroupMessageModel) {// 群

                        final YouBanGroupMessageModel youBanGroupMessageModel = (YouBanGroupMessageModel) object;
                        GreenDaoUtils.getInstance().deletePushdbModelById(youBanGroupMessageModel.getMessageId());
                    } else if (object instanceof YouBanSayLoveInfoMessageModel) {// 表白
                        final YouBanSayLoveInfoMessageModel youBanSayLoveInfoMessageModel = (YouBanSayLoveInfoMessageModel) object;
                        GreenDaoUtils.getInstance().deletePushdbModelById(youBanSayLoveInfoMessageModel.getMessageId());

                    } else if (object instanceof YouBanOrganizationMessageModel) {// 圈子
                        final YouBanOrganizationMessageModel youBanOrganizationMessageModel = (YouBanOrganizationMessageModel) object;
                        GreenDaoUtils.getInstance().deletePushdbModelById(youBanOrganizationMessageModel.getMessageId());

                    } else if (object instanceof YouBanHelpInfoMessageModel) {//帮帮忙
                        YouBanHelpInfoMessageModel youBanHelpInfoMessageModel = (YouBanHelpInfoMessageModel) object;
                        GreenDaoUtils.getInstance().deletePushdbModelById(youBanHelpInfoMessageModel.getMessageId());
                    }
                }


                initEmptyView(adapter.getItemCount() == 0 ? true : false, type == 1 ? "暂时没有收到通知消息" : "暂时没有收到回复消息");
            }
        });

        // 刷新框架
        TwinklingRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_layout_view);

        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(App.getInstance());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recycleView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNumber = 1;
                refreshLayout.finishRefreshing();


            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();

            }
        });


    }

    /**
     * 提示语
     *
     * @param isVisibility
     * @param blankHint
     */
    private void initEmptyView(boolean isVisibility, String blankHint) {
        if (emptyView == null) {
            emptyView = rootView.findViewById(R.id.empty_view);
        }

        if (!StringUtils.isEmpty(blankHint)) {
            TextView textView = emptyView.findViewById(R.id.empty_textView);
            textView.setText(blankHint);
        }

        emptyView.setVisibility(isVisibility ? View.VISIBLE : View.GONE);

    }

    private void initSendView() {
        sendView = rootView.findViewById(R.id.send_comment_view);
        editText = sendView.findViewById(R.id.reply_edit_input);
        sendTextView = sendView.findViewById(R.id.send_view);

        anonymousSwitch = sendView.findViewById(R.id.anonymous_view);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    sendTextView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                    sendTextView.setTextColor(getResources().getColor(R.color.color_333333));
                } else {
                    sendTextView.setBackgroundResource(R.drawable.shape_round_10_solid_fff69f);
                    sendTextView.setTextColor(getResources().getColor(R.color.color_999999));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String text = editText.getText().toString().trim();
                    int anonymousType = 0;
                    long parentId = 0;
                    long commentParentId = 0;
                    long toUserId = 0;
                    long moduleId = 0;
                    int moudle = 0;//
                    if (replyItemObject instanceof YouBanSayLoveInfoMessageModel) {// 表白
                        YouBanSayLoveInfoMessageModel youBanSayLoveInfoMessageModel = (YouBanSayLoveInfoMessageModel) replyItemObject;
                        parentId = youBanSayLoveInfoMessageModel.getCommentId();
                        commentParentId = youBanSayLoveInfoMessageModel.getCommentParentId();
                        toUserId = youBanSayLoveInfoMessageModel.getBaseUser().getUserId();
                        moduleId = youBanSayLoveInfoMessageModel.getSayLoveId();
                        moudle = 2;
                    } else if (replyItemObject instanceof YouBanOrganizationMessageModel) {// 圈子
                        YouBanOrganizationMessageModel youBanOrganizationMessageModel = (YouBanOrganizationMessageModel) replyItemObject;
                        parentId = youBanOrganizationMessageModel.getCommentId();
                        commentParentId = youBanOrganizationMessageModel.getCommentParentId();
                        toUserId = youBanOrganizationMessageModel.getBaseUser().getUserId();
                        moduleId = youBanOrganizationMessageModel.getPostInfoId();
                        moudle = 1;

                    } else if (replyItemObject instanceof YouBanHelpInfoMessageModel) {//帮帮忙
                        YouBanHelpInfoMessageModel youBanHelpInfoMessageModel = (YouBanHelpInfoMessageModel) replyItemObject;
                        parentId = youBanHelpInfoMessageModel.getCommentId();
                        commentParentId = youBanHelpInfoMessageModel.getCommentParentId();
                        toUserId = youBanHelpInfoMessageModel.getBaseUser().getUserId();
                        moduleId = youBanHelpInfoMessageModel.getHelpId();
                        moudle = 4;
                    }

                    if (commentParentId == 0) {
                        commentParentId = parentId;
                    }
                    if (!StringUtils.isEmpty(text)) {
                        getPostPresenter().commentUploadImage(currentBaseUserId, moduleId, moudle, text, 2, anonymousType,
                                toUserId, currentBaseUserId, parentId, commentParentId, 1, null,
                                null, null, null);
                    }
                    editText.setText("");
                    sendView.setVisibility(View.GONE);
                    KeyBoardUtils.closeKeybord(editText, App.getInstance());

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }


    /**
     * 加载数据
     */
    private void loadData() {

        String sql = null;

        switch (type) {
            case 1:
                /**
                 * 圈子：加入和拒绝
                 * 群：加入，拒绝
                 * 活动：加入，退出
                 */
                queryPushdbModelByQueryBuilder(10002, 10003, 30002, 30003, 40001, 40002);
                sql = "UPDATE " + PushdbModelDao.TABLENAME + " SET " + " UN_READ = 1 WHERE MESSAGE_TYPE IN(10002, 10003, 30002, 30003, 40001, 40002) ";

                // 更新未读数
                GreenDaoUtils.getInstance().updatePushdbModelBySq(sql);

                EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());
                break;

            case 2:
                /**
                 * 圈子：评论和回复
                 * 表白：评论和回复
                 * 帮帮忙：评论和回复
                 */
                queryPushdbModelByQueryBuilder(10005, 10006, 20001, 20002, 70001, 70002);
                sql = "update " + PushdbModelDao.TABLENAME + " set " + " UN_READ = 1 WHERE MESSAGE_TYPE IN(10005, 10006, 20001, 20002,70001, 70002) ";

                // 更新未读数
                GreenDaoUtils.getInstance().updatePushdbModelBySq(sql);

                EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());
                break;

            case 3:

                /**
                 * 圈子：申请
                 * 群：申请
                 */

                break;
        }

    }

    private void queryPushdbModelByQueryBuilder(final Object... objects) {
        GreenDaoUtils.getInstance().queryPushdbModelByQueryBuilder(pageNumber, pageSize, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(final Object object) {
                if (!isAdded()) {
                    Logger.d("isAdded === is false ");
                    return;
                }

                // 更新UI线程
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (object instanceof List) {
                            final List list = (List) object;
                            if (list != null && list.size() > 0) {

                                if (pageNumber == 1) {
                                    adapter.setList(list);
                                } else {
                                    adapter.addList(list);
                                }

                                pageNumber++;

                            }
                        }

                        initEmptyView(adapter.getItemCount() == 0 ? true : false, type == 1 ? "暂时没有收到通知消息" : "暂时没有收到回复消息");


                    }
                });


            }
        }, objects);

    }


    @Override
    public void updateView(String apiName, Object object) {

        if (!isAdded()) {
            return;
        }

        if (InterfaceUrl.URL_CHECKISDELETECOMMENT.equals(apiName)) {

            if (object instanceof CheckIsDeleteCommentResponse) {

                CheckIsDeleteCommentResponse response = (CheckIsDeleteCommentResponse) object;
                if (response.getCode() == 200 && response.getStatus() == 0) {

                    if (sendView != null) {
                        sendView.setVisibility(View.VISIBLE);
                    }
                    String nickName = null;
                    if (replyItemObject instanceof YouBanSayLoveInfoMessageModel) {// 表白
                        YouBanSayLoveInfoMessageModel youBanSayLoveInfoMessageModel = (YouBanSayLoveInfoMessageModel) replyItemObject;
                        nickName = youBanSayLoveInfoMessageModel.getBaseUser().getNickName();
                        anonymousSwitch.setVisibility(View.GONE);
                    } else if (replyItemObject instanceof YouBanOrganizationMessageModel) {// 圈子
                        YouBanOrganizationMessageModel youBanOrganizationMessageModel = (YouBanOrganizationMessageModel) replyItemObject;
                        nickName = youBanOrganizationMessageModel.getBaseUser().getNickName();
                        anonymousSwitch.setVisibility(View.GONE);
                    } else if (replyItemObject instanceof YouBanHelpInfoMessageModel) {//帮帮忙
                        YouBanHelpInfoMessageModel youBanHelpInfoMessageModel = (YouBanHelpInfoMessageModel) replyItemObject;
                        nickName = youBanHelpInfoMessageModel.getBaseUser().getNickName();
                        anonymousSwitch.setVisibility(View.GONE);
                    }
                    if (editText != null) {
                        editText.setHint("回复：" + nickName);
                        editText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                KeyBoardUtils.openKeybord(editText, App.getInstance());
                            }
                        }, 500);

                    }

                } else {

                    Utils.showToastShortTime(response.getMsg());

                }

            }

        } else if (InterfaceUrl.URL_COMMENTUPLOADIMAGE.equals(apiName)) {
            if (object instanceof PostCommentResponse) {
                PostCommentResponse response = (PostCommentResponse) object;
                Utils.showToastShortTime(response.getMsg());
            } else if (object instanceof SayLoveCommentResponse) {
                SayLoveCommentResponse response = (SayLoveCommentResponse) object;
                Utils.showToastShortTime(response.getMsg());
            } else if (object instanceof HelpCommentResponse) {
                HelpCommentResponse helpCommentResponse = (HelpCommentResponse) object;
                Utils.showToastShortTime(helpCommentResponse.getMsg());
            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();

    }


    @Override
    public void onGlobalLayout() {
        // 减去标题
        int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight() - Utils.dp2px(44);
        Logger.d("heightDiff ===== " + heightDiff);
        float temp = getResources().getDimension(R.dimen.height_diff);
        Logger.d("temp ===== " + temp);
        if (heightDiff > temp) {
            //大小超过100时，一般为显示虚拟键盘事件

            if (sendView != null && sendView.getVisibility() == View.GONE) {
                sendView.setVisibility(View.VISIBLE);
            }
        } else {
            //大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
            if (sendView != null && sendView.getVisibility() == View.VISIBLE) {
                sendView.setVisibility(View.GONE);
            }
        }
    }
}
