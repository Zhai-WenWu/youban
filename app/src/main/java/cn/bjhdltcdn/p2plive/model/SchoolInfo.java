package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZHUDI on 2017/11/21.
 */

public class SchoolInfo implements Parcelable {
    private long schoolId;//学校ID(ID=0为自定义学校名称)
    private String schoolName;//学校名称,

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

    public SchoolInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.schoolId);
        dest.writeString(this.schoolName);
    }

    protected SchoolInfo(Parcel in) {
        this.schoolId = in.readLong();
        this.schoolName = in.readString();
    }

    public static final Creator<SchoolInfo> CREATOR = new Creator<SchoolInfo>() {
        @Override
        public SchoolInfo createFromParcel(Parcel source) {
            return new SchoolInfo(source);
        }

        @Override
        public SchoolInfo[] newArray(int size) {
            return new SchoolInfo[size];
        }
    };
}
