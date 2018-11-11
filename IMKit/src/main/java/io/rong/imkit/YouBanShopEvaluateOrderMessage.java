package io.rong.imkit;

import android.os.Parcel;

import io.rong.imlib.MessageTag;
import io.rong.message.TextMessage;

/**
 * Created by zhaiww on 2018/7/25.
 * 订单评价
 */

@MessageTag(value = "RC:YouBanShopEvaluateOrderMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class YouBanShopEvaluateOrderMessage extends TextMessage {
    public static final Creator<YouBanShopEvaluateOrderMessage> CREATOR = new Creator<YouBanShopEvaluateOrderMessage>() {
        public YouBanShopEvaluateOrderMessage createFromParcel(Parcel source) {
            return new YouBanShopEvaluateOrderMessage(source);
        }

        public YouBanShopEvaluateOrderMessage[] newArray(int size) {
            return new YouBanShopEvaluateOrderMessage[size];
        }
    };

    public YouBanShopEvaluateOrderMessage() {
        super();
    }

    public YouBanShopEvaluateOrderMessage(byte[] data) {
        super(data);
    }

    public YouBanShopEvaluateOrderMessage(Parcel in) {
        super(in);
    }

    public YouBanShopEvaluateOrderMessage(String content) {
        super(content);
    }
}
