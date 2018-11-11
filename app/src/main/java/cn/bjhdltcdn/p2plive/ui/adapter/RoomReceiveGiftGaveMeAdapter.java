package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.MyProps;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by xiawenquan on 17/3/30.
 */

public class RoomReceiveGiftGaveMeAdapter extends BaseRecyclerAdapter {

    private List<MyProps> list;

    private Context mContext;

    private AttentionClickViewCall attentionClickViewCall;

    public AttentionClickViewCall getAttentionClickViewCall() {
        return attentionClickViewCall;
    }

    public void setAttentionClickViewCall(AttentionClickViewCall attentionClickViewCall) {
        this.attentionClickViewCall = attentionClickViewCall;
    }

    public RoomReceiveGiftGaveMeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<MyProps> getList() {
        return list;
    }

    public void setList(List<MyProps> list) {
        this.list = list;
    }

    public void addList(List<MyProps> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        if (this.list == null) {
            this.list = new ArrayList<>(1);
        }
        this.list.addAll(list);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        return new ItemViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.room_receive_gift_gave_me_list_item_layout, null));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        if (getItemCount() > 0) {

            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                MyProps myProps = list.get(position);

                final BaseUser baseUser = myProps.getBaseUser();
                if (baseUser != null) {
                    // 头像
                    Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), itemViewHolder.circleImageView);
                    // 昵称
                    itemViewHolder.nickNameView.setText(baseUser.getNickName());
                }

                // 金币
                SpannableStringBuilder style = new SpannableStringBuilder(myProps.getTotalGold() + "金币");
                style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_fc5555)), 0, style.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_666666)), style.length() - 2, style.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                itemViewHolder.propsGlodView.setText(style);

                if (baseUser != null && baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    itemViewHolder.attentionView.setVisibility(View.GONE);
                    itemViewHolder.messagenView.setVisibility(View.GONE);
                } else {

                    itemViewHolder.messagenView.setVisibility(View.VISIBLE);
                    // 发送私信
                    itemViewHolder.messagenView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mContext != null) {
                                RongIMutils.startToConversation(mContext, baseUser.getUserId() + "", baseUser.getNickName());
                            }

                        }
                    });

                    itemViewHolder.attentionView.setVisibility(View.VISIBLE);
                    final int type;
                    // 关注状态
                    if (baseUser.getIsAttention() == 1) {
                        itemViewHolder.attentionView.setBackgroundResource(R.drawable.attention);
                        type = 2;
                    } else {
                        itemViewHolder.attentionView.setBackgroundResource(R.drawable.sigle_attention);
                        type = 1;
                    }

                    itemViewHolder.attentionView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (attentionClickViewCall != null && type > 0) {
                                attentionClickViewCall.attentionClickView(type, position);
                            }
                        }
                    });

                }


            }

        }
    }


    public void clearDate() {
        if (mContext != null) {
            mContext = null;
        }

        if (list != null) {
            list.clear();
            list = null;
        }

        if (attentionClickViewCall != null) {
            attentionClickViewCall = null;
        }

    }

    class ItemViewHolder extends BaseViewHolder {

        CircleImageView circleImageView;
        TextView nickNameView;
        TextView propsGlodView;

        ImageView messagenView;
        ImageView attentionView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.user_image_view);
            nickNameView = itemView.findViewById(R.id.nick_name_view);
            propsGlodView = itemView.findViewById(R.id.props_glod_view);
            messagenView = itemView.findViewById(R.id.messagen_view);
            attentionView = itemView.findViewById(R.id.attention_view);
        }
    }

    /**
     * 关注按钮点击事件回调
     */
    public interface AttentionClickViewCall {
        /**
         * 点击事件
         *
         * @param type     1：关注 2：取消关注
         * @param position
         */
        void attentionClickView(int type, int position);

    }
}
