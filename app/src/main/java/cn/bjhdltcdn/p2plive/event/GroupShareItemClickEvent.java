package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.Group;

/**
 * Created by xiawenquan on 17/12/4.
 */

public class GroupShareItemClickEvent {
    private Group group;

    public GroupShareItemClickEvent(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
