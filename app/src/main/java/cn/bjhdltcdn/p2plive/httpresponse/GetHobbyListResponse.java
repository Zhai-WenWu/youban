package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HobbyInfo;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public class GetHobbyListResponse  extends BaseResponse{
    private List<HobbyInfo> hobbyList;//[{HobbyInfo}]兴趣爱好,

    public List<HobbyInfo> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<HobbyInfo> hobbyList) {
        this.hobbyList = hobbyList;
    }
}
