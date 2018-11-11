package io.rong.imkit;

import android.os.Parcel;

import io.rong.imlib.MessageTag;
import io.rong.message.TextMessage;


/**
 * 闪购活动推送消息
 * flag的值
 * MessageTag.NONE	为空值，不表示任何意义，发送的自定义消息不会在会话页面和会话列表中展示。
 * MessageTag.ISCOUNTED	表示客户端收到消息后，要进行未读消息计数（未读消息数增加 1），所有内容型消息都应该设置此值。非内容类消息暂不支持消息计数。
 * MessageTag.ISPERSISTED	表示客户端收到消息后，要进行存储，并在之后可以通过接口查询，存储后会在会话界面中显示。
 */

@MessageTag(value = "RC:YouBanFlashSalesHelpMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class YouBanFlashSalesHelpMessage extends TextMessage {

    public static final Creator<YouBanFlashSalesHelpMessage> CREATOR = new Creator<YouBanFlashSalesHelpMessage>() {
        @Override
        public YouBanFlashSalesHelpMessage createFromParcel(Parcel source) {
            return new YouBanFlashSalesHelpMessage(source);
        }

        @Override
        public YouBanFlashSalesHelpMessage[] newArray(int size) {
            return new YouBanFlashSalesHelpMessage[size];
        }
    };


    protected YouBanFlashSalesHelpMessage() {
        super();
    }

    public YouBanFlashSalesHelpMessage(byte[] data) {
        super(data);
    }

    public YouBanFlashSalesHelpMessage(Parcel in) {
        super(in);
    }

    public YouBanFlashSalesHelpMessage(String content) {
        super(content);
    }
}
