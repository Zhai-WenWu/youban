package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.bjhdltcdn.p2plive.model.ShopComment;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class MoreGoodsCommentRecycleAdapter extends BaseRecyclerAdapter {

    private AppCompatActivity activity;


    private List<ShopComment> list=new ArrayList<ShopComment>();

    public List<ShopComment> getList() {
        return list;
    }

    private RequestOptions options;

    private ViewClick viewClick;

    private long userId;

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

    public MoreGoodsCommentRecycleAdapter(AppCompatActivity activity) {
        this.activity = activity;
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

    }

    public void setList(List<ShopComment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<ShopComment> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        if (this.list == null) {
            return;
        }

        this.list.addAll(list);
        notifyDataSetChanged();

    }

    public void addItem(ShopComment comment, boolean isUpdate) {
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




    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(list==null||list.size()<=0){
           return;
        }
        Object object = list.get(position);


                if (holder instanceof CommentItemViewHolder) {

                    CommentItemViewHolder viewHolder = (CommentItemViewHolder) holder;
                            if (object instanceof ShopComment) {
                                ShopComment shopComment = (ShopComment) object;
                                bindComment(viewHolder, shopComment, position);
                            }
                }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = null;
        CommentItemViewHolder commentItemViewHolder = null;
                itemView = View.inflate(App.getInstance(), R.layout.goods_comment_more_recycle_item_layout, null);
                commentItemViewHolder = new CommentItemViewHolder(itemView);
                return commentItemViewHolder;
    }


    /**
     * 绑定帖子评论
     *
     * @param viewHolder
     * @param comment
     * @param position
     */
    private void bindComment(CommentItemViewHolder viewHolder, final ShopComment comment, final int position) {

        if (viewHolder == null || comment == null) {
            return;
        }


//        String fromBaseUserNickName = "";
//        String toBaseUserNickName = "";
//
//        //1--->评论,2--->回复
        final BaseUser fromBaseUser = comment.getFromBaseUser();
//        fromBaseUserNickName = fromBaseUser.getNickName();
//        if (fromBaseUserNickName.length() > 5) {
//            fromBaseUserNickName = fromBaseUserNickName.substring(0, 4) + "..";
//        }
        final BaseUser toBaseUser = comment.getToBaseUser();
//        toBaseUserNickName = toBaseUser.getNickName();
//        if (toBaseUserNickName.length() > 5) {
//            toBaseUserNickName = toBaseUserNickName.substring(0, 4) + "..";
//        }

        if(comment.getParentId()==comment.getCommentParentId()){
//            viewHolder.replyView.setVisibility(View.GONE);
//            viewHolder.toUserNicknameView.setVisibility(View.GONE);
//            viewHolder.formUserNicknameView.setText(fromBaseUserNickName+"：");
            setReplyCommentStyle(viewHolder.formUserNicknameView, comment,position);
        }else{
//            viewHolder.replyView.setVisibility(View.VISIBLE);
//            viewHolder.toUserNicknameView.setVisibility(View.VISIBLE);
//            viewHolder.formUserNicknameView.setText(fromBaseUserNickName);
            setReplyReplyCommentStyle(viewHolder.formUserNicknameView, comment,position);
        }


//        viewHolder.toUserNicknameView.setText(toBaseUserNickName+"：");


//            // 内容
//            if (TextUtils.isEmpty(comment.getContent())) {
//                viewHolder.contentView.setVisibility(View.GONE);
//            } else {
//                viewHolder.contentView.setVisibility(View.VISIBLE);
//                viewHolder.contentView.setText(comment.getContent());
//            }

        viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewClick.onComment(comment.getCommentId(),2,fromBaseUser.getUserId(),fromBaseUser.getNickName(),position);
            }
        });

//        viewHolder.formUserNicknameView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (fromBaseUser.getUserId() != userId) {
//                    //跳到用户详情
//                    activity.startActivity(new Intent(activity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, fromBaseUser));
//                }
//            }
//        });
//
//
//
//        viewHolder.toUserNicknameView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (toBaseUser.getUserId() != userId) {
//                    //跳到用户详情
//                    activity.startActivity(new Intent(activity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, toBaseUser));
//                }
//            }
//        });

    }

    /**
     * 回复一级评论
     *
     * @param textView
     * @param comment
     */
    private void setReplyCommentStyle(TextView textView, ShopComment comment,int position) {
        String content = comment.getContent();
        final BaseUser fromBaseUser = comment.getFromBaseUser();
//        if(fromBaseUser.getUserId()==storeUserId){
//            fromBaseUser.setNickName("掌柜");
//        }
        String fromBaseUserNickName = fromBaseUser.getNickName();
        if (!StringUtils.isEmpty(fromBaseUserNickName)) {
            if (fromBaseUserNickName.length() > 5) {
                fromBaseUserNickName = fromBaseUserNickName.substring(0, 4) + "..."+"：";
            }else{
                fromBaseUserNickName = fromBaseUserNickName +"：";
            }
        }
        SpannableString str = new SpannableString(fromBaseUserNickName+content);
        str.setSpan(new MyClickText(activity,fromBaseUser,0,0,0),0, fromBaseUserNickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new MyClickText(activity,fromBaseUser,1,comment.getCommentId(),position),fromBaseUserNickName.length(), str.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //当然这里也可以通过setSpan来设置哪些位置的文本哪些颜色
        textView.setText(str);
        textView.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
    }

    /**
     * 回复二级评论
     *
     * @param textView
     * @param comment
     */
    private void setReplyReplyCommentStyle(TextView textView, ShopComment comment,int position) {
        String content = comment.getContent();
        final BaseUser fromBaseUser = comment.getFromBaseUser();
//        if(fromBaseUser.getUserId()==storeUserId){
//            fromBaseUser.setNickName("掌柜");
//        }
        String fromBaseUserNickName = fromBaseUser.getNickName();
        if (!StringUtils.isEmpty(fromBaseUserNickName)) {
            if (fromBaseUserNickName.length() > 5) {
                fromBaseUserNickName = fromBaseUserNickName.substring(0, 4) + "...";
            }
        }
        final BaseUser toBaseUser = comment.getToBaseUser();
//        if(toBaseUser.getUserId()==storeUserId){
//            toBaseUser.setNickName("掌柜");
//        }
        String toBaseUserNickName = toBaseUser.getNickName();
        if (!TextUtils.isEmpty(toBaseUserNickName)) {
            if (toBaseUserNickName.length() > 5) {
                toBaseUserNickName = toBaseUserNickName.substring(0, 4) + "..."+"：";
            }else{
                toBaseUserNickName = toBaseUserNickName+"：";
            }
        }

        SpannableString str = new SpannableString(fromBaseUserNickName+" 回复 "+toBaseUserNickName+content);
        str.setSpan(new MyClickText(activity,fromBaseUser,0,0,0),0, fromBaseUserNickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new MyClickText(activity,fromBaseUser,1,comment.getCommentId(),position),fromBaseUserNickName.length(), fromBaseUserNickName.length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new MyClickText(activity,toBaseUser,0,0,0),fromBaseUserNickName.length() + 4, fromBaseUserNickName.length() + 4 + toBaseUserNickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new MyClickText(activity,fromBaseUser,1,comment.getCommentId(),position),fromBaseUserNickName.length() + 4 + toBaseUserNickName.length(), str.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //当然这里也可以通过setSpan来设置哪些位置的文本哪些颜色
        textView.setText(str);
        textView.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
    }





    class CommentItemViewHolder extends BaseViewHolder {

        View rootLayout;
        // 评论昵称
        TextView formUserNicknameView;
        // 评论昵称
        TextView toUserNicknameView;
        // 内容
        TextView contentView;

        TextView replyView;

        public CommentItemViewHolder(View itemView) {
            super(itemView);
            rootLayout= itemView.findViewById(R.id.root_layout);
            formUserNicknameView = itemView.findViewById(R.id.from_baseuser_name_textview);
            toUserNicknameView = itemView.findViewById(R.id.to_baseuser_name_textview);
            replyView= itemView.findViewById(R.id.tv_2);
            contentView = itemView.findViewById(R.id.comment_content);

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

        void onComment(long parentId, int type, long toUserId,String toUserNickName,int position);
    }

    class MyClickText extends ClickableSpan{
        private Context context;
        private BaseUser baseUser;//需要跳转的baseUser
        private int type;
        private long commentId;
        private int position;
        public MyClickText(Context context,BaseUser baseUser,int type,long commentId,int position) {
            this.context = context;
            this.baseUser=baseUser;
            this.type=type;
            this.commentId=commentId;
            this.position=position;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            //设置文本的颜色
            if(type==1){
                ds.setColor(App.getInstance().getResources().getColor(R.color.color_151515));
            }else{
                ds.setColor(App.getInstance().getResources().getColor(R.color.color_536895));
            }
            //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if(type==1){
                //回复
                viewClick.onComment(commentId,2,baseUser.getUserId(),baseUser.getNickName(),position);
            }else {
                if (baseUser.getUserId() != userId) {
                    //跳到用户详情
                    activity.startActivity(new Intent(activity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                }
            }
        }
    }
}
