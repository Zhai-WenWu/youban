package cn.bjhdltcdn.p2plive.model;

/**
 * Description:匿名好友对象
 * Data: 2018/6/6
 *
 * @author zhudi
 */
public class AnonymousUser {
    private long userId;
    private ChatBaseUser chatBaseUser;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ChatBaseUser getChatBaseUser() {
        return chatBaseUser;
    }

    public void setChatBaseUser(ChatBaseUser chatBaseUser) {
        this.chatBaseUser = chatBaseUser;
    }
}
