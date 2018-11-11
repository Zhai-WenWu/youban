package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 多人视频礼物展示model
 */

public class GroupVideoGiftShowModel implements Parcelable {
    private Props props;
    private BaseUser receiveBaseUser;
    private BaseUser sendBaseUser;

    public GroupVideoGiftShowModel(Props props, BaseUser receiveBaseUser, BaseUser sendBaseUser) {
        this.props = props;
        this.receiveBaseUser = receiveBaseUser;
        this.sendBaseUser = sendBaseUser;
    }

    public Props getProps() {
        return props;
    }

    public void setProps(Props props) {
        this.props = props;
    }

    public BaseUser getReceiveBaseUser() {
        return receiveBaseUser;
    }

    public void setReceiveBaseUser(BaseUser receiveBaseUser) {
        this.receiveBaseUser = receiveBaseUser;
    }

    public BaseUser getSendBaseUser() {
        return sendBaseUser;
    }

    public void setSendBaseUser(BaseUser sendBaseUser) {
        this.sendBaseUser = sendBaseUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.props, 0);
        dest.writeParcelable(this.receiveBaseUser, 0);
        dest.writeParcelable(this.sendBaseUser, 0);
    }

    public GroupVideoGiftShowModel() {
    }

    protected GroupVideoGiftShowModel(Parcel in) {
        this.props = in.readParcelable(Props.class.getClassLoader());
        this.receiveBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.sendBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
    }

    public static final Creator<GroupVideoGiftShowModel> CREATOR = new Creator<GroupVideoGiftShowModel>() {
        public GroupVideoGiftShowModel createFromParcel(Parcel source) {
            return new GroupVideoGiftShowModel(source);
        }

        public GroupVideoGiftShowModel[] newArray(int size) {
            return new GroupVideoGiftShowModel[size];
        }
    };
}
