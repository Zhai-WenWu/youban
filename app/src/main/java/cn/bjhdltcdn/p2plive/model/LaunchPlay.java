package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHUDI on 2017/12/15.
 * 发起PK挑战对象
 */

public class LaunchPlay implements Parcelable {
    /*  "launchId":发起Id,
    "userId":用户Id,
    "title":标题,
    "description":描述,
    "addTime":添加时间,
    "type":发起类型(1官方,2用户),
    "category":PK类别(1全民活动,2PK挑战),
    "gotoUrl":跳转链接,
    "imgUrl":图片路径,
    "baseUser":{BaseUser}发起用户基本信息,*/
    private long launchId;//发起Id,
    private long userId;//用户Id,
    private String title;//标题,
    private String description;//描述,
    private String addTime;//添加时间,
    private int status;

    public int getTotalNumber() {
        return totalNumber;
    }

    private int totalNumber;//参与PK的总人数,
    private int type;//发起类型(1官方,2用户),
    private int category;//PK类别(1全民活动,2PK挑战),
    private String gotoUrl;//跳转链接,
    private String imgUrl;//图片路径,
    private BaseUser baseUser;//参与PK的总人数,
    private List<PlayInfo> playList;//PK挑战列表,

    public long getLaunchId() {
        return launchId;
    }

    public void setLaunchId(long launchId) {
        this.launchId = launchId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getGotoUrl() {
        return gotoUrl;
    }

    public void setGotoUrl(String gotoUrl) {
        this.gotoUrl = gotoUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public List<PlayInfo> getPlayList() {
        return playList;
    }

    public void setPlayList(List<PlayInfo> playList) {
        this.playList = playList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.launchId);
        dest.writeLong(this.userId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.addTime);
        dest.writeInt(this.status);
        dest.writeInt(this.totalNumber);
        dest.writeInt(this.type);
        dest.writeInt(this.category);
        dest.writeString(this.gotoUrl);
        dest.writeString(this.imgUrl);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeTypedList(this.playList);
    }

    public LaunchPlay() {
    }

    protected LaunchPlay(Parcel in) {
        this.launchId = in.readLong();
        this.userId = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.addTime = in.readString();
        this.status = in.readInt();
        this.totalNumber = in.readInt();
        this.type = in.readInt();
        this.category = in.readInt();
        this.gotoUrl = in.readString();
        this.imgUrl = in.readString();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.playList = in.createTypedArrayList(PlayInfo.CREATOR);
    }

    public static final Creator<LaunchPlay> CREATOR = new Creator<LaunchPlay>() {
        @Override
        public LaunchPlay createFromParcel(Parcel source) {
            return new LaunchPlay(source);
        }

        @Override
        public LaunchPlay[] newArray(int size) {
            return new LaunchPlay[size];
        }
    };
}
