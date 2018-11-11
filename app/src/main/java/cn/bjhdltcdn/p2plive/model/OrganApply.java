package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiawenquan on 17/12/6.
 */

public class OrganApply implements Parcelable {

    /**
     *  "baseUser":{BaseUser对象}(申请人的基本信息),
     "applyId":申请Id,
     "organId":圈子Id,
     "organImg":圈子封面,
     "organName":圈子名称,
     "status":审核状态(1-->未审核,2-->同意,3-->拒绝),
     "authTime":审核时间,
     "addTime":添加时间,
     */

    private BaseUser baseUser;
    private long applyId;
    private long organId;
    private String organImg;
    private String organName;
    private int status;
    private String authTime;
    private String addTime;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }

    public String getOrganImg() {
        return organImg;
    }

    public void setOrganImg(String organImg) {
        this.organImg = organImg;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuthTime() {
        return authTime;
    }

    public void setAuthTime(String authTime) {
        this.authTime = authTime;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public OrganApply() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseUser, flags);
        dest.writeLong(this.applyId);
        dest.writeLong(this.organId);
        dest.writeString(this.organImg);
        dest.writeString(this.organName);
        dest.writeInt(this.status);
        dest.writeString(this.authTime);
        dest.writeString(this.addTime);
        dest.writeInt(this.type);
    }

    protected OrganApply(Parcel in) {
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.applyId = in.readLong();
        this.organId = in.readLong();
        this.organImg = in.readString();
        this.organName = in.readString();
        this.status = in.readInt();
        this.authTime = in.readString();
        this.addTime = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<OrganApply> CREATOR = new Creator<OrganApply>() {
        @Override
        public OrganApply createFromParcel(Parcel source) {
            return new OrganApply(source);
        }

        @Override
        public OrganApply[] newArray(int size) {
            return new OrganApply[size];
        }
    };
}
