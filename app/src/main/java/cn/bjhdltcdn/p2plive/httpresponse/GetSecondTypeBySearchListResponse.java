package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * Created by xiawenquan on 17/12/15.
 */

public class GetSecondTypeBySearchListResponse extends BaseResponse {


    private List<HobbyInfo> hobbyList;

    private List<OrganizationInfo> organList;


    public List<HobbyInfo> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<HobbyInfo> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public List<OrganizationInfo> getOrganList() {
        return organList;
    }

    public void setOrganList(List<OrganizationInfo> organList) {
        this.organList = organList;
    }
}
