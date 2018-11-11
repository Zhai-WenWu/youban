package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiawenquan on 17/12/1.
 */

public class GroupUser implements Parcelable {
    private long guId;//主键ID',
    private long groupId;//群组Id,
    private long userId;//用户Id,
    private int userRole;//所属角色(1--->群主,2--->管理员,3--->普通成员),
    private String addTime;//创建时间,
    private int isDisturbMode;//消息免打扰模式(默认为0--->关闭,1--->开启),
    private BaseUser baseUser;//{BaseUser对象}(用户的基本信息),
    private String groupName;//群组名称,

    private int isSelect;

    public long getGuId() {
        return guId;
    }

    public void setGuId(long guId) {
        this.guId = guId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getIsDisturbMode() {
        return isDisturbMode;
    }

    public void setIsDisturbMode(int isDisturbMode) {
        this.isDisturbMode = isDisturbMode;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public GroupUser() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.guId);
        dest.writeLong(this.groupId);
        dest.writeLong(this.userId);
        dest.writeInt(this.userRole);
        dest.writeString(this.addTime);
        dest.writeInt(this.isDisturbMode);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeString(this.groupName);
        dest.writeInt(this.isSelect);
    }

    protected GroupUser(Parcel in) {
        this.guId = in.readLong();
        this.groupId = in.readLong();
        this.userId = in.readLong();
        this.userRole = in.readInt();
        this.addTime = in.readString();
        this.isDisturbMode = in.readInt();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.groupName = in.readString();
        this.isSelect = in.readInt();
    }

    public static final Creator<GroupUser> CREATOR = new Creator<GroupUser>() {
        @Override
        public GroupUser createFromParcel(Parcel source) {
            return new GroupUser(source);
        }

        @Override
        public GroupUser[] newArray(int size) {
            return new GroupUser[size];
        }
    };
}
