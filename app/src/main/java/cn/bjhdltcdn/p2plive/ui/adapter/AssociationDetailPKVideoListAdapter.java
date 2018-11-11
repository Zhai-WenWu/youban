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
//import com.orhanobut.logger.Logger;
//
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OriginalInfo;
//import cn.bjhdltcdn.p2plive.model.PlayInfo;
//import cn.bjhdltcdn.p2plive.model.PostInfo;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.model.StoreInfo;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailPKVideoListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ClassMateHelpDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ClassMateHelpListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKParkActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PostDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.SayLoveDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.SayLoveListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
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
// * Created by xiawenquan on 18/3/3.
// */
//
//public class AssociationDetailPKVideoListAdapter extends BaseAdapter {
//
//    /**
//     * 1帖子
//     */
//    private final int ITEM_1 = 1;
//    /**
//     * 5pk挑战
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
//    private RequestOptions gifOptions,matchOptions;
//
//
//    private ItemListener itemListener;
//
//    public void setItemListener(ItemListener itemListener) {
//        this.itemListener = itemListener;
//    }
//
//    // 圈子下帖子活动列表
//    // 圈子下帖子活动列表
//    private List<HomeInfo> list;
//    private AppCompatActivity mActivity;
//    private RequestOptions options;
//    private RequestOptions userOptions;
//    private AssociationDetailListAdapter.ItemWidgetOnClick itemWidgetOnClick;
//
//    public void setItemWidgetOnClick(AssociationDetailListAdapter.ItemWidgetOnClick itemWidgetOnClick) {
//        this.itemWidgetOnClick = itemWidgetOnClick;
//    }
//
//    public AssociationDetailPKVideoListAdapter(List<HomeInfo> list, AppCompatActivity mActivity) {
//        this.list = list;
//        this.mActivity = mActivity;
//
//        gifOptions = new RequestOptions().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).centerCrop().transform(new GlideRoundTransform( 9)).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        userOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon).error(R.mipmap.error_user_icon);
//        options = new RequestOptions().centerCrop().transform(new GlideRoundTransform( 9)).placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg);
//        matchOptions= new RequestOptions().placeholder(R.mipmap.post_match_error_icon).error(R.mipmap.post_match_error_icon);
//        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
//
//    }
//
//
//    private int screenWidth;
//
//    /**
//     * @return the mList
//     */
//    public List<HomeInfo> getList() {
//        return list;
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
//
//            HomeInfo homeInfo = list.get(position);
//            return homeInfo.getType();
//
//        }
//
//
//        return super.getItemViewType(position);
//    }
//
//
//    @Override
//    public int getCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list == null || list.size() == 0 ? null : list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup arg2) {
//
//        if (getCount() == 0) {
//            return null;
//        }
//
//
//        PostInfoItemViewHolder postInfoItemViewHolder = null;
//        PKItemViewHolder pkItemViewHolder = null;
//        AssociationDetailListAdapter.SayLoveItemViewHolder sayLoveItemViewHolder = null;
//        AssociationDetailListAdapter.ClassMateHelpItemViewHolder classMateHelpItemViewHolder = null;
//
//        final HomeInfo homeInfo = list.get(position);
//
//        int itemType = getItemViewType(position);
//        Logger.d("itemType == " + itemType);
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
//                case ITEM_8:
//
//
//                    convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.home_recommend_post_list_item_layout, null);
//                    sayLoveItemViewHolder = new AssociationDetailListAdapter.SayLoveItemViewHolder();
//                    convertView.setTag(R.layout.home_recommend_post_list_item_layout, sayLoveItemViewHolder);
//                    bindSayLoveItemViewHolder(convertView, sayLoveItemViewHolder);
//
//                    break;
//
//
//                case ITEM_9:
//
//                    convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.schoolmate_list_item_layout, null);
//                    classMateHelpItemViewHolder = new AssociationDetailListAdapter.ClassMateHelpItemViewHolder();
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
//                case ITEM_5:
//
//                    pkItemViewHolder = (PKItemViewHolder) convertView.getTag(R.layout.association_pk_list_item_layout);
//
//                    break;
//
//                case ITEM_8:
//
//                    sayLoveItemViewHolder = (AssociationDetailListAdapter.SayLoveItemViewHolder) convertView.getTag(R.layout.home_recommend_post_list_item_layout);
//
//                    break;
//
//                case ITEM_9:
//
//                    classMateHelpItemViewHolder = (AssociationDetailListAdapter.ClassMateHelpItemViewHolder) convertView.getTag(R.layout.schoolmate_list_item_layout);
//
//                    break;
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
//            case ITEM_5:
//
//                bindPKData(pkItemViewHolder, homeInfo, position);
//                break;
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
//        }
//
//
//        return convertView;
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
//        final BaseUser baseUser = postInfo.getBaseUser() != null ? postInfo.getBaseUser() : homeInfo.getBaseUser();
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
//
//                // 城市
//                viewHolder.cityTextView.setText(baseUser.getCity());
//
//                // 距离
//                viewHolder.locationTextView.setText(baseUser.getDistance());
//
//                viewHolder.userLevelTextView.setVisibility(View.VISIBLE);
//
//            } else {// 匿名
//
//                viewHolder.alumnusView.setVisibility(View.GONE);
//
//                viewHolder.userAgeTextView.setText("");
//
//                viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//
//                viewHolder.cityTextView.setText("");
//
//                viewHolder.locationTextView.setText("");
//
//                viewHolder.userLevelTextView.setVisibility(View.GONE);
//            }
//
//
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
//
//        //招聘信息
//        final StoreInfo storeInfo=postInfo.getStoreInfo();
//        if(storeInfo!=null){
//            viewHolder.recruitTextView.setVisibility(View.VISIBLE);
//            viewHolder.contentTextView.setPadding(0,0,0,0);
//        }else{
//            viewHolder.recruitTextView.setVisibility(View.GONE);
//            viewHolder.contentTextView.setPadding(0,Utils.dp2px(15),0,0);
//        }
//        viewHolder.recruitTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                if(storeInfo.getUserId()!=SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)){
////                    itemWidgetOnClick.onApplyClerk(storeInfo.getStoreId());
////                }
//                //跳转到店铺详情
//                Intent intent=new Intent(mActivity,ShopDetailActivity.class);
//                intent.putExtra(Constants.Fields.STORE_ID,storeInfo.getStoreId());
//                mActivity.startActivity(intent);
//            }
//        });
//
//        //跟拍对象
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
//                } else {
//
//                    if (postInfo != null) {
//                        mActivity.startActivity(new Intent(mActivity, PostDetailActivity.class)
//                                .putExtra(Constants.KEY.KEY_OBJECT, postInfo)
//                                .putExtra(Constants.KEY.KEY_POSITION, position)
//                                .putExtra(Constants.Fields.COME_IN_TYPE, 1));
//                    }
//
//                }
//            }
//        });
//
//        viewHolder.postLabelTwoLayout.setVisibility(View.INVISIBLE);
//        viewHolder.labelFirstTextView.setVisibility(View.INVISIBLE);
//
//        // 跳转到圈子资料
//        viewHolder.labelSecondTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.onOrgain(postInfo.getOrganId(), position);
//                }
//
//            }
//        });
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
//        // 分享
//        viewHolder.shareTextView.setText(Utils.bigNumberToStr(postInfo.getShareNumber(), postInfo.getShareNumberStr()));
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
//            Glide.with(App.getInstance()).load(postInfo.getVideoImageUrl()).apply(options).into(viewHolder.imgFirst);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.imgFirst.getLayoutParams();
//            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//            viewHolder.imgFirst.setLayoutParams(layoutParams);
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
//                    layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                    viewHolder.imgFirst.setLayoutParams(layoutParams);
//                    Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.imgFirst);
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
//                    PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity,imageList, 3, Utils.dp2px(9));
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
//        viewHolder.moreImg.setVisibility(View.VISIBLE);
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
//        final BaseUser baseUser = playInfo.getBaseUser() != null ? playInfo.getBaseUser() : homeInfo.getBaseUser();
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
//            // 学校
//            pkHolder.schoolTextView.setText(baseUser.getSchoolName());
//
//            if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                //校友 并且不是自己
//                pkHolder.alumnusView.setVisibility(View.VISIBLE);
//            } else {
//                pkHolder.alumnusView.setVisibility(View.GONE);
//            }
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
//        Glide.with(App.getInstance()).load(playInfo.getVideoImageUrl()).apply(options).into(pkHolder.imageView);
//
//        // 更多按钮
//        pkHolder.moreImg.setVisibility(View.VISIBLE);
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
//       /* pkHolder.imageLayout.setOnClickListener(new View.OnClickListener() {
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
//    private void bindClassMateHelpData(AssociationDetailListAdapter.ClassMateHelpItemViewHolder viewHolder, final HomeInfo homeInfo, final int position) {
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
//        final BaseUser baseUser = helpInfo.getBaseUser() != null ? helpInfo.getBaseUser() : homeInfo.getBaseUser();
//        if (baseUser != null) {
//            // 头像
//            Glide.with(mActivity).load(baseUser.getUserIcon()).apply(options).into(viewHolder.user_img);
//            // 昵称
//            viewHolder.user_nickname_text.setText(baseUser.getNickName());
//            // 学校名称
//            viewHolder.tv_school_name.setText(baseUser.getSchoolName());
//
//            // 显示校友标识
//            int isSchoolmate = baseUser.getIsSchoolmate();
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
//        viewHolder.fromTagView.setVisibility(View.VISIBLE);
//        viewHolder.fromTagLayoutView.setVisibility(View.VISIBLE);
//        viewHolder.fromTagLayoutView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //同学帮帮忙
//                if (mActivity != null) {
//                    Intent intent = new Intent(mActivity, ClassMateHelpListActivity.class);
//                    intent.putExtra(Constants.Fields.TYPE, 1);
//                    mActivity.startActivity(intent);
//                }
//
//            }
//        });
//        // 发布时间
//        viewHolder.time_text.setText(helpInfo.getAddTime());
//        // 显示距离
//        viewHolder.distance_text.setText(helpInfo.getDistance());
//
//
//        //跟拍人以及内容
//        String content = helpInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.content_text.setText(content);
//            viewHolder.content_text.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.content_text.setVisibility(View.GONE);
//        }
//
//
//        final OriginalInfo originalInfo = helpInfo.getOriginalInfo();
//        //标签展示
//        long labelId = helpInfo.getLabelId();
//        String labelName = helpInfo.getLabelName();
//        if (helpInfo.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
//            //跟拍加参赛
//            viewHolder.content_text.setVisibility(View.VISIBLE);
//            viewHolder.content_text.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, helpInfo.getScondlabelList(), content));
//        } else {
//            //只有跟拍
//            if (helpInfo.getIsOriginal() == 1 && originalInfo != null) {
//                viewHolder.content_text.setVisibility(View.VISIBLE);
//                viewHolder.content_text.setText(Utils.getFollowContent(originalInfo, content));
//            }
//            //只有参赛
//            if (!StringUtils.isEmpty(labelName)) {
//                viewHolder.content_text.setVisibility(View.VISIBLE);
//                viewHolder.content_text.setText(Utils.getMatchContent(labelId, labelName, helpInfo.getScondlabelList(), content));
//            }
//        }
//        viewHolder.content_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (helpInfo.getIsOriginal() == 1 && originalInfo != null) {
//                    //跳转到视频广场界面
//                    Intent intent = new Intent(mActivity, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, helpInfo.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID,originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_HELPINFO);
//                    mActivity.startActivity(intent);
//                } else {
//
//                    HelpInfo helpInfo = homeInfo.getHelpInfo();
//                    Intent intent = new Intent(mActivity, ClassMateHelpDetailActivity.class);
//                    intent.putExtra(Constants.Fields.POSITION, position);
//                    intent.putExtra(Constants.KEY.KEY_OBJECT, helpInfo);
//                    mActivity.startActivity(intent);
//
//
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
//                        Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
//                    } else {
//                        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                        layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                        viewHolder.img_first.setLayoutParams(layoutParams);
//                        Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.img_first);
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
//                    PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity,imageList, 3, Utils.dp2px(9));
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
//            Glide.with(App.getInstance()).load(helpInfo.getVideoImageUrl()).apply(options).into(viewHolder.img_first);
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
//    private void bindSayLoveItemData(AssociationDetailListAdapter.SayLoveItemViewHolder viewHolder, final HomeInfo homeInfo, final int position) {
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
//
//        //跟拍人以及内容
//        String content = sayLoveInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.contentTextView.setText(content);
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.contentTextView.setVisibility(View.GONE);
//        }
//
//
//        final OriginalInfo originalInfo = sayLoveInfo.getOriginalInfo();
//        //标签展示
//        long labelId = sayLoveInfo.getLabelId();
//        String labelName = sayLoveInfo.getLabelName();
//        if (sayLoveInfo.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
//            //跟拍加参赛
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//            viewHolder.contentTextView.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, sayLoveInfo.getScondlabelList(), content));
//        } else {
//            //只有跟拍
//            if (sayLoveInfo.getIsOriginal() == 1 && originalInfo != null) {
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getFollowContent(originalInfo, content));
//            }
//            //只有参赛
//            if (!StringUtils.isEmpty(labelName)) {
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getMatchContent(labelId, labelName, sayLoveInfo.getScondlabelList(), content));
//            }
//        }
//        viewHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sayLoveInfo.getIsOriginal() == 1 && originalInfo != null) {
//                    //跳转到视频广场界面
//                    Intent intent = new Intent(mActivity, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, sayLoveInfo.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID, originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_SAYLOVE);
//                    mActivity.startActivity(intent);
//                } else {
//
//                    if (sayLoveInfo != null) {
//                        //跳转到表白详情页
//                        Intent intent = new Intent(mActivity, SayLoveDetailActivity.class);
//                        intent.putExtra(Constants.KEY.KEY_SAYLOVE_ID, sayLoveInfo.getSayLoveId());
//                        intent.putExtra(Constants.Fields.POSITION, position);
//                        mActivity.startActivity(intent);
//                    }
//
//
//                }
//            }
//        });
//
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
//        viewHolder.labelFirstTextView.setVisibility(View.GONE);
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
//
//        viewHolder.labelFirstTextView.setText("");
//        viewHolder.labelFirstTextView.setVisibility(View.VISIBLE);
//
//        viewHolder.labelSecondTextView.setText("校园表白墙");
//        viewHolder.labelTwoTextTipsView.setText("");
//        viewHolder.postLabelTwoLayout.setVisibility(View.VISIBLE);
//        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) viewHolder.postLabelTwoLayout.getLayoutParams();
//        layoutParams1.leftMargin = 0;
//        viewHolder.postLabelTwoLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //附近表白
//                Intent intent = new Intent(mActivity, SayLoveListActivity.class);
//                intent.putExtra(Constants.Fields.USER_ID, 0);
//                intent.putExtra("comeInType", 1);
//                mActivity.startActivity(intent);
//            }
//        });
//
//        // 图片显示区域
//        viewHolder.recycler_image.setVisibility(View.GONE);
//        viewHolder.linear_image_two.setVisibility(View.GONE);
//        if (sayLoveInfo.getConfessionType() == 2) {
//            viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//            viewHolder.video_play_img.setVisibility(View.VISIBLE);
//            Glide.with(App.getInstance()).load(sayLoveInfo.getVideoImageUrl()).apply(options).into(viewHolder.img_first);
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
//                        Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.img_first);
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
//                            Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.img_first);
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
//                    PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity,imageList, 3, Utils.dp2px(9));
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
//        if (position == 0) {
//            viewHolder.divider.setVisibility(View.GONE);
//        } else {
//            viewHolder.divider.setVisibility(View.VISIBLE);
//        }
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
//    private void bindPostInfoViewHolder(View convertView, PostInfoItemViewHolder viewHolder) {
//
//        viewHolder.convertView = convertView;
//        viewHolder.userImg = (ImageView) convertView.findViewById(R.id.user_img);
//        viewHolder.schoolTextView = (TextView) convertView.findViewById(R.id.school_text_view);
//        viewHolder.isMatchImg= (ImageView) convertView.findViewById(R.id.ismatch_img);
//        viewHolder.userNickNameTextView = (TextView) convertView.findViewById(R.id.user_nickname_text);
//        viewHolder.userLevelTextView = (ImageView) convertView.findViewById(R.id.user_level_text);
//        viewHolder.userAgeTextView = (TextView) convertView.findViewById(R.id.user_age_text);
//        viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time_text);
//        viewHolder.alumnusView = (ImageView) convertView.findViewById(R.id.alumnus_text);
//        viewHolder.cityTextView = (TextView) convertView.findViewById(R.id.city_text);
//        viewHolder.locationTextView = (TextView) convertView.findViewById(R.id.location_text);
//        viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.content_text);
//        viewHolder.recruitTextView= (TextView) convertView.findViewById(R.id.recruit_text);
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
//        viewHolder.postLabelTwoLayout.setVisibility(View.GONE);
//
//
//    }
//
//    private void bindSayLoveItemViewHolder(View convertView, AssociationDetailListAdapter.SayLoveItemViewHolder viewHolder) {
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
//    private void bindSchoolMateHelpItemViewHolder(View convertView, AssociationDetailListAdapter.ClassMateHelpItemViewHolder viewHolder) {
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
//    private void bindPKItemViewHolder(View convertView, PKItemViewHolder viewHolder) {
//        viewHolder.convertView = convertView;
//
//        viewHolder.convertView = convertView;
//        viewHolder.moreImg = convertView.findViewById(R.id.more_img);
//        viewHolder.userImg = convertView.findViewById(R.id.user_img);
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
//        viewHolder.imageView = convertView.findViewById(R.id.img_bg);
//        viewHolder.videoPlayImg = convertView.findViewById(R.id.video_play_img);
//        viewHolder.imageLayout = convertView.findViewById(R.id.image_layout);
//        viewHolder.schoolTextView = (TextView) convertView.findViewById(R.id.school_text_view);
//
//        viewHolder.lineView2 = convertView.findViewById(R.id.line_view_2);
//
//    }
//
//    static class PostInfoItemViewHolder {
//        View convertView;
//        ImageView userImg, imgFirst, videoPlayImg,isMatchImg;
//        TextView userNickNameTextView, userAgeTextView, timeTextView, locationTextView, contentTextView,recruitTextView,
//                labelFirstTextView, labelSecondTextView, praisNumTextView, commentNumTextView;
//        ImageView userLevelTextView;
//        ImageView postStickView;
//        TextView schoolTextView;
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
//        ImageView imageView;
//        ImageView videoPlayImg;
//        View imageLayout;
//
//        TextView cityTextView;
//
//        View lineView2;
//
//    }
//
//}
