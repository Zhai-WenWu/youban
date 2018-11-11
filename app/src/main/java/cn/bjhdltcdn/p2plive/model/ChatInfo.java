package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Description:聊天室对象
 * <p>
 * Data: 2018/6/8
 *
 * @author: zhudi
 */
public class ChatInfo implements Parcelable {
    private String chatId;//聊天室Id,
    private String chatName;//聊天室名称
    private String chatIcon;//聊天室头像,
    private String backgroundUrl;//房间背景,
    private ChatBaseUser baseUser;//{ChatBaseUser对象}(创建者的基本信息),
    private ChatBaseUser toBaseUser;//{ChatBaseUser对象}(当前用户的基本信息),
    private int status;//聊天室状态(默认为0--->开启,1--->关闭,2--->注销),
    private String description;//聊天室描述,
    private int onlineNumber;//在线人数,
    private String updateTime;//更新时间,
    private String addTime;//创建时间,
    private List<LabelInfo> labelList;//[{ChatRoomLabel},{ChatRoomLabel}...]标签列表
    private int userRole;//所属角色(1--->主持,2--->场控(管理员),默认为3--->观众),
    private int isLock;//是否上锁 0否 1是
    private int schoolLimit;//校友限制(0--->不限制,1--->校友),
    private int sexLimit;//性别限制(0--->不限制,1--->男,2--->女),

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public ChatBaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(ChatBaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public ChatBaseUser getToBaseUser() {
        return toBaseUser;
    }

    public void setToBaseUser(ChatBaseUser toBaseUser) {
        this.toBaseUser = toBaseUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOnlineNumber() {
        return onlineNumber;
    }

    public void setOnlineNumber(int onlineNumber) {
        this.onlineNumber = onlineNumber;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public List<LabelInfo> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<LabelInfo> labelList) {
        this.labelList = labelList;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public String getChatIcon() {
        return chatIcon;
    }

    public void setChatIcon(String chatIcon) {
        this.chatIcon = chatIcon;
    }

    public int getSchoolLimit() {
        return schoolLimit;
    }

    public void setSchoolLimit(int schoolLimit) {
        this.schoolLimit = schoolLimit;
    }

    public int getSexLimit() {
        return sexLimit;
    }

    public void setSexLimit(int sexLimit) {
        this.sexLimit = sexLimit;
    }

    public ChatInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chatId);
        dest.writeString(this.chatName);
        dest.writeString(this.chatIcon);
        dest.writeString(this.backgroundUrl);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeParcelable(this.toBaseUser, flags);
        dest.writeInt(this.status);
        dest.writeString(this.description);
        dest.writeInt(this.onlineNumber);
        dest.writeString(this.updateTime);
        dest.writeString(this.addTime);
        dest.writeTypedList(this.labelList);
        dest.writeInt(this.userRole);
        dest.writeInt(this.isLock);
        dest.writeInt(this.schoolLimit);
        dest.writeInt(this.sexLimit);
    }

    protected ChatInfo(Parcel in) {
        this.chatId = in.readString();
        this.chatName = in.readString();
        this.chatIcon = in.readString();
        this.backgroundUrl = in.readString();
        this.baseUser = in.readParcelable(ChatBaseUser.class.getClassLoader());
        this.toBaseUser = in.readParcelable(ChatBaseUser.class.getClassLoader());
        this.status = in.readInt();
        this.description = in.readString();
        this.onlineNumber = in.readInt();
        this.updateTime = in.readString();
        this.addTime = in.readString();
        this.labelList = in.createTypedArrayList(LabelInfo.CREATOR);
        this.userRole = in.readInt();
        this.isLock = in.readInt();
        this.schoolLimit = in.readInt();
        this.sexLimit = in.readInt();
    }

    public static final Creator<ChatInfo> CREATOR = new Creator<ChatInfo>() {
        @Override
        public ChatInfo createFromParcel(Parcel source) {
            return new ChatInfo(source);
        }

        @Override
        public ChatInfo[] newArray(int size) {
            return new ChatInfo[size];
        }
    };
}
