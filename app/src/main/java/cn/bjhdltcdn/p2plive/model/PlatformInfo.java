package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiawenquan on 17/11/7.
 */

public class PlatformInfo implements Parcelable {
    //软件版本号
    private String appVersion;
    //平台(Android)
    private String platform;
    //渠道号
    private String channel;
    //设备号
    private String imei;
    //系统版本
    private String systemVersion;
    // 获取手机型号：android.os.Build.MODEL
    private String model;
    // 获取手机厂商: android.os.Build.MANUFACTURER
    private String manufacturer;

    // 获取ip地址
    private String ip;

    // 用户凭证(id,或者手机号,有可能是null)
    private long userId;

    public PlatformInfo() {
    }

    public PlatformInfo(String appVersion, String platform, String channel, String imei, String systemVersion, String model, String manufacturer,String ip,long userId) {
        this.appVersion = appVersion;
        this.platform = platform;
        this.channel = channel;
        this.imei = imei;
        this.systemVersion = systemVersion;
        this.model = model;
        this.manufacturer = manufacturer;
        this.ip = ip;
        this.userId = userId;
    }


    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public static Creator<PlatformInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appVersion);
        dest.writeString(this.platform);
        dest.writeString(this.channel);
        dest.writeString(this.imei);
        dest.writeString(this.systemVersion);
        dest.writeString(this.model);
        dest.writeString(this.manufacturer);
        dest.writeString(this.ip);
        dest.writeLong(this.userId);
    }

    protected PlatformInfo(Parcel in) {
        this.appVersion = in.readString();
        this.platform = in.readString();
        this.channel = in.readString();
        this.imei = in.readString();
        this.systemVersion = in.readString();
        this.model = in.readString();
        this.manufacturer = in.readString();
        this.ip = in.readString();
        this.userId = in.readLong();
    }

    public static final Creator<PlatformInfo> CREATOR = new Creator<PlatformInfo>() {
        @Override
        public PlatformInfo createFromParcel(Parcel source) {
            return new PlatformInfo(source);
        }

        @Override
        public PlatformInfo[] newArray(int size) {
            return new PlatformInfo[size];
        }
    };
}
