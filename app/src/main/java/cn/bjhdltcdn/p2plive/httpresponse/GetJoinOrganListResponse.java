package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public class GetJoinOrganListResponse extends BaseResponse{
    private List<OrganizationInfo> list;//[{OrganizationInfo对象},{OrganizationInfo对象}...],
    private int total;//总条数,

    public List<OrganizationInfo> getList() {
        return list;
    }

    public void setList(List<OrganizationInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
