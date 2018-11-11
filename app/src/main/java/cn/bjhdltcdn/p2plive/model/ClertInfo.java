package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaiww on 2018/5/19.
 */

public class ClertInfo implements Parcelable {
    /* "userId":申请用户Id,
    "baseUser":{BaseUser对象}店主基本信息,
    "storeId":店铺id,
    "phoneNumber":手机号,
    "addr":地址,
    "selfDesc":自我描述,
    "receiptNum":接单次数,
    "addTime":申请时间,
    "updateTime":入职时间,
    "receiptNum":总接单次数,
    "status":店员状态(0正常,1解雇),*/

    public long userId;
    public BaseUser baseUser;
    public long storeId;
    public String phoneNumber;
    public String addr;
    public String selfDesc;
    public int receiptNum;
    public String addTime;
    public String updateTime;
    public int status;
    public long applyId;

    public long getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSelfDesc() {
        return selfDesc;
    }

    public void setSelfDesc(String selfDesc) {
        this.selfDesc = selfDesc;
    }

    public int getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(int receiptNum) {
        this.receiptNum = receiptNum;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public ClertInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeLong(this.storeId);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.addr);
        dest.writeString(this.selfDesc);
        dest.writeInt(this.receiptNum);
        dest.writeString(this.addTime);
        dest.writeString(this.updateTime);
        dest.writeInt(this.status);
        dest.writeLong(this.applyId);
    }

    protected ClertInfo(Parcel in) {
        this.userId = in.readLong();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.storeId = in.readLong();
        this.phoneNumber = in.readString();
        this.addr = in.readString();
        this.selfDesc = in.readString();
        this.receiptNum = in.readInt();
        this.addTime = in.readString();
        this.updateTime = in.readString();
        this.status = in.readInt();
        this.applyId = in.readLong();
    }

    public static final Creator<ClertInfo> CREATOR = new Creator<ClertInfo>() {
        @Override
        public ClertInfo createFromParcel(Parcel source) {
            return new ClertInfo(source);
        }

        @Override
        public ClertInfo[] newArray(int size) {
            return new ClertInfo[size];
        }
    };
}
