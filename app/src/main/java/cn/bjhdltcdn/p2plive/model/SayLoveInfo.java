package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hu_PC on 2017/11/13.
 * 表白对象
 */

public class SayLoveInfo implements Parcelable {
    private BaseUser baseUser;//{BaseUser}基本信息对象,
    private long sayLoveId;//主键Id,
    private String title;//表白标题,
    private String content;//表白内容,
    private List<Image> imageList;//[{Image对象},{Image对象}...]图片列表,
    private int isAnonymous;//是否匿名(1匿名,2不匿名),
    private int confessionType;//表白类型(1-->普通图文，2--->视频),
    private String videoUrl;//表白视频路径(类型为视频使用),
    private String videoImageUrl;//表白图片地址(类型为视频使用),
    private String addTime;//表白时间,
    private int praiseCount;//表白点赞总数,
    private int commentCount;//表白评论回复总数,
    private List<Comment> commentList;//[{Comment},{Comment}...]表白评论列表,
    private String distance;//离我距离,
    private int userRole;//当前操作用户的角色(1发布用户,2普通用户)
    private int isPraise;//当前用户是否点赞该表白(0未点赞 1 点赞),
    private int shareNumber;//分享次数,
    private BaseUser toBaseUser;//分享关注使用,
    private String praiseCountStr;//转译点赞总数(字符串),
    private String commentCountStr;//转译评论回复总数(字符串),
    private String shareNumberStr;//转译分享次数(字符串),
    private int status;//表白状态(0--->未删除，1---->删除),
    private int isOriginal;//是否原表白(0--->原表白，1--->跟表白),
    private long parentId;//原表白Id,
    private int followCount;//跟拍视频总数,
    private List<SayLoveInfo> followList ;//[{SayLoveInfo对象},{SayLoveInfo对象}...]跟拍表白列表,
    private OriginalInfo originalInfo;//":{OriginalInfo对象}原贴信息,
    private List<OrganBaseInfo> organList;// 帖子关联的圈子列表,
    private String labelName;//标签名称,
    private long labelId;//标签id
    private List<String> scondlabelList;// 二级标签列表
    private int ranking;// 排名,

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public List<OrganBaseInfo> getOrganList() {
        return organList;
    }

    public void setOrganList(List<OrganBaseInfo> organList) {
        this.organList = organList;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public long getSayLoveId() {
        return sayLoveId;
    }

    public void setSayLoveId(long sayLoveId) {
        this.sayLoveId = sayLoveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
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

    public int getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public int getConfessionType() {
        return confessionType;
    }

    public void setConfessionType(int confessionType) {
        this.confessionType = confessionType;
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

    public int getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(int shareNumber) {
        this.shareNumber = shareNumber;
    }

    public BaseUser getToBaseUser() {
        return toBaseUser;
    }

    public void setToBaseUser(BaseUser toBaseUser) {
        this.toBaseUser = toBaseUser;
    }


    public String getPraiseCountStr() {
        return praiseCountStr;
    }

    public void setPraiseCountStr(String praiseCountStr) {
        this.praiseCountStr = praiseCountStr;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsOriginal() {
        return isOriginal;
    }

    public void setIsOriginal(int isOriginal) {
        this.isOriginal = isOriginal;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public List<SayLoveInfo> getFollowList() {
        return followList;
    }

    public void setFollowList(List<SayLoveInfo> followList) {
        this.followList = followList;
    }


    public OriginalInfo getOriginalInfo() {
        return originalInfo;
    }

    public void setOriginalInfo(OriginalInfo originalInfo) {
        this.originalInfo = originalInfo;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public long getLabelId() {
        return labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    public List<String> getScondlabelList() {
        return scondlabelList;
    }

    public void setScondlabelList(List<String> scondlabelList) {
        this.scondlabelList = scondlabelList;
    }

    public SayLoveInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseUser, flags);
        dest.writeLong(this.sayLoveId);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeTypedList(this.imageList);
        dest.writeInt(this.isAnonymous);
        dest.writeInt(this.confessionType);
        dest.writeString(this.videoUrl);
        dest.writeString(this.videoImageUrl);
        dest.writeString(this.addTime);
        dest.writeInt(this.praiseCount);
        dest.writeInt(this.commentCount);
        dest.writeList(this.commentList);
        dest.writeString(this.distance);
        dest.writeInt(this.userRole);
        dest.writeInt(this.isPraise);
        dest.writeInt(this.shareNumber);
        dest.writeParcelable(this.toBaseUser, flags);
        dest.writeString(this.praiseCountStr);
        dest.writeString(this.commentCountStr);
        dest.writeString(this.shareNumberStr);
        dest.writeInt(this.status);
        dest.writeInt(this.isOriginal);
        dest.writeLong(this.parentId);
        dest.writeInt(this.followCount);
        dest.writeTypedList(this.followList);
        dest.writeParcelable(this.originalInfo, flags);
        dest.writeTypedList(this.organList);
        dest.writeString(this.labelName);
        dest.writeLong(this.labelId);
        dest.writeStringList(this.scondlabelList);
        dest.writeInt(this.ranking);
    }

    protected SayLoveInfo(Parcel in) {
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.sayLoveId = in.readLong();
        this.title = in.readString();
        this.content = in.readString();
        this.imageList = in.createTypedArrayList(Image.CREATOR);
        this.isAnonymous = in.readInt();
        this.confessionType = in.readInt();
        this.videoUrl = in.readString();
        this.videoImageUrl = in.readString();
        this.addTime = in.readString();
        this.praiseCount = in.readInt();
        this.commentCount = in.readInt();
        this.commentList = new ArrayList<Comment>();
        in.readList(this.commentList, Comment.class.getClassLoader());
        this.distance = in.readString();
        this.userRole = in.readInt();
        this.isPraise = in.readInt();
        this.shareNumber = in.readInt();
        this.toBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.praiseCountStr = in.readString();
        this.commentCountStr = in.readString();
        this.shareNumberStr = in.readString();
        this.status = in.readInt();
        this.isOriginal = in.readInt();
        this.parentId = in.readLong();
        this.followCount = in.readInt();
        this.followList = in.createTypedArrayList(SayLoveInfo.CREATOR);
        this.originalInfo = in.readParcelable(OriginalInfo.class.getClassLoader());
        this.organList = in.createTypedArrayList(OrganBaseInfo.CREATOR);
        this.labelName = in.readString();
        this.labelId = in.readLong();
        this.scondlabelList = in.createStringArrayList();
        this.ranking = in.readInt();
    }

    public static final Creator<SayLoveInfo> CREATOR = new Creator<SayLoveInfo>() {
        @Override
        public SayLoveInfo createFromParcel(Parcel source) {
            return new SayLoveInfo(source);
        }

        @Override
        public SayLoveInfo[] newArray(int size) {
            return new SayLoveInfo[size];
        }
    };
}
