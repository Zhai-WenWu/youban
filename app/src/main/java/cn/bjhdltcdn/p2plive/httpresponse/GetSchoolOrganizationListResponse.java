package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * Created by xiawenquan on 18/3/6.
 */

public class GetSchoolOrganizationListResponse extends BaseResponse {

    private List<OrganizationInfo> organList;
    private int total;

    private String blankHint;

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

    @Override
    public String getBlankHint() {
        return blankHint;
    }

    @Override
    public void setBlankHint(String blankHint) {
        this.blankHint = blankHint;
    }
}
