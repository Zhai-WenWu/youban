package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZHAI on 2018/3/6.
 */

public class UserOccupationRecordInfo implements Parcelable {
    public String occupationName;
    public int status;
    public long occupationRecordId;
    public String addTime;
    public long userId;

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getOccupationRecordId() {
        return occupationRecordId;
    }

    public void setOccupationRecordId(long occupationRecordId) {
        this.occupationRecordId = occupationRecordId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.occupationName);
        dest.writeInt(this.status);
        dest.writeLong(this.occupationRecordId);
        dest.writeString(this.addTime);
        dest.writeLong(this.userId);
    }

    public UserOccupationRecordInfo() {
    }

    protected UserOccupationRecordInfo(Parcel in) {
        this.occupationName = in.readString();
        this.status = in.readInt();
        this.occupationRecordId = in.readLong();
        this.addTime = in.readString();
        this.userId = in.readLong();
    }

    public static final Parcelable.Creator<UserOccupationRecordInfo> CREATOR = new Parcelable.Creator<UserOccupationRecordInfo>() {
        @Override
        public UserOccupationRecordInfo createFromParcel(Parcel source) {
            return new UserOccupationRecordInfo(source);
        }

        @Override
        public UserOccupationRecordInfo[] newArray(int size) {
            return new UserOccupationRecordInfo[size];
        }
    };
}
