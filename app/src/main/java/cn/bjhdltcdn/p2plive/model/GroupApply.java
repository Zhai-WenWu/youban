package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiawenquan on 17/12/6.
 */

public class GroupApply implements Parcelable {

    /**
     *        "baseUser":{BaseUser对象}(申请人的基本信息),
     "applyId":申请Id,
     "groupId":群组Id,
     "groupName":群组名称,
     "groupImg":群组头像,
     "status":审核状态(0-->未审核,1-->同意,2-->拒绝),
     "updateTime":审核时间,
     "addTime":添加时间,

     */

    private long applyId;
    private BaseUser baseUser;
    private long groupId;
    private String groupName;
    private String groupImg;
    private int status;
    private String updateTime;
    private String addTime;

    public long getApplyId() {
        return applyId;
    }

    public void setApplyId(long applyId) {
        this.applyId = applyId;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

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

    /**
     * 审核状态(0-->未审核,1-->同意,2-->拒绝),
     * @return
     */
    public int getStatus() {
        return status;
    }
    /**
     * 审核状态(0-->未审核,1-->同意,2-->拒绝),
     * @return
     */
    public void setStatus(int status) {
        this.status = status;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.applyId);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeLong(this.groupId);
        dest.writeString(this.groupName);
        dest.writeString(this.groupImg);
        dest.writeInt(this.status);
        dest.writeString(this.updateTime);
        dest.writeString(this.addTime);
    }

    public GroupApply() {
    }

    protected GroupApply(Parcel in) {
        this.applyId = in.readLong();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.groupId = in.readLong();
        this.groupName = in.readString();
        this.groupImg = in.readString();
        this.status = in.readInt();
        this.updateTime = in.readString();
        this.addTime = in.readString();
    }

    public static final Parcelable.Creator<GroupApply> CREATOR = new Parcelable.Creator<GroupApply>() {
        @Override
        public GroupApply createFromParcel(Parcel source) {
            return new GroupApply(source);
        }

        @Override
        public GroupApply[] newArray(int size) {
            return new GroupApply[size];
        }
    };
}
