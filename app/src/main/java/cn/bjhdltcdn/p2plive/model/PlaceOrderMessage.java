package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Hu_PC on 2018/7/23.
 */
@Entity
public class PlaceOrderMessage implements Parcelable {
    @Id(autoincrement = false)
    private String userIdAndStoreId;

    public String getUserIdAndStoreId() {
        return userIdAndStoreId;
    }

    public void setUserIdAndStoreId(String userIdAndStoreId) {
        this.userIdAndStoreId = userIdAndStoreId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userIdAndStoreId);
    }

    public PlaceOrderMessage() {
    }

    protected PlaceOrderMessage(Parcel in) {
        this.userIdAndStoreId = in.readString();
    }

    @Generated(hash = 2099290706)
    public PlaceOrderMessage(String userIdAndStoreId) {
        this.userIdAndStoreId = userIdAndStoreId;
    }

    public static final Creator<PlaceOrderMessage> CREATOR = new Creator<PlaceOrderMessage>() {
        @Override
        public PlaceOrderMessage createFromParcel(Parcel source) {
            return new PlaceOrderMessage(source);
        }

        @Override
        public PlaceOrderMessage[] newArray(int size) {
            return new PlaceOrderMessage[size];
        }
    };
}
