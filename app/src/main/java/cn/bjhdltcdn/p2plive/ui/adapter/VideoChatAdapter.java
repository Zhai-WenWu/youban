package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * 视频的聊天
 */
public class VideoChatAdapter extends BaseRecyclerAdapter {

    private List<VideoCMDTextMessageModel> list;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof VideoChatViewHolder) {
            try {
                VideoChatViewHolder videoChatHolder = (VideoChatViewHolder) holder;
                videoChatHolder.textView.setText("");
                VideoCMDTextMessageModel videoCMDTextMessage = list.get(position);
                String itemTextMessage = videoCMDTextMessage.getMessageTips();
                if (!TextUtils.isEmpty(itemTextMessage)) {
                    BaseUser messageUser = videoCMDTextMessage.getBaseUser();
                    if (messageUser != null) {
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        String nikeName = messageUser.getNickName();
                        if (TextUtils.isEmpty(nikeName)) {
                            nikeName = "对方用户";
                        }
                        if (!TextUtils.isEmpty(nikeName)) {
                            builder.append(nikeName + ": " + itemTextMessage);
                            ForegroundColorSpan foregroundColorSpan = null;
                            if (SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) == messageUser.getUserId()) {//本地发送
                                nikeName = "我";
                                builder.clear();
                                builder.append(nikeName + ": " + itemTextMessage);
                                foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ffda44));
                            } else {//接收文本
                                foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_e4e4e4));
                            }
                            builder.setSpan(foregroundColorSpan, 0, nikeName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            videoChatHolder.textView.setText(builder);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.video_chat_item_layout, null);
        return new VideoChatViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }


    /**
     * 新增一条数据
     *
     * @param textMessageModel
     */
    public void addItemData(VideoCMDTextMessageModel textMessageModel) {
        if (textMessageModel == null) {
            return;
        }

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(textMessageModel);
        notifyDataSetChanged();

    }


    class VideoChatViewHolder extends BaseViewHolder {

        private TextView textView;

        public VideoChatViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
