package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2018/4/13.
 */

public class UserOrder implements Parcelable {
    private long guid;//主键,
    private long orderId;//订单号,
    private long userId;//用户ID,
    private int useType;//服务类型,
    private int payType;//支付类型,
    private int isFee;//订单状态,
    private String feeTime;//订单回调时间,
    private String amount;//支付金额,
    private String mobile;//用户手机,
    private String fromChannel;//用户渠道,
    private long serviceId;//服务ID,
    private String spareStr;//扩展字段,
    private String spareStr2;//扩展字段,
    private String spareLong;//扩展字段,
    private String payTime;//支付时间,
    private String coinsNum;//币数,
    private UseTypeInfo useTypeInfo;//{服务对象}

    public long getGuid() {
        return guid;
    }

    public void setGuid(long guid) {
        this.guid = guid;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getIsFee() {
        return isFee;
    }

    public void setIsFee(int isFee) {
        this.isFee = isFee;
    }

    public String getFeeTime() {
        return feeTime;
    }

    public void setFeeTime(String feeTime) {
        this.feeTime = feeTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFromChannel() {
        return fromChannel;
    }

    public void setFromChannel(String fromChannel) {
        this.fromChannel = fromChannel;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getSpareStr() {
        return spareStr;
    }

    public void setSpareStr(String spareStr) {
        this.spareStr = spareStr;
    }

    public String getSpareStr2() {
        return spareStr2;
    }

    public void setSpareStr2(String spareStr2) {
        this.spareStr2 = spareStr2;
    }

    public String getSpareLong() {
        return spareLong;
    }

    public void setSpareLong(String spareLong) {
        this.spareLong = spareLong;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getCoinsNum() {
        return coinsNum;
    }

    public void setCoinsNum(String coinsNum) {
        this.coinsNum = coinsNum;
    }

    public UseTypeInfo getUseTypeInfo() {
        return useTypeInfo;
    }

    public void setUseTypeInfo(UseTypeInfo useTypeInfo) {
        this.useTypeInfo = useTypeInfo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.guid);
        dest.writeLong(this.orderId);
        dest.writeLong(this.userId);
        dest.writeInt(this.useType);
        dest.writeInt(this.payType);
        dest.writeInt(this.isFee);
        dest.writeString(this.feeTime);
        dest.writeString(this.amount);
        dest.writeString(this.mobile);
        dest.writeString(this.fromChannel);
        dest.writeLong(this.serviceId);
        dest.writeString(this.spareStr);
        dest.writeString(this.spareStr2);
        dest.writeString(this.spareLong);
        dest.writeString(this.payTime);
        dest.writeString(this.coinsNum);
        dest.writeParcelable(this.useTypeInfo, flags);
    }

    public UserOrder() {
    }

    protected UserOrder(Parcel in) {
        this.guid = in.readLong();
        this.orderId = in.readLong();
        this.userId = in.readLong();
        this.useType = in.readInt();
        this.payType = in.readInt();
        this.isFee = in.readInt();
        this.feeTime = in.readString();
        this.amount = in.readString();
        this.mobile = in.readString();
        this.fromChannel = in.readString();
        this.serviceId = in.readLong();
        this.spareStr = in.readString();
        this.spareStr2 = in.readString();
        this.spareLong = in.readString();
        this.payTime = in.readString();
        this.coinsNum = in.readString();
        this.useTypeInfo = in.readParcelable(UseTypeInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserOrder> CREATOR = new Parcelable.Creator<UserOrder>() {
        @Override
        public UserOrder createFromParcel(Parcel source) {
            return new UserOrder(source);
        }

        @Override
        public UserOrder[] newArray(int size) {
            return new UserOrder[size];
        }
    };
}
