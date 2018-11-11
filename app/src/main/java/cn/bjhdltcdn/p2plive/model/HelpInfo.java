package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


/**
 * Created by ZHAI on 2018/2/27.
 */

public class HelpInfo implements Parcelable {
    /*"baseUser":{BaseUser}发布用户基本信息对象,
    "helpId":主键Id,
    "content":发布内容,
    "imageList":[{Image对象},{Image对象}...]图片列表,
    "organId":圈子Id,
    "organName":圈子名称,
    "addTime":发布时间,
    "shareNumber":分享次数,
    "praiseCount":点赞总数,
    "praiseCountStr":转译点赞总数(字符串),
    "commentCount":评论回复总数,
    "userRole":所属角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
    "topicType":类型(1-->普通图文，2--->视频),
    "videoUrl":视频路径,
    "videoImageUrl":图片地址,
    "activeLevel":发帖用户在圈子的活跃等级(字符串),
    "isPraise":当前用户是否点赞该帖子(0未点赞 1 点赞),*/
    private BaseUser baseUser;
    private long helpId;
    private String content;
    private List<Image> imageList;
    private long organId;
    private String organName;
    private String addTime;
    private int praiseCount;
    private String praiseCountStr;
    private int commentCount;
    private int userRole;
    private int helpType;
    private int shareNumber;
    private String videoUrl;
    private String videoImageUrl;
    private String activeLevel;
    private String distance;
    private String city;
    private int isPraise;
    private String commentCountStr;//转译评论回复总数(字符串),
    private String shareNumberStr;//转译分享次数(字符串),
    private int status;//帮帮忙状态(0--->未删除，1---->删除),
    private int isOriginal;//是否原帮帮忙(0--->原帮帮忙，1--->跟帮帮忙),
    private long parentId;//原帮帮忙Id,
    private int followCount;//跟拍视频总数,
    private List<HelpInfo> followList;//[{HelpInfo对象},{HelpInfo对象}...]跟拍帮帮忙列表,
    private OriginalInfo originalInfo;//{OriginalInfo对象}原贴信息,
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public long getHelpId() {
        return helpId;
    }

    public void setHelpId(long helpId) {
        this.helpId = helpId;
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

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
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

    public int getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(int shareNumber) {
        this.shareNumber = shareNumber;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getPraiseCountStr() {
        return praiseCountStr;
    }

    public void setPraiseCountStr(String praiseCountStr) {
        this.praiseCountStr = praiseCountStr;
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

    public int getHelpType() {
        return helpType;
    }

    public void setHelpType(int helpType) {
        this.helpType = helpType;
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

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
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

    public List<HelpInfo> getFollowList() {
        return followList;
    }

    public void setFollowList(List<HelpInfo> followList) {
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

    public HelpInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseUser, flags);
        dest.writeLong(this.helpId);
        dest.writeString(this.content);
        dest.writeTypedList(this.imageList);
        dest.writeLong(this.organId);
        dest.writeString(this.organName);
        dest.writeString(this.addTime);
        dest.writeInt(this.praiseCount);
        dest.writeString(this.praiseCountStr);
        dest.writeInt(this.commentCount);
        dest.writeInt(this.userRole);
        dest.writeInt(this.helpType);
        dest.writeInt(this.shareNumber);
        dest.writeString(this.videoUrl);
        dest.writeString(this.videoImageUrl);
        dest.writeString(this.activeLevel);
        dest.writeString(this.distance);
        dest.writeString(this.city);
        dest.writeInt(this.isPraise);
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

    protected HelpInfo(Parcel in) {
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.helpId = in.readLong();
        this.content = in.readString();
        this.imageList = in.createTypedArrayList(Image.CREATOR);
        this.organId = in.readLong();
        this.organName = in.readString();
        this.addTime = in.readString();
        this.praiseCount = in.readInt();
        this.praiseCountStr = in.readString();
        this.commentCount = in.readInt();
        this.userRole = in.readInt();
        this.helpType = in.readInt();
        this.shareNumber = in.readInt();
        this.videoUrl = in.readString();
        this.videoImageUrl = in.readString();
        this.activeLevel = in.readString();
        this.distance = in.readString();
        this.city = in.readString();
        this.isPraise = in.readInt();
        this.commentCountStr = in.readString();
        this.shareNumberStr = in.readString();
        this.status = in.readInt();
        this.isOriginal = in.readInt();
        this.parentId = in.readLong();
        this.followCount = in.readInt();
        this.followList = in.createTypedArrayList(HelpInfo.CREATOR);
        this.originalInfo = in.readParcelable(OriginalInfo.class.getClassLoader());
        this.organList = in.createTypedArrayList(OrganBaseInfo.CREATOR);
        this.labelName = in.readString();
        this.labelId = in.readLong();
        this.scondlabelList = in.createStringArrayList();
        this.ranking = in.readInt();
    }

    public static final Creator<HelpInfo> CREATOR = new Creator<HelpInfo>() {
        @Override
        public HelpInfo createFromParcel(Parcel source) {
            return new HelpInfo(source);
        }

        @Override
        public HelpInfo[] newArray(int size) {
            return new HelpInfo[size];
        }
    };
}
