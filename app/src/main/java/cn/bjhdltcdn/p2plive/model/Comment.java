package cn.bjhdltcdn.p2plive.model;

import java.util.List;

/**
 * Created by Hu_PC on 2017/11/13.
 * 评论对象
 */

public class Comment {
    private long commentId;//评论/回复id,
    private String content;//评论/回复内容",
    private String addTime;//添加时间,
    private int type;//内容类型(1--->评论,2--->回复,3--->匿名评论,4--->匿名回复),
    private BaseUser toBaseUser;//{BaseUser}被回复人,
    private BaseUser fromBaseUser;//{BaseUser}回复人,
    private int anonymousType;//匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
    private String activeLevel;//发帖用户在圈子的活跃等级(字符串),
    private int praiseCount;//点赞总数,
    private String praiseCountStr;//转译点赞总数(字符串),
    private int commentType;//评论/回复类型(1-->普通图文，2--->视频),
    private String imageUrl;//图片地址(类型为图文使用)
    private String videoUrl;//评论/回复视频路径(类型为视频使用),
    private String videoImageUrl;//评论/回复图片地址(类型为视频使用),
    private List<Comment> replyList;//[{Comment},{Comment}...]回复列表,
    private long commentParentId;//楼中楼的父评论id,
    private int replyCount;//回复总条数,
    private int isPraise;//当前用户是否点赞该评论回复(0未点赞 1 点赞),
    private long parentId;//上一条评论/回复Id(评论时0),
    private String replyCountStr;//转译回复总条数(字符串),
    private List<Image> imageList;//评论回复相册,

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getAnonymousType() {
        return anonymousType;
    }

    public void setAnonymousType(int anonymousType) {
        this.anonymousType = anonymousType;
    }

    public String getActiveLevel() {
        return activeLevel;
    }

    public void setActiveLevel(String activeLevel) {
        this.activeLevel = activeLevel;
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

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public List<Comment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Comment> replyList) {
        this.replyList = replyList;
    }

    public long getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(long commentParentId) {
        this.commentParentId = commentParentId;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getReplyCountStr() {
        return replyCountStr;
    }

    public void setReplyCountStr(String replyCountStr) {
        this.replyCountStr = replyCountStr;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
