package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ShareNumUpdateEvent;
import cn.bjhdltcdn.p2plive.event.UpdatePostListEvent;
import cn.bjhdltcdn.p2plive.event.VideoCommentFinishEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.CommentPraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindPostDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPostCommentListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Comment;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.ReportType;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.service.SingleTaskShortVideoUploaderIntentService;
import cn.bjhdltcdn.p2plive.ui.adapter.CommentImageRecyclerAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.PostCommentListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveEditSelectDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.SelectorReportContentDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.BitmapUtils;
import cn.bjhdltcdn.p2plive.utils.DateUtils;
import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.UriUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 帖子详情页
 * 注意：使用到视频播发器必须在配置文件加 android:hardwareAccelerated="true" 属性，否则会黑屏（播放不出来画面）
 */

public class PostDetailActivity extends BaseActivity implements BaseView, View.OnClickListener {


    private View rootView;

    private RecyclerView recyclerView;

    private TwinklingRefreshLayout refreshLayout;
    private LoadingView loadingView;
    private int pageSize = 20, pageNum = 1;
    private PostInfo postInfo;
    private int selectPostIndex;
    /**
     * 圈子详情页面进入 0：首页帖子列表 1：圈子帖子列表 2：附近帖子列表 3：他的帖子列表 4：回复消息列表帖子列表 5：聊天列表 10--从申请店员界面进入
     */
    private int comeInType;
    private CommonPresenter commonPresenter;
    private PostCommentListPresenter postPresenter;
    private GetStoreListPresenter getStoreListPresenter;
    private PostCommentListAdapter postCommentListAdapter;
    private CommentImageRecyclerAdapter commentImageRecyclerAdapter;
    private ImagePresenter imagePresenter;

    private EditText editText;
    private TextView sendTextView;
    private View sendView;
    /**
     * 评论图片列表
     */
    private RecyclerView commentImageRecyclerView;
    /**
     * 选择类型
     * 1图片 2视频 3gif
     */
    private int selectMode;
    /**
     * 评论图片最大选择数量
     */
    int maxSelectNum = 9;
    private Switch anonymousSwitch;
    private ImageView img_image, img_gif, img_video;

    private int commentPosition = -1;
    //本地选择视频
    private final int VIDEO_PICKER_CODE = 2;
    //打开录制视频页面
    private final int VIDEO_CODE = 1;
    private String videoSuffixs[] = {"mp4", "avi", "mov", "mkv", "flv", "f4v", "rmvb"};
    //视频路径
    private String videoPath;
    //视频图片的宽高
    private int imgWidth, imgHeight;
    // 评论类型(1--->评论,2--->回复)
    private int commentType = 1;
    private UserPresenter userPresenter;
    //标记是否开启匿名
    private boolean anonymousSwitchIsChecked;
    // 类型(1-->帖子,2-->帖子评论,3-->帖子回复,4-->群组,5-->用户,6-->表白,7-->表白评论,8-->表白回复,9-->活动,10-->PK挑战)
    private int reportType;

    // (帖子id,帖子评论id,帖子回复id,群组id,用户id,表白id,表白评论id,表白回复id,活动id,PK挑战id)
    private long reportParentId;
    //添加到黑名单的用户id
    private long toBlacklistUserId;
    //排序(1按热度,2按时间)
    private int sort = 1;
    private long currentBaseUserId;
    //二级评论对象，点击二级回复使用
    private Comment replyComment;
    private long postId;

    private UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    private CommonPresenter getCommonPresenter() {
        if (commonPresenter == null) {
            commonPresenter = new CommonPresenter(this);
        }
        return commonPresenter;
    }

    private PostCommentListPresenter getPostPresenter() {

        if (postPresenter == null) {
            postPresenter = new PostCommentListPresenter(this);
        }
        return postPresenter;
    }

    public ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_post_detail);

        getExtraDate(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        getExtraDate(getIntent());
    }

    private void getExtraDate(Intent intent) {

        postInfo = intent.getParcelableExtra(Constants.KEY.KEY_OBJECT);
        selectPostIndex = intent.getIntExtra(Constants.KEY.KEY_POSITION, -1);
        comeInType = intent.getIntExtra(Constants.Fields.COME_IN_TYPE, -1);
        rootView = findViewById(R.id.root_layout);

        setTitle();

        initView();

        initPostInfo(postInfo);

        initSendView();

        setOtherClick();


        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        requestDetailData();

    }

    private void setTitle() {
        final TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_post_detail);
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                Utils.hideSystemSoftInputKeyboard(editText);
                finish();
            }
        });
    }

    /**
     * 查询帖子详情
     */
    private void requestDetailData() {
        if (postInfo != null) {
            getPostPresenter().findPostDetail(currentBaseUserId, postInfo.getPostId());
        }
    }

    /**
     * 获取评论列表
     */
    private void requestCommentData() {
        getPostPresenter().getPostCommentList(currentBaseUserId, postInfo.getPostId(), sort, pageSize, pageNum);
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
        if (postCommentListAdapter == null) {
            postCommentListAdapter = new PostCommentListAdapter(null, this);
            postCommentListAdapter.setUserPresenter(getUserPresenter());
            LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
            layoutManager.setAutoMeasureEnabled(true);
            recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1)));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(postCommentListAdapter);
        }

        // 刷新框架
        refreshLayout = findViewById(R.id.refresh_layout_view);
        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);
        //支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setFloatRefresh(false);
        //是否允许越界回弹。
        refreshLayout.setEnableOverScroll(false);
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        //设置滚动事件的作用对象。
        refreshLayout.setTargetView(recyclerView);
        //灵活的设置是否禁用上下拉。
        refreshLayout.setEnableRefresh(true);
        //是否在底部越界的时候自动切换到加载更多模式
        refreshLayout.setAutoLoadMore(false);

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                refreshLayout.finishRefreshing();
                requestDetailData();


            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();
                requestCommentData();
            }
        });

    }

    private void initSendView() {

        sendView = findViewById(R.id.send_comment_view);
        editText = sendView.findViewById(R.id.reply_edit_input);
        sendTextView = sendView.findViewById(R.id.send_view);
        commentImageRecyclerView = sendView.findViewById(R.id.recycler_image_comment);
        commentImageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager hlayoutManager = new LinearLayoutManager(App.getInstance());
        hlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        commentImageRecyclerView.setLayoutManager(hlayoutManager);
        commentImageRecyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(12), false));
        commentImageRecyclerAdapter = new CommentImageRecyclerAdapter(this);
        commentImageRecyclerView.setAdapter(commentImageRecyclerAdapter);
        commentImageRecyclerAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (selectMode != 2) {
                    ImageViewPageDialog.newInstance(commentImageRecyclerAdapter.getList(), position).show(getSupportFragmentManager());
                }
            }
        });
        commentImageRecyclerAdapter.setOnDeleteItemClick(new CommentImageRecyclerAdapter.OnDeleteItemClick() {
            @Override
            public void deleteItemClick(int position) {
                if (commentImageRecyclerAdapter != null && commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > position) {
                    videoPath = null;
                    commentImageRecyclerAdapter.getList().remove(position);
                    maxSelectNum++;

                    commentImageRecyclerAdapter.notifyDataSetChanged();

                    if (commentImageRecyclerAdapter.getItemCount() > 0) {
                        commentImageRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        commentImageRecyclerView.setVisibility(View.GONE);
                    }
                }

            }
        });
        img_image = sendView.findViewById(R.id.img_image);
        img_gif = sendView.findViewById(R.id.img_gif);
        img_video = sendView.findViewById(R.id.img_video);
        img_image.setOnClickListener(this);
        img_gif.setOnClickListener(this);
        img_video.setOnClickListener(this);
        anonymousSwitch = sendView.findViewById(R.id.anonymous_view);
        anonymousSwitch.setVisibility(View.VISIBLE);
        anonymousSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                anonymousSwitchIsChecked = isChecked;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean imageOrVideoNotNull = commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0;
                setSendViewBg(s, imageOrVideoNotNull);
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
                long commentParentId = 0;
                if (postInfo != null && postInfo.getBaseUser() != null) {
                    toUserId = postInfo.getBaseUser().getUserId();
                }

                if (replyComment == null) {
                    if (commentPosition < 0) {
                        return;
                    }

                    Object object = postCommentListAdapter.getList().get(commentPosition);
                    // 推送消息使用
                    if (object instanceof PostInfo) {
                        PostInfo postInfo = (PostInfo) object;
                        if (postInfo.getBaseUser() != null) {
                            toUserId = postInfo.getBaseUser().getUserId();
                        }

                    } else if (object instanceof Comment) {

                        Comment comment = (Comment) object;
                        parentId = comment.getCommentId();
                        commentParentId = parentId;
                        if (comment.getFromBaseUser() != null) {
                            toUserId = comment.getFromBaseUser().getUserId();
                        }

                    }

                } else {
                    parentId = replyComment.getCommentId();
                    commentParentId = replyComment.getCommentParentId();
                    if (replyComment.getFromBaseUser() != null) {
                        toUserId = replyComment.getFromBaseUser().getUserId();
                    }
                }
                // 评论用户匿名
                if (commentType == 1 && anonymousSwitchIsChecked) {
                    anonymousType = 1;
                    // 回复用户匿名
                } else if (commentType == 2 && anonymousSwitchIsChecked) {
                    anonymousType = 2;
                }
                //图文
                if (TextUtils.isEmpty(videoPath)) {
                    boolean imageOrVideoNotNull = commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0;
                    if (!TextUtils.isEmpty(text) || imageOrVideoNotNull) {
                        getPostPresenter().commentUploadImage(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), postInfo.getPostId(), 1,
                                text, commentType, anonymousType, toUserId, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), parentId, commentParentId,
                                1, null, null, DateUtils.getTime(new Date()), null);
                    } else {
                        return;
                    }
                } else {
                    if (StringUtils.isEmpty(videoPath)) {
                        Utils.showToastShortTime("请添加视频");
                        return;
                    }

                    LinkedHashMap<String, Object> map = new LinkedHashMap<>(1);
                    map.put(Constants.Fields.VIDEO_PATH, videoPath);
                    map.put(Constants.Fields.IMG_WIDTH, imgWidth);
                    map.put(Constants.Fields.IMG_HEIGHT, imgHeight);
                    map.put(Constants.Fields.CONTENT_STR, text);
                    map.put(Constants.Fields.IS_ANONYMOUS, anonymousType);
                    map.put(Constants.Fields.LAUNCH_ID, postInfo.getPostId());
                    map.put(Constants.Fields.TO_USER_ID, toUserId);
                    map.put(Constants.Fields.ENTER_TYPE_MODE, 6);
                    map.put(Constants.Fields.TYPE, 1);
                    SingleTaskShortVideoUploaderIntentService.startShortVideoUploader(PostDetailActivity.this, map);
                    Utils.showToastShortTime("视频上传中，上传完成后将自动发布。");

                }
                videoPath = null;
                editText.setText("");
                sendView.setVisibility(View.GONE);
                KeyBoardUtils.closeKeybord(editText, App.getInstance());
                if (replyComment != null) {
                    replyComment = null;
                }
                commentPosition = -1;

            }
        });

    }

    /**
     * 评论设置按钮背景
     *
     * @param s
     * @param imageOrVideoNotNull
     */
    private void setSendViewBg(CharSequence s, boolean imageOrVideoNotNull) {
        if (s != null && s.length() > 0 || imageOrVideoNotNull) {
            sendTextView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
            sendTextView.setTextColor(getResources().getColor(R.color.color_333333));
        } else {
            sendTextView.setBackgroundResource(R.drawable.shape_round_10_solid_fff69f);
            sendTextView.setTextColor(getResources().getColor(R.color.color_999999));
        }
    }


    private void setOtherClick() {

        postCommentListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position > 0) {
                    img_image.setVisibility(View.GONE);
                    img_gif.setVisibility(View.GONE);
                    img_video.setVisibility(View.GONE);
                    Object object = postCommentListAdapter.getList().get(position);
                    if (object instanceof Comment) {
                        Comment comment = (Comment) object;
                        //自己的评论不能回复
                        if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
                            return;
                        } else {
                            String fromBaseUserNickName;
                            if (comment.getAnonymousType() == 1 || comment.getAnonymousType() == 3) {
                                fromBaseUserNickName = "匿名";
                            } else {
                                fromBaseUserNickName = comment.getFromBaseUser().getNickName();
                            }
                            editText.setHint("回复：" + fromBaseUserNickName);
                            commentType = 2;
                            if (commentImageRecyclerAdapter.getList() != null) {
                                commentImageRecyclerAdapter.getList().clear();
                                commentImageRecyclerAdapter.notifyDataSetChanged();
                                if (commentImageRecyclerAdapter.getItemCount() > 0) {
                                    commentImageRecyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    commentImageRecyclerView.setVisibility(View.GONE);
                                }
                            }
                            videoPath = null;
                        }
                        //空消息
                    } else if (object instanceof String) {
                        img_image.setVisibility(View.VISIBLE);
                        img_gif.setVisibility(View.VISIBLE);
                        img_video.setVisibility(View.VISIBLE);
                        commentType = 1;
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
                }
            }
        });

        postCommentListAdapter.setViewClick(new PostCommentListAdapter.ViewClick() {
            @Override
            public void onPraise(int itemType, long postId, int type, int position) {// 点赞

                if (itemType == 0) {
                    getPostPresenter().postPraise(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), postId, type);
                } else {
                    getPostPresenter().commentPraise(postId, type, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 1);
                }

            }

            @Override
            public void onComment(long postId, int type, final int position) {// 评论
                img_image.setVisibility(View.VISIBLE);
                img_gif.setVisibility(View.VISIBLE);
                img_video.setVisibility(View.VISIBLE);
                if (sendView != null) {
                    sendView.setVisibility(View.VISIBLE);
                }
                if (editText != null) {
                    setSendViewBg(editText.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
                    editText.setHint("说点什么吧...");
                    editText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            KeyBoardUtils.openKeybord(editText, App.getInstance());
                        }
                    }, 500);

                }

                commentPosition = position;
                commentType = 1;


            }

            @Override
            public void attentionView(int type, long userId, int position) {// 关注
                // 关注状态(0-->未关注,1-->已关注)
                if (type == 0) {
                    type = 1;
                } else if (type == 1) {
                    type = 2;
                }
                getUserPresenter().attentionOperation(type, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), userId);

            }

            @Override
            public void moreImg(final int itemType, int position) {
                final PostOperationFragmentDialog dialog = new PostOperationFragmentDialog();

                if (itemType == 0) {// 帖子
                    final PostInfo postInfo = (PostInfo) postCommentListAdapter.getList().get(0);
                    //用户角色：圈主或者管理员是自己
                    //用户角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
                    if (postInfo.getUserRole() == 1 || postInfo.getUserRole() == 2) {
                        if (postInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            dialog.setTextList("删除", "分享", "取消");
                        } else if (postInfo.getIsAnonymous() == 1) {
//                            dialog.setTextList("将Ta踢出圈子", "对Ta禁言/解除禁言", "举报", "删除", "置顶/取消置顶", "分享", "取消");
                            dialog.setTextList("举报", "分享", "取消");
                        } else {
//                            dialog.setTextList("将Ta踢出圈子", "对Ta禁言/解除禁言", "举报", "删除", "置顶/取消置顶", "分享", "拉黑", "取消");
                            dialog.setTextList("举报", "分享", "拉黑", "取消");
                        }
                    } else {
                        if (postInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            dialog.setTextList("删除", "分享", "取消");
                        } else if (postInfo.getIsAnonymous() == 1) {
                            dialog.setTextList("举报", "分享", "取消");
                        } else {
                            dialog.setTextList("举报", "分享", "拉黑", "取消");
                        }
                    }

                    reportParentId = postInfo.getPostId();
                    reportType = 1;

                    dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                        @Override
                        public void onClick(int type) {

                            //用户角色：圈主或者管理员是自己
                            if ((postInfo.getUserRole() == 1 || postInfo.getUserRole() == 2) && postInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {

                                switch (type) {

                                    case 1://删除 帖子

                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                                        dialog.setText("", "确定删除帖子？", "取消", "确定");
                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                                            @Override
                                            public void onLeftClick() {
                                                //取消
                                            }

                                            @Override
                                            public void onRightClick() {
                                                getPostPresenter().deletePost(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), postInfo.getPostId());
                                            }
                                        });
                                        dialog.show(getSupportFragmentManager());

                                        break;

                                    case 2:

                                        //是否置顶(0取消,1置顶)
                                        getPostPresenter().postTop(postInfo.getOrganId(), postInfo.getPostId(), 1, postInfo.getIsTop() == 0 ? 1 : 2);

                                        break;

                                    case 3://分享 帖子

                                        String imgUrl = "";

                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
                                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
                                        }
                                        ShareUtil.getInstance().showShare(PostDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);

                                        break;

                                }
                            } else if (postInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {//自己的帖子

                                switch (type) {

                                    case 1://删除 帖子

                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                                        dialog.setText("", "确定删除帖子？", "取消", "确定");
                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                                            @Override
                                            public void onLeftClick() {
                                                //取消
                                            }

                                            @Override
                                            public void onRightClick() {
                                                getPostPresenter().deletePost(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), postInfo.getPostId());
                                            }
                                        });
                                        dialog.show(getSupportFragmentManager());

                                        break;

                                    case 2://分享 帖子

                                        String imgUrl = "";

                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
                                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
                                        }
                                        ShareUtil.getInstance().showShare(PostDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);


                                        break;

                                }

                            } else {//别人的帖子
                                switch (type) {

                                    case 1://举报 帖子
                                        SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
                                        selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
                                            @Override
                                            public void reportItemClick(Object object) {
                                                if (object instanceof ReportType) {

                                                    ReportType reportTypeObj = (ReportType) object;
                                                    getCommonPresenter().reportOperation(reportParentId, reportType
                                                            , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
                                                            , 0
                                                            , reportTypeObj.getReportTypeId());
                                                }
                                            }
                                        });
                                        selectorReportContentDialog.show(getSupportFragmentManager());
                                        break;

                                    case 2://分享 帖子

                                        String imgUrl = "";

                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
                                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
                                        }
                                        ShareUtil.getInstance().showShare(PostDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);


                                        break;

                                    case 3://拉黑 发帖子的人

                                        BaseUser baseUser = postInfo.getBaseUser();
                                        if (baseUser != null) {
                                            toBlacklistUserId = baseUser.getUserId();
                                            getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
                                        }

                                        break;

                                }
                            }

                        }
                    });


                } else {// 评论
                    Object object = postCommentListAdapter.getList().get(position);
                    if (object instanceof Comment) {
                        final Comment comment = (Comment) object;

                        commentPosition = position;

                        reportParentId = comment.getCommentId();

                        //内容类型(1--->评论,2--->回复,3--->匿名评论,4--->匿名回复),
                        if (comment.getType() == 1) {
                            reportType = 2;
                        } else if (comment.getType() == 2) {
                            reportType = 3;
                        }


                        if (comment.getFromBaseUser() != null) {
                            if (comment.getFromBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //自己的评论
                                dialog.setTextList("删除", "取消");
                            } else {
                                //用户角色(0-->普通成员,1-->管理员,2-->圈主),
                                if (postInfo.getUserRole() == 1 || postInfo.getUserRole() == 2) {
                                    if (comment.getAnonymousType() == 1) {
                                        dialog.setTextList("举报", "删除", "取消");
                                    } else {
                                        dialog.setTextList("举报", "删除", "拉黑", "取消");
                                    }

                                } else {
                                    if (comment.getAnonymousType() == 1) {
                                        dialog.setTextList("举报", "取消");
                                    } else {
                                        dialog.setTextList("举报", "拉黑", "取消");
                                    }

                                }
                            }

                            dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                                @Override
                                public void onClick(int type) {

                                    if ((postInfo.getUserRole() == 1 || postInfo.getUserRole() == 2) && comment.getFromBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                        switch (type) {

                                            case 1:// 举报 评论
                                                SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
                                                selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
                                                    @Override
                                                    public void reportItemClick(Object object) {
                                                        if (object instanceof ReportType) {

                                                            ReportType reportTypeObj = (ReportType) object;
                                                            getCommonPresenter().reportOperation(reportParentId, reportType
                                                                    , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
                                                                    , 0
                                                                    , reportTypeObj.getReportTypeId());
                                                        }
                                                    }
                                                });
                                                selectorReportContentDialog.show(getSupportFragmentManager());
                                                break;

                                            case 2:// 删除  评论

                                                ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                                                dialog.setText("", "确定删除评论？", "取消", "确定");
                                                dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                                                    @Override
                                                    public void onLeftClick() {
                                                        //取消
                                                    }

                                                    @Override
                                                    public void onRightClick() {
                                                        getPostPresenter().deleteComment(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), comment.getCommentId());
                                                    }
                                                });
                                                dialog.show(getSupportFragmentManager());


                                                break;

                                            case 3://拉黑 发评论的人

                                                BaseUser baseUser = comment.getFromBaseUser();
                                                if (baseUser != null) {
                                                    toBlacklistUserId = baseUser.getUserId();
                                                    long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                                                    toBlacklistUserId = baseUser.getUserId();
                                                    if (myUserId == toBlacklistUserId) {
                                                        return;
                                                    }
                                                    getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
                                                }

                                                break;


                                        }
                                    } else if (comment.getFromBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
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
                                                        getPostPresenter().deleteComment(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), comment.getCommentId());
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
                                                                    , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
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
                                                    long myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
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
                }
                dialog.show(getSupportFragmentManager());

            }

            @Override
            public void moreComment(long commentId) {
                Intent intent = new Intent(PostDetailActivity.this, MoreCommentActivity.class);
                intent.putExtra(Constants.Fields.MODULE_ID, postInfo.getPostId());
                intent.putExtra(Constants.Fields.TYPE, 1);
                intent.putExtra(Constants.Fields.COMMENT_PARENT_ID, commentId);
                startActivity(intent);
            }

            @Override
            public void ReplyListComment(Comment comment) {
                replyComment = comment;
                String fromBaseUserNickName;
                if (comment.getAnonymousType() == 1 || comment.getAnonymousType() == 3) {
                    fromBaseUserNickName = "匿名";
                } else {
                    fromBaseUserNickName = comment.getFromBaseUser().getNickName();
                }
                editText.setHint("回复：" + fromBaseUserNickName);
                if (commentImageRecyclerAdapter.getList() != null) {
                    commentImageRecyclerAdapter.getList().clear();
                    commentImageRecyclerAdapter.notifyDataSetChanged();

                    if (commentImageRecyclerAdapter.getItemCount() > 0) {
                        commentImageRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        commentImageRecyclerView.setVisibility(View.GONE);
                    }
                }
                videoPath = null;
                commentType = 2;
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
            }

            @Override
            public void sortViewClick() {
                if (sort == 1) {
                    sort = 2;
                } else if (sort == 2) {
                    sort = 1;
                }
                postCommentListAdapter.setSort(sort);
                requestCommentData();
            }

            @Override
            public void onApplyClerk(long storeId) {
                if(comeInType==10){
                    finish();
                }else {
//                    getStoreListPresenter().findStoreDetail(currentBaseUserId, storeId);
                    //跳转到店铺详情
                    Intent intent=new Intent(PostDetailActivity.this,ShopDetailActivity.class);
                    intent.putExtra(Constants.Fields.STORE_ID,storeId);
                    startActivity(intent);
                }
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


        if (InterfaceUrl.URL_FINDPOSTDETAIL.equals(apiName)) {

            if (object instanceof FindPostDetailResponse) {

                FindPostDetailResponse response = (FindPostDetailResponse) object;

                if (response.getCode() == 200) {
                    postInfo = response.getPostInfo();

                    if (postInfo == null) {
                        Utils.showToastShortTime(response.getMsg());
                        finish();
                        return;
                    }

                    initPostInfo(response.getPostInfo());

                    requestCommentData();

                } else {
                    Utils.showToastShortTime(response.getMsg());
                    finish();
                }
            }

        } else if (InterfaceUrl.URL_GETPOSTCOMMENTLIST.equals(apiName)) {
            if (object instanceof GetPostCommentListResponse) {
                GetPostCommentListResponse getPostCommentListResponse = (GetPostCommentListResponse) object;
                int code = getPostCommentListResponse.getCode();
                if (code == 200) {
                    List commentList = getPostCommentListResponse.getList();
                    if (commentList != null) {
                        if (pageNum == 1) {
                            postCommentListAdapter.setList(commentList);
                        } else {
                            postCommentListAdapter.addList(commentList);
                        }
                    }
                    if (getPostCommentListResponse.getTotal() == 0 && postCommentListAdapter.getItemCount() == 1) {
                        postCommentListAdapter.addEmptyItem(getPostCommentListResponse.getBlankHint());
                    }


                    if (getPostCommentListResponse.getTotal() <= postCommentListAdapter.getItemCount() - 1) {
                        // 没有更多数据
                        refreshLayout.setEnableLoadmore(false);
                    } else {
                        pageNum++;
                        refreshLayout.setEnableLoadmore(true);
                    }

                } else {
                    Utils.showToastShortTime(getPostCommentListResponse.getMsg());
                }

            }
        } else if (InterfaceUrl.URL_POSTPRAISE.equals(apiName)) {
            if (object instanceof PostPraiseResponse) {
                PostPraiseResponse postPraiseResponse = (PostPraiseResponse) object;
                if (postPraiseResponse.getCode() == 200) {
                    int isPraise = postPraiseResponse.getIsPraise();
                    EventBus.getDefault().post(new UpdatePostListEvent(2, selectPostIndex, 0, isPraise, comeInType));
                }

            }
        } else if (InterfaceUrl.URL_ATTENTIONOPERATION.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                Utils.showToastShortTime(response.getMsg());

                if (response.getCode() == 200) {

                    PostInfo postInfo = (PostInfo) postCommentListAdapter.getList().get(0);
                    // 关注状态(0-->未关注,1-->已关注)
                    postInfo.getBaseUser().setIsAttention(postInfo.getBaseUser().getIsAttention() == 0 ? 1 : 0);
                    postCommentListAdapter.notifyItemChanged(0, true);

                }


            }
        } else if (InterfaceUrl.URL_COMMENTUPLOADIMAGE.equals(apiName)) {
            if (object instanceof PostCommentResponse) {
                PostCommentResponse postCommentResponse = (PostCommentResponse) object;
                Utils.showToastShortTime(postCommentResponse.getMsg());
                if (postCommentResponse.getCode() == 200) {

                    // 更新评论数
                    PostInfo postInfo = (PostInfo) postCommentListAdapter.getList().get(0);
                    postInfo.setCommentCount(postInfo.getCommentCount() + 1);
                    postCommentListAdapter.notifyItemChanged(0, true);
                    // 发送事件更新列表
                    EventBus.getDefault().post(new UpdatePostListEvent(1, selectPostIndex, postInfo.getCommentCount(), 0, comeInType));

                    final List<Image> imgList = commentImageRecyclerAdapter.getList();
                    if (imgList != null && imgList.size() > 0) {
                        for (int i = 0; i < imgList.size(); i++) {
                            Image image = imgList.get(i);
                            getImagePresenter().uploadImages(postCommentResponse.getComment().getCommentId(), 15, currentBaseUserId, DateUtils.getFormatDataString(new java.util.Date(), DateUtils.DATE_FORMAT_2), image.getImageUrl(), 0, i + 1, imgList.size());
                        }
                        commentImageRecyclerAdapter.getList().clear();
                        commentImageRecyclerAdapter.notifyDataSetChanged();
                        maxSelectNum = 9;
                        if (commentImageRecyclerAdapter.getItemCount() > 0) {
                            commentImageRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            commentImageRecyclerView.setVisibility(View.GONE);
                        }
                    }

//
//                    // 更新评论
//                    Comment comment = postCommentResponse.getComment();
//
////                    List<Object> list = postCommentListAdapter.getList();
////                    if (list.get(list.size() - 1) instanceof String) {
////                        postCommentListAdapter.getList().remove(list.size() - 1);
////                    }
////
//                    postCommentListAdapter.addItem(comment, false);
////
//                    postCommentListAdapter.notifyDataSetChanged();
////
//                    recyclerView.smoothScrollToPosition(1);
////

                    pageNum = 1;
                    requestCommentData();
                }
            }
        } else if (InterfaceUrl.URL_DELETEPOST.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.getCode() == 200) {
                    Utils.showToastShortTime(response.getMsg());

                    // 删除帖子
                    EventBus.getDefault().post(new UpdatePostListEvent(5, selectPostIndex));


                    finish();

                }
            }
        } else if (InterfaceUrl.URL_POSTTOP.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                Utils.showToastShortTime(response.getMsg());

                if (response.getCode() == 200) {

                    // 置顶和取消置顶
                    EventBus.getDefault().post(new UpdatePostListEvent(postInfo.getIsTop() == 0 ? 6 : 7, selectPostIndex));

                    PostInfo postInfo = (PostInfo) postCommentListAdapter.getList().get(0);
                    //是否置顶(0取消,1置顶),
                    postInfo.setIsTop(postInfo.getIsTop() == 0 ? 1 : 0);
                    postCommentListAdapter.notifyItemChanged(0);

                }
            }
        } else if (InterfaceUrl.URL_DELETECOMMENT.equals(apiName)) {

            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.getCode() == 200) {
                    Utils.showToastShortTime(response.getMsg());

                    // 更新评论数
                    PostInfo postInfo = (PostInfo) postCommentListAdapter.getList().get(0);
                    postInfo.setCommentCount(postInfo.getCommentCount() - 1);
                    postCommentListAdapter.notifyItemChanged(0, true);

                    EventBus.getDefault().post(new UpdatePostListEvent(1, selectPostIndex, postInfo.getCommentCount(), 0, comeInType));


                    if (commentPosition < 0) {
                        return;
                    }

                    // 更新评论列表

                    if (postCommentListAdapter == null) {
                        return;
                    }

                    if (postCommentListAdapter.getList() == null) {
                        return;
                    }

                    if (postCommentListAdapter.getList().size() <= commentPosition) {
                        return;
                    }

                    postCommentListAdapter.getList().remove(commentPosition);
                    postCommentListAdapter.notifyItemRemoved(commentPosition);
                    postCommentListAdapter.notifyItemRangeChanged(commentPosition, postCommentListAdapter.getItemCount() - commentPosition);
                    commentPosition = -1;


                }
            }


        } else if (InterfaceUrl.URL_REPORTOPERATION.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.getCode() == 200) {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        } else if (InterfaceUrl.URL_PULLBLACKUSER.equals(apiName)) {
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
        } else if (InterfaceUrl.URL_COMMENTPRAISE.equals(apiName)) {

            if (object instanceof CommentPraiseResponse) {
                CommentPraiseResponse response = (CommentPraiseResponse) object;
                Utils.showToastShortTime(response.getMsg());
            }

        }else if (apiName.equals(InterfaceUrl.URL_FINDSTOREDETAIL)) {
            if (object instanceof FindStoreDetailResponse) {
                FindStoreDetailResponse findStoreDetailResponse = (FindStoreDetailResponse) object;
                if (findStoreDetailResponse.getCode() == 200) {
                    StoreDetail storeDetail = findStoreDetailResponse.getStoreDetail();
                    if (storeDetail != null) {
                        int isClert = storeDetail.getIsClert();//当前是否是店员(1店主,2店员,3普通用户,4店员申请中),
                        StoreInfo storeInfo=storeDetail.getStoreInfo();
                        int isPublish=storeInfo.getIsPublish();
                        if(isPublish==1){
                            if (isClert == 1) {
                                //店长自己不跳转
                            }else if (isClert == 2) {
                                Utils.showToastShortTime("您已成为该店店员");
                            } else if (isClert == 3) {
                                //跳到店员申请界面
                                Intent intent = new Intent(PostDetailActivity.this, ApplyClerkActivity.class);
                                intent.putExtra(Constants.Fields.STORE_ID, storeInfo.getStoreId());
                                startActivity(intent);
                            } else if (isClert == 4) {
                                //店员申请中
                                Utils.showToastShortTime("店员申请中");
                            }
                        }else{
                            Utils.showToastShortTime("该店已关闭招聘信息");
                        }
                    }
                } else {
                    Utils.showToastShortTime(findStoreDetailResponse.getMsg());
                }
            }
        }
    }

    public void initPostInfo(PostInfo postInfo) {

        if (postInfo == null) {
            return;
        }

        if (postInfo.getBaseUser() == null) {
            return;
        }

        if (postCommentListAdapter != null) {
            postCommentListAdapter.setPostItemView(postInfo);
        }

        recyclerView.smoothScrollBy(0, 0);

    }

    @Override
    public void showLoading() {

        ProgressDialogUtils.getInstance().showProgressDialog(this);

    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ShareNumUpdateEvent event) {
        if (event == null) {
            return;
        }
        if (event.getType() == ShareUtil.POST) {
            //本地更新帖子分享次数
            postInfo.setShareNumber(postInfo.getShareNumber() + 1);
            postCommentListAdapter.setPostItemView(postInfo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVideoCommentFinish(VideoCommentFinishEvent event) {
        if (event == null) {
            return;
        }
        if (event.getModule() == 1) {
            pageNum = 1;
            requestCommentData();
            if (commentImageRecyclerAdapter.getList() != null) {
                commentImageRecyclerAdapter.getList().clear();
            }
            setSendViewBg(editText.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
            commentImageRecyclerAdapter.notifyDataSetChanged();
            if (commentImageRecyclerAdapter.getItemCount() > 0) {
                commentImageRecyclerView.setVisibility(View.VISIBLE);
            } else {
                commentImageRecyclerView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (rootView != null) {
            rootView = null;
        }

        if (postCommentListAdapter != null) {
            postCommentListAdapter.onDestroy();
        }


        EventBus.getDefault().unregister(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectMode == 3) {
                        if (commentImageRecyclerAdapter.getList() != null) {
                            commentImageRecyclerAdapter.getList().clear();
                        }
                    }
                    ArrayList<Image> imageList = new ArrayList(1);
                    for (LocalMedia info : selectList) {
                        Image image = new Image();
                        if (info.isCompressed()) {
                            image.setImageUrl(info.getCompressPath());
                        } else {
                            image.setImageUrl(info.getPath());
                        }
                        imageList.add(image);
                        maxSelectNum--;
                    }

                    selectList.clear();
                    commentImageRecyclerAdapter.setList(imageList);
                    commentImageRecyclerAdapter.notifyDataSetChanged();

                    if (commentImageRecyclerAdapter.getItemCount() > 0) {
                        commentImageRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        commentImageRecyclerView.setVisibility(View.GONE);
                    }
                    setSendViewBg(editText.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
                    break;
                case VIDEO_CODE:
                case VIDEO_PICKER_CODE:

                    if (VIDEO_PICKER_CODE == requestCode) {
                        Uri selectedMediaUri = data.getData();
                        videoPath = UriUtils.getFileAbsolutePath(App.getInstance(), selectedMediaUri);
                    } else if (VIDEO_CODE == requestCode) {
                        videoPath = data.getStringExtra(Constants.KEY.KEY_EXTRA);
                    }

                    Log.e("videoPath ==== ", videoPath);

                    if (StringUtils.isEmpty(videoPath)) {
                        Log.e("==文件路径是null== ", videoPath);
                        return;
                    }

                    File file = new File(videoPath);
                    Log.e("file.length() ==== ", file.length() + "");

                    if (file.length() <= 0) {
                        Utils.showToastShortTime("对不起，您选择的文件无效");
                        return;
                    }

                    // 视频类型
                    try {
                        String suffix = videoPath.substring(videoPath.lastIndexOf(".") + 1, videoPath.length());
                        Log.e("suffix ==== ", suffix);
                        if (StringUtils.isEmpty(suffix)) {
                            Utils.showToastShortTime("对不起，您选择的文件格式无效");
                            return;
                        }

                        // 判断文件是否是视频文件
                        boolean isVideo = false;
                        for (int i = 0; i < videoSuffixs.length; i++) {
                            if (videoSuffixs[i].equals(suffix)) {
                                isVideo = true;
                                break;
                            }
                        }


                        if (!isVideo) {
                            Utils.showToastShortTime("视频文件格式(" + suffix + ")不正确");
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showToastShortTime("对不起，系统解析文件错误");
                        return;
                    }

                    if (file.length() / (1024 * 1024) > 50) {
                        Utils.showToastShortTime("文件过大，请不要超过50M");
                        return;
                    }

                    if (!StringUtils.isEmpty(videoPath)) {
                        // 视频缩略图
                        RequestOptions options = new RequestOptions().centerCrop();
                        Glide.with(App.getInstance()).asBitmap().load(videoPath).apply(options).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                if (resource != null) {
                                    if (commentImageRecyclerAdapter.getList() != null) {
                                        commentImageRecyclerAdapter.getList().clear();
                                    }
                                    ArrayList<Image> videoImageList = new ArrayList(1);
                                    Image image = new Image();
                                    image.setImageUrl(videoPath);
                                    videoImageList.add(image);
                                    commentImageRecyclerAdapter.setList(videoImageList);
                                    commentImageRecyclerAdapter.notifyDataSetChanged();

                                    if (commentImageRecyclerAdapter.getItemCount() > 0) {
                                        commentImageRecyclerView.setVisibility(View.VISIBLE);
                                    } else {
                                        commentImageRecyclerView.setVisibility(View.GONE);
                                    }
                                    setSendViewBg(editText.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
                                    int width = resource.getWidth();
                                    int height = resource.getHeight();

                                    Logger.d("width =原图1== " + width + " ; height =原图1== " + height);

                                    float ww = BitmapUtils.getBitmapWidth(App.getInstance());
                                    float hh = BitmapUtils.getBitmapHeight(App.getInstance());
                                    Logger.d("ww ==指定大小== " + ww + " ; hh ==指定大小== " + hh);

                                    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                                    double scale = 1;//scale=1表示不缩放
                                    if (width > height && width > ww) {//如果宽度大的话根据宽度固定大小缩放
                                        scale = width / ww;
                                    } else if (width < height && height > hh) {//如果高度高的话根据宽度固定大小缩放
                                        scale = height / hh;
                                    }

                                    Logger.d("scale ==缩放比== " + scale);

                                    if (scale < 1) {
                                        scale = 1;
                                    }

                                    width = (int) (width / scale);
                                    height = (int) (height / scale);

                                    Logger.d("width =scale=最终= " + width + " ; height =scale=最终= " + height);

                                    imgWidth = width;
                                    imgHeight = height;


                                }
                            }
                        });

                    } else {
                        Utils.showToastShortTime("没有选择视频文件!");
                    }
                    KeyBoardUtils.openKeybord(editText, App.getInstance());
                    break;
                default:
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_image:
                if (selectMode != 1) {
                    selectMode = 1;
                    maxSelectNum = 9;
                    if (commentImageRecyclerAdapter.getList() != null) {
                        commentImageRecyclerAdapter.getList().clear();
                    }
                }
                Utils.hideSystemSoftInputKeyboard(editText);
                if (maxSelectNum == 0) {
                    Utils.showToastShortTime("您最多可选9张图片");
                } else {
                    selectPic();
                }
                break;
            case R.id.img_gif:
                selectMode = 3;
                maxSelectNum = 1;
                Utils.hideSystemSoftInputKeyboard(editText);
                selectPic();
                break;
            case R.id.img_video:
                selectMode = 2;
                Utils.hideSystemSoftInputKeyboard(editText);
                ActiveEditSelectDialog dialog = new ActiveEditSelectDialog();
                dialog.setItemText("拍摄", "从手机相册选择");
                dialog.setItemClick(new ActiveEditSelectDialog.ItemClick() {
                    @Override
                    public void itemClick(int position) {
                        switch (position) {
                            case 1:
                                //拍摄
                                Intent intent = new Intent(PostDetailActivity.this, PublishVideoActivity.class);
                                intent.putExtra(Constants.Fields.TYPE, 4);
                                intent.putExtra(Constants.Fields.FLAG, 1);
                                startActivityForResult(intent, VIDEO_CODE);
                                break;
                            case 2:
                                //从手机相册选择
                                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                pickIntent.setType("video/*");
                                pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(pickIntent, VIDEO_PICKER_CODE);
                                break;
                            default:
                        }
                    }
                });
                dialog.show(getSupportFragmentManager());
                break;
            default:
        }
    }

    private void selectPic() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(maxSelectNum)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(selectMode == 3 ? PictureConfig.SINGLE : PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .enableCrop(false)// 是否裁剪 true or false
                .compress(selectMode != 3)// 是否压缩 true or false
                .glideOverride(Utils.dp2px(110), Utils.dp2px(110))// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif(selectMode == 3)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(true)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .cropWH(Utils.dp2px(110), Utils.dp2px(110))// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
