package cn.bjhdltcdn.p2plive.provider;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.YouBanFlashSalesHelpMessageModel;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;
import io.rong.imkit.YouBanFlashSalesHelpMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * 商品试用的item
 * 自定义样式
 */


@ProviderTag(messageContent = YouBanFlashSalesHelpMessage.class)
public class YouBanFlashSalesHelpMessageProvider extends IContainerItemProvider.MessageProvider {
    private AppCompatActivity context;

    @Override
    public void bindView(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

    }

    @Override
    public Spannable getContentSummary(MessageContent messageContent) {

        YouBanFlashSalesHelpMessage youBanFlashSalesHelpMessage = (YouBanFlashSalesHelpMessage) messageContent;

        // 内容
        String content = youBanFlashSalesHelpMessage.getExtra();

        Spannable spannable = null;

        if (!StringUtils.isEmpty(content)) {
            try {
                final YouBanFlashSalesHelpMessageModel youBanFlashSalesHelpMessageModel = JsonUtil.getObjectMapper().readValue(content, YouBanFlashSalesHelpMessageModel.class);
                if (youBanFlashSalesHelpMessageModel != null) {
                    if (youBanFlashSalesHelpMessageModel.getMessageType() == 8000) {
                        spannable = new SpannableString("欢迎您来友伴");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return spannable;
    }

    @Override
    public void onItemClick(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = (AppCompatActivity) context;

        View itemView = View.inflate(App.getInstance(), R.layout.item_list_message_flash_sales, null);

        ItemViewHolder viewHolder = new ItemViewHolder();
        viewHolder.itemLayout = itemView.findViewById(R.id.item_layout);
        viewHolder.contentView = itemView.findViewById(R.id.tv_content);
        viewHolder.salesImageView = itemView.findViewById(R.id.iv_sales);
        viewHolder.lookView = itemView.findViewById(R.id.tv_look);

        itemView.setTag(viewHolder);


        return itemView;
    }

    @Override
    public void bindView(View view, int i, Object object) {

        if (object instanceof UIMessage) {
            final UIMessage uiMessage = (UIMessage) object;
            if (uiMessage.getContent() instanceof YouBanFlashSalesHelpMessage) {
                YouBanFlashSalesHelpMessage youBanFlashSalesHelpMessage = (YouBanFlashSalesHelpMessage) uiMessage.getContent();

                String content = youBanFlashSalesHelpMessage.getExtra();
                Logger.json(content);

                if (!StringUtils.isEmpty(content)) {

                    try {

                        final YouBanFlashSalesHelpMessageModel youBanFlashSalesHelpMessageModel = JsonUtil.getObjectMapper().readValue(content, YouBanFlashSalesHelpMessageModel.class);
                        if (youBanFlashSalesHelpMessageModel != null) {

                            final ItemViewHolder holder = (ItemViewHolder) view.getTag();

                            if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                                holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
                            } else {
                                holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
                            }
                            if (youBanFlashSalesHelpMessageModel.getMessageType() == 8000) {

                                // 提示语
                                holder.contentView.setText(youBanFlashSalesHelpMessageModel.getMessageTips());
                                Utils.ImageViewDisplayByUrl(youBanFlashSalesHelpMessageModel.getImageUrl(), holder.salesImageView);
                                // 查看我帮好友抽到的商品
                                holder.lookView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(App.getInstance(), WXPayEntryActivity.class);
                                        intent.putExtra(Constants.KEY.KEY_URL, youBanFlashSalesHelpMessageModel.getLinkUrl());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        App.getInstance().startActivity(intent);


                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }

    }


    class ItemViewHolder {
        View itemLayout;
        TextView contentView;
        TextView lookView;
        ImageView salesImageView;


    }

}
