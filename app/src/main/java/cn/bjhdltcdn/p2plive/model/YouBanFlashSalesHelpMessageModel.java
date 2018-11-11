package cn.bjhdltcdn.p2plive.model;

/**
 * 闪购活动消息解析实体
 */

public class YouBanFlashSalesHelpMessageModel {

    /**
     * 跳转链接,
     */
    private String linkUrl;
    /**
     * banner的缩略图路径
     */
    private String imageUrl;
    /**
     * 消息提示
     */
    private String messageTips;

    /**
     * 消息类型
     */
    private int messageType;

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
