package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by huwenhua on 2017/11/16.
 */

public class GetRecommendOrganListResponse extends BaseResponse{
    private List<OrganizationInfo> recommendList;//[{OrganizationInfo圈子对象},{OrganizationInfo圈子对象}...]推荐圈子,
    private int total;//总条数,

    public List<OrganizationInfo> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<OrganizationInfo> recommendList) {
        this.recommendList = recommendList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
