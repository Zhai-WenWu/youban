package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Comment;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.OriginalInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import de.hdodenhof.circleimageview.CircleImageView;

import static io.rong.imkit.utilities.RongUtils.screenWidth;

public class PostCommentListAdapter extends BaseRecyclerAdapter {

    private final RequestOptions gifOptions;
    private AppCompatActivity activity;

    /**
     * 帖子信息
     */
    private final int ITEM_POSTINF_1 = 1;
    /**
     * 评论信息
     */
    private final int ITEM_COMMENT_2 = 2;
    /**
     * 评论为空
     */
    private final int ITEM_COMMENT_3 = 3;

    private List<Object> list;

    public List<Object> getList() {
        return list;
    }

    private RequestOptions options, matchOptions;

    private ViewClick viewClick;

    private long currentBaseUserId;
    private UserPresenter userPresenter;

    public void setUserPresenter(UserPresenter userPresenter) {
        this.userPresenter = userPresenter;
    }

    //排序(1按热度,2按时间)
    private int sort = 1;

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setViewClick(ViewClick viewClick) {
        this.viewClick = viewClick;
    }

    public PostCommentListAdapter(List<Object> list, AppCompatActivity activity) {
        this.list = list;
        this.activity = activity;
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon);
        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

        gifOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        matchOptions = new RequestOptions()
                .placeholder(R.mipmap.post_match_error_icon)
                .error(R.mipmap.post_match_error_icon);
    }

    public void setList(List<Object> list) {

        Object object = null;
        if (this.list != null && this.list.size() > 0) {
            object = this.list.get(0);
            this.list.clear();
        }

        if (object != null) {
            this.list.add(object);
        }

        this.list.addAll(list);
        notifyDataSetChanged();

    }

    public void addList(List<Object> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        if (this.list == null) {
            return;
        }

        int size = this.list.size();
        this.list.addAll(list);
        notifyDataSetChanged();
        notifyItemRangeInserted(size, this.list.size() - size);


    }

    public void addItem(Comment comment, boolean isUpdate) {
        if (comment == null) {
            return;
        }

        if (list == null) {
            return;
        }

        list.add(1, comment);
        if (isUpdate) {
            // 从第1个位置开始更新，更新条数是1
            notifyItemRangeInserted(1, 1);
        }


    }


    public void setPostItemView(PostInfo postInfo) {

        if (this.list == null) {
            this.list = new ArrayList<>(1);
        }

        if (this.list.size() > 0) {
            if (this.list.get(0) instanceof PostInfo) {
                this.list.remove(0);
            }
        }

        this.list.add(0, postInfo);

        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = list.get(position);

        if (object instanceof PostInfo) {
            return ITEM_POSTINF_1;
        } else if ((object instanceof String)) {
            return ITEM_COMMENT_3;
        } else {
            return ITEM_COMMENT_2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (payloads == null || payloads.isEmpty()) {

            Object object = list.get(position);

            int itemType = getItemViewType(position);
            switch (itemType) {

                case ITEM_POSTINF_1:// 帖子
                    if (holder instanceof PostInfoViewHolder) {

                        PostInfoViewHolder viewHolder = (PostInfoViewHolder) holder;
                        if (object instanceof PostInfo) {
                            PostInfo postInfo = (PostInfo) object;
                            bindPostInfo(viewHolder, postInfo, position, false);
                        }
                    }

                    break;
                case ITEM_COMMENT_2:// 评论

                    if (holder instanceof CommentItemViewHolder) {

                        CommentItemViewHolder viewHolder = (CommentItemViewHolder) holder;
                        if (object instanceof Comment) {
                            Comment comment = (Comment) object;
                            bindComment(viewHolder, comment, position, false);
                        }
                    }

                    break;
                case ITEM_COMMENT_3:// 空评论

                    if (holder instanceof EmputyItemViewHolder) {

                        EmputyItemViewHolder viewHolder = (EmputyItemViewHolder) holder;
                        if (object instanceof String) {
                            viewHolder.empty_textView.setText((String) list.get(position));
                        }
                    }

                    break;

            }

        } else {

            Object object = list.get(position);

            int itemType = getItemViewType(position);
            switch (itemType) {

                case ITEM_POSTINF_1:// 帖子
                    if (holder instanceof PostInfoViewHolder) {

                        PostInfoViewHolder viewHolder = (PostInfoViewHolder) holder;
                        if (object instanceof PostInfo) {
                            PostInfo postInfo = (PostInfo) object;
                            bindPostInfo(viewHolder, postInfo, position, true);
                        }
                    }

                    break;
                case ITEM_COMMENT_2:// 评论

                    if (holder instanceof CommentItemViewHolder) {

                        CommentItemViewHolder viewHolder = (CommentItemViewHolder) holder;
                        if (object instanceof Comment) {
                            Comment comment = (Comment) object;
                            bindComment(viewHolder, comment, position, true);
                        }
                    }

                    break;
                case ITEM_COMMENT_3:// 空评论

                    if (holder instanceof EmputyItemViewHolder) {

                        EmputyItemViewHolder viewHolder = (EmputyItemViewHolder) holder;
                        if (object instanceof String) {
                            viewHolder.empty_textView.setText((String) list.get(position));
                        }
                    }

                    break;

            }

        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {


        View itemView = null;
        PostInfoViewHolder postInfoViewHolder = null;
        CommentItemViewHolder commentItemViewHolder = null;
        EmputyItemViewHolder emputyItemViewHolder = null;

        switch (itemType) {
            case ITEM_POSTINF_1:
                itemView = View.inflate(App.getInstance(), R.layout.post_detail_header_layout, null);
                postInfoViewHolder = new PostInfoViewHolder(itemView);
                return postInfoViewHolder;
            case ITEM_COMMENT_2:
                itemView = View.inflate(App.getInstance(), R.layout.post_comment_list_item_layout, null);
                commentItemViewHolder = new CommentItemViewHolder(itemView);
                return commentItemViewHolder;
            case ITEM_COMMENT_3:
                itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_common_layout, viewGroup, false);
                emputyItemViewHolder = new EmputyItemViewHolder(itemView);
                return emputyItemViewHolder;

        }

        return super.onCreateViewHolder(viewGroup, itemType);
    }


    /**
     * 绑定帖子信息
     *
     * @param viewHolder
     * @param postInfo
     * @param position
     */
    private void bindPostInfo(PostInfoViewHolder viewHolder, final PostInfo postInfo, final int position, boolean isUpdate) {

        if (viewHolder == null || postInfo == null) {
            return;
        }

        // 用户信息
        if (postInfo.getBaseUser() != null) {
            // 头像
            if (!isUpdate) {
                Glide.with(App.getInstance()).load(postInfo.getBaseUser().getUserIcon()).apply(options).into(viewHolder.userImageView);
            }

            //是否匿名发布帖子(1--->匿名，2--->不匿名)
            viewHolder.userImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postInfo.getBaseUser().getUserId() != currentBaseUserId) {
                        if (postInfo.getIsAnonymous() == 2) {
                            //跳到用户详情
                            activity.startActivity(new Intent(activity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, postInfo.getBaseUser()));
                        } else {
                            userPresenter.getAnonymityUser(currentBaseUserId, postInfo.getBaseUser().getUserId());
                        }
                    }
                }
            });

            //是否匿名发布帖子(1--->匿名，2--->不匿名)
            if (postInfo.getIsAnonymous() == 2) {

                String baseUserNickName = postInfo.getBaseUser().getNickName();
                if (!StringUtils.isEmpty(baseUserNickName)) {
                    if (baseUserNickName.length() > 10) {
                        baseUserNickName = baseUserNickName.substring(0, 10) + "...";
                    }
                }

                viewHolder.userNicknameTextView.setText(baseUserNickName);
                viewHolder.userNicknameTextView.setVisibility(View.VISIBLE);

                // 用户级别
//                viewHolder.userLevelTextView.setVisibility(View.VISIBLE);
//                Utils.getActiveLevel(viewHolder.userLevelTextView, postInfo.getActiveLevel());

                //是否是校友(1否2是) //校友 并且不是自己
                viewHolder.userSchoolmateView.setVisibility((postInfo.getBaseUser().getIsSchoolmate() == 2 && postInfo.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) ? View.VISIBLE : View.GONE);

                // 性别
                int sex = postInfo.getBaseUser().getSex();
                if (sex == 1) {
                    //男性
                    viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
                } else if (sex == 2) {
                    //女性
                    viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
                }

                // 年龄
                viewHolder.userAgeTextView.setText(postInfo.getBaseUser().getAge() + "岁");
                viewHolder.userAgeTextView.setPadding(0, 0, Utils.dp2px(4), 0);
                // 城市
                viewHolder.cityTextView.setText(postInfo.getBaseUser().getCity());
                viewHolder.cityTextView.setVisibility(View.VISIBLE);
                // 地址
                viewHolder.locationTextView.setText(postInfo.getBaseUser().getDistance());

                // 自己发布的不需要关注
                if (postInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    viewHolder.attentionView.setVisibility(View.INVISIBLE);
                }

                viewHolder.attentionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewClick != null) {
                            viewClick.attentionView(postInfo.getBaseUser().getIsAttention(), postInfo.getBaseUser().getUserId(), position);
                        }
                    }
                });

                // 关注状态(0-->未关注,1-->已关注)
                viewHolder.attentionView.setVisibility(View.VISIBLE);
                if (postInfo.getBaseUser().getIsAttention() == 1) {
                    viewHolder.attentionView.setText("已关注");
                    viewHolder.attentionView.setOnClickListener(null);
                    viewHolder.attentionView.setVisibility(View.INVISIBLE);
                }

                //自己查看自己的帖子，不显示城市和关注
                if (postInfo.getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    viewHolder.attentionView.setVisibility(View.INVISIBLE);
//                    viewHolder.cityTextView.setVisibility(View.GONE);
                }

            } else {
                viewHolder.userNicknameTextView.setText(postInfo.getBaseUser().getNickName());
                viewHolder.userNicknameTextView.setVisibility(View.VISIBLE);

                //是否是校友(1否2是) //校友 并且不是自己
                viewHolder.userSchoolmateView.setVisibility(View.GONE);

                viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                // 年龄
                viewHolder.userAgeTextView.setText("");
                viewHolder.userAgeTextView.setPadding(0, 0, 0, 0);

                // 城市
                viewHolder.cityTextView.setText(postInfo.getBaseUser().getCity());
                viewHolder.cityTextView.setVisibility(View.VISIBLE);
                // 地址
                viewHolder.locationTextView.setText(postInfo.getBaseUser().getDistance());
                viewHolder.locationTextView.setVisibility(View.VISIBLE);

//                viewHolder.userLevelTextView.setVisibility(View.GONE);

                viewHolder.attentionView.setVisibility(View.INVISIBLE);


            }
            if (!StringUtils.isEmpty(postInfo.getBaseUser().getSchoolName())) {
                //学校名称
                viewHolder.schoolNameTextView.setVisibility(View.VISIBLE);
                viewHolder.schoolNameTextView.setText(postInfo.getBaseUser().getSchoolName());
                if (viewHolder.cityTextView.getVisibility() == View.VISIBLE) {
                    viewHolder.schoolNameTextView.setPadding(Utils.dp2px(5), 0, 0, 0);
                } else {
                    viewHolder.schoolNameTextView.setPadding(Utils.dp2px(12), 0, 0, 0);
                }
            } else {
                viewHolder.schoolNameTextView.setVisibility(View.GONE);
            }

        }

        // 时间
        viewHolder.timeTextView.setText(postInfo.getAddTime());

        // 圈子名称
//        List<OrganBaseInfo> organBaseInfoList = postInfo.getOrganList();
//        if (organBaseInfoList != null && organBaseInfoList.size() > 0) {
//            viewHolder.orgainzationRecycleView.setVisibility(View.VISIBLE);
//            viewHolder.contentTextView.setPadding(0, 0, 0, 0);
//            final PostDetailOrganizationRecyclerViewAdapter postDetailOrganizationRecyclerViewAdapter = new PostDetailOrganizationRecyclerViewAdapter(activity);
//            postDetailOrganizationRecyclerViewAdapter.setList(organBaseInfoList);
//            viewHolder.orgainzationRecycleView.setAdapter(postDetailOrganizationRecyclerViewAdapter);
//            postDetailOrganizationRecyclerViewAdapter.setOnItemListener(new ItemListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    //跳转到圈子详情
//                }
//            });
//        } else {
//            viewHolder.contentTextView.setPadding(0, Utils.dp2px(10), 0, 0);
//        }


        //跟拍人以及内容
        String content = postInfo.getContent();
        if (!StringUtils.isEmpty(content)) {
            viewHolder.contentTextView.setText(content);
            viewHolder.contentTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.contentTextView.setVisibility(View.GONE);
        }

        //店铺信息
        if (postInfo.getStoreDetail() != null) {
            final StoreInfo storeInfo = postInfo.getStoreDetail().getStoreInfo();
            if (storeInfo != null) {
                viewHolder.storeLayout.setVisibility(View.VISIBLE);
                Utils.CornerImageViewDisplayByUrl(storeInfo.getStoreIcon(),viewHolder.storeIcon,3);
                if (postInfo.getIsRecurit() == 1) {
                    viewHolder.recruitTextView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.recruitTextView.setVisibility(View.GONE);
                }
                viewHolder.storeNameTextView.setText(storeInfo.getTitle());
                StoreCategorySecondRecycleAdapter secondRecycleAdapter = new StoreCategorySecondRecycleAdapter();
                if (storeInfo.getFirstLabelInfo() != null && storeInfo.getFirstLabelInfo().getList() != null) {
                    secondRecycleAdapter.setList(storeInfo.getFirstLabelInfo().getList());
                }
                viewHolder.recycler_store_label.setAdapter(secondRecycleAdapter);
            } else {
                viewHolder.storeLayout.setVisibility(View.GONE);
            }
            viewHolder.storeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                if(storeInfo.getUserId()!=SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)){
//                    onClick.onApplyClerk(storeInfo.getStoreId());
//                }
                    //跳转到店铺详情
                    Intent intent = new Intent(activity, ShopDetailActivity.class);
                    intent.putExtra(Constants.Fields.STORE_ID, storeInfo.getStoreId());
                    activity.startActivity(intent);

                }
            });
        } else {
            viewHolder.storeLayout.setVisibility(View.GONE);
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
                //跳转到视频广场界面
                if (postInfo.getIsOriginal() == 1 && originalInfo != null) {
                }
            }
        });

        if (!isUpdate) {
            if (postInfo.getTopicType() == 2) {
                //视频
                viewHolder.gifImgView.setVisibility(View.GONE);
                viewHolder.recyclerImage.setVisibility(View.GONE);

                //视频
                Glide.with(App.getInstance()).load(postInfo.getVideoImageUrl()).apply(options).into(viewHolder.imgFirst);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.imgFirst.getLayoutParams();
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
                viewHolder.imgFirst.setLayoutParams(layoutParams);
                viewHolder.videoPlayImg.setVisibility(View.VISIBLE);
                viewHolder.imgFirst.setVisibility(View.VISIBLE);
                viewHolder.linear_image_two.setVisibility(View.VISIBLE);

                viewHolder.videoPlayImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转到视频播放界面
                        Intent intent = new Intent(activity, PKVideoPlayActivity.class);
                        intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, postInfo.getPostId());
                        intent.putExtra(Constants.Fields.COME_IN_TYPE, 1);
                        intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_POST);
                        activity.startActivity(intent);
                    }
                });

            } else {
                // 帖子图片
                if (postInfo.getImageList() != null) {

                    // 动图
                    if (postInfo.getImageList().size() == 1 && postInfo.getImageList().get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
                        viewHolder.gifImgView.setVisibility(View.VISIBLE);
                        viewHolder.recyclerImage.setVisibility(View.GONE);
//                    viewHolder.frameLayout.setVisibility(View.GONE);

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.gifImgView.getLayoutParams();
                        Glide.with(App.getInstance()).load(postInfo.getImageList().get(0).getImageUrl()).apply(gifOptions).into(viewHolder.gifImgView);

                        viewHolder.gifImgView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (activity != null && !activity.isFinishing()) {
                                    ImageViewPageDialog.newInstance(postInfo.getImageList(), 0).show(activity.getSupportFragmentManager());
                                }
                            }
                        });


                    } else {
                        viewHolder.gifImgView.setVisibility(View.GONE);
                        viewHolder.recyclerImage.setVisibility(View.VISIBLE);
//                    viewHolder.frameLayout.setVisibility(View.GONE);

                        PostDetailImageRecycleAdapter imageRecycleAdapter = new PostDetailImageRecycleAdapter(activity);

                        imageRecycleAdapter.setImageViewPageDialogClick(new PostDetailImageRecycleAdapter.ImageViewPageDialogClick() {
                            @Override
                            public void onClick(List<Image> mList, int currentItem) {
                                ImageViewPageDialog.newInstance(mList, currentItem).show(activity.getSupportFragmentManager());
                            }
                        });

                        imageRecycleAdapter.setList(postInfo.getImageList());
                        viewHolder.recyclerImage.setAdapter(imageRecycleAdapter);

                    }


                }
            }
        }


        // 点赞数量
        viewHolder.praiseTextView.setText(Utils.bigNumberToStr(postInfo.getPraiseCount(), postInfo.getPraiseCountStr()));
        final int isPraise = postInfo.getIsPraise();
        if (isPraise == 1) {
            //1 : 已点赞
            viewHolder.praiseTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
        } else {
            //0: 未点赞
            viewHolder.praiseTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
        }

        viewHolder.praiseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点赞 (1 点赞  2 取消点赞)
                int type = (isPraise == 0 | isPraise == 2) ? 1 : 2;
                postInfo.setIsPraise(type);
                postInfo.setPraiseCount(postInfo.getPraiseCount() + (type == 1 ? 1 : -1));
                notifyItemChanged(position, true);

                if (viewClick != null) {
                    viewClick.onPraise(0, postInfo.getPostId(), type, position);
                }


            }
        });

        // 显示评论数量
        viewHolder.commentTextView.setText(Utils.bigNumberToStr(postInfo.getCommentCount(), postInfo.getCommentCountStr()));
        viewHolder.commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewClick != null) {
                    viewClick.onComment(postInfo.getPostId(), 2, position);
                }
            }
        });
        // 更多按钮
        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewClick != null) {
                    viewClick.moreImg(0, position);
                }
            }
        });

        /**
         * 分享按钮
         */
        viewHolder.shareView.setText(Utils.bigNumberToStr(postInfo.getShareNumber(), postInfo.getShareNumberStr()));
        viewHolder.shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgUrl = "";

                if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
                    imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
                }

                ShareUtil.getInstance().showShare(activity, ShareUtil.POST, postInfo.getPostId(), postInfo, null, "", "", imgUrl, true);
            }
        });

        PostLabelInfoRecycleAdapter postLabelInfoRecycleAdapter = new PostLabelInfoRecycleAdapter(14);
        viewHolder.recycler_post_label.setVisibility(View.VISIBLE);
        if (postInfo.getPostLabelList() != null && postInfo.getPostLabelList().size() > 0) {
            postLabelInfoRecycleAdapter.setList(postInfo.getPostLabelList());
        } else {
            viewHolder.recycler_post_label.setVisibility(View.GONE);
        }
        viewHolder.recycler_post_label.setAdapter(postLabelInfoRecycleAdapter);


    }

    /**
     * 绑定评论
     *
     * @param viewHolder
     * @param comment
     * @param position
     */
    private void bindComment(CommentItemViewHolder viewHolder, final Comment comment, final int position, boolean isUpdate) {

        if (viewHolder == null || comment == null) {
            return;
        }


        if (position == 1) {
            if (sort == 1) {
                viewHolder.tv_sort.setText("按热度");
            } else if (sort == 2) {
                viewHolder.tv_sort.setText("按时间");
            }
            viewHolder.tv_sort.setVisibility(View.VISIBLE);
            viewHolder.line_sort.setVisibility(View.VISIBLE);
            viewHolder.tv_sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewClick.sortViewClick();
                }
            });
        } else {
            viewHolder.tv_sort.setVisibility(View.GONE);
            viewHolder.line_sort.setVisibility(View.GONE);
        }
        String userIcon = "";
        String fromBaseUserNickName = "";
        String toBaseUserNickName = "";

        SpannableStringBuilder style = new SpannableStringBuilder();

        //1--->评论,2--->回复
        final BaseUser fromBaseUser = comment.getFromBaseUser();
        if (comment.getType() == 1) {
            userIcon = fromBaseUser.getUserIcon();
            //anonymousType 匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
            if (comment.getAnonymousType() == 0) {
                fromBaseUserNickName = fromBaseUser.getNickName();
                if (fromBaseUserNickName.length() > 12) {
                    fromBaseUserNickName = fromBaseUserNickName.substring(0, 12) + "...";
                }
            } else {
                fromBaseUserNickName = "匿名";
            }

            if (fromBaseUserNickName.equals("匿名")) {
                viewHolder.formUserNicknameView.setTextColor(App.getInstance().getResources().getColor(R.color.color_666666));
            } else {
                viewHolder.formUserNicknameView.setTextColor(App.getInstance().getResources().getColor(R.color.color_576b95));
            }

            style.append(fromBaseUserNickName);
            viewHolder.formUserNicknameView.setText(style);

            //图片地址
            if (!isUpdate) {
                String videoImageUrl = null;
                final int commentType = comment.getCommentType();
                final List<Image> imageList = comment.getImageList();
                if (commentType == 1) {
                    if (imageList != null && imageList.get(0) != null) {
                        videoImageUrl = imageList.get(0).getImageUrl();
                    }
                } else if (commentType == 2) {
                    videoImageUrl = comment.getVideoImageUrl();
                }
                if (TextUtils.isEmpty(videoImageUrl)) {
                    viewHolder.linear_image_two.setVisibility(View.GONE);
                } else {
                    viewHolder.linear_image_two.setVisibility(View.VISIBLE);
                    if (commentType == 1) {//图文
                        viewHolder.video_play_img.setVisibility(View.GONE);
                        if (imageList.size() > 1) {
                            viewHolder.imageNumView.setVisibility(View.VISIBLE);
                            viewHolder.imageNumView.setText(String.valueOf(imageList.size()));
                        } else {
                            viewHolder.imageNumView.setVisibility(View.GONE);
                        }
                    } else if (commentType == 2) {//视频
                        viewHolder.video_play_img.setVisibility(View.VISIBLE);
                        viewHolder.imageNumView.setVisibility(View.GONE);
                    }
                    if (videoImageUrl.endsWith("gif")) {
                        Utils.CornerImageViewGifDisplayByUrl(videoImageUrl, viewHolder.iv_comment_image);
                    } else {
                        Utils.CornerImageViewDisplayByUrl(videoImageUrl, viewHolder.iv_comment_image,9);
                    }
                    final String finalVideoImageUrl = videoImageUrl;
                    viewHolder.iv_comment_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (commentType == 1) {
                                ImageViewPageDialog.newInstance(imageList, 0).show(activity.getSupportFragmentManager());
                            } else if (commentType == 2) {
                                //跳转到视频播放界面
                                Intent intent = new Intent(activity, VideoPlayFullScreenActivity.class);
                                intent.putExtra(Constants.Fields.VIDEO_PATH, comment.getVideoUrl());
                                intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, finalVideoImageUrl);
                                activity.startActivity(intent);
                            }
                        }
                    });
                }
            }

            int replyCount = comment.getReplyCount();
            if (replyCount > 0) {
                viewHolder.ll_comment_reply.setVisibility(View.VISIBLE);
                final List<Comment> replyList = comment.getReplyList();
                if (replyCount >= 1) {
                    viewHolder.tv_comment_1.setVisibility(View.VISIBLE);
                    final Comment comment1 = replyList.get(0);


                    if (comment1.getParentId() == comment1.getCommentParentId()) {//一级评论下的评论
                        setReplyCommentStyle(viewHolder.tv_comment_1, comment1);

                    } else {
                        setReplyReplyCommentStyle(viewHolder.tv_comment_1, comment1);
                    }

                    viewHolder.tv_comment_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (currentBaseUserId != comment1.getFromBaseUser().getUserId()) {
                                viewClick.ReplyListComment(comment1);
                            }
                        }
                    });
                } else {
                    viewHolder.tv_comment_1.setVisibility(View.GONE);
                }
                if (replyCount >= 2) {
                    viewHolder.tv_comment_2.setVisibility(View.VISIBLE);
                    final Comment comment2 = replyList.get(1);

                    if (comment2.getParentId() == comment2.getCommentParentId()) {//一级评论下的评论
                        setReplyCommentStyle(viewHolder.tv_comment_2, comment2);
                    } else {
                        setReplyReplyCommentStyle(viewHolder.tv_comment_2, comment2);
                    }

                    viewHolder.tv_comment_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (currentBaseUserId != comment2.getFromBaseUser().getUserId()) {
                                viewClick.ReplyListComment(comment2);
                            }
                        }
                    });
                } else {
                    viewHolder.tv_comment_2.setVisibility(View.GONE);
                }
                if (replyCount >= 3) {
                    viewHolder.tv_comment_3.setVisibility(View.VISIBLE);
                    final Comment comment3 = replyList.get(2);

                    if (comment3.getParentId() == comment3.getCommentParentId()) {//一级评论下的评论
                        setReplyCommentStyle(viewHolder.tv_comment_3, comment3);
                    } else {
                        setReplyReplyCommentStyle(viewHolder.tv_comment_3, comment3);
                    }
                    viewHolder.tv_comment_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (currentBaseUserId != comment3.getFromBaseUser().getUserId()) {
                                viewClick.ReplyListComment(comment3);
                            }
                        }
                    });
                } else {
                    viewHolder.tv_comment_3.setVisibility(View.GONE);
                }
                if (replyCount >= 4) {
                    viewHolder.tv_comment_more.setVisibility(View.VISIBLE);
                    String content = "共"
                            + replyCount
                            + "条回复>";
                    viewHolder.tv_comment_more.setText(content);
                    viewHolder.tv_comment_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewClick.moreComment(comment.getCommentId());
                        }
                    });
                } else {
                    viewHolder.tv_comment_more.setVisibility(View.GONE);
                }

            } else {
                viewHolder.ll_comment_reply.setVisibility(View.GONE);
            }
            viewHolder.tv_num_comment.setText(String.valueOf(replyCount));
            // 点赞数量
            viewHolder.tv_num_praise.setText(String.valueOf(comment.getPraiseCount()));
            final int isPraise = comment.getIsPraise();
            if (isPraise == 1) {
                //1 : 已点赞
                viewHolder.tv_num_praise.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
            } else {
                //0: 未点赞
                viewHolder.tv_num_praise.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
            }

            viewHolder.tv_num_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //点赞 (1 点赞  2 取消点赞)
                    int type = (isPraise == 0 | isPraise == 2) ? 1 : 2;
                    comment.setIsPraise(type);
                    comment.setPraiseCount(comment.getPraiseCount() + (type == 1 ? 1 : -1));
                    notifyItemChanged(position, true);

                    if (viewClick != null) {
                        viewClick.onPraise(1, comment.getCommentId(), type, position);
                    }


                }
            });
        }


        // 头像
        if (!isUpdate) {
            Glide.with(App.getInstance()).load(userIcon).apply(options).into(viewHolder.circleImageView);
        }


        viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity != null) {
                    //匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
                    if (fromBaseUser.getUserId() != currentBaseUserId) {
                        if (comment.getAnonymousType() == 0 || comment.getAnonymousType() == 2) {
                            //跳到用户详情
                            activity.startActivity(new Intent(activity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, fromBaseUser));
                        } else {
                            userPresenter.getAnonymityUser(currentBaseUserId, fromBaseUser.getUserId());
                        }
                    }
                }
            }
        });


        // 当前发送数据的时间
        viewHolder.timeView.setText(comment.getAddTime());
        // 内容
        if (TextUtils.isEmpty(comment.getContent())) {
            viewHolder.contentView.setVisibility(View.GONE);
        } else {
            viewHolder.contentView.setVisibility(View.VISIBLE);
            viewHolder.contentView.setText(comment.getContent());
        }

        String schoolAndDistance = null;
        String schoolName = fromBaseUser.getSchoolName();
        if (!TextUtils.isEmpty(schoolName) && !"null".equals(schoolName)) {
            schoolAndDistance = schoolName;
            String distance = fromBaseUser.getDistance();
            if (!TextUtils.isEmpty(distance) && !"null".equals(distance)) {
                schoolAndDistance = schoolName + "　" + distance;
            }
        } else {
            schoolAndDistance = "该评论来自于微信用户";
        }

        if (!TextUtils.isEmpty(schoolAndDistance)) {
            viewHolder.tv_school_distance.setText(schoolAndDistance);
        }

        // 更多按钮

        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewClick != null) {
                    viewClick.moreImg(1, position);
                }
            }
        });

    }

    /**
     * 回复一级评论
     *
     * @param textView
     * @param comment
     */
    private void setReplyCommentStyle(TextView textView, Comment comment) {
        String content = comment.getContent();
        BaseUser fromBaseUser = comment.getFromBaseUser();
        String fromBaseUserNickName = fromBaseUser.getNickName();
        if (comment.getAnonymousType() == 1 || comment.getAnonymousType() == 3) {
            fromBaseUserNickName = "匿名";
        } else {
            if (!StringUtils.isEmpty(fromBaseUserNickName)) {
                if (fromBaseUserNickName.length() > 4) {
                    fromBaseUserNickName = fromBaseUserNickName.substring(0, 4) + "...";
                }
            }
        }
        SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(fromBaseUserNickName + "：");
        style.append(content);
        // 回复昵称
        if (fromBaseUserNickName.contains("匿名")) {
            style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_666666)), 0, fromBaseUserNickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_576b95)), 0, fromBaseUserNickName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // 内容
        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), fromBaseUserNickName.length() + 1, style.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(style);
    }

    /**
     * 回复二级评论
     *
     * @param textView
     * @param comment
     */
    private void setReplyReplyCommentStyle(TextView textView, Comment comment) {
        String content = comment.getContent();
        BaseUser fromBaseUser = comment.getFromBaseUser();
        String fromBaseUserNickName = fromBaseUser.getNickName();
        if (comment.getAnonymousType() == 1 || comment.getAnonymousType() == 3) {
            fromBaseUserNickName = "匿名";
        } else {
            if (!StringUtils.isEmpty(fromBaseUserNickName)) {
                if (fromBaseUserNickName.length() > 4) {
                    fromBaseUserNickName = fromBaseUserNickName.substring(0, 4) + "...";
                }
            }
        }

        String toBaseUserNickName = comment.getToBaseUser().getNickName();
        if (comment.getAnonymousType() == 2 || comment.getAnonymousType() == 3) {
            toBaseUserNickName = "匿名";
        } else {
            if (!TextUtils.isEmpty(toBaseUserNickName)) {
                if (toBaseUserNickName.length() > 4) {
                    toBaseUserNickName = toBaseUserNickName.substring(0, 4) + "...";
                }
            }
        }

        SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(fromBaseUserNickName);
        style.append(" 回复 ");
        style.append(toBaseUserNickName);
        style.append(content);
        // 回复昵称
        if (fromBaseUserNickName.contains("匿名")) {
            style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_666666)), 0, fromBaseUserNickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_576b95)), 0, fromBaseUserNickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // 回复
        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), fromBaseUserNickName.length(), fromBaseUserNickName.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 被回复人昵称
        if (toBaseUserNickName.contains("匿名")) {
            style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_666666)), fromBaseUserNickName.length() + 4, fromBaseUserNickName.length() + 4 + toBaseUserNickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_576b95)), fromBaseUserNickName.length() + 4, fromBaseUserNickName.length() + 4 + toBaseUserNickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        // 内容
        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), fromBaseUserNickName.length() + 4 + toBaseUserNickName.length(), style.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(style);
    }

    public void addEmptyItem(String blankHint) {
        list.add(1, "暂无评论，快来写下第一条评论吧！");
        notifyDataSetChanged();
    }


    static class PostInfoViewHolder extends BaseViewHolder {


        CircleImageView userImageView;
        TextView userNicknameTextView, schoolNameTextView;
        ImageView userLevelTextView;
        ImageView userSchoolmateView, isMatchImg;
        TextView userAgeTextView;
        TextView timeTextView;
        TextView locationTextView;
        TextView cityTextView;
        TextView contentTextView, recruitTextView, storeNameTextView;
        TextView attentionView;
        FrameLayout frameLayout;

        ImageView gifImgView;
        ImageView storeIcon;

        RecyclerView recyclerImage;

        TextView praiseTextView;
        TextView commentTextView;

        ImageView moreImg;
        TextView shareView;
        RecyclerView orgainzationRecycleView, recycler_post_label, recycler_store_label;
        RelativeLayout linear_image_two, storeLayout;
        ImageView imgFirst;
        ImageView videoPlayImg;


        public PostInfoViewHolder(View itemView) {
            super(itemView);

            // 头像
            userImageView = itemView.findViewById(R.id.user_img);
            // 用户昵称
            userNicknameTextView = itemView.findViewById(R.id.user_nickname_text);
            //学校名称
            schoolNameTextView = itemView.findViewById(R.id.school_name_text_view);
            // 用户级别
//            userLevelTextView = itemView.findViewById(R.id.user_level_text);
            // 校友
            userSchoolmateView = itemView.findViewById(R.id.user_school_mate);
            //参赛标签
//            isMatchImg = itemView.findViewById(R.id.ismatch_img);
            // 时间控件
            timeTextView = itemView.findViewById(R.id.time_text);
            // 性别 1 男；2女
            userAgeTextView = itemView.findViewById(R.id.user_age_text);
            // 用户地址
            locationTextView = itemView.findViewById(R.id.location_text);
            // 城市
            cityTextView = itemView.findViewById(R.id.city_text);
            // 圈子名称
//            orgainzationRecycleView = (RecyclerView) itemView.findViewById(R.id.recycler_orgainzation);
//            FlowLayoutManager flowLayoutManager = new FlowLayoutManager(App.getInstance().getCurrentActivity());
//            orgainzationRecycleView.setLayoutManager(flowLayoutManager);
            // 内容
            contentTextView = itemView.findViewById(R.id.content_text);
            recruitTextView = itemView.findViewById(R.id.recruit_text);
            storeLayout = itemView.findViewById(R.id.store_layout);
            storeIcon = itemView.findViewById(R.id.store_img);
            storeNameTextView = itemView.findViewById(R.id.store_name_text);
            // 关注
            attentionView = itemView.findViewById(R.id.attention_tv);

            imgFirst = itemView.findViewById(R.id.img_first);
            videoPlayImg = itemView.findViewById(R.id.video_play_img);

//            //视频
//            frameLayout = itemView.findViewById(R.id.video_frame);

            linear_image_two = itemView.findViewById(R.id.linear_image_two);
            gifImgView = itemView.findViewById(R.id.gift_img);
            // 图片空间
            recyclerImage = itemView.findViewById(R.id.recycler_image);
            // 点赞按钮
            praiseTextView = itemView.findViewById(R.id.praise_text);
            // 评论按钮
            commentTextView = itemView.findViewById(R.id.comment_text);
            // 更多按钮
            moreImg = itemView.findViewById(R.id.more_img);
            // 分享
            shareView = itemView.findViewById(R.id.share_text);

            LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
            layoutManager.setAutoMeasureEnabled(true);
            recyclerImage.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(15)));
            recyclerImage.setHasFixedSize(true);
            recyclerImage.setLayoutManager(layoutManager);

            recycler_post_label = (RecyclerView) itemView.findViewById(R.id.recycler_post_label);
            recycler_store_label = (RecyclerView) itemView.findViewById(R.id.store_label_recycle_view);
            FlowLayoutManager flowLayoutManager = new FlowLayoutManager(App.getInstance().getCurrentActivity());
            recycler_post_label.setLayoutManager(flowLayoutManager);
//            LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
//            viewHolder.recycler_post_label.setLayoutManager(linearLayoutManager2);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.HORIZONTAL, false);
            recycler_store_label.setLayoutManager(linearLayoutManager);

        }
    }

    static class CommentItemViewHolder extends BaseViewHolder {

        // 头像
        CircleImageView circleImageView;
        // 评论昵称
        TextView formUserNicknameView;
        // 时间
        TextView timeView;
        // 内容
        TextView contentView;
        RelativeLayout linear_image_two;
        //播放按钮
        ImageView video_play_img;
        //图片
        ImageView iv_comment_image;
        //图片数量
        TextView imageNumView;
        //回复列表
        LinearLayout ll_comment_reply;
        TextView tv_comment_1, tv_comment_2, tv_comment_3, tv_comment_more;
        TextView tv_school_distance;
        TextView tv_num_praise, tv_num_comment;
        TextView tv_sort;
        View line_sort;
        // 更多按钮
        ImageView moreImg;
        // 分隔符
        View divider;

        public CommentItemViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.user_img);
            formUserNicknameView = itemView.findViewById(R.id.form_user_nickname_text);
            timeView = itemView.findViewById(R.id.time_text);
            contentView = itemView.findViewById(R.id.content_text);
            tv_sort = itemView.findViewById(R.id.tv_sort);
            line_sort = itemView.findViewById(R.id.line_sort);
            linear_image_two = itemView.findViewById(R.id.linear_image_two);
            video_play_img = itemView.findViewById(R.id.video_play_img);
            iv_comment_image = itemView.findViewById(R.id.iv_comment_image);
            imageNumView = itemView.findViewById(R.id.tv_num_image);
            ll_comment_reply = itemView.findViewById(R.id.ll_comment_reply);
            tv_comment_1 = itemView.findViewById(R.id.tv_comment_1);
            tv_comment_2 = itemView.findViewById(R.id.tv_comment_2);
            tv_comment_3 = itemView.findViewById(R.id.tv_comment_3);
            tv_comment_more = itemView.findViewById(R.id.tv_comment_more);
            tv_school_distance = itemView.findViewById(R.id.tv_school_distance);
            tv_num_praise = itemView.findViewById(R.id.tv_num_praise);
            tv_num_comment = itemView.findViewById(R.id.tv_num_comment);
            moreImg = itemView.findViewById(R.id.more_img);
            divider = itemView.findViewById(R.id.divider_line);

        }

    }

    static class EmputyItemViewHolder extends BaseViewHolder {

        ImageView empty_image;
        TextView empty_textView;

        public EmputyItemViewHolder(View itemView) {
            super(itemView);
            empty_image = itemView.findViewById(R.id.empty_image);
            empty_textView = itemView.findViewById(R.id.empty_textView);
        }
    }

    public void onDestroy() {
        if (activity != null) {
            activity = null;
        }

        if (list != null) {
            list.clear();
        }
        list = null;
    }


    public interface ViewClick {
        //点赞 0是帖子
        void onPraise(int itemType, long postId, int type, int position);

        void onComment(long postId, int type, int position);

        void attentionView(int type, long userId, int position);

        void moreImg(int type, int position);

        void moreComment(long commentId);

        void ReplyListComment(Comment comment);

        void sortViewClick();

        void onApplyClerk(long storeId);
    }
}
