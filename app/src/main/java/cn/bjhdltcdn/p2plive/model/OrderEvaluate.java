package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hu_PC on 2018/5/11.
 */

public class OrderEvaluate implements Parcelable {
    private long evalId;//评价Id,
    private long userId;//评级用户Id,
    private BaseUser baseUser;//{BaseUser}用户基本信息,
    private long evalScore;//评价分数,
    private String addTime;//评价时间,
    private String content;//评论/回复内容",
    private long type;//内容类型(1--->评论,2--->回复),
    private BaseUser toBaseUser;//{BaseUser}被回复人,
    private BaseUser fromBaseUser;//{BaseUser}回复人,
    private long status;//状态(0--->未删除，1--->删除),
    private long praiseCount;//点赞总数,
    private String praiseCountStr;//转译点赞总数(字符串),
    private long commentType;//评论/回复类型(0-->普通文本,1-->图/图文,2--->视频),
    private String videoUrl;//评论/回复视频路径(类型为视频使用),
    private String videoImageUrl;//评论/回复图片地址(类型为视频使用),
    private List<Image> imageList;//[{Image对象},{Image对象}...]评论回复相册,
    private List<Comment> replyList;//[{Comment},{Comment}...]回复列表,
    private long replyCount;//回复总条数,
    private String replyCountStr;//转译回复总条数(字符串),
    private long commentParentId;//楼中楼的父评论id,
    private long isPraise;//当前用户是否点赞该评论回复(0未点赞 1 点赞),
    private long parentId;//上一条评论/回复Id(评论时0),

    public OrderEvaluate() {
    }

    public OrderEvaluate(long evalId, long userId, BaseUser baseUser, long evalScore, String addTime, String content, long type, BaseUser toBaseUser, BaseUser fromBaseUser, long status, long praiseCount, String praiseCountStr, long commentType, String videoUrl, String videoImageUrl, List<Image> imageList, List<Comment> replyList, long replyCount, String replyCountStr, long commentParentId, long isPraise, long parentId) {
        this.evalId = evalId;
        this.userId = userId;
        this.baseUser = baseUser;
        this.evalScore = evalScore;
        this.addTime = addTime;
        this.content = content;
        this.type = type;
        this.toBaseUser = toBaseUser;
        this.fromBaseUser = fromBaseUser;
        this.status = status;
        this.praiseCount = praiseCount;
        this.praiseCountStr = praiseCountStr;
        this.commentType = commentType;
        this.videoUrl = videoUrl;
        this.videoImageUrl = videoImageUrl;
        this.imageList = imageList;
        this.replyList = replyList;
        this.replyCount = replyCount;
        this.replyCountStr = replyCountStr;
        this.commentParentId = commentParentId;
        this.isPraise = isPraise;
        this.parentId = parentId;
    }

    public long getEvalId() {
        return evalId;
    }

    public void setEvalId(long evalId) {
        this.evalId = evalId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public long getEvalScore() {
        return evalScore;
    }

    public void setEvalScore(long evalScore) {
        this.evalScore = evalScore;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public BaseUser getToBaseUser() {
        return toBaseUser;
    }

    public void setToBaseUser(BaseUser toBaseUser) {
        this.toBaseUser = toBaseUser;
    }

    public BaseUser getFromBaseUser() {
        return fromBaseUser;
    }

    public void setFromBaseUser(BaseUser fromBaseUser) {
        this.fromBaseUser = fromBaseUser;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(long praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getPraiseCountStr() {
        return praiseCountStr;
    }

    public void setPraiseCountStr(String praiseCountStr) {
        this.praiseCountStr = praiseCountStr;
    }

    public long getCommentType() {
        return commentType;
    }

    public void setCommentType(long commentType) {
        this.commentType = commentType;
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

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public List<Comment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Comment> replyList) {
        this.replyList = replyList;
    }

    public long getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(long replyCount) {
        this.replyCount = replyCount;
    }

    public String getReplyCountStr() {
        return replyCountStr;
    }

    public void setReplyCountStr(String replyCountStr) {
        this.replyCountStr = replyCountStr;
    }

    public long getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(long commentParentId) {
        this.commentParentId = commentParentId;
    }

    public long getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(long isPraise) {
        this.isPraise = isPraise;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.evalId);
        dest.writeLong(this.userId);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeLong(this.evalScore);
        dest.writeString(this.addTime);
        dest.writeString(this.content);
        dest.writeLong(this.type);
        dest.writeParcelable(this.toBaseUser, flags);
        dest.writeParcelable(this.fromBaseUser, flags);
        dest.writeLong(this.status);
        dest.writeLong(this.praiseCount);
        dest.writeString(this.praiseCountStr);
        dest.writeLong(this.commentType);
        dest.writeString(this.videoUrl);
        dest.writeString(this.videoImageUrl);
        dest.writeTypedList(this.imageList);
        dest.writeList(this.replyList);
        dest.writeLong(this.replyCount);
        dest.writeString(this.replyCountStr);
        dest.writeLong(this.commentParentId);
        dest.writeLong(this.isPraise);
        dest.writeLong(this.parentId);
    }

    protected OrderEvaluate(Parcel in) {
        this.evalId = in.readLong();
        this.userId = in.readLong();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.evalScore = in.readLong();
        this.addTime = in.readString();
        this.content = in.readString();
        this.type = in.readLong();
        this.toBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.fromBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.status = in.readLong();
        this.praiseCount = in.readLong();
        this.praiseCountStr = in.readString();
        this.commentType = in.readLong();
        this.videoUrl = in.readString();
        this.videoImageUrl = in.readString();
        this.imageList = in.createTypedArrayList(Image.CREATOR);
        this.replyList = new ArrayList<Comment>();
        in.readList(this.replyList, Comment.class.getClassLoader());
        this.replyCount = in.readLong();
        this.replyCountStr = in.readString();
        this.commentParentId = in.readLong();
        this.isPraise = in.readLong();
        this.parentId = in.readLong();
    }

    public static final Parcelable.Creator<OrderEvaluate> CREATOR = new Parcelable.Creator<OrderEvaluate>() {
        @Override
        public OrderEvaluate createFromParcel(Parcel source) {
            return new OrderEvaluate(source);
        }

        @Override
        public OrderEvaluate[] newArray(int size) {
            return new OrderEvaluate[size];
        }
    };
}
