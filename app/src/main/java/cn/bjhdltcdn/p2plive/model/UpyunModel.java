package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiawenquan on 17/8/11.
 */

public class UpyunModel implements Parcelable {
    private int code;
    private String message;
    private long file_size;
    private String url;
    private long time;
    private String mimetype;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.message);
        dest.writeLong(this.file_size);
        dest.writeString(this.url);
        dest.writeLong(this.time);
        dest.writeString(this.mimetype);
    }

    public UpyunModel() {
    }

    protected UpyunModel(Parcel in) {
        this.code = in.readInt();
        this.message = in.readString();
        this.file_size = in.readLong();
        this.url = in.readString();
        this.time = in.readLong();
        this.mimetype = in.readString();
    }

    public static final Creator<UpyunModel> CREATOR = new Creator<UpyunModel>() {
        @Override
        public UpyunModel createFromParcel(Parcel source) {
            return new UpyunModel(source);
        }

        @Override
        public UpyunModel[] newArray(int size) {
            return new UpyunModel[size];
        }
    };
}
