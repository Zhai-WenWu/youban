package cn.bjhdltcdn.p2plive.model;

/**
 * Created by xiawenquan on 17/12/6.
 */

public class YouBanOrganizationMessageModel {
    private OrganApply organApply;
    private String messageTips;
    /**
     * 10000：申请
     * 10002：拒绝
     * 10003：同意
     * 10005：评论
     * 10006回复
     */
    private int messageType;

    private String addTime;

    //////////////圈子评论和回复///////////////////////

    /*
      "baseUser":{}
     "anonymousType":是否匿名（0-实名，1-匿名）
     "postInfoId":帖子Id,
     "postInfoContent":"帖子内容",
     "messageTips ": "评论/回复内容",
     "addTime":评论/回复时间,
     "floor":楼层,
     "organName":圈子名称,
     "addTime":评论/回复时间,
     "messageType": (10005：评论；10006回复)

     * @return
     */

    private BaseUser baseUser;
    private int anonymousType;
    private long postInfoId;
    private String postInfoContent;
    private String organName;
    private long commentId;//评论/回复id,
    private long commentParentId;//评论/回复id,
    private int commentType;//评论/回复类型(1-->普通图文，2--->视频),
    private String videoUrl;//评论/回复视频路径(类型为视频使用),
    private String videoImageUrl;//评论/回复图片地址(类型为视频使用),
    private Long messageId;


    public OrganApply getOrganApply() {
        return organApply;
    }

    public void setOrganApply(OrganApply organApply) {
        this.organApply = organApply;
    }

    /**
     * 10000：申请
     * 10002：拒绝
     * 10003：同意
     * 10005：评论
     * 10006回复
     */
    public String getMessageTips() {
        return messageTips;
    }

    public void setMessageTips(String messageTips) {
        this.messageTips = messageTips;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }


    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    /**
     * 是否匿名（0-实名，1-匿名）
     *
     * @return
     */
    public int getAnonymousType() {
        return anonymousType;
    }

    public void setAnonymousType(int anonymousType) {
        this.anonymousType = anonymousType;
    }

    public long getPostInfoId() {
        return postInfoId;
    }

    public void setPostInfoId(long postInfoId) {
        this.postInfoId = postInfoId;
    }

    public String getPostInfoContent() {
        return postInfoContent;
    }

    public void setPostInfoContent(String postInfoContent) {
        this.postInfoContent = postInfoContent;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public long getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(long commentParentId) {
        this.commentParentId = commentParentId;
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
}
