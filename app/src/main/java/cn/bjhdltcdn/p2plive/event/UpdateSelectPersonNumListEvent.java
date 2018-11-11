package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.OrganMember;

/**
 * Created by ZHUDI on 2018/1/27.
 */

public class UpdateSelectPersonNumListEvent {

    private int num;

    public UpdateSelectPersonNumListEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
