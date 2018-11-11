package cn.bjhdltcdn.p2plive.model;

/**
 * 商品消息解析实体
 */

public class ApplyClerkMessage {

    /**
     * 申请店员对象
     */

    public ApplyClert applyClert;

    public ApplyClert getApplyClert() {
        return applyClert;
    }

    public void setApplyClert(ApplyClert applyClert) {
        this.applyClert = applyClert;
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

    /**
     * 消息提示
     */
    private String messageTips;

    /**
     * 消息类型
     */
    private int messageType;


}
