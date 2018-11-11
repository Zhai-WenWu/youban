package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2018/1/18.
 */

public class ShareInfo implements Parcelable {
    private long userId;//用户Id,
    private long shareId;//分享ID,
    private String content;//分享内容,
    private int status;//状态(0--->未删除，1--->删除),
    private String addTime;//分享时间

    public ShareInfo() {
    }

    public ShareInfo(long userId, long shareId, String content, int status) {
        this.userId = userId;
        this.shareId = shareId;
        this.content = content;
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeLong(this.shareId);
        dest.writeString(this.content);
        dest.writeInt(this.status);
        dest.writeString(this.addTime);
    }

    protected ShareInfo(Parcel in) {
        this.userId = in.readLong();
        this.shareId = in.readLong();
        this.content = in.readString();
        this.status = in.readInt();
        this.addTime = in.readString();
    }

    public static final Creator<ShareInfo> CREATOR = new Creator<ShareInfo>() {
        @Override
        public ShareInfo createFromParcel(Parcel source) {
            return new ShareInfo(source);
        }

        @Override
        public ShareInfo[] newArray(int size) {
            return new ShareInfo[size];
        }
    };
}
