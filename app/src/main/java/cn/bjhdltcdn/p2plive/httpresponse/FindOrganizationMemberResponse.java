package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OrganMember;

/**
 * Created by xiawenquan on 17/11/20.
 */

public class FindOrganizationMemberResponse extends BaseResponse {

    private List<OrganMember> memberList;
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
