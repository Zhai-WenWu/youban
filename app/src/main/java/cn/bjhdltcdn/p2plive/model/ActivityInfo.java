package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Hu_PC on 2017/11/10.
 * 活动对象
 */

public class ActivityInfo implements Parcelable {
    private BaseUser baseUser;//{BaseUser}组织者基本信息对象,
    private long userId;//组织者Id,
    private long activityId;//活动Id,
    private String theme;//活动主题/简介
    private List<Image> imageList;//[{Image对象},{Image对象}...]图片列表,
    private List<HobbyInfo> hobbyList;//[{HobbyInfo},{HobbyInfo}...]活动类型列表,
    private ActivityLocationInfo locationInfo;//{ActivityLocationInfo}活动位置对象,
    private String activityTime;//活动时间,
    private int activityNumber;//活动人数,
    private int activityPrice;//活动人均价格,
    private int status;//活动状态(1报名中,2报名结束),
    private int type;//活动类型(1--->线下，2--->线上),
    private String distance;//离我距离,
    private int userRole;//用户角色(1组织用户,2普通用户)用户不在活动中返回0,
    private int isAllowedJoin;//用户不在活动中是否允许加入(1允许,2不允许),
    private int joinNumber;//参加人数,
    private int isDel;//是否删除(0--->未删除，1--->删除),
    private int personnelLimits;//人员范围限制(0--->不限,1--->限制),
    private List<OrganizationInfo> organList;//[{OrganizationInfo},{OrganizationInfo}...]活动关联的圈子列表,
    private String activeLevel;//发帖用户在圈子的活跃等级(字符串)
    private int isTop;//是否置顶(0取消,1置顶)
    private String addTime;//添加时间,
    private int sexLimit;//性别限制(0不限,1仅限男生,2仅限女生),
    private int totalMen;//男的总数,
    private int totalWomen;//女的总数,
    private BaseUser toBaseUser;//分享关注使用,
    private int isSendInvitation;//是否可以发送邀请函(1可以,2不可以),
    private int isSendMaxNumber;//是否超出发送次数(1未超出,2已超出),
    private boolean check;//是否被选中


    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public List<HobbyInfo> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<HobbyInfo> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public ActivityLocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(ActivityLocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public int getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(int activityNumber) {
        this.activityNumber = activityNumber;
    }

    public int getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(int activityPrice) {
        this.activityPrice = activityPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getJoinNumber() {
        return joinNumber;
    }

    public void setJoinNumber(int joinNumber) {
        this.joinNumber = joinNumber;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public int getPersonnelLimits() {
        return personnelLimits;
    }

    public void setPersonnelLimits(int personnelLimits) {
        this.personnelLimits = personnelLimits;
    }

    public List<OrganizationInfo> getOrganList() {
        return organList;
    }

    public void setOrganList(List<OrganizationInfo> organList) {
        this.organList = organList;
    }

    public int getIsAllowedJoin() {
        return isAllowedJoin;
    }

    public void setIsAllowedJoin(int isAllowedJoin) {
        this.isAllowedJoin = isAllowedJoin;
    }

    public String getActiveLevel() {
        return activeLevel;
    }

    public void setActiveLevel(String activeLevel) {
        this.activeLevel = activeLevel;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public ActivityInfo() {
    }

    public int getSexLimit() {
        return sexLimit;
    }

    public void setSexLimit(int sexLimit) {
        this.sexLimit = sexLimit;
    }

    public int getTotalMen() {
        return totalMen;
    }

    public void setTotalMen(int totalMen) {
        this.totalMen = totalMen;
    }

    public int getTotalWomen() {
        return totalWomen;
    }

    public void setTotalWomen(int totalWomen) {
        this.totalWomen = totalWomen;
    }

    public BaseUser getToBaseUser() {
        return toBaseUser;
    }

    public void setToBaseUser(BaseUser toBaseUser) {
        this.toBaseUser = toBaseUser;
    }

    public int getIsSendInvitation() {
        return isSendInvitation;
    }

    public void setIsSendInvitation(int isSendInvitation) {
        this.isSendInvitation = isSendInvitation;
    }

    public int getIsSendMaxNumber() {
        return isSendMaxNumber;
    }

    public void setIsSendMaxNumber(int isSendMaxNumber) {
        this.isSendMaxNumber = isSendMaxNumber;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseUser, flags);
        dest.writeLong(this.userId);
        dest.writeLong(this.activityId);
        dest.writeString(this.theme);
        dest.writeTypedList(this.imageList);
        dest.writeTypedList(this.hobbyList);
        dest.writeParcelable(this.locationInfo, flags);
        dest.writeString(this.activityTime);
        dest.writeInt(this.activityNumber);
        dest.writeInt(this.activityPrice);
        dest.writeInt(this.status);
        dest.writeInt(this.type);
        dest.writeString(this.distance);
        dest.writeInt(this.userRole);
        dest.writeInt(this.isAllowedJoin);
        dest.writeInt(this.joinNumber);
        dest.writeInt(this.isDel);
        dest.writeInt(this.personnelLimits);
        dest.writeTypedList(this.organList);
        dest.writeString(this.activeLevel);
        dest.writeInt(this.isTop);
        dest.writeString(this.addTime);
        dest.writeInt(this.sexLimit);
        dest.writeInt(this.totalMen);
        dest.writeInt(this.totalWomen);
        dest.writeParcelable(this.toBaseUser, flags);
        dest.writeInt(this.isSendInvitation);
        dest.writeInt(this.isSendMaxNumber);
        dest.writeByte(this.check ? (byte) 1 : (byte) 0);
    }

    protected ActivityInfo(Parcel in) {
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.userId = in.readLong();
        this.activityId = in.readLong();
        this.theme = in.readString();
        this.imageList = in.createTypedArrayList(Image.CREATOR);
        this.hobbyList = in.createTypedArrayList(HobbyInfo.CREATOR);
        this.locationInfo = in.readParcelable(ActivityLocationInfo.class.getClassLoader());
        this.activityTime = in.readString();
        this.activityNumber = in.readInt();
        this.activityPrice = in.readInt();
        this.status = in.readInt();
        this.type = in.readInt();
        this.distance = in.readString();
        this.userRole = in.readInt();
        this.isAllowedJoin = in.readInt();
        this.joinNumber = in.readInt();
        this.isDel = in.readInt();
        this.personnelLimits = in.readInt();
        this.organList = in.createTypedArrayList(OrganizationInfo.CREATOR);
        this.activeLevel = in.readString();
        this.isTop = in.readInt();
        this.addTime = in.readString();
        this.sexLimit = in.readInt();
        this.totalMen = in.readInt();
        this.totalWomen = in.readInt();
        this.toBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.isSendInvitation = in.readInt();
        this.isSendMaxNumber = in.readInt();
        this.check = in.readByte() != 0;
    }

    public static final Creator<ActivityInfo> CREATOR = new Creator<ActivityInfo>() {
        @Override
        public ActivityInfo createFromParcel(Parcel source) {
            return new ActivityInfo(source);
        }

        @Override
        public ActivityInfo[] newArray(int size) {
            return new ActivityInfo[size];
        }
    };
}
