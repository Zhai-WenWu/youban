package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.PostImageRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;

/**
 *
 */
public class SayloveSharedFragment extends BaseFragment {

    private View rootView;
    private AppCompatActivity mActivity;
    private RequestOptions options, userOptions;
    private int screenWidth;
    private SayLoveInfo sayLoveInfo;

    public void setObject(SayLoveInfo sayLoveInfo) {
        this.sayLoveInfo = sayLoveInfo;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_saylove_shared_layout, null);
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
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
        userOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_user_icon)
                .error(R.mipmap.error_user_icon);
        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
        initView();
    }

    private void initView() {

        if (sayLoveInfo != null) {
            TextView userNickNameTextView = (TextView) rootView.findViewById(R.id.form_user_nickname_text);
            TextView contentTextView = (TextView) rootView.findViewById(R.id.content_text);
            TextView shareNumTextView = (TextView) rootView.findViewById(R.id.share_text);
            TextView praisNumTextView = (TextView) rootView.findViewById(R.id.praise_text);
            TextView commentNumTextView = (TextView) rootView.findViewById(R.id.comment_text);
            RelativeLayout linear_image_two = (RelativeLayout) rootView.findViewById(R.id.linear_image_two);
            ImageView img_first = (ImageView) rootView.findViewById(R.id.img_first);
            ImageView video_play_img = (ImageView) rootView.findViewById(R.id.video_play_img);
            RecyclerView recycler_image = (RecyclerView) rootView.findViewById(R.id.recycler_image);
            recycler_image.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recycler_image.setLayoutManager(gridLayoutManager);
            recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));


            final BaseUser baseUser = sayLoveInfo.getBaseUser();
            final int isAnonymous = sayLoveInfo.getIsAnonymous();
            userNickNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) && isAnonymous == 2) {
                        //跳到用户详情(自己和匿名不可点击)
                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                    }
                }
            });
            String nickName = baseUser.getNickName();
            userNickNameTextView.setText("@" + nickName);

            if (StringUtils.isEmpty(sayLoveInfo.getContent())) {
                contentTextView.setText(sayLoveInfo.getContent());
                contentTextView.setVisibility(View.GONE);
            } else {
                contentTextView.setText(sayLoveInfo.getContent());
                contentTextView.setVisibility(View.VISIBLE);
            }
            shareNumTextView.setText(Utils.bigNumberToStr(sayLoveInfo.getShareNumber(), sayLoveInfo.getShareNumberStr()));
//            shareNumTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //分享弹窗
//                    String imgUrl = "";
//                    if (sayLoveInfo.getConfessionType() == 2) {
//                        imgUrl = sayLoveInfo.getVideoImageUrl();
//                    } else {
//                        if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                            imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                        }
//                    }
//                    ShareUtil.getInstance().showShare(ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo,"", "", "", imgUrl,true);
//
//                }
//            });
            praisNumTextView.setText(Utils.bigNumberToStr(sayLoveInfo.getPraiseCount(), sayLoveInfo.getPraiseCountStr()));
            final int isPraise = sayLoveInfo.getIsPraise();
            if (isPraise == 1) {
                //1 : 已点赞
                praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
            } else {
                //0: 未点赞
                praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
            }
            commentNumTextView.setText(Utils.bigNumberToStr(sayLoveInfo.getCommentCount(), sayLoveInfo.getCommentCountStr()));
            // 图片显示区域
            recycler_image.setVisibility(View.GONE);
            linear_image_two.setVisibility(View.GONE);
            if (sayLoveInfo.getConfessionType() == 2) {
                linear_image_two.setVisibility(View.VISIBLE);
                video_play_img.setVisibility(View.VISIBLE);
                Glide.with(App.getInstance()).load(sayLoveInfo.getVideoImageUrl()).apply(options).into(img_first);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img_first.getLayoutParams();
                layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
                img_first.setLayoutParams(layoutParams);
//                img_first.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //跳转到视频播放界面
//                        Intent intent = new Intent(mActivity, VideoPlayFullScreenActivity.class);
//                        intent.putExtra(Constants.Fields.VIDEO_PATH, sayLoveInfo.getVideoUrl());
//                        intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, sayLoveInfo.getVideoImageUrl());
//                        mActivity.startActivity(intent);
//                    }
//                });
            } else {
                final List<Image> imageList = sayLoveInfo.getImageList();
                if (imageList != null) {
                    if (imageList.size() > 0 && imageList.size() <= 1) {

                        linear_image_two.setVisibility(View.VISIBLE);
                        video_play_img.setVisibility(View.GONE);
                        if (imageList.size() == 1) {
                            Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(img_first);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img_first.getLayoutParams();
                            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
                            img_first.setLayoutParams(layoutParams);
//                            img_first.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (mActivity != null) {
//                                        ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
//                                    }
//                                }
//                            });
                        }
                    } else if (imageList.size() > 1) {
                        linear_image_two.setVisibility(View.GONE);
                        PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
                        recycler_image.setAdapter(postImageLIstAdapter);

//                        postImageLIstAdapter.setOnItemListener(new ItemListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                if (mActivity != null) {
//                                    ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
//                                }
//                            }
//                        });

                        recycler_image.setVisibility(View.VISIBLE);
                    }
                }
            }


        }

    }

    @Override
    protected void onVisible(boolean isInit) {

    }


}
