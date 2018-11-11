package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaiww on 2018/5/16.
 */

public class RefundReason implements Parcelable {
    /*"reasonId":主键Id,
    "reasonName":名称,
    "reasonDesc":描述,
    "remark":备注,*/

    public long reasonId;
    public String reasonName;
    public String reasonDesc;
    public String remark;

    public long getReasonId() {
        return reasonId;
    }

    public void setReasonId(long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RefundReason() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.reasonId);
        dest.writeString(this.reasonName);
        dest.writeString(this.reasonDesc);
        dest.writeString(this.remark);
    }

    protected RefundReason(Parcel in) {
        this.reasonId = in.readLong();
        this.reasonName = in.readString();
        this.reasonDesc = in.readString();
        this.remark = in.readString();
    }

    public static final Creator<RefundReason> CREATOR = new Creator<RefundReason>() {
        @Override
        public RefundReason createFromParcel(Parcel source) {
            return new RefundReason(source);
        }

        @Override
        public RefundReason[] newArray(int size) {
            return new RefundReason[size];
        }
    };
}
