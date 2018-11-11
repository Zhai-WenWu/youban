package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZHUDI on 2017/11/21.
 */

public class UserSchoolInfo implements Parcelable {
    private long userId;
    private long schoolId;//学校ID(ID=0为自定义学校名称)
    private String schoolName;//学校名称,
    private int status;
    private int updateNum;
    private String updateTime;
    private String addTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUpdateNum() {
        return updateNum;
    }

    public void setUpdateNum(int updateNum) {
        this.updateNum = updateNum;
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
        dest.writeLong(this.userId);
        dest.writeLong(this.schoolId);
        dest.writeString(this.schoolName);
        dest.writeInt(this.status);
        dest.writeInt(this.updateNum);
        dest.writeString(this.updateTime);
        dest.writeString(this.addTime);
    }

    public UserSchoolInfo() {
    }

    protected UserSchoolInfo(Parcel in) {
        this.userId = in.readLong();
        this.schoolId = in.readLong();
        this.schoolName = in.readString();
        this.status = in.readInt();
        this.updateNum = in.readInt();
        this.updateTime = in.readString();
        this.addTime = in.readString();
    }

    public static final Parcelable.Creator<UserSchoolInfo> CREATOR = new Parcelable.Creator<UserSchoolInfo>() {
        @Override
        public UserSchoolInfo createFromParcel(Parcel source) {
            return new UserSchoolInfo(source);
        }

        @Override
        public UserSchoolInfo[] newArray(int size) {
            return new UserSchoolInfo[size];
        }
    };
}
