package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiawenquan on 17/11/7.
 */

public class User extends BaseUser implements Parcelable {
    private String password;//密码,
    private String signature;//个性签名,
    private String birthday;//生日,

    private RoomInfo roomInfo;//开启房间,

    private int userType;//用户类型(1--->使用手机号注册，2---->QQ，3--->微信，4--->微博),
    private double userBalance;//用户余额,
    private List<Image> imageList;//[{Image对象},{Image对象}...]用户相册,
    private OccupationInfo occupationInfo;//职业对象,
    private SchoolInfo schoolInfo;//学校对象,
    private List<HobbyInfo> hobbyList;//[{HobbyInfo},{HobbyInfo}...]兴趣爱好,
    private List<SayLoveInfo> sayLoveList;//[{HobbyInfo},{HobbyInfo}...]兴趣爱好,
    private int attentionCount;//关注数,
    private int fansCount;//粉丝数,
    private List<OrganizationInfo> organList;//[{OrganizationInfo对象},{OrganizationInfo对象}...]参加的圈子,
    private List<Group> groupList;//[{Group对象},{Group对象}...]加入的群组,
    private List<PlayInfo> pkList;//{PlayInfo对象}...]参与的PK挑战,

    public List<PlayInfo> getPkList() {
        return pkList;
    }

    public void setPkList(List<PlayInfo> pkList) {
        this.pkList = pkList;
    }

    private List<ActivityInfo> activeList;//[{ActivityInfo对象},{ActivityInfo对象}...]参加的活动,
    private List<PostInfo> postList;//[{PostInfo对象},{PostInfo对象}...]发布的帖子,
    private RoomBaseUser roomBaseUser;//{RoomBaseUser}房间用户对象信息,
    private int sendGoldCount;//送出金币数,
    private int receiverGoldCount;//收到金币数,
    private int isSchoolmate;//是否是校友(1否2是),isSchoolmate

    private int authStatus;//实名认证状态(0未认证,1等待审核,2已认证,3审核失败)
    /**
     * 匿名昵称
     */
    private String anonymityName;

    @Override
    public int getIsSchoolmate() {
        return isSchoolmate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public List<SayLoveInfo> getSayLoveList() {
        return sayLoveList;
    }

    public void setSayLoveList(List<SayLoveInfo> sayLoveList) {
        this.sayLoveList = sayLoveList;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    public double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(double userBalance) {
        this.userBalance = userBalance;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public OccupationInfo getOccupationInfo() {
        return occupationInfo;
    }


    public void setOccupationInfo(OccupationInfo occupationInfo) {
        this.occupationInfo = occupationInfo;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public List<HobbyInfo> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<HobbyInfo> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public int getAttentionCount() {
        return attentionCount;
    }

    public void setAttentionCount(int attentionCount) {
        this.attentionCount = attentionCount;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public List<OrganizationInfo> getOrganList() {
        return organList;
    }

    public void setOrganList(List<OrganizationInfo> organList) {
        this.organList = organList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<ActivityInfo> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<ActivityInfo> activeList) {
        this.activeList = activeList;
    }

    public List<PostInfo> getPostList() {
        return postList;
    }

    public void setPostList(List<PostInfo> postList) {
        this.postList = postList;
    }


    public RoomBaseUser getRoomBaseUser() {
        return roomBaseUser;
    }

    public void setRoomBaseUser(RoomBaseUser roomBaseUser) {
        this.roomBaseUser = roomBaseUser;
    }

    public int getSendGoldCount() {
        return sendGoldCount;
    }

    public void setSendGoldCount(int sendGoldCount) {
        this.sendGoldCount = sendGoldCount;
    }

    public int getReceiverGoldCount() {
        return receiverGoldCount;
    }

    public void setReceiverGoldCount(int receiverGoldCount) {
        this.receiverGoldCount = receiverGoldCount;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAnonymityName() {
        return anonymityName;
    }

    public void setAnonymityName(String anonymityName) {
        this.anonymityName = anonymityName;
    }

    public User() {
    }


    @Override
    public void setIsSchoolmate(int isSchoolmate) {
        this.isSchoolmate = isSchoolmate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.password);
        dest.writeString(this.signature);
        dest.writeString(this.birthday);
        dest.writeParcelable(this.roomInfo, flags);
        dest.writeInt(this.userType);
        dest.writeDouble(this.userBalance);
        dest.writeTypedList(this.imageList);
        dest.writeParcelable(this.occupationInfo, flags);
        dest.writeParcelable(this.schoolInfo, flags);
        dest.writeTypedList(this.hobbyList);
        dest.writeTypedList(this.sayLoveList);
        dest.writeInt(this.attentionCount);
        dest.writeInt(this.fansCount);
        dest.writeTypedList(this.organList);
        dest.writeTypedList(this.groupList);
        dest.writeTypedList(this.pkList);
        dest.writeTypedList(this.activeList);
        dest.writeTypedList(this.postList);
        dest.writeParcelable(this.roomBaseUser, flags);
        dest.writeInt(this.sendGoldCount);
        dest.writeInt(this.receiverGoldCount);
        dest.writeInt(this.isSchoolmate);
        dest.writeInt(this.authStatus);
        dest.writeString(this.anonymityName);
    }

    protected User(Parcel in) {
        super(in);
        this.password = in.readString();
        this.signature = in.readString();
        this.birthday = in.readString();
        this.roomInfo = in.readParcelable(RoomInfo.class.getClassLoader());
        this.userType = in.readInt();
        this.userBalance = in.readDouble();
        this.imageList = in.createTypedArrayList(Image.CREATOR);
        this.occupationInfo = in.readParcelable(OccupationInfo.class.getClassLoader());
        this.schoolInfo = in.readParcelable(SchoolInfo.class.getClassLoader());
        this.hobbyList = in.createTypedArrayList(HobbyInfo.CREATOR);
        this.sayLoveList = in.createTypedArrayList(SayLoveInfo.CREATOR);
        this.attentionCount = in.readInt();
        this.fansCount = in.readInt();
        this.organList = in.createTypedArrayList(OrganizationInfo.CREATOR);
        this.groupList = in.createTypedArrayList(Group.CREATOR);
        this.pkList = in.createTypedArrayList(PlayInfo.CREATOR);
        this.activeList = in.createTypedArrayList(ActivityInfo.CREATOR);
        this.postList = in.createTypedArrayList(PostInfo.CREATOR);
        this.roomBaseUser = in.readParcelable(RoomBaseUser.class.getClassLoader());
        this.sendGoldCount = in.readInt();
        this.receiverGoldCount = in.readInt();
        this.isSchoolmate = in.readInt();
        this.authStatus = in.readInt();
        this.anonymityName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
