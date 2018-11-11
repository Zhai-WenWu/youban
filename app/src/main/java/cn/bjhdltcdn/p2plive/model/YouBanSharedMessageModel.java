package cn.bjhdltcdn.p2plive.model;

/**
 * Created by xiawenquan on 18/1/25.
 */

public class YouBanSharedMessageModel {


//    {
//        "nickName": "哈哈",
//            "userIcon": "http://www",
//            "messageType":消息类型（11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白）,
//        "topicType":1,
//            "imageUrl": "http://www/封面图片",
//            "parentId": 123456,
//            "content": "描述"
//    }

    private String nickName;
    private String userIcon;
    private int messageType;
    /**
     * 1 普通文本，2 视频
     */
    private int topicType;
    private String imageUrl;
    private long parentId;
    private String content;

    /**
     * 0 不加密
     * 1 加密
     */
    private int isPassword ;

    private LaunchPlay launchPlay;

    public LaunchPlay getLaunchPlay() {
        return launchPlay;
    }

    public void setLaunchPlay(LaunchPlay launchPlay) {
        this.launchPlay = launchPlay;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getTopicType() {
        return topicType;
    }

    public void setTopicType(int topicType) {
        this.topicType = topicType;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsPassword() {
        return isPassword;
    }

    public void setIsPassword(int isPassword) {
        this.isPassword = isPassword;
    }
}
