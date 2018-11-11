package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hu_PC on 2017/11/9.
 * 帖子
 */

public class PostInfo implements Parcelable {
    private BaseUser baseUser;//{BaseUser}发帖用户基本信息对象,
    private long postId;//帖子Id,
    private String content;//帖子内容,
    private List<Image> imageList;//[{Image对象},{Image对象}...]图片列表,
    private String hobbyName;//兴趣爱好名称,
    private long organId;//圈子Id,
    private String organName;//圈子名称,
    private String addTime;//发帖时间,
    private int praiseCount;//点赞总数,
    private int commentCount;//评论回复总数,
    private int userRole;//用户角色(0-->普通成员,1-->管理员,2-->圈主),
    private int topicType;//帖子类型(1-->普通图文，2--->视频),
    private String videoUrl;//帖子视频路径,
    private String videoImageUrl;//帖子图片地址,
    private String distance;//离我距离,
    private String activeLevel;//发帖用户在圈子的活跃等级(字符串),
    private int isPraise;//当前用户是否点赞该帖子(0未点赞 1 点赞),
    private int isTop;//是否置顶(0取消,1置顶),
    private String secondHobbyName;//圈子二级类别名称(兴趣爱好),
    private int isAnonymous;//是否匿名发布帖子(1--->匿名，2--->不匿名)
    private int isAttention;//关注状态(0-->未关注,1-->已关注),
    private int anonymousLimit;//是否可以匿名发布评论 1 匿名，2 实名
    private int shareNumber;//分享次数,
    private BaseUser toBaseUser;//分享关注使用,
    private String praiseCountStr;// 转译点赞总数(字符串)
    private String commentCountStr;//转译评论回复总数(字符串),
    private String shareNumberStr;//转译分享次数(字符串),
    private String labelName;//标签名称,
    private long labelId;//标签id
    private int status;//帖子状态(0--->未删除，1---->删除),
    private int isOriginal;//是否原贴(0--->原帖，1--->跟帖),
    private long parentId;//原贴Id,
    private int followCount;//跟拍视频总数,
    private List<PostInfo> followList;//[{PostInfo对象},{PostInfo对象}...]跟拍帖子列表,
    private OriginalInfo originalInfo;//{OriginalInfo对象}原贴信息,
    private List<String> scondlabelList;// 二级标签列表
    private List<OrganBaseInfo> organList;// 帖子关联的圈子列表,
    private int ranking;// 排名,
    private int contentLimit; //圈子内容限制(1-->全部可见,2-->仅圈友可见),
    private StoreDetail storeDetail;//{StoreInfo对象}店铺信息,
    private int isClert;//当前是否是店员(1店主,2店员,3普通用户,4店员申请中),
    private List<PostLabelInfo> postLabelList;//帖子标签列表
    private int isRecurit;//是否为招聘信息(0 不是,1 是),

    public int getContentLimit() {
        return contentLimit;
    }

    public void setContentLimit(int contentLimit) {
        this.contentLimit = contentLimit;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
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

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public int getTopicType() {
        return topicType;
    }

    public void setTopicType(int topicType) {
        this.topicType = topicType;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getActiveLevel() {
        return activeLevel;
    }

    public void setActiveLevel(String activeLevel) {
        this.activeLevel = activeLevel;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getSecondHobbyName() {
        return secondHobbyName;
    }

    public void setSecondHobbyName(String secondHobbyName) {
        this.secondHobbyName = secondHobbyName;
    }

    public int getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public int getAnonymousLimit() {
        return anonymousLimit;
    }

    public void setAnonymousLimit(int anonymousLimit) {
        this.anonymousLimit = anonymousLimit;
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

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
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

    public List<PostInfo> getFollowList() {
        return followList;
    }

    public void setFollowList(List<PostInfo> followList) {
        this.followList = followList;
    }

    public OriginalInfo getOriginalInfo() {
        return originalInfo;
    }

    public void setOriginalInfo(OriginalInfo originalInfo) {
        this.originalInfo = originalInfo;
    }

    public List<String> getScondlabelList() {
        return scondlabelList;
    }

    public void setScondlabelList(List<String> scondlabelList) {
        this.scondlabelList = scondlabelList;
    }

    public List<OrganBaseInfo> getOrganList() {
        return organList;
    }

    public void setOrganList(List<OrganBaseInfo> organList) {
        this.organList = organList;
    }

    public long getLabelId() {
        return labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    public StoreDetail getStoreDetail() {
        return storeDetail;
    }

    public void setStoreDetail(StoreDetail storeDetail) {
        this.storeDetail = storeDetail;
    }

    public List<PostLabelInfo> getPostLabelList() {
        return postLabelList;
    }

    public void setPostLabelList(List<PostLabelInfo> postLabelList) {
        this.postLabelList = postLabelList;
    }

    public int getIsClert() {
        return isClert;
    }

    public void setIsClert(int isClert) {
        this.isClert = isClert;
    }

    public int getIsRecurit() {
        return isRecurit;
    }

    public void setIsRecurit(int isRecurit) {
        this.isRecurit = isRecurit;
    }

    public PostInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseUser, flags);
        dest.writeLong(this.postId);
        dest.writeString(this.content);
        dest.writeTypedList(this.imageList);
        dest.writeString(this.hobbyName);
        dest.writeLong(this.organId);
        dest.writeString(this.organName);
        dest.writeString(this.addTime);
        dest.writeInt(this.praiseCount);
        dest.writeInt(this.commentCount);
        dest.writeInt(this.userRole);
        dest.writeInt(this.topicType);
        dest.writeString(this.videoUrl);
        dest.writeString(this.videoImageUrl);
        dest.writeString(this.distance);
        dest.writeString(this.activeLevel);
        dest.writeInt(this.isPraise);
        dest.writeInt(this.isTop);
        dest.writeString(this.secondHobbyName);
        dest.writeInt(this.isAnonymous);
        dest.writeInt(this.isAttention);
        dest.writeInt(this.anonymousLimit);
        dest.writeInt(this.shareNumber);
        dest.writeParcelable(this.toBaseUser, flags);
        dest.writeString(this.praiseCountStr);
        dest.writeString(this.commentCountStr);
        dest.writeString(this.shareNumberStr);
        dest.writeString(this.labelName);
        dest.writeLong(this.labelId);
        dest.writeInt(this.status);
        dest.writeInt(this.isOriginal);
        dest.writeLong(this.parentId);
        dest.writeInt(this.followCount);
        dest.writeTypedList(this.followList);
        dest.writeParcelable(this.originalInfo, flags);
        dest.writeStringList(this.scondlabelList);
        dest.writeTypedList(this.organList);
        dest.writeInt(this.ranking);
        dest.writeInt(this.contentLimit);
        dest.writeParcelable(this.storeDetail, flags);
        dest.writeInt(this.isClert);
        dest.writeTypedList(this.postLabelList);
        dest.writeInt(this.isRecurit);
    }

    protected PostInfo(Parcel in) {
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.postId = in.readLong();
        this.content = in.readString();
        this.imageList = in.createTypedArrayList(Image.CREATOR);
        this.hobbyName = in.readString();
        this.organId = in.readLong();
        this.organName = in.readString();
        this.addTime = in.readString();
        this.praiseCount = in.readInt();
        this.commentCount = in.readInt();
        this.userRole = in.readInt();
        this.topicType = in.readInt();
        this.videoUrl = in.readString();
        this.videoImageUrl = in.readString();
        this.distance = in.readString();
        this.activeLevel = in.readString();
        this.isPraise = in.readInt();
        this.isTop = in.readInt();
        this.secondHobbyName = in.readString();
        this.isAnonymous = in.readInt();
        this.isAttention = in.readInt();
        this.anonymousLimit = in.readInt();
        this.shareNumber = in.readInt();
        this.toBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.praiseCountStr = in.readString();
        this.commentCountStr = in.readString();
        this.shareNumberStr = in.readString();
        this.labelName = in.readString();
        this.labelId = in.readLong();
        this.status = in.readInt();
        this.isOriginal = in.readInt();
        this.parentId = in.readLong();
        this.followCount = in.readInt();
        this.followList = in.createTypedArrayList(PostInfo.CREATOR);
        this.originalInfo = in.readParcelable(OriginalInfo.class.getClassLoader());
        this.scondlabelList = in.createStringArrayList();
        this.organList = in.createTypedArrayList(OrganBaseInfo.CREATOR);
        this.ranking = in.readInt();
        this.contentLimit = in.readInt();
        this.storeDetail = in.readParcelable(StoreDetail.class.getClassLoader());
        this.isClert = in.readInt();
        this.postLabelList = in.createTypedArrayList(PostLabelInfo.CREATOR);
        this.isRecurit = in.readInt();
    }

    public static final Creator<PostInfo> CREATOR = new Creator<PostInfo>() {
        @Override
        public PostInfo createFromParcel(Parcel source) {
            return new PostInfo(source);
        }

        @Override
        public PostInfo[] newArray(int size) {
            return new PostInfo[size];
        }
    };
}
