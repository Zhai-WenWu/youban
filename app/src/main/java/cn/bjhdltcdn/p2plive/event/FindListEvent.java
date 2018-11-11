package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHUDI on 2017/12/2.
 */

public class FindListEvent {
    int type;//1：发现列表 2：发起pk第一步界面
    public FindListEvent(int type) {
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
