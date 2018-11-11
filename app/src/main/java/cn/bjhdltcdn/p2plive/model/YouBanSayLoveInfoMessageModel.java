package cn.bjhdltcdn.p2plive.model;

/**
 * Created by xiawenquan on 17/12/6.
 */

public class YouBanSayLoveInfoMessageModel {
    /*
    {
    "baseUser": {
        "userId": 12344556,
        "nickName": "哈哈",
        "userIcon": "http://www"
    },
		"anonymousType":是否匿名（0-实名，1-匿名）
		"sayLoveId":主键Id,
		"sayLoveName":"校园表白墙",
   "messageTips ": "评论/回复内容",
	"addTime":评论/回复时间,
   "floor":楼层,
	   "messageType": (20001：评论；20002回复)
}

     */

    private BaseUser baseUser;
    private long sayLoveId;
    private String sayLoveName;
    private String messageTips;
    private String addTime;
    private int messageType;
    /**
     * 是否匿名 0不匿名 1匿名
     */
    private int anonymousType;
    private long commentId;//评论/回复id,
    private long commentParentId;//评论/回复id,
    private Long messageId;
    private int commentType;//评论/回复类型(1-->普通图文，2--->视频),
    private String videoUrl;//评论/回复视频路径(类型为视频使用),
    private String videoImageUrl;//评论/回复图片地址(类型为视频使用),

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

    public String getSayLoveName() {
        return sayLoveName;
    }

    public void setSayLoveName(String sayLoveName) {
        this.sayLoveName = sayLoveName;
    }

    public String getMessageTips() {
        return messageTips;
    }

    public void setMessageTips(String messageTips) {
        this.messageTips = messageTips;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
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

    public int getAnonymousType() {
        return anonymousType;
    }

    public void setAnonymousType(int anonymousType) {
        this.anonymousType = anonymousType;
    }
}
