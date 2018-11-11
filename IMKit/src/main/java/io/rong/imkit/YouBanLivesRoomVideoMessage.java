package io.rong.imkit;

import android.os.Parcel;

import io.rong.imlib.MessageTag;
import io.rong.message.TextMessage;

/**
 * 多人视频推送消息（不包括群分享）
 * flag的值
 * MessageTag.NONE	为空值，不表示任何意义，发送的自定义消息不会在会话页面和会话列表中展示。
 * MessageTag.ISCOUNTED	表示客户端收到消息后，要进行未读消息计数（未读消息数增加 1），所有内容型消息都应该设置此值。非内容类消息暂不支持消息计数。
 * MessageTag.ISPERSISTED	表示客户端收到消息后，要进行存储，并在之后可以通过接口查询，存储后会在会话界面中显示。
 */

@MessageTag(value = "RC:YouBanLivesRoomVideo", flag = MessageTag.NONE)
public class YouBanLivesRoomVideoMessage extends TextMessage {

    public static final Creator<YouBanLivesRoomVideoMessage> CREATOR = new Creator<YouBanLivesRoomVideoMessage>() {
        public YouBanLivesRoomVideoMessage createFromParcel(Parcel source) {
            return new YouBanLivesRoomVideoMessage(source);
        }

        public YouBanLivesRoomVideoMessage[] newArray(int size) {
            return new YouBanLivesRoomVideoMessage[size];
        }
    };

    public YouBanLivesRoomVideoMessage() {
        super();
    }

    public YouBanLivesRoomVideoMessage(byte[] data) {
        super(data);
    }

    public YouBanLivesRoomVideoMessage(Parcel in) {
        super(in);
    }

    public YouBanLivesRoomVideoMessage(String content) {
        super(content);
    }
}
