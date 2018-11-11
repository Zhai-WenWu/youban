package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.Group;

/**
 * Created by xiawenquan on 17/11/30.
 */

public class CreateGroupResponse extends BaseResponse {
    private Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
