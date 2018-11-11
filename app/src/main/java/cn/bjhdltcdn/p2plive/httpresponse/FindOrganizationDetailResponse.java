package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * Created by xiawenquan on 17/11/20.
 */

public class FindOrganizationDetailResponse extends BaseResponse {

    private OrganizationInfo organizationInfo;

    public OrganizationInfo getOrganizationInfo() {
        return organizationInfo;
    }

    public void setOrganizationInfo(OrganizationInfo organizationInfo) {
        this.organizationInfo = organizationInfo;
    }
}
