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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.OrganBaseInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.OriginalInfo;
import cn.bjhdltcdn.p2plive.model.PlayInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.model.ShareInfo;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;

/**
 * 首页关注列表
 */

public class HomeAttentionRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<HomeInfo> list;
    private AppCompatActivity mActivity;
    private final int TYPE_POST = 1, TYPE_ORGANIZATION = 2, TYPE_ACTIVE = 3, TYPE_ROOM = 4, TYPE_PK = 5, TYPE_SAYLOVE = 8,
            TYPE_POST_SHARE = 11, TYPE_ORGANIZATION_SHARE = 12, TYPE_ACTIVE_SHARE = 13, TYPE_ROOM_SHARE = 14, TYPE_PK_SHARE = 15, TYPE_SAYLOVE_SHARE = 18;
    private RequestOptions options, userOptions, matchOptions;
    private OnClick onClick;
    private String defaultImg;//默认图片
    private int screenWidth;
    private int userType;//0:首页关注 1：我的帖子 4：TA的帖子
    private RequestOptions gifOptions;
    private boolean refreshItem;

    public HomeAttentionRecyclerViewAdapter(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<>(1);
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
    }

    public void setRefreshItem(boolean refreshItem) {
        this.refreshItem = refreshItem;
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public void setList(List<HomeInfo> list, String defaultImg) {
        this.list = list;
        this.defaultImg = defaultImg;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void addList(List<HomeInfo> list) {
        this.list.addAll(list);
    }


    public HomeInfo getItem(int position) {
        return list == null ? null : list.get(position);
    }


    public List<HomeInfo> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        HomeInfo homeInfo = list.get(position);
        int type = TYPE_POST;
        if (homeInfo != null) {
            switch (homeInfo.getType()) {
                case 1:
                    //帖子
                    type = TYPE_POST;
                    break;
                case 2:
                    //圈子
                    type = TYPE_ORGANIZATION;
                    break;
                case 3:
                    //活动
                    type = TYPE_ACTIVE;
                    break;
                case 4:
                    //房间
                    type = TYPE_ROOM;
                    break;
                case 5:
                    //PK挑战
                    type = TYPE_PK;
                    break;
                case 8:
                    //表白
                    type = TYPE_SAYLOVE;
                    break;
                case 11:
                    //分享帖子
                    type = TYPE_POST_SHARE;
                    break;
                case 13:
                    //分享活动
                    type = TYPE_ACTIVE_SHARE;
                    break;
                case 18:
                    //分享表白
                    type = TYPE_SAYLOVE_SHARE;
                    break;
                case 15:
                    //分享PK
                    type = TYPE_PK_SHARE;
                    break;
                case 12:
                    //分享圈子
                    type = TYPE_ORGANIZATION_SHARE;
                    break;
                case 14:
                    //分享房间
                    type = TYPE_ROOM_SHARE;
                    break;
                default:
                    break;
            }
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        BaseViewHolder baseViewHolder = null;
        if (itemType == TYPE_POST) {
            baseViewHolder = new PostHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_post_recycle_item_layout, null));
        } else if (itemType == TYPE_ORGANIZATION) {
            baseViewHolder = new OrganizationHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_origination_recycle_item_layout, null));
        } else if (itemType == TYPE_ACTIVE) {
            baseViewHolder = new ActiveHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_active_recycle_item_layout, null));
        } else if (itemType == TYPE_ROOM) {
            baseViewHolder = new RoomViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.group_video_room_list_item_layout, null));
        } else if (itemType == TYPE_PK) {
            baseViewHolder = new PKViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_pk_list_item_layout, null));
        } else if (itemType == TYPE_SAYLOVE) {
            baseViewHolder = new SayloveViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_recommend_post_list_item_layout, null));
        } else if (itemType == TYPE_SAYLOVE_SHARE) {
            baseViewHolder = new ShareSayloveViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_share_saylove_list_item_layout, null));
        } else if (itemType == TYPE_POST_SHARE) {
            baseViewHolder = new SharePostHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_share_post_list_item_layout, null));
        } else if (itemType == TYPE_ACTIVE_SHARE) {
            baseViewHolder = new ShareActiveHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_share_activeinfo_list_item_layout, null));
        } else if (itemType == TYPE_PK_SHARE) {
            baseViewHolder = new SharePKViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_share_pk_list_item_layout, null));
        } else if (itemType == TYPE_ORGANIZATION_SHARE) {
            baseViewHolder = new ShareOrganizationHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_share_organization_list_item_layout, null));
        } else if (itemType == TYPE_ROOM_SHARE) {
            baseViewHolder = new ShareRoomViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_share_room_list_item_layout, null));
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final int type = getItemViewType(position);
        if (getItemCount() > 0) {
            if (holder instanceof PostHolder) {
                final PostHolder postHolder = (PostHolder) holder;
                HomeInfo homeInfo = list.get(position);
                final PostInfo postInfo = homeInfo.getPostInfo();
                final BaseUser baseUser = homeInfo.getBaseUser();

                if (userType == 1) {
//                    postHolder.shareNumTextView.setVisibility(View.GONE);
//                    postHolder.postDeleteImg.setVisibility(View.VISIBLE);
                    postHolder.shareNumTextView.setText("");
                    postHolder.shareNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.delete_icon),null,null,null);


                } else {
//                    postHolder.shareNumTextView.setVisibility(View.VISIBLE);
//                    postHolder.postDeleteImg.setVisibility(View.GONE);
                    postHolder.shareNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.share_small_icon),null,null,null);
                    postHolder.shareNumTextView.setText(Utils.bigNumberToStr(postInfo.getShareNumber(), postInfo.getShareNumberStr()));

                }
                if (position == 0) {
                    postHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    postHolder.divider.setVisibility(View.GONE);
                }
                if (postInfo == null || baseUser == null) {
                    return;
                }
                final int isAnonymous = postInfo.getIsAnonymous();
                Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(postHolder.userImg);
                postHolder.userImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2018/6/21
                        if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //跳到用户详情(自己不可点击)
                            if (postInfo.getIsAnonymous() == 1) {
                                oneToOneCharOnClick.onClick(baseUser.getUserId());
                            } else {
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                            }
                        }
                    }
                });

//                postHolder.postDeleteImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //删除本条帖子
//                        DelectTipDialog dialog = new DelectTipDialog();
//                        dialog.setTitleStr("确定删除帖子？");
//                        dialog.setItemClick(new DelectTipDialog.ItemClick() {
//                            @Override
//                            public void itemClick() {
//                                onClick.onDelete(postInfo.getPostId(), position);
//
//                            }
//                        });
//                        dialog.show(mActivity.getSupportFragmentManager());
//                    }
//                });


//                String level = postInfo.getActiveLevel();
//                if (isAnonymous == 2) {
//                    postHolder.userLevelTextView.setVisibility(View.VISIBLE);
//                    Utils.getActiveLevel(postHolder.userLevelTextView, level);
//                } else {
//                    postHolder.userLevelTextView.setVisibility(View.GONE);
//                }

                postHolder.userAgeTextView.setText(baseUser.getAge() + "岁");
                if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) && isAnonymous == 2) {
                    //校友 并且不是自己
                    postHolder.alumnus_tv.setVisibility(View.VISIBLE);
                } else {
                    postHolder.alumnus_tv.setVisibility(View.GONE);
                }

                int sex = baseUser.getSex();
                if (sex == 1) {
                    //男性
                    postHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                } else if (sex == 2) {
                    //女性
                    postHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                }


//                postHolder.postStickView.setVisibility(View.INVISIBLE);

                postHolder.timeTextView.setText(postInfo.getAddTime());
                if ((!StringUtils.isEmpty(baseUser.getDistance()))) {
                    postHolder.locationTextView.setText(baseUser.getDistance());
                    postHolder.locationTextView.setVisibility(View.VISIBLE);
                } else {
                    postHolder.locationTextView.setVisibility(View.GONE);
                }
                if ((!StringUtils.isEmpty(baseUser.getCity()))) {
                    postHolder.cityTextView.setText(baseUser.getCity());
                    postHolder.cityTextView.setVisibility(View.VISIBLE);
                } else {
                    postHolder.cityTextView.setVisibility(View.GONE);
                }
                if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    //自己不展示位置
                    postHolder.locationTextView.setVisibility(View.GONE);
                    postHolder.cityTextView.setVisibility(View.GONE);
                }


                if (isAnonymous == 1) {
                    postHolder.userAgeTextView.setVisibility(View.VISIBLE);
                    postHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    postHolder.userAgeTextView.setText("");
                    postHolder.userAgeTextView.setPadding(0, 0, 0, 0);
                } else {
                    postHolder.userAgeTextView.setVisibility(View.VISIBLE);
                    postHolder.userAgeTextView.setPadding(0, 0, Utils.dp2px(10), 0);
                }
                postHolder.userNickNameTextView.setText(baseUser.getNickName());
                postHolder.userNickNameTextView.setVisibility(View.VISIBLE);
                //跟拍人以及内容
                String content = postInfo.getContent();
                if (!StringUtils.isEmpty(content)) {
                    postHolder.contentTextView.setText(content);
                    postHolder.contentTextView.setVisibility(View.VISIBLE);
                } else {
                    postHolder.contentTextView.setVisibility(View.GONE);
                }

                //招聘信息
                if(postInfo.getStoreDetail()!=null) {
                    final StoreInfo storeInfo = postInfo.getStoreDetail().getStoreInfo();
                    if (storeInfo != null) {
                        postHolder.storeLayout.setVisibility(View.VISIBLE);
                        Utils.CornerImageViewDisplayByUrl(storeInfo.getStoreIcon(),postHolder.storeIcon,3);
                        if(postInfo.getIsRecurit()==1){
                            postHolder.recruitTextView.setVisibility(View.VISIBLE);
                        }else{
                            postHolder.recruitTextView.setVisibility(View.GONE);
                        }
//                        postHolder.contentTextView.setPadding(0, 0, 0, 0);
                        postHolder.storeNameTextView.setText(storeInfo.getTitle());
                        StoreCategorySecondRecycleAdapter secondRecycleAdapter=new StoreCategorySecondRecycleAdapter();
                        if(storeInfo.getFirstLabelInfo()!=null&&storeInfo.getFirstLabelInfo().getList()!=null){
                            secondRecycleAdapter.setList(storeInfo.getFirstLabelInfo().getList());
                        }
                        postHolder.recycler_store_label.setAdapter(secondRecycleAdapter);
                    } else {
                        postHolder.storeLayout.setVisibility(View.GONE);
//                        postHolder.contentTextView.setPadding(0, Utils.dp2px(15), 0, 0);
                    }
                    postHolder.storeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                        if(storeInfo.getUserId()!=SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)){
//                            onClick.onApplyClerk(storeInfo.getStoreId());
//                        }
                            //跳转到店铺详情
                            Intent intent = new Intent(mActivity, ShopDetailActivity.class);
                            intent.putExtra(Constants.Fields.STORE_ID, storeInfo.getStoreId());
                            mActivity.startActivity(intent);
                        }
                    });
                }else{
                    postHolder.storeLayout.setVisibility(View.GONE);
                }

                //跟拍对象
                final OriginalInfo originalInfo = postInfo.getOriginalInfo();
                //标签展示
                long labelId = postInfo.getLabelId();
                String labelName = postInfo.getLabelName();
                if (postInfo.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
                    //跟拍加参赛
                    postHolder.contentTextView.setVisibility(View.VISIBLE);
                    postHolder.contentTextView.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, postInfo.getScondlabelList(), content));
                } else {
                    //只有跟拍
                    if (postInfo.getIsOriginal() == 1 && originalInfo != null) {
                        postHolder.contentTextView.setVisibility(View.VISIBLE);
                        postHolder.contentTextView.setText(Utils.getFollowContent(originalInfo, content));
                    }
                    //只有参赛
                    if (!StringUtils.isEmpty(labelName)) {
                        postHolder.contentTextView.setVisibility(View.VISIBLE);
                        postHolder.contentTextView.setText(Utils.getMatchContent(labelId, labelName, postInfo.getScondlabelList(), content));
                    }
                }
                postHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (postInfo.getIsOriginal() == 1 && originalInfo != null) {
                            //跳转到视频广场界面
                        } else {
                            getOnItemListener().onItemClick(v, position);
                        }
                    }
                });

                if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
                    //学校名称
                    postHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                    postHolder.schoolNameTextView.setText(baseUser.getSchoolName());
                } else {
                    postHolder.schoolNameTextView.setVisibility(View.GONE);
                }

                postHolder.shareNumTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(userType==1){
                            //删除本条帖子
                            DelectTipDialog dialog = new DelectTipDialog();
                            dialog.setTitleStr("确定删除帖子？");
                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
                                @Override
                                public void itemClick() {
                                    onClick.onDelete(postInfo.getPostId(), position);

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


                postHolder.praisNumTextView.setText(Utils.bigNumberToStr(postInfo.getPraiseCount(), postInfo.getPraiseCountStr()));

                final int isPraise = postInfo.getIsPraise();
                if (isPraise == 1) {
                    //1 : 已点赞
                    postHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
                } else {
                    //0: 未点赞
                    postHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
                }
                postHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点赞 (1 点赞  2 取消点赞),

                        if (postInfo.getContentLimit() == 2) {
                            return;
                        }

                        onClick.onPraise(postInfo.getPostId(), isPraise + 1, position, 1);
                    }
                });

                postHolder.commentNumTextView.setText(Utils.bigNumberToStr(postInfo.getCommentCount(), postInfo.getCommentCountStr()));
                // 图片显示区域
                postHolder.recycler_image.setVisibility(View.GONE);
                postHolder.linear_image_two.setVisibility(View.GONE);

                if (postInfo.getTopicType() == 2) {
                    //视频
                    postHolder.linear_image_two.setVisibility(View.VISIBLE);
                    postHolder.video_play_img.setVisibility(View.VISIBLE);
                    if (!refreshItem) {
                        Glide.with(App.getInstance()).load(postInfo.getVideoImageUrl()).apply(options).into(postHolder.img_first);
                    }
                    postHolder.img_first.setOnClickListener(new View.OnClickListener() {
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
                    final List<Image> imageList = postInfo.getImageList();
                    if (imageList != null) {
                        if (imageList.size() > 0 && imageList.size() < 2) {
                            postHolder.linear_image_two.setVisibility(View.VISIBLE);
                            postHolder.video_play_img.setVisibility(View.GONE);
                            if (imageList.size() == 1) {
                                if (!refreshItem) {
                                    if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
                                        Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(postHolder.img_first);
                                    } else {
                                        Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(postHolder.img_first);
                                    }
                                }
                                postHolder.img_first.setOnClickListener(new View.OnClickListener() {
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
                                PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, 0);
                                postHolder.recycler_image.setAdapter(postImageLIstAdapter);
                                postImageLIstAdapter.setOnItemListener(new ItemListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (mActivity != null) {
                                            ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
                                        }
                                    }
                                });
                            }

                            postHolder.recycler_image.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (postInfo.getContentLimit() == 2 && userType != 1) {
                    postHolder.contentTextView.setText("发布内容只限圈内可见");
                    postHolder.contentTextView.setVisibility(View.VISIBLE);
                    postHolder.linear_image_two.setVisibility(View.GONE);
                    postHolder.recycler_image.setVisibility(View.GONE);
                }

                PostLabelInfoRecycleAdapter postLabelInfoRecycleAdapter=new PostLabelInfoRecycleAdapter(14);
                postHolder.recycler_post_label.setVisibility(View.VISIBLE);
                if(postInfo.getPostLabelList()!=null&&postInfo.getPostLabelList().size()>0){
                    postLabelInfoRecycleAdapter.setList(postInfo.getPostLabelList());
                }else
                {
                    postHolder.recycler_post_label.setVisibility(View.GONE);
                }
                postHolder.recycler_post_label.setAdapter(postLabelInfoRecycleAdapter);

            } else if (holder instanceof OrganizationHolder) {
                final OrganizationHolder organizationHolder = (OrganizationHolder) holder;
                ImageView userImg = organizationHolder.userImg;
                ImageView orgainImg = organizationHolder.orgainImg;
                TextView userNickNameTextView = organizationHolder.userNickNameTextView;
                TextView userStateTextView = organizationHolder.userStateTextView;
                TextView timeTextView = organizationHolder.timeTextView;
                TextView locationTextView = organizationHolder.locationTextView;
                TextView useAgeTextView = organizationHolder.useAgeTextView;
                TextView orgainNameTextView = organizationHolder.orgainNameTextView;
                TextView orgainUserNumTextView = organizationHolder.orgainUserNumTextView;
                TextView orgainPostNumTextView = organizationHolder.orgainPostNumTextView;
                TextView orgainJoinTextView = organizationHolder.orgainJoinTextView;
                HomeInfo homeInfo = list.get(position);
                OrganizationInfo organizationInfo = homeInfo.getOrganizationInfo();
                final BaseUser baseUser = organizationInfo.getBaseUser();
                if (position == 0) {
                    organizationHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    organizationHolder.divider.setVisibility(View.GONE);
                }
                if (organizationInfo != null) {
                    Glide.with(mActivity).load(organizationInfo.getOrganImg()).apply(options).into(orgainImg);
                    orgainNameTextView.setText(organizationInfo.getOrganName());
                    orgainUserNumTextView.setText("成员  " + organizationInfo.getMemberCount());
                    orgainPostNumTextView.setText("帖子  " + organizationInfo.getPostCount());

                    int myUserRole = organizationInfo.getMyUserRole();
                    final int joinLimit = organizationInfo.getJoinLimit();
                    if (myUserRole == 0) {
                        //用户不在圈子
                        orgainJoinTextView.setVisibility(View.VISIBLE);
                        orgainJoinTextView.setEnabled(true);
                        if (joinLimit == 1) {
                            orgainJoinTextView.setText("加入");
                        } else {
                            orgainJoinTextView.setText("加入");
                        }
                        orgainJoinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                        orgainJoinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                    } else if (myUserRole == 4) {
                        orgainJoinTextView.setText("申请中");
                        orgainJoinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                        orgainJoinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
                        orgainJoinTextView.setEnabled(false);
                    } else {
                        //用户在圈子
                        orgainJoinTextView.setText("已加入");
                        orgainJoinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                        orgainJoinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
                        orgainJoinTextView.setEnabled(false);
                    }
                    orgainJoinTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClick.onJoinClick(position, joinLimit);
                        }
                    });
                    if (baseUser != null) {
                        Glide.with(mActivity).load(baseUser.getUserIcon()).apply(userOptions).into(userImg);
                        userImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                    //跳到用户详情
                                    mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                                }
                            }
                        });
                        userNickNameTextView.setText(baseUser.getNickName());
                        if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
                            //学校名称
                            organizationHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                            organizationHolder.schoolNameTextView.setText(baseUser.getSchoolName());
                        } else {
                            organizationHolder.schoolNameTextView.setVisibility(View.GONE);
                        }
                        if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //校友 并且不是自己
                            organizationHolder.alumnus_tv.setVisibility(View.VISIBLE);
                        } else {
                            organizationHolder.alumnus_tv.setVisibility(View.GONE);
                        }
                        int sex = baseUser.getSex();
                        if (sex == 1) {
                            //男性
                            useAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                        } else if (sex == 2) {
                            //女性
                            useAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                        }
                        useAgeTextView.setText(baseUser.getAge() + "岁");
                        int userRole = organizationInfo.getUserRole();
                        if (userRole == 1) {
                            userStateTextView.setText("创建了圈子");
                        } else {
                            userStateTextView.setText("加入了圈子");
                        }
                        timeTextView.setText(organizationInfo.getUpdateTime());
                        if (!StringUtils.isEmpty(baseUser.getDistance())) {
                            locationTextView.setText(baseUser.getDistance());
                            locationTextView.setVisibility(View.VISIBLE);
                        } else {
                            locationTextView.setVisibility(View.GONE);
                        }
                        if (!StringUtils.isEmpty(baseUser.getCity())) {
                            organizationHolder.cityTextView.setText(baseUser.getCity());
                            organizationHolder.cityTextView.setVisibility(View.VISIBLE);
                        } else {
                            organizationHolder.cityTextView.setVisibility(View.GONE);
                        }
                        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //自己不展示位置
                            locationTextView.setVisibility(View.GONE);
                            organizationHolder.cityTextView.setVisibility(View.GONE);
                        }

                    }
                }
            } else if (holder instanceof ActiveHolder) {
                final ActiveHolder activeHolder = (ActiveHolder) holder;
                HomeInfo homeInfo = list.get(position);
                ActivityInfo activityInfo = homeInfo.getActivityInfo();
                final BaseUser baseUser = activityInfo.getBaseUser();
                if (position == 0) {
                    activeHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    activeHolder.divider.setVisibility(View.GONE);
                }
                if (activityInfo != null) {
                    if (baseUser != null) {
                        activeHolder.userName_tv.setText(baseUser.getNickName());
                        if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
                            //学校名称
                            activeHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                            activeHolder.schoolNameTextView.setText(baseUser.getSchoolName());
                        } else {
                            activeHolder.schoolNameTextView.setVisibility(View.GONE);
                        }
                        if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //校友 并且不是自己
                            activeHolder.alumnusImg.setVisibility(View.VISIBLE);
                        } else {
                            activeHolder.alumnusImg.setVisibility(View.GONE);
                        }
                        Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(activeHolder.userImg);
                        activeHolder.userImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                    //跳到用户详情
                                    mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                                }
                            }
                        });
                        if (baseUser.getSex() == 1) {
                            activeHolder.userSexImg.setImageResource(R.mipmap.boy_icon);
                        } else {
                            activeHolder.userSexImg.setImageResource(R.mipmap.girl_icon);
                        }
                        //用户距离
                        String userDiatance = baseUser.getDistance();
                        if (!StringUtils.isEmpty(userDiatance)) {
                            activeHolder.user_location_tv.setText(userDiatance);
                            activeHolder.user_location_tv.setVisibility(View.VISIBLE);
                        } else {
                            activeHolder.user_location_tv.setVisibility(View.GONE);
                        }
                        if (!StringUtils.isEmpty(baseUser.getCity())) {
                            activeHolder.city_tv.setText(baseUser.getCity());
                            activeHolder.city_tv.setVisibility(View.VISIBLE);
                        } else {
                            activeHolder.city_tv.setVisibility(View.GONE);
                        }
                        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //自己不展示位置
                            activeHolder.user_location_tv.setVisibility(View.GONE);
                            activeHolder.city_tv.setVisibility(View.GONE);
                        }

                    }
                    //发布活动时间
                    activeHolder.publishTimeTextView.setText(activityInfo.getAddTime());
                    //活动距离
                    String activeDiatance = activityInfo.getDistance();
                    if ((!StringUtils.isEmpty(activeDiatance)) && activityInfo.getType() == 1) {
                        activeHolder.activeDistanceView.setText("活动距你：" + activeDiatance);
                        activeHolder.activeDistanceView.setVisibility(View.VISIBLE);
                    } else {
                        activeHolder.activeDistanceView.setVisibility(View.GONE);
                    }
                    int status = activityInfo.getStatus();
                    if (status == 1) {
                        activeHolder.activeStateImg.setImageResource(R.mipmap.active_status_enable_icon);
                    } else {
                        activeHolder.activeStateImg.setImageResource(R.mipmap.active_status_unenable_icon);
                    }
                    int userRole = activityInfo.getUserRole();//用户角色(1组织用户,2普通用户)用户不在活动中返回0,
                    if (userRole == 1) {
//                        activeHolder.activeType_tv.setVisibility(View.VISIBLE);
                        activeHolder.activeType_tv.setText("发起了活动");
                    } else if (userRole == 2) {
//                        activeHolder.activeType_tv.setVisibility(View.VISIBLE);
                        activeHolder.activeType_tv.setText("参加了活动");
                    } else {
                        activeHolder.activeType_tv.setVisibility(View.GONE);
                    }

                    List<HobbyInfo> hobbyInfoList = activityInfo.getHobbyList();
                    if (hobbyInfoList != null) {
                        ActiveListHobbyRecyclerViewAdapter hobbyRecyclerViewAdapter = new ActiveListHobbyRecyclerViewAdapter(mActivity);
                        hobbyRecyclerViewAdapter.setList(hobbyInfoList);
                        activeHolder.hobbyRecycleView.setAdapter(hobbyRecyclerViewAdapter);
                    }
                    if (activityInfo.getOrganList() != null) {
                        List<OrganizationInfo> organizationInfoList = new ArrayList<OrganizationInfo>();
                        organizationInfoList.addAll(activityInfo.getOrganList());
                        ;
                        if (activityInfo.getOrganList().size() > 3) {
                            organizationInfoList.remove(organizationInfoList.size() - 1);
                            activeHolder.orgainzationMoreImageView.setVisibility(View.VISIBLE);
                        } else {
                            activeHolder.orgainzationMoreImageView.setVisibility(View.INVISIBLE);
                        }
                        final ActiveListOrganizationRecyclerViewAdapter activeListOrganizationRecyclerViewAdapter = new ActiveListOrganizationRecyclerViewAdapter(mActivity);
                        activeListOrganizationRecyclerViewAdapter.setList(organizationInfoList);
                        activeHolder.orgainzationRecycleView.setAdapter(activeListOrganizationRecyclerViewAdapter);
                        activeListOrganizationRecyclerViewAdapter.setOnItemListener(new ItemListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //跳转到圈子详情
                                OrganizationInfo organizationInfo = activeListOrganizationRecyclerViewAdapter.getItem(position);
                            }
                        });
                    }

                    if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
                        Glide.with(App.getInstance()).load(activityInfo.getImageList().get(0).getThumbnailUrl()).apply(options).into(activeHolder.activeImg);
                    } else {
                        Glide.with(App.getInstance()).load(defaultImg).apply(options).into(activeHolder.activeImg);
                    }
                    activeHolder.activeContentTextView.setText(activityInfo.getTheme());
                    //活动时间
                    activeHolder.activeTimeTextView.setText("活动时间：" + activityInfo.getActivityTime());

                    ActivityLocationInfo activityLocationInfo = activityInfo.getLocationInfo();
                    if (activityLocationInfo != null && activityInfo.getType() == 1) {
                        activeHolder.activePlaceTextView.setText("活动地点：" + activityInfo.getLocationInfo().getAddr());
                        activeHolder.activePlaceTextView.setVisibility(View.VISIBLE);
                    } else {
                        activeHolder.activePlaceTextView.setVisibility(View.INVISIBLE);
                    }

                    activeHolder.activeApplyNumTextView.setText(activityInfo.getJoinNumber() + "人参加");
                    if (activityInfo.getActivityPrice() >= 0) {
                        activeHolder.activeCharge_tv.setText(activityInfo.getActivityPrice() + "元/人");
                        activeHolder.activeCharge_tv.setVisibility(View.VISIBLE);
                    } else {
                        activeHolder.activeCharge_tv.setVisibility(View.INVISIBLE);
                    }

                }
            } else if (holder instanceof RoomViewHolder) {
                final RoomViewHolder roomHolder = (RoomViewHolder) holder;
                HomeInfo homeInfo = list.get(position);
                RoomInfo roomInfo = homeInfo.getRoomInfo();
                if (position == 0) {
                    roomHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    roomHolder.divider.setVisibility(View.GONE);
                }
                if (roomInfo != null) {
//                    roomHolder.userStatusView.setVisibility(View.VISIBLE);
                    roomHolder.roomNameView.setText(roomInfo.getRoomName());
                    Glide.with(App.getInstance()).load(roomInfo.getBackgroundUrl()).apply(options).into(roomHolder.roomImageView);
                    roomHolder.userNumView.setText(roomInfo.getOnlineNumber() + "人");
                    //房间标签
                    List<String> keywordInfoList = roomInfo.getLabelList();
                    if (keywordInfoList != null && keywordInfoList.size() > 0) {
                        roomHolder.tagLayout.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) roomHolder.userLayout.getLayoutParams();
                        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        StringBuffer labelString = new StringBuffer();
                        for (int i = 0; i < keywordInfoList.size(); i++) {
                            String info = keywordInfoList.get(i);
                            labelString.append(info + "    ");
                        }
                        roomHolder.tagListView.setText(labelString.toString());
                    } else {
                        roomHolder.tagLayout.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) roomHolder.userLayout.getLayoutParams();
                        layoutParams.height = Utils.dp2px(40);
                    }
                    final BaseUser baseUser = roomInfo.getBaseUser();
                    if (baseUser != null) {
                        Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(roomHolder.userImageView);
                        roomHolder.userImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                    //跳到用户详情
                                    mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                                }
                            }
                        });
                        roomHolder.userNameView.setText(baseUser.getNickName());
                        if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
                            //学校名称
                            roomHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                            roomHolder.schoolNameTextView.setText(baseUser.getSchoolName());
                        } else {
                            roomHolder.schoolNameTextView.setVisibility(View.GONE);
                        }
                        roomHolder.userAgeView.setText(baseUser.getAge() + "岁");
                        if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //校友 并且不是自己
                            roomHolder.alumnusView.setVisibility(View.VISIBLE);
                        } else {
                            roomHolder.alumnusView.setVisibility(View.GONE);
                        }
                        int sex = baseUser.getSex();
                        if (sex == 1) {
                            //男性
                            roomHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                        } else if (sex == 2) {
                            //女性
                            roomHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                        }
                        String diatance = baseUser.getDistance();
                        if (!StringUtils.isEmpty(diatance)) {
                            roomHolder.distanceView.setText(diatance);
                            roomHolder.distanceView.setVisibility(View.VISIBLE);
                        } else {
                            roomHolder.distanceView.setVisibility(View.INVISIBLE);
                        }
                        if (!StringUtils.isEmpty(baseUser.getCity())) {
                            roomHolder.cityView.setText(baseUser.getCity());
                            roomHolder.cityView.setVisibility(View.VISIBLE);
                        } else {
                            roomHolder.cityView.setVisibility(View.GONE);
                        }
                        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //自己不展示位置
                            roomHolder.distanceView.setVisibility(View.INVISIBLE);
                            roomHolder.cityView.setVisibility(View.GONE);
                        }

                    }
                }
            } else if (holder instanceof PKViewHolder) {
                final PKViewHolder pkHolder = (PKViewHolder) holder;
                HomeInfo homeInfo = list.get(position);
                final PlayInfo playInfo = homeInfo.getPlayInfo();
                pkHolder.topLayout.setVisibility(View.GONE);
                if (position == 0) {
                    pkHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    pkHolder.divider.setVisibility(View.GONE);
                }
                if (playInfo != null) {
                    pkHolder.timeView.setText(playInfo.getAddTime());
                    pkHolder.pkTitleView.setText(playInfo.getLaunchPlay().getTitle());
                    Glide.with(App.getInstance()).load(playInfo.getVideoImageUrl()).apply(options).into(pkHolder.pkVideoImageView);
                    pkHolder.pkContentVeiw.setText(playInfo.getTitle());
//                    pkHolder.pkStatusView.setVisibility(View.VISIBLE);
                    if (playInfo.getType() == 1) {
                        pkHolder.pkStatusView.setText("发布了PK挑战");
                    } else {
                        pkHolder.pkStatusView.setText("参与了PK挑战");
                    }
                    final BaseUser baseUser = playInfo.getBaseUser();
                    if (baseUser != null) {
                        Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(pkHolder.userImageView);
                        pkHolder.userImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                    //跳到用户详情
                                    mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                                }
                            }
                        });
                        pkHolder.userNameView.setText(baseUser.getNickName());
                        if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
                            //学校名称
                            pkHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                            pkHolder.schoolNameTextView.setText(baseUser.getSchoolName());
                        } else {
                            pkHolder.schoolNameTextView.setVisibility(View.GONE);
                        }
                        pkHolder.userAgeView.setText(baseUser.getAge() + "岁");
                        if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //校友 并且不是自己
                            pkHolder.alumnusView.setVisibility(View.VISIBLE);
                        } else {
                            pkHolder.alumnusView.setVisibility(View.GONE);
                        }
                        int sex = baseUser.getSex();
                        if (sex == 1) {
                            //男性
                            pkHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                        } else if (sex == 2) {
                            //女性
                            pkHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                        }
                        String distance = baseUser.getDistance();
                        if (!StringUtils.isEmpty(distance)) {
                            pkHolder.distanceView.setText(distance);
                            pkHolder.distanceView.setVisibility(View.VISIBLE);
                        } else {
                            pkHolder.distanceView.setVisibility(View.GONE);
                        }
                        if (!StringUtils.isEmpty(baseUser.getCity())) {
                            pkHolder.cityView.setText(baseUser.getCity());
                            pkHolder.cityView.setVisibility(View.VISIBLE);
                        } else {
                            pkHolder.cityView.setVisibility(View.GONE);
                        }
                        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //自己不展示位置
                            pkHolder.distanceView.setVisibility(View.GONE);
                            pkHolder.cityView.setVisibility(View.GONE);
                        }

                    }
                }
            } else if (holder instanceof SayloveViewHolder) {
                final SayloveViewHolder sayloveHolder = (SayloveViewHolder) holder;
//                sayloveHolder.userLevelTextView.setVisibility(View.GONE);
                HomeInfo homeInfo = list.get(position);
                final SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
                final BaseUser baseUser = sayLoveInfo.getBaseUser();
                if (sayLoveInfo == null || baseUser == null) {
                    return;
                }
                final int isAnonymous = sayLoveInfo.getIsAnonymous();
                Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(sayloveHolder.userImg);
                sayloveHolder.userImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //跳到用户详情(自己和匿名不可点击)
                            if (sayLoveInfo.getIsAnonymous() == 1) {
                                oneToOneCharOnClick.onClick(baseUser.getUserId());
                            } else {
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                            }
                        }
                    }
                });
                sayloveHolder.userNickNameTextView.setText(baseUser.getNickName());
                if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
                    //学校名称
                    sayloveHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                    sayloveHolder.schoolNameTextView.setText(baseUser.getSchoolName());
                } else {
                    sayloveHolder.schoolNameTextView.setVisibility(View.GONE);
                }
                if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    //校友 并且不是自己
                    sayloveHolder.alumnus_tv.setVisibility(View.VISIBLE);
                } else {
                    sayloveHolder.alumnus_tv.setVisibility(View.GONE);
                }

                sayloveHolder.userAgeTextView.setText(baseUser.getAge() + "岁");
                int sex = baseUser.getSex();
                if (sex == 1) {
                    //男性
                    sayloveHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                } else if (sex == 2) {
                    //女性
                    sayloveHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                }


                sayloveHolder.timeTextView.setText(sayLoveInfo.getAddTime());
                String diatance = baseUser.getDistance();
                if ((!StringUtils.isEmpty(diatance))) {
                    sayloveHolder.locationTextView.setText(diatance);
                    sayloveHolder.locationTextView.setVisibility(View.VISIBLE);
                } else {
                    sayloveHolder.locationTextView.setVisibility(View.GONE);
                }

                if ((!StringUtils.isEmpty(baseUser.getCity()))) {
                    sayloveHolder.cityTextView.setText(baseUser.getCity());
                    sayloveHolder.cityTextView.setVisibility(View.VISIBLE);
                } else {
                    sayloveHolder.cityTextView.setVisibility(View.GONE);
                }
                if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    //自己不展示位置
                    sayloveHolder.locationTextView.setVisibility(View.GONE);
                    sayloveHolder.cityTextView.setVisibility(View.GONE);
                }

                //跟拍人以及内容
                String content = sayLoveInfo.getContent();
                if (!StringUtils.isEmpty(content)) {
                    sayloveHolder.contentTextView.setText(content);
                    sayloveHolder.contentTextView.setVisibility(View.VISIBLE);
                } else {
                    sayloveHolder.contentTextView.setVisibility(View.GONE);
                }
                //跟拍对象
                final OriginalInfo originalInfo = sayLoveInfo.getOriginalInfo();
                //标签展示
                long labelId = sayLoveInfo.getLabelId();
                String labelName = sayLoveInfo.getLabelName();
                if (sayLoveInfo.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
                    //跟拍加参赛
                    sayloveHolder.contentTextView.setVisibility(View.VISIBLE);
                    sayloveHolder.contentTextView.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, sayLoveInfo.getScondlabelList(), content));

                } else {
                    //只有跟拍
                    if (sayLoveInfo.getIsOriginal() == 1 && originalInfo != null) {
                        sayloveHolder.contentTextView.setVisibility(View.VISIBLE);
                        sayloveHolder.contentTextView.setText(Utils.getFollowContent(originalInfo, content));
                    }
                    //只有参赛
                    if (!StringUtils.isEmpty(labelName)) {
                        sayloveHolder.contentTextView.setVisibility(View.VISIBLE);
                        sayloveHolder.contentTextView.setText(Utils.getMatchContent(labelId, labelName, sayLoveInfo.getScondlabelList(), content));
                    }
                }
                sayloveHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sayLoveInfo.getIsOriginal() == 1 && originalInfo != null) {
                            //跳转到视频广场界面
                        } else {
                            getOnItemListener().onItemClick(v, position);
                        }
                    }
                });
                if (sayLoveInfo.getIsAnonymous() != 1) {
                    sayloveHolder.userAgeTextView.setVisibility(View.VISIBLE);
                    sayloveHolder.userAgeTextView.setPadding(0, 0, Utils.dp2px(10), 0);
                } else {
                    sayloveHolder.userAgeTextView.setVisibility(View.VISIBLE);
                    sayloveHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    sayloveHolder.userAgeTextView.setText("");
                    sayloveHolder.userAgeTextView.setPadding(0, 0, 0, 0);
                }
//                sayloveHolder.type_tip_text.setVisibility(View.VISIBLE);
//                sayloveHolder.type_tip_text.setText("校园表白墙");
//                sayloveHolder.type_tip_text.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    }
//                });
//                sayloveHolder.labelFirstTextView.setVisibility(View.GONE);
//                sayloveHolder.labelSecondLayout.setVisibility(View.GONE);
                sayloveHolder.shareNumTextView.setText(Utils.bigNumberToStr(sayLoveInfo.getShareNumber(), sayLoveInfo.getShareNumberStr()));
                sayloveHolder.shareNumTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //分享弹窗
                        String imgUrl = "";
                        if (sayLoveInfo.getConfessionType() == 2) {
                            imgUrl = sayLoveInfo.getVideoImageUrl();
                        } else {
                            if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
                                imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
                            }
                        }
                        ShareUtil.getInstance().showShare(mActivity, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);

                    }
                });

                sayloveHolder.praisNumTextView.setText(Utils.bigNumberToStr(sayLoveInfo.getPraiseCount(), sayLoveInfo.getPraiseCountStr()));
                final int isPraise = sayLoveInfo.getIsPraise();
                if (isPraise == 1) {
                    //1 : 已点赞
                    sayloveHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
                } else {
                    //0: 未点赞
                    sayloveHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
                }
                sayloveHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点赞 (1 点赞  2 取消点赞),
                        onClick.onPraise(sayLoveInfo.getSayLoveId(), isPraise + 1, position, 2);
                    }
                });
                sayloveHolder.commentNumTextView.setText(Utils.bigNumberToStr(sayLoveInfo.getCommentCount(), sayLoveInfo.getCommentCountStr()));
                // 图片显示区域
                sayloveHolder.recycler_image.setVisibility(View.GONE);
                sayloveHolder.linear_image_two.setVisibility(View.GONE);
                if (sayLoveInfo.getConfessionType() == 2) {
                    sayloveHolder.linear_image_two.setVisibility(View.VISIBLE);
                    sayloveHolder.video_play_img.setVisibility(View.VISIBLE);
                    if (!refreshItem) {
                        Glide.with(App.getInstance()).load(sayLoveInfo.getVideoImageUrl()).apply(options).into(sayloveHolder.img_first);
                    }
                    sayloveHolder.img_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //跳转到视频播放界面
                            Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
                            intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_SAYLOVE);
                            intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, sayLoveInfo.getSayLoveId());
                            mActivity.startActivity(intent);
                        }
                    });
                } else {
                    final List<Image> imageList = sayLoveInfo.getImageList();
                    if (imageList != null) {
                        if (imageList.size() > 0 && imageList.size() <= 1) {

                            sayloveHolder.linear_image_two.setVisibility(View.VISIBLE);
                            sayloveHolder.video_play_img.setVisibility(View.GONE);
                            if (imageList.size() == 1) {
                                if (!refreshItem) {
                                    if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
                                        Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(sayloveHolder.img_first);
                                    } else {
                                        Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(sayloveHolder.img_first);
                                    }
                                }
                                sayloveHolder.img_first.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (mActivity != null) {
                                            ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
                                        }
                                    }
                                });
                            }
                        } else if (imageList.size() > 1) {
                            sayloveHolder.linear_image_two.setVisibility(View.GONE);
                            if (!refreshItem) {
                                PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
                                sayloveHolder.recycler_image.setAdapter(postImageLIstAdapter);
                                postImageLIstAdapter.setOnItemListener(new ItemListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (mActivity != null) {
                                            ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
                                        }
                                    }
                                });
                            }
                            sayloveHolder.recycler_image.setVisibility(View.VISIBLE);
                        }
                    }
                }


                if (position == 0) {
                    sayloveHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    sayloveHolder.divider.setVisibility(View.GONE);
                }
            } else if (holder instanceof ShareSayloveViewHolder) {
                final ShareSayloveViewHolder shareSayloveViewHolder = (ShareSayloveViewHolder) holder;
                HomeInfo homeInfo = list.get(position);
                if (userType == 1) {
                    shareSayloveViewHolder.deleteShareImg.setVisibility(View.VISIBLE);
                } else {
                    shareSayloveViewHolder.deleteShareImg.setVisibility(View.GONE);
                }
                final BaseUser shareBaseUser = homeInfo.getBaseUser();
                if (shareBaseUser == null) {
                    return;
                } else {
                    Glide.with(App.getInstance()).load(shareBaseUser.getUserIcon()).apply(userOptions).into(shareSayloveViewHolder.userImg);
                    shareSayloveViewHolder.userImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, shareBaseUser));
                            }
                        }
                    });
                    shareSayloveViewHolder.userNickNameTextView.setText(shareBaseUser.getNickName());
                    if (!StringUtils.isEmpty(shareBaseUser.getSchoolName())) {
                        //学校名称
                        shareSayloveViewHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                        shareSayloveViewHolder.schoolNameTextView.setText(shareBaseUser.getSchoolName());
                    } else {
                        shareSayloveViewHolder.schoolNameTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getIsSchoolmate() == 2 && shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //校友 并且不是自己
                        shareSayloveViewHolder.alumnus_tv.setVisibility(View.VISIBLE);
                    } else {
                        shareSayloveViewHolder.alumnus_tv.setVisibility(View.GONE);
                    }
                    shareSayloveViewHolder.userAgeTextView.setText(shareBaseUser.getAge() + "岁");
                    int sex = shareBaseUser.getSex();
                    if (sex == 1) {
                        //男性
                        shareSayloveViewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                    } else if (sex == 2) {
                        //女性
                        shareSayloveViewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                    }

                    String diatance = shareBaseUser.getDistance();
                    if ((!StringUtils.isEmpty(diatance))) {
                        shareSayloveViewHolder.locationTextView.setText(diatance);
                        shareSayloveViewHolder.locationTextView.setVisibility(View.VISIBLE);
                    } else {
                        shareSayloveViewHolder.locationTextView.setVisibility(View.GONE);
                    }

                    if ((!StringUtils.isEmpty(shareBaseUser.getCity()))) {
                        shareSayloveViewHolder.cityTextView.setText(shareBaseUser.getCity());
                        shareSayloveViewHolder.cityTextView.setVisibility(View.VISIBLE);
                    } else {
                        shareSayloveViewHolder.cityTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //自己不展示位置
                        shareSayloveViewHolder.locationTextView.setVisibility(View.GONE);
                        shareSayloveViewHolder.cityTextView.setVisibility(View.GONE);
                    }
                    shareSayloveViewHolder.tip_type_tv.setText("分享了表白");
                }

                final ShareInfo shareInfo = homeInfo.getShareInfo();
                if (shareInfo == null) {
                    return;
                } else {
                    shareSayloveViewHolder.timeTextView.setText(shareInfo.getAddTime());

                    if (StringUtils.isEmpty(shareInfo.getContent())) {
                        shareSayloveViewHolder.shareContentTextView.setVisibility(View.GONE);
                    } else {
                        shareSayloveViewHolder.shareContentTextView.setText(shareInfo.getContent());
                        shareSayloveViewHolder.shareContentTextView.setVisibility(View.VISIBLE);
                    }

                    shareSayloveViewHolder.deleteShareImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DelectTipDialog dialog = new DelectTipDialog();
                            dialog.setTitleStr("确定删除分享？");
                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
                                @Override
                                public void itemClick() {
                                    onClick.onDeleteShare(shareInfo.getShareId(), position);
                                }
                            });
                            dialog.show(mActivity.getSupportFragmentManager());
                        }
                    });
                }

                final SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
                if (sayLoveInfo == null) {
                    shareSayloveViewHolder.shareDeleteLayout.setVisibility(View.VISIBLE);
                    shareSayloveViewHolder.deleteTextView.setText("该表白已被原作者删除");
                    shareSayloveViewHolder.rootLayout.setVisibility(View.GONE);
                    return;
                } else {
                    shareSayloveViewHolder.shareDeleteLayout.setVisibility(View.GONE);
                    shareSayloveViewHolder.rootLayout.setVisibility(View.VISIBLE);
                }

                final BaseUser baseUser = sayLoveInfo.getToBaseUser();
                final int isAnonymous = sayLoveInfo.getIsAnonymous();
                if (baseUser == null) {
                    return;
                } else {
                    shareSayloveViewHolder.fromUserNickNameTextView.setText("@" + baseUser.getNickName());
                    shareSayloveViewHolder.fromUserNickNameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) && isAnonymous == 2) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                            }
                        }
                    });
                }

                if (StringUtils.isEmpty(sayLoveInfo.getContent())) {
                    shareSayloveViewHolder.contentTextView.setVisibility(View.GONE);
                } else {
                    shareSayloveViewHolder.contentTextView.setText(sayLoveInfo.getContent());
                    shareSayloveViewHolder.contentTextView.setVisibility(View.VISIBLE);
                }
                shareSayloveViewHolder.shareNumTextView.setText(Utils.bigNumberToStr(sayLoveInfo.getShareNumber(), sayLoveInfo.getShareNumberStr()));
                shareSayloveViewHolder.shareNumTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //分享弹窗
                        String imgUrl = "";
                        if (sayLoveInfo.getConfessionType() == 2) {
                            imgUrl = sayLoveInfo.getVideoImageUrl();
                        } else {
                            if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
                                imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
                            }
                        }
                        ShareUtil.getInstance().showShare(mActivity, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);

                    }
                });
                shareSayloveViewHolder.praisNumTextView.setText(Utils.bigNumberToStr(sayLoveInfo.getPraiseCount(), sayLoveInfo.getPraiseCountStr()));
                final int isPraise = sayLoveInfo.getIsPraise();
                if (isPraise == 1) {
                    //1 : 已点赞
                    shareSayloveViewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
                } else {
                    //0: 未点赞
                    shareSayloveViewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
                }
                shareSayloveViewHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点赞 (1 点赞  2 取消点赞),
                        onClick.onPraise(sayLoveInfo.getSayLoveId(), isPraise + 1, position, 2);
                    }
                });
                shareSayloveViewHolder.commentNumTextView.setText(Utils.bigNumberToStr(sayLoveInfo.getCommentCount(), sayLoveInfo.getCommentCountStr()));
                // 图片显示区域
                shareSayloveViewHolder.recycler_image.setVisibility(View.GONE);
                shareSayloveViewHolder.linear_image_two.setVisibility(View.GONE);
                if (sayLoveInfo.getConfessionType() == 2) {
                    shareSayloveViewHolder.linear_image_two.setVisibility(View.VISIBLE);
                    shareSayloveViewHolder.video_play_img.setVisibility(View.VISIBLE);
                    if (!refreshItem) {
                        Glide.with(App.getInstance()).load(sayLoveInfo.getVideoImageUrl()).apply(options).into(shareSayloveViewHolder.img_first);
                    }
                    shareSayloveViewHolder.img_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //跳转到视频播放界面
                            Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
                            intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_SAYLOVE);
                            intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, sayLoveInfo.getSayLoveId());
                            mActivity.startActivity(intent);
                        }
                    });
                } else {
                    final List<Image> imageList = sayLoveInfo.getImageList();
                    if (imageList != null) {
                        if (imageList.size() > 0 && imageList.size() <= 1) {

                            shareSayloveViewHolder.linear_image_two.setVisibility(View.VISIBLE);
                            shareSayloveViewHolder.video_play_img.setVisibility(View.GONE);
                            if (imageList.size() == 1) {
                                if (!refreshItem) {
                                    if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
                                        Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(shareSayloveViewHolder.img_first);
                                    } else {
                                        Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(shareSayloveViewHolder.img_first);
                                    }
                                }
                                shareSayloveViewHolder.img_first.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (mActivity != null) {
                                            ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
                                        }
                                    }
                                });
                            }
                        } else if (imageList.size() > 1) {
                            shareSayloveViewHolder.linear_image_two.setVisibility(View.GONE);
                            if (!refreshItem) {
                                PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
                                shareSayloveViewHolder.recycler_image.setAdapter(postImageLIstAdapter);
                                postImageLIstAdapter.setOnItemListener(new ItemListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (mActivity != null) {
                                            ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
                                        }
                                    }
                                });
                            }
                            shareSayloveViewHolder.recycler_image.setVisibility(View.VISIBLE);
                        }
                    }
                }


                if (position == 0) {
                    shareSayloveViewHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    shareSayloveViewHolder.divider.setVisibility(View.GONE);
                }
            } else if (holder instanceof ShareActiveHolder) {
                final ShareActiveHolder shareActiveHolder = (ShareActiveHolder) holder;
                HomeInfo homeInfo = list.get(position);
                if (userType == 1) {
                    shareActiveHolder.deleteShareImg.setVisibility(View.VISIBLE);
                } else {
                    shareActiveHolder.deleteShareImg.setVisibility(View.GONE);
                }
                final BaseUser shareBaseUser = homeInfo.getBaseUser();
                if (shareBaseUser == null) {
                    return;
                } else {
                    Glide.with(App.getInstance()).load(shareBaseUser.getUserIcon()).apply(userOptions).into(shareActiveHolder.userImg);
                    shareActiveHolder.userImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, shareBaseUser));
                            }
                        }
                    });
                    shareActiveHolder.userNickNameTextView.setText(shareBaseUser.getNickName());
                    if (!StringUtils.isEmpty(shareBaseUser.getSchoolName())) {
                        //学校名称
                        shareActiveHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                        shareActiveHolder.schoolNameTextView.setText(shareBaseUser.getSchoolName());
                    } else {
                        shareActiveHolder.schoolNameTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getIsSchoolmate() == 2 && shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //校友 并且不是自己
                        shareActiveHolder.alumnus_tv.setVisibility(View.VISIBLE);
                    } else {
                        shareActiveHolder.alumnus_tv.setVisibility(View.GONE);
                    }
                    shareActiveHolder.userAgeTextView.setText(shareBaseUser.getAge() + "岁");
                    int sex = shareBaseUser.getSex();
                    if (sex == 1) {
                        //男性
                        shareActiveHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                    } else if (sex == 2) {
                        //女性
                        shareActiveHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                    }

                    String diatance = shareBaseUser.getDistance();
                    if ((!StringUtils.isEmpty(diatance))) {
                        shareActiveHolder.locationTextView.setText(diatance);
                        shareActiveHolder.locationTextView.setVisibility(View.VISIBLE);
                    } else {
                        shareActiveHolder.locationTextView.setVisibility(View.GONE);
                    }

                    if ((!StringUtils.isEmpty(shareBaseUser.getCity()))) {
                        shareActiveHolder.cityTextView.setText(shareBaseUser.getCity());
                        shareActiveHolder.cityTextView.setVisibility(View.VISIBLE);
                    } else {
                        shareActiveHolder.cityTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //自己不展示位置
                        shareActiveHolder.locationTextView.setVisibility(View.GONE);
                        shareActiveHolder.cityTextView.setVisibility(View.GONE);
                    }
                    shareActiveHolder.tip_type_tv.setText("分享了活动");
                }

                final ShareInfo shareInfo = homeInfo.getShareInfo();
                if (shareInfo == null) {
                    return;
                } else {
                    shareActiveHolder.timeTextView.setText(shareInfo.getAddTime());
                    if (StringUtils.isEmpty(shareInfo.getContent())) {
                        shareActiveHolder.shareContentTextView.setVisibility(View.GONE);
                    } else {
                        shareActiveHolder.shareContentTextView.setText(shareInfo.getContent());
                        shareActiveHolder.shareContentTextView.setVisibility(View.VISIBLE);
                    }
                    shareActiveHolder.deleteShareImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DelectTipDialog dialog = new DelectTipDialog();
                            dialog.setTitleStr("确定删除分享？");
                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
                                @Override
                                public void itemClick() {
                                    onClick.onDeleteShare(shareInfo.getShareId(), position);
                                }
                            });
                            dialog.show(mActivity.getSupportFragmentManager());
                        }
                    });
                }


                ActivityInfo activityInfo = homeInfo.getActivityInfo();
                if (activityInfo == null) {
                    shareActiveHolder.shareDeleteLayout.setVisibility(View.VISIBLE);
                    shareActiveHolder.deleteTextView.setText("该活动已被原作者删除");
                    shareActiveHolder.rootLayout.setVisibility(View.GONE);
                    return;
                } else {
                    shareActiveHolder.shareDeleteLayout.setVisibility(View.GONE);
                    shareActiveHolder.rootLayout.setVisibility(View.VISIBLE);
                }

                final BaseUser baseUser = activityInfo.getToBaseUser();
                if (baseUser == null) {
                    return;
                } else {
                    shareActiveHolder.fromUserNickNameTextView.setText("@" + baseUser.getNickName());
                    shareActiveHolder.fromUserNickNameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                            }
                        }
                    });
                }

                //活动距离
                String activeDiatance = activityInfo.getDistance();
                if ((!StringUtils.isEmpty(activeDiatance)) && activityInfo.getType() == 1) {
                    shareActiveHolder.activeDistanceView.setText("活动距你：" + activeDiatance);
                    shareActiveHolder.activeDistanceView.setVisibility(View.VISIBLE);
                } else {
                    shareActiveHolder.activeDistanceView.setVisibility(View.GONE);
                }
                int status = activityInfo.getStatus();
                if (status == 1) {
                    shareActiveHolder.activeStateImg.setImageResource(R.mipmap.active_status_enable_icon);
                } else {
                    shareActiveHolder.activeStateImg.setImageResource(R.mipmap.active_status_unenable_icon);
                }
                List<HobbyInfo> hobbyInfoList = activityInfo.getHobbyList();
                if (hobbyInfoList != null) {
                    ActiveListHobbyRecyclerViewAdapter hobbyRecyclerViewAdapter = new ActiveListHobbyRecyclerViewAdapter(mActivity);
                    hobbyRecyclerViewAdapter.setList(hobbyInfoList);
                    shareActiveHolder.hobbyRecycleView.setAdapter(hobbyRecyclerViewAdapter);
                }

                if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
                    Glide.with(App.getInstance()).load(activityInfo.getImageList().get(0).getThumbnailUrl()).apply(options).into(shareActiveHolder.activeImg);
                } else {
                    Glide.with(App.getInstance()).load(defaultImg).apply(options).into(shareActiveHolder.activeImg);
                }
                shareActiveHolder.activeContentTextView.setText(activityInfo.getTheme());
                //活动时间
                shareActiveHolder.activeTimeTextView.setText("活动时间：" + activityInfo.getActivityTime());

                ActivityLocationInfo activityLocationInfo = activityInfo.getLocationInfo();
                if (activityLocationInfo != null && activityInfo.getType() == 1) {
                    shareActiveHolder.activePlaceTextView.setText("活动地点：" + activityInfo.getLocationInfo().getAddr());
                    shareActiveHolder.activePlaceTextView.setVisibility(View.VISIBLE);
                } else {
                    shareActiveHolder.activePlaceTextView.setVisibility(View.INVISIBLE);
                }

                shareActiveHolder.activeApplyNumTextView.setText(activityInfo.getJoinNumber() + "人参加");
                if (activityInfo.getActivityPrice() >= 0) {
                    shareActiveHolder.activeCharge_tv.setText(activityInfo.getActivityPrice() + "元/人");
                    shareActiveHolder.activeCharge_tv.setVisibility(View.VISIBLE);
                } else {
                    shareActiveHolder.activeCharge_tv.setVisibility(View.INVISIBLE);
                }
                if (position == 0) {
                    shareActiveHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    shareActiveHolder.divider.setVisibility(View.GONE);
                }
            } else if (holder instanceof SharePostHolder) {
                final SharePostHolder sharePostHolder = (SharePostHolder) holder;
                HomeInfo homeInfo = list.get(position);
                if (userType == 1) {
                    sharePostHolder.deleteShareImg.setVisibility(View.VISIBLE);
                } else {
                    sharePostHolder.deleteShareImg.setVisibility(View.GONE);
                }
                final BaseUser shareBaseUser = homeInfo.getBaseUser();
                if (shareBaseUser == null) {
                    return;
                } else {
                    Glide.with(App.getInstance()).load(shareBaseUser.getUserIcon()).apply(userOptions).into(sharePostHolder.userImg);
                    sharePostHolder.userImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, shareBaseUser));
                            }
                        }
                    });
                    sharePostHolder.userNickNameTextView.setText(shareBaseUser.getNickName());
                    if (!StringUtils.isEmpty(shareBaseUser.getSchoolName())) {
                        //学校名称
                        sharePostHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                        sharePostHolder.schoolNameTextView.setText(shareBaseUser.getSchoolName());
                    } else {
                        sharePostHolder.schoolNameTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getIsSchoolmate() == 2 && shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //校友 并且不是自己
                        sharePostHolder.alumnus_tv.setVisibility(View.VISIBLE);
                    } else {
                        sharePostHolder.alumnus_tv.setVisibility(View.GONE);
                    }
                    sharePostHolder.userAgeTextView.setText(shareBaseUser.getAge() + "岁");
                    int sex = shareBaseUser.getSex();
                    if (sex == 1) {
                        //男性
                        sharePostHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                    } else if (sex == 2) {
                        //女性
                        sharePostHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                    }

                    String diatance = shareBaseUser.getDistance();
                    if ((!StringUtils.isEmpty(diatance))) {
                        sharePostHolder.locationTextView.setText(diatance);
                        sharePostHolder.locationTextView.setVisibility(View.VISIBLE);
                    } else {
                        sharePostHolder.locationTextView.setVisibility(View.GONE);
                    }

                    if ((!StringUtils.isEmpty(shareBaseUser.getCity()))) {
                        sharePostHolder.cityTextView.setText(shareBaseUser.getCity());
                        sharePostHolder.cityTextView.setVisibility(View.VISIBLE);
                    } else {
                        sharePostHolder.cityTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //自己不展示位置
                        sharePostHolder.locationTextView.setVisibility(View.GONE);
                        sharePostHolder.cityTextView.setVisibility(View.GONE);
                    }
                    sharePostHolder.tip_type_tv.setText("分享了帖子");
                }


                final ShareInfo shareInfo = homeInfo.getShareInfo();
                if (shareInfo == null) {
                    return;
                } else {

                    sharePostHolder.timeTextView.setText(shareInfo.getAddTime());

                    if (StringUtils.isEmpty(shareInfo.getContent())) {
                        sharePostHolder.shareContentTextView.setVisibility(View.GONE);
                    } else {
                        sharePostHolder.shareContentTextView.setText(shareInfo.getContent());
                        sharePostHolder.shareContentTextView.setVisibility(View.VISIBLE);
                    }

                    sharePostHolder.deleteShareImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DelectTipDialog dialog = new DelectTipDialog();
                            dialog.setTitleStr("确定删除分享？");
                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
                                @Override
                                public void itemClick() {
                                    onClick.onDeleteShare(shareInfo.getShareId(), position);
                                }
                            });
                            dialog.show(mActivity.getSupportFragmentManager());
                        }
                    });
                }

                final PostInfo postInfo = homeInfo.getPostInfo();
                if (postInfo == null) {
                    sharePostHolder.shareDeleteLayout.setVisibility(View.VISIBLE);
                    sharePostHolder.deleteTextView.setText("该帖子已被原作者删除");
                    sharePostHolder.rootLayout.setVisibility(View.GONE);
                    return;
                } else {
                    sharePostHolder.shareDeleteLayout.setVisibility(View.GONE);
                    sharePostHolder.rootLayout.setVisibility(View.VISIBLE);
                }
                final int isAnonymous = postInfo.getIsAnonymous();
                final BaseUser baseUser = postInfo.getToBaseUser();
                if (baseUser == null) {
                    return;
                } else {
                    sharePostHolder.fromUserNickNameTextView.setText("@" + baseUser.getNickName());
                    sharePostHolder.fromUserNickNameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) && isAnonymous == 2) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                            }
                        }
                    });
                }


                if (!StringUtils.isEmpty(postInfo.getContent())) {
                    sharePostHolder.contentTextView.setText(postInfo.getContent());
                    sharePostHolder.contentTextView.setVisibility(View.VISIBLE);
                } else {
                    sharePostHolder.contentTextView.setText(postInfo.getContent());
                    sharePostHolder.contentTextView.setVisibility(View.GONE);
                }
//                String hobbyName = postInfo.getHobbyName();
//                String organName = postInfo.getOrganName();
//                List<OrganBaseInfo> organBaseInfoList = postInfo.getOrganList();
//                if (hobbyName != null && !StringUtils.isEmpty(hobbyName)) {
//                    sharePostHolder.labelFirstTextView.setVisibility(View.VISIBLE);
//                    sharePostHolder.labelFirstTextView.setText(postInfo.getHobbyName());
//                } else {
//                    sharePostHolder.labelFirstTextView.setVisibility(View.GONE);
//                }
//                if (organBaseInfoList != null && organBaseInfoList.size() > 0) {
//                    final OrganBaseInfo organBaseInfo = organBaseInfoList.get(0);
//                    if (organBaseInfo != null) {
//                        sharePostHolder.labelSecondLayout.setVisibility(View.VISIBLE);
//                        sharePostHolder.labelSecondTextView.setText(organBaseInfo.getOrganName());
//                        sharePostHolder.labelSecondLayout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //跳转到圈子详情
//                                OrganizationInfo organizationInfo = new OrganizationInfo();
//                                organizationInfo.setOrganId(organBaseInfo.getOrganId());
//                                organizationInfo.setOrganName(organBaseInfo.getOrganName());
//                            }
//                        });
//                    } else {
//                        sharePostHolder.labelSecondLayout.setVisibility(View.GONE);
//                    }
//
//                } else if (organName != null && !StringUtils.isEmpty(organName)) {
//                    sharePostHolder.labelSecondLayout.setVisibility(View.VISIBLE);
//                    sharePostHolder.labelSecondTextView.setText(postInfo.getOrganName());
//                    sharePostHolder.labelSecondLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            OrganizationInfo organizationInfo = new OrganizationInfo();
//                            organizationInfo.setOrganId(postInfo.getOrganId());
//                            organizationInfo.setOrganName(postInfo.getOrganName());
//                        }
//                    });
//                } else {
//                    sharePostHolder.labelSecondLayout.setVisibility(View.GONE);
//                }

                //店铺信息
                if(postInfo.getStoreDetail()!=null){
                    final StoreInfo storeInfo=postInfo.getStoreDetail().getStoreInfo();
                    if(storeInfo!=null){
                        sharePostHolder.storeLayout.setVisibility(View.VISIBLE);
                        if(storeInfo.getIsPublish()==1){
                            sharePostHolder.recruitTextView.setVisibility(View.VISIBLE);
                        }else{
                            sharePostHolder.recruitTextView.setVisibility(View.GONE);
                        }
                        sharePostHolder.storeNameTextView.setText(storeInfo.getTitle());
                        StoreCategorySecondRecycleAdapter secondRecycleAdapter=new StoreCategorySecondRecycleAdapter();
                        if(storeInfo.getFirstLabelInfo()!=null&&storeInfo.getFirstLabelInfo().getList()!=null){
                            secondRecycleAdapter.setList(storeInfo.getFirstLabelInfo().getList());
                        }
                        sharePostHolder.recycler_store_label.setAdapter(secondRecycleAdapter);
                    }else{
                        sharePostHolder.storeLayout.setVisibility(View.GONE);
                    }
                    sharePostHolder.storeLayout.setOnClickListener(new View.OnClickListener() {
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
                    sharePostHolder.storeLayout.setVisibility(View.GONE);
                }

                PostLabelInfoRecycleAdapter postLabelInfoRecycleAdapter=new PostLabelInfoRecycleAdapter(14);
                sharePostHolder.recycler_post_label.setVisibility(View.VISIBLE);
                if(postInfo.getPostLabelList()!=null&&postInfo.getPostLabelList().size()>0){
                    postLabelInfoRecycleAdapter.setList(postInfo.getPostLabelList());
                }else
                {
                    sharePostHolder.recycler_post_label.setVisibility(View.GONE);
                }
                sharePostHolder.recycler_post_label.setAdapter(postLabelInfoRecycleAdapter);

                sharePostHolder.shareNumTextView.setText(Utils.bigNumberToStr(postInfo.getShareNumber(), postInfo.getShareNumberStr()));
                sharePostHolder.shareNumTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //分享弹窗
                        String imgUrl = "";
                        if (postInfo.getTopicType() == 2) {
                            imgUrl = postInfo.getVideoImageUrl();
                        } else {
                            if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
                                imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
                            }
                        }
                        ShareUtil.getInstance().showShare(mActivity, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);

                    }
                });
                sharePostHolder.praisNumTextView.setText(Utils.bigNumberToStr(postInfo.getPraiseCount(), postInfo.getPraiseCountStr()));
                final int isPraise = postInfo.getIsPraise();
                if (isPraise == 1) {
                    //1 : 已点赞
                    sharePostHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
                } else {
                    //0: 未点赞
                    sharePostHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
                }
                sharePostHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点赞 (1 点赞  2 取消点赞),
                        onClick.onPraise(postInfo.getPostId(), isPraise + 1, position, 1);
                    }
                });
                sharePostHolder.commentNumTextView.setText(Utils.bigNumberToStr(postInfo.getCommentCount(), postInfo.getCommentCountStr()));
                // 图片显示区域
                sharePostHolder.recycler_image.setVisibility(View.GONE);
                sharePostHolder.linear_image_two.setVisibility(View.GONE);

                if (postInfo.getTopicType() == 2) {
                    //视频
                    sharePostHolder.linear_image_two.setVisibility(View.VISIBLE);
                    sharePostHolder.video_play_img.setVisibility(View.VISIBLE);
                    if (!refreshItem) {
                        Glide.with(App.getInstance()).load(postInfo.getVideoImageUrl()).apply(options).into(sharePostHolder.img_first);
                    }
                    sharePostHolder.img_first.setOnClickListener(new View.OnClickListener() {
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
                    final List<Image> imageList = postInfo.getImageList();
                    if (imageList != null) {
                        if (imageList.size() > 0 && imageList.size() < 2) {

                            sharePostHolder.linear_image_two.setVisibility(View.VISIBLE);
                            sharePostHolder.video_play_img.setVisibility(View.GONE);
                            if (imageList.size() == 1) {
                                if (!refreshItem) {
                                    if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
                                        Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(sharePostHolder.img_first);
                                    } else {
                                        Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(sharePostHolder.img_first);
                                    }
                                }
                                sharePostHolder.img_first.setOnClickListener(new View.OnClickListener() {
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
                                PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
                                sharePostHolder.recycler_image.setAdapter(postImageLIstAdapter);

                                postImageLIstAdapter.setOnItemListener(new ItemListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (mActivity != null) {
                                            ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
                                        }
                                    }
                                });
                            }

                            sharePostHolder.recycler_image.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (position == 0) {
                    sharePostHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    sharePostHolder.divider.setVisibility(View.GONE);
                }
            } else if (holder instanceof SharePKViewHolder) {
                final SharePKViewHolder sharePKViewHolder = (SharePKViewHolder) holder;
                HomeInfo homeInfo = list.get(position);
                if (userType == 1) {
                    sharePKViewHolder.deleteShareImg.setVisibility(View.VISIBLE);
                } else {
                    sharePKViewHolder.deleteShareImg.setVisibility(View.GONE);
                }
                final BaseUser shareBaseUser = homeInfo.getBaseUser();
                if (shareBaseUser == null) {
                    return;
                } else {
                    Glide.with(App.getInstance()).load(shareBaseUser.getUserIcon()).apply(userOptions).into(sharePKViewHolder.userImg);
                    sharePKViewHolder.userImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, shareBaseUser));
                            }
                        }
                    });
                    sharePKViewHolder.userNickNameTextView.setText(shareBaseUser.getNickName());
                    if (!StringUtils.isEmpty(shareBaseUser.getSchoolName())) {
                        //学校名称
                        sharePKViewHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                        sharePKViewHolder.schoolNameTextView.setText(shareBaseUser.getSchoolName());
                    } else {
                        sharePKViewHolder.schoolNameTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getIsSchoolmate() == 2 && shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //校友 并且不是自己
                        sharePKViewHolder.alumnus_tv.setVisibility(View.VISIBLE);
                    } else {
                        sharePKViewHolder.alumnus_tv.setVisibility(View.GONE);
                    }
                    sharePKViewHolder.userAgeTextView.setText(shareBaseUser.getAge() + "岁");
                    int sex = shareBaseUser.getSex();
                    if (sex == 1) {
                        //男性
                        sharePKViewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                    } else if (sex == 2) {
                        //女性
                        sharePKViewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                    }

                    String diatance = shareBaseUser.getDistance();
                    if ((!StringUtils.isEmpty(diatance))) {
                        sharePKViewHolder.locationTextView.setText(diatance);
                        sharePKViewHolder.locationTextView.setVisibility(View.VISIBLE);
                    } else {
                        sharePKViewHolder.locationTextView.setVisibility(View.GONE);
                    }

                    if ((!StringUtils.isEmpty(shareBaseUser.getCity()))) {
                        sharePKViewHolder.cityTextView.setText(shareBaseUser.getCity());
                        sharePKViewHolder.cityTextView.setVisibility(View.VISIBLE);
                    } else {
                        sharePKViewHolder.cityTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //自己不展示位置
                        sharePKViewHolder.locationTextView.setVisibility(View.GONE);
                        sharePKViewHolder.cityTextView.setVisibility(View.GONE);
                    }

                }

                final ShareInfo shareInfo = homeInfo.getShareInfo();
                if (shareInfo == null) {
                    return;
                } else {

                    sharePKViewHolder.timeTextView.setText(shareInfo.getAddTime());

                    if (StringUtils.isEmpty(shareInfo.getContent())) {
                        sharePKViewHolder.shareContentTextView.setVisibility(View.GONE);
                    } else {
                        sharePKViewHolder.shareContentTextView.setText(shareInfo.getContent());
                        sharePKViewHolder.shareContentTextView.setVisibility(View.VISIBLE);
                    }

                    sharePKViewHolder.deleteShareImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DelectTipDialog dialog = new DelectTipDialog();
                            dialog.setTitleStr("确定删除分享？");
                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
                                @Override
                                public void itemClick() {
                                    onClick.onDeleteShare(shareInfo.getShareId(), position);
                                }
                            });
                            dialog.show(mActivity.getSupportFragmentManager());
                        }
                    });
                }

                final PlayInfo playInfo = homeInfo.getPlayInfo();
                if (playInfo == null) {
                    sharePKViewHolder.shareDeleteLayout.setVisibility(View.VISIBLE);
                    sharePKViewHolder.deleteTextView.setText("该视频已被原作者删除");
                    sharePKViewHolder.rootLayout.setVisibility(View.GONE);
                    return;
                } else {
                    sharePKViewHolder.shareDeleteLayout.setVisibility(View.GONE);
                    sharePKViewHolder.rootLayout.setVisibility(View.VISIBLE);
                }
                final BaseUser baseUser = playInfo.getToBaseUser();
                if (baseUser == null) {
                    return;
                } else {
                    sharePKViewHolder.fromUserNickNameTextView.setText("@" + baseUser.getNickName());
                    sharePKViewHolder.fromUserNickNameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                            }
                        }
                    });
                }

                sharePKViewHolder.pkTitleView.setText(playInfo.getLaunchPlay().getTitle());
                Glide.with(App.getInstance()).load(playInfo.getVideoImageUrl()).apply(options).into(sharePKViewHolder.pkVideoImageView);
                sharePKViewHolder.pkContentVeiw.setText(playInfo.getTitle());
                if (position == 0) {
                    sharePKViewHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    sharePKViewHolder.divider.setVisibility(View.GONE);
                }

            } else if (holder instanceof ShareOrganizationHolder) {
                final ShareOrganizationHolder shareOrganizationHolder = (ShareOrganizationHolder) holder;
                HomeInfo homeInfo = list.get(position);
                if (userType == 1) {
                    shareOrganizationHolder.deleteShareImg.setVisibility(View.VISIBLE);
                } else {
                    shareOrganizationHolder.deleteShareImg.setVisibility(View.GONE);
                }
                final BaseUser shareBaseUser = homeInfo.getBaseUser();
                if (shareBaseUser == null) {
                    return;
                } else {
                    Glide.with(App.getInstance()).load(shareBaseUser.getUserIcon()).apply(userOptions).into(shareOrganizationHolder.userImg);
                    shareOrganizationHolder.userImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, shareBaseUser));
                            }
                        }
                    });
                    shareOrganizationHolder.userNickNameTextView.setText(shareBaseUser.getNickName());
                    if (!StringUtils.isEmpty(shareBaseUser.getSchoolName())) {
                        //学校名称
                        shareOrganizationHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                        shareOrganizationHolder.schoolNameTextView.setText(shareBaseUser.getSchoolName());
                    } else {
                        shareOrganizationHolder.schoolNameTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getIsSchoolmate() == 2 && shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //校友 并且不是自己
                        shareOrganizationHolder.alumnus_tv.setVisibility(View.VISIBLE);
                    } else {
                        shareOrganizationHolder.alumnus_tv.setVisibility(View.GONE);
                    }
                    shareOrganizationHolder.userAgeTextView.setText(shareBaseUser.getAge() + "岁");
                    int sex = shareBaseUser.getSex();
                    if (sex == 1) {
                        //男性
                        shareOrganizationHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                    } else if (sex == 2) {
                        //女性
                        shareOrganizationHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                    }

                    String diatance = shareBaseUser.getDistance();
                    if ((!StringUtils.isEmpty(diatance))) {
                        shareOrganizationHolder.locationTextView.setText(diatance);
                        shareOrganizationHolder.locationTextView.setVisibility(View.VISIBLE);
                    } else {
                        shareOrganizationHolder.locationTextView.setVisibility(View.GONE);
                    }

                    if ((!StringUtils.isEmpty(shareBaseUser.getCity()))) {
                        shareOrganizationHolder.cityTextView.setText(shareBaseUser.getCity());
                        shareOrganizationHolder.cityTextView.setVisibility(View.VISIBLE);
                    } else {
                        shareOrganizationHolder.cityTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //自己不展示位置
                        shareOrganizationHolder.locationTextView.setVisibility(View.GONE);
                        shareOrganizationHolder.cityTextView.setVisibility(View.GONE);
                    }
                    shareOrganizationHolder.tip_type_tv.setText("分享了圈子");
                    shareOrganizationHolder.tip_type_tv.setVisibility(View.VISIBLE);
                }

                final ShareInfo shareInfo = homeInfo.getShareInfo();
                if (shareInfo == null) {
                    return;
                } else {
                    shareOrganizationHolder.timeTextView.setText(shareInfo.getAddTime());
                    if (StringUtils.isEmpty(shareInfo.getContent())) {
                        shareOrganizationHolder.shareContentTextView.setVisibility(View.GONE);
                    } else {
                        shareOrganizationHolder.shareContentTextView.setText(shareInfo.getContent());
                        shareOrganizationHolder.shareContentTextView.setVisibility(View.VISIBLE);
                    }
                    shareOrganizationHolder.deleteShareImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DelectTipDialog dialog = new DelectTipDialog();
                            dialog.setTitleStr("确定删除分享？");
                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
                                @Override
                                public void itemClick() {
                                    onClick.onDeleteShare(shareInfo.getShareId(), position);
                                }
                            });
                            dialog.show(mActivity.getSupportFragmentManager());
                        }
                    });
                }

                final OrganizationInfo organizationInfo = homeInfo.getOrganizationInfo();
                if (organizationInfo == null) {
                    shareOrganizationHolder.shareDeleteLayout.setVisibility(View.VISIBLE);
                    shareOrganizationHolder.deleteTextView.setText("该圈子已被原作者删除");
                    shareOrganizationHolder.rootLayout.setVisibility(View.GONE);
                    return;
                } else {
                    shareOrganizationHolder.shareDeleteLayout.setVisibility(View.GONE);
                    shareOrganizationHolder.rootLayout.setVisibility(View.VISIBLE);
                }


                Glide.with(mActivity).load(organizationInfo.getOrganImg()).apply(options).into(shareOrganizationHolder.orgainImg);
                shareOrganizationHolder.orgainNameTextView.setText(organizationInfo.getOrganName());
                shareOrganizationHolder.orgainUserNumTextView.setText("成员  " + organizationInfo.getMemberCount());
                shareOrganizationHolder.orgainPostNumTextView.setText("帖子  " + organizationInfo.getPostCount());

                int myUserRole = organizationInfo.getMyUserRole();
                final int joinLimit = organizationInfo.getJoinLimit();
                shareOrganizationHolder.orgainJoinTextView.setVisibility(View.VISIBLE);
                if (myUserRole == 0) {
                    //用户不在圈子
                    shareOrganizationHolder.orgainJoinTextView.setEnabled(true);
                    if (joinLimit == 1) {
                        shareOrganizationHolder.orgainJoinTextView.setText("加入");
                    } else {
                        shareOrganizationHolder.orgainJoinTextView.setText("加入");
                    }
                    shareOrganizationHolder.orgainJoinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                    shareOrganizationHolder.orgainJoinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                } else if (myUserRole == 4) {
                    shareOrganizationHolder.orgainJoinTextView.setText("申请中");
                    shareOrganizationHolder.orgainJoinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                    shareOrganizationHolder.orgainJoinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
                    shareOrganizationHolder.orgainJoinTextView.setEnabled(false);
                } else {
                    //用户在圈子
                    shareOrganizationHolder.orgainJoinTextView.setText("已加入");
                    shareOrganizationHolder.orgainJoinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                    shareOrganizationHolder.orgainJoinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
                    shareOrganizationHolder.orgainJoinTextView.setEnabled(false);
                }
                shareOrganizationHolder.orgainJoinTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClick.onJoinClick(position, joinLimit);
                    }
                });

                if (position == 0) {
                    shareOrganizationHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    shareOrganizationHolder.divider.setVisibility(View.GONE);
                }
            } else if (holder instanceof ShareRoomViewHolder) {
                final ShareRoomViewHolder shareRoomViewHolder = (ShareRoomViewHolder) holder;
                HomeInfo homeInfo = list.get(position);
                if (userType == 1) {
                    shareRoomViewHolder.deleteShareImg.setVisibility(View.VISIBLE);
                } else {
                    shareRoomViewHolder.deleteShareImg.setVisibility(View.GONE);
                }
                final BaseUser shareBaseUser = homeInfo.getBaseUser();
                if (shareBaseUser == null) {
                    return;
                } else {
                    Glide.with(App.getInstance()).load(shareBaseUser.getUserIcon()).apply(userOptions).into(shareRoomViewHolder.userImg);
                    shareRoomViewHolder.userImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, shareBaseUser));
                            }
                        }
                    });
                    shareRoomViewHolder.userNickNameTextView.setText(shareBaseUser.getNickName());
                    if (!StringUtils.isEmpty(shareBaseUser.getSchoolName())) {
                        //学校名称
                        shareRoomViewHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                        shareRoomViewHolder.schoolNameTextView.setText(shareBaseUser.getSchoolName());
                    } else {
                        shareRoomViewHolder.schoolNameTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getIsSchoolmate() == 2 && shareBaseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //校友 并且不是自己
                        shareRoomViewHolder.alumnus_tv.setVisibility(View.VISIBLE);
                    } else {
                        shareRoomViewHolder.alumnus_tv.setVisibility(View.GONE);
                    }
                    shareRoomViewHolder.userAgeTextView.setText(shareBaseUser.getAge() + "岁");
                    int sex = shareBaseUser.getSex();
                    if (sex == 1) {
                        //男性
                        shareRoomViewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                    } else if (sex == 2) {
                        //女性
                        shareRoomViewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                    }

                    String diatance = shareBaseUser.getDistance();
                    if ((!StringUtils.isEmpty(diatance))) {
                        shareRoomViewHolder.locationTextView.setText(diatance);
                        shareRoomViewHolder.locationTextView.setVisibility(View.VISIBLE);
                    } else {
                        shareRoomViewHolder.locationTextView.setVisibility(View.GONE);
                    }

                    if ((!StringUtils.isEmpty(shareBaseUser.getCity()))) {
                        shareRoomViewHolder.cityTextView.setText(shareBaseUser.getCity());
                        shareRoomViewHolder.cityTextView.setVisibility(View.VISIBLE);
                    } else {
                        shareRoomViewHolder.cityTextView.setVisibility(View.GONE);
                    }
                    if (shareBaseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //自己不展示位置
                        shareRoomViewHolder.locationTextView.setVisibility(View.GONE);
                        shareRoomViewHolder.cityTextView.setVisibility(View.GONE);
                    }
                    shareRoomViewHolder.tip_type_tv.setText("分享了房间");
                    shareRoomViewHolder.tip_type_tv.setVisibility(View.GONE);
                }

                final ShareInfo shareInfo = homeInfo.getShareInfo();
                if (shareInfo == null) {
                    return;
                } else {
                    shareRoomViewHolder.timeTextView.setText(shareInfo.getAddTime());

                    if (StringUtils.isEmpty(shareInfo.getContent())) {
                        shareRoomViewHolder.shareContentTextView.setVisibility(View.GONE);
                    } else {
                        shareRoomViewHolder.shareContentTextView.setText(shareInfo.getContent());
                        shareRoomViewHolder.shareContentTextView.setVisibility(View.VISIBLE);
                    }

                    shareRoomViewHolder.deleteShareImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //删除本条分享
                            DelectTipDialog dialog = new DelectTipDialog();
                            dialog.setTitleStr("确定删除分享？");
                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
                                @Override
                                public void itemClick() {
                                    onClick.onDeleteShare(shareInfo.getShareId(), position);
                                }
                            });
                            dialog.show(mActivity.getSupportFragmentManager());

                        }
                    });
                }
                final RoomInfo roomInfo = homeInfo.getRoomInfo();
                if (roomInfo == null) {
                    shareRoomViewHolder.shareDeleteLayout.setVisibility(View.VISIBLE);
                    shareRoomViewHolder.deleteTextView.setText("该聊天频道已被原作者删除");
                    shareRoomViewHolder.rootLayout.setVisibility(View.GONE);
                    return;
                } else {
                    shareRoomViewHolder.shareDeleteLayout.setVisibility(View.GONE);
                    shareRoomViewHolder.rootLayout.setVisibility(View.VISIBLE);
                }
                final BaseUser baseUser = roomInfo.getToBaseUser();
                if (shareInfo == null) {
                    return;
                } else {
                    shareRoomViewHolder.fromUserNickNameTextView.setText("@" + baseUser.getNickName());
                    shareRoomViewHolder.fromUserNickNameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                //跳到用户详情(自己和匿名不可点击)
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                            }
                        }
                    });
                }

                shareRoomViewHolder.roomNameView.setText(roomInfo.getRoomName());
                Glide.with(App.getInstance()).load(roomInfo.getBackgroundUrl()).apply(options).into(shareRoomViewHolder.roomImageView);

                //房间标签
                List<String> keywordInfoList = roomInfo.getLabelList();
                if (keywordInfoList != null && keywordInfoList.size() > 0) {
                    shareRoomViewHolder.tagLayout.setVisibility(View.VISIBLE);
                    StringBuffer labelString = new StringBuffer();
                    for (int i = 0; i < keywordInfoList.size(); i++) {
                        String info = keywordInfoList.get(i);
                        labelString.append(info + "    ");
                    }
                    shareRoomViewHolder.tagListView.setText(labelString.toString());
                } else {
                    shareRoomViewHolder.tagLayout.setVisibility(View.GONE);
                }
                if (position == 0) {
                    shareRoomViewHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    shareRoomViewHolder.divider.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class PostHolder extends BaseViewHolder {
        View divider;
        ImageView userImg, img_first, video_play_img, alumnus_tv, storeIcon, isMatchImg, postDeleteImg;
        TextView userNickNameTextView, userAgeTextView, timeTextView, locationTextView, cityTextView, contentTextView,recruitTextView,storeNameTextView,
                labelFirstTextView, labelSecondTextView, shareNumTextView, praisNumTextView, commentNumTextView, schoolNameTextView;
        RelativeLayout linear_image_two, labelSecondLayout,storeLayout;
        RecyclerView recycler_image,recycler_post_label,recycler_store_label;
        ImageView postStickView;

        public PostHolder(View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.divider_line);
            userNickNameTextView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
//            userLevelTextView = (ImageView) itemView.findViewById(R.id.user_level_text);
            isMatchImg = (ImageView) itemView.findViewById(R.id.ismatch_img);
            userAgeTextView = (TextView) itemView.findViewById(R.id.user_age_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            locationTextView = (TextView) itemView.findViewById(R.id.location_text);
            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
            contentTextView = (TextView) itemView.findViewById(R.id.content_text);
            recruitTextView= (TextView) itemView.findViewById(R.id.recruit_text);
            storeNameTextView= (TextView) itemView.findViewById(R.id.store_name_text);
            storeLayout= (RelativeLayout) itemView.findViewById(R.id.store_layout);
            storeIcon = itemView.findViewById(R.id.store_img);
//            labelFirstTextView = (TextView) itemView.findViewById(R.id.post_label_one_text);
//            labelSecondTextView = (TextView) itemView.findViewById(R.id.post_label_two_text);
//            labelSecondLayout = (RelativeLayout) itemView.findViewById(R.id.post_label_two_layout);
            shareNumTextView = (TextView) itemView.findViewById(R.id.share_text);
            praisNumTextView = (TextView) itemView.findViewById(R.id.praise_text);
            commentNumTextView = (TextView) itemView.findViewById(R.id.comment_text);
//            postStickView = (ImageView) itemView.findViewById(R.id.post_stick_view);
//            postDeleteImg = (ImageView) itemView.findViewById(R.id.delete_img);

            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            linear_image_two = (RelativeLayout) itemView.findViewById(R.id.linear_image_two);
            img_first = (ImageView) itemView.findViewById(R.id.img_first);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img_first.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
            img_first.setLayoutParams(layoutParams);
            video_play_img = (ImageView) itemView.findViewById(R.id.video_play_img);
            recycler_image = (RecyclerView) itemView.findViewById(R.id.recycler_image);
            recycler_post_label = (RecyclerView) itemView.findViewById(R.id.recycler_post_label);
            recycler_store_label = (RecyclerView) itemView.findViewById(R.id.store_label_recycle_view);

            recycler_image.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recycler_image.setLayoutManager(gridLayoutManager);
            recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));

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
            recycler_post_label.setLayoutManager(chipsLayoutManager);

            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
            recycler_store_label.setLayoutManager(linearLayoutManager);

        }

    }


    class OrganizationHolder extends BaseViewHolder {
        View divider;
        ImageView userImg, orgainImg, alumnus_tv;
        TextView userNickNameTextView, userStateTextView, timeTextView, locationTextView, cityTextView, useAgeTextView, orgainNameTextView, orgainUserNumTextView, orgainPostNumTextView, orgainJoinTextView, schoolNameTextView;

        public OrganizationHolder(View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.top_header_view);
            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            userNickNameTextView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
            userStateTextView = (TextView) itemView.findViewById(R.id.user_state_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            locationTextView = (TextView) itemView.findViewById(R.id.location_text);
            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
            useAgeTextView = (TextView) itemView.findViewById(R.id.user_age_text);
            orgainImg = (ImageView) itemView.findViewById(R.id.orgain_img);
            orgainNameTextView = (TextView) itemView.findViewById(R.id.orgain_name_text);
            orgainUserNumTextView = (TextView) itemView.findViewById(R.id.orgain_user_num_text);
            orgainPostNumTextView = (TextView) itemView.findViewById(R.id.orgain_post_num_text);
            orgainJoinTextView = (TextView) itemView.findViewById(R.id.orgain_join_text);

        }

    }

    class ActiveHolder extends BaseViewHolder {
        View divider;
        ImageView userImg, userSexImg, activeImg, activeStateImg, alumnusImg, orgainzationMoreImageView;
        TextView userName_tv, user_location_tv, city_tv, activeType_tv, activeContentTextView, activeTimeTextView, activePlaceTextView, activeApplyNumTextView, activeCharge_tv,
                publishTimeTextView, activeDistanceView, schoolNameTextView;
        ;
        RecyclerView hobbyRecycleView, orgainzationRecycleView;

        public ActiveHolder(View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.top_header_view);
            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            userSexImg = (ImageView) itemView.findViewById(R.id.user_sex_img);
            activeImg = (ImageView) itemView.findViewById(R.id.active_img);
            activeStateImg = (ImageView) itemView.findViewById(R.id.active_status_img);
            userName_tv = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            alumnusImg = (ImageView) itemView.findViewById(R.id.alumnus_text);
            user_location_tv = (TextView) itemView.findViewById(R.id.location_text);
            city_tv = (TextView) itemView.findViewById(R.id.city_text);
            activeType_tv = (TextView) itemView.findViewById(R.id.active_type_text);
            activeContentTextView = (TextView) itemView.findViewById(R.id.active_name_tv);
            activeTimeTextView = (TextView) itemView.findViewById(R.id.active_time_tv);
            activePlaceTextView = (TextView) itemView.findViewById(R.id.active_place_tv);
            activeCharge_tv = (TextView) itemView.findViewById(R.id.active_charge_tv);
            publishTimeTextView = itemView.findViewById(R.id.publish_time_text);
            activeDistanceView = itemView.findViewById(R.id.active_distance_tv);
            activeApplyNumTextView = (TextView) itemView.findViewById(R.id.active_apply_num_tv);
            hobbyRecycleView = (RecyclerView) itemView.findViewById(R.id.recycler_hobby);
            hobbyRecycleView.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            hobbyRecycleView.setLayoutManager(gridLayoutManager);
            hobbyRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 3, false));
            orgainzationRecycleView = (RecyclerView) itemView.findViewById(R.id.recycler_orgainzation);
            orgainzationRecycleView.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager2 = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager2.setAutoMeasureEnabled(true);
            orgainzationRecycleView.setLayoutManager(gridLayoutManager2);
            orgainzationRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(10), 3, false));
            orgainzationMoreImageView = itemView.findViewById(R.id.orgainzation_more_image);
        }

    }

    class RoomViewHolder extends BaseViewHolder {
        View divider;
        RelativeLayout userLayout;
        ImageView userImageView;
        ImageView roomImageView, alumnusView;
        TextView userStatusView, userNameView, userAgeView, distanceView, cityView, userNumView, roomNameView, schoolNameTextView;
        RelativeLayout tagLayout;
        TextView tagListView;

        public RoomViewHolder(View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.top_header_view);
            userStatusView = (TextView) itemView.findViewById(R.id.user_state_text);
            userLayout = (RelativeLayout) itemView.findViewById(R.id.user_layout);
            userImageView = (ImageView) itemView.findViewById(R.id.user_image_view);
            roomImageView = (ImageView) itemView.findViewById(R.id.room_image_view);
            userNameView = (TextView) itemView.findViewById(R.id.user_name_view);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            userAgeView = (TextView) itemView.findViewById(R.id.user_age_text);
            alumnusView = (ImageView) itemView.findViewById(R.id.alumnus_text);
            distanceView = (TextView) itemView.findViewById(R.id.distance_tv);
            cityView = (TextView) itemView.findViewById(R.id.city_tv);
            userNumView = (TextView) itemView.findViewById(R.id.user_num_view);
            tagLayout = (RelativeLayout) itemView.findViewById(R.id.tag_layout);
            tagListView = (TextView) itemView.findViewById(R.id.tag_text_view);
            roomNameView = (TextView) itemView.findViewById(R.id.room_name_view);
        }
    }

    class PKViewHolder extends BaseViewHolder {
        View divider;
        ImageView userImageView;
        ImageView pkVideoImageView, pkVideoPlayView;
        TextView pkStatusView, userNameView, userAgeView, alumnusView, distanceView, cityView, timeView, pkTitleView, pkContentVeiw, schoolNameTextView;
        RelativeLayout topLayout;

        public PKViewHolder(View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.top_header_view);
            pkStatusView = (TextView) itemView.findViewById(R.id.user_state_text);
            userImageView = (ImageView) itemView.findViewById(R.id.user_img);
            pkVideoImageView = (ImageView) itemView.findViewById(R.id.img_bg);
            pkVideoPlayView = (ImageView) itemView.findViewById(R.id.video_play_img);
            userNameView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            userAgeView = (TextView) itemView.findViewById(R.id.user_age_text);
            alumnusView = (TextView) itemView.findViewById(R.id.alumnus_text);
            distanceView = (TextView) itemView.findViewById(R.id.location_text);
            cityView = (TextView) itemView.findViewById(R.id.city_text);
            timeView = (TextView) itemView.findViewById(R.id.time_text);
            pkTitleView = (TextView) itemView.findViewById(R.id.pk_title_text);
            pkContentVeiw = (TextView) itemView.findViewById(R.id.pk_content_text);
            topLayout = (RelativeLayout) itemView.findViewById(R.id.pk_top_layput);


        }
    }

    class SayloveViewHolder extends BaseViewHolder {
        ImageView userImg, img_first, video_play_img, alumnus_tv, userLevelTextView, deleteImg;
        TextView userNickNameTextView, userAgeTextView, timeTextView, locationTextView, cityTextView, contentTextView,
                labelFirstTextView, shareNumTextView, praisNumTextView, commentNumTextView, schoolNameTextView, type_tip_text;
        RelativeLayout linear_image_two, labelSecondLayout;
        RecyclerView recycler_image;
        private View divider;
        ImageView postStickView;

        public SayloveViewHolder(View itemView) {
            super(itemView);
            userNickNameTextView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            deleteImg = (ImageView) itemView.findViewById(R.id.delete_img);
            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
//            userLevelTextView = (ImageView) itemView.findViewById(R.id.user_level_text);
            userAgeTextView = (TextView) itemView.findViewById(R.id.user_age_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            locationTextView = (TextView) itemView.findViewById(R.id.location_text);
            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
            contentTextView = (TextView) itemView.findViewById(R.id.content_text);
//            labelFirstTextView = (TextView) itemView.findViewById(R.id.post_label_one_text);
//            labelSecondLayout = (RelativeLayout) itemView.findViewById(R.id.post_label_two_layout);
//            type_tip_text = (TextView) itemView.findViewById(R.id.type_tip_text);
            shareNumTextView = (TextView) itemView.findViewById(R.id.share_text);
            praisNumTextView = (TextView) itemView.findViewById(R.id.praise_text);
            commentNumTextView = (TextView) itemView.findViewById(R.id.comment_text);
//            postStickView = (ImageView) itemView.findViewById(R.id.post_stick_view);
//            postStickView.setVisibility(View.INVISIBLE);

            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            linear_image_two = (RelativeLayout) itemView.findViewById(R.id.linear_image_two);
            img_first = (ImageView) itemView.findViewById(R.id.img_first);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img_first.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
            img_first.setLayoutParams(layoutParams);
            video_play_img = (ImageView) itemView.findViewById(R.id.video_play_img);
            recycler_image = (RecyclerView) itemView.findViewById(R.id.recycler_image);
            divider = (View) itemView.findViewById(R.id.divider_line);
            recycler_image.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recycler_image.setLayoutManager(gridLayoutManager);
            recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));

        }

    }

    class ShareSayloveViewHolder extends BaseViewHolder {
        RelativeLayout rootLayout, shareDeleteLayout;
        ImageView userImg, img_first, video_play_img, alumnus_tv, deleteShareImg;
        TextView userNickNameTextView, fromUserNickNameTextView, userAgeTextView, tip_type_tv, timeTextView, locationTextView, cityTextView, shareContentTextView, contentTextView,
                shareNumTextView, praisNumTextView, commentNumTextView, deleteTextView, schoolNameTextView;
        RelativeLayout linear_image_two;
        RecyclerView recycler_image;
        View divider;

        public ShareSayloveViewHolder(View itemView) {
            super(itemView);
            shareDeleteLayout = (RelativeLayout) itemView.findViewById(R.id.delete_layout);
            deleteTextView = (TextView) itemView.findViewById(R.id.delete_text);
            deleteShareImg = (ImageView) itemView.findViewById(R.id.delete_share_img);
            rootLayout = (RelativeLayout) itemView.findViewById(R.id.share_layout);
            rootLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_fafafa));
            userNickNameTextView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            fromUserNickNameTextView = (TextView) itemView.findViewById(R.id.form_user_nickname_text);
            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
            userAgeTextView = (TextView) itemView.findViewById(R.id.user_age_text);
            tip_type_tv = (TextView) itemView.findViewById(R.id.user_state_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            locationTextView = (TextView) itemView.findViewById(R.id.location_text);
            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
            shareContentTextView = (TextView) itemView.findViewById(R.id.share_content_text);
            contentTextView = (TextView) itemView.findViewById(R.id.content_text);
            shareNumTextView = (TextView) itemView.findViewById(R.id.share_text);
            praisNumTextView = (TextView) itemView.findViewById(R.id.praise_text);
            commentNumTextView = (TextView) itemView.findViewById(R.id.comment_text);

            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            linear_image_two = (RelativeLayout) itemView.findViewById(R.id.linear_image_two);
            img_first = (ImageView) itemView.findViewById(R.id.img_first);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img_first.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
            img_first.setLayoutParams(layoutParams);
            video_play_img = (ImageView) itemView.findViewById(R.id.video_play_img);
            recycler_image = (RecyclerView) itemView.findViewById(R.id.recycler_image);
            divider = (View) itemView.findViewById(R.id.divider_line);
            recycler_image.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recycler_image.setLayoutManager(gridLayoutManager);
            recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));

        }

    }

    class ShareActiveHolder extends BaseViewHolder {
        RelativeLayout rootLayout, shareDeleteLayout;
        View divider;
        ImageView userImg, activeImg, alumnus_tv, activeStateImg, deleteShareImg;
        TextView userNickNameTextView, fromUserNickNameTextView, userAgeTextView, locationTextView, cityTextView, tip_type_tv, shareContentTextView, activeContentTextView, activeTimeTextView, activePlaceTextView, activeApplyNumTextView, activeCharge_tv,
                timeTextView, activeDistanceView, deleteTextView, schoolNameTextView;
        RecyclerView hobbyRecycleView;

        public ShareActiveHolder(View itemView) {
            super(itemView);
            shareDeleteLayout = (RelativeLayout) itemView.findViewById(R.id.delete_layout);
            deleteTextView = (TextView) itemView.findViewById(R.id.delete_text);
            deleteShareImg = (ImageView) itemView.findViewById(R.id.delete_share_img);
            rootLayout = (RelativeLayout) itemView.findViewById(R.id.share_layout);
            rootLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_fafafa));
            divider = itemView.findViewById(R.id.divider_line);
            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            userNickNameTextView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            fromUserNickNameTextView = (TextView) itemView.findViewById(R.id.form_user_nickname_text);
            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
            userAgeTextView = (TextView) itemView.findViewById(R.id.user_age_text);
            tip_type_tv = (TextView) itemView.findViewById(R.id.user_state_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            locationTextView = (TextView) itemView.findViewById(R.id.location_text);
            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
            shareContentTextView = (TextView) itemView.findViewById(R.id.share_content_text);

            activeImg = (ImageView) itemView.findViewById(R.id.active_img);
            activeStateImg = (ImageView) itemView.findViewById(R.id.active_status_img);
            activeContentTextView = (TextView) itemView.findViewById(R.id.active_name_tv);
            activeTimeTextView = (TextView) itemView.findViewById(R.id.active_time_tv);
            activePlaceTextView = (TextView) itemView.findViewById(R.id.active_place_tv);
            activeCharge_tv = (TextView) itemView.findViewById(R.id.active_charge_tv);
            activeDistanceView = itemView.findViewById(R.id.active_distance_tv);
            activeApplyNumTextView = (TextView) itemView.findViewById(R.id.active_apply_num_tv);
            hobbyRecycleView = (RecyclerView) itemView.findViewById(R.id.recycler_hobby);
            hobbyRecycleView.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            hobbyRecycleView.setLayoutManager(gridLayoutManager);
            hobbyRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 3, false));
        }

    }

    class SharePostHolder extends BaseViewHolder {
        RelativeLayout rootLayout, shareDeleteLayout;
        View divider;
        ImageView userImg, img_first, video_play_img, alumnus_tv, deleteShareImg;
        TextView userNickNameTextView, fromUserNickNameTextView, tip_type_tv, userAgeTextView, timeTextView, locationTextView, cityTextView, shareContentTextView, contentTextView,
                labelFirstTextView, labelSecondTextView, shareNumTextView, praisNumTextView, commentNumTextView, deleteTextView, schoolNameTextView,recruitTextView,storeNameTextView;
        RelativeLayout linear_image_two, labelSecondLayout,storeLayout;
        RecyclerView recycler_image,recycler_post_label,recycler_store_label;
        ImageView postStickView;

        public SharePostHolder(View itemView) {
            super(itemView);
            shareDeleteLayout = (RelativeLayout) itemView.findViewById(R.id.delete_layout);
            deleteTextView = (TextView) itemView.findViewById(R.id.delete_text);
            deleteShareImg = (ImageView) itemView.findViewById(R.id.delete_share_img);
            rootLayout = (RelativeLayout) itemView.findViewById(R.id.share_layout);
            rootLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_fafafa));
            divider = itemView.findViewById(R.id.divider_line);
            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            userNickNameTextView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            fromUserNickNameTextView = (TextView) itemView.findViewById(R.id.form_user_nickname_text);
            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
            userAgeTextView = (TextView) itemView.findViewById(R.id.user_age_text);
            tip_type_tv = (TextView) itemView.findViewById(R.id.user_state_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            locationTextView = (TextView) itemView.findViewById(R.id.location_text);
            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
            shareContentTextView = (TextView) itemView.findViewById(R.id.share_content_text);
            contentTextView = (TextView) itemView.findViewById(R.id.content_text);
            storeLayout = (RelativeLayout) itemView.findViewById(R.id.store_layout);
            recruitTextView= (TextView) itemView.findViewById(R.id.recruit_text);
            storeNameTextView= (TextView) itemView.findViewById(R.id.store_name_text);

//            labelFirstTextView = (TextView) itemView.findViewById(R.id.post_label_one_text);
//            labelSecondTextView = (TextView) itemView.findViewById(R.id.post_label_two_text);
//            labelSecondLayout = (RelativeLayout) itemView.findViewById(R.id.post_label_two_layout);
            shareNumTextView = (TextView) itemView.findViewById(R.id.share_text);
            praisNumTextView = (TextView) itemView.findViewById(R.id.praise_text);
            commentNumTextView = (TextView) itemView.findViewById(R.id.comment_text);
//            postStickView = (ImageView) itemView.findViewById(R.id.post_stick_view);

            linear_image_two = (RelativeLayout) itemView.findViewById(R.id.linear_image_two);
            img_first = (ImageView) itemView.findViewById(R.id.img_first);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img_first.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
            img_first.setLayoutParams(layoutParams);
            video_play_img = (ImageView) itemView.findViewById(R.id.video_play_img);
            recycler_image = (RecyclerView) itemView.findViewById(R.id.recycler_image);
            recycler_post_label = (RecyclerView) itemView.findViewById(R.id.recycler_post_label);
            recycler_store_label = (RecyclerView) itemView.findViewById(R.id.store_label_recycle_view);

            recycler_image.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recycler_image.setLayoutManager(gridLayoutManager);
            recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));

            FlowLayoutManager flowLayoutManager=new FlowLayoutManager(mActivity);
            recycler_post_label.setLayoutManager(flowLayoutManager);
//            LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
//            viewHolder.recycler_post_label.setLayoutManager(linearLayoutManager2);

            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
            recycler_store_label.setLayoutManager(linearLayoutManager);
        }

    }

    class SharePKViewHolder extends BaseViewHolder {
        RelativeLayout rootLayout, shareDeleteLayout;
        View divider;
        ImageView userImg, alumnus_tv, deleteShareImg;
        ImageView pkVideoImageView, pkVideoPlayView;
        TextView userNickNameTextView, fromUserNickNameTextView, tip_type_tv, userAgeTextView, timeTextView, locationTextView, cityTextView, shareContentTextView, contentTextView,
                pkTitleView, pkContentVeiw, deleteTextView, schoolNameTextView;

        public SharePKViewHolder(View itemView) {
            super(itemView);
            shareDeleteLayout = (RelativeLayout) itemView.findViewById(R.id.delete_layout);
            deleteTextView = (TextView) itemView.findViewById(R.id.delete_text);
            deleteShareImg = (ImageView) itemView.findViewById(R.id.delete_share_img);
            rootLayout = (RelativeLayout) itemView.findViewById(R.id.share_layout);
            rootLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_fafafa));
            divider = itemView.findViewById(R.id.divider_line);
            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            userNickNameTextView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            fromUserNickNameTextView = (TextView) itemView.findViewById(R.id.form_user_nickname_text);
            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
            userAgeTextView = (TextView) itemView.findViewById(R.id.user_age_text);
            tip_type_tv = (TextView) itemView.findViewById(R.id.user_state_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            locationTextView = (TextView) itemView.findViewById(R.id.location_text);
            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
            shareContentTextView = (TextView) itemView.findViewById(R.id.share_content_text);
            contentTextView = (TextView) itemView.findViewById(R.id.content_text);


            pkVideoImageView = (ImageView) itemView.findViewById(R.id.img_bg);
            pkVideoPlayView = (ImageView) itemView.findViewById(R.id.video_play_img);
            pkTitleView = (TextView) itemView.findViewById(R.id.pk_title_text);
            pkContentVeiw = (TextView) itemView.findViewById(R.id.pk_content_text);


        }
    }

    class ShareOrganizationHolder extends BaseViewHolder {
        RelativeLayout rootLayout, shareDeleteLayout;
        View divider;
        ImageView userImg, orgainImg, alumnus_tv, deleteShareImg;
        TextView userNickNameTextView, fromUserNickNameTextView, timeTextView, tip_type_tv, locationTextView, cityTextView, userAgeTextView, orgainNameTextView,
                orgainUserNumTextView, orgainPostNumTextView, orgainJoinTextView, shareContentTextView, contentTextView, deleteTextView, schoolNameTextView;

        public ShareOrganizationHolder(View itemView) {
            super(itemView);
            shareDeleteLayout = (RelativeLayout) itemView.findViewById(R.id.delete_layout);
            deleteTextView = (TextView) itemView.findViewById(R.id.delete_text);
            deleteShareImg = (ImageView) itemView.findViewById(R.id.delete_share_img);
            rootLayout = (RelativeLayout) itemView.findViewById(R.id.share_layout);
            rootLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_fafafa));
            divider = itemView.findViewById(R.id.divider_line);
            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            userNickNameTextView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
//            fromUserNickNameTextView = (TextView) itemView.findViewById(R.id.form_user_nickname_text);
            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
            userAgeTextView = (TextView) itemView.findViewById(R.id.user_age_text);
            tip_type_tv = (TextView) itemView.findViewById(R.id.user_state_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            locationTextView = (TextView) itemView.findViewById(R.id.location_text);
            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
            shareContentTextView = (TextView) itemView.findViewById(R.id.share_content_text);
//            contentTextView = (TextView) itemView.findViewById(R.id.content_text);

            orgainImg = (ImageView) itemView.findViewById(R.id.orgain_img);
            orgainNameTextView = (TextView) itemView.findViewById(R.id.orgain_name_text);
            orgainUserNumTextView = (TextView) itemView.findViewById(R.id.orgain_user_num_text);
            orgainPostNumTextView = (TextView) itemView.findViewById(R.id.orgain_post_num_text);
            orgainJoinTextView = (TextView) itemView.findViewById(R.id.orgain_join_text);

        }

    }

    class ShareRoomViewHolder extends BaseViewHolder {
        RelativeLayout rootLayout, shareDeleteLayout;
        View divider;
        ImageView userImg;
        ImageView roomImageView, alumnus_tv, deleteShareImg;
        TextView userNickNameTextView, fromUserNickNameTextView, userAgeTextView, tip_type_tv, timeTextView, locationTextView, roomNameView,
                cityTextView, shareContentTextView, deleteTextView, schoolNameTextView;
        RelativeLayout tagLayout;
        TextView tagListView;

        public ShareRoomViewHolder(View itemView) {
            super(itemView);
            shareDeleteLayout = (RelativeLayout) itemView.findViewById(R.id.delete_layout);
            deleteTextView = (TextView) itemView.findViewById(R.id.delete_text);
            deleteShareImg = (ImageView) itemView.findViewById(R.id.delete_share_img);
            rootLayout = (RelativeLayout) itemView.findViewById(R.id.share_layout);
            rootLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_fafafa));
            divider = itemView.findViewById(R.id.divider_line);
            userImg = (ImageView) itemView.findViewById(R.id.user_img);
            userNickNameTextView = (TextView) itemView.findViewById(R.id.user_nickname_text);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
            fromUserNickNameTextView = (TextView) itemView.findViewById(R.id.form_user_nickname_text);
            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
            userAgeTextView = (TextView) itemView.findViewById(R.id.user_age_text);
            tip_type_tv = (TextView) itemView.findViewById(R.id.user_state_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            locationTextView = (TextView) itemView.findViewById(R.id.location_text);
            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
            shareContentTextView = (TextView) itemView.findViewById(R.id.share_content_text);
//            contentTextView = (TextView) itemView.findViewById(R.id.content_text);

            roomImageView = (ImageView) itemView.findViewById(R.id.room_image_view);
            tagLayout = (RelativeLayout) itemView.findViewById(R.id.tag_layout);
            tagListView = (TextView) itemView.findViewById(R.id.tag_text_view);
            roomNameView = (TextView) itemView.findViewById(R.id.room_name_view);
        }
    }


    public interface OnClick {
        void onPraise(long postId, int type, int position, int operationType);//operationType1:帖子点赞2：表白点赞

        void onOrgain(long orgainId);

        void onDelete(long postId, int position);

        void onDeleteShare(long shareId, int position);

        void onJoinClick(int position, int type);

        void onApplyClerk(long storeId);
    }

    public void onDestroy() {
        if (mActivity != null) {
            mActivity = null;
        }

        if (list != null) {
            list.clear();
        }
        list = null;
    }

    public OneToOneCharOnClick oneToOneCharOnClick;

    public void setOneToOneCharOnClick(OneToOneCharOnClick oneToOneCharOnClick) {
        this.oneToOneCharOnClick = oneToOneCharOnClick;
    }

    public interface OneToOneCharOnClick {
        void onClick(long toUserId);
    }

}