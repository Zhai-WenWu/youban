package cn.bjhdltcdn.p2plive.provider;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.bjhdltcdn.p2plive.R;
import io.rong.imkit.GiftMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by Hu_PC on 2017/8/24.
 */
@ProviderTag(messageContent = GiftMessage.class)
public class GiftMessageItemProvider extends IContainerItemProvider.MessageProvider<GiftMessage> {
    private Context context;

    @Override
    public View newView(Context context, ViewGroup group) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.private_chat_gift_item_text_message, null);
        ViewHolder holder = new ViewHolder();
        holder.img = (ImageView) view.findViewById(R.id.gift_img);
        holder.name = (TextView) view.findViewById(R.id.tv_gift_name);
        holder.gold = (TextView) view.findViewById(R.id.tv_gift_gold);
        view.setTag(holder);


        return view;
    }


    @Override
    public void bindView(View view, int i, GiftMessage giftMessage, UIMessage uiMessage) {
        GiftMessageItemProvider.ViewHolder holder = (GiftMessageItemProvider.ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            view.setBackgroundResource(R.drawable.private_message_gift_bg_right);
        } else {
            view.setBackgroundResource(R.drawable.private_message_gift_bg_left);
        }
        if (giftMessage != null) {

            Glide.with(context).asBitmap().load(giftMessage.getGiftImgUrl()).apply(new RequestOptions().placeholder(R.drawable.my_gift)).into(holder.img);
            if(giftMessage!=null){
                String giftName=giftMessage.getGiftNmae();
                if(giftName.equals("友伴豆红包")){
                    giftName="友伴豆红包";
                }
                holder.name.setText("送出一个【" + giftName + "】");
                if (giftMessage.getGiftGold() > 0) {
                    holder.gold.setText("价值" + giftMessage.getGiftGold() + "金币");
                } else {
                    holder.gold.setText("价值" + giftMessage.getGiftNum() + "个友伴豆");
                }
            }

        }

    }

    @Override
    public Spannable getContentSummary(GiftMessage giftMessage) {
        return new SpannableString("[礼物]");
    }

    @Override
    public void onItemClick(View view, int i, GiftMessage giftMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, GiftMessage giftMessage, UIMessage uiMessage) {

    }

    class ViewHolder {
        ImageView img;
        TextView name, gold;
    }


}
