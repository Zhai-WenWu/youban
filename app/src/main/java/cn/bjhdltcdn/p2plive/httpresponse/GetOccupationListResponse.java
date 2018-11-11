package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OccupationInfo;

/**
 * Created by ZHUDI on 2017/11/21.
 */

public class GetOccupationListResponse extends BaseResponse {
    private List<OccupationInfo> list;//[{OccupationInfo},{OccupationInfo},...]职业列表,

    public List<OccupationInfo> getList() {
        return list;
    }

    public void setList(List<OccupationInfo> list) {
        this.list = list;
    }
}
