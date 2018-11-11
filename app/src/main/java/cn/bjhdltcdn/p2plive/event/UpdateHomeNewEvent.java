package cn.bjhdltcdn.p2plive.event;

/**
 * 清除首页小红点事件
 */

public class UpdateHomeNewEvent {

    private int type;//1：关注2：附近

    public UpdateHomeNewEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
