package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.Wheat;

/**
 * Created by ZHUDI on 2017/1/4.
 */

public class GetOnWheatListResponse extends BaseResponse {
    private List<Wheat> onWheatList;//[{BaseUser对象},{BaseUser对象},...],

    public List<Wheat> getOnWheatList() {
        return onWheatList;
    }

    public void setOnWheatList(List<Wheat> onWheatList) {
        this.onWheatList = onWheatList;
    }
}
