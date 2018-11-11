package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.PkCommentEvent;
import cn.bjhdltcdn.p2plive.event.ShareNumUpdateEvent;
import cn.bjhdltcdn.p2plive.httpresponse.AttentionStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindHelpDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindPostDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindSayLoveDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetReportTypeResponse;
import cn.bjhdltcdn.p2plive.httpresponse.HelpPraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SayLovePraiseResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.HelpInfo;
import cn.bjhdltcdn.p2plive.model.OrganBaseInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.ReportType;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ClassMateHelpPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.FollowPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.PostDetailCommentDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.PublishSuccessLabelFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.VideoPlayFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.tagview.TagContainerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by ZHAI on 2017/12/22.
 */

public class PKVideoPlayActivity extends BaseActivity implements BaseView, View.OnClickListener {

    private CircleImageView ivUserIcon;
    private ImageView ivMore, ivBack, iv_follow_shot, ivFocus, ivShare, ivFabulous, ivComment, ivPark;
    private TagContainerLayout tagContainerLayout;
    private TextView tvPkTitle, tvCommentNum, tvFabulousNum, tvPkDescrip, tvShareNum, tv_follow_shot_num;
    private UserPresenter myPresenter;
    private int praiseType = 1;
    private CommonPresenter commonPresenter;
    private int pageSize = 20;
    private int pageNumber = 1;
    private int commentCount;
    private UserPresenter userPresenter;
    private long toBlacklistUserId;
    private BaseUser baseUser;
    private int shareNumber;
    private String shareNumberStr;
    private int praiseCount;
    private String praiseCountStr;
    private String commentCountStr;
    private int followCount;
    private String videoUrl;
    private String videoImageUrl;
    private FollowPresenter followPresenter;
    private long userId;
    private int type;//类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请,8表白,9同学帮帮忙,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白)
    private String content;
    private int isPraise;
    private PostInfo postInfo;
    private SayLoveInfo sayLoveInfo;
    private HelpInfo helpInfo;
    private PostCommentListPresenter postCommentListPresenter;
    private GetCommentListPresenter getCommentListPresenter;
    private ClassMateHelpPresenter classMateHelpPresenter;
    private Intent intent;
    private long videoPlayId;
    private long parentId;
    private int comeInType;//1.详情进来2:从视频广场界面进入
    private int isAnonymous;
    private PublishSuccessLabelFragmentDialog successLabelFragmentDialog;
    private Runnable successLabelFragmentDialogRunnable;
    private long toUserId;
    private int status;
    private List<OrganBaseInfo> organList;
    private LinearLayout ll_tag;
    private String organName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk_vadio_play);
        EventBus.getDefault().register(this);
        initView();

        getCommentListPresenter = new GetCommentListPresenter(this);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        videoPlayId = getIntent().getLongExtra(Constants.Fields.VIDEO_PLAY_ID, -1);
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        Logger.d("videoPlayId: " + videoPlayId + "   type: " + type);
        comeInType = getIntent().getIntExtra(Constants.Fields.COME_IN_TYPE, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (type) {
            case 1:
                getPostCommentListPresenter().findPostDetail(userId, videoPlayId);
                break;
            case 8:
                getCommentListPresenter.findSayLoveDetail(userId, videoPlayId);
                break;
            case 9:
                getClassMateHelpPresenter().findHelpDetail(userId, videoPlayId);
                break;
        }
    }

    private void initView() {
        ivUserIcon = findViewById(R.id.iv_user_icon);
        ivFocus = findViewById(R.id.iv_focus);
        ivFabulous = findViewById(R.id.iv_price);
        ivComment = findViewById(R.id.iv_comment);
        iv_follow_shot = findViewById(R.id.iv_follow_shot);
        tv_follow_shot_num = findViewById(R.id.tv_follow_shot_num);
        ivPark = findViewById(R.id.iv_park);
        ivShare = findViewById(R.id.iv_share);
        tvFabulousNum = findViewById(R.id.tv_fabulous_num);
        tvCommentNum = findViewById(R.id.tv_comment_num);
        tvPkDescrip = findViewById(R.id.tv_pk_descrip);
        tvPkDescrip.setMovementMethod(ScrollingMovementMethod.getInstance());
        ivBack = findViewById(R.id.iv_back);
        ivMore = findViewById(R.id.iv_all);
        ll_tag = findViewById(R.id.ll_tag);
        tagContainerLayout = findViewById(R.id.tag_container_view);
        tvShareNum = findViewById(R.id.tv_share_num);
        ivFocus.setOnClickListener(this);
        ivFabulous.setOnClickListener(this);
        ivComment.setOnClickListener(this);
        iv_follow_shot.setOnClickListener(this);
        tv_follow_shot_num.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivPark.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivUserIcon.setOnClickListener(this);

    }

    private void initData() {

        if (organList == null) {
            ll_tag.setVisibility(View.INVISIBLE);
        } else {
            if (organList.size() == 0) {
                ll_tag.setVisibility(View.INVISIBLE);
            } else {
                tagContainerLayout.removeAllViews();
                for (int i = 0; i < organList.size(); i++) {
                    TextView textView = new TextView(this);
                    textView.setBackgroundResource(R.drawable.shape_round_20_stroke_ffffff);
                    organName = organList.get(i).getOrganName();
                    if (organName.length() > 4) {
                        organName = organName.substring(0, 4) + "...";
                    }
                    textView.setText(organName);
                    textView.setTextSize(10);
                    textView.setPadding(15, 2, 15, 2);
                    tagContainerLayout.addView(textView);
                    textView.setOnClickListener(new ViewOnClickListener(i));
                    if (i == 1) {
                        break;
                    }
                }
            }
        }

        if (status == 1) {
            finish();
            Utils.showToastShortTime("视频已删除");
        }

        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
            ivMore.setVisibility(View.INVISIBLE);
        }

        VideoPlayFragment mFragment = new VideoPlayFragment();
        mFragment.setData(videoUrl, videoImageUrl, 2);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.video_frame);
        String userIcon = baseUser.getUserIcon();
        Glide.with(this).load(userIcon).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(ivUserIcon);
        tvPkDescrip.setText(content);
        tvFabulousNum.setText(Utils.bigNumberToStr(praiseCount, praiseCountStr));
        tvCommentNum.setText(Utils.bigNumberToStr(commentCount, commentCountStr));
        tvShareNum.setText(Utils.bigNumberToStr(shareNumber, shareNumberStr));
        tv_follow_shot_num.setText(String.valueOf(followCount) + "条");

        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) || baseUser.getIsAttention() == 1) {
            ivFocus.setImageDrawable(getResources().getDrawable(R.drawable.hobby_select));
        }

        if (isAnonymous == 1 || userId == baseUser.getUserId()) {
            ivFocus.setVisibility(View.INVISIBLE);
        }

        if (isPraise == 1) {
            praiseType = 2;
            ivFabulous.setImageDrawable(getResources().getDrawable(R.drawable.video_dianzan_true));
        }
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (isFinishing()) {
            return;
        }
        switch (apiName) {
            case InterfaceUrl.URL_ATTENTIONOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                    if (baseResponse.getCode() == 200) {
                        ivFocus.setImageDrawable(getResources().getDrawable(R.drawable.hobby_select));
                        ivFocus.setClickable(false);
                    }
                }
                break;
            case InterfaceUrl.URL_ATTENTIONSTATUS:
                if (object instanceof AttentionStatusResponse) {
                    AttentionStatusResponse getAttentionStatusResponse = (AttentionStatusResponse) object;
                    if (getAttentionStatusResponse.getCode() == 200) {
                        int attentionStatus = getAttentionStatusResponse.getAttentionStatus();
                        if (attentionStatus == 1 || attentionStatus == 2) {
                            ivFocus.setImageDrawable(getResources().getDrawable(R.drawable.hobby_select));
                        } else {
                            ivFocus.setImageDrawable(getResources().getDrawable(R.drawable.add_focus));
                        }
                    }
                }

                break;
            case InterfaceUrl.URL_REPORTOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
                break;
            case InterfaceUrl.URL_FINDSAYLOVEDETAIL:
                if (object instanceof FindSayLoveDetailResponse) {
                    FindSayLoveDetailResponse findSayLoveDetailResponse = (FindSayLoveDetailResponse) object;
                    int code = findSayLoveDetailResponse.getCode();
                    if (code == 200) {
                        sayLoveInfo = findSayLoveDetailResponse.getSayLoveInfo();
                        if (sayLoveInfo != null) {
                            baseUser = sayLoveInfo.getBaseUser();
                            shareNumber = sayLoveInfo.getShareNumber();
                            shareNumberStr = sayLoveInfo.getShareNumberStr();
                            praiseCount = sayLoveInfo.getPraiseCount();
                            praiseCountStr = sayLoveInfo.getPraiseCountStr();
                            commentCount = sayLoveInfo.getCommentCount();
                            commentCountStr = sayLoveInfo.getCommentCountStr();
                            followCount = sayLoveInfo.getFollowCount();
                            videoUrl = sayLoveInfo.getVideoUrl();
                            videoImageUrl = sayLoveInfo.getVideoImageUrl();
                            content = sayLoveInfo.getContent();
                            isPraise = sayLoveInfo.getIsPraise();
                            parentId = sayLoveInfo.getParentId();
                            isAnonymous = sayLoveInfo.getIsAnonymous();
                            status = sayLoveInfo.getStatus();
                            organList = sayLoveInfo.getOrganList();
                            if (sayLoveInfo.getOriginalInfo() != null) {
                                toUserId = sayLoveInfo.getOriginalInfo().getUserId();
                            } else {
                                toUserId = baseUser.getUserId();
                            }
                            initData();
                        } else {
                            Utils.showToastShortTime("该表白已被删除");
                            finish();
                        }
                    }
                }
            case InterfaceUrl.URL_FINDPOSTDETAIL:
                if (object instanceof FindPostDetailResponse) {

                    FindPostDetailResponse response = (FindPostDetailResponse) object;

                    if (response.getCode() == 200) {
                        postInfo = response.getPostInfo();
                        if (postInfo != null) {
                            baseUser = postInfo.getBaseUser();
                            shareNumber = postInfo.getShareNumber();
                            shareNumberStr = postInfo.getShareNumberStr();
                            praiseCount = postInfo.getPraiseCount();
                            praiseCountStr = postInfo.getPraiseCountStr();
                            commentCount = postInfo.getCommentCount();
                            commentCountStr = postInfo.getCommentCountStr();
                            followCount = postInfo.getFollowCount();
                            videoUrl = postInfo.getVideoUrl();
                            videoImageUrl = postInfo.getVideoImageUrl();
                            content = postInfo.getContent();
                            isPraise = postInfo.getIsPraise();
                            parentId = postInfo.getParentId();
                            isAnonymous = postInfo.getIsAnonymous();
                            status = postInfo.getStatus();
                            organList = postInfo.getOrganList();
                            if (postInfo.getOriginalInfo() != null) {
                                toUserId = postInfo.getOriginalInfo().getUserId();
                            } else {
                                toUserId = baseUser.getUserId();
                            }
                            initData();
                        } else {
                            Utils.showToastShortTime(response.getMsg());
                            finish();
                        }
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                        finish();
                    }
                }
                break;
            case InterfaceUrl.URL_FINDHELPDETAIL:
                if (object instanceof FindHelpDetailResponse) {

                    FindHelpDetailResponse response = (FindHelpDetailResponse) object;

                    if (response.getCode() == 200) {
                        helpInfo = response.getHelpInfo();
                        if (helpInfo != null) {
                            baseUser = helpInfo.getBaseUser();
                            shareNumber = helpInfo.getShareNumber();
                            shareNumberStr = helpInfo.getShareNumberStr();
                            praiseCount = helpInfo.getPraiseCount();
                            praiseCountStr = helpInfo.getPraiseCountStr();
                            commentCount = helpInfo.getCommentCount();
                            commentCountStr = helpInfo.getCommentCountStr();
                            followCount = helpInfo.getFollowCount();
                            videoUrl = helpInfo.getVideoUrl();
                            videoImageUrl = helpInfo.getVideoImageUrl();
                            content = helpInfo.getContent();
                            isPraise = helpInfo.getIsPraise();
                            parentId = helpInfo.getParentId();
                            status = helpInfo.getStatus();
                            organList = helpInfo.getOrganList();
                            if (helpInfo.getOriginalInfo() != null) {
                                toUserId = helpInfo.getOriginalInfo().getUserId();
                            } else {
                                toUserId = baseUser.getUserId();
                            }

                            //isAnonymous = helpInfo.getIsAnonymous();
                            initData();
                        }
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                        finish();
                    }
                }
                break;
            case InterfaceUrl.URL_GETREPORTTYPE://举报类型
                if (object instanceof GetReportTypeResponse) {
                    GetReportTypeResponse response = (GetReportTypeResponse) object;
                    final List<ReportType> reportTypeList = response.getReprotTypeList();
                    if (response.getCode() == 200 && reportTypeList != null && reportTypeList.size() > 0) {
                        PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
                        String[] str = new String[reportTypeList.size()];
                        for (int i = 0; i < reportTypeList.size(); i++) {
                            str[i] = reportTypeList.get(i).getReportName();
                        }
                        postDetailCommentDialog.setItemStr(str);
                        postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
                            @Override
                            public void itemClick(int position, String content) {
                                switch (type) {
                                    case 1:
                                        getCommonPresenter().reportOperation(postInfo.getPostId(), 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId(), reportTypeList.get(position).getReportTypeId());
                                        break;
                                    case 8:
                                        getCommonPresenter().reportOperation(sayLoveInfo.getSayLoveId(), 6, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId(), reportTypeList.get(position).getReportTypeId());
                                        break;
                                    case 9:
                                        getCommonPresenter().reportOperation(helpInfo.getHelpId(), 12, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId(), reportTypeList.get(position).getReportTypeId());
                                        break;
                                }
                            }
                        });
                        postDetailCommentDialog.show(getSupportFragmentManager());
                    } else {
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

            case InterfaceUrl.URL_POSTPRAISE:
                if (object instanceof PostPraiseResponse) {
                    PostPraiseResponse postPraiseResponse = (PostPraiseResponse) object;
                    if (postPraiseResponse.getCode() == 200) {
                        Utils.showToastShortTime(postPraiseResponse.getMsg());
                        if (postPraiseResponse.getIsPraise() == 1) {
                            //点赞
                            praiseCount += 1;
                            isPraise = 1;
                            tvFabulousNum.setText(String.valueOf(praiseCount));
                            ivFabulous.setImageDrawable(getResources().getDrawable(R.drawable.video_dianzan_true));
                        } else if (postPraiseResponse.getIsPraise() == 2) {
                            //取消点赞
                            praiseCount -= 1;
                            isPraise = 0;
                            tvFabulousNum.setText(String.valueOf(praiseCount));
                            ivFabulous.setImageDrawable(getResources().getDrawable(R.drawable.video_dianzan_false));
                        }
                    } else {
                        Utils.showToastShortTime(postPraiseResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_SAYLOVEPRAISE:
                if (object instanceof SayLovePraiseResponse) {
                    SayLovePraiseResponse sayLovePraiseResponse = (SayLovePraiseResponse) object;
                    if (sayLovePraiseResponse.getCode() == 200) {
                        Utils.showToastShortTime(sayLovePraiseResponse.getMsg());
                        isPraise = sayLovePraiseResponse.getIsPraise();
                        if (sayLovePraiseResponse.getIsPraise() == 1) {
                            //点赞
                            praiseCount += 1;
                            isPraise = 1;
                            tvFabulousNum.setText(String.valueOf(praiseCount));
                            ivFabulous.setImageDrawable(getResources().getDrawable(R.drawable.video_dianzan_true));
                        } else if (sayLovePraiseResponse.getIsPraise() == 2) {
                            //取消点赞
                            praiseCount -= 1;
                            isPraise = 0;
                            tvFabulousNum.setText(String.valueOf(praiseCount));
                            ivFabulous.setImageDrawable(getResources().getDrawable(R.drawable.video_dianzan_false));
                        }
                    } else {
                        Utils.showToastShortTime(sayLovePraiseResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_HELPPRAISE:
                if (object instanceof HelpPraiseResponse) {
                    HelpPraiseResponse helpPraiseResponse = (HelpPraiseResponse) object;
                    Utils.showToastShortTime(helpPraiseResponse.getMsg());
                    isPraise = helpPraiseResponse.getIsPraise();
                    if (helpPraiseResponse.getCode() == 200) {
                        Utils.showToastShortTime(helpPraiseResponse.getMsg());
                        if (helpPraiseResponse.getIsPraise() == 1) {
                            //点赞
                            praiseCount += 1;
                            isPraise = 1;
                            tvFabulousNum.setText(String.valueOf(praiseCount));
                            ivFabulous.setImageDrawable(getResources().getDrawable(R.drawable.video_dianzan_true));
                        } else if (helpPraiseResponse.getIsPraise() == 2) {
                            //取消点赞
                            praiseCount -= 1;
                            isPraise = 0;
                            tvFabulousNum.setText(String.valueOf(praiseCount));
                            ivFabulous.setImageDrawable(getResources().getDrawable(R.drawable.video_dianzan_false));
                        }
                    } else {
                        Utils.showToastShortTime(helpPraiseResponse.getMsg());
                    }
                }
                break;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_focus:
                getMyPresenter().attentionOperation(1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId());
                break;
            case R.id.iv_follow_shot:
                //跟拍
                intent = new Intent(PKVideoPlayActivity.this, PublishActivity.class);
                intent.putExtra(Constants.Fields.TYPE, 1);
                intent.putExtra(Constants.Fields.PARENT_ID, parentId);
                intent.putExtra(Constants.Fields.TO_USER_ID, baseUser.getUserId());
                intent.putExtra(Constants.Fields.FOLLOWING_SHOT_ENTER_TYPE, type);
                startActivity(intent);
                break;
            case R.id.iv_share:
                switch (type) {
                    case 1:
                        ShareUtil.getInstance().showShare(PKVideoPlayActivity.this, ShareUtil.POST, postInfo.getPostId(), postInfo, "来自：友伴", "", "", videoImageUrl, true);
                        break;
                    case 8:
                        ShareUtil.getInstance().showShare(PKVideoPlayActivity.this, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "来自：友伴", "", "", videoImageUrl, true);
                        break;
                    case 9:
                        ShareUtil.getInstance().showShare(PKVideoPlayActivity.this, ShareUtil.CLASSMATEHELP, helpInfo.getHelpId(), helpInfo, "来自：友伴", "", "", videoImageUrl, true);
                        break;
                }
                break;
            case R.id.iv_park:
                if (comeInType == 2) {
                    finish();
                } else {
//                    intent = new Intent(PKVideoPlayActivity.this, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, parentId);
//                    intent.putExtra(Constants.Fields.TO_USER_ID, toUserId);
//                    intent.putExtra(Constants.Fields.TYPE, type);
//                    startActivity(intent);
                }
                break;
            case R.id.iv_price:
                switch (type) {
                    case 1:
                        if (isPraise == 1) {
                            getPostCommentListPresenter().postPraise(userId, postInfo.getPostId(), 2);
                        } else if (isPraise == 0) {
                            getPostCommentListPresenter().postPraise(userId, postInfo.getPostId(), 1);
                        }
                        break;
                    case 8:
                        if (isPraise == 0) {
                            //点赞
                            getCommentListPresenter.sayLovePraise(sayLoveInfo.getSayLoveId(), 1, userId);
                        } else if (isPraise == 1) {
                            //取消点赞
                            getCommentListPresenter.sayLovePraise(sayLoveInfo.getSayLoveId(), 2, userId);
                        }
                        break;
                    case 9:
                        if (isPraise == 0) {
                            //点赞
                            classMateHelpPresenter.helpPraise(userId, helpInfo.getHelpId(), 1);
                        } else if (isPraise == 1) {
                            //取消点赞
                            classMateHelpPresenter.helpPraise(userId, helpInfo.getHelpId(), 2);
                        }
                        break;
                }
                break;
            case R.id.iv_comment:
                if (comeInType == 1) {
                    finish();
                } else {
                    if (type == 1) {
                        Intent intent = new Intent(PKVideoPlayActivity.this, PostDetailActivity.class);
                        intent.putExtra(Constants.KEY.KEY_OBJECT, postInfo);
                        startActivity(intent);
                    } else if (type == 8) {
                    } else if (type == 9) {
                    }
                }

                break;

            case R.id.iv_all:
                final PostOperationFragmentDialog dialog = new PostOperationFragmentDialog();

                if (isAnonymous == 1) {
                    dialog.setTextList("举报", "取消");
                } else {
                    dialog.setTextList("拉黑", "举报", "取消");
                }


                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {


                    @Override
                    public void onClick(int type) {

                        if (isAnonymous == 1) {
                            switch (type) {
                                case 1:
                                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                        //举报
                                        getCommonPresenter().getReportType(1);
                                    }
                                    break;
                            }
                        } else {
                            switch (type) {
                                case 1:
                                    //拉黑
                                    toBlacklistUserId = baseUser.getUserId();
                                    getUserPresenter().pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBlacklistUserId);
                                    break;
                                case 2:
                                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                        //举报
                                        getCommonPresenter().getReportType(1);
                                    }
                                    break;
                            }
                        }

                    }
                });

                dialog.show(getSupportFragmentManager());
                break;
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.iv_user_icon:
                if (baseUser.getUserId() != userId && isAnonymous != 1) {
                    startActivity(new Intent(PKVideoPlayActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(PkCommentEvent evnet) {
        if (evnet != null) {
            commentCount += 1;
            tvCommentNum.setText(commentCount + "");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ShareNumUpdateEvent event) {
        if (event == null) {
            return;
        }
        if (event.getType() == ShareUtil.PK) {
            //本地更新pk分享次数
            tvShareNum.setText(shareNumber + 1 + "");
        }
    }


    private UserPresenter getMyPresenter() {
        if (myPresenter == null) {
            myPresenter = new UserPresenter(this);
        }
        return myPresenter;
    }

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public FollowPresenter getFollowPresenter() {
        if (followPresenter == null) {
            followPresenter = new FollowPresenter(this);
        }
        return followPresenter;
    }

    public ClassMateHelpPresenter getClassMateHelpPresenter() {
        if (classMateHelpPresenter == null) {
            classMateHelpPresenter = new ClassMateHelpPresenter(this);
        }
        return classMateHelpPresenter;
    }

    private PostCommentListPresenter getPostCommentListPresenter() {

        if (postCommentListPresenter == null) {
            postCommentListPresenter = new PostCommentListPresenter(this);
        }
        return postCommentListPresenter;
    }

    private CommonPresenter getCommonPresenter() {
        if (commonPresenter == null) {
            commonPresenter = new CommonPresenter(this);
        }
        return commonPresenter;
    }

    class ViewOnClickListener implements View.OnClickListener {

        private int position;

        public ViewOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {

        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (findViewById(R.id.video_frame) != null && successLabelFragmentDialogRunnable != null) {
            findViewById(R.id.video_frame).removeCallbacks(successLabelFragmentDialogRunnable);
        }

        if (ivUserIcon != null) {
            ivUserIcon = null;
        }

        if (ivMore != null) {
            ivMore = null;
        }

        if (ivBack != null) {
            ivBack = null;
        }

        if (iv_follow_shot != null) {
            iv_follow_shot = null;
        }

        if (ivFocus != null) {
            ivFocus = null;
        }

        if (ivShare != null) {
            ivShare = null;
        }

        if (ivFabulous != null) {
            ivFabulous = null;
        }

        if (ivComment != null) {
            ivComment = null;
        }

        if (ivPark != null) {
            ivPark = null;
        }

        if (tagContainerLayout != null) {
            tagContainerLayout.removeAllViews();
            tagContainerLayout = null;
        }

        if (tvPkTitle != null) {
            tvPkTitle = null;
        }

        if (tvCommentNum != null) {
            tvCommentNum = null;
        }

        if (tvFabulousNum != null) {
            tvFabulousNum = null;
        }

        if (tvPkDescrip != null) {
            tvPkDescrip = null;
        }

        if (tvShareNum != null) {
            tvShareNum = null;
        }

        if (tv_follow_shot_num != null) {
            tv_follow_shot_num = null;
        }

        if (myPresenter != null) {
            myPresenter.onDestroy();
            myPresenter = null;
        }

        if (commonPresenter != null) {
            commonPresenter.onDestroy();
            commonPresenter = null;
        }

        if (userPresenter != null) {
            userPresenter.onDestroy();
            userPresenter = null;
        }

        if (baseUser != null) {
            baseUser = null;
        }

        if (shareNumberStr != null) {
            shareNumberStr = null;
        }

        if (praiseCountStr != null) {
            praiseCountStr = null;
        }

        if (commentCountStr != null) {
            commentCountStr = null;
        }

        if (videoUrl != null) {
            videoUrl = null;
        }

        if (videoImageUrl != null) {
            videoImageUrl = null;
        }

        if (followPresenter != null) {
            followPresenter.onDestroy();
            followPresenter = null;
        }

        if (content != null) {
            content = null;
        }

        if (postInfo != null) {
            postInfo = null;
        }

        if (sayLoveInfo != null) {
            sayLoveInfo = null;
        }

        if (helpInfo != null) {
            helpInfo = null;
        }

        if (postCommentListPresenter != null) {
            postCommentListPresenter.onDestroy();
            postCommentListPresenter = null;
        }

        if (getCommentListPresenter != null) {
            getCommentListPresenter.onDestroy();
            getCommentListPresenter = null;
        }

        if (classMateHelpPresenter != null) {
            classMateHelpPresenter.onDestroy();
            classMateHelpPresenter = null;
        }

        if (intent != null) {
            intent = null;
        }

        if (successLabelFragmentDialog != null) {
            successLabelFragmentDialog.dismiss();
            successLabelFragmentDialog = null;
        }

        if (successLabelFragmentDialogRunnable != null) {
            successLabelFragmentDialogRunnable = null;
        }

        if (organList != null) {
            organList.clear();
            organList = null;
        }

        if (ll_tag != null) {
            ll_tag.removeAllViews();
            ll_tag = null;
        }

    }

}