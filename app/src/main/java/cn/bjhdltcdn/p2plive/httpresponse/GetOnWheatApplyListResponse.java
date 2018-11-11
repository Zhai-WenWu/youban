package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;

/**
 * Created by ZHUDI on 2017/1/4.
 */

public class GetOnWheatApplyListResponse extends BaseResponse {
    private List<RoomBaseUser> onWheatList;//[{BaseUser对象},{BaseUser对象},...],

    public List<RoomBaseUser> getOnWheatList() {
        return onWheatList;
    }

    public void setOnWheatList(List<RoomBaseUser> onWheatList) {
        this.onWheatList = onWheatList;
    }
}
