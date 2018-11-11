package cn.bjhdltcdn.p2plive.model;

/**
 * Created by xiawenquan on 17/12/4.
 */

public class YouBanGroupSharedMessageModel {

   private long groupId;
   private int number;
   private String groupName;
   private String groupImg;
   private int messageType;
   private int isExistGroup;
   private int groupMode;


    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImg() {
        return groupImg;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getIsExistGroup() {
        return isExistGroup;
    }

    public void setIsExistGroup(int isExistGroup) {
        this.isExistGroup = isExistGroup;
    }

    public int getGroupMode() {
        return groupMode;
    }

    public void setGroupMode(int groupMode) {
        this.groupMode = groupMode;
    }
}
