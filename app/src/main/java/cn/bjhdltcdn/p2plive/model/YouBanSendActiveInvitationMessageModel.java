package cn.bjhdltcdn.p2plive.model;

/**
 * Created by xiawenquan on 17/12/4.
 */

public class YouBanSendActiveInvitationMessageModel {

    private BaseUser baseUser;
    private long activityId;//活动id
    private int messageType;//用户id


    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
