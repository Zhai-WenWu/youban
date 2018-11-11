package cn.bjhdltcdn.p2plive.model;

public class BlackUser {
    private long blackId;//黑名单表主键,
    private long fromUserId;//当前用户ID,
    private long toUserId;//黑名单用户ID
    private BaseUser baseUser;//{BaseUser对象}(对方的基本信息)

    public long getBlackId() {
        return blackId;
    }

    public void setBlackId(long blackId) {
        this.blackId = blackId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }
}
