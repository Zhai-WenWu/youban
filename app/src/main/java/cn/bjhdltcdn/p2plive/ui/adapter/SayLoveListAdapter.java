//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
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
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OriginalInfo;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.ui.activity.PKParkActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.SayLoveDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.SayLoveListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
//import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
//import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
//public class SayLoveListAdapter extends BaseAdapter {
//    private List<SayLoveInfo> list;
//    private AppCompatActivity mActivity;
//    private RequestOptions options, userOptions, gifOptions;
//    private OnClick onClick;
//    private int screenWidth;
//    private int type;//1:表白列表2：我的表白
//    private boolean refreshItem;
//    private UserPresenter userPresenter;
//
//    public void setUserPresenter(UserPresenter userPresenter) {
//        this.userPresenter = userPresenter;
//    }
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public SayLoveListAdapter(AppCompatActivity mActivity) {
//        this.mActivity = mActivity;
//        options = new RequestOptions()
//                .centerCrop()
//                .transform(new GlideRoundTransform(9))
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg);
//        userOptions = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.error_user_icon)
//                .error(R.mipmap.error_user_icon);
//
//        gifOptions = new RequestOptions()
//                .centerCrop()
//                .transform(new GlideRoundTransform(9))
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//
//        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
//    }
//
//    public void setRefreshItem(boolean refreshItem) {
//        this.refreshItem = refreshItem;
//    }
//
//    public void setOnClick(OnClick onClick) {
//        this.onClick = onClick;
//    }
//
//    @Override
//    public int getCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    public void setList(List<SayLoveInfo> list) {
//        this.list = list;
//    }
//
//    public void addList(List<SayLoveInfo> list) {
//        this.list.addAll(list);
//    }
//
//    public void addSayLoveInfo(SayLoveInfo sayLoveInfo) {
//
//        if (sayLoveInfo == null) {
//            return;
//        }
//
//        if (this.list == null) {
//            this.list = new ArrayList<>(1);
//        }
//
//        this.list.add(0, sayLoveInfo);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public SayLoveInfo getItem(int position) {
//        return list == null || list.size() == 0 ? null : list.get(position);
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
//    public List<SayLoveInfo> getList() {
//        return list;
//    }
//
//    public void updateCommentNum(int position, int commentNum) {
//        list.get(position).setCommentCount(commentNum);
//        refreshItem=true;
//        notifyDataSetChanged();
//    }
//
//    public void updateIsPraise(int position, int isPraise) {
//        SayLoveInfo sayLoveInfo = list.get(position);
//        if (isPraise == 1) {//点赞
//            sayLoveInfo.setIsPraise(1);
//            sayLoveInfo.setPraiseCount(sayLoveInfo.getPraiseCount() + 1);
//        } else {
//            //取消点赞
//            sayLoveInfo.setIsPraise(0);
//            sayLoveInfo.setPraiseCount(sayLoveInfo.getPraiseCount() - 1);
//        }
//        refreshItem=true;
//        notifyDataSetChanged();
//    }
//
//    public void deleteItem(int position) {
//        list.remove(position);
//        notifyDataSetChanged();
//    }
//
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup arg2) {
//        final ViewHolder viewHolder;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.home_recommend_post_list_item_layout, null);
//            viewHolder = new ViewHolder();
//            viewHolder.userNickNameTextView = (TextView) convertView.findViewById(R.id.user_nickname_text);
//            viewHolder.schoolNameTextView = (TextView) convertView.findViewById(R.id.school_name_text_view);
//            viewHolder.deleteImg = (ImageView) convertView.findViewById(R.id.delete_img);
//            viewHolder.alumnus_tv = (ImageView) convertView.findViewById(R.id.alumnus_text);
//            viewHolder.userLevelTextView = (ImageView) convertView.findViewById(R.id.user_level_text);
//            viewHolder.userAgeTextView = (TextView) convertView.findViewById(R.id.user_age_text);
//            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time_text);
//            viewHolder.locationTextView = (TextView) convertView.findViewById(R.id.location_text);
//            viewHolder.cityTextView = (TextView) convertView.findViewById(R.id.city_text);
//            viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.content_text);
//            viewHolder.labelFirstTextView = (TextView) convertView.findViewById(R.id.post_label_one_text);
//            viewHolder.labelSecondLayout = (RelativeLayout) convertView.findViewById(R.id.post_label_two_layout);
//            viewHolder.shareNumTextView = (TextView) convertView.findViewById(R.id.share_text);
//            viewHolder.praisNumTextView = (TextView) convertView.findViewById(R.id.praise_text);
//            viewHolder.commentNumTextView = (TextView) convertView.findViewById(R.id.comment_text);
//            viewHolder.postStickView = (ImageView) convertView.findViewById(R.id.post_stick_view);
//            viewHolder.postStickView.setVisibility(View.INVISIBLE);
//
//            viewHolder.userImg = (ImageView) convertView.findViewById(R.id.user_img);
//            viewHolder.linear_image_two = (RelativeLayout) convertView.findViewById(R.id.linear_image_two);
//            viewHolder.img_first = (ImageView) convertView.findViewById(R.id.img_first);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//            viewHolder.img_first.setLayoutParams(layoutParams);
//            viewHolder.video_play_img = (ImageView) convertView.findViewById(R.id.video_play_img);
//            viewHolder.recycler_image = (RecyclerView) convertView.findViewById(R.id.recycler_image);
//            viewHolder.divider = (View) convertView.findViewById(R.id.divider_line);
//            viewHolder.recycler_image.setHasFixedSize(true);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//            gridLayoutManager.setAutoMeasureEnabled(true);
//            viewHolder.recycler_image.setLayoutManager(gridLayoutManager);
//            viewHolder.recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.userLevelTextView.setVisibility(View.GONE);
//        final SayLoveInfo sayLoveInfo = list.get(position);
//        final BaseUser baseUser = sayLoveInfo.getBaseUser();
//        if (type == 2 && baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//            viewHolder.shareNumTextView.setVisibility(View.GONE);
//            viewHolder.deleteImg.setVisibility(View.VISIBLE);
//            viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    DelectTipDialog dialog = new DelectTipDialog();
//                    dialog.setTitleStr("确定删除表白？");
//                    dialog.setItemClick(new DelectTipDialog.ItemClick() {
//                        @Override
//                        public void itemClick() {
//                            onClick.delete(sayLoveInfo.getSayLoveId(), position);
//
//                        }
//                    });
//                    dialog.show(mActivity.getSupportFragmentManager());
//                }
//            });
//        }
//        if (sayLoveInfo == null || baseUser == null) {
//            return convertView;
//        }
//        //隐藏条目评论
//        /*viewHolder.commentNumTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClick.onComment(sayLoveInfo.getBaseUser().getUserId(), sayLoveInfo.getSayLoveId(), sayLoveInfo.getCommentCount(), position);
//            }
//        });*/
//        final int isAnonymous = sayLoveInfo.getIsAnonymous();
//        Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(viewHolder.userImg);
//
//        viewHolder.userImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    if (isAnonymous == 2) {
//                        //跳到用户详情
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    } else {
//                        userPresenter.getAnonymityUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId());
//                    }
//                }
//            }
//        });
//        viewHolder.userNickNameTextView.setText(baseUser.getNickName());
//        if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
//            //学校名称
//            viewHolder.schoolNameTextView.setVisibility(View.VISIBLE);
//            viewHolder.schoolNameTextView.setText(baseUser.getSchoolName());
//        } else {
//            viewHolder.schoolNameTextView.setVisibility(View.GONE);
//        }
//
//        if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//            //校友 并且不是自己
//            viewHolder.alumnus_tv.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.alumnus_tv.setVisibility(View.GONE);
//        }
//
//        viewHolder.userAgeTextView.setText(baseUser.getAge() + "岁");
//        int sex = baseUser.getSex();
//        if (sex == 1) {
//            //男性
//            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//        } else if (sex == 2) {
//            //女性
//            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//        }
//
//
//        viewHolder.timeTextView.setText(sayLoveInfo.getAddTime());
//        String diatance = baseUser.getDistance();
//        if ((!StringUtils.isEmpty(diatance))) {
//            viewHolder.locationTextView.setText(diatance);
//            viewHolder.locationTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.locationTextView.setVisibility(View.GONE);
//        }
//
//        if ((!StringUtils.isEmpty(baseUser.getCity()))) {
//            viewHolder.cityTextView.setText(baseUser.getCity());
//            viewHolder.cityTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.cityTextView.setVisibility(View.GONE);
//        }
//        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//            //自己不展示位置
//            viewHolder.locationTextView.setVisibility(View.GONE);
//            viewHolder.cityTextView.setVisibility(View.GONE);
//        }
//
//        //跟拍人以及内容
//        String content = sayLoveInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.contentTextView.setText(content);
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.contentTextView.setVisibility(View.GONE);
//        }
//        //跟拍对象
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
//                }else{
//                    //表白详情
//                    Intent intent = new Intent(mActivity, SayLoveDetailActivity.class);
//                    intent.putExtra("sayloveId", sayLoveInfo.getSayLoveId());
//                    intent.putExtra("position", position);
//                    mActivity.startActivity(intent);
//                }
//            }
//        });
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
//        viewHolder.labelFirstTextView.setVisibility(View.GONE);
//        viewHolder.labelSecondLayout.setVisibility(View.GONE);
//        int shareCount = sayLoveInfo.getShareNumber();
//        String shareCountStr = sayLoveInfo.getShareNumberStr();
//        if (shareCount > 9999) {
//            viewHolder.shareNumTextView.setText(shareCountStr);
//        } else {
//            viewHolder.shareNumTextView.setText(shareCount + "");
//        }
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
//        int praiseCount = sayLoveInfo.getPraiseCount();
//        String praiseCountStr = sayLoveInfo.getPraiseCountStr();
//        if (praiseCount > 9999) {
//            viewHolder.praisNumTextView.setText(praiseCountStr);
//        } else {
//            viewHolder.praisNumTextView.setText(praiseCount + "");
//        }
//
//        final int isPraise = sayLoveInfo.getIsPraise();
//        if (isPraise == 1) {
//            //1 : 已点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        } else {
//            //0: 未点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        }
//        viewHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //点赞 (1 点赞  2 取消点赞),
//                onClick.onPraise(sayLoveInfo.getSayLoveId(), isPraise + 1, position);
//            }
//        });
//        int commentCount = sayLoveInfo.getCommentCount();
//        String commentCountStr = sayLoveInfo.getCommentCountStr();
//        if (shareCount > 9999) {
//            viewHolder.commentNumTextView.setText(commentCountStr);
//        } else {
//            viewHolder.commentNumTextView.setText(commentCount + "");
//        }
//        // 图片显示区域
//        viewHolder.recycler_image.setVisibility(View.GONE);
//        viewHolder.linear_image_two.setVisibility(View.GONE);
//        if (sayLoveInfo.getConfessionType() == 2) {
//            viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//            viewHolder.video_play_img.setVisibility(View.VISIBLE);
//            if (!refreshItem) {
//                Glide.with(App.getInstance()).load(sayLoveInfo.getVideoImageUrl()).apply(options).into(viewHolder.img_first);
//            }
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
//                    viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                    viewHolder.video_play_img.setVisibility(View.GONE);
//                    if (imageList.size() == 1) {
//                        if (!refreshItem) {
//                            if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
//                                Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
//                            } else {
//                                Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.img_first);
//                            }
//                        }
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
//                    if (!refreshItem) {
//                        PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
//                        viewHolder.recycler_image.setAdapter(postImageLIstAdapter);
//                        postImageLIstAdapter.setOnItemListener(new ItemListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                if (mActivity != null) {
//                                    ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
//                                }
//                            }
//                        });
//                    }
//
//                    viewHolder.recycler_image.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//
//
//        if (position == 0 && type == 1) {
//            viewHolder.divider.setVisibility(View.GONE);
//        } else {
//            viewHolder.divider.setVisibility(View.VISIBLE);
//        }
//
//
//        return convertView;
//    }
//
//    class ViewHolder {
//        ImageView userImg, img_first, video_play_img, alumnus_tv,userLevelTextView, deleteImg;
//        TextView userNickNameTextView, userAgeTextView, timeTextView, locationTextView, cityTextView, contentTextView,
//                labelFirstTextView, shareNumTextView, praisNumTextView, commentNumTextView, schoolNameTextView;
//        RelativeLayout linear_image_two, labelSecondLayout;
//        RecyclerView recycler_image;
//        private View divider;
//        ImageView postStickView;
//
//    }
//
//    public interface OnClick {
//        void onPraise(long sayloveId, int type, int position);
//
//        void onComment(long toUserId, long sayloveId, int commentCount, int position);
//
//        void delete(long sayLoveId, int position);
//    }
//
//}
