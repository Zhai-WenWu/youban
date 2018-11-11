package cn.bjhdltcdn.p2plive.event;

/**
 *
 * Data: 2018/6/9
 *
 * @author: huwenhua
 */
public class ForwardStoreDetailEvent {
    private int position;

    public ForwardStoreDetailEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
