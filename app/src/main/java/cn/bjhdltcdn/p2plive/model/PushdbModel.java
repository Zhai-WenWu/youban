package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xiawenquan on 17/12/5.
 */

@Entity
public class PushdbModel implements Parcelable {
    //主键自增类型必须是大写Long
    @Id(autoincrement = true)
    private Long messageId;
    private String source;
    private String objectName;
    private long messageType;

    /**
     * 0 未读
     * 1 已读
     */
    private int unRead;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public long getMessageType() {
        return messageType;
    }

    /**
     * 0 未读
     * 1 已读
     */
    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }



    public PushdbModel() {
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public void setMessageType(long messageType) {
        this.messageType = messageType;
    }

    @Generated(hash = 1185366944)
    public PushdbModel(Long messageId, String source, String objectName,
            long messageType, int unRead) {
        this.messageId = messageId;
        this.source = source;
        this.objectName = objectName;
        this.messageType = messageType;
        this.unRead = unRead;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.messageId);
        dest.writeString(this.source);
        dest.writeString(this.objectName);
        dest.writeLong(this.messageType);
        dest.writeInt(this.unRead);
    }

    protected PushdbModel(Parcel in) {
        this.messageId = (Long) in.readValue(Long.class.getClassLoader());
        this.source = in.readString();
        this.objectName = in.readString();
        this.messageType = in.readLong();
        this.unRead = in.readInt();
    }

    public static final Creator<PushdbModel> CREATOR = new Creator<PushdbModel>() {
        @Override
        public PushdbModel createFromParcel(Parcel source) {
            return new PushdbModel(source);
        }

        @Override
        public PushdbModel[] newArray(int size) {
            return new PushdbModel[size];
        }
    };
}
