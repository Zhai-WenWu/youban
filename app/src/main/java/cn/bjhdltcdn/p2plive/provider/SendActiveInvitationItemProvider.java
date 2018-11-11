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

import java.io.IOException;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.YouBanSendActiveInvitationMessageModel;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.YouBanSendActiveInvitationMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * 活动邀请函item
 * 自定义样式
 */

@ProviderTag(messageContent = YouBanSendActiveInvitationMessage.class)
public class SendActiveInvitationItemProvider extends IContainerItemProvider.MessageProvider {

    @Override
    public void bindView(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

        Logger.d("view == " + view + "  uiMessage ==== " + uiMessage);

    }

    @Override
    public Spannable getContentSummary(MessageContent messageContent) {
        return new SpannableString("活动邀请函");
    }

    @Override
    public void onItemClick(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

        Logger.d("view == " + view + "  uiMessage ==== " + uiMessage);

//        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {
            if (messageContent instanceof YouBanSendActiveInvitationMessage) {

                YouBanSendActiveInvitationMessage youBanSendActiveInvitationMessage = (YouBanSendActiveInvitationMessage) messageContent;
                String content = youBanSendActiveInvitationMessage.getContent();
                if (StringUtils.isEmpty(content)) {
                    content = youBanSendActiveInvitationMessage.getExtra();
                }

                Logger.json(content);

                if (!StringUtils.isEmpty(content)) {
                    try {
                        YouBanSendActiveInvitationMessageModel messageModel = JsonUtil.getObjectMapper().readValue(content, YouBanSendActiveInvitationMessageModel.class);
                        ActivityInfo activityInfo = new ActivityInfo();
                        activityInfo.setActivityId(messageModel.getActivityId());
                        //跳转到活动详情页
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
//        }
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View itemView = View.inflate(App.getInstance(), R.layout.send_invitation_message_item_layout, null);
        ItemViewHolder itemViewHolder = new ItemViewHolder();
        itemViewHolder.circleImageView = itemView.findViewById(R.id.circle_image_view);
        itemViewHolder.nickNameView = itemView.findViewById(R.id.nicke_text_view);
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
            if (uiMessage.getContent() instanceof YouBanSendActiveInvitationMessage) {
                YouBanSendActiveInvitationMessage youBanSendActiveInvitationMessage = (YouBanSendActiveInvitationMessage) uiMessage.getContent();

                String extra = youBanSendActiveInvitationMessage.getExtra();
                Logger.json(extra);

                if (!StringUtils.isEmpty(extra)) {

                    try {

                        YouBanSendActiveInvitationMessageModel messageModel = JsonUtil.getObjectMapper().readValue(extra, YouBanSendActiveInvitationMessageModel.class);

                        ItemViewHolder holder = (ItemViewHolder) view.getTag();

                        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                            holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
                        } else {
                            holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
                        }

                        if (messageModel != null) {
                            BaseUser baseUser=messageModel.getBaseUser();
                            if(baseUser!=null){
                                //  用户头像
                                Glide.with(App.getInstance()).asBitmap().load(baseUser.getUserIcon()).apply(new RequestOptions().placeholder(R.mipmap.error_user_icon).centerCrop()).into(holder.circleImageView);

                                //  用户昵称
                                holder.nickNameView.setText(baseUser.getNickName());
                            }

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
