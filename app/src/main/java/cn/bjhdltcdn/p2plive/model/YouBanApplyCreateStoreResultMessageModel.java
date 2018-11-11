package cn.bjhdltcdn.p2plive.model;

/**
 * Created by xiawenquan on 17/12/4.
 */

public class YouBanApplyCreateStoreResultMessageModel {

    private long storeId;
    private String messageTips;
    private int messageType;

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
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
