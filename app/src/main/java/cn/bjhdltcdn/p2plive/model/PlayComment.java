package cn.bjhdltcdn.p2plive.model;

import java.util.List;

import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;

/**
 * Created by ZHAI on 2017/12/23.
 */

public class PlayComment {
    private long commentId;
    private String content;
    private String addTime;
    private int type;
    private BaseUser toBaseUser;
    private int praiseCount;//点赞总数,
    private String praiseCountStr;//转译点赞总数(字符串),
    private int commentType;//评论/回复类型(1-->普通图文，2--->视频),
    private String imageUrl;//图片地址(类型为图文使用)
    private String videoUrl;//评论/回复视频路径(类型为视频使用),
    private String videoImageUrl;//评论/回复图片地址(类型为视频使用),
    private List<Comment> replyList;//[{Comment},{Comment}...]回复列表,
    private long commentParentId;//楼中楼的父评论id,
    private int isPraise;//当前用户是否点赞该评论回复(0未点赞 1 点赞),
    private long parentId;//上一条评论/回复Id(评论时0),
    private String replyCountStr;//转译回复总条数(字符串),
    public long getCommentId() {
        return commentId;
    }

    private int replyCount;//回复总条数,

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

    private BaseUser fromBaseUser;

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
}
