package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatBaseUser extends BaseUser implements Parcelable{
    private String anonymityId;
    /**
     * //所属角色(1--->创建者,2--->场控(管理员),3--->观众)
     */
    private int userRole;
    /**
     * 是否为好友 1 是 2 否
     */
    private int isFriend;

    public String getAnonymityId() {
        return anonymityId;
    }

    public void setAnonymityId(String anonymityId) {
        this.anonymityId = anonymityId;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public ChatBaseUser() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.anonymityId);
        dest.writeInt(this.userRole);
        dest.writeInt(this.isFriend);
    }

    protected ChatBaseUser(Parcel in) {
        super(in);
        this.anonymityId = in.readString();
        this.userRole = in.readInt();
        this.isFriend = in.readInt();
    }

    public static final Creator<ChatBaseUser> CREATOR = new Creator<ChatBaseUser>() {
        @Override
        public ChatBaseUser createFromParcel(Parcel source) {
            return new ChatBaseUser(source);
        }

        @Override
        public ChatBaseUser[] newArray(int size) {
            return new ChatBaseUser[size];
        }
    };
}
