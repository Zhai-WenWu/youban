package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaiww on 2018/5/18.
 */

public class ApplyClert implements Parcelable {

    /*"applyId":申请id,
    "userId":申请用户Id,
    "storeId":店铺id,
    "phoneNumber":手机号,
    "addr":地址,
    "selfDesc":自我描述,
    "cardFrontImg":学生证封面,
    "cardBackImg":学生证有你身份信息的页面,*/

    public long applyId;
    public long userId;
    public long storeId;
    public String phoneNumber;
    public String addr;
    public String selfDesc;
    public String cardFrontImg;
    public String cardBackImg;
    public BaseUser baseUser;
    public int status;//状态(0未认证,1等待审核,2审核通过,3审核拒绝)

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public long getApplyId() {
        return applyId;
    }

    public void setApplyId(long applyId) {
        this.applyId = applyId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getCardFrontImg() {
        return cardFrontImg;
    }

    public void setCardFrontImg(String cardFrontImg) {
        this.cardFrontImg = cardFrontImg;
    }

    public String getCardBackImg() {
        return cardBackImg;
    }

    public void setCardBackImg(String cardBackImg) {
        this.cardBackImg = cardBackImg;
    }

    public ApplyClert() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.applyId);
        dest.writeLong(this.userId);
        dest.writeLong(this.storeId);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.addr);
        dest.writeString(this.selfDesc);
        dest.writeString(this.cardFrontImg);
        dest.writeString(this.cardBackImg);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeInt(this.status);
    }

    protected ApplyClert(Parcel in) {
        this.applyId = in.readLong();
        this.userId = in.readLong();
        this.storeId = in.readLong();
        this.phoneNumber = in.readString();
        this.addr = in.readString();
        this.selfDesc = in.readString();
        this.cardFrontImg = in.readString();
        this.cardBackImg = in.readString();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.status = in.readInt();
    }

    public static final Creator<ApplyClert> CREATOR = new Creator<ApplyClert>() {
        @Override
        public ApplyClert createFromParcel(Parcel source) {
            return new ApplyClert(source);
        }

        @Override
        public ApplyClert[] newArray(int size) {
            return new ApplyClert[size];
        }
    };
}
