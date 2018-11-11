package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ZHAI on 2018/3/5.
 */

public class UserInfo implements Parcelable {

    public int isAttention;
    public int videoStatus;
    public List<HobbyInfo> hobbyInfo;
    public String birthday;
    public UserSchoolInfo userSchoolInfo;
    public int sex;
    public String location;
    public int state;
    public String userIcon;
    public String userBigIcon;
    public String fromChannel;
    public String totalUseTime;
    public String password;
    public String registerTime;
    public int userType;
    public int isSchoolmate;
    public String nickName;
    public int age;
    public int userLevel;
    public long userId;
    public String userName;
    public int authStatus;
    public String signature;
    public String lastLoginTime;
    public UserOccupationRecordInfo userOccupationRecord;

    public UserOccupationRecordInfo getUserOccupationRecord() {
        return userOccupationRecord;
    }

    public void setUserOccupationRecord(UserOccupationRecordInfo userOccupationRecord) {
        this.userOccupationRecord = userOccupationRecord;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public int getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(int videoStatus) {
        this.videoStatus = videoStatus;
    }

    public List<HobbyInfo> getHobbyInfo() {
        return hobbyInfo;
    }

    public void setHobbyInfo(List<HobbyInfo> hobbyInfo) {
        this.hobbyInfo = hobbyInfo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public UserSchoolInfo getUserSchoolInfo() {
        return userSchoolInfo;
    }

    public void setUserSchoolInfo(UserSchoolInfo userSchoolInfo) {
        this.userSchoolInfo = userSchoolInfo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserBigIcon() {
        return userBigIcon;
    }

    public void setUserBigIcon(String userBigIcon) {
        this.userBigIcon = userBigIcon;
    }

    public String getFromChannel() {
        return fromChannel;
    }

    public void setFromChannel(String fromChannel) {
        this.fromChannel = fromChannel;
    }

    public String getTotalUseTime() {
        return totalUseTime;
    }

    public void setTotalUseTime(String totalUseTime) {
        this.totalUseTime = totalUseTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getIsSchoolmate() {
        return isSchoolmate;
    }

    public void setIsSchoolmate(int isSchoolmate) {
        this.isSchoolmate = isSchoolmate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isAttention);
        dest.writeInt(this.videoStatus);
        dest.writeTypedList(this.hobbyInfo);
        dest.writeString(this.birthday);
        dest.writeParcelable(this.userSchoolInfo, flags);
        dest.writeInt(this.sex);
        dest.writeString(this.location);
        dest.writeInt(this.state);
        dest.writeString(this.userIcon);
        dest.writeString(this.userBigIcon);
        dest.writeString(this.fromChannel);
        dest.writeString(this.totalUseTime);
        dest.writeString(this.password);
        dest.writeString(this.registerTime);
        dest.writeInt(this.userType);
        dest.writeInt(this.isSchoolmate);
        dest.writeString(this.nickName);
        dest.writeInt(this.age);
        dest.writeInt(this.userLevel);
        dest.writeLong(this.userId);
        dest.writeString(this.userName);
        dest.writeInt(this.authStatus);
        dest.writeString(this.signature);
        dest.writeString(this.lastLoginTime);
        dest.writeParcelable(this.userOccupationRecord, flags);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.isAttention = in.readInt();
        this.videoStatus = in.readInt();
        this.hobbyInfo = in.createTypedArrayList(HobbyInfo.CREATOR);
        this.birthday = in.readString();
        this.userSchoolInfo = in.readParcelable(SchoolInfo.class.getClassLoader());
        this.sex = in.readInt();
        this.location = in.readString();
        this.state = in.readInt();
        this.userIcon = in.readString();
        this.userBigIcon = in.readString();
        this.fromChannel = in.readString();
        this.totalUseTime = in.readString();
        this.password = in.readString();
        this.registerTime = in.readString();
        this.userType = in.readInt();
        this.isSchoolmate = in.readInt();
        this.nickName = in.readString();
        this.age = in.readInt();
        this.userLevel = in.readInt();
        this.userId = in.readLong();
        this.userName = in.readString();
        this.authStatus = in.readInt();
        this.signature = in.readString();
        this.lastLoginTime = in.readString();
        this.userOccupationRecord = in.readParcelable(UserOccupationRecordInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
