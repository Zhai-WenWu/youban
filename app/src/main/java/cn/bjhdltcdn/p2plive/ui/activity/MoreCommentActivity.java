package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetCommentReplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetConfessionCommentReplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHelpCommentReplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.HelpCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SayLoveCommentResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Comment;
import cn.bjhdltcdn.p2plive.model.ConfessionComment;
import cn.bjhdltcdn.p2plive.model.HelpComment;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.ReportType;
import cn.bjhdltcdn.p2plive.mvp.presenter.ClassMateHelpPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.MoreCommentListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.SelectorReportContentDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by ZHUDI on 2018/3/5.
 */

public class MoreCommentActivity extends BaseActivity implements BaseView {

    //    private View rootView;
    private RecyclerView recyclerView;
    private TwinklingRefreshLayout refreshLayout;
    private LoadingView loadingView;
    private MoreCommentListAdapter moreCommentListAdapter;

    private CommonPresenter commonPresenter;
    private PostCommentListPresenter postPresenter;
    private UserPresenter userPresenter;
    private ClassMateHelpPresenter classMateHelpPresenter;
    private GetCommentListPresenter getCommentListPresenter;
    private int pageSize = 20, pageNum = 1;

    private EditText editText;
    private TextView sendTextView;
    private View sendView;
    private Switch anonymousSwitch;

    private int commentPosition = -1;
    //标记是否开启匿名
    private boolean anonymousSwitchIsChecked;
    // 类型(1-->帖子,2-->帖子评论,3-->帖子回复,4-->群组,5-->用户,6-->表白,7-->表白评论,8-->表白回复,9-->活动,10-->PK挑战)
    private int reportType;
    //帖子id,帖子评论id,帖子回复id,群组id,用户id,表白id,表白评论id,表白回复id,活动id,PK挑战id)
    private long reportParentId;
    //添加到黑名单的用户id
    private long toBlacklistUserId;
    //当前用户id
    private long currentBaseUserId;
    //模块(1帖子,2表白,3PK挑战,4帮帮忙),
    private int type;
    //模块id(帖子id,表白id,PK挑战id,帮帮忙id),
    private long moduleId;
    //父评论id
    private long parentCommentId;
    //排序(1按热度,2按时间)
    private int sort = 1;
    private TitleFragment titleFragment;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public CommonPresenter getCommonPresenter() {
        if (commonPresenter == null) {
            commonPresenter = new CommonPresenter(this);
        }
        return commonPresenter;
    }

    public PostCommentListPresenter getPostPresenter() {

        if (postPresenter == null) {
            postPresenter = new PostCommentListPresenter(this);
        }
        return postPresenter;
    }

    public GetCommentListPresenter getGetCommentListPresenter() {
        if (getCommentListPresenter == null) {
            getCommentListPresenter = new GetCommentListPresenter(this);
        }
        return getCommentListPresenter;
    }

    public ClassMateHelpPresenter getClassMateHelpPresenter() {
        if (classMateHelpPresenter == null) {
            classMateHelpPresenter = new ClassMateHelpPresenter(this);
        }
        return classMateHelpPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_more_comment);

        getExtraDate(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getExtraDate(getIntent());
    }

    private void getExtraDate(Intent intent) {
        moduleId = intent.getLongExtra(Constants.Fields.MODULE_ID, 0);
        type = intent.getIntExtra(Constants.Fields.TYPE, 0);
        parentCommentId = intent.getLongExtra(Constants.Fields.COMMENT_PARENT_ID, 0);

//        selectPostIndex = intent.getIntExtra(Constants.KEY.KEY_POSITION, -1);
//        comeInType = intent.getIntExtra(Constants.Fields.COME_IN_TYPE, -1);

//        rootView = findViewById(R.id.root_layout);
//        rootView.getViewTreeObserver().addOnGlobalLayoutListener(MoreActivity.this);

        setTitle();

        initView();

        initSendView();

        setOtherClick();


        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

        // 获取评论列表
        requestData();

    }

    private void initView() {

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                Utils.hideSystemSoftInputKeyboard(editText);
                return false;
            }
        });
        if (moreCommentListAdapter == null) {
            moreCommentListAdapter = new MoreCommentListAdapter(type, this);
            moreCommentListAdapter.setUserPresenter(getUserPresenter());
            LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
            layoutManager.setAutoMeasureEnabled(true);
            recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1)));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(moreCommentListAdapter);
        }

        // 刷新框架
        refreshLayout = findViewById(R.id.refresh_layout_view);
        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                refreshLayout.finishRefreshing();
                requestData();

            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                requestData();
                refreshLayout.finishLoadmore();
            }
        });

    }

    private void initSendView() {

        sendView = findViewById(R.id.send_comment_view);
        editText = sendView.findViewById(R.id.reply_edit_input);
        sendTextView = sendView.findViewById(R.id.send_view);
        if (type == 1 || type == 2) {
            anonymousSwitch = sendView.findViewById(R.id.anonymous_view);
            anonymousSwitch.setVisibility(View.VISIBLE);
            anonymousSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    anonymousSwitchIsChecked = isChecked;
                }
            });
        }

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

                String text = editText.getText().toString().trim();
                int anonymousType = 0;
                long parentId = 0;
                long toUserId = 0;

                if (commentPosition < 0) {
                    return;
                }

                Object object = moreCommentListAdapter.getList().get(commentPosition);
                if (object instanceof Comment) {
                    Comment comment = (Comment) object;
                    parentId = comment.getCommentId();
                    if (comment.getFromBaseUser() != null) {
                        toUserId = comment.getFromBaseUser().getUserId();
                    }
                } else if (object instanceof ConfessionComment) {
                    ConfessionComment comment = (ConfessionComment) object;
                    parentId = comment.getCommentId();
                    if (comment.getFromBaseUser() != null) {
                        toUserId = comment.getFromBaseUser().getUserId();
                    }
                } else if (object instanceof HelpComment) {
                    HelpComment comment = (HelpComment) object;
                    parentId = comment.getCommentId();
                    if (comment.getFromBaseUser() != null) {
                        toUserId = comment.getFromBaseUser().getUserId();
                    }
                }

                if (anonymousSwitchIsChecked) {// 回复用户匿名
                    anonymousType = 2;
                }

                if (!StringUtils.isEmpty(text)) {
                    getPostPresenter().commentUploadImage(currentBaseUserId, moduleId, type, text, 2, anonymousType,
                            toUserId, currentBaseUserId, parentId, parentCommentId, 1, null, null, null, null);
                }
                editText.setText("");
                sendView.setVisibility(View.GONE);
                KeyBoardUtils.closeKeybord(editText, App.getInstance());

                commentPosition = -1;

            }
        });

    }

    private void setOtherClick() {

        moreCommentListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position > 0) {

                    Object object = moreCommentListAdapter.getList().get(position);
                    if (object instanceof Comment) {
                        Comment comment = (Comment) object;
                        //自己的评论不能回复
                        if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                            return;
                        } else {
                            editText.setHint("回复：" + comment.getFromBaseUser().getNickName());
                        }
                    } else if (object instanceof ConfessionComment) {
                        ConfessionComment comment = (ConfessionComment) object;
                        //自己的评论不能回复
                        if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                            return;
                        } else {
                            editText.setHint("回复：" + comment.getFromBaseUser().getNickName());
                        }
                    } else if (object instanceof HelpComment) {
                        HelpComment comment = (HelpComment) object;
                        //自己的评论不能回复
                        if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                            return;
                        } else {
                            editText.setHint("回复：" + comment.getFromBaseUser().getNickName());
                        }
                    }


                    if (sendView != null) {
                        sendView.setVisibility(View.VISIBLE);
                    }

                    if (editText != null) {
                        editText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                KeyBoardUtils.openKeybord(editText, App.getInstance());
                            }
                        }, 500);

                    }

                    commentPosition = position;
                } else {

                    if (sendView != null) {
                        sendView.setVisibility(View.VISIBLE);
                    }

                    if (editText != null) {
                        editText.setHint("说点什么吧...");
                        editText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                KeyBoardUtils.openKeybord(editText, App.getInstance());
                            }
                        }, 500);

                    }
                    commentPosition = position;
                }
            }
        });

        moreCommentListAdapter.setViewClick(new MoreCommentListAdapter.ViewClick() {
            @Override
            public void onPraise(long postId, int type, int position) {// 点赞
                getPostPresenter().commentPraise(postId, type, currentBaseUserId, MoreCommentActivity.this.type);


            }

            @Override
            public void attentionView(int type, long userId, int position) {// 关注
                // 关注状态(0-->未关注,1-->已关注)
                if (type == 0) {
                    type = 1;
                } else if (type == 1) {
                    type = 2;
                }
                getUserPresenter().attentionOperation(type, currentBaseUserId, userId);

            }

            @Override
            public void moreImg(final int itemType, int position) {
                final PostOperationFragmentDialog dialog = new PostOperationFragmentDialog();

                // 评论
                Object object = moreCommentListAdapter.getList().get(position);
                if (object instanceof Comment) {
                    final Comment comment = (Comment) object;

                    commentPosition = position;

                    reportParentId = comment.getCommentId();

                    //帖子回复
                    if (comment.getType() == 1) {
                        reportType = 2;
                    } else if (comment.getType() == 2) {
                        reportType = 3;
                    }

                    if (comment.getFromBaseUser() != null) {
                        //用户角色(0-->普通成员,1-->管理员,2-->圈主),
                        if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                            //自己的评论
                            dialog.setTextList("删除", "取消");
                        } else if (comment.getAnonymousType() == 1) {//别人的评论
                            dialog.setTextList("举报", "取消");
                        } else {
                            dialog.setTextList("举报", "拉黑", "取消");

                        }

                        dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                            @Override
                            public void onClick(int type) {

                                if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                                    switch (type) {

                                        case 1:// 删除  评论

                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                                            dialog.setText("", "确定删除评论？", "取消", "确定");
                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                                                @Override
                                                public void onLeftClick() {
                                                    //取消
                                                }

                                                @Override
                                                public void onRightClick() {
                                                    getPostPresenter().deleteComment(currentBaseUserId, comment.getCommentId());
                                                }
                                            });
                                            dialog.show(getSupportFragmentManager());

                                            break;

                                    }
                                } else {//别人的帖子
                                    switch (type) {

                                        case 1:// 举报 评论
                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
                                                @Override
                                                public void reportItemClick(Object object) {
                                                    if (object instanceof ReportType) {

                                                        ReportType reportTypeObj = (ReportType) object;
                                                        getCommonPresenter().reportOperation(reportParentId, reportType
                                                                , currentBaseUserId
                                                                , 0
                                                                , reportTypeObj.getReportTypeId());
                                                    }
                                                }
                                            });
                                            selectorReportContentDialog.show(getSupportFragmentManager());
                                            break;

                                        case 2://拉黑 发评论的人

                                            BaseUser baseUser = comment.getFromBaseUser();
                                            if (baseUser != null) {
                                                long myUserId = currentBaseUserId;
                                                toBlacklistUserId = baseUser.getUserId();
                                                if (myUserId == toBlacklistUserId) {
                                                    return;
                                                }
                                                getUserPresenter().pullBlackUser(myUserId, toBlacklistUserId);
                                            }

                                            break;

                                    }
                                }


                            }
                        });
                    }
                } else if (object instanceof ConfessionComment) {
                    final ConfessionComment comment = (ConfessionComment) object;

                    commentPosition = position;

                    reportParentId = comment.getCommentId();
                    if (comment.getType() == 1) {
                        reportType = 7;
                    } else if (comment.getType() == 2) {
                        reportType = 8;
                    }


                    if (comment.getFromBaseUser() != null) {
                        //用户角色(0-->普通成员,1-->管理员,2-->圈主),
                        if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                            //自己的评论
                            dialog.setTextList("删除", "取消");
                        } else if (comment.getAnonymousType() == 1) {//别人的评论
                            dialog.setTextList("举报", "取消");
                        } else {//别人的评论
                            dialog.setTextList("举报", "拉黑", "取消");
                        }

                        dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                            @Override
                            public void onClick(int type) {

                                if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                                    switch (type) {

                                        case 1:// 删除  评论

                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                                            dialog.setText("", "确定删除评论？", "取消", "确定");
                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                                                @Override
                                                public void onLeftClick() {
                                                    //取消
                                                }

                                                @Override
                                                public void onRightClick() {
                                                    getGetCommentListPresenter().deleteSayLoveComment(comment.getCommentId());
                                                }
                                            });
                                            dialog.show(getSupportFragmentManager());

                                            break;

                                    }
                                } else {//别人的帖子
                                    switch (type) {

                                        case 1:// 举报 评论
                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
                                                @Override
                                                public void reportItemClick(Object object) {
                                                    if (object instanceof ReportType) {

                                                        ReportType reportTypeObj = (ReportType) object;
                                                        getCommonPresenter().reportOperation(reportParentId, reportType
                                                                , currentBaseUserId
                                                                , 0
                                                                , reportTypeObj.getReportTypeId());
                                                    }
                                                }
                                            });
                                            selectorReportContentDialog.show(getSupportFragmentManager());
                                            break;

                                        case 2://拉黑 发评论的人

                                            BaseUser baseUser = comment.getFromBaseUser();
                                            if (baseUser != null) {
                                                long myUserId = currentBaseUserId;
                                                toBlacklistUserId = baseUser.getUserId();
                                                if (myUserId == toBlacklistUserId) {
                                                    return;
                                                }
                                                getUserPresenter().pullBlackUser(myUserId, toBlacklistUserId);
                                            }

                                            break;

                                    }
                                }


                            }
                        });
                    }
                } else if (object instanceof HelpComment) {
                    final HelpComment comment = (HelpComment) object;

                    commentPosition = position;

                    reportParentId = comment.getCommentId();

                    if (comment.getType() == 1) {
                        reportType = 13;
                    } else if (comment.getType() == 2) {
                        reportType = 14;
                    }


                    if (comment.getFromBaseUser() != null) {
                        //用户角色(0-->普通成员,1-->管理员,2-->圈主),
                        if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                            //自己的评论
                            dialog.setTextList("删除", "取消");
                        } else {//别人的评论
                            dialog.setTextList("举报", "拉黑", "取消");
                        }

                        dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                            @Override
                            public void onClick(int type) {

                                if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                                    switch (type) {

                                        case 1:// 删除  评论

                                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                                            dialog.setText("", "确定删除评论？", "取消", "确定");
                                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                                                @Override
                                                public void onLeftClick() {
                                                    //取消
                                                }

                                                @Override
                                                public void onRightClick() {
                                                    getClassMateHelpPresenter().deleteHelpComment(currentBaseUserId, comment.getCommentId());
                                                }
                                            });
                                            dialog.show(getSupportFragmentManager());

                                            break;

                                    }
                                } else {//别人的帖子
                                    switch (type) {

                                        case 1:// 举报 评论
                                            SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
                                            selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
                                                @Override
                                                public void reportItemClick(Object object) {
                                                    if (object instanceof ReportType) {

                                                        ReportType reportTypeObj = (ReportType) object;
                                                        getCommonPresenter().reportOperation(reportParentId, reportType
                                                                , currentBaseUserId
                                                                , 0
                                                                , reportTypeObj.getReportTypeId());
                                                    }
                                                }
                                            });
                                            selectorReportContentDialog.show(getSupportFragmentManager());
                                            break;

                                        case 2://拉黑 发评论的人

                                            BaseUser baseUser = comment.getFromBaseUser();
                                            if (baseUser != null) {
                                                long myUserId = currentBaseUserId;
                                                toBlacklistUserId = baseUser.getUserId();
                                                if (myUserId == toBlacklistUserId) {
                                                    return;
                                                }
                                                getUserPresenter().pullBlackUser(myUserId, toBlacklistUserId);
                                            }

                                            break;

                                    }
                                }


                            }
                        });
                    }
                }
                dialog.show(getSupportFragmentManager());

            }

            @Override
            public void sortViewClick() {
                if (sort == 1) {
                    sort = 2;
                } else if (sort == 2) {
                    sort = 1;
                }
                moreCommentListAdapter.setSort(sort);
                requestData();
            }

        });
    }

    /**
     * 请求回复列表
     */
    private void requestData() {
        getPostPresenter().getReplyList(currentBaseUserId, parentCommentId, moduleId, type, sort, pageSize, pageNum);
    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                Utils.hideSystemSoftInputKeyboard(editText);
                finish();
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {

        if (isFinishing()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            Utils.showToastShortTime(e.getMessage());
            return;
        }
        switch (apiName) {
            case InterfaceUrl.URL_GETREPLYLIST:
                if (type == 1) {
                    if (object instanceof GetCommentReplyListResponse) {
                        GetCommentReplyListResponse getCommentReplyListResponse = (GetCommentReplyListResponse) object;
                        int code = getCommentReplyListResponse.getCode();
                        if (code == 200) {
                            List commentList = getCommentReplyListResponse.getList();
                            if (commentList != null) {
                                if (pageNum == 1) {
                                    Comment comment = getCommentReplyListResponse.getCommentInfo();
                                    titleFragment.setTitle(comment.getReplyCount() + "条回复");
                                    commentList.add(0, comment);
                                    moreCommentListAdapter.setList(commentList);
//                                moreCommentListAdapter.setParentCommentView(getCommentReplyListResponse.getCommentInfo());
                                } else {
                                    moreCommentListAdapter.addList(commentList);
                                }
                            }
                            if (getCommentReplyListResponse.getTotal() == 0 && moreCommentListAdapter.getItemCount() == 1) {
                                moreCommentListAdapter.addEmptyItem(getCommentReplyListResponse.getBlankHint());
                            }


                            if (getCommentReplyListResponse.getTotal() <= moreCommentListAdapter.getItemCount() - 1) {
                                // 没有更多数据
                                refreshLayout.setEnableLoadmore(false);
                            } else {
                                pageNum++;
                                refreshLayout.setEnableLoadmore(true);
                            }

                        } else {
                            Utils.showToastShortTime(getCommentReplyListResponse.getMsg());
                            if (code == 201) {
                                finish();
                            }
                        }
                    }
                } else if (type == 2) {
                    if (object instanceof GetConfessionCommentReplyListResponse) {
                        GetConfessionCommentReplyListResponse getConfessionCommentReplyListResponse = (GetConfessionCommentReplyListResponse) object;
                        int code = getConfessionCommentReplyListResponse.getCode();
                        if (code == 200) {
                            List commentList = getConfessionCommentReplyListResponse.getList();
                            if (commentList != null) {
                                if (pageNum == 1) {
                                    ConfessionComment comment = getConfessionCommentReplyListResponse.getCommentInfo();
                                    titleFragment.setTitle(comment.getReplyCount() + "条回复");
                                    commentList.add(0, comment);
                                    moreCommentListAdapter.setList(commentList);
//                                moreCommentListAdapter.setParentCommentView(getCommentReplyListResponse.getCommentInfo());
                                } else {
                                    moreCommentListAdapter.addList(commentList);
                                }
                            }
                            if (getConfessionCommentReplyListResponse.getTotal() == 0 && moreCommentListAdapter.getItemCount() == 1) {
                                moreCommentListAdapter.addEmptyItem(getConfessionCommentReplyListResponse.getBlankHint());
                            }


                            if (getConfessionCommentReplyListResponse.getTotal() <= moreCommentListAdapter.getItemCount() - 1) {
                                // 没有更多数据
                                refreshLayout.setEnableLoadmore(false);
                            } else {
                                pageNum++;
                                refreshLayout.setEnableLoadmore(true);
                            }

                        } else {
                            Utils.showToastShortTime(getConfessionCommentReplyListResponse.getMsg());
                            if (code == 201) {
                                finish();
                            }
                        }
                    }
                } else if (type == 4) {
                    if (object instanceof GetHelpCommentReplyListResponse) {
                        GetHelpCommentReplyListResponse getHelpCommentReplyListResponse = (GetHelpCommentReplyListResponse) object;
                        int code = getHelpCommentReplyListResponse.getCode();
                        if (code == 200) {
                            List commentList = getHelpCommentReplyListResponse.getList();
                            if (commentList != null) {
                                if (pageNum == 1) {
                                    HelpComment comment = getHelpCommentReplyListResponse.getCommentInfo();
                                    titleFragment.setTitle(comment.getReplyCount() + "条回复");
                                    commentList.add(0, comment);
                                    moreCommentListAdapter.setList(commentList);
//                                moreCommentListAdapter.setParentCommentView(getCommentReplyListResponse.getCommentInfo());
                                } else {
                                    moreCommentListAdapter.addList(commentList);
                                }
                            }
                            if (getHelpCommentReplyListResponse.getTotal() == 0 && moreCommentListAdapter.getItemCount() == 1) {
                                moreCommentListAdapter.addEmptyItem(getHelpCommentReplyListResponse.getBlankHint());
                            }


                            if (getHelpCommentReplyListResponse.getTotal() <= moreCommentListAdapter.getItemCount() - 1) {
                                // 没有更多数据
                                refreshLayout.setEnableLoadmore(false);
                            } else {
                                pageNum++;
                                refreshLayout.setEnableLoadmore(true);
                            }

                        } else {
                            Utils.showToastShortTime(getHelpCommentReplyListResponse.getMsg());
                            if (code == 201) {
                                finish();
                            }
                        }
                    }
                }
                break;
//            case InterfaceUrl.URL_POSTPRAISE:
//                if (object instanceof PostPraiseResponse) {
//                    PostPraiseResponse postPraiseResponse = (PostPraiseResponse) object;
//                    if (postPraiseResponse.getCode() == 200) {
//                        int isPraise = postPraiseResponse.getIsPraise();
//                        EventBus.getDefault().post(new UpdatePostListEvent(2, selectPostIndex, 0, isPraise, comeInType));
//                    }
//
//                }
//                break;
            case InterfaceUrl.URL_ATTENTIONOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    Utils.showToastShortTime(response.getMsg());

                    if (response.getCode() == 200) {

                        PostInfo postInfo = (PostInfo) moreCommentListAdapter.getList().get(0);
                        // 关注状态(0-->未关注,1-->已关注)
                        postInfo.getBaseUser().setIsAttention(postInfo.getBaseUser().getIsAttention() == 0 ? 1 : 0);
                        moreCommentListAdapter.notifyItemChanged(0);

                    }
                }
                break;
            case InterfaceUrl.URL_COMMENTUPLOADIMAGE:
                if (object instanceof PostCommentResponse) {
                    PostCommentResponse postCommentResponse = (PostCommentResponse) object;
                    Utils.showToastShortTime(postCommentResponse.getMsg());
                    if (postCommentResponse.getCode() == 200) {
                        pageNum = 1;
                        requestData();
//                    // 更新评论数
//                    PostInfo postInfo = (PostInfo) postCommentListAdapter.getList().get(0);
//                    postInfo.setCommentCount(postInfo.getCommentCount() + 1);

                        // 更新评论
//                    Comment comment = (Comment) commentUploadImageResponse.getComment();

//                    List<Object> list = postCommentListAdapter.getList();
//                    if (list.get(list.size() - 1) instanceof String) {
//                        postCommentListAdapter.getList().remove(list.size() - 1);
//                    }
//
//                    postCommentListAdapter.addItem(comment, false);
//
//                    postCommentListAdapter.notifyDataSetChanged();
//
//                    recyclerView.smoothScrollToPosition(1);
//
//                    EventBus.getDefault().post(new UpdatePostListEvent(1, selectPostIndex, postInfo.getCommentCount(), 0, comeInType));
//                        getPostPresenter().getPostCommentList(postInfo.getPostId(), pageSize, pageNum);

                    }
                } else if (object instanceof SayLoveCommentResponse) {
                    SayLoveCommentResponse sayLoveCommentResponse = (SayLoveCommentResponse) object;
                    Utils.showToastShortTime(sayLoveCommentResponse.getMsg());
                    if (sayLoveCommentResponse.getCode() == 200) {
                        pageNum = 1;
                        requestData();
                    }
                } else if (object instanceof HelpCommentResponse) {
                    HelpCommentResponse helpCommentResponse = (HelpCommentResponse) object;
                    Utils.showToastShortTime(helpCommentResponse.getMsg());
                    if (helpCommentResponse.getCode() == 200) {
                        pageNum = 1;
                        requestData();
                    }
                }
                break;
            case InterfaceUrl.URL_DELETECOMMENT:
            case InterfaceUrl.URL_DELETEHELPCOMMENT:
            case InterfaceUrl.URL_DELETESAYLOVECOMMENT:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {
                        Utils.showToastShortTime(response.getMsg());

                        // 更新评论数

                        Object commentObj = moreCommentListAdapter.getList().get(0);
                        if (commentObj instanceof Comment) {
                            ((Comment) commentObj).setReplyCount(((Comment) commentObj).getReplyCount() - 1);
                        } else if (commentObj instanceof ConfessionComment) {
                            ((ConfessionComment) commentObj).setReplyCount(((ConfessionComment) commentObj).getReplyCount() - 1);
                        } else if (commentObj instanceof HelpComment) {
                            ((HelpComment) commentObj).setReplyCount(((HelpComment) commentObj).getReplyCount() - 1);
                        }

//                        EventBus.getDefault().post(new UpdatePostListEvent(1, selectPostIndex, postInfo.getCommentCount(), 0, comeInType));

                        if (commentPosition < 0) {
                            return;
                        }

                        // 更新评论列表

                        if (moreCommentListAdapter == null) {
                            return;
                        }

                        if (moreCommentListAdapter.getList() == null) {
                            return;
                        }

                        if (moreCommentListAdapter.getList().size() <= commentPosition) {
                            return;
                        }

                        moreCommentListAdapter.getList().remove(commentPosition);

                        commentPosition = -1;

                        moreCommentListAdapter.notifyDataSetChanged();

                    }
                }


                break;
            case InterfaceUrl.URL_REPORTOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_PULLBLACKUSER:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    Utils.showToastShortTime(response.getMsg());
                    if (response.getCode() == 200) {
                        RongIM.getInstance().addToBlacklist(toBlacklistUserId + "", new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {

                                if (isFinishing()) {
                                    return;
                                }

                                toBlacklistUserId = 0;
                                Logger.d("拉黑成功");

                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                                if (isFinishing()) {
                                    return;
                                }

                                toBlacklistUserId = 0;
                                Logger.d("失败原因：" + errorCode.getMessage());

                            }
                        });
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
}
