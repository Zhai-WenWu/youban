package cn.bjhdltcdn.p2plive.model;

import java.util.List;

/**
 * 帮帮忙评论对象
 */

public class HelpComment {
    private long commentId;//评论/回复id,
    private String content;//评论/回复内容",
    private String addTime;//添加时间,
    private int type;//内容类型(1--->评论,2--->回复),
    private BaseUser toBaseUser;//{BaseUser}被回复人,
    private BaseUser fromBaseUser;//{BaseUser}回复人,
    private int praiseCount;//点赞总数,
    private String praiseCountStr;//转译点赞总数(字符串),
    private int commentType;//评论/回复类型(1-->普通图文，2--->视频),
    private String videoUrl;//评论/回复视频路径(类型为视频使用),
    private String videoImageUrl;//评论/回复图片地址(类型为视频使用),
    private List<HelpComment> replyList;//[{Comment},{Comment}...]回复列表,
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

    public List<HelpComment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<HelpComment> replyList) {
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
