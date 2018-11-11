package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import cn.bjhdltcdn.p2plive.model.GroupApply;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.OrganApply;
import cn.bjhdltcdn.p2plive.model.YouBanActivityMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanGroupMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanHelpInfoMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanOrganizationMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanSayLoveInfoMessageModel;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiawenquan on 17/12/5.
 */

public class NotifyListItemAdapter extends BaseRecyclerAdapter {

    // 活动通知item
    private final int ACTIVITY_ITEM_1 = 1;

    //圈子通知item
    private final int ORGANIZATION_ITEM_2 = 2;

    //圈子评论、回复item
    private final int ORGANIZATION_ITEM_3 = 3;

    //群申请结果通知item
    private final int GROUP_ITEM_4 = 4;

    // 表白评论、回复item
    private final int SAYLOVE_ITEM_5 = 5;

    //帮帮忙评论回复
    private final int HELP_ITEM_6 = 6;

    private AppCompatActivity activity;

    private List<Object> list;

    private RequestOptions options;

    private UserIconClickListener userIconClickListener;

    private ReplyViewClickListener replyViewClickListener;

    private NotifyItemRemovedViewClickListener notifyItemRemovedViewClickListener;

    public void setUserIconClickListener(UserIconClickListener userIconClickListener) {
        this.userIconClickListener = userIconClickListener;
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setReplyViewClickListener(ReplyViewClickListener replyViewClickListener) {
        this.replyViewClickListener = replyViewClickListener;
    }

    public void setNotifyItemRemovedViewClickListener(NotifyItemRemovedViewClickListener notifyItemRemovedViewClickListener) {
        this.notifyItemRemovedViewClickListener = notifyItemRemovedViewClickListener;
    }

    public NotifyListItemAdapter(AppCompatActivity activity) {
        this.activity = activity;
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon);
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * @param list 数据源
     */
    public void addList(List list) {
        if (this.list == null) {
            return;
        }

        if (list == null) {
            return;
        }


        this.list.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {

        Object object = list.get(position);
        if (object instanceof YouBanActivityMessageModel) {// 活动
            return ACTIVITY_ITEM_1;

        } else if (object instanceof YouBanOrganizationMessageModel) {// 圈子
            YouBanOrganizationMessageModel youBanOrganizationMessageModel = (YouBanOrganizationMessageModel) object;
            switch (youBanOrganizationMessageModel.getMessageType()) {
                // 圈子拒绝和同意(通知结果)
                case 10002:
                case 10003:
                    return ORGANIZATION_ITEM_2;

                // 圈子中帖子的评论、回复
                case 10005:
                case 10006:
                    return ORGANIZATION_ITEM_3;

            }

        } else if (object instanceof YouBanGroupMessageModel) {// 群
            YouBanGroupMessageModel youBanGroupMessageModel = (YouBanGroupMessageModel) object;
            switch (youBanGroupMessageModel.getMessageType()) {
                // 群拒绝和同意(结果通知)
                case 30002:
                case 30003:
                    return GROUP_ITEM_4;
            }

        } else if (object instanceof YouBanSayLoveInfoMessageModel) {// 表白
            return SAYLOVE_ITEM_5;
        } else if (object instanceof YouBanHelpInfoMessageModel) {
            return HELP_ITEM_6;
        }

        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        try {
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;

            final Object object = list.get(position);


            int itemType = getItemViewType(position);
            switch (itemType) {

                // 活动通知
                case ACTIVITY_ITEM_1:
                    if (object instanceof YouBanActivityMessageModel) {// 活动
                        final YouBanActivityMessageModel youBanActivityMessageModel = (YouBanActivityMessageModel) object;
                        final BaseUser baseUser = youBanActivityMessageModel.getBaseUser();
                        if (baseUser != null) {
                            // 头像
                            options.placeholder(R.mipmap.error_user_icon);
                            Glide.with(App.getInstance()).asBitmap().load(baseUser.getUserIcon()).apply(options).into(viewHolder.circleImageView);
                            viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (userIconClickListener != null && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                        userIconClickListener.onClick(baseUser,false);
                                    }

                                }
                            });
                            // 昵称
                            viewHolder.nickNameView.setText(baseUser.getNickName());

                        }

                        // 通知内容
                        viewHolder.contenView.setText(youBanActivityMessageModel.getMessageTips());

                        // 时间
                        viewHolder.timerView.setText(TimeUtils.getFriendlyTimeSpanByNow(youBanActivityMessageModel.getAddTime()));


                        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (itemClickListener != null) {
                                    itemClickListener.onItemClick(youBanActivityMessageModel);
                                }
                            }
                        });
                    }


                    break;


                // 圈子，群 申请的结果通知
                case ORGANIZATION_ITEM_2:
                case GROUP_ITEM_4:

                    if (object instanceof YouBanOrganizationMessageModel) {// 圈子
                        final YouBanOrganizationMessageModel youBanOrganizationMessageModel = (YouBanOrganizationMessageModel) object;
                        final OrganApply organApply = youBanOrganizationMessageModel.getOrganApply();
                        if (organApply != null) {
                            // 圈子封面
                            options.placeholder(R.mipmap.error_bg);
                            Glide.with(App.getInstance()).load(organApply.getOrganImg()).apply(options).into(viewHolder.imageView);
                            // 圈子昵称
                            viewHolder.nickNameView.setText(organApply.getOrganName());
                        }

                        // 通知内容
                        viewHolder.contenView.setText(youBanOrganizationMessageModel.getMessageTips());

                        // 时间
                        viewHolder.timerView.setText(TimeUtils.getFriendlyTimeSpanByNow(youBanOrganizationMessageModel.getAddTime()));

                        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (itemClickListener != null) {
                                    itemClickListener.onItemClick(youBanOrganizationMessageModel);
                                }
                            }
                        });

                    } else if (object instanceof YouBanGroupMessageModel) {// 群

                        final YouBanGroupMessageModel youBanGroupMessageModel = (YouBanGroupMessageModel) object;
                        final GroupApply groupApply = youBanGroupMessageModel.getGroupApply();
                        if (groupApply != null) {
                            // 群头像
                            options.placeholder(R.mipmap.error_group_icon);
                            Glide.with(App.getInstance()).load(groupApply.getGroupImg()).apply(options).into(viewHolder.imageView);

                            // 群昵称
                            viewHolder.nickNameView.setText(groupApply.getGroupName());
                        }

                        // 通知内容
                        viewHolder.contenView.setText(youBanGroupMessageModel.getMessageTips());

                        // 时间
                        viewHolder.timerView.setText(TimeUtils.getFriendlyTimeSpanByNow(youBanGroupMessageModel.getAddTime()));

                        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (itemClickListener != null) {
                                    itemClickListener.onItemClick(youBanGroupMessageModel);
                                }
                            }
                        });

                    }


                    break;

                // 评论、回复
                case ORGANIZATION_ITEM_3:
                case SAYLOVE_ITEM_5:
                case HELP_ITEM_6:

                    if (object instanceof YouBanSayLoveInfoMessageModel) {// 表白
                        final YouBanSayLoveInfoMessageModel youBanSayLoveInfoMessageModel = (YouBanSayLoveInfoMessageModel) object;
                        final BaseUser baseUser = youBanSayLoveInfoMessageModel.getBaseUser();
                        if (baseUser != null) {
                            // 头像
                            options.placeholder(R.mipmap.error_user_icon);
                            Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(options).into(viewHolder.circleImageView);
                            viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (userIconClickListener != null && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                        if (youBanSayLoveInfoMessageModel.getAnonymousType() != 1) {
                                            //不是自己 并且不是匿名才可点击跳到用户详情界面
                                            userIconClickListener.onClick(baseUser, false);
                                        } else {
                                            userIconClickListener.onClick(baseUser, true);
                                        }
                                    }

                                }
                            });
                            // 昵称
                            viewHolder.nickNameView.setText(baseUser.getNickName());

                            // 时间
                            viewHolder.timerView.setText(TimeUtils.getFriendlyTimeSpanByNow(youBanSayLoveInfoMessageModel.getAddTime()));

                            // 回复内容
                            if (youBanSayLoveInfoMessageModel.getMessageType() == 20001) {// 评论
                                setPicOrVideoDisplay(viewHolder, youBanSayLoveInfoMessageModel.getVideoImageUrl(), youBanSayLoveInfoMessageModel.getVideoUrl(), youBanSayLoveInfoMessageModel.getCommentType());
                            }
                            viewHolder.contenView.setText("回复：" + youBanSayLoveInfoMessageModel.getMessageTips());

                            // 校园表白墙名称
                            viewHolder.tagView.setText("校园表白墙");

                            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (itemClickListener != null) {
                                        itemClickListener.onItemClick(youBanSayLoveInfoMessageModel);
                                    }
                                }
                            });

                            viewHolder.replyView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (replyViewClickListener != null) {
                                        replyViewClickListener.onClickListener(object);
                                    }
                                }
                            });

                        }
                    } else if (object instanceof YouBanOrganizationMessageModel) {// 圈子帖子评论
                        final YouBanOrganizationMessageModel youBanOrganizationMessageModel = (YouBanOrganizationMessageModel) object;

                        final BaseUser baseUser = youBanOrganizationMessageModel.getBaseUser();
                        if (baseUser != null) {
                            // 头像
                            options.placeholder(R.mipmap.error_user_icon);
                            Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(options).into(viewHolder.circleImageView);
                            viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (userIconClickListener != null && youBanOrganizationMessageModel.getAnonymousType() != 1 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                        userIconClickListener.onClick(baseUser, false);
                                    }

                                }
                            });
                            // 昵称
                            viewHolder.nickNameView.setText(baseUser.getNickName());

                        }

                        // 时间
                        viewHolder.timerView.setText(TimeUtils.getFriendlyTimeSpanByNow(youBanOrganizationMessageModel.getAddTime()));

                        // 回复内容
//                        if (youBanOrganizationMessageModel.getMessageType() == 10005) {// 评论
//                            viewHolder.contenView.setText("回复：" + youBanOrganizationMessageModel.getMessageTips());
//                        } else {
//                            viewHolder.contenView.setText("回复：" + youBanOrganizationMessageModel.getMessageTips());
//                        }
                        if (youBanOrganizationMessageModel.getMessageType() == 10005) {// 评论
                            setPicOrVideoDisplay(viewHolder, youBanOrganizationMessageModel.getVideoImageUrl(), youBanOrganizationMessageModel.getVideoUrl(), youBanOrganizationMessageModel.getCommentType());
                        }
                        viewHolder.contenView.setText("回复：" + youBanOrganizationMessageModel.getMessageTips());

                        // 圈子名称
                        viewHolder.tagView.setText(youBanOrganizationMessageModel.getOrganName());

                        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (itemClickListener != null) {
                                    itemClickListener.onItemClick(youBanOrganizationMessageModel);
                                }
                            }
                        });

                        viewHolder.replyView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (replyViewClickListener != null) {
                                    replyViewClickListener.onClickListener(object);
                                }
                            }
                        });


                    } else if (object instanceof YouBanHelpInfoMessageModel) {

                        final YouBanHelpInfoMessageModel youBanHelpInfoMessageModel = (YouBanHelpInfoMessageModel) object;

                        final BaseUser baseUser = youBanHelpInfoMessageModel.getBaseUser();
                        if (baseUser != null) {
                            // 头像
                            options.placeholder(R.mipmap.error_user_icon);
                            Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(options).into(viewHolder.circleImageView);
                            viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (userIconClickListener != null && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                                        userIconClickListener.onClick(baseUser, false);
                                    }

                                }
                            });
                            // 昵称
                            viewHolder.nickNameView.setText(baseUser.getNickName());

                        }

                        // 时间
                        viewHolder.timerView.setText(TimeUtils.getFriendlyTimeSpanByNow(youBanHelpInfoMessageModel.getAddTime()));

                        // 回复内容
//                        if (youBanHelpInfoMessageModel.getMessageType() == 70001) {// 评论
//                            viewHolder.contenView.setText("回复：" + youBanHelpInfoMessageModel.getMessageTips());
//                        } else {
//                            viewHolder.contenView.setText("回复：" + youBanHelpInfoMessageModel.getMessageTips());
//                        }
                        if (youBanHelpInfoMessageModel.getMessageType() == 70001) {// 评论
                            setPicOrVideoDisplay(viewHolder, youBanHelpInfoMessageModel.getVideoImageUrl(), youBanHelpInfoMessageModel.getVideoUrl(), youBanHelpInfoMessageModel.getCommentType());
                        }
                        viewHolder.contenView.setText("回复：" + youBanHelpInfoMessageModel.getMessageTips());

                        // 圈子名称
                        viewHolder.tagView.setText("同学帮帮忙");

                        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (itemClickListener != null) {
                                    itemClickListener.onItemClick(youBanHelpInfoMessageModel);
                                }
                            }
                        });

                        viewHolder.replyView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (replyViewClickListener != null) {
                                    replyViewClickListener.onClickListener(object);
                                }
                            }
                        });
                    }
                    break;

            }

            // 删除按钮
            viewHolder.deleteViewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getList().remove(viewHolder.getAdapterPosition());
                    notifyItemRemoved(viewHolder.getAdapterPosition());

                    if (notifyItemRemovedViewClickListener != null) {
                        notifyItemRemovedViewClickListener.notifyItemRemoved(position, object);
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setPicOrVideoDisplay(final ItemViewHolder viewHolder, final String videoImageUrl, final String videoUrl, final int commentType) {
        if (TextUtils.isEmpty(videoImageUrl)) {
            viewHolder.linear_image_two.setVisibility(View.GONE);
        } else {
            viewHolder.linear_image_two.setVisibility(View.VISIBLE);
            if (commentType == 1) {//图文
                viewHolder.video_play_img.setVisibility(View.GONE);
            } else if (commentType == 2) {//视频
                viewHolder.video_play_img.setVisibility(View.VISIBLE);
            }
            if (videoImageUrl.endsWith("gif")) {
                Utils.CornerImageViewGifDisplayByUrl(videoImageUrl, viewHolder.iv_comment_image);
            } else {
                Utils.CornerImageViewDisplayByUrl(videoImageUrl, viewHolder.iv_comment_image,9);
            }
            viewHolder.iv_comment_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (commentType == 1) {
                        List<Image> list = new ArrayList<>(1);
                        Image image = new Image();
                        image.setImageUrl(videoImageUrl);
                        list.add(image);
                        ImageViewPageDialog.newInstance(list, 0).show(activity.getSupportFragmentManager());
                    } else if (commentType == 2) {
                        //跳转到视频播放界面
                        Intent intent = new Intent(activity, VideoPlayFullScreenActivity.class);
                        intent.putExtra(Constants.Fields.VIDEO_PATH, videoUrl);
                        intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, videoImageUrl);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = null;
        switch (itemType) {

            // 活动通知
            case ACTIVITY_ITEM_1:


                itemView = View.inflate(App.getInstance(), R.layout.activity_notify_item_layout, null);

                break;


            // 圈子，群 申请的结果通知
            case ORGANIZATION_ITEM_2:
            case GROUP_ITEM_4:
                itemView = View.inflate(App.getInstance(), R.layout.organization_notify_item_layout, null);

                break;

            // 评论、回复
            case ORGANIZATION_ITEM_3:
            case SAYLOVE_ITEM_5:
            case HELP_ITEM_6:
                itemView = View.inflate(App.getInstance(), R.layout.organization_comment_notify_item_layout, null);

                break;

        }

        if (itemView == null) {
            return null;
        }

        return new ItemViewHolder(itemView);


    }


    static class ItemViewHolder extends BaseViewHolder {

        View rootView;
        CircleImageView circleImageView;
        ImageView imageView;
        ImageView okImageView;
        ImageView cancelImageView;
        TextView nickNameView;
        TextView contenView;
        TextView timerView;
        RelativeLayout linear_image_two;
        //播放按钮
        ImageView video_play_img;
        //图片
        ImageView iv_comment_image;
        TextView tagView;
        TextView resultView;
        TextView replyView;
        TextView deleteViewRight;


        public ItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            circleImageView = itemView.findViewById(R.id.circle_image_view);
            imageView = itemView.findViewById(R.id.image_view);
            okImageView = itemView.findViewById(R.id.ok_image_view);
            cancelImageView = itemView.findViewById(R.id.cancel_image_view);
            nickNameView = itemView.findViewById(R.id.nicke_text_view);
            contenView = itemView.findViewById(R.id.content_view);
            timerView = itemView.findViewById(R.id.timer_view);
            linear_image_two = itemView.findViewById(R.id.linear_image_two);
            video_play_img = itemView.findViewById(R.id.video_play_img);
            iv_comment_image = itemView.findViewById(R.id.iv_comment_image);
            tagView = itemView.findViewById(R.id.tag_view);
            resultView = itemView.findViewById(R.id.result_view);
            replyView = itemView.findViewById(R.id.reply_view);
            deleteViewRight = itemView.findViewById(R.id.smMenuViewRight);

        }
    }

    public interface UserIconClickListener {
        /**
         * 点击事件
         *
         * @param baseUser    用户信息
         * @param isAnonymous 是否匿名
         */
        void onClick(BaseUser baseUser, boolean isAnonymous);
    }


    public interface ItemClickListener {
        /**
         * 点击事件
         *
         * @param object
         */
        void onItemClick(Object object);

    }

    /**
     * 点击回复按钮
     */
    public interface ReplyViewClickListener {
        void onClickListener(Object object);
    }


    /**
     * 删除事件
     */
    public interface NotifyItemRemovedViewClickListener {

        void notifyItemRemoved(int position, Object object);
    }

}
