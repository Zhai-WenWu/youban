package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class SayloveCommentListAdapter extends BaseAdapter {
    private AppCompatActivity context;
    private List<ConfessionComment> list;
    public itemOperationClick itemOperationClick;
    private RequestOptions options;
    //排序(1按热度,2按时间)
    private int sort = 1;
    private long currentBaseUserId;

    private UserPresenter userPresenter;

    public void setUserPresenter(UserPresenter userPresenter) {
        this.userPresenter = userPresenter;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public SayloveCommentListAdapter(AppCompatActivity context) {
        this.context = context;
        options = new RequestOptions();
        options.placeholder(R.color.color_bebebe);
        options.centerCrop();
        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
    }

    public void setList(List<ConfessionComment> list) {
        this.list = list;
    }

    public void addList(List<ConfessionComment> list) {
        this.list.addAll(list);
    }


    public void setItemOperationClick(SayloveCommentListAdapter.itemOperationClick itemOperationClick) {
        this.itemOperationClick = itemOperationClick;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public ConfessionComment getItem(int position) {

        return list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItemContent(int position, String Content) {
        list.get(position).setContent(Content);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }


    public void addItem(ConfessionComment confessionComment) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(0, confessionComment);
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.post_comment_list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.circleImageView = convertView.findViewById(R.id.user_img);
            viewHolder.formUserNicknameView = convertView.findViewById(R.id.form_user_nickname_text);
            viewHolder.userLevelView = convertView.findViewById(R.id.user_level_text);
            viewHolder.timeView = convertView.findViewById(R.id.time_text);
            viewHolder.contentView = convertView.findViewById(R.id.content_text);
            viewHolder.tv_sort = convertView.findViewById(R.id.tv_sort);
            viewHolder.line_sort = convertView.findViewById(R.id.line_sort);
            viewHolder.linear_image_two = convertView.findViewById(R.id.linear_image_two);
            viewHolder.video_play_img = convertView.findViewById(R.id.video_play_img);
            viewHolder.iv_comment_image = convertView.findViewById(R.id.iv_comment_image);
            viewHolder.imageNumView = convertView.findViewById(R.id.tv_num_image);
            viewHolder.ll_comment_reply = convertView.findViewById(R.id.ll_comment_reply);
            viewHolder.tv_comment_1 = convertView.findViewById(R.id.tv_comment_1);
            viewHolder.tv_comment_2 = convertView.findViewById(R.id.tv_comment_2);
            viewHolder.tv_comment_3 = convertView.findViewById(R.id.tv_comment_3);
            viewHolder.tv_comment_more = convertView.findViewById(R.id.tv_comment_more);
            viewHolder.tv_school_distance = convertView.findViewById(R.id.tv_school_distance);
            viewHolder.tv_num_praise = convertView.findViewById(R.id.tv_num_praise);
            viewHolder.tv_num_comment = convertView.findViewById(R.id.tv_num_comment);
            viewHolder.moreImg = convertView.findViewById(R.id.more_img);
            viewHolder.divider = convertView.findViewById(R.id.divider_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ConfessionComment comment = list.get(position);
        if (comment != null) {
            // 用户等级默认不显示
            viewHolder.userLevelView.setVisibility(View.GONE);
            if (position == 0) {
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
                        itemOperationClick.sortViewClick();
                    }
                });
            } else {
                viewHolder.tv_sort.setVisibility(View.GONE);
                viewHolder.line_sort.setVisibility(View.GONE);
            }


            String userIcon = "";
            String fromBaseUserNickName = "";
            BaseUser fromBaseUser = comment.getFromBaseUser();
            //1--->评论,2--->回复
            if (comment.getType() == 1) {

                userIcon = fromBaseUser.getUserIcon();

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
                viewHolder.formUserNicknameView.setText(fromBaseUserNickName);

                //图片地址
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
                                ImageViewPageDialog.newInstance(imageList, 0).show(context.getSupportFragmentManager());
                            } else if (commentType == 2) {
                                //跳转到视频播放界面
                                Intent intent = new Intent(context, VideoPlayFullScreenActivity.class);
                                intent.putExtra(Constants.Fields.VIDEO_PATH, comment.getVideoUrl());
                                intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, finalVideoImageUrl);
                                context.startActivity(intent);
                            }
                        }
                    });
                }
                int replyCount = comment.getReplyCount();
                if (replyCount > 0) {
                    viewHolder.ll_comment_reply.setVisibility(View.VISIBLE);
                    final List<ConfessionComment> replyList = comment.getReplyList();
                    if (replyCount >= 1) {
                        viewHolder.tv_comment_1.setVisibility(View.VISIBLE);
                        final ConfessionComment confessionComment1 = replyList.get(0);

                        if (confessionComment1.getParentId() == confessionComment1.getCommentParentId()) {//一级评论下的评论
                            setReplyCommentStyle(viewHolder.tv_comment_1, confessionComment1);
                        } else {
                            setReplyReplyCommentStyle(viewHolder.tv_comment_1, confessionComment1);
                        }
                        viewHolder.tv_comment_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentBaseUserId != confessionComment1.getFromBaseUser().getUserId()) {
                                    itemOperationClick.ReplyListComment(confessionComment1);
                                }
                            }
                        });
                    } else {
                        viewHolder.tv_comment_1.setVisibility(View.GONE);
                    }
                    if (replyCount >= 2) {
                        viewHolder.tv_comment_2.setVisibility(View.VISIBLE);
                        final ConfessionComment confessionComment2 = replyList.get(1);
                        if (confessionComment2.getParentId() == confessionComment2.getCommentParentId()) {//一级评论下的评论
                            setReplyCommentStyle(viewHolder.tv_comment_2, confessionComment2);
                        } else {
                            setReplyReplyCommentStyle(viewHolder.tv_comment_2, confessionComment2);
                        }

                        viewHolder.tv_comment_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentBaseUserId != confessionComment2.getFromBaseUser().getUserId()) {
                                    itemOperationClick.ReplyListComment(confessionComment2);
                                }
                            }
                        });
                    } else {
                        viewHolder.tv_comment_2.setVisibility(View.GONE);
                    }
                    if (replyCount >= 3) {
                        viewHolder.tv_comment_3.setVisibility(View.VISIBLE);
                        final ConfessionComment confessionComment3 = replyList.get(2);
                        if (confessionComment3.getParentId() == confessionComment3.getCommentParentId()) {//一级评论下的评论
                            setReplyCommentStyle(viewHolder.tv_comment_3, confessionComment3);
                        } else {
                            setReplyReplyCommentStyle(viewHolder.tv_comment_3, confessionComment3);
                        }
                        viewHolder.tv_comment_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentBaseUserId != confessionComment3.getFromBaseUser().getUserId()) {
                                    itemOperationClick.ReplyListComment(confessionComment3);
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
                                itemOperationClick.moreComment(comment.getCommentId());
                            }
                        });
                    } else {
                        viewHolder.tv_comment_more.setVisibility(View.GONE);
                    }

                } else {
                    viewHolder.ll_comment_reply.setVisibility(View.GONE);
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
                        notifyDataSetChanged();
                        if (itemOperationClick != null) {
                            itemOperationClick.onPraise(comment.getCommentId(), type, position);
                        }

                    }
                });

                // 显示评论数量
                viewHolder.tv_num_comment.setText(String.valueOf(replyCount));
            }

            // 头像
            Glide.with(App.getInstance()).load(userIcon).apply(options).into(viewHolder.circleImageView);

            viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (context != null) {
                        if (comment.getFromBaseUser().getUserId() != currentBaseUserId) {
                            if ((comment.getAnonymousType() == 0 || comment.getAnonymousType() == 2)) {
                                Intent intent = new Intent(context, UserDetailsActivity.class);
                                intent.putExtra(Constants.KEY.KEY_OBJECT, comment.getFromBaseUser());
                                context.startActivity(intent);
                            } else {
                                userPresenter.getAnonymityUser(currentBaseUserId, comment.getFromBaseUser().getUserId());
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
            viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOperationClick != null) {
                        itemOperationClick.moreClick(position, comment.getType());
                    }
                }
            });
            if (position == list.size() - 1) {
                viewHolder.divider.setVisibility(View.GONE);
            } else {
                viewHolder.divider.setVisibility(View.GONE);
            }

        }

        return convertView;
    }

    /**
     * 回复一级评论
     *
     * @param textView
     * @param comment
     */
    private void setReplyCommentStyle(TextView textView, ConfessionComment comment) {
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
    private void setReplyReplyCommentStyle(TextView textView, ConfessionComment comment) {
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

    class ViewHolder {
        // 头像
        CircleImageView circleImageView;
        // 评论昵称
        TextView formUserNicknameView;
        // 用户等级
        ImageView userLevelView;
        // 时间
        TextView timeView;
        // 内容
        TextView contentView;
        //        //视频
//        FrameLayout frameLayout;
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


    }

    public interface itemOperationClick {
        void ReplyListComment(ConfessionComment comment);

        void moreClick(int position, int type);

        //点赞
        void onPraise(long commentParentId, int type, int position);

        void moreComment(long commentId);

        void sortViewClick();
    }


}
