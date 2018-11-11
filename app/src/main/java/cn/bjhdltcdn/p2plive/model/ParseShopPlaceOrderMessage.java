package cn.bjhdltcdn.p2plive.model;

/**
 * 商品消息解析实体
 */

public class ParseShopPlaceOrderMessage {

    /**
     * 订单对象
     */
    private ProductOrder productOrder;

    //消息的推送账号
    private long userId;

    /**
     * 消息提示
     */
    private String messageTips;

    /**
     * 消息类型
     */
    private int messageType;

    //订单Id
    private long orderId;

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }

    public String getMessageTips() {
        return messageTips;
    }

    public void setMessageTips(String messageTips) {
        this.messageTips = messageTips;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
