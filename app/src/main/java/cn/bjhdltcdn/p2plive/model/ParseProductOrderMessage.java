package cn.bjhdltcdn.p2plive.model;

/**
 * 商品消息解析实体
 */

public class ParseProductOrderMessage {

    /**
     * 订单对象
     */
    private ProductOrder productOrder;

    /**
     * 0 卖家未签收 3 卖家已签收，
     */
    private int sellerSign;

    /**
     * 0 买家未签收 1 买家已签收
     */
    private int buyerSign;

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

    public int getSellerSign() {
        return sellerSign;
    }

    public void setSellerSign(int sellerSign) {
        this.sellerSign = sellerSign;
    }

    public int getBuyerSign() {
        return buyerSign;
    }

    public void setBuyerSign(int buyerSign) {
        this.buyerSign = buyerSign;
    }

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
}
