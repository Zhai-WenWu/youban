package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaiww on 2018/5/22.
 */

public class ClertDetail implements Parcelable {
    /* "clertInfo":{ClertInfo对象}店主基本信息,
    "dayReceiptNum":当日接单次数,
    "monthReceiptNum":当月接单次数,*/
    public ClertInfo clertInfo;
    public int dayReceiptNum;
    public int monthReceiptNum;
    public int totalReceiptNum;
    public String schoolName;

    public int getTotalReceiptNum() {
        return totalReceiptNum;
    }

    public void setTotalReceiptNum(int totalReceiptNum) {
        this.totalReceiptNum = totalReceiptNum;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public ClertInfo getClertInfo() {
        return clertInfo;
    }

    public void setClertInfo(ClertInfo clertInfo) {
        this.clertInfo = clertInfo;
    }

    public int getDayReceiptNum() {
        return dayReceiptNum;
    }

    public void setDayReceiptNum(int dayReceiptNum) {
        this.dayReceiptNum = dayReceiptNum;
    }

    public int getMonthReceiptNum() {
        return monthReceiptNum;
    }

    public void setMonthReceiptNum(int monthReceiptNum) {
        this.monthReceiptNum = monthReceiptNum;
    }

    public ClertDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.clertInfo, flags);
        dest.writeInt(this.dayReceiptNum);
        dest.writeInt(this.monthReceiptNum);
        dest.writeInt(this.totalReceiptNum);
        dest.writeString(this.schoolName);
    }

    protected ClertDetail(Parcel in) {
        this.clertInfo = in.readParcelable(ClertInfo.class.getClassLoader());
        this.dayReceiptNum = in.readInt();
        this.monthReceiptNum = in.readInt();
        this.totalReceiptNum = in.readInt();
        this.schoolName = in.readString();
    }

    public static final Creator<ClertDetail> CREATOR = new Creator<ClertDetail>() {
        @Override
        public ClertDetail createFromParcel(Parcel source) {
            return new ClertDetail(source);
        }

        @Override
        public ClertDetail[] newArray(int size) {
            return new ClertDetail[size];
        }
    };
}
