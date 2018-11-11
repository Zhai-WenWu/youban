package cn.bjhdltcdn.p2plive.provider;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Map;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.FindRoomDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateUserStatusResponse;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PkPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.GroupVideoActivity;
import cn.bjhdltcdn.p2plive.ui.activity.OpenRoomSettingPassWordActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PostDetailActivity;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.YouBanSharedMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * 圈子，帖子，PK，聊天室，表白，活动 分享item
 * 自定义样式
 */

@ProviderTag(messageContent = YouBanSharedMessage.class)
public class SharedMessageProvider extends IContainerItemProvider.MessageProvider implements BaseView {

    private ChatRoomPresenter chatRoomPresenter;

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    private PkPresenter pkPresenter;

    public PkPresenter getPkPresenter() {
        if (pkPresenter == null) {
            pkPresenter = new PkPresenter(this);
        }
        return pkPresenter;
    }

    @Override
    public void bindView(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

    }

    @Override
    public Spannable getContentSummary(MessageContent messageContent) {
        YouBanSharedMessage youBanSharedMessage = (YouBanSharedMessage) messageContent;
        String content = youBanSharedMessage.getContent();
        if (StringUtils.isEmpty(content)) {
            content = youBanSharedMessage.getExtra();
        }

        Spannable spannable = null;

        if (!StringUtils.isEmpty(content)) {
            try {
                Map map = JsonUtil.getObjectMapper().readValue(content, Map.class);
                if (map != null && !map.isEmpty()) {


                    Object objectMap = map.get(Constants.Fields.MESSAGE_TYPE);

                    int messageType = 0;
                    if (objectMap instanceof Integer) {
                        messageType = (int) objectMap;
                    } else if (objectMap instanceof Long) {
                        messageType = (int) objectMap;
                    }


                    String title = null;
                    switch (messageType) {
                        case 11:
                            title = "分享一个帖子";
                            break;
                        case 12:
                            title = "分享一个圈子";
                            break;

                        case 13:
                            title = "分享一个活动";
                            break;

                        case 14:
                            title = "分享一个聊天频道";

                            break;

                        case 18:
                            title = "分享一个表白";
                            break;

                        default:
                            title = "";
                            break;

                    }
                    spannable = new SpannableString(title);

                }

                if (spannable == null) {
                    spannable = new SpannableString("分享");
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
        View itemView = View.inflate(App.getInstance(), R.layout.shared_message_item_layout, null);

        ItemViewHolder itemViewHolder = new ItemViewHolder();

        itemViewHolder.itemLayout = itemView.findViewById(R.id.item_layout);
        itemViewHolder.tagView = itemView.findViewById(R.id.tag_view);
        itemViewHolder.circleImageView = itemView.findViewById(R.id.circle_image_view);
        itemViewHolder.nickNameView = itemView.findViewById(R.id.nicke_text_view);

        itemViewHolder.imgViewGg = itemView.findViewById(R.id.img_bg);
        itemViewHolder.videoPlayImgView = itemView.findViewById(R.id.video_play_img);
        itemViewHolder.sharedTipsView = itemView.findViewById(R.id.shared_tips_view);
        itemViewHolder.imgLayout = itemView.findViewById(R.id.image_layout);


        itemView.setTag(itemViewHolder);

        return itemView;
    }

    @Override
    public void bindView(View view, int i, Object object) {

        Logger.d("view == " + view + "  object ==== " + object);

        if (object instanceof UIMessage) {
            UIMessage uiMessage = (UIMessage) object;
            if (uiMessage.getContent() instanceof YouBanSharedMessage) {
                YouBanSharedMessage youBanSharedMessage = (YouBanSharedMessage) uiMessage.getContent();

                String content = youBanSharedMessage.getContent();
                if (StringUtils.isEmpty(content)) {
                    content = youBanSharedMessage.getExtra();
                }

                Logger.json(content);

                if (!StringUtils.isEmpty(content)) {

                    ItemViewHolder holder = (ItemViewHolder) view.getTag();

                    if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                        holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
                    } else {
                        holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
                    }

                    try {

                        holder.itemLayout.setOnClickListener(null);

                        if (!StringUtils.isEmpty(content)) {
                            int messageType = -1;
                            String messageTypeText = JsonUtil.getJsonValue(content, Constants.Fields.MESSAGE_TYPE);
                            if (messageTypeText instanceof String) {
                                messageType = Integer.valueOf(messageTypeText);
                            }
                            // 头像
                            String userIcon = null;
                            // 昵称
                            String nickName = null;
                            // 图片
                            String imgUrl = null;

                            // 内容
                            String contentText = null;

                            String tag = null;

                            holder.videoPlayImgView.setVisibility(View.GONE);


                            switch (messageType) {
                                case 11:
                                    tag = "帖子";
                                    final PostInfo postInfo = JsonUtil.getJsonValueObject(content, Constants.Fields.SHARED_OBJECT, PostInfo.class);
                                    Logger.d("postInfo === " + postInfo);

                                    if (postInfo == null) {
                                        return;
                                    }

                                    userIcon = postInfo.getBaseUser().getUserIcon();
                                    nickName = postInfo.getBaseUser().getNickName();

                                    // 普通文本帖子
                                    if (postInfo.getTopicType() == 1) {
                                        if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
                                            imgUrl = postInfo.getImageList().get(0).getImageUrl();
                                        }

                                    } else {
                                        holder.videoPlayImgView.setVisibility(View.VISIBLE);
                                        imgUrl = postInfo.getVideoImageUrl();
                                    }

                                    contentText = postInfo.getContent();

                                    holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(App.getInstance().getCurrentActivity(), PostDetailActivity.class);
                                            intent.putExtra(Constants.KEY.KEY_OBJECT, postInfo);
                                            intent.putExtra(Constants.Fields.COME_IN_TYPE, 5);
                                            App.getInstance().getCurrentActivity().startActivity(intent);
                                        }
                                    });

                                    break;
                                case 12:
                                    tag = "圈子";


                                    break;

                                case 13:
                                    tag = "活动";

                                    break;

                                case 14:
                                    tag = "聊天频道";
                                    final RoomInfo roomInfo = JsonUtil.getJsonValueObject(content, Constants.Fields.SHARED_OBJECT, RoomInfo.class);
                                    if (roomInfo == null) {
                                        return;
                                    }

                                    userIcon = roomInfo.getBaseUser().getUserIcon();
                                    nickName = roomInfo.getBaseUser().getNickName();
                                    imgUrl = roomInfo.getBackgroundUrl();

                                    contentText = roomInfo.getRoomName();

                                    holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (roomInfo.getUserRole() == 1 || roomInfo.getPasswordType() == 0) {//不加密
                                                getChatRoomPresenter().updateUserStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomInfo.getRoomId(), 0, roomInfo.getPasswordType(), roomInfo.getPassword());
                                            } else if (roomInfo.getPasswordType() == 1) {//加密
                                                getChatRoomPresenter().findRoomDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomInfo.getRoomId());
                                            }

                                        }
                                    });


                                    break;

                                case 18:
                                    tag = "表白";

                                    break;

                            }

                            // 标记
                            holder.tagView.setText(tag);

                            // 头像
                            Glide.with(App.getInstance()).load(userIcon).apply(new RequestOptions().placeholder(R.mipmap.error_user_icon).centerCrop()).into(holder.circleImageView);

                            // 昵称
                            holder.nickNameView.setText(nickName);

                            // 图片
                            holder.imgLayout.setVisibility(View.GONE);
                            if (!StringUtils.isEmpty(imgUrl)) {
                                Glide.with(App.getInstance()).asBitmap().load(imgUrl).apply(new RequestOptions().placeholder(R.mipmap.error_bg).centerCrop()).into(holder.imgViewGg);
                                holder.imgLayout.setVisibility(View.VISIBLE);
                            }

                            // 内容
                            holder.sharedTipsView.setText(contentText);


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    @Override
    public void updateView(String apiName, Object object) {

        if (App.getInstance().getCurrentActivity().isFinishing()) {
            return;
        }

        if (InterfaceUrl.URL_UPDATEUSERSTATUS.equals(apiName)) {//进入房间
            if (object instanceof UpdateUserStatusResponse) {
                UpdateUserStatusResponse response = (UpdateUserStatusResponse) object;
                if (response.getCode() == 200) {
                    Intent intent = new Intent(App.getInstance().getCurrentActivity(), GroupVideoActivity.class);
                    intent.putExtra(Constants.Fields.ROOMINFO, response.getRoomInfo());
                    App.getInstance().getCurrentActivity().startActivity(intent);
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_FINDROOMDETAIL)) {
            if (object instanceof FindRoomDetailResponse) {
                FindRoomDetailResponse findRoomDetailResponse = (FindRoomDetailResponse) object;
                if (findRoomDetailResponse.getCode() == 200) {
                    RoomInfo roomInfo = findRoomDetailResponse.getRoomInfo();
                    if (roomInfo.getStatus() == 0) {
                        Intent intent = new Intent(App.getInstance().getCurrentActivity(), OpenRoomSettingPassWordActivity.class);
                        intent.putExtra(Constants.Fields.TYPE, 2);
                        intent.putExtra(Constants.Fields.ROOMINFO, roomInfo);
                        App.getInstance().getCurrentActivity().startActivity(intent);
                    } else {
                        Utils.showToastShortTime("该聊天频道已关闭");
                    }
                }
            }
        }


    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(App.getInstance().getCurrentActivity());
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    static class ItemViewHolder {

        View itemLayout;
        TextView tagView;
        CircleImageView circleImageView;
        TextView nickNameView;
        ImageView imgViewGg;
        ImageView videoPlayImgView;

        TextView sharedTipsView;
        View imgLayout;


    }


}
