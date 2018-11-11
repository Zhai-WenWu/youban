package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * Created by Hu_PC on 2017/11/9.
 */

public class GetRecommendListResponse extends BaseResponse{
    private List<OrganizationInfo> recommendList;//[{OrganizationInfo圈子对象},{OrganizationInfo圈子对象}...]推荐圈子,

    public List<OrganizationInfo> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<OrganizationInfo> recommendList) {
        this.recommendList = recommendList;
    }
}
