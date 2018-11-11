package cn.bjhdltcdn.p2plive.provider;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ParseShopPlaceOrderMessage;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import io.rong.imkit.YouBanShopEvaluateOrderMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * Created by zhaiww on 2018/7/25.
 */
@ProviderTag(messageContent = YouBanShopEvaluateOrderMessage.class)
public class YouBanShopEvaluateOrderMessageProvider extends IContainerItemProvider.MessageProvider {

    private AppCompatActivity mContext;

    @Override
    public void bindView(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

    }

    @Override
    public Spannable getContentSummary(MessageContent messageContent) {

        YouBanShopEvaluateOrderMessage YouBanShopEvaluateOrderMessage = (YouBanShopEvaluateOrderMessage) messageContent;

        // 内容
        String content = YouBanShopEvaluateOrderMessage.getContent();

        Spannable spannable = null;

        if (!StringUtils.isEmpty(content)) {
            try {
                final ParseShopPlaceOrderMessage parseShopPlaceOrderMessage = JsonUtil.getObjectMapper().readValue(content, ParseShopPlaceOrderMessage.class);
                if (parseShopPlaceOrderMessage != null) {
                    String tipText = "";
                    switch (parseShopPlaceOrderMessage.getMessageType()) {

                        //卖家
                        case 1022:
                            ProductOrder productOrder = parseShopPlaceOrderMessage.getProductOrder();
                            if (productOrder != null) {
                                BaseUser baseUser = productOrder.getBaseUser();
                                if (baseUser != null) {
                                    tipText = "订单评论";
                                }

                            }
                            break;

                    }

                    spannable = new SpannableString(tipText);

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
        this.mContext = (AppCompatActivity) context;

        View itemView = View.inflate(App.getInstance(), R.layout.product_order_message_public_item_layout, null);

        YouBanShopEvaluateOrderMessageProvider.ItemViewHolder viewHolder = new YouBanShopEvaluateOrderMessageProvider.ItemViewHolder();

        viewHolder.itemLayout = itemView.findViewById(R.id.layout_view_1);
        viewHolder.tipTextView = itemView.findViewById(R.id.tip_text_view_1);
        viewHolder.btnView3 = itemView.findViewById(R.id.btn_view_3);

        itemView.setTag(viewHolder);

        return itemView;
    }

    @Override
    public void bindView(View view, int i, Object object) {

        if (object instanceof UIMessage) {
            final UIMessage uiMessage = (UIMessage) object;
            if (uiMessage.getContent() instanceof YouBanShopEvaluateOrderMessage) {
                YouBanShopEvaluateOrderMessage YouBanShopEvaluateOrderMessage = (YouBanShopEvaluateOrderMessage) uiMessage.getContent();

                final String content = YouBanShopEvaluateOrderMessage.getContent();
                Logger.json(content);

                if (!StringUtils.isEmpty(content)) {
                    try {
                        final ParseShopPlaceOrderMessage parseShopPlaceOrderMessage = JsonUtil.getObjectMapper().readValue(content, ParseShopPlaceOrderMessage.class);
                        if (parseShopPlaceOrderMessage != null) {


                            YouBanShopEvaluateOrderMessageProvider.ItemViewHolder holder = (YouBanShopEvaluateOrderMessageProvider.ItemViewHolder) view.getTag();
                            if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                                holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
                            } else {
                                holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
                            }

                            // 提示语
                            holder.tipTextView.setText(Html.fromHtml(parseShopPlaceOrderMessage.getMessageTips()));

                            // 按钮
                            holder.btnView3.setText("查看详情");

                            holder.btnView3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(mContext,ShopDetailActivity.class);
                                    intent.putExtra(Constants.Fields.STORE_ID,parseShopPlaceOrderMessage.getProductOrder().getStoreId());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(intent);
                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public class ItemViewHolder {
        View itemLayout;
        TextView tipTextView;
        Button btnView3;
    }
}
