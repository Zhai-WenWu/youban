package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.HelpInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ClassMateHelpPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.PublishActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;


public class VideoPlayRightSeviceFragment extends BaseFragment implements BaseView, View.OnClickListener {

    private View rootView;
    private CircleImageView ivUserIcon, ivFocus, ivShare, ivPrice, ivComment, ivPark, iv_follow_shot;
    private ImageView ivMore, ivBack;
    private TextView tvCommentNum, tvFabulousNum, tvPkDescrip, tvShareNum, tv_follow_shot_num;
    private UserPresenter myPresenter;
    private int praiseType = 1;
    private int type;//类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请,8表白,9同学帮帮忙,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白)
    private HelpInfo helpInfo;
    private BaseUser baseUser;
    private String videoImageUrl;
    private String videoUrl;
    private String content;
    private int praiseCount;
    private String praiseCountStr;
    private int commentCount;
    private String commentCountStr;
    private int shareNumber;
    private String shareNumberStr;
    private int followCount;
    private int isPraise;
    private Intent intent;
    private long parentId;
    private ClassMateHelpPresenter classMateHelpPresenter;

    @Override
    protected void onVisible(boolean isInit) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_video_play_right_sevice_layout, null);
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
        //super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        ivUserIcon = rootView.findViewById(R.id.iv_user_icon);
        ivFocus = rootView.findViewById(R.id.iv_focus);
        ivPrice = rootView.findViewById(R.id.iv_price);
        ivComment = rootView.findViewById(R.id.iv_comment);
        iv_follow_shot = rootView.findViewById(R.id.iv_follow_shot);
        tv_follow_shot_num = rootView.findViewById(R.id.tv_follow_shot_num);
        ivPark = rootView.findViewById(R.id.iv_park);
        ivShare = rootView.findViewById(R.id.iv_share);
        tvFabulousNum = rootView.findViewById(R.id.tv_fabulous_num);
        tvCommentNum = rootView.findViewById(R.id.tv_comment_num);
        tvPkDescrip = rootView.findViewById(R.id.tv_pk_descrip);
        tvPkDescrip.setMovementMethod(ScrollingMovementMethod.getInstance());
        ivBack = rootView.findViewById(R.id.iv_back);
        ivMore = rootView.findViewById(R.id.iv_all);
        tvShareNum = rootView.findViewById(R.id.tv_share_num);
        ivFocus.setOnClickListener(this);
        ivPrice.setOnClickListener(this);
        ivComment.setOnClickListener(this);
        iv_follow_shot.setOnClickListener(this);
        tv_follow_shot_num.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivPark.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivUserIcon.setOnClickListener(this);

    }

    public void setData(HelpInfo helpInfo) {
        this.helpInfo = helpInfo;
        baseUser = helpInfo.getBaseUser();
        videoImageUrl = helpInfo.getVideoImageUrl();
        videoUrl = helpInfo.getVideoUrl();
        content = helpInfo.getContent();
        praiseCount = helpInfo.getPraiseCount();
        praiseCountStr = helpInfo.getPraiseCountStr();
        commentCount = helpInfo.getCommentCount();
        commentCountStr = helpInfo.getCommentCountStr();
        shareNumber = helpInfo.getShareNumber();
        shareNumberStr = helpInfo.getShareNumberStr();
        followCount = helpInfo.getFollowCount();
        isPraise = helpInfo.getIsPraise();
        parentId = helpInfo.getParentId();
        initData();
    }

    private void initData() {

        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
            ivMore.setVisibility(View.GONE);
        }

        String userIcon = baseUser.getUserIcon();
        Glide.with(App.getInstance()).load(userIcon).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(ivUserIcon);
        tvPkDescrip.setText(content);
        tvFabulousNum.setText(Utils.bigNumberToStr(praiseCount, praiseCountStr));
        tvCommentNum.setText(Utils.bigNumberToStr(commentCount, commentCountStr));
        tvShareNum.setText(Utils.bigNumberToStr(shareNumber, shareNumberStr));
        tv_follow_shot_num.setText(String.valueOf(followCount));

        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) || baseUser.getIsAttention() == 1) {
            ivFocus.setVisibility(View.INVISIBLE);
        }

        if (isPraise == 1) {
            praiseType = 2;
            ivPrice.setImageDrawable(getResources().getDrawable(R.drawable.video_dianzan_true));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_focus:
                getMyPresenter().attentionOperation(1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId());
                break;
            case R.id.iv_follow_shot:
                //跟拍
                intent = new Intent(getActivity(), PublishActivity.class);
                intent.putExtra(Constants.Fields.TYPE, 1);
                intent.putExtra(Constants.Fields.PARENT_ID, parentId);
                intent.putExtra(Constants.Fields.FOLLOWING_SHOT_ENTER_TYPE, type);
                startActivity(intent);
                break;
            case R.id.iv_share:
                switch (type) {
//                    case 1:
//                        ShareUtil.getInstance().showShare(getActivity(), ShareUtil.POST, postInfo.getPostId(), postInfo, "来自：友伴", "", "", videoImageUrl, true);
//                        break;
//                    case 8:
//                        ShareUtil.getInstance().showShare(getActivity(), ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "来自：友伴", "", "", videoImageUrl, true);
//                        break;
                    case 9:
                        ShareUtil.getInstance().showShare(getActivity(), ShareUtil.CLASSMATEHELP, helpInfo.getHelpId(), helpInfo, "来自：友伴", "", "", videoImageUrl, true);
                        break;
                }
                break;
            case R.id.iv_park:
                break;
            case R.id.iv_price:
                switch (type) {
//                    case 1:
//                        if (isPraise == 1) {
//                            postCommentListPresenter.postPraise(userId, postInfo.getPostId(), 2);
//                        } else if (isPraise == 0) {
//                            postCommentListPresenter.postPraise(userId, postInfo.getPostId(), 1);
//                        }
//                        break;
//                    case 8:
//                        if (isPraise == 0) {
//                            //点赞
//                            getCommentListPresenter.sayLovePraise(sayLoveInfo.getSayLoveId(), 1, userId);
//                        } else if (isPraise == 1) {
//                            //取消点赞
//                            getCommentListPresenter.sayLovePraise(sayLoveInfo.getSayLoveId(), 2, userId);
//                        }
//                        break;
                    case 9:
                        if (isPraise == 0) {
                            //点赞
                            getClassMateHelpPresenter() .helpPraise(baseUser.getUserId(), helpInfo.getHelpId(), 1);
                        } else if (isPraise == 1) {
                            //取消点赞
                            getClassMateHelpPresenter() .helpPraise(baseUser.getUserId(), helpInfo.getHelpId(), 2);
                        }
                        break;
                }
                break;
            case R.id.iv_comment:
//                if (type == 1) {
//                    Intent intent = new Intent(getActivity(), PostDetailActivity.class);
//                    intent.putExtra(Constants.KEY.KEY_OBJECT, postInfo.getParentId());
//                    startActivity(intent);
//                } else if (type == 8) {
//                    Intent intent = new Intent(getActivity(), SayLoveDetailActivity.class);
//                    intent.putExtra(Constants.KEY.KEY_SAYLOVE_ID, sayLoveInfo.getSayLoveId());
//                    App.getInstance().getCurrentActivity().startActivity(intent);
//                } else
                break;

            case R.id.iv_user_icon:
                startActivity(new Intent(getActivity(), UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                break;
        }
    }

    private UserPresenter getMyPresenter() {
        if (myPresenter == null) {
            myPresenter = new UserPresenter(this);
        }
        return myPresenter;
    }

    public ClassMateHelpPresenter getClassMateHelpPresenter() {
        if (classMateHelpPresenter == null) {
            classMateHelpPresenter = new ClassMateHelpPresenter(this);
        }
        return classMateHelpPresenter;
    }

    @Override
    public void updateView(String apiName, Object object) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

}
