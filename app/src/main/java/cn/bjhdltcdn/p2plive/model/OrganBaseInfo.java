package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2018/4/4.
 */

public class OrganBaseInfo implements Parcelable {
    private long organId;//圈子ID,
    private String organName;//圈子名称,
    private String organImg;//圈子背景图,
    private long hobbyId;//圈子一级类别id,
    private String hobbyName;//圈子类别名称,

    public long getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(long hobbyId) {
        this.hobbyId = hobbyId;
    }

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganImg() {
        return organImg;
    }

    public void setOrganImg(String organImg) {
        this.organImg = organImg;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
    }

    public OrganBaseInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.organId);
        dest.writeString(this.organName);
        dest.writeString(this.organImg);
        dest.writeLong(this.hobbyId);
        dest.writeString(this.hobbyName);
    }

    protected OrganBaseInfo(Parcel in) {
        this.organId = in.readLong();
        this.organName = in.readString();
        this.organImg = in.readString();
        this.hobbyId = in.readLong();
        this.hobbyName = in.readString();
    }

    public static final Creator<OrganBaseInfo> CREATOR = new Creator<OrganBaseInfo>() {
        @Override
        public OrganBaseInfo createFromParcel(Parcel source) {
            return new OrganBaseInfo(source);
        }

        @Override
        public OrganBaseInfo[] newArray(int size) {
            return new OrganBaseInfo[size];
        }
    };
}
