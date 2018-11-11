package io.rong.imkit;

import android.os.Parcel;

import io.rong.imlib.MessageTag;
import io.rong.message.TextMessage;

/**
 * 匿名聊天室
 * flag的值
 * MessageTag.NONE	为空值，不表示任何意义，发送的自定义消息不会在会话页面和会话列表中展示。
 * MessageTag.ISCOUNTED	表示客户端收到消息后，要进行未读消息计数（未读消息数增加 1），所有内容型消息都应该设置此值。非内容类消息暂不支持消息计数。
 * MessageTag.ISPERSISTED	表示客户端收到消息后，要进行存储，并在之后可以通过接口查询，存储后会在会话界面中显示。
 */

@MessageTag(value = "RC:YouBanAnonymousMessage", flag = MessageTag.NONE)
public class YouBanAnonymousMessage extends TextMessage {

    public static final Creator<YouBanAnonymousMessage> CREATOR = new Creator<YouBanAnonymousMessage>() {
        @Override
        public YouBanAnonymousMessage createFromParcel(Parcel source) {
            return new YouBanAnonymousMessage(source);
        }

        @Override
        public YouBanAnonymousMessage[] newArray(int size) {
            return new YouBanAnonymousMessage[size];
        }
    };

    protected YouBanAnonymousMessage() {
        super();
    }

    public YouBanAnonymousMessage(byte[] data) {
        super(data);
    }

    public YouBanAnonymousMessage(Parcel in) {
        super(in);
    }

    public YouBanAnonymousMessage(String content) {
        super(content);
    }
}
