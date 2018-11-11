package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ZHUDI on 2017/11/17.
 */

public class RoomInfo implements Parcelable {
    private long roomId;//房间Id,
    private String roomName;//房间名称,
    private String backgroundUrl;//房间背景,
    private RoomBaseUser baseUser;//{RoomBaseUser对象}(主持人的基本信息),
    private RoomBaseUser toBaseUser;//{RoomBaseUser对象}(当前用户的基本信息),
    private int status;//房间状态(默认为0--->开启,1--->关闭,2--->注销),
    private String description;//房间描述,
    private int onlineNumber;//在线人数,
    private String updateTime;//更新时间,
    private String addTime;//创建时间,
    private int passwordType;//加密类型(0--->不加密,1--->密码),
    private int isAttention;//关注状态(0-->未关注,1-->已关注),
    private List<String> labelList;//[{String},{String},..]房间标签,
    private String password;//密码,
    private int userRole;//所属角色(1--->主持,2--->场控(管理员),默认为3--->观众),

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
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

    public int getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(int passwordType) {
        this.passwordType = passwordType;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoomBaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(RoomBaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public RoomBaseUser getToBaseUser() {
        return toBaseUser;
    }

    public void setToBaseUser(RoomBaseUser toBaseUser) {
        this.toBaseUser = toBaseUser;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public RoomInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.roomId);
        dest.writeString(this.roomName);
        dest.writeString(this.backgroundUrl);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeParcelable(this.toBaseUser, flags);
        dest.writeInt(this.status);
        dest.writeString(this.description);
        dest.writeInt(this.onlineNumber);
        dest.writeString(this.updateTime);
        dest.writeString(this.addTime);
        dest.writeInt(this.passwordType);
        dest.writeInt(this.isAttention);
        dest.writeStringList(this.labelList);
        dest.writeString(this.password);
        dest.writeInt(this.userRole);
    }

    protected RoomInfo(Parcel in) {
        this.roomId = in.readLong();
        this.roomName = in.readString();
        this.backgroundUrl = in.readString();
        this.baseUser = in.readParcelable(RoomBaseUser.class.getClassLoader());
        this.toBaseUser = in.readParcelable(RoomBaseUser.class.getClassLoader());
        this.status = in.readInt();
        this.description = in.readString();
        this.onlineNumber = in.readInt();
        this.updateTime = in.readString();
        this.addTime = in.readString();
        this.passwordType = in.readInt();
        this.isAttention = in.readInt();
        this.labelList = in.createStringArrayList();
        this.password = in.readString();
        this.userRole = in.readInt();
    }

    public static final Creator<RoomInfo> CREATOR = new Creator<RoomInfo>() {
        @Override
        public RoomInfo createFromParcel(Parcel source) {
            return new RoomInfo(source);
        }

        @Override
        public RoomInfo[] newArray(int size) {
            return new RoomInfo[size];
        }
    };
}
