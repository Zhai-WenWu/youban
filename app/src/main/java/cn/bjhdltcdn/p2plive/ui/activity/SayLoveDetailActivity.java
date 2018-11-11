//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.Switch;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
//import cn.bjhdltcdn.p2plive.event.ShareNumUpdateEvent;
//import cn.bjhdltcdn.p2plive.event.UpdateSayloveListEvent;
//import cn.bjhdltcdn.p2plive.event.VideoCommentFinishEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.CommentPraiseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindSayLoveDetailResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetCommentListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetReportTypeResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SayLoveCommentResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SayLovePraiseResponse;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.ConfessionComment;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OriginalInfo;
//import cn.bjhdltcdn.p2plive.model.ReportType;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.service.SingleTaskShortVideoUploaderIntentService;
//import cn.bjhdltcdn.p2plive.ui.adapter.CommentImageRecyclerAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.PostDetailImageRecycleAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.SayloveCommentListAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveEditSelectDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.PostDetailCommentDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.BitmapUtils;
//import cn.bjhdltcdn.p2plive.utils.DateUtils;
//import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.UriUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//import io.rong.imlib.RongIMClient;
//
///**
// * Created by Hu_PC on 2017/11/9.
// * 表白详情页
// * 注意：使用到视频播发器必须在配置文件加 android:hardwareAccelerated="true" 属性，否则会黑屏（播放不出来画面）
// */
//
//public class SayLoveDetailActivity extends BaseActivity implements BaseView, View.OnClickListener {
//    private GetCommentListPresenter getCommentListPresenter;
//    private PostCommentListPresenter postCommentListPresenter;
//    private CommonPresenter commonPresenter;
//    private UserPresenter userPresenter;
//    private ImagePresenter imagePresenter;
//
//    private View hearViewContainer;
//    private TwinklingRefreshLayout refreshLayout;
//    private LoadingView loadingView;
//    private int pageSize = 20, pageNum = 1;
//    private ListView listView;
//    private SayloveCommentListAdapter sayloveCommentListAdapter;
//    private SayLoveInfo saylove;
//    private BaseUser sayloveBaseUser;
//    private TextView praise_tv, comment_tv, share_tv;
//    private ImageView more_imgv;
//    private View sendView;// 输入框
//
//    private EditText sendEdit;
//    private TextView sendTextView;
//    private ImageView img_image, img_gif, img_video;
//    /**
//     * 匿名按钮
//     */
//    private Switch anonymousSwitch;
//
//    /**
//     * 标记是否开启匿名
//     */
//    private boolean anonymousSwitchIsChecked;
//    /**
//     * 评论图片列表
//     */
//    private RecyclerView commentImageRecyclerView;
//    private CommentImageRecyclerAdapter commentImageRecyclerAdapter;
//    /**
//     * 选择类型
//     * 1图片 2视频 3gif
//     */
//    private int selectMode;
//    /**
//     * 评论图片最大选择数量
//     */
//    int maxSelectNum = 9;
//    //本地选择视频
//    private final int VIDEO_PICKER_CODE = 2;
//    //打开录制视频页面
//    private final int VIDEO_CODE = 1;
//    private String videoSuffixs[] = {"mp4", "avi", "mov", "mkv", "flv", "f4v", "rmvb"};
//    //视频路径
//    private String videoPath;
//    //视频图片的宽高
//    private int imgWidth, imgHeight;
//
//    private int commentType = 1;//1:评论2：回复
//    private RequestOptions options, userOptions, gifOptions;
//    private long commentId;//  评论Id  回复用的
//    private long commentParentId;//  父评论Id  回复用的
//    private int commentPosition;
//    private long toUserId;//被回复的人的id
//    private int commentItemType;//评论或者回复的标志 //1:评论2：回复
//    private int sayloveListPosition;
//    private long userId;
//    private int reportType;//6-->表白,7-->表白评论,8-->表白回复
//    private long pullBackUserId;
//    //排序(1按热度,2按时间)
//    private int sort = 1;
//    //头布局组件
//    private RecyclerView recycler_image;
//    private PostDetailImageRecycleAdapter postDetailImageRecycleAdapter;
//    private ImageView userimg, alumnus_tv, giftImgView;
//    private TextView nickName_tv, age_tv, time_tv, location_tv, city_tv, content_tv, attention_tv;
//    private int isPraise;
//    private long sayloveId;
//    private TextView emptyTextView;
//    private String emptyStr;
//    private View emptyView;
//    private int screenWidth;
//    private PostCommentListPresenter postPresenter;
//
//    private PostCommentListPresenter getPostPresenter() {
//
//        if (postPresenter == null) {
//            postPresenter = new PostCommentListPresenter(this);
//        }
//        return postPresenter;
//    }
//
//    public ImagePresenter getImagePresenter() {
//        if (imagePresenter == null) {
//            imagePresenter = new ImagePresenter(this);
//        }
//        return imagePresenter;
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_saylove_detail);
//        EventBus.getDefault().register(this);
//        options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg);
//        userOptions = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.error_user_icon)
//                .error(R.mipmap.error_user_icon);
//        gifOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        screenWidth = PlatformInfoUtils.getWidthOrHeight(App.getInstance())[0];
//        userPresenter = new UserPresenter(this);
//        commonPresenter = new CommonPresenter(this);
//        getCommentListPresenter = new GetCommentListPresenter(this);
//        postCommentListPresenter = new PostCommentListPresenter(this);
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        initView();
//        setTitle();
//        getCommentListPresenter.findSayLoveDetail(userId, sayloveId);
//        requestCommentData();
//    }
//
//    private void initView() {
//        Intent intent = getIntent();
//        sayloveId = intent.getLongExtra(Constants.KEY.KEY_SAYLOVE_ID, 0);
//        sayloveListPosition = intent.getIntExtra("position", 0);
//        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
//        listView = (ListView) findViewById(R.id.comment_list_view);
//        sendView = findViewById(R.id.send_comment_view);
//        sendEdit = findViewById(R.id.reply_edit_input);
//        anonymousSwitch = sendView.findViewById(R.id.anonymous_view);
//        anonymousSwitch.setVisibility(View.VISIBLE);
//        anonymousSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                anonymousSwitchIsChecked = isChecked;
//            }
//        });
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
//        img_image.setVisibility(View.VISIBLE);
//        img_gif.setVisibility(View.VISIBLE);
//        img_video.setVisibility(View.VISIBLE);
//        sendEdit.addTextChangedListener(new TextWatcher() {
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
//        sendTextView = findViewById(R.id.send_view);
//        sendTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //评论或回复
//                String editStr = sendEdit.getText().toString().trim();
//                int anonymousType = 0;
//                // 评论用户匿名
//                if (commentType == 1 && anonymousSwitchIsChecked) {
//                    anonymousType = 1;
//                    // 回复用户匿名
//                } else if (commentType == 2 && anonymousSwitchIsChecked) {
//                    anonymousType = 2;
//                }
//                if (TextUtils.isEmpty(videoPath)) {//图文
//                    boolean imageOrVideoNotNull = commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0;
//                    if (!TextUtils.isEmpty(editStr) || imageOrVideoNotNull) {
//                        if (commentType == 1) {
//                            commentId = 0;
//                            commentParentId = 0;
//                        }
//                        getPostPresenter().commentUploadImage(userId, sayloveId, 2, editStr, commentType, anonymousType, toUserId, userId,
//                                commentId, commentParentId, 1, null, null, DateUtils.getTime(new Date()), null);
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
//                    map.put(Constants.Fields.CONTENT_STR, editStr);
//                    map.put(Constants.Fields.IS_ANONYMOUS, anonymousType);
//                    map.put(Constants.Fields.LAUNCH_ID, sayloveId);
//                    map.put(Constants.Fields.TO_USER_ID, toUserId);
//                    map.put(Constants.Fields.ENTER_TYPE_MODE, 6);
//                    map.put(Constants.Fields.TYPE, 2);
//                    SingleTaskShortVideoUploaderIntentService.startShortVideoUploader(SayLoveDetailActivity.this, map);
//                    Utils.showToastShortTime("视频上传中，上传完成后将自动发布。");
//
//                }
//                videoPath = null;
//                sendEdit.setText("");
//                sendEdit.setHint("说点什么吧...");
//                sendView.setVisibility(View.GONE);
//                commentType = 1;
//                //隐藏键盘
//                InputMethodManager imm = (InputMethodManager) SayLoveDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
//
//            }
//        });
//        sayloveCommentListAdapter = new SayloveCommentListAdapter(this);
//        sayloveCommentListAdapter.setUserPresenter(userPresenter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i > 0) {
//                    commentPosition = i - 1;
//                    ConfessionComment confessionComment = sayloveCommentListAdapter.getItem(commentPosition);
//                    //回复  自己不能回复自己的评论
//                    if (confessionComment.getFromBaseUser().getUserId() != userId) {
//                        toUserId = confessionComment.getFromBaseUser().getUserId();
//                        img_image.setVisibility(View.GONE);
//                        img_gif.setVisibility(View.GONE);
//                        img_video.setVisibility(View.GONE);
//                        String fromBaseUserNickName;
//                        if (confessionComment.getAnonymousType() == 1 || confessionComment.getAnonymousType() == 3) {
//                            fromBaseUserNickName = "匿名";
//                        } else {
//                            fromBaseUserNickName = confessionComment.getFromBaseUser().getNickName();
//                        }
//                        sendEdit.setHint("回复：" + fromBaseUserNickName);
//                        commentType = 2;
//                        commentId = confessionComment.getCommentId();
//                        commentParentId = commentId;
//                        getEditFocuse();
//                        if (commentImageRecyclerAdapter.getList() != null) {
//                            commentImageRecyclerAdapter.getList().clear();
//                            commentImageRecyclerAdapter.notifyDataSetChanged();
//
//                            if (commentImageRecyclerAdapter.getItemCount() > 0) {
//                                commentImageRecyclerView.setVisibility(View.VISIBLE);
//                            } else {
//                                commentImageRecyclerView.setVisibility(View.GONE);
//                            }
//                        }
//                        videoPath = null;
//                    }
//                }
//            }
//        });
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    Utils.hideSystemSoftInputKeyboard(sendEdit);
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
//        sayloveCommentListAdapter.setItemOperationClick(new SayloveCommentListAdapter.itemOperationClick() {
//
//            @Override
//            public void ReplyListComment(ConfessionComment comment) {
//                toUserId = comment.getFromBaseUser().getUserId();
//                img_image.setVisibility(View.GONE);
//                img_gif.setVisibility(View.GONE);
//                img_video.setVisibility(View.GONE);
//
//                String fromBaseUserNickName;
//                if (comment.getAnonymousType() == 1 || comment.getAnonymousType() == 3) {
//                    fromBaseUserNickName = "匿名";
//                } else {
//                    fromBaseUserNickName = comment.getFromBaseUser().getNickName();
//                }
//                sendEdit.setHint("回复：" + fromBaseUserNickName);
//                commentType = 2;
//                commentId = comment.getCommentId();
//                commentParentId = comment.getCommentParentId();
//                getEditFocuse();
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
//            }
//
//            @Override
//            public void moreClick(final int itemPosition, int type) {
//                commentPosition = itemPosition;
//                commentItemType = type;
//                String[] str;
//                if (sayloveCommentListAdapter.getItem(itemPosition).getFromBaseUser().getUserId() == userId) {
//                    //我自己
//                    str = new String[]{"删除"};
//                } else if (sayloveCommentListAdapter.getItem(itemPosition).getAnonymousType() == 1) {
//                    //他人
//                    str = new String[]{"举报"};
//                } else {
//                    str = new String[]{"拉黑", "举报"};
//
//                }
//                PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
//                postDetailCommentDialog.setItemStr(str);
//                postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
//                    @Override
//                    public void itemClick(int position, String content) {
//                        if (content.equals("删除")) {
//                            //删除评论
//                            DelectTipDialog dialog = new DelectTipDialog();
//                            if (commentItemType == 1) {
//                                dialog.setTitleStr("确定删除评论内容？");
//                            } else {
//                                dialog.setTitleStr("确定删除回复内容？");
//                            }
//                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
//                                @Override
//                                public void itemClick() {
//                                    getCommentListPresenter.deleteSayLoveComment(sayloveCommentListAdapter.getItem(itemPosition).getCommentId());
//                                }
//                            });
//                            dialog.show(getSupportFragmentManager());
//                        } else if (content.equals("拉黑")) {
//                            //拉黑
//                            pullBackUserId = sayloveCommentListAdapter.getItem(itemPosition).getFromBaseUser().getUserId();
//                            userPresenter.pullBlackUser(userId, pullBackUserId);
//                        } else if (content.equals("举报")) {
//                            //举报
//                            if (commentItemType == 1) {
//                                //评论
//                                reportType = 7;
//                            } else {
//                                //回复
//                                reportType = 8;
//                            }
//                            commentId = sayloveCommentListAdapter.getItem(itemPosition).getCommentParentId();
//                            pullBackUserId = sayloveCommentListAdapter.getItem(itemPosition).getFromBaseUser().getUserId();
//                            commonPresenter.getReportType(1);
//                        }
//                    }
//                });
//                postDetailCommentDialog.show(getSupportFragmentManager());
//            }
//
//            @Override
//            public void onPraise(long commentParentId, int type, int position) {
//                //评论点赞
//                postCommentListPresenter.commentPraise(commentParentId, type, userId, 2);
//            }
//
//            @Override
//            public void moreComment(long commentId) {
//                Intent intent = new Intent(SayLoveDetailActivity.this, MoreCommentActivity.class);
//                intent.putExtra(Constants.Fields.MODULE_ID, sayloveId);
//                intent.putExtra(Constants.Fields.TYPE, 2);
//                intent.putExtra(Constants.Fields.COMMENT_PARENT_ID, commentId);
//                startActivity(intent);
//            }
//
//            @Override
//            public void sortViewClick() {
//                if (sort == 1) {
//                    sort = 2;
//                } else if (sort == 2) {
//                    sort = 1;
//                }
//                sayloveCommentListAdapter.setSort(sort);
//                requestCommentData();
//            }
//        });
//
//        //头布局
//        hearViewContainer = getLayoutInflater().inflate(R.layout.post_detail_header_layout, null);
//        hearViewContainer.findViewById(R.id.user_level_text).setVisibility(View.GONE);
//        emptyView = hearViewContainer.findViewById(R.id.empty_view);
//        emptyTextView = hearViewContainer.findViewById(R.id.empty_textView);
//        userimg = hearViewContainer.findViewById(R.id.user_img);
//        userimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sayloveBaseUser.getUserId() != userId) {
//                    if (saylove.getIsAnonymous() == 2) {
//                        //跳到用户详情
//                        startActivity(new Intent(SayLoveDetailActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, sayloveBaseUser));
//                    } else {
//                        userPresenter.getAnonymityUser(userId, sayloveBaseUser.getUserId());
//                    }
//                }
//            }
//        });
//        nickName_tv = hearViewContainer.findViewById(R.id.user_nickname_text);
//        age_tv = hearViewContainer.findViewById(R.id.user_age_text);
//        alumnus_tv = hearViewContainer.findViewById(R.id.user_school_mate);
//        time_tv = hearViewContainer.findViewById(R.id.time_text);
//        attention_tv = hearViewContainer.findViewById(R.id.attention_tv);
//        location_tv = hearViewContainer.findViewById(R.id.location_text);
//        city_tv = hearViewContainer.findViewById(R.id.city_text);
//        content_tv = hearViewContainer.findViewById(R.id.content_text);
//        praise_tv = hearViewContainer.findViewById(R.id.praise_text);
//        comment_tv = hearViewContainer.findViewById(R.id.comment_text);
//        more_imgv = hearViewContainer.findViewById(R.id.more_img);
//        share_tv = hearViewContainer.findViewById(R.id.share_text);
//        share_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //分享弹窗
//                String imgUrl = "";
//                if (saylove.getConfessionType() == 2) {
//                    imgUrl = saylove.getVideoImageUrl();
//                } else {
//                    if (saylove.getImageList() != null && saylove.getImageList().size() > 0) {
//                        imgUrl = saylove.getImageList().get(0).getThumbnailUrl();
//                    }
//                }
//                ShareUtil.getInstance().showShare(SayLoveDetailActivity.this, ShareUtil.SAYLOVE, saylove.getSayLoveId(), saylove, "", "", "", imgUrl, true);
//            }
//        });
//
//        praise_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isPraise == 0) {
//                    //点赞
//                    getCommentListPresenter.sayLovePraise(saylove.getSayLoveId(), isPraise + 1, userId);
//                } else if (isPraise == 1) {
//                    //取消点赞
//                    getCommentListPresenter.sayLovePraise(saylove.getSayLoveId(), isPraise + 1, userId);
//                } else if (isPraise == 2) {
//                    //点赞
//                    getCommentListPresenter.sayLovePraise(saylove.getSayLoveId(), isPraise - 1, userId);
//                }
//            }
//        });
//
//        comment_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //评论
//                img_image.setVisibility(View.VISIBLE);
//                img_gif.setVisibility(View.VISIBLE);
//                img_video.setVisibility(View.VISIBLE);
//                commentType = 1;
//                toUserId = saylove.getBaseUser().getUserId();
//                sendEdit.setHint("说点什么吧...");
//                getEditFocuse();
//            }
//        });
//        more_imgv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] str;
//                if (sayloveBaseUser.getUserId() == userId) {
//                    //我自己
//                    str = new String[]{"删除", "分享"};
//                } else {
//                    //他人 匿名没有拉黑
//                    if (saylove.getIsAnonymous() != 1) {
//                        str = new String[]{"分享", "拉黑", "举报"};
//                    } else {
//                        str = new String[]{"分享", "举报"};
//                    }
//                }
//                PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
//                postDetailCommentDialog.setItemStr(str);
//                postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
//                    @Override
//                    public void itemClick(int position, String content) {
//                        if (content.equals("删除")) {
//                            //删除
//                            DelectTipDialog dialog = new DelectTipDialog();
//                            dialog.setTitleStr("确定删除表白？");
//                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
//                                @Override
//                                public void itemClick() {
//                                    getCommentListPresenter.deleteSayLove(userId, saylove.getSayLoveId());
//                                }
//                            });
//                            dialog.show(getSupportFragmentManager());
//                        } else if (content.equals("分享")) {
//                            //分享表白
//                            String imgUrl = "";
//                            if (saylove.getConfessionType() == 2) {
//                                imgUrl = saylove.getVideoImageUrl();
//                            } else {
//                                if (saylove.getImageList() != null && saylove.getImageList().size() > 0) {
//                                    imgUrl = saylove.getImageList().get(0).getThumbnailUrl();
//                                }
//                            }
//                            ShareUtil.getInstance().showShare(SayLoveDetailActivity.this, ShareUtil.SAYLOVE, saylove.getSayLoveId(), saylove, "", "", "", imgUrl, true);
//                        } else if (content.equals("举报")) {
//                            //举报
//                            reportType = 6;
//                            pullBackUserId = sayloveBaseUser.getUserId();
//                            commonPresenter.getReportType(1);
//                        } else if (content.equals("拉黑")) {
//                            //拉黑
//                            pullBackUserId = sayloveBaseUser.getUserId();
//                            userPresenter.pullBlackUser(userId, pullBackUserId);
//                        }
//                    }
//                });
//                postDetailCommentDialog.show(getSupportFragmentManager());
//            }
//        });
//        recycler_image = (RecyclerView) hearViewContainer.findViewById(R.id.recycler_image);
//        recycler_image.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
//        linearLayoutManager.setAutoMeasureEnabled(true);
//        recycler_image.setLayoutManager(linearLayoutManager);
//        recycler_image.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(10)));
//        postDetailImageRecycleAdapter = new PostDetailImageRecycleAdapter(this);
//        recycler_image.setAdapter(postDetailImageRecycleAdapter);
//        postDetailImageRecycleAdapter.setImageViewPageDialogClick(new PostDetailImageRecycleAdapter.ImageViewPageDialogClick() {
//            @Override
//            public void onClick(List<Image> mList, int currentItem) {
//                ImageViewPageDialog.newInstance(mList, currentItem).show(getSupportFragmentManager());
//            }
//        });
//        giftImgView = (ImageView) hearViewContainer.findViewById(R.id.gift_img);
//        listView.addHeaderView(hearViewContainer);
//        listView.setAdapter(sayloveCommentListAdapter);
//
//
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        loadingView = new LoadingView(this);
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(listView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//        refreshLayout.setEnableLoadmore(false);
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNum = 1;
//                refreshLayout.finishRefreshing();
//                requestCommentData();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                if (!loadingView.isOnLoadFinish()) {
//                    pageNum++;
//                    requestCommentData();
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 设置评论发送按钮颜色
//     *
//     * @param s
//     * @param imageOrVideoNotNull
//     */
//    private void setSendViewBg(CharSequence s, boolean imageOrVideoNotNull) {
//        if (!TextUtils.isEmpty(s) || imageOrVideoNotNull) {
//            sendTextView.setEnabled(true);
//            sendTextView.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
//            sendTextView.setTextColor(getResources().getColor(R.color.color_333333));
//        } else {
//            sendTextView.setEnabled(false);
//            sendTextView.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_fff69f));
//            sendTextView.setTextColor(getResources().getColor(R.color.color_999999));
//        }
//    }
//
//    private void setTitle() {
//        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_saylove_detail);
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                Utils.hideSystemSoftInputKeyboard(sendEdit);
//                finish();
//            }
//        });
//    }
//
//    private void getEditFocuse() {
//        sendView.setVisibility(View.VISIBLE);
//        sendEdit.setFocusable(true);
//        sendEdit.setFocusableInTouchMode(true);
//        sendEdit.requestFocus();
//        sendEdit.findFocus();
//        setSendViewBg(sendEdit.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
//        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(sendEdit, InputMethodManager.SHOW_FORCED);// 显示输入法
//    }
//
//    private void initSayLoveInfo(final SayLoveInfo sayLoveinfo) {
//        saylove = sayLoveinfo;
//        sayloveBaseUser = saylove.getBaseUser();
//        isPraise = sayLoveinfo.getIsPraise();
//        Glide.with(App.getInstance()).load(sayloveBaseUser.getUserIcon()).apply(userOptions).into(userimg);
//        nickName_tv.setText(sayloveBaseUser.getNickName());
//        TextView schoolNameTextView = hearViewContainer.findViewById(R.id.school_name_text_view);
//        age_tv.setText(sayloveBaseUser.getAge() + "岁");
//        final int isAnonymous = sayLoveinfo.getIsAnonymous();
//        if (sayloveBaseUser.getIsSchoolmate() == 2 && sayloveBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//            //校友 并且不是自己
//            alumnus_tv.setVisibility(View.VISIBLE);
//        } else {
//            alumnus_tv.setVisibility(View.GONE);
//        }
//        int sex = sayloveBaseUser.getSex();
//        if (sex == 1) {
//            //男性
//            age_tv.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//        } else if (sex == 2) {
//            //女性
//            age_tv.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//        }
//        if (sayLoveinfo.getIsAnonymous() != 1) {
//            age_tv.setVisibility(View.VISIBLE);
//            age_tv.setPadding(0, 0, Utils.dp2px(10), 0);
//        } else {
//            age_tv.setVisibility(View.VISIBLE);
//            age_tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//            age_tv.setText("");
//            age_tv.setPadding(0, 0, 0, 0);
//        }
//        nickName_tv.setVisibility(View.VISIBLE);
//        //跟拍人以及内容
//        String content = saylove.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            content_tv.setText(content);
//            content_tv.setVisibility(View.VISIBLE);
//        } else {
//            content_tv.setVisibility(View.GONE);
//        }
//        //跟拍对象
//        final OriginalInfo originalInfo = saylove.getOriginalInfo();
//        //标签展示
//        long labelId = saylove.getLabelId();
//        String labelName = saylove.getLabelName();
//        if (saylove.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
//            //跟拍加参赛
//            content_tv.setVisibility(View.VISIBLE);
//            content_tv.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, saylove.getScondlabelList(), content));
//        } else {
//            //只有跟拍
//            if (saylove.getIsOriginal() == 1 && originalInfo != null) {
//                content_tv.setVisibility(View.VISIBLE);
//                content_tv.setText(Utils.getFollowContent(originalInfo, content));
//            }
//            //只有参赛
//            if (!StringUtils.isEmpty(labelName)) {
//                content_tv.setVisibility(View.VISIBLE);
//                content_tv.setText(Utils.getMatchContent(labelId, labelName, saylove.getScondlabelList(), content));
//            }
//        }
//        content_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (saylove.getIsOriginal() == 1 && originalInfo != null) {
//                    //跳转到视频广场界面
//                    Intent intent = new Intent(SayLoveDetailActivity.this, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, saylove.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID, originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_SAYLOVE);
//                    startActivity(intent);
//                }
//            }
//        });
//        content_tv.setPadding(0, Utils.dp2px(10), 0, 0);
//
//        if (saylove.getConfessionType() == 2) {
//            //视频
//            recycler_image.setVisibility(View.GONE);
//            giftImgView.setVisibility(View.GONE);
////            hearViewContainer.findViewById(R.id.video_frame).setVisibility(View.VISIBLE);
////            VideoPlayFragment mFragment = new VideoPlayFragment();
////            mFragment.setData(saylove.getVideoUrl(), sayLoveinfo.getVideoImageUrl(), 1);
////            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.video_frame);
//            ImageView imgFirst = hearViewContainer.findViewById(R.id.img_first);
//            ImageView videoPlayImg = hearViewContainer.findViewById(R.id.video_play_img);
//            RelativeLayout linear_image_two = hearViewContainer.findViewById(R.id.linear_image_two);
//            Glide.with(App.getInstance()).load(sayLoveinfo.getVideoImageUrl()).apply(options).into(imgFirst);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgFirst.getLayoutParams();
//            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//            imgFirst.setLayoutParams(layoutParams);
//            videoPlayImg.setVisibility(View.VISIBLE);
//            imgFirst.setVisibility(View.VISIBLE);
//            linear_image_two.setVisibility(View.VISIBLE);
//            videoPlayImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //跳转到视频播放界面
//                    Intent intent = new Intent(SayLoveDetailActivity.this, PKVideoPlayActivity.class);
//                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, saylove.getSayLoveId());
//                    intent.putExtra(Constants.Fields.COME_IN_TYPE, 1);
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_SAYLOVE);
//                    startActivity(intent);
//                }
//            });
//        } else {
//            if (saylove.getImageList().size() == 1 && saylove.getImageList().get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
//                //gif
//                giftImgView.setVisibility(View.VISIBLE);
//                recycler_image.setVisibility(View.GONE);
//
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) giftImgView.getLayoutParams();
//                layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//                layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//                giftImgView.setLayoutParams(layoutParams);
//
//                Glide.with(App.getInstance()).asGif().load(saylove.getImageList().get(0).getImageUrl()).apply(gifOptions).into(giftImgView);
//
//                giftImgView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        ImageViewPageDialog.newInstance(saylove.getImageList(), 0).show(getSupportFragmentManager());
//                    }
//                });
//
//
//            } else {
//                //图文
//                giftImgView.setVisibility(View.GONE);
//                recycler_image.setVisibility(View.VISIBLE);
//                postDetailImageRecycleAdapter.setList(saylove.getImageList());
//                postDetailImageRecycleAdapter.notifyDataSetChanged();
//            }
//        }
//        time_tv.setText(saylove.getAddTime());
//        if (sayloveBaseUser.getIsAttention() == 0 && sayloveBaseUser.getUserId() != userId) {
//            //未关注 并且不是自己
//            if (isAnonymous == 2) {
//                attention_tv.setVisibility(View.VISIBLE);
//            } else {
//                attention_tv.setVisibility(View.GONE);
//            }
//        } else {
//            attention_tv.setVisibility(View.GONE);
//        }
//        attention_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //关注某人
//                userPresenter.attentionOperation(1, userId, sayloveBaseUser.getUserId());
//            }
//        });
//        String diatance = sayloveBaseUser.getDistance();
//        if ((!StringUtils.isEmpty(diatance))) {
//            location_tv.setText(diatance);
//            location_tv.setVisibility(View.VISIBLE);
//        } else {
//            location_tv.setVisibility(View.GONE);
//        }
//        if ((!StringUtils.isEmpty(sayloveBaseUser.getCity()))) {
//            city_tv.setText(sayloveBaseUser.getCity());
//            city_tv.setVisibility(View.VISIBLE);
//        } else {
//            city_tv.setVisibility(View.GONE);
//        }
//        if (sayloveBaseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//            //自己不展示位置
//            location_tv.setVisibility(View.GONE);
//            city_tv.setVisibility(View.GONE);
//        }
//        if (!StringUtils.isEmpty(sayloveBaseUser.getSchoolName())) {
//            //学校名称
//            schoolNameTextView.setVisibility(View.VISIBLE);
//            schoolNameTextView.setText(sayloveBaseUser.getSchoolName());
//            if (city_tv.getVisibility() == View.VISIBLE) {
//                schoolNameTextView.setPadding(Utils.dp2px(5), 0, 0, 0);
//            } else {
//                schoolNameTextView.setPadding(Utils.dp2px(12), 0, 0, 0);
//            }
//        } else {
//            schoolNameTextView.setVisibility(View.GONE);
//        }
//
//        if (isPraise == 0) {
//            //未点赞
//            praise_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        } else {
//            //已点赞
//            praise_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        }
//        isPraise = saylove.getIsPraise();
//        //点赞个数
//        int praiseCount = saylove.getPraiseCount();
//        String praiseCountStr = saylove.getPraiseCountStr();
//        if (praiseCount > 9999) {
//            praise_tv.setText(praiseCountStr);
//        } else {
//            praise_tv.setText(praiseCount + "");
//        }
//        //评论个数
//        int commentCount = saylove.getCommentCount();
//        String commentCountStr = saylove.getCommentCountStr();
//        if (commentCount > 9999) {
//            comment_tv.setText(commentCountStr);
//        } else {
//            comment_tv.setText(commentCount + "");
//        }
//        //分享个数
//        int shareCount = saylove.getShareNumber();
//        String shareCountStr = saylove.getShareNumberStr();
//        if (shareCount > 9999) {
//            share_tv.setText(shareCountStr);
//        } else {
//            share_tv.setText(shareCount + "");
//        }
//    }
//
//    /**
//     * 获取表白评论数据
//     */
//    private void requestCommentData() {
//        getCommentListPresenter.getCommentList(userId, sayloveId, sort, pageSize, pageNum, true);
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (apiName.equals(InterfaceUrl.URL_FINDSAYLOVEDETAIL)) {
//            if (object instanceof FindSayLoveDetailResponse) {
//                FindSayLoveDetailResponse findSayLoveDetailResponse = (FindSayLoveDetailResponse) object;
//                int code = findSayLoveDetailResponse.getCode();
//                if (code == 200) {
//                    if (findSayLoveDetailResponse.getSayLoveInfo() != null) {
//                        initSayLoveInfo(findSayLoveDetailResponse.getSayLoveInfo());
//                    } else {
//                        Utils.showToastShortTime("该表白已被删除");
//                        finish();
//                    }
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_GETCOMMENTLIST)) {
//            if (object instanceof GetCommentListResponse) {
//                GetCommentListResponse getCommentListResponse = (GetCommentListResponse) object;
//                int code = getCommentListResponse.getCode();
//                if (code == 200) {
//                    List<ConfessionComment> confessionCommentList = getCommentListResponse.getList();
//                    if (confessionCommentList != null) {
//                        if (pageNum == 1) {
//                            refreshLayout.finishRefreshing();
//                            sayloveCommentListAdapter.setList(confessionCommentList);
//                        } else {
//                            refreshLayout.finishLoadmore();
//                            sayloveCommentListAdapter.addList(confessionCommentList);
//                        }
//                        sayloveCommentListAdapter.notifyDataSetChanged();
//                        if (confessionCommentList.size() > 0) {
//                            emptyView.setVisibility(View.GONE);
//                        } else {
//                            emptyView.setVisibility(View.VISIBLE);
//                            emptyStr = getCommentListResponse.getBlankHint();
//                            emptyTextView.setText(emptyStr);
//                        }
//
//                    } else {
//                        Utils.showToastShortTime(getCommentListResponse.getMsg());
//                    }
//                    if (getCommentListResponse.getTotal() <= pageNum * pageSize) {
//                        //没有更多数据时  下拉刷新不可用
//                        refreshLayout.setEnableLoadmore(false);
//                    } else {
//                        //有更多数据时  下拉刷新才可用
//                        refreshLayout.setEnableLoadmore(true);
//                    }
//
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_COMMENTUPLOADIMAGE)) {
//            if (object instanceof SayLoveCommentResponse) {
//                SayLoveCommentResponse sayLoveCommentResponse = (SayLoveCommentResponse) object;
//                int code = sayLoveCommentResponse.getCode();
//                Utils.showToastShortTime(sayLoveCommentResponse.getMsg());
//                if (code == 200) {
//                    ConfessionComment confessionComment = sayLoveCommentResponse.getComment();
//                    if (confessionComment != null) {
////                        //评论或者回复成功
////                        sayloveCommentListAdapter.addItem(confessionComment);
//                        int commentCount = Integer.parseInt(comment_tv.getText().toString()) + 1;
//                        String commentCountStr = saylove.getShareNumberStr();
//                        if (commentCount > 9999) {
//                            comment_tv.setText(commentCountStr);
//                        } else {
//                            comment_tv.setText(commentCount + "");
//                        }
//                        final List<Image> imgList = commentImageRecyclerAdapter.getList();
//                        if (imgList != null && imgList.size() > 0) {
//                            for (int i = 0; i < imgList.size(); i++) {
//                                Image image = imgList.get(i);
//                                getImagePresenter().uploadImages(confessionComment.getCommentId(), 16, userId, DateUtils.getFormatDataString(new java.util.Date(), DateUtils.DATE_FORMAT_2), image.getImageUrl(), 0, i + 1, imgList.size());
//                            }
//                            commentImageRecyclerAdapter.getList().clear();
//                            commentImageRecyclerAdapter.notifyDataSetChanged();
//                            maxSelectNum = 9;
//                            if (commentImageRecyclerAdapter.getItemCount() > 0) {
//                                commentImageRecyclerView.setVisibility(View.VISIBLE);
//                            } else {
//                                commentImageRecyclerView.setVisibility(View.GONE);
//                            }
//                        }
//
////                        comment_tv.setText((Integer.parseInt(comment_tv.getText().toString()) + 1) + "");
////                        sendEdit.setText("");
////                        if (sayloveCommentListAdapter.getCount() > 0) {
////                            emptyView.setVisibility(View.GONE);
////                        } else {
////                            emptyView.setVisibility(View.VISIBLE);
////                            emptyTextView.setText(emptyStr);
////                        }
////                        //刷新表白列表的评论个数
////                        EventBus.getDefault().post(new UpdateSayloveListEvent(1, sayloveListPosition, sayloveCommentListAdapter.getCount(), 0));
//                    }
//                    pageNum = 1;
//                    requestCommentData();
//                }
//
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_SAYLOVEPRAISE)) {
//            if (object instanceof SayLovePraiseResponse) {
//                SayLovePraiseResponse sayLovePraiseResponse = (SayLovePraiseResponse) object;
//                int code = sayLovePraiseResponse.getCode();
//                if (code == 200) {
//                    isPraise = sayLovePraiseResponse.getIsPraise();
//                    if (isPraise == 1) {
//                        // 点赞
//                        praise_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
////                        praise_tv.setText((Integer.parseInt(praise_tv.getText().toString()) + 1) + "");
//                        int praiseCount = Integer.parseInt(praise_tv.getText().toString()) + 1;
//                        String praiseCountStr = saylove.getPraiseCountStr();
//                        if (praiseCount > 9999) {
//                            praise_tv.setText(praiseCountStr);
//                        } else {
//                            praise_tv.setText(praiseCount + "");
//                        }
//                    } else if (isPraise == 2) {
//                        // 取消点赞
//                        praise_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
////                        praise_tv.setText((Integer.parseInt(praise_tv.getText().toString()) - 1) + "");
//                        int praiseCount = Integer.parseInt(praise_tv.getText().toString()) - 1;
//                        String praiseCountStr = saylove.getPraiseCountStr();
//                        if (praiseCount > 9999) {
//                            praise_tv.setText(praiseCountStr);
//                        } else {
//                            praise_tv.setText(praiseCount + "");
//                        }
//                    }
//                    //刷新表白列表的点赞状态
//                    EventBus.getDefault().post(new UpdateSayloveListEvent(2, sayloveListPosition, 0, isPraise));
//                } else {
//                    Utils.showToastShortTime(sayLovePraiseResponse.getMsg());
//                }
//
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_DELETESAYLOVECOMMENT)) {
//            if (object instanceof NoParameterResponse) {
//                NoParameterResponse noParameterResponse = (NoParameterResponse) object;
//                int code = noParameterResponse.getCode();
//                if (code == 200) {
//                    Utils.showToastShortTime(noParameterResponse.getMsg());
//                    //删除评论或者删除回复成功
//                    sayloveCommentListAdapter.removeItem(commentPosition);
//                    comment_tv.setText((Integer.parseInt(comment_tv.getText().toString()) - 1) + "");
//                    if (sayloveCommentListAdapter.getCount() > 0) {
//                        emptyView.setVisibility(View.GONE);
//                    } else {
//                        emptyView.setVisibility(View.VISIBLE);
//                        emptyTextView.setText(emptyStr);
//                    }
//                    //刷新表白列表的评论个数
//                    EventBus.getDefault().post(new UpdateSayloveListEvent(1, sayloveListPosition, sayloveCommentListAdapter.getCount(), 0));
//                } else {
//                    Utils.showToastShortTime(noParameterResponse.getMsg());
//                }
//
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_DELETESAYLOVE)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse baseResponse = (BaseResponse) object;
//                int code = baseResponse.getCode();
//                if (code == 200) {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                    //删除成功
//                    finish();
//                    //刷新表白列表的删除状态
//                    EventBus.getDefault().post(new UpdateSayloveListEvent(3, sayloveListPosition, 0, 0));
//                } else {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                }
//
//            }
//        } else if (InterfaceUrl.URL_GETREPORTTYPE.equals(apiName)) {
//            if (object instanceof GetReportTypeResponse) {
//                GetReportTypeResponse response = (GetReportTypeResponse) object;
//                final List<ReportType> reportTypeList = response.getReprotTypeList();
//                if (response.getCode() == 200 && reportTypeList != null && reportTypeList.size() > 0) {
//                    PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
//                    String[] str = new String[reportTypeList.size()];
//                    for (int i = 0; i < reportTypeList.size(); i++) {
//                        str[i] = reportTypeList.get(i).getReportName();
//                    }
//                    postDetailCommentDialog.setItemStr(str);
//                    postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
//                        @Override
//                        public void itemClick(int position, String content) {
//                            if (reportType == 6) {
//                                //举报表白
//                                commonPresenter.reportOperation(saylove.getSayLoveId(), reportType, userId, pullBackUserId, reportTypeList.get(position).getReportTypeId());
//                            } else {
//                                //举报评论或者回复
//                                commonPresenter.reportOperation(commentId, reportType, userId, pullBackUserId, reportTypeList.get(position).getReportTypeId());
//                            }
//                        }
//                    });
//                    postDetailCommentDialog.show(getSupportFragmentManager());
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_ATTENTIONOPERATION)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse baseResponse = (BaseResponse) object;
//                int code = baseResponse.getCode();
//                if (code == 200) {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                    //关注成功
//                    attention_tv.setVisibility(View.GONE);
//                } else {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                }
//
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_PULLBLACKUSER)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse baseResponse = (BaseResponse) object;
//                Utils.showToastShortTime(baseResponse.getMsg());
//                if (baseResponse.getCode() == 200) {
//                    try {
//                        RongIMClient.getInstance().addToBlacklist(pullBackUserId + "", new RongIMClient.OperationCallback() {
//                            @Override
//                            public void onSuccess() {
//                                Log.d("addToBlack onSuccess", "");
//                            }
//
//                            @Override
//                            public void onError(RongIMClient.ErrorCode errorCode) {
//                                Log.d("errorCode == ", errorCode + "");
//
//                                return;
//                            }
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_COMMENTPRAISE)) {
//            if (object instanceof CommentPraiseResponse) {
//                CommentPraiseResponse response = (CommentPraiseResponse) object;
//                int code = response.getCode();
//                if (code == 200) {
//                    Utils.showToastShortTime(response.getMsg());
//                    //评论点赞成功
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
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(ShareNumUpdateEvent event) {
//        if (event == null) {
//            return;
//        }
//        if (event.getType() == ShareUtil.SAYLOVE) {
//            //本地更新表白分享次数
//            int shareCount = saylove.getShareNumber() + 1;
//            String shareCountStr = saylove.getShareNumberStr();
//            if (shareCount > 9999) {
//                share_tv.setText(shareCountStr);
//            } else {
//                share_tv.setText(shareCount + "");
//            }
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventVideoCommentFinish(VideoCommentFinishEvent event) {
//        if (event == null) {
//            return;
//        }
//        if (event.getModule() == 2) {
//            pageNum = 1;
//            requestCommentData();
//            if (commentImageRecyclerAdapter.getList() != null) {
//                commentImageRecyclerAdapter.getList().clear();
//            }
//            setSendViewBg(sendEdit.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
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
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
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
//                    setSendViewBg(sendEdit.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
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
//                                    setSendViewBg(sendEdit.getText().toString().trim(), commentImageRecyclerAdapter.getList() != null && commentImageRecyclerAdapter.getList().size() > 0);
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
//                Utils.hideSystemSoftInputKeyboard(sendEdit);
//                if (maxSelectNum == 0) {
//                    Utils.showToastShortTime("您最多可选9张图片");
//                } else {
//                    selectPic();
//                }
//                break;
//            case R.id.img_gif:
//                selectMode = 3;
//                maxSelectNum = 1;
//                Utils.hideSystemSoftInputKeyboard(sendEdit);
//                selectPic();
//                break;
//            case R.id.img_video:
//                selectMode = 2;
//                Utils.hideSystemSoftInputKeyboard(sendEdit);
//                ActiveEditSelectDialog dialog = new ActiveEditSelectDialog();
//                dialog.setItemText("拍摄", "从手机相册选择");
//                dialog.setItemClick(new ActiveEditSelectDialog.ItemClick() {
//                    @Override
//                    public void itemClick(int position) {
//                        switch (position) {
//                            case 1:
//                                //拍摄
//                                Intent intent = new Intent(SayLoveDetailActivity.this, PublishVideoActivity.class);
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
//}
