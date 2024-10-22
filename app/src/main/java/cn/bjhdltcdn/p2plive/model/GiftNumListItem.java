package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiawenquan on 17/1/12.
 */

public class GiftNumListItem implements Parcelable {
    private String key;
    private String value ;

    public GiftNumListItem(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
    }

    protected GiftNumListItem(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
    }

    public static final Creator<GiftNumListItem> CREATOR = new Creator<GiftNumListItem>() {
        public GiftNumListItem createFromParcel(Parcel source) {
            return new GiftNumListItem(source);
        }

        public GiftNumListItem[] newArray(int size) {
            return new GiftNumListItem[size];
        }
    };
}
