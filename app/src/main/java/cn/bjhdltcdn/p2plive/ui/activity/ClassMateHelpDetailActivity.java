//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
//import com.orhanobut.logger.Logger;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.ClassMateHelpEvent;
//import cn.bjhdltcdn.p2plive.event.ShareNumUpdateEvent;
//import cn.bjhdltcdn.p2plive.event.VideoCommentFinishEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindHelpDetailResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHelpCommentListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.HelpCommentResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.HelpPraiseResponse;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.HelpComment;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.ReportType;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ClassMateHelpPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.service.SingleTaskShortVideoUploaderIntentService;
//import cn.bjhdltcdn.p2plive.ui.adapter.ClassMateHelpDetailAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.CommentImageRecyclerAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveEditSelectDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.SelectorReportContentDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.BitmapUtils;
//import cn.bjhdltcdn.p2plive.utils.DateUtils;
//import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.UriUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;
//
///**
// * 帮帮忙详情页
// * 注意：使用到视频播发器必须在配置文件加 android:hardwareAccelerated="true" 属性，否则会黑屏（播放不出来画面）
// */
//
//public class ClassMateHelpDetailActivity extends BaseActivity implements BaseView, View.OnClickListener {
//
//
//    private View rootView;
//
//    private RecyclerView recyclerView;
//
//    private TwinklingRefreshLayout refreshLayout;
//    private LoadingView loadingView;
//    private int pageSize = 20, pageNum = 1;
//    private HelpInfo helpInfo;
//    private int selectPostIndex;
//
//    private EditText editText;
//    private TextView sendTextView;
//    private View sendView;
//    private ImageView img_image, img_gif, img_video;
//    /**
//     * 评论图片列表
//     */
//    private RecyclerView commentImageRecyclerView;
//    /**
//     * 选择类型
//     * 1图片 2视频 3gif
//     */
//    private int selectMode;
//    /**
//     * 评论图片最大选择数量
//     */
//    int maxSelectNum = 9;
//    private int commentPosition = -1;
//    //本地选择视频
//    private final int VIDEO_PICKER_CODE = 2;
//    //打开录制视频页面
//    private final int VIDEO_CODE = 1;
//    private String videoSuffixs[] = {"mp4", "avi", "mov", "mkv", "flv", "f4v", "rmvb"};
//    private String videoPath;//视频路径
//    //视频图片的宽高
//    private int imgWidth, imgHeight;
//
//    //评论类型(1--->评论,2--->回复)
//    private int commentType = 1;
//    //举报类型
//    int reportType;
//    private long toBlacklistUserId;
//
//    //排序(1按热度,2按时间)
//    private int sort = 1;
//    private long currentBaseUserId;
//    //二级评论对象，点击二级回复使用
//    private HelpComment replyComment;
//    private UserPresenter userPresenter;
//    private ClassMateHelpPresenter classMateHelpPresenter;
//    private PostCommentListPresenter postCommentListPresenter;
//    private CommonPresenter commonPresenter;
//    private ClassMateHelpDetailAdapter classMateHelpDetailAdapter;
//    private CommentImageRecyclerAdapter commentImageRecyclerAdapter;
//    private ImagePresenter imagePresenter;
//    private long helpId;
//
//    public ClassMateHelpPresenter getClassMateHelpPresenter() {
//        if (classMateHelpPresenter == null) {
//            classMateHelpPresenter = new ClassMateHelpPresenter(this);
//        }
//        return classMateHelpPresenter;
//    }
//
//    public PostCommentListPresenter getPostPresenter() {
//        if (postCommentListPresenter == null) {
//            postCommentListPresenter = new PostCommentListPresenter(this);
//        }
//        return postCommentListPresenter;
//
//
//    }
//
//    public ImagePresenter getImagePresenter() {
//        if (imagePresenter == null) {
//            imagePresenter = new ImagePresenter(this);
//        }
//        return imagePresenter;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//        setContentView(R.layout.activity_post_detail);
//
//        getExtraDate(getIntent());
//
//    }
//
//    private void getExtraDate(Intent intent) {
//
//        helpInfo = intent.getParcelableExtra(Constants.KEY.KEY_OBJECT);
//        selectPostIndex = intent.getIntExtra(Constants.Fields.POSITION, -1);
//
//        helpId = intent.getLongExtra(Constants.Fields.HELP_ID, 0);
//
//        if (helpInfo == null && helpId > 0) {
//            HelpInfo helpInfo = new HelpInfo();
//            helpInfo.setHelpId(helpId);
//        }
//
//        rootView = findViewById(R.id.root_layout);
//        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//
//        setTitle();
//
//        initView();
//
//        //initPostInfo(helpInfo);
//
//        initSendView();
//
//        setOtherClick();
//
//        requestDetailData();
//        requestCommentData();
//
//    }
//
//    private void setTitle() {
//        final TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_helpt_detail);
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                Utils.hideSystemSoftInputKeyboard(editText);
//                finish();
//            }
//        });
//    }
//
//    private void initView() {
//
//        recyclerView = findViewById(R.id.recycle_view);
//        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
//            @Override
//            public boolean onFling(int velocityX, int velocityY) {
//                Utils.hideSystemSoftInputKeyboard(editText);
//                return false;
//            }
//        });
//        if (classMateHelpDetailAdapter == null) {
//            classMateHelpDetailAdapter = new ClassMateHelpDetailAdapter(null, this);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
//            layoutManager.setAutoMeasureEnabled(true);
//            recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1)));
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setAdapter(classMateHelpDetailAdapter);
//        }
//
//        // 刷新框架
//        refreshLayout = findViewById(R.id.refresh_layout_view);
//        // 头部加载样式
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        // 底部加载样式
//        loadingView = new LoadingView(this);
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNum = 1;
//                refreshLayout.finishRefreshing();
//                requestCommentData();
//                requestDetailData();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                requestCommentData();
//
//                refreshLayout.finishLoadmore();
//            }
//        });
//
//    }
//
//    /**
//     * 查询帮帮忙详情
//     */
//    private void requestDetailData() {
//        getClassMateHelpPresenter().findHelpDetail(currentBaseUserId, helpInfo.getHelpId());
//    }
//
//    /**
//     * 请求帮帮忙评论
//     */
//    private void requestCommentData() {
//        getClassMateHelpPresenter().getHelpCommentList(currentBaseUserId, helpInfo.getHelpId(), sort, pageSize, pageNum);
//    }
//
//
//    private void initSendView() {
//
//        sendView = findViewById(R.id.send_comment_view);
//        editText = sendView.findViewById(R.id.reply_edit_input);
//        sendTextView = sendView.findViewById(R.id.send_view);
//        commentImageRecyclerView = sendView.findViewById(R.id.recycler_image_comment);
//        commentImageRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager hlayoutManager = new LinearLayoutManager(App.getInstance());
//        hlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        commentImageRecyclerView.setLayoutManager(hlayoutManager);
//        commentImageRecyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(12), false));
//        commentImageRecyclerAdapter = new CommentImageRecyclerAdapter(this);
//        commentImageRecyclerView.setAdapter(commentImageRecyclerAdapter);
//        commentImageRecyclerAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (selectMode != 2) {
//                    ImageViewPageDialog.newInstance(commentImageRecyclerAdapter.getList(), position).show(getSupportFragmentManager());
//                }
//            }
//        });
//        commentImageRecyclerAdapter.setOnDeleteItemClick(new CommentImageRecyclerAdapter.OnDeleteItemClick() {
//            @Override
//            public void deleteItemClick(int position) {
//                if (commentImageRecyclerAdapter != null && commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > position) {
//                    videoPath = null;
//                    commentImageRecyclerAdapter.getList().remove(position);
//                    maxSelectNum++;
//
//                    commentImageRecyclerAdapter.notifyDataSetChanged();
//
//                    if (commentImageRecyclerAdapter.getItemCount() > 0) {
//                        commentImageRecyclerView.setVisibility(View.VISIBLE);
//                    } else {
//                        commentImageRecyclerView.setVisibility(View.GONE);
//                    }
//                }
//
//            }
//        });
//        img_image = sendView.findViewById(R.id.img_image);
//        img_gif = sendView.findViewById(R.id.img_gif);
//        img_video = sendView.findViewById(R.id.img_video);
//        img_image.setOnClickListener(this);
//        img_gif.setOnClickListener(this);
//        img_video.setOnClickListener(this);
//
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                boolean imageOrVideoNotNull = commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0;
//                setSendViewBg(s, imageOrVideoNotNull);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        sendTextView.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//
//                String text = editText.getText().toString().trim();
//                long parentId = 0;
//                long toUserId = 0;
//                long commentParentId = 0;
//                if (helpInfo != null && helpInfo.getBaseUser() != null) {
//                    toUserId = helpInfo.getBaseUser().getUserId();
//                }
//                if (replyComment == null) {
//                    if (commentPosition < 0) {
//                        return;
//                    }
//
//                    Object object = classMateHelpDetailAdapter.getList().get(commentPosition);
//                    if (object instanceof HelpInfo) {// 推送消息使用
//                        HelpInfo postInfo = (HelpInfo) object;
//                        if (postInfo.getBaseUser() != null) {
//                            toUserId = postInfo.getBaseUser().getUserId();
//                        }
//
//                    } else if (object instanceof HelpComment) {
//                        HelpComment comment = (HelpComment) object;
//                        parentId = comment.getCommentId();
//                        commentParentId = parentId;
//                        if (comment.getFromBaseUser() != null) {
//                            toUserId = comment.getFromBaseUser().getUserId();
//                        }
//
//                    }
//                } else {
//                    parentId = replyComment.getCommentId();
//                    commentParentId = replyComment.getCommentParentId();
//                    if (replyComment.getFromBaseUser() != null) {
//                        toUserId = replyComment.getFromBaseUser().getUserId();
//                    }
//                }
//
//                if (TextUtils.isEmpty(videoPath)) {//图文
//                    boolean imageOrVideoNotNull = commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0;
//                    if (!TextUtils.isEmpty(text) || imageOrVideoNotNull) {
//                        getPostPresenter().commentUploadImage(currentBaseUserId, helpInfo.getHelpId(), 4, text, commentType, 0,
//                                toUserId, currentBaseUserId, parentId, commentParentId, 1, null, null, DateUtils.getTime(new Date()), null);
//                    } else {
//                        return;
//                    }
//                } else {
//                    if (StringUtils.isEmpty(videoPath)) {
//                        Utils.showToastShortTime("请添加视频");
//                        return;
//                    }
//
//                    LinkedHashMap<String, Object> map = new LinkedHashMap<>(1);
//                    map.put(Constants.Fields.VIDEO_PATH, videoPath);
//                    map.put(Constants.Fields.IMG_WIDTH, imgWidth);
//                    map.put(Constants.Fields.IMG_HEIGHT, imgHeight);
//                    map.put(Constants.Fields.CONTENT_STR, text);
//                    map.put(Constants.Fields.IS_ANONYMOUS, 0);
//                    map.put(Constants.Fields.LAUNCH_ID, helpInfo.getHelpId());
//                    map.put(Constants.Fields.TO_USER_ID, toUserId);
//                    map.put(Constants.Fields.ENTER_TYPE_MODE, 6);
//                    map.put(Constants.Fields.TYPE, 4);
//                    SingleTaskShortVideoUploaderIntentService.startShortVideoUploader(ClassMateHelpDetailActivity.this, map);
//                    Utils.showToastShortTime("视频上传中，上传完成后将自动发布。");
//                }
//                videoPath = null;
//                editText.setText("");
//                sendView.setVisibility(View.GONE);
//                KeyBoardUtils.closeKeybord(editText, App.getInstance());
//
//                commentPosition = -1;
//
//            }
//        });
//
//    }
//
//    /**
//     * 评论设置按钮背景
//     *
//     * @param s
//     * @param imageOrVideoNotNull
//     */
//    private void setSendViewBg(CharSequence s, boolean imageOrVideoNotNull) {
//        if (s != null && s.length() > 0 || imageOrVideoNotNull) {
//            sendTextView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//            sendTextView.setTextColor(getResources().getColor(R.color.color_333333));
//        } else {
//            sendTextView.setBackgroundResource(R.drawable.shape_round_10_solid_fff69f);
//            sendTextView.setTextColor(getResources().getColor(R.color.color_999999));
//        }
//    }
//
//
//    private void setOtherClick() {
//
//        classMateHelpDetailAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (position > 0) {
//                    img_image.setVisibility(View.GONE);
//                    img_gif.setVisibility(View.GONE);
//                    img_video.setVisibility(View.GONE);
//                    Object object = classMateHelpDetailAdapter.getList().get(position);
//                    if (object instanceof HelpComment) {
//                        HelpComment comment = (HelpComment) object;
//                        //自己的评论不能回复
//                        if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
//                            return;
//                        } else {
//                            editText.setHint("回复：" + comment.getFromBaseUser().getNickName());
//                            if (commentImageRecyclerAdapter.getList() != null) {
//                                commentImageRecyclerAdapter.getList().clear();
//                                commentImageRecyclerAdapter.notifyDataSetChanged();
//
//                                if (commentImageRecyclerAdapter.getItemCount() > 0) {
//                                    commentImageRecyclerView.setVisibility(View.VISIBLE);
//                                } else {
//                                    commentImageRecyclerView.setVisibility(View.GONE);
//                                }
//                            }
//                            videoPath = null;
//                        }
//                        commentType = 2;
//                    } else if (object instanceof String) {//空消息
//                        img_image.setVisibility(View.VISIBLE);
//                        img_gif.setVisibility(View.VISIBLE);
//                        img_video.setVisibility(View.VISIBLE);
//                        commentType = 1;
//                    }
//
//
//                    if (sendView != null) {
//                        sendView.setVisibility(View.VISIBLE);
//                    }
//
//                    if (editText != null) {
//                        editText.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                KeyBoardUtils.openKeybord(editText, App.getInstance());
//                            }
//                        }, 500);
//
//                    }
//
//                    commentPosition = position;
//                }
//            }
//        });
//
//        classMateHelpDetailAdapter.setViewClick(new ClassMateHelpDetailAdapter.ViewClick() {
//            @Override
//            public void onPraise(int itemType, long helpId, int type, int position) {// 点赞
//                if (itemType == 0) {
//                    getClassMateHelpPresenter().helpPraise(currentBaseUserId, helpId, type);
//                } else {
//                    getPostPresenter().commentPraise(helpId, type, currentBaseUserId, 4);
//                }
//            }
//
//            @Override
//            public void onComment(long postId, int type, final int position) {// 评论
//                img_image.setVisibility(View.VISIBLE);
//                img_gif.setVisibility(View.VISIBLE);
//                img_video.setVisibility(View.VISIBLE);
//                if (sendView != null) {
//                    sendView.setVisibility(View.VISIBLE);
//                }
//                setSendViewBg(editText.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
//                if (editText != null) {
//                    editText.setHint("说点什么吧...");
//                    editText.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            KeyBoardUtils.openKeybord(editText, App.getInstance());
//                        }
//                    }, 500);
//
//                }
//
//                commentPosition = position;
//                commentType = 1;
//
//
//            }
//
//            @Override
//            public void attentionView(int type, long userId, int position) {// 关注
//                // 关注状态(0-->未关注,1-->已关注)
//                if (type == 0) {
//                    type = 1;
//                } else if (type == 1) {
//                    type = 2;
//                }
//                getUserPresenter().attentionOperation(type, currentBaseUserId, userId);
//
//            }
//
//            @Override
//            public void moreImg(final int itemType, int position) {
//                final PostOperationFragmentDialog dialog = new PostOperationFragmentDialog();
//                if (itemType == 0) {
//                    final HelpInfo helpInfo = (HelpInfo) classMateHelpDetailAdapter.getList().get(position);
//                    if (helpInfo.getBaseUser().getUserId() == currentBaseUserId) {
//                        //自己的帖子
//                        dialog.setTextList("删除", "取消");
//                    } else {//别人的帖子
//                        dialog.setTextList("举报", "拉黑", "取消");
//                    }
//
//                    final long helpId = helpInfo.getHelpId();
//                    //reportType = 1;
//
//                    dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                        @Override
//                        public void onClick(int type) {
//
//                            //用户角色：圈主或者管理员是自己
//                            if (helpInfo.getBaseUser().getUserId() == currentBaseUserId) {//自己的帖子
//
//                                switch (type) {
//
//                                    case 1://删除 帖子
//
//                                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                        dialog.setText("", "确定删除同学帮帮忙？", "取消", "确定");
//                                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                            @Override
//                                            public void onLeftClick() {
//                                                //取消
//                                            }
//
//                                            @Override
//                                            public void onRightClick() {
//                                                getClassMateHelpPresenter().deleteHelp(currentBaseUserId, helpInfo.getHelpId());
//                                            }
//                                        });
//                                        dialog.show(getSupportFragmentManager());
//
//                                        break;
//
//                                    /*case 2://分享 帖子
//
//                                        String imgUrl = "";
//
//                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                                        }
//                                        ShareUtil.getInstance().showShare(ClassMateHelpDetailActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//
//
//                                        break;*/
//
//                                }
//
//                            } else {//别人的帖子
//                                switch (type) {
//
//                                    case 1://举报 帖子
//                                        SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                        selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                            @Override
//                                            public void reportItemClick(Object object) {
//                                                if (object instanceof ReportType) {
//
//                                                    ReportType reportTypeObj = (ReportType) object;
//                                                    getCommonPresenter().reportOperation(helpId, 12
//                                                            , currentBaseUserId
//                                                            , helpInfo.getBaseUser().getUserId()
//                                                            , reportTypeObj.getReportTypeId());
//                                                }
//                                            }
//                                        });
//                                        selectorReportContentDialog.show(getSupportFragmentManager());
//                                        break;
//
//
//                                    case 2://拉黑 发帖子的人
//
//                                        BaseUser baseUser = helpInfo.getBaseUser();
//                                        if (baseUser != null) {
//                                            toBlacklistUserId = baseUser.getUserId();
//                                            getUserPresenter().pullBlackUser(currentBaseUserId, toBlacklistUserId);
//                                        }
//
//                                        break;
//
//                                }
//                            }
//
//                        }
//                    });
//
//
//                } else {
//                    // 评论
//                    Object object = classMateHelpDetailAdapter.getList().get(position);
//                    if (object instanceof HelpComment) {
//                        final HelpComment comment = (HelpComment) object;
//
//                        commentPosition = position;
//
//                        final long reportParentId = comment.getCommentId();
//                        //内容类型(评论13 回复14),
//                        if (comment.getType() == 1) {
//                            reportType = 13;
//                        } else if (comment.getType() == 2) {
//                            reportType = 14;
//                        }
//
//
//                        if (comment.getFromBaseUser() != null) {
//                            //用户角色(0-->普通成员,1-->管理员,2-->圈主),
//                            if ((helpInfo.getUserRole() == 1 || helpInfo.getUserRole() == 2) && comment.getFromBaseUser().getUserId() != currentBaseUserId) {
//                                dialog.setTextList("举报", "删除", "拉黑", "取消");
//                            } else if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
//                                //自己的评论
//                                dialog.setTextList("删除", "取消");
//                            } else {//别人的评论
//                                dialog.setTextList("举报", "拉黑", "取消");
//                            }
//
//                            final int finalReportType = reportType;
//                            dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                                @Override
//                                public void onClick(int type) {
//
//                                    if ((helpInfo.getUserRole() == 1 || helpInfo.getUserRole() == 2) && comment.getFromBaseUser().getUserId() != currentBaseUserId) {
//                                        switch (type) {
//
//                                            case 1:// 举报 评论
//                                                SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                                selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                    @Override
//                                                    public void reportItemClick(Object object) {
//                                                        if (object instanceof ReportType) {
//
//                                                            ReportType reportTypeObj = (ReportType) object;
//                                                            getCommonPresenter().reportOperation(reportParentId, reportType
//                                                                    , currentBaseUserId
//                                                                    , 0
//                                                                    , reportTypeObj.getReportTypeId());
//                                                        }
//                                                    }
//                                                });
//                                                selectorReportContentDialog.show(getSupportFragmentManager());
//                                                break;
//
//                                            case 2:// 删除  评论
//
//                                                ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                                dialog.setText("", "确定删除评论？", "取消", "确定");
//                                                dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                    @Override
//                                                    public void onLeftClick() {
//                                                        //取消
//                                                    }
//
//                                                    @Override
//                                                    public void onRightClick() {
//                                                        getClassMateHelpPresenter().deleteHelpComment(currentBaseUserId, reportParentId);
//                                                    }
//                                                });
//                                                dialog.show(getSupportFragmentManager());
//
//
//                                                break;
//
//                                            case 3://拉黑 发评论的人
//
//                                                BaseUser baseUser = comment.getFromBaseUser();
//                                                if (baseUser != null) {
//                                                    toBlacklistUserId = baseUser.getUserId();
//                                                    toBlacklistUserId = baseUser.getUserId();
//                                                    if (currentBaseUserId == toBlacklistUserId) {
//                                                        return;
//                                                    }
//                                                    getUserPresenter().pullBlackUser(currentBaseUserId, toBlacklistUserId);
//                                                }
//
//                                                break;
//
//
//                                        }
//                                    } else if (comment.getFromBaseUser().getUserId() == currentBaseUserId) {
//                                        switch (type) {
//
//                                            case 1:// 删除  评论
//
//                                                ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                                                dialog.setText("", "确定删除评论？", "取消", "确定");
//                                                dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                                    @Override
//                                                    public void onLeftClick() {
//                                                        //取消
//                                                    }
//
//                                                    @Override
//                                                    public void onRightClick() {
//                                                        getClassMateHelpPresenter().deleteHelpComment(currentBaseUserId, comment.getCommentId());
//                                                    }
//                                                });
//                                                dialog.show(getSupportFragmentManager());
//
//                                                break;
//
//                                        }
//                                    } else {//别人的帖子
//                                        switch (type) {
//
//                                            case 1:// 举报 评论
//                                                SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
//                                                selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
//                                                    @Override
//                                                    public void reportItemClick(Object object) {
//                                                        if (object instanceof ReportType) {
//
//                                                            ReportType reportTypeObj = (ReportType) object;
//                                                            getCommonPresenter().reportOperation(comment.getCommentId(), finalReportType
//                                                                    , currentBaseUserId
//                                                                    , 0
//                                                                    , reportTypeObj.getReportTypeId());
//                                                        }
//                                                    }
//                                                });
//                                                selectorReportContentDialog.show(getSupportFragmentManager());
//                                                break;
//
//                                            case 2://拉黑 发评论的人
//
//                                                BaseUser baseUser = comment.getFromBaseUser();
//                                                if (baseUser != null) {
//                                                    toBlacklistUserId = baseUser.getUserId();
//                                                    if (currentBaseUserId == toBlacklistUserId) {
//                                                        return;
//                                                    }
//                                                    getUserPresenter().pullBlackUser(currentBaseUserId, toBlacklistUserId);
//                                                }
//
//                                                break;
//
//                                        }
//                                    }
//
//
//                                }
//                            });
//                        }
//                    }
//
//                }
//                dialog.show(getSupportFragmentManager());
//
//            }
//
//            @Override
//            public void moreComment(long commentId) {
//                Intent intent = new Intent(ClassMateHelpDetailActivity.this, MoreCommentActivity.class);
//                intent.putExtra(Constants.Fields.MODULE_ID, helpInfo.getHelpId());
//                intent.putExtra(Constants.Fields.TYPE, 4);
//                intent.putExtra(Constants.Fields.COMMENT_PARENT_ID, commentId);
//                startActivity(intent);
//            }
//
//            @Override
//            public void ReplyListComment(HelpComment comment) {
//                replyComment = comment;
//                editText.setHint("回复：" + comment.getFromBaseUser().getNickName());
//                commentType = 2;
//                if (commentImageRecyclerAdapter.getList() != null) {
//                    commentImageRecyclerAdapter.getList().clear();
//                    commentImageRecyclerAdapter.notifyDataSetChanged();
//
//                    if (commentImageRecyclerAdapter.getItemCount() > 0) {
//                        commentImageRecyclerView.setVisibility(View.VISIBLE);
//                    } else {
//                        commentImageRecyclerView.setVisibility(View.GONE);
//                    }
//                }
//                videoPath = null;
//                if (sendView != null) {
//                    sendView.setVisibility(View.VISIBLE);
//                }
//
//                if (editText != null) {
//                    editText.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            KeyBoardUtils.openKeybord(editText, App.getInstance());
//                        }
//                    }, 500);
//
//                }
//            }
//
//            @Override
//            public void sortViewClick() {
//                if (sort == 1) {
//                    sort = 2;
//                } else if (sort == 2) {
//                    sort = 1;
//                }
//                classMateHelpDetailAdapter.setSort(sort);
//                requestCommentData();
//            }
//        });
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (isFinishing()) {
//            return;
//        }
//
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            Utils.showToastShortTime(e.getMessage());
//            return;
//        }
//        if (InterfaceUrl.URL_FINDHELPDETAIL.equals(apiName)) {
//
//            if (object instanceof FindHelpDetailResponse) {
//
//                FindHelpDetailResponse response = (FindHelpDetailResponse) object;
//
//                if (response.getCode() == 200) {
//                    helpInfo = response.getHelpInfo();
//
//                    if (helpInfo == null) {
//                        Utils.showToastShortTime(response.getMsg());
//                        finish();
//                        return;
//                    }
//
//                    initPostInfo(helpInfo);
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                    finish();
//                }
//            }
//
//        } else if (InterfaceUrl.URL_GETHELPCOMMENTLIST.equals(apiName)) {
//            if (object instanceof GetHelpCommentListResponse) {
//                GetHelpCommentListResponse getHelpCommentListResponse = (GetHelpCommentListResponse) object;
//                int code = getHelpCommentListResponse.getCode();
//                if (code == 200) {
//                    List commentList = getHelpCommentListResponse.getList();
//                    if (commentList != null) {
//                        if (pageNum == 1) {
//                            classMateHelpDetailAdapter.setList(commentList);
//                            classMateHelpDetailAdapter.setHelpItemView(helpInfo);
//                        } else {
//                            classMateHelpDetailAdapter.addList(commentList);
//                        }
//                    }
//                    if (getHelpCommentListResponse.getTotal() == 0 && classMateHelpDetailAdapter.getItemCount() == 1) {
//                        classMateHelpDetailAdapter.addEmptyItem(getHelpCommentListResponse.getBlankHint());
//                    }
//
//
//                    if (getHelpCommentListResponse.getTotal() <= classMateHelpDetailAdapter.getItemCount() - 1) {
//                        // 没有更多数据
//                        refreshLayout.setEnableLoadmore(false);
//                    } else {
//                        pageNum++;
//                        refreshLayout.setEnableLoadmore(true);
//                    }
//
//                } else {
//                    Utils.showToastShortTime(getHelpCommentListResponse.getMsg());
//                }
//
//            }
//        } else if (InterfaceUrl.URL_COMMENTUPLOADIMAGE.equals(apiName)) {
//            if (object instanceof HelpCommentResponse) {
////                HelpCommentResponse commentUploadImageResponse = (HelpCommentResponse) object;
//                HelpCommentResponse helpCommentResponse = (HelpCommentResponse) object;
//                Utils.showToastShortTime(helpCommentResponse.getMsg());
//                if (helpCommentResponse.getCode() == 200) {
//                    final List<Image> imgList = commentImageRecyclerAdapter.getList();
//                    if (imgList != null && imgList.size() > 0) {
//                        for (int i = 0; i < imgList.size(); i++) {
//                            Image image = imgList.get(i);
//                            getImagePresenter().uploadImages(helpCommentResponse.getComment().getCommentId(), 17, currentBaseUserId, DateUtils.getFormatDataString(new java.util.Date(), DateUtils.DATE_FORMAT_2), image.getImageUrl(), 0, i + 1, imgList.size());
//                        }
//                        commentImageRecyclerAdapter.getList().clear();
//                        commentImageRecyclerAdapter.notifyDataSetChanged();
//                        maxSelectNum = 9;
//                        if (commentImageRecyclerAdapter.getItemCount() > 0) {
//                            commentImageRecyclerView.setVisibility(View.VISIBLE);
//                        } else {
//                            commentImageRecyclerView.setVisibility(View.GONE);
//                        }
//                    }
////                    // 更新评论数
////                    PostInfo postInfo = (PostInfo) postCommentListAdapter.getList().get(0);
////                    postInfo.setCommentCount(postInfo.getCommentCount() + 1);
//
//                    // 更新评论
////                    Comment comment = (Comment) commentUploadImageResponse.getComment();
//
////                    List<Object> list = postCommentListAdapter.getList();
////                    if (list.get(list.size() - 1) instanceof String) {
////                        postCommentListAdapter.getList().remove(list.size() - 1);
////                    }
////
////                    postCommentListAdapter.addItem(comment, false);
////
////                    postCommentListAdapter.notifyDataSetChanged();
////
////                    recyclerView.smoothScrollToPosition(1);
////
////                    EventBus.getDefault().post(new UpdatePostListEvent(1, selectPostIndex, postInfo.getCommentCount(), 0, comeInType));
//                    pageNum = 1;
//                    requestCommentData();
//                }
//            }
//        } else if (InterfaceUrl.URL_HELPPRAISE.equals(apiName)) {
//            if (object instanceof HelpPraiseResponse) {
//                HelpPraiseResponse sayLovePraiseResponse = (HelpPraiseResponse) object;
//                if (sayLovePraiseResponse.getCode() == 200) {
//                    int isPraise = sayLovePraiseResponse.getIsPraise();
//                    EventBus.getDefault().post(new ClassMateHelpEvent(2, selectPostIndex, isPraise));
//                }
//                Utils.showToastShortTime(sayLovePraiseResponse.getMsg());
//            }
//
//
//        } else if (InterfaceUrl.URL_ATTENTIONOPERATION.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse baseResponse = (BaseResponse) object;
//                if (baseResponse.getCode() == 200) {
//                    helpInfo.getBaseUser().setIsAttention(1);
//                    classMateHelpDetailAdapter.notifyItemChanged(0);
//                }
//                Utils.showToastShortTime(baseResponse.getMsg());
//            }
//        } else if (InterfaceUrl.URL_DELETEHELP.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                if (response.getCode() == 200) {
//                    Utils.showToastShortTime(response.getMsg());
//
//                    // 删除帖子
//                    EventBus.getDefault().post(new ClassMateHelpEvent(5, selectPostIndex, 0));
//
//
//                    finish();
//
//                }
//            }
//        } else if (InterfaceUrl.URL_DELETEHELPCOMMENT.equals(apiName)) {
//
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                if (response.getCode() == 200) {
//                    Utils.showToastShortTime(response.getMsg());
//
//                    // 更新评论数
//                    HelpInfo helpInfo = (HelpInfo) classMateHelpDetailAdapter.getList().get(0);
//                    helpInfo.setCommentCount(helpInfo.getCommentCount() - 1);
////                    classMateHelpDetailAdapter.notifyItemChanged(0);
//
//
//                    if (commentPosition < 0) {
//                        return;
//                    }
//
//                    // 更新评论列表
//
//                    if (classMateHelpDetailAdapter == null) {
//                        return;
//                    }
//
//                    if (classMateHelpDetailAdapter.getList() == null) {
//                        return;
//                    }
//
//                    if (classMateHelpDetailAdapter.getList().size() <= commentPosition) {
//                        return;
//                    }
//
//                    classMateHelpDetailAdapter.getList().remove(commentPosition);
//
//                    commentPosition = -1;
//
//                    classMateHelpDetailAdapter.notifyDataSetChanged();
//
//                }
//            }
//
//
//        } else if (InterfaceUrl.URL_REPORTOPERATION.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                if (response.getCode() == 200) {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_PULLBLACKUSER.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//                if (response.getCode() == 200) {
//                    RongIM.getInstance().addToBlacklist(toBlacklistUserId + "", new RongIMClient.OperationCallback() {
//                        @Override
//                        public void onSuccess() {
//
//                            if (isFinishing()) {
//                                return;
//                            }
//
//                            toBlacklistUserId = 0;
//                            Logger.d("拉黑成功");
//
//                        }
//
//                        @Override
//                        public void onError(RongIMClient.ErrorCode errorCode) {
//
//                            if (isFinishing()) {
//                                return;
//                            }
//
//                            toBlacklistUserId = 0;
//                            Logger.d("失败原因：" + errorCode.getMessage());
//
//                        }
//                    });
//                }
//            }
//        }
//    }
//
//    public void initPostInfo(HelpInfo helpInfo) {
//
//        if (helpInfo == null) {
//            return;
//        }
//
//        if (helpInfo.getBaseUser() == null) {
//            return;
//        }
//
//        if (classMateHelpDetailAdapter != null) {
//            classMateHelpDetailAdapter.setHelpItemView(helpInfo);
//        }
//
//        recyclerView.smoothScrollBy(0, 0);
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventVideoCommentFinish(VideoCommentFinishEvent event) {
//        if (event == null) {
//            return;
//        }
//        if (event.getModule() == 4) {
//            pageNum = 1;
//            requestCommentData();
//            if (commentImageRecyclerAdapter.getList() != null) {
//                commentImageRecyclerAdapter.getList().clear();
//            }
//            setSendViewBg(editText.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
//            commentImageRecyclerAdapter.notifyDataSetChanged();
//            if (commentImageRecyclerAdapter.getItemCount() > 0) {
//                commentImageRecyclerView.setVisibility(View.VISIBLE);
//            } else {
//                commentImageRecyclerView.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    @Override
//    public void showLoading() {
//
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(ShareNumUpdateEvent event) {
//        if (event == null) {
//            return;
//        }
//        if (event.getType() == ShareUtil.CLASSMATEHELP) {
//            //本地更新帮帮忙分享次数
//            helpInfo.setShareNumber(helpInfo.getShareNumber() + 1);
//            classMateHelpDetailAdapter.setHelpItemView(helpInfo);
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (rootView != null) {
//            rootView = null;
//        }
//
//        if (classMateHelpDetailAdapter != null) {
//            classMateHelpDetailAdapter.onDestroy();
//        }
//
//        EventBus.getDefault().unregister(this);
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && data != null) {
//            switch (requestCode) {
//                case PictureConfig.CHOOSE_REQUEST:// 选择头像
//                    // 图片选择结果回调
//                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//                    // 例如 LocalMedia 里面返回三种path
//                    // 1.media.getPath(); 为原图path
//                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//
//                    if (selectMode == 3) {
//                        if (commentImageRecyclerAdapter.getList() != null) {
//                            commentImageRecyclerAdapter.getList().clear();
//                        }
//                    }
//                    ArrayList<Image> imageList = new ArrayList(1);
//                    for (LocalMedia info : selectList) {
//                        Image image = new Image();
//                        if (info.isCompressed()) {
//                            image.setImageUrl(info.getCompressPath());
//                        } else {
//                            image.setImageUrl(info.getPath());
//                        }
//                        imageList.add(image);
//                        maxSelectNum--;
//                    }
//
//                    selectList.clear();
//                    commentImageRecyclerAdapter.setList(imageList);
//                    commentImageRecyclerAdapter.notifyDataSetChanged();
//
//                    if (commentImageRecyclerAdapter.getItemCount() > 0) {
//                        commentImageRecyclerView.setVisibility(View.VISIBLE);
//                    } else {
//                        commentImageRecyclerView.setVisibility(View.GONE);
//                    }
//                    setSendViewBg(editText.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
//                    break;
//                case VIDEO_CODE:
//                case VIDEO_PICKER_CODE:
//                    if (VIDEO_PICKER_CODE == requestCode) {
//                        Uri selectedMediaUri = data.getData();
//                        videoPath = UriUtils.getFileAbsolutePath(App.getInstance(), selectedMediaUri);
//                    } else if (VIDEO_CODE == requestCode) {
//                        videoPath = data.getStringExtra(Constants.KEY.KEY_EXTRA);
//                    }
//
//                    Log.e("videoPath ==== ", videoPath);
//
//                    if (StringUtils.isEmpty(videoPath)) {
//                        Log.e("==文件路径是null== ", videoPath);
//                        return;
//                    }
//
//                    File file = new File(videoPath);
//                    Log.e("file.length() ==== ", file.length() + "");
//
//                    if (file.length() <= 0) {
//                        Utils.showToastShortTime("对不起，您选择的文件无效");
//                        return;
//                    }
//
//                    // 视频类型
//                    try {
//                        String suffix = videoPath.substring(videoPath.lastIndexOf(".") + 1, videoPath.length());
//                        Log.e("suffix ==== ", suffix);
//                        if (StringUtils.isEmpty(suffix)) {
//                            Utils.showToastShortTime("对不起，您选择的文件格式无效");
//                            return;
//                        }
//
//                        // 判断文件是否是视频文件
//                        boolean isVideo = false;
//                        for (int i = 0; i < videoSuffixs.length; i++) {
//                            if (videoSuffixs[i].equals(suffix)) {
//                                isVideo = true;
//                                break;
//                            }
//                        }
//
//
//                        if (!isVideo) {
//                            Utils.showToastShortTime("视频文件格式(" + suffix + ")不正确");
//                            return;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Utils.showToastShortTime("对不起，系统解析文件错误");
//                        return;
//                    }
//
//                    if (file.length() / (1024 * 1024) > 50) {
//                        Utils.showToastShortTime("文件过大，请不要超过50M");
//                        return;
//                    }
//
//                    if (!StringUtils.isEmpty(videoPath)) {
//                        // 视频缩略图
//                        RequestOptions options = new RequestOptions().centerCrop();
//                        Glide.with(App.getInstance()).asBitmap().load(videoPath).apply(options).into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                                if (resource != null) {
//                                    if (commentImageRecyclerAdapter.getList() != null) {
//                                        commentImageRecyclerAdapter.getList().clear();
//                                    }
//                                    ArrayList<Image> videoImageList = new ArrayList(1);
//                                    Image image = new Image();
//                                    image.setImageUrl(videoPath);
//                                    videoImageList.add(image);
//                                    commentImageRecyclerAdapter.setList(videoImageList);
//                                    commentImageRecyclerAdapter.notifyDataSetChanged();
//
//                                    if (commentImageRecyclerAdapter.getItemCount() > 0) {
//                                        commentImageRecyclerView.setVisibility(View.VISIBLE);
//                                    } else {
//                                        commentImageRecyclerView.setVisibility(View.GONE);
//                                    }
//                                    setSendViewBg(editText.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
//                                    int width = resource.getWidth();
//                                    int height = resource.getHeight();
//
//                                    Logger.d("width =原图1== " + width + " ; height =原图1== " + height);
//
//                                    float ww = BitmapUtils.getBitmapWidth(App.getInstance());
//                                    float hh = BitmapUtils.getBitmapHeight(App.getInstance());
//                                    Logger.d("ww ==指定大小== " + ww + " ; hh ==指定大小== " + hh);
//
//                                    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//                                    double scale = 1;//scale=1表示不缩放
//                                    if (width > height && width > ww) {//如果宽度大的话根据宽度固定大小缩放
//                                        scale = width / ww;
//                                    } else if (width < height && height > hh) {//如果高度高的话根据宽度固定大小缩放
//                                        scale = height / hh;
//                                    }
//
//                                    Logger.d("scale ==缩放比== " + scale);
//
//                                    if (scale < 1) {
//                                        scale = 1;
//                                    }
//
//                                    width = (int) (width / scale);
//                                    height = (int) (height / scale);
//
//                                    Logger.d("width =scale=最终= " + width + " ; height =scale=最终= " + height);
//
//                                    imgWidth = width;
//                                    imgHeight = height;
//
//
//                                }
//                            }
//                        });
//
//                    } else {
//                        Utils.showToastShortTime("没有选择视频文件!");
//                    }
//                    break;
//                default:
//            }
//        }
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.img_image:
//                if (selectMode != 1) {
//                    selectMode = 1;
//                    maxSelectNum = 9;
//                    if (commentImageRecyclerAdapter.getList() != null) {
//                        commentImageRecyclerAdapter.getList().clear();
//                    }
//                }
//                Utils.hideSystemSoftInputKeyboard(editText);
//                if (maxSelectNum == 0) {
//                    Utils.showToastShortTime("您最多可选9张图片");
//                } else {
//                    selectPic();
//                }
//                break;
//            case R.id.img_gif:
//                selectMode = 3;
//                maxSelectNum = 1;
//                Utils.hideSystemSoftInputKeyboard(editText);
//                selectPic();
//                break;
//            case R.id.img_video:
//                selectMode = 2;
//                Utils.hideSystemSoftInputKeyboard(editText);
//                ActiveEditSelectDialog dialog = new ActiveEditSelectDialog();
//                dialog.setItemText("拍摄", "从手机相册选择");
//                dialog.setItemClick(new ActiveEditSelectDialog.ItemClick() {
//                    @Override
//                    public void itemClick(int position) {
//                        switch (position) {
//                            case 1:
//                                //拍摄
//                                Intent intent = new Intent(ClassMateHelpDetailActivity.this, PublishVideoActivity.class);
//                                intent.putExtra(Constants.Fields.TYPE, 4);
//                                intent.putExtra(Constants.Fields.FLAG, 4);
//                                startActivityForResult(intent, VIDEO_CODE);
//                                break;
//                            case 2:
//                                //从手机相册选择
//                                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                                pickIntent.setType("video/*");
//                                pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                                startActivityForResult(pickIntent, VIDEO_PICKER_CODE);
//                                break;
//                        }
//                    }
//                });
//                dialog.show(getSupportFragmentManager());
//                break;
//            default:
//        }
//
//
//    }
//
//    private void selectPic() {
//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
//                .maxSelectNum(maxSelectNum)// 最大图片选择数量 int
//                .minSelectNum(1)// 最小选择数量 int
//                .imageSpanCount(4)// 每行显示个数 int
//                .selectionMode(selectMode == 3 ? PictureConfig.SINGLE : PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                .previewImage(true)// 是否可预览图片 true or false
//                .isCamera(true)// 是否显示拍照按钮 true or false
//                .enableCrop(false)// 是否裁剪 true or false
//                .compress(selectMode != 3)// 是否压缩 true or false
//                .glideOverride(Utils.dp2px(110), Utils.dp2px(110))// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .isGif(selectMode == 3)// 是否显示gif图片 true or false
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
//                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                .openClickSound(true)// 是否开启点击声音 true or false
//                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH(Utils.dp2px(110), Utils.dp2px(110))// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
//                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .forResult(PictureConfig.CHOOSE_REQUEST);
//    }
//
//    public CommonPresenter getCommonPresenter() {
//        if (commonPresenter == null) {
//            commonPresenter = new CommonPresenter(this);
//        }
//        return commonPresenter;
//    }
//
//    public UserPresenter getUserPresenter() {
//        if (userPresenter == null) {
//            userPresenter = new UserPresenter(this);
//        }
//        return userPresenter;
//    }
//
//
//}
