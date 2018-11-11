package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHUDI on 2017/12/2.
 */

public class ShareNumUpdateEvent {
    private int type;//POST = 1, ORGAIN = 2, ACTIVE = 3, VIDEO = 4, PK = 5, SAYLOVE = 6,

    public ShareNumUpdateEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
