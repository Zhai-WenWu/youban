package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiawenquan on 18/3/8.
 */

public class CashAccount implements Parcelable {

    /*
     "exchangeId":提现账户记录id,
    "userId":用户Id,
    "account":提现账户,
    "name":开户人姓名,
    "phoneNumber":银行预留的手机号
     */

    private long exchangeId;
    private long userId;
    private String account;
    private String name;
    private String phoneNumber;
    private String bankName;
    private String updateTime;
    private String addTime;
    private int status;
    private int type;

    public long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.exchangeId);
        dest.writeLong(this.userId);
        dest.writeString(this.account);
        dest.writeString(this.name);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.bankName);
        dest.writeString(this.updateTime);
        dest.writeString(this.addTime);
        dest.writeInt(this.status);
        dest.writeInt(this.type);
    }

    public CashAccount() {
    }

    protected CashAccount(Parcel in) {
        this.exchangeId = in.readLong();
        this.userId = in.readLong();
        this.account = in.readString();
        this.name = in.readString();
        this.phoneNumber = in.readString();
        this.bankName = in.readString();
        this.updateTime = in.readString();
        this.addTime = in.readString();
        this.status = in.readInt();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<CashAccount> CREATOR = new Parcelable.Creator<CashAccount>() {
        @Override
        public CashAccount createFromParcel(Parcel source) {
            return new CashAccount(source);
        }

        @Override
        public CashAccount[] newArray(int size) {
            return new CashAccount[size];
        }
    };
}
