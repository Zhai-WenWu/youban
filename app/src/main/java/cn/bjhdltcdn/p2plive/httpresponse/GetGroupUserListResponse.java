package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.GroupUser;

/**
 * Created by xiawenquan on 17/12/2.
 */

public class GetGroupUserListResponse extends BaseResponse {
    private List<GroupUser> groupUserList;//[{GroupUser对象},{GroupUser对象},...]
    private GroupUser groupUser;
    private Group group;
    private int total;

    public List<GroupUser> getGroupUserList() {
        return groupUserList;
    }

    public void setGroupUserList(List<GroupUser> groupUserList) {
        this.groupUserList = groupUserList;
    }

    public GroupUser getGroupUser() {
        return groupUser;
    }

    public void setGroupUser(GroupUser groupUser) {
        this.groupUser = groupUser;
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
