package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.Group;

/**
 * Created by ZHUDI on 2017/11/29.
 */

public class GetGroupListResponse extends BaseResponse {
    private List<Group> groupList;//[{Group对象},{Group对象},...]群组列表,

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
