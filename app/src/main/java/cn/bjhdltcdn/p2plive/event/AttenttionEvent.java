package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHUDI on 2017/12/2.
 */

public class AttenttionEvent {
    private int type;//关注类型(1-->关注;2-->取消关注)
    private long userId;//被关注/取消关注的用户id
    
    public AttenttionEvent(int type, long userId) {
        this.type = type;
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public long getUserId() {
        return userId;
    }
}
