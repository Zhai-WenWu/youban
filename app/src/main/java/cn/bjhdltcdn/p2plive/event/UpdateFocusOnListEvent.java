package cn.bjhdltcdn.p2plive.event;

/**
 * Created by huwenhua on 2016/9/13.
 */
public class UpdateFocusOnListEvent {
    private int type;//0:关注 1:粉丝 2：好友

    public UpdateFocusOnListEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
