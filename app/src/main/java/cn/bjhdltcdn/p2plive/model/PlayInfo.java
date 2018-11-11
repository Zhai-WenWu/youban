package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Hu_PC on 2017/12/15.
 */

public class PlayInfo implements Parcelable {
    private long playId;//挑战Id,
    private long userId;//用户Id,
    private String title;//挑战标题,
    private String videoUrl;//挑战视频路径,
    private String videoImageUrl;//挑战图片地址,
    private int type;//PK类型(1发起,2参与),
    private String addTime;//添加时间,
    private int praiseCount;//点赞总数,
    private int shareNumber;//点赞总数,
    private int commentCount;//评论回复总数,
    private int userRole;//用户角色(1-->自己,2-->发起用户,3-->其他),
    private int isAttention;//关注状态(0-->未关注,1-->已关注),
    private LaunchPlay launchPlay;//{LaunchPlay}发起PK对象,
    private int isTop;//是否置顶(0取消,1置顶),
    private int isPraise;//当前用户是否点赞该帖子(0未点赞 1 点赞),
    private BaseUser baseUser;//{BaseUser}挑战用户基本信息,
    private String activeLevel;//发帖用户在圈子的活跃等级(字符串),
    private int ranking;//pk挑战排名,
    private BaseUser toBaseUser;//分享关注使用,
    private int status;//"status":状态(0--->未删除，1--->删除),
    private String commentCountStr;//转译评论回复总数(字符串),
    private String shareNumberStr;//转译分享次数(字符串),

    public int getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(int shareNumber) {
        this.shareNumber = shareNumber;
    }

    public long getPlayId() {
        return playId;
    }

    public void setPlayId(long playId) {
        this.playId = playId;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public LaunchPlay getLaunchPlay() {
        return launchPlay;
    }

    public void setLaunchPlay(LaunchPlay launchPlay) {
        this.launchPlay = launchPlay;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public String getActiveLevel() {
        return activeLevel;
    }

    public void setActiveLevel(String activeLevel) {
        this.activeLevel = activeLevel;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public BaseUser getToBaseUser() {
        return toBaseUser;
    }

    public void setToBaseUser(BaseUser toBaseUser) {
        this.toBaseUser = toBaseUser;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCommentCountStr() {
        return commentCountStr;
    }

    public void setCommentCountStr(String commentCountStr) {
        this.commentCountStr = commentCountStr;
    }

    public String getShareNumberStr() {
        return shareNumberStr;
    }

    public void setShareNumberStr(String shareNumberStr) {
        this.shareNumberStr = shareNumberStr;
    }

    public PlayInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.playId);
        dest.writeLong(this.userId);
        dest.writeString(this.title);
        dest.writeString(this.videoUrl);
        dest.writeString(this.videoImageUrl);
        dest.writeInt(this.type);
        dest.writeString(this.addTime);
        dest.writeInt(this.praiseCount);
        dest.writeInt(this.shareNumber);
        dest.writeInt(this.commentCount);
        dest.writeInt(this.userRole);
        dest.writeInt(this.isAttention);
        dest.writeParcelable(this.launchPlay, flags);
        dest.writeInt(this.isTop);
        dest.writeInt(this.isPraise);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeString(this.activeLevel);
        dest.writeInt(this.ranking);
        dest.writeParcelable(this.toBaseUser, flags);
        dest.writeInt(this.status);
        dest.writeString(this.commentCountStr);
        dest.writeString(this.shareNumberStr);
    }

    protected PlayInfo(Parcel in) {
        this.playId = in.readLong();
        this.userId = in.readLong();
        this.title = in.readString();
        this.videoUrl = in.readString();
        this.videoImageUrl = in.readString();
        this.type = in.readInt();
        this.addTime = in.readString();
        this.praiseCount = in.readInt();
        this.shareNumber = in.readInt();
        this.commentCount = in.readInt();
        this.userRole = in.readInt();
        this.isAttention = in.readInt();
        this.launchPlay = in.readParcelable(LaunchPlay.class.getClassLoader());
        this.isTop = in.readInt();
        this.isPraise = in.readInt();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.activeLevel = in.readString();
        this.ranking = in.readInt();
        this.toBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.status = in.readInt();
        this.commentCountStr = in.readString();
        this.shareNumberStr = in.readString();
    }

    public static final Creator<PlayInfo> CREATOR = new Creator<PlayInfo>() {
        @Override
        public PlayInfo createFromParcel(Parcel source) {
            return new PlayInfo(source);
        }

        @Override
        public PlayInfo[] newArray(int size) {
            return new PlayInfo[size];
        }
    };
}
