package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * Created by xiawenquan on 17/11/18.
 */

public class GetOrganizationListResponse extends BaseResponse {


    //圈子列表
    private List<OrganizationInfo> organList;
    //圈子列表
    private List<OrganizationInfo> list;

    public List<OrganizationInfo> getList() {
        return list;
    }

    public void setList(List<OrganizationInfo> list) {
        this.list = list;
    }

    //总数
    private int total;

    public List<OrganizationInfo> getOrganList() {
        return organList;
    }

    public void setOrganList(List<OrganizationInfo> organList) {
        this.organList = organList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
