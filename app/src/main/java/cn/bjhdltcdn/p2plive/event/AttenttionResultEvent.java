package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHUDI on 2017/12/2.
 */

public class AttenttionResultEvent {
    private int type;//关注类型(1-->关注;2-->取消关注)

    public AttenttionResultEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
