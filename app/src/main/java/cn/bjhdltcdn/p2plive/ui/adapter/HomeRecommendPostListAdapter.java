package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.OrganBaseInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.OriginalInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PostDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;

public class HomeRecommendPostListAdapter extends BaseAdapter {
    private List<PostInfo> list;
    private AppCompatActivity mActivity;
    private RequestOptions options, userOptions, matchOptions;
    private OnClick onClick;
    private int screenWidth;
    private int type;//1：我的帖子列表   2:帖子列表 3: 附近帖子 4：他的帖子列表
    private RequestOptions gifOptions;
    private boolean refreshItem;
    private UserPresenter userPresenter;

    public void setUserPresenter(UserPresenter userPresenter) {
        this.userPresenter = userPresenter;
    }

    public HomeRecommendPostListAdapter(AppCompatActivity mActivity, int type) {
        this.mActivity = mActivity;
        options = new RequestOptions()
                .centerCrop()
                .transform(new GlideRoundTransform(9))
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);

        userOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_user_icon)
                .error(R.mipmap.error_user_icon);

        gifOptions = new RequestOptions()
                .centerCrop()
                .transform(new GlideRoundTransform(9))
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
        matchOptions = new RequestOptions()
                .placeholder(R.mipmap.post_match_error_icon)
                .error(R.mipmap.post_match_error_icon);

        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
        this.type = type;
    }


    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public void setRefreshItem(boolean refreshItem) {
        this.refreshItem = refreshItem;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<PostInfo> list) {
        this.list = list;
    }

    public void setListAll(List<PostInfo> list) {

        if (this.list == null) {
            return;
        }

        if (list == null) {
            return;
        }

        this.list.addAll(list);

    }

    public void addPostInfo(PostInfo postInfo) {

        if (postInfo == null) {
            return;
        }

        if (this.list == null) {
            this.list = new ArrayList<>(1);
        }

        this.list.add(0, postInfo);
        notifyDataSetChanged();

    }


    @Override
    public PostInfo getItem(int position) {
        return list == null || list.size() == 0 ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateCommentNum(int position, int commentNum) {
        list.get(position).setCommentCount(commentNum);
        refreshItem = true;
        notifyDataSetChanged();
    }

    public void updateIsPraise(int position, int isPraise) {
        PostInfo postInfo = list.get(position);
        if (isPraise == 1) {//点赞
            postInfo.setIsPraise(1);
            postInfo.setPraiseCount(postInfo.getPraiseCount() + 1);
        } else {
            //取消点赞
            postInfo.setIsPraise(0);
            postInfo.setPraiseCount(postInfo.getPraiseCount() - 1);
        }
        refreshItem = true;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    /**
     * @return the mList
     */
    public List<PostInfo> getList() {
        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.home_recommend_post_list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.userNickNameTextView = (TextView) convertView.findViewById(R.id.user_nickname_text);
            viewHolder.schoolNameTextView = (TextView) convertView.findViewById(R.id.school_name_text_view);
            viewHolder.alumnus_tv = (ImageView) convertView.findViewById(R.id.alumnus_text);
//            viewHolder.userLevelTextView = (ImageView) convertView.findViewById(R.id.user_level_text);
            viewHolder.userAgeTextView = (TextView) convertView.findViewById(R.id.user_age_text);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time_text);
            viewHolder.locationTextView = (TextView) convertView.findViewById(R.id.location_text);
            viewHolder.cityTextView = (TextView) convertView.findViewById(R.id.city_text);
            viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.recruitTextView= (TextView) convertView.findViewById(R.id.recruit_text);
            viewHolder.storeLayout= (RelativeLayout) convertView.findViewById(R.id.store_layout);
            viewHolder.storeNameTextView= (TextView) convertView.findViewById(R.id.store_name_text);
            viewHolder.storeIcon = convertView.findViewById(R.id.store_img);
//            viewHolder.labelFirstTextView = (TextView) convertView.findViewById(R.id.post_label_one_text);
//            viewHolder.labelSecondTextView = (TextView) convertView.findViewById(R.id.post_label_two_text);
//            viewHolder.labelSecondLayout = (RelativeLayout) convertView.findViewById(R.id.post_label_two_layout);
            viewHolder.shareNumTextView = (TextView) convertView.findViewById(R.id.share_text);
//            viewHolder.deleteImg = (ImageView) convertView.findViewById(R.id.delete_img);
            viewHolder.praisNumTextView = (TextView) convertView.findViewById(R.id.praise_text);
            viewHolder.commentNumTextView = (TextView) convertView.findViewById(R.id.comment_text);
//            viewHolder.postStickView = (ImageView) convertView.findViewById(R.id.post_stick_view);

            viewHolder.userImg = (ImageView) convertView.findViewById(R.id.user_img);
            viewHolder.linear_image_two = (RelativeLayout) convertView.findViewById(R.id.linear_image_two);
            viewHolder.img_first = (ImageView) convertView.findViewById(R.id.img_first);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
            viewHolder.img_first.setLayoutParams(layoutParams);
            viewHolder.video_play_img = (ImageView) convertView.findViewById(R.id.video_play_img);
            viewHolder.recycler_image = (RecyclerView) convertView.findViewById(R.id.recycler_image);
            viewHolder.recycler_post_label = (RecyclerView) convertView.findViewById(R.id.recycler_post_label);
            viewHolder.recycler_store_label = (RecyclerView) convertView.findViewById(R.id.store_label_recycle_view);
            viewHolder.divider = convertView.findViewById(R.id.divider_line);

            viewHolder.recycler_image.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            viewHolder.recycler_image.setLayoutManager(gridLayoutManager);
            viewHolder.recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(11), 3, false));
            viewHolder.recycler_image.setHasFixedSize(true);

            ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mActivity)
                    .setChildGravity(Gravity.TOP)
                    .setScrollingEnabled(true)
                    .setGravityResolver(new IChildGravityResolver() {
                        @Override
                        public int getItemGravity(int position) {
                            return Gravity.LEFT;
                        }
                     })
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT )
                    .withLastRow(true)
                    .build();
            viewHolder.recycler_post_label.setLayoutManager(chipsLayoutManager);
            viewHolder.recycler_post_label.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
            linearLayoutManager.setAutoMeasureEnabled(true);
            viewHolder.recycler_store_label.setLayoutManager(linearLayoutManager);
            viewHolder.recycler_store_label.setHasFixedSize(true);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PostInfo postInfo = list.get(position);
        final BaseUser baseUser = postInfo.getBaseUser();
        if (postInfo == null || baseUser == null) {
            return convertView;
        }
        final int isAnonymous = postInfo.getIsAnonymous();
        Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(viewHolder.userImg);
        viewHolder.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    if (isAnonymous == 2) {
                        //跳到用户详情
                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                    } else {
                        userPresenter.getAnonymityUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId());
                    }
                }
            }
        });
//        String level = postInfo.getActiveLevel();
//        if (!StringUtils.isEmpty(level) && isAnonymous == 2) {
//            viewHolder.userLevelTextView.setVisibility(View.VISIBLE);
//            Utils.getActiveLevel(viewHolder.userLevelTextView, level);
//        } else {
//            viewHolder.userLevelTextView.setVisibility(View.GONE);
//        }

        viewHolder.userAgeTextView.setText(baseUser.getAge() + "岁");
        if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) && isAnonymous == 2) {
            //校友 并且不是自己
            viewHolder.alumnus_tv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.alumnus_tv.setVisibility(View.GONE);
        }
        int sex = baseUser.getSex();
        if (sex == 1) {
            //男性
            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
        } else if (sex == 2) {
            //女性
            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
        }

        if (isAnonymous == 1) {
            viewHolder.userAgeTextView.setVisibility(View.VISIBLE);
            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            viewHolder.userAgeTextView.setText("");
            viewHolder.userAgeTextView.setPadding(0, 0, 0, 0);
        } else {
            viewHolder.userAgeTextView.setVisibility(View.VISIBLE);
            viewHolder.userAgeTextView.setPadding(0, 0, Utils.dp2px(10), 0);
        }
        viewHolder.userNickNameTextView.setText(baseUser.getNickName());
        viewHolder.userNickNameTextView.setVisibility(View.VISIBLE);


        if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
            //学校名称
            viewHolder.schoolNameTextView.setVisibility(View.VISIBLE);
            viewHolder.schoolNameTextView.setText(baseUser.getSchoolName());
        } else {
            viewHolder.schoolNameTextView.setVisibility(View.GONE);
        }
        viewHolder.timeTextView.setText(postInfo.getAddTime());
        if ((!StringUtils.isEmpty(baseUser.getDistance()))) {
            viewHolder.locationTextView.setText(baseUser.getDistance());
            viewHolder.locationTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.locationTextView.setVisibility(View.GONE);
        }
        if ((!StringUtils.isEmpty(baseUser.getCity()))) {
            viewHolder.cityTextView.setText(baseUser.getCity());
            viewHolder.cityTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.cityTextView.setVisibility(View.GONE);
        }
//        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//            //自己不展示位置
//            viewHolder.locationTextView.setVisibility(View.GONE);
//            viewHolder.cityTextView.setVisibility(View.GONE);
//        }

//        viewHolder.postStickView.setVisibility(View.INVISIBLE);
        //跟拍人以及内容
        String content = postInfo.getContent();
        if (!StringUtils.isEmpty(content)) {
            viewHolder.contentTextView.setText(content);
            viewHolder.contentTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.contentTextView.setVisibility(View.GONE);
        }
        //跟拍对象
        final OriginalInfo originalInfo = postInfo.getOriginalInfo();
        //标签展示
        long labelId = postInfo.getLabelId();
        String labelName = postInfo.getLabelName();
        if (postInfo.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
            //跟拍加参赛
            viewHolder.contentTextView.setVisibility(View.VISIBLE);
            viewHolder.contentTextView.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, postInfo.getScondlabelList(), content));
        } else {
            //只有跟拍
            if (postInfo.getIsOriginal() == 1 && originalInfo != null) {
                viewHolder.contentTextView.setVisibility(View.VISIBLE);
                viewHolder.contentTextView.setText(Utils.getFollowContent(originalInfo, content));
            }
            //只有参赛
            if (!StringUtils.isEmpty(labelName)) {
                viewHolder.contentTextView.setVisibility(View.VISIBLE);
                viewHolder.contentTextView.setText(Utils.getMatchContent(labelId, labelName, postInfo.getScondlabelList(), content));
            }
        }

        viewHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getContentLimit() != 2) {
                    if (postInfo.getIsOriginal() == 1 && originalInfo != null) {
                        //跳转到视频广场界面
                    } else {
                        //帖子详情

                        Intent intent = new Intent(mActivity, PostDetailActivity.class);
                        intent.putExtra(Constants.KEY.KEY_OBJECT, postInfo);
                        intent.putExtra(Constants.KEY.KEY_POSITION, position);
                        intent.putExtra(Constants.Fields.COME_IN_TYPE, 0);
                        mActivity.startActivity(intent);
                    }
                }
            }
        });
        //店铺信息
        if(postInfo.getStoreDetail()!=null){
            final StoreInfo storeInfo=postInfo.getStoreDetail().getStoreInfo();
            if(storeInfo!=null){
                viewHolder.storeLayout.setVisibility(View.VISIBLE);
                Utils.CornerImageViewDisplayByUrl(storeInfo.getStoreIcon(),viewHolder.storeIcon,3);
                if(postInfo.getIsRecurit()==1){
                    viewHolder.recruitTextView.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.recruitTextView.setVisibility(View.GONE);
                }
                viewHolder.storeNameTextView.setText(storeInfo.getTitle());
                StoreCategorySecondRecycleAdapter secondRecycleAdapter=new StoreCategorySecondRecycleAdapter();
                if(storeInfo.getFirstLabelInfo()!=null&&storeInfo.getFirstLabelInfo().getList()!=null){
                    secondRecycleAdapter.setList(storeInfo.getFirstLabelInfo().getList());
                }
                viewHolder.recycler_store_label.setAdapter(secondRecycleAdapter);
            }else{
                viewHolder.storeLayout.setVisibility(View.GONE);
            }
            viewHolder.storeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                if(storeInfo.getUserId()!=SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)){
//                    onClick.onApplyClerk(storeInfo.getStoreId());
//                }
                    //跳转到店铺详情
                    Intent intent=new Intent(mActivity,ShopDetailActivity.class);
                    intent.putExtra(Constants.Fields.STORE_ID,storeInfo.getStoreId());
                    mActivity.startActivity(intent);

                }
            });
        }else{
            viewHolder.storeLayout.setVisibility(View.GONE);
        }

        if (type == 1) {
//            viewHolder.shareNumTextView.setVisibility(View.GONE);
//            viewHolder.deleteImg.setVisibility(View.VISIBLE);
            viewHolder.shareNumTextView.setText("");
            viewHolder.shareNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.delete_icon),null,null,null);


        } else {
//            viewHolder.shareNumTextView.setVisibility(View.VISIBLE);
            viewHolder.shareNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.share_small_icon),null,null,null);
            viewHolder.shareNumTextView.setText(Utils.bigNumberToStr(postInfo.getShareNumber(), postInfo.getShareNumberStr()));

//            viewHolder.deleteImg.setVisibility(View.GONE);
        }
        viewHolder.shareNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    //删除本条帖子
                    DelectTipDialog dialog = new DelectTipDialog();
                    dialog.setTitleStr("确定删除帖子？");
                    dialog.setItemClick(new DelectTipDialog.ItemClick() {
                        @Override
                        public void itemClick() {
                            onClick.delete(postInfo.getPostId(), position);

                        }
                    });
                    dialog.show(mActivity.getSupportFragmentManager());
                }else
                {
                    //分享弹窗
                    String imgUrl = "";
                    if (postInfo.getTopicType() == 2) {
                        imgUrl = postInfo.getVideoImageUrl();
                    } else {
                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
                            imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
                        }
                    }

                    if (postInfo.getContentLimit() == 2) {
                        return;
                    }

                    ShareUtil.getInstance().showShare(mActivity, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);

                }

            }
        });
//        viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //删除本条帖子
//                DelectTipDialog dialog = new DelectTipDialog();
//                dialog.setTitleStr("确定删除帖子？");
//                dialog.setItemClick(new DelectTipDialog.ItemClick() {
//                    @Override
//                    public void itemClick() {
//                        onClick.delete(postInfo.getPostId(), position);
//
//                    }
//                });
//                dialog.show(mActivity.getSupportFragmentManager());
//            }
//        });

        viewHolder.praisNumTextView.setText(Utils.bigNumberToStr(postInfo.getPraiseCount(), postInfo.getPraiseCountStr()));

        final int isPraise = postInfo.getIsPraise();
        if (isPraise == 1) {
            //1 : 已点赞
            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
        } else {
            //0: 未点赞
            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
        }
        viewHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞 (1 点赞  2 取消点赞),
                if (onClick != null) {
                    onClick.onPraise(postInfo.getPostId(), isPraise + 1, position);
                }

            }
        });

        viewHolder.commentNumTextView.setText(Utils.bigNumberToStr(postInfo.getCommentCount(), postInfo.getCommentCountStr()));

        //隐藏条目评论
        /*viewHolder.commentNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick != null) {
                    onClick.onComment(postInfo.getBaseUser().getUserId(), postInfo.getPostId(), position);
                }
            }
        });*/
        // 图片显示区域
        viewHolder.recycler_image.setVisibility(View.GONE);
        viewHolder.linear_image_two.setVisibility(View.GONE);
        if (postInfo.getTopicType() == 2) {
            //视频
            viewHolder.linear_image_two.setVisibility(View.VISIBLE);
            viewHolder.video_play_img.setVisibility(View.VISIBLE);
            if (!refreshItem) {
                Glide.with(App.getInstance()).load(postInfo.getVideoImageUrl()).apply(options).into(viewHolder.img_first);
            }
            viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转到视频播放界面
                    Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_POST);
                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, postInfo.getPostId());
                    mActivity.startActivity(intent);
                }
            });
        } else {
            //图文
            final List<Image> imageList = postInfo.getImageList();
            if (imageList != null) {
                if (imageList.size() > 0 && imageList.size() < 2) {
                    viewHolder.linear_image_two.setVisibility(View.VISIBLE);
                    viewHolder.video_play_img.setVisibility(View.GONE);
                    if (imageList.size() == 1) {
                        if (!refreshItem) {
                            if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
                                Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
                            } else {
                                Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.img_first);
                            }
                        }
                        viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mActivity != null) {
                                    ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
                                }
                            }
                        });
                    }
                } else if (imageList.size() > 1) {
                    if (!refreshItem) {
                        PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(11));
                        viewHolder.recycler_image.setAdapter(postImageLIstAdapter);
                        postImageLIstAdapter.setOnItemListener(new ItemListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (mActivity != null) {
                                    ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
                                }
                            }
                        });
                    }
                    viewHolder.recycler_image.setVisibility(View.VISIBLE);

                }
            }
        }

        PostLabelInfoRecycleAdapter postLabelInfoRecycleAdapter=new PostLabelInfoRecycleAdapter(20);
        viewHolder.recycler_post_label.setVisibility(View.VISIBLE);
        if(postInfo.getPostLabelList()!=null&&postInfo.getPostLabelList().size()>0){
            postLabelInfoRecycleAdapter.setList(postInfo.getPostLabelList());
        }else
        {
            viewHolder.recycler_post_label.setVisibility(View.GONE);
        }
        viewHolder.recycler_post_label.setAdapter(postLabelInfoRecycleAdapter);

        if (position == 0 && type == 2) {
            viewHolder.divider.setVisibility(View.GONE);
        } else {
            viewHolder.divider.setVisibility(View.VISIBLE);
        }

//        if (list.get(position).getContentLimit() == 2) {
//            viewHolder.contentTextView.setText("发布内容只限圈内可见");
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//            viewHolder.linear_image_two.setVisibility(View.GONE);
//            viewHolder.recycler_image.setVisibility(View.GONE);
//        }

        return convertView;
    }

    class ViewHolder {
        ImageView userImg, img_first, video_play_img, alumnus_tv, userLevelTextView, storeIcon;
        TextView userNickNameTextView, userAgeTextView, timeTextView, cityTextView, locationTextView, contentTextView,recruitTextView,storeNameTextView,
                labelFirstTextView, labelSecondTextView, shareNumTextView, praisNumTextView, commentNumTextView, schoolNameTextView;
        RelativeLayout linear_image_two, labelSecondLayout,storeLayout;
        RecyclerView recycler_image,recycler_post_label,recycler_store_label;
        View divider;
        ImageView postStickView;
    }

    public interface OnClick {
        void onPraise(long postId, int type, int position);

        void onOrgain(long orgainId);

        void onComment(long toUserId, long postId, int position);

        void delete(long postId, int position);

        void onApplyClerk(long storeId);
    }


}
