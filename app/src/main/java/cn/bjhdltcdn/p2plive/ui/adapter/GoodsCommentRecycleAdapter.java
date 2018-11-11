package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import cn.bjhdltcdn.p2plive.model.ShopComment;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class GoodsCommentRecycleAdapter extends BaseRecyclerAdapter {

    private final RequestOptions gifOptions;
    private AppCompatActivity activity;

    private List<ShopComment> list=new ArrayList<ShopComment>();

    public List<ShopComment> getList() {
        return list;
    }

    private RequestOptions options;

    private ViewClick viewClick;

    private long currentBaseUserId;
    private UserPresenter userPresenter;
    private long storeUserId;

    public void setUserPresenter(UserPresenter userPresenter) {
        this.userPresenter = userPresenter;
    }

    public void setStoreUserId(long storeUserId) {
        this.storeUserId = storeUserId;
    }

    public void setViewClick(ViewClick viewClick) {
        this.viewClick = viewClick;
    }

    public GoodsCommentRecycleAdapter(AppCompatActivity activity) {
        this.activity = activity;
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon);
        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        gifOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setList(List<ShopComment> list,boolean payloads) {
        if (this.list != null && this.list.size() > 0) {
            this.list.clear();
        }
        if (list != null&&list.size()>0) {
            this.list.addAll(list);
        }
        if(payloads){
            notifyItemRangeChanged(0,list.size(),payloads);
        }else{
            notifyDataSetChanged();
        }

    }

    public void removeAllList(){
        if (this.list != null && this.list.size() > 0) {
            this.list.clear();
        }
        notifyDataSetChanged();
    }


    public void addList(List<ShopComment> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        if (this.list == null) {
            return;
        }

        int size = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(size, this.list.size() - size);

    }

    public void addItem(ShopComment shopComment, boolean isUpdate) {
        if (shopComment == null) {
            return;
        }

        if (list == null) {
            return;
        }

        list.add(1, shopComment);
        if (isUpdate) {
            // 从第1个位置开始更新，更新条数是1
            notifyItemRangeInserted(1, 1);
        }


    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if(list==null||list.size()<=0){
            CommentItemViewHolder viewHolder = (CommentItemViewHolder) holder;
            bindComment(viewHolder, null, position, false);
            return;
        }
        if (payloads == null || payloads.isEmpty()) {

            Object object = list.get(position);

                    if (holder instanceof CommentItemViewHolder) {

                        CommentItemViewHolder viewHolder = (CommentItemViewHolder) holder;
                        if (object instanceof ShopComment) {
                            ShopComment comment = (ShopComment) object;
                            bindComment(viewHolder, comment, position, false);
                        }
                    }

        } else {

            Object object = list.get(position);


                    if (holder instanceof CommentItemViewHolder) {

                        CommentItemViewHolder viewHolder = (CommentItemViewHolder) holder;
                        if (object instanceof ShopComment) {
                            ShopComment comment = (ShopComment) object;
                            bindComment(viewHolder, comment, position, true);
                        }
                    }

        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {


        View itemView = null;
        CommentItemViewHolder commentItemViewHolder = null;
        itemView = View.inflate(App.getInstance(), R.layout.goods_comment_recycle_item_layout, null);
        commentItemViewHolder = new CommentItemViewHolder(itemView);
        return commentItemViewHolder;


    }



    /**
     * 绑定评论
     *
     * @param viewHolder
     * @param comment
     * @param position
     */
    private void bindComment(CommentItemViewHolder viewHolder, final ShopComment comment, final int position, boolean isUpdate) {

        if (viewHolder == null || comment == null) {
            return;
        }

        String userIcon = "";
        String fromBaseUserNickName = "";
        //1--->评论,2--->回复
        final BaseUser fromBaseUser = comment.getFromBaseUser();
        if (comment.getType() == 1) {
            userIcon = fromBaseUser.getUserIcon();
            fromBaseUserNickName = fromBaseUser.getNickName();
            viewHolder.formUserNicknameView.setText(fromBaseUserNickName);

            //图片地址
            if (!isUpdate) {
                String videoImageUrl = "";
                String imageUrl = "";
                final int commentType = comment.getCommentType();//(0-->普通文本,1-->图/图文,2--->视频,3-->图和视频),
                final List<Image> imageList = (List<Image>) comment.getImageList();
                if (imageList != null && imageList.get(0) != null) {
                    imageUrl = imageList.get(0).getImageUrl();
                }
                videoImageUrl = comment.getVideoImageUrl();
                if (commentType == 0) {
                    viewHolder.linear_image_one.setVisibility(View.GONE);
                    viewHolder.linear_image_two.setVisibility(View.GONE);
                } else {
                    if (commentType == 1) {
                        viewHolder.linear_image_one.setVisibility(View.VISIBLE);
                        viewHolder.linear_image_one.getLayoutParams().width = Utils.dp2px(0);
                        viewHolder.linear_image_one.getLayoutParams().height = Utils.dp2px(140);
                        viewHolder.linear_image_two.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) viewHolder.linear_image_two.getLayoutParams();
                        layoutParams.width = Utils.dp2px(218);
                        layoutParams.height = Utils.dp2px(140);
                        layoutParams.setMargins(0,Utils.dp2px(12),0,0);
                    } else if (commentType == 2) {
                        viewHolder.linear_image_one.setVisibility(View.VISIBLE);
                        viewHolder.linear_image_one.getLayoutParams().width = Utils.dp2px(218);
                        viewHolder.linear_image_one.getLayoutParams().height = Utils.dp2px(140);
                        viewHolder.linear_image_two.setVisibility(View.GONE);
                    } else if (commentType == 3) {
                        viewHolder.linear_image_one.setVisibility(View.VISIBLE);
                        viewHolder.linear_image_one.getLayoutParams().width = Utils.dp2px(111);
                        viewHolder.linear_image_one.getLayoutParams().height = Utils.dp2px(111);
                        viewHolder.linear_image_two.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) viewHolder.linear_image_two.getLayoutParams();
                        layoutParams.width = Utils.dp2px(111);
                        layoutParams.height = Utils.dp2px(111);
                        layoutParams.setMargins(Utils.dp2px(10),Utils.dp2px(12),0,0);
                    }
                    if (imageList != null &&imageList.size() > 1) {
                        viewHolder.imageNumView.setVisibility(View.VISIBLE);
                        viewHolder.imageNumView.setText(String.valueOf(imageList.size()));
                    } else {
                        viewHolder.imageNumView.setVisibility(View.GONE);
                    }
                    if (imageUrl.endsWith("gif")) {
                        Utils.CornerImageViewGifDisplayByUrl(imageUrl, viewHolder.iv_comment_image);
                    } else {
                        Utils.CornerImageViewDisplayByUrl(imageUrl, viewHolder.iv_comment_image,9);
                    }
                    Utils.CornerImageViewDisplayByUrl(videoImageUrl, viewHolder.iv_comment_video_image,9);

                    viewHolder.iv_comment_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageViewPageDialog.newInstance(imageList, 0).show(activity.getSupportFragmentManager());
                        }
                    });
                    final String finalvideoImageUrl = videoImageUrl;
                    viewHolder.iv_comment_video_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //跳转到视频播放界面
                            Intent intent = new Intent(activity, VideoPlayFullScreenActivity.class);
                            intent.putExtra(Constants.Fields.VIDEO_PATH, comment.getVideoUrl());
                            intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, finalvideoImageUrl);
                            activity.startActivity(intent);
                        }
                    });
                }
                if (imageList == null){
                    viewHolder.linear_image_two.setVisibility(View.GONE);
                }
            }

            // 头像
            if (!isUpdate) {
                Glide.with(App.getInstance()).load(userIcon).apply(options).into(viewHolder.circleImageView);
            }


            int replyCount = comment.getReplyCount();
            if (replyCount > 0) {
                viewHolder.recyclerView.setVisibility(View.VISIBLE);
                MoreGoodsCommentRecycleAdapter moreGoodsCommentRecycleAdapter=new MoreGoodsCommentRecycleAdapter(activity);
                moreGoodsCommentRecycleAdapter.setStoreUserId(storeUserId);
                moreGoodsCommentRecycleAdapter.setList(comment.getReplyList());
                viewHolder.recyclerView.setAdapter(moreGoodsCommentRecycleAdapter);
                moreGoodsCommentRecycleAdapter.setViewClick(new MoreGoodsCommentRecycleAdapter.ViewClick() {
                    @Override
                    public void onComment(long parentId, int type, long toUserId,String toUserNickName, int position) {
                        if (viewClick != null) {
                            viewClick.onComment(comment.getCommentId(),parentId, 2 ,comment.getProductId(),toUserId,toUserNickName,position);
                        }
                    }
                });
            } else {
                viewHolder.recyclerView.setVisibility(View.GONE);
            }

            // 显示评论数量
            viewHolder.tv_num_comment.setText(Utils.bigNumberToStr(comment.getReplyCount(), comment.getReplyCountStr()));
            viewHolder.tv_num_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewClick != null) {
                        viewClick.onComment(comment.getCommentId(), comment.getCommentId(),2 ,comment.getProductId(),comment.getFromBaseUser().getUserId(),comment.getFromBaseUser().getNickName(),position);
                    }
                }
            });
            // 点赞数量
            viewHolder.tv_num_praise.setText(Utils.bigNumberToStr(comment.getPraiseCount(), comment.getPraiseCountStr()));
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
                        viewClick.onPraise(comment.getCommentId(), type, position);
                    }


                }
            });


            viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity != null) {
                        //匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
                        if (fromBaseUser.getUserId() != currentBaseUserId) {
                            //跳到用户详情
                            activity.startActivity(new Intent(activity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, fromBaseUser));
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

            setEvaluateShowLayout(comment.getEvalScore(),viewHolder.evaluateStarLayout);
        }

    }



    /**
     * 设置星级
     */
    private void setEvaluateShowLayout(double averageScore,LinearLayout evaluateStarLayout) {
        //显示评价星数
        if (averageScore >= 0) {

            evaluateStarLayout.removeAllViews();

            if(averageScore==0){
                //0星
                for (int i = 0; i < (5 - Math.round(averageScore)); i++) {
                    ImageView starImage = new ImageView(activity);
                    starImage.setImageResource(R.drawable.rating_unselect_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, Utils.dp2px(10), 0);
                    params.height = Utils.dp2px(17);//设置图片的高度
                    params.width = Utils.dp2px(17); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }
                return;
            }

            if(averageScore>0&&averageScore<1){
                //大于0 小于1 星
                if (Math.round(averageScore) > averageScore) {
                    ImageView starImage = new ImageView(activity);
                    starImage.setImageResource(R.drawable.star_half_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, Utils.dp2px(10), 0);
                    params.height = Utils.dp2px(17);//设置图片的高度
                    params.width = Utils.dp2px(17); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }else{
                    ImageView starImage = new ImageView(activity);
                    starImage.setImageResource(R.drawable.rating_unselect_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, Utils.dp2px(10), 0);
                    params.height = Utils.dp2px(17);//设置图片的高度
                    params.width = Utils.dp2px(17); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }
            }else{
                //大于等于1星
                // 新增完整的星
                for (int i = 0; i < (int) averageScore; i++) {
                    ImageView starImage = new ImageView(activity);
                    starImage.setImageResource(R.drawable.rating_select_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, Utils.dp2px(10), 0);
                    params.height = Utils.dp2px(17);//设置图片的高度
                    params.width = Utils.dp2px(17); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }

                // 四舍五入，则新增半个星
                if (Math.round(averageScore) > averageScore) {
                    ImageView starImage = new ImageView(activity);
                    starImage.setImageResource(R.drawable.star_half_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, Utils.dp2px(10), 0);
                    params.height = Utils.dp2px(17);//设置图片的高度
                    params.width = Utils.dp2px(17); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }
            }

            // 如果还没有满5个星，则补加未选中的星
            if (evaluateStarLayout.getChildCount() < 5) {
                for (int i = 0; i < (5 - Math.round(averageScore)); i++) {
                    ImageView starImage = new ImageView(activity);
                    starImage.setImageResource(R.drawable.rating_unselect_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, Utils.dp2px(10), 0);
                    params.height = Utils.dp2px(17);//设置图片的高度
                    params.width = Utils.dp2px(17); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }
            }

        } else {
//            evaluateStarLayout.setVisibility(View.GONE);
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
        RelativeLayout linear_image_one,linear_image_two;
        //播放按钮
        ImageView video_play_img;
        //图片
        ImageView iv_comment_image,iv_comment_video_image;
        //图片数量
        TextView imageNumView;
        //回复列表
        RecyclerView recyclerView;
        TextView tv_num_praise, tv_num_comment;
        // 分隔符
        View divider;
        LinearLayout evaluateStarLayout;

        public CommentItemViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.user_img);
            formUserNicknameView = itemView.findViewById(R.id.form_user_nickname_text);
            timeView = itemView.findViewById(R.id.time_text);
            contentView = itemView.findViewById(R.id.content_text);
            linear_image_one= itemView.findViewById(R.id.linear_image_one);
            video_play_img = itemView.findViewById(R.id.video_play_img);
            iv_comment_video_image= itemView.findViewById(R.id.iv_comment_video_image);
            linear_image_two = itemView.findViewById(R.id.linear_image_two);
            iv_comment_image = itemView.findViewById(R.id.iv_comment_image);
            imageNumView = itemView.findViewById(R.id.tv_num_image);
            recyclerView=itemView.findViewById(R.id.comment_recycle_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
            layoutManager.setAutoMeasureEnabled(true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            tv_num_praise = itemView.findViewById(R.id.tv_num_praise);
            tv_num_comment = itemView.findViewById(R.id.tv_num_comment);
            divider = itemView.findViewById(R.id.divider_line);
            evaluateStarLayout= (LinearLayout) itemView.findViewById(R.id.evaluate_star_layout);

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
        void onPraise(long commentId, int type, int position);

        void onComment(long commentParentId,long parentId, int type, long productId,long toUserId,String toUserNickName,int position);

        void attentionView(int type, long userId, int position);

        void moreImg(int type, int position);

        void moreComment(long commentId);

        void ReplyListComment(Comment comment);

        void sortViewClick();
    }
}
