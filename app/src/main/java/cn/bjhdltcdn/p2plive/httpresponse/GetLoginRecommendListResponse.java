package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.LoginRecommendInfo;
import cn.bjhdltcdn.p2plive.model.Props;

/**
 * Created by xiawenquan on 17/12/27.
 */

public class GetLoginRecommendListResponse extends BaseResponse {
    private List<LoginRecommendInfo> list;//[{Props对象},{Props对象}],

    public List<LoginRecommendInfo> getList() {
        return list;
    }

    public void setList(List<LoginRecommendInfo> list) {
        this.list = list;
    }
}
