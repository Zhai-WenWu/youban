package cn.bjhdltcdn.p2plive.model;


import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by xiawenquan on 17/11/7.
 * 用户对象基本信息
 */

@Entity
public class BaseUser implements Parcelable {
    @Id(autoincrement = false)
    private long userId;//用户标识id,
    private String userName;//用户名,
    private String nickName;//用户昵称,
    private String userIcon;//头像,
    private int sex;//性别 1-->男性，2-->女性，
    @Transient
    private String userBigIcon;//头像大图,
    //    @Transient
    private int age;//:年龄,
    @Transient
    private String distance;//离我距离,
    //    @Transient
    private String location;//所在地,
    @Transient
    private int isAttention;//关注状态(0-->未关注,1-->已关注),
    //    @Transient
    private int isSchoolmate;//是否是校友(1否2是),
    private int userLevel;//用户等级
    //    @Transient
    private String city;//用户城市
    @Transient
    private boolean check;//是否选中
    //    @Transient
    private String schoolName;//学校名称

    private String phoneNumber;//手机号
    @Transient
    private int isClert;
    @Transient
    private int isAuth;//当前用户是否已通过实名认证(0 已通过,1未通过),



    public int getIsClert() {
        return isClert;
    }

    public void setIsClert(int isClert) {
        this.isClert = isClert;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public int getIsSchoolmate() {
        return isSchoolmate;
    }

    public void setIsSchoolmate(int isSchoolmate) {
        this.isSchoolmate = isSchoolmate;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isCheck() {
        return check;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(int isAuth) {
        this.isAuth = isAuth;
    }

    public BaseUser() {
    }


    @Generated(hash = 542155370)
    public BaseUser(long userId, String userName, String nickName, String userIcon, int sex,
            int age, String location, int isSchoolmate, int userLevel, String city,
            String schoolName, String phoneNumber) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.userIcon = userIcon;
        this.sex = sex;
        this.age = age;
        this.location = location;
        this.isSchoolmate = isSchoolmate;
        this.userLevel = userLevel;
        this.city = city;
        this.schoolName = schoolName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.nickName);
        dest.writeString(this.userIcon);
        dest.writeInt(this.sex);
        dest.writeString(this.userBigIcon);
        dest.writeInt(this.age);
        dest.writeString(this.distance);
        dest.writeString(this.location);
        dest.writeInt(this.isAttention);
        dest.writeInt(this.isSchoolmate);
        dest.writeInt(this.userLevel);
        dest.writeString(this.city);
        dest.writeByte(this.check ? (byte) 1 : (byte) 0);
        dest.writeString(this.schoolName);
        dest.writeString(this.phoneNumber);
        dest.writeInt(this.isClert);
        dest.writeInt(this.isAuth);
    }

    protected BaseUser(Parcel in) {
        this.userId = in.readLong();
        this.userName = in.readString();
        this.nickName = in.readString();
        this.userIcon = in.readString();
        this.sex = in.readInt();
        this.userBigIcon = in.readString();
        this.age = in.readInt();
        this.distance = in.readString();
        this.location = in.readString();
        this.isAttention = in.readInt();
        this.isSchoolmate = in.readInt();
        this.userLevel = in.readInt();
        this.city = in.readString();
        this.check = in.readByte() != 0;
        this.schoolName = in.readString();
        this.phoneNumber = in.readString();
        this.isClert = in.readInt();
        this.isAuth = in.readInt();
    }

    public static final Creator<BaseUser> CREATOR = new Creator<BaseUser>() {
        @Override
        public BaseUser createFromParcel(Parcel source) {
            return new BaseUser(source);
        }

        @Override
        public BaseUser[] newArray(int size) {
            return new BaseUser[size];
        }
    };
}
