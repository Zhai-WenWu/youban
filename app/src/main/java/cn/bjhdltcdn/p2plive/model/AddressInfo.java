package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhai_PC on 2018/4/17.
 */

public class AddressInfo implements Parcelable {
    /*  "addressId":地址id,
    "userId"：用户Id,
    "contactName":联系人,
    "sex":性别,
    "phoneNumber":电话,
    "address":地址,
    "addTime":添加时间,
    "updateTime":更新时间,*/

    public long addressId;
    public long userId;
    public String contactName;
    public int sex;
    public String phoneNumber;
    public String address;
    public String addTime;
    public String updateTime;
    private int isDefault;

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.addressId);
        dest.writeLong(this.userId);
        dest.writeString(this.contactName);
        dest.writeInt(this.sex);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.address);
        dest.writeString(this.addTime);
        dest.writeString(this.updateTime);
        dest.writeInt(this.isDefault);
    }

    public AddressInfo() {
    }

    protected AddressInfo(Parcel in) {
        this.addressId = in.readLong();
        this.userId = in.readLong();
        this.contactName = in.readString();
        this.sex = in.readInt();
        this.phoneNumber = in.readString();
        this.address = in.readString();
        this.addTime = in.readString();
        this.updateTime = in.readString();
        this.isDefault = in.readInt();
    }

    public static final Creator<AddressInfo> CREATOR = new Creator<AddressInfo>() {
        @Override
        public AddressInfo createFromParcel(Parcel source) {
            return new AddressInfo(source);
        }

        @Override
        public AddressInfo[] newArray(int size) {
            return new AddressInfo[size];
        }
    };
}
