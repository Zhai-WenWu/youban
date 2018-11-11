package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.OrganMember;

/**
 * Created by ZHUDI on 2018/1/27.
 */

public class AssociationBlackListEvent {

    private int type;//1禁言的用户 2踢出的用户
    private OrganMember organMember;
    private int position;

    public AssociationBlackListEvent(int type, OrganMember organMember,int position) {
        this.type = type;
        this.organMember = organMember;
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public OrganMember getOrganMember() {
        return organMember;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
