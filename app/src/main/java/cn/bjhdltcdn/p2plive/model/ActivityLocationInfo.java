package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2017/11/24.
 */

public class ActivityLocationInfo implements Parcelable {
    private String longitude;//经度,
    private String latitude;//维度,
    private String province;//省,
    private String city;//市,
    private String district;//区县,
    private String addr;//地址,

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.addr);
    }

    public ActivityLocationInfo() {
    }

    protected ActivityLocationInfo(Parcel in) {
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.addr = in.readString();
    }

    public static final Parcelable.Creator<ActivityLocationInfo> CREATOR = new Parcelable.Creator<ActivityLocationInfo>() {
        @Override
        public ActivityLocationInfo createFromParcel(Parcel source) {
            return new ActivityLocationInfo(source);
        }

        @Override
        public ActivityLocationInfo[] newArray(int size) {
            return new ActivityLocationInfo[size];
        }
    };
}
