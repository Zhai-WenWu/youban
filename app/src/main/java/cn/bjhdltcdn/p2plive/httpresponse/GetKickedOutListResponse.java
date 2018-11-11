package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OrganMember;

/**
 * Created by ZHUDI on 2018/1/27.
 */

public class GetKickedOutListResponse extends BaseResponse {
    private List<OrganMember> memberList;//[{OrganMember},{OrganMember}...]圈子禁言成员列表,
    private int total;

    public List<OrganMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<OrganMember> memberList) {
        this.memberList = memberList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
