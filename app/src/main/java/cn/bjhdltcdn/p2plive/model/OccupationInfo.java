package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZHUDI on 2017/11/21.
 */

public class OccupationInfo implements Parcelable {
    private long occupationId;//职业ID,
    private String occupationName;//职业名称,
    private String occupationDesc;//职业描述,
    private int isSelect;//是否选择(0未选,1已选),

    public long getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(long occupationId) {
        this.occupationId = occupationId;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    public String getOccupationDesc() {
        return occupationDesc;
    }

    public void setOccupationDesc(String occupationDesc) {
        this.occupationDesc = occupationDesc;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.occupationId);
        dest.writeString(this.occupationName);
        dest.writeString(this.occupationDesc);
        dest.writeInt(this.isSelect);
    }

    public OccupationInfo() {
    }

    protected OccupationInfo(Parcel in) {
        this.occupationId = in.readLong();
        this.occupationName = in.readString();
        this.occupationDesc = in.readString();
        this.isSelect = in.readInt();
    }

    public static final Creator<OccupationInfo> CREATOR = new Creator<OccupationInfo>() {
        @Override
        public OccupationInfo createFromParcel(Parcel source) {
            return new OccupationInfo(source);
        }

        @Override
        public OccupationInfo[] newArray(int size) {
            return new OccupationInfo[size];
        }
    };
}
