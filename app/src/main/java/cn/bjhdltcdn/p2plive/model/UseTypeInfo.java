package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huwenhua on 2016/7/13.
 */
public class UseTypeInfo implements Parcelable {
    private long guid;//主键,
    private String serviceId;//服务ID,
    private String serviceName;//服务名称,
    private String serviceIcon;//服务图片路径,
    private int amount;//服务金额,
    private String serviceDesc;//服务描述(简介),
    private int status;//服务状态 0未启用 1 启用 2 已失效,
    private int useType;//服务类型,
    private String spareStr;//扩展字段,
    private String spareStr2;//扩展字段,
    private String spareLong;//扩展字段,
    private int coinsNum;//币数,
    private int iosPresentation;//IOS赠送币数,
    private int otherPresentation;//其他赠送币数,
    private String addTime;//添加时间

    public UseTypeInfo() {
    }

    public UseTypeInfo(long guid, String serviceId, String serviceName, String serviceIcon, int amount, String serviceDesc, int status, int useType, String spareStr, String spareStr2, String spareLong, int coinsNum, int iosPresentation, int otherPresentation, String addTime) {
        this.guid = guid;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceIcon = serviceIcon;
        this.amount = amount;
        this.serviceDesc = serviceDesc;
        this.status = status;
        this.useType = useType;
        this.spareStr = spareStr;
        this.spareStr2 = spareStr2;
        this.spareLong = spareLong;
        this.coinsNum = coinsNum;
        this.iosPresentation = iosPresentation;
        this.otherPresentation = otherPresentation;
        this.addTime = addTime;
    }

    public void setGuid(long guid) {
        this.guid = guid;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceIcon(String serviceIcon) {
        this.serviceIcon = serviceIcon;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public void setSpareStr(String spareStr) {
        this.spareStr = spareStr;
    }

    public void setSpareStr2(String spareStr2) {
        this.spareStr2 = spareStr2;
    }

    public void setSpareLong(String spareLong) {
        this.spareLong = spareLong;
    }

    public void setCoinsNum(int coinsNum) {
        this.coinsNum = coinsNum;
    }

    public void setIosPresentation(int iosPresentation) {
        this.iosPresentation = iosPresentation;
    }

    public void setOtherPresentation(int otherPresentation) {
        this.otherPresentation = otherPresentation;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public long getGuid() {
        return guid;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceIcon() {
        return serviceIcon;
    }

    public int getAmount() {
        return amount;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public int getStatus() {
        return status;
    }

    public int getUseType() {
        return useType;
    }

    public String getSpareStr() {
        return spareStr;
    }

    public String getSpareStr2() {
        return spareStr2;
    }

    public String getSpareLong() {
        return spareLong;
    }

    public int getCoinsNum() {
        return coinsNum;
    }

    public int getIosPresentation() {
        return iosPresentation;
    }

    public int getOtherPresentation() {
        return otherPresentation;
    }

    public String getAddTime() {
        return addTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.guid);
        dest.writeString(this.serviceId);
        dest.writeString(this.serviceName);
        dest.writeString(this.serviceIcon);
        dest.writeInt(this.amount);
        dest.writeString(this.serviceDesc);
        dest.writeInt(this.status);
        dest.writeInt(this.useType);
        dest.writeString(this.spareStr);
        dest.writeString(this.spareStr2);
        dest.writeString(this.spareLong);
        dest.writeInt(this.coinsNum);
        dest.writeInt(this.iosPresentation);
        dest.writeInt(this.otherPresentation);
        dest.writeString(this.addTime);
    }

    protected UseTypeInfo(Parcel in) {
        this.guid = in.readLong();
        this.serviceId = in.readString();
        this.serviceName = in.readString();
        this.serviceIcon = in.readString();
        this.amount = in.readInt();
        this.serviceDesc = in.readString();
        this.status = in.readInt();
        this.useType = in.readInt();
        this.spareStr = in.readString();
        this.spareStr2 = in.readString();
        this.spareLong = in.readString();
        this.coinsNum = in.readInt();
        this.iosPresentation = in.readInt();
        this.otherPresentation = in.readInt();
        this.addTime = in.readString();
    }

    public static final Parcelable.Creator<UseTypeInfo> CREATOR = new Parcelable.Creator<UseTypeInfo>() {
        @Override
        public UseTypeInfo createFromParcel(Parcel source) {
            return new UseTypeInfo(source);
        }

        @Override
        public UseTypeInfo[] newArray(int size) {
            return new UseTypeInfo[size];
        }
    };
}

