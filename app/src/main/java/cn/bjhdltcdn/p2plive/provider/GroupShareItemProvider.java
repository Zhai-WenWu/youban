package cn.bjhdltcdn.p2plive.provider;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.event.GroupShareItemClickEvent;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.YouBanGroupSharedMessageModel;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.YouBanGroupSharedMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * 群分享item
 * 自定义样式
 */

@ProviderTag(messageContent = YouBanGroupSharedMessage.class)
public class GroupShareItemProvider extends IContainerItemProvider.MessageProvider {

    @Override
    public void bindView(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

        Logger.d("view == " + view + "  uiMessage ==== " + uiMessage);

    }

    @Override
    public Spannable getContentSummary(MessageContent messageContent) {
        return new SpannableString("推荐名片");
    }

    @Override
    public void onItemClick(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

        Logger.d("view == " + view + "  uiMessage ==== " + uiMessage);

        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {
            if (messageContent instanceof YouBanGroupSharedMessage) {

                YouBanGroupSharedMessage youBanGroupSharedMessage = (YouBanGroupSharedMessage) messageContent;
                String content = youBanGroupSharedMessage.getContent();
                if (StringUtils.isEmpty(content)) {
                    content = youBanGroupSharedMessage.getExtra();
                }

                Logger.json(content);

                if (!StringUtils.isEmpty(content)) {
                    try {
                        YouBanGroupSharedMessageModel messageModel = JsonUtil.getObjectMapper().readValue(content, YouBanGroupSharedMessageModel.class);
                        Group group = new Group();
                        group.setGroupId(messageModel.getGroupId());
                        group.setNumber(messageModel.getNumber());
                        group.setGroupName(messageModel.getGroupName());
                        group.setGroupImg(messageModel.getGroupImg());
                        group.setGroupMode(messageModel.getGroupMode());
                        EventBus.getDefault().post(new GroupShareItemClickEvent(group));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View itemView = View.inflate(App.getInstance(), R.layout.group_item_share_layout, null);
        ItemViewHolder itemViewHolder = new ItemViewHolder();
        itemViewHolder.circleImageView = itemView.findViewById(R.id.circle_image_view);
        itemViewHolder.nickNameView = itemView.findViewById(R.id.nicke_text_view);
        itemViewHolder.countView = itemView.findViewById(R.id.count_view);
        itemViewHolder.sharedTipsView = itemView.findViewById(R.id.shared_tips_view);
        itemViewHolder.itemLayout = itemView.findViewById(R.id.item_layout);
        itemView.setTag(itemViewHolder);
        return itemView;

    }

    @Override
    public void bindView(View view, int i, Object object) {

        Logger.d("view == " + view + "  object ==== " + object);

        if (object instanceof UIMessage) {
            UIMessage uiMessage = (UIMessage) object;
            if (uiMessage.getContent() instanceof YouBanGroupSharedMessage) {
                YouBanGroupSharedMessage youBanGroupSharedMessage = (YouBanGroupSharedMessage) uiMessage.getContent();

                String extra = youBanGroupSharedMessage.getExtra();
                Logger.json(extra);
                if (!StringUtils.isEmpty(extra)) {

                    try {

                        YouBanGroupSharedMessageModel messageModel = JsonUtil.getObjectMapper().readValue(extra, YouBanGroupSharedMessageModel.class);

                        ItemViewHolder holder = (ItemViewHolder) view.getTag();

                        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                            holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
                        } else {
                            holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
                        }

                        if (messageModel != null) {
                            //  群封面
                            Glide.with(App.getInstance()).asBitmap().load(messageModel.getGroupImg()).apply(new RequestOptions().placeholder(R.mipmap.error_group_icon).centerCrop()).into(holder.circleImageView);

                            //  群昵称
                            holder.nickNameView.setText(messageModel.getGroupName());

                            // 群成员数量
                            holder.countView.setText(messageModel.getNumber() + "人");

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    static class ItemViewHolder {
        CircleImageView circleImageView;
        TextView nickNameView;
        TextView countView;
        TextView sharedTipsView;
        View itemLayout;
    }

}
