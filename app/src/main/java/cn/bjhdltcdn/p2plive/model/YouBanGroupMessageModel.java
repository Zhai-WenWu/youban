package cn.bjhdltcdn.p2plive.model;

/**
 * Created by xiawenquan on 17/12/6.
 */

public class YouBanGroupMessageModel {
    private Long messageId;
    private GroupApply groupApply;
    private String messageTips;
    private int messageType;
    private String addTime;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public GroupApply getGroupApply() {
        return groupApply;
    }

    public void setGroupApply(GroupApply groupApply) {
        this.groupApply = groupApply;
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

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
