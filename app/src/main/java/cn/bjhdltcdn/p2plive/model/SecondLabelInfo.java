package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2018/8/3.
 */

public class SecondLabelInfo implements Parcelable {
    private long secondLabelId;//二级标签id,
    private String secondLabelName;//二级标签名称,
    private String secondLabelDesc;//二级标签描述,
    private int status;//状态(0正常,1禁用),
    private String addTime;//添加时间,
    private int isSelect;//是否选择(0未选择,1选择),

    public SecondLabelInfo() {
    }

    public SecondLabelInfo(long secondLabelId, String secondLabelName, String secondLabelDesc, int status, String addTime) {
        this.secondLabelId = secondLabelId;
        this.secondLabelName = secondLabelName;
        this.secondLabelDesc = secondLabelDesc;
        this.status = status;
        this.addTime = addTime;
    }

    public long getSecondLabelId() {
        return secondLabelId;
    }

    public void setSecondLabelId(long secondLabelId) {
        this.secondLabelId = secondLabelId;
    }

    public String getSecondLabelName() {
        return secondLabelName;
    }

    public void setSecondLabelName(String secondLabelName) {
        this.secondLabelName = secondLabelName;
    }

    public String getSecondLabelDesc() {
        return secondLabelDesc;
    }

    public void setSecondLabelDesc(String secondLabelDesc) {
        this.secondLabelDesc = secondLabelDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
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
        dest.writeLong(this.secondLabelId);
        dest.writeString(this.secondLabelName);
        dest.writeString(this.secondLabelDesc);
        dest.writeInt(this.status);
        dest.writeString(this.addTime);
        dest.writeInt(this.isSelect);
    }

    protected SecondLabelInfo(Parcel in) {
        this.secondLabelId = in.readLong();
        this.secondLabelName = in.readString();
        this.secondLabelDesc = in.readString();
        this.status = in.readInt();
        this.addTime = in.readString();
        this.isSelect = in.readInt();
    }

    public static final Parcelable.Creator<SecondLabelInfo> CREATOR = new Parcelable.Creator<SecondLabelInfo>() {
        @Override
        public SecondLabelInfo createFromParcel(Parcel source) {
            return new SecondLabelInfo(source);
        }

        @Override
        public SecondLabelInfo[] newArray(int size) {
            return new SecondLabelInfo[size];
        }
    };
}
