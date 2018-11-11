//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.ApiData;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OrganBaseInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.OriginalInfo;
//import cn.bjhdltcdn.p2plive.model.PlayInfo;
//import cn.bjhdltcdn.p2plive.model.PostInfo;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.ui.activity.ActiveListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationTypeListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ClassMateHelpListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKParkActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.SayLoveListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
//import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Created by xiawenquan on 17/11/25.
// */
//
//public class AssociationDetailListAdapter extends BaseAdapter {
//
//    //类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请,8表白,9同学帮帮忙,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白)
//    /**
//     * 1 帖子
//     */
//    private final int ITEM_1 = 1;
//    /**
//     * 3 活动
//     */
//    private final int ITEM_3 = 3;
//    /**
//     * 5 pk挑战
//     */
//    private final int ITEM_5 = 5;
//
//    /**
//     * 8 表白
//     */
//    private final int ITEM_8 = 8;
//
//    /**
//     * 9 同学帮帮忙
//     */
//    private final int ITEM_9 = 9;
//
//
//    /**
//     * 圈子类型(0-->普通圈子，1-->官方圈子，2-->学校圈子),
//     */
//    private int orgType;
//
//    // 圈子下帖子活动列表
//    private List<HomeInfo> list;
//    private AppCompatActivity mActivity;
//    private RequestOptions options, userOptions, gifOptions, matchOptions;
//
//
//    private ItemListener itemListener;
//
//    public void setItemListener(ItemListener itemListener) {
//        this.itemListener = itemListener;
//    }
//
//    private ItemWidgetOnClick itemWidgetOnClick;
//    private int screenWidth;
//
//    public void setItemWidgetOnClick(ItemWidgetOnClick itemWidgetOnClick) {
//        this.itemWidgetOnClick = itemWidgetOnClick;
//    }
//
//    public AssociationDetailListAdapter(AppCompatActivity mActivity, int orgType) {
//        this.mActivity = mActivity;
//        this.orgType = orgType;
//        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).transform(new GlideRoundTransform( 9));
//
//        gifOptions = new RequestOptions()
//                .centerCrop()
//                .transform(new GlideRoundTransform( 9))
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//
//
//        userOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon).error(R.mipmap.error_user_icon);
//        matchOptions = new RequestOptions()
//                .placeholder(R.mipmap.post_match_error_icon)
//                .error(R.mipmap.post_match_error_icon);
//
//        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
//    }
//
//    @Override
//    public int getCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    public void setList(List<HomeInfo> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }
//
//    public void setListAll(List<HomeInfo> list) {
//
//        if (this.list == null) {
//            return;
//        }
//
//        if (list == null) {
//            return;
//        }
//
//        this.list.addAll(list);
//
//        notifyDataSetChanged();
//
//    }
//
//    public void addItem(HomeInfo homeInfo) {
//
//        if (homeInfo == null) {
//            return;
//        }
//
//
//        if (this.list == null) {
//            this.list = new ArrayList<>(1);
//        }
//        this.list.add(0, homeInfo);
//
//        notifyDataSetChanged();
//
//    }
//
//
//    public void deleteItem(int position) {
//
//        if (position < 0) {
//            return;
//        }
//
//        this.list.remove(position);
//
//        notifyDataSetChanged();
//
//    }
//
//
//    @Override
//    public Object getItem(int position) {
//        return (list == null || list.size() == 0) ? null : list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    /**
//     * @return the mList
//     */
//    public List<HomeInfo> getList() {
//        return list;
//    }
//
//
//    /**
//     * 该方法返回多少个不同的布局
//     */
//    @Override
//    public int getViewTypeCount() {
//        return 10;
//    }
//
//
//    @Override
//    public int getItemViewType(int position) {
//
//        if (getCount() > 0) {
//            HomeInfo homeInfo = list.get(position);
//            return homeInfo.getType();
//        }
//        return super.getItemViewType(position);
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup arg2) {
//
//        PostInfoItemViewHolder postInfoItemViewHolder = null;
//        ActiveItemViewHolder activeItemViewHolder = null;
//        PKItemViewHolder pkItemViewHolder = null;
//        SayLoveItemViewHolder sayLoveItemViewHolder = null;
//        ClassMateHelpItemViewHolder classMateHelpItemViewHolder = null;
//
//        final HomeInfo homeInfo = list.get(position);
//
//        int itemType = getItemViewType(position);
//        if (convertView == null) {
//            switch (itemType) {//类型(1帖子,2圈子,3活动)
//
//                case ITEM_1:
//
//                    convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.association_post_list_item_layout, null);
//                    postInfoItemViewHolder = new PostInfoItemViewHolder();
//                    convertView.setTag(R.layout.association_post_list_item_layout, postInfoItemViewHolder);
//                    bindPostInfoViewHolder(convertView, postInfoItemViewHolder);
//
//                    break;
//
//
//                case ITEM_3:
//
//                    convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.association_active_list_item_layout, null);
//                    activeItemViewHolder = new ActiveItemViewHolder();
//                    convertView.setTag(R.layout.association_active_list_item_layout, activeItemViewHolder);
//                    bindActiveItemViewHolder(convertView, activeItemViewHolder);
//
//                    break;
//
//                case ITEM_5:
//
//                    convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.association_pk_list_item_layout, null);
//                    pkItemViewHolder = new PKItemViewHolder();
//                    convertView.setTag(R.layout.association_pk_list_item_layout, pkItemViewHolder);
//                    bindPKItemViewHolder(convertView, pkItemViewHolder);
//
//
//                    break;
//
//
//                case ITEM_8:
//
//                    convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.home_recommend_post_list_item_layout, null);
//                    sayLoveItemViewHolder = new SayLoveItemViewHolder();
//                    convertView.setTag(R.layout.home_recommend_post_list_item_layout, sayLoveItemViewHolder);
//                    bindSayLoveItemViewHolder(convertView, sayLoveItemViewHolder);
//
//                    break;
//
//                case ITEM_9:
//
//                    convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.schoolmate_list_item_layout, null);
//                    classMateHelpItemViewHolder = new ClassMateHelpItemViewHolder();
//                    convertView.setTag(R.layout.schoolmate_list_item_layout, classMateHelpItemViewHolder);
//                    bindSchoolMateHelpItemViewHolder(convertView, classMateHelpItemViewHolder);
//
//                    break;
//
//            }
//
//
//        } else {
//
//            switch (itemType) {//类型(1帖子,3活动,5 pk ,11分享帖子)
//
//                case ITEM_1:
//
//                    postInfoItemViewHolder = (PostInfoItemViewHolder) convertView.getTag(R.layout.association_post_list_item_layout);
//
//                    break;
//
//                case ITEM_3:
//                    activeItemViewHolder = (ActiveItemViewHolder) convertView.getTag(R.layout.association_active_list_item_layout);
//                    break;
//
//                case ITEM_5:
//
//                    pkItemViewHolder = (PKItemViewHolder) convertView.getTag(R.layout.association_pk_list_item_layout);
//
//                    break;
//
//
//                case ITEM_8:
//
//                    sayLoveItemViewHolder = (SayLoveItemViewHolder) convertView.getTag(R.layout.home_recommend_post_list_item_layout);
//
//                    break;
//
//                case ITEM_9:
//
//                    classMateHelpItemViewHolder = (ClassMateHelpItemViewHolder) convertView.getTag(R.layout.schoolmate_list_item_layout);
//
//                    break;
//
//            }
//
//        }
//
//        // 绑定数据
//        switch (itemType) {//类型(1帖子,2圈子,3活动)
//
//            case ITEM_1:
//
//                bindPostInfoData(postInfoItemViewHolder, homeInfo, position);
//
//                break;
//
//            case ITEM_3:
//                bindActiveItemData(activeItemViewHolder, homeInfo, position);
//                break;
//
//
//            case ITEM_5:
//
//                bindPKData(pkItemViewHolder, homeInfo, position);
//                break;
//
//
//            case ITEM_8:
//
//                bindSayLoveItemData(sayLoveItemViewHolder, homeInfo, position);
//
//                break;
//
//            case ITEM_9:
//
//                bindClassMateHelpData(classMateHelpItemViewHolder, homeInfo, position);
//
//                break;
//
//
//        }
//
//        if (convertView != null) {
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (itemListener != null) {
//                        itemListener.onItemClick(view, position);
//                    }
//                }
//            });
//            return convertView;
//        } else {
//            return new View(mActivity);
//        }
//    }
//
//
//    private void bindPostInfoData(PostInfoItemViewHolder viewHolder, final HomeInfo homeInfo, final int position) {
//
//        if (viewHolder == null || homeInfo == null) {
//            return;
//        }
//
//        viewHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getCount() - 1) {
//            viewHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//
//        final PostInfo postInfo = homeInfo.getPostInfo();
//
//        if (postInfo == null) {
//            return;
//        }
//
//
//        final BaseUser baseUser = postInfo.getBaseUser();
//        if (baseUser != null) {
//            // 头像
//            Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(viewHolder.userImg);
//
//            // 学校
//            viewHolder.schoolTextView.setText(baseUser.getSchoolName());
//
//            // 昵称
//            viewHolder.userNickNameTextView.setText(baseUser.getNickName());
//
//            //是否匿名发布帖子(1--->匿名，2--->不匿名)
//            if (postInfo.getIsAnonymous() == 2) {// 实名
//                if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //校友 并且不是自己
//                    viewHolder.alumnusView.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.alumnusView.setVisibility(View.GONE);
//                }
//
//                // 性别 1 男；2女
//                viewHolder.userAgeTextView.setText(baseUser.getAge() + "岁");
//                int sex = baseUser.getSex();
//                if (sex == 1) {
//                    //男性
//                    viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//                } else if (sex == 2) {
//                    //女性
//                    viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//                }
//                viewHolder.userLevelTextView.setVisibility(View.VISIBLE);
//                // 城市
//                viewHolder.cityTextView.setText(baseUser.getCity());
//                viewHolder.cityTextView.setPadding(Utils.dp2px(10), 0, 0, 0);
//                // 距离
//                viewHolder.locationTextView.setText(baseUser.getDistance());
//
//            } else {// 匿名
//
//                viewHolder.alumnusView.setVisibility(View.GONE);
//
//                viewHolder.userAgeTextView.setText("");
//
//                viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//
//                viewHolder.userLevelTextView.setVisibility(View.GONE);
//                // 城市
//                viewHolder.cityTextView.setText(baseUser.getCity());
//                viewHolder.cityTextView.setPadding(0, 0, 0, 0);
//                // 距离
//                viewHolder.locationTextView.setText(baseUser.getDistance());
//            }
//        }
//
//
//        // 用户级别
//        Utils.getActiveLevel(viewHolder.userLevelTextView, postInfo.getActiveLevel());
//
//        // 时间
//        viewHolder.timeTextView.setText(postInfo.getAddTime());
//
//        //跟拍人以及内容
//        String content=postInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.contentTextView.setText(content);
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.contentTextView.setVisibility(View.GONE);
//        }
//        final OriginalInfo originalInfo=postInfo.getOriginalInfo();
//        //标签展示
//        long labelId=postInfo.getLabelId();
//        String labelName=postInfo.getLabelName();
//        if(postInfo.getIsOriginal()==1&&originalInfo!=null&&!StringUtils.isEmpty(labelName)){
//            //跟拍加参赛
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//            viewHolder.contentTextView.setText(Utils.getFollowAndMatchContent(originalInfo,labelName,postInfo.getScondlabelList(),content));
//        }else{
//            //只有跟拍
//            if(postInfo.getIsOriginal()==1&&originalInfo!=null){
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getFollowContent(originalInfo,content));
//            }
//            //只有参赛
//            if(!StringUtils.isEmpty(labelName)){
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getMatchContent(labelId,labelName,postInfo.getScondlabelList(),content));
//            }
//        }
//        viewHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(postInfo.getIsOriginal()==1&&originalInfo!=null) {
//                    //跳转到视频广场界面
//                    Intent intent = new Intent(mActivity, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, postInfo.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID, originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_POST);
//                    mActivity.startActivity(intent);
//                }
//            }
//        });
//
//
//        if(orgType==2){
//            List<OrganBaseInfo> organBaseInfoList=postInfo.getOrganList();
//            if(organBaseInfoList!=null&&organBaseInfoList.size()>0){
//                final OrganBaseInfo organBaseInfo=organBaseInfoList.get(0);
//                // 一级标签
//                viewHolder.labelFirstTextView.setText(organBaseInfo.getHobbyName());
//
//                // 二级标签
//                viewHolder.labelSecondTextView.setText(organBaseInfo.getOrganName());
//                //只有学校圈子才显示圈子标签
//                viewHolder.labelFirstTextView.setVisibility(View.VISIBLE);
//                viewHolder.postLabelTwoLayout.setVisibility(View.VISIBLE);
//                viewHolder.postLabelTwoLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //跳转到圈子详情
//                        OrganizationInfo organizationInfo = new OrganizationInfo();
//                        organizationInfo.setOrganId(organBaseInfo.getOrganId());
//                        organizationInfo.setOrganName(organBaseInfo.getOrganName());
//                        mActivity.startActivity(new Intent(mActivity, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//                    }
//                });
//            }
//
//        }
//
//
//        //是否置顶(0取消,1置顶)
//        if (postInfo.getIsTop() == 1) {
//            viewHolder.postStickView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.postStickView.setVisibility(View.INVISIBLE);
//        }
//
//
//        // 分享
//        viewHolder.shareTextView.setText(Utils.bigNumberToStr(postInfo.getShareNumber(), postInfo.getShareNumberStr()));
//
//        viewHolder.shareTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String imgUrl = "";
//
//                if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                    imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                }
//                ShareUtil.getInstance().showShare(mActivity, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//            }
//        });
//
//        // 点赞
//        viewHolder.praisNumTextView.setText(Utils.bigNumberToStr(postInfo.getPraiseCount(), postInfo.getPraiseCountStr()));
//        final int isPraise = postInfo.getIsPraise();
//        if (isPraise == 1) {
//            //1 : 已点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        } else {
//            //0: 未点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        }
//
//        // 点赞事件
//        viewHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //点赞 (1 点赞  2 取消点赞)
//                int type = (isPraise == 0 | isPraise == 2) ? 1 : 2;
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.onPraise(postInfo.getPostId(), type, position);
//                }
//                postInfo.setIsPraise(type);
//                postInfo.setPraiseCount(postInfo.getPraiseCount() + (type == 1 ? 1 : -1));
//                notifyDataSetChanged();
//
//            }
//        });
//
//        // 评论数量
//        viewHolder.commentNumTextView.setText(Utils.bigNumberToStr(postInfo.getCommentCount(), postInfo.getCommentCountStr()));
//
//        // 图片显示区域
//        viewHolder.recyclerImage.setVisibility(View.GONE);
//        viewHolder.linearImageTwo.setVisibility(View.GONE);
//        viewHolder.imgFirst.setOnClickListener(null);
//
//        if (postInfo.getTopicType() == 2) {
//            //视频
//            viewHolder.linearImageTwo.setVisibility(View.VISIBLE);
//            viewHolder.videoPlayImg.setVisibility(View.VISIBLE);
//
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.imgFirst.getLayoutParams();
//            if (layoutParams != null) {
//                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                viewHolder.imgFirst.setLayoutParams(layoutParams);
//            }
//
//            Glide.with(App.getInstance()).asBitmap().load(postInfo.getVideoImageUrl()).apply(options).into(viewHolder.imgFirst);
//
//
//            viewHolder.imgFirst.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //imgFirst
//                    Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_POST);
//                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, postInfo.getPostId());
//                    mActivity.startActivity(intent);
//                }
//            });
//        } else {
//
//            final List<Image> imageList = postInfo.getImageList();
//            if (imageList != null && imageList.size() > 0) {
//
//                if (imageList.size() == 1) {// 显示一张图片的样式
//                    viewHolder.linearImageTwo.setVisibility(View.VISIBLE);
//                    viewHolder.videoPlayImg.setVisibility(View.GONE);
//
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.imgFirst.getLayoutParams();
//                    if (layoutParams != null) {
//                        if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
//                            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                            viewHolder.imgFirst.setLayoutParams(layoutParams);
//                            Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.imgFirst);
//                        } else {
//                            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                            viewHolder.imgFirst.setLayoutParams(layoutParams);
//                            Glide.with(App.getInstance()).asBitmap().load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.imgFirst);
//                        }
//
//                    }
//
//                    viewHolder.imgFirst.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (mActivity != null && !mActivity.isFinishing()) {
//                                ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
//                            }
//                        }
//                    });
//
//
//                } else {// 显示多张图片的样式
//                    PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
//                    viewHolder.recyclerImage.setAdapter(postImageLIstAdapter);
//
//                    postImageLIstAdapter.setOnItemListener(new ItemListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//
//                            if (mActivity != null && !mActivity.isFinishing()) {
//                                ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
//                            }
//
//                        }
//                    });
//
//                    viewHolder.recyclerImage.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//
//
//        // 更多按钮
//        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//
//    }
//
//
//    private void bindActiveItemData(ActiveItemViewHolder activeHolder, final HomeInfo homeInfo, final int position) {
//
//        if (activeHolder == null || homeInfo == null) {
//            return;
//        }
//
//        final ActivityInfo activityInfo = homeInfo.getActivityInfo();
//        if (activityInfo == null) {
//            return;
//        }
//
//        activeHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getCount() - 1) {
//            activeHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//        // 跳转到附近活动
//        activeHolder.activetopView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mActivity != null) {
//
//                    Intent intent = new Intent(mActivity, ActiveListActivity.class);
//                    intent.putExtra(Constants.Fields.TO_USER_ID, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//                    intent.putExtra(Constants.Fields.COME_IN_TYPE, 1);
//                    mActivity.startActivity(intent);
//
//
//                }
//            }
//        });
//
//        // 用户信息
//        final BaseUser baseUser = homeInfo.getBaseUser();
//        if (baseUser != null) {
//            // 用户头像
//            Glide.with(mActivity).asBitmap().apply(userOptions).load(baseUser.getUserIcon()).into(activeHolder.userImg);
//            activeHolder.userImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        //跳到用户详情
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    }
//                }
//            });
//
//            // 学校
//            activeHolder.schoolTextView.setText(baseUser.getSchoolName());
//
//            if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                //校友 并且不是自己
//                activeHolder.alumnusView.setVisibility(View.VISIBLE);
//            } else {
//                activeHolder.alumnusView.setVisibility(View.GONE);
//            }
//
//            // 用户昵称
//            activeHolder.nickNameView.setText(baseUser.getNickName());
//
//            int sex = baseUser.getSex();
//            if (sex == 1) {
//                //男性
//                activeHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//            } else if (sex == 2) {
//                //女性
//                activeHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//            }
//            activeHolder.userAgeView.setText(baseUser.getAge() + "岁");
//
//            // 城市
//            activeHolder.cityTextView.setText(baseUser.getCity());
//
//            // 位置
//            activeHolder.locationView.setText(baseUser.getDistance());
//
//        }
//
//        // 用户级别
//        Utils.getActiveLevel(activeHolder.userLevelView, activityInfo.getActiveLevel());
//
//        // 活动时间
//        activeHolder.timeTextView.setText(activityInfo.getActivityTime());
//
//        // 活动状态(1报名中,2报名结束)
//        activeHolder.activeStatusImgView.setVisibility(View.INVISIBLE);
//        int status = activityInfo.getStatus();
//        if (status == 2) {
//            activeHolder.activeStatusImgView.setVisibility(View.VISIBLE);
//            activeHolder.activeStatusImgView.setImageResource(R.mipmap.active_status_unenable_icon);
//        }
//
//        // 是否置顶(0取消,1置顶)
//        if (activityInfo.getIsTop() == 0) {
//            activeHolder.topView.setVisibility(View.INVISIBLE);
//        } else {
//            activeHolder.topView.setVisibility(View.VISIBLE);
//        }
//
//        // 活动的兴起标签
//        List<HobbyInfo> hobbyInfoList = activityInfo.getHobbyList();
//        if (hobbyInfoList != null) {
//            ActiveListHobbyRecyclerViewAdapter hobbyRecyclerViewAdapter = new ActiveListHobbyRecyclerViewAdapter(mActivity);
//            hobbyRecyclerViewAdapter.setList(hobbyInfoList);
//            activeHolder.hobbyRecycleView.setAdapter(hobbyRecyclerViewAdapter);
//        }
//
//
//        final ActiveListOrganizationRecyclerViewAdapter activeListOrganizationRecyclerViewAdapter = new ActiveListOrganizationRecyclerViewAdapter(mActivity);
//        activeListOrganizationRecyclerViewAdapter.setList((activityInfo.getOrganList() != null && activityInfo.getOrganList().size() > 3) ? activityInfo.getOrganList().subList(0, 3) : activityInfo.getOrganList());
//        activeHolder.orgainzationRecycleView.setAdapter(activeListOrganizationRecyclerViewAdapter);
//        activeListOrganizationRecyclerViewAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳转到圈子详情
//                OrganizationInfo organizationInfo = activeListOrganizationRecyclerViewAdapter.getItem(position);
//                mActivity.startActivity(new Intent(mActivity, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//            }
//        });
//
//        if (activityInfo.getOrganList() != null && activityInfo.getOrganList().size() > 2) {
//            activeHolder.moreImgView.setVisibility(View.VISIBLE);
//        } else {
//            activeHolder.moreImgView.setVisibility(View.INVISIBLE);
//        }
//
//        // 圈子更多标签
//        activeHolder.moreImgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到圈子列表页面
//                Intent intent = new Intent(mActivity, AssociationTypeListActivity.class);
//                if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //我的圈子列表
//                    intent.putExtra(Constants.Fields.TYPE, 2);
//                } else {
//                    //他的圈子列表
//                    intent.putExtra(Constants.Fields.TYPE, 3);
//                }
//                intent.putExtra(Constants.Fields.Ta_USER_ID, baseUser.getUserId());
//                mActivity.startActivity(intent);
//
//            }
//        });
//
//
//        // 图片显示
//        activeHolder.activeImgView.setOnClickListener(null);
//        String imgUrl = null;
//        if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//            imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//            activeHolder.activeImgView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mActivity != null && !mActivity.isFinishing()) {
//                        ImageViewPageDialog.newInstance(activityInfo.getImageList(), 0).show(mActivity.getSupportFragmentManager());
//                    }
//                }
//            });
//        }
//
//        // 显示默认图
//        if (StringUtils.isEmpty(imgUrl)) {
//            String host = ApiData.getInstance().getHost();
//            host = host.substring(0, host.lastIndexOf("/"));
//            imgUrl = host + "/uploadfile/system/activity/activity_img.png";
//        }
//
//        Utils.CornerImageViewDisplayByUrl(imgUrl, activeHolder.activeImgView);
//
//        // 活动主题
//        activeHolder.activeContentView.setText(activityInfo.getTheme());
//
//        // 更多按钮
//        activeHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//
//    }
//
//
//    private void bindPKData(PKItemViewHolder pkHolder, final HomeInfo homeInfo, final int position) {
//
//        if (pkHolder == null || homeInfo == null) {
//            return;
//        }
//
//        final PlayInfo playInfo = homeInfo.getPlayInfo();
//        if (playInfo == null) {
//            return;
//        }
//
//        pkHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getCount() - 1) {
//            pkHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//
//        // 用户信息
//        final BaseUser baseUser = homeInfo.getBaseUser();
//        if (baseUser != null) {
//            // 用户头像
//            Glide.with(mActivity).asBitmap().apply(options).load(baseUser.getUserIcon()).into(pkHolder.userImg);
//            pkHolder.userImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        //跳到用户详情
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    }
//                }
//            });
//
//            if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                //校友 并且不是自己
//                pkHolder.alumnusView.setVisibility(View.VISIBLE);
//            } else {
//                pkHolder.alumnusView.setVisibility(View.GONE);
//            }
//
//            // 学校
//            pkHolder.schoolTextView.setText(baseUser.getSchoolName());
//
//            // 用户昵称
//            pkHolder.nickNameView.setText(baseUser.getNickName());
//
//            int sex = baseUser.getSex();
//            if (sex == 1) {
//                //男性
//                pkHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//            } else if (sex == 2) {
//                //女性
//                pkHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//            }
//            pkHolder.userAgeView.setText(baseUser.getAge() + "岁");
//
//            // 城市
//            pkHolder.cityTextView.setText(baseUser.getCity());
//
//            // 位置
//            pkHolder.locationView.setText(baseUser.getDistance());
//
//        }
//
//        // 用户级别
//        Utils.getActiveLevel(pkHolder.userLevelView, playInfo.getActiveLevel());
//
//        // 是否置顶(0取消,1置顶)
//        if (playInfo.getIsTop() == 0) {
//            pkHolder.topView.setVisibility(View.INVISIBLE);
//        } else {
//            pkHolder.topView.setVisibility(View.VISIBLE);
//        }
//
//        // 活动时间
//        pkHolder.timeTextView.setText(playInfo.getAddTime());
//
//        // pk标题
//        if (playInfo.getLaunchPlay() != null) {
//            pkHolder.pkTitleTextView.setText(playInfo.getLaunchPlay().getTitle());
//        }
//
//        // PK描述(个人用户编辑的内容)
//        pkHolder.pkContentTextView.setText(playInfo.getTitle());
//
//        // pk视频
//        Glide.with(App.getInstance()).asBitmap().load(playInfo.getVideoImageUrl()).apply(options).into(pkHolder.pkImageView);
//
//        // 更多按钮
//        pkHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//
//        // 全屏播发视频
//        /*pkHolder.imageLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playInfo.getLaunchPlay().setBaseUser(homeInfo.getBaseUser());
//                Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
//                intent.putExtra(Constants.Fields.PK_ID, playInfo.getPlayId());
//                mActivity.startActivity(intent);
//
//            }
//        });*/
//
//    }
//
//
//    private void bindSayLoveItemData(SayLoveItemViewHolder viewHolder, final HomeInfo homeInfo, final int position) {
//
//        if (viewHolder == null || homeInfo == null) {
//            return;
//        }
//
//        if (homeInfo.getSayLoveInfo() == null) {
//            return;
//        }
//
//        viewHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getCount() - 1) {
//            viewHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//
//        viewHolder.userLevelTextView.setVisibility(View.GONE);
//
//
//        final SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//        final BaseUser baseUser = sayLoveInfo.getBaseUser();
//
//        final int isAnonymous = sayLoveInfo.getIsAnonymous();
//
//        // 头像
//        Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(viewHolder.userImg);
//        viewHolder.userImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) && isAnonymous == 2) {
//                    //跳到用户详情(自己和匿名不可点击)
//                    mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                }
//            }
//        });
//
//        // 昵称
//        viewHolder.userNickNameTextView.setText(baseUser.getNickName());
//
//        //学校名称
//        if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
//            viewHolder.schoolNameTextView.setVisibility(View.VISIBLE);
//            viewHolder.schoolNameTextView.setText(baseUser.getSchoolName());
//        } else {
//            viewHolder.schoolNameTextView.setVisibility(View.GONE);
//        }
//
//        //校友
//        if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//            //校友 并且不是自己
//            viewHolder.alumnus_tv.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.alumnus_tv.setVisibility(View.GONE);
//        }
//
//        // 年龄
//        viewHolder.userAgeTextView.setText(baseUser.getAge() + "岁");
//
//        // 性别
//        int sex = baseUser.getSex();
//        if (sex == 1) {
//            //男性
//            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//        } else if (sex == 2) {
//            //女性
//            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//        }
//
//        // 发布时间
//        viewHolder.timeTextView.setText(sayLoveInfo.getAddTime());
//
//        // 距离
//        String distance = baseUser.getDistance();
//        if ((!StringUtils.isEmpty(distance))) {
//            viewHolder.locationTextView.setText(distance);
//            viewHolder.locationTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.locationTextView.setVisibility(View.GONE);
//        }
//
//        // 城市
//        if ((!StringUtils.isEmpty(baseUser.getCity()))) {
//            viewHolder.cityTextView.setText(baseUser.getCity());
//            viewHolder.cityTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.cityTextView.setVisibility(View.GONE);
//        }
//
//
////        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
////            //自己不展示位置
////            viewHolder.locationTextView.setVisibility(View.GONE);
////            viewHolder.cityTextView.setVisibility(View.GONE);
////        }
//
//        //跟拍人以及内容
//        String content=sayLoveInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.contentTextView.setText(content);
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.contentTextView.setVisibility(View.GONE);
//        }
//        //跟拍对象
//        final OriginalInfo originalInfo=sayLoveInfo.getOriginalInfo();
//        //标签展示
//        long labelId=sayLoveInfo.getLabelId();
//        String labelName=sayLoveInfo.getLabelName();
//        if(sayLoveInfo.getIsOriginal()==1&&originalInfo!=null&&!StringUtils.isEmpty(labelName)){
//            //跟拍加参赛
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//            viewHolder.contentTextView.setText(Utils.getFollowAndMatchContent(originalInfo,labelName,sayLoveInfo.getScondlabelList(),content));
//        }else{
//            //只有跟拍
//            if(sayLoveInfo.getIsOriginal()==1&&originalInfo!=null){
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getFollowContent(originalInfo,content));
//            }
//            //只有参赛
//            if(!StringUtils.isEmpty(labelName)){
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getMatchContent(labelId,labelName,sayLoveInfo.getScondlabelList(),content));
//            }
//        }
//        viewHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(sayLoveInfo.getIsOriginal()==1&&originalInfo!=null) {
//                    //跳转到视频广场界面
//                    Intent intent = new Intent(mActivity, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, sayLoveInfo.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID, originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_SAYLOVE);
//                    mActivity.startActivity(intent);
//                }
//            }
//        });
//
//        // 是否匿名
//        if (sayLoveInfo.getIsAnonymous() != 1) {
//            viewHolder.userAgeTextView.setVisibility(View.VISIBLE);
//            viewHolder.userAgeTextView.setPadding(0, 0, Utils.dp2px(10), 0);
//        } else {
//            viewHolder.userAgeTextView.setVisibility(View.VISIBLE);
//            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//            viewHolder.userAgeTextView.setText("");
//            viewHolder.userAgeTextView.setPadding(0, 0, 0, 0);
//        }
//
//
//        // 分享数量
//        int shareCount = sayLoveInfo.getShareNumber();
//        String shareCountStr = sayLoveInfo.getShareNumberStr();
//        viewHolder.shareNumTextView.setText(Utils.bigNumberToStr(shareCount, shareCountStr));
//
//        // 分享按钮事件
//        viewHolder.shareNumTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //分享弹窗
//                String imgUrl = "";
//                if (sayLoveInfo.getConfessionType() == 2) {
//                    imgUrl = sayLoveInfo.getVideoImageUrl();
//                } else {
//                    if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                        imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                    }
//                }
//                ShareUtil.getInstance().showShare(mActivity, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//            }
//        });
//
//        // 点赞
//        int praiseCount = sayLoveInfo.getPraiseCount();
//        String praiseCountStr = sayLoveInfo.getPraiseCountStr();
//        viewHolder.praisNumTextView.setText(Utils.bigNumberToStr(praiseCount, praiseCountStr));
//
//
//        final int isPraise = sayLoveInfo.getIsPraise();
//        if (isPraise == 1) {
//            //1 : 已点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        } else {
//            //0: 未点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        }
//
//        //点赞事件 (1 点赞  2 取消点赞),
//        viewHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.onPraise(sayLoveInfo.getSayLoveId(), homeInfo.getType(), position);
//                }
//
//            }
//        });
//
//        // 评论数量
//        int commentCount = sayLoveInfo.getCommentCount();
//        String commentCountStr = sayLoveInfo.getCommentCountStr();
//        viewHolder.commentNumTextView.setText(Utils.bigNumberToStr(commentCount, commentCountStr));
//
//        if (orgType == 2) {
//            // 一级标签
//            viewHolder.labelFirstTextView.setText("");
//            viewHolder.labelFirstTextView.setVisibility(View.VISIBLE);
//
//            viewHolder.labelSecondTextView.setText("校园表白墙");
//            viewHolder.labelTwoTextTipsView.setText("");
//            viewHolder.postLabelTwoLayout.setVisibility(View.VISIBLE);
//
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.postLabelTwoLayout.getLayoutParams();
//            layoutParams.leftMargin = 0;
//
//            viewHolder.postLabelTwoLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //附近表白
//                    Intent intent = new Intent(mActivity, SayLoveListActivity.class);
//                    intent.putExtra(Constants.Fields.USER_ID, 0);
//                    intent.putExtra("comeInType", 1);
//                    mActivity.startActivity(intent);
//                }
//            });
//
//        } else {
//            // 一级标签
//            // 二级标签
//
//            viewHolder.labelFirstTextView.setVisibility(View.INVISIBLE);
//            viewHolder.labelSecondTextView.setVisibility(View.INVISIBLE);
//        }
//
//
//        // 图片显示区域
//        viewHolder.recycler_image.setVisibility(View.GONE);
//        viewHolder.linear_image_two.setVisibility(View.GONE);
//        if (sayLoveInfo.getConfessionType() == 2) {
//            viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//            viewHolder.video_play_img.setVisibility(View.VISIBLE);
//            Utils.CornerImageViewDisplayByUrl(sayLoveInfo.getVideoImageUrl(), viewHolder.img_first);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//            viewHolder.img_first.setLayoutParams(layoutParams);
//            viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //跳转到视频播放界面
//                    Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_SAYLOVE);
//                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, sayLoveInfo.getSayLoveId());
//                    mActivity.startActivity(intent);
//                }
//            });
//        } else {
//            final List<Image> imageList = sayLoveInfo.getImageList();
//            if (imageList != null) {
//                if (imageList.size() > 0 && imageList.size() <= 1) {
//
//                    viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                    viewHolder.video_play_img.setVisibility(View.GONE);
//                    if (imageList.size() == 1) {
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//
//                        if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
//                            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                            viewHolder.img_first.setLayoutParams(layoutParams);
//                            Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
//                        } else {
//                            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                            viewHolder.img_first.setLayoutParams(layoutParams);
//                            Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.img_first);
//                        }
//
//
//                        viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (mActivity != null) {
//                                    ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
//                                }
//                            }
//                        });
//                    }
//                } else if (imageList.size() > 1) {
//                    viewHolder.linear_image_two.setVisibility(View.GONE);
//                    PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
//                    viewHolder.recycler_image.setAdapter(postImageLIstAdapter);
//
//                    postImageLIstAdapter.setOnItemListener(new ItemListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            if (mActivity != null) {
//                                ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
//                            }
//                        }
//                    });
//
//                    viewHolder.recycler_image.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//
////        if (position == 0) {
////            viewHolder.divider.setVisibility(View.GONE);
////        } else {
////            viewHolder.divider.setVisibility(View.VISIBLE);
////        }
//
//        // 更多按钮
//        viewHolder.moreImg.setVisibility(View.VISIBLE);
//        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//
//    }
//
//
//    private void bindClassMateHelpData(ClassMateHelpItemViewHolder viewHolder, final HomeInfo homeInfo, final int position) {
//        if (viewHolder == null || homeInfo == null) {
//            return;
//        }
//
//        if (homeInfo.getHelpInfo() == null) {
//            return;
//        }
//
//        final HelpInfo helpInfo = homeInfo.getHelpInfo();
//
//        viewHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getCount() - 1) {
//            viewHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//        // 用户信息
//        final BaseUser baseUser = homeInfo.getBaseUser();
//        if (baseUser != null) {
//            // 头像
//            Glide.with(mActivity).load(baseUser.getUserIcon()).apply(options).into(viewHolder.user_img);
//            // 昵称
//            viewHolder.user_nickname_text.setText(baseUser.getNickName());
//            // 学校名称
//            String schoolName = baseUser.getSchoolName();
//            viewHolder.tv_school_name.setText(schoolName);
//
//            //年龄
//            if (baseUser.getSex() == 1) {
//                //男性
//                viewHolder.user_age_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//            } else if (baseUser.getSex() == 2) {
//                //女性
//                viewHolder.user_age_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//            }
//
//            // 年龄
//            viewHolder.user_age_text.setText(helpInfo.getBaseUser().getAge() + "岁");
//            viewHolder.user_age_text.setPadding(0, Utils.dp2px(7), Utils.dp2px(10), 0);
//
//            // 显示校友标识
//            int isSchoolmate = baseUser.getIsSchoolmate();
//            if (isSchoolmate == 2) {
//                viewHolder.alumnus_text.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.alumnus_text.setVisibility(View.INVISIBLE);
//            }
//
//            // 点击跳转到用户详情
//            viewHolder.user_img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getInt(Constants.Fields.USER_ID, 0)) {
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    }
//                }
//            });
//        }
//
//        //城市
//        viewHolder.user_city.setText(helpInfo.getCity());
//
//        if (orgType == 2) {
//            viewHolder.fromTagView.setVisibility(View.VISIBLE);
//            viewHolder.fromTagLayoutView.setVisibility(View.VISIBLE);
//            viewHolder.fromTagLayoutView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //同学帮帮忙
//                    if (mActivity != null) {
//                        Intent intent = new Intent(mActivity, ClassMateHelpListActivity.class);
//                        intent.putExtra(Constants.Fields.TYPE, 1);
//                        mActivity.startActivity(intent);
//                    }
//
//                }
//            });
//
//        } else {
//            viewHolder.fromTagView.setVisibility(View.GONE);
//            viewHolder.fromTagLayoutView.setVisibility(View.GONE);
//        }
//
//        // 发布时间
//        viewHolder.time_text.setText(helpInfo.getAddTime());
//        // 显示距离
//        viewHolder.distance_text.setText(helpInfo.getDistance());
//
//        //跟拍人以及内容
//        String content=helpInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.content_text.setText(content);
//            viewHolder.content_text.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.content_text.setVisibility(View.GONE);
//        }
//        //跟拍对象
//        final OriginalInfo originalInfo=helpInfo.getOriginalInfo();
//        //标签展示
//        long labelId=helpInfo.getLabelId();
//        String labelName=helpInfo.getLabelName();
//        if(helpInfo.getIsOriginal()==1&&originalInfo!=null&&!StringUtils.isEmpty(labelName)){
//            //跟拍加参赛
//            viewHolder.content_text.setVisibility(View.VISIBLE);
//            viewHolder.content_text.setText(Utils.getFollowAndMatchContent(originalInfo,labelName,helpInfo.getScondlabelList(),content));
//
//        }else{
//            //只有跟拍
//            if(helpInfo.getIsOriginal()==1&&originalInfo!=null){
//                viewHolder.content_text.setVisibility(View.VISIBLE);
//                viewHolder.content_text.setText(Utils.getFollowContent(originalInfo,content));
//            }
//            //只有参赛
//            if(!StringUtils.isEmpty(labelName)){
//                viewHolder.content_text.setVisibility(View.VISIBLE);
//                viewHolder.content_text.setText(Utils.getMatchContent(labelId,labelName,helpInfo.getScondlabelList(),content));
//            }
//        }
//        viewHolder.content_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(helpInfo.getIsOriginal()==1&&originalInfo!=null) {
//                    //跳转到视频广场界面
//                    Intent intent = new Intent(mActivity, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, helpInfo.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID, originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_HELPINFO);
//                    mActivity.startActivity(intent);
//                }
//            }
//        });
//
//
//        // 普通文图
//        if (helpInfo.getHelpType() == 1) {
//            final List<Image> imageList = helpInfo.getImageList();
//            if (imageList != null) {
//                if (imageList.size() == 1) {
//                    viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                    viewHolder.video_play_img.setVisibility(View.GONE);
//                    viewHolder.recycler_image.setVisibility(View.GONE);
//
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//
//                    if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
//                        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                        layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                        viewHolder.img_first.setLayoutParams(layoutParams);
//                        Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
//
//                    } else {
//                        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                        layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                        viewHolder.img_first.setLayoutParams(layoutParams);
//                        Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
//                    }
//
//                    viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (mActivity != null) {
//                                ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
//                            }
//                        }
//                    });
//
//                } else if (imageList.size() > 1) {
//                    viewHolder.linear_image_two.setVisibility(View.GONE);
//                    viewHolder.recycler_image.setVisibility(View.VISIBLE);
//                    PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
//                    viewHolder.recycler_image.setAdapter(postImageLIstAdapter);
//                    postImageLIstAdapter.setOnItemListener(new ItemListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            if (mActivity != null) {
//                                ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
//                            }
//                        }
//                    });
//                }
//            } else {
//                viewHolder.linear_image_two.setVisibility(View.GONE);
//                viewHolder.recycler_image.setVisibility(View.GONE);
//            }
//
//        } else if (helpInfo.getHelpType() == 2) {//视频
//            viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//            viewHolder.video_play_img.setVisibility(View.VISIBLE);
//            viewHolder.recycler_image.setVisibility(View.GONE);
//            //视频
//            Utils.CornerImageViewDisplayByUrl(helpInfo.getVideoImageUrl(),viewHolder.img_first);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//            viewHolder.img_first.setLayoutParams(layoutParams);
//            viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //跳转到视频播放界面
//                    Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_HELPINFO);
//                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, helpInfo.getHelpId());
//                    mActivity.startActivity(intent);
//                }
//            });
//        }
//
//        // 点赞
//        viewHolder.praise_text.setText(Utils.bigNumberToStr(helpInfo.getPraiseCount(), helpInfo.getPraiseCountStr()));
//        if (helpInfo.getIsPraise() == 1) {
//            //1 : 已点赞
//            viewHolder.praise_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        } else {
//            //0: 未点赞
//            viewHolder.praise_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        }
//        viewHolder.praise_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.onPraise(helpInfo.getHelpId(), homeInfo.getType(), position);
//                }
//            }
//        });
//
//        // 评论数量
//        viewHolder.comment_text.setText(Utils.bigNumberToStr(helpInfo.getCommentCount(), helpInfo.getCommentCountStr()));
//
//        // 更多按钮
//        viewHolder.moreImg.setVisibility(View.VISIBLE);
//        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//    }
//
//
//    private void bindPostInfoViewHolder(View convertView, PostInfoItemViewHolder viewHolder) {
//
//        viewHolder.convertView = convertView;
//        viewHolder.userImg = (ImageView) convertView.findViewById(R.id.user_img);
//        viewHolder.schoolTextView = (TextView) convertView.findViewById(R.id.school_text_view);
//        viewHolder.userNickNameTextView = (TextView) convertView.findViewById(R.id.user_nickname_text);
//        viewHolder.userLevelTextView = (ImageView) convertView.findViewById(R.id.user_level_text);
//        viewHolder.isMatchImg = (ImageView) convertView.findViewById(R.id.ismatch_img);
//        viewHolder.userAgeTextView = (TextView) convertView.findViewById(R.id.user_age_text);
//        viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time_text);
//        viewHolder.alumnusView = (ImageView) convertView.findViewById(R.id.alumnus_text);
//        viewHolder.cityTextView = (TextView) convertView.findViewById(R.id.city_text);
//        viewHolder.locationTextView = (TextView) convertView.findViewById(R.id.location_text);
//        viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.content_text);
//
//        viewHolder.labelFirstTextView = (TextView) convertView.findViewById(R.id.post_label_one_text);
//        viewHolder.labelSecondTextView = (TextView) convertView.findViewById(R.id.post_label_two_text);
//        viewHolder.praisNumTextView = (TextView) convertView.findViewById(R.id.praise_text);
//        viewHolder.commentNumTextView = (TextView) convertView.findViewById(R.id.comment_text);
//        viewHolder.postStickView = (ImageView) convertView.findViewById(R.id.post_stick_view);
//        viewHolder.moreImg = (ImageView) convertView.findViewById(R.id.more_img);
//        viewHolder.shareTextView = (TextView) convertView.findViewById(R.id.share_text);
//
//
//        viewHolder.linearImageTwo = (RelativeLayout) convertView.findViewById(R.id.linear_image_two);
//        viewHolder.imgFirst = (ImageView) convertView.findViewById(R.id.img_first);
//        viewHolder.videoPlayImg = (ImageView) convertView.findViewById(R.id.video_play_img);
//        viewHolder.recyclerImage = (RecyclerView) convertView.findViewById(R.id.recycler_image);
//
//        viewHolder.recyclerImage.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//        gridLayoutManager.setAutoMeasureEnabled(true);
//        viewHolder.recyclerImage.setLayoutManager(gridLayoutManager);
//        viewHolder.recyclerImage.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));
//
//        viewHolder.lineView2 = convertView.findViewById(R.id.line_view_2);
//
//        viewHolder.postLabelTwoLayout = convertView.findViewById(R.id.post_label_two_layout);
//
//
//
//    }
//
//
//    private void bindActiveItemViewHolder(View convertView, ActiveItemViewHolder viewHolder) {
//
//        viewHolder.convertView = convertView;
//        viewHolder.activeTopLayout = convertView.findViewById(R.id.active_top_layout);
//        viewHolder.activetopView = convertView.findViewById(R.id.active_top_view);
//        viewHolder.moreImg = convertView.findViewById(R.id.more_img);
//        viewHolder.userImg = convertView.findViewById(R.id.user_img);
//        viewHolder.schoolTextView = (TextView) convertView.findViewById(R.id.school_text_view);
//        viewHolder.nickNameView = convertView.findViewById(R.id.user_nickname_text);
//        viewHolder.userLevelView = convertView.findViewById(R.id.user_level_img);
//        viewHolder.alumnusView = convertView.findViewById(R.id.alumnus_img);
//        viewHolder.timeTextView = convertView.findViewById(R.id.time_text);
//        viewHolder.userAgeView = convertView.findViewById(R.id.user_age_text);
//        viewHolder.cityTextView = (TextView) convertView.findViewById(R.id.city_text);
//        viewHolder.locationView = convertView.findViewById(R.id.location_text);
//        viewHolder.activeStatusImgView = convertView.findViewById(R.id.active_status_img);
//        viewHolder.topView = convertView.findViewById(R.id.post_stick_view);
//        viewHolder.activeImgView = convertView.findViewById(R.id.active_img);
//        viewHolder.activeContentView = convertView.findViewById(R.id.active_name_tv);
//
//        viewHolder.hobbyRecycleView = (RecyclerView) convertView.findViewById(R.id.recycler_hobby);
//        viewHolder.hobbyRecycleView.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 4);
//        gridLayoutManager.setAutoMeasureEnabled(true);
//        viewHolder.hobbyRecycleView.setLayoutManager(gridLayoutManager);
//        viewHolder.hobbyRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 4, false));
//
//        viewHolder.orgainzationRecycleView = convertView.findViewById(R.id.recycler_orgainzation);
//        viewHolder.orgainzationRecycleView.setHasFixedSize(true);
//
//        GridLayoutManager orgainzationlayout = new GridLayoutManager(App.getInstance(), 3);
//        gridLayoutManager.setAutoMeasureEnabled(true);
//
//        viewHolder.orgainzationRecycleView.setLayoutManager(orgainzationlayout);
//        viewHolder.orgainzationRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(10), 3, false));
//
//        viewHolder.moreImgView = convertView.findViewById(R.id.orgainzation_more_image);
//
//
//        viewHolder.lineView2 = convertView.findViewById(R.id.line_view_2);
//
//    }
//
//    private void bindPKItemViewHolder(View convertView, PKItemViewHolder viewHolder) {
//
//        viewHolder.convertView = convertView;
//        viewHolder.moreImg = convertView.findViewById(R.id.more_img);
//        viewHolder.userImg = convertView.findViewById(R.id.user_img);
//        viewHolder.schoolTextView = convertView.findViewById(R.id.school_text_view);
//        viewHolder.nickNameView = convertView.findViewById(R.id.user_nickname_text);
//        viewHolder.userLevelView = convertView.findViewById(R.id.user_level_img);
//        viewHolder.alumnusView = convertView.findViewById(R.id.alumnus_img);
//        viewHolder.timeTextView = convertView.findViewById(R.id.add_time_view);
//        viewHolder.userAgeView = convertView.findViewById(R.id.user_age_text);
//        viewHolder.cityTextView = convertView.findViewById(R.id.city_text);
//        viewHolder.locationView = convertView.findViewById(R.id.location_text);
//        viewHolder.topView = convertView.findViewById(R.id.post_stick_view);
//        viewHolder.pkTitleTextView = convertView.findViewById(R.id.pk_title_text);
//        viewHolder.pkContentTextView = convertView.findViewById(R.id.pk_content_text);
//        viewHolder.pkImageView = convertView.findViewById(R.id.img_bg);
//        viewHolder.videoPlayImg = convertView.findViewById(R.id.video_play_img);
//        viewHolder.imageLayout = convertView.findViewById(R.id.image_layout);
//
//        viewHolder.lineView2 = convertView.findViewById(R.id.line_view_2);
//
//    }
//
//    private void bindSayLoveItemViewHolder(View convertView, SayLoveItemViewHolder viewHolder) {
//        viewHolder.convertView = convertView;
//        viewHolder.userNickNameTextView = (TextView) convertView.findViewById(R.id.user_nickname_text);
//        viewHolder.schoolNameTextView = (TextView) convertView.findViewById(R.id.school_name_text_view);
//        viewHolder.deleteImg = (ImageView) convertView.findViewById(R.id.delete_img);
//        viewHolder.alumnus_tv = (ImageView) convertView.findViewById(R.id.alumnus_text);
//        viewHolder.userLevelTextView = (ImageView) convertView.findViewById(R.id.user_level_text);
//        viewHolder.userAgeTextView = (TextView) convertView.findViewById(R.id.user_age_text);
//        viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time_text);
//        viewHolder.moreImg = convertView.findViewById(R.id.more_img);
//        viewHolder.locationTextView = (TextView) convertView.findViewById(R.id.location_text);
//        viewHolder.cityTextView = (TextView) convertView.findViewById(R.id.city_text);
//        viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.content_text);
//
//        viewHolder.labelFirstTextView = (TextView) convertView.findViewById(R.id.post_label_one_text);
//        viewHolder.labelTwoTextTipsView = (TextView) convertView.findViewById(R.id.post_label_two_text_tips_view);
//        viewHolder.postLabelTwoLayout = convertView.findViewById(R.id.post_label_two_layout);
//        viewHolder.labelSecondTextView = convertView.findViewById(R.id.post_label_two_text);
//        viewHolder.postLabelTwoLayout.setVisibility(View.GONE);
//
//        viewHolder.shareNumTextView = (TextView) convertView.findViewById(R.id.share_text);
//        viewHolder.praisNumTextView = (TextView) convertView.findViewById(R.id.praise_text);
//        viewHolder.commentNumTextView = (TextView) convertView.findViewById(R.id.comment_text);
//        viewHolder.postStickView = (ImageView) convertView.findViewById(R.id.post_stick_view);
//        viewHolder.postStickView.setVisibility(View.INVISIBLE);
//
//        viewHolder.userImg = (ImageView) convertView.findViewById(R.id.user_img);
//        viewHolder.linear_image_two = (RelativeLayout) convertView.findViewById(R.id.linear_image_two);
//        viewHolder.img_first = (ImageView) convertView.findViewById(R.id.img_first);
//        viewHolder.video_play_img = (ImageView) convertView.findViewById(R.id.video_play_img);
//        viewHolder.recycler_image = (RecyclerView) convertView.findViewById(R.id.recycler_image);
//        viewHolder.divider = (View) convertView.findViewById(R.id.divider_line);
//        viewHolder.recycler_image.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//        gridLayoutManager.setAutoMeasureEnabled(true);
//        viewHolder.recycler_image.setLayoutManager(gridLayoutManager);
//        viewHolder.recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));
//
//        viewHolder.lineView2 = convertView.findViewById(R.id.line_view_2);
//
//
//    }
//
//    private void bindSchoolMateHelpItemViewHolder(View convertView, ClassMateHelpItemViewHolder viewHolder) {
//        viewHolder.convertView = convertView;
//
//        viewHolder.moreImg = convertView.findViewById(R.id.more_img);
//        viewHolder.user_img = convertView.findViewById(R.id.user_img);
//        viewHolder.user_nickname_text = convertView.findViewById(R.id.user_nickname_text);
//        viewHolder.tv_school_name = convertView.findViewById(R.id.tv_school_name);
//        viewHolder.alumnus_text = convertView.findViewById(R.id.alumnus_text);
//        viewHolder.time_text = convertView.findViewById(R.id.time_text);
//        viewHolder.distance_text = convertView.findViewById(R.id.distance_text);
//        viewHolder.content_text = convertView.findViewById(R.id.content_text);
//        viewHolder.linear_image_two = convertView.findViewById(R.id.linear_image_two);
//        viewHolder.img_first = convertView.findViewById(R.id.img_first);
//        viewHolder.video_play_img = convertView.findViewById(R.id.video_play_img);
//        viewHolder.recycler_image = convertView.findViewById(R.id.recycler_image);
//        viewHolder.delete_img = convertView.findViewById(R.id.delete_img);
//        viewHolder.delete_img.setVisibility(View.GONE);
//        viewHolder.praise_text = convertView.findViewById(R.id.praise_text);
//        viewHolder.comment_text = convertView.findViewById(R.id.comment_text);
//        viewHolder.user_age_text = convertView.findViewById(R.id.user_age_text);
//        viewHolder.user_city = convertView.findViewById(R.id.user_city);
//
//        viewHolder.fromTagView = convertView.findViewById(R.id.from_tag_view);
//        viewHolder.fromTagLayoutView = convertView.findViewById(R.id.from_tag_layout_view);
//
//        viewHolder.recycler_image.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//        gridLayoutManager.setAutoMeasureEnabled(true);
//        viewHolder.recycler_image.setLayoutManager(gridLayoutManager);
//        viewHolder.recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));
//
//        viewHolder.lineView2 = convertView.findViewById(R.id.line_view_2);
//
//    }
//
//
//    static class PostInfoItemViewHolder {
//        View convertView;
//        ImageView userImg, imgFirst, videoPlayImg, isMatchImg;
//        TextView userNickNameTextView, userAgeTextView, timeTextView, locationTextView, contentTextView,
//                labelFirstTextView, labelSecondTextView, praisNumTextView, commentNumTextView;
//
//        TextView schoolTextView;
//
//        ImageView userLevelTextView;
//        ImageView postStickView;
//
//        RelativeLayout linearImageTwo;
//        RecyclerView recyclerImage;
//
//        ImageView alumnusView;
//        ImageView moreImg;
//        TextView shareTextView;
//
//        TextView cityTextView;
//        View lineView2;
//
//        RelativeLayout postLabelTwoLayout;
//
//    }
//
//
//    static class ActiveItemViewHolder {
//        View convertView;
//        TextView activeTopLayout;
//        ImageView moreImg;
//        CircleImageView userImg;
//        TextView schoolTextView;
//        ImageView userLevelView;
//        ImageView alumnusView;
//        TextView nickNameView;
//        TextView timeTextView;
//        TextView userAgeView;
//        TextView locationView;
//        ImageView activeStatusImgView;
//        ImageView topView;
//        ImageView activeImgView;
//        TextView activeContentView;
//        RecyclerView hobbyRecycleView;
//        ImageView moreImgView;
//        TextView cityTextView;
//        TextView activetopView;
//        View lineView2;
//
//        RecyclerView orgainzationRecycleView;
//
//
//    }
//
//    static class PKItemViewHolder {
//        View convertView;
//        ImageView moreImg;
//        CircleImageView userImg;
//        TextView schoolTextView;
//        ImageView userLevelView;
//        ImageView alumnusView;
//        TextView nickNameView;
//        TextView timeTextView;
//        TextView userAgeView;
//        TextView locationView;
//        ImageView topView;
//
//        TextView pkTitleTextView;
//        TextView pkContentTextView;
//
//        ImageView pkImageView;
//        ImageView videoPlayImg;
//        View imageLayout;
//
//        TextView cityTextView;
//
//        View lineView2;
//
//    }
//
//    static class ClassMateHelpItemViewHolder {
//        View convertView;
//        public CircleImageView user_img;
//        public TextView user_nickname_text;
//        public TextView tv_school_name;
//        public ImageView alumnus_text;
//        public ImageView moreImg;
//        public TextView time_text;
//        public TextView distance_text;
//        public TextView content_text;
//        public RelativeLayout linear_image_two;
//        public ImageView img_first;
//        public ImageView video_play_img;
//        public RecyclerView recycler_image;
//        public ImageView delete_img;
//        public TextView praise_text;
//        public TextView comment_text;
//        public TextView user_age_text;
//        public TextView user_city;
//        public View lineView2;
//        public TextView fromTagView;
//        public View fromTagLayoutView;
//
//    }
//
//    static class SayLoveItemViewHolder {
//        View convertView;
//        ImageView userImg;
//        ImageView img_first;
//        ImageView video_play_img;
//        ImageView alumnus_tv;
//        ImageView userLevelTextView;
//        ImageView deleteImg;
//
//        TextView userNickNameTextView;
//        TextView userAgeTextView;
//        TextView timeTextView;
//        ImageView moreImg;
//        TextView locationTextView;
//        TextView cityTextView;
//        TextView contentTextView;
//        TextView labelFirstTextView;
//        TextView shareNumTextView;
//        TextView praisNumTextView;
//        TextView commentNumTextView;
//        TextView schoolNameTextView;
//        RelativeLayout linear_image_two;
//        RecyclerView recycler_image;
//        View divider;
//        ImageView postStickView;
//
//        View postLabelTwoLayout;
//
//        TextView labelSecondTextView;
//        TextView labelTwoTextTipsView;
//
//        View lineView2;
//    }
//
//    public interface ItemWidgetOnClick {
//        void onPraise(long postId, int type, int position);
//
//        void onOrgain(long orgainId, int position);
//
//        void moreImg(int type, int position);
//
//        void onApplyClerk(long storeId);
//    }
//
//
//}
