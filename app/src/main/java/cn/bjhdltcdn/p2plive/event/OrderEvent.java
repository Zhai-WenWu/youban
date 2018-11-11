package cn.bjhdltcdn.p2plive.event;

/**
 * Created by zhaiww on 2018/4/21.
 */

public class OrderEvent {

    public int getPosition() {
        return position;
    }

    public int position;

    public OrderEvent(int position) {
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
