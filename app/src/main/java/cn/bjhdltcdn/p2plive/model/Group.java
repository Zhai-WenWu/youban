package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2017/11/10.
 * 群组对象
 */

public class Group implements Parcelable {
    private long groupId;//群组Id,
    private String groupName;//群组名称,
    private String groupImg;//群组头像,
    private String userId;//用户Id,
    private int status;//群组状态(默认为0--->开启,1--->解散),
    private int groupMode;//成员进群方式(1直接入群,2申请进群),
    private String addTime;//创建时间,
    private BaseUser baseUser;//{BaseUser对象}(用户的基本信息),
    private int isExistGroup;//当前用户是否在群中(0不在,1在),
    private int number;//成员个数
    private int isPublic;//是否公开圈子(默认为0--->公开,1--->隐藏),

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGroupMode() {
        return groupMode;
    }

    public void setGroupMode(int groupMode) {
        this.groupMode = groupMode;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public int getIsExistGroup() {
        return isExistGroup;
    }

    public void setIsExistGroup(int isExistGroup) {
        this.isExistGroup = isExistGroup;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public Group() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.groupId);
        dest.writeString(this.groupName);
        dest.writeString(this.groupImg);
        dest.writeString(this.userId);
        dest.writeInt(this.status);
        dest.writeInt(this.groupMode);
        dest.writeString(this.addTime);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeInt(this.isExistGroup);
        dest.writeInt(this.number);
        dest.writeInt(this.isPublic);
    }

    protected Group(Parcel in) {
        this.groupId = in.readLong();
        this.groupName = in.readString();
        this.groupImg = in.readString();
        this.userId = in.readString();
        this.status = in.readInt();
        this.groupMode = in.readInt();
        this.addTime = in.readString();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.isExistGroup = in.readInt();
        this.number = in.readInt();
        this.isPublic = in.readInt();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
