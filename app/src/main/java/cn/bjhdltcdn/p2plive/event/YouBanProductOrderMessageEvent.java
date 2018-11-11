package cn.bjhdltcdn.p2plive.event;

/**
 * Created by xiawenquan on 18/4/20.
 */

public class YouBanProductOrderMessageEvent {

    //订单Id
    private long orderId;

    private int messageId;

    // 消息类型
    private int messageType;

    public YouBanProductOrderMessageEvent(long orderId, int messageId, int messageType) {
        this.orderId = orderId;
        this.messageId = messageId;
        this.messageType = messageType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
