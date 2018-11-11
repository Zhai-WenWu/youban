package cn.bjhdltcdn.p2plive.provider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.YouBanProductOrderMessageEvent;
import cn.bjhdltcdn.p2plive.model.AddressInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ParseProductOrderMessage;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.ui.activity.OrderDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PublishActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.GoodsreceiptDialog;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.YouBanProductOrder;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * 商品试用的item
 * 自定义样式
 */


@ProviderTag(messageContent = YouBanProductOrder.class)
public class YouBanProductOrderMessageProvider extends IContainerItemProvider.MessageProvider {
    private AppCompatActivity context;

    @Override
    public void bindView(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

    }

    @Override
    public Spannable getContentSummary(MessageContent messageContent) {

        YouBanProductOrder youBanProductOrder = (YouBanProductOrder) messageContent;

        // 内容
        String content = youBanProductOrder.getContent();

        Spannable spannable = null;

        if (!StringUtils.isEmpty(content)) {

            try {
                final ParseProductOrderMessage parseProductOrderMessage = JsonUtil.getObjectMapper().readValue(content, ParseProductOrderMessage.class);
                if (parseProductOrderMessage != null) {
                    if (parseProductOrderMessage.getMessageType() == 7000) {
                        spannable = new SpannableString("【订单信息】");
                    } else if (parseProductOrderMessage.getMessageType() == 7001) {
                        spannable = new SpannableString("【确认收货信息】");
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

        View itemView = View.inflate(App.getInstance(), R.layout.product_order_message_item_layout, null);

        ItemViewHolder viewHolder = new ItemViewHolder();

        viewHolder.itemLayout = itemView.findViewById(R.id.item_layout);


        ViewStub viewStub1 = itemView.findViewById(R.id.public_item_layout);
        if (viewStub1 != null) {
            viewStub1.inflate();
        }

        viewHolder.relativeLayout1 = itemView.findViewById(R.id.layout_view_1);
        if (viewHolder.relativeLayout1 != null) {
            // 提示语控件
            viewHolder.tipTextView1 = itemView.findViewById(R.id.tip_text_view_1);
            // 确认收货按钮控件
            viewHolder.btnView3 = itemView.findViewById(R.id.btn_view_3);

        }


        ViewStub viewStub2 = itemView.findViewById(R.id.apply_item_layout);
        if (viewStub2 != null) {
            viewStub2.inflate();
        }

        viewHolder.relativeLayout2 = itemView.findViewById(R.id.layout_view_2);
        if (viewHolder.relativeLayout2 != null) {
            // 提示语控件
            viewHolder.tipTextView2 = itemView.findViewById(R.id.tip_text_view_2);
            // 商品图片控件
            viewHolder.imageView = itemView.findViewById(R.id.image_view);
            // 商品名称控件
            viewHolder.productNameView = itemView.findViewById(R.id.product_name_view);
            // 商品数量控件
            viewHolder.productNumberView = itemView.findViewById(R.id.product_number_view);
            // 商品原价
            viewHolder.productPriceView1 = itemView.findViewById(R.id.product_price_view_1);
            // 折扣价
            viewHolder.productPriceView2 = itemView.findViewById(R.id.product_price_view_2);
            // 提示语2控件
            viewHolder.tipTextView3 = itemView.findViewById(R.id.tip_text_view_3);
            // 收货人
            viewHolder.acceptedTextView = itemView.findViewById(R.id.accepted_text_view);
            // 收货地址
            viewHolder.addressTextView = itemView.findViewById(R.id.address_text_view);
            // 取货码
            viewHolder.codeTextView = itemView.findViewById(R.id.code_text_view);
            // 提示语3控件
            viewHolder.tipTextView4 = itemView.findViewById(R.id.tip_text_view_4);
            // 确认收货按钮控件
            viewHolder.btnView1 = itemView.findViewById(R.id.btn_view_1);
            // 查看订单详情
            viewHolder.btnView2 = itemView.findViewById(R.id.btn_view_2);

        }

        itemView.setTag(viewHolder);


        return itemView;
    }

    @Override
    public void bindView(View view, int i, Object object) {

        if (object instanceof UIMessage) {
            final UIMessage uiMessage = (UIMessage) object;
            if (uiMessage.getContent() instanceof YouBanProductOrder) {
                YouBanProductOrder youBanProductOrder = (YouBanProductOrder) uiMessage.getContent();

                String content = youBanProductOrder.getContent();
                Logger.json(content);

                if (!StringUtils.isEmpty(content)) {

                    try {

                        final ParseProductOrderMessage parseProductOrderMessage = JsonUtil.getObjectMapper().readValue(content, ParseProductOrderMessage.class);
                        if (parseProductOrderMessage != null) {

                            final ItemViewHolder holder = (ItemViewHolder) view.getTag();

                            if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                                holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
                            } else {
                                holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
                            }

                            if (parseProductOrderMessage.getMessageType() == 7000) {// 商品试用消息

                                holder.relativeLayout1.setVisibility(View.GONE);
                                holder.relativeLayout2.setVisibility(View.VISIBLE);

                                // 订单信息
                                final ProductOrder productOrder = parseProductOrderMessage.getProductOrder();
                                if (productOrder != null) {
                                    final ProductInfo productInfo = productOrder.getProductInfo();
                                    if (productInfo != null) {// 商品信息

                                        //商品类型(1试用商品,2店铺商品)
                                        if (productInfo.getProductType() == 1) {
                                            holder.tipTextView2.setVisibility(View.VISIBLE);
                                            holder.tipTextView3.setVisibility(View.VISIBLE);
                                        } else {
                                            holder.tipTextView2.setVisibility(View.GONE);
                                            holder.tipTextView3.setVisibility(View.GONE);
                                        }

                                        // 商品图片
                                        Utils.CornerImageViewDisplayByUrl(productInfo.getProductImg(), holder.imageView,9);

                                        // 商品名称
                                        holder.productNameView.setText(productInfo.getProductName());

                                        // 商品数量
                                        holder.productNumberView.setText("限量" + productInfo.getProductTotal() + "件");

                                        //商品原价
                                        holder.productPriceView1.setText(productInfo.getProductPrice() + "");
                                        if (holder.productPriceView1.getPaint() != null) {
                                            holder.productPriceView1.getPaint().setAntiAlias(true);//抗锯齿
                                            holder.productPriceView1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                                        }

                                        //商品折扣价
                                        holder.productPriceView2.setText("￥" + productInfo.getSalePrice());

                                        // 商品收件人
                                        StringBuffer buffer = new StringBuffer();
                                        AddressInfo addressInfo = productOrder.getAddressInfo();
                                        if (addressInfo != null) {// 手机号
                                            buffer.append("收件人：");
                                            buffer.append(addressInfo.getContactName());
                                            buffer.append("    ");
                                            buffer.append(addressInfo.getPhoneNumber());
                                        }

                                        holder.acceptedTextView.setText(buffer.toString());

                                        // 收货地址
                                        if (addressInfo != null) {
                                            holder.addressTextView.setText("收货地址：" + addressInfo.getAddress());
                                        }

                                        // 收货码
                                        if (!StringUtils.isEmpty(productOrder.getClaimCode())) {
                                            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
                                            String temp = "取货码为:";
                                            stringBuilder.append(temp);
                                            stringBuilder.append(productOrder.getClaimCode());

                                            stringBuilder.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), 0, temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            stringBuilder.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_507daf)), temp.length(), stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                            holder.codeTextView.setText(stringBuilder);
                                        }

                                        // 3-5个工作日内 提示语
                                        holder.tipTextView4.setText(parseProductOrderMessage.getMessageTips());


                                        // 附近信息 : extra == null 则表示还可以确认收货；否则是已收货
                                        final Message message = uiMessage.getMessage();
                                        if (message != null) {
                                            String extra = message.getExtra();
                                            if (!StringUtils.isEmpty(extra) && "1".equals(extra)) {
                                                holder.btnView1.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
                                                holder.btnView1.setOnClickListener(null);
                                            } else {
                                                holder.btnView1.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);

                                                // 确认收货
                                                holder.btnView1.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        GoodsreceiptDialog dialog = new GoodsreceiptDialog();
                                                        dialog.setOrderId(productOrder.orderId);

                                                        dialog.setItemClick(new GoodsreceiptDialog.ItemClick() {
                                                            @Override
                                                            public void itemClick() {

                                                                RongIMClient.getInstance().setMessageExtra(uiMessage.getMessageId(), "1", new RongIMClient.ResultCallback<Boolean>() {
                                                                    @Override
                                                                    public void onSuccess(Boolean aBoolean) {
                                                                        Logger.d("aBoolean ==== " + aBoolean);
                                                                        message.setExtra("1");
                                                                    }

                                                                    @Override
                                                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                                                        Logger.d("errorCode ==== " + errorCode);
                                                                    }
                                                                });

                                                            }
                                                        });
                                                        dialog.show(context.getSupportFragmentManager());

//                                                        EventBus.getDefault().post(new YouBanProductOrderMessageEvent(productOrder.orderId, uiMessage.getMessageId(), parseProductOrderMessage.getMessageType()));

                                                    }
                                                });
                                            }
                                        }

                                        // 查看订单详情
                                        holder.btnView2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(App.getInstance(), OrderDetailActivity.class);
                                                intent.putExtra(Constants.Fields.ORDER_ID, productOrder.getOrderId());
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                App.getInstance().startActivity(intent);
                                            }
                                        });

                                    }
                                }

                            } else if (parseProductOrderMessage.getMessageType() == 7001) {

                                holder.relativeLayout1.setVisibility(View.VISIBLE);
                                holder.relativeLayout2.setVisibility(View.GONE);

                                // 提示语
                                holder.tipTextView1.setText(parseProductOrderMessage.getMessageTips());

                                // 发布点击按钮
                                holder.btnView3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(App.getInstance(), PublishActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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


    static class ItemViewHolder {

        View itemLayout;
        View relativeLayout1;
        View relativeLayout2;

        TextView tipTextView1;
        TextView tipTextView2;

        ImageView imageView;
        TextView productNameView;
        TextView productNumberView;
        TextView productPriceView1;
        TextView productPriceView2;
        TextView tipTextView3;
        TextView acceptedTextView;
        TextView addressTextView;
        TextView codeTextView;
        TextView tipTextView4;

        Button btnView1;
        Button btnView2;
        Button btnView3;


    }

}
