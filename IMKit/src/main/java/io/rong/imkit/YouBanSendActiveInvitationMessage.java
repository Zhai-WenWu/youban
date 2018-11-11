package io.rong.imkit;

import android.os.Parcel;

import io.rong.imlib.MessageTag;
import io.rong.message.TextMessage;

/**
 * 群分享推送消息
 * flag的值
 * MessageTag.NONE	为空值，不表示任何意义，发送的自定义消息不会在会话页面和会话列表中展示。
 * MessageTag.ISCOUNTED	表示客户端收到消息后，要进行未读消息计数（未读消息数增加 1），所有内容型消息都应该设置此值。非内容类消息暂不支持消息计数。
 * MessageTag.ISPERSISTED	表示客户端收到消息后，要进行存储，并在之后可以通过接口查询，存储后会在会话界面中显示。
 */
@MessageTag(value = "RC:YouBanSendActiveInvitation", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class YouBanSendActiveInvitationMessage extends TextMessage {

    public static final Creator<YouBanSendActiveInvitationMessage> CREATOR = new Creator<YouBanSendActiveInvitationMessage>() {
        public YouBanSendActiveInvitationMessage createFromParcel(Parcel source) {
            return new YouBanSendActiveInvitationMessage(source);
        }

        public YouBanSendActiveInvitationMessage[] newArray(int size) {
            return new YouBanSendActiveInvitationMessage[size];
        }
    };


    public YouBanSendActiveInvitationMessage() {
        super();
    }

    public YouBanSendActiveInvitationMessage(byte[] data) {
        super(data);
    }

    public YouBanSendActiveInvitationMessage(Parcel in) {
        super(in);
    }

    public YouBanSendActiveInvitationMessage(String content) {
        super(content);
    }
}
