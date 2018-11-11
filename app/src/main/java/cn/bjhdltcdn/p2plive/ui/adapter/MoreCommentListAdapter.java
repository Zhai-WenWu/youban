package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Comment;
import cn.bjhdltcdn.p2plive.model.ConfessionComment;
import cn.bjhdltcdn.p2plive.model.HelpComment;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.PlayComment;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class MoreCommentListAdapter extends BaseRecyclerAdapter {

    private AppCompatActivity activity;
    //模块(1帖子,2表白,3PK挑战,4帮帮忙),
    private int type;
    //评论信息
    private final int ITEM_COMMENT_1 = 1;
    //评论为空
    private final int ITEM_COMMENT_2 = 2;


    private List<Object> list;

    public List<Object> getList() {
        return list;
    }

    private RequestOptions options;

    private ViewClick viewClick;

    private long userId;

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

    public MoreCommentListAdapter(int type, AppCompatActivity activity) {
        this.type = type;
        this.activity = activity;
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

    }

    public void setList(List<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<Object> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        if (this.list == null) {
            return;
        }

        this.list.addAll(list);
        notifyDataSetChanged();

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


    public void setParentCommentView(Object object) {

        if (this.list == null) {
            this.list = new ArrayList<>(1);
        }

        if (this.list.size() > 0) {
            if (type == 1) {
                if (this.list.get(0) instanceof Comment) {
                    this.list.remove(0);
                }
            } else if (type == 2) {
                if (this.list.get(0) instanceof ConfessionComment) {
                    this.list.remove(0);
                }
            } else if (type == 3) {
                if (this.list.get(0) instanceof PlayComment) {
                    this.list.remove(0);
                }
            } else if (type == 4) {
                if (this.list.get(0) instanceof HelpComment) {
                    this.list.remove(0);
                }
            }
        }

        this.list.add(0, object);

        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public int getItemViewType(int position) {
        Object object = list.get(position);
        if (object != null) {
            return ITEM_COMMENT_1;
        } else {
            return ITEM_COMMENT_2;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Object object = list.get(position);

        int itemType = getItemViewType(position);
        switch (itemType) {

            case ITEM_COMMENT_1:// 评论

                if (holder instanceof CommentItemViewHolder) {

                    CommentItemViewHolder viewHolder = (CommentItemViewHolder) holder;

                    switch (type) {
                        case 1:
                            if (object instanceof Comment) {
                                Comment comment = (Comment) object;
                                bindComment(viewHolder, comment, position);
                            }
                            break;
                        case 2:
                            if (object instanceof ConfessionComment) {
                                ConfessionComment comment = (ConfessionComment) object;
                                bindConfessionComment(viewHolder, comment, position);
                            }
                            break;
//                        case 3:
//                            if (object instanceof PlayComment) {
//                                PlayComment comment = (PlayComment) object;
//                                bindPKComment(viewHolder, comment, position);
//                            }
//                            break;
                        case 4:
                            if (object instanceof HelpComment) {
                                HelpComment comment = (HelpComment) object;
                                bindHelpComment(viewHolder, comment, position);
                            }
                            break;

                    }
                }

                break;
            case ITEM_COMMENT_2:// 评论

                if (holder instanceof EmputyItemViewHolder) {

                    EmputyItemViewHolder viewHolder = (EmputyItemViewHolder) holder;
                    if (object instanceof String) {
                        viewHolder.empty_textView.setText((String) list.get(position));
                    }
                }

                break;

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = null;
        CommentItemViewHolder commentItemViewHolder = null;
        EmputyItemViewHolder emputyItemViewHolder = null;

        switch (itemType) {
            case ITEM_COMMENT_1:
                itemView = View.inflate(App.getInstance(), R.layout.post_comment_list_item_layout, null);
                commentItemViewHolder = new CommentItemViewHolder(itemView);
                return commentItemViewHolder;
            case ITEM_COMMENT_2:
                itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_common_layout, viewGroup, false);
                emputyItemViewHolder = new EmputyItemViewHolder(itemView);
                return emputyItemViewHolder;

        }

        return super.onCreateViewHolder(viewGroup, itemType);
    }


    /**
     * 绑定帖子评论
     *
     * @param viewHolder
     * @param comment
     * @param position
     */
    private void bindComment(CommentItemViewHolder viewHolder, final Comment comment, final int position) {

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
        userIcon = comment.getFromBaseUser().getUserIcon();
        //anonymousType 匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
        if (comment.getAnonymousType() == 0 || comment.getAnonymousType() == 2) {
            fromBaseUserNickName = comment.getFromBaseUser().getNickName();
            if (fromBaseUserNickName.length() > 12) {
                fromBaseUserNickName = fromBaseUserNickName.substring(0, 12) + "...";
            }
        } else {
            fromBaseUserNickName = "匿名";
        }

        if ("匿名".equals(fromBaseUserNickName)) {
            viewHolder.formUserNicknameView.setTextColor(App.getInstance().getResources().getColor(R.color.color_666666));
        } else {
            viewHolder.formUserNicknameView.setTextColor(App.getInstance().getResources().getColor(R.color.color_576b95));
        }

        viewHolder.formUserNicknameView.setText(fromBaseUserNickName);
        if (comment.getType() == 1) {
            if (position == 0) {
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
                viewHolder.divider.setVisibility(View.VISIBLE);
            } else {
                viewHolder.divider.setVisibility(View.GONE);
            }
            viewHolder.tv_num_comment.setText(String.valueOf(comment.getReplyCount()));
            // 内容
            if (TextUtils.isEmpty(comment.getContent())) {
                viewHolder.contentView.setVisibility(View.GONE);
            } else {
                viewHolder.contentView.setVisibility(View.VISIBLE);
                viewHolder.contentView.setText(comment.getContent());
            }
        } else if (comment.getType() == 2) {

            if (comment.getParentId() == comment.getCommentParentId()) {//一级评论下的评论
                viewHolder.contentView.setText(comment.getContent());
            } else {
                toBaseUserNickName = comment.getToBaseUser().getNickName();
                if (!StringUtils.isEmpty(toBaseUserNickName)) {
                    if (toBaseUserNickName.length() > 4) {
                        toBaseUserNickName = toBaseUserNickName.substring(0, 4) + "...";
                    }
                }
                style.append("回复：");
                style.append("@" + toBaseUserNickName + "：");
                style.append(comment.getContent());
                // 回复
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                // 被回复人昵称
                if ("匿名".contains(toBaseUserNickName)) {
                    style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_666666)), 3, 4 + toBaseUserNickName.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_576b95)), 3, 4 + toBaseUserNickName.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }

                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), 4 + toBaseUserNickName.length() + 1, style.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                viewHolder.contentView.setText(style);
            }
            viewHolder.tv_num_comment.setText("");
            // 内容
        }

        // 头像
        Glide.with(App.getInstance()).load(userIcon).apply(options).into(viewHolder.circleImageView);
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
                notifyItemChanged(position);

                if (viewClick != null) {
                    viewClick.onPraise(comment.getCommentId(), type, position);
                }
            }
        });

        viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity != null) {
                    //匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
                    if (fromBaseUser.getUserId() != userId) {
                        if (comment.getAnonymousType() == 0 || comment.getAnonymousType() == 2) {
                            //跳到用户详情
                            activity.startActivity(new Intent(activity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, fromBaseUser));
                        } else {
                            userPresenter.getAnonymityUser(userId, fromBaseUser.getUserId());
                        }
                    }
                }
            }
        });


        // 当前发送数据的时间
        viewHolder.timeView.setText(comment.getAddTime());

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
     * 绑定表白评论
     *
     * @param viewHolder
     * @param comment
     * @param position
     */
    private void bindConfessionComment(CommentItemViewHolder viewHolder, final ConfessionComment comment, final int position) {

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
        userIcon = comment.getFromBaseUser().getUserIcon();
        //anonymousType 匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
        if (comment.getAnonymousType() == 0 || comment.getAnonymousType() == 2) {
            fromBaseUserNickName = comment.getFromBaseUser().getNickName();
            if (fromBaseUserNickName.length() > 12) {
                fromBaseUserNickName = fromBaseUserNickName.substring(0, 12) + "...";
            }
        } else {
            fromBaseUserNickName = "匿名";
        }

        if ("匿名".equals(fromBaseUserNickName)) {
            viewHolder.formUserNicknameView.setTextColor(App.getInstance().getResources().getColor(R.color.color_666666));
        } else {
            viewHolder.formUserNicknameView.setTextColor(App.getInstance().getResources().getColor(R.color.color_576b95));
        }
        viewHolder.formUserNicknameView.setText(fromBaseUserNickName);
        if (comment.getType() == 1) {
            if (position == 0) {
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
                viewHolder.divider.setVisibility(View.VISIBLE);
            } else {
                viewHolder.divider.setVisibility(View.GONE);
            }
            viewHolder.tv_num_comment.setText(String.valueOf(comment.getReplyCount()));
            // 内容
            if (TextUtils.isEmpty(comment.getContent())) {
                viewHolder.contentView.setVisibility(View.GONE);
            } else {
                viewHolder.contentView.setVisibility(View.VISIBLE);
                viewHolder.contentView.setText(comment.getContent());
            }
        } else if (comment.getType() == 2) {
            if (comment.getParentId() == comment.getCommentParentId()) {//一级评论下的评论
                // 内容
                viewHolder.contentView.setText(comment.getContent());
            } else {
                toBaseUserNickName = comment.getToBaseUser().getNickName();

                if (!StringUtils.isEmpty(toBaseUserNickName)) {
                    if (toBaseUserNickName.length() > 4) {
                        toBaseUserNickName = toBaseUserNickName.substring(0, 4) + "...";
                    }
                }


                style.append("回复：");
                style.append("@" + toBaseUserNickName + "：");
                style.append(comment.getContent());

                // 回复
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                // 被回复人昵称
                if ("匿名".contains(toBaseUserNickName)) {
                    style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_666666)), 3, 4 + toBaseUserNickName.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_576b95)), 3, 4 + toBaseUserNickName.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                // 内容
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), 4 + toBaseUserNickName.length() + 1, style.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                viewHolder.contentView.setText(style);
            }
            viewHolder.tv_num_comment.setText("");
        }

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
                notifyItemChanged(position);

                if (viewClick != null) {
                    viewClick.onPraise(comment.getCommentId(), type, position);
                }


            }
        });

        // 头像
        Glide.with(App.getInstance()).load(userIcon).apply(options).into(viewHolder.circleImageView);


        viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity != null) {
                    //匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
                    if (fromBaseUser.getUserId() != userId) {
                        if (comment.getAnonymousType() == 0 || comment.getAnonymousType() == 2) {
                            //跳到用户详情
                            activity.startActivity(new Intent(activity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, fromBaseUser));
                        } else {
                            userPresenter.getAnonymityUser(userId, fromBaseUser.getUserId());
                        }
                    }
                }
            }
        });


        // 当前发送数据的时间
        viewHolder.timeView.setText(comment.getAddTime());
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
     * 绑定帮帮忙评论
     *
     * @param viewHolder
     * @param comment
     * @param position
     */
    private void bindHelpComment(CommentItemViewHolder viewHolder, final HelpComment comment, final int position) {

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
        userIcon = fromBaseUser.getUserIcon();
        fromBaseUserNickName = fromBaseUser.getNickName();
        if (fromBaseUserNickName.length() > 12) {
            fromBaseUserNickName = fromBaseUserNickName.substring(0, 12) + "...";
        }
        viewHolder.formUserNicknameView.setTextColor(App.getInstance().getResources().getColor(R.color.color_576b95));

        viewHolder.formUserNicknameView.setText(fromBaseUserNickName);

        if (comment.getType() == 1) {
            if (position == 0) {
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
                viewHolder.divider.setVisibility(View.VISIBLE);
            } else {
                viewHolder.divider.setVisibility(View.GONE);
            }
            viewHolder.tv_num_comment.setText(String.valueOf(comment.getReplyCount()));
            // 内容
            if (TextUtils.isEmpty(comment.getContent())) {
                viewHolder.contentView.setVisibility(View.GONE);
            } else {
                viewHolder.contentView.setVisibility(View.VISIBLE);
                viewHolder.contentView.setText(comment.getContent());
            }
        } else if (comment.getType() == 2) {
            if (comment.getParentId() == comment.getCommentParentId()) {//一级评论下的评论
                // 内容
                viewHolder.contentView.setText(comment.getContent());
            } else {
                toBaseUserNickName = comment.getToBaseUser().getNickName();

                if (!StringUtils.isEmpty(toBaseUserNickName)) {
                    if (toBaseUserNickName.length() > 4) {
                        toBaseUserNickName = toBaseUserNickName.substring(0, 4) + "...";
                    }
                }

                style.append("回复：");
                style.append("@" + toBaseUserNickName + "：");
                style.append(comment.getContent());

                // 回复
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                // 被回复人昵称
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_576b95)), 3, 4 + toBaseUserNickName.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                // 内容
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), 4 + toBaseUserNickName.length() + 1, style.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                viewHolder.contentView.setText(style);
            }
            viewHolder.tv_num_comment.setText("");
        }

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
                notifyItemChanged(position);

                if (viewClick != null) {
                    viewClick.onPraise(comment.getCommentId(), type, position);
                }


            }
        });
        // 头像
        Glide.with(App.getInstance()).load(userIcon).apply(options).into(viewHolder.circleImageView);


        viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity != null) {
                    //匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
                    if (fromBaseUser.getUserId() != userId) {
                        Intent intent = new Intent(activity, UserDetailsActivity.class);
                        intent.putExtra(Constants.KEY.KEY_OBJECT, fromBaseUser);
                        activity.startActivity(intent);
                    }
                }
            }
        });


        // 当前发送数据的时间
        viewHolder.timeView.setText(comment.getAddTime());
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

    public void addEmptyItem(String blankHint) {
        list.add(1, "暂无评论，快来写下第一条评论吧！");
        notifyDataSetChanged();
    }


    class CommentItemViewHolder extends BaseViewHolder {

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
            iv_comment_image = itemView.findViewById(R.id.iv_comment_image);
            video_play_img = itemView.findViewById(R.id.video_play_img);
            imageNumView = itemView.findViewById(R.id.tv_num_image);
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
        void onPraise(long postId, int type, int position);

        void attentionView(int type, long userId, int position);

        void moreImg(int type, int position);

        void sortViewClick();
    }
}
