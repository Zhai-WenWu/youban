//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.TextUtils;
//import android.text.style.ForegroundColorSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
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
//import cn.bjhdltcdn.p2plive.model.Comment;
//import cn.bjhdltcdn.p2plive.model.HelpComment;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OriginalInfo;
//import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
//import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class ClassMateHelpDetailAdapter extends BaseRecyclerAdapter {
//
//    private AppCompatActivity activity;
//
//    /**
//     * 帮忙信息
//     */
//    private final int ITEM_POSTINF_1 = 1;
//    /**
//     * 评论信息
//     */
//    private final int ITEM_COMMENT_2 = 2;
//    /**
//     * 评论为空
//     */
//    private final int ITEM_COMMENT_3 = 3;
//
//    private List<Object> list;
//    private final int screenWidth;
//    private final RequestOptions gifOptions;
//
//    public List<Object> getList() {
//        return list;
//    }
//
//    private RequestOptions options;
//
//    private ViewClick viewClick;
//
//    private long currentBaseUserId;
//    //排序(1按热度,2按时间)
//    private int sort = 1;
//
//    public void setSort(int sort) {
//        this.sort = sort;
//    }
//
//    public void setViewClick(ViewClick viewClick) {
//        this.viewClick = viewClick;
//    }
//
//    public ClassMateHelpDetailAdapter(List<Object> list, AppCompatActivity activity) {
//        this.list = list;
//        this.activity = activity;
//        options = new RequestOptions()
//                .transform(new GlideRoundTransform(9))
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg);
//        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        screenWidth = PlatformInfoUtils.getWidthOrHeight(activity)[0];
//        gifOptions = new RequestOptions().centerCrop().transform(new GlideRoundTransform(9))
//                .placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//
//
//    }
//
//    public void setList(List<Object> list) {
//
//        this.list = list;
//        notifyDataSetChanged();
//    }
//
//    public void addList(List<Object> list) {
//        if (list == null || list.size() == 0) {
//            return;
//        }
//
//        if (this.list == null) {
//            return;
//        }
//
//        this.list.addAll(list);
//        notifyDataSetChanged();
//
//    }
//
//    public void addItem(Comment comment, boolean isUpdate) {
//        if (comment == null) {
//            return;
//        }
//
//        if (list == null) {
//            return;
//        }
//
//        list.add(1, comment);
//        if (isUpdate) {
//            // 从第1个位置开始更新，更新条数是1
//            notifyItemRangeInserted(1, 1);
//        }
//
//
//    }
//
//
//    public void setHelpItemView(HelpInfo helpInfo) {
//
//        if (this.list == null) {
//            this.list = new ArrayList<>(1);
//        }
//
//        if (this.list.size() > 0) {
//            if (this.list.get(0) instanceof HelpInfo) {
//                this.list.remove(0);
//            }
//        }
//
//        this.list.add(0, helpInfo);
//
//        notifyDataSetChanged();
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        Object object = list.get(position);
//
//        if (object instanceof HelpInfo) {
//            return ITEM_POSTINF_1;
//        } else if ((object instanceof String)) {
//            return ITEM_COMMENT_3;
//        } else {
//            return ITEM_COMMENT_2;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position);
//
//        Object object = list.get(position);
//
//        int itemType = getItemViewType(position);
//        switch (itemType) {
//
//            case ITEM_POSTINF_1:// 帮忙
//                if (holder instanceof HelpInfoViewHolder) {
//
//                    HelpInfoViewHolder viewHolder = (HelpInfoViewHolder) holder;
//                    if (object instanceof HelpInfo) {
//                        HelpInfo helpInfo = (HelpInfo) object;
//                        bindHelpInfo(viewHolder, helpInfo, position);
//                    }
//                }
//
//                break;
//            case ITEM_COMMENT_2:// 评论
//
//                if (holder instanceof CommentItemViewHolder) {
//
//                    CommentItemViewHolder viewHolder = (CommentItemViewHolder) holder;
//                    if (object instanceof HelpComment) {
//                        HelpComment comment = (HelpComment) object;
//                        bindComment(viewHolder, comment, position);
//                    }
//                }
//
//                break;
//            case ITEM_COMMENT_3:// 空
//                if (holder instanceof EmputyItemViewHolder) {
//
//                    EmputyItemViewHolder viewHolder = (EmputyItemViewHolder) holder;
//                    if (object instanceof String) {
//                        viewHolder.empty_textView.setText((String) list.get(position));
//                    }
//                }
//
//                break;
//            default:
//
//        }
//
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//
//
//        View itemView = null;
//        HelpInfoViewHolder helpInfoViewHolder = null;
//        CommentItemViewHolder commentItemViewHolder = null;
//        EmputyItemViewHolder emputyItemViewHolder = null;
//
//        switch (itemType) {
//            case ITEM_POSTINF_1:
//                itemView = View.inflate(App.getInstance(), R.layout.help_detail_header_layout, null);
//                helpInfoViewHolder = new HelpInfoViewHolder(itemView);
//                return helpInfoViewHolder;
//            case ITEM_COMMENT_2:
//                itemView = View.inflate(App.getInstance(), R.layout.post_comment_list_item_layout, null);
//                commentItemViewHolder = new CommentItemViewHolder(itemView);
//                return commentItemViewHolder;
//            case ITEM_COMMENT_3:
//                itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_common_layout, viewGroup, false);
//                emputyItemViewHolder = new EmputyItemViewHolder(itemView);
//                return emputyItemViewHolder;
//
//        }
//
//        return super.onCreateViewHolder(viewGroup, itemType);
//    }
//
//
//    /**
//     * 绑定帮忙信息
//     *
//     * @param viewHolder
//     * @param position
//     */
//    private void bindHelpInfo(HelpInfoViewHolder viewHolder, final HelpInfo helpInfo, final int position) {
//
//        if (viewHolder == null || helpInfo == null) {
//            return;
//        }
//
//        // 用户信息
//        if (helpInfo.getBaseUser() != null) {
//            // 头像
//            Glide.with(App.getInstance()).load(helpInfo.getBaseUser().getUserIcon()).apply(options).into(viewHolder.userImageView);
//
//            if (helpInfo.getBaseUser().getUserId() != currentBaseUserId) {
//                viewHolder.userImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (activity != null) {
//                            activity.startActivity(new Intent(activity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, helpInfo.getBaseUser()));
//                        }
//
//                    }
//                });
//            }
//
//            //自己查看自己的帖子，不显示城市和关注
//            if (helpInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                viewHolder.attentionView.setVisibility(View.INVISIBLE);
//                viewHolder.cityTextView.setVisibility(View.GONE);
//            } else {
//                viewHolder.attentionView.setVisibility(View.VISIBLE);
//                viewHolder.cityTextView.setVisibility(View.VISIBLE);
//                // 关注状态(0-->未关注,1-->已关注)
//
//                if (helpInfo.getBaseUser().getIsAttention() == 1) {
//                /*viewHolder.attentionView.setText("已关注");
//                viewHolder.attentionView.setOnClickListener(null);*/
//                    viewHolder.attentionView.setVisibility(View.INVISIBLE);
//                } else {
//                    viewHolder.attentionView.setVisibility(View.VISIBLE);
//                    viewHolder.attentionView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            if (viewClick != null) {
//                                viewClick.attentionView(helpInfo.getBaseUser().getIsAttention(), helpInfo.getBaseUser().getUserId(), position);
//                            }
//                        }
//                    });
//                }
//
//            }
//
//
//            //是否匿名发布帖子(1--->匿名，2--->不匿名)
//
//            String baseUserNickName = helpInfo.getBaseUser().getNickName();
//            if (!StringUtils.isEmpty(baseUserNickName)) {
//                if (baseUserNickName.length() > 10) {
//                    baseUserNickName = baseUserNickName.substring(0, 10) + "...";
//                }
//            }
//
//            viewHolder.userNicknameTextView.setText(baseUserNickName);
//            viewHolder.userNicknameTextView.setVisibility(View.VISIBLE);
//
//            if (helpInfo.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                viewHolder.tv_distance.setVisibility(View.VISIBLE);
//                String distance = helpInfo.getDistance();
//                if (!StringUtils.isEmpty(distance)) {
//                    viewHolder.tv_distance.setText(helpInfo.getDistance());
//                } else {
//                    viewHolder.tv_distance.setText(helpInfo.getBaseUser().getDistance());
//                }
//
//            } else {
//                viewHolder.tv_distance.setVisibility(View.INVISIBLE);
//            }
//
//            //是否是校友(1否2是) //校友 并且不是自己
//            viewHolder.userSchoolmateView.setVisibility((helpInfo.getBaseUser().getIsSchoolmate() == 2 && helpInfo.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) ? View.VISIBLE : View.INVISIBLE);
//
//            // 性别
//            int sex = helpInfo.getBaseUser().getSex();
//            if (sex == 1) {
//                //男性
//                viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//            } else if (sex == 2) {
//                //女性
//                viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//            }
//
//            // 年龄
//            viewHolder.userAgeTextView.setText(helpInfo.getBaseUser().getAge() + "岁");
//            viewHolder.userAgeTextView.setPadding(0, Utils.dp2px(7), Utils.dp2px(10), 0);
//            // 城市
//            viewHolder.cityTextView.setText(helpInfo.getCity());
//            //学校
//            viewHolder.school_name_text_view.setText(helpInfo.getBaseUser().getSchoolName());
//
//        }
//
//        // 时间
//        viewHolder.timeTextView.setText(helpInfo.getAddTime());
//
//
//        //跟拍人以及内容
//        String content = helpInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.contentTextView.setText(content);
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.contentTextView.setVisibility(View.GONE);
//        }
//        //跟拍对象
//        final OriginalInfo originalInfo = helpInfo.getOriginalInfo();
//        //标签展示
//        long labelId = helpInfo.getLabelId();
//        String labelName = helpInfo.getLabelName();
//        if (helpInfo.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
//            //跟拍加参赛
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//            viewHolder.contentTextView.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, helpInfo.getScondlabelList(), content));
//        } else {
//            //只有跟拍
//            if (helpInfo.getIsOriginal() == 1 && originalInfo != null) {
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getFollowContent(originalInfo, content));
//            }
//            //只有参赛
//            if (!StringUtils.isEmpty(labelName)) {
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getMatchContent(labelId, labelName, helpInfo.getScondlabelList(), content));
//            }
//        }
//        viewHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (helpInfo.getIsOriginal() == 1 && originalInfo != null) {
//                    //跳转到视频广场界面
//                }
//            }
//        });
//
//        if (helpInfo.getHelpType() == 2) {
//
//            //视频
//            Glide.with(App.getInstance()).load(helpInfo.getVideoImageUrl()).apply(options).into(viewHolder.imgFirst);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.imgFirst.getLayoutParams();
//            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//            viewHolder.imgFirst.setLayoutParams(layoutParams);
//            viewHolder.frameLayout.setVisibility(View.VISIBLE);
//            viewHolder.videoPlayImg.setVisibility(View.VISIBLE);
//            viewHolder.imgFirst.setVisibility(View.VISIBLE);
//            viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//
//            viewHolder.videoPlayImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //跳转到视频播放界面
//                    Intent intent = new Intent(activity, PKVideoPlayActivity.class);
//                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, helpInfo.getHelpId());
//                    intent.putExtra(Constants.Fields.COME_IN_TYPE, 1);
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_HELPINFO);
//                    activity.startActivity(intent);
//                }
//            });
//
////            DetailVideoPlayFragment mFragment = new DetailVideoPlayFragment();
////            mFragment.setData(helpInfo);
////            ActivityUtils.addFragmentToActivity(activity.getSupportFragmentManager(), mFragment, R.id.video_frame);
//
//        } else {
//
//            viewHolder.frameLayout.setVisibility(View.GONE);
//
//            final List<Image> imageList = helpInfo.getImageList();
//            if (imageList != null && imageList.size() > 0) {
//
//                if (imageList.size() == 1 && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {// 显示一张图片的样式
//                    viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                    viewHolder.imgFirst.setVisibility(View.VISIBLE);
//                    viewHolder.videoPlayImg.setVisibility(View.GONE);
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.imgFirst.getLayoutParams();
//                    layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//                    layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//                    viewHolder.imgFirst.setLayoutParams(layoutParams);
//                    Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.imgFirst);
//
//                    viewHolder.imgFirst.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (helpInfo != null) {
//                                ImageViewPageDialog.newInstance(imageList, 0).show(activity.getSupportFragmentManager());
//                            }
//                        }
//                    });
//
//
//                } else {// 显示多张图片的样式
//
//                    viewHolder.imgFirst.setVisibility(View.GONE);
//
//                    PostDetailImageRecycleAdapter postImageLIstAdapter = new PostDetailImageRecycleAdapter(activity);
//                    postImageLIstAdapter.setList(imageList);
//                    viewHolder.recyclerImage.setAdapter(postImageLIstAdapter);
//
//                    postImageLIstAdapter.setImageViewPageDialogClick(new PostDetailImageRecycleAdapter.ImageViewPageDialogClick() {
//                        @Override
//                        public void onClick(List<Image> mList, int currentItem) {
//                            ImageViewPageDialog.newInstance(imageList, currentItem).show(activity.getSupportFragmentManager());
//
//                        }
//                    });
//                    viewHolder.recyclerImage.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//
//
//        // 点赞数量
//        viewHolder.praiseTextView.setText(helpInfo.getPraiseCount() + "");
//        final int isPraise = helpInfo.getIsPraise();
//        if (isPraise == 1) {
//            //1 : 已点赞
//            viewHolder.praiseTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        } else {
//            //0: 未点赞
//            viewHolder.praiseTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        }
//
//        viewHolder.praiseTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //点赞 (1 点赞  2 取消点赞)
//                int type = (isPraise == 0 | isPraise == 2) ? 1 : 2;
//                helpInfo.setIsPraise(type);
//                helpInfo.setPraiseCount(helpInfo.getPraiseCount() + (type == 1 ? 1 : -1));
//                notifyItemChanged(position);
//
//                if (viewClick != null) {
//                    viewClick.onPraise(0, helpInfo.getHelpId(), type, position);
//                }
//
//
//            }
//        });
//
//        // 显示评论数量
//        viewHolder.commentTextView.setText(helpInfo.getCommentCount() + "");
//        viewHolder.commentTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (viewClick != null) {
//                    viewClick.onComment(helpInfo.getHelpId(), 2, position);
//                }
//            }
//        });
//        // 更多按钮
//        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (viewClick != null) {
//                    viewClick.moreImg(0, position);
//                }
//            }
//        });
//
//        /**
//         * 分享按钮
//         */
//        viewHolder.shareView.setText(helpInfo.getShareNumber() + "");
//        viewHolder.shareView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String imgUrl = "";
//
//                if (helpInfo.getImageList() != null && helpInfo.getImageList().size() > 0) {
//                    imgUrl = helpInfo.getImageList().get(0).getThumbnailUrl();
//                }
//
//                ShareUtil.getInstance().showShare(activity, ShareUtil.CLASSMATEHELP, helpInfo.getHelpId(), helpInfo, "", "", "", imgUrl, false);
//            }
//        });
//
//
//    }
//
//    /**
//     * 绑定评论
//     *
//     * @param viewHolder
//     * @param comment
//     * @param position
//     */
//    private void bindComment(CommentItemViewHolder viewHolder, final HelpComment comment, final int position) {
//
//        if (viewHolder == null || comment == null) {
//            return;
//        }
//
//
//        // 用户等级默认不显示
//        viewHolder.userLevelView.setVisibility(View.GONE);
//        if (position == 1) {
//            if (sort == 1) {
//                viewHolder.tv_sort.setText("按热度");
//            } else if (sort == 2) {
//                viewHolder.tv_sort.setText("按时间");
//            }
//            viewHolder.tv_sort.setVisibility(View.VISIBLE);
//            viewHolder.line_sort.setVisibility(View.VISIBLE);
//            viewHolder.tv_sort.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    viewClick.sortViewClick();
//                }
//            });
//        } else {
//            viewHolder.tv_sort.setVisibility(View.GONE);
//            viewHolder.line_sort.setVisibility(View.GONE);
//        }
//        String userIcon = "";
//        String fromBaseUserNickName = "";
//        String toBaseUserNickName = "";
//
//        //1--->评论,2--->回复
//        final BaseUser fromBaseUser = comment.getFromBaseUser();
//        if (comment.getType() == 1) {
//            userIcon = comment.getFromBaseUser().getUserIcon();
//            fromBaseUserNickName = comment.getFromBaseUser().getNickName();
//            viewHolder.formUserNicknameView.setText(fromBaseUserNickName);
//
//            //图片地址
//            String videoImageUrl = null;
//            final int commentType = comment.getCommentType();
//            final List<Image> imageList = comment.getImageList();
//            if (commentType == 1) {
//                if (imageList != null && imageList.get(0) != null) {
//                    videoImageUrl = imageList.get(0).getImageUrl();
//                }
//            } else if (commentType == 2) {
//                videoImageUrl = comment.getVideoImageUrl();
//            }
//            if (TextUtils.isEmpty(videoImageUrl)) {
//                viewHolder.linear_image_two.setVisibility(View.GONE);
//            } else {
//                viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                if (commentType == 1) {//图文
//                    viewHolder.video_play_img.setVisibility(View.GONE);
//                    if (imageList.size() > 1) {
//                        viewHolder.imageNumView.setVisibility(View.VISIBLE);
//                        viewHolder.imageNumView.setText(String.valueOf(imageList.size()));
//                    } else {
//                        viewHolder.imageNumView.setVisibility(View.GONE);
//                    }
//                } else if (commentType == 2) {//视频
//                    viewHolder.video_play_img.setVisibility(View.VISIBLE);
//                    viewHolder.imageNumView.setVisibility(View.GONE);
//                }
//                if (videoImageUrl.endsWith("gif")) {
//                    Utils.CornerImageViewGifDisplayByUrl(videoImageUrl, viewHolder.iv_comment_image);
//                } else {
//                    Utils.CornerImageViewDisplayByUrl(videoImageUrl, viewHolder.iv_comment_image);
//                }
//                final String finalVideoImageUrl = videoImageUrl;
//                viewHolder.iv_comment_image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (commentType == 1) {
//                            ImageViewPageDialog.newInstance(imageList, 0).show(activity.getSupportFragmentManager());
//                        } else if (commentType == 2) {
//                            //跳转到视频播放界面
//                            Intent intent = new Intent(activity, VideoPlayFullScreenActivity.class);
//                            intent.putExtra(Constants.Fields.VIDEO_PATH, comment.getVideoUrl());
//                            intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, finalVideoImageUrl);
//                            activity.startActivity(intent);
//                        }
//                    }
//                });
//            }
//            int replyCount = comment.getReplyCount();
//            if (replyCount > 0) {
//                viewHolder.ll_comment_reply.setVisibility(View.VISIBLE);
//                final List<HelpComment> replyList = comment.getReplyList();
//                if (replyCount >= 1) {
//                    viewHolder.tv_comment_1.setVisibility(View.VISIBLE);
//                    if (replyList.get(0).getFromBaseUser() != null) {
//                        fromBaseUserNickName = replyList.get(0).getFromBaseUser().getNickName();
//                        if (!StringUtils.isEmpty(fromBaseUserNickName)) {
//                            if (fromBaseUserNickName.length() > 4) {
//                                fromBaseUserNickName = fromBaseUserNickName.substring(0, 4) + "...";
//                            }
//                        }
//                        String content = replyList.get(0).getContent();
//                        if (replyList.get(0).getParentId() == replyList.get(0).getCommentParentId()) {//一级评论下的评论
//                            setReplyCommentStyle(viewHolder.tv_comment_1, fromBaseUserNickName, content);
//                        } else {
//                            toBaseUserNickName = replyList.get(0).getToBaseUser().getNickName();
//                            setReplyReplyCommentStyle(viewHolder.tv_comment_1, fromBaseUserNickName, toBaseUserNickName, content);
//                        }
//                    }
//                    viewHolder.tv_comment_1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (currentBaseUserId != replyList.get(0).getFromBaseUser().getUserId()) {
//                                viewClick.ReplyListComment(replyList.get(0));
//                            }
//                        }
//                    });
//                } else {
//                    viewHolder.tv_comment_1.setVisibility(View.GONE);
//                }
//                if (replyCount >= 2) {
//                    viewHolder.tv_comment_2.setVisibility(View.VISIBLE);
//                    if (replyList.get(1).getFromBaseUser() != null) {
//                        fromBaseUserNickName = replyList.get(1).getFromBaseUser().getNickName();
//                        if (!StringUtils.isEmpty(fromBaseUserNickName)) {
//                            if (fromBaseUserNickName.length() > 4) {
//                                fromBaseUserNickName = fromBaseUserNickName.substring(0, 4) + "...";
//                            }
//                        }
//                        String content = replyList.get(1).getContent();
//                        if (replyList.get(1).getParentId() == replyList.get(1).getCommentParentId()) {//一级评论下的评论
//                            setReplyCommentStyle(viewHolder.tv_comment_2, fromBaseUserNickName, content);
//                        } else {
//                            toBaseUserNickName = replyList.get(1).getToBaseUser().getNickName();
//                            setReplyReplyCommentStyle(viewHolder.tv_comment_2, fromBaseUserNickName, toBaseUserNickName, content);
//                        }
//
//                    }
//                    viewHolder.tv_comment_2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (currentBaseUserId != replyList.get(1).getFromBaseUser().getUserId()) {
//                                viewClick.ReplyListComment(replyList.get(1));
//                            }
//                        }
//                    });
//                } else {
//                    viewHolder.tv_comment_2.setVisibility(View.GONE);
//                }
//                if (replyCount >= 3) {
//                    viewHolder.tv_comment_3.setVisibility(View.VISIBLE);
//                    if (replyList.get(2).getFromBaseUser() != null) {
//                        fromBaseUserNickName = replyList.get(2).getFromBaseUser().getNickName();
//                        if (!StringUtils.isEmpty(fromBaseUserNickName)) {
//                            if (fromBaseUserNickName.length() > 4) {
//                                fromBaseUserNickName = fromBaseUserNickName.substring(0, 4) + "...";
//                            }
//                        }
//                        String content = replyList.get(2).getContent();
//                        if (replyList.get(2).getParentId() == replyList.get(2).getCommentParentId()) {//一级评论下的评论
//                            setReplyCommentStyle(viewHolder.tv_comment_3, fromBaseUserNickName, content);
//                        } else {
//                            toBaseUserNickName = replyList.get(2).getToBaseUser().getNickName();
//                            setReplyReplyCommentStyle(viewHolder.tv_comment_3, fromBaseUserNickName, toBaseUserNickName, content);
//                        }
//
//                    }
//                    viewHolder.tv_comment_3.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (currentBaseUserId != replyList.get(2).getFromBaseUser().getUserId()) {
//                                viewClick.ReplyListComment(replyList.get(2));
//                            }
//                        }
//                    });
//                } else {
//                    viewHolder.tv_comment_3.setVisibility(View.GONE);
//                }
//                if (replyCount >= 4) {
//                    viewHolder.tv_comment_more.setVisibility(View.VISIBLE);
//                    String content = "共"
//                            + replyCount
//                            + "条回复>";
//                    viewHolder.tv_comment_more.setText(content);
//                    viewHolder.tv_comment_more.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            viewClick.moreComment(comment.getCommentId());
//                        }
//                    });
//                } else {
//                    viewHolder.tv_comment_more.setVisibility(View.GONE);
//                }
//
//            } else {
//                viewHolder.ll_comment_reply.setVisibility(View.GONE);
//            }
//            // 点赞数量
//            viewHolder.tv_num_praise.setText(String.valueOf(comment.getPraiseCount()));
//            final int isPraise = comment.getIsPraise();
//            if (isPraise == 1) {
//                //1 : 已点赞
//                viewHolder.tv_num_praise.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//            } else {
//                //0: 未点赞
//                viewHolder.tv_num_praise.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//            }
//
//            viewHolder.tv_num_praise.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    //点赞 (1 点赞  2 取消点赞)
//                    int type = (isPraise == 0 | isPraise == 2) ? 1 : 2;
//                    comment.setIsPraise(type);
//                    comment.setPraiseCount(comment.getPraiseCount() + (type == 1 ? 1 : -1));
//                    notifyItemChanged(position);
//
//                    if (viewClick != null) {
//                        viewClick.onPraise(1, comment.getCommentId(), type, position);
//                    }
//
//
//                }
//            });
//            viewHolder.tv_num_comment.setText(String.valueOf(replyCount));
//        }
//
//        // 头像
//        Glide.with(App.getInstance()).load(userIcon).apply(options).into(viewHolder.circleImageView);
//
//        if (comment.getFromBaseUser().getUserId() != currentBaseUserId) {
//            viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (activity != null) {
//                        Intent intent = new Intent(activity, UserDetailsActivity.class);
//                        intent.putExtra(Constants.KEY.KEY_OBJECT, comment.getFromBaseUser());
//                        activity.startActivity(intent);
//
//                    }
//                }
//            });
//
//        }
//
//
//        // 当前发送数据的时间
//        viewHolder.timeView.setText(comment.getAddTime());
//
//        // 内容
//        if (TextUtils.isEmpty(comment.getContent())) {
//            viewHolder.contentView.setVisibility(View.GONE);
//        } else {
//            viewHolder.contentView.setVisibility(View.VISIBLE);
//            viewHolder.contentView.setText(comment.getContent());
//        }
//        String schoolAndDistance = null;
//        String schoolName = fromBaseUser.getSchoolName();
//        if (!TextUtils.isEmpty(schoolName) && !"null".equals(schoolName)) {
//            schoolAndDistance = schoolName;
//            String distance = fromBaseUser.getDistance();
//            if (!TextUtils.isEmpty(distance) && !"null".equals(distance)) {
//                schoolAndDistance = schoolName + "　" + distance;
//            }
//        } else {
//            schoolAndDistance = "该评论来自于微信用户";
//        }
//
//        if (!TextUtils.isEmpty(schoolAndDistance)) {
//            viewHolder.tv_school_distance.setText(schoolAndDistance);
//        }
//        // 更多按钮
//        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (viewClick != null) {
//                    viewClick.moreImg(1, position);
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 回复一级评论
//     *
//     * @param textView
//     * @param fromBaseUserNickName
//     * @param content
//     */
//    private void setReplyCommentStyle(TextView textView, String fromBaseUserNickName, String content) {
//        SpannableStringBuilder style = new SpannableStringBuilder();
//        style.append(fromBaseUserNickName + ":");
//        style.append(content);
//        // 回复昵称
//        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_576b95)), 0, fromBaseUserNickName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        // 内容
//        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), fromBaseUserNickName.length() + 1, style.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.setText(style);
//    }
//
//    /**
//     * 回复二级评论
//     *
//     * @param textView
//     * @param fromBaseUserNickName
//     * @param toBaseUserNickName
//     * @param content
//     */
//    private void setReplyReplyCommentStyle(TextView textView, String fromBaseUserNickName, String toBaseUserNickName, String content) {
//        if (!TextUtils.isEmpty(toBaseUserNickName)) {
//            if (toBaseUserNickName.length() > 4) {
//                toBaseUserNickName = toBaseUserNickName.substring(0, 4) + "...";
//            }
//        }
//        SpannableStringBuilder style = new SpannableStringBuilder();
//        style.append(fromBaseUserNickName);
//        style.append(" 回复 ");
//        style.append(toBaseUserNickName);
//        style.append(content);
//        // 回复昵称
//        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_576b95)), 0, fromBaseUserNickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        // 回复
//        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), fromBaseUserNickName.length(), fromBaseUserNickName.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        // 被回复人昵称
//        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_576b95)), fromBaseUserNickName.length() + 4, fromBaseUserNickName.length() + 4 + toBaseUserNickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        // 内容
//        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), fromBaseUserNickName.length() + 4 + toBaseUserNickName.length(), style.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.setText(style);
//    }
//
//    public void addEmptyItem(String blankHint) {
//        list.add(1, "暂无评论，快来写下第一条评论吧！");
//        notifyDataSetChanged();
//    }
//
//
//    static class HelpInfoViewHolder extends BaseViewHolder {
//
//
//        CircleImageView userImageView;
//        TextView userNicknameTextView;
//        ImageView userSchoolmateView;
//        ImageView imgFirst;
//        ImageView videoPlayImg;
//        TextView userAgeTextView;
//        TextView timeTextView;
//        TextView cityTextView;
//        TextView contentTextView;
//        TextView tv_distance;
//        TextView attentionView;
//        FrameLayout frameLayout;
//
//        RecyclerView recyclerImage;
//
//        TextView praiseTextView;
//        TextView commentTextView;
//
//        ImageView moreImg;
//        ImageView postStickView;
//        TextView shareView;
//        TextView school_name_text_view;
//        RelativeLayout linear_image_two;
//
//
//        public HelpInfoViewHolder(View itemView) {
//            super(itemView);
//
//            // 头像
//            userImageView = itemView.findViewById(R.id.user_img);
//            // 用户昵称
//            userNicknameTextView = itemView.findViewById(R.id.user_nickname_text);
//            school_name_text_view = itemView.findViewById(R.id.school_name_text_view);
//            // 校友
//            userSchoolmateView = itemView.findViewById(R.id.user_school_mate);
//            // 时间控件
//            timeTextView = itemView.findViewById(R.id.time_text);
//
//            postStickView = (ImageView) itemView.findViewById(R.id.post_stick_view);
//
//            // 性别 1 男；2女
//            userAgeTextView = itemView.findViewById(R.id.user_age_text);
//            // 城市
//            cityTextView = itemView.findViewById(R.id.city_text);
//            // 内容
//            contentTextView = itemView.findViewById(R.id.content_text);
//            // 内容
//            tv_distance = itemView.findViewById(R.id.tv_distance);
//            // 关注
//            attentionView = itemView.findViewById(R.id.attention_tv);
//            // 图片空间
//            recyclerImage = itemView.findViewById(R.id.recycler_image);
//            // 点赞按钮
//            praiseTextView = itemView.findViewById(R.id.praise_text);
//            // 评论按钮
//            commentTextView = itemView.findViewById(R.id.comment_text);
//            // 更多按钮
//            moreImg = itemView.findViewById(R.id.more_img);
//            // 分享
//            shareView = itemView.findViewById(R.id.share_text);
//
//            imgFirst = itemView.findViewById(R.id.img_first);
//            videoPlayImg = itemView.findViewById(R.id.video_play_img);
//
//            //视频
//            frameLayout = itemView.findViewById(R.id.video_frame);
//
//            linear_image_two = itemView.findViewById(R.id.linear_image_two);
//
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
//            layoutManager.setAutoMeasureEnabled(true);
//            recyclerImage.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(15)));
//            recyclerImage.setHasFixedSize(true);
//            recyclerImage.setLayoutManager(layoutManager);
//
//
//        }
//    }
//
//    static class CommentItemViewHolder extends BaseViewHolder {
//
//        // 头像
//        CircleImageView circleImageView;
//        // 评论昵称
//        TextView formUserNicknameView;
//        // 用户等级
//        ImageView userLevelView;
//        // 时间
//        TextView timeView;
//        // 内容
//        TextView contentView;
//        //视频
////        FrameLayout frameLayout;
//        RelativeLayout linear_image_two;
//        //播放按钮
//        ImageView video_play_img;
//        //图片
//        ImageView iv_comment_image;
//        //图片数量
//        TextView imageNumView;
//        //回复列表
//        LinearLayout ll_comment_reply;
//        TextView tv_comment_1, tv_comment_2, tv_comment_3, tv_comment_more;
//        TextView tv_school_distance;
//        TextView tv_num_praise, tv_num_comment;
//        TextView tv_sort;
//        View line_sort;
//        // 更多按钮
//        ImageView moreImg;
//        // 分隔符
//        View divider;
//
//        public CommentItemViewHolder(View itemView) {
//            super(itemView);
//            circleImageView = itemView.findViewById(R.id.user_img);
//            formUserNicknameView = itemView.findViewById(R.id.form_user_nickname_text);
//            userLevelView = itemView.findViewById(R.id.user_level_text);
//            timeView = itemView.findViewById(R.id.time_text);
//            contentView = itemView.findViewById(R.id.content_text);
//            tv_sort = itemView.findViewById(R.id.tv_sort);
//            line_sort = itemView.findViewById(R.id.line_sort);
//            linear_image_two = itemView.findViewById(R.id.linear_image_two);
//            video_play_img = itemView.findViewById(R.id.video_play_img);
//            iv_comment_image = itemView.findViewById(R.id.iv_comment_image);
//            imageNumView = itemView.findViewById(R.id.tv_num_image);
//            ll_comment_reply = itemView.findViewById(R.id.ll_comment_reply);
//            tv_comment_1 = itemView.findViewById(R.id.tv_comment_1);
//            tv_comment_2 = itemView.findViewById(R.id.tv_comment_2);
//            tv_comment_3 = itemView.findViewById(R.id.tv_comment_3);
//            tv_comment_more = itemView.findViewById(R.id.tv_comment_more);
//            tv_school_distance = itemView.findViewById(R.id.tv_school_distance);
//            tv_num_praise = itemView.findViewById(R.id.tv_num_praise);
//            tv_num_comment = itemView.findViewById(R.id.tv_num_comment);
//            moreImg = itemView.findViewById(R.id.more_img);
//            divider = itemView.findViewById(R.id.divider_line);
//
//
//        }
//
//    }
//
//    static class EmputyItemViewHolder extends BaseViewHolder {
//
//        ImageView empty_image;
//        TextView empty_textView;
//
//        public EmputyItemViewHolder(View itemView) {
//            super(itemView);
//            empty_image = itemView.findViewById(R.id.empty_image);
//            empty_textView = itemView.findViewById(R.id.empty_textView);
//        }
//    }
//
//    public void onDestroy() {
//        if (activity != null) {
//            activity = null;
//        }
//
//        if (list != null) {
//            list.clear();
//        }
//        list = null;
//    }
//
//
//    public interface ViewClick {
//        //点赞 0是帖子
//        void onPraise(int itemType, long helpId, int type, int position);
//
//        void onComment(long helpId, int type, int position);
//
//        void attentionView(int type, long userId, int position);
//
//        void moreImg(int type, int position);
//
//        void moreComment(long commentId);
//
//        void ReplyListComment(HelpComment comment);
//
//        void sortViewClick();
//    }
//}
