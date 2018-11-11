package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2018/3/26.
 */

public class OriginalInfo implements Parcelable {
    private long userId;//用户Id,
    private String nickName;//用户昵称,
    private String content;//原文内容,

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeString(this.nickName);
        dest.writeString(this.content);
    }

    public OriginalInfo() {
    }

    protected OriginalInfo(Parcel in) {
        this.userId = in.readLong();
        this.nickName = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<OriginalInfo> CREATOR = new Parcelable.Creator<OriginalInfo>() {
        @Override
        public OriginalInfo createFromParcel(Parcel source) {
            return new OriginalInfo(source);
        }

        @Override
        public OriginalInfo[] newArray(int size) {
            return new OriginalInfo[size];
        }
    };
}
